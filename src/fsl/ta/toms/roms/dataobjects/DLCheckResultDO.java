package fsl.ta.toms.roms.dataobjects;

import java.io.Serializable;
import java.sql.Blob;
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
@Table(name="ROMS_dl_check_result")
public class DLCheckResultDO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1973768117448411993L;
	
	public DLCheckResultDO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
	
	
	
	public DLCheckResultDO(RoadCheckDO roadCheck, String dlNo,
			String firstName, String middleName, String lastName,
			String dlClass, Blob photograph, Date issueDate, Date expiryDate,
			AuditEntry auditEntry) {
		super();
		this.roadCheck = roadCheck;
		this.dlNo = dlNo;
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
		this.dlClass = dlClass;
		this.photograph = photograph;
		this.issueDate = issueDate;
		this.expiryDate = expiryDate;
		this.auditEntry = auditEntry;
	}






	@Id
	@Column(name="drivers_lic_check_id")
	@GeneratedValue(strategy=GenerationType.AUTO)
	Integer driversLicCheckId;
	
	@ManyToOne
	@JoinColumn(name="road_check_id")
	RoadCheckDO roadCheck;
	
	@Column(name="dl_no")
	String dlNo;
	
	@Column(name="first_name")
	String firstName;
	
	@Column(name="mid_name")
	String middleName;
	
	@Column(name="last_name")
	String lastName;
	
	@Column(name="dl_class")
	String dlClass;
	
	@Column(name="photograph")
	Blob photograph;
	
	@Column(name="issue_date")
	Date issueDate;
	
	@Column(name="expiry_date")
	Date expiryDate;
	
	@Embedded 
	AuditEntry auditEntry;
	
	@Version
	@Column(name="version_nbr")
	Integer versionNbr;

	public Integer getDriversLicCheckId() {
		return driversLicCheckId;
	}

	public void setDriversLicCheckId(Integer driversLicCheckId) {
		this.driversLicCheckId = driversLicCheckId;
	}

	public RoadCheckDO getRoadCheck() {
		return roadCheck;
	}

	public void setRoadCheck(RoadCheckDO roadCheck) {
		this.roadCheck = roadCheck;
	}

	public String getDlNo() {
		return dlNo;
	}

	public void setDlNo(String dlNo) {
		this.dlNo = dlNo;
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

	public String getDlClass() {
		return dlClass;
	}

	public void setDlClass(String dlClass) {
		this.dlClass = dlClass;
	}

	public Blob getPhotograph() {
		return photograph;
	}

	public void setPhotograph(Blob photograph) {
		this.photograph = photograph;
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

	
}
