package fsl.ta.toms.roms.search.criteria.impl;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class JPCriteriaBO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1516557464300186223L;

	private String regNumber;
	private String trnNbr, firstName , middleName,lastName;
	private String parishCode;
	private String statusId;
	private List<String> regionCodes;
	
	public JPCriteriaBO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getRegNumber() {
		return regNumber;
	}

	public void setRegNumber(String regNumber) {
		if(StringUtils.isNotBlank(regNumber))
			this.regNumber = regNumber.trim();
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

	public String getParishCode() {
		return parishCode;
	}

	public void setParishCode(String parishCode) {
		if(StringUtils.isNotBlank(parishCode))
			this.parishCode = parishCode.trim();
	}

	public String getStatusId() {
		return statusId;
	}

	public void setStatusId(String statusId) {
		if(StringUtils.isNotBlank(statusId))
			this.statusId = statusId.trim();
	}

	public List<String> getRegionCodes() {
		return regionCodes;
	}

	public void setRegionCodes(List<String> regionCodes) {
		this.regionCodes = regionCodes;
	}

	@Override
	public String toString() {
		return "JPCriteriaBO [regNumber=" + regNumber + ", trnNbr=" + trnNbr
				+ ", firstName=" + firstName + ", middleName=" + middleName
				+ ", lastName=" + lastName + ", parishCode=" + parishCode
				+ ", statusId=" + statusId + ", regionCodes=" + regionCodes
				+ "]";
	}
	
	

}
