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
@Table(name="ROMS_road_check_offence")
public class RoadCheckOffenceDO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2345846107122436255L;

	public RoadCheckOffenceDO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
	
	public RoadCheckOffenceDO(RoadCheckDO roadCheck, OffenceDO offence,
			AuditEntry auditEntry) {
		super();
		this.roadCheck = roadCheck;
		this.offence = offence;
		this.auditEntry = auditEntry;
	}




	@Id
	@Column(name="road_check_offence_id")
	@GeneratedValue(strategy=GenerationType.AUTO)
	Integer roadCheckOffenceId;
	
	@ManyToOne
	@JoinColumn(name="road_check_id")
	RoadCheckDO roadCheck;
	
	@ManyToOne
	@JoinColumn(name="offence_id")
	OffenceDO offence;
	
	@Embedded
	AuditEntry auditEntry;
	
	@Version
	@Column(name="version_nbr")
	Integer versionNbr;

	public Integer getRoadCheckOffenceId() {
		return roadCheckOffenceId;
	}

	public void setRoadCheckOffenceId(Integer roadCheckOffenceId) {
		this.roadCheckOffenceId = roadCheckOffenceId;
	}

	public RoadCheckDO getRoadCheck() {
		return roadCheck;
	}

	public void setRoadCheck(RoadCheckDO roadCheck) {
		this.roadCheck = roadCheck;
	}

	public OffenceDO getOffence() {
		return offence;
	}

	public void setOffence(OffenceDO offence) {
		this.offence = offence;
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
