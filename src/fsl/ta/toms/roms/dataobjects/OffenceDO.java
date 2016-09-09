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
@Table(name="ROMS_offence")
public class OffenceDO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8495839908944747050L;
	
	public OffenceDO() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Id
	@Column(name="offence_id")
	Integer offenceId;
	
	@ManyToOne
	@JoinColumn(name="road_check_type_id")
	CDRoadCheckTypeDO roadCheckType;
	
	@Column(name="description")
	String description;
	
	@Column(name="short_desc")
	String shortDescription;
	
	//@Column(name="is_complete")
	//Character isComplete;
	
	@ManyToOne
	@JoinColumn(name="status_id")
	StatusDO status;
	
	@Column(name="excerpt")
	String excerpt;
	
	@Embedded
	AuditEntry auditEntry;
	
	@Version
	@Column(name="version_nbr")
	Integer versionNbr;
	
	public Integer getOffenceId() {
		return offenceId;
	}

	public void setOffenceId(Integer offenceId) {
		this.offenceId = offenceId;
	}

	public CDRoadCheckTypeDO getRoadCheckType() {
		return roadCheckType;
	}

	public void setRoadCheckType(CDRoadCheckTypeDO roadCheckType) {
		this.roadCheckType = roadCheckType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	/*public Character getIsComplete() {
		return isComplete;
	}

	public void setIsComplete(Character isComplete) {
		this.isComplete = isComplete;
	}*/

	public StatusDO getStatus() {
		return status;
	}

	public void setStatus(StatusDO status) {
		this.status = status;
	}

	public String getExcerpt() {
		return excerpt;
	}

	public void setExcerpt(String excerpt) {
		this.excerpt = excerpt;
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
