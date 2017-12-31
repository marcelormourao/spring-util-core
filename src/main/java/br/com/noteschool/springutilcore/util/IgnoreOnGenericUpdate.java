package br.com.noteschool.springutilcore.util;

import static java.lang.annotation.ElementType.FIELD;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author marcelo.mourao
 * @since  02/06/2017
 */
@Target(FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface IgnoreOnGenericUpdate {

}
