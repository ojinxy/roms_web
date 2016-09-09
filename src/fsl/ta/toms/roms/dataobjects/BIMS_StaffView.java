package fsl.ta.toms.roms.dataobjects;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="roms_bims_staff_view")
public class BIMS_StaffView implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6916250229870064118L;

	public BIMS_StaffView() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Id
	@Column(name="staff_no")
	Integer staffNo;
	
	@Column(name="staff_id")
	String staffId;
	
	@Column(name="trn_nbr")
	String trnNbr;
	
	@Column(name="insp_reg_no")
	String inspRegNo;
	
	@Column(name="staff_type")
	String staffType;
	
	@Column(name="first_name")
	String firstName;
	
	@Column(name="mid_name")
	String middleName;
	
	@Column(name="last_name")
	String lastName;
	
	@Column(name="gender_code")
	String genderCode;
	
	@Column(name="loc_code")
	String locationCode;
	
	@Column(name="badge_status_code")
	String badgeStatusCode;
	
	@Column(name="badge_update_dtime")
	Date badgeUpdateDTime;

	@Column(name="staff_update_dtime")
	Date staffUpdateDTime;
	
	
	public Integer getStaffNo() {
		return staffNo;
	}

	public void setStaffNo(Integer staffNo) {
		this.staffNo = staffNo;
	}

	public String getStaffId() {
		return staffId;
	}

	public void setStaffId(String staffId) {
		this.staffId = staffId;
	}

	public String getTrnNbr() {
		return trnNbr;
	}

	public void setTrnNbr(String trnNbr) {
		this.trnNbr = trnNbr;
	}

	public String getInspRegNo() {
		return inspRegNo;
	}

	public void setInspRegNo(String inspRegNo) {
		this.inspRegNo = inspRegNo;
	}

	public String getStaffType() {
		return staffType;
	}

	public void setStaffType(String staffType) {
		this.staffType = staffType;
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

	public String getGenderCode() {
		return genderCode;
	}

	public void setGenderCode(String genderCode) {
		this.genderCode = genderCode;
	}

	public String getLocationCode() {
		return locationCode;
	}

	public void setLocationCode(String locationCode) {
		this.locationCode = locationCode;
	}

	public String getBadgeStatusCode() {
		return badgeStatusCode;
	}

	public void setBadgeStatusCode(String badgeStatusCode) {
		this.badgeStatusCode = badgeStatusCode;
	}

	public Date getBadgeUpdateDTime() {
		return badgeUpdateDTime;
	}

	public void setBadgeUpdateDTime(Date badgeUpdateDTime) {
		this.badgeUpdateDTime = badgeUpdateDTime;
	}

	public Date getStaffUpdateDTime() {
		return staffUpdateDTime;
	}

	public void setStaffUpdateDTime(Date staffUpdateDTime) {
		this.staffUpdateDTime = staffUpdateDTime;
	}


		

}
