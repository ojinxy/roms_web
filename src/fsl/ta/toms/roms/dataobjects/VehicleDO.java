package fsl.ta.toms.roms.dataobjects;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name="ROMS_vehicle")
public class VehicleDO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6077249223749513612L;

	public VehicleDO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	@Id
	@Column(name="vehicle_id")
	@GeneratedValue(strategy=GenerationType.AUTO)
	Integer vehicleId;
	
	@Column(name="plate_reg_no")
	String plateRegNo;
	
	@Column(name="engine_no")
	String engineNo;
	
	@Column(name="chassis_no")
	String chassisNo;
	
	@Column(name="colour")
	String colour;
	
	@Column(name="make_desc")
	String makeDescription;
	
	@Column(name="model")
	String model;
	
	@Column(name="type_desc")
	String typeDesc;
	
	@Column(name="year")
	Integer year;
	
	@Column(name="owner_name")
	String ownerName;
	
	@Embedded
	AuditEntry auditEntry;
	
	@Version
	@Column(name="version_nbr")
	Integer versionNbr;

	public Integer getVehicleId() {
		return vehicleId;
	}

	public void setVehicleId(Integer vehicleId) {
		this.vehicleId = vehicleId;
	}

	public String getPlateRegNo() {
		return plateRegNo;
	}

	public void setPlateRegNo(String plateRegNo) {
		this.plateRegNo = plateRegNo;
	}

	public String getEngineNo() {
		return engineNo;
	}

	public void setEngineNo(String engineNo) {
		this.engineNo = engineNo;
	}

	public String getChassisNo() {
		return chassisNo;
	}

	public void setChassisNo(String chassisNo) {
		this.chassisNo = chassisNo;
	}

	public String getColour() {
		return colour;
	}

	public void setColour(String colour) {
		this.colour = colour;
	}

	public String getMakeDescription() {
		return makeDescription;
	}

	public void setMakeDescription(String makeDescription) {
		this.makeDescription = makeDescription;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getTypeDesc() {
		return typeDesc;
	}

	public void setTypeDesc(String typeDesc) {
		this.typeDesc = typeDesc;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
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

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}
	
	

}
