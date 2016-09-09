package fsl.ta.toms.roms.bo;

import java.io.Serializable;
import java.util.Date;


public class VehicleInsuranceBO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1568752315068880909L;
	Integer insuranceId;
	Integer vehicleCheckResultID;
	String policyNo,companyName;
	Date issueDate, expiryDate;
	public Integer getInsuranceId() {
		return insuranceId;
	}
	public void setInsuranceId(Integer insuranceId) {
		this.insuranceId = insuranceId;
	}
	public Integer getVehicleCheckResultID() {
		return vehicleCheckResultID;
	}
	public void setVehicleCheckResultID(Integer vehicleCheckResultID) {
		this.vehicleCheckResultID = vehicleCheckResultID;
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
