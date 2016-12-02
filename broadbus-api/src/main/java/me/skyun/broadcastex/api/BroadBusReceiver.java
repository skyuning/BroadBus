package me.skyun.broadcastex.api;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public @interface BroadBusReceiver {

    String[] actions() default "";

    String[] categories() default {};

    /**
     * 必须跟{@link #actionTypes()}函数名一致
     */
    String ACTION_TYPES = "actionTypes";

    Class[] actionTypes() default Class.class;

    /**
     * 必须跟{@link #categoryTypes()} ()}函数名一致
     */
    String CATEGORY_TYPES = "categoryTypes";

    Class[] categoryTypes() default {};

    boolean isFragmentRefresher() default false;
}
