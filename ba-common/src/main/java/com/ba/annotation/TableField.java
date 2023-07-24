package com.ba.annotation;

import com.ba.enums.FieldFill;
import com.ba.enums.FieldType;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
public @interface TableField {

    String value() default "";

    /**
     * 生成类型
     */
    FieldType type() default FieldType.DEFAULT;

    /**
     * SQL类型
     */
    FieldFill fill() default FieldFill.DEFAULT;

}
