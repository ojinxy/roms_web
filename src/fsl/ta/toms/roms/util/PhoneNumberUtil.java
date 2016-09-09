package fsl.ta.toms.roms.util;

import java.nio.CharBuffer;
import java.util.regex.Pattern;

public class PhoneNumberUtil {
	public boolean validatePhoneNumber(String userPhoneNumberString)
	{
		
	
		//Regular Expression for Validating Phone Number
		String areacode="[0-9]{4}"; //first 4 digits (1876)
		String numbers="[0-9]{6}"; //last 6 digits
		String num="[1-9]";//first digit of the number
		
		//Combined together, these form the allowed phone number regexp 
		String phoneSpec="^" + areacode + num + numbers + "$";
		
		Pattern VALID_PATTERN2 = Pattern.compile( phoneSpec );
		
		CharBuffer buffer = CharBuffer.wrap(userPhoneNumberString.toCharArray());
		return Pattern.matches(phoneSpec,buffer);
	}
	
	public boolean validatePhoneNumberNoAreaCode(String userPhoneNumberString)
	{
			
		//Regular Expression for Validating Phone Number
		String lastFourNumbers="[0-9]{4}"; //last 6 digits
		String firstThreeNumbers="[0-9]{3}";//first digit of the number
	
		//Combined together, these form the allowed phone number regexp 
		String phoneSpec="^" + firstThreeNumbers+ "-" + lastFourNumbers + "$";
		
		Pattern VALID_PATTERN2 = Pattern.compile( phoneSpec );
		
		CharBuffer buffer = CharBuffer.wrap(userPhoneNumberString.toCharArray());
		return Pattern.matches(phoneSpec,buffer);
	}


	public boolean validatePhoneNumberNoAreaCodeNoDash(String userPhoneNumberString)
	{
			
		//Regular Expression for Validating Phone Number
		String lastFourNumbers="[0-9]{4}"; //last 6 digits
		String firstThreeNumbers="[0-9]{3}";//first digit of the number
	
		//Combined together, these form the allowed phone number regexp 
		String phoneSpec="^" + firstThreeNumbers + lastFourNumbers + "$";
		
		Pattern VALID_PATTERN2 = Pattern.compile( phoneSpec );
		
		CharBuffer buffer = CharBuffer.wrap(userPhoneNumberString.toCharArray());
		return Pattern.matches(phoneSpec,buffer);
	}
}
