package br.com.noteschool.springutilcore.repository.interceptor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author marcelo.mourao
 * @since  18/09/2017
 * @see AcentuacaoInterceptorHandler
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AcentuacaoInterceptor {

}
