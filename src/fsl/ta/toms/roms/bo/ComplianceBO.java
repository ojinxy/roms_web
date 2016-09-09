package fsl.ta.toms.roms.bo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import fsl.ta.toms.roms.dataobjects.ComplianceDO;
import fsl.ta.toms.roms.dataobjects.ComplianceParamDO;
import fsl.ta.toms.roms.dataobjects.PersonDO;


public class ComplianceBO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 627863119133594318L;
	
	Integer complianceId,roadOperation;
	String taStaff,compliant,personRole,comment,otherRole;
	
	PersonBO person;
	VehicleBO vehicle;
	
	String currentUserName;
	
	Integer compliancyArteryid;
	
	List<OffenceBO> listOfOffences;
	
	List<RoadCheckBO> listOfRoadChecks;
	
	List<ComplianceParamBO>listOfComplianceParams;
	
	TAStaffBO taStaffBO;
	
	RoadOperationBO roadOperationBO;
	
	Date complianceDate;
	
	Date courtDate;
	
	CourtBO court;
	
	Date createdDate;
	
	String statusDescription;
	
	String statusId;
	
	Boolean vehicleInfoDifferent;
	
	PersonBO aidAbetVehicleOwner;
	
	Integer reasonId;
	
	public ComplianceBO() {
		super();
		// TODO Auto-generated constructor stub
	}


	public ComplianceBO(ComplianceDO complianceDo) 
	{
		if(complianceDo != null)
		{
			this.setComplianceId(complianceDo.getComplianceId());
			this.setPerson(new PersonBO(complianceDo.getPerson()));
			this.setComment(complianceDo.getComment());
			this.setCompliant(complianceDo.getCompliant());
			this.setPersonRole(complianceDo.getPersonRole());
			this.setOtherRole(complianceDo.getOtherRole());
			if(complianceDo.getCompliancyArtery() != null){
				this.setCompliancyArteryid(complianceDo.getCompliancyArtery().getArteryId());
			}
			
			this.statusDescription = complianceDo.getStatus().getDescription();
			
			this.statusId = complianceDo.getStatus().getStatusId();
			
			this.setComplianceDate(complianceDo.getOffenceDateTime());
			
			roadOperationBO = new RoadOperationBO(complianceDo.getRoadOperation());
			
			if(complianceDo.getRoadOperation() != null)
			{	this.setRoadOperation(complianceDo.getRoadOperation().getRoadOperationId());}
			
			taStaffBO = new TAStaffBO(complianceDo.getTaStaff());
			
			if(complianceDo.getTaStaff() != null)
			{	this.setTaStaff(complianceDo.getTaStaff().getStaffId());}
			
			this.setVehicle(new VehicleBO(complianceDo.getVehicle()));
			
			this.createdDate = complianceDo.getAuditEntry().getCreateDTime();
			
			this.courtDate = complianceDo.getCourtDateTime();
			
			if(complianceDo.getCourt() != null){
				this.setCourt(new CourtBO(complianceDo.getCourt()));}
			
			this.vehicleInfoDifferent = complianceDo.getVehicleInfoDifferent();
						
			if(complianceDo.getAidAbetVehicleOwner() != null){
				this.aidAbetVehicleOwner = new PersonBO(complianceDo.getAidAbetVehicleOwner());
			}else{
				this.aidAbetVehicleOwner = null;
			}
		}
		
	}


	public Integer getComplianceId() {
		return complianceId;
	}


	public void setComplianceId(Integer complianceId) {
		this.complianceId = complianceId;
	}


	public Integer getRoadOperation() {
		return roadOperation;
	}


	public void setRoadOperation(Integer roadOperation) {
		this.roadOperation = roadOperation;
	}


	public String getTaStaff() {
		return taStaff;
	}


	public void setTaStaff(String taStaff) {
		this.taStaff = taStaff;
	}


	public String getCompliant() {
		return compliant;
	}


	public void setCompliant(String compliant) {
		this.compliant = compliant;
	}


	public String getPersonRole() {
		return personRole;
	}


	public void setPersonRole(String personRole) {
		this.personRole = personRole;
	}


	public String getComment() {
		return comment;
	}


	public void setComment(String comment) {
		this.comment = comment;
	}

	
	public PersonBO getPerson() {
		return person;
	}


	public void setPerson(PersonBO person) {
		this.person = person;
	}


	public VehicleBO getVehicle() {
		return vehicle;
	}


	public void setVehicle(VehicleBO vehicle) {
		this.vehicle = vehicle;
	}


	public String getCurrentUserName() {
		return currentUserName;
	}


	public void setCurrentUserName(String currentUserName) {
		this.currentUserName = currentUserName;
	}


	public Integer getCompliancyArteryid() {
		return compliancyArteryid;
	}


	public void setCompliancyArteryid(Integer compliancyArteryid) {
		this.compliancyArteryid = compliancyArteryid;
	}


	public List<OffenceBO> getListOfOffences() {
		return listOfOffences;
	}


	public void setListOfOffences(List<OffenceBO> listOfOffences) {
		this.listOfOffences = listOfOffences;
	}


	public List<RoadCheckBO> getListOfRoadChecks() {
		return listOfRoadChecks;
	}


	public void setListOfRoadChecks(List<RoadCheckBO> listOfRoadChecks) {
		this.listOfRoadChecks = listOfRoadChecks;
	}


	public TAStaffBO getTaStaffBO() {
		return taStaffBO;
	}


	public void setTaStaffBO(TAStaffBO taStaffBO) {
		this.taStaffBO = taStaffBO;
	}


	public RoadOperationBO getRoadOperationBO() {
		return roadOperationBO;
	}


	public void setRoadOperationBO(RoadOperationBO roadOperationBO) {
		this.roadOperationBO = roadOperationBO;
	}


	public Date getComplianceDate() {
		return complianceDate;
	}


	public void setComplianceDate(Date complianceDate) {
		this.complianceDate = complianceDate;
	}


	public String getStatusDescription() {
		return statusDescription;
	}


	public void setStatusDescription(String statusDescription) {
		this.statusDescription = statusDescription;
	}


	public String getStatusId() {
		return statusId;
	}


	public void setStatusId(String statusId) {
		this.statusId = statusId;
	}


	public Date getCreatedDate() {
		return createdDate;
	}


	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}


	public List<ComplianceParamBO> getListOfComplianceParams() {
		return listOfComplianceParams;
	}


	public void setListOfComplianceParams(
			List<ComplianceParamBO> listOfComplianceParams) {
		this.listOfComplianceParams = listOfComplianceParams;
	}


	public Date getCourtDate()
	{
		return courtDate;
	}


	public void setCourtDate(Date courtDate)
	{
		this.courtDate = courtDate;
	}


	public Boolean getVehicleInfoDifferent()
	{
		return vehicleInfoDifferent;
	}


	public void setVehicleInfoDifferent(Boolean vehicleInfoDifferent)
	{
		this.vehicleInfoDifferent = vehicleInfoDifferent;
	}


	@Override
	public String toString() {
		return "ComplianceBO [complianceId=" + complianceId
				+ ", roadOperation=" + roadOperation + ", taStaff=" + taStaff
				+ ", compliant=" + compliant + ", personRole=" + personRole
				+ ", comment=" + comment + ", person=" + person + ", vehicle="
				+ vehicle + ", currentUserName=" + currentUserName
				+ ", compliancyArteryid=" + compliancyArteryid
				+ ", listOfOffences=" + listOfOffences + ", listOfRoadChecks="
				+ listOfRoadChecks + ", listOfComplianceParams="
				+ listOfComplianceParams + ", taStaffBO=" + taStaffBO
				+ ", roadOperationBO=" + roadOperationBO + ", complianceDate="
				+ complianceDate + ", courtDate=" + courtDate
				+", court=" + court
				+ ", createdDate=" + createdDate + ", statusDescription="
				+ statusDescription + ", statusId=" + statusId
				+ ", vehicleInfoDifferent=" + vehicleInfoDifferent + "]";
	}


	/**
	 * @return the aidAbetVehicleOwner
	 */
	public PersonBO getAidAbetVehicleOwner() {
		return aidAbetVehicleOwner;
	}


	/**
	 * @param aidAbetVehicleOwner the aidAbetVehicleOwner to set
	 */
	public void setAidAbetVehicleOwner(PersonBO aidAbetVehicleOwner) {
		this.aidAbetVehicleOwner = aidAbetVehicleOwner;
	}


	/**
	 * @return the court
	 */
	public CourtBO getCourt() {
		return court;
	}


	/**
	 * @param court the court to set
	 */
	public void setCourt(CourtBO court) {
		this.court = court;
	}


	public String getOtherRole() {
		return otherRole;
	}


	public void setOtherRole(String otherRole) {
		this.otherRole = otherRole;
	}


	public Integer getReasonId() {
		return reasonId;
	}


	public void setReasonId(Integer reasonId) {
		this.reasonId = reasonId;
	}




	
	

}
