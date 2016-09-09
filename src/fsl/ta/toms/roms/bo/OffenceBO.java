package fsl.ta.toms.roms.bo;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

import fsl.ta.toms.roms.dataobjects.OffenceDO;

public class OffenceBO implements Serializable{

/**
	 * 
	 */
	private static final long serialVersionUID = 5045867336825581447L;

	private Integer offenceId;
	private String roadCheckTypeId;
	private String roadCheckTypeDescription;
	private String offenceDescription;
	private String shortDescription;
	private String statusId;
	private String statusDescription;
	private String excerpt;
	private String currentUsername;
	private AuditEntryBO auditEntryBO;
	
	public OffenceBO() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	
	
	public OffenceBO(OffenceDO offenceDO) {
		this.offenceId = offenceDO.getOffenceId();
		
		if(offenceDO.getRoadCheckType()!=null){
			this.roadCheckTypeId = offenceDO.getRoadCheckType().getRoadCheckTypeId();
			this.roadCheckTypeDescription = offenceDO.getRoadCheckType().getDescription();
		}

		this.offenceDescription = offenceDO.getDescription();
		this.shortDescription = offenceDO.getShortDescription();
		
		if(offenceDO.getStatus()!=null){
			this.statusId = offenceDO.getStatus().getStatusId();
			this.statusDescription = offenceDO.getStatus().getDescription();
		}
	
		this.excerpt = offenceDO.getExcerpt();
		
		if(offenceDO.getAuditEntry()!=null){
			this.auditEntryBO = new AuditEntryBO(offenceDO.getAuditEntry());
		}
	
	}




	public Integer getOffenceId() {
		return offenceId;
	}

	public void setOffenceId(Integer offenceId) {
		this.offenceId = offenceId;
	}

	public String getRoadCheckTypeId() {
		return roadCheckTypeId;
	}

	public void setRoadCheckTypeId(String roadCheckTypeId) {
		if(StringUtils.isNotBlank(roadCheckTypeId))
			this.roadCheckTypeId = roadCheckTypeId.trim();
	}

	public String getRoadCheckTypeDescription() {
		return roadCheckTypeDescription;
	}

	public void setRoadCheckTypeDescription(String roadCheckTypeDescription) {
		if(StringUtils.isNotBlank(roadCheckTypeDescription))
			this.roadCheckTypeDescription = roadCheckTypeDescription.trim();
	}

	public String getOffenceDescription() {
		return offenceDescription;
	}

	public void setOffenceDescription(String offenceDescription) {
		if(StringUtils.isNotBlank(offenceDescription))
			this.offenceDescription = offenceDescription.trim();
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		if(StringUtils.isNotBlank(shortDescription))
			this.shortDescription = shortDescription.trim();
	}

	public String getStatusId() {
		return statusId;
	}

	public void setStatusId(String statusId) {
		if(StringUtils.isNotBlank(statusId))
			this.statusId = statusId.trim();
	}

	public String getStatusDescription() {
		return statusDescription;
	}

	public void setStatusDescription(String statusDescription) {
		if(StringUtils.isNotBlank(statusDescription))
			this.statusDescription = statusDescription.trim();
	}

	public String getExcerpt() {
		return excerpt;
	}

	public void setExcerpt(String excerpt) {
		if(StringUtils.isNotBlank(excerpt))
			this.excerpt = excerpt.trim();
	}

	public String getCurrentUsername() {
		return currentUsername;
	}

	public void setCurrentUsername(String currentUsername) {
		if(StringUtils.isNotBlank(currentUsername))
			this.currentUsername = currentUsername.trim();
	}




	public AuditEntryBO getAuditEntryBO() {
		return auditEntryBO;
	}




	public void setAuditEntryBO(AuditEntryBO auditEntryBO) {
		this.auditEntryBO = auditEntryBO;
	}
	

}
