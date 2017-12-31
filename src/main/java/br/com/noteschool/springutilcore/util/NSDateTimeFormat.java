package br.com.noteschool.springutilcore.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author marcelo.mourao
 * @since  11/10/2017
 * @see NSDateFormat
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
public @interface NSDateTimeFormat {
	
}
