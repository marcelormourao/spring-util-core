package br.com.noteschool.springutilcore.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author marcelo.mourao
 * @since  13/10/2017
 * @see NSDateTimeFormat
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@DateTimeFormat(pattern="yyyy-MM-dd")
public @interface NSDateFormat {
	
}
