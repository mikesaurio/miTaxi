package codigo.labplc.mx.mitaxi.utils;

import java.util.regex.Pattern;

public class RegularExpressions {
	private static String IS_NUMBER = "(\\d+)";
	private static String IS_EMAIL = "^[\\w-]+(\\.[\\w-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	private static String IS_STRING = "(\\D+)";
	
	public static boolean isNumber(String expression) {
		return evaluateExpression(IS_NUMBER, expression);
	}
	
	public static  boolean isEmail(String expression) {
		return evaluateExpression(IS_EMAIL, expression);
	}
	
	public static  boolean isString(String expression) {
		return evaluateExpression(IS_STRING, expression);
	}
	
	private static boolean evaluateExpression(String regularExpression, String expression) {
		boolean match = Pattern.matches(regularExpression, expression);
		
		if (match) {
			return true;
		} else {
			return false;
		}
	}
}