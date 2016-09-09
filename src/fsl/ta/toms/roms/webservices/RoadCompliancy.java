package fsl.ta.toms.roms.webservices;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import fsl.ta.toms.roms.bo.BadgeBO;
import fsl.ta.toms.roms.bo.BadgeCheckResultBO;
import fsl.ta.toms.roms.bo.CitationCheckResultBO;
import fsl.ta.toms.roms.bo.CitationOffenceBO;
import fsl.ta.toms.roms.bo.ComplianceBO;
import fsl.ta.toms.roms.bo.ComplianceDetailsBO;
import fsl.ta.toms.roms.bo.CompliancyCheckBO;
import fsl.ta.toms.roms.bo.CompliancyCheckBOList;
import fsl.ta.toms.roms.bo.DLCheckResultBO;
import fsl.ta.toms.roms.bo.DocumentViewBO;
import fsl.ta.toms.roms.bo.ExcerptParamMappingBO;
import fsl.ta.toms.roms.bo.OffenceMandatoryOutcomeBO;
import fsl.ta.toms.roms.bo.OffenceOutcomeWrapper;
import fsl.ta.toms.roms.bo.PersonBO;
import fsl.ta.toms.roms.bo.RoadLicCheckResultBO;
import fsl.ta.toms.roms.bo.RoadLicVehCheckResultBO;
import fsl.ta.toms.roms.bo.RoadLicenceBO;
import fsl.ta.toms.roms.bo.VehicleBO;
import fsl.ta.toms.roms.bo.VehicleCheckResultBO;
import fsl.ta.toms.roms.constants.Constants;
import fsl.ta.toms.roms.dataobjects.AuditEntry;
import fsl.ta.toms.roms.dataobjects.ComplianceDO;
import fsl.ta.toms.roms.dlwebservice.DriverLicenceDetails;
import fsl.ta.toms.roms.dlwebservice.FslWebServiceException_Exception;
import fsl.ta.toms.roms.exception.ErrorSavingException;
import fsl.ta.toms.roms.exception.InvalidFieldException;
import fsl.ta.toms.roms.exception.NoRecordFoundException;
import fsl.ta.toms.roms.exception.RequiredFieldMissingException;
import fsl.ta.toms.roms.search.criteria.impl.RoadCompliancyCriteriaBO;
import fsl.ta.toms.roms.service.BIMSService;
import fsl.ta.toms.roms.service.LMISService;
import fsl.ta.toms.roms.service.RoadCompliancyService;
import fsl.ta.toms.roms.service.ServiceFactory;
import fsl.ta.toms.roms.service.TicketService;
import fsl.ta.toms.roms.ticketwebservice.TrafficTicket;
import fsl.ta.toms.roms.util.StringUtil;

/**
 * 
 * @author rbrooks
 * Created Date: May 14, 2013
 */
@Controller
@RequestMapping("/roms_rest/roadComp")
public class RoadCompliancy extends SpringBeanAutowiringSupport implements Serializable{

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3348280134812129743L;
	
	
	@Autowired
	private ServiceFactory serviceFactory;
	
	//Intercept all Exceptions thrown as HTTPSTATUS.NOT_ACCEPTABLE -406
	@ExceptionHandler(Exception.class)
    public ResponseEntity<Exception> interceptorInvalidFieldException(HttpServletRequest req, Exception e)  {

          System.err.println(e.getMessage());
          return new ResponseEntity<Exception>(e, HttpStatus.NOT_ACCEPTABLE);
          
    }	
		
		
	public RoadCompliancy() {
		super();
		// TODO Auto-generated constructor stub
	}

	@RequestMapping("/checkValidROMSWSUrl")
	public @ResponseBody Boolean checkValidROMSWSUrl(){
		return true;
	}
	
	@RequestMapping("/saveCompliance")
	public @ResponseBody ComplianceBO saveCompliance(@RequestBody ComplianceBO compliance) throws ErrorSavingException, RequiredFieldMissingException
	{
		//current user is required
		if(!StringUtil.isSet(compliance.getCurrentUserName()))
		{
			throw new RequiredFieldMissingException("Current User is Required");
		}
				
		try
		{
			RoadCompliancyService service = serviceFactory.getRoadCompliancyService();
			ComplianceBO newComplianceBO = service.saveCompliance(compliance);
			return newComplianceBO;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new ErrorSavingException(e.getMessage());
		}
		
	}
	
	
	/**
	 * Returns all offences commited by an individual
	 * @param trn
	 * @param dln
	 * @return 
	 * @throws InvalidFieldException
	 * @throws RequiredFieldMissingException
	 * @throws NoRecordFoundException
	 */
	@RequestMapping("/getCitationOffence")
	public @ResponseBody List<CitationOffenceBO> getCitationOffence(@RequestParam String trn, @RequestParam String dln,@RequestParam String licencePlate,@RequestParam boolean isHandheld) 
			throws InvalidFieldException, RequiredFieldMissingException,NoRecordFoundException
	{
		List<CitationOffenceBO> listOfCitations = new ArrayList<CitationOffenceBO>();		
		
		RoadCompliancyService service = serviceFactory.getRoadCompliancyService();
		listOfCitations = service.getCitationOffence(trn, dln,licencePlate, isHandheld);
		return listOfCitations;
		
		
	}
	

	/**
	 * 
	 * @param badgeCheck
	 * @return
	 * @throws ErrorSavingException
	 * @throws InvalidFieldException
	 * @throws RequiredFieldMissingException
	 * @throws NoRecordFoundException
	 */
	@RequestMapping("/performBadgeCheck")
	public @ResponseBody BadgeCheckResultBO performBadgeCheck( @RequestBody BadgeCheckResultBO badgeCheck)
			throws ErrorSavingException, InvalidFieldException,RequiredFieldMissingException,NoRecordFoundException
	{
		String badgeNo =  badgeCheck.getBadgeNumber();
		String badgeType = badgeCheck.getBadgeType();
		
		if(!StringUtil.isSet(badgeNo) || !StringUtil.isSet(badgeType) ) 
		{
			throw new RequiredFieldMissingException("Badge Number and Badge Type are Required");
		}else
		{//If no compliance ID is sent
			if(badgeCheck.getComplianceID() == null || badgeCheck.getComplianceID() < 1)
			{
				throw new RequiredFieldMissingException("Compliance ID is Required");
			}
		}
		
		//current user is required
		if(!StringUtil.isSet(badgeCheck.getCurrentUserName()))
		{
			throw new RequiredFieldMissingException("Current User is Required");
		}
				
		
		
		
		try
		{
			//Call BIMS Badge Service
			BIMSService bimsService = serviceFactory.getBIMSService();
			BadgeBO badgeDetails = bimsService.getBadgeDetails(badgeNo, badgeType);
			
			//Process Returned Results
			if(badgeDetails != null)  
			{
				RoadCompliancyService roadCompliancyService = serviceFactory.getRoadCompliancyService();
				badgeCheck = roadCompliancyService.processBadgeCheck(badgeCheck,badgeDetails);
			}
			else
			{
				throw new NoRecordFoundException();
			}
		}
		catch(NoRecordFoundException exc)
		{
			serviceFactory.getRoadCompliancyService().createRoadCheckRecord(Constants.RoadCheckType.BADGE,badgeCheck.getComplianceID(), 
					badgeCheck.getCurrentUserName(), badgeCheck.getComment()); 
			
			throw exc;
		}
	
		
		
		return badgeCheck;
		
	}
	

	
	/**
	 * 
	 * @param roadLicCheck
	 * @return
	 * @throws ErrorSavingException
	 * @throws InvalidFieldException
	 * @throws RequiredFieldMissingException
	 * @throws NoRecordFoundException
	 */
	@RequestMapping("/performRoadLicenceCheckByPlateNumber")
	public @ResponseBody RoadLicCheckResultBO performRoadLicenceCheckByPlateNumber( @RequestBody VehicleCheckResultBO mvCheck,@RequestParam boolean isQuickQuery)
			throws ErrorSavingException, InvalidFieldException,RequiredFieldMissingException,NoRecordFoundException
	{
		
		
		
		if(mvCheck.getPlateRegNo() == null || !StringUtil.isSet(mvCheck.getPlateRegNo())) 
		{
			throw new RequiredFieldMissingException("Plate Number is Required");
		}
		
		
		
		//current user is required
		if(!StringUtil.isSet(mvCheck.getCurrentUserName()))
		{
			throw new RequiredFieldMissingException("Current User is Required");
		}
		
		RoadLicCheckResultBO roadLicCheck = new RoadLicCheckResultBO();
		
		roadLicCheck.setCurrentUserName(mvCheck.getCurrentUserName());
		roadLicCheck.setComment(mvCheck.getComment());
		
		if(! isQuickQuery)
		{
			roadLicCheck.setComplianceBO(new ComplianceBO());
			roadLicCheck.getComplianceBO().setComplianceId(mvCheck.getComplianceID());
		}
		
		//Call LMIS Road Licence service
		LMISService lmisService = serviceFactory.getLMISService();
		
		try
		{
			RoadLicenceBO roadLicDetails = lmisService.getRoadLicenceDetails(mvCheck.getPlateRegNo());
			
			
			//Process Returned Results
			if(roadLicDetails != null)  
			{
				roadLicCheck = serviceFactory.getRoadCompliancyService().processRoadLicenceCheck(roadLicCheck,roadLicDetails, isQuickQuery, mvCheck.getPlateRegNo());
			}
			
			
			

		}
		catch(NoRecordFoundException exc)
		{
			if(! isQuickQuery)
			{
				serviceFactory.getRoadCompliancyService().createRoadCheckRecord(Constants.RoadCheckType.ROAD_LICENCE,roadLicCheck.getComplianceBO().getComplianceId(), 
					mvCheck.getCurrentUserName(), mvCheck.getComment()); 
			}
			
			return null;
		}
		catch(RequiredFieldMissingException exc)
		{
			throw exc;
		}
		
		
		
		
		
		return roadLicCheck;
		
	}
	
	@RequestMapping("/performRoadLicMotorVehicleCheck")
	public @ResponseBody RoadLicVehCheckResultBO performRoadLicMotorVehicleCheck(@RequestBody VehicleCheckResultBO mvCheck) throws ErrorSavingException, InvalidFieldException, RequiredFieldMissingException, NoRecordFoundException
	{
		VehicleCheckResultBO vehicleCheckResult = null; 
				
		RoadLicCheckResultBO roadLicCheckResultBO = new RoadLicCheckResultBO();
		
	
		//I-143826
		//roadLicCheckResultBO = this.performRoadLicenceCheckByPlateNumber(mvCheck,false);
		
		LMISService lmisService = serviceFactory.getLMISService();

		RoadLicenceBO roadLicDetails = lmisService.getRoadLicenceDetails(mvCheck.getPlateRegNo());
		
		try
		{
			vehicleCheckResult = performMotorVehicleCheck(mvCheck,false,roadLicDetails);
		}
		catch(NoRecordFoundException e)
		{
			e.printStackTrace();
		}
		
		//I-143826
		//This was added to ensure that road licence check results are being saved - oa- 8 Jan 2015
		roadLicCheckResultBO =  new RoadLicCheckResultBO(roadLicDetails);
		
		ComplianceBO compliance = new ComplianceBO();
		compliance.setComplianceId(mvCheck.getComplianceID());
		
		roadLicCheckResultBO.setComplianceBO(compliance);
		roadLicCheckResultBO.setCurrentUserName(mvCheck.getCurrentUserName());
				
		try{
			roadLicCheckResultBO = serviceFactory.getRoadCompliancyService().processRoadLicenceCheck(roadLicCheckResultBO,roadLicDetails, false, mvCheck.getPlateRegNo());
		}
		catch(Exception e){
			serviceFactory.getRoadCompliancyService().createRoadCheckRecord(Constants.RoadCheckType.ROAD_LICENCE,roadLicCheckResultBO.getComplianceBO().getComplianceId(), 
					mvCheck.getCurrentUserName(), mvCheck.getComment()); 
		}
		
		RoadLicVehCheckResultBO roadLicVehCheckResult = new RoadLicVehCheckResultBO(vehicleCheckResult, roadLicCheckResultBO);
		
		return roadLicVehCheckResult;
	}
	
	@RequestMapping("/performRoadLicMotorVehicleQuery")
	public @ResponseBody RoadLicVehCheckResultBO performRoadLicMotorVehicleQuery(@RequestBody VehicleCheckResultBO mvCheck) throws ErrorSavingException, InvalidFieldException, RequiredFieldMissingException, NoRecordFoundException
	{
		VehicleCheckResultBO vehicleCheckResult = null; 
		
		//RoadLicCheckResultBO roadLicCheckResultBO = null;
		
		RoadLicenceBO roadLicDetails = null;

		//Call LMIS Road Licence service
		LMISService lmisService = serviceFactory.getLMISService();
				
		try
		{
			//I-143826
			//roadLicCheckResultBO = this.performRoadLicenceCheckByPlateNumber(mvCheck,true);
			
			roadLicDetails = lmisService.getRoadLicenceDetails(mvCheck.getPlateRegNo());
			
			/*Only throw no record found exception if the user is not on an active road operation. Section 8 of OSR-ROMS-0001*/
			if(! mvCheck.getOnActiveRoadOperation())
			{
				if(roadLicDetails == null)
				{
					throw new NoRecordFoundException();
				}else
				{
					if(roadLicDetails.getLmisApplicationBO() == null )
					{
						throw new NoRecordFoundException();
					}
				}
			}
			
			
			try
			{
				vehicleCheckResult = performMotorVehicleCheck(mvCheck, true,roadLicDetails);
			}
			catch(Exception e)
			{
				e.printStackTrace();
				//If road licence is found and vehicle is not in AMVS still return the road licence.
			}
			
			
			
			
		
		}
		catch(NoRecordFoundException e)
		{
			e.printStackTrace();
			throw new NoRecordFoundException();
	
		}
		
		//Logics to not show road licence where the plate is a private plate and the road licence is not active
		//This section was commented out by Oneal Anguin due to changes is OSR-0001 where as long as the road licence exist in LMIS show the results
//		if(vehicleCheckResult != null)
//		{
//			if(vehicleCheckResult.getTypeCode().equalsIgnoreCase("pv"))
//			{
//				if(! roadLicCheckResultBO.getStatusCode().equalsIgnoreCase(Constants.LMISLicenceStatusCode.DELIVERED) 
//						&& ! roadLicCheckResultBO.getStatusCode().equalsIgnoreCase(Constants.LMISLicenceStatusCode.PRINTED))
//				{
//					throw new NoRecordFoundException("No active road licence found for private vehicle with plate # " + mvCheck.getPlateRegNo());
//				}
//			}
//		}
		
		return  new RoadLicVehCheckResultBO(vehicleCheckResult, new RoadLicCheckResultBO(roadLicDetails));
	}
	
	
	/**
	 * 
	 * @param roadLicCheck
	 * @return
	 * @throws ErrorSavingException
	 * @throws InvalidFieldException
	 * @throws RequiredFieldMissingException
	 * @throws NoRecordFoundException
	 */
	@RequestMapping("/performCitationHistoryCheck")
	public @ResponseBody CitationCheckResultBO performCitationHistoryCheck(@RequestBody CitationCheckResultBO citationCheck, @RequestParam boolean isHandheld)
			throws ErrorSavingException, InvalidFieldException,RequiredFieldMissingException,NoRecordFoundException
	{
		String trn =  citationCheck.getTrnNbr();
		String dlNo = citationCheck.getDlNo();
		String regPlateNo = citationCheck.getRegPlateNo();
		
		
		List<TrafficTicket> trafficTickets = null;
		List<CitationOffenceBO> romsOffences = null;
		List<CitationOffenceBO> romsOffencesVehicle = null;
		

		
		//compliance ID is required
		if(citationCheck.getComplianceID() == null || citationCheck.getComplianceID() < 1)
		{
			throw new RequiredFieldMissingException("Compliance ID is Required");
		}
		
		
		//current user is required
		if(!StringUtil.isSet(citationCheck.getCurrentUserName()))
		{
			throw new RequiredFieldMissingException("Current User is Required");
		}
				
		
			
		try
		{
			//Call TTMS Traffic Ticket Service
			if(StringUtil.isSet(dlNo) && citationCheck.getIncludeTTMSResults()) 
			{
				TicketService ticketService = serviceFactory.getTicketService();
				//trafficTickets = ticketService.queryTrafficTicket(dlNo, fromDate);
				trafficTickets = ticketService.queryTrafficTicket(dlNo);
				
			}
			
			
			//Call ROMS Offence History Method for person
			if(StringUtil.isSet(trn) || StringUtil.isSet(dlNo)){
				romsOffences = getCitationOffence(trn, dlNo, null, isHandheld);
			}
			
			//Call ROMS Offences History method for vehicle
			if(StringUtil.isSet(citationCheck.getRegPlateNo())){
				romsOffencesVehicle = getCitationOffence(null, null, citationCheck.getRegPlateNo(), isHandheld);
			}
			
			//Process Returned Results
			if(trafficTickets != null || romsOffences !=null || romsOffencesVehicle != null)  
			{
				citationCheck.setCitationOffences(romsOffences);
				citationCheck.setTrafficTickets(trafficTickets);
				citationCheck.setCitationOffencesVehicle(romsOffencesVehicle);
				citationCheck = serviceFactory.getRoadCompliancyService().processCitationHistoryCheck(citationCheck);
			}else
			{
				throw new NoRecordFoundException();
			}
			
			serviceFactory.getRoadCompliancyService().setMaxOffenceValues(citationCheck);
			
			serviceFactory.getRoadCompliancyService().setTotalROMSOffences(citationCheck, trn, dlNo);
		}
		catch(NoRecordFoundException exc)
		{
			serviceFactory.getRoadCompliancyService().createRoadCheckRecord(Constants.RoadCheckType.CITATION,citationCheck.getComplianceID(), 
					citationCheck.getCurrentUserName(), citationCheck.getComment()); 
			
			throw exc;
		}

		
		return citationCheck;
		
	}

	private CitationCheckResultBO getCitationCheckResult(Integer complianceId)
			throws InvalidFieldException,RequiredFieldMissingException,NoRecordFoundException
	{
		
		CitationCheckResultBO citationCheck = serviceFactory.getRoadCompliancyService().getCitationCheck(complianceId);
		
		if(citationCheck == null)
			return null;
		
		String trn =  citationCheck.getTrnNbr();
		String dlNo = citationCheck.getDlNo();
		String regPlateNo = citationCheck.getRegPlateNo();
		String fromDate = "1900-01-01"; //TODO - may need this input from user.
		
		
		List<TrafficTicket> trafficTickets = null;
		List<CitationOffenceBO> romsOffences = null;
		List<CitationOffenceBO> romsVehicleOffences = null;
		

		
		
		//compliance ID is required
		if(citationCheck.getComplianceID() == null || citationCheck.getComplianceID() < 1)
		{
			throw new RequiredFieldMissingException("Compliance ID is Required");
		}
		
		
		//current user is required
		if(!StringUtil.isSet(citationCheck.getCurrentUserName()))
		{
			throw new RequiredFieldMissingException("Current User is Required");
		}
				
		
		//Call TTMS Traffic Ticket Service
		if(StringUtil.isSet(dlNo) && citationCheck.getIncludeTTMSResults()) 
		{
			TicketService ticketService = serviceFactory.getTicketService();
			trafficTickets = ticketService.queryTrafficTicket(dlNo, fromDate);
			
		}
		
		
		//Call ROMS Offence History Method for person
		romsOffences = getCitationOffence(trn, dlNo, null, false);
		
		//Call ROMS Offence History Method for vehicle
		romsVehicleOffences = getCitationOffence(null, null, regPlateNo,false);
		
			
		//Process Returned Results
		if(trafficTickets != null || romsOffences !=null || romsVehicleOffences != null)  
		{
			citationCheck.setCitationOffences(romsOffences);
			citationCheck.setCitationOffencesVehicle(romsVehicleOffences);
			citationCheck.setTrafficTickets(trafficTickets);
	
		}else
		{
			throw new NoRecordFoundException();
		}
				
		return citationCheck;
		
	}
	
	
	@RequestMapping("/performDriversLicenceCheck")
	public @ResponseBody DLCheckResultBO performDriversLicenceCheck( @RequestBody DLCheckResultBO dlicenceCheck)
			throws ErrorSavingException, InvalidFieldException,RequiredFieldMissingException,NoRecordFoundException
	{
		String dlNo =  dlicenceCheck.getDlNo();
		
		if(!StringUtil.isSet(dlNo)) 
		{
			throw new RequiredFieldMissingException("Driver's Licence Number is Required");
		}else
		{
			if (!StringUtil.isNumber(dlNo,false,false))
			{
				throw new InvalidFieldException("Driver's Licence Number is Invalid");
			}
		}
		
		{//If no compliance ID is sent
			if(dlicenceCheck.getComplianceID() == null || dlicenceCheck.getComplianceID() < 1)
			{
				throw new RequiredFieldMissingException("Compliance ID is Required");
			}
		}
		
		//current user is required
		if(!StringUtil.isSet(dlicenceCheck.getCurrentUserName()))
		{
			throw new RequiredFieldMissingException("Current User is Required");
		}
		
		
		try{
			//Call Drivers Licence service
			DLWebService dlService = new DLWebService();
			DriverLicenceDetails dlDetails = dlService.getDriversLicence(dlNo);
		
			//System.err.println("Results " + dlDetails);
			//Process Returned Results
			if(dlDetails != null)  
			{
				
				dlicenceCheck = serviceFactory.getRoadCompliancyService().processDLCheck(dlicenceCheck,dlDetails);
			}else
			{
				throw new NoRecordFoundException();
			}
		}
		catch(FslWebServiceException_Exception fslwe)
		{
			String error = fslwe.getFaultInfo().getErrorCode();
			
			if(error.equals(Constants.FslWebServiceExceptionCodes.NO_REC_FOUND))
			{
				serviceFactory.getRoadCompliancyService().createRoadCheckRecord(Constants.RoadCheckType.DRIVERS_LICENCE,dlicenceCheck.getComplianceID(), 
						dlicenceCheck.getCurrentUserName(), dlicenceCheck.getComment()); 
				
				
				throw new NoRecordFoundException();
			}
			
			
			fslwe.printStackTrace();
		}
		
		
		return dlicenceCheck;
		
	}
	
	
	

	private  VehicleCheckResultBO performMotorVehicleCheck(VehicleCheckResultBO mvCheck, boolean isQuickQuery,RoadLicenceBO roadLicDetails)
			throws ErrorSavingException, InvalidFieldException,RequiredFieldMissingException,NoRecordFoundException
	{
		String plateNo =  mvCheck.getPlateRegNo();
		
		if(!StringUtil.isSet(plateNo)) 
		{
			throw new RequiredFieldMissingException("Plate Registration Number is Required");
		}
		
		if(! isQuickQuery)
		{
			//compliance ID is required
			if(mvCheck.getComplianceID() == null || mvCheck.getComplianceID() < 1)
			{
				throw new RequiredFieldMissingException("Compliance ID is Required");
			}
		}

		//current user is required
		if(!StringUtil.isSet(mvCheck.getCurrentUserName()))
		{
			throw new RequiredFieldMissingException("Current User is Required");
		}
		
		try
		{
			mvCheck = serviceFactory.getRoadCompliancyService().performMVCheck(mvCheck, isQuickQuery,roadLicDetails);
		}
		catch(NoRecordFoundException exc)
		{
			AuditEntry audit = null;
			//prepare audit entry
			 audit = new AuditEntry();
			 audit = new AuditEntry();
			 audit.setCreateUsername(mvCheck.getCurrentUserName());
			 audit.setCreateDTime(Calendar.getInstance().getTime());
		
			 ComplianceDO complianceDO  = null;
		
			if(! isQuickQuery)
			{
				
				
				//Check if valid ComplianceID entered
				 complianceDO = serviceFactory.getRoadCompliancyService().find(ComplianceDO.class, mvCheck.getComplianceID());
					
				 if(complianceDO == null)
				 {throw new InvalidFieldException("Compliance ID Invalid");}
			}
			
			
			serviceFactory.getRoadCompliancyService().doMVCheckEventAudit(isQuickQuery, mvCheck, complianceDO, audit);
			
			
			if(! isQuickQuery)
			{
			serviceFactory.getRoadCompliancyService().createRoadCheckRecord(Constants.RoadCheckType.MOTOR_VEHICLE,mvCheck.getComplianceID(), 
					mvCheck.getCurrentUserName(), mvCheck.getComment()); 
			}
			
					
			throw exc;
		}
	
		
		return mvCheck;
		
	}
	

	
	/**
	 *
	 * This webservice was updated 23 Dec 2103 to return a list of created RoadCheckOffenceOutcomes.
	 * 
	 * @param compliancyCheck
	 * @return
	 * @throws RequiredFieldMissingException
	 * @throws InvalidFieldException
	 * @throws ErrorSavingException
	 *
	 */
	
	@RequestMapping("/recordOffenceOutcome")
	public @ResponseBody List<DocumentViewBO> recordOffenceOutcome( @RequestBody List<CompliancyCheckBO> listOfCompliancyChecks, @RequestBody ComplianceBO updatedComplianceDetails) 
			throws RequiredFieldMissingException, InvalidFieldException, ErrorSavingException
	{
		
		List<DocumentViewBO> allSavedDocs = new ArrayList<DocumentViewBO>();
		
		try
		{
			RoadCompliancyService service = serviceFactory.getRoadCompliancyService();
			allSavedDocs = service.recordOffenceOutcomeHelper(listOfCompliancyChecks, updatedComplianceDetails);
			
			//if no errors are thrown, update compliance record details
			service.saveCompliance(updatedComplianceDetails);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new ErrorSavingException(e.getMessage());
		}	
		
		
		return allSavedDocs;
			
	
	}
	

/**
 * Searches for road compliance activities	
 * @param roadCompliancyCriteriaBO
 * @return
 * @throws InvalidFieldException
 * @throws RequiredFieldMissingException
 * @throws NoRecordFoundException
 */
@RequestMapping("/lookupRoadCompliance")
public @ResponseBody List<ComplianceBO> lookupRoadCompliance( @RequestBody RoadCompliancyCriteriaBO roadCompliancyCriteriaBO)throws InvalidFieldException, RequiredFieldMissingException, NoRecordFoundException{
		
		List<ComplianceBO> complianceBOList = new ArrayList<ComplianceBO>();
		
		if(roadCompliancyCriteriaBO==null){
			throw new RequiredFieldMissingException();
		}
		
		if((roadCompliancyCriteriaBO.getOperationID()==null || roadCompliancyCriteriaBO.getOperationID()<1) 
				&&(roadCompliancyCriteriaBO.getCurrentUserName() == null)
				&& (roadCompliancyCriteriaBO.getFirstName() == null)
				&& (roadCompliancyCriteriaBO.getStatus() == null) 
				&& (roadCompliancyCriteriaBO.getLastName()== null) 
				&& (roadCompliancyCriteriaBO.getOperationName() == null) 
				&& (roadCompliancyCriteriaBO.getTrn() == null)
				&& (roadCompliancyCriteriaBO.getScheduledEndDateRange() == null)
				&& (roadCompliancyCriteriaBO.getScheduledStartDateRange() == null)
				&& (roadCompliancyCriteriaBO.getActualEndDateRange() == null)
				&& (roadCompliancyCriteriaBO.getActualStartDateRange() == null)
				&& (roadCompliancyCriteriaBO.getComplianceId()== null || roadCompliancyCriteriaBO.getComplianceId()<1
				&& (roadCompliancyCriteriaBO.getRoadCheckEndDateRange() == null)
				&& (roadCompliancyCriteriaBO.getRoadCheckStartDateRange() == null))
			)
		{
			throw new RequiredFieldMissingException();
		}
		
		
		
		//if only a start data range is entered for either scheduled or actual, throw exception
		if((roadCompliancyCriteriaBO.getScheduledEndDateRange() != null && roadCompliancyCriteriaBO.getScheduledStartDateRange() == null)
		   || (roadCompliancyCriteriaBO.getScheduledEndDateRange() == null && roadCompliancyCriteriaBO.getScheduledStartDateRange() != null))
		{
			throw new RequiredFieldMissingException("Both a Start and End Date Range Must be Entered");
		}
		
		
		if((roadCompliancyCriteriaBO.getActualEndDateRange() != null && roadCompliancyCriteriaBO.getActualStartDateRange() == null)
		   || (roadCompliancyCriteriaBO.getActualEndDateRange() == null && roadCompliancyCriteriaBO.getActualStartDateRange() != null))
		{
			throw new RequiredFieldMissingException("Both a Start and End Date Range Must be Entered");
		}
		
		if((roadCompliancyCriteriaBO.getRoadCheckEndDateRange() != null && roadCompliancyCriteriaBO.getRoadCheckStartDateRange() == null)
				   || (roadCompliancyCriteriaBO.getRoadCheckEndDateRange() == null && roadCompliancyCriteriaBO.getRoadCheckStartDateRange() != null))
				{
					throw new RequiredFieldMissingException("Both a Start and End Date Range Must be Entered");
				}
				
		
		
		complianceBOList =  serviceFactory.getRoadCompliancyService().lookupRoadComplianceActivities(roadCompliancyCriteriaBO);
		
		if(complianceBOList==null || complianceBOList.size()==0){
			throw new NoRecordFoundException();
		}
		
		return complianceBOList;
	}

	
		
		
		/**
		 * This method is used to retrieve all information related to a compliance activity. 
		 * @param roadOperationId
		 * @return
		 * @throws InvalidFieldException
		 * @throws RequiredFieldMissingException
		 * @throws NoRecordFoundException
		 */
		@RequestMapping("/getComplianceDetails")
		public @ResponseBody ComplianceDetailsBO getComplianceDetails(@RequestParam Integer complianceId) throws InvalidFieldException, RequiredFieldMissingException, NoRecordFoundException{
			ComplianceDetailsBO complianceDetailsBO = new ComplianceDetailsBO();
			ComplianceDO savedComplianceDO = new ComplianceDO();
			
			if(complianceId==null || complianceId<1){
				throw new RequiredFieldMissingException("Compliance ID is Required");
			}
			
			
			savedComplianceDO = serviceFactory.getRoadCompliancyService().find(ComplianceDO.class, complianceId);
			
			if(savedComplianceDO==null){
				throw new InvalidFieldException("Invalid Compliance ID");
			}
			
			complianceDetailsBO = serviceFactory.getRoadCompliancyService().findComplianceDetails(complianceId);
			
			if(complianceDetailsBO==null){
				throw new NoRecordFoundException();
			}
			
			try
			{
				complianceDetailsBO.setCitationCheckResult(this.getCitationCheckResult(complianceId));
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
			return complianceDetailsBO;
		}
		
		@RequestMapping("/getRequiredExcerptParams")
		public @ResponseBody List<ExcerptParamMappingBO> getRequiredExcerptParams(@RequestBody List<Integer> offences) throws RequiredFieldMissingException
		{
			List<ExcerptParamMappingBO> result = new ArrayList<ExcerptParamMappingBO>();
			
			if(offences == null || (offences!= null && offences.isEmpty()))
			{
				throw new RequiredFieldMissingException("At least One Ofence Must be Selected" );
			}
			
			result = serviceFactory.getRoadCompliancyService().findParamsforOffence(offences);
			
			return result;
		}
		
		@RequestMapping("/getActiveOffenceMandatoryOutcomeList")
		public @ResponseBody List<OffenceMandatoryOutcomeBO> getActiveOffenceMandatoryOutcomeList() throws NoRecordFoundException{
			List<OffenceMandatoryOutcomeBO> offenceMandatoryOutcomeBOList = new ArrayList<OffenceMandatoryOutcomeBO>();
			
			offenceMandatoryOutcomeBOList = serviceFactory.getRoadCompliancyService().getActiveOffenceMandatoryOutcomeList();
			
			if(offenceMandatoryOutcomeBOList==null){
				throw new NoRecordFoundException();
			}
			
			return offenceMandatoryOutcomeBOList;
		}
		
		@RequestMapping("/createOtherCheck")
		public @ResponseBody void createOtherCheck( @RequestParam Integer complianceId,@RequestParam String currentUserName) throws InvalidFieldException, RequiredFieldMissingException
		{
			
			//System.err.println("Create other check called");
			if(complianceId == null || complianceId < 0)
				throw new RequiredFieldMissingException("Compliance ID is required." );
			
			if(! StringUtil.isSet(currentUserName))
				throw new RequiredFieldMissingException("Current user name is required." );
			
			serviceFactory.getRoadCompliancyService().createOtherCheck(complianceId, currentUserName);
			
			
		}
		
		@RequestMapping("/getPersonFromDriversLicense")
		public @ResponseBody PersonBO getPersonFromDriversLicense(@RequestParam String dlNo,@RequestParam String currentUserName)throws NoRecordFoundException, RequiredFieldMissingException
		{
			if(StringUtils.isEmpty(dlNo))
			{
				throw new RequiredFieldMissingException("Drivers License Number is required." );
			}
			
			if(StringUtils.isEmpty(currentUserName))
			{
				throw new RequiredFieldMissingException("Current user name is required." );
			}
			
			try
			{
				PersonBO personBO = this.serviceFactory.getRoadCompliancyService().getPersonFromDriversLicense(dlNo, currentUserName);
				
				if(personBO == null)
					throw new NoRecordFoundException();
				else
					return personBO;
				
			}
			catch (Exception e)
			{
				
				e.printStackTrace();
				
				throw new NoRecordFoundException();
			}
			
			
		}
		
		@RequestMapping("/searchForWreckerVehicle")
		public  @ResponseBody VehicleBO searchForWreckerVehicle(@RequestParam String plateRegNo) throws NoRecordFoundException, RequiredFieldMissingException
		{
			if(StringUtils.isEmpty(plateRegNo))
			{
				throw new RequiredFieldMissingException();
			}
			else
			{
				try
				{
					return this.serviceFactory.getRoadCompliancyService().getWreckerVehicle(plateRegNo);
				} 
				catch (Exception e)
				{
					
					 e.printStackTrace();
					 throw new NoRecordFoundException();
				
				}
			}
		}
		
		/**
		 *@author kguthrie
		 * This webservice was created 04 Feb 2016 to return a list of created RoadCheckOffenceOutcomes
		 * created using the mobile app.
		 * 
		 * @param offenceOutcomeWrapper
		 * @return 
		 * @throws RequiredFieldMissingException
		 * @throws InvalidFieldException
		 * @throws ErrorSavingException
		 *
		 */
		@RequestMapping("/recordOffenceOutcomeMobile")
		public @ResponseBody List<DocumentViewBO> recordOffenceOutcomeMobile( 
				@RequestBody OffenceOutcomeWrapper offenceOutcomeWrapper) 
				throws RequiredFieldMissingException, InvalidFieldException, ErrorSavingException
		{
			List<DocumentViewBO> allSavedDocs = new ArrayList<DocumentViewBO>();
			try {
				allSavedDocs = recordOffenceOutcome(offenceOutcomeWrapper.getListOfCompliancyChecks(),
						offenceOutcomeWrapper.getUpdatedComplianceDetails());
	
			} catch (Exception e) {
				e.printStackTrace();
				throw new ErrorSavingException(e.getMessage());
			}
	
			return allSavedDocs;
				
		
		}
}
