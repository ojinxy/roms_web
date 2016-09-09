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
@Table(name="ROMS_cd_outcome_type")
public class CDOutcomeTypeDO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7975680055317571113L;

	public CDOutcomeTypeDO() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Id
	@Column(name="outcome_type_id")
	String outcomeTypeId;
	
	@Column(name="description")
	String description;
	
	@Column(name="positive_outcome")
	Character positiveOutcome;
	
	@ManyToOne
	@JoinColumn(name="status_id")
	StatusDO status;
	
	@Embedded
	AuditEntry auditEntry;
	
	@Version
	@Column(name="version_nbr")
	Integer versionNbr;

	public String getOutcomeTypeId() {
		return outcomeTypeId;
	}

	public void setOutcomeTypeId(String outcomeTypeId) {
		this.outcomeTypeId = outcomeTypeId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Character getPositiveOutcome() {
		return positiveOutcome;
	}

	public void setPositiveOutcome(Character positiveOutcome) {
		this.positiveOutcome = positiveOutcome;
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
