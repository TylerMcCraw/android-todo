package com.w3bshark.todo.util;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by Tyler McCraw on 4/5/17.
 * <p/>
 * In Dagger, an unscoped component cannot depend on a scoped component. As
 * {@link com.w3bshark.todo.data.source.ISessionComponent}
 * is a scoped component ({@code @Singleton}, we create a custom
 * scope to be used by all activity components. Additionally, a component with a specific scope
 * cannot have a sub component with the same scope.
 */

@Documented
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface ActivityScoped {
}
