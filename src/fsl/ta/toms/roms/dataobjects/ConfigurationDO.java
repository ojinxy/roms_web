package fsl.ta.toms.roms.dataobjects;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import fsl.ta.toms.roms.bo.ConfigurationBO;

@Entity
@Table(name="ROMS_configuration")
public class ConfigurationDO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6429334793500664407L;

	public ConfigurationDO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ConfigurationDO(ConfigurationBO configurationBO) {
		this.cfgCode = configurationBO.getCfgCode();
		
		if(configurationBO.getDataType()!= null)
			this.dataType =  configurationBO.getDataType();
		
		this.description = configurationBO.getDescription();
		this.endDate = configurationBO.getEndDate();
		this.startDate = configurationBO.getStartDate();
		
		
		this.status = new StatusDO();
		this.status.setStatusId(configurationBO.getStatusId());
				
		this.value = configurationBO.getValue();
	}


	@Id
	@Column(name="cfg_code")
	String cfgCode;
	
	@Column(name="description")
	String description;
	
	@Column(name="data_type")
	String dataType;
	
	@Column(name="value")
	String value;
	
	@Column(name="is_visible")
	String isVisible;
	
	@Column(name="is_editable")
	String isEditable;
	
	@Column(name="start_date")
	Date startDate;
	
	@Column(name="end_date")
	Date endDate;
	
	@ManyToOne
	@JoinColumn(name="status_id")
	StatusDO status;
	
	@Embedded
	AuditEntry auditEntry;
	
	@Version
	@Column(name="version_nbr")
	Integer versionNbr;

	public String getCfgCode() {
		return cfgCode;
	}

	public void setCfgCode(String cfgCode) {
		this.cfgCode = cfgCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	
	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}


	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public StatusDO getStatus() {
		return status;
	}

	public void setStatus(StatusDO status) {
		this.status = status;
	}

	
	public AuditEntry getAuditEntry() {
		return auditEntry;
	}

	public void setAuditEntry(AuditEntry auditEntry) {
		this.auditEntry = auditEntry;
	}

	public Integer getVersionNbr() {
		return versionNbr;
	}

	public void setVersionNbr(Integer versionNbr) {
		this.versionNbr = versionNbr;
	}
	
	

	public String getIsVisible() {
		return isVisible;
	}

	public void setIsVisible(String isVisible) {
		this.isVisible = isVisible;
	}

	public String getIsEditable() {
		return isEditable;
	}

	public void setIsEditable(String isEditable) {
		this.isEditable = isEditable;
	}

	public void updateConfigurationDOFields(ConfigurationBO configurationBO) {
		this.dataType = configurationBO.getDataType();
		this.description = configurationBO.getDescription();
		this.endDate = configurationBO.getEndDate();
		this.startDate = configurationBO.getStartDate();
		
		this.status = new StatusDO();
		this.status.setStatusId(configurationBO.getStatusId());
				
		this.value = configurationBO.getValue();
		
	}
	
	
}
