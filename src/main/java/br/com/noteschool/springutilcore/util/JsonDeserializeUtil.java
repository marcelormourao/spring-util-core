package br.com.noteschool.springutilcore.util;

import java.util.Date;

import com.fasterxml.jackson.databind.JsonNode;

public class JsonDeserializeUtil {

	public static Long getLongAttr(JsonNode jsonNode, String atribute){
		
		validateJsonNodeNullable(jsonNode);
		JsonNode longAttr = jsonNode.get(atribute);
		
		if(longAttr != null && longAttr.canConvertToLong())
			return longAttr.asLong();
		else
			return null;
	}
	
	public static String getStringAttr(JsonNode jsonNode, String atribute){
		
		validateJsonNodeNullable(jsonNode);
		JsonNode stringAttr = jsonNode.get(atribute);
		
		if(!isStringNull(stringAttr))
			return stringAttr.toString();
		else
			return null;
	}
	
	public static Date getDateAttr(JsonNode jsonNode, String atribute){
		
		validateJsonNodeNullable(jsonNode);
		
		if(!JsonDeserializeUtil.isStringNull(jsonNode.get(atribute))){
			String dateString = jsonNode.get(atribute).toString();
			DateUtil.validateDateInMiliOrStringDate(dateString);
			
			if(dateString.indexOf('T') != -1 )
				return DateUtil.transformStringToDate(dateString);
			else
				return DateUtil.transformMilliToDate(dateString);
		}
		
		return null;
	}
	
	public static Boolean getBooleanAttr(JsonNode jsonNode, String atribute){
		
		validateJsonNodeNullable(jsonNode);
		JsonNode booleanAttr = jsonNode.get(atribute);
		
		if(booleanAttr != null && booleanAttr.isBoolean())
			return booleanAttr.asBoolean();
		else
			return null;
	}
	
	private static void validateJsonNodeNullable(JsonNode jsonNode){
		if(jsonNode == null || jsonNode.isNull())
			throw new IllegalArgumentException("JsonNode não pode ser nullo!");
	}
	
	public static boolean isStringNull(JsonNode stringAttr) {
		
		if(stringAttr == null || stringAttr.toString().equals("null"))
			return true;
		else
			return false;
	}
}
