package fsl.ta.toms.roms.service;


/**
 * Defines a method for retrieving all Services required by the application.
 * A central repository for accessing Services.
 *
 */
public interface ServiceFactory {
	
	public SchedulerService getSchedulerService();
	
	public EventAuditService getEventAuditService();
	
	public RoadCompliancyService getRoadCompliancyService();
	
	public ReferenceCodeService getReferenceCodeService();

	public StaffUserMappingService getStaffUserMappingService(); 
	
	public AuthorizationService getAuthorizationService();
	
	public RecordingCourtOutcomeService getRecordingCourtOutcomeService();
	
	public RoadOperationService getRoadOperationService();
	
	public SummonsService getSummonsService();
	
	public LMISService getLMISService();

	public CourtAppearanceService getCourtAppearanceService();
	
	public CourtCaseService getCourtCaseService();
	
	public BIMSService getBIMSService();
	
	public ReportService getReportService();

	public WarningNoticeService getWarningNoticeService();
	
	public WarningNoProsecutionService getWarningNoProsecutionService();
	
	public TicketService getTicketService();
	
	public TTMSCodeService getTTMSCodeService();

	public ITAExaminerService getItaExaminerService();
	
	public JPService getJpService();
	
	//reference codes
	public WreckingCompanyService getWreckingCompanyService();
	
	public PoundService getPoundService();
	
	public ParishService getParishService();
	
	public ArteryService getArteryService();
	
	public GoverningLawService getGoverningLawService();
	
	public LocationService getLocationService();

	public ConfigurationService getConfigurationService();
	
	public CourtService getCourtService();
		
	public TAVehicleService getTaVehicleService();
	
	public OffenceService getOffenceService();
	
	public ReasonService getReasonService();
	
	public StrategyService getStrategyService();

	public ScannedDocService getScannedDocService();
	
	public HolidayExceptionsService getHolidayExceptionsService();
	
	public UserService getUserService();
	
	public PersonService getPersonService();
}
