package fsl.ta.toms.roms.bo;

import java.io.Serializable;
import java.util.Date;

public class VehicleFitnessBO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3311968481661111745L;
	Integer fitnessId;
	Integer vehicleCheckResultID;
	String fitnessNo,examDepot;
	Date issueDate, expiryDate;
	public Integer getFitnessId() {
		return fitnessId;
	}
	public void setFitnessId(Integer fitnessId) {
		this.fitnessId = fitnessId;
	}
	public Integer getVehicleCheckResultID() {
		return vehicleCheckResultID;
	}
	public void setVehicleCheckResultID(Integer vehicleCheckResultID) {
		this.vehicleCheckResultID = vehicleCheckResultID;
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
