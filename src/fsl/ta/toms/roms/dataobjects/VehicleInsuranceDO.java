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
@Table(name="ROMS_vehicle_insurance")
public class VehicleInsuranceDO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 469493562851685088L;



	public VehicleInsuranceDO() {
		super();
		// TODO Auto-generated constructor stub
	}
		


	public VehicleInsuranceDO(VehicleCheckResultDO vehicleCheckResult,
			String policyNo, String companyName, Date issueDate,
			Date expiryDate, AuditEntry auditEntry) {
		super();
		this.vehicleCheckResult = vehicleCheckResult;
		this.policyNo = policyNo;
		this.companyName = companyName;
		this.issueDate = issueDate;
		this.expiryDate = expiryDate;
		this.auditEntry = auditEntry;
	}



	@Id
	@Column(name="insurance_id")
	@GeneratedValue(strategy=GenerationType.AUTO)
	Integer insuranceId;
	
	@ManyToOne
	@JoinColumn(name="vehicle_check_id")
	VehicleCheckResultDO vehicleCheckResult;
	
	@Column(name="policy_no")
	String policyNo;
	
	@Column(name="company_name")
	String companyName;
	
	@Column(name="issue_date")
	Date issueDate;
	
	@Column(name="expiry_date")
	Date expiryDate;
	
	@Embedded
	AuditEntry auditEntry;
	
	@Version
	@Column(name="version_nbr")
	Integer versionNbr;



	public Integer getInsuranceId() {
		return insuranceId;
	}

	public void setInsuranceId(Integer insuranceId) {
		this.insuranceId = insuranceId;
	}

	public VehicleCheckResultDO getVehicleCheckResult() {
		return vehicleCheckResult;
	}

	public void setVehicleCheckResult(VehicleCheckResultDO vehicleCheckResult) {
		this.vehicleCheckResult = vehicleCheckResult;
	}

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
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
