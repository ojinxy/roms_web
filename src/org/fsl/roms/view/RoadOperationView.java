package org.fsl.roms.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.datatype.XMLGregorianCalendar;

import org.fsl.roms.constants.Constants;
import org.fsl.roms.uicomponents.ParishDataModel;
import org.primefaces.model.DualListModel;
import org.primefaces.model.TreeNode;

import fsl.ta.toms.roms.bo.ArteryBO;
import fsl.ta.toms.roms.bo.CourtBO;
import fsl.ta.toms.roms.bo.ITAExaminerBO;
import fsl.ta.toms.roms.bo.JPBO;
import fsl.ta.toms.roms.bo.LocationBO;
import fsl.ta.toms.roms.bo.ParishBO;
import fsl.ta.toms.roms.bo.PoliceOfficerBO;
import fsl.ta.toms.roms.bo.StrategyBO;
import fsl.ta.toms.roms.bo.TAStaffBO;
import fsl.ta.toms.roms.bo.TAVehicleBO;

public class RoadOperationView implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -497678789339036823L;

	private Integer roadOperationId;
	private String operationName;

	private String categoryId;	
	private String categoryDescription;
	
	private String confirmStartPopup;
	private List<StrategyBO> availableStrategies;
	private List<StrategyBO> selectedStrategies;
	private String strategiesStr;

	private List<ParishBO> selectedParishes;
	private List<String> selectedRegions;
	private List<LocationBO> selectedLocations;
	private List<ArteryBO> selectedArteries;
	private List<TAStaffBO> selectedTAStaff;
	private List<String> policeFilters;
	
	
	private List<TeamView> teams;
	TeamView currentTeam = new TeamView();
	
	private String schedulerStaffId;
	private String schedulerFirstName;
	private String schedulerMiddleName;
	private String schedulerLastName;
	
	private String officeLocCode;
	
	/*private Date scheduledStartDtime;
	private Date scheduledEndDtime;
	private Date actualStartDtime;
	private Date actualEndDtime;
	*/
	
	private String scheduledStartDate;
	private String scheduledStartTime;
	private Date scheduledStartDtime;
	private String scheduledEndDate;
	private String scheduledEndTime;
	private Date scheduledEndDtime;
	private int schedNumOfDays;
	private int schedNumOfHours;
	private int schedNumOfMinutes;
	/**
	 * new to track changes
	 */
	private Date savedScheduledStartDtime;
	private Date savedScheduledEndDtime;
	
	
	private String actualStartDate;
	private String actualStartTime;
	private Date actualStartDtime;
	private String actualEndDate;
	private String actualEndTime;
	private Date actualEndDtime;
	private int actualNumOfDays;
	private int actualNumOfHours;
	private int actualNumOfMinutes;
	
	
	private String backDated;

	private String statusId;
	private String statusDescription;
	private Date statusDtime;
	
	private String authorized;
	private String authorizedUserName;
	private String authorizedUserPassword;
	private Date authorizedDtime;
	
	private Integer reasonId;
	private String reasonDescription;
	private String comment;
	
	private String courtIdStr;
	private Integer courtId;
	private CourtBO courtBO;
	private Date courtDate;
	private String courtTime;
	private String courtName;
	
	private String currentUsername;

	
	private TreeNode locationTreeView; 
	
	private boolean allTeamsSelected;
	
	private List<TAStaffBO> possibleTeamLeads;

	private DualListModel<TAStaffBO> listOfStaff;
	private DualListModel<ITAExaminerBO> listOfITA;
	private DualListModel<JPBO> listOfJP;
	private DualListModel<PoliceOfficerBO> listOfPolice;
	private DualListModel<TAVehicleBO> listOfVehicles;

	private List<TAStaffBO> listOfStaffAux;
	private List<TAVehicleBO> listOfVehAux;
	private List<JPBO> listOfJPAux;
	private List<ITAExaminerBO> listOfITAAux;
	private List<PoliceOfficerBO> listOfPoliceAux;
	private List<StrategyBO> listOfStrategyAux;
	
	private DualListModel<StrategyBO> listOfStrategies;

	private TeamView selectedTeam;
	private List<TeamView> selectedTeams;
	
	
	//private String courtAddress;
	private String courtAddressLine1;
	private String courtAddressLine2;
	
	
	private String cancelConfirm;
	private String terminateConfirm;
	private String authorizeConfirm;
	
	
//	private ParishDataModel parishDataMod;
//	private LocationDataModel locationDataMod;
//	private ArteryDataModel arteryDataMod;
	
	
	private Map<String, ParishBO> hashMapOfParishes = new HashMap<String, ParishBO>();
	private Map<String, List<LocationBO>> hashMapOfLocations = new HashMap<String,List<LocationBO>>();
	private Map<Integer, List<ArteryBO>> hashMapOfArteries = new HashMap<Integer, List<ArteryBO>>();
	
	private HashMap<String, ParishBO> hashSelectedParishes = new HashMap<String, ParishBO>();
	private HashMap<Integer, LocationBO> hashSelectedLocations = new HashMap<Integer, LocationBO>();
	private HashMap<Integer, ArteryBO> hashSelectedArteries = new HashMap<Integer, ArteryBO>();
	private HashMap<String, TAStaffBO> hashSelectedTAStaff = new HashMap<String, TAStaffBO>();
	
	//private HashMap<String, TaStaffBO> hashSelectedTAStaff = new HashMap<String, TaStaffBO>();
	
	
	private Integer numOfStrategies, numOfLocations, numOfITA, numOfTAStaff, numOfJP, numOfPolice, numOfVeh;
	private Float totalCoverage;
	
	private ParishDataModel parishDataMod;
	
	//private String listOfSelectedStaffID; 
	
	
	private List<TAStaffBO> availableStaff;
	private List<ITAExaminerBO> availableITA;
	private List<JPBO> availableJP;
	private List<PoliceOfficerBO> availablePolice;
	private List<TAVehicleBO> availableVehicles;
	
	
	private List<TAStaffBO> scheduledStaff = new ArrayList<TAStaffBO>(); 
	private List<ITAExaminerBO> scheduledITA = new ArrayList<ITAExaminerBO>();
	private List<JPBO> scheduledJP = new ArrayList<JPBO>();
	private List<PoliceOfficerBO> scheduledPolice = new ArrayList<PoliceOfficerBO>();
	private List<TAVehicleBO> scheduledVehicles = new ArrayList<TAVehicleBO>();
	
	
	
	private String listOfSelectedStrategyID;
	
	private String autoGeneratedName;
	
	private boolean validUserToScheduleOp;
	
	private Integer policeOfficerCompNum;
	private String policeOfficerFirstName;
	private String policeOfficerLastName;
	private String policeStation;
	
	private Integer teamSerialId;
	/************************2014-10-27:REDO*******************************/
	/**
	 * Fields used in Parish/Location/Artery
	 */
	private List<ParishBO> allParishes;
	private Map<String, List<LocationBO>> allLocationsMap;
	private Map<Integer, List<ArteryBO>> allArteriesMap;
	
	private List<LocationBO> operationLocationList;
	private List<ArteryBO> operationArteryList;
	private List<ParishBO> operationParishList;
	
	private List<LocationBO> selectedLocationList;
	private List<ArteryBO> selectedArteryList;
	private List<ParishBO> selectedParishList;
	/********************************************************/
	
	public RoadOperationView() 
	{
		selectedParishes = new ArrayList<ParishBO>();
		selectedRegions = new ArrayList<String>();
		selectedLocations = new ArrayList<LocationBO>();
		selectedArteries = new ArrayList<ArteryBO>();
		this.terminateConfirm = "N";
		this.policeFilters = new ArrayList<String>();
		teamSerialId = new Integer(0);
		
	}

	

	public RoadOperationView(Integer roadOperationId, String operationName,
			String categoryId, String categoryDescription,
			ArrayList<StrategyBO> availableStrategies,
			ArrayList<StrategyBO> selectedStrategies, String strategiesStr,
			List<ParishBO> selectedParishes, List<String> selectedRegions,
			List<LocationBO> selectedLocations,
			List<ArteryBO> selectedArteries, List<TeamView> teams,
			TeamView currentTeam, String schedulerStaffId,
			String schedulerFirstName, String schedulerMiddleName,
			String schedulerLastName, String officeLocCode,
			String scheduledStartDate, String scheduledStartTime,
			Date scheduledStartDtime, String scheduledEndDate,
			String scheduledEndTime, Date scheduledEndDtime,
			int schedNumOfDays, int schedNumOfHours, String actualStartDate,
			String actualStartTime, Date actualStartDtime,
			String actualEndDate, String actualEndTime, Date actualEndDtime,
			int actualNumOfDays, int actualNumOfHours, String backDated,
			String statusId, String statusDescription, Date statusDtime,
			String authorized, String authorizedUserName, Date authorizedDtime,
			Integer reasonId, String comment, Integer courtId, CourtBO courtBO,
			Date courtDate, String courtTime, String courtName,
			String currentUsername, TreeNode locationTreeView) {
		super();
		this.roadOperationId = roadOperationId;
		this.operationName = operationName;
		this.categoryId = categoryId;
		this.categoryDescription = categoryDescription;
		this.availableStrategies = availableStrategies;
		this.selectedStrategies = selectedStrategies;
		this.strategiesStr = strategiesStr;
		this.selectedParishes = selectedParishes;
		this.selectedRegions = selectedRegions;
		this.selectedLocations = selectedLocations;
		this.selectedArteries = selectedArteries;
		this.teams = teams;
		this.currentTeam = currentTeam;
		this.schedulerStaffId = schedulerStaffId;
		this.schedulerFirstName = schedulerFirstName;
		this.schedulerMiddleName = schedulerMiddleName;
		this.schedulerLastName = schedulerLastName;
		this.officeLocCode = officeLocCode;
		this.scheduledStartDate = scheduledStartDate;
		this.scheduledStartTime = scheduledStartTime;
		this.scheduledStartDtime = scheduledStartDtime;
		this.scheduledEndDate = scheduledEndDate;
		this.scheduledEndTime = scheduledEndTime;
		this.scheduledEndDtime = scheduledEndDtime;
		this.schedNumOfDays = schedNumOfDays;
		this.schedNumOfHours = schedNumOfHours;
		this.actualStartDate = actualStartDate;
		this.actualStartTime = actualStartTime;
		this.actualStartDtime = actualStartDtime;
		this.actualEndDate = actualEndDate;
		this.actualEndTime = actualEndTime;
		this.actualEndDtime = actualEndDtime;
		this.actualNumOfDays = actualNumOfDays;
		this.actualNumOfHours = actualNumOfHours;
		this.backDated = backDated;
		this.statusId = statusId;
		this.statusDescription = statusDescription;
		this.statusDtime = statusDtime;
		this.authorized = authorized;
		this.authorizedUserName = authorizedUserName;
		this.authorizedDtime = authorizedDtime;
		this.reasonId = reasonId;
		this.comment = comment;
		this.courtId = courtId;
		this.courtBO = courtBO;
		this.courtDate = courtDate;
		this.courtTime = courtTime;
		this.courtName = courtName;
		this.currentUsername = currentUsername;
		this.locationTreeView = locationTreeView;
		this.policeFilters = new ArrayList<String>();
	}


	public boolean isOnScheduledDate(){
			
		try{		
			//Calendar today = new GregorianCalendar();
			
			Date today = new Date();
			
			//DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
						
			//System.err.println("Operation status: "+this.getStatusId()+"; "+this.getScheduledStartDtime()+"; backdated: "+this.getBackDated());
			
			//System.err.println("is today b4 sch date: "+today.before(this.getScheduledStartDtime()));
			
			//System.err.println("is today aft sch date: "+today.after(this.getScheduledStartDtime()));
			
			if(Constants.Status.ROAD_OPERATION_NO_ACTION.equalsIgnoreCase(this.getStatusId())){
				return true;
			}
			
			if(today.before(this.getScheduledStartDtime())
					|| !Constants.Status.ROAD_OPERATION_SCHEDULING.equalsIgnoreCase(this.getStatusId())){
				return false;
			}else{
				return true;
			}
		}catch(Exception ex){
			return false;
		}
	}
	
	
	public String getReasonDescription() {
		return reasonDescription;
	}



	public void setReasonDescription(String reasonDescription) {
		this.reasonDescription = reasonDescription;
	}



	public Integer getRoadOperationId() {
		return roadOperationId;
	}

	public void setRoadOperationId(Integer roadOperationId) {
		this.roadOperationId = roadOperationId;
	}

	public String getOperationName() {
		return operationName;
	}

	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryDescription() {
		return categoryDescription;
	}

	public void setCategoryDescription(String categoryDescription) {
		this.categoryDescription = categoryDescription;
	}

	public String getSchedulerStaffId() {
		return schedulerStaffId;
	}

	public void setSchedulerStaffId(String schedulerStaffId) {
		this.schedulerStaffId = schedulerStaffId;
	}

	public String getSchedulerFirstName() {
		return schedulerFirstName;
	}

	public void setSchedulerFirstName(String schedulerFirstName) {
		this.schedulerFirstName = schedulerFirstName;
	}

	public String getSchedulerMiddleName() {
		return schedulerMiddleName;
	}

	public void setSchedulerMiddleName(String schedulerMiddleName) {
		this.schedulerMiddleName = schedulerMiddleName;
	}

	public String getSchedulerLastName() {
		return schedulerLastName;
	}

	public void setSchedulerLastName(String schedulerLastName) {
		this.schedulerLastName = schedulerLastName;
	}

	public String getOfficeLocCode() {
		return officeLocCode;
	}

	public void setOfficeLocCode(String officeLocCode) {
		this.officeLocCode = officeLocCode;
	}

	public String getScheduledStartDate() {
		return scheduledStartDate;
	}

	public void setScheduledStartDate(String scheduledStartDate) {
		this.scheduledStartDate = scheduledStartDate;
	}

	public String getScheduledStartTime() {
		return scheduledStartTime;
	}

	public void setScheduledStartTime(String scheduledStartTime) {
		this.scheduledStartTime = scheduledStartTime;
	}

	public Date getScheduledStartDtime() {
		return scheduledStartDtime;
	}

	public void setScheduledStartDtime(Date scheduledStartDtime) {
		this.scheduledStartDtime = scheduledStartDtime;
	}

	public String getScheduledEndDate() {
		return scheduledEndDate;
	}

	public void setScheduledEndDate(String scheduledEndDate) {
		this.scheduledEndDate = scheduledEndDate;
	}

	public String getScheduledEndTime() {
		return scheduledEndTime;
	}

	public void setScheduledEndTime(String scheduledEndTime) {
		this.scheduledEndTime = scheduledEndTime;
	}

	public Date getScheduledEndDtime() {
		return scheduledEndDtime;
	}

	public void setScheduledEndDtime(Date scheduledEndDtime) {
		this.scheduledEndDtime = scheduledEndDtime;
	}

	public String getActualStartDate() {
		return actualStartDate;
	}

	public void setActualStartDate(String actualStartDate) {
		this.actualStartDate = actualStartDate;
	}

	public String getActualStartTime() {
		return actualStartTime;
	}

	public void setActualStartTime(String actualStartTime) {
		this.actualStartTime = actualStartTime;
	}

	public Date getActualStartDtime() {
		return actualStartDtime;
	}

	public void setActualStartDtime(Date actualStartDtime) {
		this.actualStartDtime = actualStartDtime;
	}

	public String getActualEndDate() {
		return actualEndDate;
	}

	public void setActualEndDate(String actualEndDate) {
		this.actualEndDate = actualEndDate;
	}

	public String getActualEndTime() {
		return actualEndTime;
	}

	public void setActualEndTime(String actualEndTime) {
		this.actualEndTime = actualEndTime;
	}

	public Date getActualEndDtime() {
		return actualEndDtime;
	}

	public void setActualEndDtime(Date actualEndDtime) {
		this.actualEndDtime = actualEndDtime;
	}

	public String getBackDated() {
		return backDated;
	}

	public void setBackDated(String backDated) {
		this.backDated = backDated;
	}

	public String getStatusId() {
		return statusId;
	}

	public void setStatusId(String statusId) {
		this.statusId = statusId;
	}

	public String getStatusDescription() {
		return statusDescription;
	}

	public void setStatusDescription(String statusDescription) {
		this.statusDescription = statusDescription;
	}

	public Date getStatusDtime() {
		return statusDtime;
	}

	public void setStatusDtime(Date statusDtime) {
		this.statusDtime = statusDtime;
	}

	public String getAuthorized() {
		return authorized;
	}

	public void setAuthorized(String authorized) {
		this.authorized = authorized;
	}

	public String getAuthorizedUserName() {
		return authorizedUserName;
	}

	public void setAuthorizedUserName(String authorizedUserName) {
		this.authorizedUserName = authorizedUserName;
	}

	public Date getAuthorizedDtime() {
		return authorizedDtime;
	}

	public void setAuthorizedDtime(Date authorizedDtime) {
		this.authorizedDtime = authorizedDtime;
	}

	public Integer getReasonId() {
		return reasonId;
	}

	public void setReasonId(Integer reasonId) {
		this.reasonId = reasonId;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Integer getCourtId() {
		return courtId;
	}

	public void setCourtId(Integer courtId) {
		this.courtId = courtId;
	}

	public Date getCourtDate() {
		return courtDate;
	}

	public void setCourtDate(Date courtDate) {
		this.courtDate = courtDate;
	}

	public String getCourtTime() {
		return courtTime;
	}

	public void setCourtTime(String courtTime) {
		this.courtTime = courtTime;
	}

	public String getCurrentUsername() {
		return currentUsername;
	}

	public void setCurrentUsername(String currentUsername) {
		this.currentUsername = currentUsername;
	}

	public int getSchedNumOfDays() {
		return schedNumOfDays;
	}

	public void setSchedNumOfDays(int schedNumOfDays) {
		this.schedNumOfDays = schedNumOfDays;
	}

	public int getSchedNumOfHours() {
		return schedNumOfHours;
	}

	public void setSchedNumOfHours(int schedNumOfHours) {
		this.schedNumOfHours = schedNumOfHours;
	}

	public int getActualNumOfDays() {
		return actualNumOfDays;
	}

	public void setActualNumOfDays(int actualNumOfDays) {
		this.actualNumOfDays = actualNumOfDays;
	}

	public int getActualNumOfHours() {
		return actualNumOfHours;
	}

	public void setActualNumOfHours(int actualNumOfHours) {
		this.actualNumOfHours = actualNumOfHours;
	}

	
	

	public List<StrategyBO> getAvailableStrategies() {
		return availableStrategies;
	}

	public void setAvailableStrategies(List<StrategyBO> availableStrategies) {
		this.availableStrategies = availableStrategies;
	}

	public List<StrategyBO> getSelectedStrategies() {
		return selectedStrategies;
	}

	public void setSelectedStrategies(List<StrategyBO> selectedStrategies) {
		this.selectedStrategies = selectedStrategies;
	}

	public List<String> getSelectedRegions() {
		return selectedRegions;
	}

	public void setSelectedRegions(List<String> selectedRegions) {
		this.selectedRegions = selectedRegions;
	}

	public String getStrategiesStr() {
		return strategiesStr;
	}

	public void setStrategiesStr(String strategiesStr) {
		
		List<StrategyBO> strats = this.selectedStrategies;
		
		if(strats != null)
		{
			for (StrategyBO strategyBO : strats) {
				
				this.strategiesStr = this.strategiesStr + strategyBO.getStatusDescription();
			}
			
			
		}else
		{
			this.strategiesStr = strategiesStr;
		}
	}

	
   
	public List<LocationBO> getSelectedLocations() {
		return selectedLocations;
	}

	public void setSelectedLocations(List<LocationBO> selectedLocations) {
		this.selectedLocations = selectedLocations;
	}

	public List<ArteryBO> getSelectedArteries() {
		return selectedArteries;
	}

	public void setSelectedArteries(List<ArteryBO> selectedArteries) {
		this.selectedArteries = selectedArteries;
	}

	public String getCourtName() {
		return courtName;
	}

	public void setCourtName(String courtName) {
		this.courtName = courtName;
	}

	public List<ParishBO> getSelectedParishes() {
		return selectedParishes;
	}

	public void setSelectedParishes(List<ParishBO> selectedParishes) {
		this.selectedParishes = selectedParishes;
	}

	public List<TeamView> getTeams() {
		return teams;
	}

	public void setTeams(List<TeamView> teams) {
		this.teams = teams;
	}

	public TeamView getCurrentTeam() {
		return currentTeam;
	}

	public void setCurrentTeam(TeamView currentTeam) {
		this.currentTeam = currentTeam;
	}

	public TreeNode getLocationTreeView() {
		return locationTreeView;
	}

	public void setLocationTreeView(TreeNode locationTreeView) {
		this.locationTreeView = locationTreeView;
	}

	public CourtBO getCourtBO() {
		return courtBO;
	}

	public void setCourtBO(CourtBO courtBO) {
		this.courtBO = courtBO;
	}



	



	public List<TAStaffBO> getPossibleTeamLeads() {
		return possibleTeamLeads;
	}



	public void setPossibleTeamLeads(List<TAStaffBO> possibleTeamLeads) {
		this.possibleTeamLeads = possibleTeamLeads;
	}



	public DualListModel<TAStaffBO> getListOfStaff() {
		return listOfStaff;
	}



	public void setListOfStaff(DualListModel<TAStaffBO> listOfStaff) {
		this.listOfStaff = listOfStaff;
	}



	public DualListModel<ITAExaminerBO> getListOfITA() {
		return listOfITA;
	}



	public void setListOfITA(DualListModel<ITAExaminerBO> listOfITA) {
		this.listOfITA = listOfITA;
	}



	public DualListModel<JPBO> getListOfJP() {
		return listOfJP;
	}



	public void setListOfJP(DualListModel<JPBO> listOfJP) {
		this.listOfJP = listOfJP;
	}



	public DualListModel<PoliceOfficerBO> getListOfPolice() {
		return listOfPolice;
	}



	public void setListOfPolice(DualListModel<PoliceOfficerBO> listOfPolice) {
		this.listOfPolice = listOfPolice;
	}



	public DualListModel<TAVehicleBO> getListOfVehicles() {
		return listOfVehicles;
	}



	public void setListOfVehicles(DualListModel<TAVehicleBO> listOfVehicles) {
		this.listOfVehicles = listOfVehicles;
	}



	public DualListModel<StrategyBO> getListOfStrategies() {
		return listOfStrategies;
	}



	public void setListOfStrategies(DualListModel<StrategyBO> listOfStrategies) {
		this.listOfStrategies = listOfStrategies;
	}



	public TeamView getSelectedTeam() {
		return selectedTeam;
	}



	public void setSelectedTeam(TeamView selectedTeam) {
		this.selectedTeam = selectedTeam;
	}



	public List<TeamView> getSelectedTeams() {
		return selectedTeams;
	}



	public void setSelectedTeams(List<TeamView> selectedTeams) {
		this.selectedTeams = selectedTeams;
	}

	public List<TAStaffBO> getSelectedTAStaff() {
		return selectedTAStaff;
	}



	public void setSelectedTAStaff(List<TAStaffBO> selectedTAStaff) {
		this.selectedTAStaff = selectedTAStaff;
	}


	public String getCourtIdStr() {
		return courtIdStr;
	}



	public void setCourtIdStr(String courtIdStr) {
		this.courtIdStr = courtIdStr;
	}



	public String getCancelConfirm() {
		return cancelConfirm;
	}



	public void setCancelConfirm(String cancelConfirm) {
		this.cancelConfirm = cancelConfirm;
	}



	/*public String getCourtAddress() {
		return courtAddress;
	}



	public void setCourtAddress(String courtAddress) {
		this.courtAddress = courtAddress;
	}*/



	public Integer getNumOfStrategies() {
		return numOfStrategies;
	}



	public void setNumOfStrategies(Integer numOfStrategies) {
		this.numOfStrategies = numOfStrategies;
	}



	public Integer getNumOfLocations() {
		return numOfLocations;
	}



	public void setNumOfLocations(Integer numOfLocations) {
		this.numOfLocations = numOfLocations;
	}



	public Integer getNumOfITA() {
		return numOfITA;
	}



	public void setNumOfITA(Integer numOfITA) {
		this.numOfITA = numOfITA;
	}



	public Integer getNumOfTAStaff() {
		return numOfTAStaff;
	}



	public void setNumOfTAStaff(Integer numOfTAStaff) {
		this.numOfTAStaff = numOfTAStaff;
	}



	public Integer getNumOfJP() {
		return numOfJP;
	}



	public void setNumOfJP(Integer numOfJP) {
		this.numOfJP = numOfJP;
	}



	public Integer getNumOfPolice() {
		return numOfPolice;
	}



	public void setNumOfPolice(Integer numOfPolice) {
		this.numOfPolice = numOfPolice;
	}



	public Integer getNumOfVeh() {
		return numOfVeh;
	}



	public void setNumOfVeh(Integer numOfVeh) {
		this.numOfVeh = numOfVeh;
	}


	public Map<String, ParishBO> getHashMapOfParishes() {
		return hashMapOfParishes;
	}



	public void setHashMapOfParishes(Map<String, ParishBO> hashMapOfParishes) {
		this.hashMapOfParishes = hashMapOfParishes;
	}






	public Map<String, List<LocationBO>> getHashMapOfLocations() {
		return hashMapOfLocations;
	}



	public void setHashMapOfLocations(
			Map<String, List<LocationBO>> hashMapOfLocations) {
		this.hashMapOfLocations = hashMapOfLocations;
	}



	public Map<Integer, List<ArteryBO>> getHashMapOfArteries() {
		return hashMapOfArteries;
	}



	public void setHashMapOfArteries(Map<Integer, List<ArteryBO>> hashMapOfArteries) {
		this.hashMapOfArteries = hashMapOfArteries;
	}



	public HashMap<String, ParishBO> getHashSelectedParishes() {
		return hashSelectedParishes;
	}



	public void setHashSelectedParishes(
			HashMap<String, ParishBO> hashSelectedParishes) {
		this.hashSelectedParishes = hashSelectedParishes;
	}



	public HashMap<Integer, LocationBO> getHashSelectedLocations() {
		return hashSelectedLocations;
	}



	public void setHashSelectedLocations(
			HashMap<Integer, LocationBO> hashSelectedLocations) {
		this.hashSelectedLocations = hashSelectedLocations;
	}



	public HashMap<Integer, ArteryBO> getHashSelectedArteries() {
		return hashSelectedArteries;
	}



	public void setHashSelectedArteries(
			HashMap<Integer, ArteryBO> hashSelectedArteries) {
		this.hashSelectedArteries = hashSelectedArteries;
	}



	public HashMap<String, TAStaffBO> getHashSelectedTAStaff() {
		return hashSelectedTAStaff;
	}



	public void setHashSelectedTAStaff(
			HashMap<String, TAStaffBO> hashSelectedTAStaff) {
		this.hashSelectedTAStaff = hashSelectedTAStaff;
	}



	public ParishDataModel getParishDataMod() {
		return parishDataMod;
	}



	public void setParishDataMod(ParishDataModel parishDataMod) {
		this.parishDataMod = parishDataMod;
	}



	public List<TAStaffBO> getListOfStaffAux() {
		return listOfStaffAux;
	}



	public void setListOfStaffAux(List<TAStaffBO> listOfStaffAux) {
		this.listOfStaffAux = listOfStaffAux;
	}



	
	public String getTerminateConfirm() {
		return terminateConfirm;
	}



	public void setTerminateConfirm(String terminateConfirm) {
		this.terminateConfirm = terminateConfirm;
	}



	public String getAuthorizeConfirm() {
		return authorizeConfirm;
	}



	public void setAuthorizeConfirm(String authorizeConfirm) {
		this.authorizeConfirm = authorizeConfirm;
	}






	public List<TAStaffBO> getAvailableStaff() {
		return availableStaff;
	}



	public void setAvailableStaff(List<TAStaffBO> availableStaff) {
		this.availableStaff = availableStaff;
	}



	public List<ITAExaminerBO> getAvailableITA() {
		return availableITA;
	}



	public void setAvailableITA(List<ITAExaminerBO> availableITA) {
		this.availableITA = availableITA;
	}



	public List<JPBO> getAvailableJP() {
		return availableJP;
	}



	public void setAvailableJP(List<JPBO> availableJP) {
		this.availableJP = availableJP;
	}



	public List<PoliceOfficerBO> getAvailablePolice() {
		return availablePolice;
	}



	public void setAvailablePolice(List<PoliceOfficerBO> availablePolice) {
		this.availablePolice = availablePolice;
	}



	public List<TAVehicleBO> getAvailableVehicles() {
		return availableVehicles;
	}



	public void setAvailableVehicles(List<TAVehicleBO> availableVehicles) {
		this.availableVehicles = availableVehicles;
	}



	public List<TAVehicleBO> getListOfVehAux() {
		return listOfVehAux;
	}



	public void setListOfVehAux(List<TAVehicleBO> listOfVehAux) {
		this.listOfVehAux = listOfVehAux;
	}



	public List<JPBO> getListOfJPAux() {
		return listOfJPAux;
	}



	public void setListOfJPAux(List<JPBO> listOfJPAux) {
		this.listOfJPAux = listOfJPAux;
	}



	public List<ITAExaminerBO> getListOfITAAux() {
		return listOfITAAux;
	}



	public void setListOfITAAux(List<ITAExaminerBO> listOfITAAux) {
		this.listOfITAAux = listOfITAAux;
	}



	public List<PoliceOfficerBO> getListOfPoliceAux() {
		return listOfPoliceAux;
	}



	public void setListOfPoliceAux(List<PoliceOfficerBO> listOfPoliceAux) {
		this.listOfPoliceAux = listOfPoliceAux;
	}



	public String getListOfSelectedStrategyID() {
		return listOfSelectedStrategyID;
	}



	public void setListOfSelectedStrategyID(String listOfSelectedStrategyID) {
		this.listOfSelectedStrategyID = listOfSelectedStrategyID;
	}



	public List<StrategyBO> getListOfStrategyAux() {
		return listOfStrategyAux;
	}



	public void setListOfStrategyAux(List<StrategyBO> listOfStrategyAux) {
		this.listOfStrategyAux = listOfStrategyAux;
	}



	public String getAuthorizedUserPassword() {
		return authorizedUserPassword;
	}



	public void setAuthorizedUserPassword(String authorizedUserPassword) {
		this.authorizedUserPassword = authorizedUserPassword;
	}



	public String getAutoGeneratedName() {
		return autoGeneratedName;
	}



	public void setAutoGeneratedName(String autoGeneratedName) {
		this.autoGeneratedName = autoGeneratedName;
	}



	public String getConfirmStartPopup() {
		return confirmStartPopup;
	}



	public void setConfirmStartPopup(String confirmStartPopup) {
		this.confirmStartPopup = confirmStartPopup;
	}



	public Float getTotalCoverage() {
		return totalCoverage;
	}



	public void setTotalCoverage(Float totalCoverage) {
		this.totalCoverage = totalCoverage;
	}



	public boolean isValidUserToScheduleOp() {
		return validUserToScheduleOp;
	}



	public void setValidUserToScheduleOp(boolean validUserToScheduleOp) {
		this.validUserToScheduleOp = validUserToScheduleOp;
	}



	public String getCourtAddressLine1() {
		return courtAddressLine1;
	}



	public void setCourtAddressLine1(String courtAddressLine1) {
		this.courtAddressLine1 = courtAddressLine1;
	}



	public String getCourtAddressLine2() {
		return courtAddressLine2;
	}



	public void setCourtAddressLine2(String courtAddressLine2) {
		this.courtAddressLine2 = courtAddressLine2;
	}



	public List<TAStaffBO> getScheduledStaff() {
		return scheduledStaff;
	}



	public void setScheduledStaff(List<TAStaffBO> scheduledStaff) {
		this.scheduledStaff = scheduledStaff;
	}



	public List<ITAExaminerBO> getScheduledITA() {
		return scheduledITA;
	}



	public void setScheduledITA(List<ITAExaminerBO> scheduledITA) {
		this.scheduledITA = scheduledITA;
	}



	public List<JPBO> getScheduledJP() {
		return scheduledJP;
	}



	public void setScheduledJP(List<JPBO> scheduledJP) {
		this.scheduledJP = scheduledJP;
	}



	public List<PoliceOfficerBO> getScheduledPolice() {
		return scheduledPolice;
	}



	public void setScheduledPolice(List<PoliceOfficerBO> scheduledPolice) {
		this.scheduledPolice = scheduledPolice;
	}



	public List<TAVehicleBO> getScheduledVehicles() {
		return scheduledVehicles;
	}



	public void setScheduledVehicles(List<TAVehicleBO> scheduledVehicles) {
		this.scheduledVehicles = scheduledVehicles;
	}



	public int getSchedNumOfMinutes()
	{
		return schedNumOfMinutes;
	}



	public void setSchedNumOfMinutes(int schedNumOfMinutes)
	{
		this.schedNumOfMinutes = schedNumOfMinutes;
	}



	public int getActualNumOfMinutes()
	{
		return actualNumOfMinutes;
	}



	public void setActualNumOfMinutes(int actualNumOfMinutes)
	{
		this.actualNumOfMinutes = actualNumOfMinutes;
	}



	public Integer getPoliceOfficerCompNum()
	{
		return policeOfficerCompNum;
	}



	public void setPoliceOfficerCompNum(Integer policeOfficerCompNum)
	{
		this.policeOfficerCompNum = policeOfficerCompNum;
	}



	public String getPoliceOfficerFirstName()
	{
		return policeOfficerFirstName;
	}



	public void setPoliceOfficerFirstName(String policeOfficerFirstName)
	{
		this.policeOfficerFirstName = policeOfficerFirstName;
	}



	public String getPoliceOfficerLastName()
	{
		return policeOfficerLastName;
	}



	public void setPoliceOfficerLastName(String policeOfficerLastName)
	{
		this.policeOfficerLastName = policeOfficerLastName;
	}



	public String getPoliceStation()
	{
		return policeStation;
	}



	public void setPoliceStation(String policeStation)
	{
		this.policeStation = policeStation;
	}



	public List<String> getPoliceFilters()
	{
		return policeFilters;
	}



	public void setPoliceFilters(List<String> policeFilters)
	{
		this.policeFilters = policeFilters;
	}



	/***********************2014-10-27:REDO*******************************/
	

	public List<ParishBO> getAllParishes() {
		return allParishes;
	}



	public void setAllParishes(List<ParishBO> allParishes) {
		this.allParishes = allParishes;
	}



	public Map<String, List<LocationBO>> getAllLocationsMap() {
		return allLocationsMap;
	}



	public void setAllLocationsMap(Map<String, List<LocationBO>> allLocationsMap) {
		this.allLocationsMap = allLocationsMap;
	}



	public Map<Integer, List<ArteryBO>> getAllArteriesMap() {
		return allArteriesMap;
	}



	public void setAllArteriesMap(Map<Integer, List<ArteryBO>> allArteriesMap) {
		this.allArteriesMap = allArteriesMap;
	}



	public List<LocationBO> getOperationLocationList() {
		return operationLocationList;
	}



	public void setOperationLocationList(List<LocationBO> operationLocationList) {
		this.operationLocationList = operationLocationList;
	}



	public List<ArteryBO> getOperationArteryList() {
		return operationArteryList;
	}



	public void setOperationArteryList(List<ArteryBO> operationArteryList) {
		this.operationArteryList = operationArteryList;
	}



	public List<ParishBO> getOperationParishList() {
		return operationParishList;
	}



	public void setOperationParishList(List<ParishBO> operationParishList) {
		this.operationParishList = operationParishList;
	}



	public List<LocationBO> getSelectedLocationList() {
		return selectedLocationList;
	}



	public void setSelectedLocationList(List<LocationBO> selectedLocationList) {
		this.selectedLocationList = selectedLocationList;
	}



	public List<ArteryBO> getSelectedArteryList() {
		return selectedArteryList;
	}



	public void setSelectedArteryList(List<ArteryBO> selectedArteryList) {
		this.selectedArteryList = selectedArteryList;
	}



	public List<ParishBO> getSelectedParishList() {
		return selectedParishList;
	}



	public void setSelectedParishList(List<ParishBO> selectedParishList) {
		this.selectedParishList = selectedParishList;
	}



	public Date getSavedScheduledStartDtime() {
		return savedScheduledStartDtime;
	}



	public void setSavedScheduledStartDtime(Date savedScheduledStartDtime) {
		this.savedScheduledStartDtime = savedScheduledStartDtime;
	}



	public Date getSavedScheduledEndDtime() {
		return savedScheduledEndDtime;
	}



	public void setSavedScheduledEndDtime(Date savedScheduledEndDtime) {
		this.savedScheduledEndDtime = savedScheduledEndDtime;
	}



	public boolean isAllTeamsSelected() {
		return allTeamsSelected;
	}



	public void setAllTeamsSelected(boolean allTeamsSelected) {
		this.allTeamsSelected = allTeamsSelected;
	}



	public Integer getTeamSerialId() {
		return teamSerialId;
	}



	public void setTeamSerialId(Integer teamSerialId) {
		this.teamSerialId = teamSerialId;
	}

	
	
	
	
	
	
	/*****************************************************/


}
