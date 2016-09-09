package org.fsl.converter;

import java.io.Serializable;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import org.fsl.application.ApplicationProperties;

import fsl.ta.toms.roms.bo.ArteryBO;
import fsl.ta.toms.roms.webservices.RoadOperation;



public class ArteryConverter  implements Converter, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8801098999236615233L;

List<ArteryBO> arteryList;
	
	public ArteryConverter(){
		super();
		this.getAllArtery();
	}

	private void getAllArtery()
	{
		
		RoadOperation roadOpWS = new RoadOperation();
		
		
		try 
		{
			this.arteryList = roadOpWS.findArteryList();
			
			
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
		 
	}
	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) throws ConverterException 
	{
		ArteryBO foundArtery = new ArteryBO();
		String arteryId="";
		
		if(arteryList!=null){
			for(ArteryBO artery : arteryList)
			{
				arteryId=artery.getArteryId()+"";
				
				if(arteryId.trim().compareToIgnoreCase(value.trim()) == 0){
					foundArtery = artery;
					
					continue;
				}
			}
		}
		return foundArtery;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) throws ConverterException 
	{
		
		return ((ArteryBO)value).getArteryId()+"";
	}

}
