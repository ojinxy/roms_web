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

import fsl.ta.toms.roms.bo.TAVehicleBO;

@Entity
@Table(name="ROMS_ta_vehicle")
public class TAVehicleDO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 765281596407044341L;
	
	public TAVehicleDO() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	@Id
	@Column(name="ta_vehicle_id")
	@GeneratedValue(strategy=GenerationType.AUTO)
	Integer taVehicleId;
	
	@ManyToOne
	@JoinColumn(name="vehicle_id")
	VehicleDO vehicle;
	
	@Column(name="office_loc_code")
	String officeLocationCode;
	
	@ManyToOne
	@JoinColumn(name="status_id")
	StatusDO status;
	
	@Embedded
	AuditEntry auditEntry;
	
	@Version
	@Column(name="version_nbr")
	Integer versionNbr;
	
	public Integer getTaVehicleId() {
		return taVehicleId;
	}

	public void setTaVehicleId(Integer taVehicleId) {
		this.taVehicleId = taVehicleId;
	}

	public VehicleDO getVehicle() {
		return vehicle;
	}

	public void setVehicle(VehicleDO vehicle) {
		this.vehicle = vehicle;
	}


	public String getOfficeLocationCode() {
		return officeLocationCode;
	}

	public void setOfficeLocationCode(String officeLocationCode) {
		this.officeLocationCode = officeLocationCode;
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

	
}
