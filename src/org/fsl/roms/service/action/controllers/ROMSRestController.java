/**
 *  
 *  
 */
package org.fsl.roms.service.action.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;

import org.apache.commons.lang.StringUtils;
import org.fsl.application.ApplicationProperties;
import org.fsl.roms.jqueryobject.CalendarEvent;
import org.fsl.roms.service.action.BaseServiceAction;
import org.fsl.roms.service.action.DocumentManagerServiceAction;
import org.fsl.roms.service.action.RoadCheckServiceAction;
import org.fsl.roms.util.DateUtils;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import fsl.ta.toms.roms.bo.RefCodeBO;


/**
 * @author oanguin
 *
 */
@Controller()
@RequestMapping("/roms_rest")
public class ROMSRestController 
{

	@Autowired
	RoadCheckServiceAction roadCheckServiceAction;
	
	public ROMSRestController() 
	{
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	

	@RequestMapping(value="isMobileURL",method=RequestMethod.GET)
	public @ResponseBody JSONObject isMobileURL(@RequestParam(value="url",required=true) String url)
	{
		System.out.println("Testing URL");
		
		HashMap<String,String> filter = new HashMap<String, String>();
		
		filter.put("cfg_code", "mobile_url");
		
		List<RefCodeBO> refInfos = roadCheckServiceAction.getRefInfo("roms_configuration",filter );
		
		JSONObject obj = new JSONObject();
		
		
		
		if(refInfos.size() > 0)
		{
			String mobileUrl = refInfos.get(0).getAltId();
			
			if(! StringUtils.isEmpty(mobileUrl) && url.toString().toLowerCase().contains(mobileUrl.toLowerCase().trim()))
			{
				obj.put("isMobile", "true");
				
			}
			else
			{
				obj.put("isMobile", "false");
			}
			
			return obj;
			
		}
		else
		{
			return null;
		}
	}
	
	
	
	
}
