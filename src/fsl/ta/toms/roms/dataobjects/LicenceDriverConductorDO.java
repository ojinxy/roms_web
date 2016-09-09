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
@Table(name="ROMS_licence_driver_cond")
public class LicenceDriverConductorDO implements Serializable {

	public String getBadgeType()
	{
		return badgeType;
	}




	public void setBadgeType(String badgeType)
	{
		this.badgeType = badgeType;
	}




	/**
	 * 
	 */
	private static final long serialVersionUID = -2947245481658045738L;

	
	public LicenceDriverConductorDO() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	
	
	public LicenceDriverConductorDO(RoadLicCheckResultDO roadLicCheck, String firstName,
			String middleName, String lastName, String badgeNumber, String badgeType,
			AuditEntry auditEntry) {
		super();
		this.roadLicCheck = roadLicCheck;
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
		this.badgeNumber = badgeNumber;
		this.badgeType = badgeType;
		this.auditEntry = auditEntry;
	}




	@Id
	@Column(name="dr_cond_id")
	@GeneratedValue(strategy=GenerationType.AUTO)
	Integer drCondId;
	
	@ManyToOne
	@JoinColumn(name="road_lic_check_id")
	RoadLicCheckResultDO roadLicCheck;
	
	@Column(name="first_name")
	String firstName;
	
	@Column(name="mid_name")
	String middleName;
	
	@Column(name="last_name")
	String lastName;
	
	@Column(name="badge_number")
	String badgeNumber;
	
	@Column(name="badge_type")
	String badgeType;
	
	@Embedded
	AuditEntry auditEntry;
	
	@Version
	@Column(name="version_nbr")
	Integer versionNbr;


	public Integer getDrCondId() {
		return drCondId;
	}


	public void setDrCondId(Integer drCondId) {
		this.drCondId = drCondId;
	}


	public RoadLicCheckResultDO getRoadLicCheck() {
		return roadLicCheck;
	}


	public void setRoadLicCheck(RoadLicCheckResultDO roadLicCheck) {
		this.roadLicCheck = roadLicCheck;
	}


	public String getFirstName() {
		return firstName;
	}


	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}


	public String getMiddleName() {
		return middleName;
	}


	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}


	public String getLastName() {
		return lastName;
	}


	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


	public String getBadgeNumber() {
		return badgeNumber;
	}


	public void setBadgeNumber(String badgeNumber) {
		this.badgeNumber = badgeNumber;
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
