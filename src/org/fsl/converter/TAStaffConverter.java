package org.fsl.converter;

import java.io.Serializable;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import fsl.ta.toms.roms.bo.TAStaffBO;
import fsl.ta.toms.roms.webservices.RoadOperation;


public class TAStaffConverter implements Converter, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 9101388845228519176L;
	
	List<TAStaffBO> staffList;
	
	public TAStaffConverter(){
		super();
		this.getAllStaff();
	}

	private void getAllStaff()
	{
		RoadOperation roadOpService = new RoadOperation();

		
		try 
		{
			this.staffList = roadOpService.getAllROMSStaff();
			
			
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
		 
	}
	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) throws ConverterException 
	{
		TAStaffBO foundStaff = new TAStaffBO();
		
		if(staffList!=null){
			for(TAStaffBO staff : staffList)
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
		
		return ((TAStaffBO)value).getStaffId();
	}

}
