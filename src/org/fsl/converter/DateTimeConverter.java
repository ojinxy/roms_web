package org.fsl.converter;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.component.StateHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import org.apache.commons.lang.StringUtils;
import org.fsl.roms.util.DateUtils;

/**
 * 
 * @author jreid
 * 
 */
@FacesConverter(value = "DateTimeConverter")
public class DateTimeConverter implements Converter, Serializable
{

	/**   
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String datePattern = "yyyy-MM-dd";
	//String timePattern = "hh:mm a";
	String timePattern = "h:mm a";
	
	String showTime;
	SimpleDateFormat sdf;
	public static final String CONVERTER_ID = "DateTimeConverter";

	public DateTimeConverter() 
	{

		setShowTime("false");
		
	}

	
	public DateTimeConverter(String showTime) {
		super();
		this.showTime = showTime;
	}


	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) throws ConverterException 
	{
		System.out.println("Show Time " + this.showTime);
		
		java.util.Date uDate = null;
		java.util.Date sDate = null;
		this.setDatePattern();
		
		
		if (StringUtils.isBlank(value)) {
			return null;
		}

		if (DateUtils.isDateValid(value.trim(),this.showTime.toLowerCase().equalsIgnoreCase("true")) == true) {
			try {
				uDate = sdf.parse(value);
			} catch (ParseException ex) {

				FacesMessage message = new FacesMessage();
				String fieldName = (String) component.getAttributes()
						.get("alt");

				message.setDetail(fieldName + " Date is invalid.");
				message.setSummary(fieldName + " Date is invalid.");

				message.setSeverity(FacesMessage.SEVERITY_ERROR);

				throw new ConverterException(message);
			}

			if (uDate != null) {
				//sDate = new  java.sql.Date(uDate.getTime());
				sDate = uDate;
			}

			return sDate;
		} else {

			FacesMessage message = new FacesMessage();
			String fieldName = (String) component.getAttributes().get("alt");

			message.setDetail(fieldName + " Date is invalid.");
			message.setSummary(fieldName + " Date is invalid.");

			message.setSeverity(FacesMessage.SEVERITY_ERROR);

			throw new ConverterException(message);
		}
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object value) {
		// TODO Auto-generated method stub
		this.setDatePattern();
		
		if (value == null) {
			return "";
		}
	
		String rtDateStr = "";
		
		try{
			rtDateStr = sdf.format((Date)value);
		}catch(Exception ex){
			ex.printStackTrace();
			rtDateStr = value.toString();
		}		
		//System.err.println("Uicomp: "+arg1.getClientId()+" | "+arg1.getParent().getClientId()+", VALUE: "+value);
		
		return rtDateStr;
		
	}


	
	public String getShowTime() {
		return showTime;
	}

	public void setShowTime(String showTime) {
		this.showTime = showTime;
	}

	private void setDatePattern()
	{
		
		if(this.showTime != null && this.showTime.trim().toLowerCase().equalsIgnoreCase("false"))
			this.sdf = new SimpleDateFormat(datePattern.trim());
		else
			this.sdf = new SimpleDateFormat(datePattern.trim() + " " + timePattern.trim());
		
	
		
	}
}
