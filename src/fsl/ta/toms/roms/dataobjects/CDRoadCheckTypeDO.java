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
@Table(name="ROMS_cd_road_check_type")
public class CDRoadCheckTypeDO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3877566107331373879L;

	public CDRoadCheckTypeDO() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Id
	@Column(name="road_check_type_id")
	String roadCheckTypeId;
	
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

	public String getRoadCheckTypeId() {
		return roadCheckTypeId;
	}

	public void setRoadCheckTypeId(String roadCheckTypeId) {
		this.roadCheckTypeId = roadCheckTypeId;
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
