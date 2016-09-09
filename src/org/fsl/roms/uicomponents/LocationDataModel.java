package org.fsl.roms.uicomponents;

import java.io.Serializable;
import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

import fsl.ta.toms.roms.bo.LocationBO;
  
/**
 * 
 * @author rbrooks
 *
 */
public class LocationDataModel extends ListDataModel<LocationBO> implements Serializable, SelectableDataModel<LocationBO> {    
  
    /**
	 * 
	 */
	private static final long serialVersionUID = -3031428316129705906L;

    public LocationDataModel(List<LocationBO> data) {  
        super(data);  
    }  
    
    
      
    public LocationDataModel() {
		super();
		// TODO Auto-generated constructor stub
	}



	@Override  
    public LocationBO getRowData(String rowKey) {  
          
          
        List<LocationBO> parishes = (List<LocationBO>)getWrappedData();  
          
        for(LocationBO parish : parishes) {  
            if(parish.getLocationId().equals(rowKey))
        	    return parish;  
        }  
          
        return null;  
    }  
  
    @Override  
    public Object getRowKey(LocationBO par) {  
        return par.getLocationId();  
    }  
}  
                      