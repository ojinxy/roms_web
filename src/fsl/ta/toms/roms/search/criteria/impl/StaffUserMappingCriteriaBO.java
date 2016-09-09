package fsl.ta.toms.roms.search.criteria.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import fsl.ta.toms.roms.search.criteria.SearchCriteria;

public class StaffUserMappingCriteriaBO implements Serializable, SearchCriteria{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2493618818160744597L;
	private String staffId;
	private String firstName;
	private String midName;
	private String lastName;
	private String officeLocationCode;
	private List<String> selectedRegions;
	private String staffType;
	private String loginUsername;
	
	public StaffUserMappingCriteriaBO() {
		super();
		this.selectedRegions = new ArrayList<String>();
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

	public String getOfficeLocationCode() {
		return officeLocationCode;
	}

	public void setOfficeLocationCode(String officeLocationCode) {
		if(StringUtils.isNotBlank(officeLocationCode))
			this.officeLocationCode = officeLocationCode.trim();
	}

	public String getStaffType() {
		return staffType;
	}

	public void setStaffType(String staffType) {
		if(StringUtils.isNotBlank(staffType))
			this.staffType = staffType.trim();
	}

	public String getLoginUsername() {
		return loginUsername;
	}

	public void setLoginUsername(String loginUsername) {
		if(StringUtils.isNotBlank(loginUsername))
			this.loginUsername = loginUsername.trim();
	}


	public List<String> getSelectedRegions() {
		return selectedRegions;
	}


	public void setSelectedRegions(List<String> selectedRegions) {
		this.selectedRegions = selectedRegions;
	}


}
