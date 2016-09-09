package org.fsl.roms.util;

import org.fsl.application.ApplicationProperties;


public class PropBean {

	public PropBean() {
		//System.err.println("Popbean initialize...");
	}
	
	public String  getVersion(){
		 return  ApplicationProperties.get("version");
	}
	
	public String getCopyright(){
		return  ApplicationProperties.get("copyright");
	}
	
	public String getYear(){
		return  ApplicationProperties.get("year");
	}
	
	
	
	
	
}

