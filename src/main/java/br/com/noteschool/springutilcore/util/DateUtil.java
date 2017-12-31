package br.com.noteschool.springutilcore.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

/**
 * @author marcelo.mourao
 * @since  13/04/2017
 */
public abstract class DateUtil {

	public static Date getFirstDayCurrentMonth(){
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
		
		return calendar.getTime();
	}
	
	public static Date getFirstDay(Date data){
		Calendar cal = Calendar.getInstance();
        cal.setTime(data);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        return cal.getTime();
	}
	
	public static Date addMonthsFirstDayCurrentMonth(int months){
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
		calendar.set(Calendar.AM_PM, Calendar.PM);
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.add(Calendar.MONTH, months);
		
		return calendar.getTime();
	}
	
	public static Long calcularIdade(Date nascimento){
		LocalDate hoje = LocalDate.now();
		
		Calendar dataNascimento = Calendar.getInstance();
		dataNascimento.setTime(nascimento);
		
		LocalDate nascimentoCidadao = LocalDate.of(dataNascimento.get(Calendar.YEAR), dataNascimento.get(Calendar.MONTH)+1, dataNascimento.get(Calendar.DAY_OF_MONTH));
			
		return ChronoUnit.YEARS.between(nascimentoCidadao, hoje);
	}
	
	@Deprecated
	public static Date transformStringToDate(String dataString){
		String []data;
		Calendar calData = Calendar.getInstance();
		if(dataString.indexOf("GMT") != -1)
		{
			data = dataString.substring(0, dataString.indexOf("GMT")).split("-");
		}
		else if(dataString.indexOf('T') != -1)
		{
			data = dataString.substring(0, dataString.indexOf('T')).split("-");
		}
		else
		{
			data = dataString.split("-");
		}
		
		calData.set(Integer.parseInt(data[0]), Integer.parseInt(data[1])-1, Integer.parseInt(data[2]),0,0,0);
		calData.set(Calendar.MILLISECOND, 0);
			
		return calData.getTime();
	}
	
	@Deprecated
	public static Date transformMilliToDate(String dataString){
		Calendar calData = Calendar.getInstance();
		calData.setTimeInMillis(Long.parseLong(dataString));
		
		return calData.getTime();
	}
	
	@Deprecated
	public static void validateDateInMiliOrStringDate(String dateString){
		if( !StringUtils.isNotEmpty(dateString) || (dateString.toString().indexOf('T') == -1 && !StringUtils.isNumeric(dateString)))
			throw new IllegalArgumentException("Formato da data inválido. A data deve estar em milisegundos ou no formato yyyy-MM-dd'T'");
	}

}
