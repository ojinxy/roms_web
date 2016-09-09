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

@Entity
@Table(name="roms_jp")
public class JPDO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5734888198087785433L;
	
	public JPDO() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Id
	@Column(name="id_number")
	@GeneratedValue(strategy=GenerationType.AUTO)
	Integer idNumber;	
	
	@Column(name="reg_number")
	String regNumber;
	
	@ManyToOne
	@JoinColumn(name="person_id")
	PersonDO person;
	
	@ManyToOne
	@JoinColumn(name="parish_code")
	ParishDO parish;
	
	//@Column(name="pin_username")
	//String pinUsername;
	
	@Column(name="pin_hash")
	String pinHash;
	
	@ManyToOne
	@JoinColumn(name="status_id")
	StatusDO status;
	
	@Embedded
	AuditEntry auditEntry;
	
	@Version
	@Column(name="version_nbr")
	Integer versionNbr;

	

	public String getRegNumber() {
		return regNumber;
	}

	public void setRegNumber(String regNumber) {
		this.regNumber = regNumber;
	}

	public PersonDO getPerson() {
		return person;
	}

	public void setPerson(PersonDO person) {
		this.person = person;
	}

	public ParishDO getParish() {
		return parish;
	}

	public void setParish(ParishDO parish) {
		this.parish = parish;
	}

	public String getPinHash() {
		return pinHash;
	}

	public void setPinHash(String pinHash) {
		this.pinHash = pinHash;
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

	public Integer getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(Integer idNumber) {
		this.idNumber = idNumber;
	}

	
}
