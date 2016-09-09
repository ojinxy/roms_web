/**
 * Created By: oanguin
 * Date: May 8, 2013
 *
 */
package fsl.ta.toms.roms.dao;

import java.util.List;

import fsl.dao.DAO;
import fsl.ta.toms.roms.bo.CourtCasesOpenedReportBO;
import fsl.ta.toms.roms.bo.CourtScheduleReportBO;
import fsl.ta.toms.roms.bo.EventAuditReportBO;
import fsl.ta.toms.roms.bo.RoadOperationSummaryBO;
import fsl.ta.toms.roms.bo.RoadOperationsStatisticsReportBO;
import fsl.ta.toms.roms.bo.SummonsOutstandingReportBO;
import fsl.ta.toms.roms.bo.SummonsOutstandingReportBO;
import fsl.ta.toms.roms.bo.SummonsReportBO;
import fsl.ta.toms.roms.bo.VehicleSeizedReportBO;
import fsl.ta.toms.roms.bo.WarningNoProsecutionReportBO;
import fsl.ta.toms.roms.dao.impl.ReportDisplayInformationDAOImpl;
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
 * Created Date: May 8, 2013
 */
public interface ReportDAO extends DAO 
{
	/**
	 * This function uses the criteria input to search for report data which is returned as a list of vehicle seized report business objects.
	 * @param reportCriteria Contains {Offense Start Date, Offense End Date, TA-Office Region, TA-Staff ID, Offender ID,
	 * 	Pound I, Wrecking Company ID, Road Operation ID}
	 * @param userName - The name of the person running the report.
	 * @param userRegion - The region where the user is running the report from.
	 * @param reportDisplayInformation - This is display information which will be used in the report.
	 * @return List<VehicleSeizedReportBO> This is a list of VehicleSeizedBO which will be used as line items in the report.
	 */
	public VehicleSeizedReportBO vehicleSeizedReport(VehicleSeizedReportCriteriaBO reportCriteria,String userName,String userRegion, ReportDisplayInformationDAOImpl reportDisplayInformation);
	
	/**
	 * This function uses the inputs to create a aggregate summary reports based on operations
	 * @param reportCriteria - This is the filter which will be used in the report.
	 * @param userName - This is the name of the user running the report.
	 * @param reportDisplayInformation - This is display information which will be used in the report.
	 * @return RoadOperationSummaryBO - This is a list of road operation summary business objects.
	 */
	public RoadOperationSummaryBO operationSummaryReport(OperationSummaryReportCriteriaBO reportCriteria, String userName,String userRegion,ReportDisplayInformationDAOImpl reportDisplayInformation);
	
	/**
	 * This function takes arguments pertaining outstanding summons and returns 
	 * a list of SummonsOutstandingReportBO
	 * @param reportCriteria - This is information which will be used to narrow down search results.
	 * @param userName
	 * @param userRegion
	 * @param reportDisplayInformation
	 * @return List <code>SummonsOutstandingReportBO</code> This is a list of outstanding summons that meets the report criteria 
	 */
	public SummonsOutstandingReportBO summonsOutstandingReport(SummonsOutstandingReportCriteriaBO reportCriteria, String userName, String userRegion, ReportDisplayInformationDAOImpl reportDisplayInformation);
	
	/**
	 * This function takes arguments pertaining to court schedules and returns a list of trial details based on the search criteria
	 * @param reportCriteria
	 * @param userName
	 * @param userRegion
	 * @param reportDisplayInformation
	 * @return <code>CourtScheduleReportBO</code> This contains a list of query results and other information for the report.
	 */
	public CourtScheduleReportBO courtScheduleReport(CourtScheduleCriteriaBO reportCriteria, String userName, String userRegion, ReportDisplayInformationDAOImpl reportDisplayInformation);
	
	/**
	 * This function takes arguments pertaining to court case and returns a list of court cases and court appearances details based on the search criteria
	 * @param reportCriteria
	 * @param userName
	 * @param userRegion
	 * @param reportDisplayInformation
	 * @return <code>CourtCasesOpenedReportBO</code> This contains a list of query results and other information for the report.
	 */
	public CourtCasesOpenedReportBO unclosedCourtCasesReport(CourtScheduleCriteriaBO reportCriteria, String userName, String userRegion, ReportDisplayInformationDAOImpl reportDisplayInformation);
	
	/**
	 * This function looks at search criteria and creates a report object with statistical data.
	 * @param reportCriteria - The searching information used to focus query results.
	 * @param userName - Person running the report.
	 * @param userRegion - The region where the report is being run from.
	 * @param reportDisplayInformation - The information which is to be displayed on he report. 
	 * @return RoadOperationsStatisticsReportBO  - Which contains a list of all the return results.
	 */
	public RoadOperationsStatisticsReportBO performanceSaisticsReport(PerformanceStatisticsReportCriteriaBO reportCriteria, String userName, String userRegion, ReportDisplayInformationDAOImpl reportDisplayInformation);
	
	/**
	 * This function looks at search criteria and create a report of event audit details based on the search criteria.
	 * @param reportCriteria
	 * @param userName
	 * @param userRegion
	 * @param reportDisplayInformation
	 * @return EventAuditReportBO - WHich contains details of audit events.
	 */
	public EventAuditReportBO eventAuditeport(EventAuditReportCriteriaBO reportCriteria, String userName, String userRegion, ReportDisplayInformationDAOImpl reportDisplayInformation);
	
	/**
	 * This function return results on summons based on search criteria. This function is generic and looks to display summons details.
	 * @param reportCriteria
	 * @param userName
	 * @param userRegion
	 * @param reportDisplayInformation
	 * @return
	 */
	public SummonsReportBO summonsReport(SummonsReportCriteriaBO reportCriteria, String userName, String userRegion, ReportDisplayInformationDAOImpl reportDisplayInformation);
	
	/**
	 * This function returns warning notice no prosecution details. Based on the criteria warning notice no prosecution items
	 * are filtered from the list.
	 * @param reportCriteria
	 * @param userName
	 * @param userRegion
	 * @param reportDisplayInformation
	 * @return
	 */
	public WarningNoProsecutionReportBO warningNoProsecutionReport(WarningNoProReportCriteriaBO reportCriteria, String userName, String userRegion, ReportDisplayInformationDAOImpl reportDisplayInformation);
}
