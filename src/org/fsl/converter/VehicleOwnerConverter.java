package org.fsl.converter;

import java.util.HashMap;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import org.apache.commons.lang.StringUtils;
import org.springframework.webflow.execution.RequestContext;
import org.springframework.webflow.execution.RequestContextHolder;
import fsl.ta.toms.roms.bo.VehicleOwnerBO;

@FacesConverter(value = "vehicleOwnerConverter")
public class VehicleOwnerConverter implements Converter
{
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component,String value)
	{
			
		RequestContext reqContext = RequestContextHolder.getRequestContext();
		
		HashMap<String, VehicleOwnerBO> vehMap = (HashMap<String, VehicleOwnerBO>)reqContext.getViewScope().get("ownerMap");
						
		if(StringUtils.trimToNull(value)!=null&&vehMap!=null){
			return vehMap.get(value);
		}
				
		return null;
				
	}
	
	@Override
	public String getAsString(FacesContext context, UIComponent component,Object value)
	{
				
		RequestContext reqContext = RequestContextHolder.getRequestContext();
		
		HashMap<String, VehicleOwnerBO> vehMap = (HashMap<String, VehicleOwnerBO>)reqContext.getViewScope().get("ownerMap");
		if(vehMap == null){
			vehMap = new HashMap<String, VehicleOwnerBO>();
		}		
		
		if(value !=null && !(value.toString()).isEmpty()){
	
			VehicleOwnerBO owner = (VehicleOwnerBO)value;
	
			vehMap.put(owner.getTrnNbr(), owner);
			
			reqContext.getViewScope().put("ownerMap", vehMap);
			
			return owner.getTrnNbr();
		}
		return "";
	}
	
	
}
