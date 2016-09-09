package org.fsl.roms.businessobject;

import java.io.Serializable;

public class RegionBO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1856031551994272847L;
	
	private String id;
	private String description;
	
	
	
	public RegionBO() {
		super();
		// TODO Auto-generated constructor stub
	}



	public String getId() {
		return id;
	}



	public void setId(String id) {
		this.id = id;
	}



	public String getDescription() {
		return description;
	}



	public void setDescription(String description) {
		this.description = description;
	}



	
	

}