package com.w3bshark.todo.data.source;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;

/**
 * Created by Tyler McCraw on 3/7/17.
 * <p/>
 * Qualifier for Remote Data Source
 */

@Qualifier
@Documented
@Retention(RetentionPolicy.RUNTIME)
@interface Remote {
}