package fsl.ta.toms.roms.service;

import java.util.Date;
import java.util.List;

import fsl.ta.toms.roms.bo.ArteryBO;
import fsl.ta.toms.roms.bo.AssignedPersonBO;
import fsl.ta.toms.roms.bo.AssignedResourceBO;
import fsl.ta.toms.roms.bo.AssignedTeamDetailsBO;
import fsl.ta.toms.roms.bo.AvailableResourceBO;
import fsl.ta.toms.roms.bo.AvailableResourceIds;
import fsl.ta.toms.roms.bo.ITAExaminerBO;
import fsl.ta.toms.roms.bo.JPBO;
import fsl.ta.toms.roms.bo.LocationBO;
import fsl.ta.toms.roms.bo.PoliceOfficerBO;
import fsl.ta.toms.roms.bo.RoadOperationBO;
import fsl.ta.toms.roms.bo.RoadOperationOtherDetailsBO;
import fsl.ta.toms.roms.bo.StrategyBO;
import fsl.ta.toms.roms.bo.StrategyRuleBO;
import fsl.ta.toms.roms.bo.TAStaffBO;
import fsl.ta.toms.roms.bo.TAVehicleBO;
import fsl.ta.toms.roms.dataobjects.ArteryDO;
import fsl.ta.toms.roms.dataobjects.LMIS_TAOfficeLocationViewDO;
import fsl.ta.toms.roms.dataobjects.LocationDO;
import fsl.ta.toms.roms.dataobjects.ParishDO;
import fsl.ta.toms.roms.dataobjects.RoadOperationDO;
import fsl.ta.toms.roms.dataobjects.StrategyDO;
import fsl.ta.toms.roms.dataobjects.TAStaffDO;
import fsl.ta.toms.roms.exception.InvalidFieldException;
import fsl.ta.toms.roms.exception.NoRecordFoundException;
import fsl.ta.toms.roms.search.criteria.impl.AvailableResourceCriteriaBO;
import fsl.ta.toms.roms.search.criteria.impl.RoadOperationCriteriaBO;

public interface RoadOperationService {

	public List<RoadOperationBO> lookupRoadOperation(RoadOperationCriteriaBO roadOperationCriteriaBO);
	
	public RoadOperationOtherDetailsBO findRoadOperationOtherDetails(Integer roadOperationId);
	
	public TAStaffBO findTAStaffByPersonID(Integer personId);
	
	public JPBO findJPByPersonID(Integer personId);

	public ITAExaminerBO findITAExaminerByPersonID(Integer personId);
	
	public PoliceOfficerBO findPoliceOfficerByPersonID(Integer personId);
	
	public List<TAStaffBO> getAvailableTAStaff(AvailableResourceCriteriaBO availableResourceCriteriaBO);
	
	public List<JPBO> getAvailableJP(AvailableResourceCriteriaBO availableResourceCriteriaBO);
	
	public List<ITAExaminerBO> getAvailableITAExaminer(AvailableResourceCriteriaBO availableResourceCriteriaBO);
	
	public List<PoliceOfficerBO> getAvailablePoliceOfficer(AvailableResourceCriteriaBO availableResourceCriteriaBO);
	
	public List<TAVehicleBO> getAvailableTAVehicle(AvailableResourceCriteriaBO availableResourceCriteriaBO);
	
	public AvailableResourceBO getAvailableResource(AvailableResourceCriteriaBO availableResourceCriteriaBO);
	
	public AvailableResourceIds getAvailableResourceIds(AvailableResourceCriteriaBO availableResourceCriteriaBO);
	
	public boolean isPersonAvailable(AvailableResourceCriteriaBO availableResourceCriteriaBO);
	
	public boolean isTAVehicleAvailable(AvailableResourceCriteriaBO availableResourceCriteriaBO);
	
	public List<StrategyRuleBO> getOperationStrategyRule(List<Integer> strategyIdList);
	
	public boolean isVehicleRequired(List<Integer> strategyIdList);
	
	public boolean isOperationStrategyValid(List<Integer> strategyIdList, List<AssignedTeamDetailsBO> assignedTeamDetailsBOList);
	
	public boolean isStrategyIdListValid(List<Integer> strategyList);
	
	public RoadOperationBO saveRoadOperation(RoadOperationBO roadOperationBO, List<AssignedTeamDetailsBO> assignedTeamDetailsBOList) throws Exception;
	
	public boolean updateRoadOperation(RoadOperationBO roadOperationBO, List<AssignedTeamDetailsBO> assignedTeamDetailsBOList) throws Exception;
	
	public RoadOperationDO findRoadOperationById(Integer roadOperationId);
	
	public RoadOperationBO findRoadOperationByName(String operationName);
	
	public boolean operationNameExists(String operationName);
	
	public TAStaffDO findStaffById(String staffId);
	
	public List<LocationDO> returnLocationDOList(List<Integer> locationList);
	
	public LMIS_TAOfficeLocationViewDO findOfficeLocationViewById(String officeLocationCode);
	
	public List<StrategyDO> returnStrategyDOList(List<Integer> strategyList);
	
	public boolean isArteryRequired(List<Integer> strategyList);
	
	public List<ArteryDO> returnArteryDOList(List<Integer> arteryIdList);
	
	public List<AssignedPersonBO> findAssignedPersonByType(Integer roadOperationId, String personType);
	
	public AssignedResourceBO findSavedAssignedResource(Integer roadOperationId, Integer teamId);
	
	public boolean authorizeRoadOperation(RoadOperationBO roadOperationBO);

	public List<RoadOperationBO> lookupRoadOperationWithDateRange(
			RoadOperationCriteriaBO roadOperationCriteriaBO);
	
	public boolean updateRoadOperationTeam(RoadOperationBO roadOperationBO,
			AssignedTeamDetailsBO assignedTeamDetailsBO);
	
	public List<ParishDO> findRegionParishes(String officeLocationCode);
	
	public AssignedResourceBO findSavedAssignedResource(Integer roadOperationId);
	
	public List<TAStaffBO> getAvailableTAStaffResource(AvailableResourceCriteriaBO availableResourceCriteriaBO) ;
	
	public List<JPBO> getAvailableJPResource(AvailableResourceCriteriaBO availableResourceCriteriaBO) ;
	
	public List<PoliceOfficerBO> getAvailablePoliceResource(AvailableResourceCriteriaBO availableResourceCriteriaBO) throws NoRecordFoundException;
	
	public List<ITAExaminerBO> getAvailableITAResource(AvailableResourceCriteriaBO availableResourceCriteriaBO);
	
	public List<TAVehicleBO> getAvailableTAVehicleResource(AvailableResourceCriteriaBO availableResourceCriteriaBO);
	
	public List<LocationBO> returnOperationLocationList(Integer roadOperationId);
	
	public List<ArteryBO> findOperationArteryList(List<Integer> locationIdList, String arteryStr);
	
	public List<ArteryBO> findOperationArteryList(List<Integer> locationIdList, Integer operationId);
	
	public List<RoadOperationBO> findOperationForRoadCheckList(String operationStr);
	
	public List<String> findOfficeLocCodeList(Integer roadOperationId);
	
	public RoadOperationBO findActiveOperationForUser(String userName);
	
	public List<RoadOperationBO> findOperationForRoadCheckList();
	
	public List<TAStaffBO> findAssignedStaffForRoadOperation(Integer roadOperationId);
	
	public List<ArteryBO> findArteryList();

	void updateOperationStatus(RoadOperationBO roadOpBo,
			String status) throws InvalidFieldException, Exception;
	
	public String canOperationBeClosed(Integer roadOpId);
	
	//public String validateActualDurationAgainstOffenceDates(Integer roadOpId, Date actualStartDate, Date actualEndDate);
	public void validateActualEndDateAgainstOffenceDates(Integer roadOpId, Date actualEndDate)throws InvalidFieldException;
	public void validateActualStartDateAgainstOffenceDates(Integer roadOpId, Date actualStartDate)throws InvalidFieldException;
	public boolean validateStrategyRule(List<StrategyBO> strats, AssignedTeamDetailsBO teamDetails, List<Integer> strategyList);

		
}
