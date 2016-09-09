package org.fsl.roms.view;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import fsl.ta.toms.roms.bo.LocationBO;
import fsl.ta.toms.roms.bo.ParishBO;

public class ParishView implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1632285331815873468L;
	String description;
	String officeLocationCode;
	String officeLocationDescription;
	String parishCode;
	String statusDescription;
	String statusId;
	HashMap<Integer,LocationBO> listOfLocations;
	boolean selected;
	
	private List<ParishBO> parishList;
	private ParishBO selectedParish;	
	
	
	public ParishView() {
		super();
		parishList = new ArrayList<ParishBO>();
		selectedParish = new ParishBO();
	}
	
	public ParishView(List<ParishBO> var)
	{
		parishList = var;
		selectedParish = new ParishBO();
	}




	public String getDescription() {
		return description;
	}




	public void setDescription(String description) {
		this.description = description;
	}




	public String getOfficeLocationCode() {
		return officeLocationCode;
	}




	public void setOfficeLocationCode(String officeLocationCode) {
		this.officeLocationCode = officeLocationCode;
	}




	public String getOfficeLocationDescription() {
		return officeLocationDescription;
	}




	public void setOfficeLocationDescription(String officeLocationDescription) {
		this.officeLocationDescription = officeLocationDescription;
	}




	public String getParishCode() {
		return parishCode;
	}




	public void setParishCode(String parishCode) {
		this.parishCode = parishCode;
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




	public HashMap<Integer, LocationBO> getListOfLocations() {
		return listOfLocations;
	}




	public void setListOfLocations(HashMap<Integer, LocationBO> listOfLocations) {
		this.listOfLocations = listOfLocations;
	}




	public boolean isSelected() {
		return selected;
	}




	public void setSelected(boolean selected) {
		this.selected = selected;
	}




	public List<ParishBO> getParishList() {
		return parishList;
	}




	public void setParishList(List<ParishBO> parishList) {
		this.parishList = parishList;
	}




	public ParishBO getSelectedParish() {
		return selectedParish;
	}




	public void setSelectedParish(ParishBO selectedParish) {
		this.selectedParish = selectedParish;
	}

	
}