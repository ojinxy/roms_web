package fsl.ta.toms.roms.bo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import fsl.ta.toms.roms.dataobjects.RoadCheckDO;
import fsl.ta.toms.roms.dataobjects.RoadLicCheckResultDO;

public class RoadLicCheckResultBO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4423102533099111294L;
	Integer roadLicCheckId;
	RoadCheckDO roadCheck;
	Integer licenceNo;
	Date expiryDate;
	Date issueDate;
	String controlNumber;
	String licenceType;
	String licenceRoute;
	String vehicleMakeDesc;
	String vehicleModelDesc;
	String vehiclePlateRegNo;
	String status;
	String statusCode;
	
	String currentUserName;
	
	ComplianceBO complianceBO;
	String comment;
	
	
	String routeStart;
	String routeEnd;
	Integer seatCapacity;
	
	List<RoadLicenceOwnerBO> roadLicenceOwners;
	
	LMISApplicationBO lmisApplicationBO;


	/**RFC-ROMS-0008
	 * R.B.
	 * 22/07/2014
	 */
	//Driver Badge Details
	List<RoadLicenceDriverConductorBO> assignedDriverConductor;
	
	//Fitness Details
	String fitnessNumber, depot;
	Date fitnessIssueDate, fitnessExpDate;
		
	//Insurance Details
	String insuranceComp;
	Date insuranceIssueDate, insuranceExpDate;
	
	
	
	public RoadLicCheckResultBO(Integer roadLicCheckId,
			RoadCheckDO roadCheck, Integer licenceNo, Date expiryDate,
			String licenceType, String licenceRoute, String vehicleMakeDesc,
			String vehicleModelDesc, String vehiclePlateRegNo,
			String currentUser, ComplianceBO complianceBO, String comment, String status) {
		super();
		this.roadLicCheckId = roadLicCheckId;
		this.roadCheck = roadCheck;
		this.licenceNo = licenceNo;
		this.expiryDate = expiryDate;
		this.licenceType = licenceType;
		this.licenceRoute = licenceRoute;
		this.vehicleMakeDesc = vehicleMakeDesc;
		this.vehicleModelDesc = vehicleModelDesc;
		this.vehiclePlateRegNo = vehiclePlateRegNo;
		this.currentUserName = currentUser;
		this.complianceBO = complianceBO;
		this.comment = comment;
		this.status = status;
	}
	
	
	
	public RoadLicCheckResultBO(RoadLicCheckResultDO roadLicCheckRsltDO) {
		super();
		
		this.roadLicCheckId = roadLicCheckRsltDO.getRoadLicCheckId();
		this.roadCheck = roadLicCheckRsltDO.getRoadCheck();
		this.licenceNo = roadLicCheckRsltDO.getLicenceNo();
		this.expiryDate = roadLicCheckRsltDO.getExpiryDate();
		this.licenceType = roadLicCheckRsltDO.getLicenceType();
		this.licenceRoute = roadLicCheckRsltDO.getLicenceRoute();
		this.vehicleMakeDesc = roadLicCheckRsltDO.getVehicleMakeDesc();
		this.vehicleModelDesc = roadLicCheckRsltDO.getVehicleModelDesc();
		this.vehiclePlateRegNo = roadLicCheckRsltDO.getVehiclePlateRegNo();
		this.lmisApplicationBO = new LMISApplicationBO();
		this.lmisApplicationBO.setApplDate(roadLicCheckRsltDO.getApplDate());
		this.lmisApplicationBO.setApplNo(roadLicCheckRsltDO.getApplNo());
		this.lmisApplicationBO.setLicDesc(roadLicCheckRsltDO.getApplType());
		this.lmisApplicationBO.setStatusDesc(roadLicCheckRsltDO.getApplStatus());
		
	}

	public RoadLicCheckResultBO(RoadLicCheckResultDO roadLicCheckRsltDO, String routeStart, String routeEnd,  Integer seatCapacity) {
		super();
		
		this.roadLicCheckId = roadLicCheckRsltDO.getRoadLicCheckId();
		this.roadCheck = roadLicCheckRsltDO.getRoadCheck();
		this.licenceNo = roadLicCheckRsltDO.getLicenceNo();
		this.expiryDate = roadLicCheckRsltDO.getExpiryDate();
		this.licenceType = roadLicCheckRsltDO.getLicenceType();
		this.licenceRoute = roadLicCheckRsltDO.getLicenceRoute();
		this.vehicleMakeDesc = roadLicCheckRsltDO.getVehicleMakeDesc();
		this.vehicleModelDesc = roadLicCheckRsltDO.getVehicleModelDesc();
		this.vehiclePlateRegNo = roadLicCheckRsltDO.getVehiclePlateRegNo();
		this.routeStart = routeStart;
		this.routeEnd = routeEnd;
		this.seatCapacity = seatCapacity;
		this.status = roadLicCheckRsltDO.getStatus_desc();
		this.lmisApplicationBO = new LMISApplicationBO();
		this.lmisApplicationBO.setApplDate(roadLicCheckRsltDO.getApplDate());
		this.lmisApplicationBO.setApplNo(roadLicCheckRsltDO.getApplNo());
		this.lmisApplicationBO.setLicDesc(roadLicCheckRsltDO.getApplType());
		this.lmisApplicationBO.setStatusDesc(roadLicCheckRsltDO.getApplStatus());
	}

	
	

	public RoadLicCheckResultBO(RoadLicCheckResultDO roadLicCheckRsltDO, RoadLicenceBO roadLicDetails) {
		super();
		
		this.roadLicCheckId = roadLicCheckRsltDO.getRoadLicCheckId();
		this.roadCheck = roadLicCheckRsltDO.getRoadCheck();
		this.licenceNo = roadLicCheckRsltDO.getLicenceNo();
		this.expiryDate = roadLicCheckRsltDO.getExpiryDate();
		this.licenceType = roadLicCheckRsltDO.getLicenceType();
		this.licenceRoute = roadLicCheckRsltDO.getLicenceRoute();
		this.vehicleMakeDesc = roadLicCheckRsltDO.getVehicleMakeDesc();
		this.vehicleModelDesc = roadLicCheckRsltDO.getVehicleModelDesc();
		this.vehiclePlateRegNo = roadLicCheckRsltDO.getVehiclePlateRegNo();
		this.routeStart = roadLicDetails.getRouteStart();
		this.routeEnd = roadLicDetails.getRouteEnd();
		this.seatCapacity = roadLicDetails.getSeatCapacity();
		this.status = roadLicCheckRsltDO.getStatus_desc();
		this.assignedDriverConductor = roadLicDetails.getAssignedDriverConductor();
		this.fitnessNumber = roadLicDetails.getFitnessNumber();
		this.depot = roadLicDetails.getDepot();
		this.fitnessIssueDate = roadLicDetails.getFitnessIssueDate();
		this.fitnessExpDate = roadLicDetails.getFitnessExpDate();
		this.insuranceComp = roadLicDetails.getInsuranceComp();
		this.insuranceIssueDate = roadLicDetails.getInsuranceIssueDate();
		this.insuranceExpDate = roadLicDetails.getInsuranceExpDate();
		this.controlNumber = roadLicCheckRsltDO.getControlNbr() != null ? roadLicCheckRsltDO.getControlNbr().toString(): null;
		this.issueDate = roadLicCheckRsltDO.getIssueDate();
		this.setLmisApplicationBO(roadLicDetails.getLmisApplicationBO());
	}



	public RoadLicCheckResultBO(RoadLicenceBO roadLicDetails) {
		super();
		
		//this.roadLicCheckId = roadLicCheckRsltDO.getRoadLicCheckId();
		//this.roadCheck = roadLicCheckRsltDO.getRoadCheck();
		this.licenceNo = roadLicDetails.getLicenceNo();
		this.expiryDate = roadLicDetails.getExpiryDate();
		this.licenceType = roadLicDetails.getLicType();
		this.licenceRoute = roadLicDetails.getRouteDesc();
		this.vehicleMakeDesc = roadLicDetails.getVehMakeDesc();
		this.vehicleModelDesc = roadLicDetails.getModelDesc();
		this.vehiclePlateRegNo = roadLicDetails.getLicPlate();
		this.routeStart = roadLicDetails.getRouteStart();
		this.routeEnd = roadLicDetails.getRouteEnd();
		this.seatCapacity = roadLicDetails.getSeatCapacity();
		this.status = roadLicDetails.getStatusDesc();
		this.assignedDriverConductor = roadLicDetails.getAssignedDriverConductor();
		this.fitnessNumber = roadLicDetails.getFitnessNumber();
		this.depot = roadLicDetails.getDepot();
		this.fitnessIssueDate = roadLicDetails.getFitnessIssueDate();
		this.fitnessExpDate = roadLicDetails.getFitnessExpDate();
		this.insuranceComp = roadLicDetails.getInsuranceComp();
		this.insuranceIssueDate = roadLicDetails.getInsuranceIssueDate();
		this.insuranceExpDate = roadLicDetails.getInsuranceExpDate();
		this.controlNumber = roadLicDetails.getControlNo() != null ? roadLicDetails.getControlNo().toString(): null;
		this.issueDate = roadLicDetails.getIssueDate();
		this.roadLicenceOwners = roadLicDetails.getLicenceOwners();
		this.setLmisApplicationBO(roadLicDetails.getLmisApplicationBO());
	}
	
	public RoadLicCheckResultBO() {
		super();

	}



	public Integer getRoadLicCheckId() {
		return roadLicCheckId;
	}
	public void setRoadLicCheckId(Integer roadLicCheckId) {
		this.roadLicCheckId = roadLicCheckId;
	}
	public RoadCheckDO getRoadCheck() {
		return roadCheck;
	}
	public void setRoadCheck(RoadCheckDO roadCheck) {
		this.roadCheck = roadCheck;
	}
	public Integer getLicenceNo() {
		return licenceNo;
	}
	public void setLicenceNo(Integer licenceNo) {
		this.licenceNo = licenceNo;
	}
	public Date getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}
	public String getLicenceType() {
		return licenceType;
	}
	public void setLicenceType(String licenceType) {
		this.licenceType = licenceType;
	}
	public String getLicenceRoute() {
		return licenceRoute;
	}
	public void setLicenceRoute(String licenceRoute) {
		this.licenceRoute = licenceRoute;
	}
	public String getVehicleMakeDesc() {
		return vehicleMakeDesc;
	}
	public void setVehicleMakeDesc(String vehicleMakeDesc) {
		this.vehicleMakeDesc = vehicleMakeDesc;
	}
	public String getVehicleModelDesc() {
		return vehicleModelDesc;
	}
	public void setVehicleModelDesc(String vehicleModelDesc) {
		this.vehicleModelDesc = vehicleModelDesc;
	}
	public String getVehiclePlateRegNo() {
		return vehiclePlateRegNo;
	}
	public void setVehiclePlateRegNo(String vehiclePlateRegNo) {
		this.vehiclePlateRegNo = vehiclePlateRegNo;
	}
	public String getCurrentUserName() {
		return currentUserName;
	}
	public void setCurrentUserName(String currentUserName) {
		this.currentUserName = currentUserName;
	}
	
	public ComplianceBO getComplianceBO() {
		return complianceBO;
	}



	public void setComplianceBO(ComplianceBO complianceBO) {
		this.complianceBO = complianceBO;
	}



	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}



	public String getStatus() {
		return status;
	}



	public void setStatus(String status) {
		this.status = status;
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



	public List<RoadLicenceOwnerBO> getRoadLicenceOwners()
	{
		return roadLicenceOwners;
	}



	public void setRoadLicenceOwners(List<RoadLicenceOwnerBO> roadLicenceOwners)
	{
		this.roadLicenceOwners = roadLicenceOwners;
	}



	public List<RoadLicenceDriverConductorBO> getAssignedDriverConductor() {
		return assignedDriverConductor;
	}



	public void setAssignedDriverConductor(
			List<RoadLicenceDriverConductorBO> assignedDriverConductor) {
		this.assignedDriverConductor = assignedDriverConductor;
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



	public String getInsuranceComp() {
		return insuranceComp;
	}



	public void setInsuranceComp(String insuranceComp) {
		this.insuranceComp = insuranceComp;
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



	public String getStatusCode()
	{
		return statusCode;
	}



	public void setStatusCode(String statusCode)
	{
		this.statusCode = statusCode;
	}



	public Date getIssueDate()
	{
		return issueDate;
	}



	public void setIssueDate(Date issueDate)
	{
		this.issueDate = issueDate;
	}



	public String getControlNumber()
	{
		return controlNumber;
	}



	public void setControlNumber(String controlNumber)
	{
		this.controlNumber = controlNumber;
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
