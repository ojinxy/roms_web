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
@Table(name="ROMS_vehicle_owner")
public class VehicleOwnerDO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5290877841997218441L;


	public VehicleOwnerDO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public VehicleOwnerDO(VehicleDO vehicle,
			String trnNbr, String trnBranch, String firstName, String midName,
			String lastName, AddressDO addressDO,String city, String parishDesc, 
			String country, AuditEntry auditEntry) {
		super();
		this.vehicle = vehicle;
		this.trnNbr = trnNbr;
		this.trnBranch = trnBranch;
		this.firstName = firstName;
		this.midName = midName;
		this.lastName = lastName;
		this.auditEntry = auditEntry;
		
		if(addressDO != null)
		{
			this.address = new AddressDO(addressDO.getMarkText(), addressDO.getStreetNo(), addressDO.getStreetName(),
				addressDO.getPoLocationName(), addressDO.getPoBoxNo(), null);
		}	
		
		this.city = city;
		this.parishDesc = parishDesc;
		this.country = country;
	}


	
	@Id
	@Column(name="owner_id")
	@GeneratedValue(strategy=GenerationType.AUTO)
	Integer ownerId;
	
	@ManyToOne
	@JoinColumn(name="vehicle_id")
	VehicleDO vehicle;
	
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


	public VehicleDO getVehicle() {
		return vehicle;
	}

	public void setVehicle(VehicleDO vehicle) {
		this.vehicle = vehicle;
	}

	public AddressDO getAddress() {
		return address;
	}

	public void setAddress(AddressDO address) {
		this.address = address;
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
