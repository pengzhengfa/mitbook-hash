package com.mitbook.annotation;

import java.lang.annotation.*;

/**
 * @author pengzhengfa
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CommonLimit {


}
