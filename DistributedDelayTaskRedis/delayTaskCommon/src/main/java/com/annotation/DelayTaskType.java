package com.annotation;
import com.enums.BusinessTypeEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import	java.lang.annotation.Target;

/**
 * 延时任务类型
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DelayTaskType {
    /**
     * 主题
     * @return
     */
    BusinessTypeEnum topic() default BusinessTypeEnum.opendAccount;

    /**
     * 描述
     * @return
     */
    String topicDesc() default "";
}
