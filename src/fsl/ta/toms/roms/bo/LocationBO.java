package fsl.ta.toms.roms.bo;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

import fsl.ta.toms.roms.dataobjects.LocationDO;
/**
 * 
 * @author jreid
 * Created Date: May 28, 2013
 */
public class LocationBO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3645882642921136335L;
	private String currentUsername;
	private String locationDescription;
	private Integer locationId;
	private String parishCode;
	private String parishDescription;
	private String shortDesc;
	private String statusDescription;	
	private String statusId;
	AuditEntryBO auditEntryBO;
	
	boolean selected;
	
	public LocationBO() {
		super();
	}

	
	/**
	 * Constructor that converts DO to BO
	 * @param locationDO
	 */
	public LocationBO(LocationDO locationDO) {
	
		this.locationDescription = locationDO.getDescription();
		this.locationId = locationDO.getLocationId();
		
		if(locationDO.getParish() != null) {
			this.parishCode = locationDO.getParish().getParishCode();
			this.parishDescription = locationDO.getParish().getDescription();		
		}
		
		if(locationDO.getStatus() != null) {
			this.statusDescription = locationDO.getStatus().getDescription();
			this.statusId = locationDO.getStatus().getStatusId();
		}
		
		this.shortDesc = locationDO.getShortDesc();
		
		if(locationDO.getAuditEntry()!=null){
			this.auditEntryBO = new AuditEntryBO(locationDO.getAuditEntry());
		}
		
	}

	public String getCurrentUsername() {
		return currentUsername;
	}

	public String getLocationDescription() {
		return locationDescription;
	}

	public Integer getLocationId() {
		return locationId;
	}

	public String getParishCode() {
		return parishCode;
	}

	public String getParishDescription() {
		return parishDescription;
	}

	public String getShortDesc() {
		return shortDesc;
	}

	public String getStatusDescription() {
		return statusDescription;
	}

	public String getStatusId() {
		return statusId;
	}

	public void setCurrentUsername(String currentUsername) {
		this.currentUsername = currentUsername;
	}

	public void setLocationDescription(String locationDescription) {
		if(StringUtils.isNotBlank(locationDescription))
			this.locationDescription = locationDescription.trim();
	}

	public void setLocationId(Integer locationId) {
		this.locationId = locationId;
	}

	public void setParishCode(String parishCode) {
		if(StringUtils.isNotBlank(parishCode))
			this.parishCode = parishCode.trim();
	}

	public void setParishDescription(String parishDescription) {
		if(StringUtils.isNotBlank(parishDescription))
			this.parishDescription = parishDescription.trim();
	}

	public void setShortDesc(String shortDesc) {
		if(StringUtils.isNotBlank(shortDesc))
			this.shortDesc = shortDesc.trim();
	}

	public void setStatusDescription(String statusDescription) {
		if(StringUtils.isNotBlank(statusDescription))
			this.statusDescription = statusDescription.trim();
	}

	public void setStatusId(String statusId) {
		if(StringUtils.isNotBlank(statusId))
			this.statusId = statusId.trim();
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


	public boolean isSelected() {
		return selected;
	}


	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	
}
