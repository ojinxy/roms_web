package fsl.ta.toms.roms.dao;

import java.sql.Date;
import java.util.List;

import fsl.dao.DAO;
import fsl.ta.toms.roms.bo.ArteryBO;
import fsl.ta.toms.roms.bo.AssignedPersonBO;
import fsl.ta.toms.roms.bo.AssignedVehicleBO;
import fsl.ta.toms.roms.bo.ITAExaminerBO;
import fsl.ta.toms.roms.bo.JPBO;
import fsl.ta.toms.roms.bo.LocationBO;
import fsl.ta.toms.roms.bo.OperationArteryBO;
import fsl.ta.toms.roms.bo.OperationLocationBO;
import fsl.ta.toms.roms.bo.OperationStrategyBO;
import fsl.ta.toms.roms.bo.ParishBO;
import fsl.ta.toms.roms.bo.PoliceOfficerBO;
import fsl.ta.toms.roms.bo.RoadOperationBO;
import fsl.ta.toms.roms.bo.StrategyBO;
import fsl.ta.toms.roms.bo.StrategyRuleBO;
import fsl.ta.toms.roms.bo.TAStaffBO;
import fsl.ta.toms.roms.bo.TAVehicleBO;
import fsl.ta.toms.roms.dataobjects.OperationStrategyDO;
import fsl.ta.toms.roms.dataobjects.ParishDO;
import fsl.ta.toms.roms.dataobjects.TeamArteryDO;
import fsl.ta.toms.roms.dataobjects.TeamDO;
import fsl.ta.toms.roms.dataobjects.ArteryDO;
import fsl.ta.toms.roms.dataobjects.AssignedPersonDO;
import fsl.ta.toms.roms.dataobjects.AssignedVehicleDO;
import fsl.ta.toms.roms.dataobjects.LocationDO;
import fsl.ta.toms.roms.dataobjects.RoadOperationDO;
import fsl.ta.toms.roms.dataobjects.StrategyDO;
import fsl.ta.toms.roms.dataobjects.TeamLocationDO;
import fsl.ta.toms.roms.dataobjects.TeamParishDO;
import fsl.ta.toms.roms.exception.NoRecordFoundException;
import fsl.ta.toms.roms.search.criteria.impl.AvailableResourceCriteriaBO;
import fsl.ta.toms.roms.search.criteria.impl.RoadOperationCriteriaBO;

public interface RoadOperationDAO extends DAO{
	
	public List<RoadOperationBO> lookupRoadOperation(RoadOperationCriteriaBO roadOperationCriteriaBO);
	
	/**
	 * This function does the same thing as the lookupRoadOperation. The diffrence is that it uses
	 * a ranges for date to filter results. That is any road op. with dates that fall between the 
	 * date parameters.
	 * @param roadOperationCriteriaBO
	 * @return
	 */
	public List<RoadOperationBO> lookupRoadOperationWithDateRange(RoadOperationCriteriaBO roadOperationCriteriaBO);
	
	public List<AssignedPersonBO> findAssignedPersonByTeam(Integer roadOperationId, Integer teamId);
	
	public List<AssignedPersonBO> findAssignedPerson(Integer roadOperationId);
	
	public List<AssignedPersonBO> findAssignedTeamPersonByType(Integer teamId, String personType);
	
	public List<AssignedVehicleBO> findAssignedVehicle(Integer roadOperationId);
	
	public List<AssignedVehicleBO> findAssignedVehicleByTeam(Integer roadOperationId, Integer teamId);
	
	public List<OperationStrategyBO> findOperationStrategy(Integer roadOperationId);
	
	public List<OperationLocationBO> findOperationLocation(Integer roadOperationId);
	
	public List<OperationLocationBO> findOperationLocationByTeam(Integer roadOperationId, Integer teamId);
	
	public List<OperationArteryBO> findOperationArtery(Integer roadOperationId);
	
	public List<OperationArteryBO> findOperationArteryByTeam(Integer roadOperationId, Integer teamId);
	
	public TAStaffBO findTAStaffByPersonID(Integer personId);
	
	public JPBO findJPByPersonID(Integer personId);

	public ITAExaminerBO findITAExaminerByPersonID(Integer personId);
	
	public PoliceOfficerBO findPoliceOfficerByPersonID(Integer personId);
	
	public List<TAStaffBO> getAvailableTAStaff(AvailableResourceCriteriaBO availableResourceCriteriaBO);
	
	public List<Integer> getAvailableTAStaffIds(AvailableResourceCriteriaBO availableResourceCriteriaBO);
	
	public List<JPBO> getAvailableJP(AvailableResourceCriteriaBO availableResourceCriteriaBO);
	
	List<Integer> getAvailableJPIds(AvailableResourceCriteriaBO availableResourceCriteriaBO);
	
	public List<ITAExaminerBO> getAvailableITAExaminer(AvailableResourceCriteriaBO availableResourceCriteriaBO);
	
	public List<Integer> getAvailableITAExaminerIds(AvailableResourceCriteriaBO availableResourceCriteriaBO);
	
	public List<PoliceOfficerBO> getAvailablePoliceOfficer(AvailableResourceCriteriaBO availableResourceCriteriaBO) throws NoRecordFoundException;
	
	public List<Integer> getAvailablePoliceOfficerIds(AvailableResourceCriteriaBO availableResourceCriteriaBO) throws NoRecordFoundException;
	
	public List<TAVehicleBO> getAvailableTAVehicle(AvailableResourceCriteriaBO availableResourceCriteriaBO);
	
	public List<Integer> getAvailableTAVehicleIds(AvailableResourceCriteriaBO availableResourceCriteriaBO);
	
	public boolean isPersonAvailable(AvailableResourceCriteriaBO availableResourceCriteriaBO);
	
	public boolean isTAVehicleAvailable(AvailableResourceCriteriaBO availableResourceCriteriaBO);
	
	public List<StrategyRuleBO> getOperationStrategyRule(List<Integer> strategyIdList);
	
	public boolean isVehicleRequired(List<Integer> strategyIdList);
	
	public Integer saveRoadOperation(RoadOperationDO roadOperationDO);
	
	public List<AssignedPersonBO> findAssignedPersonByPersonType(Integer roadOperationId, String personTypeId);
	
	public List<AssignedPersonBO> findAssignedTeamPersonByPersonType(Integer teamId, String personTypeId);
	
	public List<AssignedVehicleBO> findAssignedTeamVehicle(Integer teamId);
	
	public AssignedPersonDO findAssignedPersonById(Integer personId, Integer teamId, String personTypeId);
	
	public AssignedVehicleDO findAssignedVehicleById(Integer taVehicleId, Integer teamId);
	
	public RoadOperationBO findRoadOperationByName(String operationName);
	
	public List<LocationDO> returnLocationDOList(List<Integer> locationIdList);
	
	public List<StrategyDO> returnStrategyDOList(List<Integer> strategyIdList);
	
	public boolean isArteryRequired(List<Integer> strategyIdList);
	
	public List<ArteryDO> returnArteryDOList(List<Integer> arteryIdList);
	
	public List<TeamDO> findOperationTeams(Integer roadOperationId);
	
	public List<TAStaffBO> findAssignedStaffByTeam(Integer roadOperationId, Integer teamId);
	
	public List<JPBO> findAssignedJPByTeam(Integer roadOperationId, Integer teamId);
	
	public List<ITAExaminerBO> findAssignedITAExaminerByTeam(Integer roadOperationId, Integer teamId);
	
	public List<PoliceOfficerBO> findAssignedPoliceOfficerByTeam(Integer roadOperationId, Integer teamId);
	
	public List<TAVehicleBO> findAssignedTAVehicleByTeam(Integer roadOperationId, Integer teamId);
	
	public List<ArteryBO> findArteryByTeam(Integer roadOperationId, Integer teamId);
	
	public List<LocationBO> findLocationByTeam(Integer roadOperationId, Integer teamId);
	
	public List<ParishBO> findParishByTeam(Integer roadOperationId, Integer teamId);
	
	public List<StrategyBO> findStrategyForOperation(Integer roadOperationId);
	
	public Integer saveTeam(TeamDO teamDO);
	
	public TeamArteryDO findAssignedArteryById(Integer arteryId, Integer teamId);
	
	public TeamLocationDO findAssignedLocationById(Integer locationId, Integer teamId);
	
	public List<ParishDO> findRegionParishes(String officeLocationCode);
	
	public List<LocationBO> returnOperationLocationList(Integer roadOperationId);
	
	public List<ArteryBO> findOperationArteryList(List<Integer> locationIdList, String arteryStr);
	public List<ArteryBO> findOperationArteryList(List<Integer> locationIdList, Integer opertationId);
	public List<RoadOperationBO> findOperationForRoadCheckList(List<String> statusIdList, String operationStr);
	public List<RoadOperationBO> findOperationForRoadCheckList(List<String> statusIdList);
	/**
	 * This function returns a list of all associated office codes based on the road operation id.
	 * This look up is done by checking ROMS_OPERATION_REGION TABLE.
	 * @param roadOperationId
	 * @return
	 * @author oanguin
	 * @since 11 November 2013
	 */
	public List<String> findOfficeLocCodeList(Integer roadOperationId);
	public RoadOperationBO findActiveOperationForUser(String userName);
	
	public List<TAStaffBO> findAssignedStaffForRoadOperation(Integer roadOperationId);
	
	public List<ArteryBO> findArteryList();

	public void saveAssPerson(AssignedPersonDO assignedPersonDO);
	
	public TeamParishDO findAssignedParishById(String parishCode,Integer teamId);
	
	public List<String> findOperationRegions(Integer roadOperationId);

	List<PoliceOfficerBO> getAvailablePoliceOfficerOld(
			AvailableResourceCriteriaBO availableResourceCriteriaBO);
	
	public List<OperationStrategyDO> getOpStrategyListDO(Integer roadOperationId);
	
	public List<TeamParishDO> getTeamParishListDO(Integer teamId);
	
	public List<TeamLocationDO> getTeamLocationListDO(Integer teamId);
	
	public List<TeamArteryDO> getTeamArteryListDO(Integer teamId);
	
	/**
	 * This function returns a message stating the whether or not there are unprinted documents for the road operation.
	 * If there are no unprinted documents NULL is returned.
	 * @param roadOpId
	 * @return
	 */
	public String checkIfThereAreUnprocessedDocuments(Integer roadOpId);
	public java.util.Date getFirstOffenceDate(Integer roadOpId);
	public java.util.Date getLastOffenceDate(Integer roadOpId);

	
		
}
