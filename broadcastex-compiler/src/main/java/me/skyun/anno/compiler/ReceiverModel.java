package me.skyun.anno.compiler;

/**
 * Created by linyun on 16/11/6.
 */
public class ReceiverModel {
    public String statement = "test statement";
    public String action = "no action";
    public String[] categories = {"no", "categories"};
    public String methodName;

    public String getStatement() {
        return statement;
    }

    public String getAction() {
        return action;
    }

    public String[] getCategories() {
        return categories;
    }

    public String getMethodName() {
        return methodName;
    }
}
