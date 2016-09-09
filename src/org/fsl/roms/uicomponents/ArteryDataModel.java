package org.fsl.roms.uicomponents;

import java.io.Serializable;
import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

import fsl.ta.toms.roms.bo.ArteryBO;

//import fsl.ta.toms.roms.webservices.maintenance.ArteryBO;
  
/**
 * 
 * @author rbrooks
 *
 */
public class ArteryDataModel extends ListDataModel<ArteryBO> implements Serializable, SelectableDataModel<ArteryBO> {    
  
    /**
	 * 
	 */
	private static final long serialVersionUID = -3031428316129705906L;

	public ArteryDataModel() {  
    }  
  
    public ArteryDataModel(List<ArteryBO> data) {  
        super(data);  
    }  
      
    @Override  
    public ArteryBO getRowData(String rowKey) {  
          
          
        List<ArteryBO> parishes = (List<ArteryBO>)getWrappedData();  
          
        for(ArteryBO parish : parishes) {  
            if(parish.getArteryId().equals(rowKey))
        	    return parish;  
        }  
          
        return null;  
    }  
  
    @Override  
    public Object getRowKey(ArteryBO par) {  
        return par.getArteryId();  
    }  
}  
                      