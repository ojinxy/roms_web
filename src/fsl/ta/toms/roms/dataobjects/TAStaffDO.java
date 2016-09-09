package fsl.ta.toms.roms.dataobjects;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.Table;
import javax.persistence.Version;


//@NamedNativeQueries({
//	@NamedNativeQuery(
//	name = "getAvailableTA",
//	query = "execute procedure  roms_sp_available_ta(:startDate,:endDate,:cat,:region)",
//	resultClass = TAStaffDO.class
//	)
//})


@Entity
@Table(name="ROMS_ta_staff")
public class TAStaffDO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8817468927825172766L;

	public TAStaffDO() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Id
	@Column(name="staff_id")
	String staffId;

	@ManyToOne
	@JoinColumn(name="person_id")
	PersonDO person;
	
	@Column(name="office_loc_code")
	String officeLocationCode;
	
	@Column(name="staff_type_code")
	String staffTypeCode;
	
	@Column(name="login_username")
	String loginUsername;
	
	@ManyToOne
	@JoinColumn(name="status_id")
	StatusDO status;
	
	@Embedded
	AuditEntry auditEntry;
	
	@Version
	@Column(name="version_nbr")
	Integer versionNbr;

	public String getStaffId() {
		return staffId;
	}

	public void setStaffId(String staffId) {
		this.staffId = staffId;
	}

	public PersonDO getPerson() {
		return person;
	}

	public void setPerson(PersonDO person) {
		this.person = person;
	}

	public String getOfficeLocationCode() {
		return officeLocationCode;
	}

	public void setOfficeLocationCode(String officeLocationCode) {
		this.officeLocationCode = officeLocationCode;
	}

	public String getStaffTypeCode() {
		return staffTypeCode;
	}

	public void setStaffTypeCode(String staffTypeCode) {
		this.staffTypeCode = staffTypeCode;
	}

	public String getLoginUsername() {
		return loginUsername;
	}

	public void setLoginUsername(String loginUsername) {
		this.loginUsername = loginUsername;
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
