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

import fsl.ta.toms.roms.bo.PoundBO;

@Entity
@Table(name="ROMS_pound")
public class PoundDO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5062845026703409921L;

	public PoundDO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public PoundDO(PoundBO poundBO) {
		//this.poundId = poundBO.getPoundId();
		this.poundName = poundBO.getPoundName();
		this.shortDesc = poundBO.getShortDesc();
		this.address = new AddressDO();
		this.address.markText = poundBO.getMarkText();

		this.address.streetNo = poundBO.getStreetNo();

		this.address.streetName = poundBO.getStreetName();

		this.address.poLocationName = poundBO.getPoLocationName();

		this.address.poBoxNo = poundBO.getPoBoxNo();

		this.address.parish = new ParishDO();
		this.address.parish.setParishCode(poundBO.getParishCode());
		

		this.telephoneHome = poundBO.getTelephoneHome();

		this.telephoneCell = poundBO.getTelephoneCell();

		this.telephoneWork = poundBO.getTelephoneWork();

		this.numberOfLots = poundBO.getNumberOfLots();

		this.numberOfParkingSpaces = poundBO.getNumberOfParkingSpaces();

		this.status = new StatusDO();
		this.status.setStatusId(poundBO.getStatusId());
		
	}

	@Id
	@Column(name="pound_id")
	@GeneratedValue(strategy=GenerationType.AUTO)
	Integer poundId;
	
	@Column(name="pound_name")
	String poundName;
	
	@Column(name="short_desc")
	String shortDesc;
	
	@Embedded
	AddressDO address;
	
	@Column(name="tel_home")
	String telephoneHome;
	
	@Column(name="tel_cell")
	String telephoneCell;
	
	@Column(name="tel_work")
	String telephoneWork;
	
	@Column(name="number_of_lots")
	Integer numberOfLots;
	
	@Column(name="number_of_parking_spaces")
	Integer numberOfParkingSpaces;
	
	@ManyToOne
	@JoinColumn(name="status_id")
	StatusDO status;
	
	@Embedded
	AuditEntry auditEntry;
	
	@Version
	@Column(name="version_nbr")
	Integer versionNbr;

	public Integer getPoundId() {
		return poundId;
	}

	public void setPoundId(Integer poundId) {
		this.poundId = poundId;
	}

	public String getPoundName() {
		return poundName;
	}

	public void setPoundName(String poundName) {
		this.poundName = poundName;
	}

	public String getShortDesc() {
		return shortDesc;
	}

	public void setShortDesc(String shortDesc) {
		this.shortDesc = shortDesc;
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

	public Integer getNumberOfLots() {
		return numberOfLots;
	}

	public void setNumberOfLots(Integer numberOfLots) {
		this.numberOfLots = numberOfLots;
	}

	public Integer getNumberOfParkingSpaces() {
		return numberOfParkingSpaces;
	}

	public void setNumberOfParkingSpaces(Integer numberOfParkingSpaces) {
		this.numberOfParkingSpaces = numberOfParkingSpaces;
	}

	public StatusDO getStatus() {
		return status;
	}

	public void setStatus(StatusDO status) {
		this.status = status;
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

	public void updatePoundDOFields(PoundBO poundBO) {
		this.poundName = poundBO.getPoundName();
		this.shortDesc = poundBO.getShortDesc();
		this.address = new AddressDO();
		this.address.markText = poundBO.getMarkText();

		this.address.streetNo = poundBO.getStreetNo();

		this.address.streetName = poundBO.getStreetName();

		this.address.poLocationName = poundBO.getPoLocationName();

		this.address.poBoxNo = poundBO.getPoBoxNo();

		this.address.parish = new ParishDO();
		this.address.parish.setParishCode(poundBO.getParishCode());
		

		this.telephoneHome = poundBO.getTelephoneHome();

		this.telephoneCell = poundBO.getTelephoneCell();

		this.telephoneWork = poundBO.getTelephoneWork();

		this.numberOfLots = poundBO.getNumberOfLots();

		this.numberOfParkingSpaces = poundBO.getNumberOfParkingSpaces();

		this.status = new StatusDO();
		this.status.setStatusId(poundBO.getStatusId());
		
	}

	
}
