package me.skyun.anno.api;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
public @interface BroadcastExReceiver {

    int integer() default 0;

    String action() default "";

    String[] categories() default {};

    /**
     * 必须跟{@link #actionType()}函数名一致
     */
    public static final String ACTION_TYPE = "actionType";

    Class actionType() default Class.class;

    /**
     * 必须跟{@link #actionType()}函数名一致
     */
    public static final String CATEGORY_TYPES = "categoryTypes";

    Class[] categoryTypes() default {};
}
