/**
 * Created By: oanguin
 * Date: May 10, 2013
 *
 */
package fsl.ta.toms.roms.bo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * This class has all the data to be displayed in the Operation Summary Report
 * @author oanguin
 * Created Date: May 10, 2013
 */
public class RoadOperationSummaryResultsBO implements Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Date operationCreateDate,
		 scheduledEndDateTime,
		 scheduledStartDateTime,
		 actualStartDateTime,
		 actualEndDateTime;
	
	String operationStatus,
	 	   operationCategory,
	 	   operationName,
	 	   TAOfficeRegion,
		   TAOfficeRegionDescription;
	
	Integer countSummonsIssued,
			countWaningNoticesIssued,
			countVehiclesSeized,
			countCompliancyActivitiesCommited,
			countMotorVehiclesChecked,
			countDrivesLicenceChecked,
			countBadgesChecked,
			countCitationChecks,
			countRoadLicencesChecked,
			countOtherChecks,
			durationOfOperationInMinutes,
			countAbsentMembers,
			countTeamMembers,
			operationId,
			countWarningNoProsecutions,
			countJPs,
			countPoliceOfficers,
			countTAInspectors,
			countITAExaminers;
	
	
	//UR-057
	Integer countAbsentTAInspectors,
			countAbsentJPs,
			countAbsentPoliceOfficers,
			countAbsentITAExaminers;
			
	
	
	Integer countPlatesRemoved;
	
	Integer warningsForProcecution;
	
	Integer allInOrders;
	
	List<RoadOperationTeamSummaryResults> teamSummaries;
	

	public Integer getCountPlatesRemoved() {
		return countPlatesRemoved;
	}

	public void setCountPlatesRemoved(Integer countPlatesRemoved) {
		this.countPlatesRemoved = countPlatesRemoved;
	}

	public Integer getWarningsForProcecution() {
		return warningsForProcecution;
	}

	public void setWarningsForProcecution(Integer warningsForProcecution) {
		this.warningsForProcecution = warningsForProcecution;
	}

	public Integer getAllInOrders() {
		return allInOrders;
	}

	public void setAllInOrders(Integer allInOrders) {
		this.allInOrders = allInOrders;
	}

	public Date getOperationCreateDate() {
		return operationCreateDate;
	}

	public void setOperationCreateDate(Date operationCreateDate) {
		this.operationCreateDate = operationCreateDate;
	}

	public Date getScheduledEndDateTime() {
		return scheduledEndDateTime;
	}

	public void setScheduledEndDateTime(Date scheduledEndDateTime) {
		this.scheduledEndDateTime = scheduledEndDateTime;
	}

	public Date getScheduledStartDateTime() {
		return scheduledStartDateTime;
	}

	public void setScheduledStartDateTime(Date scheduledStartDateTime) {
		this.scheduledStartDateTime = scheduledStartDateTime;
	}

	public Date getActualStartDateTime() {
		return actualStartDateTime;
	}

	public void setActualStartDateTime(Date actualStartDateTime) {
		this.actualStartDateTime = actualStartDateTime;
	}

	public Date getActualEndDateTime() {
		return actualEndDateTime;
	}

	public void setActualEndDateTime(Date actualEndDateTime) {
		this.actualEndDateTime = actualEndDateTime;
	}

	public String getOperationStatus() {
		return operationStatus;
	}

	public void setOperationStatus(String operationStatus) {
		this.operationStatus = operationStatus;
	}

	public String getOperationCategory() {
		return operationCategory;
	}

	public void setOperationCategory(String operationCategory) {
		this.operationCategory = operationCategory;
	}

	public Integer getCountSummonsIssued() {
		return countSummonsIssued;
	}

	public void setCountSummonsIssued(Integer countSummonsIssued) {
		this.countSummonsIssued = countSummonsIssued;
	}

	public Integer getCountWaningNoticesIssued() {
		return countWaningNoticesIssued;
	}

	public void setCountWaningNoticesIssued(Integer countWaningNoticesIssued) {
		this.countWaningNoticesIssued = countWaningNoticesIssued;
	}

	public Integer getCountVehiclesSeized() {
		return countVehiclesSeized;
	}

	public void setCountVehiclesSeized(Integer countVehiclesSeized) {
		this.countVehiclesSeized = countVehiclesSeized;
	}

	public Integer getCountCompliancyActivitiesCommited() {
		return countCompliancyActivitiesCommited;
	}

	public void setCountCompliancyActivitiesCommited(
			Integer countCompliancyActivitiesCommited) {
		this.countCompliancyActivitiesCommited = countCompliancyActivitiesCommited;
	}

	public Integer getCountMotorVehiclesChecked() {
		return countMotorVehiclesChecked;
	}

	public void setCountMotorVehiclesChecked(Integer countMotorVehiclesChecked) {
		this.countMotorVehiclesChecked = countMotorVehiclesChecked;
	}

	public Integer getCountDrivesLicenceChecked() {
		return countDrivesLicenceChecked;
	}

	public void setCountDrivesLicenceChecked(Integer countDrivesLicenceChecked) {
		this.countDrivesLicenceChecked = countDrivesLicenceChecked;
	}

	public Integer getCountBadgesChecked() {
		return countBadgesChecked;
	}

	public void setCountBadgesChecked(Integer countBadgesChecked) {
		this.countBadgesChecked = countBadgesChecked;
	}

	public Integer getCountCitationChecks() {
		return countCitationChecks;
	}

	public void setCountCitationChecks(Integer countCitationChecks) {
		this.countCitationChecks = countCitationChecks;
	}

	public Integer getCountRoadLicencesChecked() {
		return countRoadLicencesChecked;
	}

	public void setCountRoadLicencesChecked(Integer countRoadLicencesChecked) {
		this.countRoadLicencesChecked = countRoadLicencesChecked;
	}

	public Integer getCountOtherChecks() {
		return countOtherChecks;
	}

	public void setCountOtherChecks(Integer countOtherChecks) {
		this.countOtherChecks = countOtherChecks;
	}

	public Integer getDurationOfOperationInMinutes() {
		return durationOfOperationInMinutes;
	}

	public void setDurationOfOperationInMinutes(Integer durationOfOperationInMinutes) {
		this.durationOfOperationInMinutes = durationOfOperationInMinutes;
	}

	public Integer getCountAbsentMembers() {
		return countAbsentMembers;
	}

	public void setCountAbsentMembers(Integer countAbsentMembers) {
		this.countAbsentMembers = countAbsentMembers;
	}

	
	public String getOperationName() {
		return operationName;
	}

	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}

	public Integer getOperationId() {
		return operationId;
	}

	public void setOperationId(Integer operationId) {
		this.operationId = operationId;
	}
	
	

	public Integer getCountTeamMembers() {
		return countTeamMembers;
	}

	public void setCountTeamMembers(Integer countTeamMembers) {
		this.countTeamMembers = countTeamMembers;
	}

	
	
	public List<RoadOperationTeamSummaryResults> getTeamSummaries() {
		return teamSummaries;
	}

	public void setTeamSummaries(List<RoadOperationTeamSummaryResults> teamSummaries) {
		this.teamSummaries = teamSummaries;
	}
	
	

	public Integer getCountWarningNoProsecutions() {
		return countWarningNoProsecutions;
	}

	public void setCountWarningNoProsecutions(Integer countWarningNoProsecutions) {
		this.countWarningNoProsecutions = countWarningNoProsecutions;
	}
	
	

	public Integer getCountJPs() {
		return countJPs;
	}

	public void setCountJPs(Integer countJPs) {
		this.countJPs = countJPs;
	}

	public Integer getCountPoliceOfficers() {
		return countPoliceOfficers;
	}

	public void setCountPoliceOfficers(Integer countPoliceOfficers) {
		this.countPoliceOfficers = countPoliceOfficers;
	}

	public Integer getCountTAInspectors() {
		return countTAInspectors;
	}

	public void setCountTAInspectors(Integer countTAInspectors) {
		this.countTAInspectors = countTAInspectors;
	}

	public Integer getCountITAExaminers() {
		return countITAExaminers;
	}

	public void setCountITAExaminers(Integer countITAExaminers) {
		this.countITAExaminers = countITAExaminers;
	}
	
	

	public String getTAOfficeRegion()
	{
		return TAOfficeRegion;
	}

	public void setTAOfficeRegion(String tAOfficeRegion)
	{
		TAOfficeRegion = tAOfficeRegion;
	}

	public String getTAOfficeRegionDescription()
	{
		return TAOfficeRegionDescription;
	}

	public void setTAOfficeRegionDescription(String tAOfficeRegionDescription)
	{
		TAOfficeRegionDescription = tAOfficeRegionDescription;
	}

	public RoadOperationSummaryResultsBO(Date operationCreateDate,
			Date scheduledEndDateTime, Date scheduledStartDateTime,
			Date actualStartDateTime, Date actualEndDateTime,
			String operationStatus,
			String operationCategory, String operationName,
			Integer countSummonsIssued, Integer countWaningNoticesIssued,
			Integer countVehiclesSeized,
			Integer countCompliancyActivitiesCommited,
			Integer countMotorVehiclesChecked,
			Integer countDrivesLicenceChecked, Integer countBadgesChecked,
			Integer countCitationChecks, Integer countRoadLicencesChecked,
			Integer countOtherChecks, Integer durationOfOperationInMinutes,
			Integer countAbsentMembers, Integer operationId, Integer countTeamMembers,
			List<RoadOperationTeamSummaryResults> teamSummaries) {
		super();
		this.operationCreateDate = operationCreateDate;
		this.scheduledEndDateTime = scheduledEndDateTime;
		this.scheduledStartDateTime = scheduledStartDateTime;
		this.actualStartDateTime = actualStartDateTime;
		this.actualEndDateTime = actualEndDateTime;
		this.operationStatus = operationStatus;
		this.operationCategory = operationCategory;
		this.operationName = operationName;
		this.countSummonsIssued = countSummonsIssued;
		this.countWaningNoticesIssued = countWaningNoticesIssued;
		this.countVehiclesSeized = countVehiclesSeized;
		this.countCompliancyActivitiesCommited = countCompliancyActivitiesCommited;
		this.countMotorVehiclesChecked = countMotorVehiclesChecked;
		this.countDrivesLicenceChecked = countDrivesLicenceChecked;
		this.countBadgesChecked = countBadgesChecked;
		this.countCitationChecks = countCitationChecks;
		this.countRoadLicencesChecked = countRoadLicencesChecked;
		this.countOtherChecks = countOtherChecks;
		this.durationOfOperationInMinutes = durationOfOperationInMinutes;
		this.countAbsentMembers = countAbsentMembers;
		this.operationId = operationId;
		this.countTeamMembers = countTeamMembers;
		this.teamSummaries = teamSummaries;
	}
	
	
	
	
	
	

	public RoadOperationSummaryResultsBO(Date operationCreateDate,
			Date scheduledEndDateTime, Date scheduledStartDateTime,
			Date actualStartDateTime, Date actualEndDateTime,
			String operationStatus,
			String operationCategory, String operationName,
			Integer countSummonsIssued, Integer countWaningNoticesIssued,
			Integer countVehiclesSeized,
			Integer countCompliancyActivitiesCommited,
			Integer countMotorVehiclesChecked,
			Integer countDrivesLicenceChecked, Integer countBadgesChecked,
			Integer countCitationChecks, Integer countRoadLicencesChecked,
			Integer countOtherChecks, Integer durationOfOperationInMinutes,
			Integer countAbsentMembers, Integer operationId,Integer countPlatesRemoved,Integer warningsForProcecution,
			Integer allInOrders, Integer countTeamMembers,
			List<RoadOperationTeamSummaryResults> teamSummaries) {
		super();
		this.operationCreateDate = operationCreateDate;
		this.scheduledEndDateTime = scheduledEndDateTime;
		this.scheduledStartDateTime = scheduledStartDateTime;
		this.actualStartDateTime = actualStartDateTime;
		this.actualEndDateTime = actualEndDateTime;
		this.operationStatus = operationStatus;
		this.operationCategory = operationCategory;
		this.operationName = operationName;
		this.countSummonsIssued = countSummonsIssued;
		this.countWaningNoticesIssued = countWaningNoticesIssued;
		this.countVehiclesSeized = countVehiclesSeized;
		this.countCompliancyActivitiesCommited = countCompliancyActivitiesCommited;
		this.countMotorVehiclesChecked = countMotorVehiclesChecked;
		this.countDrivesLicenceChecked = countDrivesLicenceChecked;
		this.countBadgesChecked = countBadgesChecked;
		this.countCitationChecks = countCitationChecks;
		this.countRoadLicencesChecked = countRoadLicencesChecked;
		this.countOtherChecks = countOtherChecks;
		this.durationOfOperationInMinutes = durationOfOperationInMinutes;
		this.countAbsentMembers = countAbsentMembers;
		this.operationId = operationId;	
		this.countPlatesRemoved = countPlatesRemoved;
		this.warningsForProcecution = warningsForProcecution;
		this.allInOrders = allInOrders;
		this.countTeamMembers = countTeamMembers;
		this.teamSummaries = teamSummaries;
	}
	
	public RoadOperationSummaryResultsBO(Date operationCreateDate,
			Date scheduledEndDateTime, Date scheduledStartDateTime,
			Date actualStartDateTime, Date actualEndDateTime,
			String operationStatus,
			String operationCategory, String operationName,
			Integer countSummonsIssued, Integer countWaningNoticesIssued,
			Integer countVehiclesSeized,
			Integer countCompliancyActivitiesCommited,
			Integer countMotorVehiclesChecked,
			Integer countDrivesLicenceChecked, Integer countBadgesChecked,
			Integer countCitationChecks, Integer countRoadLicencesChecked,
			Integer countOtherChecks, Integer durationOfOperationInMinutes,
			Integer countAbsentMembers, Integer operationId,Integer countPlatesRemoved,Integer warningsForProcecution,
			Integer allInOrders, Integer countTeamMembers,
			List<RoadOperationTeamSummaryResults> teamSummaries,String TAOfficeRegion,String TAOfficeRegionDescription)
	{
		super();
		this.operationCreateDate = operationCreateDate;
		this.scheduledEndDateTime = scheduledEndDateTime;
		this.scheduledStartDateTime = scheduledStartDateTime;
		this.actualStartDateTime = actualStartDateTime;
		this.actualEndDateTime = actualEndDateTime;
		this.operationStatus = operationStatus;
		this.operationCategory = operationCategory;
		this.operationName = operationName;
		this.countSummonsIssued = countSummonsIssued;
		this.countWaningNoticesIssued = countWaningNoticesIssued;
		this.countVehiclesSeized = countVehiclesSeized;
		this.countCompliancyActivitiesCommited = countCompliancyActivitiesCommited;
		this.countMotorVehiclesChecked = countMotorVehiclesChecked;
		this.countDrivesLicenceChecked = countDrivesLicenceChecked;
		this.countBadgesChecked = countBadgesChecked;
		this.countCitationChecks = countCitationChecks;
		this.countRoadLicencesChecked = countRoadLicencesChecked;
		this.countOtherChecks = countOtherChecks;
		this.durationOfOperationInMinutes = durationOfOperationInMinutes;
		this.countAbsentMembers = countAbsentMembers;
		this.operationId = operationId;	
		this.countPlatesRemoved = countPlatesRemoved;
		this.warningsForProcecution = warningsForProcecution;
		this.allInOrders = allInOrders;
		this.countTeamMembers = countTeamMembers;
		this.teamSummaries = teamSummaries;
		this.TAOfficeRegion = TAOfficeRegion;
		this.TAOfficeRegionDescription = TAOfficeRegionDescription;
	}
	
	public RoadOperationSummaryResultsBO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Integer getCountAbsentTAInspectors() {
		return countAbsentTAInspectors;
	}

	public void setCountAbsentTAInspectors(Integer countAbsentTAInspectors) {
		this.countAbsentTAInspectors = countAbsentTAInspectors;
	}

	public Integer getCountAbsentJPs() {
		return countAbsentJPs;
	}

	public void setCountAbsentJPs(Integer countAbsentJPs) {
		this.countAbsentJPs = countAbsentJPs;
	}

	public Integer getCountAbsentPoliceOfficers() {
		return countAbsentPoliceOfficers;
	}

	public void setCountAbsentPoliceOfficers(Integer countAbsentPoliceOfficers) {
		this.countAbsentPoliceOfficers = countAbsentPoliceOfficers;
	}

	public Integer getCountAbsentITAExaminers() {
		return countAbsentITAExaminers;
	}

	public void setCountAbsentITAExaminers(Integer countAbsentITAExaminers) {
		this.countAbsentITAExaminers = countAbsentITAExaminers;
	}

	
	
	
	
	
}
