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
@Table(name="ROMS_road_check")
public class RoadCheckDO implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 247125275040269369L;
	
	
	public RoadCheckDO() {
		super();

	}
	
	
	
	public RoadCheckDO(CDRoadCheckTypeDO roadCheckType,
			ComplianceDO compliance, Character compliant, String comment,
			AuditEntry auditEntry) {
		super();
		
		this.roadCheckType = roadCheckType;
		this.compliance = compliance;
		this.compliant = compliant;
		this.comment = comment;
		this.auditEntry = auditEntry;
	}



	@Id
	@Column(name="road_check_id")
	@GeneratedValue(strategy=GenerationType.AUTO)
	Integer roadCheckId;
	
	@ManyToOne
	@JoinColumn(name="road_check_type_id")
	CDRoadCheckTypeDO roadCheckType;
	
	@ManyToOne
	@JoinColumn(name="compliance_id")
	ComplianceDO compliance;
	
	@Column(name="compliant")
	Character compliant;
	
	@Column(name="comment")
	String comment;
	
	@ManyToOne
	@JoinColumn(name="status_id")
	StatusDO status;
	
	@Column(name="is_scheduled")
	String isScheduled;
	
	@Embedded
	AuditEntry auditEntry;
	
	@Version
	@Column(name="version_nbr")
	Integer versionNbr;


	public Integer getRoadCheckId() {
		return roadCheckId;
	}

	public void setRoadCheckId(Integer roadCheckId) {
		this.roadCheckId = roadCheckId;
	}

	public CDRoadCheckTypeDO getRoadCheckType() {
		return roadCheckType;
	}

	public void setRoadCheckType(CDRoadCheckTypeDO roadCheckType) {
		this.roadCheckType = roadCheckType;
	}

	public ComplianceDO getCompliance() {
		return compliance;
	}

	public void setCompliance(ComplianceDO compliance) {
		this.compliance = compliance;
	}

	public Character getCompliant() {
		return compliant;
	}

	public void setCompliant(Character compliant) {
		this.compliant = compliant;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
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



	public StatusDO getStatus() {
		return status;
	}



	public void setStatus(StatusDO status) {
		this.status = status;
	}



	/**
	 * @return the isScheduled
	 */
	public String getIsScheduled() {
		return isScheduled;
	}



	/**
	 * @param isScheduled the isScheduled to set
	 */
	public void setIsScheduled(String isScheduled) {
		this.isScheduled = isScheduled;
	}

	
	
}
