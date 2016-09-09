package org.fsl.roms.util;

import java.nio.CharBuffer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author cduricka
 * @date Nov 14, 2011
 **/

public class PhoneNumberUtil {

	public boolean validatePhoneNumber(String userPhoneNumberString) {

		// Regular Expression for Validating Phone Number
		String areacode = "[0-9]{4}"; // first 4 digits (1876)
		String numbers = "[0-9]{6}"; // last 6 digits
		String num = "[1-9]";// first digit of the number

		// Combined together, these form the allowed phone number regexp
		String phoneSpec = "^" + areacode + num + numbers + "$";

		Pattern VALID_PATTERN2 = Pattern.compile(phoneSpec);

		CharBuffer buffer = CharBuffer
				.wrap(userPhoneNumberString.toCharArray());
		return Pattern.matches(phoneSpec, buffer);
	}

	public boolean validatePhoneNumberNoAreaCode(String userPhoneNumberString) {

		// Regular Expression for Validating Phone Number
		String lastFourNumbers = "[0-9]{4}"; // last 6 digits
		String firstThreeNumbers = "[0-9]{3}";// first digit of the number

		// Combined together, these form the allowed phone number regexp
		String phoneSpec = "^" + firstThreeNumbers + "-" + lastFourNumbers
				+ "$";

		Pattern VALID_PATTERN2 = Pattern.compile(phoneSpec);

		CharBuffer buffer = CharBuffer
				.wrap(userPhoneNumberString.toCharArray());
		return Pattern.matches(phoneSpec, buffer);
	}

	public boolean validatePhoneNumberNoAreaCodeNoDash(
			String userPhoneNumberString) {

		// Regular Expression for Validating Phone Number
		String lastFourNumbers = "[0-9]{4}"; // last 6 digits
		String firstThreeNumbers = "[0-9]{3}";// first digit of the number

		// Combined together, these form the allowed phone number regexp
		String phoneSpec = "^" + firstThreeNumbers + lastFourNumbers + "$";

		Pattern VALID_PATTERN2 = Pattern.compile(phoneSpec);

		CharBuffer buffer = CharBuffer
				.wrap(userPhoneNumberString.toCharArray());
		return Pattern.matches(phoneSpec, buffer);
	}
	
	public static boolean validateAllPhoneNumbers(String phoneNumberString){
		  boolean returnVal = false;
			 
	      //Pattern pattern1 = Pattern.compile("\\d{3}-\\d{4}");
	     //Pattern pattern2 = Pattern.compile("\\d{3}-\\d{3}-\\d{4}");
		  //Pattern pattern = Pattern.compile("\\d{3}-\\d{3}-\\d{4}");
	      Pattern pattern3 = Pattern.compile("\\(\\d{3}\\)\\d{3}-\\d{4}");
	     
	      
	      
	      //Checks for validity of phonenumber with no area code
	     // Matcher matcher1 = pattern1.matcher(phoneNumberString);
	      //Checks for validity of phonenumber with area code
	     // Matcher matcher2 = pattern2.matcher(phoneNumberString);
	      Matcher matcher3 = pattern3.matcher(phoneNumberString);
	      
	     if (matcher3.matches()) {
		    	 returnVal = true;
		      }
	     
	      
	      return returnVal;
	}
}
