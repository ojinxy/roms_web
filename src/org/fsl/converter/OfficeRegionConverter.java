package org.fsl.converter;

import java.util.HashMap;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import org.primefaces.component.selectmanymenu.SelectManyMenu;
import org.springframework.webflow.execution.RequestContext;
import org.springframework.webflow.execution.RequestContextHolder;
import org.fsl.roms.businessobject.RegionBO;

@FacesConverter(value = "regionConverter")
public class OfficeRegionConverter implements Converter
{

	private final static String REGIONS = "regions";
	
	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2)
			throws ConverterException
	{
		
		RequestContext context = RequestContextHolder.getRequestContext();
		
		HashMap<String,RegionBO> regions = (HashMap<String, RegionBO>) context.getViewScope().get(this.REGIONS);
		
		if(regions != null){
			return regions.get(arg2);
		}else{
			return null;
		}
		
		
		
	}

	@Override
	public String getAsString(FacesContext fContext, UIComponent component, Object object)
			throws ConverterException
	{
		
		RequestContext context = RequestContextHolder.getRequestContext();
		
		HashMap<String,RegionBO> regions = (HashMap<String, RegionBO>) context.getViewScope().get(this.REGIONS);
		
		if(regions == null){
			regions = new HashMap<String, RegionBO>();
			
		}
		
		if(object != null){
			RegionBO region = ((RegionBO)object);
			
			regions.put(region.getId(), region);
			
			context.getViewScope().put(this.REGIONS, regions);
			
			return (((RegionBO)object).getId());
			
		}else{
			return null;
		}
		
		
		
	}
	
}
