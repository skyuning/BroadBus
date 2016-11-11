package me.skyun.anno.compiler;

import com.sun.tools.javac.code.Attribute;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.util.Assert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by linyun on 16/11/6.
 */
public class ReceiverModel {

    public String action = "";
    public List<String> categories = new ArrayList<>();

    public String methodName;
    private List<String> paramTypes = new ArrayList<>();

    public String getAction() {
        return action;
    }

    public void addCategories(String[] categories) {
        Collections.addAll(this.categories, categories);
    }

    public void addCategoryTypes(List<Attribute> categoryTypes) {
        for (Attribute item : categoryTypes) {
            categories.add(item.getValue().toString());
        }
    }

    public List<String> getCategories() {
        return categories;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setParamTypes(List<Type> types) {
        for (Type type : types) {
            paramTypes.add(type.toString());
        }
    }

    public List<String> getParamTypes() {
        return paramTypes;
    }
}
