package org.fsl.roms.view;

import java.io.Serializable;
import java.util.HashMap;

import fsl.ta.toms.roms.bo.ArteryBO;

public class LocationView implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7257686160790049803L;
	private String locationDescription;
	private Integer locationId;
	private String parishCode;
	private String parishDescription;
	private String shortDesc;
	private String statusDescription;	
	private String statusId;

	HashMap<Integer,ArteryBO> listOfArteries;
	boolean selected;
	
	
	
	
	public LocationView() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getLocationDescription() {
		return locationDescription;
	}
	public void setLocationDescription(String locationDescription) {
		this.locationDescription = locationDescription;
	}
	public Integer getLocationId() {
		return locationId;
	}
	public void setLocationId(Integer locationId) {
		this.locationId = locationId;
	}
	public String getParishCode() {
		return parishCode;
	}
	public void setParishCode(String parishCode) {
		this.parishCode = parishCode;
	}
	public String getParishDescription() {
		return parishDescription;
	}
	public void setParishDescription(String parishDescription) {
		this.parishDescription = parishDescription;
	}
	public String getShortDesc() {
		return shortDesc;
	}
	public void setShortDesc(String shortDesc) {
		this.shortDesc = shortDesc;
	}
	public String getStatusDescription() {
		return statusDescription;
	}
	public void setStatusDescription(String statusDescription) {
		this.statusDescription = statusDescription;
	}
	public String getStatusId() {
		return statusId;
	}
	public void setStatusId(String statusId) {
		this.statusId = statusId;
	}
	public HashMap<Integer, ArteryBO> getListOfArteries() {
		return listOfArteries;
	}
	public void setListOfArteries(HashMap<Integer, ArteryBO> listOfArteries) {
		this.listOfArteries = listOfArteries;
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	
}