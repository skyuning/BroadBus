package me.skyun.anno.compiler;

import com.sun.tools.javac.code.Attribute;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.util.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by linyun on 16/11/6.
 */
public class ReceiverModel {

    public String methodName;
    private Map<String, String> localVars = new HashMap<String, String>();
    private List<String> paramNames = new ArrayList<String>(); // 因为有参数列表有顺序，所以还要保存一份List
    private String paramLine = "";

    public String action = "";
    public List<String> categories = new ArrayList<String>();

    public String getAction() {
        return action;
    }

    public void addCategories(String[] categories) {
        for (String c : categories) {
            this.categories.add(String.format(Locale.getDefault(), "\"%s\"", c));
        }
    }

    public void addCategoryTypes(List<Attribute> categoryTypes) {
        for (Attribute c : categoryTypes) {
            categories.add(String.format(Locale.getDefault(), "\"%s\"", c.getValue().toString()));
        }
    }

    public String getCategories() {
        if (categories.isEmpty()) {
            return "";
        }
        StringBuilder builder = new StringBuilder(categories.get(0));
        for (int i = 1; i < categories.size(); i++) {
            builder.append(", ").append(categories.get(i));
        }
        return builder.toString();
    }

    public String getMethodName() {
        return methodName;
    }

    public void setParams(List<Type> type, List<Symbol.VarSymbol> nameSymbols) {
        Assert.check(type.size() == nameSymbols.size());
        if (type.size() == 0) {
            return;
        }
        for (int i = 0; i < type.size(); i++) {
            String typeStr = type.get(i).toString();
            paramNames.add(nameSymbols.get(i).toString());
            if (!typeStr.startsWith("android.content")) {
                localVars.put(typeStr, nameSymbols.get(i).toString());
            }
        }
    }

    public Map<String, String> getLocalVars() {
        return localVars;
    }

    public String getParamLine() {
        if (paramNames.isEmpty()) {
            return "";
        }
        StringBuilder builder = new StringBuilder(paramNames.get(0));
        for (int i = 1; i < paramNames.size(); i++) {
            builder.append(", ").append(paramNames.get(i));
        }
        paramLine = builder.toString();
        return paramLine;
    }
}
