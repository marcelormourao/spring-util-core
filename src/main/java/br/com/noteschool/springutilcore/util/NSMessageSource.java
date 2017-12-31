package br.com.noteschool.springutilcore.util;

import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

/**
 * @author marcelo.mourao
 * @since 16/01/2017
 */
@Component
public class NSMessageSource {
	
	private @Autowired MessageSource messageSource;
	
	public NSMessageSource() {}
	
	public NSMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
	
	public String getMessage(String code, Object... args) throws NoSuchMessageException {
		return this.messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
	}
	
	public String getMessage(String code, Locale locale, Object... args) throws NoSuchMessageException {
		return this.messageSource.getMessage(code, args, locale);
	}
	
	public String getMessage(MessageSourceResolvable resolvable, Locale locale){
		return this.messageSource.getMessage(resolvable,locale);
	}
	
	public String getMessage(MessageSourceResolvable resolvable){
		return this.messageSource.getMessage(resolvable, LocaleContextHolder.getLocale());
	}
}


