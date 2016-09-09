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
@Table(name="ROMS_vehicle_check_result")
public class VehicleCheckResultDO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3955325594627233219L;

	public VehicleCheckResultDO() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	
	
	public VehicleCheckResultDO(RoadCheckDO roadCheck, String plateRegNo,
			String engineNo, String chassisNo, String colour,
			String makeDescription, String model, String typeDesc, String laNo,
			Date mvrcExpiryDate, Integer year, Integer roadLicNum,
			String roadLicOwner, String roadLicStatus, AuditEntry auditEntry,Date mvrcIssueDate) {
		super();
		this.roadCheck = roadCheck;
		this.plateRegNo = plateRegNo;
		this.engineNo = engineNo;
		this.chassisNo = chassisNo;
		this.colour = colour;
		this.makeDescription = makeDescription;
		this.model = model;
		this.typeDesc = typeDesc;
		this.laNo = laNo;
		this.mvrcExpiryDate = mvrcExpiryDate;
		this.year = year;
		this.roadLicNumber = roadLicNum;
		this.roadLicOwner = roadLicOwner;
		this.roadLicStatus = roadLicStatus;
		this.auditEntry = auditEntry;
		this.mvrcIssueDate = mvrcIssueDate;
	}



	@Id
	@Column(name="vehicle_check_id")
	@GeneratedValue(strategy=GenerationType.AUTO)
	Integer vehicleCheckId;

	@ManyToOne
	@JoinColumn(name="road_check_id")
	RoadCheckDO roadCheck;
	
	@Column(name="plate_reg_no")
	String plateRegNo;
	
	@Column(name="engine_no")
	String engineNo;
	
	@Column(name="chassis_no")
	String chassisNo;
	
	@Column(name="colour")
	String colour;
	
	@Column(name="make_desc")
	String makeDescription;
	
	@Column(name="model")
	String model;
	
	@Column(name="type_desc")
	String typeDesc;
	
	
	@Column(name="la_no")
	String laNo;
	
	@Column(name="mvrc_issue_date")
	Date mvrcIssueDate;
	
	@Column(name="mvrc_expiry_date")
	Date mvrcExpiryDate;
	
	@Column(name="year")
	Integer year;
		
	@Column(name="road_licence_num")
	Integer roadLicNumber;
	
	@Column(name="road_licence_owner")
	String roadLicOwner;
	
	@Column(name="road_licence_status")
	String roadLicStatus;
	
	@Embedded
	AuditEntry auditEntry;
	
	@Version
	@Column(name="version_nbr")
	Integer versionNbr;

	public Integer getVehicleCheckId() {
		return vehicleCheckId;
	}

	public void setVehicleCheckId(Integer vehicleCheckId) {
		this.vehicleCheckId = vehicleCheckId;
	}

	public RoadCheckDO getRoadCheck() {
		return roadCheck;
	}

	public void setRoadCheck(RoadCheckDO roadCheck) {
		this.roadCheck = roadCheck;
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

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}



	public String getTypeDesc() {
		return typeDesc;
	}



	public void setTypeDesc(String typeDesc) {
		this.typeDesc = typeDesc;
	}




	public Integer getRoadLicNumber() {
		return roadLicNumber;
	}




	public void setRoadLicNumber(Integer roadLicNumber) {
		this.roadLicNumber = roadLicNumber;
	}




	public String getRoadLicOwner() {
		return roadLicOwner;
	}




	public void setRoadLicOwner(String roadLicOwner) {
		this.roadLicOwner = roadLicOwner;
	}




	public String getRoadLicStatus() {
		return roadLicStatus;
	}




	public void setRoadLicStatus(String roadLicStatus) {
		this.roadLicStatus = roadLicStatus;
	}




	public Date getMvrcIssueDate()
	{
		return mvrcIssueDate;
	}




	public void setMvrcIssueDate(Date mvrcIssueDate)
	{
		this.mvrcIssueDate = mvrcIssueDate;
	}
	
	
	
}
