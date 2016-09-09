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
@Table(name="ROMS_police_officer")
public class PoliceOfficerDO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 375856930963127176L;

	public PoliceOfficerDO() {
		super();
		// TODO Auto-generated constructor stub
	}
	

//	public PoliceOfficerDO(Integer polOfficerCompNo, PersonDO person,
//			String rank, String stationDescription, StatusDO status) {
//		super();
//		this.polOfficerCompNo = polOfficerCompNo;
//		this.person = person;
//		this.rank = rank;
//		this.stationDescription = stationDescription;
//		this.status = status;
//		this.auditEntry = auditEntry;
//	}


	public PoliceOfficerDO(Integer polOfficerCompNo, PersonDO person,
			String rank, String stationDescription, StatusDO status,
			AuditEntry auditEntry) {
		super();
		this.polOfficerCompNo = polOfficerCompNo;
		this.person = person;
		this.rank = rank;
		this.stationDescription = stationDescription;
		this.status = status;
		this.auditEntry = auditEntry;
	}


	@Id
	@Column(name="police_officer_comp_no")
	Integer polOfficerCompNo;
	
	@ManyToOne
	@JoinColumn(name="person_id")
	PersonDO person;
	
	@Column(name="rank")
	String rank;
	
	@Column(name="station_description")
	String stationDescription;
	
	@ManyToOne
	@JoinColumn(name="status_id")
	StatusDO status;
	
	@Embedded
	AuditEntry auditEntry;
	
	@Version
	@Column(name="version_nbr")
	Integer versionNbr;

	public Integer getPolOfficerCompNo() {
		return polOfficerCompNo;
	}

	public void setPolOfficerCompNo(Integer polOfficerCompNo) {
		this.polOfficerCompNo = polOfficerCompNo;
	}

	public PersonDO getPerson() {
		return person;
	}

	public void setPerson(PersonDO person) {
		this.person = person;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public String getStationDescription() {
		return stationDescription;
	}

	public void setStationDescription(String stationDescription) {
		this.stationDescription = stationDescription;
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
