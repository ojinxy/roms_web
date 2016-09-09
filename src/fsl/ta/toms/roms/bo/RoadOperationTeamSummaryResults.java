/**
 * Created By: oanguin
 * Date: Sep 16, 2013
 *
 */
package fsl.ta.toms.roms.bo;

import java.io.Serializable;

/**
 * @author oanguin
 * Created Date: Sep 16, 2013
 */
public class RoadOperationTeamSummaryResults implements Serializable 
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	String teamName;
	String teamLeadName;
	
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
	countAbsentMembers,
	countTeamMembers,
	operationId,
	teamId;

	Integer countPlatesRemoved;
	
	Integer warningsForProcecution;
	
	Integer allInOrders;

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
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


	public Integer getCountAbsentMembers() {
		return countAbsentMembers;
	}

	public void setCountAbsentMembers(Integer countAbsentMembers) {
		this.countAbsentMembers = countAbsentMembers;
	}

	public Integer getCountTeamMembers() {
		return countTeamMembers;
	}

	public void setCountTeamMembers(Integer countTeamMembers) {
		this.countTeamMembers = countTeamMembers;
	}

	public Integer getOperationId() {
		return operationId;
	}

	public void setOperationId(Integer operationId) {
		this.operationId = operationId;
	}

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

	
	public String getTeamLeadName() {
		return teamLeadName;
	}

	public void setTeamLeadName(String teamLeadName) {
		this.teamLeadName = teamLeadName;
	}



	public Integer getTeamId() {
		return teamId;
	}

	public void setTeamId(Integer teamId) {
		this.teamId = teamId;
	}

	public RoadOperationTeamSummaryResults(String teamName,
			String teamLeadName, Integer countSummonsIssued,
			Integer countWaningNoticesIssued, Integer countVehiclesSeized,
			Integer countCompliancyActivitiesCommited,
			Integer countMotorVehiclesChecked,
			Integer countDrivesLicenceChecked, Integer countBadgesChecked,
			Integer countCitationChecks, Integer countRoadLicencesChecked,
			Integer countOtherChecks,
			Integer countAbsentMembers, Integer countTeamMembers,
			Integer operationId, Integer countPlatesRemoved,
			Integer warningsForProcecution, Integer allInOrders, Integer teamId) {
		super();
		this.teamName = teamName;
		this.teamLeadName = teamLeadName;
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
		this.countAbsentMembers = countAbsentMembers;
		this.countTeamMembers = countTeamMembers;
		this.operationId = operationId;
		this.countPlatesRemoved = countPlatesRemoved;
		this.warningsForProcecution = warningsForProcecution;
		this.allInOrders = allInOrders;
		this.teamId = teamId;
	}

	public RoadOperationTeamSummaryResults() {
		super();
		
	}

	
}
