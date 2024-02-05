package com.ba.annotation;

import com.ba.enums.OperationEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Permission {
    /**
     * 菜单权限标识
     * @return
     */
    String menuFlag() default "";

    /**
     * 按钮权限
     * @return
     */
    OperationEnum perms() default OperationEnum.OTHER;
}
