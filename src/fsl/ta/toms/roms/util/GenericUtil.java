package fsl.ta.toms.roms.util;

import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Properties;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;

public class GenericUtil {

	private static Properties messageProps; ///	props.load(getClass().getClassLoader().getResourceAsStream("messages.properties"));
	private static ResourceBundle messages = ResourceBundle.getBundle("messages");


	/**
	 * 
	 * @param context
	 * @param messageString
	 * @return
	 */
	public static String buildErrorMessageWithParameter(String messageString, String parameter) {
		
		Object[] objArray = {parameter};
		MessageFormat formatter = new MessageFormat("");
		formatter.applyPattern(messages.getString(messageString));
		String errorOutput = formatter.format(objArray);

		return errorOutput;
	}
	
	
	/**
	 * @author kpowell
	 * Address Validation
	 */
	public static String validateAddress( AddressView address){
	
		
		System.err.println("validateAddress");

		if (StringUtils.isBlank(address.getParish())) {
			return buildErrorMessageWithParameter("RequiredFields","Parish");

		}
		
		if (StringUtils.isBlank(address.getStreetName()) && StringUtils.isBlank(address.getMarkText())&& StringUtils.isBlank(address.getPoLocationName())) {
			return buildErrorMessageWithParameter("RequiredFields", "Street Name, Mark or PO Location");
			
		}else{
			
			if (!StringUtils.isBlank(address.getStreetNo()) && StringUtils.isBlank(address.getStreetName())) {
				return buildErrorMessageWithParameter( "RequiredFields", "Street Name");
			}
			
			if (!StringUtils.isBlank(address.getPoBoxNo()) && StringUtils.isBlank(address.getPoLocationName())) {
				return buildErrorMessageWithParameter("RequiredFields", "PO Location");

			}
		}

		return null;

	
	}
}
