package fsl.ta.toms.roms.dataobjects;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name="ROMS_reason")
public class ReasonDO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5525126942145476538L;

	public ReasonDO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	@Id
	@Column(name="reason_id")
	@GeneratedValue(strategy=GenerationType.AUTO)
	Integer reasonId;
	
	@Column(name="description")
	String description;
	
	@ManyToOne
	@JoinColumn(name="reason_type_code")
	ReasonTypeDO reasonType;
	
	@ManyToOne
	@JoinColumn(name="status_id")
	StatusDO status;
	
	@Column(name="is_visible")
	String isVisible;
	
	@Embedded
	AuditEntry auditEntry;
	
	@Version
	@Column(name="version_nbr")
	Integer versionNbr;
	
	public Integer getReasonId() {
		return reasonId;
	}

	public void setReasonId(Integer reasonId) {
		this.reasonId = reasonId;
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
	
	public String getIsVisible() {
		return isVisible;
	}

	public void setIsVisible(String isVisible) {
		this.isVisible = isVisible;
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

	public ReasonTypeDO getReasonType() {
		return reasonType;
	}

	public void setReasonType(ReasonTypeDO reasonType) {
		this.reasonType = reasonType;
	}

	
}
