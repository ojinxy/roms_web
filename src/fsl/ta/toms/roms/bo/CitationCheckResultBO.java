package fsl.ta.toms.roms.bo;

import java.io.Serializable;
import java.util.List;

import fsl.ta.toms.roms.dataobjects.RoadCheckDO;
import fsl.ta.toms.roms.ticketwebservice.TrafficTicket;

public class CitationCheckResultBO  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -419202448438713555L;

	
Integer citationCheckId;
	
	String trnNbr;
	String dlNo;
	String regPlateNo;
	List<CitationOffenceBO> citationOffences;
	List<CitationOffenceBO> citationOffencesVehicle;
	List<TrafficTicket> trafficTickets;
	String comment;
	RoadCheckBO roadCheckBO;
	Integer complianceID;
	Integer MAX_ROMS_OFFENCES;
	Integer MAX_TTMS_OFFENCES;
	Integer totalAmountOfROMSOffences;
	Integer totalAmountOfROMSOffencesVehicle;
	Integer totalAmountOfTTMSOffences;
	Boolean includeTTMSResults;
	
	
	String currentUserName;

	public Integer getCitationCheckId() {
		return citationCheckId;
	}

	public void setCitationCheckId(Integer citationCheckId) {
		this.citationCheckId = citationCheckId;
	}

	public String getTrnNbr() {
		return trnNbr;
	}

	public void setTrnNbr(String trnNbr) {
		this.trnNbr = trnNbr;
	}

	public String getDlNo() {
		return dlNo;
	}

	public void setDlNo(String dlNo) {
		this.dlNo = dlNo;
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

	public String getRegPlateNo() {
		return regPlateNo;
	}

	public void setRegPlateNo(String regPlateNo) {
		this.regPlateNo = regPlateNo;
	}

	public List<CitationOffenceBO> getCitationOffences() {
		return citationOffences;
	}

	public void setCitationOffences(List<CitationOffenceBO> citationOffences) {
		this.citationOffences = citationOffences;
	}

	public List<TrafficTicket> getTrafficTickets() {
		return trafficTickets;
	}

	public void setTrafficTickets(List<TrafficTicket> trafficTickets) {
		this.trafficTickets = trafficTickets;
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

	public Integer getMaxRomsOffences()
	{
		return MAX_ROMS_OFFENCES;
	}

	public void setMaxRomsOffences(Integer mAX_ROMS_OFFENCES)
	{
		MAX_ROMS_OFFENCES = mAX_ROMS_OFFENCES;
	}

	public Integer getMaxTtmsOffences()
	{
		return MAX_TTMS_OFFENCES;
	}

	public void setMaxTtmsOffences(Integer mAX_TTMS_OFFENCES)
	{
		MAX_TTMS_OFFENCES = mAX_TTMS_OFFENCES;
	}

	public Boolean getIncludeTTMSResults()
	{
		return includeTTMSResults;
	}

	public void setIncludeTTMSResults(Boolean includeTTMSResults)
	{
		this.includeTTMSResults = includeTTMSResults;
	}

	public Integer getTotalAmountOfROMSOffences()
	{
		return totalAmountOfROMSOffences;
	}

	public void setTotalAmountOfROMSOffences(Integer totalAmountOfROMSOffences)
	{
		this.totalAmountOfROMSOffences = totalAmountOfROMSOffences;
	}

	public Integer getTotalAmountOfTTMSOffences()
	{
		return totalAmountOfTTMSOffences;
	}

	public void setTotalAmountOfTTMSOffences(Integer totalAmountOfTTMSOffences)
	{
		this.totalAmountOfTTMSOffences = totalAmountOfTTMSOffences;
	}

	public List<CitationOffenceBO> getCitationOffencesVehicle()
	{
		return citationOffencesVehicle;
	}

	public void setCitationOffencesVehicle(
			List<CitationOffenceBO> citationOffencesVehicle)
	{
		this.citationOffencesVehicle = citationOffencesVehicle;
	}

	public Integer getTotalAmountOfROMSOffencesVehicle()
	{
		return totalAmountOfROMSOffencesVehicle;
	}

	public void setTotalAmountOfROMSOffencesVehicle(
			Integer totalAmountOfROMSOffencesVehicle)
	{
		this.totalAmountOfROMSOffencesVehicle = totalAmountOfROMSOffencesVehicle;
	}

	
	
	
}
