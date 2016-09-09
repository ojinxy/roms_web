package fsl.ta.toms.roms.bo;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

import fsl.ta.toms.roms.dataobjects.PoundDO;

/**
 * 
 * @author jreid
 * Created Date: May 28, 2013
 */
public class PoundBO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5062845026703409921L;

	private String currentUsername;

	String markText;

	Integer numberOfLots;

	Integer numberOfParkingSpaces;

	String parishCode;

	String parishName;

	String poBoxNo;

	String poLocationName;

	Integer poundId;

	String poundName;

	String shortDesc;

	String statusDescription;

	String statusId;

	String streetName;

	String streetNo;

	String telephoneCell;

	String telephoneHome;

	String telephoneWork;
	
	AuditEntryBO auditEntryBO;

	public PoundBO() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * Create constructor from pound Data Object
	 * @param poundDO
	 */
	public PoundBO(PoundDO poundDO) {
		this.poundId = poundDO.getPoundId();
		this.poundName = poundDO.getPoundName();
		this.shortDesc = poundDO.getShortDesc();
		this.markText = poundDO.getAddress().getMarkText();

		this.streetNo = poundDO.getAddress().getStreetNo();

		this.streetName = poundDO.getAddress().getStreetName();

		this.poLocationName = poundDO.getAddress().getPoLocationName();

		this.poBoxNo = poundDO.getAddress().getPoBoxNo();

		if (poundDO.getAddress().getParish() != null) {
			this.parishCode = poundDO.getAddress().getParish().getParishCode();

			this.parishName = poundDO.getAddress().getParish().getDescription();
		}

		this.telephoneHome = poundDO.getTelephoneHome();

		this.telephoneCell = poundDO.getTelephoneCell();

		this.telephoneWork = poundDO.getTelephoneWork();

		this.numberOfLots = poundDO.getNumberOfLots();

		this.numberOfParkingSpaces = poundDO.getNumberOfParkingSpaces();

		if (poundDO.getStatus() != null) {
			this.statusId = poundDO.getStatus().getStatusId();
			this.statusDescription = poundDO.getStatus().getDescription();
		}
		
		if(poundDO.getAuditEntry()!=null){
			this.auditEntryBO = new AuditEntryBO(poundDO.getAuditEntry());
		}

	}

	public String getCurrentUsername() {
		return currentUsername;
	}

	public String getMarkText() {
		return markText;
	}

	public Integer getNumberOfLots() {
		return numberOfLots;
	}

	public Integer getNumberOfParkingSpaces() {
		return numberOfParkingSpaces;
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

	public Integer getPoundId() {
		return poundId;
	}

	public String getPoundName() {
		return poundName;
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

	public void setCurrentUsername(String currentUsername) {
		//if (StringUtils.isNotBlank(currentUsername))
			this.currentUsername = currentUsername;
	}

	public void setMarkText(String markText) {
		if(StringUtils.isNotBlank(markText))
			this.markText = markText.trim();
	}

	public void setNumberOfLots(Integer numberOfLots) {
		this.numberOfLots = numberOfLots;
	}

	public void setNumberOfParkingSpaces(Integer numberOfParkingSpaces) {
		this.numberOfParkingSpaces = numberOfParkingSpaces;
	}

	public void setParishCode(String parishCode) {
		this.parishCode = parishCode;
	}

	public void setParishName(String parishName) {
		if(StringUtils.isNotBlank(parishName))
			this.parishName = parishName.trim();
	}

	public void setPoBoxNo(String poBoxNo) {
		if(StringUtils.isNotBlank(poBoxNo))
			this.poBoxNo = poBoxNo.trim();
	}

	public void setPoLocationName(String poLocationName) {
		if(StringUtils.isNotBlank(poLocationName))
			this.poLocationName = poLocationName.trim();
	}

	public void setPoundId(Integer poundId) {
		this.poundId = poundId;
	}

	public void setPoundName(String poundName) {
		if (StringUtils.isNotBlank(poundName))
			this.poundName = poundName.trim();
	}

	public void setShortDesc(String shortDesc) {
		if(StringUtils.isNotBlank(shortDesc))
			this.shortDesc = shortDesc.trim();
	}

	public void setStatusDescription(String statusDescription) {
		if(StringUtils.isNotBlank(statusDescription))
			this.statusDescription = statusDescription.trim();
	}

	public void setStatusId(String statusId) {
		if(StringUtils.isNotBlank(statusId))
			this.statusId = statusId.trim();
	}

	public void setStreetName(String streetName) {
		if(StringUtils.isNotBlank(streetName))
			this.streetName = streetName.trim();
	}

	
	public void setStreetNo(String streetNo) {
		if(StringUtils.isNotBlank(streetNo))
			this.streetNo = streetNo.trim();
	}

	public void setTelephoneCell(String telephoneCell) {
		if(StringUtils.isNotBlank(telephoneCell))
			this.telephoneCell = telephoneCell.trim();
	}

	public void setTelephoneHome(String telephoneHome) {
		if(StringUtils.isNotBlank(telephoneHome))
			this.telephoneHome = telephoneHome.trim();
	}

	public void setTelephoneWork(String telephoneWork) {
		if(StringUtils.isNotBlank(telephoneWork))
			this.telephoneWork = telephoneWork.trim();
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
