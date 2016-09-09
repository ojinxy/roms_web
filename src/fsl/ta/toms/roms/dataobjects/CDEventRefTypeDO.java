package fsl.ta.toms.roms.dataobjects;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name="ROMS_cd_event_ref_type")
public class CDEventRefTypeDO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8595617456890159254L;

	public CDEventRefTypeDO() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Id
	@Column(name="ref_type_code")
	String refTypeCode;
	
	@Column(name="ref_type_desc")
	String refTypeDescription;
	
	@Column(name="ref_label")
	String refLabel;
	
	@ManyToOne
	@JoinColumn(name="status_id")
	StatusDO status;
	
	@Embedded
	AuditEntry auditEntry;
	
	@Version
	@Column(name="version_nbr")
	Integer versionNbr;

	public String getRefTypeCode() {
		return refTypeCode;
	}

	public void setRefTypeCode(String refTypeCode) {
		this.refTypeCode = refTypeCode;
	}

	public String getRefTypeDescription() {
		return refTypeDescription;
	}

	public void setRefTypeDescription(String refTypeDescription) {
		this.refTypeDescription = refTypeDescription;
	}

	public String getRefLabel() {
		return refLabel;
	}

	public void setRefLabel(String refLabel) {
		this.refLabel = refLabel;
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


}
