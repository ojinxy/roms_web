package fsl.ta.toms.roms.search.criteria.impl;

import org.apache.commons.lang.StringUtils;

import fsl.ta.toms.roms.search.criteria.SearchCriteria;
/**
 * 
 * @author jreid
 * Created Date: Jun 4, 2013
 */
public class WreckingCompanyCriteriaBO implements SearchCriteria  {


	private static final long serialVersionUID = 6410012000950717493L;


	String companyName;

	String contactPersonFirstName;

	String contactPersonLastName;

	String contactPersonMiddleName;

	String parishCode;

	String shortDesc;
	
	String statusId;
		
	String trnBranch;

	String trnNbr;

	Integer wreckingCompanyId;
	
	public WreckingCompanyCriteriaBO() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	
	public String getCompanyName() {
		return companyName;
	}

	public String getContactPersonFirstName() {
		return contactPersonFirstName;
	}

	
	public String getContactPersonLastName() {
		return contactPersonLastName;
	}

	public String getContactPersonMiddleName() {
		return contactPersonMiddleName;
	}

	public String getParishCode() {
		return parishCode;
	}

	public String getShortDesc() {
		return shortDesc;
	}

	public String getStatusId() {
		return statusId;
	}

	public String getTrnBranch() {
		return trnBranch;
	}

	public String getTrnNbr() {
		return trnNbr;
	}

	public Integer getWreckingCompanyId() {
		return wreckingCompanyId;
	}

	public void setCompanyName(String companyName) {
		if(StringUtils.isNotBlank(companyName))
			this.companyName = companyName.trim();
	}

	public void setContactPersonFirstName(String contactPersonFirstName) {
		if(StringUtils.isNotBlank(contactPersonFirstName))
			this.contactPersonFirstName = contactPersonFirstName.trim();
	}

	
	public void setContactPersonLastName(String contactPersonLastName) {
		if(StringUtils.isNotBlank(contactPersonLastName))
			this.contactPersonLastName = contactPersonLastName.trim();
	}

	public void setContactPersonMiddleName(String contactPersonMiddleName) {
		if(StringUtils.isNotBlank(contactPersonMiddleName))
			this.contactPersonMiddleName = contactPersonMiddleName.trim();
	}

	public void setParishCode(String parishCode) {
		if(StringUtils.isNotBlank(parishCode))
			this.parishCode = parishCode.trim();
	}

	public void setShortDesc(String shortDesc) {
		if(StringUtils.isNotBlank(shortDesc))
			this.shortDesc = shortDesc.trim();
	}

	public void setStatusId(String statusId) {
		if(StringUtils.isNotBlank(statusId))
			this.statusId = statusId.trim();
	}

	public void setTrnBranch(String trnBranch) {
		if(StringUtils.isNotBlank(trnBranch))
			this.trnBranch = trnBranch.trim();
	}

	public void setTrnNbr(String trnNbr) {
		if(StringUtils.isNotBlank(trnNbr))
			this.trnNbr = trnNbr.trim();
	}

	public void setWreckingCompanyId(Integer wreckingCompanyId) {
		this.wreckingCompanyId = wreckingCompanyId;
	}

	

}
