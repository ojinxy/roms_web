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
@Table(name="ROMS_cd_court_ruling")
public class CDCourtRulingDO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2321244650706091622L;

	public CDCourtRulingDO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CDCourtRulingDO(String rulingId, String description,
			Character finalRuling, StatusDO status, AuditEntry auditEntry) {
		super();
		this.rulingId = rulingId;
		this.description = description;
		this.finalRuling = finalRuling;
		this.status = status;
		this.auditEntry = auditEntry;
	}

	@Id
	@Column(name="ruling_id")
	String rulingId;
	
	@Column(name="description")	
	String description;
	
	@Column(name="final_ruling")
	Character finalRuling;
	
	@ManyToOne
	@JoinColumn(name="status_id")
	StatusDO status;
	
	@Embedded
	AuditEntry auditEntry;
	
	@Version
	@Column(name="version_nbr")
	Integer version_nbr;

	public String getRulingId() {
		return rulingId;
	}

	public void setRulingId(String rulingId) {
		this.rulingId = rulingId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Character getFinalRuling() {
		return finalRuling;
	}

	public void setFinalRuling(Character finalRuling) {
		this.finalRuling = finalRuling;
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

	public Integer getVersion_nbr() {
		return version_nbr;
	}

	public void setVersion_nbr(Integer version_nbr) {
		this.version_nbr = version_nbr;
	}

		
}
