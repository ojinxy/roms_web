package fsl.ta.toms.roms.bo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;


public class ComplianceDetailsBO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5381585452722717998L;
	
	private List<RoadCheckOffenceOutcomeBO> listOfOutcomes;
	private List<DocumentViewBO> listOfDocuments;
	private List<PersonBO> listOfWitnesses;
	private List<CourtBO> listOfCourts;
	private PoundBO poundDetails;
	private WreckingCompanyBO wreckingCompanyDetails;
	
	private VehicleBO wreckerVehicle;
	
	private PersonBO vehicleMover;
	
	private String vehicleMoverType;
	
	private String allegation;
	
	private BadgeCheckResultBO badgeCheckResult;
	
	private VehicleCheckResultBO vehicleCheckResult;
	
	List<ComplianceParamBO>listOfComplianceParams;
	
	public VehicleCheckResultBO getVehicleCheckResult() {
		return vehicleCheckResult;
	}




	public void setVehicleCheckResult(VehicleCheckResultBO vehicleCheckResult) {
		this.vehicleCheckResult = vehicleCheckResult;
	}




	private RoadLicCheckResultBO roadLicCheckResult;
	
	private DLCheckResultBO dlCheckResult;
	
	private CitationCheckResultBO citationCheckResult;
	
	
	
	public ComplianceDetailsBO() {
		super();
		// TODO Auto-generated constructor stub
	}




	public List<RoadCheckOffenceOutcomeBO> getListOfOutcomes() {
		return listOfOutcomes;
	}

	public void setListOfOutcomes(List<RoadCheckOffenceOutcomeBO> listOfOutcomes) {
		this.listOfOutcomes = listOfOutcomes;
	}

	




	public List<PersonBO> getListOfWitnesses() {
		return listOfWitnesses;
	}




	public void setListOfWitnesses(List<PersonBO> listOfWitnesses) {
		this.listOfWitnesses = listOfWitnesses;
	}




	public PoundBO getPoundDetails() {
		return poundDetails;
	}




	public void setPoundDetails(PoundBO poundDetails) {
		this.poundDetails = poundDetails;
	}




	public WreckingCompanyBO getWreckingCompanyDetails() {
		return wreckingCompanyDetails;
	}




	public void setWreckingCompanyDetails(WreckingCompanyBO wreckingCompanyDetails) {
		this.wreckingCompanyDetails = wreckingCompanyDetails;
	}




	public List<DocumentViewBO> getListOfDocuments() {
		return listOfDocuments;
	}




	public void setListOfDocuments(List<DocumentViewBO> listOfDocuments) {
		this.listOfDocuments = listOfDocuments;
	}




	public List<CourtBO> getListOfCourts() {
		return listOfCourts;
	}




	public void setListOfCourts(List<CourtBO> listOfCourts) {
		this.listOfCourts = listOfCourts;
	}




	public BadgeCheckResultBO getBadgeCheckResult() {
		return badgeCheckResult;
	}




	public void setBadgeCheckResult(BadgeCheckResultBO badgeCheckResult) {
		this.badgeCheckResult = badgeCheckResult;
	}





	public RoadLicCheckResultBO getRoadLicCheckResult() {
		return roadLicCheckResult;
	}




	public void setRoadLicCheckResult(RoadLicCheckResultBO roadLicCheckResult) {
		this.roadLicCheckResult = roadLicCheckResult;
	}




	public DLCheckResultBO getDlCheckResult() {
		return dlCheckResult;
	}




	public void setDlCheckResult(DLCheckResultBO dlCheckResult) {
		this.dlCheckResult = dlCheckResult;
	}




	public CitationCheckResultBO getCitationCheckResult() {
		return citationCheckResult;
	}




	public void setCitationCheckResult(CitationCheckResultBO citationCheckResult) {
		this.citationCheckResult = citationCheckResult;
	}




	public VehicleBO getWreckerVehicle()
	{
		return wreckerVehicle;
	}




	public void setWreckerVehicle(VehicleBO wreckerVehicle)
	{
		this.wreckerVehicle = wreckerVehicle;
	}




	public PersonBO getVehicleMover()
	{
		return vehicleMover;
	}




	public void setVehicleMover(PersonBO vehicleMover)
	{
		this.vehicleMover = vehicleMover;
	}




	public String getVehicleMoverType()
	{
		return vehicleMoverType;
	}




	public void setVehicleMoverType(String vehicleMoverType)
	{
		this.vehicleMoverType = vehicleMoverType;
	}




	public List<ComplianceParamBO> getListOfComplianceParams() {
		return listOfComplianceParams;
	}




	public void setListOfComplianceParams(
			List<ComplianceParamBO> listOfComplianceParams) {
		this.listOfComplianceParams = listOfComplianceParams;
	}




	public String getAllegation()
	{
		return allegation;
	}




	public void setAllegation(String allegation)
	{
		this.allegation = allegation;
	}



	


}
