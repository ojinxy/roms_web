package org.fsl.roms.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import fsl.ta.toms.roms.bo.LocationBO;


public class LocationBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2869504764735294488L;
	
	private List<LocationBO> locationList;
	private LocationBO selectedLocation;
	
	public LocationBean()
	{
		locationList = new ArrayList<LocationBO>();
		selectedLocation = new LocationBO();
	}
	
	public LocationBean(List<LocationBO> var)
	{
		locationList = var;
		selectedLocation = new LocationBO();
	}

	public List<LocationBO> getLocationList() {
		return locationList;
	}

	public void setLocationList(List<LocationBO> locationList) {
		this.locationList = locationList;
	}

	public LocationBO getSelectedLocation() {
		return selectedLocation;
	}

	public void setSelectedLocation(LocationBO selectedLocation) {
		this.selectedLocation = selectedLocation;
	}

	
}
