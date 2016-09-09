package fsl.ta.toms.roms.webservices;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import fsl.ta.toms.roms.bo.BIMSStaffViewBO;
import fsl.ta.toms.roms.bo.LMISUserViewBO;
import fsl.ta.toms.roms.bo.StaffUserMappingBO;
import fsl.ta.toms.roms.bo.TAStaffBO;
import fsl.ta.toms.roms.bo.TAStaffTypeBO;
import fsl.ta.toms.roms.constants.Constants;
import fsl.ta.toms.roms.exception.ErrorSavingException;
import fsl.ta.toms.roms.exception.InvalidFieldException;
import fsl.ta.toms.roms.exception.NoRecordFoundException;
import fsl.ta.toms.roms.exception.RequiredFieldMissingException;
import fsl.ta.toms.roms.exception.UserMappingException;
import fsl.ta.toms.roms.search.criteria.impl.StaffUserMappingCriteriaBO;
import fsl.ta.toms.roms.service.ServiceFactory;
import fsl.ta.toms.roms.service.StaffUserMappingService;

@Controller
@RequestMapping("/roms_rest/staffUserMapping")
public class StaffUserMapping extends SpringBeanAutowiringSupport implements Serializable{ 
	
	@Autowired
	private ServiceFactory serviceFactory;
	
	
	//Intercept all Exceptions thrown as HTTPSTATUS.NOT_ACCEPTABLE -406
	@ExceptionHandler(Exception.class)
    public ResponseEntity<Exception> interceptorInvalidFieldException(HttpServletRequest req, Exception e)  {

          System.err.println(e.getMessage());
          return new ResponseEntity<Exception>(e, HttpStatus.NOT_ACCEPTABLE);
          
    }	
			
		
		
	/**
	 * This method is used to lookup all Staff from BIMS
	 * @return
	 * @throws NoRecordFoundException
	 */
	public List<BIMSStaffViewBO> lookupAllStaff() throws NoRecordFoundException{
		List<BIMSStaffViewBO> staffViewList = new ArrayList<BIMSStaffViewBO>();
				
		staffViewList = serviceFactory.getStaffUserMappingService().lookupAllStaff();
		
		if(staffViewList==null || staffViewList.size()==0){
			throw new NoRecordFoundException();
		}
		
		return staffViewList;
	}
	
	/**
	 * This method is used to lookup all Active Staff from BIMS
	 * @return
	 * @throws NoRecordFoundException
	 */
	public List<BIMSStaffViewBO> lookupActiveStaff() throws NoRecordFoundException{
		List<BIMSStaffViewBO> staffViewList = new ArrayList<BIMSStaffViewBO>();
		
		staffViewList = serviceFactory.getStaffUserMappingService().lookupActiveStaff();
		
		if(staffViewList==null || staffViewList.size()==0){
			throw new NoRecordFoundException();
		}
		
		return staffViewList;
	}
	
	/**
	 * This method is used to lookup all Unmapped Staff
	 * @return
	 * @throws NoRecordFoundException
	 */
	public List<BIMSStaffViewBO> lookupAllUnmappedStaff() throws NoRecordFoundException{
		List<BIMSStaffViewBO> staffViewList = new ArrayList<BIMSStaffViewBO>();
		
		staffViewList = serviceFactory.getStaffUserMappingService().lookupAllUnmappedStaff();
		
		if(staffViewList==null || staffViewList.size()==0){
			throw new NoRecordFoundException();
		}
		
		return staffViewList;
	}
	
	/**
	 * This method is used to lookup all Active Unmapped Staff 
	 * @return
	 * @throws NoRecordFoundException
	 */
	public List<BIMSStaffViewBO> lookupActiveUnmappedStaff() throws NoRecordFoundException{
		List<BIMSStaffViewBO> staffViewList = new ArrayList<BIMSStaffViewBO>();
		
		staffViewList = serviceFactory.getStaffUserMappingService().lookupActiveUnmappedStaff();
		
		if(staffViewList==null || staffViewList.size()==0){
			throw new NoRecordFoundException();
		}
		
		return staffViewList;
	}
	
	/**
	 * This method is used to lookup all Users from LMIS
	 * @return
	 * @throws NoRecordFoundException
	 */
	public List<LMISUserViewBO> lookupAllUsers() throws NoRecordFoundException{
		List<LMISUserViewBO> userViewList = new ArrayList<LMISUserViewBO>();
		
		userViewList = serviceFactory.getStaffUserMappingService().lookupAllUsers();
		
		if(userViewList==null || userViewList.size()==0){
			throw new NoRecordFoundException();
		}
		
		return userViewList;
	}
	
	/**
	 * This method is used to lookup all Active Users from LMIS
	 * @return
	 * @throws NoRecordFoundException
	 */
	public List<LMISUserViewBO> lookupActiveUsers()throws NoRecordFoundException{
		List<LMISUserViewBO> userViewList = new ArrayList<LMISUserViewBO>();
		
		userViewList = serviceFactory.getStaffUserMappingService().lookupActiveUsers();
		
		if(userViewList==null || userViewList.size()==0){
			throw new NoRecordFoundException();
		}
		
		return userViewList;
	}
	
	/**
	 * This method is used to lookup all Unmapped Users
	 * @return
	 * @throws NoRecordFoundException
	 */
	public List<LMISUserViewBO> lookupAllUnmappedUsers() throws NoRecordFoundException{
		List<LMISUserViewBO> userViewList = new ArrayList<LMISUserViewBO>();
		
		userViewList = serviceFactory.getStaffUserMappingService().lookupAllUnmappedUsers();
		
		if(userViewList==null || userViewList.size()==0){
			throw new NoRecordFoundException();
		}
		
		return userViewList;
	}
	
	/**
	 * This method is used to lookup all Active Unmapped Users
	 * @return
	 * @throws NoRecordFoundException
	 */
	public List<LMISUserViewBO> lookupActiveUnmappedUsers() throws NoRecordFoundException{
		List<LMISUserViewBO> userViewList = new ArrayList<LMISUserViewBO>();
		
		userViewList = serviceFactory.getStaffUserMappingService().lookupActiveUnmappedUsers();
		
		if(userViewList==null || userViewList.size()==0){
			throw new NoRecordFoundException();
		}
		
		return userViewList;
	}
	
	/**
	 * This method is used to lookup Staff User Mappings based on the search criteria entered
	 * @param staffUserMappingCriteriaBO
	 * @return
	 * @throws NoRecordFoundException
	 */
	@RequestMapping("/lookupStaffUserMappings")
	public @ResponseBody List<StaffUserMappingBO> lookupStaffUserMappings(@RequestBody StaffUserMappingCriteriaBO staffUserMappingCriteriaBO)
		throws NoRecordFoundException{
		
		List<StaffUserMappingBO> staffUserMappingList = new ArrayList<StaffUserMappingBO>();
		
		staffUserMappingList = serviceFactory.getStaffUserMappingService().lookupStaffUserMappings(staffUserMappingCriteriaBO);
		
		if(staffUserMappingList==null || staffUserMappingList.size()==0){
			throw new NoRecordFoundException();
		}
		
		return staffUserMappingList;
	}
		
	/**
	 * This method checks if a User has been mapped
	 * @param username
	 * @return
	 * @throws RequiredFieldMissingException
	 */
	@RequestMapping("/isUserMapped")
	public @ResponseBody boolean isUserMapped(@RequestParam String username) throws RequiredFieldMissingException{
		
		if(StringUtils.isBlank(username)){
			throw new RequiredFieldMissingException();
		}
		
		return serviceFactory.getStaffUserMappingService().isUserMapped(username.trim());
	}
	
	/**
	 * This method checks is a Staff Id exists in the roms_ta_staff table
	 * @param staffId
	 * @return
	 * @throws RequiredFieldMissingException
	 */
	public boolean staffExists(String staffId) throws RequiredFieldMissingException{
		
		if(StringUtils.isBlank(staffId)){
			throw new RequiredFieldMissingException();
		}
		
		return serviceFactory.getStaffUserMappingService().staffExists(staffId.trim());
	}
	
	/**
	 * This method checks if a Staff id has been mapped
	 * @param staffId
	 * @return
	 * @throws RequiredFieldMissingException
	 */
	public boolean isStaffMapped(String staffId) throws RequiredFieldMissingException{
		
		if(StringUtils.isBlank(staffId)){
			throw new RequiredFieldMissingException();
		}
		
		return serviceFactory.getStaffUserMappingService().isStaffMapped(staffId.trim());
	}
	
	/**
	 * This method retrieves TA Staff details by username entered
	 * @param username
	 * @return
	 * @throws RequiredFieldMissingException
	 * @throws NoRecordFoundException
	 */
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
	 * This method saves a TA Staff User Mapping
	 * @param staffUserMappingBO
	 * @throws RequiredFieldMissingException
	 * @throws InvalidFieldException
	 * @throws ErrorSavingException
	 */
	public void saveStaffUserMapping(StaffUserMappingBO staffUserMappingBO)
		throws RequiredFieldMissingException, InvalidFieldException, ErrorSavingException{
		
		boolean saveStaffUserMapping=true;
		BIMSStaffViewBO bimsStaffViewBO = new BIMSStaffViewBO();
		
		StaffUserMappingService staffUserMappingService = serviceFactory.getStaffUserMappingService();
		
		//Checks if required StaffUserMappingBO fields are NULL
		if(staffUserMappingBO==null || StringUtils.isBlank(staffUserMappingBO.getStaffId()) || StringUtils.isBlank(staffUserMappingBO.getStaffUsername()) || StringUtils.isBlank(staffUserMappingBO.getCurrentUsername())){
			throw new RequiredFieldMissingException();
		}
				
		
		//Checks if the staff is already mapped and if the user is already mapped and if so throws a InvalidFieldException 
		if(isStaffMapped(staffUserMappingBO.getStaffId()) || isUserMapped(staffUserMappingBO.getStaffUsername())){
			
			throw new InvalidFieldException("Staff Id or Username is already mapped");
		}
		
		bimsStaffViewBO = staffUserMappingService.findTAStaffByStaffId(staffUserMappingBO.getStaffId());
		
		if(bimsStaffViewBO == null){
			throw new InvalidFieldException("Staff Id does not exist");
		}

		boolean taStaffWithTRN = false;
		
		taStaffWithTRN =serviceFactory.getItaExaminerService().itaExaminerExistWithTRN(bimsStaffViewBO.getTrnNbr());
		if(taStaffWithTRN==true){
			throw new InvalidFieldException("Another TA Staff Exists with selected TRN");
		}
		
		/*if(!bimsStaffViewBO.getBadgeStatusCode().equalsIgnoreCase(Constants.BadgeStatusCode.PRINTED) && !bimsStaffViewBO.getBadgeStatusCode().equalsIgnoreCase(Constants.BadgeStatusCode.DELIVERED)){
			throw new InvalidFieldException("Inactive Staff Id");
		}*/
		
		staffUserMappingBO.setCurrentUsername(staffUserMappingBO.getCurrentUsername().trim().toUpperCase());
		staffUserMappingBO.setStaffUsername(staffUserMappingBO.getStaffUsername().trim().toUpperCase());
		
		if(staffUserMappingService.findUserByUsername(staffUserMappingBO.getCurrentUsername())== false || staffUserMappingService.findUserByUsername(staffUserMappingBO.getStaffUsername())== false){
			throw new InvalidFieldException("Current Username or Staff Username does not exist");
		}
		
		saveStaffUserMapping = staffUserMappingService.saveStaffUserMapping(staffUserMappingBO, bimsStaffViewBO);
		
		if(saveStaffUserMapping==false){
			throw new ErrorSavingException();
		}
		
		
	}
	
	/**
	 * This method updates an existing TA Staff User Mapping
	 * @param staffUserMappingBO
	 * @throws RequiredFieldMissingException
	 * @throws InvalidFieldException
	 * @throws ErrorSavingException
	 */
	public void updateStaffUserMapping(StaffUserMappingBO staffUserMappingBO) 
			throws RequiredFieldMissingException, InvalidFieldException, ErrorSavingException{
		
		boolean updateStaffUserMapping = true;
		StaffUserMappingService staffUserMappingService = serviceFactory.getStaffUserMappingService();
				
		//Checks if required StaffUserMappingBO fields are NULL
		if(staffUserMappingBO==null || StringUtils.isBlank(staffUserMappingBO.getStaffId()) || StringUtils.isBlank(staffUserMappingBO.getStaffUsername()) || StringUtils.isBlank(staffUserMappingBO.getCurrentUsername())){
			throw new RequiredFieldMissingException();
		}
		
		//Checks if the staff is not yet mapped and if the user is already mapped and if so throws a InvalidFieldException 
		if(!isStaffMapped(staffUserMappingBO.getStaffId()) || isUserMapped(staffUserMappingBO.getStaffUsername())){
			throw new InvalidFieldException("Staff Id is not yet mapped or Username is already mapped");
		}
		
		staffUserMappingBO.setCurrentUsername(staffUserMappingBO.getCurrentUsername().trim().toUpperCase());
		staffUserMappingBO.setStaffUsername(staffUserMappingBO.getStaffUsername().trim().toUpperCase());
		
		if(staffUserMappingService.findUserByUsername(staffUserMappingBO.getCurrentUsername())== false || staffUserMappingService.findUserByUsername(staffUserMappingBO.getStaffUsername())== false){
			throw new InvalidFieldException("Current Username or Staff Username does not exist");
		}
		
		updateStaffUserMapping = staffUserMappingService.updateStaffUserMapping(staffUserMappingBO);
		
		if(updateStaffUserMapping==false){
			throw new ErrorSavingException();
		}
			
	}
	
	/**
	 * This method checks is a TA Staff exists with the TRN entered
	 * @param trnNbr
	 * @return
	 * @throws RequiredFieldMissingException
	 */
	public boolean taStaffExistWithTRN(String trnNbr) throws RequiredFieldMissingException{
		boolean taStaffExistWithTRN = false;
		
		if(StringUtils.isBlank(trnNbr) ){
			throw new RequiredFieldMissingException();
		}
		
		taStaffExistWithTRN =serviceFactory.getStaffUserMappingService().taStaffExistWithTRN(trnNbr.trim());
		
		return taStaffExistWithTRN;
			
	}
	
	public List<TAStaffBO> getAllROMSStaff() throws NoRecordFoundException{
		List<TAStaffBO> taStaffBOList = new ArrayList<TAStaffBO>();
		taStaffBOList =serviceFactory.getStaffUserMappingService().getAllROMSStaff();
		
		if(taStaffBOList==null){
			throw new NoRecordFoundException();
		}
		
		return taStaffBOList;
		
	}
	
	public List<StaffUserMappingBO> lookupTAStaff(StaffUserMappingCriteriaBO staffUserMappingCriteriaBO)
			throws NoRecordFoundException{
			
			List<StaffUserMappingBO> staffUserMappingList = new ArrayList<StaffUserMappingBO>();
			
			staffUserMappingList = serviceFactory.getStaffUserMappingService().lookupTAStaff(staffUserMappingCriteriaBO);
			
			if(staffUserMappingList==null || staffUserMappingList.size()==0){
				throw new NoRecordFoundException();
			}
			
			return staffUserMappingList;
		}
	
	public List<TAStaffTypeBO> lookupBIMSStaffType()
			throws NoRecordFoundException{
			
			List<TAStaffTypeBO> staffType = new ArrayList<TAStaffTypeBO>();
			
			staffType = serviceFactory.getStaffUserMappingService().lookupBIMSStaffType();
			
			if(staffType==null || staffType.size()==0){
				throw new NoRecordFoundException();
			}
			
			return staffType;
		}
	
	public List<LMISUserViewBO> lookupUsersByCriteria(String term) throws NoRecordFoundException, RequiredFieldMissingException{
		List<LMISUserViewBO> userViewBOList = new ArrayList<LMISUserViewBO>();
		
		if(StringUtils.isBlank(term)){
			throw new RequiredFieldMissingException();
		}
		
		userViewBOList = serviceFactory.getStaffUserMappingService().lookupUsersByCriteria(term);
		
		if(userViewBOList==null || userViewBOList.size()==0){
			throw new NoRecordFoundException();
		}
		
		return userViewBOList;
	}
	public List<LMISUserViewBO> lookupAllUnmappedUsersByCriteria(String term)throws NoRecordFoundException, RequiredFieldMissingException{
List<LMISUserViewBO> userViewBOList = new ArrayList<LMISUserViewBO>();
		
		if(StringUtils.isBlank(term)){
			throw new RequiredFieldMissingException();
		}
		
		userViewBOList = serviceFactory.getStaffUserMappingService().lookupAllUnmappedUsersByCriteria(term);
		
		if(userViewBOList==null || userViewBOList.size()==0){
			throw new NoRecordFoundException();
		}
		
		return userViewBOList;
	}
	public List<LMISUserViewBO> lookupAllUnmappedUsersByCriteriaExceptSpecificUser(String term, String staffId)throws NoRecordFoundException, RequiredFieldMissingException{
		List<LMISUserViewBO> userViewBOList = new ArrayList<LMISUserViewBO>();
		
		if(StringUtils.isBlank(term) || StringUtils.isBlank(staffId)){
			throw new RequiredFieldMissingException();
		}
		
		userViewBOList = serviceFactory.getStaffUserMappingService().lookupAllUnmappedUsersByCriteriaExceptSpecificUser(term, staffId);
		
		if(userViewBOList==null || userViewBOList.size()==0){
			throw new NoRecordFoundException();
		}
		
		return userViewBOList;
	}
	public void saveOrUpdateStaffUserMapping(StaffUserMappingBO staffUserMappingBO)
			throws RequiredFieldMissingException, InvalidFieldException, ErrorSavingException{
		
		if(isStaffMapped(staffUserMappingBO.getStaffId())){
			
			updateStaffUserMapping(staffUserMappingBO);
		}
		else{
			saveStaffUserMapping(staffUserMappingBO);
		}
		
	}
	
	
	public LMISUserViewBO lookupLMISUser(String username) throws RequiredFieldMissingException, NoRecordFoundException{
		LMISUserViewBO userViewList = new LMISUserViewBO();
			
		if(StringUtils.isBlank(username) ){
			throw new RequiredFieldMissingException();
		}
		userViewList = serviceFactory.getStaffUserMappingService().lookupLMISUser(username);
		
		if(userViewList==null){
			throw new NoRecordFoundException();
		}
		
		return userViewList;
	}
	

	@RequestMapping("/isValidUsernameTAStaffRegionMapping")
	public @ResponseBody boolean isValidUsernameTAStaffRegionMapping(@RequestParam String staffId, @RequestParam String username) throws RequiredFieldMissingException, InvalidFieldException, UserMappingException{
		LMISUserViewBO userViewList = new LMISUserViewBO();
		BIMSStaffViewBO bimsStaffViewBO = new BIMSStaffViewBO();
		if(StringUtils.isBlank(username) || StringUtils.isBlank(staffId)){
			throw new RequiredFieldMissingException();
		}
		userViewList = serviceFactory.getStaffUserMappingService().lookupLMISUser(username);
		bimsStaffViewBO = serviceFactory.getStaffUserMappingService().findTAStaffByStaffId(staffId);
				
		if(userViewList==null){
			throw new InvalidFieldException();
		}
		
		if(bimsStaffViewBO != null){
			if(userViewList.getLocationCode().equalsIgnoreCase(bimsStaffViewBO.getLocationCode())){
				return true;
			}
		}else{
			throw new UserMappingException("Login User Name is not mapped to a TA Staff");			
		}
		return false;
	}
}
