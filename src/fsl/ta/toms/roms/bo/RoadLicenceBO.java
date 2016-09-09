/**
 * Created By: oanguin
 * Date: May 3, 2013
 *
 */
package fsl.ta.toms.roms.bo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;

/**
 * @author oanguin
 * Created Date: May 3, 2013
 */
public class RoadLicenceBO implements Serializable 
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RoadLicenceBO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	Integer licenceNo;
	String licType;
	Date issueDate;
	Date expiryDate;
	String licPlate;
	String statusDesc;
	String statusCode;
	String routeDesc;
	String vehMakeDesc;
	String modelDesc;
	Integer modelYear;
	List<RoadLicenceOwnerBO> licenceOwners;
	String routeStart;
	String routeEnd;
	Integer seatCapacity;
	
	/**RFC-ROMS-0008
	 * R.B.
	 * 22/07/2014
	 */
	Integer controlNo;
	
	//Driver and Conductor Badge Details
	List<RoadLicenceDriverConductorBO> assignedDriverConductor;
	
	//Fitness Details
	String fitnessNumber, depot;
	Date fitnessIssueDate, fitnessExpDate;
		
	//Insurance Details
	String insuranceComp;
	Date insuranceIssueDate, insuranceExpDate;
	
	LMISApplicationBO lmisApplicationBO;
	
	public Integer getLicenceNo() {
		return licenceNo;
	}



	public void setLicenceNo(Integer licenceNo) {
		this.licenceNo = licenceNo;
	}



	public String getLicType() {
		return licType;
	}



	public void setLicType(String licType) {
		this.licType = licType;
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



	public String getLicPlate() {
		return licPlate;
	}



	public void setLicPlate(String licPlate) {
		this.licPlate = licPlate;
	}



	public String getStatusDesc() {
		return statusDesc;
	}



	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}



	public String getRouteDesc() {
		return routeDesc;
	}



	public void setRouteDesc(String routeDesc) {
		this.routeDesc = routeDesc;
	}



	public String getVehMakeDesc() {
		return vehMakeDesc;
	}



	public void setVehMakeDesc(String vehMakeDesc) {
		this.vehMakeDesc = vehMakeDesc;
	}



	public String getModelDesc() {
		return modelDesc;
	}



	public void setModelDesc(String modelDesc) {
		this.modelDesc = modelDesc;
	}



	public Integer getModelYear() {
		return modelYear;
	}



	public void setModelYear(Integer modelYear) {
		this.modelYear = modelYear;
	}



	public List<RoadLicenceOwnerBO> getLicenceOwners() {
		return licenceOwners;
	}



	public void setLicenceOwners(List<RoadLicenceOwnerBO> licenceOwners) {
		this.licenceOwners = licenceOwners;
	}



	public String getRouteStart() {
		return routeStart;
	}



	public void setRouteStart(String routeStart) {
		this.routeStart = routeStart;
	}



	public String getRouteEnd() {
		return routeEnd;
	}



	public void setRouteEnd(String routeEnd) {
		this.routeEnd = routeEnd;
	}



	public Integer getSeatCapacity() {
		return seatCapacity;
	}



	public void setSeatCapacity(Integer seatCapacity) {
		this.seatCapacity = seatCapacity;
	}


	
	
	public RoadLicenceBO(Integer licenceNo, String licType, Date issueDate,
			Date expiryDate, String licPlate, String statusDesc,
			String statusCode, String routeDesc, String vehMakeDesc,
			String modelDesc, Integer modelYear,
			List<RoadLicenceOwnerBO> licenceOwners, String routeStart,
			String routeEnd, Integer seatCapacity, Integer controlNo,
			List<RoadLicenceDriverConductorBO> assignedDriverConductor,
			String fitnessNumber, String depot, Date fitnessIssueDate,
			Date fitnessExpDate, String insuranceComp, Date insuranceIssueDate,
			Date insuranceExpDate) {
		super();
		this.licenceNo = licenceNo;
		this.licType = licType;
		this.issueDate = issueDate;
		this.expiryDate = expiryDate;
		this.licPlate = licPlate;
		this.statusDesc = statusDesc;
		this.statusCode = statusCode;
		this.routeDesc = routeDesc;
		this.vehMakeDesc = vehMakeDesc;
		this.modelDesc = modelDesc;
		this.modelYear = modelYear;
		this.licenceOwners = licenceOwners;
		this.routeStart = routeStart;
		this.routeEnd = routeEnd;
		this.seatCapacity = seatCapacity;
		this.controlNo = controlNo;
		this.assignedDriverConductor = assignedDriverConductor;
		this.fitnessNumber = fitnessNumber;
		this.depot = depot;
		this.fitnessIssueDate = fitnessIssueDate;
		this.fitnessExpDate = fitnessExpDate;
		this.insuranceComp = insuranceComp;
		this.insuranceIssueDate = insuranceIssueDate;
		this.insuranceExpDate = insuranceExpDate;
		
	}



	public RoadLicenceBO(Integer licenceNo, String licType, Date issueDate,
			Date expiryDate, String licPlate, String statusDesc,
			String routeDesc, String vehMakeDesc, String modelDesc,
			Integer modelYear, List<RoadLicenceOwnerBO> licenceOwners, String routeStart,String routeEnd,  Integer seatCapacity) {
		super();
		this.licenceNo = licenceNo;
		this.licType = licType;
		this.issueDate = issueDate;
		this.expiryDate = expiryDate;
		this.licPlate = licPlate;
		this.statusDesc = statusDesc;
		this.routeDesc = routeDesc;
		this.vehMakeDesc = vehMakeDesc;
		this.modelDesc = modelDesc;
		this.modelYear = modelYear;
		this.licenceOwners = licenceOwners;
		this.routeStart = routeStart;
		this.routeEnd = routeEnd;
		this.seatCapacity = seatCapacity;
		
	}




	public String getFitnessNumber() {
		return fitnessNumber;
	}



	public void setFitnessNumber(String fitnessNumber) {
		this.fitnessNumber = fitnessNumber;
	}



	public String getDepot() {
		return depot;
	}



	public void setDepot(String depot) {
		this.depot = depot;
	}



	public Date getFitnessIssueDate() {
		return fitnessIssueDate;
	}



	public void setFitnessIssueDate(Date fitnessIssueDate) {
		this.fitnessIssueDate = fitnessIssueDate;
	}



	public Date getFitnessExpDate() {
		return fitnessExpDate;
	}



	public void setFitnessExpDate(Date fitnessExpDate) {
		this.fitnessExpDate = fitnessExpDate;
	}



	public Date getInsuranceIssueDate() {
		return insuranceIssueDate;
	}



	public void setInsuranceIssueDate(Date insuranceIssueDate) {
		this.insuranceIssueDate = insuranceIssueDate;
	}



	public Date getInsuranceExpDate() {
		return insuranceExpDate;
	}



	public void setInsuranceExpDate(Date insuranceExpDate) {
		this.insuranceExpDate = insuranceExpDate;
	}



	public String getInsuranceComp() {
		return insuranceComp;
	}



	public void setInsuranceComp(String insuranceComp) {
		this.insuranceComp = insuranceComp;
	}


	public List<RoadLicenceDriverConductorBO> getAssignedDriverConductor() {
		return assignedDriverConductor;
	}



	public void setAssignedDriverConductor(
			List<RoadLicenceDriverConductorBO> assignedDriverConductor) {
		this.assignedDriverConductor = assignedDriverConductor;
	}



	public Integer getControlNo() {
		return controlNo;
	}



	public void setControlNo(Integer controlNo) {
		this.controlNo = controlNo;
	}



	public String getStatusCode()
	{
		return statusCode;
	}



	public void setStatusCode(String statusCode)
	{
		this.statusCode = statusCode;
	}



	public LMISApplicationBO getLmisApplicationBO()
	{
		return lmisApplicationBO;
	}



	public void setLmisApplicationBO(LMISApplicationBO lmisApplicationBO)
	{
		this.lmisApplicationBO = lmisApplicationBO;
	}
	
	
	
	

}
