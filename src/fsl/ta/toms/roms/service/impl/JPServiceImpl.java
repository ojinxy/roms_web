package fsl.ta.toms.roms.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import fsl.ta.toms.roms.bo.JPBO;
import fsl.ta.toms.roms.bo.PersonBO;
import fsl.ta.toms.roms.constants.Constants;
import fsl.ta.toms.roms.dao.DAOFactory;
import fsl.ta.toms.roms.dataobjects.AuditEntry;
import fsl.ta.toms.roms.dataobjects.EventAuditDO;
import fsl.ta.toms.roms.dataobjects.JPDO;
import fsl.ta.toms.roms.dataobjects.ParishDO;
import fsl.ta.toms.roms.dataobjects.PersonDO;
import fsl.ta.toms.roms.dataobjects.StatusDO;
import fsl.ta.toms.roms.search.criteria.impl.JPCriteriaBO;
import fsl.ta.toms.roms.security.Encryptor;
import fsl.ta.toms.roms.service.JPService;
import fsl.ta.toms.roms.service.ServiceFactory;

@Transactional
public class JPServiceImpl implements JPService{

	private DAOFactory daoFactory;
	private ServiceFactory serviceFactory;
	
	public void setDaoFactory(DAOFactory daoFactory) {
		this.daoFactory = daoFactory;
	}
	
	public void setServiceFactory(ServiceFactory serviceFactory) {
		this.serviceFactory = serviceFactory;
	}
	
	@Override
	public List<JPBO> lookupJP(JPCriteriaBO jpCriteriaBO) {
		return daoFactory.getJPDAO().lookupJP(jpCriteriaBO);
	}

	@Override
	public boolean jpExistWithTRN(String trnNbr) {
		return daoFactory.getJPDAO().jpExistWithTRN(trnNbr);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW,rollbackFor={Exception.class,RuntimeException.class})
	public boolean saveJP(JPBO jpBO) {
		PersonDO savedPersonDO = new PersonDO();
		JPDO savedJPDO = new JPDO();
		AuditEntry auditEntry = new AuditEntry();
		Calendar calendar = Calendar.getInstance();
		Date currentDate = calendar.getTime();
		EventAuditDO eventAuditDO = new EventAuditDO();
		
		try{
			
			auditEntry.setCreateDTime(currentDate);
			auditEntry.setCreateUsername(jpBO.getCurrentUsername());
			
			jpBO.getPersonBO().trimPersonDetails();
			jpBO.getPersonBO().setCurrentUserName(jpBO.getCurrentUsername());
			jpBO.getPersonBO().setParishCode(jpBO.getParishCode());
			PersonBO savedPersonBO = serviceFactory.getItaExaminerService().savePerson(jpBO.getPersonBO());
		
			savedPersonDO = daoFactory.getITAExaminerDAO().find(PersonDO.class, savedPersonBO.getPersonId());
			
			savedJPDO.setPerson(savedPersonDO);
			savedJPDO.setRegNumber(jpBO.getRegNumber());
			
			ParishDO assignedParishDO = new ParishDO();
			
			assignedParishDO = daoFactory.getJPDAO().find(ParishDO.class, jpBO.getParishCode());
			
			if(assignedParishDO!=null){
				savedJPDO.setParish(assignedParishDO);
			}else{
				throw new Exception();
			}
			
			
			StatusDO statusDO = new StatusDO();
			statusDO = daoFactory.getITAExaminerDAO().find(StatusDO.class, Constants.Status.ACTIVE);
			savedJPDO.setStatus(statusDO);
			savedJPDO.setAuditEntry(auditEntry);
			
			if(StringUtils.isNotBlank(jpBO.getNewPin())){
				savedJPDO.setPinHash(Encryptor.encrypt(jpBO.getNewPin().trim()));
				eventAuditDO = createEventAuditRecord(jpBO, Constants.EventCode.CREATE_JP_PIN);
				
				serviceFactory.getEventAuditService().saveEventAuditDO(eventAuditDO);
				
			}
			
			daoFactory.getJPDAO().save(savedJPDO);
			eventAuditDO = new EventAuditDO();
			eventAuditDO = createEventAuditRecord(jpBO, Constants.EventCode.CREATE_REFERENCE_CODE);
			
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
	 * @param jpBO
	 * @param eventCode
	 * @return
	 * @throws Exception
	 */
	private EventAuditDO createEventAuditRecord(JPBO jpBO, Integer eventCode)throws Exception{
		try{
		EventAuditDO eventAuditBO = new EventAuditDO();
		AuditEntry auditEntry = new AuditEntry();   
		auditEntry.setCreateUsername(jpBO.getCurrentUsername());
		
		eventAuditBO.setEventCode(eventCode);	  
		
		
		if(eventCode.equals(Constants.EventCode.CREATE_REFERENCE_CODE) || eventCode.equals(Constants.EventCode.UPDATE_REFERENCE_CODE)){
			eventAuditBO.setRefType1Code(Constants.EventRefTypeCode.REFERENCE_TABLE_NAME);
			eventAuditBO.setRefValue1(" roms_jp");
				
			eventAuditBO.setRefType2Code(Constants.EventRefTypeCode.JP_REG_NUMBER);	
			eventAuditBO.setRefValue2(jpBO.getRegNumber());
		}
		else{
			eventAuditBO.setRefType1Code(Constants.EventRefTypeCode.JP_REG_NUMBER);
			eventAuditBO.setRefValue1(jpBO.getRegNumber());
		}
		
		eventAuditBO.setComment("");
	
		
	
		eventAuditBO.setAuditEntry(auditEntry);
		
		return eventAuditBO;
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new Exception();
		}
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW,rollbackFor={Exception.class,RuntimeException.class})
	public boolean updateJP(JPBO jpBO) {
		JPDO savedJPDO = new JPDO();
		Calendar calendar = Calendar.getInstance();
		Date currentDate = calendar.getTime();
		EventAuditDO eventAuditDO = new EventAuditDO();
		
		try{
				
			jpBO.getPersonBO().trimPersonDetails();
			jpBO.getPersonBO().setCurrentUserName(jpBO.getCurrentUsername());
			serviceFactory.getItaExaminerService().savePerson(jpBO.getPersonBO());
						
			savedJPDO = daoFactory.getJPDAO().find(JPDO.class, jpBO.getIdNumber());
			
			ParishDO assignedParishDO = new ParishDO();
			
			assignedParishDO = daoFactory.getJPDAO().find(ParishDO.class, jpBO.getParishCode());
			
			
			savedJPDO.setRegNumber(jpBO.getRegNumber());
			
			
			if(assignedParishDO!=null){
				savedJPDO.setParish(assignedParishDO);
			}else{
				throw new Exception();
			}
			
			StatusDO statusDO = new StatusDO();
			statusDO = daoFactory.getJPDAO().find(StatusDO.class, jpBO.getStatusId());
			if(statusDO!=null){
				if(!statusDO.getType().equalsIgnoreCase(Constants.StatusType.MAINTENANCE)){
					throw new Exception();
				}
				savedJPDO.setStatus(statusDO);
			}
			else{
				throw new Exception();
			}
			
			
			if(StringUtils.isNotBlank(jpBO.getNewPin())){

				eventAuditDO = new EventAuditDO();
				
				if(savedJPDO.getPinHash()==null || StringUtils.isBlank(savedJPDO.getPinHash())){
					eventAuditDO = createEventAuditRecord(jpBO, Constants.EventCode.CREATE_JP_PIN);
				}
				else{
					eventAuditDO = createEventAuditRecord(jpBO, Constants.EventCode.UPDATE_JP_PIN);
				}
				serviceFactory.getEventAuditService().saveEventAuditDO(eventAuditDO);
				
				
				savedJPDO.setPinHash(Encryptor.encrypt(jpBO.getNewPin().trim()));
			}
			
			savedJPDO.getAuditEntry().setUpdateDTime(currentDate);
			savedJPDO.getAuditEntry().setUpdateUsername(jpBO.getCurrentUsername());
			
			
			daoFactory.getJPDAO().update(savedJPDO);
			eventAuditDO = new EventAuditDO();
			eventAuditDO = createEventAuditRecord(jpBO, Constants.EventCode.UPDATE_REFERENCE_CODE);
			
			serviceFactory.getEventAuditService().saveEventAuditDO(eventAuditDO);
			
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public JPDO findJPByRegNumber(String regNumber) {
		JPDO jpDO = new JPDO();

		jpDO = daoFactory.getJPDAO().findByRegNumber(regNumber);
		
		if(jpDO!=null){
			return jpDO;
		}
		
		return null;
	}
	
	@Override
	public JPDO findJPById(Integer id) {
		JPDO jpDO = new JPDO();

		jpDO = daoFactory.getJPDAO().find(JPDO.class, id);
		
		if(jpDO!=null){
			return jpDO;
		}
		
		return null;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW,rollbackFor={Exception.class,RuntimeException.class})
	public boolean resetPin(JPBO jpBO) {
		JPDO savedJPDO = new JPDO();
		Calendar calendar = Calendar.getInstance();
		Date currentDate = calendar.getTime();
		EventAuditDO eventAuditDO = new EventAuditDO();
		try{
			savedJPDO = daoFactory.getJPDAO().find(JPDO.class, jpBO.getIdNumber());
			
			savedJPDO.setPinHash("");
			savedJPDO.getAuditEntry().setUpdateUsername(jpBO.getCurrentUsername());
			savedJPDO.getAuditEntry().setUpdateDTime(currentDate);
			
			daoFactory.getJPDAO().update(savedJPDO);
			
			eventAuditDO = createEventAuditRecord(jpBO, Constants.EventCode.RESET_JP_PIN);
			serviceFactory.getEventAuditService().saveEventAuditDO(eventAuditDO);
			
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
	}
	
	@Override
	public List<JPBO> getJPListByRegion(String region) {
		return daoFactory.getJPDAO().getJPListByRegion(region);
	}

}
