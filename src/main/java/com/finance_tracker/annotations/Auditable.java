package com.finance_tracker.annotations;

import com.finance_tracker.enums.AuditResourceType;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Auditable {
    String actionType();
    boolean includeArgs() default true;
    boolean includeResult() default true;
    AuditResourceType resourceType() default AuditResourceType.UNKNOWN;
}