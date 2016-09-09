package fsl.ta.toms.roms.bo;

import java.io.Serializable;
import java.util.Date;

import fsl.ta.toms.roms.util.NameUtil;



public class BIMSStaffViewBO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7400500726200845973L;

	private String staffId;
	private String trnNbr;
	private String inspRegNo;
	private String staffType;
	private String firstName;
	private String middleName;
	private String lastName;
	private String genderCode;
	private String locationCode;
	private String badgeStatusCode;
	private Date badgeUpdateDTime,staffUpdateDTime;
	
	private String fullName;
	
	public BIMSStaffViewBO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
	public BIMSStaffViewBO(String staffId, String trnNbr,
			String inspRegNo, String staffType, String firstName,
			String middleName, String lastName, String genderCode,
			String locationCode, String badgeStatusCode,
			Date badgeUpdateDTime,Date staffUpdateDTime) {
		super();
		this.staffId = staffId;
		this.trnNbr = trnNbr;
		this.inspRegNo = inspRegNo;
		this.staffType = staffType;
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
		
		NameUtil util = new NameUtil();
		this.fullName= util.getLastNameCapsFirstNameMiddleName(firstName, lastName, middleName);
		
		
		this.genderCode = genderCode;
		this.locationCode = locationCode;
		this.badgeStatusCode = badgeStatusCode;
		
		this.badgeUpdateDTime = badgeUpdateDTime;
		this.staffUpdateDTime = staffUpdateDTime;
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
	public String getFullName() {
		
		this.fullName= NameUtil.getFullName(firstName, lastName);
		
		return fullName;
	}
	public void setFullName(String fullName) {
		
		this.fullName = fullName;
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
