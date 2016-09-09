package org.fsl.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import org.primefaces.component.picklist.PickList;
import org.primefaces.model.DualListModel;
import fsl.ta.toms.roms.bo.TAVehicleBO;



@FacesConverter(value = "vehiclePickListConverter")
public class VehiclePicklistConverter implements Converter {

	
		
	 @Override
     public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
         Object ret = null;
         
         if (arg1 instanceof PickList) {
       
             Object dualList = ((PickList) arg1).getValue();
             DualListModel dl = (DualListModel) dualList;
             
             for (Object o : dl.getSource()) 
             {
                 String id = "";                        
   
                
                 id += ((TAVehicleBO) o).getTaVehicleId();
                	 
            	if(id.equalsIgnoreCase(arg2)){
            		ret =  ((TAVehicleBO) o);
            		break;
            	}
                  
                 
                 

             }
             
             if(ret == null){
            	  for (Object o : dl.getTarget()) {
            		  String id = "";
            		 
                       id += ((TAVehicleBO) o).getTaVehicleId();
                     	 
                     	if(id.equalsIgnoreCase(arg2)){
                     		ret =  ((TAVehicleBO) o);
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
    		
             str = "" + ((TAVehicleBO) arg2).getTaVehicleId();
       	 
         }
         
         return str;
     }
   }
