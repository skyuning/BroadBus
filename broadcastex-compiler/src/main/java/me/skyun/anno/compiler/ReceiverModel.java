package me.skyun.anno.compiler;

import com.sun.tools.javac.code.Attribute;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Type;

import java.util.Collection;
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
    public StringBuilder categories = new StringBuilder();

    public String getAction() {
        return action;
    }

    public void setCategoryTypes(List<Attribute.Class> categoryTypes) {
        categories.delete(0, categories.length());
        for (Attribute.Class c : categoryTypes) {
            categories.append(String.format(Locale.getDefault(), "\"%s\", ", c.getValue().toString()));
        }
        if (categories.length() > 2) {
            categories.delete(categories.length() - 2, categories.length());
        }
    }

    public String getCategories() {
        return categories.toString();
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
