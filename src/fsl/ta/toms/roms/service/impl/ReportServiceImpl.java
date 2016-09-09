/**
 * Created By: oanguin
 * Date: May 9, 2013
 *
 */
package fsl.ta.toms.roms.service.impl;

import fsl.ta.toms.roms.bo.CourtCasesOpenedReportBO;
import fsl.ta.toms.roms.bo.CourtScheduleReportBO;
import fsl.ta.toms.roms.bo.EventAuditReportBO;
import fsl.ta.toms.roms.bo.RoadOperationSummaryBO;
import fsl.ta.toms.roms.bo.RoadOperationsStatisticsReportBO;
import fsl.ta.toms.roms.bo.SummonsOutstandingReportBO;
import fsl.ta.toms.roms.bo.SummonsReportBO;
import fsl.ta.toms.roms.bo.VehicleSeizedReportBO;
import fsl.ta.toms.roms.bo.WarningNoProsecutionReportBO;
import fsl.ta.toms.roms.dao.DAOFactory;
import fsl.ta.toms.roms.dao.ReportDAO;
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

/**
 * @author oanguin
 * Created Date: May 9, 2013
 * 
 */
public class ReportServiceImpl implements ReportService {

	private DAOFactory daoFactory;
	
	public void setDaoFactory(DAOFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public VehicleSeizedReportBO vehicleSeizedReport(
			VehicleSeizedReportCriteriaBO reportCriteria, String userName,
			String userRegion) throws RequiredFieldMissingException {
		
		
		if(reportCriteria.getOffenceStartDate() == null || reportCriteria.getOffenceEndDate() == null || (userName == null || userName.isEmpty()))
			throw new RequiredFieldMissingException();
		
		
		ReportDAO reportDAO = daoFactory.getReportDAO();
		
		return reportDAO.vehicleSeizedReport(reportCriteria, userName, userRegion, daoFactory.getReportDisplayInformation());
	}

	@Override
	public RoadOperationSummaryBO operationSummaryReport(
			OperationSummaryReportCriteriaBO reportCriteria, String userName,
			String userRegion) throws RequiredFieldMissingException 
	{
		
		if( (userName == null || userName.isEmpty()))
			throw new RequiredFieldMissingException();

		
		ReportDAO reportDAO = daoFactory.getReportDAO();
		
		
		return reportDAO.operationSummaryReport(reportCriteria, userName, userRegion, daoFactory.getReportDisplayInformation());
	}

	@Override
	public SummonsOutstandingReportBO summonsOutstandingReport(
			SummonsOutstandingReportCriteriaBO reportCriteria, String userName,
			String userRegion) throws RequiredFieldMissingException {
		
		if(reportCriteria.getOperationStartDate() == null || reportCriteria.getOperationEndDate() == null || (userName == null || userName.isEmpty()))
			throw new RequiredFieldMissingException();
		
		ReportDAO reportDAO = daoFactory.getReportDAO();
		
		return reportDAO.summonsOutstandingReport(reportCriteria, userName, userRegion, daoFactory.getReportDisplayInformation());
	}

	@Override
	public CourtScheduleReportBO courtScheduleReport(
			CourtScheduleCriteriaBO reportCriteria, String userName,
			String userRegion) throws RequiredFieldMissingException 
	{
		if(reportCriteria.getTrialStartDate() == null || reportCriteria.getTrialEndDate() == null || (userName == null || userName.isEmpty()))
			throw new RequiredFieldMissingException();
		
		ReportDAO reportDAO = daoFactory.getReportDAO();
		
		return reportDAO.courtScheduleReport(reportCriteria, userName, userRegion, daoFactory.getReportDisplayInformation());
	}

	@Override
	public RoadOperationsStatisticsReportBO performanceStatisticsReport(
			PerformanceStatisticsReportCriteriaBO reportCriteria,
			String userName, String userRegion) throws RequiredFieldMissingException
	{
		if(reportCriteria.getEndDate() == null || reportCriteria.getStartDate() == null)
			throw new RequiredFieldMissingException();
		
		if(userName == null || userName.isEmpty())
			throw new RequiredFieldMissingException();
		
		
		ReportDAO reportDAO = daoFactory.getReportDAO();
		
		return reportDAO.performanceSaisticsReport(reportCriteria, userName, userRegion, daoFactory.getReportDisplayInformation());
		
		
		
	}

	@Override
	public EventAuditReportBO eventAuditeport(
			EventAuditReportCriteriaBO reportCriteria, String userName,
			String userRegion) throws RequiredFieldMissingException 
	{
		if(reportCriteria.getEndDate() == null || reportCriteria.getStartDate() == null)
			throw new RequiredFieldMissingException();
		
		if(userName == null || userName.isEmpty())
			throw new RequiredFieldMissingException();
		
		ReportDAO reportDAO = daoFactory.getReportDAO();
		
		return reportDAO.eventAuditeport(reportCriteria, userName, userRegion, daoFactory.getReportDisplayInformation());
	}

	@Override
	public CourtCasesOpenedReportBO courtCasesNotClosed(
			CourtScheduleCriteriaBO reportCriteria, String userName,
			String userRegion) throws RequiredFieldMissingException 
	{
		if(reportCriteria.getTrialStartDate() == null || reportCriteria.getTrialEndDate() == null || (userName == null || userName.isEmpty()))
			throw new RequiredFieldMissingException();
		
		ReportDAO reportDAO = daoFactory.getReportDAO();
		
		return reportDAO.unclosedCourtCasesReport(reportCriteria, userName, userRegion, daoFactory.getReportDisplayInformation());
	}

	@Override
	public SummonsReportBO summonsReport(
			SummonsReportCriteriaBO reportCriteria, String userName,
			String userRegion) throws RequiredFieldMissingException 
	{
		if((userName == null || userName.isEmpty()))
		{
			throw new RequiredFieldMissingException();
		}
		
		if(reportCriteria.getOperationStartDate() == null || reportCriteria.getOperationEndDate() == null)
		{
			if(reportCriteria.getPrintStartDate() == null || reportCriteria.getPrintEndDate() == null)
			{
				if(reportCriteria.getIssuedStartDate() == null || reportCriteria.getIssuedEndDate() == null)
				{
					if(reportCriteria.getOffenceStartDate() == null || reportCriteria.getOffenceEndDate() == null)
					{
						throw new RequiredFieldMissingException();
					}
				}
			}
		}
			
		
		ReportDAO reportDAO = daoFactory.getReportDAO();
		
		return reportDAO.summonsReport(reportCriteria, userName, userRegion, daoFactory.getReportDisplayInformation());
	}

	@Override
	public WarningNoProsecutionReportBO warningNoProsecution(
			WarningNoProReportCriteriaBO reportCriteria, String userName,
			String userRegion) throws RequiredFieldMissingException 
	{
		if((userName == null || userName.isEmpty()))
		{
			throw new RequiredFieldMissingException();
		}
		
		if(reportCriteria.getOperationStartDate() == null || reportCriteria.getOperationEndDate() == null)
		{
			if(reportCriteria.getIssuedStartDate() == null || reportCriteria.getIssuedEndDate() == null)
			{
				if(reportCriteria.getPrintStartDate() == null || reportCriteria.getPrintEndDate() == null)
				{
					throw new RequiredFieldMissingException();
				}
			}
		}
		
		ReportDAO reportDAO = this.daoFactory.getReportDAO();
		
		return reportDAO.warningNoProsecutionReport(reportCriteria, userName, userRegion, daoFactory.getReportDisplayInformation());
	}
	


}
