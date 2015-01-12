package com.openi.interfaces;

import java.util.HashMap;
import java.util.Map;

public class Measurements {
	final static int WEIGHT = 1;
	final static int HEIGHT = 2; 
	final static int SYSTOLIC = 3; 
	final static int DIASTOLIC = 4;	
	
	final static String WEIGHT_MS = "KG";
	final static String HEIGHT_MS = "CM"; 
	final static String SYSTOLIC_MS = "mmHg"; 
	final static String DIASTOLIC_MS = "mmHg";	
	
	public static Map<String,String> dictionary;
	
	public static Map<String,String> getDictionary(){
		dictionary = new HashMap<String, String>();
		dictionary.put("WEIGHT",WEIGHT_MS);
		dictionary.put("HEIGHT",HEIGHT_MS);
		dictionary.put("SYSTOLIC",SYSTOLIC_MS);
		dictionary.put("DIASTOLIC",DIASTOLIC_MS);
		return dictionary;
		
	}
}
