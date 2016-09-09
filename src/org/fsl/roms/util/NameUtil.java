package org.fsl.roms.util;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.fsl.roms.constants.Constants;

public class NameUtil implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Last Name + First Name + Middle Name
	 * 
	 * @param firstName
	 * @param lastName
	 * @param middleName
	 * @return
	 */
	public String getLastNameFirstNameMiddleName(String firstName,
			String lastName, String middleName) {
		String retFullName = "";
		retFullName = (lastName != null) ? lastName.trim() : "";
		if (firstName != null && StringUtils.isNotBlank(firstName)) {
			retFullName += (!retFullName.trim().isEmpty()) ? ", "
					+ firstName.trim() : firstName.trim();
		}
		if (middleName != null && StringUtils.isNotBlank(middleName)) {
			retFullName += (!retFullName.trim().isEmpty()) ? " "
					+ middleName.trim() : middleName.trim();
		}

		return WordUtils.capitalize(retFullName.toLowerCase().trim(),
				Constants.STRING_DELIM_ARRAY);
	}

	/**
	 * First + Middle Name + Last Name
	 * 
	 * @param firstName
	 * @param lastName
	 * @param middleName
	 * @return
	 */
	public String getFirstNameMiddleNameLastName(String firstName,
			String middleName, String lastName) {
		String retFullName = "";
		retFullName = (StringUtils.isNotBlank(firstName)) ? firstName.trim()
				: "";

		if (middleName != null && StringUtils.isNotBlank(middleName)) {
			retFullName += (!retFullName.trim().isEmpty()) ? ", "
					+ middleName.trim() : middleName.trim();
		}
		if (lastName != null && StringUtils.isNotBlank(lastName)) {
			retFullName += (!retFullName.trim().isEmpty()) ? " "
					+ lastName.trim() : lastName.trim();
		}

		return WordUtils.capitalize(retFullName.toLowerCase().trim(),
				Constants.STRING_DELIM_ARRAY);
	}

	/*
	 * public String getFullName(String firstName, String lastName){ String
	 * retFullName = ""; retFullName = (lastName!=null)?lastName.trim():"";
	 * if(firstName!=null && StringUtils.isNotBlank(firstName)){ retFullName +=
	 * (!retFullName.trim().isEmpty())?", "+firstName.trim():firstName.trim(); }
	 * return WordUtils.capitalize(retFullName.toLowerCase().trim(),Constants.
	 * STRING_DELIM_ARRAY); }
	 */

	/*
	 * public String getProperName(String firstName, String lastName){ //return
	 * lastName + ", " + firstName; if(lastName !=null && firstName != null)
	 * return WordUtils.capitalize(lastName.toLowerCase().trim(),
	 * Constants.STRING_DELIM_ARRAY) + ", " +
	 * WordUtils.capitalize(firstName.toLowerCase().trim(),
	 * Constants.STRING_DELIM_ARRAY); if(lastName !=null && firstName == null)
	 * return WordUtils.capitalize(lastName.toLowerCase().trim(),
	 * Constants.STRING_DELIM_ARRAY); if(lastName ==null && firstName != null)
	 * return WordUtils.capitalize(firstName.toLowerCase().trim(),
	 * Constants.STRING_DELIM_ARRAY);
	 * 
	 * return ""; }
	 */

	/**
	 * Last Name + First Name
	 * 
	 * @param firstName
	 * @param lastName
	 * @return
	 */
	public String getLastNameFirstName(String firstName, String lastName) {
		if (lastName != null && firstName != null)
			return WordUtils.capitalize(lastName.toLowerCase().trim(),
					Constants.STRING_DELIM_ARRAY)
					+ ", "
					+ WordUtils.capitalize(firstName.toLowerCase().trim(),
							Constants.STRING_DELIM_ARRAY);
		if (lastName != null && firstName == null)
			return WordUtils.capitalize(lastName.toLowerCase().trim(),
					Constants.STRING_DELIM_ARRAY);
		if (lastName == null && firstName != null)
			return WordUtils.capitalize(firstName.toLowerCase().trim(),
					Constants.STRING_DELIM_ARRAY);

		return "";
	}

	/**
	 * First Name + Last Name
	 * 
	 * @param firstName
	 * @param lastName
	 * @return
	 */
	public String getFirstNameLastName(String firstName, String lastName) {
		// return WordUtils.capitalize(firstName.toLowerCase().trim()+ " " +
		// lastName.toLowerCase().trim(), Constants.STRING_DELIM_ARRAY) ;
		// return firstName+" "+lastName;
		if (lastName != null && firstName != null)
			return WordUtils.capitalize(firstName.toLowerCase().trim(),
					Constants.STRING_DELIM_ARRAY)
					+ " "
					+ WordUtils.capitalize(lastName.toLowerCase().trim(),
							Constants.STRING_DELIM_ARRAY);
		if (lastName != null && firstName == null)
			return WordUtils.capitalize(lastName.toLowerCase().trim(),
					Constants.STRING_DELIM_ARRAY);
		if (lastName == null && firstName != null)
			return WordUtils.capitalize(firstName.toLowerCase().trim(),
					Constants.STRING_DELIM_ARRAY);

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
}
