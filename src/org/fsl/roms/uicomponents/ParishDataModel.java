package org.fsl.roms.uicomponents;

import java.io.Serializable;
import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

import fsl.ta.toms.roms.bo.ParishBO;
  
/**
 * 
 * @author rbrooks
 *
 */
public class ParishDataModel extends ListDataModel<ParishBO> implements Serializable, SelectableDataModel<ParishBO> {    
  
    /**
	 * 
	 */
	private static final long serialVersionUID = -3031428316129705906L;

	public ParishDataModel() {  
    }  
  
    public ParishDataModel(List<ParishBO> listParish) {  
        super(listParish);  
    }  
      
    @Override  
    public ParishBO getRowData(String rowKey) {  
       
          
        List<ParishBO> parishes = (List<ParishBO>)getWrappedData();  
          
        for(ParishBO parish : parishes) {  
            if(parish.getParishCode().equals(rowKey))
        	    return parish;  
        }  
          
        return null;  
    }  
  
    @Override  
    public Object getRowKey(ParishBO par) {  
        return par.getParishCode();  
    }  
}  
                      