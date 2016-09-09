package org.fsl.roms.uicomponents;

import java.io.Serializable;
import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

import fsl.ta.toms.roms.bo.DocumentViewBO;

//import fsl.ta.toms.roms.webservices.documentmanager.DocumentViewBO;
  
/**
 * 
 * @author jreid
 *
 */
public class DocumentDataModel extends ListDataModel<DocumentViewBO> implements Serializable, SelectableDataModel<DocumentViewBO> {    
  
    /**
	 * 
	 */
	private static final long serialVersionUID = -3031428316129705906L;

	public DocumentDataModel() {  
		super();
    }  
  
    public DocumentDataModel(List<DocumentViewBO> data) {  
        super(data);  
    }  
      
    @Override  
    public DocumentViewBO getRowData(String rowKey) {  
          
          
        List<DocumentViewBO> documents = (List<DocumentViewBO>)getWrappedData();  
          
        for(DocumentViewBO document : documents) {  
            if( document.getAutomaticSerialNo() != null && document.getAutomaticSerialNo().equals(rowKey))
        	    return document;  
        }    
          
        return new DocumentViewBO();  
    }  
  
    @Override  
    public Object getRowKey(DocumentViewBO doc) {  
        return doc.getAutomaticSerialNo();  
    }


}  
                      