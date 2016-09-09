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

import fsl.ta.toms.roms.bo.CourtBO;
import fsl.ta.toms.roms.bo.ExcerptParamMappingBO;
import fsl.ta.toms.roms.bo.GoverningLawBO;
import fsl.ta.toms.roms.bo.ITAExaminerBO;
import fsl.ta.toms.roms.bo.JPBO;
import fsl.ta.toms.roms.bo.PoliceOfficerBO;
import fsl.ta.toms.roms.bo.RefCodeBO;
import fsl.ta.toms.roms.bo.StrategyBO;
import fsl.ta.toms.roms.bo.TAStaffBO;
import fsl.ta.toms.roms.bo.TAVehicleBO;
import fsl.ta.toms.roms.bo.VehicleOwnerBO;


@FacesConverter(value = "pickListConverter")
public class PicklistConverter implements Converter {

	
		
	 @Override
     public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
         Object ret = null;
         
         if (arg1 instanceof PickList) {
       
             Object dualList = ((PickList) arg1).getValue();
             DualListModel dl = (DualListModel) dualList;
             
             for (Object o : dl.getSource()) 
             {
                 String id = "";
                 
              
                 //TA SATAFF
                 if (o instanceof TAStaffBO) {
                     id += ((TAStaffBO) o).getStaffId();
                     
                     if(id.equalsIgnoreCase(arg2)){
                 		ret =  ((TAStaffBO) o);
                 	}
                 }
              
                 
                 //Strategy
                 if (o instanceof StrategyBO) {
                	 id += ((StrategyBO) o).getStrategyId();
                    
                 }
                 
                 
                 //ITA Examiner
                 if (o instanceof ITAExaminerBO) {
                	 id += ((ITAExaminerBO) o).getExaminerId();
                	 
                	 if(id.equalsIgnoreCase(arg2)){
                  		ret =  ((ITAExaminerBO) o);
                  	}
                  }
                 
                 
                 //JP
                 if (o instanceof JPBO) {
                	 id += ((JPBO) o).getRegNumber();
                	 
                	 if(id.equalsIgnoreCase(arg2)){
                 		ret =  ((JPBO) o);
                 	}
                  }
                 
                 
                 //Police
                 if (o instanceof PoliceOfficerBO) {
                	 id += ((PoliceOfficerBO) o).getPolOfficerCompNo();
                	 
                	if(id.equalsIgnoreCase(arg2)){
                		ret =  ((PoliceOfficerBO) o);
                	}
                  }
                 
                 
                 //Vehicle
                 if (o instanceof TAVehicleBO) {
                	 id += ((TAVehicleBO) o).getTaVehicleId();
                	 
                	 if(id.equalsIgnoreCase(arg2)){
                 		ret =  ((TAVehicleBO) o);
                 	}
                  }
                 
                 //Court
                 if (o instanceof CourtBO) {
                	 id += ((CourtBO) o).getShortDescription();
                  }
                 
                 //Governing Law
                 if (o instanceof GoverningLawBO) {
                	 id += ((GoverningLawBO) o).getLawId();
                  }
                 
             	//Excerpt Offence Params
                 if (o instanceof ExcerptParamMappingBO) {
                	 id += ((ExcerptParamMappingBO) o).getParamMapId();
                  }
                 
               //Vehicle Owner 
                 if (o instanceof VehicleOwnerBO) {
                	 id += ((VehicleOwnerBO) o).getFirstName() +" "+((VehicleOwnerBO) o).getLastName();
                  }
                                 
               //RefCode
                 if (o instanceof RefCodeBO) {
                	 id += ((RefCodeBO) o).getAltId();
                  }
                 
                 if (arg2.equals(id)) {
                     ret = o;
                     break;
                 }
             }
             
             if(ret == null){
            	  for (Object o : dl.getTarget()) {
            		  String id = "";
            		  
            		  //Strategy
            		  if (o instanceof StrategyBO) {
                       	 id += ((StrategyBO) o).getStrategyId();
                       	 
                       	if(id.equalsIgnoreCase(arg2)){
                       		ret =  ((StrategyBO) o);
                       	}
                      }
            		 
            		//tastaff
                      if (o instanceof TAStaffBO) {
                      	 id += ((TAStaffBO) o).getStaffId();
                      	 
                      	if(id.equalsIgnoreCase(arg2)){
                      		ret =  ((TAStaffBO) o);
                      	}
                        }
                      
            		     //Police
                      if (o instanceof PoliceOfficerBO) {
                     	 id += ((PoliceOfficerBO) o).getPolOfficerCompNo();
                     	 
                     	if(id.equalsIgnoreCase(arg2)){
                     		ret =  ((PoliceOfficerBO) o);
                     	}
                       }
                      
                      //ita
                      if (o instanceof ITAExaminerBO) {
                      	 id += ((ITAExaminerBO) o).getExaminerId();
                      	 
                      	if(id.equalsIgnoreCase(arg2)){
                      		ret =  ((ITAExaminerBO) o);
                      	}
                        }
                      
                      
                    //vehicle
                      if (o instanceof TAVehicleBO) {
                      	 id += ((TAVehicleBO) o).getTaVehicleId();
                      	 
                      	if(id.equalsIgnoreCase(arg2)){
                      		ret =  ((TAVehicleBO) o);
                      	}
                        }
                      
                    //JP
                      if (o instanceof JPBO) {
                      	 id += ((JPBO) o).getRegNumber();
                      	 
                      	if(id.equalsIgnoreCase(arg2)){
                      		ret =  ((JPBO) o);
                      	}
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
         }else
         {
        	 
        	 //TA Staff
	         if (arg2 instanceof TAStaffBO) 
	         {
	        	 str = "" + ((TAStaffBO) arg2).getStaffId();
	        	 return str;
	         }
	         
	         //Strategy
	         if (arg2 instanceof StrategyBO) 
	         {
		         str = "" + ((StrategyBO) arg2).getStrategyId();
		         return str;
		         
		         
		         
	         }
     
	         //Ita Examiner
        	 if (arg2 instanceof ITAExaminerBO) 
        	 {
	             str = "" + ((ITAExaminerBO) arg2).getExaminerId();
	             return str;
        	 }
		      
    		 //JP
        	 if (arg2 instanceof JPBO) 
        	 {
	             str = "" + ((JPBO) arg2).getRegNumber();
	             return str;
        	 }
        	 
    		 //Police
        	 if (arg2 instanceof PoliceOfficerBO) 
        	 {
	             str = "" + ((PoliceOfficerBO) arg2).getPolOfficerCompNo();
	             return str;
        	 }
        	 
        	 //Vehicle
        	 if (arg2 instanceof TAVehicleBO) 
        	 {
	             str = "" + ((TAVehicleBO) arg2).getTaVehicleId();
	             return str;
        	 }
        	 
        	 //Court
        	 if (arg2 instanceof CourtBO) 
        	 {
	             str = "" + ((CourtBO) arg2).getShortDescription();
	             return str;
        	 }
        	 
        	 
        	 //GoverningLawBO
        	 if (arg2 instanceof GoverningLawBO) 
        	 {
	             str = "" + ((GoverningLawBO) arg2).getLawId();
	             return str;
        	 }
        	 
        	//ExcerptOffenceParamsBO
        	 if (arg2 instanceof ExcerptParamMappingBO) 
        	 {
	             str = "" + ((ExcerptParamMappingBO) arg2).getParamMapId();
	             return str;
        	 }
        	 
        	//Vehicle Owner 
        	 if (arg2 instanceof VehicleOwnerBO) 
        	 {
	             str = "" + ((VehicleOwnerBO) arg2).getFirstName() +" "+((VehicleOwnerBO) arg2).getLastName();
	             return str;
        	 }
        	 
        	 
        	//RefCode
        	 if (arg2 instanceof RefCodeBO) 
        	 {
	             str = "" + ((RefCodeBO) arg2).getAltId();
	             return str;
        	 }
        	 
        	//default
        	 str= (String)arg2;
        	 
         }
         
         return str;
     }
   }
