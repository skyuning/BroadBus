package me.skyun.anno.compiler;

import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.processing.JavacFiler;

import java.io.File;
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

import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;
import me.skyun.anno.api.BroadcastExReceiver;

/**
 * Created by linyun on 16/10/28.
 */
public class BroadcastProcessor extends AbstractProcessor {

    private static final String GEN_FILE_POSTFIX = "$$ReceiverRegister";
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
        try {
            mConfig.setTemplateLoader(
                    new MultiTemplateLoader(
                            new TemplateLoader[]{
                                    new FileTemplateLoader(new File("broadcastex-compiler/src/main/resources/templates")),
                                    new FileTemplateLoader(new File("app/build/intermediates/classes/debug"))
                            }
                    )
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new HashSet<String>();
        types.add(BroadcastExReceiver.class.getCanonicalName());
        return types;
    }

    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment env) {
        Map<Symbol.ClassSymbol, List<Element>> annotatedElementByClass =
                new HashMap<Symbol.ClassSymbol, List<Element>>();
        for (TypeElement anno : annotations) {
            for (Element element : env.getElementsAnnotatedWith(anno)) {
                Symbol.ClassSymbol classSymbol = AptUtils.getEnclosingClass(element);
                List<Element> annotatedElements = annotatedElementByClass.get(classSymbol);
                if (annotatedElements == null) {
                    annotatedElements = new ArrayList<Element>();
                }
                annotatedElements.add(element);
                annotatedElementByClass.put(classSymbol, annotatedElements);
            }
        }
        for (Symbol.ClassSymbol classSymbol : annotatedElementByClass.keySet()) {
            try {
                processReceiverRegister(classSymbol, annotatedElementByClass.get(classSymbol));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    private void processReceiverRegister(Symbol.ClassSymbol classSymbol, List<Element> annatatedElementList) throws IOException {
        mMessager.printMessage(Diagnostic.Kind.NOTE,
                "Start processing receiver register for: " + classSymbol.getQualifiedName());
        Writer writer = genReceiverRegister(classSymbol);
        for (Element annotationElement : annatatedElementList) {
            processReceiver(annotationElement, writer);
        }
        writer.write("    }\n}");
        writer.flush();
        writer.close();
        mMessager.printMessage(Diagnostic.Kind.NOTE,
                "Processing receiver register succeed for: " + classSymbol.getQualifiedName());
    }

    private Writer genReceiverRegister(Symbol.ClassSymbol classSymbol) {
        String packageName = mElementUtils.getPackageOf(classSymbol).getQualifiedName().toString();
        String simpleName = classSymbol.getSimpleName() + GEN_FILE_POSTFIX;
        String fullName = packageName + "." + simpleName;
        mMessager.printMessage(Diagnostic.Kind.NOTE, "Start rendering receiver register: " + fullName);

        try {
            Writer writer = mFiler.createSourceFile(fullName).openWriter();
            Template template = mConfig.getTemplate(RECEIVER_REGISTER_TEMPLATE);
            FileModel fileModel = new FileModel(packageName, simpleName);
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

    private void processReceiver(Element annotatedElement, Writer writer) throws IOException {
        mMessager.printMessage(Diagnostic.Kind.NOTE,
                "Start processing receiver " + annotatedElement.getSimpleName());
        try {
            ReceiverModel model = new ReceiverModel();
            model.methodName = annotatedElement.getSimpleName().toString();
            writer.write("        // register receiver for " + model.methodName + "\n");
        } catch (IOException e) {
            e.printStackTrace();
            mMessager.printMessage(Diagnostic.Kind.NOTE,
                    "Processing receiver failed" + annotatedElement.getSimpleName());
        }
        mMessager.printMessage(Diagnostic.Kind.NOTE,
                "Processing receiver succeed: " + annotatedElement.getSimpleName());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}