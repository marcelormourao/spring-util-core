package br.com.noteschool.springutilcore.util;

import java.lang.reflect.Field;
import java.util.Arrays;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

/**
 * @author marcelo.mourao
 * @since 23/01/2017
 */
public class ReflectionUtil {
	
	public static Field getIdField(Object entity) {		
		Class<? extends Object> clazz = entity.getClass();
		
		Field idField = null;
		
		do{
			idField = getIdField(clazz);
			
			if(idField != null)
				break;
			
			clazz = clazz.getSuperclass();
		}while(clazz != null && clazz != Object.class);
		
		return idField;
	}
	
	public static <T> Field getIdField(Class<T> clazz) {
		return Arrays.asList(clazz.getDeclaredFields()).parallelStream().filter(f -> f.getAnnotation(Id.class) != null ).findFirst().orElse(null);
	}

	public static  boolean isIdField(Field field){
		return field.isAnnotationPresent(Id.class);
	}
	
	public static  boolean isTransient(Field field){
		return field.isAnnotationPresent(Transient.class);
	}

	public static boolean isSubclassItarable(Field field) {
		return Iterable.class.isAssignableFrom(field.getType());
	}

	public static boolean isSerialVersionField(Field field) {
		return field.getName().equals("serialVersionUID");
	}

	public static boolean isEntity(Field field) {
		return field.getType().isAnnotationPresent(Entity.class);
	}
	
	public static boolean isIgnoreOnGenericUpdate(Field field) {
		return field.isAnnotationPresent(IgnoreOnGenericUpdate.class);
	}
	
	public static boolean isMappedByField(Field field){
		return 
		(field.getAnnotation(OneToOne.class) != null && !"".equals(field.getAnnotation(OneToOne.class).mappedBy()))
				
		||
		
		(field.getAnnotation(OneToMany.class) != null && !"".equals(field.getAnnotation(OneToMany.class).mappedBy()))
		
		||
		
		(field.getAnnotation(ManyToMany.class) != null && !"".equals(field.getAnnotation(ManyToMany.class).mappedBy()));
	}

	public static boolean isFieldNotNull(Field field, Object entity){
	    return getValueByField(field, entity) != null;
	}
	
	public static boolean isFieldNull(Field field, Object entity){
	    return getValueByField(field, entity) == null;
	}
	
	public static Object getValueByField(Field field, Object entity){
		try {
			field.setAccessible(true);
			
			return field.get(entity);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new RuntimeException(e.getMessage());	
		}
	}
	
	public static boolean isUpdatable(Field field){
		return 
				(field.getAnnotation(Column.class) == null || field.getAnnotation(Column.class).updatable()) &&
				(field.getAnnotation(JoinColumn.class) == null || field.getAnnotation(JoinColumn.class).updatable());
	}
	
	public static boolean isEntityIdNull(Object entity){
		
		if(entity == null){
			return true;
		}
		
		Field idField = getIdField(entity);
		
		if(idField == null){
			throw new IllegalArgumentException("Objeto passado não possui a anotação @Id.");
		}
		
		if(getValueByField(idField, entity) == null){
			return true;
		}
		
		return false;
	}
	
	public static Field getFieldByName(String fieldName, Object entity){
		try {
			return entity.getClass().getDeclaredField(fieldName);
		} catch (NoSuchFieldException | SecurityException e) {
			return null;
		}
	}
}
