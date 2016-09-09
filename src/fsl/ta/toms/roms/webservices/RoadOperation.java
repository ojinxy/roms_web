package fsl.ta.toms.roms.webservices;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import fsl.ta.toms.roms.bo.ArteryBO;
import fsl.ta.toms.roms.bo.AssignedPersonBO;
import fsl.ta.toms.roms.bo.AssignedResourceBO;
import fsl.ta.toms.roms.bo.AssignedTeamDetailsBO;
import fsl.ta.toms.roms.bo.AssignedVehicleBO;
import fsl.ta.toms.roms.bo.AvailableResourceBO;
import fsl.ta.toms.roms.bo.AvailableResourceIds;
import fsl.ta.toms.roms.bo.ITAExaminerBO;
import fsl.ta.toms.roms.bo.JPBO;
import fsl.ta.toms.roms.bo.LMISUserViewBO;
import fsl.ta.toms.roms.bo.LocationBO;
import fsl.ta.toms.roms.bo.OperationLocationBO;
import fsl.ta.toms.roms.bo.OperationStrategyBO;
import fsl.ta.toms.roms.bo.ParishBO;
import fsl.ta.toms.roms.bo.PoliceOfficerBO;
import fsl.ta.toms.roms.bo.RoadOpSearchResultsMobileBO;
import fsl.ta.toms.roms.bo.RoadOperationBO;
import fsl.ta.toms.roms.bo.RoadOperationOtherDetailsBO;
import fsl.ta.toms.roms.bo.RoadOperationUpdateCustomBO;
import fsl.ta.toms.roms.bo.StrategyBO;
import fsl.ta.toms.roms.bo.StrategyRuleBO;
import fsl.ta.toms.roms.bo.TAStaffBO;
import fsl.ta.toms.roms.bo.TAVehicleBO;
import fsl.ta.toms.roms.constants.Constants;
import fsl.ta.toms.roms.constants.Constants.PersonType;
import fsl.ta.toms.roms.dataobjects.ArteryDO;
import fsl.ta.toms.roms.dataobjects.LocationDO;
import fsl.ta.toms.roms.dataobjects.ParishDO;
import fsl.ta.toms.roms.dataobjects.RoadOperationDO;
import fsl.ta.toms.roms.dataobjects.TAVehicleDO;
import fsl.ta.toms.roms.exception.ErrorSavingException;
import fsl.ta.toms.roms.exception.InvalidFieldException;
import fsl.ta.toms.roms.exception.NoRecordFoundException;
import fsl.ta.toms.roms.exception.RequiredFieldMissingException;
import fsl.ta.toms.roms.search.criteria.impl.AvailableResourceCriteriaBO;
import fsl.ta.toms.roms.search.criteria.impl.RoadOperationCriteriaBO;
import fsl.ta.toms.roms.service.ServiceFactory;
import fsl.ta.toms.roms.util.DateUtils;
import fsl.ta.toms.roms.util.GenericReferenceVO;


@Controller
@RequestMapping("/roms_rest/roadOp")
public class RoadOperation extends SpringBeanAutowiringSupport{

	@Autowired
	private ServiceFactory serviceFactory;
	
	//Format All java.util.Date for rest calls
	@InitBinder
	public void initBinder(WebDataBinder binder) {
	    SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");//java.util.Date
	    sdf.setLenient(true);
	    binder.registerCustomEditor(Date.class, new CustomDateEditor(sdf, true));
	}
	
	//Intercept all Exceptions thrown as HTTPSTATUS.NOT_ACCEPTABLE -406
	@ExceptionHandler(Exception.class)
    public ResponseEntity<Exception> interceptorInvalidFieldException(HttpServletRequest req, Exception e)  {

          System.err.println(e.getMessage());
          return new ResponseEntity<Exception>(e, HttpStatus.NOT_ACCEPTABLE);
          
    }	
	
	/**
	 * This method is used to lookup Road Operations based on the search criteria entered
	 * @param roadOperationCriteriaBO
	 * @return
	 * @throws InvalidFieldException
	 * @throws RequiredFieldMissingException
	 * @throws NoRecordFoundException
	 */
	@RequestMapping("/lookupRoadOperation")
	public @ResponseBody List<RoadOperationBO> lookupRoadOperation(@RequestBody RoadOperationCriteriaBO roadOperationCriteriaBO)throws InvalidFieldException, RequiredFieldMissingException, NoRecordFoundException{
		
		List<RoadOperationBO> roadOperationBOList = new ArrayList<RoadOperationBO>();
		
		if(roadOperationCriteriaBO==null){
			throw new RequiredFieldMissingException();
		}
		
		if((roadOperationCriteriaBO.getArteryId()==null || roadOperationCriteriaBO.getArteryId()<1) && (roadOperationCriteriaBO.getRoadOperationId()==null || roadOperationCriteriaBO.getRoadOperationId()<1)
						&& StringUtils.isBlank(roadOperationCriteriaBO.getCategoryId()) && (roadOperationCriteriaBO.getLocationId()==null || roadOperationCriteriaBO.getLocationId()<1) && StringUtils.isBlank(roadOperationCriteriaBO.getOfficeLocCode())
						&& roadOperationCriteriaBO.getOperationEndDate()==null && StringUtils.isBlank(roadOperationCriteriaBO.getOperationEndTime()) && StringUtils.isBlank(roadOperationCriteriaBO.getOperationName()) 
						&& roadOperationCriteriaBO.getOperationStartDate()==null && StringUtils.isBlank(roadOperationCriteriaBO.getOperationStartTime()) && StringUtils.isBlank(roadOperationCriteriaBO.getParishCode()) 
						&& StringUtils.isBlank(roadOperationCriteriaBO.getSchedulerStaffId()) && StringUtils.isBlank(roadOperationCriteriaBO.getStatusId()) 
						&& (roadOperationCriteriaBO.getStrategyId()==null || roadOperationCriteriaBO.getStrategyId()<1) && StringUtils.isBlank(roadOperationCriteriaBO.getTeamLeadStaffId())){
			
			throw new RequiredFieldMissingException();
		}
		
		
		
		if(StringUtils.isNotBlank(roadOperationCriteriaBO.getOperationStartTime()) && roadOperationCriteriaBO.getOperationStartDate()==null){
			throw new RequiredFieldMissingException();
		}
		
		if(StringUtils.isNotBlank(roadOperationCriteriaBO.getOperationEndTime()) && roadOperationCriteriaBO.getOperationEndDate()==null){
			throw new RequiredFieldMissingException();
		}
				
		if((roadOperationCriteriaBO.isBackDated()!=false &&  roadOperationCriteriaBO.isBackDated()!=true) || (roadOperationCriteriaBO.isScheduledDTime()!=false &&  roadOperationCriteriaBO.isScheduledDTime()!=true)){
			throw new InvalidFieldException();
		}	
		
		if(StringUtils.isNotBlank(roadOperationCriteriaBO.getOperationStartTime())){
			validateTime(roadOperationCriteriaBO.getOperationStartTime());
			roadOperationCriteriaBO.setOperationStartTime(roadOperationCriteriaBO.getOperationStartTime().concat(":00"));
		}
		

		if(StringUtils.isNotBlank(roadOperationCriteriaBO.getOperationEndTime())){
			validateTime(roadOperationCriteriaBO.getOperationEndTime());
			roadOperationCriteriaBO.setOperationEndTime(roadOperationCriteriaBO.getOperationEndTime().concat(":00"));
		}
			
		try {
			if(roadOperationCriteriaBO.getOperationStartDate()!=null){
					DateUtils.utilDateToSqlDate(roadOperationCriteriaBO.getOperationStartDate());
			}
			
			if(roadOperationCriteriaBO.getOperationEndDate()!= null){			
					DateUtils.utilDateToSqlDate(roadOperationCriteriaBO.getOperationEndDate());
			}
		}
		catch (ParseException e) {
			e.printStackTrace();
			throw new InvalidFieldException();
		}
	
		roadOperationBOList =  serviceFactory.getRoadOperationService().lookupRoadOperation(roadOperationCriteriaBO);
		
		if(roadOperationBOList==null || roadOperationBOList.size()==0){
			throw new NoRecordFoundException();
		}
		
		return roadOperationBOList;
	}
	
	/**
	 * This method is used to lookup Road Operations based on the search criteria entered.
	 * It uses a date range for filter.
	 * @param roadOperationCriteriaBO
	 * @return
	 * @throws InvalidFieldException
	 * @throws RequiredFieldMissingException
	 * @throws NoRecordFoundException
	 */
	public List<RoadOperationBO> lookupRoadOperationWithDateRange(RoadOperationCriteriaBO roadOperationCriteriaBO)throws InvalidFieldException, RequiredFieldMissingException, NoRecordFoundException{
		
		List<RoadOperationBO> roadOperationBOList = new ArrayList<RoadOperationBO>();
		
		if(roadOperationCriteriaBO==null){
			throw new RequiredFieldMissingException();
		}
		
		if((roadOperationCriteriaBO.getArteryId()==null || roadOperationCriteriaBO.getArteryId()<1) && (roadOperationCriteriaBO.getRoadOperationId()==null || roadOperationCriteriaBO.getRoadOperationId()<1)
						&& StringUtils.isBlank(roadOperationCriteriaBO.getCategoryId()) && (roadOperationCriteriaBO.getLocationId()==null || roadOperationCriteriaBO.getLocationId()<1) && StringUtils.isBlank(roadOperationCriteriaBO.getOfficeLocCode())
						&& roadOperationCriteriaBO.getOperationEndDate()==null && StringUtils.isBlank(roadOperationCriteriaBO.getOperationEndTime()) && StringUtils.isBlank(roadOperationCriteriaBO.getOperationName()) 
						&& roadOperationCriteriaBO.getOperationStartDate()==null && StringUtils.isBlank(roadOperationCriteriaBO.getOperationStartTime()) && StringUtils.isBlank(roadOperationCriteriaBO.getParishCode()) 
						&& StringUtils.isBlank(roadOperationCriteriaBO.getSchedulerStaffId()) && StringUtils.isBlank(roadOperationCriteriaBO.getStatusId()) 
						&& (roadOperationCriteriaBO.getStrategyId()==null || roadOperationCriteriaBO.getStrategyId()<1) && StringUtils.isBlank(roadOperationCriteriaBO.getTeamLeadStaffId())){
			
			throw new RequiredFieldMissingException();
		}
		
		
		
		if(StringUtils.isNotBlank(roadOperationCriteriaBO.getOperationStartTime()) && roadOperationCriteriaBO.getOperationStartDate()==null){
			throw new RequiredFieldMissingException();
		}
		
		if(StringUtils.isNotBlank(roadOperationCriteriaBO.getOperationEndTime()) && roadOperationCriteriaBO.getOperationEndDate()==null){
			throw new RequiredFieldMissingException();
		}
				
		if((roadOperationCriteriaBO.isBackDated()!=false &&  roadOperationCriteriaBO.isBackDated()!=true) || (roadOperationCriteriaBO.isScheduledDTime()!=false &&  roadOperationCriteriaBO.isScheduledDTime()!=true)){
			throw new InvalidFieldException();
		}	
		
		if(StringUtils.isNotBlank(roadOperationCriteriaBO.getOperationStartTime())){
			validateTime(roadOperationCriteriaBO.getOperationStartTime());
			roadOperationCriteriaBO.setOperationStartTime(roadOperationCriteriaBO.getOperationStartTime().concat(":00"));
		}
		

		if(StringUtils.isNotBlank(roadOperationCriteriaBO.getOperationEndTime())){
			validateTime(roadOperationCriteriaBO.getOperationEndTime());
			roadOperationCriteriaBO.setOperationEndTime(roadOperationCriteriaBO.getOperationEndTime().concat(":00"));
		}
			
		try {
			if(roadOperationCriteriaBO.getOperationStartDate()!=null){
					DateUtils.utilDateToSqlDate(roadOperationCriteriaBO.getOperationStartDate());
			}
			
			if(roadOperationCriteriaBO.getOperationEndDate()!= null){			
					DateUtils.utilDateToSqlDate(roadOperationCriteriaBO.getOperationEndDate());
			}
		}
		catch (ParseException e) {
			e.printStackTrace();
			throw new InvalidFieldException();
		}
	
		roadOperationBOList =  serviceFactory.getRoadOperationService().lookupRoadOperationWithDateRange(roadOperationCriteriaBO);
		
		if(roadOperationBOList==null || roadOperationBOList.size()==0){
			throw new NoRecordFoundException();
		}
		
		return roadOperationBOList;
	}
	
	/**
	 * This method is used to retrieve the Road Operation Other Details such as Assigned Person(s),
	 *  Assigned Vehicle(s), Operation Location(s), Operation Strategy(s) and Operation Artery(s)
	 * @param roadOperationId
	 * @return
	 * @throws InvalidFieldException
	 * @throws RequiredFieldMissingException
	 * @throws NoRecordFoundException
	 */
	@RequestMapping("/findRoadOperationOtherDetails")
	public @ResponseBody RoadOperationOtherDetailsBO findRoadOperationOtherDetails(@RequestParam Integer roadOperationId) throws InvalidFieldException, RequiredFieldMissingException, NoRecordFoundException{
		RoadOperationOtherDetailsBO roadOperationOtherDetailsBO = new RoadOperationOtherDetailsBO();
		RoadOperationDO savedOperationDO = new RoadOperationDO();
		
		if(roadOperationId==null || roadOperationId<1){
			throw new RequiredFieldMissingException();
		}
		
		
		savedOperationDO = serviceFactory.getRoadOperationService().findRoadOperationById(roadOperationId);
		
		if(savedOperationDO==null){
			throw new InvalidFieldException();
		}
		
		roadOperationOtherDetailsBO = serviceFactory.getRoadOperationService().findRoadOperationOtherDetails(roadOperationId);
		
		if(roadOperationOtherDetailsBO==null){
			throw new NoRecordFoundException();
		}
		
		return roadOperationOtherDetailsBO;
	}
	
	/**
	 * This method is used to retrieve a TA Staff by Person Id
	 * @param personId
	 * @return
	 * @throws RequiredFieldMissingException
	 * @throws NoRecordFoundException
	 */
	public TAStaffBO findTAStaffByPersonID(Integer personId) throws RequiredFieldMissingException, NoRecordFoundException{
		TAStaffBO staffBO = new TAStaffBO();
		
		if(personId==null || personId<1){
			throw new RequiredFieldMissingException();
		}
		
		staffBO= serviceFactory.getRoadOperationService().findTAStaffByPersonID(personId);
		
		if(staffBO == null){
			throw new NoRecordFoundException();
		}
		
		return staffBO;
	}
	
	/**
	 * This method is used to retrieve a JP by Person Id
	 * @param personId
	 * @return
	 * @throws RequiredFieldMissingException
	 * @throws NoRecordFoundException
	 */
	public JPBO findJPByPersonID(Integer personId) throws RequiredFieldMissingException, NoRecordFoundException{
		JPBO jpBO = new JPBO();
		
		if(personId==null || personId<1){
			throw new RequiredFieldMissingException();
		}
		
		jpBO= serviceFactory.getRoadOperationService().findJPByPersonID(personId);
		
		if(jpBO == null){
			throw new NoRecordFoundException();
		}
		
		return jpBO;
	}

	
	/**
	 * This method is used to retrieve a ITA Examiner by Person Id
	 * @param personId
	 * @return
	 * @throws RequiredFieldMissingException
	 * @throws NoRecordFoundException
	 */
	public ITAExaminerBO findITAExaminerByPersonID(Integer personId) throws RequiredFieldMissingException, NoRecordFoundException{
		ITAExaminerBO itaExaminerBO = new ITAExaminerBO();
		
		if(personId==null || personId<1){
			throw new RequiredFieldMissingException();
		}
		
		itaExaminerBO= serviceFactory.getRoadOperationService().findITAExaminerByPersonID(personId);
		
		if(itaExaminerBO == null){
			throw new NoRecordFoundException();
		}
		
		return itaExaminerBO;
	}
	
	/**
	 * This method is used to retrieve a Police Officer by Person Id
	 * @param personId
	 * @return
	 * @throws RequiredFieldMissingException
	 * @throws NoRecordFoundException
	 */
	public PoliceOfficerBO findPoliceOfficerByPersonID(Integer personId) throws RequiredFieldMissingException, NoRecordFoundException{
		PoliceOfficerBO policeOfficerBO = new PoliceOfficerBO();
		
		if(personId==null || personId<1){
			throw new RequiredFieldMissingException();
		}
		
		policeOfficerBO= serviceFactory.getRoadOperationService().findPoliceOfficerByPersonID(personId);
		
		if(policeOfficerBO == null){
			throw new NoRecordFoundException();
		}
		
		return policeOfficerBO;
	}
	
	/**
	 * This method retrieves the Available TA Staff, JP, Police Officer, ITA Examiner and TA Vehicle based on the search criteria entered
	 * @param availableResourceCriteriaBO
	 * @return
	 * @throws RequiredFieldMissingException
	 * @throws InvalidFieldException
	 * @throws NoRecordFoundException
	 */
	public AvailableResourceBO getAvailableResource(AvailableResourceCriteriaBO availableResourceCriteriaBO) throws RequiredFieldMissingException, InvalidFieldException, NoRecordFoundException{
		AvailableResourceBO availableResourceBO = new AvailableResourceBO();
		
		validateAvailResCriteria(availableResourceCriteriaBO,null);
		
		availableResourceBO = serviceFactory.getRoadOperationService().getAvailableResource(availableResourceCriteriaBO);
		
		if(availableResourceBO==null){
			throw new NoRecordFoundException();
		}
		
		return availableResourceBO;
	}
	

	
	private void validateAvailResCriteria(AvailableResourceCriteriaBO availableResourceCriteriaBO, String type) throws RequiredFieldMissingException, InvalidFieldException
	{
		try{
			if(availableResourceCriteriaBO==null) {
				throw new RequiredFieldMissingException();
			}
			
			if(StringUtils.isBlank(availableResourceCriteriaBO.getCategoryId()) || availableResourceCriteriaBO.getScheduledStartDate()==null || availableResourceCriteriaBO.getScheduledEndDate()==null
					|| StringUtils.isBlank(availableResourceCriteriaBO.getScheduledEndTime()) || StringUtils.isBlank(availableResourceCriteriaBO.getScheduledStartTime()) || availableResourceCriteriaBO.getOfficeLocCodeList()==null){
				throw new RequiredFieldMissingException();
			}
			
			if(type.equals(PersonType.JP) && (availableResourceCriteriaBO.getParishCodeList()==null || availableResourceCriteriaBO.getParishCodeList().size()<=0)){
				throw new RequiredFieldMissingException();
			}
			
			if(availableResourceCriteriaBO.getOfficeLocCodeList()==null || availableResourceCriteriaBO.getOfficeLocCodeList().size()<=0){
				throw new RequiredFieldMissingException();
			}
			
			//Validate Scheduled Start Time
			if(availableResourceCriteriaBO.getScheduledStartTime().length() < 6)
			{
				validateTime(availableResourceCriteriaBO.getScheduledStartTime());
				availableResourceCriteriaBO.setScheduledStartTime(availableResourceCriteriaBO.getScheduledStartTime().concat(":00"));
			}
			
			//Validate Scheduled End Time
			if(availableResourceCriteriaBO.getScheduledEndTime().length() < 6)
			{
				validateTime(availableResourceCriteriaBO.getScheduledEndTime());
				availableResourceCriteriaBO.setScheduledEndTime(availableResourceCriteriaBO.getScheduledEndTime().concat(":00"));
			}
				
			//validate Scheduled Start and End Date Time		
			validateDateTime(availableResourceCriteriaBO.getScheduledStartDate(), availableResourceCriteriaBO.getScheduledStartTime(), availableResourceCriteriaBO.getScheduledEndDate(), availableResourceCriteriaBO.getScheduledEndTime());			
				
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new InvalidFieldException();
		}
	}
	
	/**
	 * This method retrieves the Available TA Staff, JP, Police Officer, ITA Examiner and TA Vehicle based on the search criteria entered
	 * @param availableResourceCriteriaBO
	 * @return
	 * @throws RequiredFieldMissingException
	 * @throws InvalidFieldException
	 * @throws NoRecordFoundException
	 */
	public List<TAStaffBO> getAvailableTAStaffResource(AvailableResourceCriteriaBO availableResourceCriteriaBO) throws RequiredFieldMissingException, InvalidFieldException, NoRecordFoundException{
		
		validateAvailResCriteria(availableResourceCriteriaBO, PersonType.TA_STAFF);
		
		List<TAStaffBO> taStaff;
		
		taStaff = serviceFactory.getRoadOperationService().getAvailableTAStaff(availableResourceCriteriaBO);
		
		if(taStaff==null){
			throw new NoRecordFoundException();
		}
		
		return taStaff;
	}


	
	public List<JPBO> getAvailableJPResource(AvailableResourceCriteriaBO availableResourceCriteriaBO) throws RequiredFieldMissingException, InvalidFieldException, NoRecordFoundException{
		
		validateAvailResCriteria(availableResourceCriteriaBO,PersonType.JP);
		
		List<JPBO> jp;
		
		jp = serviceFactory.getRoadOperationService().getAvailableJPResource(availableResourceCriteriaBO);
		
		if(jp==null){
			throw new NoRecordFoundException();
		}
		
		return jp;
	}
	
	
   public List<PoliceOfficerBO> getAvailablePoliceResource(AvailableResourceCriteriaBO availableResourceCriteriaBO) throws RequiredFieldMissingException, InvalidFieldException, NoRecordFoundException{
		
		validateAvailResCriteria(availableResourceCriteriaBO,PersonType.POLICE_OFFCER);
		
		List<PoliceOfficerBO> pol;
		
		pol = serviceFactory.getRoadOperationService().getAvailablePoliceResource(availableResourceCriteriaBO);
		
		if(pol==null || pol.size() < 1){
			throw new NoRecordFoundException();
		}
		
		return pol;
	}

   
   public List<ITAExaminerBO> getAvailableITAResource(AvailableResourceCriteriaBO availableResourceCriteriaBO) throws RequiredFieldMissingException, InvalidFieldException, NoRecordFoundException{
		
		validateAvailResCriteria(availableResourceCriteriaBO,PersonType.ITA_EXAMINER);
		
		List<ITAExaminerBO> ita;
		
		ita = serviceFactory.getRoadOperationService().getAvailableITAResource(availableResourceCriteriaBO);
		
		if(ita==null){
			throw new NoRecordFoundException();
		}
		
		return ita;
	}
   
   
   public List<TAVehicleBO> getAvailableTAVehResource(AvailableResourceCriteriaBO availableResourceCriteriaBO) throws RequiredFieldMissingException, InvalidFieldException, NoRecordFoundException{
		
		validateAvailResCriteria(availableResourceCriteriaBO,"VEH");
		
		List<TAVehicleBO> veh;
		
		veh = serviceFactory.getRoadOperationService().getAvailableTAVehicleResource(availableResourceCriteriaBO);
		
		if(veh==null){
			throw new NoRecordFoundException();
		}
		
		return veh;
	}
  
	
	
   
	/**
	 * This method checks if the Person(TA Staff, JP, Police Officer or ITA Examiner) is available based on the search criteria entered
	 * @param availableResourceCriteriaBO
	 * @return
	 * @throws RequiredFieldMissingException
	 * @throws InvalidFieldException
	 */
	public boolean isPersonAvailable(AvailableResourceCriteriaBO availableResourceCriteriaBO) throws RequiredFieldMissingException, InvalidFieldException{
		if(availableResourceCriteriaBO==null) {
			throw new RequiredFieldMissingException();
		}
		
		if(availableResourceCriteriaBO.getScheduledStartDate()==null || availableResourceCriteriaBO.getScheduledEndDate()==null
				|| StringUtils.isBlank(availableResourceCriteriaBO.getScheduledEndTime()) || StringUtils.isBlank(availableResourceCriteriaBO.getScheduledStartTime())
				|| (availableResourceCriteriaBO.getResourceId()==null || availableResourceCriteriaBO.getResourceId()<1)){
			throw new RequiredFieldMissingException();
		}
				
		//Validate Scheduled Start Time
		validateTime(availableResourceCriteriaBO.getScheduledStartTime());
		availableResourceCriteriaBO.setScheduledStartTime(availableResourceCriteriaBO.getScheduledStartTime().concat(":00"));
			

		//Validate Scheduled End Time
		validateTime(availableResourceCriteriaBO.getScheduledEndTime());
		availableResourceCriteriaBO.setScheduledEndTime(availableResourceCriteriaBO.getScheduledEndTime().concat(":00"));
					
		//validate Scheduled Start and End Date Time	
		validateDateTime(availableResourceCriteriaBO.getScheduledStartDate(), availableResourceCriteriaBO.getScheduledStartTime(), availableResourceCriteriaBO.getScheduledEndDate(), availableResourceCriteriaBO.getScheduledEndTime());			
		
			
		return serviceFactory.getRoadOperationService().isPersonAvailable(availableResourceCriteriaBO);
	}
	
	/**
	 * This method checks if the TA Vehicle is available based on the search criteria entered
	 * @param availableResourceCriteriaBO
	 * @return
	 * @throws RequiredFieldMissingException
	 * @throws InvalidFieldException
	 */
	public boolean isTAVehicleAvailable(AvailableResourceCriteriaBO availableResourceCriteriaBO) throws RequiredFieldMissingException, InvalidFieldException{
		if(availableResourceCriteriaBO==null) {
			throw new RequiredFieldMissingException();
		}
		
		if(availableResourceCriteriaBO.getScheduledStartDate()==null || availableResourceCriteriaBO.getScheduledEndDate()==null
				|| StringUtils.isBlank(availableResourceCriteriaBO.getScheduledEndTime()) || StringUtils.isBlank(availableResourceCriteriaBO.getScheduledStartTime())
				|| (availableResourceCriteriaBO.getResourceId()==null || availableResourceCriteriaBO.getResourceId()<1)){
			throw new RequiredFieldMissingException();
		}
				
		//Validate Scheduled Start Time
		validateTime(availableResourceCriteriaBO.getScheduledStartTime());	
		availableResourceCriteriaBO.setScheduledStartTime(availableResourceCriteriaBO.getScheduledStartTime().concat(":00"));
			

		//Validate Scheduled End Time
		validateTime(availableResourceCriteriaBO.getScheduledEndTime());
		availableResourceCriteriaBO.setScheduledEndTime(availableResourceCriteriaBO.getScheduledEndTime().concat(":00"));
		
			
		//validate Scheduled Start and End Date Time	
		validateDateTime(availableResourceCriteriaBO.getScheduledStartDate(), availableResourceCriteriaBO.getScheduledStartTime(), availableResourceCriteriaBO.getScheduledEndDate(), availableResourceCriteriaBO.getScheduledEndTime());			
		
			
		return serviceFactory.getRoadOperationService().isTAVehicleAvailable(availableResourceCriteriaBO);
	}
	
	/**
	 * This method retrieves the Operation Strategy Rules based on the List of Strategy Id(s) passed
	 * @param strategyIdList
	 * @return
	 * @throws RequiredFieldMissingException
	 * @throws InvalidFieldException
	 * @throws NoRecordFoundException
	 */
	@RequestMapping("/getOperationStrategyRule")
	public @ResponseBody List<StrategyRuleBO> getOperationStrategyRule(@RequestBody List<Integer> strategyIdList) throws RequiredFieldMissingException, InvalidFieldException, NoRecordFoundException{
		List<StrategyRuleBO> strategyRuleList = new ArrayList<StrategyRuleBO>();
		
		if(strategyIdList==null || strategyIdList.size()==0){
			throw new RequiredFieldMissingException();
		}
		
		for(Integer strategyId : strategyIdList){
			if(strategyId==null){
				throw new InvalidFieldException();
			}
		}
		
		boolean isStrategyIdListValid = false;
		isStrategyIdListValid = serviceFactory.getRoadOperationService().isStrategyIdListValid(strategyIdList);
		
		if(isStrategyIdListValid == false){
			throw new InvalidFieldException();
		}
		
		strategyRuleList = serviceFactory.getRoadOperationService().getOperationStrategyRule(strategyIdList);
		
		if(strategyRuleList==null){
			throw new NoRecordFoundException();
		}
		
		return strategyRuleList;
	}
	
	/**
	 * This method checks if a Vehicle is required based on the List of Strategy Id(s) passed
	 * @param strategyIdList
	 * @return
	 * @throws RequiredFieldMissingException
	 * @throws InvalidFieldException
	 */
	public boolean isVehicleRequired(List<Integer> strategyIdList) throws RequiredFieldMissingException, InvalidFieldException{
		boolean isVehicleRequired = false;
		
		if(strategyIdList==null || strategyIdList.size()==0){
			throw new RequiredFieldMissingException();
		}
		
		for(Integer strategyId : strategyIdList){
			if(strategyId==null){
				throw new InvalidFieldException();
			}
		}
		
		boolean isStrategyIdListValid = false;
		isStrategyIdListValid = serviceFactory.getRoadOperationService().isStrategyIdListValid(strategyIdList);
		
		if(isStrategyIdListValid == false){
			throw new InvalidFieldException();
		}
		
		isVehicleRequired = serviceFactory.getRoadOperationService().isVehicleRequired(strategyIdList);
		
		return isVehicleRequired;
	}
	
	/**
	 * This method checks if a Operation Strategy is valid based on the Assigned Resources(TA Staff, JP, Police Officer, ITA Examiner and TA Vehicle)
	 * @param strategyIdList
	 * @param assignedResourceBO
	 * @return
	 * @throws RequiredFieldMissingException
	 * @throws InvalidFieldException
	 */
	public boolean isOperationStrategyValid(List<StrategyBO> strategyList, List<AssignedTeamDetailsBO> assignedTeamDetailsBOList) throws RequiredFieldMissingException, InvalidFieldException{
		boolean isOperationStrategyValid = false;
		
		if(strategyList==null || strategyList.size()==0){
			throw new RequiredFieldMissingException();
		}
		
		for(StrategyBO strategyId : strategyList){
			if(strategyId==null){
				throw new InvalidFieldException();
			}
		}
	
		
		if(assignedTeamDetailsBOList==null){
			throw new RequiredFieldMissingException();
		}
		
		List<Integer> strategyIdList = returnIntegerStrategyList(strategyList);
		
		boolean isStrategyListValid = false;
		isStrategyListValid = serviceFactory.getRoadOperationService().isStrategyIdListValid(strategyIdList);
		
		if(isStrategyListValid == false){
			throw new InvalidFieldException();
		}
		
		isOperationStrategyValid = serviceFactory.getRoadOperationService().isOperationStrategyValid(strategyIdList, assignedTeamDetailsBOList);
		
		return isOperationStrategyValid;
	}
	
	/*public boolean operationNameExists(String operationName) throws RequiredFieldMissingException{
		boolean operationNameExists = false;
		
		if(StringUtils.isBlank(operationName)){
			throw new RequiredFieldMissingException();
		}
		
		operationNameExists = serviceFactory.getRoadOperationService().operationNameExists(operationName);
		
		
		return operationNameExists;
	}*/
	
	/**
	 * TODO to be added to a centralized Class
	 * @param teamName
	 * @param currTeams
	 * @param teamID
	 * @return
	 */
	private boolean nameUsedForOtherTeam(String teamName,
			List<AssignedTeamDetailsBO> currTeams, Integer teamID) {
		boolean result = false;

		if (!StringUtils.isBlank(teamName)) {
			if (currTeams != null && currTeams.size()>1) {
				//System.err.println("currTeams Size"+ currTeams.size());
				for (AssignedTeamDetailsBO teamView : currTeams) {
					
					//teamName comparison is not case sensitive
					//kpowell
					if (teamView.getTeamBO().getTeamName().equalsIgnoreCase(teamName)) {
						if (!teamID.equals(teamView.getTeamBO().getTeamId())) {
							result = true;
							break;
						}
					}
				}
			}
		}

		return result;

	}
	/**
	 * This method saves a Road Operation and Assigned Person(s), Assigned Vehicle(s), Operation Strategy(s), Operation Location(s), Operation Artery(s)
	 * @param roadOperationBO
	 * @param assignedResourceBO
	 * @param assignedOtherDetailsBO
	 * @return
	 * @throws RequiredFieldMissingException
	 * @throws InvalidFieldException
	 * @throws ErrorSavingException
	 */
	public RoadOperationBO saveRoadOperation(RoadOperationBO roadOperationBO, List<AssignedTeamDetailsBO> assignedTeamDetailsBOList ) 
			throws RequiredFieldMissingException, InvalidFieldException, ErrorSavingException{
		
		RoadOperationBO savedRoadOperationBO = new RoadOperationBO();
				
		if(roadOperationBO==null || assignedTeamDetailsBOList == null){
			
			throw new RequiredFieldMissingException();
		}
	
		if(StringUtils.isBlank(roadOperationBO.getCategoryId())
				|| roadOperationBO.getScheduledStartDate() == null || roadOperationBO.getScheduledEndDate() == null
				|| StringUtils.isBlank(roadOperationBO.getScheduledStartTime()) || StringUtils.isBlank(roadOperationBO.getScheduledEndTime()) 
				|| StringUtils.isBlank(roadOperationBO.getCurrentUsername()) || roadOperationBO.getOfficeLocCodeList()==null){
			throw new RequiredFieldMissingException();
		}
		
		
		if(roadOperationBO.getOfficeLocCodeList().size()<=0){
			
			throw new RequiredFieldMissingException();
		}
		
		for(AssignedTeamDetailsBO teamDetails : assignedTeamDetailsBOList){
			if(teamDetails.getTeamBO().getTeamLead()==null || teamDetails.getOperationLocationList()==null){
				
				throw new RequiredFieldMissingException();
			}
			
		}
		
		if(roadOperationBO.getOperationStrategyList()==null){
			
			throw new RequiredFieldMissingException();
		}
		
		
		if(!roadOperationBO.getCategoryId().equals(Constants.Category.REGIONAL) && !roadOperationBO.getCategoryId().equals(Constants.Category.SPECIAL)){
			throw new InvalidFieldException("Invalid Category");
		}
		
		if(roadOperationBO.getCategoryId().equals(Constants.Category.REGIONAL)){
			if(roadOperationBO.getCourtBO()==null || roadOperationBO.getCourtDate()==null || StringUtils.isBlank(roadOperationBO.getCourtTime())){
				
				throw new RequiredFieldMissingException();
			}
			
			validateTime(roadOperationBO.getCourtTime());
			roadOperationBO.setCourtTime(roadOperationBO.getCourtTime().concat(":00"));
			
			try {
				//Validate Court Date
				DateUtils.utilDateToSqlDate(roadOperationBO.getCourtDate());
				
			}
			catch (ParseException e) {
				e.printStackTrace();
				throw new InvalidFieldException();
			}
			
			validateCourtDate(roadOperationBO.getScheduledStartDate(), roadOperationBO.getCourtDate());
			
		}
				
		/*if(StringUtils.isNotBlank(roadOperationBO.getOperationName())){
			boolean operationNameExists = false;
			operationNameExists = serviceFactory.getRoadOperationService().operationNameExists(roadOperationBO.getOperationName());
			
			if(operationNameExists == true){
				throw new InvalidFieldException("Operation Name already exists");
			}
		}*/
		
		//Validate Scheduled Start Time
		validateTime(roadOperationBO.getScheduledStartTime());
		roadOperationBO.setScheduledStartTime(roadOperationBO.getScheduledStartTime().concat(":00"));
	

		//Validate Scheduled End Time
		validateTime(roadOperationBO.getScheduledEndTime());
		roadOperationBO.setScheduledEndTime(roadOperationBO.getScheduledEndTime().concat(":00"));	
		
		//validate Scheduled Start and End Date Time
		validateDateTime(roadOperationBO.getScheduledStartDate(), roadOperationBO.getScheduledStartTime(), roadOperationBO.getScheduledEndDate(), roadOperationBO.getScheduledEndTime());		
		
			
		roadOperationBO.setCurrentUsername(roadOperationBO.getCurrentUsername().trim().toUpperCase());
		
		if(serviceFactory.getStaffUserMappingService().isUserMapped(roadOperationBO.getCurrentUsername())==false){
			throw new InvalidFieldException("Current Username is not mapped");
		}
			
		
		/**
		 * //removed as this validation should ONLY be applied on a per team basis
		//kpowell-2015/01/13
		 * if(isOperationStrategyValid(roadOperationBO.getOperationStrategyList(), assignedTeamDetailsBOList)==false){
			throw new InvalidFieldException("The selected strategies are not consistent with assigned resources.");
		}*/
		
		TAStaffBO loggedInStaff = new TAStaffBO();
		boolean isArteryRequired = false;
		
		if(roadOperationBO.getOperationStrategyList()!=null){
			isArteryRequired = serviceFactory.getRoadOperationService().isArteryRequired(returnIntegerStrategyList(roadOperationBO.getOperationStrategyList()));
		}
		else{
			
			throw new RequiredFieldMissingException();
		}
	
		
		loggedInStaff = serviceFactory.getStaffUserMappingService().getStaffByUsername(roadOperationBO.getCurrentUsername());
		
		
		int arteryCount=0;
		
		for(AssignedTeamDetailsBO teamDetails : assignedTeamDetailsBOList){
			
			//Validate Team lead and that the team lead is an assigned Staff to the operation
			
			if(teamDetails.getTeamBO().getTeamLead()!=null){
				
				if(teamDetails.getTaStaffList()!=null){
					List<Integer> assignedTAStaffPersonList = new ArrayList<Integer>();
					assignedTAStaffPersonList = returnIntegerStaffPersonIdList(teamDetails.getTaStaffList());
					if(!assignedTAStaffPersonList.contains(teamDetails.getTeamBO().getTeamLead().getPersonId())){
						throw new InvalidFieldException("Team Lead must be an assigned TA Staff");
					}
				}
			}
			else{
				throw new InvalidFieldException("Team Lead Id does not exist");
			}
			
			// Team Name Cannot be Null
			if (StringUtils.isBlank(teamDetails.getTeamBO().getTeamName())) {
				throw new RequiredFieldMissingException("Team Name is required");
			}

			// Team Name must be Unique if adding new team			
			/*if (nameUsedForOtherTeam(teamDetails.getTeamBO().getTeamName(),
					assignedTeamDetailsBOList, teamDetails.getTeamBO().getTeamId())) {
				
				throw new InvalidFieldException("Team Name "+ teamDetails.getTeamBO().getTeamName()+ " already used for another team");

			}*/

		
			//validate location
			List<Integer> locationIdList = new ArrayList<Integer>();
			if(teamDetails.getOperationLocationList()!=null){
				locationIdList = returnIntegerLocationList(teamDetails.getOperationLocationList());
				if(roadOperationBO.getCategoryId().equals(Constants.Category.REGIONAL)){
					boolean validLocationList = validateRegionalOperationLocation(locationIdList,loggedInStaff.getOfficeLocationCode());
					if(validLocationList==false){
						//throw new InvalidFieldException("Invalid Location for Regional Operation");
						throw new InvalidFieldException("Location(s) selected is not within "+roadOperationBO.getCurrentUsername()+" region - "+ loggedInStaff.getOfficeLocationCode());
					}
				}
				
				
				/*boolean validateAssignedResource = validateAssignedResource(roadOperationBO, teamDetails, parishList, false);
				if(validateAssignedResource==false){
					throw new InvalidFieldException("Invalid Assigned Resource ");
				}*/
			}
			else{
				throw new RequiredFieldMissingException("At least one location is required");
			}
			
			
			//validate assigned resources
			if(teamDetails.getOperationParishList()!=null){
			
			
				//R.B. Modified on March 18: This validation was previously done based on selected locations instead of parishes
				List<String> parishList = new ArrayList<String>();
				List<ParishBO> selectedPar = teamDetails.getOperationParishList();
				
				for (ParishBO parishBO : selectedPar) {
				
					parishList.add(parishBO.getParishCode());
				}
				
				//added fix for Incident:
				//if Kingston parish is in the list, add St. Andrew parish and vice versa
				if(parishList.contains(Constants.ParishKingstonAndStAndrew.KINGSTON)) {
					parishList.add(Constants.ParishKingstonAndStAndrew.ST_ANDREW);
					
				}else if(parishList.contains(Constants.ParishKingstonAndStAndrew.ST_ANDREW)){
					parishList.add(Constants.ParishKingstonAndStAndrew.KINGSTON);						
				}
				//
				validateAssignedResource(roadOperationBO, teamDetails, parishList, false);
				
				
			}else{
				throw new RequiredFieldMissingException("At least one parish is required");
			}
			
			
			
			
			//validate artery
			
			if(teamDetails.getOperationArteryList()!=null && teamDetails.getOperationArteryList().size()>0){
				arteryCount = arteryCount + teamDetails.getOperationArteryList().size();
				
				boolean validArtery = validateArtery(returnIntegerArteryList(teamDetails.getOperationArteryList()), locationIdList);
				if(validArtery==false){
					throw new InvalidFieldException("Artery selected with location id that was not selected");
				}
			}
				

			//TODO- revise and add to function
			if(roadOperationBO.getOperationStrategyList()==null || roadOperationBO.getOperationStrategyList().size()==0){
				throw new RequiredFieldMissingException();
			}
			
			for(StrategyBO strategyId : roadOperationBO.getOperationStrategyList()){
				if(strategyId==null){
					throw new InvalidFieldException();
				}
			}
		
			
			if(assignedTeamDetailsBOList==null){
				throw new RequiredFieldMissingException();
			}
			//
			List<Integer> strategyIdList = returnIntegerStrategyList(roadOperationBO.getOperationStrategyList());
			System.err.println("validateStrategyRule");
			if (serviceFactory.getRoadOperationService().validateStrategyRule(roadOperationBO.getOperationStrategyList(), teamDetails, returnIntegerStrategyList(roadOperationBO.getOperationStrategyList())) == false) {
				throw new InvalidFieldException("The selected strategies are not consistent with assigned resources.");
			}
			
		}
		
		if(isArteryRequired==true){
			if(arteryCount==0){
				
				throw new RequiredFieldMissingException();
			}
		}
		
		
	
		try
		{
			savedRoadOperationBO = serviceFactory.getRoadOperationService().saveRoadOperation(roadOperationBO, assignedTeamDetailsBOList);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new ErrorSavingException();
		}
		

		return savedRoadOperationBO;
	}

	/**
	 * This method checks if the selected Artery(s) are valid 
	 * @param arteryIdList
	 * @param assignedLocationIdList
	 * @return
	 * @throws InvalidFieldException
	 */
	private boolean validateArtery(List<Integer> arteryIdList, List<Integer> assignedLocationIdList) throws InvalidFieldException{
		List<ArteryDO> arteryDOList = new ArrayList<ArteryDO>();
		//List<Integer> arteryLocationIdList = new ArrayList<Integer>();
		
		arteryDOList = serviceFactory.getRoadOperationService().returnArteryDOList(arteryIdList);
		if(arteryDOList!=null){
			//arteryLocationIdList = returnIntegerArteryLocationIdList(arteryDOList);
			for(ArteryDO arteryDO : arteryDOList){
				if(!assignedLocationIdList.contains(arteryDO.getLocation().getLocationId())){
					return false;
				}
				
				if(arteryDO.getStatus().getStatusId().equals(Constants.Status.INACTIVE)){
					return false;
				}
			}
		}
		else{
			throw new InvalidFieldException();
		}
		return true;
	}
	
	/**
	 * This method checks if the Regional Operation Location(s) are valid
	 * @param locationList
	 * @param parishCode
	 * @return
	 * @throws InvalidFieldException
	 */
	private boolean validateRegionalOperationLocation(List<Integer> locationList,  String  officeLocationCode) throws InvalidFieldException{
		List<LocationDO> locationDOList = new ArrayList<LocationDO>();
		List<ParishDO> parishDOList = new ArrayList<ParishDO>();
		List<String> locatonParishList = new ArrayList<String>();
		List<String> regionParishList = new ArrayList<String>();
		
		locationDOList = serviceFactory.getRoadOperationService().returnLocationDOList(locationList);
		parishDOList = serviceFactory.getRoadOperationService().findRegionParishes(officeLocationCode);
		
		
		if(locationDOList==null || parishDOList==null){
			throw new InvalidFieldException();
		}
		else{
			locatonParishList = returnStringLocationParishList(locationDOList);
			regionParishList = returnStringRegionParishList(parishDOList);
			
			for(String locParish : locatonParishList){
				if(!regionParishList.contains(locParish)){
					return false;
				}
			}
			/*for(LocationDO locationDO : locationDOList){
				if(!locationDO.getParish().getParishCode().equals(parishCode)){
					return false;
				}
				if(locationDO.getStatus().getStatusId().equals(Constants.Status.INACTIVE)){
					return false;
				}
			}*/
			
		}
		
		return true;
	}
	
	/**
	 * This method returns a List of Parish Code based on the Location List passed
	 * @param locationList
	 * @return
	 */
	private List<String> returnParishList(List<Integer> locationList){
		List<LocationDO> locationDOList = new ArrayList<LocationDO>();
		List<String> parishList = new ArrayList<String>();
		
		locationDOList = serviceFactory.getRoadOperationService().returnLocationDOList(locationList);
		
		for(LocationDO locationDO : locationDOList){
			parishList.add(locationDO.getParish().getParishCode());
		}
		
		return parishList;
	}
	
	
	public AssignedResourceBO findSavedRoadOperationAssignedResource(Integer roadOperationId) throws RequiredFieldMissingException, NoRecordFoundException{
		AssignedResourceBO assignedResourceBO = new AssignedResourceBO();
		RoadOperationDO savedOperationDO = new RoadOperationDO();
		
		if(roadOperationId==null){
			throw new RequiredFieldMissingException();
		}
		savedOperationDO = serviceFactory.getRoadOperationService().findRoadOperationById(roadOperationId);
		
		
		if(savedOperationDO==null){
			throw new NoRecordFoundException();
		}
		assignedResourceBO = serviceFactory.getRoadOperationService().findSavedAssignedResource(roadOperationId);
	
		
		return assignedResourceBO;
	}
	
	public void cancelRoadOperation(RoadOperationBO roadOpBo) throws InvalidFieldException, Exception{
		serviceFactory.getRoadOperationService().updateOperationStatus(roadOpBo, Constants.Status.ROAD_OPERATION_CANCELLED);
	}
	
	public void terminateRoadOperation(RoadOperationBO roadOpBo) throws InvalidFieldException, Exception{
		
		if(roadOpBo.getActualStartDate()!=null && StringUtils.isNotBlank(roadOpBo.getActualStartTime())){
			validateTime(roadOpBo.getActualStartTime());
			roadOpBo.setActualStartTime(roadOpBo.getActualStartTime().concat(":00"));
			try {
				//Validate Actual Start Date
				DateUtils.utilDateToSqlDate(roadOpBo.getActualStartDate());
				
				
				//Validate Actual Start Date Time
				//Added extra parameter Constants.Status.ROAD_OPERATION_TERMINATED which states that this method call is from terminateRoadOperation by Ricardo Thompson 11/3/2014
				validateActualStartDTime(roadOpBo.getActualStartDate(), roadOpBo.getActualStartTime(),roadOpBo.getActualEndDate(), roadOpBo.getActualEndTime(), roadOpBo.getScheduledStartDate(), roadOpBo.getScheduledStartTime(), roadOpBo.getScheduledEndDate(),roadOpBo.getCategoryId(),Constants.Status.ROAD_OPERATION_TERMINATED);
				validateActualEndDTime(roadOpBo.getActualStartDate(), roadOpBo.getActualStartTime(),roadOpBo.getActualEndDate(), roadOpBo.getActualEndTime(), roadOpBo.getScheduledStartDate(), roadOpBo.getScheduledStartTime(), roadOpBo.getScheduledEndDate(),roadOpBo.getCategoryId(),Constants.Status.ROAD_OPERATION_TERMINATED);
				
			}
			catch (ParseException e) {
				e.printStackTrace();
				throw new InvalidFieldException();
			}
		}
		else{
			throw new RequiredFieldMissingException("Actual Start Date is required");
		}
			
		if(roadOpBo.getActualEndDate()!=null && StringUtils.isNotBlank(roadOpBo.getActualEndTime())){
			
			validateTime(roadOpBo.getActualEndTime());
			roadOpBo.setActualEndTime(roadOpBo.getActualEndTime().concat(":00")); //??
			//Validate Actual Start Date
			try {
				DateUtils.utilDateToSqlDate(roadOpBo.getActualEndDate());
			
				
				//2014-10-15::validate actual duration with offence date
				serviceFactory.getRoadOperationService().validateActualStartDateAgainstOffenceDates(roadOpBo.getRoadOperationId(),
						roadOpBo.getActualStartDtime());
				serviceFactory.getRoadOperationService().validateActualEndDateAgainstOffenceDates(roadOpBo.getRoadOperationId(),
						roadOpBo.getActualEndDtime());
				
			
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			throw new RequiredFieldMissingException("Actual End Date is required");
		}
		
		
		serviceFactory.getRoadOperationService().updateOperationStatus(roadOpBo, Constants.Status.ROAD_OPERATION_TERMINATED);
	}
	
	public void closeRoadOperation(RoadOperationBO roadOpBo) throws InvalidFieldException, Exception
	{
		String operationCloseMessage = serviceFactory.getRoadOperationService().canOperationBeClosed(roadOpBo.getRoadOperationId());
		
		if(StringUtils.isBlank(operationCloseMessage)){
			serviceFactory.getRoadOperationService().updateOperationStatus(roadOpBo, Constants.Status.ROAD_OPERATION_CLOSED);
		}else{
			throw new ErrorSavingException(operationCloseMessage);
		}
	}
	
	
	/**
	 * This method checks if the Assigned Resources(TA Staff, JP , Police Officer and TA Vehicle) are valid
	 * @param roadOperationBO
	 * @param assignedResourceBO
	 * @param officeLocationCode
	 * @param parishCodeList
	 * @param updateFlag
	 * @return
	 * @throws InvalidFieldException
	 */
	private void validateAssignedResource(RoadOperationBO roadOperationBO, AssignedTeamDetailsBO assignedResourceBO, List<String> parishCodeList, boolean updateFlag)throws InvalidFieldException{
		AvailableResourceIds availableResourceIds = new AvailableResourceIds();
		AvailableResourceCriteriaBO availableResourceCriteriaBO = new AvailableResourceCriteriaBO();
		List<Integer> assignedPersonIntegerList = new ArrayList<Integer>();
		List<GenericReferenceVO> allAssignedResource = new ArrayList<GenericReferenceVO>();
		
		availableResourceCriteriaBO.setCategoryId(roadOperationBO.getCategoryId());
		availableResourceCriteriaBO.setOfficeLocCodeList(roadOperationBO.getOfficeLocCodeList());
		availableResourceCriteriaBO.setParishCodeList(parishCodeList);
		availableResourceCriteriaBO.setScheduledStartDate(roadOperationBO.getScheduledStartDate());
		availableResourceCriteriaBO.setScheduledStartTime(roadOperationBO.getScheduledStartTime());
		availableResourceCriteriaBO.setScheduledEndDate(roadOperationBO.getScheduledEndDate());
		availableResourceCriteriaBO.setScheduledEndTime(roadOperationBO.getScheduledEndTime());
		availableResourceCriteriaBO.setUsePoliceMaxFilter(false);
		
		//availableResourceBO = serviceFactory.getRoadOperationService().getAvailableResource(availableResourceCriteriaBO);
		availableResourceIds = serviceFactory.getRoadOperationService().getAvailableResourceIds(availableResourceCriteriaBO);
		
		AssignedResourceBO savedResourceBO = new AssignedResourceBO();
		boolean valid = true;
		if(availableResourceIds==null){
			throw new InvalidFieldException();
		}
		else{
			if(updateFlag==true){
				//savedResourceBO = serviceFactory.getRoadOperationService().findSavedAssignedResource(roadOperationBO.getRoadOperationId(), assignedResourceBO.getTeamBO().getTeamId());
				
				//Find saved resource for the road operation and not the team because the UI should handle the person not being selected more than once
				savedResourceBO = serviceFactory.getRoadOperationService().findSavedAssignedResource(roadOperationBO.getRoadOperationId());
			}
			else{
				savedResourceBO=null;
			}
			
			if(assignedResourceBO.getTaStaffList()!=null){
				
				assignedPersonIntegerList = new ArrayList<Integer>();
				
				if(savedResourceBO!=null && savedResourceBO.getTaStaffList()!=null){
					assignedPersonIntegerList = returnOnlyNewlyAddedStaffPersonIntegerList(assignedResourceBO.getTaStaffList(), savedResourceBO.getTaStaffList());
				}
				else{
					assignedPersonIntegerList = returnIntegerStaffPersonIdList(assignedResourceBO.getTaStaffList());
				}
				if(assignedPersonIntegerList!=null && assignedPersonIntegerList.size()>0){
					if(availableResourceIds.getTaStaffList()==null){
						throw new InvalidFieldException("No TA Staff available");
					}
					
					List<Integer> avaliableTAStaffIntegerList = new ArrayList<Integer>();
					avaliableTAStaffIntegerList = availableResourceIds.getTaStaffList();
					
					for(Integer personId : assignedPersonIntegerList){
						if(!avaliableTAStaffIntegerList.contains(personId)){
							valid=false;
							TAStaffBO invalidStaff = serviceFactory.getRoadOperationService().findTAStaffByPersonID(personId);
							throw new InvalidFieldException("TA Staff " + invalidStaff.getFullName()+"["+invalidStaff.getStaffId() + "] is no longer available");
							//return false;
						
						}
					}
					
					//used 
					
					for(TAStaffBO staff :assignedResourceBO.getTaStaffList()){
						allAssignedResource.add(new GenericReferenceVO(staff.getPersonId(), staff.getFullName(),Constants.PersonType.TA_STAFF));
					}
					//System.err.println("allAssignedResource"+ allAssignedResource.size());
				}
			}
			
			
			if(assignedResourceBO.getJpList()!=null){
				
				assignedPersonIntegerList = new ArrayList<Integer>();
				
				if(savedResourceBO!=null && savedResourceBO.getJpList()!=null){
					assignedPersonIntegerList = returnOnlyNewlyAddedJPPersonIntegerList(assignedResourceBO.getJpList(), savedResourceBO.getJpList());
				}
				else{
					assignedPersonIntegerList = returnIntegerJpPersonIdList(assignedResourceBO.getJpList());
				}
				
				if(assignedPersonIntegerList!=null && assignedPersonIntegerList.size()>0){
					if(availableResourceIds.getJpList()==null){
						throw new InvalidFieldException("No JP available");
					}
					
					List<Integer> avaliableJPIntegerList = new ArrayList<Integer>();
					avaliableJPIntegerList = availableResourceIds.getJpList();
					
					for(Integer personId : assignedPersonIntegerList){
						if(!avaliableJPIntegerList.contains(personId)){
							valid=false;
							JPBO invalidJP = serviceFactory.getRoadOperationService().findJPByPersonID(personId);
							throw new InvalidFieldException("JP " + invalidJP.getPersonBO().getFullName()+"["+ invalidJP.getRegNumber() + "] is no longer available");
							//throw new InvalidFieldException("Invalid Assigned JP");
							//return false;
						}
					}
					
					
					for(JPBO jp :assignedResourceBO.getJpList()){
						allAssignedResource.add(new GenericReferenceVO(jp.getPersonBO().getPersonId(), jp.getPersonBO().getFullName(),Constants.PersonType.JP));
					}
				}
			}
			
			if(assignedResourceBO.getPoliceOfficerList()!=null){
				
				assignedPersonIntegerList = new ArrayList<Integer>();
				
				if(savedResourceBO!=null && savedResourceBO.getPoliceOfficerList()!=null){
					assignedPersonIntegerList = returnOnlyNewlyAddedPoliceOfficerPersonIntegerList(assignedResourceBO.getPoliceOfficerList(), savedResourceBO.getPoliceOfficerList());
				}
				else{
					assignedPersonIntegerList = returnIntegerPoliceOfficerPersonIdList(assignedResourceBO.getPoliceOfficerList());
				}
				if(assignedPersonIntegerList!=null && assignedPersonIntegerList.size()>0){
					if(availableResourceIds.getPoliceOfficerList()==null){
						throw new InvalidFieldException("No POlice Officer Available");
					}
					
					List<Integer> avaliablePoliceOfficerIntegerList = new ArrayList<Integer>();
					avaliablePoliceOfficerIntegerList = availableResourceIds.getPoliceOfficerList();
					
					//System.err.println("avaliablePoliceOfficerIntegerList " + avaliablePoliceOfficerIntegerList.size());
					for(Integer personId : assignedPersonIntegerList){
						if(!avaliablePoliceOfficerIntegerList.contains(personId)){
							valid=false;
							PoliceOfficerBO invalidPolice = serviceFactory.getRoadOperationService().findPoliceOfficerByPersonID(personId);
							throw new InvalidFieldException("Police Officer " + invalidPolice.getFullName()+"["+invalidPolice.getPolOfficerCompNo() + "] is no longer available");
							//throw new InvalidFieldException("Invalid Assigned Police Officer");
							//return false;
						}
					}
					
					//used 
					for(PoliceOfficerBO police :assignedResourceBO.getPoliceOfficerList()){
						allAssignedResource.add(new GenericReferenceVO(police.getPersonID(), police.getFullName(),Constants.PersonType.POLICE_OFFCER));
					}
				}
			}
			
			if(assignedResourceBO.getItaExaminerList()!=null){
					
				assignedPersonIntegerList = new ArrayList<Integer>();
				
				if(savedResourceBO!=null && savedResourceBO.getItaExaminerList()!=null){
					assignedPersonIntegerList = returnOnlyNewlyAddedITAExaminerPersonIntegerList(assignedResourceBO.getItaExaminerList(), savedResourceBO.getItaExaminerList());
				}
				else{
					assignedPersonIntegerList = returnIntegerITAExaminerPersonIdList(assignedResourceBO.getItaExaminerList());
				}			
				
				if(assignedPersonIntegerList!=null && assignedPersonIntegerList.size()>0){
					if(availableResourceIds.getItaExaminerList()==null){
						throw new InvalidFieldException("No ITA Examiner Available");
					}
					
					List<Integer> avaliableITAExaminerIntegerList = new ArrayList<Integer>();
					avaliableITAExaminerIntegerList = availableResourceIds.getItaExaminerList();
				
					for(Integer personId : assignedPersonIntegerList){
						if(!avaliableITAExaminerIntegerList.contains(personId)){
							valid=false;
							ITAExaminerBO invalidITA = serviceFactory.getRoadOperationService().findITAExaminerByPersonID(personId);
							throw new InvalidFieldException("ITA Examiner " + invalidITA.getPersonBO().getFullName()+"["+invalidITA.getIdNumber() + "] is no longer available");
						
							//throw new InvalidFieldException("Invalid Assigned ITA Examiner");
							//return false;
						}
					}
					
					//used 
					for(ITAExaminerBO ita :assignedResourceBO.getItaExaminerList()){
						allAssignedResource.add(new GenericReferenceVO(ita.getPersonBO().getPersonId(), ita.getPersonBO().getFullName(),Constants.PersonType.ITA_EXAMINER));
					}
				}
			}
			
			if(assignedResourceBO.getTaVehicleList()!=null){
				
				List<Integer> assignedVehicleIntegerList = new ArrayList<Integer>();
				if(savedResourceBO!=null && savedResourceBO.getTaVehicleList()!=null){
					assignedVehicleIntegerList = returnOnlyNewlyAddedTAVehicleIntegerList(assignedResourceBO.getTaVehicleList(), savedResourceBO.getTaVehicleList());
				}
				else{
					assignedVehicleIntegerList = returnIntegerTAVehicleList(assignedResourceBO.getTaVehicleList());
				}		
			
				if(assignedVehicleIntegerList!=null && assignedVehicleIntegerList.size()>0){
					if(availableResourceIds.getTaVehicleList()==null){
						throw new InvalidFieldException("No TA Vehicle Available");
					}
					
					List<Integer> avaliableTAVehicleIntegerList = new ArrayList<Integer>();
					avaliableTAVehicleIntegerList = availableResourceIds.getTaVehicleList();
					
					for(Integer taVehicleId : assignedVehicleIntegerList){
						if(!avaliableTAVehicleIntegerList.contains(taVehicleId)){
							valid=false;
							TAVehicleDO invalidTAVehicle = (TAVehicleDO) serviceFactory.getRoadCompliancyService().find(TAVehicleDO.class, taVehicleId);
							throw new InvalidFieldException("TA Vehicle " + invalidTAVehicle.getVehicle().getPlateRegNo() + " is no longer available");
						
							//throw new InvalidFieldException("Invalid TA Vehicle");
							//return false;
						}
					}
				}
			}
			
			//System.err.println("Here before  findDuplicates");
			//ValidateResource on Operation
			Collection<GenericReferenceVO> collection = new ArrayList<GenericReferenceVO>();// {allAssignedResource};
			
			//System.err.println("allAssignedResource "+ allAssignedResource.size());
			collection.addAll(allAssignedResource);
			Set setDuplicates = findDuplicates(collection);
			if(setDuplicates.size()>0){
				for (Iterator iterator = setDuplicates.iterator(); iterator
						.hasNext();) {
					GenericReferenceVO object = (GenericReferenceVO) iterator.next();
					throw new InvalidFieldException(object.getKeyDescription()+" ["+ object.getType() + "] is assigned in multiple Roles on the Operation");
					//System.err.println("findDuplicates" + object.getKeyDescription()+object.getKeyValue()+object.getType());
					
				}
					
			}
				
			
		}
		
		//return valid;
	}
	
	
	
	private void validateResourceOnTeam(AssignedTeamDetailsBO teamDetails, Integer personId, String personType){
		
		
		if(personType.equals(Constants.PersonType.ITA_EXAMINER)){
			
		}
		/*//kpowell-2014-11-03
		//ensure person is not added mutiple times in different roles
		if(allAssignedPersonIntegerList.contains(personId)){
			//JPBO invalidJP = serviceFactory.getRoadOperationService().findJPByPersonID(personId);
			throw new InvalidFieldException("JP " + invalidJP.getPersonBO().getFullName()+"["+ invalidJP.getRegNumber() + "] is no longer available");
		}else{
			allAssignedPersonIntegerList.add(personId);
		}
		*/
	
		
	}
	
	private Set<GenericReferenceVO> findDuplicates(Collection<GenericReferenceVO> list) {

		//System.err.println("Inside findDuplicates");
	    Set<GenericReferenceVO> duplicates = new LinkedHashSet<GenericReferenceVO>();
	    Set<GenericReferenceVO> uniques = new HashSet<GenericReferenceVO>();
	    Set<Integer> uniquesIds = new HashSet<Integer>();

	    for (Iterator iterator = list.iterator(); iterator.hasNext();) {
	    	GenericReferenceVO genericReferenceVO = (GenericReferenceVO) iterator.next();
	    	
	    	if(!uniquesIds.contains(genericReferenceVO.getKeyValue())){
	    		uniquesIds.add(genericReferenceVO.getKeyValue());
	    	}else{
	    		//      if(!uniques.add(genericReferenceVO)) {
	            duplicates.add(genericReferenceVO);
	            //System.err.println("duplicate found"+ genericReferenceVO.getKeyValue());
	            
	        }
	    }

	    return duplicates;
	}
	/**
	 * This method checks if the Court Date passed is valid
	 * @param scheduledStartDate
	 * @param courtDate
	 * @throws InvalidFieldException
	 * @throws RequiredFieldMissingException 
	 */
	//@RequestMapping("/validateCourtDate")
	private void validateCourtDateDo( Date scheduledStartDate,  Date courtDate,  String... dateUsed ) throws InvalidFieldException, RequiredFieldMissingException{
		/*Calendar earlier = Calendar.getInstance();
	    Calendar later = Calendar.getInstance();
		if(scheduledStartDate.before(courtDate))
			earlier.setTime(scheduledStartDate);
			later.setTime(courtDate);
			long daysDiff = DateUtils.workDaysBetween(earlier, later);
			
			if(daysDiff<3){
				throw new InvalidFieldException("Court Date must be at least 3 clear working days");
			}
			
			if(DateUtils.isWeekDay(later)==false){
				throw new InvalidFieldException("Court Date cannot be a Weekend");
			}
		}
		else{
			throw new InvalidFieldException("Court Date cannot be after Scheduled Start Date");
		}*/
		String errorParam ="";
		if(dateUsed.length > 0 ){
			errorParam = dateUsed[0].toString();
		}else{
			errorParam ="Scheduled Start Date";
		}
		
		if(courtDate.before(scheduledStartDate)){
			throw new InvalidFieldException("Court Date cannot be before "+ errorParam+ " [" + DateUtils.formatDate("yyyy-MM-dd", scheduledStartDate)+ "]");//("Court Date cannot be before Scheduled Start Date");
		}
		
		try {
			DateUtils.validateCourtDate(courtDate, scheduledStartDate);
		} catch (RequiredFieldMissingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	   
	}
	
	
	@RequestMapping("/validateCourtDate")
	public @ResponseBody void validateCourtDateRest(@RequestParam Date scheduledStartDate, @RequestParam Date courtDate, @RequestParam String dateUsed ) throws InvalidFieldException, RequiredFieldMissingException{
		this.validateCourtDateDo(scheduledStartDate, courtDate, dateUsed);	
	}
	
	
	public void validateCourtDate(Date scheduledStartDate, Date courtDate) throws InvalidFieldException, RequiredFieldMissingException{
		
		this.validateCourtDateDo(scheduledStartDate, courtDate, "");
	}
	
	
	private boolean validateOperationStrategyRule(List<StrategyRuleBO> strategyRuleList, boolean isVehicleRequired, boolean vehicleAssigned, int staffListSize,
			int jpListSize, int itaExaminerListSize, int policeOfficerListSize){
		
		boolean result = true;
		
//		String teamString = "";
//		
//		if(StringUtils.isNotBlank(teamName)){
//			teamString = "for Team: " + teamName;
//		}
		if (isVehicleRequired == true && vehicleAssigned == false) {

			result = false;
		}

		
		if (strategyRuleList != null) {
			for (StrategyRuleBO rule : strategyRuleList) {
				if (rule.getPersonTypeId().equalsIgnoreCase(
						Constants.PersonType.TA_STAFF)) {
					if (staffListSize < rule.getMinimum()
							|| staffListSize > rule.getMaximum()) {

						result = false;
					}
				} else if (rule.getPersonTypeId().equalsIgnoreCase(
						Constants.PersonType.JP)) {
					if (jpListSize < rule.getMinimum()
							|| jpListSize > rule.getMaximum()) {
						
						result = false;
					}
				} else if (rule.getPersonTypeId().equalsIgnoreCase(
						Constants.PersonType.ITA_EXAMINER)) {
					if (itaExaminerListSize < rule.getMinimum()
							|| itaExaminerListSize > rule.getMaximum()) {
						
						result = false;
					}
				} else if (rule.getPersonTypeId().equalsIgnoreCase(
						Constants.PersonType.POLICE_OFFCER)) {
					if (policeOfficerListSize < rule.getMinimum()
							|| policeOfficerListSize > rule.getMaximum()) {
						
						result = false;
					}
				}
			}
		}
		
		return result;

	}

	/**
	 *  This method updates a existing Road Operation and Assigned Person(s), Assigned Vehicle(s), Operation Strategy(s), Operation Location(s), Operation Artery(s)
	 *  This is a wrapper method used for the mobile application
	 * @param roadOperationBO
	 * @param assignedResourceBO
	 * @throws RequiredFieldMissingException
	 * @throws InvalidFieldException
	 * @throws ErrorSavingException
	 */
		@RequestMapping("/updateRoadOperationMobile")
		public @ResponseBody boolean updateRoadOperationMobile(@RequestBody RoadOperationUpdateCustomBO roadOpUpdateBO) 
				throws RequiredFieldMissingException, InvalidFieldException, ErrorSavingException
		{
			if(roadOpUpdateBO != null)
			{
				RoadOperationBO roadOperationBO =  roadOpUpdateBO.getRoadOperationBO();
				List<AssignedTeamDetailsBO> assignedTeamDetailsBOList = roadOpUpdateBO.getAssignedTeamDetailsBOList();
				
				updateRoadOperation(roadOperationBO,assignedTeamDetailsBOList);
			}
			
			return true;
		}
	
	/**
	 *  This method updates a existing Road Operation and Assigned Person(s), Assigned Vehicle(s), Operation Strategy(s), Operation Location(s), Operation Artery(s)
	 * @param roadOperationBO
	 * @param assignedResourceBO
	 * @throws RequiredFieldMissingException
	 * @throws InvalidFieldException
	 * @throws ErrorSavingException
	 */
		public void updateRoadOperation(RoadOperationBO roadOperationBO, List<AssignedTeamDetailsBO> assignedTeamDetailsBOList) 
				throws RequiredFieldMissingException, InvalidFieldException, ErrorSavingException{
			boolean updated = false;
			RoadOperationDO savedOperationDO = new RoadOperationDO();
			RoadOperationOtherDetailsBO roadOperationOtherDetailsBO = new RoadOperationOtherDetailsBO();
			Calendar calendar = Calendar.getInstance();
			Date currentDate = calendar.getTime();
			
			if(roadOperationBO==null || assignedTeamDetailsBOList == null){
				
				throw new RequiredFieldMissingException();
			}
			
			
			List<AssignedTeamDetailsBO>  teams = new ArrayList<AssignedTeamDetailsBO>();
			List<AssignedTeamDetailsBO>  assignedTeamDetailsBOListWithDeletes = new ArrayList<AssignedTeamDetailsBO>();
			//TODO Make deleted team functionality more efficient -20150112 @kpowell
			//remove deleted teams from list for processing
			Iterator<AssignedTeamDetailsBO> iterator = assignedTeamDetailsBOList.iterator();
			while (iterator.hasNext()) {
				AssignedTeamDetailsBO teamView = (AssignedTeamDetailsBO) iterator.next();
				if(!teamView.getTeamBO().isDelete()){
					teams.add(teamView);
				}
				assignedTeamDetailsBOListWithDeletes.add(teamView);
			}
			assignedTeamDetailsBOList.clear();
			assignedTeamDetailsBOList.addAll(teams);//excludes deleted teams when performing validation checks
			//
			
			if(StringUtils.isBlank(roadOperationBO.getCurrentUsername()) || StringUtils.isBlank(roadOperationBO.getStatusId()) || roadOperationBO.getRoadOperationId()==null){
				
				throw new RequiredFieldMissingException();
			}
			
			roadOperationBO.setCurrentUsername(roadOperationBO.getCurrentUsername().trim().toUpperCase());
			
			if(serviceFactory.getStaffUserMappingService().isUserMapped(roadOperationBO.getCurrentUsername())==false){
				throw new InvalidFieldException("Current Username is not mapped");
			}
			
			
			savedOperationDO = serviceFactory.getRoadOperationService().findRoadOperationById(roadOperationBO.getRoadOperationId());
			
			if(savedOperationDO!=null){
			if(savedOperationDO.getStatus().getStatusId().equals(Constants.Status.ROAD_OPERATION_SCHEDULING)){
				if(roadOperationBO.getStatusId().equals(Constants.Status.ROAD_OPERATION_TERMINATED) || roadOperationBO.getStatusId().equals(Constants.Status.ROAD_OPERATION_NO_ACTION) || roadOperationBO.getStatusId().equals(Constants.Status.ROAD_OPERATION_COMPLETED) || roadOperationBO.getStatusId().equals(Constants.Status.ROAD_OPERATION_CLOSED)){
					throw new InvalidFieldException("Cannot update to specified Status");
				}
			}
			else if(savedOperationDO.getStatus().getStatusId().equals(Constants.Status.ROAD_OPERATION_CANCELLED)){
				throw new InvalidFieldException("Cannot update Canncelled Road Operation");
			}
			else if(savedOperationDO.getStatus().getStatusId().equals(Constants.Status.ROAD_OPERATION_STARTED)){
				if(roadOperationBO.getStatusId().equals(Constants.Status.ROAD_OPERATION_SCHEDULING) || roadOperationBO.getStatusId().equals(Constants.Status.ROAD_OPERATION_NO_ACTION) || roadOperationBO.getStatusId().equals(Constants.Status.ROAD_OPERATION_CANCELLED) || roadOperationBO.getStatusId().equals(Constants.Status.ROAD_OPERATION_CLOSED)){
					throw new InvalidFieldException("Cannot update to specified Status");
				}
			}
			else if(savedOperationDO.getStatus().getStatusId().equals(Constants.Status.ROAD_OPERATION_TERMINATED)){
				throw new InvalidFieldException("Cannot update Terminated Road Operation");
			}
			else if(savedOperationDO.getStatus().getStatusId().equals(Constants.Status.ROAD_OPERATION_COMPLETED)){
				if(!roadOperationBO.getStatusId().equals(Constants.Status.ROAD_OPERATION_COMPLETED) && !roadOperationBO.getStatusId().equals(Constants.Status.ROAD_OPERATION_CLOSED)){
					throw new InvalidFieldException("Cannot update to specified Status");
				}
			}
			else if(savedOperationDO.getStatus().getStatusId().equals(Constants.Status.ROAD_OPERATION_CLOSED)){
				throw new InvalidFieldException("Cannot update Closed Road Operation");
			}
			else if(savedOperationDO.getStatus().getStatusId().equals(Constants.Status.ROAD_OPERATION_NO_ACTION)){
				if(!roadOperationBO.getStatusId().equals(Constants.Status.ROAD_OPERATION_STARTED) &&
						!roadOperationBO.getStatusId().equals(Constants.Status.ROAD_OPERATION_CANCELLED)  &&
						!roadOperationBO.getStatusId().equals(Constants.Status.ROAD_OPERATION_NO_ACTION)
						){
					throw new InvalidFieldException("Cannot update to specified Status");
				}
			}
		}
		else{
			throw new InvalidFieldException("Road Operation Id does not exist");
		}
		
			
			
			List<StrategyBO> selectedStrat = roadOperationBO.getOperationStrategyList();
			List<StrategyRuleBO> strategyRuleList = new ArrayList<StrategyRuleBO>();
			
			List<Integer> selectedStratIDs = new ArrayList<Integer>();
			
			for (StrategyBO strat : selectedStrat) {
				selectedStratIDs.add(strat.getStrategyId());
			}
			
			if (selectedStratIDs.size() > 0) {// kpowell
				try {
					strategyRuleList = getOperationStrategyRule(selectedStratIDs);
				} catch (NoRecordFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			boolean isVehicleRequired = false;
			for (StrategyBO strat : selectedStrat) {
				if (strat.isVehicleRequired()) {
					isVehicleRequired= true;
					break;
				}
	
			}
			
			Integer taStaffCount = 0;
			Integer itaExamCount = 0;
			Integer jpCount = 0;
			Integer policeCount = 0;
			Integer vehicleCount = 0;
			boolean teamAtend = true;
			boolean allTeamAttend = true;
			boolean commentRequired = false;
			for(AssignedTeamDetailsBO team : assignedTeamDetailsBOList){
						
				
				//Validate Attendance
				
				teamAtend = true;
				taStaffCount = 0;
				itaExamCount = 0;
				jpCount = 0;
				policeCount = 0;
				vehicleCount = 0;
				if (team.getTaStaffList() != null) {
					for (TAStaffBO taStaff : team.getTaStaffList()) {
						if (taStaff.getAttended() == null) {
							teamAtend = false;
							allTeamAttend=false;
							//break teamsloop;
						}
						else if(taStaff.getAttended()== true){
							taStaffCount++;
						}
						else if(taStaff.getAttended()== false && StringUtils.isBlank(taStaff.getComment())){ 
							commentRequired =true;
						}
						
					}
				}

				if (team.getItaExaminerList() != null) {
					for (ITAExaminerBO itaExam : team.getItaExaminerList()) {

						if (itaExam.getAttended() == null) {
							teamAtend = false;
							allTeamAttend=false;
							//break teamsloop;
						}
						else if(itaExam.getAttended()== true){
							itaExamCount++;
						}
						else if(itaExam.getAttended()== false && StringUtils.isBlank(itaExam.getComment())){ 
							commentRequired =true;
						}
					}
				}

				if (team.getPoliceOfficerList() != null) {
					for (PoliceOfficerBO police : team
							.getPoliceOfficerList()) {

						if (police.getAttended() == null) {
							teamAtend = false;
							allTeamAttend=false;
							//break teamsloop;
						}
						else if(police.getAttended()== true){
							policeCount++;
						}
						else if(police.getAttended()== false && StringUtils.isBlank(police.getComment())){ 
							commentRequired =true;
						}
				}
				}
				if (team.getJpList()!= null) {
					for (JPBO jp : team
							.getJpList()) {

						if (jp.getAttended() == null) {
							teamAtend = false;
							allTeamAttend=false;
							//break teamsloop;
						}
						else if(jp.getAttended()== true){
							jpCount++;
						}
						else if(jp.getAttended()== false && StringUtils.isBlank(jp.getComment())){ 
							commentRequired =true;
						}
					}
				}

				if (team.getTaVehicleList() != null) {
					for (TAVehicleBO vehicle : team.getTaVehicleList()) {

						if (vehicle.getAttended() == null) {
							teamAtend = false;
							allTeamAttend=false;
							//break teamsloop;
						}
						else if(vehicle.getAttended()== true){
							vehicleCount++;
						}
						else if(vehicle.getAttended()== false && StringUtils.isBlank(vehicle.getComment())){ 
							commentRequired =true;
						}
					}
				}	
				if(roadOperationBO.getStatusId().equals(Constants.Status.ROAD_OPERATION_STARTED)){
					if(teamAtend==true){
						
						boolean vehicleAssigned = false;
						if(vehicleCount>0){
							vehicleAssigned = true;
						}
						
						boolean validStrategyRule = validateOperationStrategyRule(strategyRuleList, isVehicleRequired, vehicleAssigned, taStaffCount, jpCount, itaExamCount, policeCount);
	
						if(validStrategyRule==false){
							throw new InvalidFieldException("Operation Strategy not validated");
						}
						
					}
				}
			}
			
		if(roadOperationBO.getStatusId().equals(Constants.Status.ROAD_OPERATION_SCHEDULING) || savedOperationDO.getStatus().getStatusId().equals(Constants.Status.ROAD_OPERATION_SCHEDULING)
				|| roadOperationBO.getStatusId().equalsIgnoreCase(Constants.Status.ROAD_OPERATION_NO_ACTION)){
			
			if(StringUtils.isBlank(roadOperationBO.getOperationName()) || roadOperationBO.getScheduledStartDate() == null || roadOperationBO.getScheduledEndDate() == null
					|| StringUtils.isBlank(roadOperationBO.getScheduledStartTime()) || StringUtils.isBlank(roadOperationBO.getScheduledEndTime()) || roadOperationBO.getOfficeLocCodeList()==null){
				throw new RequiredFieldMissingException();
			}
			
			if(roadOperationBO.getOfficeLocCodeList().size()<=0){
				
				throw new RequiredFieldMissingException();
			}
			
			for(AssignedTeamDetailsBO teamDetails : assignedTeamDetailsBOList){
				if(teamDetails.getTeamBO().getTeamLead()==null || teamDetails.getOperationLocationList()==null){
					
					throw new RequiredFieldMissingException();
				}
				
			}
			
			if(roadOperationBO.getOperationStrategyList()==null){
				
				throw new RequiredFieldMissingException();
			}
			
			
			//R.B. - This validation should only be carried out in the scheduling phase		
			if(roadOperationBO.getStatusId().equals(Constants.Status.ROAD_OPERATION_SCHEDULING))
			{
				if(savedOperationDO.getAuthorized().equalsIgnoreCase("Y") && savedOperationDO.getBackDated().equalsIgnoreCase("Y")){
				throw new InvalidFieldException("Operation cannot be updated because it was backdated and is now authorized");
				}
			}
			
			
			roadOperationOtherDetailsBO = serviceFactory.getRoadOperationService().findRoadOperationOtherDetails(roadOperationBO.getRoadOperationId());
			/***
			 * * removed as this validation should ONLY be applied on a per team basis
			 * //kpowell-2015/01/13
			 
			if(roadOperationOtherDetailsBO!=null){
				if(isOperationStrategyValid(roadOperationOtherDetailsBO.getOperationStrategyList(), assignedTeamDetailsBOList)==false){
					throw new InvalidFieldException("The selected strategies are not consistent with assigned resources.");
				}
			}
			*/
			if(roadOperationBO.getStatusId().equals(Constants.Status.ROAD_OPERATION_CANCELLED) ){
				if(StringUtils.isBlank(roadOperationBO.getComment()) || (roadOperationBO.getReasonId()==null || roadOperationBO.getReasonId()<1)){
					throw new RequiredFieldMissingException();
				}
				
				roadOperationBO.setComment(roadOperationBO.getComment().trim());
			}
			
			if(savedOperationDO.getCategory().getCategoryId().equals(Constants.Category.REGIONAL)){
				if(roadOperationBO.getCourtBO()==null  || roadOperationBO.getCourtDate()==null || StringUtils.isBlank(roadOperationBO.getCourtTime())){
					
					throw new RequiredFieldMissingException();
				}
				
				validateTime(roadOperationBO.getCourtTime());
				roadOperationBO.setCourtTime(roadOperationBO.getCourtTime().concat(":00"));	
				
				try {
					//Validate Court Date
					DateUtils.utilDateToSqlDate(roadOperationBO.getCourtDate());
					
				}
				catch (ParseException e) {
					e.printStackTrace();
					throw new InvalidFieldException();
				}
				
				validateCourtDate(roadOperationBO.getScheduledStartDate(), roadOperationBO.getCourtDate());
				
			}
			
			/*if(StringUtils.isNotBlank(roadOperationBO.getOperationName()) &&  !savedOperationDO.getOperationName().trim().equalsIgnoreCase(roadOperationBO.getOperationName().trim())){
				boolean operationNameExists = false;
				operationNameExists = serviceFactory.getRoadOperationService().operationNameExists(roadOperationBO.getOperationName());
				
				if(operationNameExists == true){
					throw new InvalidFieldException("Operation Name already exists");
				}
			}*/
			
			
			
			

			//Validate Scheduled Start Time
			validateTime(roadOperationBO.getScheduledStartTime());
			roadOperationBO.setScheduledStartTime(roadOperationBO.getScheduledStartTime().concat(":00"));	
			
			//Validate Scheduled End Time
			validateTime(roadOperationBO.getScheduledEndTime());
			roadOperationBO.setScheduledEndTime(roadOperationBO.getScheduledEndTime().concat(":00"));
		
			//validate Scheduled Start and End Date Time
			validateDateTime(roadOperationBO.getScheduledStartDate(), roadOperationBO.getScheduledStartTime(), roadOperationBO.getScheduledEndDate(), roadOperationBO.getScheduledEndTime());

			
			
			//added
			
			TAStaffBO loggedInStaff = new TAStaffBO();
			
			loggedInStaff = serviceFactory.getStaffUserMappingService().getStaffByUsername(roadOperationBO.getCurrentUsername());
			
			
			for(AssignedTeamDetailsBO teamDetails : assignedTeamDetailsBOList){
				
				//Validate Team lead and that the team lead is an assigned Staff to the operation
				
				if(teamDetails.getTeamBO().getTeamLead()!=null){
					
					if(teamDetails.getTaStaffList()!=null){
						List<Integer> assignedTAStaffPersonList = new ArrayList<Integer>();
						assignedTAStaffPersonList = returnIntegerStaffPersonIdList(teamDetails.getTaStaffList());
						if(!assignedTAStaffPersonList.contains(teamDetails.getTeamBO().getTeamLead().getPersonId())){
							throw new InvalidFieldException("Team Lead must be an assigned TA Staff");
						}
					}
				}
				else{
					throw new InvalidFieldException("Team Lead Id does not exist");
				}
				
				
				// Team Name Cannot be Null
				if (StringUtils.isBlank(teamDetails.getTeamBO().getTeamName())) {
					throw new RequiredFieldMissingException("Team Name is required");
				}

				// Team Name must be Unique if adding new team			
				/*if (nameUsedForOtherTeam(teamDetails.getTeamBO().getTeamName(),
						assignedTeamDetailsBOList, teamDetails.getTeamBO().getTeamId())) {
					
					throw new InvalidFieldException("Team Name "+ teamDetails.getTeamBO().getTeamName()+ " already used for another team");

				}*/
				
					
				
				
				//validate location
				List<Integer> locationIdList = new ArrayList<Integer>();
				if(teamDetails.getOperationLocationList()!=null)
				{
					locationIdList = returnIntegerLocationList(teamDetails.getOperationLocationList());
					
					LMISUserViewBO user = this.serviceFactory.getUserService().findUser(roadOperationBO.getCurrentUsername());
					
					
					if(roadOperationBO.getCategoryId().equals(Constants.Category.REGIONAL) && !user.getPermissions().contains(Constants.Permissions.ROMS_ROAD_OP_SCHEDULE_SPC))
					{
						boolean validLocationList = validateRegionalOperationLocation(locationIdList,loggedInStaff.getOfficeLocationCode());
						
						if(validLocationList==false)
						{
							//throw new InvalidFieldException("Invalid Location for Regional Operation");
							throw new InvalidFieldException("Location(s) selected is not within "+roadOperationBO.getCurrentUsername()+" region - "+ loggedInStaff.getOfficeLocationCode());
						}
					}
					List<String> parishList = new ArrayList<String>();
					parishList = returnParishList(locationIdList);
					
					//added fix for Incident:
					//if Kingston parish is in the list, add St. Andrew parish and vice versa
					if(parishList.contains(Constants.ParishKingstonAndStAndrew.KINGSTON)) {
						parishList.add(Constants.ParishKingstonAndStAndrew.ST_ANDREW);
						
					}else if(parishList.contains(Constants.ParishKingstonAndStAndrew.ST_ANDREW)){
						parishList.add(Constants.ParishKingstonAndStAndrew.KINGSTON);						
					}
					//
					//validateAssignedResource(roadOperationBO, teamDetails, parishList, true);
					/*boolean validateAssignedResource = validateAssignedResource(roadOperationBO, teamDetails, parishList, true);
					if(validateAssignedResource==false){
						throw new InvalidFieldException("Invalid Assigned Resource ");
					}*/
					
					//TODO- revise
					if(roadOperationBO.getOperationStrategyList()==null || roadOperationBO.getOperationStrategyList().size()==0){
						throw new RequiredFieldMissingException();
					}
					
					for(StrategyBO strategyId : roadOperationBO.getOperationStrategyList()){
						if(strategyId==null){
							throw new InvalidFieldException();
						}
					}
				
					
					if(assignedTeamDetailsBOList==null){
						throw new RequiredFieldMissingException();
					}
					//
					List<Integer> strategyIdList = returnIntegerStrategyList(roadOperationBO.getOperationStrategyList());
					System.err.println("validateStrategyRule");
					if (serviceFactory.getRoadOperationService().validateStrategyRule(roadOperationBO.getOperationStrategyList(), teamDetails, returnIntegerStrategyList(roadOperationBO.getOperationStrategyList())) == false) {
						throw new InvalidFieldException("The selected strategies are not consistent with assigned resources.");
					}
				}
				else
				{
					throw new RequiredFieldMissingException("At least one location is required");
				}
				
				

				
			}
	
					
			
		//Removed as this rule was not valid					
//			if(savedOperationDO.getBackDated().equalsIgnoreCase("N")){
//				if(roadOperationBO.getScheduledStartDate().before(currentDate) ||  roadOperationBO.getScheduledEndDate().before(currentDate)){
//					throw new InvalidFieldException("Cannot update an operation that was not backdated to a date before today");
//				}
//			}
			
			if(roadOperationBO.getStatusId().equals(Constants.Status.ROAD_OPERATION_STARTED)){
				if(roadOperationBO.getActualStartDate()!=null && StringUtils.isNotBlank(roadOperationBO.getActualStartTime())){
					validateTime(roadOperationBO.getActualStartTime());
					roadOperationBO.setActualStartTime(roadOperationBO.getActualStartTime().concat(":00"));
					try {
						//Validate Actual Start Date
						DateUtils.utilDateToSqlDate(roadOperationBO.getActualStartDate());
						
						//Validate Actual Start Date Time
						validateActualStartDTime(roadOperationBO.getActualStartDate(), roadOperationBO.getActualStartTime(),roadOperationBO.getActualEndDate(), roadOperationBO.getActualEndTime() ,roadOperationBO.getScheduledStartDate(), roadOperationBO.getScheduledStartTime(), roadOperationBO.getScheduledEndDate(),roadOperationBO.getCategoryId());
						
					}
					catch (ParseException e) {
						e.printStackTrace();
						throw new InvalidFieldException();
					}
				}
				else{
					throw new RequiredFieldMissingException("Actual Start Date is required");
				}
				
				//Validate Attendance
				
				if (allTeamAttend == false) {
					throw new RequiredFieldMissingException("Attendance for All Resource is required");
				}
				
				if(commentRequired == true){
					throw new RequiredFieldMissingException("Comment for All Resource that will not attend is required");
				}
			}
					
			
		}
		
		else if(roadOperationBO.getStatusId().equals(Constants.Status.ROAD_OPERATION_STARTED)){
			if(roadOperationBO.getActualStartDate()!=null && StringUtils.isNotBlank(roadOperationBO.getActualStartTime())){
				validateTime(roadOperationBO.getActualStartTime());
				roadOperationBO.setActualStartTime(roadOperationBO.getActualStartTime().concat(":00"));
				try {
					//Validate Actual Start Date
					DateUtils.utilDateToSqlDate(roadOperationBO.getActualStartDate());
					
					
					//Validate Actual Start Date Time
					validateActualStartDTime(roadOperationBO.getActualStartDate(), roadOperationBO.getActualStartTime(),roadOperationBO.getActualEndDate(), roadOperationBO.getActualEndTime(),roadOperationBO.getScheduledStartDate(), roadOperationBO.getScheduledStartTime(), roadOperationBO.getScheduledEndDate(),roadOperationBO.getCategoryId());
				
				}
				catch (ParseException e) {
					e.printStackTrace();
					throw new InvalidFieldException();
				}
			}
			else{
				throw new RequiredFieldMissingException("Actual Start Date is required");
			}
			
	//Validate Attendance
			
			if (allTeamAttend == false) {
				throw new RequiredFieldMissingException("Attendance for All Resource is required");
			}
			
			if(commentRequired == true){
				throw new RequiredFieldMissingException("Comment for All Resource that will not attend is required");
			}
			
		}
		
		else if(roadOperationBO.getStatusId().equals(Constants.Status.ROAD_OPERATION_COMPLETED)){
			if(roadOperationBO.getActualStartDate() == null || roadOperationBO.getActualEndDate() == null
					|| StringUtils.isBlank(roadOperationBO.getActualStartTime()) || StringUtils.isBlank(roadOperationBO.getActualEndTime()) ){
				throw new RequiredFieldMissingException();
			}
			
			//Validate Actual Start Time
			validateTime(roadOperationBO.getActualStartTime());
			roadOperationBO.setActualStartTime(roadOperationBO.getActualStartTime().concat(":00"));
			
			//Validate Scheduled Start Time
			validateTime(roadOperationBO.getScheduledStartTime());
			roadOperationBO.setScheduledStartTime(roadOperationBO.getScheduledStartTime().concat(":00"));	
			
			//Validate Scheduled End Time
			validateTime(roadOperationBO.getScheduledEndTime());
			roadOperationBO.setScheduledEndTime(roadOperationBO.getScheduledEndTime().concat(":00"));
			
			//Validate Actual End Time
			validateTime(roadOperationBO.getActualEndTime());
			roadOperationBO.setActualEndTime(roadOperationBO.getActualEndTime().concat(":00"));
					
			//validate Actual Start and End Date Time
			validateDateTime(roadOperationBO.getActualStartDate(), roadOperationBO.getActualStartTime(), roadOperationBO.getActualEndDate(), roadOperationBO.getActualEndTime());
			
			//Validate Actual Start Date Time
			validateActualStartDTime(roadOperationBO.getActualStartDate(), roadOperationBO.getActualStartTime(),roadOperationBO.getActualEndDate(), roadOperationBO.getActualEndTime(), roadOperationBO.getScheduledStartDate(), roadOperationBO.getScheduledStartTime(), roadOperationBO.getScheduledEndDate(),roadOperationBO.getCategoryId());
			
			validateActualEndDTime(roadOperationBO.getActualStartDate(), roadOperationBO.getActualStartTime(),roadOperationBO.getActualEndDate(), roadOperationBO.getActualEndTime(), roadOperationBO.getScheduledStartDate(), roadOperationBO.getScheduledStartTime(), roadOperationBO.getScheduledEndDate(),roadOperationBO.getCategoryId());
		
			//added by kpowell
			//2014-10-15::validate actual duration with offence date
			serviceFactory.getRoadOperationService().validateActualStartDateAgainstOffenceDates(roadOperationBO.getRoadOperationId(),
					roadOperationBO.getActualStartDtime());
			serviceFactory.getRoadOperationService().validateActualEndDateAgainstOffenceDates(roadOperationBO.getRoadOperationId(),
					roadOperationBO.getActualEndDtime());
			//System.err.println("inside RoadOperation ::"+validDatesTxt );
			
			
			//Validate Attendance
			
			if (allTeamAttend == false) {
				throw new RequiredFieldMissingException("Attendance for All Resource is required");
			}
			
			if(commentRequired == true){
				throw new RequiredFieldMissingException("Comment for All Resource that will not attend is required");
			}
		
		}
		else if(roadOperationBO.getStatusId().equals(Constants.Status.ROAD_OPERATION_CLOSED)){
			if(savedOperationDO.getStatus().getStatusId().equals(Constants.Status.ROAD_OPERATION_COMPLETED)){
			
				if(roadOperationBO.getActualStartDate() == null || roadOperationBO.getActualEndDate() == null
						|| StringUtils.isBlank(roadOperationBO.getActualStartTime()) || StringUtils.isBlank(roadOperationBO.getActualEndTime()) ){
					throw new RequiredFieldMissingException();
				}
				
				//added by kpowell
				//2014-10-15::validate actual duration with offence date
				serviceFactory.getRoadOperationService().validateActualStartDateAgainstOffenceDates(roadOperationBO.getRoadOperationId(),
						roadOperationBO.getActualStartDtime());
				serviceFactory.getRoadOperationService().validateActualEndDateAgainstOffenceDates(roadOperationBO.getRoadOperationId(),
						roadOperationBO.getActualEndDtime());					
				
				
			}
			
			/*if(savedOperationDO.getStatus().getStatusId().equals(Constants.Status.ROAD_OPERATION_NO_ACTION)){
				if(StringUtils.isBlank(roadOperationBO.getComment())){
					throw new RequiredFieldMissingException();
				}
			}*/
			
			if(StringUtils.isNotBlank(roadOperationBO.getComment())){
				roadOperationBO.setComment(roadOperationBO.getComment().trim());
			}
			
			if(StringUtils.isNotBlank(roadOperationBO.getActualStartTime())){
				//Validate Actual Start Time
				validateTime(roadOperationBO.getActualStartTime());
				roadOperationBO.setActualStartTime(roadOperationBO.getActualStartTime().concat(":00"));
			}
			
			if(StringUtils.isNotBlank(roadOperationBO.getActualEndTime())){
				//Validate Actual End Time
				validateTime(roadOperationBO.getActualEndTime());
				roadOperationBO.setActualEndTime(roadOperationBO.getActualEndTime().concat(":00"));
			}
			
			if(roadOperationBO.getActualStartDate()!=null && StringUtils.isNotBlank(roadOperationBO.getActualStartTime()) && roadOperationBO.getActualEndDate()!=null &&  StringUtils.isNotBlank(roadOperationBO.getActualEndTime())){
				//validate Actual Start and End Date Time
				validateDateTime(roadOperationBO.getActualStartDate(), roadOperationBO.getActualStartTime(), roadOperationBO.getActualEndDate(), roadOperationBO.getActualEndTime());
				
				//Validate Actual Start Date Time
				validateActualStartDTime(roadOperationBO.getActualStartDate(), roadOperationBO.getActualStartTime(),roadOperationBO.getActualEndDate(), roadOperationBO.getActualEndTime(), roadOperationBO.getScheduledStartDate(), roadOperationBO.getScheduledStartTime(), roadOperationBO.getScheduledEndDate(),roadOperationBO.getCategoryId());
				
				validateActualEndDTime(roadOperationBO.getActualStartDate(), roadOperationBO.getActualStartTime(),roadOperationBO.getActualEndDate(), roadOperationBO.getActualEndTime(), roadOperationBO.getScheduledStartDate(), roadOperationBO.getScheduledStartTime(), roadOperationBO.getScheduledEndDate(),roadOperationBO.getCategoryId());
			
			}
				
			
		}
		else if(roadOperationBO.getStatusId().equals(Constants.Status.ROAD_OPERATION_CANCELLED) || roadOperationBO.getStatusId().equals(Constants.Status.ROAD_OPERATION_TERMINATED)){
			if(roadOperationBO.getStatusId().equals(Constants.Status.ROAD_OPERATION_TERMINATED)){
				
				if(roadOperationBO.getActualStartDate()!=null && StringUtils.isNotBlank(roadOperationBO.getActualStartTime())){
					validateTime(roadOperationBO.getActualStartTime());
					roadOperationBO.setActualStartTime(roadOperationBO.getActualStartTime().concat(":00"));
					try {
						//Validate Actual Start Date
						DateUtils.utilDateToSqlDate(roadOperationBO.getActualStartDate());
						
						//Validate Actual Start Date Time
						validateActualStartDTime(roadOperationBO.getActualStartDate(), roadOperationBO.getActualStartTime(),roadOperationBO.getActualEndDate(), roadOperationBO.getActualEndTime(), roadOperationBO.getScheduledStartDate(), roadOperationBO.getScheduledStartTime(), roadOperationBO.getScheduledEndDate(),roadOperationBO.getCategoryId());
						validateActualEndDTime(roadOperationBO.getActualStartDate(), roadOperationBO.getActualStartTime(),roadOperationBO.getActualEndDate(), roadOperationBO.getActualEndTime(), roadOperationBO.getScheduledStartDate(), roadOperationBO.getScheduledStartTime(), roadOperationBO.getScheduledEndDate(),roadOperationBO.getCategoryId());
						
					}
					catch (ParseException e) {
						e.printStackTrace();
						throw new InvalidFieldException();
					}
				}
				else{
					throw new RequiredFieldMissingException("Actual Start Date is required");
				}
					
				if(roadOperationBO.getActualEndDate()!=null && StringUtils.isNotBlank(roadOperationBO.getActualEndTime())){
					
					validateTime(roadOperationBO.getActualEndTime());
					roadOperationBO.setActualEndTime(roadOperationBO.getActualEndTime().concat(":00")); //??
					//Validate Actual Start Date
					try {
						DateUtils.utilDateToSqlDate(roadOperationBO.getActualEndDate());
					
						
						//2014-10-15::validate actual duration with offence date
						serviceFactory.getRoadOperationService().validateActualStartDateAgainstOffenceDates(roadOperationBO.getRoadOperationId(),
								roadOperationBO.getActualStartDtime());
						serviceFactory.getRoadOperationService().validateActualEndDateAgainstOffenceDates(roadOperationBO.getRoadOperationId(),
								roadOperationBO.getActualEndDtime());
						
					
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else{
					throw new RequiredFieldMissingException("Actual End Date is required");
				}
				
				
				
				
			}
			
			if(StringUtils.isBlank(roadOperationBO.getComment()) || (roadOperationBO.getReasonId()==null || roadOperationBO.getReasonId()<1)){
				throw new RequiredFieldMissingException();
			}
			roadOperationBO.setComment(roadOperationBO.getComment().trim());
		}
		
		try
		{
			//Use the object list which includes the deleted teams
			//kpowell
			//updated = serviceFactory.getRoadOperationService().updateRoadOperation(roadOperationBO, assignedTeamDetailsBOList);
			updated = serviceFactory.getRoadOperationService().updateRoadOperation(roadOperationBO, assignedTeamDetailsBOListWithDeletes);
		}
		catch(Exception e)
		{
			
			e.printStackTrace();
			throw new ErrorSavingException();
			
		}
		
//		if(updated==false){
//			throw new ErrorSavingException();
//		}
		
	}
	
	
	/*public void updateRoadOperationTeam(RoadOperationBO roadOperationBO, AssignedTeamDetailsBO assignedTeamDetailsBO)
			throws RequiredFieldMissingException, InvalidFieldException, ErrorSavingException{
		
		boolean updated = false;
		TAStaffBO loggedInStaff = new TAStaffBO();
		
		

		if(StringUtils.isBlank(roadOperationBO.getCurrentUsername()) || StringUtils.isBlank(roadOperationBO.getStatusId()) || roadOperationBO.getRoadOperationId()==null){
			throw new RequiredFieldMissingException();
		}
		
		if(StringUtils.isBlank(roadOperationBO.getOperationName()) || roadOperationBO.getScheduledStartDate() == null || roadOperationBO.getScheduledEndDate() == null
				|| StringUtils.isBlank(roadOperationBO.getScheduledStartTime()) || StringUtils.isBlank(roadOperationBO.getScheduledEndTime())){
			throw new RequiredFieldMissingException();
		}
		
		loggedInStaff = serviceFactory.getStaffUserMappingService().getStaffByUsername(roadOperationBO.getCurrentUsername());
		
		
	
			
			//Validate Team lead and that the team lead is an assigned Staff to the operation
			
			if(assignedTeamDetailsBO.getTeamBO().getTeamLead()!=null){
				
				if(assignedTeamDetailsBO.getTaStaffList()!=null){
					List<Integer> assignedTAStaffPersonList = new ArrayList<Integer>();
					assignedTAStaffPersonList = returnIntegerStaffPersonIdList(assignedTeamDetailsBO.getTaStaffList());
					if(!assignedTAStaffPersonList.contains(assignedTeamDetailsBO.getTeamBO().getTeamLead().getPersonId())){
						throw new InvalidFieldException("Team Lead must be an assigned TA Staff");
					}
				}
			}
			else{
				throw new InvalidFieldException("Team Lead Id does not exist");
			}
			
			
			
				
			
			
			//validate location
			List<Integer> locationIdList = new ArrayList<Integer>();
			if(assignedTeamDetailsBO.getOperationLocationList()!=null){
				locationIdList = returnIntegerLocationList(assignedTeamDetailsBO.getOperationLocationList());
				if(roadOperationBO.getCategoryId().equals(Constants.Category.REGIONAL)){
					boolean validLocationList = validateRegionalOperationLocation(locationIdList,loggedInStaff.getOfficeLocationCode());
					if(validLocationList==false){
						throw new InvalidFieldException("Invalid Location for Regional Operation");
					}
				}
				List<String> parishList = new ArrayList<String>();
				parishList = returnParishList(locationIdList);
				
				boolean validateAssignedResource = validateAssignedResource(roadOperationBO, assignedTeamDetailsBO, loggedInStaff.getOfficeLocationCode(), parishList, true);
				if(validateAssignedResource==false){
					throw new InvalidFieldException("Invalid Assigned Resource ");
				}
			}
			else{
				throw new RequiredFieldMissingException("At least one location is required");
			}
			
			
			updated = serviceFactory.getRoadOperationService().updateRoadOperationTeam(roadOperationBO, assignedTeamDetailsBO);
			
			if(updated==false){
				throw new ErrorSavingException();
			}
			
		
	}*/
	
	/**
	 * This method return a Integer List of Strategy Id from the Operation Strategy List passed
	 * @param operationStrategyList
	 * @return
	 */
	private List<Integer> returnIntegerOperationStrategyList(List<OperationStrategyBO> operationStrategyList){
		List<Integer> integerList = new ArrayList<Integer>();
		
		for(OperationStrategyBO strategy : operationStrategyList){
			integerList.add(strategy.getStrategyId());
		}
		
		return integerList;
	}
	
	/**
	 * This method return a Integer List of Strategy Id from the Strategy List passed
	 * @param operationStrategyList
	 * @return
	 */
	private List<Integer> returnIntegerStrategyList(List<StrategyBO> strategyList){
		List<Integer> integerList = new ArrayList<Integer>();
		
		for(StrategyBO strategy : strategyList){
			integerList.add(strategy.getStrategyId());
		}
		
		return integerList;
	}
	
	
	/**
	 * This method return a Integer List of Artery Id from the Artery List passed
	 * @param operationStrategyList
	 * @return
	 */
	private List<Integer> returnIntegerArteryList(List<ArteryBO> arteryList){
		List<Integer> integerList = new ArrayList<Integer>();
		
		for(ArteryBO artery : arteryList){
			integerList.add(artery.getArteryId());
		}
		
		return integerList;
	}
	
	
	/**
	 * This method return a Integer List of Location Id from the Location List passed
	 * @param operationStrategyList
	 * @return
	 */
	private List<Integer> returnIntegerLocationList(List<LocationBO> locationList){
		List<Integer> integerList = new ArrayList<Integer>();
		
		for(LocationBO location : locationList){
			integerList.add(location.getLocationId());
		}
		
		return integerList;
	}
	
	/**
	 * This method return a Integer List of Location Id from the Operation Location List passed
	 * @param operationLocationList
	 * @return
	 */
	private List<Integer> returnIntegerOperationLocationList(List<OperationLocationBO> operationLocationList){
		List<Integer> integerList = new ArrayList<Integer>();
		
		for(OperationLocationBO location : operationLocationList){
			integerList.add(location.getLocationId());
		}
		
		return integerList;
	}
	
	/**
	 * This method return a Integer List of Person Id from the Assigned Person List passed
	 * @param assignedPersonList
	 * @return
	 */
	private List<Integer> returnIntegerAssignedPersonList(List<AssignedPersonBO> assignedPersonList){
		List<Integer> integerList = new ArrayList<Integer>();
		
		for(AssignedPersonBO assignedPersonBO : assignedPersonList){
			integerList.add(assignedPersonBO.getPersonId());
		}
		
		return integerList;
	}
	
	/**
	 * This method return a Integer List of TA Vehicle Id from the Assigned Vehicle List passed
	 * @param assignedVehicleList
	 * @return
	 */
	private List<Integer> returnIntegerAssignedVehicleList(List<AssignedVehicleBO> assignedVehicleList){
		List<Integer> integerList = new ArrayList<Integer>();
		
		for(AssignedVehicleBO assignedVehicleBO : assignedVehicleList){
			integerList.add(assignedVehicleBO.getTaVehicle().getTaVehicleId());
		}
		
		return integerList;
	}
	
	/**
	 * This method return a Integer List of TA Vehicle Id from the TA Vehicle List passed
	 * @param taVehicleList
	 * @return
	 */
	private List<Integer> returnIntegerTAVehicleList(List<TAVehicleBO> taVehicleList){
		List<Integer> integerList = new ArrayList<Integer>();
		
		for(TAVehicleBO taVehicleBO : taVehicleList){
			integerList.add(taVehicleBO.getTaVehicleId());
		}
		
		return integerList;
	}
	
	/**
	 * This method return a Integer List of Person Id from the TA Staff List passed
	 * @param taStaffBOList
	 * @return
	 */
	private List<Integer> returnIntegerStaffPersonIdList(List<TAStaffBO> taStaffBOList){
		List<Integer> integerList = new ArrayList<Integer>();
		
		for(TAStaffBO staffBO : taStaffBOList){
			integerList.add(staffBO.getPersonId());
		}
		
		return integerList;
	}
	
	/**
	 * This method return a Integer List of Person Id from the JP List passed
	 * @param jpBOList
	 * @return
	 */
	private List<Integer> returnIntegerJpPersonIdList(List<JPBO> jpBOList){
		List<Integer> integerList = new ArrayList<Integer>();
		//System.err.println("inside returnIntegerJpPersonIdList");
		for(JPBO jpBO : jpBOList){
			integerList.add(jpBO.getPersonBO().getPersonId());
			//System.err.println(jpBO.getPersonBO().getFullName()+ "- "+jpBO.getPersonBO().getPersonId());
		}
		
		//System.err.println("exit returnIntegerJpPersonIdList");
		return integerList;
	}
	
	/**
	 * This method return a Integer List of Person Id from the Police Officer List passed
	 * @param policeOfficerBOList
	 * @return
	 */
	private List<Integer> returnIntegerPoliceOfficerPersonIdList(List<PoliceOfficerBO> policeOfficerBOList){
		List<Integer> integerList = new ArrayList<Integer>();
		
		for(PoliceOfficerBO policeOfficerBO : policeOfficerBOList){
			integerList.add(policeOfficerBO.getPersonID());
		}
		 
		return integerList;
	}
	
	/**
	 * This method return a Integer List of Person Id from the ITA Examiner List passed
	 * @param itaExaminerBOList
	 * @return
	 */
	private List<Integer> returnIntegerITAExaminerPersonIdList(List<ITAExaminerBO> itaExaminerBOList){
		List<Integer> integerList = new ArrayList<Integer>();
		
		for(ITAExaminerBO itaExaminerBO : itaExaminerBOList){
			integerList.add(itaExaminerBO.getPersonBO().getPersonId());
		}
		
		return integerList;
	}
	
	
	/**
	 * This method checks if the time passed is valid
	 * @param time
	 * @throws InvalidFieldException
	 */
	private void validateTime(String time)throws InvalidFieldException{
		if(time.length()>5){
			//throw new InvalidFieldException();
			time = time.substring(0, 5);
		}
		Date parsedTime = DateUtils.parse(time, "HH:mm");
		
		if(parsedTime==null){
			throw new InvalidFieldException();
		}
	}
	
	
	private List<String> returnStringRegionParishList(List<ParishDO> parishList){
		List<String> stringList = new ArrayList<String>();
		
		for(ParishDO region : parishList){
			stringList.add(region.getParishCode());
		}
		
		return stringList;
	}
	
	private List<String> returnStringLocationParishList(List<LocationDO> locationList){
		List<String> stringList = new ArrayList<String>();
		
		for(LocationDO location : locationList){
			stringList.add(location.getParish().getParishCode());
		}
		
		return stringList;
	}
	
	/**
	 * This method checks if the the start date, time and end date, time is valid
	 * @param startDate
	 * @param startTime
	 * @param endDate
	 * @param endTime
	 * @throws InvalidFieldException
	 */
	private void validateDateTime(Date startDate, String startTime, Date endDate, String endTime) throws InvalidFieldException{
		try {
			//Validate Start and End Date
			DateUtils.utilDateToSqlDate(startDate);
			DateUtils.utilDateToSqlDate(endDate);
			
			String sDate = DateUtils.formatDate("yyyy-MM-dd", startDate);
			Date startDTime = DateUtils.parse(sDate + " " +  startTime, "yyyy-MM-dd HH:mm:ss");
			String eDate = DateUtils.formatDate("yyyy-MM-dd", endDate);
			Date endDTime = DateUtils.parse(eDate + " " +  endTime, "yyyy-MM-dd HH:mm:ss");
			
			if(startDTime.after(endDTime))
			{
				throw new InvalidFieldException("Start Date cannot be after End Date");
			}
			
			if(startDTime.equals(endDTime))
			{
				throw new InvalidFieldException("Start Date cannot be same as End Date");
			}
			
			if(DateUtils.getDateDiff(startDTime, endDTime, TimeUnit.HOURS) < 1)
			{
				throw new InvalidFieldException("An operation has to have a minimum of ONE (1) hour.");
			}
		}
		catch (ParseException e) {
			e.printStackTrace();
			throw new InvalidFieldException();
		}
	}
	
	/**
	 * This method validates actual start date, time against scheduled start date, time and scheduled end date
	 * @param actualStartDate
	 * @param actualStartTime
	 * @param scheduledStartDate
	 * @param scheduledStartTime
	 * @param scheduledEndDate
	 * @throws InvalidFieldException
	 */
	private void validateActualStartDTime(Date actualStartDate, String actualStartTime,Date actualEndDate, String actualEndTime, Date scheduledStartDate, String scheduledStartTime, Date scheduledEndDate, String operationCategory,String...terminateMethodVar) throws InvalidFieldException{
		
		String methodCall = "";
		
		if(terminateMethodVar.length > 0 ){
			methodCall = terminateMethodVar[0].toString();
		}
		
			
		String actualSDate = DateUtils.formatDate("yyyy-MM-dd", actualStartDate);
		Date actualStartDTime = DateUtils.parse(actualSDate + " " +  actualStartTime, "yyyy-MM-dd HH:mm");
		
		String scheduledSDate = DateUtils.formatDate("yyyy-MM-dd", scheduledStartDate);
		Date scheduledStartDTime = DateUtils.parse(scheduledSDate + " " +  scheduledStartTime, "yyyy-MM-dd HH:mm");
		
		
		
		if(((!DateUtils.getOrdinalDayOfMonthFromDate(actualStartDTime).equalsIgnoreCase(DateUtils.getOrdinalDayOfMonthFromDate(scheduledStartDTime)))
				|| (DateUtils.getOrdinalDayOfMonthFromDate(actualStartDTime).equalsIgnoreCase(DateUtils.getOrdinalDayOfMonthFromDate(scheduledStartDTime)) &&  DateUtils.getDateDiff(actualStartDTime, scheduledStartDTime, TimeUnit.DAYS) != 0))
				&& operationCategory.equalsIgnoreCase(Constants.Category.REGIONAL))
		{
			throw new InvalidFieldException("Actual start date and time must be on the same day as the scheduled start date.");
		}
		
		if(DateUtils.getDateDiff(actualStartDTime, scheduledStartDTime, TimeUnit.DAYS) < 0 )
		{
			throw new InvalidFieldException("Actual start date and time must be on same day as the scheduled start date.");
		}
			
		if(actualEndDate != null && actualEndDate.before(actualStartDate))
		{
			throw new InvalidFieldException("Actual start date and time must be before actual end date time.");
		}
		
		//System.err.println("This is validateStart " + terminateMethodVar[0].toString());
		//Added extra condition !methodCall.equals(Constants.Status.ROAD_OPERATION_TERMINATED) which prevents this condition from being met when the method is being called from terminateRoadOp by Ricardo Thompson 11/3/2014
		if(actualEndDate != null && DateUtils.getDateDiff(actualStartDate, actualEndDate, TimeUnit.HOURS) < 1 && !methodCall.equals(Constants.Status.ROAD_OPERATION_TERMINATED))
		{
			throw new InvalidFieldException("An operation has to have a minimum of ONE (1) hour.");
		}
		
		scheduledEndDate.setHours(0);
		scheduledEndDate.setMinutes(0);
		scheduledEndDate.setSeconds(0);
		
		actualStartDate.setHours(0);
		actualStartDate.setMinutes(0);
		actualStartDate.setSeconds(0);
		
		scheduledStartDTime.setHours(0);
		scheduledStartDTime.setMinutes(0);
		scheduledStartDTime.setSeconds(0);
		
		
		if(actualStartDate.after(scheduledEndDate))
		{
			throw new InvalidFieldException("Actual start date cannot be after scheduled end date");
		}
		
		if(actualStartDate.before(scheduledStartDTime))
		{
			throw new InvalidFieldException("Actual start date cannot be before scheduled start date");
		}
		
		
	}
	
	/**
	 * This method validates actual start date, time against scheduled start date, time and scheduled end date
	 * @param actualStartDate
	 * @param actualStartTime
	 * @param scheduledStartDate
	 * @param scheduledStartTime
	 * @param scheduledEndDate
	 * @throws InvalidFieldException
	 */
	private void validateActualEndDTime(Date actualStartDate, String actualStartTime,Date actualEndDate, String actualEndTime, Date scheduledStartDate, String scheduledStartTime, Date scheduledEndDate, String operationCategory,String...terminateMethodVar) throws InvalidFieldException{
	
		String methodCall = "";
		
		if(terminateMethodVar.length > 0 ){
			methodCall = terminateMethodVar[0].toString();
		}
		
		String actualSDate = DateUtils.formatDate("yyyy-MM-dd", actualStartDate);
		Date actualStartDTime = DateUtils.parse(actualSDate + " " +  actualStartTime, "yyyy-MM-dd HH:mm");
		
		String actualEDate = DateUtils.formatDate("yyyy-MM-dd", actualEndDate);
		Date actualEndDTime = DateUtils.parse(actualEDate + " " +  actualEndTime, "yyyy-MM-dd HH:mm");
		
		String scheduledSDate = DateUtils.formatDate("yyyy-MM-dd", scheduledStartDate);
		Date scheduledStartDTime = DateUtils.parse(scheduledSDate + " " +  scheduledStartTime, "yyyy-MM-dd HH:mm");
		
		
		
		if(((!DateUtils.getOrdinalDayOfMonthFromDate(actualEndDTime).equalsIgnoreCase(DateUtils.getOrdinalDayOfMonthFromDate(scheduledStartDTime)))
				|| (DateUtils.getOrdinalDayOfMonthFromDate(actualEndDTime).equalsIgnoreCase(DateUtils.getOrdinalDayOfMonthFromDate(scheduledStartDTime)) &&  DateUtils.getDateDiff(actualEndDTime, scheduledStartDTime, TimeUnit.DAYS) != 0)) 
				&& !methodCall.equals(Constants.Status.ROAD_OPERATION_TERMINATED)
				&& operationCategory.equalsIgnoreCase(Constants.Category.REGIONAL))
		{
			throw new InvalidFieldException("Actual end date and time must be on the same day as the scheduled start date.");
		}
		
		
		if(actualEndDate != null && actualEndDate.before(actualStartDate))
		{
			throw new InvalidFieldException("Actual end date time must be before actual end date time.");
		}
			
		//Added extra condition !methodCall.equals(Constants.Status.ROAD_OPERATION_TERMINATED) which prevents this condition from being met when the method is being called from terminateRoadOp by Ricardo Thompson 11/3/2014
		if(actualEndDate != null && DateUtils.getDateDiff(actualStartDate, actualEndDate, TimeUnit.HOURS) < 1 && !methodCall.equals(Constants.Status.ROAD_OPERATION_TERMINATED))
		{
			throw new InvalidFieldException("An operation has to have a minimum of ONE (1) hour.");
		}
		
		scheduledEndDate.setHours(0);
		scheduledEndDate.setMinutes(0);
		scheduledEndDate.setSeconds(0);
		
		actualStartDate.setHours(0);
		actualStartDate.setMinutes(0);
		actualStartDate.setSeconds(0);
		
		actualEndDate.setHours(0);
		actualEndDate.setMinutes(0);
		actualEndDate.setSeconds(0);
		
		
		if(actualEndDate.before(actualStartDate))
		{
			throw new InvalidFieldException("Actual end date and time cannot be after actual start date and time.");
		}
		
		
		if(actualEndDate.after(scheduledEndDate))
		{
			throw new InvalidFieldException("Actual end date and time cannot be after scheduled end date and time.");
		}
		
	
		
	}
	
	/**
	 * This method return a List of Staff Person Id that has been added to an existing operation. 
	 * The method removes the Person Id's that were saved previously to the operation
	 * @param assignedList
	 * @param savedList
	 * @return
	 */
	private List<Integer> returnOnlyNewlyAddedStaffPersonIntegerList(List<TAStaffBO> assignedList, List<AssignedPersonBO>  savedList){
			
		List<Integer> assignedStaffList = returnIntegerStaffPersonIdList(assignedList);
		List<Integer> savedStaffList = returnIntegerAssignedPersonList(savedList);
	
		for(Integer savedPersonId : savedStaffList){
			if(assignedStaffList.contains(savedPersonId)){
				assignedStaffList.remove(savedPersonId);
			}
		}
		
		
		return assignedStaffList;
		
	}
	
	/**
	 * This method return a List of JP Person Id that has been added to an existing operation. 
	 * The method removes the Person Id's that were saved previously to the operation
	 * @param assignedList
	 * @param savedList
	 * @return
	 */
	private List<Integer> returnOnlyNewlyAddedJPPersonIntegerList(List<JPBO> assignedList, List<AssignedPersonBO>  savedList){
			//System.err.println("inside returnOnlyNewlyAddedJPPersonIntegerList");
		List<Integer> assignedStaffList = returnIntegerJpPersonIdList(assignedList);
		List<Integer> savedStaffList = returnIntegerAssignedPersonList(savedList);
	
		for(Integer savedPersonId : savedStaffList){
			if(assignedStaffList.contains(savedPersonId)){
				//System.err.println("savedPersonId- "+savedPersonId);
				assignedStaffList.remove(savedPersonId);
			}
		}
		
		
		return assignedStaffList;
		
	}
	
	/**
	 * This method return a List of ITA Examiner Person Id that has been added to an existing operation. 
	 * The method removes the Person Id's that were saved previously to the operation
	 * @param assignedList
	 * @param savedList
	 * @return
	 */
	private List<Integer> returnOnlyNewlyAddedITAExaminerPersonIntegerList(List<ITAExaminerBO> assignedList, List<AssignedPersonBO>  savedList){
			
		List<Integer> assignedStaffList = returnIntegerITAExaminerPersonIdList(assignedList);
		List<Integer> savedStaffList = returnIntegerAssignedPersonList(savedList);
	
		for(Integer savedPersonId : savedStaffList){
			if(assignedStaffList.contains(savedPersonId)){
				assignedStaffList.remove(savedPersonId);
			}
		}
		
		
		return assignedStaffList;
		
	}
	
	/**
	 * This method return a List of Police Officer Person Id that has been added to an existing operation. 
	 * The method removes the Person Id's that were saved previously to the operation
	 * @param assignedList
	 * @param savedList
	 * @return
	 */
	private List<Integer> returnOnlyNewlyAddedPoliceOfficerPersonIntegerList(List<PoliceOfficerBO> assignedList, List<AssignedPersonBO>  savedList){
			
		List<Integer> assignedStaffList = returnIntegerPoliceOfficerPersonIdList(assignedList);
		List<Integer> savedStaffList = returnIntegerAssignedPersonList(savedList);
	
		for(Integer savedPersonId : savedStaffList){
			if(assignedStaffList.contains(savedPersonId)){
				assignedStaffList.remove(savedPersonId);
			}
		}
		
		
		return assignedStaffList;
		
	}
	
	/**
	 * This method return a List of Person Id that has been added to an existing operation. 
	 * The method removes the Person Id's that were saved previously to the operation
	 * @param assignedList
	 * @param savedList
	 * @return
	 */
	private List<Integer> returnOnlyNewlyAddedPersonIntegerList(List<AssignedPersonBO> assignedList, List<AssignedPersonBO>  savedList){
			
		List<Integer> assignedStaffList = returnIntegerAssignedPersonList(assignedList);
		List<Integer> savedStaffList = returnIntegerAssignedPersonList(savedList);
	
		for(Integer savedPersonId : savedStaffList){
			if(assignedStaffList.contains(savedPersonId)){
				assignedStaffList.remove(savedPersonId);
			}
		}
		
		
		return assignedStaffList;
		
	}
	/**
	 * This method return a List of TA Vehicle Id that has been added to an existing operation. 
	 * The method removes the TA Vehicle  Id's that were saved previously to the operation
	 * @param assignedList
	 * @param savedList
	 * @return
	 */
	private List<Integer> returnOnlyNewlyAddedVehicleIntegerList(List<AssignedVehicleBO> assignedList, List<AssignedVehicleBO>  savedList){
		
		List<Integer> assignedStaffList = returnIntegerAssignedVehicleList(assignedList);
		List<Integer> savedStaffList = returnIntegerAssignedVehicleList(savedList);
	
		for(Integer savedVehicleId : savedStaffList){
			if(assignedStaffList.contains(savedVehicleId)){
				assignedStaffList.remove(savedVehicleId);
			}
		}
		
		
		return assignedStaffList;
		
	}
	/**
	 * This method return a List of TA Vehicle Id that has been added to an existing operation. 
	 * The method removes the TA Vehicle  Id's that were saved previously to the operation
	 * @param assignedList
	 * @param savedList
	 * @return
	 */
	private List<Integer> returnOnlyNewlyAddedTAVehicleIntegerList(List<TAVehicleBO> assignedList, List<AssignedVehicleBO>  savedList){
		
		List<Integer> assignedStaffList = returnIntegerTAVehicleList(assignedList);
		List<Integer> savedStaffList = returnIntegerAssignedVehicleList(savedList);
	
		for(Integer savedVehicleId : savedStaffList){
			if(assignedStaffList.contains(savedVehicleId)){
				assignedStaffList.remove(savedVehicleId);
			}
		}
		
		
		return assignedStaffList;
	}
	
	/**
	 * This method updates an existing Road Operation that has not yet been authorized with the Authorization details passed
	 * @param roadOperationBO
	 * @throws RequiredFieldMissingException
	 * @throws InvalidFieldException
	 * @throws ErrorSavingException
	 */
	public void authorizeRoadOperation(RoadOperationBO roadOperationBO) throws RequiredFieldMissingException, InvalidFieldException, ErrorSavingException{
		RoadOperationDO savedOperationDO = new RoadOperationDO();
		boolean authorized = true;
		
		if((roadOperationBO.getRoadOperationId()==null || roadOperationBO.getRoadOperationId()<1) || StringUtils.isBlank(roadOperationBO.getAuthorizedUserName()) || StringUtils.isBlank(roadOperationBO.getCurrentUsername())){
			throw new RequiredFieldMissingException();
		}	
		roadOperationBO.setCurrentUsername(roadOperationBO.getCurrentUsername().toUpperCase());
		roadOperationBO.setAuthorizedUserName(roadOperationBO.getAuthorizedUserName().toUpperCase());
		
		savedOperationDO = serviceFactory.getRoadOperationService().findRoadOperationById(roadOperationBO.getRoadOperationId());
		
		if(savedOperationDO!=null){
			if(savedOperationDO.getAuthorized().equalsIgnoreCase("Y") && savedOperationDO.getBackDated().equalsIgnoreCase("Y")){
				throw new InvalidFieldException("Road Operation already Authorized");
			}
			else{
			
				if(roadOperationBO.getAuthorizedUserName().equalsIgnoreCase(savedOperationDO.getAuditEntry().getCreateUsername()) || roadOperationBO.getAuthorizedUserName().equalsIgnoreCase(savedOperationDO.getAuditEntry().getUpdateUsername())){
					throw new InvalidFieldException("Authorized Username cannot be the same as the Create Username or Update Username");
				}
				
				authorized = serviceFactory.getRoadOperationService().authorizeRoadOperation(roadOperationBO);
				
				if(authorized==false){
					throw new ErrorSavingException();
				}
			}
		}else{
			throw new InvalidFieldException("Road Operation ID does not exist");
		}
		
		
	}
	
public List<RoadOperationBO> lookupRoadOperationForRoadCheck(String operationStr)throws RequiredFieldMissingException, NoRecordFoundException{
		
		List<RoadOperationBO> roadOperationBOList = new ArrayList<RoadOperationBO>();
		
		if(StringUtils.isBlank(operationStr)){
			throw new RequiredFieldMissingException();
		}
		
		
		roadOperationBOList =  serviceFactory.getRoadOperationService().findOperationForRoadCheckList(operationStr);
		
		if(roadOperationBOList==null || roadOperationBOList.size()==0){
			throw new NoRecordFoundException();
		}
		
		return roadOperationBOList;
	}

public List<RoadOperationBO> lookupAllRoadOperationForRoadCheck()throws NoRecordFoundException{
	
	List<RoadOperationBO> roadOperationBOList = new ArrayList<RoadOperationBO>();
	
	
	
	roadOperationBOList =  serviceFactory.getRoadOperationService().findOperationForRoadCheckList();
	
	if(roadOperationBOList==null || roadOperationBOList.size()==0){
		throw new NoRecordFoundException();
	}
	
	return roadOperationBOList;
}

public List<ArteryBO> lookupOperationArtery(Integer roadOperationId, String arteryStr)throws RequiredFieldMissingException, NoRecordFoundException{
	
	List<ArteryBO> operationArteryList = new ArrayList<ArteryBO>();
	List<LocationBO> operationlLocationList = new ArrayList<LocationBO>();
	List<Integer> locationIntegerList = new ArrayList<Integer>();
	if(roadOperationId==null || StringUtils.isBlank(arteryStr)){
		throw new RequiredFieldMissingException();
	}
	
	operationlLocationList = serviceFactory.getRoadOperationService().returnOperationLocationList(roadOperationId);
	
	if(operationlLocationList==null || operationlLocationList.size()==0){
		throw new NoRecordFoundException();
	}
	locationIntegerList = returnIntegerLocationList(operationlLocationList);
	operationArteryList =  serviceFactory.getRoadOperationService().findOperationArteryList(locationIntegerList, arteryStr);
	
	if(operationArteryList==null || operationArteryList.size()==0){
		throw new NoRecordFoundException();
	}
	
	return operationArteryList;
}

@RequestMapping("/lookupAllOperationArtery")
public @ResponseBody List<ArteryBO> lookupAllOperationArtery(@RequestParam Integer roadOperationId)throws RequiredFieldMissingException, NoRecordFoundException{
	
	List<ArteryBO> operationArteryList = new ArrayList<ArteryBO>();
	List<LocationBO> operationlLocationList = new ArrayList<LocationBO>();
	List<Integer> locationIntegerList = new ArrayList<Integer>();
	if(roadOperationId==null){
		throw new RequiredFieldMissingException();
	}
	
	operationlLocationList = serviceFactory.getRoadOperationService().returnOperationLocationList(roadOperationId);
	
	if(operationlLocationList==null || operationlLocationList.size()==0){
		throw new NoRecordFoundException();
	}
	locationIntegerList = returnIntegerLocationList(operationlLocationList);
	operationArteryList =  serviceFactory.getRoadOperationService().findOperationArteryList(locationIntegerList,roadOperationId);
	
	if(operationArteryList==null || operationArteryList.size()==0){
		throw new NoRecordFoundException();
	}
	
	return operationArteryList;
}


	public List<String> findOfficeLocCodeList(Integer roadOperationId) throws RequiredFieldMissingException, NoRecordFoundException
	{
		if(roadOperationId == null || roadOperationId < 1)
			throw new RequiredFieldMissingException();
		
		List<String> officeLocCodes = serviceFactory.getRoadOperationService().findOfficeLocCodeList(roadOperationId);
		
		if(officeLocCodes.size() < 1)
			throw new NoRecordFoundException();
		
		return officeLocCodes;
		
	}
	
	@RequestMapping("/findActiveOperationForUser")
	public @ResponseBody RoadOperationBO findActiveOperationForUser(@RequestParam String userName) throws RequiredFieldMissingException, NoRecordFoundException
	{
		RoadOperationBO roadOperationBO = new RoadOperationBO();
		
		if(StringUtils.isBlank(userName))
			throw new RequiredFieldMissingException();
		
		roadOperationBO = serviceFactory.getRoadOperationService().findActiveOperationForUser(userName);
		
		if(roadOperationBO==null)
			throw new NoRecordFoundException();
		
		return roadOperationBO;
		
	}
	
	@RequestMapping("/findAssignedStaffForRoadOperation")
	public @ResponseBody List<TAStaffBO> findAssignedStaffForRoadOperation(@RequestParam Integer roadOperationId) throws RequiredFieldMissingException, NoRecordFoundException{
		 List<TAStaffBO> taStaffBOList = new ArrayList<TAStaffBO>();
		
		if(roadOperationId==null)
			throw new RequiredFieldMissingException();
		
		taStaffBOList = serviceFactory.getRoadOperationService().findAssignedStaffForRoadOperation(roadOperationId);
		
		if(taStaffBOList==null)
			throw new NoRecordFoundException();
		
		return taStaffBOList;
	}
	
	@RequestMapping("/getAllROMSStaff")
	public @ResponseBody List<TAStaffBO> getAllROMSStaff() throws NoRecordFoundException{
		List<TAStaffBO> taStaffBOList = new ArrayList<TAStaffBO>();
		taStaffBOList =serviceFactory.getStaffUserMappingService().getAllROMSStaff();
		
		if(taStaffBOList==null){
			throw new NoRecordFoundException();
		}
		
		return taStaffBOList;
		
	}
	
	@RequestMapping("/findArteryList")
	public @ResponseBody List<ArteryBO> findArteryList() throws NoRecordFoundException{
		List<ArteryBO> arteryBOList = serviceFactory.getRoadOperationService().findArteryList();
		
		if(arteryBOList==null){
			throw new NoRecordFoundException();
		}
		
		return arteryBOList;
		
	}
	
	@RequestMapping("/getStaffByUsername")
	public @ResponseBody TAStaffBO getStaffByUsername(@RequestParam String username) throws RequiredFieldMissingException, NoRecordFoundException{
		TAStaffBO taStaffBO = new TAStaffBO();
		
		if(StringUtils.isBlank(username)){
			throw new RequiredFieldMissingException();
		}
		
		taStaffBO = serviceFactory.getStaffUserMappingService().getStaffByUsername(username.trim());
		
		if(taStaffBO==null){
			throw new NoRecordFoundException();
		}
		
		return taStaffBO;
	}
	
	/**
	 * This method is used to lookup Road Operations on the Android Mobile App
	 * based on the search criteria entered
	 * @param roadOperationCriteriaBO
	 * @return
	 * @throws InvalidFieldException
	 * @throws RequiredFieldMissingException
	 * @throws NoRecordFoundException
	 */
	@RequestMapping("/lookupRoadOperationMobileSearch")
	public @ResponseBody RoadOpSearchResultsMobileBO lookupRoadOperationMobileSearch(@RequestBody RoadOperationCriteriaBO roadOperationCriteriaBO)throws InvalidFieldException, RequiredFieldMissingException, NoRecordFoundException{
		RoadOpSearchResultsMobileBO roadOpSearchResults = new RoadOpSearchResultsMobileBO();
		List<RoadOperationBO> roadOperationBOList = new ArrayList<RoadOperationBO>();

		roadOperationBOList = this.lookupRoadOperationWithDateRange(roadOperationCriteriaBO);
		
		if(roadOperationBOList==null || roadOperationBOList.size()==0){
			throw new NoRecordFoundException();
		}else{
			roadOpSearchResults.setResultCount(roadOperationBOList.size());
			//return the first 20 results if size is greater than 19
			if(roadOperationBOList.size()>20)
				roadOpSearchResults.setRoadOps(roadOperationBOList.subList(0, 20));		
			else
				roadOpSearchResults.setRoadOps(roadOperationBOList);
		}
		return roadOpSearchResults;
	}
	
		
}
