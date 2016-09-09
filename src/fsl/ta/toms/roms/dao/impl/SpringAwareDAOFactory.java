package fsl.ta.toms.roms.dao.impl;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import fsl.ta.toms.roms.dao.ArteryDAO;
import fsl.ta.toms.roms.dao.AuthorizationDAO;
import fsl.ta.toms.roms.dao.BadgeDAO;
import fsl.ta.toms.roms.dao.CategoryDAO;
import fsl.ta.toms.roms.dao.ConfigurationDAO;
import fsl.ta.toms.roms.dao.CourtCaseDAO;
import fsl.ta.toms.roms.dao.CourtDAO;
import fsl.ta.toms.roms.dao.CourtRulingDAO;
import fsl.ta.toms.roms.dao.DAOFactory;
import fsl.ta.toms.roms.dao.EventAuditDAO;
import fsl.ta.toms.roms.dao.EventDAO;
import fsl.ta.toms.roms.dao.ExcerptParameterMappingDAO;
import fsl.ta.toms.roms.dao.GoverningLawDAO;
import fsl.ta.toms.roms.dao.HolidayExceptionsDAO;
import fsl.ta.toms.roms.dao.ITAExaminerDAO;
import fsl.ta.toms.roms.dao.JPDAO;
import fsl.ta.toms.roms.dao.LocationDAO;
import fsl.ta.toms.roms.dao.OffenceDAO;
import fsl.ta.toms.roms.dao.OtherRoleDAO;
import fsl.ta.toms.roms.dao.OutcomeTypeDAO;
import fsl.ta.toms.roms.dao.ParishDAO;
import fsl.ta.toms.roms.dao.PersonDAO;
import fsl.ta.toms.roms.dao.PersonTypeDAO;
import fsl.ta.toms.roms.dao.PleaDAO;
import fsl.ta.toms.roms.dao.PoliceOfficerDAO;
import fsl.ta.toms.roms.dao.PoundDAO;
import fsl.ta.toms.roms.dao.ReasonDAO;
import fsl.ta.toms.roms.dao.ReportDAO;
import fsl.ta.toms.roms.dao.RoadCheckTypeDAO;
import fsl.ta.toms.roms.dao.RoadCompliancyDAO;
import fsl.ta.toms.roms.dao.RoadLicenceDAO;
import fsl.ta.toms.roms.dao.RoadOperationDAO;
import fsl.ta.toms.roms.dao.ScannedDocDAO;
import fsl.ta.toms.roms.dao.ScannedDocumentTypeDAO;
import fsl.ta.toms.roms.dao.SchedulerDAO;
import fsl.ta.toms.roms.dao.StaffUserMappingDAO;
import fsl.ta.toms.roms.dao.StatusDAO;
import fsl.ta.toms.roms.dao.StrategyDAO;
import fsl.ta.toms.roms.dao.SummonsDAO;
import fsl.ta.toms.roms.dao.TAOfficeLocationDAO;
import fsl.ta.toms.roms.dao.TAStaffDAO;
import fsl.ta.toms.roms.dao.TAVehicleDAO;
import fsl.ta.toms.roms.dao.CourtAppearanceDAO;
import fsl.ta.toms.roms.dao.UserDAO;
import fsl.ta.toms.roms.dao.VerdictDAO;
import fsl.ta.toms.roms.dao.WarningNoProsecutionDAO;
import fsl.ta.toms.roms.dao.WarningNoticeDAO;
import fsl.ta.toms.roms.dao.WreckingCompanyDAO;

/**
 * Implementation of the <code>fsl.toms.roms.dao.DAOFactory</code> that also
 * has access to the spring application context that runs it. It 'looks up'
 * DAOs defined in the application's spring configuration using the <code>ApplicationContext.getBean()</code>
 * method.
 *-
 */
public final class SpringAwareDAOFactory implements DAOFactory,ApplicationContextAware {

	
	private ApplicationContext applicationContext;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}

	
	@Override
	public SchedulerDAO getSchedulerDAO() {
		
		return (SchedulerDAO)applicationContext.getBean("schedulerDAO");
	}
	
	@Override
	public EventAuditDAO getEventAuditDAO(){
		return (EventAuditDAO)applicationContext.getBean("eventAuditDAO");
	}


	@Override
	public CategoryDAO getCategoryDAO() {
		
		return (CategoryDAO)applicationContext.getBean("categoryDAO");
	}


	@Override
	public StrategyDAO getStrategyDAO() {
		
		return (StrategyDAO)applicationContext.getBean("strategyDAO");
	}
	
	@Override
	public LocationDAO getLocationDAO() {

		return (LocationDAO)applicationContext.getBean("locationDAO");
	}


	@Override
	public ArteryDAO getArteryDAO() {
		
		return (ArteryDAO)applicationContext.getBean("arteryDAO");
	}


	@Override
	public TAStaffDAO getTAStaffDAO() {
		return (TAStaffDAO)applicationContext.getBean("taStaffDAO");
	}


	@Override
	public JPDAO getJPDAO() {
		return (JPDAO)applicationContext.getBean("JPDAO");
	}


	@Override
	public PoliceOfficerDAO getPoliceOfficerDAO() {
		return (PoliceOfficerDAO)applicationContext.getBean("policeOfficerDAO");
	}


	@Override
	public ITAExaminerDAO getITAExaminerDAO() {
		return (ITAExaminerDAO)applicationContext.getBean("ITAExaminerDAO");
	}


	@Override
	public StatusDAO getStatusDAO() {
		return (StatusDAO)applicationContext.getBean("statusDAO");
	}


	@Override
	public CourtDAO getCourtDAO() {
		return (CourtDAO)applicationContext.getBean("courtDAO");
	}


	@Override
	public CourtRulingDAO getCourtRulingDAO() {
		return (CourtRulingDAO)applicationContext.getBean("courtRulingDAO");
	}


	@Override
	public PleaDAO getPleaDAO() 
	{
		return (PleaDAO)applicationContext.getBean("pleaDAO");
	}


	@Override
	public TAVehicleDAO getTAVehicleDAO() 
	{
		return (TAVehicleDAO)applicationContext.getBean("TAVehicleDAO");
	}


	@Override
	public OutcomeTypeDAO getOutcomeTypeDAO() {
		return (OutcomeTypeDAO)applicationContext.getBean("outcomeTypeDAO");
	}


	@Override
	public OffenceDAO getOffenceDAO() {
		return (OffenceDAO)applicationContext.getBean("offenceDAO");
	}


	@Override
	public RoadCheckTypeDAO getRoadCheckTypeDAO() {
		return (RoadCheckTypeDAO)applicationContext.getBean("roadCheckTypeDAO");
	}


	@Override
	public PersonTypeDAO getPersonTypeDAO() {
		return (PersonTypeDAO)applicationContext.getBean("personTypeDAO");
	}


	@Override
	public EventDAO getEventDAO() {
		return (EventDAO)applicationContext.getBean("eventDAO");
	}


	@Override
	public ConfigurationDAO getConfigurationDAO() {
		return (ConfigurationDAO)applicationContext.getBean("configurationDAO");
	}


	@Override
	public GoverningLawDAO getGoverningLawDAO() {
		return (GoverningLawDAO)applicationContext.getBean("governingLawDAO");
	}


	@Override
	public ExcerptParameterMappingDAO getExcerptParameterMappingDAO() {
		return (ExcerptParameterMappingDAO)applicationContext.getBean("excerptParameterMappingDAO");
	}


	@Override
	public ParishDAO getParishDAO() {
		return (ParishDAO)applicationContext.getBean("parishDAO");
	}


	@Override
	public PoundDAO getPoundDAO() {
		return (PoundDAO)applicationContext.getBean("poundDAO");
	}



	@Override
	public WreckingCompanyDAO getWreckingCompanyDAO() {
		return (WreckingCompanyDAO)applicationContext.getBean("wreckingCompanyDAO");
	}


	@Override
	public StaffUserMappingDAO getStaffUserMappingDAO() {
		return (StaffUserMappingDAO) applicationContext.getBean("staffUserMappingDAO");
	}


	@Override
	public TAOfficeLocationDAO getTAOfficeLocationDAO() {
		return (TAOfficeLocationDAO) applicationContext.getBean("TAOfficeLocationDAO");
	}


	@Override
	public AuthorizationDAO getAuthorizationDAO() {
		return (AuthorizationDAO) applicationContext.getBean("authorizationDAO");
	}


	@Override
	public VerdictDAO getVerdictDAO() {
		return (VerdictDAO) applicationContext.getBean("verdictDAO");
	}


	@Override
	public CourtAppearanceDAO getCourtAppearanceDAO() {
		return (CourtAppearanceDAO) applicationContext.getBean("courtAppearanceDAO");
	}
	
	
	

	@Override
	public RoadOperationDAO getRoadOperationDAO() {
		// TODO Auto-generated method stub
		return (RoadOperationDAO) applicationContext.getBean("roadOperationDAO");
	}
	
	@Override
	public SummonsDAO getSummonsDAO() {
		return (SummonsDAO) applicationContext.getBean("summonsDAO");
	}
	
	



	@Override
	public RoadCompliancyDAO getRoadCompliancyDAO() {
		return (RoadCompliancyDAO) applicationContext.getBean("roadCompliancyDAO");
	}


	@Override
	public RoadLicenceDAO getRoadLicenceDAO() {
		return (RoadLicenceDAO) applicationContext.getBean("roadLicenceDAO");
	}


	@Override
	public WarningNoticeDAO getWarningNoticeDAO() {
		return (WarningNoticeDAO) applicationContext.getBean("warningNoticeDAO");
	}


	@Override
	public BadgeDAO getBadgeDAO() {
		return (BadgeDAO) applicationContext.getBean("badgeDAO");
	}


	@Override
	public ReportDAO getReportDAO() {
		return (ReportDAO) applicationContext.getBean("reportDAO");
	}


	@Override
	public ReportDisplayInformationDAOImpl getReportDisplayInformation() {
		return (ReportDisplayInformationDAOImpl) applicationContext.getBean("reportDisplayInformation");
	}


	@Override
	public ReasonDAO getReasonDAO() {
		return (ReasonDAO) applicationContext.getBean("reasonDAO");
	}
	
	@Override
	public ScannedDocDAO getScannedDocDAO() {
		return (ScannedDocDAO) applicationContext.getBean("scannedDocDAO");
	}
	
	@Override
	public CourtCaseDAO getCourtCaseDAO() 
	{
		return (CourtCaseDAO ) applicationContext.getBean("courtCaseDAO");
	}
	
	
	@Override
	public HolidayExceptionsDAO getHolidayExceptionsDAO() {
		return (HolidayExceptionsDAO) applicationContext.getBean("holidayExceptionsDAO");
	}

	@Override
	public WarningNoProsecutionDAO getWarningNoProsecutionDAO() 
	{
		return (WarningNoProsecutionDAO) applicationContext.getBean("warningNoProsecutionDAO");
	}


	@Override
	public UserDAO getUserDAO() {
		
		return (UserDAO) applicationContext.getBean("userDAO");
	}


	@Override
	public ScannedDocumentTypeDAO getScannedDocumentTypeDAO() {
		return (ScannedDocumentTypeDAO) applicationContext.getBean("scannedDocumentTypeDAO");
	}


	@Override
	public PersonDAO getPersonDAO() {
		return (PersonDAO) applicationContext.getBean("personDAO");
	}


	@Override
	public OtherRoleDAO getOtherRoleDAO() {
		return (OtherRoleDAO) applicationContext.getBean("otherRoleDAO");
	}
}
