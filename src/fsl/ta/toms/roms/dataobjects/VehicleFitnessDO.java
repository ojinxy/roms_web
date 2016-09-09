package fsl.ta.toms.roms.dataobjects;

import java.io.Serializable;
import java.util.Date;

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
@Table(name="ROMS_vehicle_fitness")
public class VehicleFitnessDO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 642112286867643397L;

	public VehicleFitnessDO() {
		super();
		// TODO Auto-generated constructor stub
	}
		

	public VehicleFitnessDO(VehicleCheckResultDO vehicleCheckResult,
			String fitnessNo, String examDepot, Date issueDate,
			Date expiryDate, AuditEntry auditEntry) {
		super();
		this.vehicleCheckResult = vehicleCheckResult;
		this.fitnessNo = fitnessNo;
		this.examDepot = examDepot;
		this.issueDate = issueDate;
		this.expiryDate = expiryDate;
		this.auditEntry = auditEntry;
	}


	@Id
	@Column(name="fitness_id")
	@GeneratedValue(strategy=GenerationType.AUTO)
	Integer fitnessId;
	
	@ManyToOne
	@JoinColumn(name="vehicle_check_id")
	VehicleCheckResultDO vehicleCheckResult;
	
	@Column(name="fitness_no")
	String fitnessNo;
	
	@Column(name="exam_depot")
	String examDepot;
	
	@Column(name="issue_date")
	Date issueDate;
	
	@Column(name="expiry_date")
	Date expiryDate;
	
	@Embedded
	AuditEntry auditEntry;
	
	@Version
	@Column(name="version_nbr")
	Integer versionNbr;

	public Integer getFitnessId() {
		return fitnessId;
	}

	public void setFitnessId(Integer fitnessId) {
		this.fitnessId = fitnessId;
	}

	public VehicleCheckResultDO getVehicleCheckResult() {
		return vehicleCheckResult;
	}

	public void setVehicleCheckResult(VehicleCheckResultDO vehicleCheckResult) {
		this.vehicleCheckResult = vehicleCheckResult;
	}

	public String getFitnessNo() {
		return fitnessNo;
	}

	public void setFitnessNo(String fitnessNo) {
		this.fitnessNo = fitnessNo;
	}

	public String getExamDepot() {
		return examDepot;
	}

	public void setExamDepot(String examDepot) {
		this.examDepot = examDepot;
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

	public Date getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}


}
