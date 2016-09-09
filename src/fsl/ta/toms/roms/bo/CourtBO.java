package fsl.ta.toms.roms.bo;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

import fsl.ta.toms.roms.dataobjects.CourtDO;
/**
 * 
 * @author jreid
 * Created Date: May 28, 2013
 */
public class CourtBO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8832000667269948805L;

	Integer courtId;

	String currentUsername;

	String description;
/*
	String locationDescription;

	Integer locationId;*/
	
	String markText;
	String streetNo;
	String streetName;
	String poLocationName;
	String poBoxNo;
	String parishCode;
	String parishName;
	String shortDescription;

	String statusDescription;

	String statusId;
	
	AuditEntryBO auditEntryBO;
	
	public CourtBO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CourtBO(CourtDO courtDO) {
		this.courtId = courtDO.getCourtId();
		this.description = courtDO.getDescription();
		
		
	/*	if(courtDO.getLocation() != null) {
			this.locationDescription= courtDO.getLocation().getDescription();
			this.locationId = courtDO.getLocation().getLocationId();
		
		}*/
		if(courtDO.getAddress() != null)
		{
			this.markText = courtDO.getAddress().getMarkText();
			this.streetNo = courtDO.getAddress().getStreetNo();
			this.streetName = courtDO.getAddress().getStreetName();
			this.poLocationName = courtDO.getAddress().getPoLocationName();
			this.poBoxNo = courtDO.getAddress().getPoBoxNo();
			if (courtDO.getAddress().getParish() != null) {
				this.parishCode = courtDO.getAddress().getParish().getParishCode();
				this.parishName = courtDO.getAddress().getParish().getDescription();
			}
			this.parishCode = courtDO.getAddress().getParish().getParishCode();
		}
		
		this.shortDescription = courtDO.getShortDesc();
		
		if(courtDO.getStatus() != null) {
			this.statusId = courtDO.getStatus().getStatusId();
			this.statusDescription = courtDO.getStatus().getDescription();
		
		}
		
		if(courtDO.getAuditEntry()!=null){
			this.auditEntryBO = new AuditEntryBO(courtDO.getAuditEntry());
		}
		
	}

	public Integer getCourtId() {
		return courtId;
	}

	public String getCurrentUsername() {
		return currentUsername;
	}

	public String getDescription() {
		return description;
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

	public void setCourtId(Integer courtId) {
		this.courtId = courtId;
	}

	public void setCurrentUsername(String currentUsername) {
		this.currentUsername = currentUsername;
	}

	public void setDescription(String description) {
		if(StringUtils.isNotBlank(description))
			this.description = description.trim();
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

	public String getParishCode() {
		return parishCode;
	}

	public void setParishCode(String parishCode) {
		this.parishCode = parishCode;
	}

	public String getParishName() {
		return parishName;
	}

	public void setParishName(String parishName) {
		this.parishName = parishName;
	}

	/**
	 * @return the auditEntryBO
	 */
	public AuditEntryBO getAuditEntryBO() {
		return auditEntryBO;
	}

	/**
	 * @param auditEntryBO the auditEntryBO to set
	 */
	public void setAuditEntryBO(AuditEntryBO auditEntryBO) {
		this.auditEntryBO = auditEntryBO;
	}

}
