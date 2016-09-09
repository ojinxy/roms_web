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

import fsl.ta.toms.roms.bo.PersonBO;
import fsl.ta.toms.roms.util.NameUtil;

@Entity
@Table(name="ROMS_person")
public class PersonDO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8436396938427107785L;

	public PersonDO() {
		super();
		this.address = new AddressDO();
		// TODO Auto-generated constructor stub
	}
	
	
	
	public PersonDO(Integer personId, String trnNbr, String firstName,
			String middleName, String lastName, String telephoneHome,
			String telephoneCell, String telephoneWork, String markText,
			String streetNo, String streetName, String poLocationName,
			String poBoxNo, ParishDO parish, AuditEntry auditEntry) {
		super();
		this.personId = personId;
		this.trnNbr = trnNbr;
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
		this.telephoneHome = telephoneHome;
		this.telephoneCell = telephoneCell;
		this.telephoneWork = telephoneWork;
		this.address = new AddressDO();
		this.address.markText = markText;
		this.address.streetNo = streetNo;
		this.address.streetName = streetName;
		this.address.poLocationName = poLocationName;
		this.address.poBoxNo = poBoxNo;
		this.address.parish = parish;
		this.auditEntry = auditEntry;
	}
	
	public PersonDO(Integer personId, String trnNbr, String firstName,
			String middleName, String lastName, String telephoneHome,
			String telephoneCell, String telephoneWork, String markText,
			String streetNo, String streetName, String poLocationName,
			String poBoxNo, ParishDO parish, AuditEntry auditEntry, String dlNo) {
		super();
		this.personId = personId;
		this.trnNbr = trnNbr;
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
		this.telephoneHome = telephoneHome;
		this.telephoneCell = telephoneCell;
		this.telephoneWork = telephoneWork;
		this.address = new AddressDO();
		this.address.markText = markText;
		this.address.streetNo = streetNo;
		this.address.streetName = streetName;
		this.address.poLocationName = poLocationName;
		this.address.poBoxNo = poBoxNo;
		this.address.parish = parish;
		this.dlNo = dlNo;
		this.auditEntry = auditEntry;
	}



	/**
	 * Constructor from Business Object
	 * @param person
	 */
	public PersonDO(PersonBO person) {
		if(person != null)
		{
	
			this.setTrnNbr(person.getTrnNbr());
			this.setFirstName(person.getFirstName());
			this.setLastName(person.getLastName());
			this.setMiddleName(person.getMiddleName());	
			
			this.setAddress(new PersonDO().getAddress());
			this.getAddress().setMarkText(person.getMarkText());
			
			if(person.getParishCode()!=null){
				this.getAddress().parish = new ParishDO();
				this.getAddress().parish.parishCode = person.getParishCode();
			}
		
			this.getAddress().setPoBoxNo(person.getPoBoxNo());
			this.getAddress().setPoLocationName(person.getPoLocationName());
			this.getAddress().setStreetName(person.getStreetName());
			this.getAddress().setStreetNo(person.getStreetNo());
			this.setTelephoneCell(person.getTelephoneCell());
			this.setTelephoneHome(person.getTelephoneHome());
			this.setTelephoneWork(person.getTelephoneWork());
			this.dlNo = person.getDlNo();
			this.auditEntry = new AuditEntry();
		}
		
	}



	@Id
	@Column(name="person_id")
	@GeneratedValue(strategy=GenerationType.AUTO)
	Integer personId;
	
	@Column(name="trn_nbr")
	String trnNbr;
	
	@Column(name="dl_no")
	String dlNo;
	
	@Column(name="first_name")
	String firstName;
	
	@Column(name="mid_name")
	String middleName;
	
	@Column(name="last_name")
	String lastName;

	@Column(name="tel_home")
	String telephoneHome;
	
	@Column(name="tel_cell")	
	String telephoneCell;
	
	@Column(name="tel_work")
	String telephoneWork;
	
	@Embedded
	AddressDO address;
	
	@Column(name="occupation")
	String occupation;
	
	@Embedded
	AuditEntry auditEntry;
	
	@Version
	@Column(name="version_nbr")
	Integer versionNbr;
	

	
	public Integer getPersonId() {
		return personId;
	}

	public void setPersonId(Integer personId) {
		this.personId = personId;
	}

	public String getTrnNbr() {
		return trnNbr;
	}

	public void setTrnNbr(String trnNbr) {
		this.trnNbr = trnNbr;
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

	
	public AddressDO getAddress() {
		if(address == null){
			address = new AddressDO();
		}
		return address;
	}

	public void setAddress(AddressDO address) {
		this.address = address;
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

	public String getOccupation() {
		return occupation;
	}


	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}



	public String getDlNo()
	{
		return dlNo;
	}



	public void setDlNo(String dlNo)
	{
		this.dlNo = dlNo;
	}
	
	
}
