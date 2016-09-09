/**
 * Created By: jreid
 * Date: Jan 28, 2014
 *
 */
package fsl.ta.toms.roms.util;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import fsl.ta.toms.roms.bo.DocumentViewBO;
import fsl.ta.toms.roms.bo.OffenceParamBO;

/**
 * @author jreid Created Date: Jan 28, 2014
 */
public class OffenceParamUtil {

	public static String fillOffenceExcerpt(String offenceExcerpt, List<OffenceParamBO> offenceParams, DocumentViewBO document){
		
		String summonsExcerpt = null;
		
		if (StringUtils.isNotBlank(offenceExcerpt) && offenceParams != null
				&& !offenceParams.isEmpty() && document != null) {
			
			for (OffenceParamBO key : offenceParams) {
				// replace the parameters with the data coming over
				 if(key.getParamName().equalsIgnoreCase("Artery")){
					 offenceExcerpt.replaceAll("[" + key + "]", document.getOffenceLocation());					
								
				 }                 
				 if(key.getParamName().equalsIgnoreCase("Directive_of_Inspector")){
					 //offenceExcerpt.replaceAll("[" + key + "]", document.get);			
								
				 } 
				 if(key.getParamName().equalsIgnoreCase("Drivers_Name")){
					 offenceExcerpt.replaceAll("[" + key + "]", document.getOffenderFullName());
						
								
				 }                     
				 if(key.getParamName().equalsIgnoreCase("Inspector_Threatened")){
					 //offenceExcerpt.replaceAll("[" + key + "]", document.get);													
				 }          
				 
				 if(key.getParamName().equalsIgnoreCase("Licence_Type")){
					 offenceExcerpt.replaceAll("[" + key + "]", document.getRoadLicence().getLicType());
						
								
				 }                       
				 if(key.getParamName().equalsIgnoreCase("Number_of_Passengers")){
					 //offenceExcerpt.replaceAll("[" + key + "]", document.getRoadLicence().get);													
				 }    
				 
				 if(key.getParamName().equalsIgnoreCase("Parish_of_Offence")){
					//offenceExcerpt.replaceAll("[" + key + "]", document.get);
						
								
				 }                        
				 if(key.getParamName().equalsIgnoreCase("Parish_of_Offender")){
					// offenceExcerpt.replaceAll("[" + key + "]", document.getRoadLicence().get);
						
								
				 }            
				 if(key.getParamName().equalsIgnoreCase("Plate_Number")){
					 offenceExcerpt.replaceAll("[" + key + "]", document.getVehicle().getPlateRegNo());
						
								
				 }                       
				 if(key.getParamName().equalsIgnoreCase("Route")){
					 offenceExcerpt.replaceAll("[" + key + "]", document.getRoadLicence().getRouteDesc());
						
								
				 }                                  
				 if(key.getParamName().equalsIgnoreCase("Route_End")){
					 //offenceExcerpt.replaceAll("[" + key + "]", document.getRoadLicence().get);
						
								
				 }           
				 if(key.getParamName().equalsIgnoreCase("Route_Start")){
					 //offenceExcerpt.replaceAll("[" + key + "]", document.get);		
								
				 } 
				 
				 if(key.getParamName().equalsIgnoreCase("Seating_capacity")){
					 //offenceExcerpt.replaceAll("[" + key + "]", document.getRoadLicence().get);
						
								
				 }              
				 if(key.getParamName().equalsIgnoreCase("Vehicle_Type")){
					 offenceExcerpt.replaceAll("[" + key + "]", document.getVehicle().getMakeDescription());											
				 }              			
			}
		}				
		return summonsExcerpt;		
	}

}
