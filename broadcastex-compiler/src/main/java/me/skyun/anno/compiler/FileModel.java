package me.skyun.anno.compiler;

/**
 * Created by linyun on 16/11/6.
 */
public class FileModel {
    public String packageName;
    public String className;
    public String targetType;

    public FileModel(String packageName, String className, String targetType) {
        this.packageName = packageName;
        this.className = className;
        this.targetType = targetType;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getClassName() {
        return className;
    }

    public String getTargetType() {
        return targetType;
    }
}
