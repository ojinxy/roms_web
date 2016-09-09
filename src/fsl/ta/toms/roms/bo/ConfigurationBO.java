package fsl.ta.toms.roms.bo;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import fsl.ta.toms.roms.dataobjects.ConfigurationDO;

/**
 * 
 * @author jreid
 * Created Date: May 28, 2013
 */
public class ConfigurationBO implements Serializable {

	private static final long serialVersionUID = 6429334793500664407L;

	String cfgCode;

	String currentUsername;

	String dataType;

	String description;

	Date endDate;

	String isEditableString;
	
	String isVisibleString;
	
boolean isEditable;
	
	boolean isVisible;

	Date startDate;

	String statusDescription;

	String statusId;
	
	String value;
	
	AuditEntryBO auditEntryBO;
	
	public ConfigurationBO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Constructing creating BO from DO
	 * @param configurationDO
	 * @author jreid
	 */
	public ConfigurationBO(ConfigurationDO configurationDO) {
		this.cfgCode = configurationDO.getCfgCode();
		
		if(configurationDO.getDataType() != null)
			this.dataType = configurationDO.getDataType()+"";
		
		this.description = configurationDO.getDescription();
		this.endDate = configurationDO.getEndDate();
		this.startDate = configurationDO.getStartDate();
		
		if(configurationDO.getIsEditable() != null){
			if(configurationDO.getIsEditable().equalsIgnoreCase("Y"))
				this.isEditable =true;
			else
				this.isEditable =false;
		}
		
		if(configurationDO.getIsVisible() != null){
			if(configurationDO.getIsVisible().equalsIgnoreCase("Y"))
				this.isVisible =true;
			else
				this.isVisible =false;
		}
			
		
		if(configurationDO.getStatus() != null) {
			this.statusId = configurationDO.getStatus().getStatusId();
			this.statusDescription = configurationDO.getStatus().getDescription();
		}
		
		this.value = configurationDO.getValue();
		
		if(configurationDO.getAuditEntry()!=null){
			this.auditEntryBO = new AuditEntryBO(configurationDO.getAuditEntry());
		}
	
	}

	public String getCfgCode() {
		return cfgCode;
	}

	public String getCurrentUsername() {
		return currentUsername;
	}


	public String getDescription() {
		return description;
	}

	public Date getEndDate() {
		return endDate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public String getStatusDescription() {
		return statusDescription;
	}

	public String getStatusId() {
		return statusId;
	}

	public String getValue() {
		return value;
	}

	public void setCfgCode(String cfgCode) {
		if(StringUtils.isNotBlank(cfgCode))
			this.cfgCode = cfgCode.trim();
	}

	public void setCurrentUsername(String currentUsername) {
		this.currentUsername = currentUsername;
	}

public void setDescription(String description) {
		if(StringUtils.isNotBlank(description))
			this.description = description.trim();
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}


	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	

	/**
	 * @return the isEditable
	 */
	public boolean isEditable() {
		return isEditable;
	}

	/**
	 * @param isEditable the isEditable to set
	 */
	public void setEditable(boolean isEditable) {
		this.isEditable = isEditable;
	}

	/**
	 * @return the isVisible
	 */
	public boolean isVisible() {
		return isVisible;
	}

	/**
	 * @param isVisible the isVisible to set
	 */
	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public void setStatusDescription(String statusDescription) {
		if(StringUtils.isNotBlank(statusDescription))
			this.statusDescription = statusDescription.trim();
	}

	public void setStatusId(String statusId) {
		if(StringUtils.isNotBlank(statusId))
			this.statusId = statusId.trim();
	}

	public void setValue(String value) {
		if(StringUtils.isNotBlank(value))
			this.value = value.trim();
	}

	/**
	 * @return the isEditableString
	 */
	public String getIsEditableString() {
		return isEditableString;
	}

	/**
	 * @param isEditableString the isEditableString to set
	 */
	public void setIsEditableString(String isEditableString) {
		this.isEditableString = isEditableString;
	}

	/**
	 * @return the isVisibleString
	 */
	public String getIsVisibleString() {
		return isVisibleString;
	}

	/**
	 * @param isVisibleString the isVisibleString to set
	 */
	public void setIsVisibleString(String isVisibleString) {
		this.isVisibleString = isVisibleString;
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
