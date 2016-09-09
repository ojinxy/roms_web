package org.fsl.roms.businessobject;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;

import fsl.ta.toms.roms.bo.ArteryBO;
/**
 * 
 * @author jreid
 * Created Date: Jun 4, 2013
 */
public class RoadOpStartDetailsBO implements Serializable{

		/**
	 * 
	 */
	private static final long serialVersionUID = -2397055159340244785L;
	private String arteryDescription;
	private Integer arteryId;
	private String currentUsername;
	private Float latitude;
	private String locationDescription;
	private Integer locationId;
	private Float longitude;
	private String shortDescription;
	private String statusDescription;
	private  String statusId;
	private String selected;
	
	public RoadOpStartDetailsBO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public RoadOpStartDetailsBO( ArteryBO artery) {
		super();
		BeanUtils.copyProperties(artery, this);
		
	}
	
	public RoadOpStartDetailsBO setArteryBOFromWS(ArteryBO artery){
		
		RoadOpStartDetailsBO arteryW = new RoadOpStartDetailsBO();
		BeanUtils.copyProperties(artery, arteryW);
		return arteryW;
	}
	
	public String getArteryDescription() {
		return arteryDescription;
	}

	public Integer getArteryId() {
		return arteryId;
	}

	public String getCurrentUsername() {
		return currentUsername;
	}

	public Float getLatitude() {
		return latitude;
	}

	public String getLocationDescription() {
		return locationDescription;
	}

	public Integer getLocationId() {
		return locationId;
	}

	public Float getLongitude() {
		return longitude;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public String getStatusDescription() {
		return statusDescription;
	}

	public String getStatusId() {
		return statusId;
	}

	public void setArteryDescription(String arteryDescription) {
		if(StringUtils.isNotBlank(arteryDescription))
			this.arteryDescription = arteryDescription.trim();
	}

	public void setArteryId(Integer arteryId) {
		this.arteryId = arteryId;
	}

	public void setCurrentUsername(String currentUsername) {
		this.currentUsername = currentUsername;
	}

	public void setLatitude(Float latitude) {
		this.latitude = latitude;
	}

	public void setLocationDescription(String locationDescription) {
		if(StringUtils.isNotBlank(locationDescription))
			this.locationDescription = locationDescription.trim();
	}

	public void setLocationId(Integer locationId) {
		this.locationId = locationId;
	}

	public void setLongitude(Float longitude) {
		this.longitude = longitude;
	}

	public void setShortDescription(String shortDescription) {
		if(StringUtils.isNotBlank(shortDescription))
			this.shortDescription = shortDescription.trim();
	}

	public void setStatusDescription(String statusDescription) {
		if(StringUtils.isNotBlank(statusDescription))
			this.statusDescription = statusDescription.trim();
	}

	public void setStatusId(String statusId) {
		if(StringUtils.isNotBlank(statusId))
			this.statusId = statusId.trim();
	}

	public String getSelected() {
		return selected;
	}

	public void setSelected(String selected) {
		this.selected = selected;
	}
	
	

}
