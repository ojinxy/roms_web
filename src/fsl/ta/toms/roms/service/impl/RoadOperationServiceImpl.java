package fsl.ta.toms.roms.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import fsl.ta.toms.roms.bo.ArteryBO;
import fsl.ta.toms.roms.bo.AssignedPersonBO;
import fsl.ta.toms.roms.bo.AssignedResourceBO;
import fsl.ta.toms.roms.bo.AssignedTeamDetailsBO;
import fsl.ta.toms.roms.bo.AssignedVehicleBO;
import fsl.ta.toms.roms.bo.AvailableResourceBO;
import fsl.ta.toms.roms.bo.AvailableResourceIds;
import fsl.ta.toms.roms.bo.ITAExaminerBO;
import fsl.ta.toms.roms.bo.JPBO;
import fsl.ta.toms.roms.bo.LocationBO;
import fsl.ta.toms.roms.bo.OperationStrategyBO;
import fsl.ta.toms.roms.bo.ParishBO;
import fsl.ta.toms.roms.bo.PoliceOfficerBO;
import fsl.ta.toms.roms.bo.RoadOperationBO;
import fsl.ta.toms.roms.bo.RoadOperationOtherDetailsBO;
import fsl.ta.toms.roms.bo.StrategyBO;
import fsl.ta.toms.roms.bo.StrategyRuleBO;
import fsl.ta.toms.roms.bo.TAStaffBO;
import fsl.ta.toms.roms.bo.TAVehicleBO;
import fsl.ta.toms.roms.bo.TeamBO;
import fsl.ta.toms.roms.constants.Constants;
import fsl.ta.toms.roms.dao.DAOFactory;
import fsl.ta.toms.roms.dao.RoadOperationDAO;
import fsl.ta.toms.roms.dataobjects.ArteryDO;
import fsl.ta.toms.roms.dataobjects.AssignedPersonDO;
import fsl.ta.toms.roms.dataobjects.AssignedPersonKey;
import fsl.ta.toms.roms.dataobjects.AssignedVehicleDO;
import fsl.ta.toms.roms.dataobjects.AssignedVehicleKey;
import fsl.ta.toms.roms.dataobjects.AuditEntry;
import fsl.ta.toms.roms.dataobjects.CDCategoryDO;
import fsl.ta.toms.roms.dataobjects.CDPersonTypeDO;
import fsl.ta.toms.roms.dataobjects.CourtDO;
import fsl.ta.toms.roms.dataobjects.EventAuditDO;
import fsl.ta.toms.roms.dataobjects.LMIS_TAOfficeLocationViewDO;
import fsl.ta.toms.roms.dataobjects.LocationDO;
import fsl.ta.toms.roms.dataobjects.OperationRegionDO;
import fsl.ta.toms.roms.dataobjects.OperationRegionKey;
import fsl.ta.toms.roms.dataobjects.OperationStrategyDO;
import fsl.ta.toms.roms.dataobjects.OperationStrategyKey;
import fsl.ta.toms.roms.dataobjects.ParishDO;
import fsl.ta.toms.roms.dataobjects.PersonDO;
import fsl.ta.toms.roms.dataobjects.ReasonDO;
import fsl.ta.toms.roms.dataobjects.RoadOperationDO;
import fsl.ta.toms.roms.dataobjects.StatusDO;
import fsl.ta.toms.roms.dataobjects.StrategyDO;
import fsl.ta.toms.roms.dataobjects.TAStaffDO;
import fsl.ta.toms.roms.dataobjects.TAVehicleDO;
import fsl.ta.toms.roms.dataobjects.TeamArteryDO;
import fsl.ta.toms.roms.dataobjects.TeamArteryKey;
import fsl.ta.toms.roms.dataobjects.TeamDO;
import fsl.ta.toms.roms.dataobjects.TeamLocationDO;
import fsl.ta.toms.roms.dataobjects.TeamLocationKey;
import fsl.ta.toms.roms.dataobjects.TeamParishDO;
import fsl.ta.toms.roms.dataobjects.TeamParishKey;
import fsl.ta.toms.roms.exception.ErrorSavingException;
import fsl.ta.toms.roms.exception.InvalidFieldException;
import fsl.ta.toms.roms.exception.NoRecordFoundException;
import fsl.ta.toms.roms.search.criteria.impl.AvailableResourceCriteriaBO;
import fsl.ta.toms.roms.search.criteria.impl.RoadOperationCriteriaBO;
import fsl.ta.toms.roms.service.RoadOperationService;
import fsl.ta.toms.roms.service.ServiceFactory;
import fsl.ta.toms.roms.util.DateUtils;
import fsl.ta.toms.roms.util.StringUtil;

@Transactional
public class RoadOperationServiceImpl implements RoadOperationService{

	private DAOFactory daoFactory;
	private ServiceFactory serviceFactory;
	
	public void setDaoFactory(DAOFactory daoFactory) {
		this.daoFactory = daoFactory;
	}
	
	public void setServiceFactory(ServiceFactory serviceFactory) {
		this.serviceFactory = serviceFactory;
	}


	@Override
	public List<RoadOperationBO> lookupRoadOperation(
			RoadOperationCriteriaBO roadOperationCriteriaBO) {
		
		/*List<RoadOperationDO> roadOperationDOList = new ArrayList<RoadOperationDO>();
		List<RoadOperationBO> roadOperationBOList = new ArrayList<RoadOperationBO>();
		RoadOperationBO roadOperationBO;
		
		roadOperationDOList =  daoFactory.getRoadOperationDAO().lookupRoadOperation(roadOperationCriteriaBO);
		
		for(RoadOperationDO roadOpDO : roadOperationDOList){
			roadOperationBO = new RoadOperationBO(roadOpDO);
			roadOperationBOList.add(roadOperationBO);
		}
		
		return roadOperationBOList;*/
		
		return  daoFactory.getRoadOperationDAO().lookupRoadOperation(roadOperationCriteriaBO);
	}
	
	@Override
	public RoadOperationOtherDetailsBO findRoadOperationOtherDetails(
			Integer roadOperationId) {
		
		List<TeamDO> teamList = new ArrayList<TeamDO>();
		RoadOperationOtherDetailsBO roadOperationOtherDetailsBO = new RoadOperationOtherDetailsBO();
		
		List<TAStaffBO> taStaffList = new ArrayList<TAStaffBO>();
		List<JPBO> jpList = new ArrayList<JPBO>();
        List<PoliceOfficerBO> policeOfficerList = new ArrayList<PoliceOfficerBO>();;
		List<ITAExaminerBO> itaExaminerList = new ArrayList<ITAExaminerBO>();
		TAStaffBO teamLeamBO = new TAStaffBO();
		
		AssignedTeamDetailsBO assignedTeamDetailsBO = new AssignedTeamDetailsBO();
		List<AssignedTeamDetailsBO> assignedTeamDetailsBOList = new ArrayList<AssignedTeamDetailsBO>();
		
		teamList = daoFactory.getRoadOperationDAO().findOperationTeams(roadOperationId);
		
		for(TeamDO team : teamList){
			teamLeamBO = new TAStaffBO();
			taStaffList = new ArrayList<TAStaffBO>();
			jpList = new ArrayList<JPBO>();
			policeOfficerList = new ArrayList<PoliceOfficerBO>();
			itaExaminerList = new ArrayList<ITAExaminerBO>();
			
			taStaffList = daoFactory.getRoadOperationDAO().findAssignedStaffByTeam(roadOperationId, team.getTeamId());
			jpList = daoFactory.getRoadOperationDAO().findAssignedJPByTeam(roadOperationId, team.getTeamId());
			policeOfficerList = daoFactory.getRoadOperationDAO().findAssignedPoliceOfficerByTeam(roadOperationId, team.getTeamId());
			itaExaminerList = daoFactory.getRoadOperationDAO().findAssignedITAExaminerByTeam(roadOperationId, team.getTeamId());
			
			List<TAVehicleBO> assignedVehicleList = daoFactory.getRoadOperationDAO().findAssignedTAVehicleByTeam(roadOperationId, team.getTeamId());
			List<ArteryBO> operationArteryList = daoFactory.getRoadOperationDAO().findArteryByTeam(roadOperationId, team.getTeamId());
			List<LocationBO> operationLocationList = daoFactory.getRoadOperationDAO().findLocationByTeam(roadOperationId, team.getTeamId());
			List<ParishBO> teamParishList = daoFactory.getRoadOperationDAO().findParishByTeam(roadOperationId, team.getTeamId());
			teamLeamBO = new TAStaffBO(team.getTeamLead());
			
			
			assignedTeamDetailsBO = new AssignedTeamDetailsBO(taStaffList, jpList, policeOfficerList, itaExaminerList, assignedVehicleList, operationLocationList, operationArteryList, new TeamBO(team), teamParishList);
			
			assignedTeamDetailsBOList.add(assignedTeamDetailsBO);
		}
		
		List<StrategyBO> strategyBOList = daoFactory.getRoadOperationDAO().findStrategyForOperation(roadOperationId);
		List<String> regionList = daoFactory.getRoadOperationDAO().findOperationRegions(roadOperationId);
		
		roadOperationOtherDetailsBO = new RoadOperationOtherDetailsBO(assignedTeamDetailsBOList, strategyBOList, regionList);
		
		
		return roadOperationOtherDetailsBO;
		
	}

	/*@Override
	public RoadOperationOtherDetailsBO findRoadOperationOtherDetails(
			Integer roadOperationId) {

	
		List<TeamDO> teamList = new ArrayList<TeamDO>();
		
		List<AssignedPersonBO> taStaffList = new ArrayList<AssignedPersonBO>();
		List<AssignedPersonBO> jpList = new ArrayList<AssignedPersonBO>();
        List<AssignedPersonBO> policeOfficerList = new ArrayList<AssignedPersonBO>();;
		List<AssignedPersonBO> itaExaminerList = new ArrayList<AssignedPersonBO>();
		 
		TAStaffBO staffBO;
		JPBO jpBO;
		PoliceOfficerBO policeOfficerBO;
		
		teamList = daoFactory.getRoadOperationDAO().findOperationTeams(roadOperationId);
		AssignedTeamDetailsBO assignedTeamDetailsBO = new AssignedTeamDetailsBO();
		List<AssignedTeamDetailsBO> assignedTeamDetailsBOList = new ArrayList<AssignedTeamDetailsBO>();
		List<Integer> teamLocationList;
		List<Integer> teamArteryList;
		for(TeamDO team : teamList){
			List<AssignedPersonBO> assignedPersonList = daoFactory.getRoadOperationDAO().findAssignedPersonByTeam(roadOperationId, team.getTeamId());
			taStaffList = new ArrayList<AssignedPersonBO>();
			jpList = new ArrayList<AssignedPersonBO>();
			policeOfficerList= new ArrayList<AssignedPersonBO>();
			itaExaminerList = new ArrayList<AssignedPersonBO>();
			
			for(AssignedPersonBO assignedPerson : assignedPersonList){
				if(assignedPerson.getPersonTypeId().equalsIgnoreCase(Constants.PersonType.TA_STAFF)){
					taStaffList.add(assignedPerson);
					staffBO = daoFactory.getRoadOperationDAO().findTAStaffByPersonID(assignedPerson.getPersonId());
					if(staffBO!=null){
						assignedPerson.setOtherInfo(staffBO.getStaffTypeCode());
					}
				}
				else if(assignedPerson.getPersonTypeId().equalsIgnoreCase(Constants.PersonType.JP)){
					jpList.add(assignedPerson);
					jpBO = daoFactory.getRoadOperationDAO().findJPByPersonID(assignedPerson.getPersonId());
					if(jpBO!=null){
						assignedPerson.setOtherInfo(jpBO.getParishDescription());
					}
				}
				
				else if(assignedPerson.getPersonTypeId().equalsIgnoreCase(Constants.PersonType.POLICE_OFFCER)){
					policeOfficerList.add(assignedPerson);
					policeOfficerBO = daoFactory.getRoadOperationDAO().findPoliceOfficerByPersonID(assignedPerson.getPersonId());
					if(policeOfficerBO!=null){
						assignedPerson.setOtherInfo(policeOfficerBO.getStationDescription());
					}
				}
				else if(assignedPerson.getPersonTypeId().equalsIgnoreCase(Constants.PersonType.ITA_EXAMINER)){
					itaExaminerList.add(assignedPerson);
				}
			}
			
			List<AssignedVehicleBO> assignedVehicleList = daoFactory.getRoadOperationDAO().findAssignedVehicleByTeam(roadOperationId, team.getTeamId());
			List<OperationArteryBO> operationArteryList = daoFactory.getRoadOperationDAO().findOperationArteryByTeam(roadOperationId, team.getTeamId());
			List<OperationLocationBO> operationLocationList = daoFactory.getRoadOperationDAO().findOperationLocationByTeam(roadOperationId, team.getTeamId());
			
			teamLocationList = new ArrayList<Integer>();
			if(operationLocationList!=null && operationLocationList.size()>0){
				teamLocationList = returnIntegerAssignedLocationList(operationLocationList);
			}
		
			teamArteryList = new ArrayList<Integer>();
			if(operationArteryList!=null && operationArteryList.size()>0){
				teamArteryList= returnIntegerAssignedArteryList(operationArteryList);
			}
			
			assignedTeamDetailsBO = new AssignedTeamDetailsBO(taStaffList, jpList, policeOfficerList, itaExaminerList, assignedVehicleList, teamLocationList, teamArteryList, team.getTeamLead().getStaffId(), team.getTeamId(), team.getTeamName());

			assignedTeamDetailsBOList.add(assignedTeamDetailsBO);
		}
		
		List<OperationStrategyBO> operationStrategyList = daoFactory.getRoadOperationDAO().findOperationStrategy(roadOperationId);
		List<Integer> opStrategyList = new ArrayList<Integer>();	
		
		if(operationStrategyList!=null && operationStrategyList.size()>0){
			opStrategyList = returnIntegerAssignedStrategyList(operationStrategyList);
		}
		
		RoadOperationOtherDetailsBO roadOperationOtherDetailsBO;
		roadOperationOtherDetailsBO = new RoadOperationOtherDetailsBO(assignedTeamDetailsBOList, opStrategyList);
		
		return roadOperationOtherDetailsBO;
	}*/

	@Override
	public TAStaffBO findTAStaffByPersonID(Integer personId) {
		return daoFactory.getRoadOperationDAO().findTAStaffByPersonID(personId);
	}

	@Override
	public JPBO findJPByPersonID(Integer personId) {
		return daoFactory.getRoadOperationDAO().findJPByPersonID(personId);
	}

	@Override
	public ITAExaminerBO findITAExaminerByPersonID(Integer personId) {
		return daoFactory.getRoadOperationDAO().findITAExaminerByPersonID(personId);
	}

	@Override
	public PoliceOfficerBO findPoliceOfficerByPersonID(Integer personId) {
		return daoFactory.getRoadOperationDAO().findPoliceOfficerByPersonID(personId);
	}

	@Override
	public List<TAStaffBO> getAvailableTAStaff(
			AvailableResourceCriteriaBO availableResourceCriteriaBO) {
		return daoFactory.getRoadOperationDAO().getAvailableTAStaff(availableResourceCriteriaBO);
	}

	@Override
	public List<JPBO> getAvailableJP(
			AvailableResourceCriteriaBO availableResourceCriteriaBO) {
		return daoFactory.getRoadOperationDAO().getAvailableJP(availableResourceCriteriaBO);
	}

	@Override
	public List<ITAExaminerBO> getAvailableITAExaminer(
			AvailableResourceCriteriaBO availableResourceCriteriaBO) {
		return daoFactory.getRoadOperationDAO().getAvailableITAExaminer(availableResourceCriteriaBO);
	}

	@Override
	public List<PoliceOfficerBO> getAvailablePoliceOfficer(
			AvailableResourceCriteriaBO availableResourceCriteriaBO) {
		try{
			List<PoliceOfficerBO> polcieOfficers = daoFactory.getRoadOperationDAO().getAvailablePoliceOfficer(availableResourceCriteriaBO);
			
			return polcieOfficers;
		}
		
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<TAVehicleBO> getAvailableTAVehicle(
			AvailableResourceCriteriaBO availableResourceCriteriaBO) {
		return daoFactory.getRoadOperationDAO().getAvailableTAVehicle(availableResourceCriteriaBO);
	}

	@Override
	public AvailableResourceBO getAvailableResource(
			AvailableResourceCriteriaBO availableResourceCriteriaBO) {

		List<TAStaffBO> availableTAStaffList = new ArrayList<TAStaffBO>();
		List<JPBO> availableJPList = new ArrayList<JPBO>();
		List<PoliceOfficerBO> availablePoliceOfficerList = new ArrayList<PoliceOfficerBO>();
		List<ITAExaminerBO> availableITAExaminerList = new ArrayList<ITAExaminerBO>();
		List<TAVehicleBO> availableTAVehicleList = new ArrayList<TAVehicleBO>();
		
		availableTAStaffList = daoFactory.getRoadOperationDAO().getAvailableTAStaff(availableResourceCriteriaBO);
		availableJPList = daoFactory.getRoadOperationDAO().getAvailableJP(availableResourceCriteriaBO);
		try{
			availablePoliceOfficerList = daoFactory.getRoadOperationDAO().getAvailablePoliceOfficer(availableResourceCriteriaBO);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		availableITAExaminerList = daoFactory.getRoadOperationDAO().getAvailableITAExaminer(availableResourceCriteriaBO);
		availableTAVehicleList = daoFactory.getRoadOperationDAO().getAvailableTAVehicle(availableResourceCriteriaBO);
		
		AvailableResourceBO availableResource = new AvailableResourceBO(availableTAStaffList, availableJPList, availablePoliceOfficerList, availableITAExaminerList, availableTAVehicleList);
		
		return availableResource;
		
	}
	
	@Override
	public AvailableResourceIds getAvailableResourceIds(
			AvailableResourceCriteriaBO availableResourceCriteriaBO)
	{
		List<Integer> availableTAStaffList = new ArrayList<Integer>();
		List<Integer> availableJPList = new ArrayList<Integer>();
		List<Integer> availablePoliceOfficerList = new ArrayList<Integer>();
		List<Integer> availableITAExaminerList = new ArrayList<Integer>();
		List<Integer> availableTAVehicleList = new ArrayList<Integer>();
		
		availableTAStaffList = daoFactory.getRoadOperationDAO().getAvailableTAStaffIds(availableResourceCriteriaBO);
		availableJPList = daoFactory.getRoadOperationDAO().getAvailableJPIds(availableResourceCriteriaBO);
		try{
			availablePoliceOfficerList = daoFactory.getRoadOperationDAO().getAvailablePoliceOfficerIds(availableResourceCriteriaBO);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		availableITAExaminerList = daoFactory.getRoadOperationDAO().getAvailableITAExaminerIds(availableResourceCriteriaBO);
		availableTAVehicleList = daoFactory.getRoadOperationDAO().getAvailableTAVehicleIds(availableResourceCriteriaBO);
		
		AvailableResourceIds availableResourceIds = new AvailableResourceIds(availableTAStaffList, availableJPList, availablePoliceOfficerList, availableITAExaminerList, availableTAVehicleList);
		
		return availableResourceIds;
	}
	
	@Override
	public List<TAStaffBO> getAvailableTAStaffResource(AvailableResourceCriteriaBO availableResourceCriteriaBO) {

		List<TAStaffBO> availableTAStaffList = new ArrayList<TAStaffBO>();
		
		availableTAStaffList = daoFactory.getRoadOperationDAO().getAvailableTAStaff(availableResourceCriteriaBO);
		
		return availableTAStaffList;
		
	}
	
	
	@Override
	public List<JPBO> getAvailableJPResource(AvailableResourceCriteriaBO availableResourceCriteriaBO) {

		List<JPBO> availableJPList = new ArrayList<JPBO>();
		
		availableJPList = daoFactory.getRoadOperationDAO().getAvailableJP(availableResourceCriteriaBO);
		
		return availableJPList;
		
	}

	
	@Override
	public List<PoliceOfficerBO> getAvailablePoliceResource(AvailableResourceCriteriaBO availableResourceCriteriaBO) throws NoRecordFoundException {

		List<PoliceOfficerBO> availablePoliceOfficerList = new ArrayList<PoliceOfficerBO>();
		
		//availablePoliceOfficerList = daoFactory.getRoadOperationDAO().getAvailablePoliceOfficer(availableResourceCriteriaBO);
		try{
			availablePoliceOfficerList = daoFactory.getRoadOperationDAO().getAvailablePoliceOfficer(availableResourceCriteriaBO);
		}
		catch(NoRecordFoundException e){
			throw e;
		}
		
		return availablePoliceOfficerList;
		
	}

	
	@Override
	public List<ITAExaminerBO> getAvailableITAResource(AvailableResourceCriteriaBO availableResourceCriteriaBO) {

		List<ITAExaminerBO> availableITAExaminerList = new ArrayList<ITAExaminerBO>();
		
		availableITAExaminerList = daoFactory.getRoadOperationDAO().getAvailableITAExaminer(availableResourceCriteriaBO);
		
		return availableITAExaminerList;
		
	}

	
	@Override
	public List<TAVehicleBO> getAvailableTAVehicleResource(AvailableResourceCriteriaBO availableResourceCriteriaBO) {

		List<TAVehicleBO> availableTAVehicleList = new ArrayList<TAVehicleBO>();
		
		availableTAVehicleList = daoFactory.getRoadOperationDAO().getAvailableTAVehicle(availableResourceCriteriaBO);
		
		
		return availableTAVehicleList;
		
	}

	
	@Override
	public boolean isPersonAvailable(
			AvailableResourceCriteriaBO availableResourceCriteriaBO) {
		return daoFactory.getRoadOperationDAO().isPersonAvailable(availableResourceCriteriaBO);
	}

	@Override
	public boolean isTAVehicleAvailable(
			AvailableResourceCriteriaBO availableResourceCriteriaBO) {
		return daoFactory.getRoadOperationDAO().isTAVehicleAvailable(availableResourceCriteriaBO);
	}

	@Override
	public List<StrategyRuleBO> getOperationStrategyRule(
			List<Integer> strategyIdList) {
		return daoFactory.getRoadOperationDAO().getOperationStrategyRule(strategyIdList);
	}

	@Override
	public boolean isVehicleRequired(List<Integer> strategyIdList) {
		return daoFactory.getRoadOperationDAO().isVehicleRequired(strategyIdList);
	}

	
	/**
	 * @author kpowell
	 * @validates StrategyRules against each team
	 */
	@Override
	public boolean validateStrategyRule(List<StrategyBO> strats, AssignedTeamDetailsBO teamDetails, List<Integer> strategyList) {
		System.err.println("validateStrategyRule");
		boolean valid = true;
		boolean isVehicleRequired = false;

		int staffListSize = (teamDetails.getTaStaffList()!= null)? teamDetails.getTaStaffList().size(): 0;
		int jpListSize = (teamDetails.getJpList()!= null)? teamDetails.getJpList().size():0;
		int itaExaminerListSize = (teamDetails.getItaExaminerList()!= null)? teamDetails.getItaExaminerList().size() : 0;
		int policeOfficerListSize = (teamDetails.getPoliceOfficerList()!= null) ? teamDetails.getPoliceOfficerList().size() : 0;
		boolean vehicleAssigned = false;
		/*RoadOperationView roadOpView = (RoadOperationView) context
				.getFlowScope().get("operation");*/
		List<StrategyRuleBO> strategyRuleList = daoFactory.getRoadOperationDAO().getOperationStrategyRule(strategyList);
		/*(ArrayList<StrategyRuleBO>) context
				.getFlowScope().get("strategyRules");*/
		//List<StrategyBO> strats = roadOpView.getListOfStrategies().getTarget();

		isVehicleRequired = daoFactory.getRoadOperationDAO().isVehicleRequired(strategyList);
		
		if(teamDetails.getTaVehicleList()!=null && teamDetails.getTaVehicleList().size()>0){
			vehicleAssigned=true;
		}
		
		if(isVehicleRequired==true && vehicleAssigned==false){
			return false;
		}
		
			
		if(strategyRuleList!= null){
			for(StrategyRuleBO rule : strategyRuleList){
				if(rule.getPersonTypeId().equalsIgnoreCase(Constants.PersonType.TA_STAFF)){
					if(staffListSize < rule.getMinimum() || staffListSize > rule.getMaximum()){
						return false;
					}
				}
				else if(rule.getPersonTypeId().equalsIgnoreCase(Constants.PersonType.JP)){
					if(jpListSize < rule.getMinimum() || jpListSize > rule.getMaximum()){
						return false;
					}
				}
				else if(rule.getPersonTypeId().equalsIgnoreCase(Constants.PersonType.ITA_EXAMINER)){
					if(itaExaminerListSize < rule.getMinimum() || itaExaminerListSize > rule.getMaximum()){
						return false;
					}
				}
				else if(rule.getPersonTypeId().equalsIgnoreCase(Constants.PersonType.POLICE_OFFCER)){
					if(policeOfficerListSize < rule.getMinimum() || policeOfficerListSize > rule.getMaximum()){
						return false;
					}
				}
			}
		}
		
		
		return true;
	}
	
	/**/
	
	
	@Override
	public boolean isOperationStrategyValid(List<Integer> strategyList,
			List<AssignedTeamDetailsBO> assignedTeamDetailsBOList) {

		List<StrategyRuleBO> strategyRuleList = new ArrayList<StrategyRuleBO>();
		boolean isVehicleRequired = false;
		
		int staffListSize = 0;
		int jpListSize = 0;
		int itaExaminerListSize = 0;
		int policeOfficerListSize = 0;
		boolean  vehicleAssigned = false;
	
		
		strategyRuleList = daoFactory.getRoadOperationDAO().getOperationStrategyRule(strategyList);
		isVehicleRequired = daoFactory.getRoadOperationDAO().isVehicleRequired(strategyList);
		
		
		for(AssignedTeamDetailsBO teamDetails : assignedTeamDetailsBOList){
			if(teamDetails.getTaVehicleList()!=null && teamDetails.getTaVehicleList().size()>0){
				vehicleAssigned=true;
			}
			
			if(teamDetails.getTaStaffList()!=null){
				staffListSize = staffListSize + teamDetails.getTaStaffList().size();
			}
			
			if(teamDetails.getJpList()!=null){
				jpListSize = jpListSize + teamDetails.getJpList().size();
			}
			
			if(teamDetails.getItaExaminerList()!=null){
				itaExaminerListSize = itaExaminerListSize + teamDetails.getItaExaminerList().size();
			}
			
			if(teamDetails.getPoliceOfficerList()!=null){
				policeOfficerListSize = policeOfficerListSize + teamDetails.getPoliceOfficerList().size();
			}
		}
		
		/*if(isVehicleRequired==true && (assignedResourceBO.getTaVehicleList()==null || assignedResourceBO.getTaVehicleList().size()<1)){
			return false;
		}
		
				
		if(assignedResourceBO.getTaStaffList()!=null){
			staffListSize = assignedResourceBO.getTaStaffList().size();
		}
		
		if(assignedResourceBO.getJpList()!=null){
			jpListSize = assignedResourceBO.getJpList().size();
		}
		
		if(assignedResourceBO.getItaExaminerList()!=null){
			itaExaminerListSize = assignedResourceBO.getItaExaminerList().size();
		}
		
		if(assignedResourceBO.getPoliceOfficerList()!=null){
			policeOfficerListSize = assignedResourceBO.getPoliceOfficerList().size();
		}*/
		
		if(isVehicleRequired==true && vehicleAssigned==false){
			return false;
		}
		
			
		if(strategyRuleList!= null){
			for(StrategyRuleBO rule : strategyRuleList){
				if(rule.getPersonTypeId().equalsIgnoreCase(Constants.PersonType.TA_STAFF)){
					if(staffListSize < rule.getMinimum() || staffListSize > rule.getMaximum()){
						return false;
					}
				}
				else if(rule.getPersonTypeId().equalsIgnoreCase(Constants.PersonType.JP)){
					if(jpListSize < rule.getMinimum() || jpListSize > rule.getMaximum()){
						return false;
					}
				}
				else if(rule.getPersonTypeId().equalsIgnoreCase(Constants.PersonType.ITA_EXAMINER)){
					if(itaExaminerListSize < rule.getMinimum() || itaExaminerListSize > rule.getMaximum()){
						return false;
					}
				}
				else if(rule.getPersonTypeId().equalsIgnoreCase(Constants.PersonType.POLICE_OFFCER)){
					if(policeOfficerListSize < rule.getMinimum() || policeOfficerListSize > rule.getMaximum()){
						return false;
					}
				}
			}
		}
		
		
		return true;
	}

	@Override
	public boolean isStrategyIdListValid(List<Integer> strategyList) {
		StrategyDO strategyDO;
		for(Integer strategy : strategyList){
			strategyDO = daoFactory.getRoadOperationDAO().find(StrategyDO.class, strategy);
			
			if(strategyDO==null){
				return false;
			}
		}
		
		return true;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW,rollbackFor={Exception.class,RuntimeException.class})
	public RoadOperationBO saveRoadOperation(RoadOperationBO roadOperationBO,
			List<AssignedTeamDetailsBO> assignedTeamDetailsBOList) throws Exception {

		RoadOperationDAO roadOperationDAO = daoFactory.getRoadOperationDAO(); 
		RoadOperationDO  roadOperationDO = new RoadOperationDO();
		RoadOperationDO saveRoadOperationDO = new RoadOperationDO();
		Calendar calendar = Calendar.getInstance();
		Date currentDate = calendar.getTime();
		EventAuditDO eventAuditDO = new EventAuditDO();
		AuditEntry auditEntry = new AuditEntry();
		try{
			//Set Road Operation Details
			if(StringUtils.trimToNull(roadOperationBO.getOperationName()) != null){
				roadOperationDO.setOperationName(roadOperationBO.getOperationName().trim());
			}
			else{
				roadOperationDO.setOperationName("");
			}
			
			
			CDCategoryDO categoryDO = roadOperationDAO.find(CDCategoryDO.class, roadOperationBO.getCategoryId());
			if(categoryDO !=null){
				roadOperationDO.setCategory(categoryDO);
			}
			else{
				throw new Exception();
			}
			
			/*TAStaffDO teamLead = roadOperationDAO.find(TAStaffDO.class, roadOperationBO.getTeamLeadStaffId());
			if(teamLead != null){
				roadOperationDO.setTeamLead(teamLead);
			}
			else{
				throw new Exception();
			}*/
			
			TAStaffBO taStaffBO = daoFactory.getStaffUserMappingDAO().getStaffByUsername(roadOperationBO.getCurrentUsername());
			if(taStaffBO!=null){
				TAStaffDO scheduler = roadOperationDAO.find(TAStaffDO.class, taStaffBO.getStaffId());
				if(scheduler != null){
					roadOperationDO.setScheduler(scheduler);
					roadOperationDO.setOfficeLocCode(scheduler.getOfficeLocationCode());
				}
			}
			else{
				throw new Exception();
			}
			
			String formattedStartDate = DateUtils.formatDate("yyyy-MM-dd", roadOperationBO.getScheduledStartDate());
			String formattedStartDateTime =  formattedStartDate + " " + roadOperationBO.getScheduledStartTime();
		
			Date scheduledStartDtime = DateUtils.parse(formattedStartDateTime, "yyyy-MM-dd HH:mm:ss");
			//System.err.println(" scheduledStartDtime " + scheduledStartDtime);
			
			//Date fomatSTimeD = DateUtils.parse(roadOperationBO.getScheduledStartTime(), "hh:mm:ss");
			//String formatSTime = DateUtils.formatDate("hh:mm a", fomatSTimeD);
			//Set for event audit
			roadOperationBO.setScheduledStartDtime(roadOperationDO.getScheduledStartDtime());
			
			String formattedEndDate = DateUtils.formatDate("yyyy-MM-dd", roadOperationBO.getScheduledEndDate());
			String formattedEndDateTime =  formattedEndDate + " " + roadOperationBO.getScheduledEndTime();
			Date scheduledEndDtime = DateUtils.parse(formattedEndDateTime, "yyyy-MM-dd HH:mm:ss");
			//System.err.println(" scheduledEndDtime " + scheduledEndDtime);
			
			//Date fomatETimeD = DateUtils.parse(roadOperationBO.getScheduledEndTime(), "hh:mm:ss");
			//String formatETime = DateUtils.formatDate("hh:mm a", fomatETimeD);
			//Set for event audit
			roadOperationBO.setScheduledEndDtime(roadOperationDO.getScheduledEndDtime());
			
			roadOperationDO.setScheduledStartDtime(scheduledStartDtime);
			roadOperationDO.setScheduledEndDtime(scheduledEndDtime);
			
			if(DateUtils.before(scheduledStartDtime, currentDate) || DateUtils.before(scheduledEndDtime, currentDate)){
				roadOperationDO.setBackDated("Y");
				roadOperationDO.setAuthorized("N");
			}
			else{
				roadOperationDO.setBackDated("N");
				roadOperationDO.setAuthorized("Y");
				roadOperationDO.setAuthorizedDtime(currentDate);
				roadOperationDO.setAuthorizedUserName(roadOperationBO.getCurrentUsername().toUpperCase());
			}
			
			StatusDO statusDO = roadOperationDAO.find(StatusDO.class, Constants.Status.ROAD_OPERATION_SCHEDULING);
			
			if(statusDO != null){
				roadOperationDO.setStatus(statusDO);
			}
			else{
				throw new Exception();
			}
			
			if(roadOperationBO.getCategoryId().equalsIgnoreCase(Constants.Category.REGIONAL)){
				CourtDO courtDO = roadOperationDAO.find(CourtDO.class, roadOperationBO.getCourtBO().getCourtId());
				if(courtDO!=null){
					roadOperationDO.setCourt(courtDO);
				}
				else{
					throw new Exception();
				}
				
				String formattedDate = DateUtils.formatDate("yyyy-MM-dd", roadOperationBO.getCourtDate());
				String formattedDateTime =  formattedDate + " " + roadOperationBO.getCourtTime();
			
				Date courtDtime = DateUtils.parse(formattedDateTime, "yyyy-MM-dd HH:mm:ss");
				
				roadOperationDO.setCourtDtime(courtDtime);
				
			}
			
			roadOperationDO.setStatusDtime(currentDate);
			
			auditEntry.setCreateDTime(currentDate);
			auditEntry.setCreateUsername(roadOperationBO.getCurrentUsername());
			
			roadOperationDO.setAuditEntry(auditEntry);
			
			Integer roadOperationId = (Integer) roadOperationDAO.save(roadOperationDO);
			saveRoadOperationDO = roadOperationDAO.find(RoadOperationDO.class, roadOperationId);
			
			if(StringUtils.trimToNull(roadOperationBO.getOperationName()) == null){
				String operationType="";
				if(saveRoadOperationDO.getCategory().getCategoryId().equalsIgnoreCase(Constants.Category.REGIONAL)){
					operationType = Constants.OperationType.REGIONAL;
				}
				else if(saveRoadOperationDO.getCategory().getCategoryId().equalsIgnoreCase(Constants.Category.SPECIAL)){
					operationType = Constants.OperationType.SPECIAL;
				}
				saveRoadOperationDO.setOperationName(saveRoadOperationDO.getOfficeLocCode() + "-" +operationType + "-" + roadOperationId);
			}
			
			RoadOperationBO savedRoadOperationBO = new RoadOperationBO(saveRoadOperationDO);
			
			//Set Assigned Resource Details and Save
			//TeamDO savedTeamDO;
			for(AssignedTeamDetailsBO  teamDetails: assignedTeamDetailsBOList){
				saveTeamAndDetails(teamDetails, saveRoadOperationDO, auditEntry);
				/*Integer teamId = saveTeam(teamDetails, saveRoadOperationDO, auditEntry);
				savedTeamDO = new TeamDO();
				savedTeamDO = roadOperationDAO.find(TeamDO.class, teamId);
				
				if(teamDetails.getTaStaffList()!=null){
					saveAssignedPerson(returnIntegerAssignedStaffPersonList(teamDetails.getTaStaffList()), Constants.PersonType.TA_STAFF, savedTeamDO, auditEntry);
				}
				if(teamDetails.getJpList()!=null){
					saveAssignedPerson(returnIntegerAssignedJPPersonList(teamDetails.getJpList()), Constants.PersonType.JP, savedTeamDO, auditEntry);
				}
				if(teamDetails.getPoliceOfficerList()!=null){
					saveAssignedPerson(returnIntegerAssignedPoliceOfficerPersonList(teamDetails.getPoliceOfficerList()), Constants.PersonType.POLICE_OFFCER, savedTeamDO, auditEntry);
				}
				if(teamDetails.getItaExaminerList()!=null){
					saveAssignedPerson(returnIntegerAssignedITAExaminerPersonList(teamDetails.getItaExaminerList()), Constants.PersonType.ITA_EXAMINER, savedTeamDO, auditEntry);
				}
				if(teamDetails.getTaVehicleList()!=null){
					saveAssignedVehicle(returnIntegerAssignedTAVehicleList(teamDetails.getTaVehicleList()), savedTeamDO, auditEntry);
				}
				
				//Set Assigned Location and Artery Details and Save
				if(teamDetails.getOperationLocationList()!=null){
					saveAssignedLocation(returnIntegerLocationList(teamDetails.getOperationLocationList()), savedTeamDO, auditEntry);
				}
				if(teamDetails.getOperationArteryList()!=null){
					saveAssignedArtery(returnIntegerArteryList(teamDetails.getOperationArteryList()), savedTeamDO, auditEntry);
				}*/
			}
			
			
			
			
			//Set Assigned Strategy Details and Save
			if(roadOperationBO.getOperationStrategyList()!=null){
				saveAssignedStrategy(roadOperationBO.getOperationStrategyList(), saveRoadOperationDO, auditEntry);
			}
			
			
			
			//R.B. Added Feb 2014 : Save Regions for the operation
			if(roadOperationBO.getOfficeLocCodeList()!=null){
				saveOperationRegions(roadOperationBO.getOfficeLocCodeList(), saveRoadOperationDO, auditEntry);
			}
			
			
			
			//String locationList = returnCommaDelimitedIntegerList(assignedOtherDetailsBO.getOpeationLocationList());
			roadOperationBO.setOperationName(savedRoadOperationBO.getOperationName());
			roadOperationBO.setCategoryDescription(savedRoadOperationBO.getCategoryDescription());
			
			
			eventAuditDO = createEventAuditForMainStatesRecord(saveRoadOperationDO, roadOperationBO.getCurrentUsername(), Constants.EventCode.SCHEDULE_OPERATION,roadOperationBO);
			serviceFactory.getEventAuditService().saveEventAuditDO(eventAuditDO);
			
			return savedRoadOperationBO;
		}
		
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw e;
		}
		
	}
	

		/**
		 * This method saves the roms_operation_region record(s)
		 * @param opeationStrategyList
		 * @param saveRoadOperationDO
		 * @param auditEntry
		 * @throws Exception
		 */
		private void  saveOperationRegions(List<String> officeLocCodeList,
				RoadOperationDO saveRoadOperationDO, AuditEntry auditEntry) throws Exception{
			RoadOperationDAO roadOperationDAO = daoFactory.getRoadOperationDAO(); 
		
		
			OperationRegionDO operationRegionDO= new OperationRegionDO();
			OperationRegionKey operationRegionKey  = new OperationRegionKey();
		
			
			for (String region : officeLocCodeList) {
			
				operationRegionKey.setLoc_code(region);
				
				operationRegionDO = new OperationRegionDO();
				operationRegionKey.setRoadOperation(saveRoadOperationDO);
				operationRegionDO.setOperationRegionKey(operationRegionKey);
				operationRegionDO.setAuditEntry(auditEntry);
				
				roadOperationDAO.save(operationRegionDO);
				operationRegionKey  = new OperationRegionKey();
			}
		}
		
		
		

	private Integer saveTeam(AssignedTeamDetailsBO assignedTeamDetailsBO, RoadOperationDO saveRoadOperationDO, AuditEntry auditEntry) throws Exception{
		RoadOperationDAO roadOperationDAO = daoFactory.getRoadOperationDAO(); 
		TeamDO teamDO = new TeamDO();
		
		teamDO.setNoMotorCycleAssigned(assignedTeamDetailsBO.getTeamBO().getNoMotorCycleAssigned());
		teamDO.setTeamName(assignedTeamDetailsBO.getTeamBO().getTeamName().trim());
		teamDO.setRoadOperation(saveRoadOperationDO);
		teamDO.setAuditEntry(auditEntry);
		
		if(assignedTeamDetailsBO.getTeamBO().getTeamLead()!=null){
			TAStaffDO teamLead = roadOperationDAO.find(TAStaffDO.class, assignedTeamDetailsBO.getTeamBO().getTeamLead().getStaffId());
			if(teamLead != null){
				teamDO.setTeamLead(teamLead);
			}
			else{
				throw new Exception();
			}
		}
		
		return roadOperationDAO.saveTeam(teamDO);
	}
	
	/**
	 * This method saves the roms_assigned_person record(s) for the personType passed
	 * @param assignedPersonList
	 * @param personType
	 * @param saveRoadOperationDO
	 * @param auditEntry
	 * @throws Exception
	 */
		
		private void saveAssignedPerson(List<Integer> assignedPersonList, String personType, TeamDO savedTeamDO, AuditEntry auditEntry) throws Exception{
			RoadOperationDAO roadOperationDAO = daoFactory.getRoadOperationDAO(); 
		AssignedPersonDO assignedPersonDO = new AssignedPersonDO();
		PersonDO personDO = new PersonDO();
		AssignedPersonKey assignedPersonKey = new AssignedPersonKey();
	
		CDPersonTypeDO personTypeDO = roadOperationDAO.find(CDPersonTypeDO.class, personType);
		if(personTypeDO == null){
			throw new Exception();
		}
				
		
		for(Integer personId : assignedPersonList){
			
			System.err.println("Person " + personId);
			
			personDO = roadOperationDAO.find(PersonDO.class, personId);
			assignedPersonKey = new AssignedPersonKey();
			if(personDO!=null){
				assignedPersonKey.setPerson(personDO);
			}
			else{
				throw new Exception();
			}
			assignedPersonDO = new AssignedPersonDO();
			
			assignedPersonKey.setPersonType(personTypeDO);
			assignedPersonKey.setTeam(savedTeamDO);
			assignedPersonDO.setAssignedPersonKey(assignedPersonKey);
			assignedPersonDO.setAuditEntry(auditEntry);
			
			roadOperationDAO.saveAssPerson(assignedPersonDO);
		}
	}
	
	/**
	 * This method saves the roms_assigned_vehicle record(s)
	 * @param assignedVehicleList
	 * @param saveRoadOperationDO
	 * @param auditEntry
	 * @throws Exception
	 */
	private void saveAssignedVehicle(List<Integer> assignedVehicleList, TeamDO savedTeamDO, AuditEntry auditEntry) throws Exception{
		RoadOperationDAO roadOperationDAO = daoFactory.getRoadOperationDAO(); 
	
		AssignedVehicleDO assignedVehicleDO = new AssignedVehicleDO();
		TAVehicleDO taVehicleDO = new TAVehicleDO();
		AssignedVehicleKey assignedVehicleKey = new AssignedVehicleKey();
	
		for(Integer assignedVehicle : assignedVehicleList){
			taVehicleDO = roadOperationDAO.find(TAVehicleDO.class, assignedVehicle);
			assignedVehicleKey = new AssignedVehicleKey();
			if(taVehicleDO!=null){
				assignedVehicleKey.setTaVehicle(taVehicleDO);
			}
			else{
				throw new Exception();
			}
			assignedVehicleDO = new AssignedVehicleDO();
			
			assignedVehicleKey.setTeam(savedTeamDO);
			assignedVehicleDO.setAssignedVehicleKey(assignedVehicleKey);
			assignedVehicleDO.setAuditEntry(auditEntry);
			
			roadOperationDAO.save(assignedVehicleDO);
		}
	}
	
	/**
	 * This method saves the roms_operation_strategy record(s)
	 * @param opeationStrategyList
	 * @param saveRoadOperationDO
	 * @param auditEntry
	 * @throws Exception
	 */
	private void saveAssignedStrategy(List<StrategyBO> opeationStrategyList, RoadOperationDO saveRoadOperationDO, AuditEntry auditEntry) throws Exception
	{
		RoadOperationDAO roadOperationDAO = daoFactory.getRoadOperationDAO(); 
	
	
		StrategyDO strategyDO = new StrategyDO();
		OperationStrategyDO operationStrategyDO= new OperationStrategyDO();
		OperationStrategyKey operationStrategyKey  = new OperationStrategyKey();
	
		for(StrategyBO strategy : opeationStrategyList){
			strategyDO = roadOperationDAO.find(StrategyDO.class, strategy.getStrategyId());
			operationStrategyKey = new OperationStrategyKey();
			if(strategyDO!=null){
				operationStrategyKey.setStrategy(strategyDO);
			}else{
				throw new Exception();
			}
			
			operationStrategyDO = new OperationStrategyDO();
			operationStrategyKey.setRoadOperation(saveRoadOperationDO);
			operationStrategyDO.setOperationStrategyKey(operationStrategyKey);
			operationStrategyDO.setAuditEntry(auditEntry);
			
			roadOperationDAO.save(operationStrategyDO);
			
			
		}
	}
	
	private void updateAssignedStrategy(List<StrategyBO> opeationStrategyList, RoadOperationDO saveRoadOperationDO, AuditEntry auditEntry) throws Exception
	{
		if(opeationStrategyList == null || opeationStrategyList.size() < 1)
			return;
		
		RoadOperationDAO roadOperationDAO = daoFactory.getRoadOperationDAO(); 
		
		
		StrategyDO strategyDO = new StrategyDO();
		OperationStrategyDO operationStrategyDO= new OperationStrategyDO();
		OperationStrategyKey operationStrategyKey  = new OperationStrategyKey();
		
		List<OperationStrategyDO> opStrategiesDO =  daoFactory.getRoadOperationDAO().getOpStrategyListDO(saveRoadOperationDO.getRoadOperationId());
		
		
		//Remove strategies which are not in operation strategy list
		for(OperationStrategyDO opStrategy : opStrategiesDO)
		{
			boolean found = false;
			
			for(StrategyBO strategy : opeationStrategyList)
			{
				if(strategy.getStrategyId().intValue() == opStrategy.getOperationStrategyKey().getStrategy().getStrategyId().intValue())
				{
					found = true;
					
					break;
				}
			}
			
			
			if(!found)
			{
				roadOperationDAO.delete(opStrategy);
			}
			
		}
		
		for(StrategyBO strategy : opeationStrategyList)
		{
			boolean found = false;
			
			for(OperationStrategyDO opStrategy : opStrategiesDO)
			{
				if(opStrategy.getOperationStrategyKey().getStrategy().getStrategyId().intValue() == strategy.getStrategyId().intValue())
				{
					found = true;
					
					break;
				}
			}
			
			if(!found)
			{
				strategyDO = roadOperationDAO.find(StrategyDO.class, strategy.getStrategyId());
				operationStrategyKey = new OperationStrategyKey();
				if(strategyDO!=null)
				{
					operationStrategyKey.setStrategy(strategyDO);
				}
				else
				{
					throw new Exception();
				}
				
				operationStrategyDO = new OperationStrategyDO();
				operationStrategyKey.setRoadOperation(saveRoadOperationDO);
				operationStrategyDO.setOperationStrategyKey(operationStrategyKey);
				operationStrategyDO.setAuditEntry(auditEntry);
				
				roadOperationDAO.save(operationStrategyDO);
			}
			
		}
		
	}
	
	/**
	 * This method saves the roms_operation_location record(s)
	 * @param opeationLocationList
	 * @param saveRoadOperationDO
	 * @param auditEntry
	 * @throws Exception
	 */
	private void saveAssignedLocation(List<Integer> teamLocationList, TeamDO teamDO, AuditEntry auditEntry) throws Exception{
		RoadOperationDAO roadOperationDAO = daoFactory.getRoadOperationDAO(); 
	
	
		LocationDO locationDO = new LocationDO();
		TeamLocationDO teamLocationDO= new TeamLocationDO();
		TeamLocationKey teamnLocationKey = new TeamLocationKey();
	
		for(Integer locationId : teamLocationList){
			locationDO = roadOperationDAO.find(LocationDO.class, locationId);
			teamnLocationKey = new TeamLocationKey();
			if(locationDO!=null){
				teamnLocationKey.setLocation(locationDO);
			}
			else{
				throw new Exception();
			}
			
			teamLocationDO = new TeamLocationDO();
			teamnLocationKey.setTeam(teamDO);
			teamLocationDO.setOperationLocationKey(teamnLocationKey);
			teamLocationDO.setAuditEntry(auditEntry);
			
			roadOperationDAO.save(teamLocationDO);
		}
	}
	
	private void updateAssignedLocation(List<LocationBO> opeationLocationList, TeamDO savedTeamDO, AuditEntry auditEntry) throws Exception
	{
		if(opeationLocationList == null || opeationLocationList.size() < 1)
			return;
		
		RoadOperationDAO roadOperationDAO = daoFactory.getRoadOperationDAO(); 
		
		
		LocationDO locationDO = new LocationDO();
		TeamLocationDO teamLocationDO= new TeamLocationDO();
		TeamLocationKey teamLocationKey = new TeamLocationKey();
	
	
		
		List<TeamLocationDO> teamLocationListCurrent = roadOperationDAO.getTeamLocationListDO(savedTeamDO.getTeamId());
		//List of Inputted locations
		for(LocationBO operationLocation : opeationLocationList)
		{
			boolean found = false;
			//Compares the inputted list to the list of locations from database
			for(TeamLocationDO teamLocation : teamLocationListCurrent)
			{
				if(teamLocation.getOperationLocationKey().getLocation().getLocationId().intValue() == operationLocation.getLocationId().intValue())
					{
						found = true;
						
						continue;
					}
			 }
			//If not found is then added to database
			if(!found)
			{
				locationDO = roadOperationDAO.find(LocationDO.class, operationLocation.getLocationId());
				
				teamLocationKey = new TeamLocationKey();
				
				if(locationDO != null){
					teamLocationKey.setLocation(locationDO);
				}
				else{
					throw new Exception();
				}
				teamLocationDO = new TeamLocationDO();
				
				teamLocationKey.setTeam(savedTeamDO);
				teamLocationDO.setOperationLocationKey(teamLocationKey);
				teamLocationDO.setAuditEntry(auditEntry);
				roadOperationDAO.save(teamLocationDO);
			}
		}
		//Inputted list
		for(TeamLocationDO teamLocation : teamLocationListCurrent)
		{
			boolean found = false;
			//List from Database
			for(LocationBO operationArtery : opeationLocationList)
			{
				if(teamLocation.getOperationLocationKey().getLocation().getLocationId().intValue() == operationArtery.getLocationId().intValue())
				{
					found = true;
					continue;
				}
				
				
			}
			//Remove from database if not found in list
			if(!found)
			{
				roadOperationDAO.delete(teamLocation);
			}
		}
	}
	
	/**
	 * This method saves the roms_operation_artery record(s)
	 * @param opeationArteryList
	 * @param saveRoadOperationDO
	 * @param auditEntry
	 * @throws Exception
	 */
	private void saveAssignedArtery(List<Integer> opeationArteryList, TeamDO savedTeamDO, AuditEntry auditEntry) throws Exception{
		RoadOperationDAO roadOperationDAO = daoFactory.getRoadOperationDAO(); 
	
	
		ArteryDO arteryDO = new ArteryDO();
		TeamArteryDO teamArteryDO = new TeamArteryDO();
		TeamArteryKey teamArteryKey = new TeamArteryKey();
	
		for(Integer arteryId : opeationArteryList){
			arteryDO = roadOperationDAO.find(ArteryDO.class, arteryId);
			
			teamArteryKey = new TeamArteryKey();
			
			if(arteryDO!=null){
				teamArteryKey.setArtery(arteryDO);
			}
			else{
				throw new Exception();
			}
			teamArteryDO = new TeamArteryDO();
			
			teamArteryKey.setTeam(savedTeamDO);
			teamArteryDO.setOperationArteryKey(teamArteryKey);
			teamArteryDO.setAuditEntry(auditEntry);
			
			roadOperationDAO.save(teamArteryDO);
		}
	}
	
	private void updateAssignedArtery(List<ArteryBO> opeationArteryList, TeamDO savedTeamDO, AuditEntry auditEntry) throws Exception
	{
		
		if(opeationArteryList == null || opeationArteryList.size() < 1)
			return;
		
		RoadOperationDAO roadOperationDAO = daoFactory.getRoadOperationDAO(); 
		
		
		ArteryDO arteryDO = new ArteryDO();
		TeamArteryDO teamArteryDO = new TeamArteryDO();
		TeamArteryKey teamArteryKey = new TeamArteryKey();
	
		
		List<TeamArteryDO> teamArteryListCurrent = roadOperationDAO.getTeamArteryListDO(savedTeamDO.getTeamId());
		//Inputted list of arteries
		for(ArteryBO operationArtery : opeationArteryList)
		{
			boolean found = false;
			//List of arteries compared 
			for(TeamArteryDO teamArtery : teamArteryListCurrent)
			{
				if(teamArtery.getOperationArteryKey().getArtery().getArteryId().intValue() == operationArtery.getArteryId().intValue())
				{
					found = true;
					
					break;
				}
			}
			//Added if not in databse
			if(!found)
			{
				arteryDO = roadOperationDAO.find(ArteryDO.class, operationArtery.getArteryId());
				
				teamArteryKey = new TeamArteryKey();
				
				if(arteryDO!=null){
					teamArteryKey.setArtery(arteryDO);
				}
				else{
					throw new Exception();
				}
				teamArteryDO = new TeamArteryDO();
				
				teamArteryKey.setTeam(savedTeamDO);
				teamArteryDO.setOperationArteryKey(teamArteryKey);
				teamArteryDO.setAuditEntry(auditEntry);
				
				roadOperationDAO.save(teamArteryDO);
			}
		}
		
		for(TeamArteryDO teamArtery : teamArteryListCurrent)
		{
			boolean found = false;
			
			for(ArteryBO operationArtery : opeationArteryList)
			{
				if(teamArtery.getOperationArteryKey().getArtery().getArteryId().intValue() == operationArtery.getArteryId().intValue())
				{
					found = true;
					
					break;
				}
			}
			//Removed from database if not in inputted list
			if(!found)
			{
				roadOperationDAO.delete(teamArtery);
			}
		}
	}
	
	
	
	/**
	 * This method saves the roms_team_parish record(s)
	 * @param opeationArteryList
	 * @param saveRoadOperationDO
	 * @param auditEntry
	 * @throws Exception
	 */
	private void saveAssignedParish(List<String> opeationParishList, TeamDO savedTeamDO, AuditEntry auditEntry) throws Exception{
		RoadOperationDAO roadOperationDAO = daoFactory.getRoadOperationDAO(); 
	
	
		ParishDO parishDO = new ParishDO();
		TeamParishDO teamParishDO = new TeamParishDO();
		TeamParishKey teamParishKey = new TeamParishKey();
	
		for (String parishCode : opeationParishList) {
			teamParishDO = new TeamParishDO();
			parishDO = roadOperationDAO.find(ParishDO.class, parishCode);
			
			if(parishDO!=null){
				teamParishKey.setParish(parishDO);
			}
			else{
				throw new Exception();
			}
			
			teamParishKey.setTeam(savedTeamDO);
			teamParishDO.setTeamParishKey(teamParishKey);
			teamParishDO.setAuditEntry(auditEntry);
			
			roadOperationDAO.save(teamParishDO);
			teamParishKey = new TeamParishKey();
		}
	}
	
	private void updateAssignedParish(List<ParishBO> opeationParishList, TeamDO savedTeamDO, AuditEntry auditEntry) throws Exception
	{
		
		if(opeationParishList == null || opeationParishList.size() < 1)
			return;
		
		RoadOperationDAO roadOperationDAO = daoFactory.getRoadOperationDAO(); 
		
		
		ParishDO parishDO = new ParishDO();
		TeamParishDO teamParishDO = new TeamParishDO();
		TeamParishKey teamParishKey = new TeamParishKey();
		
		List<TeamParishDO> teamParishListCurrent = roadOperationDAO.getTeamParishListDO(savedTeamDO.getTeamId());
		
		for(ParishBO operationParish : opeationParishList)
		{
			boolean found = false;
			
			for(TeamParishDO teamParish : teamParishListCurrent)
			{
				if(teamParish.getTeamParishKey().getParish().getParishCode().equalsIgnoreCase(operationParish.getParishCode()))
				{
					found = true;
					
					break;
				}
			}
			
			if(!found)
			{
				teamParishDO = new TeamParishDO();
				parishDO = roadOperationDAO.find(ParishDO.class, operationParish.getParishCode());
				
				if(parishDO!=null){
					teamParishKey.setParish(parishDO);
				}
				else
				{
					throw new Exception();
				}
				
				teamParishKey.setTeam(savedTeamDO);
				teamParishDO.setTeamParishKey(teamParishKey);
				teamParishDO.setAuditEntry(auditEntry);
				
				roadOperationDAO.save(teamParishDO);
				teamParishKey = new TeamParishKey();
			}
		}
		
		for(TeamParishDO teamParish : teamParishListCurrent)
		{
			boolean found = false;
			
			for(ParishBO operationParish : opeationParishList)
			{
				if(operationParish.getParishCode().equalsIgnoreCase(teamParish.getTeamParishKey().getParish().getParishCode()))
				{
					found = true;
					
					break;
				}
			}
			if(!found)
			{
				roadOperationDAO.delete(teamParish);
			}
		}
	}
	
	/**
	 * This method populates the Event Audit record to be saved for Scheduling, Starting, Completing or Closing a Road Operation
	 * @param savedRoadOperationDO
	 * @param currentUsername
	 * @param eventCode
	 * @param roadOperationBO 
	 * @return
	 * @throws Exception
	 */
	private EventAuditDO createEventAuditForMainStatesRecord(RoadOperationDO savedRoadOperationDO, String currentUsername, Integer eventCode, RoadOperationBO roadOperationBO)throws Exception{
		try{
		EventAuditDO eventAuditBO = new EventAuditDO();
		AuditEntry auditEntry = new AuditEntry();   
		StringBuffer comment = new StringBuffer();
		RoadOperationDAO roadOperationDAO = daoFactory.getRoadOperationDAO(); 
		
		auditEntry.setCreateUsername(currentUsername);
		
		eventAuditBO.setEventCode(eventCode);	  
		
		eventAuditBO.setRefType1Code(Constants.EventRefTypeCode.OPERATION_NAME);
		eventAuditBO.setRefValue1(savedRoadOperationDO.getOperationName());
		
		eventAuditBO.setRefType2Code(Constants.EventRefTypeCode.OPERATION_CATEGORY);
		eventAuditBO.setRefValue2(savedRoadOperationDO.getCategory().getDescription());
			
		
		//get reason
		ReasonDO reasonDO = null;
		String reasonDesc = null;
		
		if(roadOperationBO.getReasonId() != null)
		{
			reasonDO = roadOperationDAO.find(ReasonDO.class, roadOperationBO.getReasonId());
			
			if(reasonDO != null)
			{
				reasonDesc = reasonDO.getDescription();
			}
		}
		
		
		if(eventCode.equals(Constants.EventCode.CLOSE_OPERATION) || eventCode.equals(Constants.EventCode.CANCEL_OPERATION))
		{
			if(roadOperationBO != null)
			{
				comment.append(StringUtil.appendSemiColon(comment.toString(),"Reason: " + reasonDesc));
			}
			
			
			if(savedRoadOperationDO.getComment() != null)
			{
				comment.append(StringUtil.appendSemiColon(comment.toString(),"Comment: " + savedRoadOperationDO.getComment()));
			}
			
		}
	
		
		
		String actualFormatedStartDTime = "";
		String actualFormatedEndDTime = "";
		String scheduledFormatedStartDTime = "";
		String scheduledFormatedEndDTime = "";
		
		if(savedRoadOperationDO.getActualStartDtime() != null)
			actualFormatedStartDTime = DateUtils.formatDate("yyyy-MM-dd hh:mm a",  savedRoadOperationDO.getActualStartDtime());
		
		if(savedRoadOperationDO.getActualEndDtime() != null)
			actualFormatedEndDTime = DateUtils.formatDate("yyyy-MM-dd hh:mm a",  savedRoadOperationDO.getActualEndDtime());
		
		if(savedRoadOperationDO.getScheduledStartDtime() != null)
			scheduledFormatedStartDTime = DateUtils.formatDate("yyyy-MM-dd hh:mm a",  savedRoadOperationDO.getScheduledStartDtime());
		
		if(savedRoadOperationDO.getScheduledEndDtime() != null)
			scheduledFormatedEndDTime = DateUtils.formatDate("yyyy-MM-dd hh:mm a",  savedRoadOperationDO.getScheduledEndDtime());
		
		
		
		if(eventCode.equals(Constants.EventCode.CLOSE_OPERATION) 
		|| eventCode.equals(Constants.EventCode.COMPLETE_OPERATION))
		{
			comment.append(StringUtil.appendSemiColon(comment.toString(),"Actual Date and Time: " + actualFormatedStartDTime + 
                " - "  + actualFormatedEndDTime));
		}else
		{
			
			if(eventCode.equals(Constants.EventCode.START_OPERATION))
			{
				comment.append(StringUtil.appendSemiColon(comment.toString(),"Actual Start Date and Time: " + actualFormatedStartDTime));
			}else
			{
				comment.append(StringUtil.appendSemiColon(comment.toString(),"Scheduled Date and Time: " + scheduledFormatedStartDTime + 
	                " - "  + scheduledFormatedEndDTime));
			}
		}
		
		
		
		eventAuditBO.setComment(comment.toString());
	
		
	
		eventAuditBO.setAuditEntry(auditEntry);
		
		return eventAuditBO;
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new Exception();
		}
	}
	
	
	/**
	 * This method return an Integer List of Person Id for Assigned Staff
	 * @param assignedPersonList
	 * @return
	 */
	private List<Integer> returnIntegerAssignedStaffPersonList(List<TAStaffBO> staffList){
		List<Integer> integerList = new ArrayList<Integer>();
		
		for(TAStaffBO staff : staffList){
			integerList.add(staff.getPersonId());
		}
		
		return integerList;
	}
	
	/**
	 * This method return an Integer List of Person Id for Assigned JP
	 * @param assignedPersonList
	 * @return
	 */
	private List<Integer> returnIntegerAssignedJPPersonList(List<JPBO> jpList){
		List<Integer> integerList = new ArrayList<Integer>();
		
		for(JPBO jp : jpList){
			integerList.add(jp.getPersonBO().getPersonId());
		}
		
		return integerList;
	}
	
	/**
	 * This method return an Integer List of Person Id for Assigned ITA Examiner
	 * @param assignedPersonList
	 * @return
	 */
	private List<Integer> returnIntegerAssignedITAExaminerPersonList(List<ITAExaminerBO> itaList){
		List<Integer> integerList = new ArrayList<Integer>();
		
		for(ITAExaminerBO ita : itaList){
			integerList.add(ita.getPersonBO().getPersonId());
		}
		
		return integerList;
	}
	
	/**
	 * This method return an Integer List of Person Id for Assigned ITA Examiner
	 * @param assignedPersonList
	 * @return
	 */
	private List<Integer> returnIntegerAssignedPoliceOfficerPersonList(List<PoliceOfficerBO> policeList){
		List<Integer> integerList = new ArrayList<Integer>();
		
		for(PoliceOfficerBO police : policeList){
			integerList.add(police.getPersonID());
		}
		
		return integerList;
	}
	

	/**
	 * This method return an Integer List of Person Id for Assigned Person(s)
	 * @param assignedPersonList
	 * @return
	 */
	private List<Integer> returnIntegerAssignedPersonList(List<AssignedPersonBO> assignedPersonList){
		List<Integer> integerList = new ArrayList<Integer>();
		
		for(AssignedPersonBO assignedPerson : assignedPersonList){
			integerList.add(assignedPerson.getPersonId());
		}
		
		return integerList;
	}
	
	/**
	 * This method return an Integer List of TA Vehicle Id for Assigned Vehicle(s)
	 * @param assignedVehicleList
	 * @return
	 */
	private List<Integer> returnIntegerAssignedVehicleList(List<AssignedVehicleBO> assignedVehicleList){
		List<Integer> integerList = new ArrayList<Integer>();
		
		for(AssignedVehicleBO assignedVehicle : assignedVehicleList){
			integerList.add(assignedVehicle.getTaVehicle().getTaVehicleId());
		}
		
		return integerList;
	}
	
	
	/**
	 * This method return an Integer List of Artery Id for Assigned Vehicle(s)
	 * @param assignedVehicleList
	 * @return
	 */
	private List<Integer> returnIntegerAssignedArteryList(List<ArteryBO> assignedArteryList){
		List<Integer> integerList = new ArrayList<Integer>();
		
		for(ArteryBO assignedArtery : assignedArteryList){
			integerList.add(assignedArtery.getArteryId());
		}
		
		return integerList;
	}
	
	/**
	 * This method return an Integer List of location Id for Assigned Vehicle(s)
	 * @param assignedVehicleList
	 * @return
	 */
	private List<Integer> returnIntegerAssignedLocationList(List<LocationBO> assignedLocationList){
		List<Integer> integerList = new ArrayList<Integer>();
		
		for(LocationBO assignedLocation : assignedLocationList){
			integerList.add(assignedLocation.getLocationId());
		}
		
		return integerList;
	}
	
	/**
	 * This method return an Integer List of TA Vehicle Id for Assigned Vehicle(s)
	 * @param assignedVehicleList
	 * @return
	 */
	private List<Integer> returnIntegerAssignedTAVehicleList(List<TAVehicleBO> assignedVehicleList){
		List<Integer> integerList = new ArrayList<Integer>();
		
		for(TAVehicleBO assignedVehicle : assignedVehicleList){
			integerList.add(assignedVehicle.getTaVehicleId());
		}
		
		return integerList;
	}
	
	
	/**
	 * This method returns am Integer List of TA Vehicle Id to be Added to the roms_assigned_vehicle table
	 * @param assignedVehicleList
	 * @param roadOperationId
	 * @return
	 */
	private List<Integer> returnAssignedVehicleToAdd(List<Integer> assignedVehicleIntegerList, Integer teamId){
		List<Integer> addList = null;
		//List<Integer> assignedVehicleIntegerList = null;
		List<Integer> savedVehicleIntegerList = null;
		List<AssignedVehicleBO> savedVehicleList = new ArrayList<AssignedVehicleBO>();
		
		/*if(assignedVehicleList!=null){
			assignedVehicleIntegerList = returnIntegerAssignedVehicleList(assignedVehicleList);
		}*/
		
		savedVehicleList = daoFactory.getRoadOperationDAO().findAssignedTeamVehicle(teamId);
		if(savedVehicleList!=null){
			savedVehicleIntegerList = returnIntegerAssignedVehicleList(savedVehicleList);
			if(assignedVehicleIntegerList!=null){
				addList = new ArrayList<Integer>();
				for(Integer vehicleId : assignedVehicleIntegerList){
					if(!savedVehicleIntegerList.contains(vehicleId)){
						addList.add(vehicleId);
					}
				}
			}
		}
		else{
			return assignedVehicleIntegerList;
		}
		return addList;
	}
	
	/**
	 * This method returns am Integer List of TA Vehicle Id to be Deleted from the roms_assigned_vehicle table
	 * @param assignedVehicleList
	 * @param roadOperationId
	 * @return
	 */
	private List<Integer> returnAssignedVehicleToDelete(List<Integer> assignedVehicleIntegerList, Integer teamId){
		List<Integer> deleteList = null;
		//List<Integer> assignedVehicleIntegerList = null;
		List<Integer> savedVehicleIntegerList = null;
		List<AssignedVehicleBO> savedVehicleList = new ArrayList<AssignedVehicleBO>();
		
		/*if(assignedVehicleList!=null){
			assignedVehicleIntegerList = returnIntegerAssignedVehicleList(assignedVehicleList);
		}*/
		
		savedVehicleList = daoFactory.getRoadOperationDAO().findAssignedTeamVehicle(teamId);
		if(savedVehicleList!=null){
			savedVehicleIntegerList = returnIntegerAssignedVehicleList(savedVehicleList);
			
			if(assignedVehicleIntegerList==null){
				return savedVehicleIntegerList;
			}
			
			deleteList = new ArrayList<Integer>();
			for(Integer vehicleId : savedVehicleIntegerList){
				if(!assignedVehicleIntegerList.contains(vehicleId)){
					deleteList.add(vehicleId);
				}
			}
			
		}
		
		
		return deleteList;
	}
	
	/**
	 * This method returns am Integer List of Person Id to be Added to the roms_assigned_person table
	 * @param assignedPersonList
	 * @param roadOperationId
	 * @param personType
	 * @return
	 */
	private List<Integer> returnAssignedPersonToAdd(List<Integer> assignedPersonIntegerList, Integer teamId, String personType){
		List<Integer> addList = null;
		//List<Integer> assignedPersonIntegerList = null;
		List<Integer> savedPersonIntegerList = null;
		List<AssignedPersonBO> savedPersonList = new ArrayList<AssignedPersonBO>();
		
		/*if(assignedPersonList!=null){
			assignedPersonIntegerList = returnIntegerAssignedPersonList(assignedPersonList);
		}*/
		
		savedPersonList = daoFactory.getRoadOperationDAO().findAssignedTeamPersonByPersonType(teamId, personType);
		if(savedPersonList!=null){
			savedPersonIntegerList = returnIntegerAssignedPersonList(savedPersonList);
			if(assignedPersonIntegerList!=null){
				addList = new ArrayList<Integer>();
				for(Integer personId : assignedPersonIntegerList){
					if(!savedPersonIntegerList.contains(personId)){
						addList.add(personId);
					}
				}
			}
		}
		else{
			return assignedPersonIntegerList;
		}
		return addList;
	}
	
	/**
	 * This method returns am Integer List of Person Id to be Deleted from the roms_assigned_person table
	 * @param assignedPersonList
	 * @param roadOperationId
	 * @param personType
	 * @return
	 */
	private List<Integer> returnAssignedPersonToDelete(List<Integer> assignedPersonIntegerList, Integer teamId, String personType){
		List<Integer> deleteList = null;
		List<Integer> savedPersonIntegerList = null;
		List<AssignedPersonBO> savedPersonList = new ArrayList<AssignedPersonBO>();
		
		
		
		
		savedPersonList = daoFactory.getRoadOperationDAO().findAssignedTeamPersonByPersonType(teamId, personType);
		if(savedPersonList!=null){
			savedPersonIntegerList = returnIntegerAssignedPersonList(savedPersonList);
			
			if(assignedPersonIntegerList==null){
				return savedPersonIntegerList;
			}
			
			deleteList = new ArrayList<Integer>();
			for(Integer personId : savedPersonIntegerList){
				if(!assignedPersonIntegerList.contains(personId)){
					deleteList.add(personId);
				}
			}
			
		}
		
		
		return deleteList;
	}
	
	/**
	 * This method deletes a list of assigned persons from the roms_assigned_person table
	 * @param assignedPersonList
	 * @param roadOperationId
	 * @param personTypeId
	 * @throws Exception
	 */
	private void deleteAssignedPerson(List<Integer> assignedPersonList, Integer teamId, String personTypeId) throws Exception{
		RoadOperationDAO roadOperationDAO = daoFactory.getRoadOperationDAO(); 
	
		AssignedPersonDO assignedPersonDO = new AssignedPersonDO();
		
		for(Integer personId : assignedPersonList){
			assignedPersonDO = roadOperationDAO.findAssignedPersonById(personId, teamId, personTypeId);
			if(assignedPersonDO!=null){
				roadOperationDAO.delete(assignedPersonDO);
			}
			else{
				throw new Exception();
			}
		}
	}
	
	/**
	 * This method deletes a list of assigned artery from the roms_assigned_person table
	 * @param assignedPersonList
	 * @param roadOperationId
	 * @param personTypeId
	 * @throws Exception
	 */
	private void deleteAssignedArtery(List<Integer> assignedArteryList, Integer teamId) throws Exception{
		RoadOperationDAO roadOperationDAO = daoFactory.getRoadOperationDAO(); 
	
		TeamArteryDO assTeamArteryDO = new TeamArteryDO();
		
				
		
		for(Integer arteryId : assignedArteryList){
			assTeamArteryDO = roadOperationDAO.findAssignedArteryById(arteryId, teamId);
			if(assTeamArteryDO!=null){
				roadOperationDAO.delete(assTeamArteryDO);
			}
			else{
				throw new Exception();
			}
		}
	}
	
	/**
	 * This method deletes a list of assigned location from the roms_assigned_person table
	 * @param assignedPersonList
	 * @param roadOperationId
	 * @param personTypeId
	 * @throws Exception
	 */
	private void deleteAssignedLocation(List<Integer> assignedLocationList, Integer teamId) throws Exception{
		RoadOperationDAO roadOperationDAO = daoFactory.getRoadOperationDAO(); 
	
		TeamLocationDO assTeamLocationDO = new TeamLocationDO();
		
				
		
		for(Integer locationId : assignedLocationList){
			assTeamLocationDO = roadOperationDAO.findAssignedLocationById(locationId, teamId);
			if(assTeamLocationDO!=null){
				roadOperationDAO.delete(assTeamLocationDO);
			}
			else{
				throw new Exception();
			}
		}
	}
	
	/**
	 * This method deletes a list of assigned vehicle from the roms_assigned_vehicle table
	 * @param assignedVehicleList
	 * @param roadOperationId
	 * @throws Exception
	 */
	private void deleteAssignedVehicle(List<Integer> assignedVehicleList, Integer teamId) throws Exception{
		RoadOperationDAO roadOperationDAO = daoFactory.getRoadOperationDAO(); 
	
		AssignedVehicleDO assignedVehicleDO = new AssignedVehicleDO();
		
		
		for(Integer taVehicleId : assignedVehicleList){
			assignedVehicleDO = roadOperationDAO.findAssignedVehicleById(taVehicleId, teamId);
			if(assignedVehicleDO!=null){
				roadOperationDAO.delete(assignedVehicleDO);
			}
			else{
				throw new Exception();
			}
		}
	}
	
	/**
	 * This method updates the Attended flag and the comment field of the roms_assigned_person table
	 * @param assignedResourceBO
	 * @param roadOperationBO
	 * @throws Exception
	 */
	private void updateAssignedPerson(AssignedTeamDetailsBO assignedResourceBO, RoadOperationBO roadOperationBO)throws Exception{
		AssignedPersonDO assignedPersonDO = new AssignedPersonDO();
		RoadOperationDAO roadOperationDAO = daoFactory.getRoadOperationDAO();
		Calendar calendar = Calendar.getInstance();
		Date currentDate = calendar.getTime();
		
		if(assignedResourceBO.getTaStaffList()!=null){
			for(TAStaffBO assPersonBO : assignedResourceBO.getTaStaffList()){
				assignedPersonDO = roadOperationDAO.findAssignedPersonById(assPersonBO.getPersonId(), assignedResourceBO.getTeamBO().getTeamId(), Constants.PersonType.TA_STAFF);
				
				if(assignedPersonDO!=null){
					if(assPersonBO.getAttended() != null && assPersonBO.getAttended()==true){
						assignedPersonDO.setAttended("Y");
					}

					if(assPersonBO.getAttended() != null && assPersonBO.getAttended()==false){
						assignedPersonDO.setAttended("N");
					}
					if(StringUtils.trimToNull(assPersonBO.getComment())!=null){
						assignedPersonDO.setComment(assPersonBO.getComment().trim());
					}
					
					assignedPersonDO.getAuditEntry().setUpdateDTime(currentDate);
					assignedPersonDO.getAuditEntry().setUpdateUsername(roadOperationBO.getCurrentUsername());
					
					roadOperationDAO.update(assignedPersonDO);
				}
				else{
					throw new Exception();
				}
			}
		}
		
		if(assignedResourceBO.getJpList()!=null){
			for(JPBO assPersonBO : assignedResourceBO.getJpList()){
				assignedPersonDO = roadOperationDAO.findAssignedPersonById(assPersonBO.getPersonBO().getPersonId(), assignedResourceBO.getTeamBO().getTeamId(), Constants.PersonType.JP);
			
				if(assignedPersonDO!=null){
					if(assPersonBO.getAttended() != null && assPersonBO.getAttended()==true){
						assignedPersonDO.setAttended("Y");
					}
					if(assPersonBO.getAttended() != null && assPersonBO.getAttended()==false){
						assignedPersonDO.setAttended("N");
					}
					if(StringUtils.trimToNull(assPersonBO.getComment())!=null){
						assignedPersonDO.setComment(assPersonBO.getComment().trim());
					}
					
					assignedPersonDO.getAuditEntry().setUpdateDTime(currentDate);
					assignedPersonDO.getAuditEntry().setUpdateUsername(roadOperationBO.getCurrentUsername());
					
					roadOperationDAO.update(assignedPersonDO);
				}
				else{
					throw new Exception();
				}
			}
		}
		
		if(assignedResourceBO.getPoliceOfficerList()!=null){
			for(PoliceOfficerBO assPersonBO : assignedResourceBO.getPoliceOfficerList()){
				assignedPersonDO = roadOperationDAO.findAssignedPersonById(assPersonBO.getPersonID(), assignedResourceBO.getTeamBO().getTeamId(), Constants.PersonType.POLICE_OFFCER);
			
				if(assignedPersonDO!=null){
					if(assPersonBO.getAttended() != null && assPersonBO.getAttended()==true){
						assignedPersonDO.setAttended("Y");
					}
					if(assPersonBO.getAttended() != null && assPersonBO.getAttended()==false){
						assignedPersonDO.setAttended("N");
					}
					if(StringUtils.trimToNull(assPersonBO.getComment())!=null){
						assignedPersonDO.setComment(assPersonBO.getComment().trim());
					}
					
					assignedPersonDO.getAuditEntry().setUpdateDTime(currentDate);
					assignedPersonDO.getAuditEntry().setUpdateUsername(roadOperationBO.getCurrentUsername());
					
					roadOperationDAO.update(assignedPersonDO);
				}
				else{
					throw new Exception();
				}
				
			}
		}
		
		if(assignedResourceBO.getItaExaminerList()!=null){
			for(ITAExaminerBO assPersonBO : assignedResourceBO.getItaExaminerList()){
				assignedPersonDO = roadOperationDAO.findAssignedPersonById(assPersonBO.getPersonBO().getPersonId(), assignedResourceBO.getTeamBO().getTeamId(), Constants.PersonType.ITA_EXAMINER);
				
				if(assignedPersonDO!=null){
					if(assPersonBO.getAttended() != null && assPersonBO.getAttended()==true){
						assignedPersonDO.setAttended("Y");
					}
					if(assPersonBO.getAttended() != null && assPersonBO.getAttended()==false){
						assignedPersonDO.setAttended("N");
					}
					if(StringUtils.trimToNull(assPersonBO.getComment())!=null){
						assignedPersonDO.setComment(assPersonBO.getComment().trim());
					}
					
					assignedPersonDO.getAuditEntry().setUpdateDTime(currentDate);
					assignedPersonDO.getAuditEntry().setUpdateUsername(roadOperationBO.getCurrentUsername());
					
					roadOperationDAO.update(assignedPersonDO);
				}
				else{
					throw new Exception();
				}
			}
		}
	}
	
/**
 * This method updates the Attended flag and the comment field of the roms_assigned_vehicle table
 * @param taVehicleList
 * @param roadOperationBO
 * @throws Exception
 */
	private void updateAssignedVehicle(List<TAVehicleBO> taVehicleList, Integer teamId, RoadOperationBO roadOperationBO)throws Exception{
		AssignedVehicleDO assignedVehicleDO = new AssignedVehicleDO();
		RoadOperationDAO roadOperationDAO = daoFactory.getRoadOperationDAO();
		Calendar calendar = Calendar.getInstance();
		Date currentDate = calendar.getTime();
		
		for(TAVehicleBO assVehicleBO: taVehicleList){
			
			assignedVehicleDO = roadOperationDAO.findAssignedVehicleById(assVehicleBO.getTaVehicleId(), teamId);
			if(assignedVehicleDO!=null){
				if(assVehicleBO.getAttended() != null && assVehicleBO.getAttended()==true){
					assignedVehicleDO.setAttended("Y");
				}

				if(assVehicleBO.getAttended() != null && assVehicleBO.getAttended()==false){
					assignedVehicleDO.setAttended("N");
				}
				if(StringUtils.trimToNull(assVehicleBO.getComment())!=null){
					assignedVehicleDO.setComment(assVehicleBO.getComment().trim());
				}
				
				assignedVehicleDO.getAuditEntry().setUpdateDTime(currentDate);
				assignedVehicleDO.getAuditEntry().setUpdateUsername(roadOperationBO.getCurrentUsername());
				
				roadOperationDAO.update(assignedVehicleDO);
			}
			else{
				throw new Exception();
			}
		}
		
	}
	
	/**
	 * This method populates the Event Audit record to be saved  for Updating a Road Operation
	 * @param savedRoadOperationDO
	 * @param currentUsername
	 * @param eventCode
	 * @param currentStatus
	 * @return
	 * @throws Exception
	 */
	private EventAuditDO createEventAuditForUpdatingRecord(RoadOperationDO savedRoadOperationDO, String currentUsername, Integer eventCode, String currentStatus)throws Exception{
		try{
		EventAuditDO eventAuditBO = new EventAuditDO();
		AuditEntry auditEntry = new AuditEntry();   
		StringBuffer comment = new StringBuffer();
		
		
		if(eventCode.equals(Constants.EventCode.AUTHORIZE_OPERATION)){
			auditEntry.setCreateUsername(savedRoadOperationDO.getAuthorizedUserName());
		}else
		{
			auditEntry.setCreateUsername(currentUsername);
		}
		
		eventAuditBO.setEventCode(eventCode);	  
		
		
		
		eventAuditBO.setRefType1Code(Constants.EventRefTypeCode.OPERATION_NAME);
		eventAuditBO.setRefValue1(savedRoadOperationDO.getOperationName());
			
	
		eventAuditBO.setRefType2Code(Constants.EventRefTypeCode.OPERATION_CATEGORY);
		eventAuditBO.setRefValue2(savedRoadOperationDO.getCategory().getDescription());
		
		
		String scheduledFormatedStartDTime = DateUtils.formatDate("yyyy-MM-dd hh:mm a",  savedRoadOperationDO.getScheduledStartDtime());
		String scheduledFormatedEndDTime = DateUtils.formatDate("yyyy-MM-dd hh:mm a",  savedRoadOperationDO.getScheduledEndDtime());
		
		comment.append(StringUtil.appendSemiColon(comment.toString(),"Scheduled Date and Time: " + scheduledFormatedStartDTime + " - "  + scheduledFormatedEndDTime));
		
		if(eventCode.equals(Constants.EventCode.UPDATE_OPERATION)){
			comment.append(StringUtil.appendSemiColon(comment.toString(),"Current Status: " + currentStatus));
		}
		
		eventAuditBO.setComment(comment.toString());

		eventAuditBO.setAuditEntry(auditEntry);
		
		return eventAuditBO;
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new Exception();
		}
	}
	
	
	
	@Override
	public void updateOperationStatus(RoadOperationBO roadOpBo, String status) throws Exception {
		
		EventAuditDO eventAuditDO = null;
		RoadOperationDO roadOperationDO = null;
		
		Calendar calendar = Calendar.getInstance();
		Date currentDate = calendar.getTime();
		
		RoadOperationDAO roadOperationDAO = daoFactory.getRoadOperationDAO();
		
		roadOperationDO = findRoadOperationById(roadOpBo.getRoadOperationId());
		
		
		
		ReasonDO reasonDO = null;
		
		if(!status.equalsIgnoreCase(Constants.Status.ROAD_OPERATION_CLOSED))
		{
			reasonDO = roadOperationDAO.find(ReasonDO.class, roadOpBo.getReasonId());
			
			roadOperationDO.setComment(roadOpBo.getComment().trim());
		}
		
		roadOperationDO.setReason(reasonDO);
		
		Integer eventCode  = null;
		
		String statusCode = null;
		
		if(status.equalsIgnoreCase(Constants.Status.ROAD_OPERATION_TERMINATED))
		{
			eventCode = Constants.EventCode.TERMINATE_OPERATION;
			statusCode = Constants.Status.ROAD_OPERATION_TERMINATED;
			

			//kpowell- 2014-10-17:: set Actual Start and End when terminating an operation
			String formattedEndDate = DateUtils.formatDate("yyyy-MM-dd", roadOpBo.getActualEndDate());
			String formattedEndDateTime =  formattedEndDate + " " + roadOpBo.getActualEndTime();
			Date actualEndDtime = DateUtils.parse(formattedEndDateTime, "yyyy-MM-dd HH:mm:ss");
			
			roadOperationDO.setActualEndDtime(actualEndDtime);
			
			//roadOperationDO.setComment(roadOpBo.getComment().trim());
			
			//reasonDO = roadOperationDAO.find(ReasonDO.class, roadOpBo.getReasonId());
			/*if(reasonDO!=null){
				roadOperationDO.setReason(reasonDO);
			}
			else{
				throw new Exception();
			}*/
			
			//2014-10-15::validate actual duration with offence date
			validateActualStartDateAgainstOffenceDates(roadOperationDO.getRoadOperationId(),
					roadOperationDO.getActualStartDtime());
			validateActualEndDateAgainstOffenceDates(roadOperationDO.getRoadOperationId(),
					roadOperationDO.getActualEndDtime());
			
		}
		else if(status.equalsIgnoreCase(Constants.Status.ROAD_OPERATION_CANCELLED))
		{
			eventCode = Constants.EventCode.CANCEL_OPERATION;
			statusCode = Constants.Status.ROAD_OPERATION_CANCELLED;
		}
		else if(status.equalsIgnoreCase(Constants.Status.ROAD_OPERATION_CLOSED))
		{
			eventCode = Constants.EventCode.CLOSE_OPERATION;
			statusCode = Constants.Status.ROAD_OPERATION_CLOSED;
		}
		
		
		try {
			eventAuditDO = createEventAuditForCancelOrTerminateRecord(roadOperationDO, roadOpBo.getCurrentUsername() ,eventCode,reasonDO != null  ? reasonDO.getDescription() : null);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		StatusDO newStatusDO = roadOperationDAO.find(StatusDO.class,statusCode);
		if(newStatusDO!=null){
			roadOperationDO.setStatus(newStatusDO);
			roadOperationDO.setStatusDtime(currentDate);
		}
		
		roadOperationDO.getAuditEntry().setUpdateDTime(currentDate);
		roadOperationDO.getAuditEntry().setUpdateUsername(roadOpBo.getCurrentUsername());
		roadOperationDAO.update(roadOperationDO);
		
		serviceFactory.getEventAuditService().saveEventAuditDO(eventAuditDO);
		
	}
	
	/**
	 * This method populates the Event Audit record to be saved for Cancelling or Terminating a Road Operation
	 * @param savedRoadOperationDO
	 * @param currentUsername
	 * @param eventCode
	 * @param reasonDescription
	 * @return
	 * @throws Exception
	 */
	private EventAuditDO createEventAuditForCancelOrTerminateRecord(RoadOperationDO savedRoadOperationDO, String currentUsername, Integer eventCode, String reasonDescription)throws Exception{
		try{
		EventAuditDO eventAuditBO = new EventAuditDO();
		AuditEntry auditEntry = new AuditEntry();   
		
		StringBuffer comment = new StringBuffer();
		
		auditEntry.setCreateUsername(currentUsername);
		
		eventAuditBO.setEventCode(eventCode);	  
		
		String actualFormatedStartDTime = "";
		String actualFormatedEndDTime = "";
		String scheduledFormatedStartDTime = "";
		String scheduledFormatedEndDTime = "";
		
		if(savedRoadOperationDO.getActualStartDtime() != null)
			actualFormatedStartDTime = DateUtils.formatDate("yyyy-MM-dd hh:mm a",  savedRoadOperationDO.getActualStartDtime());
		
		if(savedRoadOperationDO.getActualEndDtime() != null)
			actualFormatedEndDTime = DateUtils.formatDate("yyyy-MM-dd hh:mm a",  savedRoadOperationDO.getActualEndDtime());
		
		
		eventAuditBO.setRefType1Code(Constants.EventRefTypeCode.OPERATION_NAME);
		eventAuditBO.setRefValue1(savedRoadOperationDO.getOperationName());
		
		eventAuditBO.setRefType2Code(Constants.EventRefTypeCode.OPERATION_CATEGORY);
		eventAuditBO.setRefValue2(savedRoadOperationDO.getCategory().getDescription());
			
		if(reasonDescription != null)
		{
			comment.append(StringUtil.appendSemiColon(comment.toString(),"Reason: " + reasonDescription));
			
		}
		
		
		if(savedRoadOperationDO.getComment() != null)
		{
			comment.append(StringUtil.appendSemiColon(comment.toString(),"Comment: " + savedRoadOperationDO.getComment()));
			
		}
		
		if(eventCode.equals(Constants.EventCode.CLOSE_OPERATION) 
		|| eventCode.equals(Constants.EventCode.COMPLETE_OPERATION))
		{
			comment.append(StringUtil.appendSemiColon(comment.toString(),"Actual Date and Time: " + actualFormatedStartDTime + 
                " - "  + actualFormatedEndDTime));
		}
		
				
		eventAuditBO.setComment(comment.toString());
	
		eventAuditBO.setAuditEntry(auditEntry);
		
		return eventAuditBO;	
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new Exception();
		}
	}
	
	private void saveTeamAndDetails(AssignedTeamDetailsBO teamDetails, RoadOperationDO saveRoadOperationDO, AuditEntry auditEntry) throws Exception{
		TeamDO savedTeamDO;
		Integer teamId = saveTeam(teamDetails, saveRoadOperationDO, auditEntry);
		savedTeamDO = new TeamDO();
		savedTeamDO = daoFactory.getRoadOperationDAO().find(TeamDO.class, teamId);
		
		if(teamDetails.getTaStaffList()!=null){
			saveAssignedPerson(returnIntegerAssignedStaffPersonList(teamDetails.getTaStaffList()), Constants.PersonType.TA_STAFF, savedTeamDO, auditEntry);
		}
		if(teamDetails.getJpList()!=null){
			saveAssignedPerson(returnIntegerAssignedJPPersonList(teamDetails.getJpList()), Constants.PersonType.JP, savedTeamDO, auditEntry);
		}
		if(teamDetails.getPoliceOfficerList()!=null){
			saveAssignedPerson(returnIntegerAssignedPoliceOfficerPersonList(teamDetails.getPoliceOfficerList()), Constants.PersonType.POLICE_OFFCER, savedTeamDO, auditEntry);
		}
		if(teamDetails.getItaExaminerList()!=null){
			saveAssignedPerson(returnIntegerAssignedITAExaminerPersonList(teamDetails.getItaExaminerList()), Constants.PersonType.ITA_EXAMINER, savedTeamDO, auditEntry);
		}
		if(teamDetails.getTaVehicleList()!=null){
			saveAssignedVehicle(returnIntegerAssignedTAVehicleList(teamDetails.getTaVehicleList()), savedTeamDO, auditEntry);
		}
		
		//Set Assigned Location and Artery Details and Save
		if(teamDetails.getOperationLocationList()!=null){
			saveAssignedLocation(returnIntegerLocationList(teamDetails.getOperationLocationList()), savedTeamDO, auditEntry);
		}
		if(teamDetails.getOperationArteryList()!=null){
			saveAssignedArtery(returnIntegerArteryList(teamDetails.getOperationArteryList()), savedTeamDO, auditEntry);
		}
		
		//Set Assigned Parish Details and Save
		if(teamDetails.getOperationParishList() !=null){
			saveAssignedParish(returnStringParishList(teamDetails.getOperationParishList()), savedTeamDO, auditEntry);
		}
		
	}
	
	private void deleteTeamAndDetails(TeamDO teamDO) throws Exception{
		RoadOperationDAO roadOperationDAO = daoFactory.getRoadOperationDAO(); 
		try{
			List<Integer> assignedStaffIntegerList = new ArrayList<Integer>();
			List<Integer> assignedJPIntegerList = new ArrayList<Integer>();
			List<Integer> assignedPoliceOfficerIntegerList = new ArrayList<Integer>();
			List<Integer> assignedITAExaminerIntegerList = new ArrayList<Integer>();
			List<Integer> assignedTAVehicleIntegerList = new ArrayList<Integer>();
			
			List<TAStaffBO> taStaffBOList = roadOperationDAO.findAssignedStaffByTeam(teamDO.getRoadOperation().getRoadOperationId(), teamDO.getTeamId());
			List<JPBO> jpBOList = roadOperationDAO.findAssignedJPByTeam(teamDO.getRoadOperation().getRoadOperationId(), teamDO.getTeamId());
			List<PoliceOfficerBO> policeOfficerBOList = roadOperationDAO.findAssignedPoliceOfficerByTeam(teamDO.getRoadOperation().getRoadOperationId(), teamDO.getTeamId());
			List<ITAExaminerBO> itaExaminerBOList = roadOperationDAO.findAssignedITAExaminerByTeam(teamDO.getRoadOperation().getRoadOperationId(), teamDO.getTeamId());
			List<TAVehicleBO> taVehicleBOList = roadOperationDAO.findAssignedTAVehicleByTeam(teamDO.getRoadOperation().getRoadOperationId(), teamDO.getTeamId());
			
			
			
			if(taStaffBOList!=null){
				assignedStaffIntegerList = returnIntegerAssignedStaffPersonList(taStaffBOList);
				deleteAssignedPerson(assignedStaffIntegerList, teamDO.getTeamId(), Constants.PersonType.TA_STAFF);
				
			}
			
			if(jpBOList!=null){
				assignedJPIntegerList = returnIntegerAssignedJPPersonList(jpBOList);
				deleteAssignedPerson(assignedJPIntegerList, teamDO.getTeamId(), Constants.PersonType.JP);
				
			}
			
			if(policeOfficerBOList!=null){
				assignedPoliceOfficerIntegerList = returnIntegerAssignedPoliceOfficerPersonList(policeOfficerBOList);
				deleteAssignedPerson(assignedPoliceOfficerIntegerList, teamDO.getTeamId(), Constants.PersonType.POLICE_OFFCER);
				
			}
			
			if(itaExaminerBOList!=null){
				assignedITAExaminerIntegerList = returnIntegerAssignedITAExaminerPersonList(itaExaminerBOList);
				deleteAssignedPerson(assignedITAExaminerIntegerList, teamDO.getTeamId(), Constants.PersonType.ITA_EXAMINER);
			}
			
			if(taVehicleBOList!=null){
				assignedTAVehicleIntegerList = returnIntegerAssignedTAVehicleList(taVehicleBOList);
				deleteAssignedVehicle(assignedTAVehicleIntegerList, teamDO.getTeamId());
			}
			
			
			List<ArteryBO> operationArteryList = daoFactory.getRoadOperationDAO().findArteryByTeam(teamDO.getRoadOperation().getRoadOperationId(), teamDO.getTeamId());
			List<LocationBO> operationLocationList = daoFactory.getRoadOperationDAO().findLocationByTeam(teamDO.getRoadOperation().getRoadOperationId(), teamDO.getTeamId());
			List<ParishBO> operationParishList = daoFactory.getRoadOperationDAO().findParishByTeam(teamDO.getRoadOperation().getRoadOperationId(), teamDO.getTeamId());
			
			
			List<Integer> assignedArteryIntegerList = new ArrayList<Integer>();
			List<Integer> assignedLocationIntegerList = new ArrayList<Integer>();
			List<String> assignedParishStringList = new ArrayList<String>();
			
			if(operationArteryList!=null){
				assignedArteryIntegerList = returnIntegerAssignedArteryList(operationArteryList);
				deleteAssignedArtery(assignedArteryIntegerList, teamDO.getTeamId());
			}
			
			if(operationLocationList!=null){
				assignedLocationIntegerList = returnIntegerAssignedLocationList(operationLocationList);
				deleteAssignedLocation(assignedLocationIntegerList, teamDO.getTeamId());
			}
			
			
			if(operationParishList!=null){
				assignedParishStringList = returnStringAssignedParishList(operationParishList);
				deleteAssignedParish(assignedParishStringList, teamDO.getTeamId());
			}
			
			//delete team
			roadOperationDAO.delete(teamDO);
		}
		catch (Exception e) {
			throw new Exception();
		}
	}
	
	
	/**
	 * This method deletes a list of assigned parishes
	 * @param assignedPersonList
	 * @param roadOperationId
	 * @param personTypeId
	 * @throws Exception
	 */
	private void deleteAssignedParish(List<String> assignedParishStringList,Integer teamId) throws Exception{
		RoadOperationDAO roadOperationDAO = daoFactory.getRoadOperationDAO(); 
	
		TeamParishDO assTeamParishDO = new TeamParishDO();
			
		for(String parishCode : assignedParishStringList){
			assTeamParishDO = roadOperationDAO.findAssignedParishById(parishCode, teamId);
			if(assTeamParishDO!=null){
				roadOperationDAO.delete(assTeamParishDO);
			}
			else{
				throw new Exception();
			}
		}
	}
	
	
	/**
	 * This method return an Integer List of location Id for Assigned Vehicle(s)
	 * @param assignedVehicleList
	 * @return
	 */
	private List<String> returnStringAssignedParishList(List<ParishBO> operationParishList){
		List<String> strList = new ArrayList<String>();
		
		for (ParishBO parishBO : operationParishList) {
			
			strList.add(parishBO.getParishCode());
		}
		
		return strList;
	}
	
	
	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW,rollbackFor={Exception.class,RuntimeException.class})
	public boolean updateRoadOperationTeam(RoadOperationBO roadOperationBO,
			AssignedTeamDetailsBO assignedTeamDetailsBO) {
		
		RoadOperationDAO roadOperationDAO = daoFactory.getRoadOperationDAO(); 
		List<Integer> addTAStaffIntegerList = null;
		List<Integer> addJPIntegerList = null;
		List<Integer> addPoliceOfficerIntegerList = null;
		List<Integer> addITAExaminerIntegerList = null;
		List<Integer> addTAVehicleIntegerList = null;
				
		List<Integer> deleteTAStaffIntegerList = null;
		List<Integer> deleteJPIntegerList = null;
		List<Integer> deletePoliceOfficerIntegerList = null;
		List<Integer> deleteITAExaminerIntegerList = null;
		List<Integer> deleteTAVehicleIntegerList = null;
		
		Calendar calendar = Calendar.getInstance();
		Date currentDate = calendar.getTime();
		AuditEntry auditEntry = new AuditEntry();
		
		try{
			RoadOperationDO saveRoadOperationDO = roadOperationDAO.find(RoadOperationDO.class, roadOperationBO.getRoadOperationId());
				
			auditEntry.setCreateDTime(currentDate);
			auditEntry.setCreateUsername(roadOperationBO.getCurrentUsername());
			
			TeamDO savedTeamDO;
			//Current Status of Road Operation Scheduling
			List<Integer> assignedStaffIntegerList;
			List<Integer> assignedJPIntegerList;
			List<Integer> assignedPoliceOfficerIntegerList;
			List<Integer> assignedITAExaminerIntegerList;
			List<Integer> assignedTAVehicleIntegerList;
			
			
			savedTeamDO = null;
			
			if(assignedTeamDetailsBO.getTeamBO().getTeamId()!=null){
				savedTeamDO = roadOperationDAO.find(TeamDO.class, assignedTeamDetailsBO.getTeamBO().getTeamId());
			}
			
			
			if(savedTeamDO!=null){
				if(assignedTeamDetailsBO.getTeamBO().isDelete()){
					deleteTeamAndDetails(savedTeamDO);
				}
				else{
					
					assignedStaffIntegerList = new ArrayList<Integer>();
					assignedJPIntegerList = new ArrayList<Integer>();
					assignedPoliceOfficerIntegerList = new ArrayList<Integer>();
					assignedITAExaminerIntegerList = new ArrayList<Integer>();
					assignedTAVehicleIntegerList = new ArrayList<Integer>();
					
					addTAStaffIntegerList = new ArrayList<Integer>();
					addJPIntegerList = new ArrayList<Integer>();
					addPoliceOfficerIntegerList = new ArrayList<Integer>();
					addITAExaminerIntegerList = new ArrayList<Integer>();
					addTAVehicleIntegerList = new ArrayList<Integer>();
							
					deleteTAStaffIntegerList = new ArrayList<Integer>();
					deleteJPIntegerList = new ArrayList<Integer>();
					deletePoliceOfficerIntegerList = new ArrayList<Integer>();
					deleteITAExaminerIntegerList = new ArrayList<Integer>();
					deleteTAVehicleIntegerList = new ArrayList<Integer>();
					
					//Add or Delete Assigned Persons
					if(assignedTeamDetailsBO.getTaStaffList()!=null){
						assignedStaffIntegerList = returnIntegerAssignedStaffPersonList(assignedTeamDetailsBO.getTaStaffList());
					}
					addTAStaffIntegerList = returnAssignedPersonToAdd(assignedStaffIntegerList, assignedTeamDetailsBO.getTeamBO().getTeamId(), Constants.PersonType.TA_STAFF);
					if(addTAStaffIntegerList!=null && addTAStaffIntegerList.size()>0){
						saveAssignedPerson(addTAStaffIntegerList, Constants.PersonType.TA_STAFF, savedTeamDO, auditEntry);
					}
					
					if(assignedTeamDetailsBO.getJpList()!=null){
						assignedJPIntegerList= returnIntegerAssignedJPPersonList(assignedTeamDetailsBO.getJpList());
					}
					addJPIntegerList = returnAssignedPersonToAdd(assignedJPIntegerList, assignedTeamDetailsBO.getTeamBO().getTeamId(), Constants.PersonType.JP);
					if(addJPIntegerList!=null && addJPIntegerList.size()>0){
						saveAssignedPerson(addJPIntegerList, Constants.PersonType.JP, savedTeamDO, auditEntry);
						
					}
					
					if(assignedTeamDetailsBO.getPoliceOfficerList()!=null){
						assignedPoliceOfficerIntegerList= returnIntegerAssignedPoliceOfficerPersonList(assignedTeamDetailsBO.getPoliceOfficerList());
					}
					addPoliceOfficerIntegerList = returnAssignedPersonToAdd(assignedPoliceOfficerIntegerList,assignedTeamDetailsBO.getTeamBO().getTeamId(), Constants.PersonType.POLICE_OFFCER);
					if(addPoliceOfficerIntegerList!=null && addPoliceOfficerIntegerList.size()>0){
						saveAssignedPerson(addPoliceOfficerIntegerList, Constants.PersonType.POLICE_OFFCER, savedTeamDO, auditEntry);
					}
					
					if(assignedTeamDetailsBO.getItaExaminerList()!=null){
						assignedITAExaminerIntegerList= returnIntegerAssignedITAExaminerPersonList(assignedTeamDetailsBO.getItaExaminerList());
					}
					addITAExaminerIntegerList = returnAssignedPersonToAdd(assignedITAExaminerIntegerList, assignedTeamDetailsBO.getTeamBO().getTeamId(), Constants.PersonType.ITA_EXAMINER);
					if(addITAExaminerIntegerList!=null && addITAExaminerIntegerList.size()>0){
						saveAssignedPerson(addITAExaminerIntegerList, Constants.PersonType.ITA_EXAMINER, savedTeamDO, auditEntry);
					}
					
					//Delete
					deleteTAStaffIntegerList = returnAssignedPersonToDelete(assignedStaffIntegerList, assignedTeamDetailsBO.getTeamBO().getTeamId(), Constants.PersonType.TA_STAFF);
					if(deleteTAStaffIntegerList!=null && deleteTAStaffIntegerList.size()>0){
						deleteAssignedPerson(deleteTAStaffIntegerList, assignedTeamDetailsBO.getTeamBO().getTeamId(), Constants.PersonType.TA_STAFF);
					}
					
					deleteJPIntegerList = returnAssignedPersonToDelete(assignedJPIntegerList, assignedTeamDetailsBO.getTeamBO().getTeamId(), Constants.PersonType.JP);
					if(deleteJPIntegerList!=null && deleteJPIntegerList.size()>0){
						deleteAssignedPerson(deleteJPIntegerList, assignedTeamDetailsBO.getTeamBO().getTeamId(), Constants.PersonType.JP);
					}
					
					deletePoliceOfficerIntegerList = returnAssignedPersonToDelete(assignedPoliceOfficerIntegerList, assignedTeamDetailsBO.getTeamBO().getTeamId(), Constants.PersonType.POLICE_OFFCER);
					if(deletePoliceOfficerIntegerList!=null && deletePoliceOfficerIntegerList.size()>0){
						deleteAssignedPerson(deletePoliceOfficerIntegerList, assignedTeamDetailsBO.getTeamBO().getTeamId(), Constants.PersonType.POLICE_OFFCER);
					}
					
					deleteITAExaminerIntegerList = returnAssignedPersonToDelete(assignedITAExaminerIntegerList, assignedTeamDetailsBO.getTeamBO().getTeamId(), Constants.PersonType.ITA_EXAMINER);
					if(deleteITAExaminerIntegerList!=null && deleteITAExaminerIntegerList.size()>0){
						deleteAssignedPerson(deleteITAExaminerIntegerList, assignedTeamDetailsBO.getTeamBO().getTeamId(), Constants.PersonType.ITA_EXAMINER);
					}
					
					//Add or Delete Assigned Vehicle
					if(assignedTeamDetailsBO.getTaVehicleList()!=null){
						assignedTAVehicleIntegerList= returnIntegerAssignedTAVehicleList(assignedTeamDetailsBO.getTaVehicleList());
					}
					addTAVehicleIntegerList = returnAssignedVehicleToAdd(assignedTAVehicleIntegerList, assignedTeamDetailsBO.getTeamBO().getTeamId());
					if(addTAVehicleIntegerList!=null && addTAVehicleIntegerList.size()>0){
						saveAssignedVehicle(addTAVehicleIntegerList,  savedTeamDO, auditEntry);
					}
					
					//Delete
					deleteTAVehicleIntegerList = returnAssignedVehicleToDelete(assignedTAVehicleIntegerList, assignedTeamDetailsBO.getTeamBO().getTeamId());
					if(deleteTAVehicleIntegerList!=null && deleteTAVehicleIntegerList.size()>0){
						deleteAssignedVehicle(deleteTAVehicleIntegerList, assignedTeamDetailsBO.getTeamBO().getTeamId());
					}
					
					
					if(!StringUtils.equals(savedTeamDO.getTeamName(), assignedTeamDetailsBO.getTeamBO().getTeamName().trim())){
						savedTeamDO.setNoMotorCycleAssigned(assignedTeamDetailsBO.getTeamBO().getNoMotorCycleAssigned());
						//savedTeamDO.setNoMotorCycleUsed(assignedTeamDetailsBO.getTeamBO().getNoMotorCycleUsed());
						savedTeamDO.setTeamName(assignedTeamDetailsBO.getTeamBO().getTeamName().trim());
						savedTeamDO.getAuditEntry().setUpdateDTime(currentDate);
						savedTeamDO.getAuditEntry().setUpdateUsername(roadOperationBO.getCurrentUsername());
						roadOperationDAO.update(savedTeamDO);
					}
				}
			}
			else{
				saveTeamAndDetails(assignedTeamDetailsBO, saveRoadOperationDO, auditEntry);
			}
			
			return true;
		
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW,rollbackFor={Exception.class,RuntimeException.class})
	public boolean updateRoadOperation(RoadOperationBO roadOperationBO,
			List<AssignedTeamDetailsBO> assignedTeamDetailsBOList) throws Exception {

		RoadOperationDAO roadOperationDAO = daoFactory.getRoadOperationDAO(); 
		List<Integer> addTAStaffIntegerList = null;
		List<Integer> addJPIntegerList = null;
		List<Integer> addPoliceOfficerIntegerList = null;
		List<Integer> addITAExaminerIntegerList = null;
		List<Integer> addTAVehicleIntegerList = null;
				
		List<Integer> deleteTAStaffIntegerList = null;
		List<Integer> deleteJPIntegerList = null;
		List<Integer> deletePoliceOfficerIntegerList = null;
		List<Integer> deleteITAExaminerIntegerList = null;
		List<Integer> deleteTAVehicleIntegerList = null;
		
		Calendar calendar = Calendar.getInstance();
		Date currentDate = calendar.getTime();
		EventAuditDO eventAuditDO = new EventAuditDO();
		AuditEntry auditEntry = new AuditEntry();
		
		try{
		RoadOperationDO saveRoadOperationDO = roadOperationDAO.find(RoadOperationDO.class, roadOperationBO.getRoadOperationId());
			
		auditEntry.setCreateDTime(currentDate);
		auditEntry.setCreateUsername(roadOperationBO.getCurrentUsername());
		
		//System.err.println("set Update User and datetime"+ roadOperationBO.getCurrentUsername());
		saveRoadOperationDO.getAuditEntry().setUpdateDTime(currentDate);
		saveRoadOperationDO.getAuditEntry().setUpdateUsername(roadOperationBO.getCurrentUsername());
		
		TeamDO savedTeamDO;
		//Current Status of Road Operation Scheduling
		List<Integer> assignedStaffIntegerList;
		List<Integer> assignedJPIntegerList;
		List<Integer> assignedPoliceOfficerIntegerList;
		List<Integer> assignedITAExaminerIntegerList;
		List<Integer> assignedTAVehicleIntegerList;
		if(saveRoadOperationDO.getStatus().getStatusId().equalsIgnoreCase(Constants.Status.ROAD_OPERATION_SCHEDULING) || saveRoadOperationDO.getStatus().getStatusId().equalsIgnoreCase(Constants.Status.ROAD_OPERATION_NO_ACTION))
		{//&& StringUtils.trimToNull(roadOperationBO.getNewStatusId())==null){
			for(AssignedTeamDetailsBO teamDetails : assignedTeamDetailsBOList){
				savedTeamDO = new TeamDO();
				
				if(teamDetails.getTeamBO().getTeamId()!=null)
				{
					savedTeamDO = roadOperationDAO.find(TeamDO.class, teamDetails.getTeamBO().getTeamId());
				}
								
				if(savedTeamDO.getTeamId()!=null){//updating an existing team 
					if(teamDetails.getTeamBO().isDelete())
					{
						deleteTeamAndDetails(savedTeamDO);
					}
					else
					{
						
						assignedStaffIntegerList = new ArrayList<Integer>();
						assignedJPIntegerList = new ArrayList<Integer>();
						assignedPoliceOfficerIntegerList = new ArrayList<Integer>();
						assignedITAExaminerIntegerList = new ArrayList<Integer>();
						assignedTAVehicleIntegerList = new ArrayList<Integer>();
						
						addTAStaffIntegerList = new ArrayList<Integer>();
						addJPIntegerList = new ArrayList<Integer>();
						addPoliceOfficerIntegerList = new ArrayList<Integer>();
						addITAExaminerIntegerList = new ArrayList<Integer>();
						addTAVehicleIntegerList = new ArrayList<Integer>();
								
						deleteTAStaffIntegerList = new ArrayList<Integer>();
						deleteJPIntegerList = new ArrayList<Integer>();
						deletePoliceOfficerIntegerList = new ArrayList<Integer>();
						deleteITAExaminerIntegerList = new ArrayList<Integer>();
						deleteTAVehicleIntegerList = new ArrayList<Integer>();
						
						//Add or Delete Assigned Persons
						if(teamDetails.getTaStaffList()!=null){
							assignedStaffIntegerList = returnIntegerAssignedStaffPersonList(teamDetails.getTaStaffList());
						}
						addTAStaffIntegerList = returnAssignedPersonToAdd(assignedStaffIntegerList, teamDetails.getTeamBO().getTeamId(), Constants.PersonType.TA_STAFF);
						if(addTAStaffIntegerList!=null && addTAStaffIntegerList.size()>0){
							saveAssignedPerson(addTAStaffIntegerList, Constants.PersonType.TA_STAFF, savedTeamDO, auditEntry);
						}
						
						if(teamDetails.getJpList()!=null){
							assignedJPIntegerList= returnIntegerAssignedJPPersonList(teamDetails.getJpList());
						}
						addJPIntegerList = returnAssignedPersonToAdd(assignedJPIntegerList, teamDetails.getTeamBO().getTeamId(), Constants.PersonType.JP);
						if(addJPIntegerList!=null && addJPIntegerList.size()>0){
							saveAssignedPerson(addJPIntegerList, Constants.PersonType.JP, savedTeamDO, auditEntry);
							
						}
						
						if(teamDetails.getPoliceOfficerList()!=null){
							assignedPoliceOfficerIntegerList= returnIntegerAssignedPoliceOfficerPersonList(teamDetails.getPoliceOfficerList());
						}
						addPoliceOfficerIntegerList = returnAssignedPersonToAdd(assignedPoliceOfficerIntegerList,teamDetails.getTeamBO().getTeamId(), Constants.PersonType.POLICE_OFFCER);
						if(addPoliceOfficerIntegerList!=null && addPoliceOfficerIntegerList.size()>0){
							saveAssignedPerson(addPoliceOfficerIntegerList, Constants.PersonType.POLICE_OFFCER, savedTeamDO, auditEntry);
						}
						
						if(teamDetails.getItaExaminerList()!=null){
							assignedITAExaminerIntegerList= returnIntegerAssignedITAExaminerPersonList(teamDetails.getItaExaminerList());
						}
						addITAExaminerIntegerList = returnAssignedPersonToAdd(assignedITAExaminerIntegerList, teamDetails.getTeamBO().getTeamId(), Constants.PersonType.ITA_EXAMINER);
						if(addITAExaminerIntegerList!=null && addITAExaminerIntegerList.size()>0){
							saveAssignedPerson(addITAExaminerIntegerList, Constants.PersonType.ITA_EXAMINER, savedTeamDO, auditEntry);
						}
						
						//Delete
						deleteTAStaffIntegerList = returnAssignedPersonToDelete(assignedStaffIntegerList, teamDetails.getTeamBO().getTeamId(), Constants.PersonType.TA_STAFF);
						if(deleteTAStaffIntegerList!=null && deleteTAStaffIntegerList.size()>0){
							deleteAssignedPerson(deleteTAStaffIntegerList, teamDetails.getTeamBO().getTeamId(), Constants.PersonType.TA_STAFF);
						}
						
						deleteJPIntegerList = returnAssignedPersonToDelete(assignedJPIntegerList, teamDetails.getTeamBO().getTeamId(), Constants.PersonType.JP);
						if(deleteJPIntegerList!=null && deleteJPIntegerList.size()>0){
							deleteAssignedPerson(deleteJPIntegerList, teamDetails.getTeamBO().getTeamId(), Constants.PersonType.JP);
						}
						
						deletePoliceOfficerIntegerList = returnAssignedPersonToDelete(assignedPoliceOfficerIntegerList, teamDetails.getTeamBO().getTeamId(), Constants.PersonType.POLICE_OFFCER);
						if(deletePoliceOfficerIntegerList!=null && deletePoliceOfficerIntegerList.size()>0){
							deleteAssignedPerson(deletePoliceOfficerIntegerList, teamDetails.getTeamBO().getTeamId(), Constants.PersonType.POLICE_OFFCER);
						}
						
						deleteITAExaminerIntegerList = returnAssignedPersonToDelete(assignedITAExaminerIntegerList, teamDetails.getTeamBO().getTeamId(), Constants.PersonType.ITA_EXAMINER);
						if(deleteITAExaminerIntegerList!=null && deleteITAExaminerIntegerList.size()>0){
							deleteAssignedPerson(deleteITAExaminerIntegerList, teamDetails.getTeamBO().getTeamId(), Constants.PersonType.ITA_EXAMINER);
						}
						
						//Add or Delete Assigned Vehicle
						if(teamDetails.getTaVehicleList()!=null){
							assignedTAVehicleIntegerList= returnIntegerAssignedTAVehicleList(teamDetails.getTaVehicleList());
						}
						addTAVehicleIntegerList = returnAssignedVehicleToAdd(assignedTAVehicleIntegerList, teamDetails.getTeamBO().getTeamId());
						if(addTAVehicleIntegerList!=null && addTAVehicleIntegerList.size()>0){
							saveAssignedVehicle(addTAVehicleIntegerList,  savedTeamDO, auditEntry);
						}
						
						//Delete
						deleteTAVehicleIntegerList = returnAssignedVehicleToDelete(assignedTAVehicleIntegerList, teamDetails.getTeamBO().getTeamId());
						if(deleteTAVehicleIntegerList!=null && deleteTAVehicleIntegerList.size()>0){
							deleteAssignedVehicle(deleteTAVehicleIntegerList, teamDetails.getTeamBO().getTeamId());
						}
						
						//check if the team lead has changed
						if(!teamDetails.getTeamBO().getTeamLead().getStaffId().equalsIgnoreCase(savedTeamDO.getTeamLead().getStaffId()))
						{
							savedTeamDO.setTeamLead(roadOperationDAO.find(TAStaffDO.class, teamDetails.getTeamBO().getTeamLead().getStaffId()));
						}
						
						//TODO update assigned parish, location, arteries
						updateAssignedParish(teamDetails.getOperationParishList(),savedTeamDO,auditEntry);
						
						updateAssignedLocation(teamDetails.getOperationLocationList(),savedTeamDO,auditEntry);
						
						updateAssignedArtery(teamDetails.getOperationArteryList(),savedTeamDO,auditEntry);
						
						
						
						//Update Team
						savedTeamDO.setNoMotorCycleAssigned(teamDetails.getTeamBO().getNoMotorCycleAssigned());
						savedTeamDO.setTeamName(teamDetails.getTeamBO().getTeamName().trim());
						savedTeamDO.getAuditEntry().setUpdateDTime(currentDate);
						savedTeamDO.getAuditEntry().setUpdateUsername(roadOperationBO.getCurrentUsername());
						roadOperationDAO.update(savedTeamDO);
						
						
					}
				}
				else{//creating a new team
					saveTeamAndDetails(teamDetails, saveRoadOperationDO, auditEntry);
				}
			}
			
			
			//ROMS1.0-181(kpowell) : relocated here since the  savedOperationDO.scheduled dates where being updated to the new changes in another section
			Date scheduledStartDtime = null;
			Date scheduledEndDtime = null;
			
			String formattedStartDate = DateUtils.formatDate("yyyy-MM-dd", roadOperationBO.getScheduledStartDate());
			String formattedStartDateTime =  formattedStartDate + " " + roadOperationBO.getScheduledStartTime();
		
			scheduledStartDtime = DateUtils.parse(formattedStartDateTime, "yyyy-MM-dd HH:mm:ss");
			
			String formattedEndDate = DateUtils.formatDate("yyyy-MM-dd", roadOperationBO.getScheduledEndDate());
			String formattedEndDateTime =  formattedEndDate + " " + roadOperationBO.getScheduledEndTime();
			scheduledEndDtime = DateUtils.parse(formattedEndDateTime, "yyyy-MM-dd HH:mm:ss");
			
			
			//Set backdated to no if it was backdated and is now being updated to a future date
			/*if(saveRoadOperationDO.getBackDated().equalsIgnoreCase("Y") && !(DateUtils.before(scheduledStartDtime, currentDate) && DateUtils.before(scheduledEndDtime, currentDate))){
				saveRoadOperationDO.setBackDated("N");
				saveRoadOperationDO.setAuthorized("Y");
				saveRoadOperationDO.setAuthorizedDtime(currentDate);
				saveRoadOperationDO.setAuthorizedUserName(roadOperationBO.getCurrentUsername().toUpperCase());
			
			}*/
			
			//ONLY update authorization when updated state is scheduling
			if(roadOperationBO.getStatusId().equalsIgnoreCase(Constants.Status.ROAD_OPERATION_SCHEDULING)){
				//System.err.println("saveRoadOperationDO.getAuthorized()"+saveRoadOperationDO.getAuthorized());
				//if(saveRoadOperationDO.getAuthorized().equalsIgnoreCase("N")){//only if the operation has not been authorized
				
					/*System.err.println("scheduledEndDtime"+ scheduledEndDtime);
					System.err.println("scheduledStartDtime"+ scheduledStartDtime);
					System.err.println("saveRoadOperationDO.getScheduledEndDtime()"+ saveRoadOperationDO.getScheduledEndDtime());
					System.err.println("saveRoadOperationDO.getScheduledStartDtime()"+ saveRoadOperationDO.getScheduledStartDtime());*/
					
				if(scheduledStartDtime.compareTo(saveRoadOperationDO.getScheduledStartDtime())!=0 || scheduledEndDtime.compareTo(saveRoadOperationDO.getScheduledEndDtime())!=0 ){
					if(DateUtils.before(scheduledStartDtime, currentDate) || DateUtils.before(scheduledEndDtime, currentDate) && saveRoadOperationDO.getBackDated().equalsIgnoreCase("N") ){
						saveRoadOperationDO.setBackDated("Y");
						saveRoadOperationDO.setAuthorized("N");
						//System.err.println("set backdated Yes");
					}
					else{
						saveRoadOperationDO.setBackDated("N");
						saveRoadOperationDO.setAuthorized("Y");
						saveRoadOperationDO.setAuthorizedDtime(currentDate);
						saveRoadOperationDO.setAuthorizedUserName(roadOperationBO.getCurrentUsername().toUpperCase());
						//System.err.println("set backdated No");
					}
				}else{
					//System.err.println("Schedule dates have not changed");
				}
			}
			
			
			//
					
			
			if(! saveRoadOperationDO.getStatus().getStatusId().equalsIgnoreCase(Constants.Status.ROAD_OPERATION_NO_ACTION)){
				//Update Scheduled Start and End Date Time
				
				/*TODO- delete block which addresses ROMS1.0-181: relocated since it will used by other areas
				 * String formattedStartDate = DateUtils.formatDate("yyyy-MM-dd", roadOperationBO.getScheduledStartDate());
				String formattedStartDateTime =  formattedStartDate + " " + roadOperationBO.getScheduledStartTime();
			
				scheduledStartDtime = DateUtils.parse(formattedStartDateTime, "yyyy-MM-dd HH:mm:ss");
				
				String formattedEndDate = DateUtils.formatDate("yyyy-MM-dd", roadOperationBO.getScheduledEndDate());
				String formattedEndDateTime =  formattedEndDate + " " + roadOperationBO.getScheduledEndTime();
				scheduledEndDtime = DateUtils.parse(formattedEndDateTime, "yyyy-MM-dd HH:mm:ss");*/
				
				saveRoadOperationDO.setScheduledStartDtime(scheduledStartDtime);
				saveRoadOperationDO.setScheduledEndDtime(scheduledEndDtime);
				
				saveRoadOperationDO.setOperationName(roadOperationBO.getOperationName().trim());
				
				
				
				//Set court and court date
				if(roadOperationBO.getCategoryId().equalsIgnoreCase(Constants.Category.REGIONAL)){
					CourtDO courtDO = roadOperationDAO.find(CourtDO.class, roadOperationBO.getCourtBO().getCourtId());
					if(courtDO!=null){
						saveRoadOperationDO.setCourt(courtDO);
					}
					else{
						throw new Exception();
					}
					
					String formattedDate = DateUtils.formatDate("yyyy-MM-dd", roadOperationBO.getCourtDate());
					String formattedDateTime =  formattedDate + " " + roadOperationBO.getCourtTime();
				
					Date courtDtime = DateUtils.parse(formattedDateTime, "yyyy-MM-dd HH:mm:ss");
					
					saveRoadOperationDO.setCourtDtime(courtDtime);
				
				}
			}
			if(roadOperationBO.getStatusId().equalsIgnoreCase(Constants.Status.ROAD_OPERATION_STARTED))
			{
				if(roadOperationBO.getActualStartDate()!=null && StringUtils.trimToNull(roadOperationBO.getActualStartTime())!=null){
					String formattedActualStartDate = DateUtils.formatDate("yyyy-MM-dd", roadOperationBO.getActualStartDate());
					String formattedActualStartDateTime =  formattedActualStartDate + " " + roadOperationBO.getActualStartTime();
				
					Date actualStartDtime = DateUtils.parse(formattedActualStartDateTime, "yyyy-MM-dd HH:mm:ss");
					
					saveRoadOperationDO.setActualStartDtime(actualStartDtime);
				}
				
				for(AssignedTeamDetailsBO teamDetails : assignedTeamDetailsBOList)
				{
					savedTeamDO = new TeamDO();
					
					if(teamDetails.getTeamBO().getTeamId()!=null){
						savedTeamDO = roadOperationDAO.find(TeamDO.class, teamDetails.getTeamBO().getTeamId());
					}
					else{
						throw new Exception();
					}
					updateAssignedPerson(teamDetails, roadOperationBO);
					if(teamDetails.getTaVehicleList()!=null){
						updateAssignedVehicle(teamDetails.getTaVehicleList(), teamDetails.getTeamBO().getTeamId(), roadOperationBO);
					}
					savedTeamDO.setNoMotorCycleUsed(teamDetails.getTeamBO().getNoMotorCycleUsed());
					savedTeamDO.getAuditEntry().setUpdateDTime(currentDate);
					savedTeamDO.getAuditEntry().setUpdateUsername(roadOperationBO.getCurrentUsername());
					roadOperationDAO.update(savedTeamDO);
				}
				eventAuditDO = createEventAuditForMainStatesRecord(saveRoadOperationDO, roadOperationBO.getCurrentUsername(), Constants.EventCode.START_OPERATION,roadOperationBO);
			}
			else if(roadOperationBO.getStatusId().equalsIgnoreCase(saveRoadOperationDO.getStatus().getStatusId())){
				eventAuditDO = createEventAuditForUpdatingRecord(saveRoadOperationDO, roadOperationBO.getCurrentUsername(), Constants.EventCode.UPDATE_OPERATION, saveRoadOperationDO.getStatus().getDescription());
			}
			else if(roadOperationBO.getStatusId().equalsIgnoreCase(Constants.Status.ROAD_OPERATION_CANCELLED)){
				saveRoadOperationDO.setComment(roadOperationBO.getComment().trim());
				
				ReasonDO reasonDO = roadOperationDAO.find(ReasonDO.class, roadOperationBO.getReasonId());
				if(reasonDO!=null){
					saveRoadOperationDO.setReason(reasonDO);
				}
				else{
					throw new Exception();
				}
				
				eventAuditDO = createEventAuditForCancelOrTerminateRecord(saveRoadOperationDO, roadOperationBO.getCurrentUsername(), Constants.EventCode.CANCEL_OPERATION, reasonDO.getDescription());
			}
			
			
			
			updateAssignedStrategy(roadOperationBO.getOperationStrategyList(),saveRoadOperationDO,auditEntry);
			
			
		}
		
		
		//Current Status of Road Operation Started
		else if(saveRoadOperationDO.getStatus().getStatusId().equalsIgnoreCase(Constants.Status.ROAD_OPERATION_STARTED) )
		{
			//System.err.println("if(saveRoadOperationDO.getStatus().getStatusId().equalsIgnoreCase(Constants.Status.ROAD_OPERATION_STARTED)");
			if(roadOperationBO.getActualStartDate()!=null && StringUtils.trimToNull(roadOperationBO.getActualStartTime())!=null){
			
				String formattedStartDate = DateUtils.formatDate("yyyy-MM-dd", roadOperationBO.getActualStartDate());
				String formattedStartDateTime =  formattedStartDate + " " + roadOperationBO.getActualStartTime();
			
				Date actualStartDtime = DateUtils.parse(formattedStartDateTime, "yyyy-MM-dd HH:mm:ss");
				
				//System.err.println("actualStartDtime::"+ actualStartDtime);
				
				saveRoadOperationDO.setActualStartDtime(actualStartDtime);
			}
			//Update actual staff and vehicle
			for(AssignedTeamDetailsBO teamDetails : assignedTeamDetailsBOList){
				
				savedTeamDO = new TeamDO();
				
				if(teamDetails.getTeamBO().getTeamId()!=null){
					savedTeamDO = roadOperationDAO.find(TeamDO.class, teamDetails.getTeamBO().getTeamId());
				}
				else{
					throw new Exception();
				}
				
				updateAssignedPerson(teamDetails, roadOperationBO);
				if(teamDetails.getTaVehicleList()!=null){
					updateAssignedVehicle(teamDetails.getTaVehicleList(), teamDetails.getTeamBO().getTeamId(), roadOperationBO);
				}
				
				//Update team
				savedTeamDO.setNoMotorCycleUsed(teamDetails.getTeamBO().getNoMotorCycleUsed());
				savedTeamDO.getAuditEntry().setUpdateDTime(currentDate);
				savedTeamDO.getAuditEntry().setUpdateUsername(roadOperationBO.getCurrentUsername());
				roadOperationDAO.update(savedTeamDO);
				
			}
			
			if(roadOperationBO.getStatusId().equalsIgnoreCase(Constants.Status.ROAD_OPERATION_COMPLETED)){
				String formattedEndDate = DateUtils.formatDate("yyyy-MM-dd", roadOperationBO.getActualEndDate());
				String formattedEndDateTime =  formattedEndDate + " " + roadOperationBO.getActualEndTime();
				Date actualEndDtime = DateUtils.parse(formattedEndDateTime, "yyyy-MM-dd HH:mm:ss");
				
				saveRoadOperationDO.setActualEndDtime(actualEndDtime);
				
				//2014-10-15::validate actual duration with offence date
				validateActualStartDateAgainstOffenceDates(saveRoadOperationDO.getRoadOperationId(),
						saveRoadOperationDO.getActualStartDtime());
				validateActualEndDateAgainstOffenceDates(saveRoadOperationDO.getRoadOperationId(),
						saveRoadOperationDO.getActualEndDtime());
				
				//saveRoadOperationDO.setActualEndDtime(actualEndDtime);
				
				eventAuditDO = createEventAuditForMainStatesRecord(saveRoadOperationDO, roadOperationBO.getCurrentUsername(), Constants.EventCode.COMPLETE_OPERATION,roadOperationBO);
			}
			else if(roadOperationBO.getStatusId().equalsIgnoreCase(saveRoadOperationDO.getStatus().getStatusId())){
				eventAuditDO = createEventAuditForUpdatingRecord(saveRoadOperationDO, roadOperationBO.getCurrentUsername(), Constants.EventCode.UPDATE_OPERATION, saveRoadOperationDO.getStatus().getDescription());
			}
			else if(roadOperationBO.getStatusId().equalsIgnoreCase(Constants.Status.ROAD_OPERATION_TERMINATED)){
				
				/***NOTED BY KPOWELL:2014-10-23
				 **NO LONGER CALLED FROM UI****
				 ** REPLACED BY function updateOperationStatus
				 **/
				//2014-10-17:: set Actual Start and End when terminating an operation
				String formattedEndDate = DateUtils.formatDate("yyyy-MM-dd", roadOperationBO.getActualEndDate());
				String formattedEndDateTime =  formattedEndDate + " " + roadOperationBO.getActualEndTime();
				Date actualEndDtime = DateUtils.parse(formattedEndDateTime, "yyyy-MM-dd HH:mm:ss");
				
				saveRoadOperationDO.setActualEndDtime(actualEndDtime);
				
				saveRoadOperationDO.setComment(roadOperationBO.getComment().trim());
				
				ReasonDO reasonDO = roadOperationDAO.find(ReasonDO.class, roadOperationBO.getReasonId());
				if(reasonDO!=null){
					saveRoadOperationDO.setReason(reasonDO);
				}
				else{
					throw new Exception();
				}
				
				//2014-10-15::validate actual duration with offence date
				validateActualStartDateAgainstOffenceDates(saveRoadOperationDO.getRoadOperationId(),
						saveRoadOperationDO.getActualStartDtime());
				validateActualEndDateAgainstOffenceDates(saveRoadOperationDO.getRoadOperationId(),
						saveRoadOperationDO.getActualEndDtime());
				
				
				eventAuditDO = createEventAuditForCancelOrTerminateRecord(saveRoadOperationDO, roadOperationBO.getCurrentUsername(), Constants.EventCode.TERMINATE_OPERATION, reasonDO.getDescription());
			}
			
		}
		
		//Current Status of Road Operation Completed OR No Action
		else if(saveRoadOperationDO.getStatus().getStatusId().equalsIgnoreCase(Constants.Status.ROAD_OPERATION_COMPLETED) ){
			if(roadOperationBO.getActualStartDate()!=null && StringUtils.isNotBlank(roadOperationBO.getActualStartTime()) && roadOperationBO.getActualEndDate()!=null &&  StringUtils.isNotBlank(roadOperationBO.getActualEndTime())){
				
				String formattedStartDate = DateUtils.formatDate("yyyy-MM-dd", roadOperationBO.getActualStartDate());
				String formattedStartDateTime =  formattedStartDate + " " + roadOperationBO.getActualStartTime();
			
				Date actualStartDtime = DateUtils.parse(formattedStartDateTime, "yyyy-MM-dd HH:mm:ss");
				
				String formattedEndDate = DateUtils.formatDate("yyyy-MM-dd", roadOperationBO.getActualEndDate());
				String formattedEndDateTime =  formattedEndDate + " " + roadOperationBO.getActualEndTime();
				Date actualEndDtime = DateUtils.parse(formattedEndDateTime, "yyyy-MM-dd HH:mm:ss");
				
				saveRoadOperationDO.setActualStartDtime(actualStartDtime);
				saveRoadOperationDO.setActualEndDtime(actualEndDtime);
			}
			//Update actual staff and vehicle
			for(AssignedTeamDetailsBO teamDetails : assignedTeamDetailsBOList){
				savedTeamDO = new TeamDO();
				
				if(teamDetails.getTeamBO().getTeamId()!=null){
					savedTeamDO = roadOperationDAO.find(TeamDO.class, teamDetails.getTeamBO().getTeamId());
				}
				else{
					throw new Exception();
				}
				
				/*Fix for discrepancy 152 - Only need to set attendance where operation if the operation is noting 
				 * going to be updated to No Action. OA - 17Nov2014 */
				if(! roadOperationBO.getStatusId().equalsIgnoreCase(Constants.Status.ROAD_OPERATION_NO_ACTION)){
					updateAssignedPerson(teamDetails, roadOperationBO);
					
					if(teamDetails.getTaVehicleList()!=null){
						updateAssignedVehicle(teamDetails.getTaVehicleList(), teamDetails.getTeamBO().getTeamId(), roadOperationBO);
					}
				}
				savedTeamDO.setNoMotorCycleUsed(teamDetails.getTeamBO().getNoMotorCycleUsed());
				savedTeamDO.getAuditEntry().setUpdateDTime(currentDate);
				savedTeamDO.getAuditEntry().setUpdateUsername(roadOperationBO.getCurrentUsername());
				roadOperationDAO.update(savedTeamDO);
			}
			//updateAssignedPerson(assignedResourceBO, roadOperationBO);
			//updateAssignedVehicle(assignedResourceBO.getTaVehicleList(), roadOperationBO);
			
			if(saveRoadOperationDO.getStatus().getStatusId().equalsIgnoreCase(Constants.Status.ROAD_OPERATION_COMPLETED)){
				
				//TODO
				//2014-10-15::validate actual duration with offence date
				
				if(roadOperationBO.getStatusId().equalsIgnoreCase(Constants.Status.ROAD_OPERATION_CLOSED)){
					if(StringUtils.trimToNull(roadOperationBO.getComment())!=null){
						saveRoadOperationDO.setComment(roadOperationBO.getComment().trim());
					}
					eventAuditDO = createEventAuditForMainStatesRecord(saveRoadOperationDO, roadOperationBO.getCurrentUsername(), Constants.EventCode.CLOSE_OPERATION,roadOperationBO);
				}
				
			}
			else if(saveRoadOperationDO.getStatus().getStatusId().equalsIgnoreCase(Constants.Status.ROAD_OPERATION_NO_ACTION)){
				if(roadOperationBO.getStatusId().equalsIgnoreCase(Constants.Status.ROAD_OPERATION_STARTED)){

					eventAuditDO = createEventAuditForMainStatesRecord(saveRoadOperationDO, roadOperationBO.getCurrentUsername(), Constants.EventCode.START_OPERATION,roadOperationBO);

				}else if(roadOperationBO.getStatusId().equalsIgnoreCase(Constants.Status.ROAD_OPERATION_CANCELLED)){
					//capture comments and reason
					saveRoadOperationDO.setComment(roadOperationBO.getComment().trim());								
					ReasonDO reasonDO = roadOperationDAO.find(ReasonDO.class, roadOperationBO.getReasonId());
					if(reasonDO!=null){
						saveRoadOperationDO.setReason(reasonDO);
					}
					else{
						throw new Exception();
					}

					eventAuditDO = createEventAuditForMainStatesRecord(saveRoadOperationDO, roadOperationBO.getCurrentUsername(), Constants.EventCode.CANCEL_OPERATION,roadOperationBO);
				}
			
			}//else{//any other updates
			if(roadOperationBO.getStatusId().equalsIgnoreCase(saveRoadOperationDO.getStatus().getStatusId())){
				eventAuditDO = createEventAuditForUpdatingRecord(saveRoadOperationDO, roadOperationBO.getCurrentUsername(), Constants.EventCode.UPDATE_OPERATION, saveRoadOperationDO.getStatus().getDescription());

			}
		
		}//end of complete and no_action

		
		//Current Status of Road Operation Closed
		else if(saveRoadOperationDO.getStatus().getStatusId().equalsIgnoreCase(Constants.Status.ROAD_OPERATION_CLOSED) || saveRoadOperationDO.getStatus().getStatusId().equalsIgnoreCase(Constants.Status.ROAD_OPERATION_CANCELLED) || saveRoadOperationDO.getStatus().getStatusId().equalsIgnoreCase(Constants.Status.ROAD_OPERATION_TERMINATED)){
			return false;
		}
		
		if(!roadOperationBO.getStatusId().equalsIgnoreCase(saveRoadOperationDO.getStatus().getStatusId())){
			
		
			StatusDO newStatusDO = roadOperationDAO.find(StatusDO.class, roadOperationBO.getStatusId());
			if(newStatusDO!=null){
				saveRoadOperationDO.setStatus(newStatusDO);
				saveRoadOperationDO.setStatusDtime(currentDate);
			}
			else{
				throw new Exception();
			}
		}
		
		
		//saveRoadOperationDO.getAuditEntry().setUpdateDTime(currentDate);
		//saveRoadOperationDO.getAuditEntry().setUpdateUsername(roadOperationBO.getCurrentUsername());
		roadOperationDAO.update(saveRoadOperationDO);
		
		serviceFactory.getEventAuditService().saveEventAuditDO(eventAuditDO);
		
		return true;
		
		}
		catch (Exception e) 
		{
//			e.printStackTrace();
//			return false;
			throw e;
			
		}
	}

	@Override
	public RoadOperationDO findRoadOperationById(
			Integer roadOperationId) {
		
		RoadOperationDO savedOperationDO = daoFactory.getRoadOperationDAO().find(RoadOperationDO.class, roadOperationId);
		if(savedOperationDO!=null){
			
			return savedOperationDO;
		}
		
		
		return null;
	}

	@Override
	public RoadOperationBO findRoadOperationByName(String operationName) {
		return daoFactory.getRoadOperationDAO().findRoadOperationByName(operationName);
	}

	@Override
	public boolean operationNameExists(String operationName) {
		
		RoadOperationBO roadOperationBO = new RoadOperationBO();
		
		roadOperationBO = daoFactory.getRoadOperationDAO().findRoadOperationByName(operationName);
		
		if(roadOperationBO==null){
			return false;
		}
		return true;
	}

	@Override
	public TAStaffDO findStaffById(String staffId) {
		
		TAStaffDO taStaffDO = new TAStaffDO(); 
		
		taStaffDO = daoFactory.getRoadOperationDAO().find(TAStaffDO.class, staffId);
		
		return taStaffDO;
	}

	@Override
	public List<LocationDO> returnLocationDOList(List<Integer> locationList) {
		return daoFactory.getRoadOperationDAO().returnLocationDOList(locationList);
	}

	@Override
	public LMIS_TAOfficeLocationViewDO findOfficeLocationViewById(
			String officeLocationCode) {
		LMIS_TAOfficeLocationViewDO officeLocationView = new LMIS_TAOfficeLocationViewDO();
		
		officeLocationView = daoFactory.getRoadOperationDAO().find(LMIS_TAOfficeLocationViewDO.class, officeLocationCode);
		
		return officeLocationView;
	}

	@Override
	public List<StrategyDO> returnStrategyDOList(List<Integer> strategyList) {
		return daoFactory.getRoadOperationDAO().returnStrategyDOList(strategyList);
	}

	@Override
	public boolean isArteryRequired(List<Integer> strategyList) {
		return daoFactory.getRoadOperationDAO().isArteryRequired(strategyList);
	}

	@Override
	public List<ArteryDO> returnArteryDOList(List<Integer> arteryIdList) {
		return daoFactory.getRoadOperationDAO().returnArteryDOList(arteryIdList);
	}

	@Override
	public List<AssignedPersonBO> findAssignedPersonByType(
			Integer roadOperationId, String personType) {
		return daoFactory.getRoadOperationDAO().findAssignedPersonByPersonType(roadOperationId, personType);
	}

	@Override
	public AssignedResourceBO findSavedAssignedResource(Integer roadOperationId, Integer teamId) {
		AssignedResourceBO savedAssignedResourceBO = new AssignedResourceBO();
		List<AssignedPersonBO> taStaffList = new ArrayList<AssignedPersonBO>();
		List<AssignedPersonBO> jpList = new ArrayList<AssignedPersonBO>();
		List<AssignedPersonBO> policeOfficerList = new ArrayList<AssignedPersonBO>();
		List<AssignedPersonBO> itaExaminerList = new ArrayList<AssignedPersonBO>();
		List<AssignedVehicleBO> taVehicleList = new ArrayList<AssignedVehicleBO>();
		
		taStaffList = daoFactory.getRoadOperationDAO().findAssignedTeamPersonByType(teamId, Constants.PersonType.TA_STAFF);
		jpList = daoFactory.getRoadOperationDAO().findAssignedTeamPersonByType(teamId, Constants.PersonType.JP);
		policeOfficerList = daoFactory.getRoadOperationDAO().findAssignedTeamPersonByType(teamId, Constants.PersonType.POLICE_OFFCER);
		itaExaminerList = daoFactory.getRoadOperationDAO().findAssignedTeamPersonByType(teamId, Constants.PersonType.ITA_EXAMINER);
		
		taVehicleList = daoFactory.getRoadOperationDAO().findAssignedTeamVehicle(teamId);
		
		savedAssignedResourceBO = new AssignedResourceBO(taStaffList, jpList, policeOfficerList, itaExaminerList, taVehicleList);
		
		return savedAssignedResourceBO;
	}
	
	@Override
	public AssignedResourceBO findSavedAssignedResource(Integer roadOperationId) {
		AssignedResourceBO savedAssignedResourceBO = new AssignedResourceBO();
		List<AssignedPersonBO> taStaffList = new ArrayList<AssignedPersonBO>();
		List<AssignedPersonBO> jpList = new ArrayList<AssignedPersonBO>();
		List<AssignedPersonBO> policeOfficerList = new ArrayList<AssignedPersonBO>();
		List<AssignedPersonBO> itaExaminerList = new ArrayList<AssignedPersonBO>();
		List<AssignedVehicleBO> taVehicleList = new ArrayList<AssignedVehicleBO>();
		
		taStaffList = daoFactory.getRoadOperationDAO().findAssignedPersonByPersonType(roadOperationId, Constants.PersonType.TA_STAFF);
		jpList = daoFactory.getRoadOperationDAO().findAssignedPersonByPersonType(roadOperationId, Constants.PersonType.JP);
		policeOfficerList = daoFactory.getRoadOperationDAO().findAssignedPersonByPersonType(roadOperationId, Constants.PersonType.POLICE_OFFCER);
		itaExaminerList = daoFactory.getRoadOperationDAO().findAssignedPersonByPersonType(roadOperationId, Constants.PersonType.ITA_EXAMINER);
		
		taVehicleList = daoFactory.getRoadOperationDAO().findAssignedVehicle(roadOperationId);
		
		savedAssignedResourceBO = new AssignedResourceBO(taStaffList, jpList, policeOfficerList, itaExaminerList, taVehicleList);
		
		return savedAssignedResourceBO;
	}

	@Override
	public boolean authorizeRoadOperation(RoadOperationBO roadOperationBO) {
		Calendar calendar = Calendar.getInstance();
		Date currentDate = calendar.getTime();
		EventAuditDO eventAuditDO = new EventAuditDO();
		
		try{
			RoadOperationDO saveRoadOperationDO = daoFactory.getRoadOperationDAO().find(RoadOperationDO.class, roadOperationBO.getRoadOperationId());
			
			saveRoadOperationDO.setAuthorized("Y");
			saveRoadOperationDO.setAuthorizedUserName(roadOperationBO.getAuthorizedUserName());
			saveRoadOperationDO.setAuthorizedDtime(currentDate);
			
			saveRoadOperationDO.getAuditEntry().setUpdateDTime(currentDate);
			saveRoadOperationDO.getAuditEntry().setUpdateUsername(roadOperationBO.getCurrentUsername());
			
			eventAuditDO = createEventAuditForUpdatingRecord(saveRoadOperationDO, roadOperationBO.getCurrentUsername(), Constants.EventCode.AUTHORIZE_OPERATION, saveRoadOperationDO.getStatus().getDescription());
	
			daoFactory.getRoadOperationDAO().update(saveRoadOperationDO);
			serviceFactory.getEventAuditService().saveEventAuditDO(eventAuditDO);
			
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public List<RoadOperationBO> lookupRoadOperationWithDateRange(
			RoadOperationCriteriaBO roadOperationCriteriaBO) {
		return  daoFactory.getRoadOperationDAO().lookupRoadOperationWithDateRange(roadOperationCriteriaBO);
	}
	
	
	/**
	 * This method return a Integer List of Artery Id from the Artery List passed
	 * @param operationStrategyList
	 * @return
	 */
	private List<Integer> returnIntegerArteryList(List<ArteryBO> arteryList){
		List<Integer> integerList = new ArrayList<Integer>();
		
		for(ArteryBO artery : arteryList){
			integerList.add(artery.getArteryId());
		}
		
		return integerList;
	}
	
	
	/**
	 * This method return a String List of Parish Codes
	 * @param operationStrategyList
	 * @return
	 */
	private List<String> returnStringParishList(List<ParishBO> parishList){
		List<String> strList = new ArrayList<String>();
		
		for (ParishBO parishBO : parishList) {
			strList.add(parishBO.getParishCode());
		}
		
		return strList;
	}
	
	/**
	 * This method return a Integer List of Location Id from the Location List passed
	 * @param operationStrategyList
	 * @return
	 */
	private List<Integer> returnIntegerLocationList(List<LocationBO> locationList){
		List<Integer> integerList = new ArrayList<Integer>();
		
		for(LocationBO location : locationList){
			integerList.add(location.getLocationId());
		}
		
		return integerList;
	}

	@Override
	public List<ParishDO> findRegionParishes(String officeLocationCode) {
		// TODO Auto-generated method stub
		return daoFactory.getRoadOperationDAO().findRegionParishes(officeLocationCode);
	}

	@Override
	public List<LocationBO> returnOperationLocationList(Integer roadOperationId) {
		// TODO Auto-generated method stub
		return daoFactory.getRoadOperationDAO().returnOperationLocationList(roadOperationId);
	}

	@Override
	public List<ArteryBO> findOperationArteryList(List<Integer> locationIdList,
			String arteryStr) {
		// TODO Auto-generated method stub
		return daoFactory.getRoadOperationDAO().findOperationArteryList(locationIdList, arteryStr);
	}

	@Override
	public List<RoadOperationBO> findOperationForRoadCheckList(String operationStr) {
		List<String> statusIdList = new ArrayList<String>();
		
		statusIdList.add(Constants.Status.ROAD_OPERATION_STARTED);
		statusIdList.add(Constants.Status.ROAD_OPERATION_TERMINATED);
		statusIdList.add(Constants.Status.ROAD_OPERATION_COMPLETED);
		statusIdList.add(Constants.Status.ROAD_OPERATION_NO_ACTION);
		
		return daoFactory.getRoadOperationDAO().findOperationForRoadCheckList(statusIdList, operationStr);
	}

	@Override
	public List<String> findOfficeLocCodeList(Integer roadOperationId) {
		
		return daoFactory.getRoadOperationDAO().findOfficeLocCodeList(roadOperationId);
	}

	@Override
	public RoadOperationBO findActiveOperationForUser(String userName) {
		return daoFactory.getRoadOperationDAO().findActiveOperationForUser(userName);
	}

	@Override
	public List<RoadOperationBO> findOperationForRoadCheckList() {
		List<String> statusIdList = new ArrayList<String>();
		
		statusIdList.add(Constants.Status.ROAD_OPERATION_STARTED);
		statusIdList.add(Constants.Status.ROAD_OPERATION_TERMINATED);
		statusIdList.add(Constants.Status.ROAD_OPERATION_COMPLETED);
		
		return daoFactory.getRoadOperationDAO().findOperationForRoadCheckList(statusIdList);
	}

	@Override
	public List<ArteryBO> findOperationArteryList(List<Integer> locationIdList,Integer operationId) 
	{
		return daoFactory.getRoadOperationDAO().findOperationArteryList(locationIdList,operationId );
	}
	@Override
	public List<TAStaffBO> findAssignedStaffForRoadOperation(Integer roadOperationId){
		
		List<TeamDO> teamList = new ArrayList<TeamDO>();
		
		List<TAStaffBO> operationTaStaffList = new ArrayList<TAStaffBO>();
		List<TAStaffBO> taStaffList = new ArrayList<TAStaffBO>();
		
			
		teamList = daoFactory.getRoadOperationDAO().findOperationTeams(roadOperationId);
		
		for(TeamDO team : teamList){
		
			taStaffList = new ArrayList<TAStaffBO>();
			
			
			taStaffList = daoFactory.getRoadOperationDAO().findAssignedStaffByTeam(roadOperationId, team.getTeamId());
			
			if(taStaffList!=null){
				for(TAStaffBO staffBo : taStaffList){
					operationTaStaffList.add(staffBo);
				}
			}
			
			
		}
		
		
		return operationTaStaffList;
	}

	@Override
	public List<ArteryBO> findArteryList() {
		return daoFactory.getRoadOperationDAO().findArteryList();
	}

	@Override
	public String canOperationBeClosed(Integer roadOpId)
	{
		return daoFactory.getRoadOperationDAO().checkIfThereAreUnprocessedDocuments(roadOpId);
	}
	
	/**
	 * 
	 * @param roadOpId
	 * @param actualStartDate
	 * @param actualEndDate
	 * @return
	 * @throws InvalidFieldException
	 */
	public void validateActualStartDateAgainstOffenceDates(Integer roadOpId, Date actualStartDate)throws InvalidFieldException
	{
		
		//2014-10-15::validate actual duration with offence date
		Date firstOffDate = daoFactory.getRoadOperationDAO().getFirstOffenceDate(roadOpId);
		String msgText="";
		
		boolean validDates = true;
		try{
			/*System.err.println("firstOffDate.compareTo(saveRoadOperationDO.getActualStartDtime()::"
		+ firstOffDate.compareTo(actualStartDate));*/
			//System.err.println("Start Date Actual " + actualStartDate);
		if(actualStartDate != null){
			if(firstOffDate.compareTo(actualStartDate)<0){
				//first offence date before actual start date
				msgText = "Actual Start Date and Time MUST be on or before "+ firstOffDate;
			}
		}
			//System.err.println("Validating Actual with Offence Dates::"+ msgText);
			if( StringUtils.trimToNull(msgText) != null){
				validDates = false;
				throw new InvalidFieldException(msgText);				
			}
			
		}catch (NullPointerException e) {
			// TODO: handle exception
			//no error necessary
			e.printStackTrace();
		}/*catch(Exception e1){
			e1.printStackTrace();
		}*/
		//return msgText;
		
	}
	
	public void validateActualEndDateAgainstOffenceDates(Integer roadOpId, Date actualEndDate)throws InvalidFieldException
	{
		
		//2014-10-15::validate actual duration with offence date
		Date lastOffDate = daoFactory.getRoadOperationDAO().getLastOffenceDate(roadOpId);
		String msgText="";
		
		boolean validDates = true;
		try{
		/*	System.err.println("lastOffDate.compareTo(saveRoadOperationDO.getActualEndDtime()"
			+ lastOffDate.compareTo(actualEndDate));*/
			//System.err.println("Actual End Date " + actualEndDate);
			if(actualEndDate != null){
				if(lastOffDate.compareTo(actualEndDate)>0){
					//last offence date after actual start date
					msgText ="Actual End Date and Time MUST be on or after the last offence date ["+ DateUtils.getFormattedUtilDate_YYYY_MM_DD_hh_mm_a(lastOffDate) + "].";
				}
			}
			
			//System.err.println("Validating Actual with Offence Dates::"+ msgText);
			if( StringUtils.trimToNull(msgText) != null){
				validDates = false;
				throw new InvalidFieldException(msgText);				
			}
			
		}catch (NullPointerException e) {
			// TODO: handle exception
			//no error necessary
			e.printStackTrace();
		}/*catch(Exception e1){
			e1.printStackTrace();
		}*/
		//return msgText;
		
	}

	
		
}
