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
@Table(name="ROMS_road_check_offence_outcome")
public class RoadCheckOffenceOutcomeDO implements Serializable {

	
	private static final long serialVersionUID = -2401056381498537934L;

	@Id
	@Column(name="road_check_offence_outcome_id")
	@GeneratedValue(strategy=GenerationType.AUTO)
	Integer roadCheckOffenceOutcomeId;
	
	@ManyToOne
	@JoinColumn(name="road_check_offence_id")
	RoadCheckOffenceDO roadCheckOffence;
	
	@ManyToOne
	@JoinColumn(name="outcome_type_id")
	CDOutcomeTypeDO outcomeType;
	
	@Embedded
	AuditEntry auditEntry;
	
	@Version
	@Column(name="version_nbr")
	Integer versionNbr;
	
	
	
	public RoadCheckOffenceOutcomeDO() {
		super();
	}
	
		
	/**
	 * 
	 * @param roadCheckOffence
	 * @param outcomeType
	 * @param auditEntry
	 */
	public RoadCheckOffenceOutcomeDO(RoadCheckOffenceDO roadCheckOffence,
			CDOutcomeTypeDO outcomeType, AuditEntry auditEntry) {
		super();
		this.roadCheckOffence = roadCheckOffence;
		this.outcomeType = outcomeType;
		this.auditEntry = auditEntry;
	}


	/**
	 * @return the roadCheckOffenceOutcomeId
	 */
	public Integer getRoadCheckOffenceOutcomeId() {
		return roadCheckOffenceOutcomeId;
	}


	/**
	 * @param roadCheckOffenceOutcomeId the roadCheckOffenceOutcomeId to set
	 */
	public void setRoadCheckOffenceOutcomeId(Integer roadCheckOffenceOutcomeId) {
		this.roadCheckOffenceOutcomeId = roadCheckOffenceOutcomeId;
	}


	/**
	 * @return the roadCheckOffence
	 */
	public RoadCheckOffenceDO getRoadCheckOffence() {
		return roadCheckOffence;
	}


	/**
	 * @param roadCheckOffence the roadCheckOffence to set
	 */
	public void setRoadCheckOffence(RoadCheckOffenceDO roadCheckOffence) {
		this.roadCheckOffence = roadCheckOffence;
	}


	/**
	 * @return the outcomeType
	 */
	public CDOutcomeTypeDO getOutcomeType() {
		return outcomeType;
	}


	/**
	 * @param outcomeType the outcomeType to set
	 */
	public void setOutcomeType(CDOutcomeTypeDO outcomeType) {
		this.outcomeType = outcomeType;
	}


	/**
	 * @return the auditEntry
	 */
	public AuditEntry getAuditEntry() {
		return auditEntry;
	}


	/**
	 * @param auditEntry the auditEntry to set
	 */
	public void setAuditEntry(AuditEntry auditEntry) {
		this.auditEntry = auditEntry;
	}


	/**
	 * @return the versionNbr
	 */
	public Integer getVersionNbr() {
		return versionNbr;
	}


	/**
	 * @param versionNbr the versionNbr to set
	 */
	public void setVersionNbr(Integer versionNbr) {
		this.versionNbr = versionNbr;
	}
	
}
