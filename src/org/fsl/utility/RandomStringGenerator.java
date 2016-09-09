package org.fsl.utility;

import java.util.Date;
import java.util.Random;

public class RandomStringGenerator {
	static final String BASE_STRING = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	static Random random = new Random((new Date()).getTime());
	static int DEFAULT_SIZE = 15;
	/**
	 * @param args
	 */

	public static String randomString( int size ) 
	{
		if( size <= 0 )
			return "";
		
	   StringBuilder sb = new StringBuilder( size );
	   for( int i = 0; i < size; i++ ) 
	      sb.append( BASE_STRING.charAt( random.nextInt(BASE_STRING.length()) ) );
	   return sb.toString();
	}
	
	public static String randomString() 
	{
	   StringBuilder sb = new StringBuilder( DEFAULT_SIZE );
	   for( int i = 0; i < DEFAULT_SIZE; i++ ) 
	      sb.append( BASE_STRING.charAt( random.nextInt(BASE_STRING.length()) ) );
	   return sb.toString();
	}
}
