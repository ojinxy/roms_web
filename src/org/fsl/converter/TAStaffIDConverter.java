package org.fsl.converter;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
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
import org.fsl.application.ApplicationProperties;
import org.fsl.roms.util.DateUtils;

import fsl.ta.toms.roms.bo.StaffUserMappingBO;
import fsl.ta.toms.roms.search.criteria.impl.StaffUserMappingCriteriaBO;
import fsl.ta.toms.roms.webservices.StaffUserMapping;



/**
 * 
 * @author oanguin
 * 
 */
public class TAStaffIDConverter implements Converter, Serializable
{
	
	List<StaffUserMappingBO> staffUserMaps;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public TAStaffIDConverter() 
	{
		super();
	
		this.getAllMappedStaff();
	}

	private void getAllMappedStaff()
	{
		StaffUserMapping staffUserMappingService = new StaffUserMapping();
		
		StaffUserMappingCriteriaBO criteria = new StaffUserMappingCriteriaBO();
		
		
		try 
		{
			this.staffUserMaps = staffUserMappingService.lookupStaffUserMappings(criteria);
			
			
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
		 
	}

	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) throws ConverterException 
	{
		StaffUserMappingBO foundUserMapping = new StaffUserMappingBO();
		
		if(staffUserMaps!=null){
			for(StaffUserMappingBO staffMapping : staffUserMaps)
			{
				if(staffMapping.getStaffId().trim().compareToIgnoreCase(value.trim()) == 0)
				{
					foundUserMapping = staffMapping;
					
					continue;
				}
			}
		}
		return foundUserMapping;
	}


	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) throws ConverterException 
	{
		
		return ((StaffUserMappingBO)value).getStaffId();
	}
	
	


}
