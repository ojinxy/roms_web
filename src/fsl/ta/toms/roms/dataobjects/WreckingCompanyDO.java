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

import fsl.ta.toms.roms.bo.WreckingCompanyBO;

@Entity
@Table(name="ROMS_wrecking_company")
public class WreckingCompanyDO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -52126438019319710L;

	public WreckingCompanyDO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public WreckingCompanyDO(WreckingCompanyBO wreckingCompanyBO) {
	
		this.contactPersonFirstName = wreckingCompanyBO.getContactPersonFirstName();
		this.contactPersonMiddleName = wreckingCompanyBO.getContactPersonMiddleName();
		this.contactPersonLastName = wreckingCompanyBO.getContactPersonLastName();
		this.address = new AddressDO();
		this.address.setMarkText(wreckingCompanyBO.getMarkText());
		//this.address.markText = wreckingCompanyBO.getMarkText();
	
		this.status = new StatusDO();
		this.status.setStatusId(wreckingCompanyBO.getStatusId());
	
		
		//this.address.poBoxNo = wreckingCompanyBO.getPoBoxNo();
		this.address.setPoBoxNo(wreckingCompanyBO.getPoBoxNo());
		
		//this.address.poLocationName = wreckingCompanyBO.getPoLocationName();
		this.address.setPoLocationName(wreckingCompanyBO.getPoLocationName());
		this.shortDesc = wreckingCompanyBO.getShortDesc();

		//this.address.streetName = wreckingCompanyBO.getStreetName();
		this.address.setStreetName(wreckingCompanyBO.getStreetName());
		//this.address.streetNo = wreckingCompanyBO.getStreetNo();
		this.address.setStreetNo(wreckingCompanyBO.getStreetNo());
		this.telephoneCell = wreckingCompanyBO.getTelephoneCell();
		this.telephoneHome = wreckingCompanyBO.getTelephoneHome();
		this.telephoneWork = wreckingCompanyBO.getTelephoneWork();

		this.trnBranch = wreckingCompanyBO.getTrnBranch();
		this.trnNbr = wreckingCompanyBO.getTrnNbr();
		this.companyName = wreckingCompanyBO.getCompanyName();
	}

	public  void updateWreckingCompanyDOFields(WreckingCompanyBO wreckingCompanyBO) {
		this.contactPersonFirstName = wreckingCompanyBO.getContactPersonFirstName();
		this.contactPersonMiddleName = wreckingCompanyBO.getContactPersonMiddleName();
		this.contactPersonLastName = wreckingCompanyBO.getContactPersonLastName();
		this.address = new AddressDO();
		this.address.markText = wreckingCompanyBO.getMarkText();

		this.status = new StatusDO();
		this.status.setStatusId(wreckingCompanyBO.getStatusId());
	
		
		this.address.poBoxNo = wreckingCompanyBO.getPoBoxNo();
		this.address.poLocationName = wreckingCompanyBO.getPoLocationName();
		this.shortDesc = wreckingCompanyBO.getShortDesc();

		this.address.streetName = wreckingCompanyBO.getStreetName();
		this.address.streetNo = wreckingCompanyBO.getStreetNo();
		this.telephoneCell = wreckingCompanyBO.getTelephoneCell();
		this.telephoneHome = wreckingCompanyBO.getTelephoneHome();
		this.telephoneWork = wreckingCompanyBO.getTelephoneWork();

		this.trnBranch = wreckingCompanyBO.getTrnBranch();
		this.trnNbr = wreckingCompanyBO.getTrnNbr();
		this.companyName = wreckingCompanyBO.getCompanyName();
		
	}
	
	
	@Id
	@Column(name="wrecking_company_id")
	@GeneratedValue(strategy=GenerationType.AUTO)
	Integer wreckingCompanyId;
	
	@Column(name="company_name")
	String companyName;
	
	@Column(name="short_desc")
	String shortDesc;
	
	@Column(name="trn_nbr")
	String trnNbr;
	
	@Column(name="trn_branch")
	String trnBranch;
	
	@Embedded
	AddressDO address;
	
	@Column(name="tel_home")
	String telephoneHome;
	
	@Column(name="tel_cell")
	String telephoneCell;
	
	@Column(name="tel_work")
	String telephoneWork;
	
	@Column(name="contact_person_first_name")
	String contactPersonFirstName;
	
	@Column(name="contact_person_mid_name")
	String contactPersonMiddleName;
	
	@Column(name="contact_person_last_name")
	String contactPersonLastName;
	

	@ManyToOne
	@JoinColumn(name="status_id")
	StatusDO status;
	
	
	@Embedded
	AuditEntry auditEntry;
	
	@Version
	@Column(name="version_nbr")
	Integer versionNbr;

	public Integer getWreckingCompanyId() {
		return wreckingCompanyId;
	}

	public void setWreckingCompanyId(Integer wreckingCompanyId) {
		this.wreckingCompanyId = wreckingCompanyId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getShortDesc() {
		return shortDesc;
	}

	public void setShortDesc(String shortDesc) {
		this.shortDesc = shortDesc;
	}

	public String getTrnNbr() {
		return trnNbr;
	}

	public void setTrnNbr(String trnNbr) {
		this.trnNbr = trnNbr;
	}

	public String getTrnBranch() {
		return trnBranch;
	}

	public void setTrnBranch(String trnBranch) {
		this.trnBranch = trnBranch;
	}

	

	public AddressDO getAddress() {
		return address;
	}

	public void setAddress(AddressDO address) {
		this.address = address;
	}

	public String getTelephoneHome() {
		return telephoneHome;
	}

	public void setTelephoneHome(String telephoneHome) {
		this.telephoneHome = telephoneHome;
	}

	public String getTelephoneCell() {
		return telephoneCell;
	}

	public void setTelephoneCell(String telephoneCell) {
		this.telephoneCell = telephoneCell;
	}

	public String getTelephoneWork() {
		return telephoneWork;
	}

	public void setTelephoneWork(String telephoneWork) {
		this.telephoneWork = telephoneWork;
	}

	public String getContactPersonFirstName() {
		return contactPersonFirstName;
	}

	public void setContactPersonFirstName(String contactPersonFirstName) {
		this.contactPersonFirstName = contactPersonFirstName;
	}

	public String getContactPersonMiddleName() {
		return contactPersonMiddleName;
	}

	public void setContactPersonMiddleName(String contactPersonMiddleName) {
		this.contactPersonMiddleName = contactPersonMiddleName;
	}

	public String getContactPersonLastName() {
		return contactPersonLastName;
	}

	public void setContactPersonLastName(String contactPersonLastName) {
		this.contactPersonLastName = contactPersonLastName;
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

	public StatusDO getStatus() {
		return status;
	}

	public void setStatus(StatusDO status) {
		this.status = status;
	}

	
}
