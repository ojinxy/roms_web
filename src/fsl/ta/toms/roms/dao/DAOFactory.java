package fsl.ta.toms.roms.dao;

import fsl.ta.toms.roms.dao.impl.ReportDisplayInformationDAOImpl;



public interface DAOFactory {
			
	public SchedulerDAO getSchedulerDAO();
	
	public EventAuditDAO getEventAuditDAO();
	
	public RoadCompliancyDAO getRoadCompliancyDAO();
	
	public CategoryDAO getCategoryDAO();
	
	public StrategyDAO getStrategyDAO();

	public LocationDAO getLocationDAO();
	
	public ArteryDAO getArteryDAO();

	public TAStaffDAO getTAStaffDAO();

	public JPDAO getJPDAO();
	
	public PoliceOfficerDAO getPoliceOfficerDAO();
	
	public ITAExaminerDAO getITAExaminerDAO();
	
	public StatusDAO getStatusDAO();
	
	public CourtDAO getCourtDAO();
	
	public CourtRulingDAO getCourtRulingDAO();
	
	public PleaDAO getPleaDAO();
	
	public TAVehicleDAO getTAVehicleDAO();
	
	public OutcomeTypeDAO getOutcomeTypeDAO();
	
	public OffenceDAO getOffenceDAO();
	
	public RoadCheckTypeDAO getRoadCheckTypeDAO();
	
	public PersonTypeDAO getPersonTypeDAO();
	
	public EventDAO getEventDAO();
	
	public ConfigurationDAO getConfigurationDAO();

	public GoverningLawDAO getGoverningLawDAO();
	
	public ExcerptParameterMappingDAO getExcerptParameterMappingDAO();
	
	public ParishDAO getParishDAO();
	
	public PoundDAO getPoundDAO();
	
	public WreckingCompanyDAO getWreckingCompanyDAO();
	
	public StaffUserMappingDAO getStaffUserMappingDAO();
	
	public TAOfficeLocationDAO getTAOfficeLocationDAO();
	
	public AuthorizationDAO getAuthorizationDAO();
	
	public VerdictDAO getVerdictDAO();
	
	public CourtAppearanceDAO getCourtAppearanceDAO();
	
	public CourtCaseDAO getCourtCaseDAO();

	public RoadOperationDAO getRoadOperationDAO();
	
	public RoadLicenceDAO getRoadLicenceDAO();

	public SummonsDAO getSummonsDAO();

	public WarningNoticeDAO getWarningNoticeDAO();
	
	public WarningNoProsecutionDAO getWarningNoProsecutionDAO();
	
	public BadgeDAO getBadgeDAO();
	
	public ReportDAO getReportDAO();
	
	public ReportDisplayInformationDAOImpl getReportDisplayInformation();
	
	public ReasonDAO getReasonDAO();

	public ScannedDocDAO getScannedDocDAO();
	
	public HolidayExceptionsDAO getHolidayExceptionsDAO();
	
	public UserDAO getUserDAO();
	
	public ScannedDocumentTypeDAO getScannedDocumentTypeDAO();
	
	public PersonDAO getPersonDAO();
	
	public OtherRoleDAO getOtherRoleDAO();
	
}
