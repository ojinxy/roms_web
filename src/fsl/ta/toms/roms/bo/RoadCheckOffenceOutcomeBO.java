package fsl.ta.toms.roms.bo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import fsl.ta.toms.roms.dataobjects.RoadCheckOffenceOutcomeDO;

public class RoadCheckOffenceOutcomeBO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6525049693376791265L;
	
	Integer roadCheckOffenceOutcomeId;
	Integer offenceId;
	String outcomeTypeID;
	String outcomeTypeDesc;
	Date issueDate;
	Date offenceDateTime;
	
	//Court Information and JP related to a summons, if outcome results in a summons being issued
	Integer courtId;
	Date courtDateTime;
	String jpRegNum;
	
	Integer jpIdNumber;
	
	
	
	//Pound, Wrecking Company, and Witness Info related to warning notice, if outcome results in a warning notice being issued
	Integer poundID;
	Integer wreckingCoID;
	List<PersonBO> witnesses;

	
	
	//Needed for returning all details
	OffenceBO offenceBO;
	CourtBO courtBO;
	JPBO jpBO;
	PoundBO poundBO;
	WreckingCompanyBO wreckingCoBO;
	
	Integer wreckerVehicleID;
	
	Integer vehicleMoverPersonID;
	
	String  vehicleMoverPersonType;
	
	VehicleOwnerBO owner;
	
	
	public RoadCheckOffenceOutcomeBO() {
		super();
		// TODO Auto-generated constructor stub
	}


	public RoadCheckOffenceOutcomeBO(RoadCheckOffenceOutcomeDO roadCheckOffenceOutcomeDO) {
		super();
		this.roadCheckOffenceOutcomeId = roadCheckOffenceOutcomeDO.getRoadCheckOffenceOutcomeId();
		this.outcomeTypeID = roadCheckOffenceOutcomeDO.getOutcomeType().getOutcomeTypeId();
		this.outcomeTypeDesc = roadCheckOffenceOutcomeDO.getOutcomeType().getDescription();
		
		this.offenceBO = new OffenceBO(roadCheckOffenceOutcomeDO.getRoadCheckOffence().getOffence());
	}


	public Integer getRoadCheckOffenceOutcomeId() {
		return roadCheckOffenceOutcomeId;
	}
	public void setRoadCheckOffenceOutcomeId(Integer roadCheckOffenceOutcomeId) {
		this.roadCheckOffenceOutcomeId = roadCheckOffenceOutcomeId;
	}
	public Integer getOffenceId() {
		return offenceId;
	}
	public void setOffenceId(Integer offenceId) {
		this.offenceId = offenceId;
	}
	public String getOutcomeTypeID() {
		return outcomeTypeID;
	}
	public void setOutcomeTypeID(String outcomeTypeID) {
		this.outcomeTypeID = outcomeTypeID;
	}
	public Integer getCourtId() {
		return courtId;
	}
	public void setCourtId(Integer courtId) {
		this.courtId = courtId;
	}
	public Date getCourtDateTime() {
		return courtDateTime;
	}
	public void setCourtDateTime(Date courtDateTime) {
		this.courtDateTime = courtDateTime;
	}
	public String getJpRegNum() {
		return jpRegNum;
	}
	public void setJpRegNum(String jpRegNum) {
		this.jpRegNum = jpRegNum;
	}
	public List<PersonBO> getWitnesses() {
		return witnesses;
	}
	public void setWitnesses(List<PersonBO> witnesses) {
		this.witnesses = witnesses;
	}
	public Integer getPoundID() {
		return poundID;
	}
	public void setPoundID(Integer poundID) {
		this.poundID = poundID;
	}
	public Integer getWreckingCoID() {
		return wreckingCoID;
	}
	public void setWreckingCoID(Integer wreckingCoID) {
		this.wreckingCoID = wreckingCoID;
	}
	public Date getIssueDate() {
		return issueDate;
	}
	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}
	public Date getOffenceDateTime() {
		return offenceDateTime;
	}
	public void setOffenceDateTime(Date offenceDateTime) {
		this.offenceDateTime = offenceDateTime;
	}


	public OffenceBO getOffenceBO() {
		return offenceBO;
	}


	public void setOffenceBO(OffenceBO offenceBO) {
		this.offenceBO = offenceBO;
	}


	public CourtBO getCourtBO() {
		return courtBO;
	}


	public void setCourtBO(CourtBO courtBO) {
		this.courtBO = courtBO;
	}


	public JPBO getJpBO() {
		return jpBO;
	}


	public void setJpBO(JPBO jpBO) {
		this.jpBO = jpBO;
	}


	public PoundBO getPoundBO() {
		return poundBO;
	}


	public void setPoundBO(PoundBO poundBO) {
		this.poundBO = poundBO;
	}


	public WreckingCompanyBO getWreckingCoBO() {
		return wreckingCoBO;
	}


	public void setWreckingCoBO(WreckingCompanyBO wreckingCoBO) {
		this.wreckingCoBO = wreckingCoBO;
	}


	public String getOutcomeTypeDesc() {
		return outcomeTypeDesc;
	}


	public void setOutcomeTypeDesc(String outcomeTypeDesc) {
		this.outcomeTypeDesc = outcomeTypeDesc;
	}


	public Integer getWreckerVehicleID()
	{
		return wreckerVehicleID;
	}


	public void setWreckerVehicleID(Integer wreckerVehicleID)
	{
		this.wreckerVehicleID = wreckerVehicleID;
	}


	public Integer getVehicleMoverPersonID()
	{
		return vehicleMoverPersonID;
	}


	public void setVehicleMoverPersonID(Integer vehicleMoverPersonID)
	{
		this.vehicleMoverPersonID = vehicleMoverPersonID;
	}


	public String getVehicleMoverPersonType()
	{
		return vehicleMoverPersonType;
	}


	public void setVehicleMoverPersonType(String vehicleMoverPersonType)
	{
		this.vehicleMoverPersonType = vehicleMoverPersonType;
	}


	public Integer getJpIdNumber() {
		return jpIdNumber;
	}


	public void setJpIdNumber(Integer jpIdNumber) {
		this.jpIdNumber = jpIdNumber;
	}


	/**
	 * @return the owner
	 */
	public VehicleOwnerBO getOwner() {
		return owner;
	}


	/**
	 * @param owner the owner to set
	 */
	public void setOwner(VehicleOwnerBO owner) {
		this.owner = owner;
	}


	

	

}
