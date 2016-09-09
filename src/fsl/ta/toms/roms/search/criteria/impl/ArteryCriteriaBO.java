package fsl.ta.toms.roms.search.criteria.impl;

import java.util.Date;

import org.apache.commons.lang.StringUtils;

import fsl.ta.toms.roms.search.criteria.SearchCriteria;

/**
 * 
 * @author jreid
 * Created Date: Jun 4, 2013
 */
public class ArteryCriteriaBO implements SearchCriteria {

	
	private static final long serialVersionUID = 7355999678474582943L;

	private String arteryDescription;
	private Integer arteryId;
	private Integer locationId;
	private String shortDescription;
	private  String statusId;
	private String parishCode;
	
	private String points;
	
	private Float startlongitude;
	
	private Float startlatitude;
	
	private Float endlongitude;
	
	private Float endlatitude;
	
	private Float distance;
	
	
	public ArteryCriteriaBO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getArteryDescription() {
		return arteryDescription;
	}

	public Integer getArteryId() {
		return arteryId;
	}

	public Integer getLocationId() {
		return locationId;
	}

	public String getShortDescription() {
		return shortDescription;
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

	public void setLocationId(Integer locationId) {
		this.locationId = locationId;
	}
		
	public void setShortDescription(String shortDescription) {
		if(StringUtils.isNotBlank(shortDescription))
			this.shortDescription = shortDescription.trim();
	}

	public void setStatusId(String statusId) {
		if(StringUtils.isNotBlank(statusId))
			this.statusId = statusId.trim();
	}

	public String getPoints() {
		return points;
	}

	public void setPoints(String points) {
		this.points = points;
	}

	public Float getStartlongitude() {
		return startlongitude;
	}

	public void setStartlongitude(Float startlongitude) {
		this.startlongitude = startlongitude;
	}

	public Float getStartlatitude() {
		return startlatitude;
	}

	public void setStartlatitude(Float startlatitude) {
		this.startlatitude = startlatitude;
	}

	public Float getEndlongitude() {
		return endlongitude;
	}

	public void setEndlongitude(Float endlongitude) {
		this.endlongitude = endlongitude;
	}

	public Float getEndlatitude() {
		return endlatitude;
	}

	public void setEndlatitude(Float endlatitude) {
		this.endlatitude = endlatitude;
	}

	public Float getDistance() {
		return distance;
	}

	public void setDistance(Float distance) {
		this.distance = distance;
	}

	public String getParishCode() {
		return parishCode;
	}

	public void setParishCode(String parishCode) {
		this.parishCode = parishCode;
	}

		

}
