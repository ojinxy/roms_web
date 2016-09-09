/**
 * Created By: oanguin
 * Date: May 10, 2013
 *
 */
package fsl.ta.toms.roms.dao.impl;


import org.springframework.beans.factory.annotation.Autowired;


/**
 * @author oanguin
 * Created Date: May 10, 2013
 */
public class ReportDisplayInformationDAOImpl 
{
	
	public String applicationName;

	
	public String vehicleSeizedReportTitle;
	
	public String roadOperationSummaryTitle;
	
	public String summonsOutstandingReportTitle;
	
	public String courtScheduleReportTitle;
	
	public String performanceStatisticsReportTitle;
	
	public String eventAuditReportTitle;
	
	public String courtCasesOpenedReportTitle;
	
	public String summonsReportTitle;
	
	public String warningNoticeNoProsecutionReportTitle;
	
	public String getSummonsReportTitle() {
		return summonsReportTitle;
	}

	public void setSummonsReportTitle(String summonsReportTitle) {
		this.summonsReportTitle = summonsReportTitle;
	}

	public String getCourtCasesOpenedReportTitle() {
		return courtCasesOpenedReportTitle;
	}

	public void setCourtCasesOpenedReportTitle(String courtCasesOpenedReportTitle) {
		this.courtCasesOpenedReportTitle = courtCasesOpenedReportTitle;
	}

	public String getEventAuditReportTitle() {
		return eventAuditReportTitle;
	}

	@Autowired
	public void setEventAuditReportTitle(String eventAuditReportTitle) {
		this.eventAuditReportTitle = eventAuditReportTitle;
	}

	public String getPerformanceStatisticsReportTitle() {
		return performanceStatisticsReportTitle;
	}
	
	@Autowired
	public void setPerformanceStatisticsReportTitle(
			String performanceStatisticsReportTitle) {
		this.performanceStatisticsReportTitle = performanceStatisticsReportTitle;
	}

	public String getCourtScheduleReportTitle() {
		return courtScheduleReportTitle;
	}
	
	@Autowired
	public void setCourtScheduleReportTitle(String courtScheduleReportTitle) {
		this.courtScheduleReportTitle = courtScheduleReportTitle;
	}

	public String getSummonsOutstandingReportTitle() {
		return summonsOutstandingReportTitle;
	}

	@Autowired
	public void setSummonsOutstandingReportTitle(
			String summonsOutstandingReportTitle) {
		this.summonsOutstandingReportTitle = summonsOutstandingReportTitle;
	}

	public String getApplicationName() {
		return applicationName;
	}

	@Autowired
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	public String getVehicleSeizedReportTitle() {
		return vehicleSeizedReportTitle;
	}

	@Autowired
	public void setVehicleSeizedReportTitle(String vehicleSeizedReportTitle) {
		this.vehicleSeizedReportTitle = vehicleSeizedReportTitle;
	}

	public String getRoadOperationSummaryTitle() {
		return roadOperationSummaryTitle;
	}

	public void setRoadOperationSummaryTitle(String roadOperationSummaryTitle) {
		this.roadOperationSummaryTitle = roadOperationSummaryTitle;
	}

	public String getWarningNoticeNoProsecutionReportTitle() {
		return warningNoticeNoProsecutionReportTitle;
	}

	public void setWarningNoticeNoProsecutionReportTitle(
			String warningNoticeNoProsecutionReportTitle) {
		this.warningNoticeNoProsecutionReportTitle = warningNoticeNoProsecutionReportTitle;
	}
	
	
	
}
