/**
 * Created By: oanguin
 * Date: May 9, 2013
 *
 */
package fsl.ta.toms.roms.service;

import java.util.List;

import fsl.ta.toms.roms.bo.CourtCasesOpenedReportBO;
import fsl.ta.toms.roms.bo.CourtScheduleReportBO;
import fsl.ta.toms.roms.bo.EventAuditReportBO;
import fsl.ta.toms.roms.bo.RoadOperationSummaryBO;
import fsl.ta.toms.roms.bo.RoadOperationsStatisticsReportBO;
import fsl.ta.toms.roms.bo.SummonsOutstandingReportBO;
import fsl.ta.toms.roms.bo.SummonsReportBO;
import fsl.ta.toms.roms.bo.VehicleSeizedReportBO;
import fsl.ta.toms.roms.bo.WarningNoProsecutionReportBO;
import fsl.ta.toms.roms.exception.RequiredFieldMissingException;
import fsl.ta.toms.roms.search.criteria.impl.CourtScheduleCriteriaBO;
import fsl.ta.toms.roms.search.criteria.impl.EventAuditReportCriteriaBO;
import fsl.ta.toms.roms.search.criteria.impl.OperationSummaryReportCriteriaBO;
import fsl.ta.toms.roms.search.criteria.impl.PerformanceStatisticsReportCriteriaBO;
import fsl.ta.toms.roms.search.criteria.impl.SummonsOutstandingReportCriteriaBO;
import fsl.ta.toms.roms.search.criteria.impl.SummonsReportCriteriaBO;
import fsl.ta.toms.roms.search.criteria.impl.VehicleSeizedReportCriteriaBO;
import fsl.ta.toms.roms.search.criteria.impl.WarningNoProReportCriteriaBO;

/**
 * @author oanguin
 * Created Date: May 9, 2013
 */
public interface ReportService 
{
	public VehicleSeizedReportBO vehicleSeizedReport(VehicleSeizedReportCriteriaBO reportCriteria, String userName, String userRegion) throws RequiredFieldMissingException;
	
	public RoadOperationSummaryBO operationSummaryReport(OperationSummaryReportCriteriaBO reportCriteria, String userName, String userRegion) throws RequiredFieldMissingException;
	
	public SummonsOutstandingReportBO summonsOutstandingReport(SummonsOutstandingReportCriteriaBO reportCriteria, String userName, String userRegion) throws RequiredFieldMissingException;
	
	public SummonsReportBO summonsReport(SummonsReportCriteriaBO reportCriteria, String userName,String userRegion) throws RequiredFieldMissingException;
	
	public CourtScheduleReportBO courtScheduleReport(CourtScheduleCriteriaBO reportCriteria, String userName, String userRegion) throws RequiredFieldMissingException;
	
	public RoadOperationsStatisticsReportBO performanceStatisticsReport(PerformanceStatisticsReportCriteriaBO reportCriteria,
			String userName, String userRegion)  throws RequiredFieldMissingException;
	
	public EventAuditReportBO eventAuditeport(EventAuditReportCriteriaBO reportCriteria, String userName,String userRegion) throws RequiredFieldMissingException;
	
	public CourtCasesOpenedReportBO courtCasesNotClosed(CourtScheduleCriteriaBO reportCriteria, String userName, String userRegion) throws RequiredFieldMissingException;
	
	public WarningNoProsecutionReportBO warningNoProsecution(WarningNoProReportCriteriaBO reportCriteria,String userName, String userRegion) throws RequiredFieldMissingException;
}