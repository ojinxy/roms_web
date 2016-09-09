package fsl.ta.toms.roms.webservices;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import fsl.ta.toms.roms.util.AddressView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import fsl.ta.toms.roms.bo.ArteryBO;
import fsl.ta.toms.roms.bo.ConfigurationBO;
import fsl.ta.toms.roms.bo.CourtBO;
import fsl.ta.toms.roms.bo.GoverningLawBO;
import fsl.ta.toms.roms.bo.HolidayExceptionBO;
import fsl.ta.toms.roms.bo.ITAExaminerBO;
import fsl.ta.toms.roms.bo.JPBO;
import fsl.ta.toms.roms.bo.LocationBO;
import fsl.ta.toms.roms.bo.OffenceBO;
import fsl.ta.toms.roms.bo.OffenceLawBO;
import fsl.ta.toms.roms.bo.OffenceParamBO;
import fsl.ta.toms.roms.bo.ParishBO;
import fsl.ta.toms.roms.bo.PoundBO;
import fsl.ta.toms.roms.bo.ReasonBO;
import fsl.ta.toms.roms.bo.StrategyBO;
import fsl.ta.toms.roms.bo.StrategyRuleBO;
import fsl.ta.toms.roms.bo.TAVehicleBO;
import fsl.ta.toms.roms.bo.VehicleBO;
import fsl.ta.toms.roms.bo.WreckingCompanyBO;
import fsl.ta.toms.roms.constants.Constants;
import fsl.ta.toms.roms.dataobjects.ITAExaminerDO;
import fsl.ta.toms.roms.dataobjects.JPDO;
import fsl.ta.toms.roms.dataobjects.OffenceDO;
import fsl.ta.toms.roms.dataobjects.ReasonDO;
import fsl.ta.toms.roms.dataobjects.StrategyDO;
import fsl.ta.toms.roms.dataobjects.TAVehicleDO;
import fsl.ta.toms.roms.exception.ErrorSavingException;
import fsl.ta.toms.roms.exception.InvalidFieldException;
import fsl.ta.toms.roms.exception.NoRecordFoundException;
import fsl.ta.toms.roms.exception.RequiredFieldMissingException;
import fsl.ta.toms.roms.search.criteria.impl.ArteryCriteriaBO;
import fsl.ta.toms.roms.search.criteria.impl.ConfigurationCriteriaBO;
import fsl.ta.toms.roms.search.criteria.impl.CourtCriteriaBO;
import fsl.ta.toms.roms.search.criteria.impl.GoverningLawCriteriaBO;
import fsl.ta.toms.roms.search.criteria.impl.HolidayExceptionsCriteriaBO;
import fsl.ta.toms.roms.search.criteria.impl.ITAExaminerCriteriaBO;
import fsl.ta.toms.roms.search.criteria.impl.JPCriteriaBO;
import fsl.ta.toms.roms.search.criteria.impl.LocationCriteriaBO;
import fsl.ta.toms.roms.search.criteria.impl.OffenceCriteriaBO;
import fsl.ta.toms.roms.search.criteria.impl.ParishCriteriaBO;
import fsl.ta.toms.roms.search.criteria.impl.PoundCriteriaBO;
import fsl.ta.toms.roms.search.criteria.impl.ReasonCriteriaBO;
import fsl.ta.toms.roms.search.criteria.impl.StrategyCriteriaBO;
import fsl.ta.toms.roms.search.criteria.impl.TAVehicleCriteriaBO;
import fsl.ta.toms.roms.search.criteria.impl.WreckingCompanyCriteriaBO;
import fsl.ta.toms.roms.service.ArteryService;
import fsl.ta.toms.roms.service.ConfigurationService;
import fsl.ta.toms.roms.service.CourtService;
import fsl.ta.toms.roms.service.GoverningLawService;
import fsl.ta.toms.roms.service.HolidayExceptionsService;
import fsl.ta.toms.roms.service.LocationService;
import fsl.ta.toms.roms.service.ParishService;
import fsl.ta.toms.roms.service.PoundService;
import fsl.ta.toms.roms.service.ServiceFactory;
import fsl.ta.toms.roms.service.WreckingCompanyService;
import fsl.ta.toms.roms.util.GenericUtil;
import fsl.ta.toms.roms.util.PhoneNumberUtil;
import fsl.ta.toms.roms.util.TRNFormatter;

@Controller
@RequestMapping("/roms_rest/maintenance")
public class Maintenance extends SpringBeanAutowiringSupport {

	@Autowired
	private ServiceFactory serviceFactory;

	
	/***************
	 * COMMON METHODS
	 * 
	 */
	
	/**
	 * This method validates Phone Numbers(Home, Work and Cell)
	 * @param telephoneHome
	 * @param telephoneWork
	 * @param telephoneCell
	 * @throws InvalidFieldException
	 */
	private void validatePhoneNumbers(String telephoneHome, String telephoneWork, String telephoneCell) throws InvalidFieldException{
		PhoneNumberUtil phoneNumberUtil = new PhoneNumberUtil();
		boolean phoneNoValid = true;
		if(StringUtils.isNotBlank(telephoneHome)){
			phoneNoValid = phoneNumberUtil.validatePhoneNumber(telephoneHome);
			
			if(phoneNoValid==false){
				throw new InvalidFieldException("Invalid Home Telephone No");
			}
		}
		
	
		if(StringUtils.isNotBlank(telephoneWork)){
			phoneNoValid = phoneNumberUtil.validatePhoneNumber(telephoneWork);
			
			if(phoneNoValid==false){
				throw new InvalidFieldException("Invalid Work Telephone No");
			}
		}
		
		
		if(StringUtils.isNotBlank(telephoneCell)){
			phoneNoValid = phoneNumberUtil.validatePhoneNumber(telephoneCell);
			
			if(phoneNoValid==false){
				throw new InvalidFieldException("Invalid Cell Telephone No");
			}
		}
		
	}
	/*********************************************************************
	 * 
	 */
	
	
	
	/***********************************************************************************
	 * Holiday Exceptions
	 ***********************************************************************************/
	/**
	 * 
	 * @param criteriaBO
	 * @return
	 * @throws NoRecordFoundException
	 *             ,RequiredFieldMissingException
	 */
	public List<HolidayExceptionBO> lookupHolidayExceptions(
			HolidayExceptionsCriteriaBO criteriaBO)
			throws RequiredFieldMissingException, NoRecordFoundException 
			{

		HolidayExceptionsService holidayExceptionService = serviceFactory.getHolidayExceptionsService();

		if (criteriaBO == null)
			throw new RequiredFieldMissingException("Object cannot be empty.");

		List<HolidayExceptionBO> holidayExceptions = holidayExceptionService.lookupHolidayExceptions(criteriaBO);

		if (holidayExceptions == null || holidayExceptions.isEmpty())
			throw new NoRecordFoundException("No records were found.");

		return holidayExceptions;

	}

	/**
	 * 
	 * @param holidayDate
	 * @return
	 * @throws RequiredFieldMissingException
	 * @throws ErrorSavingException
	 */
	public boolean holidayExceptionExists(Date holidayDate)
			throws RequiredFieldMissingException {

		HolidayExceptionsService holidayExceptionsService = serviceFactory.getHolidayExceptionsService();

		if (holidayDate == null)
			throw new RequiredFieldMissingException(
					"Date is required.");

		boolean exists = holidayExceptionsService.holidayExceptionExists(holidayDate);

		return exists;
	}

	/**
	 * 
	 * @param holidayExceptionBO
	 * @return
	 * @return
	 * @throws RequiredFieldMissingException
	 * @throws InvalidFieldException
	 */
	public Date saveHolidayExeption(HolidayExceptionBO holidayExceptionBO)
			throws ErrorSavingException, RequiredFieldMissingException,
			InvalidFieldException {

		HolidayExceptionsService holidayExceptionService = serviceFactory.getHolidayExceptionsService();

		Date theHolidaydate = null;

		if (holidayExceptionBO == null)
			throw new ErrorSavingException(
					"Details cannot be empty.");

		if (holidayExceptionBO.getHolidayDate() == null)
			throw new RequiredFieldMissingException(
					"Holiday Date is required.");
		else {
			if (holidayExceptionService.holidayExceptionExists(holidayExceptionBO.getHolidayDate()))
				throw new InvalidFieldException(
						"Holiday Exception already exists.");
		}

		if (StringUtils.isBlank(holidayExceptionBO.getDescription()))
			throw new RequiredFieldMissingException("Description is required.");
		else {
			if (holidayExceptionService.descriptionExists(holidayExceptionBO))
				throw new InvalidFieldException(
						"Holiday Exception Description exists.");
		}
		
		if (StringUtils.isBlank(holidayExceptionBO.getCurrentUsername()))
			throw new RequiredFieldMissingException(
					"Creating user is required.");

		
		try {
			theHolidaydate = holidayExceptionService.saveHolidayException(holidayExceptionBO);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ErrorSavingException(e.getMessage());
		}

		return theHolidaydate;
	}

	/**
	 * 
	 * @param configurationBO
	 * @throws InvalidFieldException
	 */
	public void updateHolidayException(HolidayExceptionBO holidayExceptionBO)
			throws ErrorSavingException, RequiredFieldMissingException,
			InvalidFieldException {

		HolidayExceptionsService holidayExceptionService = serviceFactory.getHolidayExceptionsService();

	
		if (holidayExceptionBO.getHolidayDate() == null)
			throw new RequiredFieldMissingException(
					"Holiday Date is required.");
		else {
			if (!holidayExceptionService.holidayExceptionExists(holidayExceptionBO.getHolidayDate()))
				throw new InvalidFieldException(
						"Holiday Exception does not exist.");
		}

		if (StringUtils.isBlank(holidayExceptionBO.getDescription()))
			throw new RequiredFieldMissingException("Description is required.");
		else {
			if (holidayExceptionService.descriptionExists(holidayExceptionBO))
				throw new InvalidFieldException(
						"Holiday Exception Description exists.");
		}
		
		if (StringUtils.isBlank(holidayExceptionBO.getCurrentUsername()))
			throw new RequiredFieldMissingException(
					"Creating user is required.");

		try {
			holidayExceptionService.updateHolidayExceptions(holidayExceptionBO);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ErrorSavingException(e.getMessage());
		}
	}
	
	/***********************************************************************************
	 * STRATEGY
	 ***********************************************************************************/
	
	/**
	 * This method is used to lookup Strategy's based on the search criteria entered
	 * @param strategyCriteriaBO
	 * @return
	 * @throws RequiredFieldMissingException
	 * @throws NoRecordFoundException
	 */
	public List<StrategyBO> lookupStrategy(StrategyCriteriaBO strategyCriteriaBO)
		throws RequiredFieldMissingException, NoRecordFoundException{
		
		List<StrategyBO> strategyBOList = new ArrayList<StrategyBO>();
		
		if(strategyCriteriaBO==null){
			throw new RequiredFieldMissingException();
		}
		
		/*if(StringUtils.isBlank(strategyCriteriaBO.getStrategyDescription()) && StringUtils.isBlank(strategyCriteriaBO.getStatusId())
					 && (strategyCriteriaBO.getStrategyId()==null || strategyCriteriaBO.getStrategyId()<1)){
		
			throw new RequiredFieldMissingException();
		}*/
		
		strategyBOList = serviceFactory.getStrategyService().lookupStrategy(strategyCriteriaBO);
		
		if(strategyBOList==null || strategyBOList.size()==0){
			throw new NoRecordFoundException();
		}
		
		return strategyBOList;
	}
	
	
	/**
	 * This method is used to create a new Strategy and its Rules
	 * @param strategyBO
	 * @param strategyRuleList
	 * @throws RequiredFieldMissingException
	 * @throws InvalidFieldException
	 * @throws ErrorSavingException
	 */
	public void saveStrategy(StrategyBO strategyBO, List<StrategyRuleBO> strategyRuleList)
			throws RequiredFieldMissingException, InvalidFieldException, ErrorSavingException{
		
		boolean saved = false;
		
		if(strategyBO==null || strategyRuleList==null){
			throw new RequiredFieldMissingException();
		}
		
		if(StringUtils.isBlank(strategyBO.getCurrentUsername()) || StringUtils.isBlank(strategyBO.getStrategyDescription())){
	
			throw new RequiredFieldMissingException();
		}
	
		boolean descExist = strategyDescriptionExist(strategyBO.getStrategyDescription(), strategyBO.getStrategyId());
		
		if(descExist){
			throw new InvalidFieldException("Strategy Description already exists.");
		}
		
		
		validateStrategyRules(strategyRuleList);
		
		strategyBO.setCurrentUsername(strategyBO.getCurrentUsername().trim().toUpperCase());
				
		saved = serviceFactory.getStrategyService().saveStrategy(strategyBO, strategyRuleList);
		
		if(saved==false){
			throw new ErrorSavingException();
		}
	}
	
	/**
	 * This method validates the Strategy Rules
	 * @param strategyRuleList
	 * @throws InvalidFieldException
	 */
	private void validateStrategyRules(List<StrategyRuleBO> strategyRuleList)throws InvalidFieldException{
		int taStaffList = 0;
		int jpList = 0;
		int policeOfficerList = 0;
		int itaExaminerList = 0;
		
		if(strategyRuleList.size()!=4){
			throw new InvalidFieldException("Invalid Strategy Rule List must be 4");
		}
		
		for(StrategyRuleBO strategyRuleBO : strategyRuleList){
			if(strategyRuleBO==null || StringUtils.isBlank(strategyRuleBO.getPersonTypeId())){
				throw new InvalidFieldException("Invalid Person Type");
			}
			
			if(!strategyRuleBO.getPersonTypeId().equals(Constants.PersonType.TA_STAFF) 
					&& !strategyRuleBO.getPersonTypeId().equals(Constants.PersonType.JP) 
					&& !strategyRuleBO.getPersonTypeId().equals(Constants.PersonType.POLICE_OFFCER) 
					&& !strategyRuleBO.getPersonTypeId().equals(Constants.PersonType.ITA_EXAMINER)){
				
				throw new InvalidFieldException("Invalid Person Type");
			}
			
			if(strategyRuleBO.getPersonTypeId().equals(Constants.PersonType.TA_STAFF)){
				taStaffList++;
			}
			
			if(strategyRuleBO.getPersonTypeId().equals(Constants.PersonType.JP)){
				jpList++;
			}
			
			if(strategyRuleBO.getPersonTypeId().equals(Constants.PersonType.POLICE_OFFCER)){
				policeOfficerList++;
			}
			
			if(strategyRuleBO.getPersonTypeId().equals(Constants.PersonType.ITA_EXAMINER)){
				itaExaminerList++;
			}
			
			if(taStaffList>1 || jpList>1 || policeOfficerList>1 || itaExaminerList>1){
				throw new InvalidFieldException("Cannot have more than one Strategy Rule for each Person Type");
			}
			
			if(strategyRuleBO.getMinimum()!=null && strategyRuleBO.getMaximum()!=null){
				if(strategyRuleBO.getMaximum()<strategyRuleBO.getMinimum()){
					throw new InvalidFieldException("Strategy Rule Maximum cannot be less than Minimun");
				}
			}
			
			if(strategyRuleBO.getMinimum()!=null){
				if(strategyRuleBO.getMinimum()<0){
					throw new InvalidFieldException("Strategy Rule Minimum cannot be less than 0");
				}
				if(strategyRuleBO.getMinimum()>Constants.maxStrategyRuleValue){
					throw new InvalidFieldException("Strategy Rule Minimum cannot be greater than the Maximum Strategy Rule Value");
				}
			}
			
			if(strategyRuleBO.getMaximum()!=null){
				if(strategyRuleBO.getMaximum()<0){
					throw new InvalidFieldException("Strategy Rule Maximum cannot be less than 0");
				}
				if(strategyRuleBO.getMaximum()>Constants.maxStrategyRuleValue){
					throw new InvalidFieldException("Strategy Rule Maximum cannot be greater than the Maximum Strategy Rule Value");
				}
			}
			
			/*if(strategyRuleBO.getMinimum()!=null &&  strategyRuleBO.getMaximum()==null){
				throw new InvalidFieldException("Strategy Rule maximum cannot be null if minimun has a value");
			}
			
			if(strategyRuleBO.getMaximum()!=null &&  strategyRuleBO.getMinimum()==null){
				throw new InvalidFieldException("Strategy Rule minimum cannot be null if maximum has a value");
			}*/
		}
	}
	
	/**
	 * This method updates an existing Strategy and its Rules
	 * @param strategyBO
	 * @param strategyRuleList
	 * @throws RequiredFieldMissingException
	 * @throws InvalidFieldException
	 * @throws ErrorSavingException
	 */
	public void updateStrategy(StrategyBO strategyBO, List<StrategyRuleBO> strategyRuleList)
			throws RequiredFieldMissingException, InvalidFieldException, ErrorSavingException{
		
		boolean updated = false;
		if(strategyBO==null || strategyRuleList==null){
			throw new RequiredFieldMissingException();
		}
		
		if(StringUtils.isBlank(strategyBO.getCurrentUsername()) || StringUtils.isBlank(strategyBO.getStrategyDescription())
				|| (strategyBO.getStrategyId()==null || strategyBO.getStrategyId()<1) || StringUtils.isBlank(strategyBO.getStatusId()) ){
	
			throw new RequiredFieldMissingException();
		} 
	
		
		strategyBO.setCurrentUsername(strategyBO.getCurrentUsername().trim().toUpperCase());
		
		StrategyDO savedStrategyDO= new StrategyDO();
		savedStrategyDO = serviceFactory.getStrategyService().findStrategyById(strategyBO.getStrategyId());
	
		if(savedStrategyDO==null){
			throw new InvalidFieldException("Strategy does not exist");
		}		

		boolean descExist = strategyDescriptionExist(strategyBO.getStrategyDescription(), strategyBO.getStrategyId());
		
		if(descExist){
			throw new InvalidFieldException("Strategy Description already exists.");
		}
		
		validateStrategyRules(strategyRuleList);
		
		updated = serviceFactory.getStrategyService().updateStrategy(strategyBO, strategyRuleList);
		
		if(updated==false){
			throw new ErrorSavingException();
		}
	}
	
	/**
	 * This method retrieves the Strategy Rules for a selected Strategy
	 * @param strategyId
	 * @return
	 * @throws RequiredFieldMissingException
	 * @throws NoRecordFoundException
	 */
	public List<StrategyRuleBO> getStrategyRulesForStrategy(Integer strategyId) throws RequiredFieldMissingException, NoRecordFoundException{
		
		 List<StrategyRuleBO> strategyRuleList = new ArrayList<StrategyRuleBO>();
		 
		if(strategyId==null || strategyId<1){
			throw new RequiredFieldMissingException();
		}
		
		strategyRuleList = serviceFactory.getStrategyService().getStrategyRulesForStrategy(strategyId);
		
		if(strategyRuleList==null || strategyRuleList.size()==0){
			throw new NoRecordFoundException();
		}
		
		return strategyRuleList;
		
	}

	/**
	 * This method returns true  if the Description exists  and no strategy id passed
	 * or the Description exists  and the strategy id is not equal to the strategy id passed
	 * @param description
	 * @param strategyId
	 * @return
	 * @throws RequiredFieldMissingException
	 */
	public boolean strategyDescriptionExist(String description, Integer strategyId) throws RequiredFieldMissingException{
		
		if(StringUtils.isBlank(description)){
			throw new RequiredFieldMissingException();
		}
		
		return serviceFactory.getStrategyService().strategyDescriptionExist(description.trim(), strategyId);
	}
	
	
	/***********************************************************************************
	 * REASON
	 ***********************************************************************************/
	/**
	 * This method is used to lookup Reasons based on the search criteria entered
	 * @param reasonCriteriaBO
	 * @return
	 * @throws RequiredFieldMissingException
	 * @throws NoRecordFoundException
	 */
	@RequestMapping("/lookupReason")
	public @ResponseBody List<ReasonBO> lookupReason(@RequestBody ReasonCriteriaBO reasonCriteriaBO)
			throws RequiredFieldMissingException, NoRecordFoundException{
		
		List<ReasonBO> reasonBOList = new ArrayList<ReasonBO>();
		
		if(reasonCriteriaBO==null){
			throw new RequiredFieldMissingException();
		}
		
		/*if(StringUtils.isBlank(reasonCriteriaBO.getReasonDescription()) && StringUtils.isBlank(reasonCriteriaBO.getType()) && StringUtils.isBlank(reasonCriteriaBO.getStatusId())
					 && (reasonCriteriaBO.getReasonId()==null || reasonCriteriaBO.getReasonId()<1)){
		
			throw new RequiredFieldMissingException();
		}*/
		
		reasonBOList = serviceFactory.getReasonService().lookupReason(reasonCriteriaBO);
		
		if(reasonBOList==null || reasonBOList.size()==0){
			throw new NoRecordFoundException();
		}
		
		return reasonBOList;
	}
	
	/**
	 * This method is used to create a new Reason
	 * @param reasonBO
	 * @throws RequiredFieldMissingException
	 * @throws InvalidFieldException
	 * @throws ErrorSavingException
	 */
	public void saveReason(ReasonBO reasonBO)throws RequiredFieldMissingException, InvalidFieldException, ErrorSavingException{
		boolean saved = false;
		
		if(reasonBO==null){
			throw new RequiredFieldMissingException();
		}
		
		if(StringUtils.isBlank(reasonBO.getCurrentUsername()) || StringUtils.isBlank(reasonBO.getReasonDescription())
				|| StringUtils.isBlank(reasonBO.getType())){
	
			throw new RequiredFieldMissingException();
		}
	
		reasonBO.setCurrentUsername(reasonBO.getCurrentUsername().trim().toUpperCase());
	
		/*if(!reasonBO.getType().equals(Constants.ReasonType.ROAD_OPERATION) && !reasonBO.getType().equals(Constants.ReasonType.SUMMONS) && !reasonBO.getType().equals(Constants.ReasonType.WARNING_NOTICE)){
			throw new InvalidFieldException("Invalid Reason Type.");
		}*/
		
		boolean reasonExist = reasonDescExistForSelectedType(reasonBO);
		
		if(reasonExist){
			throw new InvalidFieldException("Reason Description already exists for the selected Type.");
		}
			
				
		saved = serviceFactory.getReasonService().saveReason(reasonBO);
		
		if(saved==false){
			throw new ErrorSavingException();
		}
	}

	
	/**
	 * This method is used to update an existing Reason
	 * @param reasonBO
	 * @throws RequiredFieldMissingException
	 * @throws InvalidFieldException
	 * @throws ErrorSavingException
	 */
	public void updateReason(ReasonBO reasonBO) throws RequiredFieldMissingException, InvalidFieldException, ErrorSavingException{
		boolean updated = false;
		
		if(reasonBO==null){
			throw new RequiredFieldMissingException();
		}
		
		if(StringUtils.isBlank(reasonBO.getCurrentUsername()) || StringUtils.isBlank(reasonBO.getReasonDescription())
				|| StringUtils.isBlank(reasonBO.getType()) || StringUtils.isBlank(reasonBO.getStatusId()) || (reasonBO.getReasonId()==null || reasonBO.getReasonId()<1)){
	
			throw new RequiredFieldMissingException();
		}
	
		reasonBO.setCurrentUsername(reasonBO.getCurrentUsername().trim().toUpperCase());
		
		
		ReasonDO savedReasonDO = new ReasonDO();
		savedReasonDO = serviceFactory.getReasonService().findReasonById(reasonBO.getReasonId());
	
		if(savedReasonDO==null){
			throw new InvalidFieldException("Reason does not exist");
		}
		
		/*if(!reasonBO.getType().equals(Constants.ReasonType.ROAD_OPERATION) && !reasonBO.getType().equals(Constants.ReasonType.SUMMONS) && !reasonBO.getType().equals(Constants.ReasonType.WARNING_NOTICE)){
			throw new InvalidFieldException("Invalid Reason Type.");
		}*/
		
		boolean reasonExist = reasonDescExistForSelectedType(reasonBO);
		
		if(reasonExist){
			throw new InvalidFieldException("Reason Description already exists for the selected Type.");
		}
			

		updated = serviceFactory.getReasonService().updateReason(reasonBO);
		
		if(updated==false){
			throw new ErrorSavingException();
		}
	}
	
	/**
	 * This method returns true  if the Description exists for the Type selected and no reason id passed
	 * or the Description exists for the Type selected and the reason id is not equal to the reason id passed
	 * @param reasonBO
	 * @return
	 * @throws RequiredFieldMissingException
	 */
	public boolean reasonDescExistForSelectedType(ReasonBO reasonBO) throws RequiredFieldMissingException{
		
		if(reasonBO==null){
			throw new RequiredFieldMissingException();
		}
		
		if(StringUtils.isBlank(reasonBO.getReasonDescription()) || StringUtils.isBlank(reasonBO.getType())){
			throw new RequiredFieldMissingException();
		}
		
		return serviceFactory.getReasonService().reasonDescExistForSelectedType(reasonBO);
	}
	
	
	/***********************************************************************************
	 * TA Vehicle
	 ***********************************************************************************/
	/**
	 * This method is used to lookup TA Vehicles based on the search criteria entered
	 * @param taVehicleCriteriaBO
	 * @return
	 * @throws RequiredFieldMissingException
	 * @throws NoRecordFoundException
	 */
	public List<TAVehicleBO> lookupTAVehicle(TAVehicleCriteriaBO taVehicleCriteriaBO)
		throws RequiredFieldMissingException, NoRecordFoundException{
		
		List<TAVehicleBO> taVehicleBOList = new ArrayList<TAVehicleBO>();
		
		if(taVehicleCriteriaBO==null){
			throw new RequiredFieldMissingException();
		}
		
		/*if(StringUtils.isBlank(taVehicleCriteriaBO.getVehicle().getChassisNo()) && StringUtils.isBlank(taVehicleCriteriaBO.getVehicle().getColour()) && StringUtils.isBlank(taVehicleCriteriaBO.getVehicle().getEngineNo())
					&& StringUtils.isBlank(taVehicleCriteriaBO.getVehicle().getMakeDescription()) && StringUtils.isBlank(taVehicleCriteriaBO.getVehicle().getModel()) && (taVehicleCriteriaBO.getVehicle().getYear()==null || taVehicleCriteriaBO.getVehicle().getYear()<1) 
					&& StringUtils.isBlank(taVehicleCriteriaBO.getVehicle().getOwnerName()) && StringUtils.isBlank(taVehicleCriteriaBO.getVehicle().getPlateRegNo()) && StringUtils.isBlank(taVehicleCriteriaBO.getVehicle().getTypeDesc())
					&& StringUtils.isBlank(taVehicleCriteriaBO.getOfficeLocationCode()) && StringUtils.isBlank(taVehicleCriteriaBO.getStatusId()) && (taVehicleCriteriaBO.getTaVehicleId()==null || taVehicleCriteriaBO.getTaVehicleId()<1) ){
		
			throw new RequiredFieldMissingException();
		}*/
		
		taVehicleBOList = serviceFactory.getTaVehicleService().lookupTAVehicle(taVehicleCriteriaBO);
		
		if(taVehicleBOList==null || taVehicleBOList.size()==0){
			throw new NoRecordFoundException();
		}
		
		return taVehicleBOList;
	}
	
	
	/**
	 * This method saves anew TA Vehicle
	 * @param taVehicleBO
	 * @throws RequiredFieldMissingException
	 * @throws InvalidFieldException
	 * @throws ErrorSavingException
	 */
	public void saveTAVehicle(TAVehicleBO taVehicleBO) throws RequiredFieldMissingException, InvalidFieldException, ErrorSavingException{
		boolean saved = false;
		
		if(taVehicleBO==null || taVehicleBO.getVehicle()==null){
			throw new RequiredFieldMissingException();
		}
		if(StringUtils.isBlank(taVehicleBO.getVehicle().getChassisNo()) || StringUtils.isBlank(taVehicleBO.getVehicle().getColour()) || StringUtils.isBlank(taVehicleBO.getVehicle().getEngineNo())
				|| StringUtils.isBlank(taVehicleBO.getVehicle().getMakeDescription()) || StringUtils.isBlank(taVehicleBO.getVehicle().getModel()) || (taVehicleBO.getVehicle().getYear()==null || taVehicleBO.getVehicle().getYear()<1)
				|| StringUtils.isBlank(taVehicleBO.getVehicle().getOwnerName()) || StringUtils.isBlank(taVehicleBO.getVehicle().getPlateRegNo()) || StringUtils.isBlank(taVehicleBO.getVehicle().getTypeDesc())
				|| StringUtils.isBlank(taVehicleBO.getOfficeLocationCode())  || StringUtils.isBlank(taVehicleBO.getCurrentUsername())){
	

			throw new RequiredFieldMissingException();
		}
	
		taVehicleBO.setCurrentUsername(taVehicleBO.getCurrentUsername().trim().toUpperCase());
		taVehicleBO.getVehicle().trimVehicleDetails();
			
		boolean taVehicleExistWithVehicleId =false;
		taVehicleExistWithVehicleId = serviceFactory.getTaVehicleService().taVehicleExistWithVehicleId(taVehicleBO.getVehicle());
		
		
		if(taVehicleExistWithVehicleId==true){
			throw new InvalidFieldException("TA Vehicle already exists.");
		}
		
		if(activeTAVehicleWithPlateRegNoExists(taVehicleBO.getVehicle().getPlateRegNo())==true){
			throw new InvalidFieldException("An Active TA Vehicle exists with selected Plate Registration Number.");
		}
		
		
		saved = serviceFactory.getTaVehicleService().saveTAVehicle(taVehicleBO);
		if(saved==false){
			throw new ErrorSavingException();
		}
	}
	
	/**
	 * This method updates an existing TA Vehicle
	 * @param taVehicleBO
	 * @throws RequiredFieldMissingException
	 * @throws InvalidFieldException
	 * @throws ErrorSavingException
	 */
	public void updateTAVehicle(TAVehicleBO taVehicleBO) throws RequiredFieldMissingException, InvalidFieldException, ErrorSavingException{
		boolean updated = false;
		
		if(taVehicleBO==null || taVehicleBO.getVehicle()==null){
			throw new RequiredFieldMissingException();
		}
		if(StringUtils.isBlank(taVehicleBO.getVehicle().getChassisNo()) || StringUtils.isBlank(taVehicleBO.getVehicle().getColour()) || StringUtils.isBlank(taVehicleBO.getVehicle().getEngineNo())
				|| StringUtils.isBlank(taVehicleBO.getVehicle().getMakeDescription()) || StringUtils.isBlank(taVehicleBO.getVehicle().getModel()) || (taVehicleBO.getVehicle().getYear()==null || taVehicleBO.getVehicle().getYear()<1)
				|| StringUtils.isBlank(taVehicleBO.getVehicle().getOwnerName()) || StringUtils.isBlank(taVehicleBO.getVehicle().getPlateRegNo()) || StringUtils.isBlank(taVehicleBO.getVehicle().getTypeDesc())
				|| StringUtils.isBlank(taVehicleBO.getOfficeLocationCode()) || StringUtils.isBlank(taVehicleBO.getStatusId())  || StringUtils.isBlank(taVehicleBO.getCurrentUsername())
				|| (taVehicleBO.getTaVehicleId()==null || taVehicleBO.getTaVehicleId()<1) || (taVehicleBO.getVehicle().getVehicleId()==null || taVehicleBO.getVehicle().getVehicleId()<1)){
	

			throw new RequiredFieldMissingException();
		}
	
		taVehicleBO.setCurrentUsername(taVehicleBO.getCurrentUsername().trim().toUpperCase());
		taVehicleBO.getVehicle().trimVehicleDetails();
		
		TAVehicleDO savedtaVehicleDO = new TAVehicleDO();
		
		savedtaVehicleDO = serviceFactory.getTaVehicleService().findTAVehicleById(taVehicleBO.getTaVehicleId());
		
		if(savedtaVehicleDO==null){
			throw new InvalidFieldException("TA Vehicle does not exist");
		}
		else{
			if(!savedtaVehicleDO.getVehicle().getVehicleId().equals(taVehicleBO.getVehicle().getVehicleId())){
				throw new InvalidFieldException("TA Vehicle, Vehicle ID entered does not match existing. Cannot update Vehicle Id.");
			}
		}
		
		if(savedtaVehicleDO.getStatus().getStatusId().equalsIgnoreCase(Constants.Status.INACTIVE)){
			if(taVehicleBO.getStatusId().equalsIgnoreCase(Constants.Status.ACTIVE)){
				if(activeTAVehicleWithPlateRegNoExists(taVehicleBO.getVehicle().getPlateRegNo())){
					throw new InvalidFieldException("Cannot update the status to Active because an Active TA Vehicle Exists with the same Plate Registration Number");
				}
			}
		}
		
		updated = serviceFactory.getTaVehicleService().updateTAVehicle(taVehicleBO);
		
		if(updated==false){
			throw new ErrorSavingException();
		}
	}
	
	/**
	 * This method checks if a TA Vehicle exists with the Vehicle Id entered
	 * @param vehicleBO
	 * @return
	 * @throws RequiredFieldMissingException
	 */
	public boolean taVehicleExistWithVehicleId(VehicleBO vehicleBO) throws RequiredFieldMissingException{
		boolean taVehicleExistWithVehicleId = false;
		
		if(vehicleBO==null){
			throw new RequiredFieldMissingException();
		}
		
		if(StringUtils.isBlank(vehicleBO.getChassisNo()) || StringUtils.isBlank(vehicleBO.getPlateRegNo()) 
				|| StringUtils.isBlank(vehicleBO.getEngineNo()) || StringUtils.isBlank(vehicleBO.getColour())
				|| (vehicleBO.getYear()==null || vehicleBO.getYear()<1)){
			throw new RequiredFieldMissingException();
		}
		
		vehicleBO.trimVehicleDetails();
		taVehicleExistWithVehicleId =serviceFactory.getTaVehicleService().taVehicleExistWithVehicleId(vehicleBO);
		
		return taVehicleExistWithVehicleId;
	}
	
	
	/**
	 * This method checks if an active TA Vehicle exists with the Plate Registration Number entered
	 * @param plateRegNo
	 * @return
	 * @throws RequiredFieldMissingException
	 */
	public boolean activeTAVehicleWithPlateRegNoExists(String plateRegNo)throws RequiredFieldMissingException{
		boolean activeTAVehicleWithPlateRegNoExists = false;
		
		
		if(StringUtils.isBlank(plateRegNo)){
			throw new RequiredFieldMissingException();
		}
		
		activeTAVehicleWithPlateRegNoExists = serviceFactory.getTaVehicleService().activeTAVehicleWithPlateRegNoExists(plateRegNo.trim());
		
		return activeTAVehicleWithPlateRegNoExists;
	}
	
	/**
	 * This method checks if a TA Vehicle exists with the Plate Registration Number entered
	 * @param plateRegNo
	 * @return
	 * @throws RequiredFieldMissingException
	 */
	public boolean taVehiclePlateRegNoExists(String plateRegNo)throws RequiredFieldMissingException{
		boolean plateRegNoExists = false;
		
		
		if(StringUtils.isBlank(plateRegNo)){
			throw new RequiredFieldMissingException();
		}
		
		plateRegNoExists = serviceFactory.getTaVehicleService().taVehiclePlateRegNoExists(plateRegNo.trim());
		
		return plateRegNoExists;
	}
	
	
	/***********************************************************************************
	 * JP
	 ***********************************************************************************/

	/**
	 * This method is used to lookup JP's based on the search criteria entered
	 * @param jpCriteriaBO
	 * @return
	 * @throws RequiredFieldMissingException
	 * @throws NoRecordFoundException
	 */
	public List<JPBO> lookupJP(JPCriteriaBO jpCriteriaBO)
		throws RequiredFieldMissingException, NoRecordFoundException{
		
		List<JPBO> jpBOList = new ArrayList<JPBO>();
		
		if(jpCriteriaBO==null){
			throw new RequiredFieldMissingException();
		}
		
		/*if(StringUtils.isBlank(jpCriteriaBO.getFirstName()) && StringUtils.isBlank(jpCriteriaBO.getRegNumber())  && StringUtils.isBlank(jpCriteriaBO.getStatusId())
					&& StringUtils.isBlank(jpCriteriaBO.getLastName()) && StringUtils.isBlank(jpCriteriaBO.getMiddleName())
					&& StringUtils.isBlank(jpCriteriaBO.getParishCode()) && StringUtils.isBlank(jpCriteriaBO.getTrnNbr())){
		
			throw new RequiredFieldMissingException();
		}*/
		
		jpBOList = serviceFactory.getJpService().lookupJP(jpCriteriaBO);
		
		if(jpBOList==null || jpBOList.size()==0){
			throw new NoRecordFoundException();
		}
		
		return jpBOList;
	}
	
	/**
	 * This method saved a new JP 
	 * @param jpBO
	 * @throws RequiredFieldMissingException
	 * @throws InvalidFieldException
	 * @throws ErrorSavingException
	 */
	public void saveJP(JPBO jpBO) throws RequiredFieldMissingException, InvalidFieldException, ErrorSavingException{
		boolean saved = false;
		
		if(jpBO==null || jpBO.getPersonBO()==null){
			throw new RequiredFieldMissingException();
		}
		if(StringUtils.isBlank(jpBO.getPersonBO().getFirstName()) || StringUtils.isBlank(jpBO.getRegNumber())
				|| StringUtils.isBlank(jpBO.getPersonBO().getLastName()) || StringUtils.isBlank(jpBO.getCurrentUsername())
				|| StringUtils.isBlank(jpBO.getParishCode()) || StringUtils.isBlank(jpBO.getPersonBO().getTrnNbr())){
	
			throw new RequiredFieldMissingException();
		}
	
		jpBO.setCurrentUsername(jpBO.getCurrentUsername().trim().toUpperCase());
		jpBO.setRegNumber(jpBO.getRegNumber().trim());
		
		boolean validID = validateRegNumber(jpBO.getRegNumber());
		if(validID==false){
			throw new InvalidFieldException("JP Registration Number cannot be greater than 10 characters.");
		}
		if(TRNFormatter.validateTRN(jpBO.getPersonBO().getTrnNbr())==false){
			throw new InvalidFieldException("Invalid TRN.");
		}
		
		JPDO jpDO = new JPDO();
		jpDO = serviceFactory.getJpService().findJPByRegNumber(jpBO.getRegNumber());
		if(jpDO!=null){
			throw new InvalidFieldException("JP Record already exist with the selected Registration Number");
		}
		
		boolean jpExistWithTRN = false;
	
		jpExistWithTRN =serviceFactory.getJpService().jpExistWithTRN(jpBO.getPersonBO().getTrnNbr().trim());
		if(jpExistWithTRN==true){
			throw new InvalidFieldException("JP Record already exist with the TRN entered.");
		}
		
		if(StringUtils.isNotBlank(jpBO.getNewPin()) && StringUtils.isBlank(jpBO.getConfirmedPin()) ){
			throw new RequiredFieldMissingException();
		}
		
		if(StringUtils.isBlank(jpBO.getNewPin()) && StringUtils.isNotBlank(jpBO.getConfirmedPin()) ){
			throw new RequiredFieldMissingException();
		}
		
		if(StringUtils.isNotBlank(jpBO.getNewPin()) && StringUtils.isNotBlank(jpBO.getConfirmedPin())){
			if(!jpBO.getNewPin().trim().equals(jpBO.getConfirmedPin().trim())){
				throw new InvalidFieldException("New Pin and Confirmed Pin does not match");
			}
		}
		
		//validatePhoneNumbers(jpBO.getPersonBO().getTelephoneHome(), jpBO.getPersonBO().getTelephoneWork(), jpBO.getPersonBO().getTelephoneCell());
		
		
		saved = serviceFactory.getJpService().saveJP(jpBO);
		
		if(saved==false){
			throw new ErrorSavingException();
		}
	
	}
	
	
	
	/**
	 * This method updates an existing JP
	 * @param jpBO
	 * @throws RequiredFieldMissingException
	 * @throws InvalidFieldException
	 * @throws ErrorSavingException
	 */
	public void updateJP(JPBO jpBO) throws RequiredFieldMissingException, InvalidFieldException, ErrorSavingException{
		boolean updated = false;
		
		if(jpBO==null || jpBO.getPersonBO()==null){
			throw new RequiredFieldMissingException();
		}
		if(StringUtils.isBlank(jpBO.getPersonBO().getFirstName()) || StringUtils.isBlank(jpBO.getRegNumber())
				|| StringUtils.isBlank(jpBO.getPersonBO().getLastName()) || StringUtils.isBlank(jpBO.getCurrentUsername())
				|| StringUtils.isBlank(jpBO.getParishCode()) || StringUtils.isBlank(jpBO.getPersonBO().getTrnNbr()) || StringUtils.isBlank(jpBO.getStatusId())){
	
			throw new RequiredFieldMissingException();
		}
		
		jpBO.setCurrentUsername(jpBO.getCurrentUsername().trim().toUpperCase());
		jpBO.setRegNumber(jpBO.getRegNumber().trim());
		
		JPDO savedJPDO = new JPDO();
		
		boolean validID = validateRegNumber(jpBO.getRegNumber());
		if(validID==false){
			throw new InvalidFieldException("JP Registration Number cannot be greater than 10 characters.");
		}
		if(TRNFormatter.validateTRN(jpBO.getPersonBO().getTrnNbr())==false){
			throw new InvalidFieldException("Invalid TRN.");
		}
		
		savedJPDO = serviceFactory.getJpService().findJPByRegNumber(jpBO.getRegNumber());
		
		if(savedJPDO!=null && savedJPDO.getIdNumber().intValue()!=jpBO.getIdNumber().intValue()){
			throw new InvalidFieldException("JP Registration Number already exist.");			
		}
				
		
		savedJPDO = serviceFactory.getJpService().findJPById(jpBO.getIdNumber());
		
		
		if(savedJPDO==null){
			throw new InvalidFieldException("JP does not exist");
		}
		else{
			if(!savedJPDO.getPerson().getTrnNbr().equals(jpBO.getPersonBO().getTrnNbr().trim())){
				throw new InvalidFieldException("JP TRN entered does not match existing. Cannot update TRN.");
			}
		}
		
		
		
		if(StringUtils.isNotBlank(jpBO.getNewPin()) && StringUtils.isBlank(jpBO.getConfirmedPin()) ){
			throw new RequiredFieldMissingException();
		}
		
		if(StringUtils.isBlank(jpBO.getNewPin()) && StringUtils.isNotBlank(jpBO.getConfirmedPin()) ){
			throw new RequiredFieldMissingException();
		}
		
		if(StringUtils.isNotBlank(jpBO.getNewPin()) && StringUtils.isNotBlank(jpBO.getConfirmedPin())){
			if(!jpBO.getNewPin().trim().equals(jpBO.getConfirmedPin().trim())){
				throw new InvalidFieldException("New Pin and Confirmed Pin does not match");
			}
			
			/*if(savedJPDO.getPinHash()!=null && StringUtils.isNotBlank(savedJPDO.getPinHash())){
				if(StringUtils.isBlank(jpBO.getOldPin())){
					throw new RequiredFieldMissingException("Old Pin required");
				}
				
				if(!Encryptor.encrypt(jpBO.getOldPin().trim()).equals(savedJPDO.getPinHash())){
					throw new InvalidFieldException("JP Old Pin does not match");
				}
				
				if(Encryptor.encrypt(jpBO.getNewPin().trim()).equals(savedJPDO.getPinHash())){
					throw new InvalidFieldException("New Pin cannot be the same as the Old");
				}
			}*/
					
		}
		
		//validatePhoneNumbers(jpBO.getPersonBO().getTelephoneHome(), jpBO.getPersonBO().getTelephoneWork(), jpBO.getPersonBO().getTelephoneCell());
				
		updated = serviceFactory.getJpService().updateJP(jpBO);
		
		if(updated==false){
			throw new ErrorSavingException();
		}
	}
	
	/**
	 * This method validates a JP Registration Number
	 * @param idNumber
	 * @return
	 */
	private boolean validateRegNumber(String idNumber){
		if(idNumber.length()>10){
			return false;
		}
		
		return true;
	}
	
	/**
	 * This method resets the Pin of a specified JP
	 * @param jpBO
	 * @throws RequiredFieldMissingException
	 * @throws InvalidFieldException
	 * @throws ErrorSavingException
	 */
	public void resetPin(JPBO jpBO)throws RequiredFieldMissingException, InvalidFieldException, ErrorSavingException{
		boolean resetPin = false;
		
		if(jpBO==null){
			throw new RequiredFieldMissingException();
		}
		if(StringUtils.isBlank(jpBO.getRegNumber()) || StringUtils.isBlank(jpBO.getCurrentUsername())){
			throw new RequiredFieldMissingException();
		}
	
		jpBO.setCurrentUsername(jpBO.getCurrentUsername().trim().toUpperCase());
		jpBO.setRegNumber(jpBO.getRegNumber().trim());
		
		boolean validID = validateRegNumber(jpBO.getRegNumber().trim());
		if(validID==false){
			throw new InvalidFieldException("JP Registration Number cannot be greater than 10 characters.");
		}
		
		JPDO savedJPDO = new JPDO();
		
		savedJPDO = serviceFactory.getJpService().findJPByRegNumber(jpBO.getRegNumber());
		if(savedJPDO==null){
			throw new InvalidFieldException("JP does not exist");
		}
		
		resetPin = serviceFactory.getJpService().resetPin(jpBO);
		
		if(resetPin==false){
			throw new ErrorSavingException();
		}
	}
	
	/**
	 * This method checks if a JP exists with the TRN entered
	 * @param trnNbr
	 * @return
	 * @throws RequiredFieldMissingException
	 */
	public boolean jpExistWithTRN(String trnNbr) throws RequiredFieldMissingException{
		boolean jpExistWithTRN = false;
		
		if(StringUtils.isBlank(trnNbr) ){
			throw new RequiredFieldMissingException();
		}
		
		jpExistWithTRN =serviceFactory.getJpService().jpExistWithTRN(trnNbr.trim());
		
		return jpExistWithTRN;
	}
	
	/**
	 * This method checks if a JP already exists with the Reg Number entered
	 * @param regNumber
	 * @return
	 * @throws RequiredFieldMissingException
	 * @throws InvalidFieldException
	 */
	public boolean jpExistWithRegNumber(String regNumber) throws RequiredFieldMissingException, InvalidFieldException{
		JPDO jpDO = new JPDO();
		
		if(StringUtils.isBlank(regNumber) ){
			throw new RequiredFieldMissingException();
		}
		
		boolean validID = validateRegNumber(regNumber.trim());
		if(validID==false){
			throw new InvalidFieldException("JP Registration Number cannot be greater than 10 characters.");
		}
		
		jpDO = serviceFactory.getJpService().findJPByRegNumber(regNumber.trim());
		if(jpDO!=null){
			return true;
		}
		
		return false;
	}
	
	
	/**
	 * This method checks if a JP exists with the TRN entered
	 * @param trnNbr
	 * @return
	 * @throws RequiredFieldMissingException
	 */
	public List<JPBO> getJPListByRegion(String region) throws RequiredFieldMissingException{
		List<JPBO> jpdo = new ArrayList<JPBO>();
		
		if(StringUtils.isBlank(region) ){
			throw new RequiredFieldMissingException();
		}
		
		jpdo =serviceFactory.getJpService().getJPListByRegion(region);
		
		return jpdo;
	}
	/***********************************************************************************
	 * ITA Examiner
	 ***********************************************************************************/
	/**
	 * This method is used to lookup ITA Examiner's based on the search criteria entered
	 * @param itaExaminerCriteriaBO
	 * @return
	 * @throws RequiredFieldMissingException
	 * @throws NoRecordFoundException
	 */
	public List<ITAExaminerBO> lookupITAExaminer(ITAExaminerCriteriaBO itaExaminerCriteriaBO)
			throws RequiredFieldMissingException, NoRecordFoundException{
		
		List<ITAExaminerBO> itaExaminerBOList = new ArrayList<ITAExaminerBO>();
		
		if(itaExaminerCriteriaBO==null){
			throw new RequiredFieldMissingException();
		}
		
		/*if(StringUtils.isBlank(itaExaminerCriteriaBO.getFirstName()) && StringUtils.isBlank(itaExaminerCriteriaBO.getIdNumber()) && StringUtils.isBlank(itaExaminerCriteriaBO.getStatusId())
					&& StringUtils.isBlank(itaExaminerCriteriaBO.getLastName()) && StringUtils.isBlank(itaExaminerCriteriaBO.getMiddleName())
					&& StringUtils.isBlank(itaExaminerCriteriaBO.getOfficeLocationCode()) && StringUtils.isBlank(itaExaminerCriteriaBO.getTrnNbr())){
		
			throw new RequiredFieldMissingException();
		}*/
		
		itaExaminerBOList = serviceFactory.getItaExaminerService().lookupITAExaminer(itaExaminerCriteriaBO);
		
		if(itaExaminerBOList==null || itaExaminerBOList.size()==0){
			throw new NoRecordFoundException();
		}
		
		return itaExaminerBOList;
	}
	
	/**
	 * This method saves a new ITA Examiner
	 * @param itaExaminerBO
	 * @throws RequiredFieldMissingException
	 * @throws InvalidFieldException
	 * @throws ErrorSavingException
	 */
	public void saveITAExaminer(ITAExaminerBO itaExaminerBO)throws RequiredFieldMissingException, InvalidFieldException, ErrorSavingException{
		boolean saved = false;
		
		if(itaExaminerBO==null || itaExaminerBO.getPersonBO()==null){
			throw new RequiredFieldMissingException();
		}
		if(StringUtils.isBlank(itaExaminerBO.getPersonBO().getFirstName()) || StringUtils.isBlank(itaExaminerBO.getExaminerId())
				|| StringUtils.isBlank(itaExaminerBO.getPersonBO().getLastName()) || StringUtils.isBlank(itaExaminerBO.getCurrentUsername())
				|| StringUtils.isBlank(itaExaminerBO.getOfficeLocationCode()) || StringUtils.isBlank(itaExaminerBO.getPersonBO().getTrnNbr())){
	
			throw new RequiredFieldMissingException();
		}
	
		itaExaminerBO.setCurrentUsername(itaExaminerBO.getCurrentUsername().trim().toUpperCase());
		itaExaminerBO.setExaminerId(itaExaminerBO.getExaminerId().trim());
			
		boolean validID = validateIdNumber(itaExaminerBO.getExaminerId());
		if(validID==false){
			throw new InvalidFieldException("Invalid Examiner ID Number. Cannot be greater than 9");
		}
		if(TRNFormatter.validateTRN(itaExaminerBO.getPersonBO().getTrnNbr())==false){
			throw new InvalidFieldException("Invalid TRN.");
		}
		
		ITAExaminerDO itaExaminerDO = new ITAExaminerDO();
		itaExaminerDO = serviceFactory.getItaExaminerService().findITAExaminerByExaminerId(itaExaminerBO.getExaminerId());
		if(itaExaminerDO!=null){
			throw new InvalidFieldException("Another ITA Examiner exists with selected ID Number");
		}
		
		boolean itaExaminerExistWithTRN = false;
	
		itaExaminerExistWithTRN =serviceFactory.getItaExaminerService().itaExaminerExistWithTRN(itaExaminerBO.getPersonBO().getTrnNbr().trim());
		if(itaExaminerExistWithTRN==true){
			throw new InvalidFieldException("Another ITA Examiner exists with selected TRN");
		}
		
		//validatePhoneNumbers(itaExaminerBO.getPersonBO().getTelephoneHome(), itaExaminerBO.getPersonBO().getTelephoneWork(), itaExaminerBO.getPersonBO().getTelephoneCell());
		
		saved = serviceFactory.getItaExaminerService().saveITAExaminer(itaExaminerBO);
		
		if(saved==false){
			throw new ErrorSavingException();
		}
	}
	
	
	/**
	 * This method validated ITA Examiner Id number
	 * @param idNumber
	 * @return
	 */
	private boolean validateIdNumber(String idNumber){
		if(idNumber.length()>9){
			return false;
		}
		
		return true;
	}
	
	/**
	 * This method updates an existing ITA Examiner
	 * @param itaExaminerBO
	 * @throws RequiredFieldMissingException
	 * @throws InvalidFieldException
	 * @throws ErrorSavingException
	 */
	public void updateITAExaminer(ITAExaminerBO itaExaminerBO) throws RequiredFieldMissingException, InvalidFieldException, ErrorSavingException{
		boolean updated = false;
		
		if(itaExaminerBO==null || itaExaminerBO.getPersonBO()==null){
			throw new RequiredFieldMissingException();
		}
		if(StringUtils.isBlank(itaExaminerBO.getPersonBO().getFirstName()) || StringUtils.isBlank(itaExaminerBO.getExaminerId())
				|| StringUtils.isBlank(itaExaminerBO.getPersonBO().getLastName()) || StringUtils.isBlank(itaExaminerBO.getCurrentUsername())
				|| StringUtils.isBlank(itaExaminerBO.getOfficeLocationCode()) || StringUtils.isBlank(itaExaminerBO.getPersonBO().getTrnNbr()) || StringUtils.isBlank(itaExaminerBO.getStatusId())){
	
			throw new RequiredFieldMissingException();
		}
		itaExaminerBO.setCurrentUsername(itaExaminerBO.getCurrentUsername().trim().toUpperCase());
		itaExaminerBO.setExaminerId(itaExaminerBO.getExaminerId().trim());
		
		boolean validID = validateIdNumber(itaExaminerBO.getExaminerId());
		if(validID==false){
			throw new InvalidFieldException("Invalid Examiner ID Number. Cannot be greater than 9");
		}
		if(TRNFormatter.validateTRN(itaExaminerBO.getPersonBO().getTrnNbr())==false){
			throw new InvalidFieldException("Invalid TRN.");
		}
		
		ITAExaminerDO savedITAExaminerDO = new ITAExaminerDO();
		
		savedITAExaminerDO = serviceFactory.getItaExaminerService().findITAExaminerByIdNumber(itaExaminerBO.getIdNumber());
		
		if(savedITAExaminerDO==null){
			throw new InvalidFieldException("ITA Examiner does not exist");
		}
		else{
			if(!savedITAExaminerDO.getPerson().getTrnNbr().equals(itaExaminerBO.getPersonBO().getTrnNbr().trim())){
				throw new InvalidFieldException("ITA Examiner TRN entered does not match existing. Cannot update TRN.");
			}
		}
		
		ITAExaminerDO itaExaminerDO = new ITAExaminerDO();
		itaExaminerDO = serviceFactory.getItaExaminerService().findITAExaminerByExaminerId(itaExaminerBO.getExaminerId());
		
		if(itaExaminerDO!=null && !itaExaminerDO.getIdNumber().equals(itaExaminerBO.getIdNumber())){
			throw new InvalidFieldException("Another ITA Examiner exists with selected ID Number");
		}
		
		//System.err.println("ITA DO id" + itaExaminerDO.getExaminerId());
		//validatePhoneNumbers(itaExaminerBO.getPersonBO().getTelephoneHome(), itaExaminerBO.getPersonBO().getTelephoneWork(), itaExaminerBO.getPersonBO().getTelephoneCell());
		
		updated = serviceFactory.getItaExaminerService().updateITAExaminer(itaExaminerBO);
		
		if(updated==false){
			throw new ErrorSavingException();
		}
	}
	
	/**
	 * This method checks if a ITA Examiner exists with the TRN entered
	 * @param trnNbr
	 * @return
	 * @throws RequiredFieldMissingException
	 */
	public boolean itaExaminerExistWithTRN(String trnNbr) throws RequiredFieldMissingException{
		boolean itaExaminerExistWithTRN = false;
		
		if(StringUtils.isBlank(trnNbr) ){
			throw new RequiredFieldMissingException();
		}
		
		itaExaminerExistWithTRN =serviceFactory.getItaExaminerService().itaExaminerExistWithTRN(trnNbr.trim());
		
		return itaExaminerExistWithTRN;
			
	}
	
	/**
	 * This method checks if an ITA Examiner exists with the Id number entered
	 * @param idNumber
	 * @return
	 * @throws RequiredFieldMissingException
	 * @throws InvalidFieldException
	 */
	public boolean itaExaminerExistWithExaminerId(String examinerIdNumber) throws RequiredFieldMissingException, InvalidFieldException{
		ITAExaminerDO itaExaminerDO = new ITAExaminerDO();
		
		if(StringUtils.isBlank(examinerIdNumber) ){
			throw new RequiredFieldMissingException();
		}
		
		boolean validID = validateIdNumber(examinerIdNumber.trim());
		if(validID==false){
			throw new InvalidFieldException("Invalid Examiner ID Number. Cannot be greater than 5");
		}
		
		itaExaminerDO = serviceFactory.getItaExaminerService().findITAExaminerByExaminerId(examinerIdNumber.trim());
		if(itaExaminerDO!=null){
			return true;
		}
		
		return false;
	}
	
	
	/***********************************************************************************
	 * Offence
	 ***********************************************************************************/
	/**
	 * This method is used to lookup Offences based on the search criteria entered
	 * @param offenceCriteriaBO
	 * @return
	 * @throws RequiredFieldMissingException
	 * @throws NoRecordFoundException
	 */
	public List<OffenceBO> lookupOffence(OffenceCriteriaBO offenceCriteriaBO)
			throws RequiredFieldMissingException, NoRecordFoundException{
		
		List<OffenceBO> offenceBOList = new ArrayList<OffenceBO>();
		
		if(offenceCriteriaBO==null){
			throw new RequiredFieldMissingException();
		}
		
		/*if(StringUtils.isBlank(offenceCriteriaBO.getOffenceDescription()) && StringUtils.isBlank(offenceCriteriaBO.getRoadCheckTypeId()) && StringUtils.isBlank(offenceCriteriaBO.getStatusId())
					&& StringUtils.isBlank(offenceCriteriaBO.getShortDescription()) && (offenceCriteriaBO.getOffenceId()==null || offenceCriteriaBO.getOffenceId()<1)){
		
			throw new RequiredFieldMissingException();
		}*/
		
		offenceBOList = serviceFactory.getOffenceService().lookupOffence(offenceCriteriaBO);
		
		if(offenceBOList==null || offenceBOList.size()==0){
			throw new NoRecordFoundException();
		}
		
		return offenceBOList;
	}
	
	/**
	 * This method saves a new Offence
	 * @param offenceBO
	 * @throws RequiredFieldMissingException
	 * @throws InvalidFieldException
	 * @throws ErrorSavingException
	 */
	public void saveOffence(OffenceBO offenceBO, List<OffenceParamBO> offenceParamList)throws RequiredFieldMissingException, InvalidFieldException, ErrorSavingException{
		boolean saved = false;
		
		if(offenceBO==null){
			throw new RequiredFieldMissingException();
		}
		
		if(StringUtils.isBlank(offenceBO.getCurrentUsername()) || StringUtils.isBlank(offenceBO.getExcerpt())
				|| StringUtils.isBlank(offenceBO.getOffenceDescription()) || StringUtils.isBlank(offenceBO.getRoadCheckTypeId())
				|| StringUtils.isBlank(offenceBO.getShortDescription()) || (offenceBO.getOffenceId()==null || offenceBO.getOffenceId()<1)){
	
			throw new RequiredFieldMissingException();
		}
	
		offenceBO.setCurrentUsername(offenceBO.getCurrentUsername().trim().toUpperCase());
		
		if(offenceBO.getOffenceId()<1){
			throw new InvalidFieldException("Offence Id cannot be less than 1");
		}
		
		OffenceDO offenceDO = new OffenceDO();
		offenceDO = serviceFactory.getOffenceService().findOffenceById(offenceBO.getOffenceId());
	
		if(offenceDO!=null){
			throw new InvalidFieldException("Another Offence exists with Offence Number");
		}
				
		boolean offenceDescExist = offenceDescriptionExist(offenceBO.getOffenceDescription(), offenceBO.getOffenceId());
		
		if(offenceDescExist){
			throw new InvalidFieldException("Offence Description already exists.");
		}
		
		boolean offenceShortDescExist = offenceShortDescriptionExist(offenceBO.getShortDescription(), offenceBO.getOffenceId());
		
		if(offenceShortDescExist){
			throw new InvalidFieldException("Offence Name already exists.");
		}
		
		saved = serviceFactory.getOffenceService().saveOffence(offenceBO, offenceParamList);
		
		if(saved==false){
			throw new ErrorSavingException();
		}
	}

	
	/**
	 * This method updates an existing Offence
	 * @param offenceBO
	 * @throws RequiredFieldMissingException
	 * @throws InvalidFieldException
	 * @throws ErrorSavingException
	 */
	public void updateOffence(OffenceBO offenceBO, List<OffenceParamBO> offenceParamList) throws RequiredFieldMissingException, InvalidFieldException, ErrorSavingException{
		boolean updated = false;
		
		if(offenceBO==null){
			throw new RequiredFieldMissingException();
		}

		if(StringUtils.isBlank(offenceBO.getCurrentUsername()) || StringUtils.isBlank(offenceBO.getExcerpt()) || StringUtils.isBlank(offenceBO.getStatusId())
				|| StringUtils.isBlank(offenceBO.getOffenceDescription()) || StringUtils.isBlank(offenceBO.getRoadCheckTypeId())
				|| StringUtils.isBlank(offenceBO.getShortDescription()) || (offenceBO.getOffenceId()==null || offenceBO.getOffenceId()<1)){
	
			throw new RequiredFieldMissingException();
		}
	
		offenceBO.setCurrentUsername(offenceBO.getCurrentUsername().trim().toUpperCase());
		
		
		OffenceDO savedOffenceDO = new OffenceDO();
		savedOffenceDO = serviceFactory.getOffenceService().findOffenceById(offenceBO.getOffenceId());
	
		if(savedOffenceDO==null){
			throw new InvalidFieldException("Offence does not exist");
		}
		

		boolean offenceDescExist = offenceDescriptionExist(offenceBO.getOffenceDescription(), offenceBO.getOffenceId());
		
		if(offenceDescExist){
			throw new InvalidFieldException("Offence Description already exists.");
		}
		
		boolean offenceShortDescExist = offenceShortDescriptionExist(offenceBO.getShortDescription(), offenceBO.getOffenceId());
		
		if(offenceShortDescExist){
			throw new InvalidFieldException("Offence Short Description already exists.");
		}
		updated = serviceFactory.getOffenceService().updateOffence(offenceBO, offenceParamList);
		
		if(updated==false){
			throw new ErrorSavingException();
		}
	}
	
	
	/**
	 * This method checks if an Offence exists with the Offence Id entered
	 * @param offenceId
	 * @return
	 * @throws RequiredFieldMissingException
	 */
	public boolean offenceExistWithID(Integer offenceId) throws RequiredFieldMissingException{
		OffenceDO offenceDO = new OffenceDO();
		
		if(offenceId==null || offenceId<1){
			throw new RequiredFieldMissingException();
		}
		
	
		offenceDO = serviceFactory.getOffenceService().findOffenceById(offenceId);
		
		if(offenceDO!=null){
			return true;
		}
		
		return false;
	}
	
	/**
	 * This method returns true  if the Description exists  and no offence id passed
	 * or the Description exists  and the offence id is not equal to the offence id passed
	 * @param description
	 * @param offenceId
	 * @return
	 * @throws RequiredFieldMissingException
	 */
	public boolean offenceDescriptionExist(String description, Integer offenceId) throws RequiredFieldMissingException{
		
		if(StringUtils.isBlank(description)){
			throw new RequiredFieldMissingException();
		}
		
		return serviceFactory.getOffenceService().offenceDescriptionExist(description.trim(), offenceId);
	}
	
	/**
	 * This method returns true if the Short Description exists  and no offence id passed
	 * or the Short Description exists and the offence id is not equal to the offence id passed
	 * @param shortDescription
	 * @param offenceId
	 * @return
	 * @throws RequiredFieldMissingException
	 */
	public boolean offenceShortDescriptionExist(String shortDescription, Integer offenceId) throws RequiredFieldMissingException{
		
		if(StringUtils.isBlank(shortDescription)){
			throw new RequiredFieldMissingException();
		}
		
		return serviceFactory.getOffenceService().offenceShortDescriptionExist(shortDescription.trim(), offenceId);
	}
	
	public List<OffenceParamBO> getOffenceParams(Integer offenceId) throws RequiredFieldMissingException, NoRecordFoundException{
		List<OffenceParamBO> offenceParamBOList = new ArrayList<OffenceParamBO>();
		if(offenceId==null){
			throw new RequiredFieldMissingException();
		}
		
		offenceParamBOList = serviceFactory.getOffenceService().findOffenceParams(offenceId);
		
		if(offenceParamBOList==null || offenceParamBOList.size()==0){
			throw new NoRecordFoundException();
		}
		
		return offenceParamBOList;
	}
	
	public List<OffenceLawBO> getOffenceLaws(Integer offenceId) throws RequiredFieldMissingException, NoRecordFoundException{
		List<OffenceLawBO> offenceLawBOList = new ArrayList<OffenceLawBO>();
		if(offenceId==null){
			throw new RequiredFieldMissingException();
		}
		
		offenceLawBOList = serviceFactory.getOffenceService().findOffenceLaws(offenceId);
		
		if(offenceLawBOList==null || offenceLawBOList.size()==0){
			throw new NoRecordFoundException();
		}
		
		return offenceLawBOList;
	}
	
	/***********************************************************************************
	 * Parish
	 ***********************************************************************************/
	
	/**
	 * 
	 * @param criteriaBO
	 * @return
	 * @throws NoRecordFoundException
	 *             ,RequiredFieldMissingException
	 */
	public List<ParishBO> lookupParish(ParishCriteriaBO criteriaBO)
			throws RequiredFieldMissingException, NoRecordFoundException {

		ParishService parishService = serviceFactory.getParishService();

		if (criteriaBO == null)
			throw new RequiredFieldMissingException("Object cannot be empty.");

		List<ParishBO> companiesBOs = parishService.lookupParishes(criteriaBO);

		if (companiesBOs == null || companiesBOs.isEmpty())
			throw new NoRecordFoundException("No records were found.");

		return companiesBOs;

	}

	/**
	 * 
	 * @param parishId
	 * @return
	 * @throws RequiredFieldMissingException
	 * @throws ErrorSavingException
	 */
	public boolean parishExists(String parishId)
			throws RequiredFieldMissingException {

		ParishService parishService = serviceFactory.getParishService();

		if (parishId == null || StringUtils.isBlank(parishId))
			throw new RequiredFieldMissingException("Parish Id is required.");

		boolean exists = parishService.parishExists(parishId);

		return exists;
	}

	/**
	 * 
	 * @param parishBO
	 * @return
	 * @throws RequiredFieldMissingException
	 * @throws InvalidFieldException
	 */
	private String saveParish(ParishBO parishBO) throws ErrorSavingException,
			RequiredFieldMissingException, InvalidFieldException {

		ParishService parishService = serviceFactory.getParishService();

		String serializable = null;

		if (parishBO == null)
			throw new ErrorSavingException("Parish details cannot be empty.");
		
		if (StringUtils.isBlank(parishBO.getParishCode()))
			throw new RequiredFieldMissingException("Parish Code is required.");
		else {
			if (parishExists(parishBO.getParishCode()))
			throw new InvalidFieldException(
					" Parish with this code already exists");
		}


		if (StringUtils.isBlank(parishBO.getDescription()))
			throw new RequiredFieldMissingException("Description is required.");
		else {
			if (parishService.descriptionExists(parishBO))
				throw new InvalidFieldException("Parish Description exists.");

		}

		if (StringUtils.isBlank(parishBO.getOfficeLocationCode()))
			throw new RequiredFieldMissingException(
					"Office Location Code is required.");

		/*if (StringUtils.isBlank(parishBO.getStatusId()))
			throw new RequiredFieldMissingException("Status is required.");

	*/	if (StringUtils.isBlank(parishBO.getCurrentUsername()))
			throw new RequiredFieldMissingException(
					"Creating user is required.");

		try {
			serializable = parishService.saveParish(parishBO);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ErrorSavingException(e.getMessage());
		}

		return serializable;
	}

	/**
	 * 
	 * @param parishBO
	 * @throws InvalidFieldException
	 */
	public void updateParish(ParishBO parishBO) throws ErrorSavingException,
			RequiredFieldMissingException, InvalidFieldException {

		ParishService parishService = serviceFactory.getParishService();

		if (parishBO == null)
			throw new ErrorSavingException("Parish details cannot be empty.");

		if (StringUtils.isBlank(parishBO.getParishCode()))
			throw new RequiredFieldMissingException("Parish Code is required.");
		else {
			if(!parishExists(parishBO.getParishCode())){
				throw new InvalidFieldException(
						"Parish does not exist.");
			}
		}

		if (StringUtils.isBlank(parishBO.getDescription()))
			throw new RequiredFieldMissingException("Description is required.");
		else {
			if (parishService.descriptionExists(parishBO))
				throw new InvalidFieldException("Parish Description exists.");

		}

		if (StringUtils.isBlank(parishBO.getOfficeLocationCode()))
			throw new RequiredFieldMissingException(
					"Office Location Code is required.");

		if (StringUtils.isBlank(parishBO.getStatusId()))
			throw new RequiredFieldMissingException("Status is required.");

		if (StringUtils.isBlank(parishBO.getCurrentUsername()))
			throw new RequiredFieldMissingException(
					"Creating user is required.");

		try {
			parishService.updateParish(parishBO);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ErrorSavingException(e.getMessage());
		}
	}
	
	/***********************************************************************************
	 * Location
	 ***********************************************************************************/
	/**
	 * 
	 * @param criteriaBO
	 * @return
	 * @throws NoRecordFoundException
	 *             ,RequiredFieldMissingException
	 */
	public List<LocationBO> lookupLocation(LocationCriteriaBO criteriaBO)
			throws RequiredFieldMissingException, NoRecordFoundException {

		LocationService locationService = serviceFactory.getLocationService();

		if (criteriaBO == null)
			throw new RequiredFieldMissingException("Object cannot be empty.");

		List<LocationBO> companiesBOs = locationService
				.lookupLocations(criteriaBO);

		if (companiesBOs == null || companiesBOs.isEmpty())
			throw new NoRecordFoundException("No records were found.");

		return companiesBOs;

	}

	/**
	 * 
	 * @param locationId
	 * @return
	 * @throws RequiredFieldMissingException
	 * @throws ErrorSavingException
	 */
	public boolean locationExists(Integer locationId)
			throws RequiredFieldMissingException {

		LocationService locationService = serviceFactory.getLocationService();

		if (locationId == null || locationId <= 0)
			throw new RequiredFieldMissingException("Location Id is required.");

		boolean exists = locationService.locationExists(locationId);

		return exists;
	}

	/**
	 * 
	 * @param locationBO
	 * @return
	 * @throws RequiredFieldMissingException
	 * @throws InvalidFieldException
	 */
	public Integer saveLocation(LocationBO locationBO)
			throws ErrorSavingException, RequiredFieldMissingException,
			InvalidFieldException {

		LocationService locationService = serviceFactory.getLocationService();

		Integer serializable = null;

		if (locationBO == null)
			throw new ErrorSavingException("Location details cannot be empty.");

		if (StringUtils.isBlank(locationBO.getLocationDescription()))
			throw new RequiredFieldMissingException(
					"Location Description is required.");
		/*else {
			if (locationService.descriptionExists(locationBO))
				throw new InvalidFieldException("Location Description exists.");

		}*/

		if (StringUtils.isBlank(locationBO.getShortDesc()))
			throw new RequiredFieldMissingException(
					"Location Name is required.");
		else {
			if (locationService.shortDescriptionExists(locationBO))
				throw new InvalidFieldException(
						"Location already exists.");

		}

		/*if (StringUtils.isBlank(locationBO.getStatusId()))
			throw new RequiredFieldMissingException("Status is required.");
*/
		if (StringUtils.isBlank(locationBO.getParishCode()))
			throw new RequiredFieldMissingException("Parish is required.");

		if (StringUtils.isBlank(locationBO.getCurrentUsername()))
			throw new RequiredFieldMissingException(
					"Creating user is required.");

		try {
			serializable = locationService.saveLocation(locationBO);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ErrorSavingException(e.getMessage());
		}
		 return serializable;
	}

	/**
	 * 
	 * @param locationBO
	 * @throws InvalidFieldException
	 */
	public void updateLocation(LocationBO locationBO)
			throws ErrorSavingException, RequiredFieldMissingException,
			InvalidFieldException {

		LocationService locationService = serviceFactory.getLocationService();

		if (locationBO == null)
			throw new ErrorSavingException("Location details cannot be empty.");

		if (locationBO.getLocationId() == null)
			throw new RequiredFieldMissingException("Location Id is required.");
		else {
			if(!locationExists(locationBO.getLocationId())){
				throw new InvalidFieldException(
						"Location does not exist.");
			}
		}

		if (StringUtils.isBlank(locationBO.getLocationDescription()))
			throw new RequiredFieldMissingException(
					"Location Description is required.");
		/*else {
			if (locationService.descriptionExists(locationBO))
				throw new InvalidFieldException("Location Description exists.");

		}*/

		if (StringUtils.isBlank(locationBO.getShortDesc()))
			throw new RequiredFieldMissingException(
					"Location Name is required.");
		else {
			if (locationService.shortDescriptionExists(locationBO))
				throw new InvalidFieldException(
						"Location already exists.");

		}

		if (StringUtils.isBlank(locationBO.getStatusId()))
			throw new RequiredFieldMissingException("Status is required.");

		if (StringUtils.isBlank(locationBO.getParishCode()))
			throw new RequiredFieldMissingException("Parish is required.");

		if (StringUtils.isBlank(locationBO.getCurrentUsername()))
			throw new RequiredFieldMissingException(
					"Creating user is required.");

		try {
			locationService.updateLocation(locationBO);

		} catch (Exception e) {
			e.printStackTrace();
			throw new ErrorSavingException(e.getMessage());
		}
	}

	
	/***********************************************************************************
	 * Artery
	 ***********************************************************************************/
	/**
	 * 
	 * @param wcCriteriaBO
	 * @return
	 * @throws NoRecordFoundException
	 *             ,RequiredFieldMissingException
	 */
	@RequestMapping("/lookupArtery")
	public @ResponseBody List<ArteryBO> lookupArtery(@RequestBody ArteryCriteriaBO arteryCriteriaBO)
			throws RequiredFieldMissingException, NoRecordFoundException {

		ArteryService arteryService = serviceFactory.getArteryService();

		if (arteryCriteriaBO == null)
			throw new RequiredFieldMissingException(
					"Criteria Object cannot be empty.");

		List<ArteryBO> companiesBOs = arteryService
				.lookupArteries(arteryCriteriaBO);

		if (companiesBOs == null || companiesBOs.isEmpty())
			throw new NoRecordFoundException("No records were found.");

		return companiesBOs;

	}

	/**
	 * 
	 * @param arteryId
	 * @return
	 * @throws RequiredFieldMissingException
	 * @throws ErrorSavingException
	 */
	public boolean arteryExists(Integer arteryId)
			throws RequiredFieldMissingException {

		ArteryService arteryService = serviceFactory.getArteryService();

		if (arteryId == null || arteryId <= 0)
			throw new RequiredFieldMissingException("Artery Id is required.");

		boolean exists = arteryService.arteryExists(arteryId);

		return exists;
	}

	/**
	 * 
	 * @param arteryBO
	 * @return 
	 * @return
	 * @throws RequiredFieldMissingException
	 * @throws InvalidFieldException
	 */
	public Integer saveArtery(ArteryBO arteryBO) throws ErrorSavingException,
			RequiredFieldMissingException, InvalidFieldException {

		ArteryService arteryService = serviceFactory.getArteryService();

		Integer serializable = null;

		if (arteryBO == null)
			throw new ErrorSavingException("Artery details cannot be empty.");

	if (StringUtils.isBlank(arteryBO.getArteryDescription()))
			throw new RequiredFieldMissingException("Description is required.");
		
		
		if (StringUtils.isBlank(arteryBO.getShortDescription()))
			throw new RequiredFieldMissingException(
					"Name is required.");
		else {
			if (arteryService.shortDescriptionExists(arteryBO) && arteryService.locationExists(arteryBO))
				throw new InvalidFieldException(
						"Artery Name already exists.");

		}
		
		if (arteryBO.getLocationId() == null)
			throw new RequiredFieldMissingException("Location is required.");
		else {

		}



		/*if (StringUtils.isBlank(arteryBO.getStatusId()))
			throw new RequiredFieldMissingException("Status is required.");
*/
		/*if (arteryBO.getLatitude() == null)
			throw new RequiredFieldMissingException("Latitude is required.");

		if (arteryBO.getLongitude() == null)
			throw new RequiredFieldMissingException("Longitude is required.");
*/
		if (StringUtils.isBlank(arteryBO.getCurrentUsername()))
			throw new RequiredFieldMissingException(
					"Creating user is required.");

		try {

			serializable = arteryService.saveArtery(arteryBO);

		} catch (Exception e) {
			e.printStackTrace();
			throw new ErrorSavingException(e.getMessage());
		}
		return serializable;
	}

	/**
	 * 
	 * @param arteryBO
	 * @throws InvalidFieldException
	 */
	public void updateArtery(ArteryBO arteryBO) throws ErrorSavingException,
			RequiredFieldMissingException, InvalidFieldException {

		ArteryService arteryService = serviceFactory.getArteryService();
		Integer tempLocId = arteryBO.getLocationId();
		
		//System.out.println(arteryService.shortDescriptionExists(arteryBO)+ " " + arteryService.locationExists(arteryBO) + " Orig Loc " + arteryBO.getLocationId()+ " Temp Loc" +  tempLocId);
		
		if (arteryBO == null)
			throw new ErrorSavingException("Artery details cannot be empty.");

		if (arteryBO.getArteryId() == null)
			throw new ErrorSavingException("Artery Id is required.");
		else {
			if(!arteryExists(arteryBO.getArteryId())){
				throw new InvalidFieldException(
						"Artery does not exist.");
			}
		}

		if (StringUtils.isBlank(arteryBO.getArteryDescription()))
			throw new RequiredFieldMissingException("Description is required.");
		
		

		if (StringUtils.isBlank(arteryBO.getShortDescription()))
			throw new RequiredFieldMissingException(
					"Name is required.");
		else {
			if ((arteryService.shortDescriptionExists(arteryBO)) && (arteryService.locationExists(arteryBO)))
				throw new InvalidFieldException(
						"Artery Name already exists.");

		}
		
		if (arteryBO.getLocationId() == null)
			throw new RequiredFieldMissingException("Location is required.");

		

		if (StringUtils.isBlank(arteryBO.getStatusId()))
			throw new RequiredFieldMissingException("Status is required.");

		/*if (arteryBO.getLatitude() == null)
			throw new RequiredFieldMissingException("Latitude is required.");

		if (arteryBO.getLongitude() == null)
			throw new RequiredFieldMissingException("Longitude is required.");*/

		if (StringUtils.isBlank(arteryBO.getCurrentUsername()))
			throw new RequiredFieldMissingException("Update user is required.");

		try {
			arteryService.updateArtery(arteryBO);

		} catch (Exception e) {
			e.printStackTrace();
			throw new ErrorSavingException(e.getMessage());
		}

	}
	/***********************************************************************************
	 * Pound
	 ***********************************************************************************/
	/**
	 * 
	 * @param criteriaBO
	 * @return
	 * @throws NoRecordFoundException
	 *             ,RequiredFieldMissingException
	 */
	@RequestMapping("/lookupPound")
	public @ResponseBody List<PoundBO> lookupPound(@RequestBody PoundCriteriaBO criteriaBO)
			throws RequiredFieldMissingException, NoRecordFoundException {

		PoundService poundService = serviceFactory.getPoundService();

		if (criteriaBO == null)
			throw new RequiredFieldMissingException("Object cannot be empty.");

		List<PoundBO> companiesBOs = poundService.lookupPounds(criteriaBO);

		if (companiesBOs == null || companiesBOs.isEmpty())
			throw new NoRecordFoundException("No records were found.");

		return companiesBOs;

	}

	/**
	 * 
	 * @param poundId
	 * @return
	 * @throws RequiredFieldMissingException
	 * @throws ErrorSavingException
	 */
	public boolean poundExists(Integer poundId)
			throws RequiredFieldMissingException {

		PoundService poundService = serviceFactory.getPoundService();

		if (poundId == null || poundId <= 0)
			throw new RequiredFieldMissingException("Pound Id is required.");

		boolean exists = poundService.poundExists(poundId);

		return exists;
	}

	/**
	 * 
	 * @param poundBO
	 * @return 
	 * @return
	 * @throws RequiredFieldMissingException
	 * @throws InvalidFieldException
	 */
	public Integer savePound(PoundBO poundBO) throws ErrorSavingException,
			RequiredFieldMissingException, InvalidFieldException {

		PoundService poundService = serviceFactory.getPoundService();

		Integer serializable = null;

		if (poundBO == null)
			throw new ErrorSavingException("Pound details cannot be empty.");
		
		
		if (StringUtils.isBlank(poundBO.getPoundName()))
			throw new RequiredFieldMissingException("Pound Name is required.");
		else {
			if (poundService.poundNameExists(poundBO)){
				//System.err.println("Already Exists");
				throw new InvalidFieldException(
						"Pound already exists.");
				
			}
		}
		
	/*	if (StringUtils.isBlank(poundBO.getShortDesc()))
			throw new RequiredFieldMissingException(
					"Pound Description is required.");*/
		/*System.err.println("Before");
		if (StringUtils.isBlank(poundBO.getShortDesc()))
			throw new RequiredFieldMissingException(
					"Pound Description is required?");
		else {
			System.err.println("In Else");
			if (poundService.shortDescriptionExists(poundBO))
				throw new InvalidFieldException(
						"Pound Description already exists?");

		}*/
/*
		if (StringUtils.isBlank(poundBO.getStatusId()))
			throw new RequiredFieldMissingException("Status is required.");
*/
/*		if (StringUtils.isBlank(poundBO.getParishCode()))
			throw new RequiredFieldMissingException("Parish is required.");*/

		/*if (StringUtils.isBlank(poundBO.getMarkText()))
			throw new RequiredFieldMissingException("Address is required.");
*/
		/*if (StringUtils.isBlank(poundBO.getStreetName()))
			throw new RequiredFieldMissingException("Street Name is required.");*/

		if (StringUtils.isBlank(poundBO.getTelephoneWork())
				&& StringUtils.isBlank(poundBO.getTelephoneCell())
				&& StringUtils.isBlank(poundBO.getTelephoneHome()))
			throw new RequiredFieldMissingException(
					"Telephone Number is required.");

		if (poundBO.getNumberOfLots() == null)
			throw new RequiredFieldMissingException(
					"Number Of Lots is required.");

		if (poundBO.getNumberOfParkingSpaces() == null)
			throw new RequiredFieldMissingException(
					"Number Of Parking Spaces is required.");

		if (StringUtils.isBlank(poundBO.getCurrentUsername()))
			throw new RequiredFieldMissingException(
					"Creating user is required.");

		/**
		 * 
		 */
		//populate addressview to be used by global validation method - 20150616 @kpowell
		AddressView poundAddress = new AddressView(poundBO.getMarkText(), poundBO.getParishCode(), poundBO.getPoBoxNo(),
				poundBO.getPoLocationName(), poundBO.getStreetName(), poundBO.getStreetNo(),
				null, null, null,poundBO.getParishName());
		
		//global address validation - kpowell
		String errorFoundInAddress  = GenericUtil.validateAddress(poundAddress);
        
        if(errorFoundInAddress!= null){
        	throw new RequiredFieldMissingException(errorFoundInAddress);
        }
        
		try {
			serializable = poundService.savePound(poundBO);

		} catch (Exception e) {
			e.printStackTrace();
			throw new ErrorSavingException(e.getMessage());
		}

		 return serializable;
	}

	/**
	 * 
	 * @param poundBO
	 * @throws InvalidFieldException
	 */
	public void updatePound(PoundBO poundBO) throws ErrorSavingException,
			RequiredFieldMissingException, InvalidFieldException {

		PoundService poundService = serviceFactory.getPoundService();

		if (poundBO == null)
			throw new ErrorSavingException("Pound details cannot be empty.");

		if (poundBO.getPoundId() == null)
			throw new RequiredFieldMissingException("Pound Id is required.");
		else {
			if(!poundExists(poundBO.getPoundId())){
				throw new InvalidFieldException(
						"Pound does not exist.");
			}
		}


		if (StringUtils.isBlank(poundBO.getPoundName()))
			throw new RequiredFieldMissingException("Pound Name is required.");
		else {
			if (poundService.poundNameExists(poundBO)){
				
				throw new InvalidFieldException("Pound already exists.");
			}
		}
		
		if (StringUtils.isBlank(poundBO.getShortDesc()))
			throw new RequiredFieldMissingException(
					"Pound Description is required.");
		else {
			if (poundService.shortDescriptionExists(poundBO))
				throw new InvalidFieldException(
						"Pound Description already exists.");

		}
		

		if (StringUtils.isBlank(poundBO.getStatusId()))
			throw new RequiredFieldMissingException("Status is required.");

		/**
		 * 
		 */
		//populate addressview to be used by global validation method - 20150616 @kpowell
		AddressView poundAddress = new AddressView(poundBO.getMarkText(), poundBO.getParishCode(), poundBO.getPoBoxNo(),
				poundBO.getPoLocationName(), poundBO.getStreetName(), poundBO.getStreetNo(),
				null, null, null,poundBO.getParishName());
		
		//global address validation - kpowell
		String errorFoundInAddress  = GenericUtil.validateAddress(poundAddress);
        
        if(errorFoundInAddress!= null){
        	throw new RequiredFieldMissingException(errorFoundInAddress);
        }
		
	//	if (StringUtils.isBlank(poundBO.getParishCode()))
	//		throw new RequiredFieldMissingException("Parish is required.");

		/*if (StringUtils.isBlank(poundBO.getMarkText()))
			throw new RequiredFieldMissingException("Address is required.");

	*/	//if (StringUtils.isBlank(poundBO.getStreetName()))
		//	throw new RequiredFieldMissingException("Street Name is required.");

		if (StringUtils.isBlank(poundBO.getTelephoneWork())
				&& StringUtils.isBlank(poundBO.getTelephoneCell())
				&& StringUtils.isBlank(poundBO.getTelephoneHome()))
			throw new RequiredFieldMissingException(
					"Telephone Number is required.");

		if (poundBO.getNumberOfLots() == null)
			throw new RequiredFieldMissingException(
					"Number Of Lots is required.");

		if (poundBO.getNumberOfParkingSpaces() == null)
			throw new RequiredFieldMissingException(
					"Number Of Parking Spaces is required.");

		if (StringUtils.isBlank(poundBO.getCurrentUsername()))
			throw new RequiredFieldMissingException(
					"Creating user is required.");

		try {
			poundService.updatePound(poundBO);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ErrorSavingException(e.getMessage());
		}
	}
	
	
	
	/***********************************************************************************
	 * Wrecking Company
	 ***********************************************************************************/
	/**
	 * 
	 * @param wcCriteriaBO
	 * @return
	 * @throws NoRecordFoundException
	 *             ,RequiredFieldMissingException
	 */
	@RequestMapping("/lookupWreckingCompany")
	public @ResponseBody List<WreckingCompanyBO> lookupWreckingCompany(
			@RequestBody WreckingCompanyCriteriaBO wcCriteriaBO)
			throws RequiredFieldMissingException, NoRecordFoundException {

		WreckingCompanyService wreckingCompanyService = serviceFactory
				.getWreckingCompanyService();

		if (wcCriteriaBO == null)
			throw new RequiredFieldMissingException("Criteria cannot be empty.");

		List<WreckingCompanyBO> companiesBOs = wreckingCompanyService
				.lookupWreckingCompany(wcCriteriaBO);

		if (companiesBOs == null || companiesBOs.isEmpty())
			throw new NoRecordFoundException("No records were found.");

		return companiesBOs;

	}

	/**
	 * 
	 * @param wreckingCompanyId
	 * @return
	 * @throws RequiredFieldMissingException
	 * @throws ErrorSavingException
	 */
	public boolean wreckingCompanyExists(Integer wreckingCompanyId)
			throws RequiredFieldMissingException {

		WreckingCompanyService wreckingCompanyService = serviceFactory
				.getWreckingCompanyService();

		if (wreckingCompanyId == null || wreckingCompanyId <= 0)
			throw new RequiredFieldMissingException(
					"Wrecker Company Id is required.");

		boolean exists = wreckingCompanyService
				.wreckingCompanyExists(wreckingCompanyId);

		return exists;
	}
	
	/**
	 * 
	 * @param wreckingCompanyBO
	 * @return
	 * @throws RequiredFieldMissingException
	 */
	public boolean wreckerCoTRNAndBranchExists(WreckingCompanyBO wreckingCompanyBO)
			throws RequiredFieldMissingException {

		WreckingCompanyService wreckingCompanyService = serviceFactory
				.getWreckingCompanyService();
		
		if(wreckingCompanyBO != null){
			if (StringUtils.isBlank(wreckingCompanyBO.getTrnNbr()))
				throw new RequiredFieldMissingException(
						"Wrecker Company TRN is required.");
			else if (StringUtils.isBlank(wreckingCompanyBO.getTrnBranch())){
				throw new RequiredFieldMissingException(
						"Wrecker Company Branch is required.");
			}
		}
		
		boolean exists = wreckingCompanyService
				.trnBranchExists(wreckingCompanyBO);

		return exists;
	}
	/**
	 * 
	 * @param wreckingCompanyBO
	 * @return
	 * @throws RequiredFieldMissingException
	 * @throws InvalidFieldException
	 */
	public Integer saveWreckingCompany(WreckingCompanyBO wreckingCompanyBO)
			throws ErrorSavingException, RequiredFieldMissingException,
			InvalidFieldException {

		WreckingCompanyService wreckingCompanyService = serviceFactory
				.getWreckingCompanyService();

		Integer serializable = null;
		//System.out.println(wreckingCompanyBO.getTrnBranch()+"trn branch exist " + wreckingCompanyService.trnBranchExists(wreckingCompanyBO) );
		if (wreckingCompanyBO == null)
			throw new ErrorSavingException(
					"Wrecker Company details cannot be empty.");

		if (StringUtils.isBlank(wreckingCompanyBO.getTrnBranch()))
			throw new RequiredFieldMissingException("Trn Branch is required.");

		if (StringUtils.isBlank(wreckingCompanyBO.getTrnNbr()))
			throw new RequiredFieldMissingException("Trn is required.");

		if (StringUtils.isBlank(wreckingCompanyBO.getCompanyName()))
			throw new RequiredFieldMissingException("Company Name is required.");
		
		if(!(StringUtils.isBlank(wreckingCompanyBO.getTrnBranch()) && StringUtils.isBlank(wreckingCompanyBO.getTrnNbr()))){
			if(wreckingCompanyService.trnBranchExists(wreckingCompanyBO))
			{
				//System.err.println("TRN Branch Exist");
				throw new RequiredFieldMissingException("Wrecker Company already exists with the specified TRN Branch.");
				//throw new RequiredFieldMissingException("Trn Branch already exists.");
			}
		}
		
		/*else {
			System.err.println("Company Name" + wreckingCompanyBO.getCompanyName());
			if (wreckingCompanyService.companyNameExists(wreckingCompanyBO))
				{
				System.err.println("Wrecking Company Exists ");
					if(wreckingCompanyService.trnBranchExists(wreckingCompanyBO))
					{
						System.err.println("TRN Branch Exist");
						throw new RequiredFieldMissingException("Wrecking Company already exists with the specified TRN Branch.");
						//throw new RequiredFieldMissingException("Trn Branch already exists.");
					}
					System.err.println("Outside IF");
				}

		}*/

		/*if (StringUtils.isBlank(wreckingCompanyBO.getShortDesc()))
			throw new RequiredFieldMissingException(
					"Short Description is required.");*/
		/*else {
			if (wreckingCompanyService
					.shortDescriptionExists(wreckingCompanyBO))
				throw new InvalidFieldException(
						"WreckingCompany Short Description exists.");

		}*/

		if (StringUtils.isBlank(wreckingCompanyBO.getContactPersonFirstName()))
			throw new RequiredFieldMissingException(
					"Contact First Name is required.");

		if (StringUtils.isBlank(wreckingCompanyBO.getContactPersonLastName()))
			throw new RequiredFieldMissingException(
					"Contact Last Name is required.");

/*		if (StringUtils.isBlank(wreckingCompanyBO.getParishCode()))
			throw new RequiredFieldMissingException("Parish is required.");*/

	/*	if (StringUtils.isBlank(wreckingCompanyBO.getMarkText()))
			throw new RequiredFieldMissingException("Address is required.");
*/
/*		if (StringUtils.isBlank(wreckingCompanyBO.getStreetName()))
			throw new RequiredFieldMissingException("Street Name is required.");*/
		
		/*if (StringUtils.isBlank(wreckingCompanyBO.getStatusId()))
			throw new RequiredFieldMissingException("Status is required.");
*/

		/**
		 * 
		 */
		//populate addressview to be used by global validation method - 20150616 @kpowell
		AddressView wreckCompAddress = new AddressView(wreckingCompanyBO.getMarkText(), wreckingCompanyBO.getParishCode(), wreckingCompanyBO.getPoBoxNo(),
				wreckingCompanyBO.getPoLocationName(), wreckingCompanyBO.getStreetName(), wreckingCompanyBO.getStreetNo(),
				null, null, null,null);
		
		//global address validation - kpowell
		String errorFoundInAddress  = GenericUtil.validateAddress(wreckCompAddress);
        
        if(errorFoundInAddress!= null){
        	throw new RequiredFieldMissingException(errorFoundInAddress);
        }
        
		if (StringUtils.isBlank(wreckingCompanyBO.getTelephoneWork()))
			throw new RequiredFieldMissingException(
					"Telephone Number is required.");

		if (StringUtils.isBlank(wreckingCompanyBO.getCurrentUsername()))
			throw new RequiredFieldMissingException(
					"Creating user is required.");

		try {

			serializable = wreckingCompanyService
					.saveWreckingCompany(wreckingCompanyBO);

		} catch (Exception e) {
			e.printStackTrace();
			throw new ErrorSavingException(e.getMessage());
		}

		 return serializable;
	}

	/**
	 * 
	 * @param wreckingCompanyBO
	 * @throws InvalidFieldException
	 */
	public void updateWreckingCompany(WreckingCompanyBO wreckingCompanyBO)
			throws ErrorSavingException, RequiredFieldMissingException,
			InvalidFieldException {

		WreckingCompanyService wreckingCompanyService = serviceFactory
				.getWreckingCompanyService();

		if (wreckingCompanyBO == null)
			throw new ErrorSavingException(
					"Wrecker Company details cannot be empty.");

		if (wreckingCompanyBO.getWreckingCompanyId() == null)
			throw new RequiredFieldMissingException(
					"Wrecker Company Id is required.");
		else {
			if(!wreckingCompanyExists(wreckingCompanyBO.getWreckingCompanyId())){
				throw new InvalidFieldException(
						" Wrecker Company does not exist.");
			}
		}

		if (StringUtils.isBlank(wreckingCompanyBO.getTrnBranch()))
			
			throw new RequiredFieldMissingException("Trn Branch is required.");

		if (StringUtils.isBlank(wreckingCompanyBO.getTrnNbr()))
			throw new RequiredFieldMissingException("Trn is required.");

		if (StringUtils.isBlank(wreckingCompanyBO.getCompanyName()))
			throw new RequiredFieldMissingException("Company Name is required.");
		
		//Removed on Feb. 3, 2015 because of ROMS1.0-196
		/*if(!(StringUtils.isBlank(wreckingCompanyBO.getTrnBranch()) && StringUtils.isBlank(wreckingCompanyBO.getTrnNbr()))){
			if(wreckingCompanyService.trnBranchExists(wreckingCompanyBO))
			{
				//System.err.println("TRN Branch Exist");
				throw new RequiredFieldMissingException("Wrecker Company already exists with the specified TRN Branch.");
				//throw new RequiredFieldMissingException("Trn Branch already exists.");
			}
		}*/
		/////////
		
		/*else {
			if (wreckingCompanyService.companyNameExists(wreckingCompanyBO))
				throw new InvalidFieldException("Wrecking Company name exists.");

		}*/

		/*if (StringUtils.isBlank(wreckingCompanyBO.getShortDesc()))
			throw new RequiredFieldMissingException(
					"Short Description is required.");*/
		/*else {
			if (wreckingCompanyService
					.shortDescriptionExists(wreckingCompanyBO))
				throw new InvalidFieldException(
						"WreckingCompany Short Description exists.");

		}*/

		if (StringUtils.isBlank(wreckingCompanyBO.getStatusId()))
			throw new RequiredFieldMissingException("Status is required.");

		
		if (StringUtils.isBlank(wreckingCompanyBO.getContactPersonFirstName()))
			throw new RequiredFieldMissingException(
					"Contact First Name is required.");

		if (StringUtils.isBlank(wreckingCompanyBO.getContactPersonLastName()))
			throw new RequiredFieldMissingException(
					"Contact Last Name is required.");

		/*if (StringUtils.isBlank(wreckingCompanyBO.getParishCode()))
			throw new RequiredFieldMissingException("Parish is required.");
*/
	/*	if (StringUtils.isBlank(wreckingCompanyBO.getMarkText()))
			throw new RequiredFieldMissingException("Address is required.");

	*/	/*if (StringUtils.isBlank(wreckingCompanyBO.getStreetName()))
			throw new RequiredFieldMissingException("Street Name is required.");*/

		/**
		 * 
		 */
		//populate addressview to be used by global validation method - 20150616 @kpowell
		AddressView wreckCompAddress = new AddressView(wreckingCompanyBO.getMarkText(), wreckingCompanyBO.getParishCode(), wreckingCompanyBO.getPoBoxNo(),
				wreckingCompanyBO.getPoLocationName(), wreckingCompanyBO.getStreetName(), wreckingCompanyBO.getStreetNo(),
				null, null, null,null);
		
		//global address validation - kpowell
		String errorFoundInAddress  = GenericUtil.validateAddress(wreckCompAddress);
        
        if(errorFoundInAddress!= null){
        	throw new RequiredFieldMissingException(errorFoundInAddress);
        }
        
		if (StringUtils.isBlank(wreckingCompanyBO.getTelephoneWork()))
			throw new RequiredFieldMissingException(
					"Telephone Number is required.");

		if (StringUtils.isBlank(wreckingCompanyBO.getCurrentUsername()))
			throw new RequiredFieldMissingException(
					"Creating user is required.");

		try {
			wreckingCompanyService.updateWreckingCompany(wreckingCompanyBO);

		} catch (Exception e) {
			e.printStackTrace();
			throw new ErrorSavingException(e.getMessage());
		}
	}
	
	
	/***********************************************************************************
	 * Governing Law
	 ***********************************************************************************/
	/**
	 * 
	 * @param criteriaBO
	 * @return
	 * @throws NoRecordFoundException
	 *             ,RequiredFieldMissingException
	 */
	public List<GoverningLawBO> lookupGoverningLaw(
			GoverningLawCriteriaBO criteriaBO)
			throws RequiredFieldMissingException, NoRecordFoundException {

		GoverningLawService governingLawService = serviceFactory
				.getGoverningLawService();

		if (criteriaBO == null)
			throw new RequiredFieldMissingException("Object cannot be empty.");

		List<GoverningLawBO> companiesBOs = governingLawService
				.lookupGoverningLaws(criteriaBO);

		if (companiesBOs == null || companiesBOs.isEmpty())
			throw new NoRecordFoundException("No records were found.");

		return companiesBOs;

	}

	/**
	 * 
	 * @param governingLawId
	 * @return
	 * @throws RequiredFieldMissingException
	 * @throws ErrorSavingException
	 */
	public boolean governingLawExists(Integer governingLawId)
			throws RequiredFieldMissingException {

		GoverningLawService governingLawService = serviceFactory
				.getGoverningLawService();

		if (governingLawId == null || governingLawId <= 0)
			throw new RequiredFieldMissingException(
					"Governing Law Id is required.");

		boolean exists = governingLawService.governingLawExists(governingLawId);

		return exists;
	}

	/**
	 * 
	 * @param governingLawBO
	 * @return
	 * @throws RequiredFieldMissingException
	 * @throws InvalidFieldException
	 */
	public Integer saveGoverningLaw(GoverningLawBO governingLawBO)
			throws ErrorSavingException, RequiredFieldMissingException,
			InvalidFieldException {

		GoverningLawService governingLawService = serviceFactory
				.getGoverningLawService();

		Integer serializable = null;

		if (governingLawBO == null)
			throw new ErrorSavingException(
					"Governing Law details cannot be empty.");

		if (StringUtils.isBlank(governingLawBO.getDescription()))
			throw new RequiredFieldMissingException("Description is required.");
		else {
			if (governingLawService.descriptionExists(governingLawBO))
				throw new InvalidFieldException(
						"Governing Law Description exists.");

		}

		if (StringUtils.isBlank(governingLawBO.getShortDesc()))
			throw new RequiredFieldMissingException(
					"Short Description is required.");
		else {
			if (governingLawService.shortDescriptionExists(governingLawBO))
				throw new InvalidFieldException(
						"Governing Law Short Description exists.");

		}

	/*	if (StringUtils.isBlank(governingLawBO.getStatusId()))
			throw new RequiredFieldMissingException("Status is required.");
*/
		if (StringUtils.isBlank(governingLawBO.getCurrentUsername()))
			throw new RequiredFieldMissingException(
					"Creating user is required.");

		try {
			serializable = governingLawService.saveGoverningLaw(governingLawBO);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ErrorSavingException(e.getMessage());
		}

		return serializable;
	}

	/**
	 * 
	 * @param governingLawBO
	 * @throws InvalidFieldException
	 */
	public void updateGoverningLaw(GoverningLawBO governingLawBO)
			throws ErrorSavingException, RequiredFieldMissingException,
			InvalidFieldException {

		GoverningLawService governingLawService = serviceFactory
				.getGoverningLawService();

		if (governingLawBO == null)
			throw new ErrorSavingException(
					"Governing Law details cannot be empty.");

		if (governingLawBO.getLawId() == null)
			throw new RequiredFieldMissingException(
					"Governing Law Id is required.");
		else {
			if(!governingLawExists(governingLawBO.getLawId())){
				throw new InvalidFieldException(
						"Governing Law Id does not exist.");
			}
		}

		if (StringUtils.isBlank(governingLawBO.getDescription()))
			throw new RequiredFieldMissingException("Description is required.");
		else {
			if (governingLawService.descriptionExists(governingLawBO))
				throw new InvalidFieldException(
						"Governing Law Description exists.");

		}

		if (StringUtils.isBlank(governingLawBO.getShortDesc()))
			throw new RequiredFieldMissingException(
					"Short Description is required.");
		else {
			if (governingLawService.shortDescriptionExists(governingLawBO))
				throw new InvalidFieldException(
						"Governing Law Short Description exists.");

		}

		if (StringUtils.isBlank(governingLawBO.getStatusId()))
			throw new RequiredFieldMissingException("Status is required.");

		if (StringUtils.isBlank(governingLawBO.getCurrentUsername()))
			throw new RequiredFieldMissingException(
					"Creating user is required.");

		try {
			governingLawService.updateGoverningLaw(governingLawBO);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ErrorSavingException(e.getMessage());
		}
	}
	
	
	/***********************************************************************************
	 * Court
	 ***********************************************************************************/
	/**
	 * 
	 * @param criteriaBO
	 * @return
	 * @throws NoRecordFoundException
	 *             ,RequiredFieldMissingException
	 */
	@RequestMapping("/lookupCourt")
	public @ResponseBody List<CourtBO> lookupCourt(@RequestBody CourtCriteriaBO criteriaBO)
			throws RequiredFieldMissingException, NoRecordFoundException {

		CourtService courtService = serviceFactory.getCourtService();

		if (criteriaBO == null)
			throw new RequiredFieldMissingException("Object cannot be empty.");

		List<CourtBO> companiesBOs = courtService.lookupCourts(criteriaBO);

		if (companiesBOs == null || companiesBOs.isEmpty())
			throw new NoRecordFoundException("No records were found.");

		return companiesBOs;

	}

	/**
	 * 
	 * @param courtId
	 * @return
	 * @throws RequiredFieldMissingException
	 * @throws ErrorSavingException
	 */
	public boolean courtExists(Integer courtId)
			throws RequiredFieldMissingException {

		CourtService courtService = serviceFactory.getCourtService();

		if (courtId == null || courtId <= 0)
			throw new RequiredFieldMissingException("Court Id is required.");

		boolean exists = courtService.courtExists(courtId);

		return exists;
	}

	/**
	 * 
	 * @param courtBO
	 * @return
	 * @return
	 * @throws RequiredFieldMissingException
	 * @throws InvalidFieldException
	 */
	public Integer saveCourt(CourtBO courtBO) throws ErrorSavingException,
			RequiredFieldMissingException, InvalidFieldException {

		CourtService courtService = serviceFactory.getCourtService();

		Integer serializable = null;

		if (courtBO == null)
			throw new ErrorSavingException("Court details cannot be empty.");

		if (StringUtils.isBlank(courtBO.getDescription()))
			throw new RequiredFieldMissingException("Description is required.");
		else {
			if (courtService.descriptionExists(courtBO))
				throw new InvalidFieldException("Court Description exists.");

		}

		if (StringUtils.isBlank(courtBO.getShortDescription()))
			throw new RequiredFieldMissingException(
					"Court Name is required.");
		else {
			if (courtService.shortDescriptionExists(courtBO))
				throw new InvalidFieldException(
						"Court Name exists.");

		}

	/*	if (StringUtils.isBlank(courtBO.getStatusId()))
			throw new RequiredFieldMissingException("Status is required.");
*/
		/*if (courtBO.getParishCode() == null)
			throw new RequiredFieldMissingException("Parish is required.");*/
		/**
		 * 
		 */
		//populate addressview to be used by global validation method - 20150616 @kpowell
		AddressView courtAddress = new AddressView(courtBO.getMarkText(), courtBO.getParishCode(), courtBO.getPoBoxNo(),
				courtBO.getPoLocationName(), courtBO.getStreetName(), courtBO.getStreetNo(),
				null, null, null,courtBO.getParishName());
		
		//global address validation - kpowell
		String errorFoundInAddress  = GenericUtil.validateAddress(courtAddress);
        
        if(errorFoundInAddress!= null){
        	throw new RequiredFieldMissingException(errorFoundInAddress);
        }
        

		if (StringUtils.isBlank(courtBO.getCurrentUsername()))
			throw new RequiredFieldMissingException(
					"Creating user is required.");

		try {
			serializable = courtService.saveCourt(courtBO);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ErrorSavingException(e.getMessage());
		}

		return serializable;
	}

	/**
	 * 
	 * @param courtBO
	 * @throws InvalidFieldException
	 */
	public void updateCourt(CourtBO courtBO) throws ErrorSavingException,
			RequiredFieldMissingException, InvalidFieldException {

		CourtService courtService = serviceFactory.getCourtService();

		if (courtBO == null)
			throw new ErrorSavingException("Court details cannot be empty.");

		if (courtBO.getCourtId() == null)
			throw new RequiredFieldMissingException("Court Id is required.");
		else {
			if(!courtExists(courtBO.getCourtId())){
				throw new InvalidFieldException(
						"Court does not exist.");
			}
		}

		if (StringUtils.isBlank(courtBO.getDescription()))
			throw new RequiredFieldMissingException("Description is required.");
		/*else {
			if (!courtService.descriptionExists(courtBO))
				throw new InvalidFieldException("Court Description does not exists.");

		}*/

		if (StringUtils.isBlank(courtBO.getShortDescription()))
			throw new RequiredFieldMissingException("Short Description is required.");
		/*else {
			if (!courtService.shortDescriptionExists(courtBO))
				throw new InvalidFieldException(
						"Court Short Description does not exists.");

		}*/

		if (StringUtils.isBlank(courtBO.getStatusId()))
			throw new RequiredFieldMissingException("Status is required.");

		/*if (courtBO.getParishCode() == null)
			throw new RequiredFieldMissingException("Parish is required.");*/

		if (StringUtils.isBlank(courtBO.getCurrentUsername()))
			throw new RequiredFieldMissingException(
					"Creating user is required.");

		/**
		 * 
		 */
		//populate addressview to be used by global validation method - 20150616 @kpowell
		AddressView courtAddress = new AddressView(courtBO.getMarkText(), courtBO.getParishCode(), courtBO.getPoBoxNo(),
				courtBO.getPoLocationName(), courtBO.getStreetName(), courtBO.getStreetNo(),
				null, null, null,courtBO.getParishName());
		
		//global address validation - kpowell
		String errorFoundInAddress  = GenericUtil.validateAddress(courtAddress);
        
        if(errorFoundInAddress!= null){
        	throw new RequiredFieldMissingException(errorFoundInAddress);
        }
        
        
		try {
			courtService.updateCourt(courtBO);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ErrorSavingException(e.getMessage());
		}
	}
	
	
	/***********************************************************************************
	 * Configuration
	 ***********************************************************************************/
	
	
	/**
	 * 
	 * @param criteriaBO
	 * @return
	 * @throws NoRecordFoundException
	 *             ,RequiredFieldMissingException
	 */
	public List<ConfigurationBO> lookupConfiguration(
			ConfigurationCriteriaBO criteriaBO)
			throws RequiredFieldMissingException, NoRecordFoundException {

		ConfigurationService configurationService = serviceFactory
				.getConfigurationService();

		if (criteriaBO == null)
			throw new RequiredFieldMissingException("Object cannot be empty.");

		List<ConfigurationBO> companiesBOs = configurationService
				.lookupConfigurations(criteriaBO);

		if (companiesBOs == null || companiesBOs.isEmpty())
			throw new NoRecordFoundException("No records were found.");

		return companiesBOs;

	}

	/**
	 * 
	 * @param configurationId
	 * @return
	 * @throws RequiredFieldMissingException
	 * @throws ErrorSavingException
	 */
	public boolean configurationExists(String configurationId)
			throws RequiredFieldMissingException {

		ConfigurationService configurationService = serviceFactory
				.getConfigurationService();

		if (configurationId == null || StringUtils.isBlank(configurationId))
			throw new RequiredFieldMissingException(
					"Configuration Id is required.");

		boolean exists = configurationService
				.configurationExists(configurationId);

		return exists;
	}

	/**
	 * 
	 * @param configurationBO
	 * @return
	 * @return
	 * @throws RequiredFieldMissingException
	 * @throws InvalidFieldException
	 */
	public String saveConfiguration(ConfigurationBO configurationBO)
			throws ErrorSavingException, RequiredFieldMissingException,
			InvalidFieldException {

		ConfigurationService configurationService = serviceFactory
				.getConfigurationService();

		String serializable = null;

		if (configurationBO == null)
			throw new ErrorSavingException(
					"Configuration details cannot be empty.");

		if (StringUtils.isBlank(configurationBO.getCfgCode()))
			throw new RequiredFieldMissingException(
					"Configuration Code is required.");
		else {
			if (configurationService.configurationExists(configurationBO
					.getCfgCode()))
				throw new InvalidFieldException(
						"Configuration with this code already exists.");
		}

		if (StringUtils.isBlank(configurationBO.getDescription()))
			throw new RequiredFieldMissingException("Description is required.");
		else {
			if (configurationService.descriptionExists(configurationBO))
				throw new InvalidFieldException(
						"Configuration Description exists.");
		}

	/*	if (StringUtils.isBlank(configurationBO.getStatusId()))
			throw new RequiredFieldMissingException("Status is required.");
*/
		if (StringUtils.isBlank(configurationBO.getValue()))
			throw new RequiredFieldMissingException("Value is required.");

		//System.err.println("Data Type ':" + configurationBO.getDataType()+"'");
		if (StringUtils.isBlank(configurationBO.getDataType()))
			throw new RequiredFieldMissingException("Data Type is required.");

		if (configurationBO.getStartDate() == null)
			throw new RequiredFieldMissingException("Start Date is required.");
		
		if (configurationBO.getStartDate() != null && configurationBO.getEndDate() != null) {
			
			if(configurationBO.getStartDate().after(configurationBO.getEndDate()))
					throw new RequiredFieldMissingException("Start Date should not be after End Date.");

		}
			
		/*
		 * if (configurationBO.getEndDate() == null) throw new
		 * RequiredFieldMissingException("End Date is required.");
		 */
		
		if (StringUtils.isBlank(configurationBO.getIsEditableString()))
			throw new RequiredFieldMissingException(
					"Is Editable (Y/N) is required.");
		
		if (StringUtils.isBlank(configurationBO.getIsVisibleString()))
			throw new RequiredFieldMissingException(
					"Is Visible (Y/N) is required.");
		

		if (StringUtils.isBlank(configurationBO.getCurrentUsername()))
			throw new RequiredFieldMissingException(
					"Creating user is required.");

		try {
			serializable = configurationService
					.saveConfiguration(configurationBO);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ErrorSavingException(e.getMessage());
		}

		return serializable;
	}

	/**
	 * 
	 * @param configurationBO
	 * @throws InvalidFieldException
	 */
	public void updateConfiguration(ConfigurationBO configurationBO)
			throws ErrorSavingException, RequiredFieldMissingException,
			InvalidFieldException {

		ConfigurationService configurationService = serviceFactory
				.getConfigurationService();

		if (configurationBO == null)
			throw new ErrorSavingException(
					"Configuration details cannot be empty.");

		if (StringUtils.isBlank(configurationBO.getCfgCode()))
			throw new RequiredFieldMissingException(
					"Configuration Code is required.");
		else {
			if(!configurationExists(configurationBO.getCfgCode())){
				throw new InvalidFieldException(
						"Configuration does not exist.");
			}
		}

		if (StringUtils.isBlank(configurationBO.getDescription()))
			throw new RequiredFieldMissingException("Description is required.");
		else {
			if (configurationService.descriptionExists(configurationBO))
				throw new InvalidFieldException(
						"Configuration Description exists.");
		}

		if (StringUtils.isBlank(configurationBO.getStatusId()))
			throw new RequiredFieldMissingException("Status is required.");

		if (StringUtils.isBlank(configurationBO.getValue()))
			throw new RequiredFieldMissingException("Value is required.");

		if (StringUtils.isBlank(configurationBO.getDataType()))
			throw new RequiredFieldMissingException("Data Type is required.");

		if (configurationBO.getStartDate() == null)
			throw new RequiredFieldMissingException("Start Date is required.");

		if (configurationBO.getEndDate() == null)
			throw new RequiredFieldMissingException("End Date is required.");

		if (StringUtils.isBlank(configurationBO.getIsEditableString()))
			throw new RequiredFieldMissingException(
					"Is Editable (Y/N) is required.");
		
		if (StringUtils.isBlank(configurationBO.getIsVisibleString()))
			throw new RequiredFieldMissingException(
					"Is Visible (Y/N) is required.");
		

		if (StringUtils.isBlank(configurationBO.getCurrentUsername()))
			throw new RequiredFieldMissingException(
					"Creating user is required.");

		try {
			configurationService.updateConfiguration(configurationBO);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ErrorSavingException(e.getMessage());
		}
	}
	
	/**
	 * 
	 * @param username
	 * @returns fullname of user
	 */
	public String usernameToFullName(String username){
			String fullName = "";
			//Goes through the arteryService for no main reason other that the fact that the method is declared there
			ArteryService arteryService = serviceFactory.getArteryService();
			fullName = arteryService.usernameToFullName(username);;
		return fullName;
	}
	
}



