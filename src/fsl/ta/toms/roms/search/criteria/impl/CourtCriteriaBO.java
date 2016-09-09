package fsl.ta.toms.roms.search.criteria.impl;

import org.apache.commons.lang.StringUtils;

import fsl.ta.toms.roms.search.criteria.SearchCriteria;

/**
 * 
 * @author jreid
 * Created Date: May 24, 2013
 */
public class CourtCriteriaBO implements SearchCriteria  {

	private static final long serialVersionUID = 8832000667269948805L;

	Integer courtId;

	String description;

	String parishCode;
	
	String markText;
	String streetNo;
	String streetName;
	String poLocationName;
	String poBoxNo;
	String parishName;
	String shortDescription;
	
	String shortDesc;

	String statusId;

	public CourtCriteriaBO() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	public Integer getCourtId() {
		return courtId;
	}

	public String getDescription() {
		return description;
	}


	public String getShortDesc() {
		return shortDesc;
	}
	
	public String getStatusId() {
		return statusId;
	}

	public void setCourtId(Integer courtId) {
		this.courtId = courtId;
	}

	public void setDescription(String description) {
		if(StringUtils.isNotBlank(description))
			this.description = description.trim();
	}


	public void setShortDesc(String shortDesc) {
		if(StringUtils.isNotBlank(shortDesc))
			this.shortDesc = shortDesc.trim();
	}

	public void setStatusId(String statusId) {
		if(StringUtils.isNotBlank(statusId))
			this.statusId = statusId.trim();
	}


	public String getParishCode() {
		return parishCode;
	}


	public void setParishCode(String parishCode) {
		this.parishCode = parishCode;
	}


	public String getMarkText() {
		return markText;
	}


	public void setMarkText(String markText) {
		this.markText = markText;
	}


	public String getStreetNo() {
		return streetNo;
	}


	public void setStreetNo(String streetNo) {
		this.streetNo = streetNo;
	}


	public String getStreetName() {
		return streetName;
	}


	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}


	public String getPoLocationName() {
		return poLocationName;
	}


	public void setPoLocationName(String poLocationName) {
		this.poLocationName = poLocationName;
	}


	public String getPoBoxNo() {
		return poBoxNo;
	}


	public void setPoBoxNo(String poBoxNo) {
		this.poBoxNo = poBoxNo;
	}


	public String getParishName() {
		return parishName;
	}


	public void setParishName(String parishName) {
		this.parishName = parishName;
	}


	public String getShortDescription() {
		return shortDescription;
	}


	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	

	

}
