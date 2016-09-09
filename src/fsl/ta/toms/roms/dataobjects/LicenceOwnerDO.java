package fsl.ta.toms.roms.dataobjects;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import fsl.ta.toms.roms.bo.RoadLicenceOwnerBO;

@Entity
@Table(name="ROMS_licence_owner")
public class LicenceOwnerDO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1765122538477871419L;


	public LicenceOwnerDO() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	public LicenceOwnerDO(RoadLicCheckResultDO roadLicCheck, String firstName,
			String middleName, String lastName, AuditEntry auditEntry) {
		super();
		this.roadLicCheck = roadLicCheck;
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
		this.auditEntry = auditEntry;
	}
	
	public LicenceOwnerDO(RoadLicCheckResultDO roadLicCheck, RoadLicenceOwnerBO rlOwner, AuditEntry auditEntry) {
		super();
		this.roadLicCheck = roadLicCheck;
		this.firstName = rlOwner.getFirstName();		
		this.lastName = rlOwner.getLastName();
		this.setLicenceNo(rlOwner.getLicenceNo());
		this.setMarkText(rlOwner.getMark());
		this.setOwnerType(rlOwner.getOwnerType());
		this.setParishDesc(rlOwner.getParishCode());
		this.setPoBoxNo(rlOwner.getPoBox());
		this.setPoLocationName(rlOwner.getPostOffice());
		this.setStreetName(rlOwner.getStreetName());
		this.setStreetNo(rlOwner.getStreetNo());
		this.setTelephoneCell(rlOwner.getMobilePhoneNo());
		this.setTelephoneHome(rlOwner.getHomePhoneNo());
		this.setTelephoneWork(rlOwner.getWorkPhoneNo());
		this.setTrnNbr(rlOwner.getTrn());
		this.auditEntry = auditEntry;
	}


	@Id
	@Column(name="owner_id")
	@GeneratedValue(strategy=GenerationType.AUTO)
	Integer ownerId;
	
	@ManyToOne
	@JoinColumn(name="road_lic_check_id")
	RoadLicCheckResultDO roadLicCheck;
	
	@Column(name="first_name")
	String firstName;
	
	@Column(name="mid_name")
	String middleName;
	
	@Column(name="last_name")
	String lastName;
	
	@Column(name="trn_nbr")
	String trnNbr;
	
	@Column(name="licence_no")
	Integer licenceNo;
	
	@Column(name="tel_home")
	String telephoneHome;
	
	@Column(name="tel_cell")	
	String telephoneCell;
	
	@Column(name="tel_work")
	String telephoneWork;
	
	@Column(name = "mark_text")
	String		markText;

	@Column(name = "street_no")
	String		streetNo;

	@Column(name = "street_name")
	String		streetName;

	@Column(name = "po_loc_name")
	String		poLocationName;

	@Column(name = "po_box_no")
	String		poBoxNo;
	
	@Column(name = "parish_desc")
	String		parishDesc;
	
	@Column(name = "owner_type")
	String ownerType;
	
	@Embedded
	AuditEntry auditEntry;
	
	@Version
	@Column(name="version_nbr")
	Integer versionNbr;

	public Integer getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Integer ownerId) {
		this.ownerId = ownerId;
	}

	public RoadLicCheckResultDO getRoadLicCheck() {
		return roadLicCheck;
	}

	public void setRoadLicCheck(RoadLicCheckResultDO roadLicCheck) {
		this.roadLicCheck = roadLicCheck;
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

	
	public AuditEntry getAuditEntry() {
		return auditEntry;
	}

	public void setAuditEntry(AuditEntry auditEntry) {
		this.auditEntry = auditEntry;
	}

	public Integer getVersionNbr() {
		return versionNbr;
	}

	public void setVersionNbr(Integer versionNbr) {
		this.versionNbr = versionNbr;
	}


	public String getTrnNbr()
	{
		return trnNbr;
	}


	public void setTrnNbr(String trnNbr)
	{
		this.trnNbr = trnNbr;
	}


	public Integer getLicenceNo()
	{
		return licenceNo;
	}


	public void setLicenceNo(Integer licenceNo)
	{
		this.licenceNo = licenceNo;
	}


	public String getTelephoneHome()
	{
		return telephoneHome;
	}


	public void setTelephoneHome(String telephoneHome)
	{
		this.telephoneHome = telephoneHome;
	}


	public String getTelephoneCell()
	{
		return telephoneCell;
	}


	public void setTelephoneCell(String telephoneCell)
	{
		this.telephoneCell = telephoneCell;
	}


	public String getTelephoneWork()
	{
		return telephoneWork;
	}


	public void setTelephoneWork(String telephoneWork)
	{
		this.telephoneWork = telephoneWork;
	}


	public String getMarkText()
	{
		return markText;
	}


	public void setMarkText(String markText)
	{
		this.markText = markText;
	}


	public String getStreetNo()
	{
		return streetNo;
	}


	public void setStreetNo(String streetNo)
	{
		this.streetNo = streetNo;
	}


	public String getStreetName()
	{
		return streetName;
	}


	public void setStreetName(String streetName)
	{
		this.streetName = streetName;
	}


	public String getPoLocationName()
	{
		return poLocationName;
	}


	public void setPoLocationName(String poLocationName)
	{
		this.poLocationName = poLocationName;
	}


	public String getPoBoxNo()
	{
		return poBoxNo;
	}


	public void setPoBoxNo(String poBoxNo)
	{
		this.poBoxNo = poBoxNo;
	}


	public String getParishDesc()
	{
		return parishDesc;
	}


	public void setParishDesc(String parishDesc)
	{
		this.parishDesc = parishDesc;
	}


	public String getOwnerType()
	{
		return ownerType;
	}


	public void setOwnerType(String ownerType)
	{
		this.ownerType = ownerType;
	}

		
}
