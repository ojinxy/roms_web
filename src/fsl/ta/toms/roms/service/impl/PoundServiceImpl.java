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

import fsl.ta.toms.roms.bo.PoundBO;
import fsl.ta.toms.roms.constants.Constants;
import fsl.ta.toms.roms.dao.DAOFactory;
import fsl.ta.toms.roms.dataobjects.AuditEntry;
import fsl.ta.toms.roms.dataobjects.EventAuditDO;
import fsl.ta.toms.roms.dataobjects.ParishDO;
import fsl.ta.toms.roms.dataobjects.PoundDO;
import fsl.ta.toms.roms.dataobjects.StatusDO;
import fsl.ta.toms.roms.exception.ErrorSavingException;
import fsl.ta.toms.roms.exception.InvalidFieldException;
import fsl.ta.toms.roms.search.criteria.impl.PoundCriteriaBO;
import fsl.ta.toms.roms.service.PoundService;
import fsl.ta.toms.roms.service.ServiceFactory;

/**
 * 
 * @author jreid Created Date: May 20, 2013
 */

public class PoundServiceImpl implements PoundService {
	@Autowired
	private DAOFactory daoFactory;

	

	@Autowired
	ServiceFactory serviceFactory;

	@Override
	@Transactional(readOnly = true)
	public List<PoundBO> getPoundReference(HashMap<String, String> filter,
			String status) throws InvalidFieldException {

		List<PoundBO> poundsBOs = new ArrayList<PoundBO>();
		List<PoundDO> poundsDOs = this.daoFactory.getPoundDAO()
				.getPoundReference(filter, status);

		for (PoundDO poundDO : poundsDOs) {
			PoundBO poundBO = new PoundBO(poundDO);
			poundsBOs.add(poundBO);
		}
		return poundsBOs;

	}

	@Override
	@Transactional(readOnly = true)
	public boolean poundExists(Integer poundId) {
		return this.daoFactory.getPoundDAO().poundExists(poundId);
	}

	@Override
	@Transactional(readOnly = true)
	public List<PoundBO> lookupPounds(PoundCriteriaBO poundDOCriteriaBO) {
		List<PoundBO> poundsBOs = new ArrayList<PoundBO>();
		List<PoundDO> poundsDOs = this.daoFactory.getPoundDAO().lookupPounds(
				poundDOCriteriaBO);

		if (poundsDOs == null || poundsDOs.isEmpty())
			return null;

		for (PoundDO poundDO : poundsDOs) {
			PoundBO poundBO = new PoundBO(poundDO);
			poundsBOs.add(poundBO);
		}
		return poundsBOs;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW,rollbackFor={Exception.class,RuntimeException.class})
	public void updatePound(PoundBO poundBO) throws ErrorSavingException {

		// AuditEntry auditEntry = new AuditEntry();
		Calendar calendar = Calendar.getInstance();
		Date currentDate = calendar.getTime();
		EventAuditDO eventAuditDO = new EventAuditDO();

		try {
			PoundDO poundDO = daoFactory.getPoundDAO().find(PoundDO.class,
					poundBO.getPoundId());

			if (poundDO == null)
				throw new ErrorSavingException(" Pound does not exist.");

			// this will update the updatable fields
			poundDO.updatePoundDOFields(poundBO);

			// get the audit entry from saved object
			AuditEntry auditEntry = poundDO.getAuditEntry();

			auditEntry.setUpdateDTime(currentDate);
			auditEntry.setUpdateUsername(poundBO.getCurrentUsername());

			ParishDO parishDO = new ParishDO();

			parishDO = daoFactory.getPoundDAO().find(ParishDO.class,
					poundBO.getParishCode());

			if (parishDO != null) {
				poundDO.getAddress().setParish(parishDO);
			} else {
				throw new InvalidFieldException(" Parish is invalid.");
			}

			// set the status
			StatusDO statusDO = new StatusDO();
			statusDO = daoFactory.getPoundDAO().find(StatusDO.class,
					poundBO.getStatusId());

			if (statusDO != null) {
				poundDO.setStatus(statusDO);
			} else {
				throw new InvalidFieldException(" Status is invalid.");
			}

			// set audit entry
			poundDO.setAuditEntry(auditEntry);

			// save audit
			eventAuditDO = createEventAuditRecord(poundDO,
					Constants.EventCode.UPDATE_REFERENCE_CODE);
			serviceFactory.getEventAuditService()
					.saveEventAuditDO(eventAuditDO);

			// save wrecking company
			daoFactory.getPoundDAO().update(poundDO);

		}

		catch (Exception e) {
			e.printStackTrace();
			throw new ErrorSavingException(e.getMessage());
		}

	}

	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW,rollbackFor={Exception.class,RuntimeException.class})
	public Integer savePound(PoundBO poundBO) throws ErrorSavingException {
		PoundDO poundDO = new PoundDO(poundBO);

		AuditEntry auditEntry = new AuditEntry();
		Calendar calendar = Calendar.getInstance();
		Date currentDate = calendar.getTime();
		EventAuditDO eventAuditDO = new EventAuditDO();

		try {

			auditEntry.setCreateDTime(currentDate);
			auditEntry.setCreateUsername(poundBO.getCurrentUsername());

			ParishDO parishDO = new ParishDO();

			parishDO = daoFactory.getPoundDAO().find(ParishDO.class,
					poundBO.getParishCode());

			if (parishDO != null) {
				poundDO.getAddress().setParish(parishDO);
			} else {
				throw new InvalidFieldException(" Parish is invalid.");
			}

			// set the status
			StatusDO statusDO = new StatusDO();
			statusDO = daoFactory.getPoundDAO().find(StatusDO.class,
					Constants.Status.ACTIVE);

			if (statusDO != null) {
				poundDO.setStatus(statusDO);
			} else {
				throw new InvalidFieldException(" Status is invalid.");
			}

			// set audit entry
			poundDO.setAuditEntry(auditEntry);

			// save wrecking company
			Integer id = (Integer) daoFactory.getPoundDAO().save(poundDO);

			// save audit
			eventAuditDO = createEventAuditRecord(poundDO,
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

	private EventAuditDO createEventAuditRecord(PoundDO poundDO,
			Integer eventCode) throws Exception {

		EventAuditDO eventAuditBO = new EventAuditDO();
		AuditEntry auditEntry = new AuditEntry();

		eventAuditBO.setEventCode(eventCode);

		eventAuditBO
				.setRefType1Code(Constants.EventRefTypeCode.REFERENCE_TABLE_NAME);
		eventAuditBO.setRefValue1(" roms_pound");

//		eventAuditBO.setRefType2Code(Constants.EventRefTypeCode.REFERENCE_CODE);
//		eventAuditBO.setRefValue2(poundDO.getPoundId() + "");

		eventAuditBO.setComment("Pound Name: " + poundDO.getPoundName());
		
		if (eventCode.equals(Constants.EventCode.UPDATE_REFERENCE_CODE)) {
			auditEntry.setCreateDTime(poundDO.getAuditEntry().getUpdateDTime());
			auditEntry.setCreateUsername(poundDO.getAuditEntry()
					.getUpdateUsername());
		} else {
			auditEntry.setCreateDTime(poundDO.getAuditEntry().getCreateDTime());
			auditEntry.setCreateUsername(poundDO.getAuditEntry()
					.getCreateUsername());
		}

		
		eventAuditBO.setAuditEntry(auditEntry);

		return eventAuditBO;

	}

	/**
	 * This method checks database to see if this pound name already exists for
	 * this pound it return 'true' if the description is previously used,
	 * ignoring case it returns false if the description has not been used
	 * previously
	 * 
	 * @param poundBO
	 * @return boolean
	 */
	@Override
	@Transactional(readOnly = true)
	public boolean poundNameExists(PoundBO poundBO) {

		if (StringUtils.isNotBlank(poundBO.getPoundName()))
			return daoFactory.getPoundDAO().poundNameExists(poundBO);
		
		return false;
	}

	/**
	 * This method checks database to see if this short description already
	 * exists for this pound it return 'true' if the description is previously
	 * used, ignoring case it returns false if the description has not been used
	 * previously
	 * 
	 * @param poundBO
	 * @return boolean
	 */
	@Override
	@Transactional(readOnly = true)
	public boolean shortDescriptionExists(PoundBO poundBO) {

		if (StringUtils.isNotBlank(poundBO.getShortDesc()))
			return daoFactory.getPoundDAO().poundShortDescriptionExists(poundBO);
		
		return false;
	}

}
