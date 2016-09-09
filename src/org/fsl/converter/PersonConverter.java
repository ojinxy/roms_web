package org.fsl.converter;

import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import org.springframework.webflow.execution.RequestContext;
import org.springframework.webflow.execution.RequestContextHolder;


public class PersonConverter implements Converter
{
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component,String value) throws ConverterException
	{
		RequestContext flowContext = RequestContextHolder.getRequestContext();
		
		@SuppressWarnings("unchecked")
		List<PersonBOForRoadCompConverter> personList = (List<PersonBOForRoadCompConverter>) flowContext.getFlowScope().get("personList");
		
		PersonBOForRoadCompConverter foundPerson = new PersonBOForRoadCompConverter();
		
		for(PersonBOForRoadCompConverter person : personList)
		{
			if(person.getPersonId().toString().equalsIgnoreCase(value))
				foundPerson = person;
		}
		
		return foundPerson;
		
		
	}
	
	@Override
	public String getAsString(FacesContext context, UIComponent component,Object value) throws ConverterException
	{
		if(value != null)
		{
			PersonBOForRoadCompConverter person = (PersonBOForRoadCompConverter)value;
			
			if(person.getPersonId() != null && person.getPersonId() > 0)
				return String.valueOf(person.getPersonId());
			else
				return "";
		}
		else
			return "";
	}
	
	
}
