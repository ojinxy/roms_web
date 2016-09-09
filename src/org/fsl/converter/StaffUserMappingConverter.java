package org.fsl.converter;

import java.util.ArrayList;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import org.apache.commons.lang.StringUtils;
import org.primefaces.component.autocomplete.AutoComplete;
import org.primefaces.component.selectonemenu.SelectOneMenu;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SelectableDataModel;

import fsl.ta.toms.roms.bo.StaffUserMappingBO;

/**
 * 
 * @author jreid
 * 
 */

public class StaffUserMappingConverter implements Converter {

	/*public StaffUserMappingConverter() {
		super();
	}*/

	/*@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) throws ConverterException {
		System.err.println(" in converter 0 Value : " + value);
		
		if (StringUtils.isBlank(value.trim())) {
			
			return null;
		} else {
			
			Object dualList = ((AutoComplete) component).getValue();
			//ArrayList<StaffUserMappingBO> dl = (ArrayList<StaffUserMappingBO>) dualList;
			LazyDataModel<StaffUserMappingBO> dl = (LazyDataModel<StaffUserMappingBO>) dualList;
			//SelectableDataModel dl = (SelectableDataModel) dualList;
				System.err.println(" value " + dl);
			
				ArrayList<StaffUserMappingBO> al = (ArrayList<StaffUserMappingBO>) dl.getWrappedData();
			//	return dl.getRowData(value);
				
			for (StaffUserMappingBO o : al) {
				// TA SATAFF
				if (value.equals(((StaffUserMappingBO) o).getStaffId()))
					return o;
			}

			return null;
		}

	}*/

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object value) {
		System.err.println(" get as string called : value " + value);
		if (value == null || value.equals("")) {
			System.err.println(" here");
			return "";
		} else {
			System.err.println(" there");
			return  value.toString() ;
		}
	}

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2)
			throws ConverterException {
		System.err.println(" in converter 0 2 : " + arg2);
		System.err.println(" in converter 0 1 : " + arg1);
		System.err.println(" in converter 0 0 : " + arg0);
		if (StringUtils.isBlank(arg2.trim())) {
			
			return null;
		} else {
			
			Object dualList = ((AutoComplete) arg1).getValue();
			ArrayList<StaffUserMappingBO> dl = (ArrayList<StaffUserMappingBO>) dualList;
			//LazyDataModel<StaffUserMappingBO> dl = (LazyDataModel<StaffUserMappingBO>) dualList;
			//SelectableDataModel dl = (SelectableDataModel) dualList;
				System.err.println(" value " + dl);
			
				//ArrayList<StaffUserMappingBO> al = (ArrayList<StaffUserMappingBO>) dl.getWrappedData();
			//	return dl.getRowData(value);
				
			for (StaffUserMappingBO o : dl) {
				// TA SATAFF
				if (arg2.equals(((StaffUserMappingBO) o).getStaffId()))
					return o.getStaffId();
			}

			return null;
		}
	}

}
