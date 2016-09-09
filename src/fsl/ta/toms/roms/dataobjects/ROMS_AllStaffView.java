package fsl.ta.toms.roms.dataobjects;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="roms_all_staff_view")
public class ROMS_AllStaffView implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2696285984605799017L;

	@Id
	
	@Column(name="staff_id")
	String staffId;
	
	@Column(name="first_name")
	String firstName;
	
	@Column(name="mid_name")
	String middleName;
	
	@Column(name="last_name")
	String lastName;
	
	@Column(name="login_username")
	String loginUsername;
	
	@Column(name="staff_type_code")
	String staffTypeCode; 
	
	@Column(name="staff_type_desc")
	String staffTypeDesc;

	@Column(name="loc_code")
	String officeLocationCode;
	
	@Column(name="loc_desc")
	String officeLocationDesc;
	
	@Column(name="create_username")
	String createUsername;
	
	@Column(name="create_dtime")
	Date createDtime;
	
	@Column(name="update_username")
	String updateUsername;
	
	@Column(name="update_dtime")
	Date updateDtime;

	public String getStaffId() {
		return staffId;
	}

	public void setStaffId(String staffId) {
		this.staffId = staffId;
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

	public String getLoginUsername() {
		return loginUsername;
	}

	public void setLoginUsername(String loginUsername) {
		this.loginUsername = loginUsername;
	}

	public String getStaffTypeDesc() {
		return staffTypeDesc;
	}

	public void setStaffTypeDesc(String staffTypeDesc) {
		this.staffTypeDesc = staffTypeDesc;
	}

	public String getOfficeLocationDesc() {
		return officeLocationDesc;
	}

	public void setOfficeLocationDesc(String officeLocationDesc) {
		this.officeLocationDesc = officeLocationDesc;
	}

	public String getCreateUsername() {
		return createUsername;
	}

	public void setCreateUsername(String createUsername) {
		this.createUsername = createUsername;
	}


	public String getUpdateUsername() {
		return updateUsername;
	}

	public void setUpdateUsername(String updateUsername) {
		this.updateUsername = updateUsername;
	}

	public String getStaffTypeCode() {
		return staffTypeCode;
	}

	public void setStaffTypeCode(String staffTypeCode) {
		this.staffTypeCode = staffTypeCode;
	}

	public String getOfficeLocationCode() {
		return officeLocationCode;
	}

	public void setOfficeLocationCode(String officeLocationCode) {
		this.officeLocationCode = officeLocationCode;
	}

	public Date getCreateDtime() {
		return createDtime;
	}

	public void setCreateDtime(Date createDtime) {
		this.createDtime = createDtime;
	}

	public Date getUpdateDtime() {
		return updateDtime;
	}

	public void setUpdateDtime(Date updateDtime) {
		this.updateDtime = updateDtime;
	}
	
	
}
