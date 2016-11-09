package me.skyun.anno.compiler;

import com.sun.tools.javac.code.Attribute;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.processing.JavacFiler;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.FilerException;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;
import me.skyun.anno.api.BroadcastExReceiver;
import me.skyun.anno.api.ReceiverRegister;

/**
 * Created by linyun on 16/10/28.
 */
public class BroadcastProcessor extends AbstractProcessor {

    private static final String RECEIVER_REGISTER_TEMPLATE = "ReceiverRegisterTemplate.java";
    private static final String RECEIVER_TEMPLATE = "ReceiverTemplate.java";
    private JavacFiler mFiler;
    private Messager mMessager;
    private Configuration mConfig;
    private Elements mElementUtils;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mElementUtils = processingEnvironment.getElementUtils();
        mFiler = (JavacFiler) processingEnvironment.getFiler();
        mMessager = processingEnvironment.getMessager();
        mConfig = AptUtils.initConfiguration();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new HashSet<String>();
        types.add(BroadcastExReceiver.class.getCanonicalName());
        return types;
    }

    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment env) {
        Map<Symbol.ClassSymbol, List<Symbol.MethodSymbol>> methodSymbolsByClass =
                new HashMap<Symbol.ClassSymbol, List<Symbol.MethodSymbol>>();
        for (TypeElement anno : annotations) {
            for (Element element : env.getElementsAnnotatedWith(anno)) {
                Symbol.ClassSymbol classSymbol = AptUtils.getEnclosingClass(element);
                List<Symbol.MethodSymbol> methodSymbols = methodSymbolsByClass.get(classSymbol);
                if (methodSymbols == null) {
                    methodSymbols = new ArrayList<Symbol.MethodSymbol>();
                }
                methodSymbols.add((Symbol.MethodSymbol) element);
                methodSymbolsByClass.put(classSymbol, methodSymbols);
            }
        }
        for (Symbol.ClassSymbol classSymbol : methodSymbolsByClass.keySet()) {
            try {
                processReceiverRegister(classSymbol, methodSymbolsByClass.get(classSymbol));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    private void processReceiverRegister(Symbol.ClassSymbol classSymbol, List<Symbol.MethodSymbol> methodSymbolList)
            throws IOException {
        mMessager.printMessage(Diagnostic.Kind.NOTE,
                "Start processing receiver register for: " + classSymbol.getQualifiedName());
        Writer writer = genReceiverRegister(classSymbol);
        for (Symbol.MethodSymbol methodSymbol : methodSymbolList) {
            processReceiver(methodSymbol, writer);
        }
        writer.write("    }\n}");
        writer.flush();
        writer.close();
        mMessager.printMessage(Diagnostic.Kind.NOTE,
                "Processing receiver register succeed for: " + classSymbol.getQualifiedName());
    }

    private Writer genReceiverRegister(Symbol.ClassSymbol classSymbol) {
        String packageName = mElementUtils.getPackageOf(classSymbol).getQualifiedName().toString();
        String simpleName = classSymbol.getSimpleName() + ReceiverRegister.GEN_FILE_POSTFIX;
        String fullName = packageName + "." + simpleName;
        mMessager.printMessage(Diagnostic.Kind.NOTE, "Start rendering receiver register: " + fullName);

        try {
            Writer writer = mFiler.createSourceFile(fullName).openWriter();
            Template template = mConfig.getTemplate(RECEIVER_REGISTER_TEMPLATE);
            FileModel fileModel = new FileModel(packageName, simpleName, classSymbol.getSimpleName().toString());
            template.process(fileModel, writer);
            mMessager.printMessage(Diagnostic.Kind.NOTE, "Render receiver register succeed: " + fullName);
            return writer;
        } catch (FilerException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        } catch (MalformedTemplateNameException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (TemplateNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("Render Receiver Register failed for: " + fullName);
    }

    private void processReceiver(Symbol.MethodSymbol methodSymbol, Writer writer) throws IOException {
        mMessager.printMessage(Diagnostic.Kind.NOTE,
                "Start processing receiver " + methodSymbol.getSimpleName());

        ReceiverModel model = new ReceiverModel();
        model.methodName = methodSymbol.getSimpleName().toString();
        if (methodSymbol.getParameters().size() > 0) {
            Type.MethodType methodType = (Type.MethodType) methodSymbol.asType();
            model.setParams(methodType.getParameterTypes(), methodSymbol.getParameters());
        }
        Attribute.Compound annoMirror = AptUtils.getAnnotatioMirror(methodSymbol, BroadcastExReceiver.class);
        if (annoMirror != null) {
            Map<Symbol.MethodSymbol, Attribute> elementValues = annoMirror.getElementValues();
            for (Symbol.MethodSymbol annoMethod : elementValues.keySet()) {
                if (BroadcastExReceiver.ACTION_TYPE.equals(annoMethod.getSimpleName().toString())) {
                    Attribute.Class actionTypeValue = (Attribute.Class) elementValues.get(annoMethod);
                    model.action = actionTypeValue.getValue().toString();
                } else if (BroadcastExReceiver.CATEGORY_TYPES.equals(annoMethod.getSimpleName().toString())) {
                    List<Attribute> categoryTypesValue = (List<Attribute>) elementValues.get(annoMethod).getValue();
                    model.addCategoryTypes(categoryTypesValue);
                }
            }
        }
        BroadcastExReceiver anno = methodSymbol.getAnnotation(BroadcastExReceiver.class);
        model.addCategories(anno.categories());
        String action = anno.action();
        if (action != null && action.length() > 0) {
            model.action = anno.action();
        }

        try {
            Template template = mConfig.getTemplate(RECEIVER_TEMPLATE);
            template.process(model, writer);
        } catch (IOException e) {
            e.printStackTrace();
            mMessager.printMessage(Diagnostic.Kind.NOTE, "Processing receiver failed" + model.methodName);
        } catch (TemplateException e) {
            e.printStackTrace();
        }
        mMessager.printMessage(Diagnostic.Kind.NOTE, "Processing receiver succeed: " + model.methodName);
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}