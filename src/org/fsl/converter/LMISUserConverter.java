package org.fsl.converter;

import java.io.Serializable;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import org.fsl.application.ApplicationProperties;

import fsl.ta.toms.roms.bo.LMISUserViewBO;
import fsl.ta.toms.roms.webservices.StaffUserMapping;

public class LMISUserConverter implements Converter, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7128770218479314978L;

	List<LMISUserViewBO> userList;
	
	public LMISUserConverter(){
		super();
		this.getAllLMISUser();
	}
	
	private void getAllLMISUser()
	{
		StaffUserMapping staffUserMappingService = new StaffUserMapping();
		
		try 
		{
			this.userList = staffUserMappingService.lookupAllUsers();
			
			
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
		 
	}
	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) throws ConverterException 
	{
		LMISUserViewBO foundUser = new LMISUserViewBO();
		
		for(LMISUserViewBO user : userList)
		{
			if(user.getUsername().trim().compareToIgnoreCase(value.trim()) == 0)
			{
				foundUser = user;
				
				continue;
			}
		}
		return foundUser;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) throws ConverterException 
	{
		
		return ((LMISUserViewBO)value).getUsername();
	}

}
