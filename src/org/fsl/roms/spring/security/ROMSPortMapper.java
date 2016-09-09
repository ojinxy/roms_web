package org.fsl.roms.spring.security;

import java.util.HashMap;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.springframework.security.web.PortMapperImpl;

public class ROMSPortMapper extends PortMapperImpl {
	
	public ROMSPortMapper() {
		HashMap<String,String> portMapping = new HashMap<String,String>();
		
		String HTTP_PORT = "";
		String HTTPS_PORT = "";
		try{
		Context initialContext = new InitialContext();
		Context envcontext = (Context)initialContext.lookup("java:comp/env");
		HTTP_PORT =  (String)envcontext.lookup("HTTP_PORT");
		HTTPS_PORT =  (String)envcontext.lookup("HTTPS_PORT");
		
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		
		//portMapping.put("8080", "9443");
		portMapping.put(HTTP_PORT, HTTPS_PORT);
		
		//System.err.println("Using port mapping");
		
		setPortMappings(portMapping);
	}

}
