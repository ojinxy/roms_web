package org.fsl.converter;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.faces.application.FacesMessage;
import javax.faces.component.StateHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.lang.StringUtils;
import org.fsl.roms.util.DateUtils;

/**
 * 
 * @author oanguin
 * 
 */
public class XMLGregorianCalendarConverter implements Converter, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String datePattern = "yyyy-MM-dd";
	String timePattern = "h:mm a";
	private String showTime = "";
	SimpleDateFormat sdf;
	public static final String CONVERTER_ID = "XMLGregorianCalendarConverter";

	public XMLGregorianCalendarConverter() 
	{
		super();
		//setShowTime("false");
		
	}
	
	

	public XMLGregorianCalendarConverter(String showTime) {
		super();
		this.showTime = showTime;
	}



	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) throws ConverterException 
	{
		java.util.Date uDate = null;
		
		this.setDatePattern();
		
		System.out.println("XMLGreg Date is " + value);
		
		if (StringUtils.isBlank(value)) {
			return null;
		}

		if (DateUtils.isDateValid(value.trim(),this.showTime.toLowerCase().equalsIgnoreCase("true")) == true) 
		{
			try
			{
				
				uDate = sdf.parse(value);
		
			} catch (ParseException ex) {

				FacesMessage message = new FacesMessage();
				String fieldName = (String) component.getAttributes()
						.get("alt");

				message.setDetail(fieldName + " Date is invalid.");
				message.setSummary(fieldName + " Date is invalid.");

				message.setSeverity(FacesMessage.SEVERITY_ERROR);
				ex.printStackTrace();
				throw new ConverterException(message);
			}

	
			
			GregorianCalendar startDateGCal = new GregorianCalendar();
			startDateGCal.setTime(uDate);				
			XMLGregorianCalendar startDateXML = null;
			try 
			{
				startDateXML = DatatypeFactory.newInstance().newXMLGregorianCalendar(startDateGCal);
			} 
			catch (DatatypeConfigurationException e) 
			{
				e.printStackTrace();
				FacesMessage message = new FacesMessage();
				String fieldName = (String) component.getAttributes()
						.get("alt");

				message.setDetail(fieldName + " Date is invalid.");
				message.setSummary(fieldName + " Date is invalid.");

				message.setSeverity(FacesMessage.SEVERITY_ERROR);
				e.printStackTrace();
				throw new ConverterException(message);
				
			}
			
			return startDateXML;
			
		} 
		else 
		{

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
	
		try
		{
			XMLGregorianCalendar xmlGregCal = (XMLGregorianCalendar)value;
			
			return sdf.format(xmlGregCal.toGregorianCalendar().getTime());
		}
		catch(Exception exe)
		{
			return "";
		}
		
		
		
		
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
