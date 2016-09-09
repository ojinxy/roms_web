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
@Table(name="ROMS_vehicle_check_result_owner")
public class VehicleCheckResultOwnerDO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3149086873645928796L;



	public VehicleCheckResultOwnerDO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public VehicleCheckResultOwnerDO(VehicleCheckResultDO vehicleCheckResult,
			String trnNbr, String trnBranch, String firstName, String midName,
			String lastName, AddressDO address, String city, String parishDesc, 
			String country,AuditEntry auditEntry) {
		super();
		this.vehicleCheckResult = vehicleCheckResult;
		this.trnNbr = trnNbr;
		this.trnBranch = trnBranch;
		this.firstName = firstName;
		this.midName = midName;
		this.lastName = lastName;
		this.auditEntry = auditEntry;
		
		this.address = address;

		this.city = city;
		this.parishDesc = parishDesc;
		this.country = country;
	}

	

	@Id
	@Column(name="owner_id")
	@GeneratedValue(strategy=GenerationType.AUTO)
	Integer ownerId;
	
	@ManyToOne
	@JoinColumn(name="vehicle_check_id")
	VehicleCheckResultDO vehicleCheckResult;
	
	@Column(name="trn_nbr")
	String trnNbr;
	
	@Column(name="trn_branch")
	String trnBranch;
	
	@Column(name="first_name")
	String firstName;
	
	@Column(name="mid_name")
	String midName;
	
	@Column(name="last_name")
	String lastName;
	
	@Embedded
	AddressDO address;
	
	
	@Column(name="parish_desc")
	String parishDesc;
	
	@Column(name="city")
	String city;
	
	@Column(name="country")
	String country;
	
	
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

	public VehicleCheckResultDO getVehicleCheckResult() {
		return vehicleCheckResult;
	}

	public void setVehicleCheckResult(VehicleCheckResultDO vehicleCheckResult) {
		this.vehicleCheckResult = vehicleCheckResult;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMidName() {
		return midName;
	}

	public void setMidName(String midName) {
		this.midName = midName;
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

	public String getParishDesc() {
		return parishDesc;
	}

	public void setParishDesc(String parishDesc) {
		this.parishDesc = parishDesc;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	
}
