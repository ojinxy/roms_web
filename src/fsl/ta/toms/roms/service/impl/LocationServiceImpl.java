/**
 * Created By: jreid
 * Date: May 27, 2013
 *
 */
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

import fsl.ta.toms.roms.bo.LocationBO;
import fsl.ta.toms.roms.constants.Constants;
import fsl.ta.toms.roms.dao.DAOFactory;
import fsl.ta.toms.roms.dataobjects.AuditEntry;
import fsl.ta.toms.roms.dataobjects.EventAuditDO;
import fsl.ta.toms.roms.dataobjects.LocationDO;
import fsl.ta.toms.roms.dataobjects.ParishDO;
import fsl.ta.toms.roms.dataobjects.StatusDO;
import fsl.ta.toms.roms.exception.ErrorSavingException;
import fsl.ta.toms.roms.exception.InvalidFieldException;
import fsl.ta.toms.roms.search.criteria.impl.LocationCriteriaBO;
import fsl.ta.toms.roms.service.LocationService;
import fsl.ta.toms.roms.service.ServiceFactory;

/**
 * @author jreid Created Date: May 27, 2013
 */

public class LocationServiceImpl implements LocationService {

	@Autowired
	DAOFactory daoFactory;

	@Autowired
	ServiceFactory serviceFactory;

	@Override
	@Transactional(readOnly = true)
	public List<LocationBO> getLocationReference(
			HashMap<String, String> filter, String status)
			throws InvalidFieldException {

		List<LocationBO> locationsBOs = new ArrayList<LocationBO>();
		List<LocationDO> locationsDOs = this.daoFactory.getLocationDAO()
				.getLocationReference(filter, status);

		for (LocationDO locationDO : locationsDOs) {
			LocationBO locationBO = new LocationBO(locationDO);
			locationsBOs.add(locationBO);
		}

		return locationsBOs;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fsl.ta.toms.roms.service.LocationService#locationExists(java.lang.String)
	 */
	@Override
	@Transactional(readOnly = true)
	public boolean locationExists(Integer locationId) {
		return this.daoFactory.getLocationDAO().locationExists(locationId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fsl.ta.toms.roms.service.LocationService#lookupLocationes(fsl.ta.toms
	 * .roms.search.criteria.impl.LocationCriteriaBO)
	 */
	@Override
	@Transactional(readOnly = true)
	public List<LocationBO> lookupLocations(
			LocationCriteriaBO locationCriteriaBO) {

		List<LocationBO> locationsBOs = new ArrayList<LocationBO>();
		List<LocationDO> locationsDOs = this.daoFactory.getLocationDAO()
				.lookupLocations(locationCriteriaBO);

		if (locationsDOs == null || locationsDOs.isEmpty())
			return null;

		for (LocationDO locationDO : locationsDOs) {
			LocationBO locationBO = new LocationBO(locationDO);
			locationsBOs.add(locationBO);
		}

		return locationsBOs;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW,rollbackFor={Exception.class,RuntimeException.class})
	public void updateLocation(LocationBO locationBO)
			throws ErrorSavingException {

		// AuditEntry auditEntry = new AuditEntry();
		Calendar calendar = Calendar.getInstance();
		Date currentDate = calendar.getTime();
		EventAuditDO eventAuditDO = new EventAuditDO();

		try {
			LocationDO locationDO = daoFactory.getLocationDAO().find(
					LocationDO.class, locationBO.getLocationId());

			if (locationDO == null)
				throw new ErrorSavingException(" Location does not exist.");

			// this will update the updatable fields
			locationDO.updateLocationDOFields(locationBO);

			// get the audit entry from saved object
			AuditEntry auditEntry = locationDO.getAuditEntry();

			auditEntry.setUpdateDTime(currentDate);
			auditEntry.setUpdateUsername(locationBO.getCurrentUsername());

			ParishDO parishDO = new ParishDO();

			parishDO = daoFactory.getLocationDAO().find(ParishDO.class,
					locationBO.getParishCode());

			if (parishDO != null) {
				locationDO.setParish(parishDO);
			} else {
				throw new InvalidFieldException(" Parish is invalid.");
			}

			// set the status
			StatusDO statusDO = new StatusDO();
			statusDO = daoFactory.getLocationDAO().find(StatusDO.class,
					locationBO.getStatusId());

			if (statusDO != null) {
				locationDO.setStatus(statusDO);
			} else {
				throw new InvalidFieldException(" Status is invalid.");
			}

			// set audit entry
			locationDO.setAuditEntry(auditEntry);

			// save audit
			eventAuditDO = createEventAuditRecord(locationDO,
					Constants.EventCode.UPDATE_REFERENCE_CODE);
			serviceFactory.getEventAuditService()
					.saveEventAuditDO(eventAuditDO);

			// save wrecking company
			daoFactory.getLocationDAO().update(locationDO);

		}

		catch (Exception e) {
			e.printStackTrace();
			throw new ErrorSavingException(e.getMessage());
		}

	}

	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW,rollbackFor={Exception.class,RuntimeException.class})
	public Integer saveLocation(LocationBO locationBO)
			throws ErrorSavingException {
		LocationDO locationDO = new LocationDO(locationBO);

		AuditEntry auditEntry = new AuditEntry();
		Calendar calendar = Calendar.getInstance();
		Date currentDate = calendar.getTime();
		EventAuditDO eventAuditDO = new EventAuditDO();

		try {

			auditEntry.setCreateDTime(currentDate);
			auditEntry.setCreateUsername(locationBO.getCurrentUsername());

			ParishDO parishDO = new ParishDO();

			parishDO = daoFactory.getLocationDAO().find(ParishDO.class,
					locationBO.getParishCode());

			if (parishDO != null) {
				locationDO.setParish(parishDO);
			} else {
				throw new InvalidFieldException(" Parish is invalid.");
			}

			// set the status
			StatusDO statusDO = new StatusDO();
			statusDO = daoFactory.getLocationDAO().find(StatusDO.class,
					Constants.Status.ACTIVE);

			if (statusDO != null) {
				locationDO.setStatus(statusDO);
			} else {
				throw new InvalidFieldException(" Status is invalid.");
			}

			// set audit entry
			locationDO.setAuditEntry(auditEntry);

			// save wrecking company
			Integer id = (Integer) daoFactory.getLocationDAO().save(locationDO);

			// save audit
			eventAuditDO = createEventAuditRecord(locationDO,
					Constants.EventCode.CREATE_REFERENCE_CODE);
			serviceFactory.getEventAuditService()
					.saveEventAuditDO(eventAuditDO);

			return id;
		}

		catch (Exception e) {
			e.printStackTrace();
			throw new ErrorSavingException(e.getMessage());
		}
		// return null;
	}

	/**
	 * 
	 * @param locationDO
	 * @param eventCode
	 * @return
	 * @throws Exception
	 */
	private EventAuditDO createEventAuditRecord(LocationDO locationDO,
			Integer eventCode) throws Exception {

		EventAuditDO eventAuditBO = new EventAuditDO();
		AuditEntry auditEntry = new AuditEntry();

		eventAuditBO.setEventCode(eventCode);

		eventAuditBO
				.setRefType1Code(Constants.EventRefTypeCode.REFERENCE_TABLE_NAME);
		eventAuditBO.setRefValue1(" roms_location");

//		eventAuditBO.setRefType2Code(Constants.EventRefTypeCode.REFERENCE_CODE);
//		eventAuditBO.setRefValue2(locationDO.getLocationId() + "");

		eventAuditBO.setComment("Location Name: " + locationDO.getShortDesc());
		
		if (eventCode.equals(Constants.EventCode.UPDATE_REFERENCE_CODE)) {
			auditEntry.setCreateDTime(locationDO.getAuditEntry()
					.getUpdateDTime());
			auditEntry.setCreateUsername(locationDO.getAuditEntry()
					.getUpdateUsername());
		} else {
			auditEntry.setCreateDTime(locationDO.getAuditEntry()
					.getCreateDTime());
			auditEntry.setCreateUsername(locationDO.getAuditEntry()
					.getCreateUsername());
		}

		eventAuditBO.setAuditEntry(auditEntry);

		return eventAuditBO;

	}

	/**
	 * This method checks database to see if this description already exists for
	 * this location it return 'true' if the description is previously used,
	 * ignoring case it returns false if the description has not been used
	 * previously
	 * 
	 * @param 
	 * @return boolean
	 */
	@Override
	@Transactional(readOnly = true)
	public boolean descriptionExists(LocationBO locationBO) {

		if (StringUtils.isNotBlank(locationBO.getLocationDescription()))
			return daoFactory.getLocationDAO().locationDescriptionExists(locationBO);
		
		return false;
	
	}

	/**
	 * This method checks database to see if this short description already
	 * exists for this location it return 'true' if the description is
	 * previously used, ignoring case it returns false if the description has
	 * not been used previously
	 * 
	 * @param 
	 * @return boolean
	 */
	@Override
	@Transactional(readOnly = true)
	public boolean shortDescriptionExists(LocationBO locationBO) {

		if (StringUtils.isNotBlank(locationBO.getShortDesc()))
			return daoFactory.getLocationDAO().locationShortDescriptionExists(locationBO);
		
		return false;
	
	}

}
