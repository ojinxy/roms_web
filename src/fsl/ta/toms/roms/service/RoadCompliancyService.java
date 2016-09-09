package fsl.ta.toms.roms.service;

import java.io.Serializable;
import java.util.List;

import fsl.ta.toms.roms.bo.BadgeBO;
import fsl.ta.toms.roms.bo.BadgeCheckResultBO;
import fsl.ta.toms.roms.bo.CitationCheckResultBO;
import fsl.ta.toms.roms.bo.CitationOffenceBO;
import fsl.ta.toms.roms.bo.ComplianceBO;
import fsl.ta.toms.roms.bo.ComplianceDetailsBO;
import fsl.ta.toms.roms.bo.CompliancyCheckBO;
import fsl.ta.toms.roms.bo.DLCheckResultBO;
import fsl.ta.toms.roms.bo.DocumentViewBO;
import fsl.ta.toms.roms.bo.ExcerptParamMappingBO;
import fsl.ta.toms.roms.bo.OffenceMandatoryOutcomeBO;
import fsl.ta.toms.roms.bo.PersonBO;
import fsl.ta.toms.roms.bo.RoadCheckOffenceOutcomeBO;
import fsl.ta.toms.roms.bo.RoadLicCheckResultBO;
import fsl.ta.toms.roms.bo.RoadLicenceBO;
import fsl.ta.toms.roms.bo.VehicleBO;
import fsl.ta.toms.roms.bo.VehicleCheckResultBO;
import fsl.ta.toms.roms.dataobjects.AuditEntry;
import fsl.ta.toms.roms.dataobjects.ComplianceDO;
import fsl.ta.toms.roms.dataobjects.PersonDO;
import fsl.ta.toms.roms.dataobjects.VehicleDO;
import fsl.ta.toms.roms.dlwebservice.DriverLicenceDetails;
import fsl.ta.toms.roms.exception.ErrorSavingException;
import fsl.ta.toms.roms.exception.InvalidFieldException;
import fsl.ta.toms.roms.exception.NoRecordFoundException;
import fsl.ta.toms.roms.exception.RequiredFieldMissingException;
import fsl.ta.toms.roms.search.criteria.impl.RoadCompliancyCriteriaBO;

public interface RoadCompliancyService {
	

	//public String getCitationOffence(String trn, String dln);
	
	public PersonBO savePerson(PersonBO person);
	
	
	public VehicleBO saveVehicle(VehicleBO vehicle) ;
	
	public ComplianceBO saveCompliance(ComplianceBO complianceDetails) throws ErrorSavingException, InvalidFieldException;
	
	public <T> T find(Class<T> clazz, Serializable id );
	
	public List<CitationOffenceBO> getCitationOffence(String trn, String dln,String licencePlate, boolean isHandheld);
	
	public boolean updatePerson(PersonDO retrievedPersonDO,PersonBO personBO);
	
	public VehicleDO vehicleExists(VehicleBO vehicle);
	
	public BadgeCheckResultBO processBadgeCheck(BadgeCheckResultBO badgeCheck, BadgeBO badgeDetails) throws InvalidFieldException;
	
	public RoadLicCheckResultBO processRoadLicenceCheck(RoadLicCheckResultBO roadLicCheck, RoadLicenceBO roadLicDetails, boolean isQuickQuery,String plateRegNo) throws InvalidFieldException;
	
	public CitationCheckResultBO processCitationHistoryCheck(CitationCheckResultBO citationCheck) throws InvalidFieldException;
	
	public DLCheckResultBO processDLCheck(DLCheckResultBO dlCheck, DriverLicenceDetails dlDetails) throws InvalidFieldException;
	
	public VehicleCheckResultBO performMVCheck(VehicleCheckResultBO mvCheck, boolean isQuickQuery,RoadLicenceBO roadLicDetails) throws NoRecordFoundException, InvalidFieldException, ErrorSavingException;
	
	public void createOtherCheck(Integer complianceId,String currentUserName) throws InvalidFieldException;
	
	public List<DocumentViewBO> recordOffenceOutcomeHelper(List<CompliancyCheckBO> listOfCompliancyChecks, ComplianceBO updatedComplianceDetails) throws InvalidFieldException, RequiredFieldMissingException, ErrorSavingException;
	public List<DocumentViewBO> recordOffenceOutcome(CompliancyCheckBO compliancyCheck) throws InvalidFieldException, RequiredFieldMissingException, ErrorSavingException;
	
	public List<ComplianceBO> lookupRoadComplianceActivities(RoadCompliancyCriteriaBO roadCompliancyCriteriaBO);
	
	public ComplianceDetailsBO findComplianceDetails(Integer complianceId);
	
	public List<ExcerptParamMappingBO> findParamsforOffence(List<Integer> offences);
	
	public List<OffenceMandatoryOutcomeBO> getActiveOffenceMandatoryOutcomeList();


	CitationCheckResultBO getCitationCheck(Integer complianceId)
			throws InvalidFieldException;



	public void createRoadCheckRecord(String roadCheckType, Integer complianceId,
			String currentUserName, String comment) throws InvalidFieldException;
	
	public PersonBO getPersonFromDriversLicense(String dlNo,String currentUserName) throws Exception;
	
	public VehicleBO getWreckerVehicle(String regPlateNo) throws Exception;

	public void setMaxOffenceValues(CitationCheckResultBO citationCheck);

	public void setTotalROMSOffences(CitationCheckResultBO citationCheck, String trn, String dln);

	public void doMVCheckEventAudit(boolean isQuickQuery, VehicleCheckResultBO mvCheckRequest,ComplianceDO complianceDO, AuditEntry audit);
	
	
}
