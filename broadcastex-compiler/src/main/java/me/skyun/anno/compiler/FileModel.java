package me.skyun.anno.compiler;

/**
 * Created by linyun on 16/11/6.
 */
public class FileModel {
    public String packageName = "me.skyun.mock";
    public String className = "MockClass";
    public String statement = "${statement}";

    public FileModel(String packageName, String className) {
        this.packageName = packageName;
        this.className = className;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getClassName() {
        return className;
    }

    public String getStatement() {
        return statement;
    }
}
