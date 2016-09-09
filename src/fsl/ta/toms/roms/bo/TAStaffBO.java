package fsl.ta.toms.roms.bo;

import java.io.Serializable;

import fsl.ta.toms.roms.dataobjects.AssignedPersonDO;
import fsl.ta.toms.roms.dataobjects.TAStaffDO;
import fsl.ta.toms.roms.util.NameUtil;

public class TAStaffBO extends AttendanceDetailsBO implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 819982621298244165L;

	private String staffId;
	private Integer personId;
	private String firstName;
	private String middleName;
	private String lastName;
	private String fullName;
	private String officeLocationCode;
	private String staffTypeCode;
	private String loginUsername;
	private String statusId;
	
	private boolean scheduled;
	
	public TAStaffBO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TAStaffBO(String staffId, Integer personId,
			String officeLocationCode, String staffTypeCode,
			String loginUsername, String statusId) {
		super();
		this.staffId = staffId;
		this.personId = personId;
		this.officeLocationCode = officeLocationCode;
		this.staffTypeCode = staffTypeCode;
		this.loginUsername = loginUsername;
		this.statusId = statusId;
	}
	
	public TAStaffBO(TAStaffDO taStaffDO) {
		if(taStaffDO!=null){
			this.staffId = taStaffDO.getStaffId();
			
			if(taStaffDO.getPerson()!=null){
				this.personId = taStaffDO.getPerson().getPersonId();
				this.firstName = taStaffDO.getPerson().getFirstName();
				this.middleName = taStaffDO.getPerson().getMiddleName();
				this.lastName = taStaffDO.getPerson().getLastName();
	
				NameUtil util = new NameUtil();
				this.fullName= util.getLastNameCapsFirstNameMiddleName(firstName, lastName, middleName);
			
			}
			
			this.officeLocationCode = taStaffDO.getOfficeLocationCode();
			this.staffTypeCode = taStaffDO.getStaffTypeCode();
		
			if(taStaffDO.getStatus()!=null){
				this.statusId = taStaffDO.getStatus().getStatusId();
			}
		}
	}
	
	public TAStaffBO(TAStaffDO taStaffDO, AssignedPersonDO assignedPersonDO) {
		if(taStaffDO!=null){
			this.staffId = taStaffDO.getStaffId();
			
			if(taStaffDO.getPerson()!=null){
				this.personId = taStaffDO.getPerson().getPersonId();
				this.firstName = taStaffDO.getPerson().getFirstName();
				this.middleName = taStaffDO.getPerson().getMiddleName();
				this.lastName = taStaffDO.getPerson().getLastName();
	
				NameUtil util = new NameUtil();
				this.fullName= util.getLastNameCapsFirstNameMiddleName(firstName, lastName, middleName);
			
			}
			
			this.officeLocationCode = taStaffDO.getOfficeLocationCode();
			this.staffTypeCode = taStaffDO.getStaffTypeCode();
		
			if(taStaffDO.getStatus()!=null){
				this.statusId = taStaffDO.getStatus().getStatusId();
			}
			
			
		}
		
		if(assignedPersonDO!=null){
			this.setComment(assignedPersonDO.getComment());
			
			this.setScheduled(true);
			
			if(assignedPersonDO.getAttended()==null){
				this.setAttended(null);
			}
			else{
				if(assignedPersonDO.getAttended().equalsIgnoreCase("Y")){
					this.setAttended(true);
				}
				if(assignedPersonDO.getAttended().equalsIgnoreCase("N")){
					this.setAttended(false);
				}
			}
		}
	}

	public String getStaffId() {
		return staffId;
	}

	public void setStaffId(String staffId) {
		this.staffId = staffId;
	}

	public Integer getPersonId() {
		return personId;
	}

	public void setPersonId(Integer personId) {
		this.personId = personId;
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

	public String getStatusId() {
		return statusId;
	}

	public void setStatusId(String statusId) {
		this.statusId = statusId;
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

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	
	public boolean isScheduled() {
		return scheduled;
	
	}

	
	public void  setScheduled(boolean scheduled) {
		this.scheduled = scheduled;
	}
	
	
}
