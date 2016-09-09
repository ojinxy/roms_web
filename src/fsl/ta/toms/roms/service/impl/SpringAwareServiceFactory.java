package fsl.ta.toms.roms.service.impl;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import fsl.ta.toms.roms.service.ArteryService;
import fsl.ta.toms.roms.service.AuthorizationService;
import fsl.ta.toms.roms.service.BIMSService;
import fsl.ta.toms.roms.service.ConfigurationService;
import fsl.ta.toms.roms.service.CourtAppearanceService;
import fsl.ta.toms.roms.service.CourtCaseService;
import fsl.ta.toms.roms.service.CourtService;
import fsl.ta.toms.roms.service.EventAuditService;
import fsl.ta.toms.roms.service.GoverningLawService;
import fsl.ta.toms.roms.service.HolidayExceptionsService;
import fsl.ta.toms.roms.service.ITAExaminerService;
import fsl.ta.toms.roms.service.JPService;
import fsl.ta.toms.roms.service.LMISService;
import fsl.ta.toms.roms.service.LocationService;
import fsl.ta.toms.roms.service.OffenceService;
import fsl.ta.toms.roms.service.ParishService;
import fsl.ta.toms.roms.service.PersonService;
import fsl.ta.toms.roms.service.PoundService;
import fsl.ta.toms.roms.service.ReasonService;
import fsl.ta.toms.roms.service.RecordingCourtOutcomeService;
import fsl.ta.toms.roms.service.ReferenceCodeService;
import fsl.ta.toms.roms.service.ReportService;
import fsl.ta.toms.roms.service.RoadCompliancyService;
import fsl.ta.toms.roms.service.RoadOperationService;
import fsl.ta.toms.roms.service.ScannedDocService;
import fsl.ta.toms.roms.service.SchedulerService;
import fsl.ta.toms.roms.service.ServiceFactory;
import fsl.ta.toms.roms.service.StaffUserMappingService;
import fsl.ta.toms.roms.service.StrategyService;
import fsl.ta.toms.roms.service.SummonsService;
import fsl.ta.toms.roms.service.TAVehicleService;
import fsl.ta.toms.roms.service.TTMSCodeService;
import fsl.ta.toms.roms.service.TicketService;
import fsl.ta.toms.roms.service.UserService;
import fsl.ta.toms.roms.service.WarningNoProsecutionService;
import fsl.ta.toms.roms.service.WarningNoticeService;
import fsl.ta.toms.roms.service.WreckingCompanyService;

public class SpringAwareServiceFactory implements ServiceFactory,ApplicationContextAware {

	private ApplicationContext applicationContext;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		
		this.applicationContext = applicationContext;

	}

	@Override
	public SchedulerService getSchedulerService() {
		
		return (SchedulerService) applicationContext.getBean("schedulerService");
	}
	
	@Override
	public EventAuditService getEventAuditService(){		
		return (EventAuditService)applicationContext.getBean("eventAuditService");
	}


	@Override
	public ReferenceCodeService getReferenceCodeService()
	{
		return (ReferenceCodeService)applicationContext.getBean("referenceCodeService");
	}


	@Override
	public StaffUserMappingService getStaffUserMappingService() {
		// TODO Auto-generated method stub
		return (StaffUserMappingService) applicationContext.getBean("staffUserMappingService");
	}


	@Override
	public AuthorizationService getAuthorizationService() {
		// TODO Auto-generated method stub
		return (AuthorizationService) applicationContext.getBean("authorizationService");
	}


	@Override
	public RecordingCourtOutcomeService getRecordingCourtOutcomeService() {
		return (RecordingCourtOutcomeService) applicationContext.getBean("recordingCourtOutcomeService");
	}
	
	@Override
	public RoadCompliancyService getRoadCompliancyService() {
		
		return (RoadCompliancyService) applicationContext.getBean("roadCompliancyService");
	}


	@Override
	public RoadOperationService getRoadOperationService() {
		return (RoadOperationService) applicationContext.getBean("roadOperationService");
	}
	
	@Override
	public SummonsService getSummonsService() {
		return (SummonsService) applicationContext.getBean("summonsService");
	}
	
	@Override
	public CourtAppearanceService getCourtAppearanceService() {
		return (CourtAppearanceService) applicationContext.getBean("courtAppearanceService");
	}	

	
	@Override
	public LMISService getLMISService() 
	{
		return (LMISService) applicationContext.getBean("LMISService");
	}


	@Override
	public WarningNoticeService getWarningNoticeService() {
		
		return (WarningNoticeService) applicationContext.getBean("warningNoticeService");
	}


	@Override
	public BIMSService getBIMSService() {
		return (BIMSService) applicationContext.getBean("BIMSService");
	}
	@Override
	public ReportService getReportService() {
		return (ReportService) applicationContext.getBean("reportService");
	}
	
	@Override
	public TicketService getTicketService() {
		return (TicketService) applicationContext.getBean("ticketService");
	}
	
	@Override
	public TTMSCodeService getTTMSCodeService() {
		return (TTMSCodeService) applicationContext.getBean("ttmsCodeService");
	}
		
	@Override
	public ITAExaminerService getItaExaminerService() {
		return (ITAExaminerService) applicationContext.getBean("itaExaminerService");
	}


	@Override
	public JPService getJpService() {
		return (JPService) applicationContext.getBean("jpService");
	}


	@Override
	public WreckingCompanyService getWreckingCompanyService() {
		return (WreckingCompanyService) applicationContext.getBean("wreckingCompanyService");
	}


	@Override
	public PoundService getPoundService() {
		return (PoundService) applicationContext.getBean("poundService");
	}


	@Override
	public ParishService getParishService() {
		return (ParishService) applicationContext.getBean("parishService");
	}


	@Override
	public TAVehicleService getTaVehicleService() {
		return  (TAVehicleService) applicationContext.getBean("taVehicleService");
	}

	@Override
	public ArteryService getArteryService() {
		return  (ArteryService) applicationContext.getBean("arteryService");
	}

	@Override
	public GoverningLawService getGoverningLawService() {
		return  (GoverningLawService) applicationContext.getBean("governingLawService");
	}

	@Override
	public LocationService getLocationService() {
		return  (LocationService) applicationContext.getBean("locationService");
	}

	@Override
	public ConfigurationService getConfigurationService() {
		return  (ConfigurationService) applicationContext.getBean("configurationService");
	}

	@Override
	public CourtService getCourtService() {
		return  (CourtService) applicationContext.getBean("courtService");
	}

	@Override
	public OffenceService getOffenceService() {
		return  (OffenceService) applicationContext.getBean("offenceService");
	}

	@Override
	public ReasonService getReasonService() {
		return (ReasonService) applicationContext.getBean("reasonService");
	}

	@Override
	public StrategyService getStrategyService() {
		return (StrategyService) applicationContext.getBean("strategyService");
	}
	
	@Override
	public ScannedDocService getScannedDocService() {
		return (ScannedDocService) applicationContext.getBean("scannedDocService");
	}
	
	
	@Override
	public CourtCaseService getCourtCaseService() {
		
		return (CourtCaseService)applicationContext.getBean("courtCaseService");
	}
	
	@Override
	public HolidayExceptionsService getHolidayExceptionsService() {
		return (HolidayExceptionsService) applicationContext.getBean("holidayExceptionsService");
	}

	@Override
	public WarningNoProsecutionService getWarningNoProsecutionService() 
	{
		return (WarningNoProsecutionService) applicationContext.getBean("warningNoProsecutionService");
	}

	@Override
	public UserService getUserService() {
		return (UserService) applicationContext.getBean("userService");
	}

	@Override
	public PersonService getPersonService() {
		return (PersonService) applicationContext.getBean("personService");
	}
}
