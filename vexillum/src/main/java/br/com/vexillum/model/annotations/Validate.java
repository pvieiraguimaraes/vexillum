package br.com.vexillum.model.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Validate {
	boolean notNull() default false;
	int min() default 0;
	int max() default 0;
	boolean past() default false;
	boolean future() default false;
	boolean email() default false;
	boolean unique() default false;
	boolean caseSensitive() default true;
}
