package org.fsl.roms.util;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class ContextUtil {

	
	private static Context getContext(){
		Context context = null;
		try{
			Context initialContext = new InitialContext();
			context = (Context)initialContext.lookup("java:comp/env");
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return context;
	}
	
	/*
	public static String getSSOURL(){
		String spURL = (String)getContext().lookup("SSO_CONTEXT_PATH");
	}*/
	
	/*public static Object getValueFor(String varName){
		try {
			Object val = getContext().lookup(varName);
			return val;
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return null;
	}*/
		
}
