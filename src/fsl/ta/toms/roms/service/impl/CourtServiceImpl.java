package fsl.ta.toms.roms.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import fsl.ta.toms.roms.bo.CourtBO;
import fsl.ta.toms.roms.constants.Constants;
import fsl.ta.toms.roms.dao.DAOFactory;
import fsl.ta.toms.roms.dataobjects.AddressDO;
import fsl.ta.toms.roms.dataobjects.AuditEntry;
import fsl.ta.toms.roms.dataobjects.CourtDO;
import fsl.ta.toms.roms.dataobjects.EventAuditDO;
import fsl.ta.toms.roms.dataobjects.LocationDO;
import fsl.ta.toms.roms.dataobjects.ParishDO;
import fsl.ta.toms.roms.dataobjects.StatusDO;
import fsl.ta.toms.roms.exception.ErrorSavingException;
import fsl.ta.toms.roms.exception.InvalidFieldException;
import fsl.ta.toms.roms.search.criteria.impl.CourtCriteriaBO;
import fsl.ta.toms.roms.service.CourtService;
import fsl.ta.toms.roms.service.ServiceFactory;

/**
 * @author jreid
 * Created Date: May 28, 2013
 */

public class CourtServiceImpl implements CourtService {
	@Autowired
	DAOFactory daoFactory;

	@Autowired
	ServiceFactory serviceFactory;

	@Override
	@Transactional(readOnly=true)
	public List<CourtBO> getCourtReference(HashMap<String, String> filter,
			String status) throws InvalidFieldException {
		List<CourtBO> courtBOs = new ArrayList<CourtBO>();
		List<CourtDO> courtDOs = this.daoFactory.getCourtDAO().getCourtReference(filter, status);

		for(CourtDO courtDO: courtDOs) {
			CourtBO courtBO = new CourtBO(courtDO);
			courtBOs.add(courtBO);
		}
		return courtBOs;
	}

	@Override
	@Transactional(readOnly=true)
	public boolean courtExists(Integer courtCode) {
		return this.daoFactory.getCourtDAO().courtExists(courtCode);
	}

	
	@Override
	@Transactional(readOnly=true)
	public List<CourtBO> lookupCourts(CourtCriteriaBO courtCriteriaBO) {
		List<CourtBO> courtBOs = new ArrayList<CourtBO>();
		List<CourtDO> courtDOs = this.daoFactory.getCourtDAO().lookupCourt(courtCriteriaBO);
		
		if(courtDOs == null || courtDOs.isEmpty())
			return null;

		for(CourtDO courtDO: courtDOs) {
			CourtBO courtBO = new CourtBO(courtDO);
			courtBOs.add(courtBO);
		}
		return courtBOs;
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW,rollbackFor={Exception.class,RuntimeException.class})
	public void updateCourt(CourtBO courtBO) throws ErrorSavingException {
		
		//AuditEntry auditEntry = new AuditEntry();
		Calendar calendar = Calendar.getInstance();
		Date currentDate = calendar.getTime();
		EventAuditDO eventAuditDO = new EventAuditDO();
		AddressDO addressDO = new AddressDO();
		ParishDO parishDO;
		try {
			CourtDO courtDO = daoFactory.getCourtDAO().find(CourtDO.class, courtBO.getCourtId());
			
			if(courtDO == null)
				throw new InvalidFieldException("Court does not exist.");
			
			//this will update the updatable fields
			courtDO.updateCourtDOFields(courtBO);

			//get the audit entry from saved object
			AuditEntry auditEntry = new AuditEntry();
			
			auditEntry.setCreateDTime(currentDate);
			auditEntry.setCreateUsername(courtBO.getCurrentUsername());

			/*LocationDO locationDO = new LocationDO();

			locationDO = daoFactory.getCourtDAO().find(LocationDO.class,
					courtBO.getLocationId());

			if (locationDO != null) {
				courtDO.setLocation(locationDO);
			} else {
				throw new InvalidFieldException(" Location is invalid.");
			}*/
			
			parishDO = daoFactory.getParishDAO().find(ParishDO.class, courtBO.getParishCode());
			
			addressDO.setMarkText(courtBO.getMarkText());
			addressDO.setPoBoxNo(courtBO.getPoBoxNo());
			addressDO.setPoLocationName(courtBO.getPoLocationName());
			addressDO.setStreetName(courtBO.getStreetName());
			addressDO.setStreetNo(courtBO.getStreetNo());
			
			addressDO.setParish(parishDO);
			
			courtDO.setAddress(addressDO);
			
			//System.err.println("Parish DO and BO " + addressDO.getParish().getParishCode() +" "+courtBO.getParishCode());
			//set the status 
			StatusDO statusDO = new StatusDO();
			statusDO = daoFactory.getCourtDAO().find(StatusDO.class, courtBO.getStatusId());
			
			if (statusDO != null) {
				courtDO.setStatus(statusDO);
			} else {
				throw new InvalidFieldException(" Status is invalid.");
			}

			// set audit entry
			//courtDO.setAuditEntry(auditEntry);
			
			courtDO.getAuditEntry().setUpdateDTime(currentDate);
			courtDO.getAuditEntry().setUpdateUsername(courtBO.getCurrentUsername());

			// save audit
/*			eventAuditDO = createEventAuditRecord(courtDO,
					Constants.EventCode.UPDATE_REFERENCE_CODE);
			serviceFactory.getEventAuditService()
					.saveEventAuditDO(eventAuditDO);
*/
			// save wrecking company
			daoFactory.getCourtDAO().update(courtDO);
			
//			eventAuditDO.setEventCode(Constants.EventCode.UPDATE_REFERENCE_CODE);
//			eventAuditDO.setRefType1Code(Constants.EventRefTypeCode.COURT_ID);
//			eventAuditDO.setComment("Saving Court");
			
			eventAuditDO = createEventAuditRecord(courtDO, Constants.EventCode.UPDATE_REFERENCE_CODE);
			eventAuditDO.setAuditEntry(auditEntry);
			serviceFactory.getEventAuditService().saveEventAuditDO(eventAuditDO);
			

		}

		catch (Exception e) {
			e.printStackTrace();
			throw new ErrorSavingException(e.getMessage());
		}
	
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW,rollbackFor={Exception.class,RuntimeException.class})
	public Integer saveCourt(CourtBO courtBO) throws ErrorSavingException {
		CourtDO courtDO = new CourtDO(
				courtBO);

		AuditEntry auditEntry = new AuditEntry();
		Calendar calendar = Calendar.getInstance();
		Date currentDate = calendar.getTime();
		EventAuditDO eventAuditDO = new EventAuditDO();

		AddressDO addressDO = new AddressDO();
		ParishDO parishDO;
		try {

			auditEntry.setCreateDTime(currentDate);
			auditEntry.setCreateUsername(courtBO.getCurrentUsername());

			parishDO = daoFactory.getParishDAO().find(ParishDO.class, courtBO.getParishCode());
			
			addressDO.setMarkText(courtBO.getMarkText());
			addressDO.setPoBoxNo(courtBO.getPoBoxNo());
			addressDO.setPoLocationName(courtBO.getPoLocationName());
			addressDO.setStreetName(courtBO.getStreetName());
			addressDO.setStreetNo(courtBO.getStreetNo());
			addressDO.setParish(parishDO);
			
			courtDO.setAddress(addressDO);
			
			//set the status 
			StatusDO statusDO = new StatusDO();
			statusDO = daoFactory.getCourtDAO().find(StatusDO.class,Constants.Status.ACTIVE);
			
			if (statusDO != null) {
				courtDO.setStatus(statusDO);
			} else {
				throw new InvalidFieldException(" Status is invalid.");
			}

			// set audit entry
			courtDO.setAuditEntry(auditEntry);

			
			// save wrecking company
			Integer id = (Integer) daoFactory.getCourtDAO().save(courtDO);

			
			//event Audit
//			eventAuditDO.setEventCode(Constants.EventCode.CREATE_REFERENCE_CODE);
//			eventAuditDO.setRefType1Code(Constants.EventRefTypeCode.COURT_ID);
//			eventAuditDO.setRefValue1(id.toString());
//			eventAuditDO.setComment("Saving Court");
			
			eventAuditDO = createEventAuditRecord(courtDO, Constants.EventCode.CREATE_REFERENCE_CODE);
			eventAuditDO.setAuditEntry(auditEntry);
			serviceFactory.getEventAuditService().saveEventAuditDO(eventAuditDO);

			
			return id;
		}

		catch (Exception e) {
			e.printStackTrace();
			throw new ErrorSavingException(e.getMessage());
		}
		//return null;
	}

	private EventAuditDO createEventAuditRecord(
			CourtDO courtDO, Integer eventCode)
			throws Exception {
		
			EventAuditDO eventAuditBO = new EventAuditDO();
			AuditEntry auditEntry = new AuditEntry();

			eventAuditBO.setEventCode(eventCode);

			eventAuditBO
					.setRefType1Code(Constants.EventRefTypeCode.REFERENCE_TABLE_NAME);
			eventAuditBO.setRefValue1(" roms_court");

			//Removed as code is not meaningful to user
//			eventAuditBO
//					.setRefType2Code(Constants.EventRefTypeCode.REFERENCE_CODE);
//			eventAuditBO.setRefValue2(courtDO.getCourtId()
//					+ "");
			
			eventAuditBO.setComment("Court Name: " + courtDO.getShortDesc());
			
			if (eventCode.equals(Constants.EventCode.UPDATE_REFERENCE_CODE)) {
				auditEntry.setCreateDTime(courtDO.getAuditEntry().getUpdateDTime());
				auditEntry.setCreateUsername(courtDO.getAuditEntry().getUpdateUsername());								
			}else {
				auditEntry.setCreateDTime(courtDO.getAuditEntry().getCreateDTime());
				auditEntry.setCreateUsername(courtDO.getAuditEntry().getCreateUsername());	
			}

			

			eventAuditBO.setAuditEntry(auditEntry);

			return eventAuditBO;
		
	}
	/**
	 * This method checks database to see if this description already exists for this court
	 * it return 'true' if the description is previously used, ignoring case
	 * it returns false if the description has not been used previously
	 * @param 
	 * @return boolean
	 */
	@Override
	@Transactional(readOnly=true)
	public boolean descriptionExists(CourtBO courtBO) {
		
		if (StringUtils.isNotBlank(courtBO.getDescription()))
			return daoFactory.getCourtDAO().courtDescriptionExists(courtBO);
		
		return false;
	
	}
	
	/**
	 * This method checks database to see if this short description already exists for this court
	 * it return 'true' if the description is previously used, ignoring case
	 * it returns false if the description has not been used previously
	 * @param 
	 * @return boolean
	 */
	@Override
	@Transactional(readOnly=true)
	public boolean shortDescriptionExists(CourtBO courtBO) {
		
		if (StringUtils.isNotBlank(courtBO.getShortDescription()))
			return daoFactory.getCourtDAO().courtShortDescExists(courtBO);
		
		return false;
	
	}

}
