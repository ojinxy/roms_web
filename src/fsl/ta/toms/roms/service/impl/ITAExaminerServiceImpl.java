package fsl.ta.toms.roms.service.impl;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import fsl.ta.toms.roms.bo.ITAExaminerBO;
import fsl.ta.toms.roms.bo.PersonBO;
import fsl.ta.toms.roms.constants.Constants;
import fsl.ta.toms.roms.dao.DAOFactory;
import fsl.ta.toms.roms.dataobjects.AddressDO;
import fsl.ta.toms.roms.dataobjects.AuditEntry;
import fsl.ta.toms.roms.dataobjects.EventAuditDO;
import fsl.ta.toms.roms.dataobjects.ITAExaminerDO;
import fsl.ta.toms.roms.dataobjects.ParishDO;
import fsl.ta.toms.roms.dataobjects.PersonDO;
import fsl.ta.toms.roms.dataobjects.StatusDO;
import fsl.ta.toms.roms.search.criteria.impl.ITAExaminerCriteriaBO;
import fsl.ta.toms.roms.service.ITAExaminerService;
import fsl.ta.toms.roms.service.ServiceFactory;

@Transactional
public class ITAExaminerServiceImpl implements ITAExaminerService{

	private DAOFactory daoFactory;
	private ServiceFactory serviceFactory;
	
	public void setDaoFactory(DAOFactory daoFactory) {
		this.daoFactory = daoFactory;
	}
	
	public void setServiceFactory(ServiceFactory serviceFactory) {
		this.serviceFactory = serviceFactory;
	}

	
	@Override
	public List<ITAExaminerBO> lookupITAExaminer(
			ITAExaminerCriteriaBO itaExaminerCriteriaBO) {
		return daoFactory.getITAExaminerDAO().lookupITAExaminer(itaExaminerCriteriaBO);
	}
	
	public PersonBO savePerson(PersonBO personBO) throws Exception{
		PersonDO retrievedPersonDO = new PersonDO();
		try{
		retrievedPersonDO = daoFactory.getStaffUserMappingDAO().findPersonByTRN(personBO.getTrnNbr());
		if(retrievedPersonDO!=null){
			updatePerson(personBO, retrievedPersonDO);
			personBO.setPersonId(retrievedPersonDO.getPersonId());
		}
		else{
			Serializable personID = addPerson(personBO);
			if(personID!=null){
				personBO.setPersonId(new Integer(personID.toString()));
			}
		}
		
		return personBO;
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new Exception();
		}
	}
	
	private Serializable addPerson(PersonBO personBO)throws Exception{
		Calendar calendar = Calendar.getInstance();
		Date currentDate = calendar.getTime();
		AuditEntry auditEntry = new AuditEntry();
		PersonDO personDO = new PersonDO();
		
		auditEntry.setCreateDTime(currentDate);
		auditEntry.setCreateUsername(personBO.getCurrentUserName());
	
		//System.err.println("Mark Text" + personBO.getMarkText());
		
		personDO.setTrnNbr(personBO.getTrnNbr());
		personDO.setFirstName(personBO.getFirstName());
		personDO.setMiddleName(personBO.getMiddleName());
		personDO.setLastName(personBO.getLastName());
		personDO.setTelephoneHome(personBO.getTelephoneHome());
		personDO.setTelephoneCell(personBO.getTelephoneCell());
		personDO.setTelephoneWork(personBO.getTelephoneWork());
		AddressDO addressDO = new AddressDO();
		addressDO.setMarkText(personBO.getMarkText());
		addressDO.setStreetNo(personBO.getStreetNo());
		
		addressDO.setStreetName(personBO.getStreetName());
		addressDO.setPoLocationName(personBO.getPoLocationName());
		addressDO.setPoBoxNo(personBO.getPoBoxNo());
		personDO.setAddress(addressDO);
		//address.setMarkText(personBO.getMarkText());
		//personDO.getAddress().setMarkText(personBO.getMarkText());
		//System.err.println("Mark Text after just address " + "via persondo "+personDO.getAddress().getMarkText());
		
		
		
		ParishDO parishDO = new ParishDO();
		if(StringUtils.trimToNull(personBO.getParishCode())!=null){
			parishDO = daoFactory.getITAExaminerDAO().find(ParishDO.class, personBO.getParishCode());
		
			if(parishDO!=null){
				personDO.getAddress().setParish(parishDO);
			}
			else{
				throw new Exception();
			}
		}
		
		personDO.setAuditEntry(auditEntry);
		Serializable personID = daoFactory.getStaffUserMappingDAO().savePersonDO(personDO);
		
		return personID;
	}

	private void updatePerson(PersonBO personBO, PersonDO retrievedPerson) throws Exception{
		Calendar calendar = Calendar.getInstance();
		Date currentDate = calendar.getTime();
		
		retrievedPerson.setFirstName(personBO.getFirstName());
		retrievedPerson.setMiddleName(personBO.getMiddleName());
		retrievedPerson.setLastName(personBO.getLastName());
		retrievedPerson.setTelephoneHome(personBO.getTelephoneHome());
		retrievedPerson.setTelephoneCell(personBO.getTelephoneCell());
		retrievedPerson.setTelephoneWork(personBO.getTelephoneWork());
		retrievedPerson.getAddress().setMarkText(personBO.getMarkText());
		retrievedPerson.getAddress().setStreetNo(personBO.getStreetNo());
		retrievedPerson.getAddress().setStreetName(personBO.getStreetName());
		retrievedPerson.getAddress().setPoLocationName(personBO.getPoLocationName());
		retrievedPerson.getAddress().setPoBoxNo(personBO.getPoBoxNo());
		ParishDO parishDO = new ParishDO();
		if(StringUtils.trimToNull(personBO.getParishCode())!=null){
			parishDO = daoFactory.getITAExaminerDAO().find(ParishDO.class, personBO.getParishCode());
		
			if(parishDO!=null){
				retrievedPerson.getAddress().setParish(parishDO);
			}
			else{
				throw new Exception();
			}
			
		}
		retrievedPerson.getAuditEntry().setUpdateDTime(currentDate);
		retrievedPerson.getAuditEntry().setUpdateUsername(personBO.getCurrentUserName());
		
		//daoFactory.getITAExaminerDAO().saveOrUpdate(retrievedPerson);
		daoFactory.getITAExaminerDAO().update(retrievedPerson);
	}
	
	
	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW,rollbackFor={Exception.class,RuntimeException.class})
	public boolean saveITAExaminer(ITAExaminerBO itaExaminerBO) {
		PersonDO savedPersonDO = new PersonDO();
		//PersonDO itaPersonDO = new PersonDO();
		ITAExaminerDO itaExaminerDO = new ITAExaminerDO();
		AuditEntry auditEntry = new AuditEntry();
		Calendar calendar = Calendar.getInstance();
		Date currentDate = calendar.getTime();
		EventAuditDO eventAuditDO = new EventAuditDO();
		
		try{
			
			auditEntry.setCreateDTime(currentDate);
			auditEntry.setCreateUsername(itaExaminerBO.getCurrentUsername());
			
			itaExaminerBO.getPersonBO().trimPersonDetails();
			itaExaminerBO.getPersonBO().setCurrentUserName(itaExaminerBO.getCurrentUsername());
			PersonBO savedPersonBO = savePerson(itaExaminerBO.getPersonBO());
			//PersonBO personBO = new PersonBO();
			//personBO.setPersonId(itaExaminerBO.get)
			savedPersonDO = daoFactory.getITAExaminerDAO().find(PersonDO.class, savedPersonBO.getPersonId());
			
			itaExaminerDO.setPerson(savedPersonDO);
			//itaExaminerDO.setIdNumber(itaExaminerBO.getIdNumber());
			itaExaminerDO.setExaminerId(itaExaminerBO.getExaminerId());
			
			itaExaminerDO.setOfficeLocationCode(itaExaminerBO.getOfficeLocationCode());
			
			StatusDO statusDO = new StatusDO();
			statusDO = daoFactory.getITAExaminerDAO().find(StatusDO.class, Constants.Status.ACTIVE);
			itaExaminerDO.setStatus(statusDO);
			itaExaminerDO.setAuditEntry(auditEntry);
			
			
			daoFactory.getITAExaminerDAO().save(itaExaminerDO);
			eventAuditDO = createEventAuditRecord(itaExaminerBO, Constants.EventCode.CREATE_REFERENCE_CODE);
			
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
	 * @param itaExaminerBO
	 * @param eventCode
	 * @return
	 * @throws Exception
	 */
	private EventAuditDO createEventAuditRecord(ITAExaminerBO itaExaminerBO, Integer eventCode)throws Exception{
		try{
		EventAuditDO eventAuditBO = new EventAuditDO();
		AuditEntry auditEntry = new AuditEntry();   
		auditEntry.setCreateUsername(itaExaminerBO.getCurrentUsername());
		StringBuffer comment = new StringBuffer();
		
		eventAuditBO.setEventCode(eventCode);	  
		
		
		
		eventAuditBO.setRefType1Code(Constants.EventRefTypeCode.REFERENCE_TABLE_NAME);
		eventAuditBO.setRefValue1(" roms_ita_examiner");
			
//		eventAuditBO.setRefType2Code(Constants.EventRefTypeCode.REFERENCE_CODE);	
//		eventAuditBO.setRefValue2(itaExaminerBO.getExaminerId());
	
		comment.append("ITA Examiner ID: " + itaExaminerBO.getExaminerId());
		
		comment.append("; Name: " + itaExaminerBO.getPersonBO().getFullName());
	
		eventAuditBO.setComment(comment.toString());
		
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
	public boolean updateITAExaminer(ITAExaminerBO itaExaminerBO) {

		ITAExaminerDO savedITAExaminerDO = new ITAExaminerDO();
		Calendar calendar = Calendar.getInstance();
		Date currentDate = calendar.getTime();
		EventAuditDO eventAuditDO = new EventAuditDO();
		
		try{
				
			itaExaminerBO.getPersonBO().trimPersonDetails();
			itaExaminerBO.getPersonBO().setCurrentUserName(itaExaminerBO.getCurrentUsername());
			savePerson(itaExaminerBO.getPersonBO());
						
			savedITAExaminerDO = daoFactory.getITAExaminerDAO().find(ITAExaminerDO.class, itaExaminerBO.getIdNumber());
			
			savedITAExaminerDO.setOfficeLocationCode(itaExaminerBO.getOfficeLocationCode());
			
			savedITAExaminerDO.setExaminerId(itaExaminerBO.getExaminerId());
		
			StatusDO statusDO = new StatusDO();
			statusDO = daoFactory.getITAExaminerDAO().find(StatusDO.class, itaExaminerBO.getStatusId());
			if(statusDO!=null){
				if(!statusDO.getType().equalsIgnoreCase(Constants.StatusType.MAINTENANCE)){
					throw new Exception();
				}
				savedITAExaminerDO.setStatus(statusDO);
			}
			else{
				throw new Exception();
			}
			savedITAExaminerDO.getAuditEntry().setUpdateDTime(currentDate);
			savedITAExaminerDO.getAuditEntry().setUpdateUsername(itaExaminerBO.getCurrentUsername());
			
			
			daoFactory.getITAExaminerDAO().update(savedITAExaminerDO);
			eventAuditDO = createEventAuditRecord(itaExaminerBO, Constants.EventCode.UPDATE_REFERENCE_CODE);
			
			serviceFactory.getEventAuditService().saveEventAuditDO(eventAuditDO);
			
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean itaExaminerExistWithTRN(String trnNbr) {
		return daoFactory.getITAExaminerDAO().itaExaminerExistWithTRN(trnNbr);
	}

	@Override
	public ITAExaminerDO findITAExaminerByIdNumber(Integer idNumber) {

		ITAExaminerDO itaExaminerDO = new ITAExaminerDO();

		itaExaminerDO = daoFactory.getITAExaminerDAO().find(ITAExaminerDO.class, idNumber);
		
		if(itaExaminerDO!=null){
			return itaExaminerDO;
		}
		
		return null;
	}
	
	@Override
	public ITAExaminerDO findITAExaminerByExaminerId(String idNumber) {

		ITAExaminerDO itaExaminerDO = new ITAExaminerDO();

		itaExaminerDO = daoFactory.getITAExaminerDAO().findByExaminerId(idNumber);
		
		if(itaExaminerDO!=null){
			return itaExaminerDO;
		}
		
		return null;
	}

}
