package fsl.ta.toms.roms.bo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class VehicleCheckResultBO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8129571607961611560L;
	Integer vehicleCheckId;
	String plateRegNo,engineNo,chassisNo,colour,makeDescription,model,laNo;
	Date mvrcExpiryDate;
	Date mvrcIssueDate;
	Integer year;
	String typeDesc;
	String typeCode;
	List<VehicleOwnerBO> vehicleOwners;
	VehicleInsuranceBO insuranceInfo;
	VehicleFitnessBO fitnessInfo;
	String currentUserName;
	String comment;
	RoadCheckBO roadCheckBO;
	Integer complianceID;
	Boolean onActiveRoadOperation;
	//RoadLicenceBO roadLicDetails;
	
	Integer vehicleId;
	
	public VehicleCheckResultBO() 
	{
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	public Integer getVehicleCheckId() {
		return vehicleCheckId;
	}
	public void setVehicleCheckId(Integer vehicleCheckId) {
		this.vehicleCheckId = vehicleCheckId;
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
	public String getLaNo() {
		return laNo;
	}
	public void setLaNo(String laNo) {
		this.laNo = laNo;
	}
	public Date getMvrcExpiryDate() {
		return mvrcExpiryDate;
	}
	public void setMvrcExpiryDate(Date mvrcExpiryDate) {
		this.mvrcExpiryDate = mvrcExpiryDate;
	}
	public Integer getYear() {
		return year;
	}
	public void setYear(Integer year) {
		this.year = year;
	}
	

	public List<VehicleOwnerBO> getVehicleOwners() {
		return vehicleOwners;
	}


	public void setVehicleOwners(List<VehicleOwnerBO> vehicleOwners) {
		this.vehicleOwners = vehicleOwners;
	}


	public VehicleInsuranceBO getInsuranceInfo() {
		return insuranceInfo;
	}
	public void setInsuranceInfo(VehicleInsuranceBO insuranceInfo) {
		this.insuranceInfo = insuranceInfo;
	}
	public VehicleFitnessBO getFitnessInfo() {
		return fitnessInfo;
	}
	public void setFitnessInfo(VehicleFitnessBO fitnessInfo) {
		this.fitnessInfo = fitnessInfo;
	}
	public String getCurrentUserName() {
		return currentUserName;
	}
	public void setCurrentUserName(String currentUserName) {
		this.currentUserName = currentUserName;
	}

	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}


	public String getTypeDesc() {
		return typeDesc;
	}


	public void setTypeDesc(String typeDesc) {
		this.typeDesc = typeDesc;
	}


	public RoadCheckBO getRoadCheckBO() {
		return roadCheckBO;
	}


	public void setRoadCheckBO(RoadCheckBO roadCheckBO) {
		this.roadCheckBO = roadCheckBO;
	}


	public Integer getComplianceID() {
		return complianceID;
	}


	public void setComplianceID(Integer complianceID) {
		this.complianceID = complianceID;
	}


	

	public Date getMvrcIssueDate()
	{
		return mvrcIssueDate;
	}


	public void setMvrcIssueDate(Date mvrcIssueDate)
	{
		this.mvrcIssueDate = mvrcIssueDate;
	}


	public String getTypeCode()
	{
		return typeCode;
	}


	public void setTypeCode(String typeCode)
	{
		this.typeCode = typeCode;
	}


	public Boolean getOnActiveRoadOperation()
	{
		return onActiveRoadOperation;
	}


	public void setOnActiveRoadOperation(Boolean onActiveRoadOperation)
	{
		this.onActiveRoadOperation = onActiveRoadOperation;
	}


	public Integer getVehicleId() {
		return vehicleId;
	}


	public void setVehicleId(Integer vehicleId) {
		this.vehicleId = vehicleId;
	}


	
	
	


	
	
}
