package me.skyun.anno.compiler;

import com.sun.tools.javac.code.Attribute;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Type;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Created by linyun on 16/11/6.
 */
public class ReceiverModel {
    public String action = "";
    public String methodName;
    public List<Type> paramTypes;
    private List<Symbol.VarSymbol> paramSymbols;
    private StringBuilder paramList = new StringBuilder();
    public List<String> categories = new ArrayList<String>();

    public String getAction() {
        return action;
    }

    public void addCategories(String[] categories) {
        Collections.addAll(this.categories, categories);
    }

    public void addCategoryTypes(List<Attribute> categoryTypes) {
        for (Attribute c : categoryTypes) {
            categories.add(String.format(Locale.getDefault(), "\"%s\", ", c.getValue().toString()));
        }
    }

    public String getCategories() {
        if (categories.isEmpty()) {
            return "";
        }
        StringBuilder builder = new StringBuilder(categories.remove(0));
        for (String c : categories) {
            builder.append(", ").append(c);
        }
        return builder.toString();
    }

    public String getMethodName() {
        return methodName;
    }

    public List<Type> getParamTypes() {
        return paramTypes;
    }

    public List<Symbol.VarSymbol> getParamSymbols() {
        return paramSymbols;
    }

    public void setParamSymbols(List<Symbol.VarSymbol> paramSymbols) {
        this.paramSymbols = paramSymbols;
        this.paramList = buildParamList(paramSymbols);
    }

    public String getParamList() {
        return paramList.toString();
    }

    public static StringBuilder buildParamList(List<Symbol.VarSymbol> paramSymbols) {
        StringBuilder paramList = new StringBuilder();
        for (Symbol.VarSymbol symbol : paramSymbols) {
            paramList.append(symbol).append(", ");
        }
        if (paramList.length() > 2) {
            paramList.delete(paramList.length() - 2, paramList.length());
        }
        return paramList;
    }
}
