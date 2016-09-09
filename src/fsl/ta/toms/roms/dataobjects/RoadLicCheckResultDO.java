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

import fsl.ta.toms.roms.bo.LMISApplicationBO;

@Entity
@Table(name="ROMS_road_lic_check_result")
public class RoadLicCheckResultDO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3570319939692618142L;

	public RoadLicCheckResultDO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
	public RoadLicCheckResultDO(RoadCheckDO roadCheck, Integer licenceNo,
			Date expiryDate, String licenceType, String licenceRoute,
			String vehicleMakeDesc, String vehicleModelDesc,
			String vehiclePlateRegNo, String status, AuditEntry auditEntry) {
		super();
		this.roadCheck = roadCheck;
		this.licenceNo = licenceNo;
		this.expiryDate = expiryDate;
		this.licenceType = licenceType;
		this.licenceRoute = licenceRoute;
		this.vehicleMakeDesc = vehicleMakeDesc;
		this.vehicleModelDesc = vehicleModelDesc;
		this.vehiclePlateRegNo = vehiclePlateRegNo;
		this.auditEntry = auditEntry;
		this.status_desc = status;
	}

	
	

	public RoadLicCheckResultDO(RoadCheckDO roadCheck, Integer licenceNo,
			Date expiryDate, Date issueDate,String licenceType, String licenceRoute,
			String vehicleMakeDesc, String vehicleModelDesc,
			String vehiclePlateRegNo, String status_desc, String fitnessNbr,
			String fitnessDepot, Date fitnessIssueDate, Date fitnessExpDate,
			String insuranceComp, Date insuranceIssueDate,
			Date insuranceExpDate, Integer controlNbr, AuditEntry auditEntry, LMISApplicationBO lmisApplication) {
		super();
		this.roadCheck = roadCheck;
		this.licenceNo = licenceNo;
		this.expiryDate = expiryDate;
		this.licenceType = licenceType;
		this.licenceRoute = licenceRoute;
		this.vehicleMakeDesc = vehicleMakeDesc;
		this.vehicleModelDesc = vehicleModelDesc;
		this.vehiclePlateRegNo = vehiclePlateRegNo;
		this.status_desc = status_desc;
		this.fitnessNbr = fitnessNbr;
		this.fitnessDepot = fitnessDepot;
		this.fitnessIssueDate = fitnessIssueDate;
		this.fitnessExpDate = fitnessExpDate;
		this.insuranceComp = insuranceComp;
		this.insuranceIssueDate = insuranceIssueDate;
		this.insuranceExpDate = insuranceExpDate;
		this.controlNbr = controlNbr;
		this.issueDate = issueDate;
		this.auditEntry = auditEntry;
		
		if(lmisApplication != null){
			this.applDate = lmisApplication.getApplDate();
			this.applNo = lmisApplication.getApplNo();
			this.applStatus = lmisApplication.getStatusDesc();
			this.applType = lmisApplication.getLicDesc();
		}
	}




	@Id
	@Column(name="road_lic_check_id")
	@GeneratedValue(strategy=GenerationType.AUTO)
	Integer roadLicCheckId;
	
	@ManyToOne
	@JoinColumn(name="road_check_id")
	RoadCheckDO roadCheck;
	
	@Column(name="licence_no")
	Integer licenceNo;
	
	@Column(name="expiry_date")
	Date expiryDate;
	
	@Column(name="issue_date")
	Date issueDate;
	
	@Column(name="licence_type")
	String licenceType;
	
	@Column(name="licence_route")
	String licenceRoute;
	
	@Column(name="vehicle_make_desc")
	String vehicleMakeDesc;
	
	@Column(name="vehicle_model_desc")
	String vehicleModelDesc;
	
	@Column(name="vehicle_plate_reg_no")
	String vehiclePlateRegNo;
	
	@Column(name="status_desc")
	String status_desc;
	
	@Column(name="fitness_nbr")
	String fitnessNbr;
	
	@Column(name="fitness_depot")
	String fitnessDepot;

	@Column(name="fitness_issue_date")
	Date fitnessIssueDate;
	
	@Column(name="fitness_exp_date")
	Date fitnessExpDate;

	@Column(name="insurance_comp")
	String insuranceComp;
	
	@Column(name="insurance_issue_date")
	Date insuranceIssueDate;
	
	@Column(name="insurance_exp_date")
	Date insuranceExpDate;
	
	@Column(name="control_nbr")
	Integer controlNbr;
	
	@Column(name="appl_no")
	Integer applNo;
	
	@Column(name="appl_date")
	Date applDate;
	
	@Column(name="appl_status")
	String applStatus;
	
	@Column(name="appl_type")
	String applType;
	

	@Embedded
	AuditEntry auditEntry;
	
	@Version
	@Column(name="version_nbr")	
	Integer versionNbr;

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



	public String getStatus_desc() {
		return status_desc;
	}



	public void setStatus_desc(String status_desc) {
		this.status_desc = status_desc;
	}



	public String getFitnessNbr() {
		return fitnessNbr;
	}



	public void setFitnessNbr(String fitnessNbr) {
		this.fitnessNbr = fitnessNbr;
	}



	public String getFitnessDepot() {
		return fitnessDepot;
	}



	public void setFitnessDepot(String fitnessDepot) {
		this.fitnessDepot = fitnessDepot;
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



	public Integer getControlNbr() {
		return controlNbr;
	}



	public void setControlNbr(Integer controlNbr) {
		this.controlNbr = controlNbr;
	}



	public Date getIssueDate()
	{
		return issueDate;
	}



	public void setIssueDate(Date issueDate)
	{
		this.issueDate = issueDate;
	}



	public Integer getApplNo()
	{
		return applNo;
	}



	public void setApplNo(Integer applNo)
	{
		this.applNo = applNo;
	}



	public Date getApplDate()
	{
		return applDate;
	}



	public void setApplDate(Date applDate)
	{
		this.applDate = applDate;
	}



	public String getApplStatus()
	{
		return applStatus;
	}



	public void setApplStatus(String applStatus)
	{
		this.applStatus = applStatus;
	}



	public String getApplType()
	{
		return applType;
	}



	public void setApplType(String applType)
	{
		this.applType = applType;
	}

	
	

}
