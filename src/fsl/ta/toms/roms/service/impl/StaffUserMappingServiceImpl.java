package fsl.ta.toms.roms.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import fsl.ta.toms.roms.bo.BIMSStaffViewBO;
import fsl.ta.toms.roms.bo.LMISUserViewBO;
import fsl.ta.toms.roms.bo.StaffUserMappingBO;
import fsl.ta.toms.roms.bo.TAStaffBO;
import fsl.ta.toms.roms.bo.TAStaffTypeBO;
import fsl.ta.toms.roms.constants.Constants;
import fsl.ta.toms.roms.dao.DAOFactory;
import fsl.ta.toms.roms.dao.StaffUserMappingDAO;
import fsl.ta.toms.roms.dataobjects.AuditEntry;
import fsl.ta.toms.roms.dataobjects.EventAuditDO;
import fsl.ta.toms.roms.dataobjects.LMIS_UserViewDO;
import fsl.ta.toms.roms.dataobjects.PersonDO;
import fsl.ta.toms.roms.dataobjects.StatusDO;
import fsl.ta.toms.roms.dataobjects.TAStaffDO;
import fsl.ta.toms.roms.search.criteria.impl.StaffUserMappingCriteriaBO;
import fsl.ta.toms.roms.service.ServiceFactory;
import fsl.ta.toms.roms.service.StaffUserMappingService;

@Transactional
public class StaffUserMappingServiceImpl implements StaffUserMappingService{

	private DAOFactory daoFactory;
	private ServiceFactory serviceFactory;
	
	public void setDaoFactory(DAOFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public void setServiceFactory(ServiceFactory serviceFactory) {
		this.serviceFactory = serviceFactory;
	}

	@Override
	public List<BIMSStaffViewBO> lookupAllStaff() {
		return daoFactory.getStaffUserMappingDAO().lookupAllStaff();
	}

	@Override
	public List<BIMSStaffViewBO> lookupActiveStaff() {
		return daoFactory.getStaffUserMappingDAO().lookupActiveStaff();
	}

	@Override
	public List<LMISUserViewBO> lookupAllUsers() {
		return daoFactory.getStaffUserMappingDAO().lookupAllUsers();
	}

	@Override
	public List<LMISUserViewBO> lookupActiveUsers() {
		return daoFactory.getStaffUserMappingDAO().lookupActiveUsers();
	}

	@Override
	public List<LMISUserViewBO> lookupAllUnmappedUsers() {
		return daoFactory.getStaffUserMappingDAO().lookupAllUnmappedUsers();
	}

	@Override
	public List<LMISUserViewBO> lookupActiveUnmappedUsers() {
		return daoFactory.getStaffUserMappingDAO().lookupActiveUnmappedUsers();
	}

	@Override
	public List<StaffUserMappingBO> lookupStaffUserMappings(
			StaffUserMappingCriteriaBO staffUserMappingCriteriaBO) {
		return daoFactory.getStaffUserMappingDAO().lookupStaffUserMappings(staffUserMappingCriteriaBO);
	}

	@Override
	public boolean isUserMapped(String username) {
		return daoFactory.getStaffUserMappingDAO().isUserMapped(username);
	}

	@Override
	public boolean staffExists(String staffId) {
		return daoFactory.getStaffUserMappingDAO().staffExists(staffId);
	}

	@Override
	public boolean isStaffMapped(String staffId) {
		return daoFactory.getStaffUserMappingDAO().isStaffMapped(staffId);
	}

	@Override
	public TAStaffBO getStaffByUsername(String username) {
		return daoFactory.getStaffUserMappingDAO().getStaffByUsername(username);
	}

	@Override
	//@Transactional(rollbackFor=RuntimeException.class)
	@Transactional(propagation=Propagation.REQUIRES_NEW,rollbackFor={Exception.class,RuntimeException.class})
	public boolean saveStaffUserMapping(StaffUserMappingBO staffUserMappingBO,
			BIMSStaffViewBO bimsStaffViewBO) {
		
		PersonDO personDO = new PersonDO();
		TAStaffDO taStaffDO = new TAStaffDO();
		Calendar calendar = Calendar.getInstance();
		Date currentDate = calendar.getTime();
		PersonDO savedPersonDO = new PersonDO();
		StatusDO status = new StatusDO();
		EventAuditDO eventAuditDO = new EventAuditDO();
		AuditEntry auditEntry = new AuditEntry();
		try{
			StaffUserMappingDAO staffUserMappingDAO = daoFactory.getStaffUserMappingDAO();
			
			auditEntry.setCreateUsername(staffUserMappingBO.getCurrentUsername());
			auditEntry.setCreateDTime(currentDate);
			
			
			// Need t check if person exist before creating person 
			savedPersonDO = staffUserMappingDAO.findPersonByTRN(bimsStaffViewBO.getTrnNbr());
			
			if(savedPersonDO==null){
				//Set Person Details
				personDO.setTrnNbr(bimsStaffViewBO.getTrnNbr());
				personDO.setFirstName(bimsStaffViewBO.getFirstName());
				personDO.setMiddleName(bimsStaffViewBO.getMiddleName());
				personDO.setLastName(bimsStaffViewBO.getLastName());
				
				personDO.setAuditEntry(auditEntry);
				
				Integer personId = staffUserMappingDAO.savePersonDO(personDO);
				
				savedPersonDO = staffUserMappingDAO.find(PersonDO.class, personId);
			}
			
			/*
			if(true){
				System.out.println("@@ HERE before RuntimeException");
				throw new IndexOutOfBoundsException();
			}*/
			
			
			//Set TA Staff Details
			taStaffDO.setStaffId(bimsStaffViewBO.getStaffId());
			taStaffDO.setPerson(savedPersonDO);
			taStaffDO.setOfficeLocationCode(bimsStaffViewBO.getLocationCode());
			taStaffDO.setStaffTypeCode(bimsStaffViewBO.getStaffType());
			taStaffDO.setLoginUsername(staffUserMappingBO.getStaffUsername());
			
			status = staffUserMappingDAO.find(StatusDO.class, Constants.Status.ACTIVE);
			taStaffDO.setStatus(status);
			//taStaffDO.getAuditEntry().setCreateDTime(currentDate);
			taStaffDO.setAuditEntry(auditEntry);
			
			staffUserMappingDAO.save(taStaffDO);
			
			eventAuditDO = createEventAuditRecord(staffUserMappingBO, Constants.EventCode.ADD_TA_STAFF_USER_MAPPING, null);
			serviceFactory.getEventAuditService().saveEventAuditDO(eventAuditDO);
			
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
/**
 * This method populates the Event Audit record to be saved 
 * @param staffUserMappingBO
 * @param eventCode
 * @param oldUserName
 * @return
 */
	private EventAuditDO createEventAuditRecord(StaffUserMappingBO staffUserMappingBO, Integer eventCode, String oldUserName){
		
		EventAuditDO eventAuditBO = new EventAuditDO();
		AuditEntry auditEntry = new AuditEntry();   
		auditEntry.setCreateUsername(staffUserMappingBO.getCurrentUsername());
		
		eventAuditBO.setEventCode(eventCode);	  
		
		if (oldUserName != null){
			eventAuditBO.setComment("Previous Username: " + oldUserName);
		}
		else{
			eventAuditBO.setComment("");
		}
		
		eventAuditBO.setRefType1Code(Constants.EventRefTypeCode.STAFF_ID);
		eventAuditBO.setRefValue1(staffUserMappingBO.getStaffId());
		eventAuditBO.setRefType2Code(Constants.EventRefTypeCode.USER_NAME);
		eventAuditBO.setRefValue2(staffUserMappingBO.getStaffUsername());
		
		eventAuditBO.setAuditEntry(auditEntry);
		
		return eventAuditBO;
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW,rollbackFor={Exception.class,RuntimeException.class})
	public boolean updateStaffUserMapping(StaffUserMappingBO staffUserMappingBO) {
		TAStaffDO taStaffDO = new TAStaffDO();
		Calendar calendar = Calendar.getInstance();
		Date currentDate = calendar.getTime();
		EventAuditDO eventAuditDO = new EventAuditDO();
		try{
			StaffUserMappingDAO staffUserMappingDAO = daoFactory.getStaffUserMappingDAO();
			
			//Retrieve TA Staff record
			taStaffDO = staffUserMappingDAO.find(TAStaffDO.class, staffUserMappingBO.getStaffId());
			
			//Create Event Audit record with Previous Username
			eventAuditDO = createEventAuditRecord(staffUserMappingBO, Constants.EventCode.UPDATE_TA_STAFF_USER_MAPPING, taStaffDO.getLoginUsername());
			
			//Set the New Username for the TA Staff
			taStaffDO.setLoginUsername(staffUserMappingBO.getStaffUsername());
			taStaffDO.getAuditEntry().setUpdateUsername(staffUserMappingBO.getCurrentUsername());
			taStaffDO.getAuditEntry().setUpdateDTime(currentDate);
			
			//Update TA Staff and Write Event Audit Records
			staffUserMappingDAO.update(taStaffDO);
			serviceFactory.getEventAuditService().saveEventAuditDO(eventAuditDO);
		
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public List<BIMSStaffViewBO> lookupAllUnmappedStaff() {
		return daoFactory.getStaffUserMappingDAO().lookupAllUnmappedStaff();
	}

	@Override
	public List<BIMSStaffViewBO> lookupActiveUnmappedStaff() {
		return daoFactory.getStaffUserMappingDAO().lookupActiveUnmappedStaff();
	}

	@Override
	public BIMSStaffViewBO findTAStaffByStaffId(String staffId) {
		return daoFactory.getStaffUserMappingDAO().findTAStaffByStaffId(staffId);
	}

	@Override
	public boolean findUserByUsername(String username) {
		LMIS_UserViewDO userView = new LMIS_UserViewDO();
		
		userView = daoFactory.getStaffUserMappingDAO().find(LMIS_UserViewDO.class, username);
		
		if(userView==null){
			return false;
		}
		
		return true;
	}

	@Override
	public boolean taStaffExistWithTRN(String trnNbr) {
		return daoFactory.getStaffUserMappingDAO().taStaffExistWithTRN(trnNbr);
	}

	@Override
	public List<TAStaffBO> getAllROMSStaff() {
		return daoFactory.getStaffUserMappingDAO().getAllROMSStaff();
	}

	@Override
	public List<StaffUserMappingBO> lookupTAStaff(
			StaffUserMappingCriteriaBO staffUserMappingCriteriaBO) {
		return daoFactory.getStaffUserMappingDAO().lookupTAStaff(staffUserMappingCriteriaBO);
	}

	@Override
	public List<TAStaffTypeBO> lookupBIMSStaffType() {
		return daoFactory.getStaffUserMappingDAO().lookupBIMSStaffType();
	}

	@Override
	public List<LMISUserViewBO> lookupUsersByCriteria(String term) {
		return daoFactory.getStaffUserMappingDAO().lookupUsersByCriteria(term);
	}

	@Override
	public List<LMISUserViewBO> lookupAllUnmappedUsersByCriteria(String term) {
		return daoFactory.getStaffUserMappingDAO().lookupAllUnmappedUsersByCriteria(term);
	}

	@Override
	public List<LMISUserViewBO> lookupAllUnmappedUsersByCriteriaExceptSpecificUser(
			String term, String staffId) {
		return daoFactory.getStaffUserMappingDAO().lookupAllUnmappedUsersByCriteriaExceptSpecificUser(term, staffId);
	}

	@Override
	public LMISUserViewBO lookupLMISUser(String username) {
		return daoFactory.getStaffUserMappingDAO().lookupLMISUser(username);
	}
}
