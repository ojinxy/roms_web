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
@Table(name="ROMS_offence_check_result")
public class OffenceCheckResultDO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4186972695380026433L;

	public OffenceCheckResultDO() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	
	
	public OffenceCheckResultDO(CitationCheckDO citationCheck,
			RoadCheckOffenceDO roadCheckOffence, Integer roadLicNum,
			String roadLicOwner, String roadLicStatus, AuditEntry auditEntry) {
		super();
		this.citationCheck = citationCheck;
		this.roadCheckOffence = roadCheckOffence;
		this.auditEntry = auditEntry;
		this.roadLicNumber = roadLicNum;
		this.roadLicOwner = roadLicOwner;
		this.roadLicStatus = roadLicStatus;
	}




	@Id
	@Column(name="offence_check_id")
	@GeneratedValue(strategy=GenerationType.AUTO)
	Integer offenceCheckId;
	
	@ManyToOne	
	@JoinColumn(name="citation_check_id")
	CitationCheckDO citationCheck;
	
	@ManyToOne
	@JoinColumn(name="road_check_offence_id")
	RoadCheckOffenceDO roadCheckOffence;
	
	@Column(name="road_licence_num")
	Integer roadLicNumber;
	
	@Column(name="road_licence_owner")
	String roadLicOwner;
	
	@Column(name="road_licence_status")
	String roadLicStatus;
	
	@Embedded
	AuditEntry auditEntry;
	
	@Version
	@Column(name="version_nbr")
	Integer versionNbr;

	public Integer getOffenceCheckId() {
		return offenceCheckId;
	}

	public void setOffenceCheckId(Integer offenceCheckId) {
		this.offenceCheckId = offenceCheckId;
	}

	public CitationCheckDO getCitationCheck() {
		return citationCheck;
	}

	public void setCitationCheck(CitationCheckDO citationCheck) {
		this.citationCheck = citationCheck;
	}



	public RoadCheckOffenceDO getRoadCheckOffence() {
		return roadCheckOffence;
	}

	public void setRoadCheckOffence(RoadCheckOffenceDO roadCheckOffence) {
		this.roadCheckOffence = roadCheckOffence;
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




	public Integer getRoadLicNumber() {
		return roadLicNumber;
	}




	public void setRoadLicNumber(Integer roadLicNumber) {
		this.roadLicNumber = roadLicNumber;
	}




	public String getRoadLicOwner() {
		return roadLicOwner;
	}




	public void setRoadLicOwner(String roadLicOwner) {
		this.roadLicOwner = roadLicOwner;
	}




	public String getRoadLicStatus() {
		return roadLicStatus;
	}




	public void setRoadLicStatus(String roadLicStatus) {
		this.roadLicStatus = roadLicStatus;
	}

		
}
