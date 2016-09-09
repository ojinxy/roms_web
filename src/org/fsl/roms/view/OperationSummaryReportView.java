package org.fsl.roms.view;

import java.util.List;

import fsl.ta.toms.roms.bo.RoadOperationTeamSummaryResults;

public class OperationSummaryReportView {

	private String actualEndDateTime;
    private String actualStartDateTime;
    private Integer allInOrders;
    private Integer countAbsentMembers;
    private Integer countBadgesChecked;
    private Integer countCitationChecks;
    private Integer countCompliancyActivitiesCommited;
    private Integer countDrivesLicenceChecked;
    private Integer countMotorVehiclesChecked;
    private Integer countOtherChecks;
    private Integer countPlatesRemoved;
    private Integer countRoadLicencesChecked;
    private Integer countSummonsIssued;
    private Integer countTeamMembers;
    private Integer countVehiclesSeized;
    private Integer countWaningNoticesIssued;
    private Integer durationOfOperationInMinutes;
    private String operationCategory;
    private String operationCreateDate;
    private Integer operationId;
    private String operationName;
    private String operationStatus;
    private String scheduledEndDateTime;
    private String scheduledStartDateTime;
    private String TAOfficeRegion;
	private String TAOfficeRegionDescription;
    private List<RoadOperationTeamSummaryResults> teamSummaries;
    private Integer warningsForProcecution;
    
    private Integer countJPs;
    private Integer countITAExaminers;
    private Integer countTAInspectors;
    private Integer countPoliceOfficers;
    private Integer countWarningNoProsecutions;
    
	//UR-057
    private Integer countAbsentJP;
    private Integer countAbsentITA;
    private Integer countAbsentTAStaff;
    private Integer countAbsentPolice;
    
    
    public String getActualEndDateTime() {
		return actualEndDateTime;
	}
	public void setActualEndDateTime(String actualEndDateTime) {
		this.actualEndDateTime = actualEndDateTime;
	}
	public String getActualStartDateTime() {
		return actualStartDateTime;
	}
	public void setActualStartDateTime(String actualStartDateTime) {
		this.actualStartDateTime = actualStartDateTime;
	}
	public Integer getAllInOrders() {
		return allInOrders;
	}
	public void setAllInOrders(Integer allInOrders) {
		this.allInOrders = allInOrders;
	}
	public Integer getCountAbsentMembers() {
		return countAbsentMembers;
	}
	public void setCountAbsentMembers(Integer countAbsentMembers) {
		this.countAbsentMembers = countAbsentMembers;
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
	public Integer getCountCompliancyActivitiesCommited() {
		return countCompliancyActivitiesCommited;
	}
	public void setCountCompliancyActivitiesCommited(
			Integer countCompliancyActivitiesCommited) {
		this.countCompliancyActivitiesCommited = countCompliancyActivitiesCommited;
	}
	public Integer getCountDrivesLicenceChecked() {
		return countDrivesLicenceChecked;
	}
	public void setCountDrivesLicenceChecked(Integer countDrivesLicenceChecked) {
		this.countDrivesLicenceChecked = countDrivesLicenceChecked;
	}
	public Integer getCountMotorVehiclesChecked() {
		return countMotorVehiclesChecked;
	}
	public void setCountMotorVehiclesChecked(Integer countMotorVehiclesChecked) {
		this.countMotorVehiclesChecked = countMotorVehiclesChecked;
	}
	public Integer getCountOtherChecks() {
		return countOtherChecks;
	}
	public void setCountOtherChecks(Integer countOtherChecks) {
		this.countOtherChecks = countOtherChecks;
	}
	public Integer getCountPlatesRemoved() {
		return countPlatesRemoved;
	}
	public void setCountPlatesRemoved(Integer countPlatesRemoved) {
		this.countPlatesRemoved = countPlatesRemoved;
	}
	public Integer getCountRoadLicencesChecked() {
		return countRoadLicencesChecked;
	}
	public void setCountRoadLicencesChecked(Integer countRoadLicencesChecked) {
		this.countRoadLicencesChecked = countRoadLicencesChecked;
	}
	public Integer getCountSummonsIssued() {
		return countSummonsIssued;
	}
	public void setCountSummonsIssued(Integer countSummonsIssued) {
		this.countSummonsIssued = countSummonsIssued;
	}
	public Integer getCountTeamMembers() {
		return countTeamMembers;
	}
	public void setCountTeamMembers(Integer countTeamMembers) {
		this.countTeamMembers = countTeamMembers;
	}
	public Integer getCountVehiclesSeized() {
		return countVehiclesSeized;
	}
	public void setCountVehiclesSeized(Integer countVehiclesSeized) {
		this.countVehiclesSeized = countVehiclesSeized;
	}
	public Integer getCountWaningNoticesIssued() {
		return countWaningNoticesIssued;
	}
	public void setCountWaningNoticesIssued(Integer countWaningNoticesIssued) {
		this.countWaningNoticesIssued = countWaningNoticesIssued;
	}
	public Integer getDurationOfOperationInMinutes() {
		return durationOfOperationInMinutes;
	}
	public void setDurationOfOperationInMinutes(Integer durationOfOperationInMinutes) {
		this.durationOfOperationInMinutes = durationOfOperationInMinutes;
	}
	public String getOperationCategory() {
		return operationCategory;
	}
	public void setOperationCategory(String operationCategory) {
		this.operationCategory = operationCategory;
	}
	public String getOperationCreateDate() {
		return operationCreateDate;
	}
	public void setOperationCreateDate(String operationCreateDate) {
		this.operationCreateDate = operationCreateDate;
	}
	public Integer getOperationId() {
		return operationId;
	}
	public void setOperationId(Integer operationId) {
		this.operationId = operationId;
	}
	public String getOperationName() {
		return operationName;
	}
	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}
	public String getOperationStatus() {
		return operationStatus;
	}
	public void setOperationStatus(String operationStatus) {
		this.operationStatus = operationStatus;
	}
	public String getScheduledEndDateTime() {
		return scheduledEndDateTime;
	}
	public void setScheduledEndDateTime(String scheduledEndDateTime) {
		this.scheduledEndDateTime = scheduledEndDateTime;
	}
	public String getScheduledStartDateTime() {
		return scheduledStartDateTime;
	}
	public void setScheduledStartDateTime(String scheduledStartDateTime) {
		this.scheduledStartDateTime = scheduledStartDateTime;
	}
	public List<RoadOperationTeamSummaryResults> getTeamSummaries() {
		return teamSummaries;
	}
	public void setTeamSummaries(List<RoadOperationTeamSummaryResults> teamSummaries) {
		this.teamSummaries = teamSummaries;
	}
	public Integer getWarningsForProcecution() {
		return warningsForProcecution;
	}
	public void setWarningsForProcecution(Integer warningsForProcecution) {
		this.warningsForProcecution = warningsForProcecution;
	}
	public Integer getCountJPs() {
		return countJPs;
	}
	public void setCountJPs(Integer countJPs) {
		this.countJPs = countJPs;
	}
	public Integer getCountITAExaminers() {
		return countITAExaminers;
	}
	public void setCountITAExaminers(Integer countITAExaminers) {
		this.countITAExaminers = countITAExaminers;
	}
	public Integer getCountTAInspectors() {
		return countTAInspectors;
	}
	public void setCountTAInspectors(Integer countTAInspectors) {
		this.countTAInspectors = countTAInspectors;
	}
	public Integer getCountPoliceOfficers() {
		return countPoliceOfficers;
	}
	public void setCountPoliceOfficers(Integer countPoliceOfficers) {
		this.countPoliceOfficers = countPoliceOfficers;
	}
	public Integer getCountWarningNoProsecutions() {
		return countWarningNoProsecutions;
	}
	public void setCountWarningNoProsecutions(Integer countWarningNoProsecutions) {
		this.countWarningNoProsecutions = countWarningNoProsecutions;
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
	public Integer getCountAbsentJP() {
		return countAbsentJP;
	}
	public void setCountAbsentJP(Integer countAbsentJP) {
		this.countAbsentJP = countAbsentJP;
	}
	public Integer getCountAbsentITA() {
		return countAbsentITA;
	}
	public void setCountAbsentITA(Integer countAbsentITA) {
		this.countAbsentITA = countAbsentITA;
	}
	public Integer getCountAbsentTAStaff() {
		return countAbsentTAStaff;
	}
	public void setCountAbsentTAStaff(Integer countAbsentTAStaff) {
		this.countAbsentTAStaff = countAbsentTAStaff;
	}
	public Integer getCountAbsentPolice() {
		return countAbsentPolice;
	}
	public void setCountAbsentPolice(Integer countAbsentPolice) {
		this.countAbsentPolice = countAbsentPolice;
	}
	
	
}
