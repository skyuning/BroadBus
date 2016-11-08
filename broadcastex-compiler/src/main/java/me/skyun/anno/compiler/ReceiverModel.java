package me.skyun.anno.compiler;

import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Type;

import java.util.List;

/**
 * Created by linyun on 16/11/6.
 */
public class ReceiverModel {
    public String action = "\"\"";
    public String[] categories = {};
    public String methodName;
    public List<Type> paramTypes;
    private List<Symbol.VarSymbol> paramSymbols;
    private String paramList = "";

    public String getAction() {
        return action;
    }

    public String[] getCategories() {
        return categories;
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
        return paramList;
    }

    public static String buildParamList(List<Symbol.VarSymbol> paramSymbols) {
        StringBuilder paramList = new StringBuilder();
        for (Symbol.VarSymbol symbol : paramSymbols) {
            paramList.append(symbol).append(", ");
        }
        paramList.delete(paramList.length() - 2, paramList.length());
        return paramList.toString();
    }
}
