package fsl.ta.toms.roms.bo;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

import fsl.ta.toms.roms.dataobjects.GoverningLawDO;

/**
 * 
 * @author jreid Created Date: May 28, 2013
 */
public class GoverningLawBO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 593628627843819751L;

	String currentUsername;

	String description;

	Integer lawId;

	String shortDesc;

	String statusDescription;

	String statusId;
	
	AuditEntryBO auditEntryBO;

	public GoverningLawBO() {
		super();
	}

	/**
	 * @author jreid
	 * @param governingLawDO
	 * @return BO from DO
	 */
	public GoverningLawBO(GoverningLawDO governingLawDO) {
		this.description = governingLawDO.getDescription();
		this.lawId = governingLawDO.getLawId();
		this.shortDesc = governingLawDO.getShortDesc();

		if (governingLawDO.getStatus() != null) {
			this.statusId = governingLawDO.getStatus().getStatusId();
			this.statusDescription = governingLawDO.getStatus()
					.getDescription();
		}
		
		if(governingLawDO.getAuditEntry()!=null){
			this.auditEntryBO = new AuditEntryBO(governingLawDO.getAuditEntry());
		}
	}
	
	

	public String getCurrentUsername() {
		return currentUsername;
	}

	public String getDescription() {
		return description;
	}

	public Integer getLawId() {
		return lawId;
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

	public void setDescription(String description) {
		if(StringUtils.isNotBlank(description))
			this.description = description.trim();
	}

	public void setLawId(Integer lawId) {
		this.lawId = lawId;
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
			this.statusId = statusId;
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
