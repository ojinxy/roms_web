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

import fsl.ta.toms.roms.bo.ConfigurationBO;
import fsl.ta.toms.roms.bo.HolidayExceptionBO;
import fsl.ta.toms.roms.constants.Constants;
import fsl.ta.toms.roms.dao.DAOFactory;
import fsl.ta.toms.roms.dataobjects.AuditEntry;
import fsl.ta.toms.roms.dataobjects.ConfigurationDO;
import fsl.ta.toms.roms.dataobjects.EventAuditDO;
import fsl.ta.toms.roms.dataobjects.HolidayExceptionsDO;
import fsl.ta.toms.roms.dataobjects.StatusDO;
import fsl.ta.toms.roms.exception.ErrorSavingException;
import fsl.ta.toms.roms.exception.InvalidFieldException;
import fsl.ta.toms.roms.search.criteria.impl.ConfigurationCriteriaBO;
import fsl.ta.toms.roms.search.criteria.impl.HolidayExceptionsCriteriaBO;
import fsl.ta.toms.roms.service.ConfigurationService;
import fsl.ta.toms.roms.service.HolidayExceptionsService;
import fsl.ta.toms.roms.service.ServiceFactory;

/**
 * 
 * @author rbrooks
 * Created Date: Jul 16, 2013
 */

public class HolidayExceptionsServiceImpl implements HolidayExceptionsService {

	@Autowired
	DAOFactory daoFactory;

	@Autowired
	ServiceFactory serviceFactory;

	@Override
	@Transactional(readOnly = true)
	public List<HolidayExceptionBO> getHolidayExceptionsReference(
			HashMap<String, String> filter, String status) throws InvalidFieldException {
		List<HolidayExceptionBO> holidayExceptionsBOs = new ArrayList<HolidayExceptionBO>();
		List<HolidayExceptionsDO> holidayExceptionsDOs = this.daoFactory
				.getHolidayExceptionsDAO()
				.getHolidayExceptionReference(filter, status);

		for (HolidayExceptionsDO holidayExceptionsDO : holidayExceptionsDOs) {
			HolidayExceptionBO holidayExceptionsBO = new HolidayExceptionBO(
					holidayExceptionsDO);
			holidayExceptionsBOs.add(holidayExceptionsBO);
		}
		return holidayExceptionsBOs;
	}

	@Override
	@Transactional(readOnly = true)
	public boolean holidayExceptionExists(Date holidayDate) {
		return this.daoFactory.getHolidayExceptionsDAO().holidayExceptionExists(holidayDate);
				
	}

	@Override
	@Transactional(readOnly = true)
	public List<HolidayExceptionBO> lookupHolidayExceptions(
			HolidayExceptionsCriteriaBO holidayExceptionsCriteriaBO) {

		List<HolidayExceptionBO> holidayExceptionsBOs = new ArrayList<HolidayExceptionBO>();
		List<HolidayExceptionsDO> holidayExceptionsDOs = this.daoFactory
				.getHolidayExceptionsDAO().lookupHolidayExceptions(holidayExceptionsCriteriaBO);

		if (holidayExceptionsDOs == null || holidayExceptionsDOs.isEmpty())
			return null;

		for (HolidayExceptionsDO holidayExceptionsDO : holidayExceptionsDOs) {
			HolidayExceptionBO holidayExceptionsBO = new HolidayExceptionBO(holidayExceptionsDO);
			holidayExceptionsBOs.add(holidayExceptionsBO);
		}
		return holidayExceptionsBOs;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW,rollbackFor={Exception.class,RuntimeException.class})
	public void updateHolidayExceptions(HolidayExceptionBO holidayExceptionsBO)
			throws ErrorSavingException {

		// AuditEntry auditEntry = new AuditEntry();
		Calendar calendar = Calendar.getInstance();
		Date currentDate = calendar.getTime();
		EventAuditDO eventAuditDO = new EventAuditDO();

		try {
			HolidayExceptionsDO holidayExceptionsDO = daoFactory.getHolidayExceptionsDAO()
					.find(HolidayExceptionsDO.class, holidayExceptionsBO.getHolidayDate());

			if (holidayExceptionsDO == null)
				throw new InvalidFieldException(
						" Holiday Exception does not exist.");

			// this will update the updatable fields
			holidayExceptionsDO.updateHolidayExceptionDOFields(holidayExceptionsBO);

			// get the audit entry from saved object
			AuditEntry auditEntry = holidayExceptionsDO.getAuditEntry();

			auditEntry.setUpdateDTime(currentDate);
			auditEntry.setUpdateUsername(holidayExceptionsBO.getCurrentUsername());

			// set the status
			StatusDO statusDO = new StatusDO();
			statusDO = daoFactory.getConfigurationDAO().find(StatusDO.class,
					holidayExceptionsBO.getStatusId());

			if (statusDO != null) {
				holidayExceptionsDO.setStatus(statusDO);
			} else {
				throw new InvalidFieldException(" Status is invalid.");
			}

			// set audit entry
			holidayExceptionsDO.setAuditEntry(auditEntry);

			// save audit
			eventAuditDO = createEventAuditRecord(holidayExceptionsDO,
					Constants.EventCode.UPDATE_REFERENCE_CODE);
			serviceFactory.getEventAuditService()
					.saveEventAuditDO(eventAuditDO);

			// save wrecking company
			daoFactory.getConfigurationDAO().update(holidayExceptionsDO);

		}

		catch (Exception e) {
			e.printStackTrace();
			throw new ErrorSavingException(e.getMessage());
		}

	}

	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW,rollbackFor={Exception.class,RuntimeException.class})
	public Date saveHolidayException(HolidayExceptionBO holidayExceptionBO)
			throws ErrorSavingException {
		HolidayExceptionsDO holidayExceptionsDO = new HolidayExceptionsDO(holidayExceptionBO);

		AuditEntry auditEntry = new AuditEntry();
		Calendar calendar = Calendar.getInstance();
		Date currentDate = calendar.getTime();
		EventAuditDO eventAuditDO = new EventAuditDO();

		try {
			if ((holidayExceptionBO.getHolidayDate()!= null)
					&& holidayExceptionExists(holidayExceptionBO.getHolidayDate())) {
				throw new InvalidFieldException(
						" Configuration with this code already exists");
			}

			auditEntry.setCreateDTime(currentDate);
			auditEntry.setCreateUsername(holidayExceptionBO.getCurrentUsername());

			// set the status
			StatusDO statusDO = new StatusDO();
			statusDO = daoFactory.getConfigurationDAO().find(StatusDO.class,
					Constants.Status.ACTIVE);

			if (statusDO != null) {
				holidayExceptionsDO.setStatus(statusDO);
			} else {
				throw new InvalidFieldException(" Status is invalid.");
			}

			// set audit entry
			holidayExceptionsDO.setAuditEntry(auditEntry);

			// save wrecking company
			daoFactory.getConfigurationDAO().save(
					holidayExceptionsDO);

			// save audit
			eventAuditDO = createEventAuditRecord(holidayExceptionsDO,
					Constants.EventCode.CREATE_REFERENCE_CODE);
			serviceFactory.getEventAuditService()
					.saveEventAuditDO(eventAuditDO);

			return holidayExceptionBO.getHolidayDate();
		}

		catch (Exception e) {
			e.printStackTrace();
			throw new ErrorSavingException(e.getMessage());
		}
		// return null;
	}

	private EventAuditDO createEventAuditRecord(
			HolidayExceptionsDO holidayExceptionsDO, Integer eventCode)
			throws Exception {
		// try {
		EventAuditDO eventAuditBO = new EventAuditDO();
		AuditEntry auditEntry = new AuditEntry();

		eventAuditBO.setEventCode(eventCode);

		eventAuditBO
				.setRefType1Code(Constants.EventRefTypeCode.REFERENCE_TABLE_NAME);
		eventAuditBO.setRefValue1(" roms_configuration");

		eventAuditBO.setRefType2Code(Constants.EventRefTypeCode.REFERENCE_CODE);
		eventAuditBO.setRefValue2(holidayExceptionsDO.getHolidayDate() + "");
		
		eventAuditBO.setComment(" Holiday: " + holidayExceptionsDO.getDescription() );
		
		if (eventCode.equals(Constants.EventCode.UPDATE_REFERENCE_CODE)) {
			auditEntry.setCreateDTime(holidayExceptionsDO.getAuditEntry()
					.getUpdateDTime());
			auditEntry.setCreateUsername(holidayExceptionsDO.getAuditEntry()
					.getUpdateUsername());
		} else {
			auditEntry.setCreateDTime(holidayExceptionsDO.getAuditEntry()
					.getCreateDTime());
			auditEntry.setCreateUsername(holidayExceptionsDO.getAuditEntry()
					.getCreateUsername());
		}

		eventAuditBO.setComment("");

		eventAuditBO.setAuditEntry(auditEntry);

		return eventAuditBO;

	}

	/**
	 * This method checks database to see if this description already exists for
	 * this configuration it return 'true' if the description is previously
	 * used, ignoring case it returns false if the description has not been used
	 * previously
	 * 
	 * @param 
	 * @return boolean
	 */
	

	@Override
	public boolean descriptionExists(HolidayExceptionBO holidayExceptionsBO) {
		if (StringUtils.isNotBlank(holidayExceptionsBO.getDescription()))
			return daoFactory.getHolidayExceptionsDAO().holidayExcDescriptionExists(holidayExceptionsBO);
		
		return false;
	
	}

}
