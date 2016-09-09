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

import fsl.ta.toms.roms.bo.ArteryBO;
import fsl.ta.toms.roms.constants.Constants;
import fsl.ta.toms.roms.dao.DAOFactory;
import fsl.ta.toms.roms.dataobjects.ArteryDO;
import fsl.ta.toms.roms.dataobjects.AuditEntry;
import fsl.ta.toms.roms.dataobjects.EventAuditDO;
import fsl.ta.toms.roms.dataobjects.LocationDO;
import fsl.ta.toms.roms.dataobjects.ParishDO;
import fsl.ta.toms.roms.dataobjects.StatusDO;
import fsl.ta.toms.roms.exception.ErrorSavingException;
import fsl.ta.toms.roms.exception.InvalidFieldException;
import fsl.ta.toms.roms.search.criteria.impl.ArteryCriteriaBO;
import fsl.ta.toms.roms.service.ArteryService;
import fsl.ta.toms.roms.service.ServiceFactory;

/**
 * 
 * @author jreid Created Date: May 29, 2013
 */

public class ArteryServiceImpl implements ArteryService {

	@Autowired
	DAOFactory daoFactory;

	@Autowired
	ServiceFactory serviceFactory;

	@Override
	@Transactional(readOnly = true)
	public List<ArteryBO> getArteryReference(HashMap<String, String> filter,
			String status) throws InvalidFieldException {
		List<ArteryBO> arteryBOs = new ArrayList<ArteryBO>();
		
		List<ArteryDO> arteryDOs = this.daoFactory.getArteryDAO()
				.getArteryReference(filter, status);

		for (ArteryDO arteryDO : arteryDOs) {
			ArteryBO arteryBO = new ArteryBO(arteryDO);
			arteryBOs.add(arteryBO);
		}
		return arteryBOs;
	}

	@Override
	@Transactional(readOnly = true)
	public boolean arteryExists(Integer arteryId) {
		return this.daoFactory.getArteryDAO().arteryExists(arteryId);
	}

	@Override
	@Transactional(readOnly = true)
	public List<ArteryBO> lookupArteries(ArteryCriteriaBO arteryCriteriaBO) {
		
		//System.out.println("From ArteryService Impl: is CriteriaBO null " + arteryCriteriaBO.getShortDescription() );
		List<ArteryBO> arteryBOs = new ArrayList<ArteryBO>();
		List<ArteryDO> arteryDOs = this.daoFactory.getArteryDAO()
				.lookupArteryDO(arteryCriteriaBO);
		//LocationDO locationDO =this.daoFactory.getLocationDAO().find(LocationDO.class, arteryCriteriaBO.getLocationId());
		//ParishDO parishDO = this.daoFactory.getParishDAO().find(ParishDO.class, arteryCriteriaBO.getParishCode());
		
		if (arteryDOs == null || arteryDOs.isEmpty())
			return null;

		for (ArteryDO arteryDO : arteryDOs) {
			//arteryDO.setLocation(locationDO);
			//arteryDO.getLocation().setParish(parishDO);
			ArteryBO arteryBO = new ArteryBO(arteryDO);
			arteryBOs.add(arteryBO);
		}
		return arteryBOs;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW,rollbackFor={Exception.class,RuntimeException.class})
	public void updateArtery(ArteryBO arteryBO) throws ErrorSavingException {

		Calendar calendar = Calendar.getInstance();
		Date currentDate = calendar.getTime();
		EventAuditDO eventAuditDO = new EventAuditDO();

		try {
			ArteryDO arteryDO = daoFactory.getArteryDAO().find(ArteryDO.class,
					arteryBO.getArteryId());
			//System.err.println("Artery Description "+arteryDO.getDescription() + "short "+arteryDO.getShortDescription() + "id " + arteryDO.getArteryId() + "arterybpo id " + arteryBO.getArteryId());
			if (arteryDO == null)
				throw new InvalidFieldException(" Artery does not exist.");

			// this will update the updatable fields
			arteryDO.updateArteryDOFields(arteryBO);

			// get the audit entry from saved object
			AuditEntry auditEntry = arteryDO.getAuditEntry();

			auditEntry.setUpdateDTime(currentDate);
			auditEntry.setUpdateUsername(arteryBO.getCurrentUsername());

			LocationDO locationDO = new LocationDO();
			/*ParishDO parishDO = new ParishDO();*/
			locationDO = daoFactory.getArteryDAO().find(LocationDO.class,
					arteryBO.getLocationId());
			/*parishDO = daoFactory.getArteryDAO().find(ParishDO.class, arteryBO.getParishCode());*/
			if (locationDO != null) {
				arteryDO.setLocation(locationDO);
			} else {
				throw new InvalidFieldException(" Location is invalid.");
			}
			
			/*if(parishDO != null)
			{
				arteryDO.getLocation().setParish(parishDO);
			}
			else
			 {
				throw new InvalidFieldException(" Parish is invalid.");
			}*/

			StatusDO statusDO = new StatusDO();
			statusDO = daoFactory.getArteryDAO().find(StatusDO.class,
					arteryBO.getStatusId());

			if (statusDO != null) {
				arteryDO.setStatus(statusDO);
			} else {
				throw new InvalidFieldException(" Status is invalid.");
			}
			
			// set audit entry
			arteryDO.setAuditEntry(auditEntry);

			// save audit
			eventAuditDO = createEventAuditRecord(arteryDO,
					Constants.EventCode.UPDATE_REFERENCE_CODE);
			serviceFactory.getEventAuditService()
					.saveEventAuditDO(eventAuditDO);

			// save wrecking company
			daoFactory.getArteryDAO().update(arteryDO);

		}

		catch (Exception e) {
			e.printStackTrace();
			throw new ErrorSavingException(e.getMessage());
		}

	}

	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW,rollbackFor={Exception.class,RuntimeException.class})
	public Integer saveArtery(ArteryBO arteryBO) throws ErrorSavingException {
		ArteryDO arteryDO = new ArteryDO(arteryBO);

		AuditEntry auditEntry = new AuditEntry();
		Calendar calendar = Calendar.getInstance();
		Date currentDate = calendar.getTime();
		EventAuditDO eventAuditDO = new EventAuditDO();

		try {

			auditEntry.setCreateDTime(currentDate);
			auditEntry.setCreateUsername(arteryBO.getCurrentUsername());

			LocationDO locationDO = new LocationDO();
			/*ParishDO parishDO = new ParishDO();
			parishDO = daoFactory.getArteryDAO().find(ParishDO.class, arteryBO.getParishCode());*/
			locationDO = daoFactory.getArteryDAO().find(LocationDO.class,
					arteryBO.getLocationId());

			if (locationDO != null) {
				arteryDO.setLocation(locationDO);
			} else {
				throw new InvalidFieldException(" Location is invalid.");
			}
			
			/*if(parishDO != null)
			{
				arteryDO.getLocation().setParish(parishDO);
			}
			else
			 {
				throw new InvalidFieldException(" Parish is invalid.");
			}*/

			
			StatusDO statusDO = new StatusDO();
			statusDO = daoFactory.getArteryDAO().find(StatusDO.class,
					Constants.Status.ACTIVE);

			if (statusDO != null) {
				arteryDO.setStatus(statusDO);
			} else {
				throw new InvalidFieldException(" Status is invalid.");
			}
			
			arteryDO.setPoints(arteryBO.getPoints());
			arteryDO.setStartlatitude(arteryBO.getStartlatitude());
			arteryDO.setStartlongitude(arteryBO.getStartlongitude());
			arteryDO.setEndlatitude(arteryBO.getEndlatitude());
			arteryDO.setEndlongitude(arteryBO.getEndlongitude());
			arteryDO.setDistance(arteryBO.getDistance());

			// set audit entry
			arteryDO.setAuditEntry(auditEntry);

			// save wrecking company
			Integer id = (Integer) daoFactory.getArteryDAO().save(arteryDO);

			// save audit
			eventAuditDO = createEventAuditRecord(arteryDO,
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
	 * @param arteryDO
	 * @param eventCode
	 * @return
	 * @throws Exception
	 */
	private EventAuditDO createEventAuditRecord(ArteryDO arteryDO,
			Integer eventCode) throws Exception {

		EventAuditDO eventAuditBO = new EventAuditDO();
		AuditEntry auditEntry = new AuditEntry();

		eventAuditBO.setEventCode(eventCode);

		eventAuditBO
				.setRefType1Code(Constants.EventRefTypeCode.REFERENCE_TABLE_NAME);
		eventAuditBO.setRefValue1(" roms_artery");

		//Removed as code is not meaningful to user
//		eventAuditBO.setRefType2Code(Constants.EventRefTypeCode.REFERENCE_CODE);
//		eventAuditBO.setRefValue2(arteryDO.getArteryId() + "");
		
		eventAuditBO.setComment("Artery Name: " + arteryDO.getShortDescription());

		if (eventCode.equals(Constants.EventCode.UPDATE_REFERENCE_CODE)) {
			auditEntry
					.setCreateDTime(arteryDO.getAuditEntry().getUpdateDTime());
			auditEntry.setCreateUsername(arteryDO.getAuditEntry()
					.getUpdateUsername());
		} else {
			auditEntry
					.setCreateDTime(arteryDO.getAuditEntry().getCreateDTime());
			auditEntry.setCreateUsername(arteryDO.getAuditEntry()
					.getCreateUsername());
		}

		eventAuditBO.setAuditEntry(auditEntry);

		return eventAuditBO;

	}

	/**
	 * This method checks database to see if this description already exists for
	 * this artery it return 'true' if the description is previously used,
	 * ignoring case it returns false if the description has not been used
	 * previously
	 * 
	 * @param arteryBO
	 * @return boolean
	 */
	@Override
	@Transactional(readOnly = true)
	public boolean descriptionExists(ArteryBO arteryBO) {

		if (StringUtils.isNotBlank(arteryBO.getArteryDescription()))
			return daoFactory.getArteryDAO().arteryDescriptionExists(arteryBO);
		
		return false;
	
	}

	/**
	 * This method checks database to see if this short description already
	 * exists for this artery it return 'true' if the description is previously
	 * used, ignoring case it returns false if the description has not been used
	 * previously
	 * 
	 * @param arteryBO
	 * @return boolean
	 */
	@Override
	@Transactional(readOnly = true)
	public boolean shortDescriptionExists(ArteryBO arteryBO) {
		if (StringUtils.isNotBlank(arteryBO.getShortDescription()))
			return daoFactory.getArteryDAO().arteryShortDescriptionExists(arteryBO);
		
		return false;
	
	}

	@Override
	public boolean locationExists(ArteryBO arteryBO) {
		if (StringUtils.isNotBlank(arteryBO.getShortDescription()))
			return daoFactory.getArteryDAO().arteryLocationExists(arteryBO);
		
		return false;
	}

	@Override
	public String usernameToFullName(String username) {
		
		return daoFactory.getArteryDAO().usernameToFullName(username);
	}

}
