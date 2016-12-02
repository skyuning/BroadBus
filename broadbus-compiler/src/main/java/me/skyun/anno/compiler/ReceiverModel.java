package me.skyun.anno.compiler;

import com.sun.tools.javac.code.Attribute;
import com.sun.tools.javac.code.Type;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by linyun on 16/11/6.
 */
public class ReceiverModel {

    public List<String> actions = new ArrayList<>();
    public List<String> categories = new ArrayList<>();

    public String methodName;
    private List<String> paramTypes = new ArrayList<>();

    public boolean isFragmentRefresher;

    public void addActions(String[] actions) {
        Collections.addAll(this.actions, actions);
    }

    public void addActionTypes(List<Attribute> actionTypes) {
        for (Attribute item : actionTypes) {
            actions.add(item.getValue().toString());
        }
    }

    public List<String> getActions() {
        return actions;
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

    public boolean getIsFragmentRefresher() {
        return isFragmentRefresher;
    }

    public void setIsFragmentRefresher(boolean isFragmentRefresher) {
        this.isFragmentRefresher = isFragmentRefresher;
    }
}
