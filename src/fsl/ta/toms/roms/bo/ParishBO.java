package fsl.ta.toms.roms.bo;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

import fsl.ta.toms.roms.dataobjects.ParishDO;
/**
 * 
 * @author jreid
 * Created Date: May 28, 2013
 */
public class ParishBO implements Serializable {

	private static final long serialVersionUID = -8106577323109687964L;

	String currentUsername;
	String description;
	String officeLocationCode;
	String officeLocationDescription;
	String parishCode;
	String statusDescription;
	String statusId;
	
	AuditEntryBO auditEntryBO;
	
	boolean selected;
	
	public ParishBO() {
		super();
	}
	
	/**
	 * Constructor of BO from DO object
	 * @param parishDO
	 */
	public ParishBO(ParishDO parishDO) {
		this.parishCode = parishDO.getParishCode();
		this.description = parishDO.getDescription();
		this.officeLocationCode = parishDO.getOfficeLocationCode();
		
		if(parishDO.getStatus() != null) {
			this.statusId = parishDO.getStatus().getStatusId();
			this.statusDescription = parishDO.getStatus().getDescription();
		}
		
		if(parishDO.getAuditEntry()!=null){
			this.auditEntryBO = new AuditEntryBO(parishDO.getAuditEntry());
		}
		
	}

	public String getCurrentUsername() {
		return currentUsername;
	}

	public String getDescription() {
		return description;
	}

	public String getOfficeLocationCode() {
		return officeLocationCode;
	}

	public String getParishCode() {
		return parishCode;
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

	public void setOfficeLocationCode(String officeLocationCode) {
		if(StringUtils.isNotBlank(officeLocationCode))
			this.officeLocationCode = officeLocationCode.trim();
	}

	public void setParishCode(String parishCode) {
		if(StringUtils.isNotBlank(parishCode))
			this.parishCode = parishCode.trim();
	}

	public void setStatusDescription(String statusDescription) {
		if(StringUtils.isNotBlank(statusDescription))
			this.statusDescription = statusDescription.trim();
	}

	public void setStatusId(String statusId) {
		if(StringUtils.isNotBlank(statusId))
			this.statusId = statusId.trim();
	}

	public String getOfficeLocationDescription() {
		return officeLocationDescription;
	}

	public void setOfficeLocationDescription(String officeLocationDescription) {
		this.officeLocationDescription = officeLocationDescription;
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
