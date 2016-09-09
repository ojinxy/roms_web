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
@Table(name="ROMS_cd_plea")
public class CDPleaDO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -28114720474645740L;

	public CDPleaDO() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	
	public CDPleaDO(Integer pleaId, String description, StatusDO status,
			AuditEntry auditEntry) {
		super();
		this.pleaId = pleaId;
		this.description = description;
		this.status = status;
		this.auditEntry = auditEntry;
	}



	@Id
	@Column(name="plea_id")
	Integer pleaId;
	
	@Column(name="description")
	String description;
	
	@ManyToOne
	@JoinColumn(name="status_id")
	StatusDO status;
	
	@Embedded
	AuditEntry auditEntry;
	
	@Version
	@Column(name="version_nbr")
	Integer versionNbr;

	public Integer getPleaId() {
		return pleaId;
	}

	public void setPleaId(Integer pleaId) {
		this.pleaId = pleaId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
