package org.fsl.converter;

import java.io.Serializable;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import fsl.ta.toms.roms.bo.BIMSStaffViewBO;
import fsl.ta.toms.roms.webservices.StaffUserMapping;

public class BIMSStaffConverter  implements Converter, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5042313221354675248L;

	List<BIMSStaffViewBO> bimsStaffList;
	
	
	
	public BIMSStaffConverter() {
		super();
		this.getAllStaff();
	}



	private void getAllStaff()
	{
		StaffUserMapping staffUserMappingService = new StaffUserMapping();
		
		
		try 
		{
			this.bimsStaffList = staffUserMappingService.lookupAllStaff();
			
			
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
		 
	}
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) throws ConverterException 
	{
		BIMSStaffViewBO foundStaff = new BIMSStaffViewBO();
		
		
		if(bimsStaffList!=null){
			for(BIMSStaffViewBO staff : bimsStaffList)
			{
				if(staff.getStaffId().trim().compareToIgnoreCase(value.trim()) == 0)
				{
					foundStaff = staff;
					
					continue;
				}
			}
		}
		return foundStaff;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) throws ConverterException 
	{
		
		return ((BIMSStaffViewBO)value).getStaffId();
	}
}
