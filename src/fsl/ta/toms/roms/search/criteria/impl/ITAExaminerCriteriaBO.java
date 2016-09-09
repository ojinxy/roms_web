package fsl.ta.toms.roms.search.criteria.impl;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

public class ITAExaminerCriteriaBO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6373401105898793855L;

	private String idNumber;
	private String examinerId;
	private String trnNbr, firstName , middleName,lastName;
	private String officeLocationCode;
	private String statusId;
	
	public ITAExaminerCriteriaBO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(String idNumber) {
		if(StringUtils.isNotBlank(idNumber))
			this.idNumber = idNumber.trim();
	}

	public String getTrnNbr() {
		return trnNbr;
	}

	public void setTrnNbr(String trnNbr) {
		if(StringUtils.isNotBlank(trnNbr))
			this.trnNbr = trnNbr.trim();
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		if(StringUtils.isNotBlank(firstName))
			this.firstName = firstName.trim();
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		if(StringUtils.isNotBlank(middleName))
			this.middleName = middleName.trim();
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

	public String getStatusId() {
		return statusId;
	}

	public void setStatusId(String statusId) {
		if(StringUtils.isNotBlank(statusId))
			this.statusId = statusId.trim();
	}

	public String getExaminerId() {
		return examinerId;
	}

	public void setExaminerId(String examinerId) {
		this.examinerId = examinerId;
	}
	
	@Override
	public String toString() {
		return "ITAExaminerCriteriaBO [idNumber=" + idNumber + ", examinerId="
				+ examinerId + ", trnNbr=" + trnNbr + ", firstName="
				+ firstName + ", middleName=" + middleName + ", lastName="
				+ lastName + ", officeLocationCode=" + officeLocationCode
				+ ", statusId=" + statusId + "]";
	}
	
	
}
