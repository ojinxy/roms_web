package fsl.ta.toms.roms.bo;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

import fsl.ta.toms.roms.dataobjects.ROMS_AllStaffView;
import fsl.ta.toms.roms.util.NameUtil;

public class StaffUserMappingBO implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5910787823168547794L;

	private String staffId;
	private String firstName;
	private String midName;
	private String lastName;
	private String fullName;
	private String staffType;
	private String officeLocation;
	private String staffUsername;
	private String currentUsername;
	private AuditEntryBO auditEntryBO;
	
	public StaffUserMappingBO() {
		super();
		this.auditEntryBO = new AuditEntryBO();
		// TODO Auto-generated constructor stub
	}

	
	
	public StaffUserMappingBO(String staffId, String firstName, String midName,
			String lastName, String staffType, String officeLocation,
			String staffUsername) {
		super();
		this.staffId = staffId;
		this.firstName = firstName;
		this.midName = midName;
		this.lastName = lastName;

		NameUtil util = new NameUtil();
		this.fullName= util.getLastNameCapsFirstNameMiddleName(firstName, lastName, midName);
		
		this.staffType = staffType;
		this.officeLocation = officeLocation;
		this.staffUsername = staffUsername;
	}



	

	public StaffUserMappingBO(ROMS_AllStaffView allStaffView) {
		super();
		this.staffId = allStaffView.getStaffId();
		this.firstName = allStaffView.getFirstName();
		this.midName = allStaffView.getMiddleName();
		this.lastName = allStaffView.getLastName();
		
		NameUtil util = new NameUtil();
		this.fullName= util.getLastNameCapsFirstNameMiddleName(firstName, lastName, midName);
		
		this.staffType = allStaffView.getStaffTypeDesc();
		this.officeLocation = allStaffView.getOfficeLocationDesc();
		
		this.auditEntryBO = new AuditEntryBO();
		
		this.staffUsername = allStaffView.getLoginUsername();
		
		/*this.auditEntryBO.setCreateDTime(allStaffView.getCreateDtime());
		this.auditEntryBO.setCreateUsername(allStaffView.getCreateUsername());
		this.auditEntryBO.setUpdateDTime(allStaffView.getUpdateDtime());
		this.auditEntryBO.setUpdateUsername(allStaffView.getUpdateUsername());
		*/
		if(allStaffView.getUpdateDtime()==null){
			this.auditEntryBO.setUpdateDTime(allStaffView.getCreateDtime());
		}
		else{
			this.auditEntryBO.setUpdateDTime(allStaffView.getUpdateDtime());
		}
		
		if(allStaffView.getUpdateUsername()==null){
			this.auditEntryBO.setUpdateUsername(allStaffView.getCreateUsername());
		}
		else{
			this.auditEntryBO.setUpdateUsername(allStaffView.getUpdateUsername());
		}
		
	}



	public String getStaffId() {
		return staffId;
	}



	public void setStaffId(String staffId) {
		if(StringUtils.isNotBlank(staffId))
			this.staffId = staffId.trim();
	}



	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		if(StringUtils.isNotBlank(firstName))
			this.firstName = firstName.trim();
	}

	public String getMidName() {
		return midName;
	}

	public void setMidName(String midName) {
		if(StringUtils.isNotBlank(midName))
			this.midName = midName.trim();
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		if(StringUtils.isNotBlank(lastName))
			this.lastName = lastName.trim();
	}
		
	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		if(StringUtils.isNotBlank(fullName))
			this.fullName = fullName.trim();
	}

	public String getStaffType() {
		return staffType;
	}

	public void setStaffType(String staffType) {
		if(StringUtils.isNotBlank(staffType))
			this.staffType = staffType.trim();
	}

	public String getOfficeLocation() {
		return officeLocation;
	}

	public void setOfficeLocation(String officeLocation) {
		if(StringUtils.isNotBlank(officeLocation))
			this.officeLocation = officeLocation.trim();
	}



	public String getStaffUsername() {
		return staffUsername;
	}



	public void setStaffUsername(String staffUsername) {
		if(StringUtils.isNotBlank(staffUsername))
			this.staffUsername = staffUsername.trim();
	}



	public String getCurrentUsername() {
		return currentUsername;
	}



	public void setCurrentUsername(String currentUsername) {
		if(StringUtils.isNotBlank(currentUsername))
			this.currentUsername = currentUsername.trim();
	}



	public AuditEntryBO getAuditEntryBO() {
		return auditEntryBO;
	}



	public void setAuditEntryBO(AuditEntryBO auditEntryBO) {
		this.auditEntryBO = auditEntryBO;
	}

	

}
