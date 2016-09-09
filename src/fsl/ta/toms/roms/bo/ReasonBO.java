package fsl.ta.toms.roms.bo;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

import fsl.ta.toms.roms.dataobjects.ReasonDO;

public class ReasonBO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1075236094930807543L;

	private Integer reasonId;
	private String reasonDescription;
	private String type;
	private String typeDescription;
	private String statusId;
	private String isVisible;
	private String statusDescription;
	private String currentUsername;
	private AuditEntryBO auditEntryBO;
	
	
	public ReasonBO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
	public ReasonBO(ReasonDO reasonDO) {
		this.reasonId = reasonDO.getReasonId();
		this.reasonDescription = reasonDO.getDescription();
		this.type = reasonDO.getReasonType().getReasonTypeCode();
		this.typeDescription = reasonDO.getReasonType().getDescription();
		if(reasonDO.getStatus()!=null){
			this.statusId = reasonDO.getStatus().getStatusId();
			this.statusDescription = reasonDO.getStatus().getDescription();
		}
		
		if(reasonDO.getAuditEntry()!=null){
			this.auditEntryBO = new AuditEntryBO(reasonDO.getAuditEntry());
		}

	}



	public Integer getReasonId() {
		return reasonId;
	}
	public void setReasonId(Integer reasonId) {
		this.reasonId = reasonId;
	}
	public String getReasonDescription() {
		return reasonDescription;
	}
	public void setReasonDescription(String reasonDescription) {
		if(StringUtils.isNotBlank(reasonDescription))
			this.reasonDescription = reasonDescription.trim();
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		if(StringUtils.isNotBlank(type))
			this.type = type.trim();
	}
	public String getStatusId() {
		return statusId;
	}
	public void setStatusId(String statusId) {
		if(StringUtils.isNotBlank(statusId))
			this.statusId = statusId.trim();
	}
	
	public String getIsVisible() {
		return isVisible;
	}

	public void setIsVisible(String isVisible) {
		this.isVisible = isVisible;
	}


	public String getStatusDescription() {
		return statusDescription;
	}
	public void setStatusDescription(String statusDescription) {
		if(StringUtils.isNotBlank(statusDescription))
			this.statusDescription = statusDescription.trim();
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



	public String getTypeDescription() {
		return typeDescription;
	}



	public void setTypeDescription(String typeDescription) {
		this.typeDescription = typeDescription;
	}
	
	
}
