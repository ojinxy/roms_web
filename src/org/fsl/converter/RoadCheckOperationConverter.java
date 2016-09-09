package org.fsl.converter;

import java.io.Serializable;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.servlet.ServletContext;
import org.fsl.application.ApplicationProperties;
import org.fsl.roms.constants.Constants;
import org.fsl.ta.toms.roms.beans.ApplicationRunTimeStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import fsl.ta.toms.roms.bo.RoadOperationBO;
import fsl.ta.toms.roms.exception.NoRecordFoundException;
import fsl.ta.toms.roms.webservices.RoadOperation;

@Component
@Scope("request")
public class RoadCheckOperationConverter implements Converter, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3409504971783609052L;

	private org.springframework.context.ApplicationContext appContext; 
	
	List<RoadOperationBO> roadOperationBOList;
	
	@Autowired
	ApplicationRunTimeStorage applicationRunTimeStorage;
	

	public RoadCheckOperationConverter(){
		super();
		setUpCache();
		this.getRoadCheckOperationList();
	
	}
	

	public void getRoadCheckOperationList(){
		
		RoadOperation roadOpService = new RoadOperation();

		try {
			//this.roadOperationBOList = (List<RoadOperationBO>) ExternalContextHolder.getExternalContext().getSessionMap().get(Constants.ROAD_OPERATION_LIST);
			this.roadOperationBOList = (List<RoadOperationBO>) applicationRunTimeStorage.getValues().get(Constants.ROAD_OPERATION_LIST);
			
			if(this.roadOperationBOList == null || this.roadOperationBOList.size() < 1){
				this.roadOperationBOList = roadOpService.lookupAllRoadOperationForRoadCheck();
				//ExternalContextHolder.getExternalContext().getSessionMap().put(Constants.ROAD_OPERATION_LIST,this.roadOperationBOList);
				applicationRunTimeStorage.getValues().put(Constants.ROAD_OPERATION_LIST,this.roadOperationBOList);
			}

		} catch (NoRecordFoundException e) {
			
			e.printStackTrace();
		}
	}
	
	/*This function is used due to issues with JSF and Spring Autowired values being null.
	 * Created By: oanguin
	 * Date: 26 May 2016*/
	private void setUpCache(){

		ServletContext servletContext = (ServletContext) FacesContext
			    .getCurrentInstance().getExternalContext().getContext();

        		
        WebApplicationContext applicationContext = 
                WebApplicationContextUtils.
                      getWebApplicationContext(servletContext);
        
        applicationRunTimeStorage = (ApplicationRunTimeStorage) applicationContext.getBean("applicationRunTimeStorage");

	}
	
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) throws ConverterException 
	{
		
		RoadOperationBO foundRoadOperation = new RoadOperationBO();
		String roadOperationId="";
		
		if(roadOperationBOList!=null){
			for(RoadOperationBO roadOperation : roadOperationBOList)
			{
				roadOperationId=roadOperation.getRoadOperationId()+"";
						
				if(roadOperationId.trim().compareToIgnoreCase(value.trim()) == 0){
					foundRoadOperation = roadOperation;
					
					continue;
				}
			
			
			}
		}
		
		return foundRoadOperation;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) throws ConverterException 
	{
		return ((RoadOperationBO)value).getRoadOperationId()+ "";
	}


}
