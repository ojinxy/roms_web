package org.fsl.converter;

import javax.faces.component.UIComponent;

import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import org.fsl.roms.service.action.RoadOperationsServiceAction;
import org.fsl.roms.view.RoadOperationView;
import org.primefaces.component.picklist.PickList;
import org.primefaces.model.DualListModel;
import org.springframework.webflow.execution.RequestContext;

import fsl.ta.toms.roms.bo.PoliceOfficerBO;


@FacesConverter(value = "policePickListConverter")
public class PolicePicklistConverter implements Converter {

	
		
	 @Override
     public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
         Object ret = null;
         
         if (arg1 instanceof PickList) {
       
             Object dualList = ((PickList) arg1).getValue();
             DualListModel dl = (DualListModel) dualList;
             
             for (Object o : dl.getSource()) 
             {
                 String id = "";                        
   
                
                 id += ((PoliceOfficerBO) o).getPolOfficerCompNo();
                	 
            	if(id.equalsIgnoreCase(arg2)){
            		ret =  ((PoliceOfficerBO) o);
            		break;
            	}
                  
                 
                 

             }
             
             if(ret == null){
            	  for (Object o : dl.getTarget()) {
            		  String id = "";
            		 
                       id += ((PoliceOfficerBO) o).getPolOfficerCompNo();
                     	 
                     	if(id.equalsIgnoreCase(arg2)){
                     		ret =  ((PoliceOfficerBO) o);
                     		break;
                     	}
                       
            	  }
             }
         }
         
         
         return ret;
     }

     @Override
     public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
         String str = "";
         
         if (arg2 == null || arg2.equals("")) {
        	 return "";
         }
         else{        	 
    		
             str = "" + ((PoliceOfficerBO) arg2).getPolOfficerCompNo();
       	 
         }
         
         return str;
     }
   }
