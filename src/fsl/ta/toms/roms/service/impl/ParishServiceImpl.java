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

import fsl.ta.toms.roms.bo.ParishBO;
import fsl.ta.toms.roms.constants.Constants;
import fsl.ta.toms.roms.dao.DAOFactory;
import fsl.ta.toms.roms.dataobjects.AuditEntry;
import fsl.ta.toms.roms.dataobjects.EventAuditDO;
import fsl.ta.toms.roms.dataobjects.LMIS_TAOfficeLocationViewDO;
import fsl.ta.toms.roms.dataobjects.LocationDO;
import fsl.ta.toms.roms.dataobjects.ParishDO;
import fsl.ta.toms.roms.dataobjects.StatusDO;
import fsl.ta.toms.roms.exception.ErrorSavingException;
import fsl.ta.toms.roms.exception.InvalidFieldException;
import fsl.ta.toms.roms.search.criteria.impl.ParishCriteriaBO;
import fsl.ta.toms.roms.service.ParishService;
import fsl.ta.toms.roms.service.ServiceFactory;

/**
 * 
 * @author jreid Created Date: May 21, 2013
 */

public class ParishServiceImpl implements ParishService {

	@Autowired
	DAOFactory daoFactory;

	@Autowired
	ServiceFactory serviceFactory;

	@Override
	@Transactional(readOnly = true)
	public
	List<ParishBO> getParishReference(HashMap<String,String> filter,String status) throws InvalidFieldException{
		List<ParishBO> parishBOs = new ArrayList<ParishBO>();
		List<ParishDO> parishDOs = this.daoFactory.getParishDAO()
				.getParishReference(filter, status);

		for (ParishDO parishDO : parishDOs) {
			ParishBO parishBO = new ParishBO(parishDO);
			parishBOs.add(parishBO);
		}
		return parishBOs;

	}

	@Override
	@Transactional(readOnly = true)
	public boolean parishExists(String parishCode) {
		return this.daoFactory.getParishDAO().parishExist(parishCode);
	}

	@Override
	@Transactional(readOnly = true)
	public List<ParishBO> lookupParishes(ParishCriteriaBO parishCriteriaBO) {
		List<ParishBO> parishBOs = new ArrayList<ParishBO>();
		List<ParishDO> parishDOs = this.daoFactory.getParishDAO().lookupParish(
				parishCriteriaBO);

		if (parishDOs == null || parishDOs.isEmpty())
			return null;

		for (ParishDO parishDO : parishDOs) {
			ParishBO parishBO = new ParishBO(parishDO);
			//add location description
			LMIS_TAOfficeLocationViewDO location = null;
			if(StringUtils.isNotBlank(parishBO.getOfficeLocationCode())) {
				location = this.daoFactory.getParishDAO().find(LMIS_TAOfficeLocationViewDO.class, parishBO.getOfficeLocationCode());
				//System.err.println(" location :" + location);
				if(location != null) {
					parishBO.setOfficeLocationDescription(location.getLocationDesc());
				}
			}
			parishBOs.add(parishBO);
		}
		return parishBOs;

	}

	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW,rollbackFor={Exception.class,RuntimeException.class})
	public void updateParish(ParishBO parishBO) throws ErrorSavingException {

		// AuditEntry auditEntry = new AuditEntry();
		Calendar calendar = Calendar.getInstance();
		Date currentDate = calendar.getTime();
		EventAuditDO eventAuditDO = new EventAuditDO();

		try {
			ParishDO parishDO = daoFactory.getParishDAO().find(ParishDO.class,
					parishBO.getParishCode());

			if (parishDO == null)
				throw new ErrorSavingException(" Parish does not exist.");

			// this will update the updatable fields
			parishDO.updateParishDOFields(parishBO);

			StatusDO statusDO = new StatusDO();
			statusDO = daoFactory.getParishDAO().find(StatusDO.class,
					parishBO.getStatusId());

			if (statusDO != null) {
				parishDO.setStatus(statusDO);
			} else {
				throw new InvalidFieldException(" Status is invalid.");
			}
			// get the audit entry from saved object
			AuditEntry auditEntry = parishDO.getAuditEntry();

			auditEntry.setUpdateDTime(currentDate);
			auditEntry.setUpdateUsername(parishBO.getCurrentUsername());

			// set audit entry
			parishDO.setAuditEntry(auditEntry);

			// save audit
			eventAuditDO = createEventAuditRecord(parishDO,
					Constants.EventCode.UPDATE_REFERENCE_CODE);
			serviceFactory.getEventAuditService()
					.saveEventAuditDO(eventAuditDO);

			// save wrecking company
			daoFactory.getParishDAO().update(parishDO);

		}

		catch (Exception e) {
			e.printStackTrace();
			throw new ErrorSavingException(e.getMessage());
		}

	}

	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW,rollbackFor={Exception.class,RuntimeException.class})
	public String saveParish(ParishBO parishBO) throws ErrorSavingException {
		ParishDO parishDO = new ParishDO(parishBO);

		AuditEntry auditEntry = new AuditEntry();
		Calendar calendar = Calendar.getInstance();
		Date currentDate = calendar.getTime();
		EventAuditDO eventAuditDO = new EventAuditDO();

		try {

			/*if (StringUtils.isNotBlank(parishBO.getParishCode())
					&& parishExists(parishBO.getParishCode()))
				throw new InvalidFieldException(
						" Parish with this code already exists");
*/
			auditEntry.setCreateDTime(currentDate);
			auditEntry.setCreateUsername(parishBO.getCurrentUsername());

			StatusDO statusDO = new StatusDO();
			statusDO = daoFactory.getParishDAO().find(StatusDO.class,
					Constants.Status.ACTIVE);

			if (statusDO != null) {
				parishDO.setStatus(statusDO);
			} else {
				throw new InvalidFieldException(" Status is invalid.");
			}

			// set audit entry
			parishDO.setAuditEntry(auditEntry);

			// save wrecking company
			//Integer id = (Integer) 
					daoFactory.getParishDAO().save(parishDO);

			// save audit
			eventAuditDO = createEventAuditRecord(parishDO,
					Constants.EventCode.CREATE_REFERENCE_CODE);
			serviceFactory.getEventAuditService()
					.saveEventAuditDO(eventAuditDO);

			return parishBO.getParishCode();
		}

		catch (Exception e) {
			e.printStackTrace();
			throw new ErrorSavingException(e.getMessage());
		}
	}

	/**
	 * 
	 * @param parishDO
	 * @param eventCode
	 * @return
	 * @throws Exception
	 */
	private EventAuditDO createEventAuditRecord(ParishDO parishDO,
			Integer eventCode) throws Exception {
		try {
			EventAuditDO eventAuditBO = new EventAuditDO();
			AuditEntry auditEntry = new AuditEntry();

			eventAuditBO.setEventCode(eventCode);

			eventAuditBO
					.setRefType1Code(Constants.EventRefTypeCode.REFERENCE_TABLE_NAME);
			eventAuditBO.setRefValue1(" roms_parish");

//			eventAuditBO
//					.setRefType2Code(Constants.EventRefTypeCode.REFERENCE_CODE);
//			eventAuditBO.setRefValue2(parishDO.getParishCode() + "");

			eventAuditBO.setComment("Parish: " + parishDO.getDescription());
			
			if (eventCode.equals(Constants.EventCode.UPDATE_REFERENCE_CODE)) {
				auditEntry.setCreateDTime(parishDO.getAuditEntry()
						.getUpdateDTime());
				auditEntry.setCreateUsername(parishDO.getAuditEntry()
						.getUpdateUsername());
			} else {
				auditEntry.setCreateDTime(parishDO.getAuditEntry()
						.getCreateDTime());
				auditEntry.setCreateUsername(parishDO.getAuditEntry()
						.getCreateUsername());
			}

			

			eventAuditBO.setAuditEntry(auditEntry);

			return eventAuditBO;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception();
		}
	}

	/**
	 * This method checks database to see if this description already exists for
	 * this parish it return 'true' if the description is previously used,
	 * ignoring case it returns false if the description has not been used
	 * previously
	 * 
	 * @param parishBO
	 * @return boolean
	 */
	@Override
	@Transactional(readOnly = true)
	public boolean descriptionExists(ParishBO parishBO) {

		if (StringUtils.isNotBlank(parishBO.getDescription()))
			return daoFactory.getParishDAO().parishDescriptionExist(parishBO);
		
		return false;
	}

}
