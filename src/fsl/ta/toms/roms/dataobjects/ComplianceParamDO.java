package fsl.ta.toms.roms.dataobjects;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Version;

import fsl.ta.toms.roms.bo.ComplianceParamBO;

@Entity
@Table(name="roms_compliance_param")
public class ComplianceParamDO implements Serializable {

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 878790353236550361L;

	



	public ComplianceParamDO()
	{
		super();
		
	}
	
	
	
	
	
	
	@EmbeddedId
	ComplianceParamKey complianceParamKey;
	
	
	
	@Column(name="param_value")
	String paramValue;
	
	
	@Embedded
	AuditEntry auditEntry;
	
	@Version
	@Column(name="version_nbr")
	Integer versionNbr;





	public ComplianceParamKey getComplianceParamKey() {
		return complianceParamKey;
	}

	public void setComplianceParamKey(ComplianceParamKey complianceParamKey) {
		this.complianceParamKey = complianceParamKey;
	}

	public String getParamValue() {
		return paramValue;
	}

	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
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

	

	

	
}
