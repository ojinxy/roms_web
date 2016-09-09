package fsl.ta.toms.roms.search.criteria.impl;

import org.apache.commons.lang.StringUtils;

import fsl.ta.toms.roms.search.criteria.SearchCriteria;
/**
 * 
 * @author jreid
 * Created Date: Jun 4, 2013
 */
public class PoundCriteriaBO implements SearchCriteria {

	
	private static final long serialVersionUID = -5062845026703409921L;

	private String currentUsername;

	Integer numberOfLots;

	Integer numberOfParkingSpaces;

	String parishCode;

	Integer poundId;

	String poundName;

	String shortDesc;

	String statusId;

	

	public PoundCriteriaBO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getCurrentUsername() {
		return currentUsername;
	}

	public Integer getNumberOfLots() {
		return numberOfLots;
	}

	public Integer getNumberOfParkingSpaces() {
		return numberOfParkingSpaces;
	}

	public String getParishCode() {
		return parishCode;
	}

	public Integer getPoundId() {
		return poundId;
	}

	public String getPoundName() {
		return poundName;
	}

	public String getShortDesc() {
		return shortDesc;
	}

	public String getStatusId() {
		return statusId;
	}

	public void setCurrentUsername(String currentUsername) {
		this.currentUsername = currentUsername;
	}

	public void setNumberOfLots(Integer numberOfLots) {
		this.numberOfLots = numberOfLots;
	}

	public void setNumberOfParkingSpaces(Integer numberOfParkingSpaces) {
		this.numberOfParkingSpaces = numberOfParkingSpaces;
	}

	public void setParishCode(String parishCode) {
		this.parishCode = parishCode;
	}

	public void setPoundId(Integer poundId) {
		this.poundId = poundId;
	}

	public void setPoundName(String poundName) {
		if(StringUtils.isNotBlank(poundName))
			this.poundName = poundName.trim();
	}

	public void setShortDesc(String shortDesc) {
		if(StringUtils.isNotBlank(shortDesc))
			this.shortDesc = shortDesc;
	}

	public void setStatusId(String statusId) {
		this.statusId = statusId;
	}

	
	
}
