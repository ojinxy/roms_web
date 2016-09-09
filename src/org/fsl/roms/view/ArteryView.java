package org.fsl.roms.view;

import java.io.Serializable;

public class ArteryView implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3066926477378982387L;
	private String arteryDescription;
	private Integer arteryId;
	private Float latitude;
	private String locationDescription;
	private Integer locationId;
	private Float longitude;
	private String shortDescription;
	private String statusDescription;
	private  String statusId;
	boolean selected;
	
	
	
	public ArteryView() {
		super();
		// TODO Auto-generated constructor stub
	}



	public String getArteryDescription() {
		return arteryDescription;
	}



	public void setArteryDescription(String arteryDescription) {
		this.arteryDescription = arteryDescription;
	}



	public Integer getArteryId() {
		return arteryId;
	}



	public void setArteryId(Integer arteryId) {
		this.arteryId = arteryId;
	}



	public Float getLatitude() {
		return latitude;
	}



	public void setLatitude(Float latitude) {
		this.latitude = latitude;
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



	public Float getLongitude() {
		return longitude;
	}



	public void setLongitude(Float longitude) {
		this.longitude = longitude;
	}



	public String getShortDescription() {
		return shortDescription;
	}



	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
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



	public boolean isSelected() {
		return selected;
	}



	public void setSelected(boolean selected) {
		this.selected = selected;
	}
		
	
}