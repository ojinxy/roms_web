package fsl.ta.toms.roms.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import fsl.ta.toms.roms.constants.Constants.BadgeStatusCode;


public class StringUtil {
	public static boolean isSet(String arg1)
	{
		boolean response = false;
		
		if(arg1!= null)
		{
			if(arg1.trim().length()>=1)
				response = true;
		}
		
		
		return response;
	}
	
	
	public static String padString(String asString, String asPad, int aiLength) {
		String sString = asString.trim();
		StringBuffer paddedStringBuffer = new StringBuffer();
		int iLength = asString.length();

		for (int i = 0; i < (aiLength - iLength); i++) {
		    paddedStringBuffer.append(asPad);
		}
		paddedStringBuffer.append(sString);

		return paddedStringBuffer.toString();
	}
	
	
	public static String byteArrayToString(byte[] input) throws IOException
	{
		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		copyInputStream(new  ByteArrayInputStream(input),bos);
		
		return bos.toString();
	}
	
	
	public static String formatAsCurrency(BigDecimal value)
	{
		NumberFormat nf = NumberFormat.getCurrencyInstance();
		
		String response = nf.format(value);
		
		return response;
	}
	

	public static boolean isNumber(String value, boolean decimalAllowed, boolean negativeAllowed){
		boolean isNumber = false;
		String regexDec="\\d{0,}(\\.\\d{0,})?";
		String regex="\\d+";
		
		if(negativeAllowed)
		{
		    regex="(-{0,})?"+regex;
		    regexDec = "(-{0,})?"+regexDec;
		}
		
		if(isSet(value))
		{
			if(decimalAllowed)
				isNumber = value.matches(regexDec);
			else
				isNumber = value.matches(regex);
		}
		return isNumber;
	}

	
	
	
	public static String getCurrentDateString()
	{
		String dateString;
		
		Date now = Calendar.getInstance().getTime();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		
		dateString = df.format(now);
		
		return dateString;
	}
	

	
	public static BigDecimal toBigDecimal(String value, int scale)
	{
	    if(!isSet(value))
	        value = "0";
	        
	    BigDecimal response =  new BigDecimal(value).setScale(scale, BigDecimal.ROUND_HALF_UP);
	    
	    
	    return response;
	}
	
	
	
	public static String stripUnwantedChars(String unstripped)
	{
		String strippedStr = null;
		strippedStr = unstripped.replaceAll("\\$", "").replaceAll("\\,", "");
		return strippedStr;
	}



public static void copyInputStream(InputStream in, OutputStream out)throws IOException
{
	  byte[] buffer = new byte[1024];
	  int len;
	
	  while((len = in.read(buffer)) >= 0)
		out.write(buffer, 0, len);
	
}

public static String mapToROMSStatus(String externalStatus)
{
	String newStatus = "INA"; //use this as default
	
	
	if (isSet(externalStatus))
	{
		if (externalStatus.trim().equalsIgnoreCase("AC"))
		{
			newStatus = "ACT";
		}
		
		if (externalStatus.trim().equalsIgnoreCase("IA"))
		{
			newStatus = "INA";
		}
		
	}
	
	return newStatus;
}

public static String mapBadgeStatusToROMSStatus(String badgeStatus)
{
	String newStatus = "ACT"; //use this as default
	
	
	if (isSet(badgeStatus))
	{
		if (badgeStatus.trim().equalsIgnoreCase(BadgeStatusCode.REVOKED) || 
			badgeStatus.trim().equalsIgnoreCase(BadgeStatusCode.SUSPENDED))
		{
			newStatus = "INA";
		}
	
	}
	
	return newStatus;
}

	/**
	 * This function replaces the single apostrophe with a double apostrophe to prevent the sql from failing
	 * @param str
	 * @return
	 */
	public static String quoteReplace(String str){
		String quoteReplaceStr;
		
		quoteReplaceStr = StringUtils.replace(str, "'", "''");
	
		return quoteReplaceStr;
		
	}
	
	
	public static String appendSemiColon(String stringExisting , String stringAppendage) 
	{
		String stringOut = "";
		
		if(!isSet(stringExisting))
		{
			return stringAppendage;
			
		}
		
		stringExisting = " " + stringExisting;
		
		if(stringExisting != null)
		{ 

			char endChar = stringExisting.charAt(stringExisting.length()-1);
			if(endChar == ';')
			{
				return stringAppendage;
			}else
			{

				return "; " + stringAppendage;
			}
			
		}

		return stringAppendage;
	}
	
	
}
