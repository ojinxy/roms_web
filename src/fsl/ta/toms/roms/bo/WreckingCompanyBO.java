package fsl.ta.toms.roms.bo;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

import fsl.ta.toms.roms.dataobjects.WreckingCompanyDO;

/**
 * 
 * @author jreid Created Date: May 24, 2013
 */
public class WreckingCompanyBO implements Serializable {

	private static final long serialVersionUID = -52126438019319710L;

	String companyName;

	String contactPersonFirstName;

	String contactPersonLastName;

	String contactPersonMiddleName;

	String currentUsername;

	String markText;

	String parishCode;

	String parishName;

	String poBoxNo;

	String poLocationName;

	String shortDesc;

	String statusDescription;

	String statusId;
	
	String streetName;

	String streetNo;

	String telephoneCell;

	String telephoneHome;

	String telephoneWork;

	String trnBranch;
	
	String trnNbr;

	Integer wreckingCompanyId;
	
	AuditEntryBO auditEntryBO;

	public WreckingCompanyBO() {
		super();

	}

	/**
	 * Created a Wrecking Company Business Object from a Data Object
	 * 
	 * @param companyDO
	 */
	public WreckingCompanyBO(WreckingCompanyDO companyDO) {
		this.wreckingCompanyId = companyDO.getWreckingCompanyId();
		this.contactPersonFirstName = companyDO.getContactPersonFirstName();
		this.contactPersonMiddleName = companyDO.getContactPersonMiddleName();
		this.contactPersonLastName = companyDO.getContactPersonLastName();
		this.markText = companyDO.getAddress().getMarkText();
		this.companyName = companyDO.getCompanyName();

		if (companyDO.getAddress().getParish() != null) {
			this.parishCode = companyDO.getAddress().getParish().getParishCode();
			this.parishName = companyDO.getAddress().getParish().getDescription();
		}

		this.poBoxNo = companyDO.getAddress().getPoBoxNo();
		this.poLocationName = companyDO.getAddress().getPoLocationName();
		this.shortDesc = companyDO.getShortDesc();

		this.streetName = companyDO.getAddress().getStreetName();
		this.streetNo = companyDO.getAddress().getStreetNo();
		this.telephoneCell = companyDO.getTelephoneCell();
		this.telephoneHome = companyDO.getTelephoneHome();
		this.telephoneWork = companyDO.getTelephoneWork();
		
		if(companyDO.getStatus() != null) {
			this.statusId = companyDO.getStatus().getStatusId();
			this.statusDescription = companyDO.getStatus().getDescription();
		}
		

		this.trnBranch = companyDO.getTrnBranch();
		this.trnNbr = companyDO.getTrnNbr();
		
		if(companyDO.getAuditEntry()!=null){
			this.auditEntryBO = new AuditEntryBO(companyDO.getAuditEntry());
		}

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

	public String getCurrentUsername() {
		return currentUsername;
	}

	public String getMarkText() {
		return markText;
	}

	public String getParishCode() {
		return parishCode;
	}

	public String getParishName() {
		return parishName;
	}

	public String getPoBoxNo() {
		return poBoxNo;
	}

	public String getPoLocationName() {
		return poLocationName;
	}

	public String getShortDesc() {
		return shortDesc;
	}

	public String getStatusDescription() {
		return statusDescription;
	}

	public String getStatusId() {
		return statusId;
	}

	public String getStreetName() {
		return streetName;
	}

	public String getStreetNo() {
		return streetNo;
	}

	public String getTelephoneCell() {
		return telephoneCell;
	}

	public String getTelephoneHome() {
		return telephoneHome;
	}

	public String getTelephoneWork() {
		return telephoneWork;
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
		if (StringUtils.isNotBlank(companyName))
			this.companyName = companyName.trim();
	}

	public void setContactPersonFirstName(String contactPersonFirstName) {
		if (StringUtils.isNotBlank(contactPersonFirstName))
			this.contactPersonFirstName = contactPersonFirstName.trim();
	}

	public void setContactPersonLastName(String contactPersonLastName) {
		if (StringUtils.isNotBlank(contactPersonLastName))
			this.contactPersonLastName = contactPersonLastName.trim();
	}

	public void setContactPersonMiddleName(String contactPersonMiddleName) {
		if (StringUtils.isNotBlank(contactPersonMiddleName))
			this.contactPersonMiddleName = contactPersonMiddleName.trim();
	}

	public void setCurrentUsername(String currentUsername) {
		//if (StringUtils.isNotBlank(currentUsername))
			this.currentUsername = currentUsername;
	}

	public void setMarkText(String markText) {
		if (StringUtils.isNotBlank(markText))
			this.markText = markText.trim();
	}

	public void setParishCode(String parishCode) {
		if (StringUtils.isNotBlank(parishCode))
			this.parishCode = parishCode.trim();
	}

	public void setParishName(String parishName) {
		if (StringUtils.isNotBlank(parishName))
			this.parishName = parishName.trim();
	}

	public void setPoBoxNo(String poBoxNo) {
		if (StringUtils.isNotBlank(poBoxNo))
			this.poBoxNo = poBoxNo.trim();
	}

	public void setPoLocationName(String poLocationName) {
		if (StringUtils.isNotBlank(poLocationName))
			this.poLocationName = poLocationName.trim();
	}

	public void setShortDesc(String shortDesc) {
		if (StringUtils.isNotBlank(shortDesc))
			this.shortDesc = shortDesc.trim();
	}

	public void setStatusDescription(String statusDescription) {
		this.statusDescription = statusDescription;
	}

	public void setStatusId(String statusId) {
		this.statusId = statusId;
	}

	public void setStreetName(String streetName) {
		if (StringUtils.isNotBlank(streetName))
			this.streetName = streetName.trim();
	}

	public void setStreetNo(String streetNo) {
		if (StringUtils.isNotBlank(streetNo))
			this.streetNo = streetNo.trim();
	}

	public void setTelephoneCell(String telephoneCell) {
		if (StringUtils.isNotBlank(telephoneCell))
			this.telephoneCell = telephoneCell.trim();
	}

	public void setTelephoneHome(String telephoneHome) {
		if (StringUtils.isNotBlank(telephoneHome))
			this.telephoneHome = telephoneHome.trim();
	}

	public void setTelephoneWork(String telephoneWork) {
		if (StringUtils.isNotBlank(telephoneWork))
			this.telephoneWork = telephoneWork.trim();
	}

	public void setTrnBranch(String trnBranch) {
		if (StringUtils.isNotBlank(trnBranch))
			this.trnBranch = trnBranch.trim();
	}

	public void setTrnNbr(String trnNbr) {
		if (StringUtils.isNotBlank(trnNbr))
			this.trnNbr = trnNbr.trim();
	}

	public void setWreckingCompanyId(Integer wreckingCompanyId) {
		this.wreckingCompanyId = wreckingCompanyId;
	}

	/**
	 * @return the auditEntryBO
	 */
	public AuditEntryBO getAuditEntryBO() {
		return auditEntryBO;
	}

	/**
	 * @param auditEntryBO the auditEntryBO to set
	 */
	public void setAuditEntryBO(AuditEntryBO auditEntryBO) {
		this.auditEntryBO = auditEntryBO;
	}

}
