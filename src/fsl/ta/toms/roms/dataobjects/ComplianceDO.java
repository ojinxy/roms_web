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
@Table(name="ROMS_compliance")
public class ComplianceDO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1849305049850038547L;

	public ComplianceDO() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	
	@Id
	@Column(name="compliance_id")
	@GeneratedValue(strategy=GenerationType.AUTO)
	Integer complianceId;
	
	@ManyToOne
	@JoinColumn(name="road_operation_id")
	RoadOperationDO roadOperation;
	
	
	@Column(name="offence_date_time")
	Date offenceDateTime;
	
	@Column(name="court_date_time")
	Date courtDateTime;
	
	@ManyToOne
	@JoinColumn(name="court_id")
	CourtDO court;
	
	@ManyToOne
	@JoinColumn(name="person_id")
	PersonDO person;
	
	@ManyToOne
	@JoinColumn(name="vehicle_id")
	VehicleDO vehicle;
	
	@Column(name="vehicle_info_different")
	Boolean vehicleInfoDifferent;
	
	@ManyToOne
	@JoinColumn(name="ta_staff_id")
	TAStaffDO taStaff;
	
	@ManyToOne
	@JoinColumn(name="status_id")
	StatusDO status;
	
	@ManyToOne
	@JoinColumn(name="compliancy_artery_id")
	ArteryDO compliancyArtery;
	
	@Column(name="compliant")
	String compliant;
	
	@Column(name="person_role")
	String personRole;
	
	@Column(name="comment")
	String comment;
	
	@Column(name="other_role_observed_id")
	String otherRole;	
	
	@ManyToOne
	@JoinColumn(name="aid_and_abet_veh_owner_id")
	PersonDO aidAbetVehicleOwner;
		
	@Embedded
	AuditEntry auditEntry;
	
	
	@Version
	@Column(name="version_nbr")
	Integer versionNbr;

	public Integer getComplianceId() {
		return complianceId;
	}

	public void setComplianceId(Integer complianceId) {
		this.complianceId = complianceId;
	}

	public RoadOperationDO getRoadOperation() {
		return roadOperation;
	}

	public void setRoadOperation(RoadOperationDO roadOperation) {
		this.roadOperation = roadOperation;
	}

	public PersonDO getPerson() {
		return person;
	}

	public void setPerson(PersonDO person) {
		this.person = person;
	}

	public VehicleDO getVehicle() {
		return vehicle;
	}

	public void setVehicle(VehicleDO vehicle) {
		this.vehicle = vehicle;
	}

	public TAStaffDO getTaStaff() {
		return taStaff;
	}

	public void setTaStaff(TAStaffDO taStaff) {
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


	public ArteryDO getCompliancyArtery() {
		return compliancyArtery;
	}

	public void setCompliancyArtery(ArteryDO compliancyArtery) {
		this.compliancyArtery = compliancyArtery;
	}


	public Date getOffenceDateTime() {
		return offenceDateTime;
	}

	public void setOffenceDateTime(Date offenceDateTime) {
		this.offenceDateTime = offenceDateTime;
	}

	public StatusDO getStatus() {
		return status;
	}

	public void setStatus(StatusDO status) {
		this.status = status;
	}

	public Date getCourtDateTime()
	{
		return courtDateTime;
	}

	public void setCourtDateTime(Date courtDateTime)
	{
		this.courtDateTime = courtDateTime;
	}

	public Boolean getVehicleInfoDifferent()
	{
		return vehicleInfoDifferent;
	}

	public void setVehicleInfoDifferent(Boolean vehicleInfoDifferent)
	{
		this.vehicleInfoDifferent = vehicleInfoDifferent;
	}

	/**
	 * @return the aidAbetVehicleOwner
	 */
	public PersonDO getAidAbetVehicleOwner() {
		return aidAbetVehicleOwner;
	}

	/**
	 * @param aidAbetVehicleOwner the aidAbetVehicleOwner to set
	 */
	public void setAidAbetVehicleOwner(PersonDO aidAbetVehicleOwner) {
		this.aidAbetVehicleOwner = aidAbetVehicleOwner;
	}

	/**
	 * @return the court
	 */
	public CourtDO getCourt() {
		return court;
	}

	/**
	 * @param court the court to set
	 */
	public void setCourt(CourtDO court) {
		this.court = court;
	}

	public String getOtherRole() {
		return otherRole;
	}

	public void setOtherRole(String otherRole) {
		this.otherRole = otherRole;
	}



	



	
}
