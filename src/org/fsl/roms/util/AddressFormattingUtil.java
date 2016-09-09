package org.fsl.roms.util;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;

/**
 * 
 * @author rbrooks
 *Created to provide a utility to format addresses
 */
public class AddressFormattingUtil {

	//String delimiter array
	public static final char[] STRING_DELIM_ARRAY = {'.',' ','-','\n','/'};
	
	public static String getFormattedAddress(String markText,
			String parishDesc, String parishCode, String poBoxNo,
			String poLocationName, String streetName, String streetNo)
	{
		
		return (getAddressLine1(markText,
			parishDesc, parishCode, poBoxNo,
			poLocationName, streetName, streetNo) + 
			getAddressLine2(markText,
					parishDesc, parishCode, poBoxNo,
					poLocationName, streetName, streetNo));
		
		
	}
	
	private static String getAddressLine1(String markText,
			String parishDesc, String parishCode, String poBoxNo,
			String poLocationName, String streetName, String streetNo) {
		
		String address = "";
		/*if (addrMark != null && !"".equals(addrMark)) {
			address = addrMark.trim();
		}*/
	/*	if (addrStreetNo != null && !"".equals(addrStreetNo.trim())) {
			if ("".equals(address)) {
				address = addrStreetNo.trim();
			} else {
				address = address + ", " + addrStreetNo.trim();
			}
		}*/
		if (streetName != null && !"".equals(streetName.trim())) {
			if ("".equals(address)) {
				address = streetName.trim();
			} else {
				address = address + " " + streetName.trim() + "";
			}
		}			
		
		return WordUtils.capitalize(address.toLowerCase(), STRING_DELIM_ARRAY);
	}

	private static String getAddressLine2(String markText,
			String parishDesc, String parishCode, String poBoxNo,
			String poLocationName, String streetName, String streetNo) {
		boolean kingston = false;
		String address = "";
					
		if (poBoxNo != null && !"".equals(poBoxNo.trim())) {				
			if ("".equals(address)) {
				address = " P.O. Box " + poBoxNo.trim();
			}else{
				address = address + ", P.O. Box "  + poBoxNo.trim();
			}
		}
			
		if (poLocationName != null && !"".equals(poLocationName.trim())) {
			String po = poLocationName.toUpperCase();
			//if po box is not there then put a comma before the poLocName 
			if(StringUtils.isBlank(poLocationName) && StringUtils.isNotBlank(address) ){
				address = address + ",";
			}
				
			if (po.contains("KINGSTON")) {
				kingston = true;
				// if kingston is in the name then put it
				if ("".equals(address)) {
					address = poLocationName.trim() ;
				} else {
					address = address + " " + poLocationName.trim();
				}
			} else {
				if ("".equals(address)) {
					address = poLocationName.trim() ;
				} else {
					address = address + " " + poLocationName.trim() ;
				}
			}
		}
				
		

		if (parishDesc != null) {
			
				String par = parishDesc.toUpperCase();
				if (par.contains("KINGSTON")) {
					// if kingston was not put before then put it
					// set variable to true
					if (kingston == false) {
						//kingston = true;
						if ("".equals(address)) {
							address = parishDesc.trim();
						} else {
							address = address+ ", " + parishDesc.trim();
						}
					}

				} else {
					if ("".equals(address)) {
						address =  parishDesc.trim();
					} else {
						address = address + ", " + parishDesc.trim();
					}
				}
			
		} 
	

		

		
		
		return WordUtils.capitalize(address.toLowerCase(), STRING_DELIM_ARRAY);
		
	}
	
	
}
