package org.fsl.roms.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import fsl.ta.toms.roms.bo.ArteryBO;
import fsl.ta.toms.roms.bo.ITAExaminerBO;
import fsl.ta.toms.roms.bo.JPBO;
import fsl.ta.toms.roms.bo.LocationBO;
import fsl.ta.toms.roms.bo.ParishBO;
import fsl.ta.toms.roms.bo.PoliceOfficerBO;
import fsl.ta.toms.roms.bo.TAStaffBO;
import fsl.ta.toms.roms.bo.TAVehicleBO;

public class TeamView implements Serializable, Comparable<TeamView>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1856031551994272847L;
	
	private Integer numOfTAStaff;
	private Integer numOfITAExaminer;
	private Integer numOfPolice;
	private Integer numOfJP;
	private Integer numOfVehicle;
	private Integer numOfMotorCycle;
	private Float coverage;
//	private ArrayList<ParishBO> selectedParishes;
//	private ArrayList<LocationBO> selectedLocations;
//	private ArrayList<ArteryBO> selectedArteries;
//	private ArrayList<TAStaffBO> selectedTAStaff;
//	
//	
	/////////////////
	
	private List<TAStaffBO> taStaffList;
	private List<JPBO> jpList;
	private List<PoliceOfficerBO> policeOfficerList;
	private List<ITAExaminerBO> itaExaminerList;
	private List<TAVehicleBO> taVehicleList;
	
	private List<LocationBO> operationLocationList;
	private List<ArteryBO> operationArteryList;
	private List<ParishBO> operationParishList;
	
	private List<LocationBO> currentTeamListLocationList;
	private List<ArteryBO> currentTeamListOperationArteryList;
	private List<ParishBO> currentTeamListOperationParishList;
		
	/**
	 * used in changes - kpowell
	 */
	private List<LocationBO> selectedTeamLocationList;
	private List<ArteryBO> selectedTeamArteryList;
	private List<ParishBO> selectedTeamParishList;
	private String parishCodeIndex;
	private Integer locationCodeIndex;
	
	private Float tempCoverageSelectedArtery = 0.0F;
	private Float tempCoverageNoSelectedArtery = 0.0F;
	/**
	 * 
	 */
	
	private TAStaffBO teamLead;
	private String teamLeadId;
	private Integer roadOperationId;
	private String teamName;
	private boolean delete;
	private String teamID;
	private Integer teamIDNumber;
	/////////////////
	
	private Integer numOfVehicleUsed;
	private Integer numOfMotorCycleUsed;
	
	private String selected;
	
	private List<TAStaffBO> selectedTAStaff;
	private List<TAVehicleBO> selectedTAVehicle;
	
	private Map<String, ParishBO> hashMapOfTeamParishes = new HashMap<String, ParishBO>();
	private Map<String, List<LocationBO>> hashMapOfTeamLocations = new HashMap<String, List<LocationBO>>();
	private Map<Integer, List<ArteryBO>> hashMapOfTeamArteries = new HashMap<Integer, List<ArteryBO>>();
	
	private String listOfSelectedStaffID;
	private String listOfSelectedITAID;
	private String listOfSelectedPoliceID;
	private String listOfSelectedJPID;
	private String listOfSelectedVehID;
	
	
	
//	public TeamView() {
//		super();
//		teamName = null;
//		numOfMotorCycle = 0;
//		// TODO Auto-generated constructor stub
//	}



	public TeamView() {
		super();
		this.selectedTeamArteryList = new ArrayList<ArteryBO>();
//		this.numOfTAStaff = null;
//		this.numOfITAExaminer = null;
//		this.numOfPolice = null;
//		this.numOfJP = null;
//		this.numOfVehicle = null;
//		this.numOfMotorCycle = null;
//		this.taStaffList = null;
//		this.jpList = null;
//		this.policeOfficerList = null;
//		this.itaExaminerList = null;
//		this.taVehicleList = null;
//		this.operationLocationList = null;
//		this.operationArteryList = null;
//		this.operationParishList = null;
//		this.currentTeamListLocationList = null;
//		this.currentTeamListOperationArteryList = null;
//		this.currentTeamListOperationParishList = null;
//		this.teamLead = null;
//		this.teamLeadId = null;
//		this.roadOperationId = null;
//		this.teamName = null;
//		this.delete = false;
//		this.teamID = null;
//		this.numOfVehicleUsed = null;
//		this.numOfMotorCycleUsed = null;
//		this.selected = null;
//		this.selectedTAStaff = null;
//		this.hashMapOfTeamParishes = null;
//		this.hashMapOfTeamLocations = null;
//		this.hashMapOfTeamArteries = null;
	}



	public TeamView( TeamView teamView) {
		super();
		this.numOfTAStaff = teamView.numOfTAStaff;
		this.numOfITAExaminer = teamView.numOfITAExaminer;
		this.numOfPolice = teamView.numOfPolice;
		this.numOfJP = teamView.numOfJP;
		this.numOfVehicle = teamView.numOfVehicle;
		this.numOfMotorCycle = teamView.numOfMotorCycle;
		this.coverage = teamView.coverage;
		this.taStaffList = teamView.taStaffList;
		this.jpList = teamView.jpList;
		this.policeOfficerList = teamView.policeOfficerList;
		this.itaExaminerList = teamView.itaExaminerList;
		this.taVehicleList = teamView. taVehicleList;
		this.operationLocationList = teamView.operationLocationList;
		this.operationArteryList = teamView.operationArteryList;
		this.operationParishList = teamView.operationParishList;
		this.currentTeamListLocationList = teamView.currentTeamListLocationList;
		this.currentTeamListOperationArteryList = teamView.currentTeamListOperationArteryList;
		this.currentTeamListOperationParishList = teamView.currentTeamListOperationParishList;
		
		if(teamView.selectedTeamLocationList!= null){
			this.selectedTeamLocationList = new ArrayList<LocationBO>();
			for (Iterator iterator = teamView.selectedTeamLocationList.iterator(); iterator.hasNext();) {
				LocationBO type = (LocationBO) iterator.next();
				this.selectedTeamLocationList.add( populateLocationBO(type));
			}
		}
		//this.selectedTeamLocationList = teamView.selectedTeamLocationList;
		
		if(teamView.selectedTeamArteryList!= null){
			this.selectedTeamArteryList = new ArrayList<ArteryBO>();
			for (Iterator iterator = teamView.selectedTeamArteryList.iterator(); iterator.hasNext();) {
				ArteryBO type = (ArteryBO) iterator.next();
				this.selectedTeamArteryList.add( populateArteryBO(type));
			}
		}
		//this.selectedTeamArteryList = teamView.selectedTeamArteryList;
		this.selectedTeamParishList = teamView.selectedTeamParishList;
		this.parishCodeIndex = teamView.parishCodeIndex;
		this.locationCodeIndex = teamView.locationCodeIndex;
		this.tempCoverageSelectedArtery = teamView.tempCoverageSelectedArtery;
		this.tempCoverageNoSelectedArtery = teamView.tempCoverageNoSelectedArtery;
		this.teamLead = teamView.teamLead;
		this.teamLeadId = teamView.teamLeadId;
		this.roadOperationId = teamView.roadOperationId;
		this.teamName = teamView.teamName;
		this.delete = teamView.delete;
		this.teamID = teamView.teamID;
		this.teamIDNumber = teamView.teamIDNumber;
		this.numOfVehicleUsed = teamView.numOfVehicleUsed;
		this.numOfMotorCycleUsed = teamView.numOfMotorCycleUsed;
		this.selected = teamView.selected;
		this.selectedTAStaff = teamView.selectedTAStaff;
		this.selectedTAVehicle = teamView.selectedTAVehicle;
		this.hashMapOfTeamParishes = teamView.hashMapOfTeamParishes;
		this.hashMapOfTeamLocations = teamView.hashMapOfTeamLocations;
		this.hashMapOfTeamArteries = teamView.hashMapOfTeamArteries;
		this.listOfSelectedStaffID = teamView.listOfSelectedStaffID;
		this.listOfSelectedITAID = teamView.listOfSelectedITAID;
		this.listOfSelectedPoliceID = teamView.listOfSelectedPoliceID;
		this.listOfSelectedJPID = teamView.listOfSelectedJPID;
		this.listOfSelectedVehID = teamView.listOfSelectedVehID;
	}



	public String getTeamName() {
		return teamName;
	}



	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}



	public Integer getNumOfTAStaff() {
		return numOfTAStaff;
	}



	public void setNumOfTAStaff(Integer numOfTAStaff) {
		this.numOfTAStaff = numOfTAStaff;
	}



	public Integer getNumOfITAExaminer() {
		return numOfITAExaminer;
	}



	public void setNumOfITAExaminer(Integer numOfITAExaminer) {
		this.numOfITAExaminer = numOfITAExaminer;
	}



	public Integer getNumOfPolice() {
		return numOfPolice;
	}



	public void setNumOfPolice(Integer numOfPolice) {
		this.numOfPolice = numOfPolice;
	}



	public Integer getNumOfJP() {
		return numOfJP;
	}



	public void setNumOfJP(Integer numOfJP) {
		this.numOfJP = numOfJP;
	}



	public Integer getNumOfVehicle() {
		return numOfVehicle;
	}



	public void setNumOfVehicle(Integer numOfVehicle) {
		this.numOfVehicle = numOfVehicle;
	}



	public Integer getNumOfMotorCycle() {
		return numOfMotorCycle;
	}



	public void setNumOfMotorCycle(Integer numOfMotorCycle) {
		this.numOfMotorCycle = numOfMotorCycle;
	}



	public List<TAStaffBO> getTaStaffList() {
		return taStaffList;
	}



	public void setTaStaffList(List<TAStaffBO> taStaffList) {
		this.taStaffList = taStaffList;
	}



	public List<JPBO> getJpList() {
		return jpList;
	}



	public void setJpList(List<JPBO> jpList) {
		this.jpList = jpList;
	}



	public List<PoliceOfficerBO> getPoliceOfficerList() {
		return policeOfficerList;
	}



	public void setPoliceOfficerList(List<PoliceOfficerBO> policeOfficerList) {
		this.policeOfficerList = policeOfficerList;
	}



	public List<ITAExaminerBO> getItaExaminerList() {
		return itaExaminerList;
	}



	public void setItaExaminerList(List<ITAExaminerBO> itaExaminerList) {
		this.itaExaminerList = itaExaminerList;
	}



	public List<TAVehicleBO> getTaVehicleList() {
		return taVehicleList;
	}



	public void setTaVehicleList(List<TAVehicleBO> taVehicleList) {
		this.taVehicleList = taVehicleList;
	}



	public TAStaffBO getTeamLead() {
		return teamLead;
	}



	public void setTeamLead(TAStaffBO teamLead) {
		this.teamLead = teamLead;
	}



	public List<LocationBO> getOperationLocationList() {
		return operationLocationList;
	}



	public List<ArteryBO> getOperationArteryList() {
		return operationArteryList;
	}



	public Integer getRoadOperationId() {
		return roadOperationId;
	}



	public void setRoadOperationId(Integer roadOperationId) {
		this.roadOperationId = roadOperationId;
	}




	public boolean isDelete() {
		return delete;
	}



	public void setDelete(boolean delete) {
		this.delete = delete;
	}



	public List<ParishBO> getOperationParishList() {
		return operationParishList;
	}



	public void setOperationParishList(List<ParishBO> operationParishList) {
		this.operationParishList = operationParishList;
	}



	public String getTeamID() {
		return teamID;
	}



	public void setTeamID(String teamID) {
		this.teamID = teamID;
	}



	public String getTeamLeadId() {
		return teamLeadId;
	}



	public void setTeamLeadId(String teamLeadId) {
		this.teamLeadId = teamLeadId;
	}



	public Integer getNumOfVehicleUsed() {
		return numOfVehicleUsed;
	}



	public void setNumOfVehicleUsed(Integer numOfVehicleUsed) {
		this.numOfVehicleUsed = numOfVehicleUsed;
	}



	public Integer getNumOfMotorCycleUsed() {
		return numOfMotorCycleUsed;
	}



	public void setNumOfMotorCycleUsed(Integer numOfMotorCycleUsed) {
		this.numOfMotorCycleUsed = numOfMotorCycleUsed;
	}



	public String getSelected() {
		return selected;
	}



	public void setSelected(String selected) {
		this.selected = selected;
	}



	public void setOperationLocationList(List<LocationBO> teamLocations) {
		this.operationLocationList = teamLocations;
		
	}



	public void setOperationArteryList(List<ArteryBO> teamArteries) {
		this.operationArteryList = teamArteries;
		
	}



	public List<TAStaffBO> getSelectedTAStaff() {
		return selectedTAStaff;
	}



	public void setSelectedTAStaff(List<TAStaffBO> selectedTAStaff) {
		this.selectedTAStaff = selectedTAStaff;
	}



	public Map<String, ParishBO> getHashMapOfTeamParishes() {
		return hashMapOfTeamParishes;
	}



	public void setHashMapOfTeamParishes(Map<String, ParishBO> hashMapOfTeamParishes) {
		this.hashMapOfTeamParishes = hashMapOfTeamParishes;
	}



	public Map<String, List<LocationBO>> getHashMapOfTeamLocations() {
		return hashMapOfTeamLocations;
	}



	public void setHashMapOfTeamLocations(
			Map<String, List<LocationBO>> hashMapOfTeamLocations) {
		this.hashMapOfTeamLocations = hashMapOfTeamLocations;
	}



	public Map<Integer, List<ArteryBO>> getHashMapOfTeamArteries() {
		return hashMapOfTeamArteries;
	}



	public void setHashMapOfTeamArteries(
			Map<Integer, List<ArteryBO>> hashMapOfTeamArteries) {
		this.hashMapOfTeamArteries = hashMapOfTeamArteries;
	}



	public List<LocationBO> getCurrentTeamListLocationList() {
		return currentTeamListLocationList;
	}



	public void setCurrentTeamListLocationList(
			List<LocationBO> currentTeamListLocationList) {
		this.currentTeamListLocationList = currentTeamListLocationList;
	}



	public List<ArteryBO> getCurrentTeamListOperationArteryList() {
		return currentTeamListOperationArteryList;
	}



	public void setCurrentTeamListOperationArteryList(
			List<ArteryBO> currentTeamListOperationArteryList) {
		this.currentTeamListOperationArteryList = currentTeamListOperationArteryList;
	}



	public List<ParishBO> getCurrentTeamListOperationParishList() {
		return currentTeamListOperationParishList;
	}



	public void setCurrentTeamListOperationParishList(
			List<ParishBO> currentTeamListOperationParishList) {
		this.currentTeamListOperationParishList = currentTeamListOperationParishList;
	}



	public String getListOfSelectedStaffID() {
		return listOfSelectedStaffID;
	}



	public void setListOfSelectedStaffID(String listOfSelectedStaffID) {
		this.listOfSelectedStaffID = listOfSelectedStaffID;
	}



	public String getListOfSelectedITAID() {
		return listOfSelectedITAID;
	}



	public void setListOfSelectedITAID(String listOfSelectedITAID) {
		this.listOfSelectedITAID = listOfSelectedITAID;
	}



	public String getListOfSelectedPoliceID() {
		return listOfSelectedPoliceID;
	}



	public void setListOfSelectedPoliceID(String listOfSelectedPoliceID) {
		this.listOfSelectedPoliceID = listOfSelectedPoliceID;
	}



	public String getListOfSelectedJPID() {
		return listOfSelectedJPID;
	}



	public void setListOfSelectedJPID(String listOfSelectedJPID) {
		this.listOfSelectedJPID = listOfSelectedJPID;
	}



	public String getListOfSelectedVehID() {
		return listOfSelectedVehID;
	}



	public void setListOfSelectedVehID(String listOfSelectedVehID) {
		this.listOfSelectedVehID = listOfSelectedVehID;
	}



	public List<TAVehicleBO> getSelectedTAVehicle() {
		return selectedTAVehicle;
	}



	public void setSelectedTAVehicle(List<TAVehicleBO> selectedTAVehicle) {
		this.selectedTAVehicle = selectedTAVehicle;
	}



	public Float getCoverage() {
		return coverage;
	}



	public void setCoverage(Float coverage) {
		this.coverage = coverage;
	}



	public Integer getTeamIDNumber() {
		return teamIDNumber;
	}



	public void setTeamIDNumber(Integer teamIDNumber) {
		this.teamIDNumber = teamIDNumber;
	}



	@Override
	public int compareTo(TeamView o) {
		// TODO Auto-generated method stub
		
		return  this.teamIDNumber.compareTo(o.getTeamIDNumber());
	}

	/*************2014-10-27:REDO*********************/

	public List<LocationBO> getSelectedTeamLocationList() {
		return selectedTeamLocationList;
	}



	public void setSelectedTeamLocationList(
			List<LocationBO> selectedTeamLocationList) {
		this.selectedTeamLocationList = selectedTeamLocationList;
	}



	public List<ArteryBO> getSelectedTeamArteryList() {
		return selectedTeamArteryList;
	}



	public void setSelectedTeamArteryList(List<ArteryBO> selectedTeamArteryList) {
		this.selectedTeamArteryList = selectedTeamArteryList;
	}



	public List<ParishBO> getSelectedTeamParishList() {
		return selectedTeamParishList;
	}



	public void setSelectedTeamParishList(List<ParishBO> selectedTeamParishList) {
		this.selectedTeamParishList = selectedTeamParishList;
	}



	public String getParishCodeIndex() {
		return parishCodeIndex;
	}



	public void setParishCodeIndex(String parishCodeIndex) {
		this.parishCodeIndex = parishCodeIndex;
	}



	public Integer getLocationCodeIndex() {
		return locationCodeIndex;
	}



	public void setLocationCodeIndex(Integer locationCodeIndex) {
		this.locationCodeIndex = locationCodeIndex;
	}



	public Float getTempCoverageSelectedArtery() {
		return tempCoverageSelectedArtery;
	}



	public void setTempCoverageSelectedArtery(Float tempCoverageSelectedArtery) {
		this.tempCoverageSelectedArtery = tempCoverageSelectedArtery;
	}



	public Float getTempCoverageNoSelectedArtery() {
		return tempCoverageNoSelectedArtery;
	}



	public void setTempCoverageNoSelectedArtery(Float tempCoverageNoSelectedArtery) {
		this.tempCoverageNoSelectedArtery = tempCoverageNoSelectedArtery;
	}



	 public LocationBO populateLocationBO(LocationBO srcLocation) {
		 LocationBO destLocationBO  = new LocationBO();
		 destLocationBO.setAuditEntryBO(srcLocation.getAuditEntryBO());
		 destLocationBO.setCurrentUsername(srcLocation.getCurrentUsername());
		 destLocationBO.setLocationDescription(srcLocation.getLocationDescription());
		 destLocationBO.setLocationId( srcLocation.getLocationId());
		 destLocationBO.setParishCode(srcLocation.getParishCode());
		 destLocationBO.setParishDescription(srcLocation.getParishDescription());
		 destLocationBO.setSelected(srcLocation.isSelected());
		 destLocationBO.setShortDesc(srcLocation.getShortDesc());
		 destLocationBO.setStatusDescription(srcLocation.getStatusDescription());
		 destLocationBO.setStatusId(srcLocation.getStatusId());
		 return destLocationBO;
		}
	
	
	
	 public ArteryBO populateArteryBO(ArteryBO srcArtery){
		 ArteryBO destArteryBO  = new ArteryBO();
		 destArteryBO.setArteryDescription(srcArtery.getArteryDescription());
		 destArteryBO.setArteryId(srcArtery.getArteryId());
		 destArteryBO.setAuditEntryBO(srcArtery.getAuditEntryBO());
		 destArteryBO.setCreatedtime(srcArtery.getCreatedtime());
		 destArteryBO.setCreateusername(srcArtery.getCreateusername());
		 destArteryBO.setCurrentUsername(srcArtery.getCurrentUsername());
		 destArteryBO.setDistance(srcArtery.getDistance());
		 destArteryBO.setEndlatitude(srcArtery.getEndlatitude());
		 destArteryBO.setEndlongitude(srcArtery.getEndlongitude());
		 destArteryBO.setLocationDescription(srcArtery.getLocationDescription());
		 destArteryBO.setLocationId(srcArtery.getLocationId());
		 destArteryBO.setParishCode(srcArtery.getParishCode());
		 destArteryBO.setParishDescription(srcArtery.getParishDescription());
		 destArteryBO.setPoints(srcArtery.getPoints());
		 destArteryBO.setSelected(srcArtery.isSelected());
		 destArteryBO.setShortDescription(srcArtery.getShortDescription());
		 destArteryBO.setStartlatitude(srcArtery.getStartlatitude());
		 destArteryBO.setStartlongitude(srcArtery.getStartlongitude());
		 destArteryBO.setStatusDescription(srcArtery.getStatusDescription());
		 destArteryBO.setStatusId(srcArtery.getStatusId());
		return destArteryBO;
			
	 }
	
	/********************************************/


}