/**
 * Created By: oanguin
 * Date: May 9, 2013
 *
 */
package fsl.ta.toms.roms.webservices;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import fsl.ta.toms.roms.bo.CourtCasesOpenedReportBO;
import fsl.ta.toms.roms.bo.CourtScheduleReportBO;
import fsl.ta.toms.roms.bo.EventAuditReportBO;
import fsl.ta.toms.roms.bo.RoadOperationSummaryBO;
import fsl.ta.toms.roms.bo.RoadOperationsStatisticsReportBO;
import fsl.ta.toms.roms.bo.SummonsOutstandingReportBO;
import fsl.ta.toms.roms.bo.SummonsReportBO;
import fsl.ta.toms.roms.bo.VehicleSeizedReportBO;
import fsl.ta.toms.roms.bo.WarningNoProsecutionReportBO;
import fsl.ta.toms.roms.exception.NoRecordFoundException;
import fsl.ta.toms.roms.exception.RequiredFieldMissingException;
import fsl.ta.toms.roms.search.criteria.impl.CourtScheduleCriteriaBO;
import fsl.ta.toms.roms.search.criteria.impl.EventAuditReportCriteriaBO;
import fsl.ta.toms.roms.search.criteria.impl.OperationSummaryReportCriteriaBO;
import fsl.ta.toms.roms.search.criteria.impl.PerformanceStatisticsReportCriteriaBO;
import fsl.ta.toms.roms.search.criteria.impl.SummonsOutstandingReportCriteriaBO;
import fsl.ta.toms.roms.search.criteria.impl.SummonsReportCriteriaBO;
import fsl.ta.toms.roms.search.criteria.impl.VehicleSeizedReportCriteriaBO;
import fsl.ta.toms.roms.search.criteria.impl.WarningNoProReportCriteriaBO;
import fsl.ta.toms.roms.service.ReportService;
import fsl.ta.toms.roms.service.ServiceFactory;
import fsl.ta.toms.roms.util.StringUtil;

/**
 * s
 * @author oanguin
 * Created Date: May 9, 2013
 */
public class Report extends SpringBeanAutowiringSupport implements Serializable 
{
	private static final long serialVersionUID = 1L;

	@Autowired
	private ServiceFactory serviceFactory;

	public Report() 
	{
		super();
		
	}
	
	
	public VehicleSeizedReportBO vehicleSeizedReport(VehicleSeizedReportCriteriaBO reportCriteria, String currentUsername, String currentUserRegion) throws RequiredFieldMissingException, NoRecordFoundException
	{
		this.checkRequiredFields(currentUsername, currentUserRegion);
		
		ReportService reportService = this.serviceFactory.getReportService();
		
		VehicleSeizedReportBO report =  reportService.vehicleSeizedReport(reportCriteria, currentUsername, currentUserRegion);
		 
		 if(report == null || report.getResults().isEmpty())
			 throw new NoRecordFoundException();
		 else
			 return report;
	}
	
	public RoadOperationSummaryBO operationSummaryReport(OperationSummaryReportCriteriaBO reportCriteria, String currentUsername, String currentUserRegion) throws RequiredFieldMissingException, NoRecordFoundException
	{
		
		this.checkRequiredFields(currentUsername, currentUserRegion);
		
		ReportService reportService = this.serviceFactory.getReportService();
		
		 RoadOperationSummaryBO  result = reportService.operationSummaryReport(reportCriteria, currentUsername, currentUserRegion);
		
		 if(result == null || result.getResults().isEmpty())
			 throw new NoRecordFoundException();
		 else
			 return result;
	}
	
	public SummonsOutstandingReportBO summonsesNotYetServed(SummonsOutstandingReportCriteriaBO reportCriteria, String currentUsername, String currentUserRegion) throws RequiredFieldMissingException, NoRecordFoundException
	{
		this.checkRequiredFields(currentUsername, currentUserRegion);
		
		ReportService reportService = this.serviceFactory.getReportService();
		
		SummonsOutstandingReportBO report = reportService.summonsOutstandingReport(reportCriteria, currentUsername, currentUserRegion);
		
		if(report == null || report.getResults().isEmpty())
			 throw new NoRecordFoundException();
		 else
			 return report;
	}
	
	public SummonsReportBO summonsReport(SummonsReportCriteriaBO reportCriteria, String currentUsername, String currentUserRegion) throws RequiredFieldMissingException, NoRecordFoundException
	{
		this.checkRequiredFields(currentUsername, currentUserRegion);
		
		ReportService reportService = this.serviceFactory.getReportService();
		
		SummonsReportBO report = reportService.summonsReport(reportCriteria, currentUsername, currentUserRegion);
		
		if(report == null || report.getResults().isEmpty())
			 throw new NoRecordFoundException();
		 else
			 return report;
	}
	
	public CourtScheduleReportBO courtScheduleReport(CourtScheduleCriteriaBO reportCriteria, String currentUsername, String currentUserRegion) throws RequiredFieldMissingException, NoRecordFoundException
	{
		this.checkRequiredFields(currentUsername, currentUserRegion);
		
		ReportService reportService = this.serviceFactory.getReportService();
		
		CourtScheduleReportBO result = reportService.courtScheduleReport(reportCriteria, currentUsername, currentUserRegion);
		
		if(result == null || result.getResults().isEmpty())
			 throw new NoRecordFoundException();
		 else
			 return result;
	}
	
	public RoadOperationsStatisticsReportBO performanceStatisticsReport(PerformanceStatisticsReportCriteriaBO reportCriteria, String currentUsername, String currentUserRegion) throws RequiredFieldMissingException, NoRecordFoundException
	{
		this.checkRequiredFields(currentUsername, currentUserRegion);
		
		ReportService reportService = this.serviceFactory.getReportService();
		
		 RoadOperationsStatisticsReportBO roadOpStats = reportService.performanceStatisticsReport(reportCriteria, currentUsername, currentUserRegion);
		
		 
		 
		if(roadOpStats == null || roadOpStats.getRegionStatistics()== null || roadOpStats.getRegionStatistics().isEmpty())
			 throw new NoRecordFoundException();
		 else
			 return roadOpStats;
	}
	
	
	public EventAuditReportBO eventAuditReport(EventAuditReportCriteriaBO reportCriteria, String currentUsername,String currentUserRegion) throws RequiredFieldMissingException, NoRecordFoundException
	{
		this.checkRequiredFields(currentUsername, currentUserRegion);
		
		ReportService reportService = this.serviceFactory.getReportService();
		
		EventAuditReportBO report = reportService.eventAuditeport(reportCriteria, currentUsername, currentUserRegion);
		
		if(report == null || report.getResults().isEmpty())
			 throw new NoRecordFoundException();
		 else
			 return report;
	}
	

	private void checkRequiredFields(String currentUsername, String currentUserRegion) throws RequiredFieldMissingException
	{
		if( ! StringUtil.isSet(currentUsername) || !  StringUtil.isSet(currentUserRegion))
			throw new RequiredFieldMissingException();
		
		
	}
	
	public CourtCasesOpenedReportBO courtCasesNotClosed(CourtScheduleCriteriaBO reportCriteria, String currentUsername, String currentUserRegion) throws RequiredFieldMissingException, NoRecordFoundException
	{
		this.checkRequiredFields(currentUsername, currentUserRegion);
		
		ReportService reportService = this.serviceFactory.getReportService();
		
		CourtCasesOpenedReportBO result = reportService.courtCasesNotClosed(reportCriteria, currentUsername, currentUserRegion);
		
		if(result == null || result.getCourtCaseList().isEmpty())
			 throw new NoRecordFoundException();
		 else
			 return result;
	}
	
	public WarningNoProsecutionReportBO warningNoticeNoProsecution(WarningNoProReportCriteriaBO reportCriteria,String currentUsername, String currentUserRegion) throws RequiredFieldMissingException, NoRecordFoundException
	{
		this.checkRequiredFields(currentUsername, currentUserRegion);
		
		ReportService reportService = this.serviceFactory.getReportService();
		
		WarningNoProsecutionReportBO result = reportService.warningNoProsecution(reportCriteria, currentUsername, currentUserRegion);
		
		if(result == null || result.getWarningNoProsecutionDetailsList().isEmpty())
			 throw new NoRecordFoundException();
		 else
			 return result;
	}
	
}
