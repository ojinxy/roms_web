package fsl.ta.toms.roms.dataobjects;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;


@Entity
@Table(name="ROMS_offence_mandatory_outcome")
public class OffenceMandatoryOutcomeDO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4383873628960684999L;

	@EmbeddedId
	OffenceMandatoryOutcomeKey offenceMandatoryOutcomeKey;
	
	@ManyToOne
	@JoinColumn(name="status_id")
	StatusDO status;
	
	@Embedded
	AuditEntry auditEntry;
	
	@Version
	@Column(name="version_nbr")
	Integer versionNbr;
	
	

	public OffenceMandatoryOutcomeDO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public OffenceMandatoryOutcomeKey getOffenceMandatoryOutcomeKey() {
		return offenceMandatoryOutcomeKey;
	}

	public void setOffenceMandatoryOutcomeKey(
			OffenceMandatoryOutcomeKey offenceMandatoryOutcomeKey) {
		this.offenceMandatoryOutcomeKey = offenceMandatoryOutcomeKey;
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
