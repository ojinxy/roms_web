package fsl.ta.toms.roms.dataobjects;

import java.io.Serializable;
import java.util.Date;

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
@Table(name="ROMS_badge_check_result")
public class BadgeCheckResultDO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8888623513113922291L;

	
	
	public BadgeCheckResultDO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	

	public BadgeCheckResultDO(RoadCheckDO roadCheck, String badgeNumber,
			String badgeTypeDescription, String firstName, String middleName,
			String lastName, Integer bioSno, Date issueDate, Date expiryDate,
			AuditEntry auditEntry) {
		super();
		this.roadCheck = roadCheck;
		this.badgeNumber = badgeNumber;
		this.badgeTypeDescription = badgeTypeDescription;
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
		this.biometricsSno = bioSno;
		this.issueDate = issueDate;
		this.expiryDate = expiryDate;
		this.auditEntry = auditEntry;
	}



	@Id
	@Column(name="badge_check_id")
	@GeneratedValue(strategy=GenerationType.AUTO)
	Integer badgeCheckId;
	
	@ManyToOne
	@JoinColumn(name="road_check_id")
	RoadCheckDO roadCheck;
	
	@Column(name="badge_number")
	String badgeNumber;
	
	@Column(name="badge_type_desc")
	String badgeTypeDescription;
	
	@Column(name="first_name")
	String firstName;
	
	@Column(name="mid_name")
	String middleName;
	
	@Column(name="last_name")
	String lastName;
	
	@Column(name="biometrics_sno")
	Integer biometricsSno;
	
	@Column(name="issue_date")
	Date issueDate;
	
	@Column(name="expiry_date")
	Date expiryDate;
	
	@Embedded
	AuditEntry auditEntry;
	
	@Version
	@Column(name="version_nbr")
	Integer versionNbr;

	public Integer getBadgeCheckId() {
		return badgeCheckId;
	}

	public void setBadgeCheckId(Integer badgeCheckId) {
		this.badgeCheckId = badgeCheckId;
	}

	public RoadCheckDO getRoadCheck() {
		return roadCheck;
	}

	public void setRoadCheck(RoadCheckDO roadCheck) {
		this.roadCheck = roadCheck;
	}

	public String getBadgeNumber() {
		return badgeNumber;
	}

	public void setBadgeNumber(String badgeNumber) {
		this.badgeNumber = badgeNumber;
	}

	public String getBadgeTypeDescription() {
		return badgeTypeDescription;
	}

	public void setBadgeTypeDescription(String badgeTypeDescription) {
		this.badgeTypeDescription = badgeTypeDescription;
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

	public Date getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
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



	public Integer getBiometricsSno() {
		return biometricsSno;
	}



	public void setBiometricsSno(Integer biometricsSno) {
		this.biometricsSno = biometricsSno;
	}
	
	
}
