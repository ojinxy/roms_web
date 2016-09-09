package fsl.ta.toms.roms.util;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;

import fsl.ta.toms.roms.constants.Constants;

public class NameUtil {
	public String toString(String firstName, String lastName, String middleName) {
		String retFullName = "";
	
		
		retFullName = (lastName!=null)?lastName.trim():"";
		if(firstName!=null && StringUtils.isNotBlank(firstName)){
			retFullName += (!retFullName.trim().isEmpty())?", "+firstName.trim():firstName.trim();
		}
		if(middleName!=null && StringUtils.isNotBlank(middleName)){
			retFullName += (!retFullName.trim().isEmpty())?" "+middleName.trim():middleName.trim();
		}
	
		return WordUtils.capitalize(retFullName.toLowerCase().trim(),Constants.STRING_DELIM_ARRAY);
	}
	
	public static String getFullName(String firstName, String lastName){
		String retFullName = "";
		retFullName = (lastName!=null)?lastName.trim():"";
		if(firstName!=null && StringUtils.isNotBlank(firstName)){
			retFullName += (!retFullName.trim().isEmpty())?", "+firstName.trim():firstName.trim();
		}
		return WordUtils.capitalize(retFullName.toLowerCase().trim(),Constants.STRING_DELIM_ARRAY);
	}
	
	public static String getProperName(String firstName, String lastName){
		//return lastName + ", " + firstName;
		if(lastName !=null && firstName != null)
			return WordUtils.capitalize(lastName.toLowerCase().trim(), Constants.STRING_DELIM_ARRAY) + ", " + WordUtils.capitalize(firstName.toLowerCase().trim(), Constants.STRING_DELIM_ARRAY);
		if(lastName !=null && firstName == null)
			return WordUtils.capitalize(lastName.toLowerCase().trim(), Constants.STRING_DELIM_ARRAY);
		if(lastName ==null && firstName != null)			
			return WordUtils.capitalize(firstName.toLowerCase().trim(), Constants.STRING_DELIM_ARRAY);
		
		return "";
	}
	
	static public String getName(String firstName, String lastName){
		if(lastName !=null && firstName != null)
			return WordUtils.capitalize(lastName.toLowerCase().trim(), Constants.STRING_DELIM_ARRAY) + ", " + WordUtils.capitalize(firstName.toLowerCase().trim(), Constants.STRING_DELIM_ARRAY);
		if(lastName !=null && firstName == null)
			return WordUtils.capitalize(lastName.toLowerCase().trim(), Constants.STRING_DELIM_ARRAY);
		if(lastName ==null && firstName != null)			
			return WordUtils.capitalize(firstName.toLowerCase().trim(), Constants.STRING_DELIM_ARRAY);
		
		return "";
	}
	
	public String getNameForPrint(String firstName, String lastName){
		//return 	WordUtils.capitalize(firstName.toLowerCase().trim()+ " " + lastName.toLowerCase().trim(), Constants.STRING_DELIM_ARRAY) ;		
		//return firstName+" "+lastName;
		if(lastName !=null && firstName != null)
			return  WordUtils.capitalize(firstName.toLowerCase().trim(), Constants.STRING_DELIM_ARRAY) + " " +WordUtils.capitalize(lastName.toLowerCase().trim(), Constants.STRING_DELIM_ARRAY) ;
		if(lastName !=null && firstName == null)
			return WordUtils.capitalize(lastName.toLowerCase().trim(), Constants.STRING_DELIM_ARRAY);
		if(lastName ==null && firstName != null)			
			return WordUtils.capitalize(firstName.toLowerCase().trim(), Constants.STRING_DELIM_ARRAY);
		
		return "";
	}
	
	/**
	 * LASTNAME, First Name Middle Name
	 * 
	 * @param firstName
	 * @param lastName
	 * @param middleInitial
	 * @return
	 */
	public String getLastNameCapsFirstNameMiddleName(String firstName,
			String lastName, String middleName) {
		String retFullName = "";
		retFullName = (lastName != null) ? lastName.trim().toUpperCase() : "";
		if (firstName != null && StringUtils.isNotBlank(firstName)) {
			retFullName += (!retFullName.trim().isEmpty()) ? ", "
					+  WordUtils.capitalize(firstName.trim().toLowerCase(),Constants.STRING_DELIM_ARRAY)  : WordUtils.capitalize(firstName.trim().toLowerCase(), Constants.STRING_DELIM_ARRAY) ;
		}
		if (middleName != null && StringUtils.isNotBlank(middleName)) {
			retFullName += (!retFullName.trim().isEmpty()) ? " "
					+ WordUtils.capitalize(middleName.trim().toLowerCase(),Constants.STRING_DELIM_ARRAY) : WordUtils.capitalize(middleName.trim().toLowerCase(),Constants.STRING_DELIM_ARRAY);
		}

		return retFullName;
	}
	
	/**
	 * 
	 * @param firstName
	 * @param lastName
	 * @param middleName
	 * @return
	 */
	public String getLastNameCapsFirstNameMiddleInitial(String firstName,
		String lastName, String middleName) {
	String retFullName = "";
	retFullName = (lastName != null) ? lastName.trim().toUpperCase() : "";
	if (firstName != null && StringUtils.isNotBlank(firstName)) {
		retFullName += (!retFullName.trim().isEmpty()) ? ", "
				+  WordUtils.capitalize(firstName.trim().toLowerCase(),Constants.STRING_DELIM_ARRAY)  : WordUtils.capitalize(firstName.trim().toLowerCase(), Constants.STRING_DELIM_ARRAY) ;
	}
	if (middleName != null && StringUtils.isNotBlank(middleName)) {
		retFullName += (!retFullName.trim().isEmpty()) ? " "
				+ middleName.trim().substring(0, 1).toUpperCase() : "";
	}

	return retFullName;
}

}
