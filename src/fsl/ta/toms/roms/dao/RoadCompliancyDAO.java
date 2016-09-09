package fsl.ta.toms.roms.dao;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import fsl.dao.DAO;
import fsl.ta.toms.roms.bo.BadgeCheckResultBO;
import fsl.ta.toms.roms.bo.CitationOffenceBO;
import fsl.ta.toms.roms.bo.ComplianceBO;
import fsl.ta.toms.roms.bo.ComplianceParamBO;
import fsl.ta.toms.roms.bo.ConfigurationBO;
import fsl.ta.toms.roms.bo.CourtBO;
import fsl.ta.toms.roms.bo.DLCheckResultBO;
import fsl.ta.toms.roms.bo.DocumentViewBO;
import fsl.ta.toms.roms.bo.ExcerptParamMappingBO;
import fsl.ta.toms.roms.bo.OffenceBO;
import fsl.ta.toms.roms.bo.OffenceMandatoryOutcomeBO;
import fsl.ta.toms.roms.bo.PersonBO;
import fsl.ta.toms.roms.bo.PoundBO;
import fsl.ta.toms.roms.bo.RoadCheckBO;
import fsl.ta.toms.roms.bo.RoadCheckOffenceOutcomeBO;
import fsl.ta.toms.roms.bo.RoadLicCheckResultBO;
import fsl.ta.toms.roms.bo.VehicleOwnerBO;
import fsl.ta.toms.roms.bo.WreckingCompanyBO;
import fsl.ta.toms.roms.dataobjects.CitationCheckDO;
import fsl.ta.toms.roms.dataobjects.ComplianceDO;
import fsl.ta.toms.roms.dataobjects.PersonDO;
import fsl.ta.toms.roms.dataobjects.VehicleCheckResultDO;
import fsl.ta.toms.roms.dataobjects.VehicleCheckResultOwnerDO;
import fsl.ta.toms.roms.dataobjects.VehicleDO;
import fsl.ta.toms.roms.dataobjects.VehicleFitnessDO;
import fsl.ta.toms.roms.dataobjects.VehicleInsuranceDO;
import fsl.ta.toms.roms.dataobjects.VehicleOwnerDO;
import fsl.ta.toms.roms.dataobjects.WarningNoProsecutionDO;
import fsl.ta.toms.roms.dataobjects.WarningNoticeDO;
import fsl.ta.toms.roms.search.criteria.impl.PersonCriteriaBO;
import fsl.ta.toms.roms.search.criteria.impl.RoadCompliancyCriteriaBO;
import fsl.ta.toms.roms.search.criteria.impl.VehicleCriteriaBO;


public interface RoadCompliancyDAO extends DAO{
	
		
	  /*************/
	  public List<PersonDO> findPersonByCriteria(PersonCriteriaBO criteria);
	  public Serializable savePerson(PersonDO personDO);
	  public boolean updatePerson(PersonDO personDO);
	  
	  /*************/
	  public List<VehicleDO> findVehiclesByCriteria(VehicleCriteriaBO criteria);
	  public boolean updateVehicle(VehicleDO vehicleDO);
	  public Serializable saveVehicle(VehicleDO vehicleDO);
	  public boolean updateVehicleOwner(VehicleOwnerDO vehicleOwnerDO);

	 /********************/
	  public boolean updateCompliance(ComplianceDO complianceDO);
	  public Serializable saveCompliance(ComplianceDO complianceDO);
	  
	  
	  public List<CitationOffenceBO> findCitationOffences(String trn, String dln,String licencePlate, boolean isHandHeld);
	
	List<VehicleCheckResultOwnerDO> findVehicleOwnersForCheck(Integer vehicleCheckId);
	List<VehicleInsuranceDO> findVehicleInsuranceForCheck(Integer vehicleCheckId);
	List<VehicleFitnessDO> findVehicleFitnessForCheck(Integer vehicleCheckId);
	
	public List<ComplianceBO> lookupRoadComplianceActivities(RoadCompliancyCriteriaBO criteria);
	 public List<RoadCheckOffenceOutcomeBO> getOffenceOutcomeforCompliance(Integer complianceId);
	 
	 public List<ComplianceParamBO> getCompliancePamsforCompliance(Integer complianceId);

	 public List<DocumentViewBO> getDocumentsforCompliance(Integer complianceId);
	 public List<PersonBO> getWitnessesforCompliance(Integer complianceId);
	 public List<CourtBO> getCourtforCompliance(Integer complianceId);
	 
	 public WreckingCompanyBO getWreckingCoforCompliance(Integer complianceId);
	 
	 public PoundBO getPoundforCompliance(Integer complianceId);
	 
	 public List<ExcerptParamMappingBO> findParamsforOffence(String offences);
	 public List<OffenceBO> getOffencesForDocument(Integer documentId, String docType, Integer complianceID);
	 public List<OffenceBO> getOffencesforCompliance(Integer complianceId);
	 public List<RoadCheckBO> getRoadChecksforCompliance(Integer complianceId);
	public boolean deletePreviousOwners(Integer vehicleID);
	public List<OffenceMandatoryOutcomeBO> getActiveOffenceMandatoryOutcomeList();
	
	public BadgeCheckResultBO getBadgeCheckResult(Integer complianceId);
	
	public RoadLicCheckResultBO getRoadLicCheckResult(Integer complianceId);
	
	public VehicleCheckResultDO getVehicleCheckResult(Integer complianceId);
	
	public DLCheckResultBO getDLCheckResult(Integer complianceId);
	public CitationCheckDO getCitationCheckDO(Integer complianceId);
	List<VehicleOwnerBO> findVehicleOwnersByPlate(String plateNo);
	
	public PersonDO getPersonFromDriversLicense(String dlNo);
	
	public WarningNoticeDO getWarningNoticeForRoadCheck(Integer complianceId);
	
	public WarningNoProsecutionDO getWarningNoProsecutionForRoadCheck(Integer complianceId);
	
	public Date getMinCourtAppearnceDateForCompliancy(Integer complianceId);
	
	public List<ConfigurationBO> getMaxCitationValues();

	public Integer getTotalROMSOffences(String trn, String dln, String... plateNumber);
	

	public List<VehicleOwnerDO> findVehicleOwnersByTRN(String trnNo, String branch);
	public VehicleCheckResultDO findLatestVehicleForPlateNo(String plateNo);

}


