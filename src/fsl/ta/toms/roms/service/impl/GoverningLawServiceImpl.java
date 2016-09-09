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

import fsl.ta.toms.roms.bo.GoverningLawBO;
import fsl.ta.toms.roms.constants.Constants;
import fsl.ta.toms.roms.dao.DAOFactory;
import fsl.ta.toms.roms.dataobjects.AuditEntry;
import fsl.ta.toms.roms.dataobjects.EventAuditDO;
import fsl.ta.toms.roms.dataobjects.GoverningLawDO;
import fsl.ta.toms.roms.dataobjects.StatusDO;
import fsl.ta.toms.roms.exception.ErrorSavingException;
import fsl.ta.toms.roms.exception.InvalidFieldException;
import fsl.ta.toms.roms.search.criteria.impl.GoverningLawCriteriaBO;
import fsl.ta.toms.roms.service.GoverningLawService;
import fsl.ta.toms.roms.service.ServiceFactory;

public class GoverningLawServiceImpl implements GoverningLawService {
	@Autowired
	DAOFactory daoFactory;

	@Autowired
	ServiceFactory serviceFactory;

	@Override
	@Transactional(readOnly = true)
	public List<GoverningLawBO> getGoverningLawReference(
			HashMap<String, String> filter, String status) throws InvalidFieldException {
		List<GoverningLawBO> governingLawsBOs = new ArrayList<GoverningLawBO>();
		List<GoverningLawDO> governingLawsDOs = this.daoFactory
				.getGoverningLawDAO().getGoverningLawReference(filter, status);

		for (GoverningLawDO governingLawDO : governingLawsDOs) {
			GoverningLawBO governingLawBO = new GoverningLawBO(governingLawDO);
			governingLawsBOs.add(governingLawBO);
		}

		return governingLawsBOs;
	}

	@Override
	@Transactional(readOnly = true)
	public boolean governingLawExists(Integer governingLawCode) {
		return this.daoFactory.getGoverningLawDAO().governingLawExists(
				governingLawCode);
	}

	@Override
	@Transactional(readOnly = true)
	public List<GoverningLawBO> lookupGoverningLaws(
			GoverningLawCriteriaBO governingLawCriteriaBO) {
		List<GoverningLawBO> governingLawsBOs = new ArrayList<GoverningLawBO>();
		List<GoverningLawDO> governingLawsDOs = this.daoFactory
				.getGoverningLawDAO()
				.lookupGoverningLaw(governingLawCriteriaBO);

		if (governingLawsDOs == null || governingLawsDOs.isEmpty())
			return null;

		for (GoverningLawDO governingLawDO : governingLawsDOs) {
			GoverningLawBO governingLawBO = new GoverningLawBO(governingLawDO);
			governingLawsBOs.add(governingLawBO);
		}

		return governingLawsBOs;

	}

	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW,rollbackFor={Exception.class,RuntimeException.class})
	public void updateGoverningLaw(GoverningLawBO governingLawBO)
			throws ErrorSavingException {

		// AuditEntry auditEntry = new AuditEntry();
		Calendar calendar = Calendar.getInstance();
		Date currentDate = calendar.getTime();
		EventAuditDO eventAuditDO = new EventAuditDO();

		try {
			GoverningLawDO governingLawDO = this.daoFactory.getGoverningLawDAO()
					.find(GoverningLawDO.class, governingLawBO.getLawId());

			if (governingLawDO == null)
				throw new InvalidFieldException(
						" Governing Law does not exist.");

			// this will update the updatable fields
			governingLawDO.updateGoverningLawDOFields(governingLawBO);

			
			// set the status
			StatusDO statusDO = new StatusDO();
			statusDO = this.daoFactory.getGoverningLawDAO().find(StatusDO.class,governingLawBO.getStatusId());

			if (statusDO != null) {
				governingLawDO.setStatus(statusDO);
			} else {
				throw new InvalidFieldException(" Status is invalid.");
			}

		
			// get the audit entry from saved object
			AuditEntry auditEntry = governingLawDO.getAuditEntry();

			auditEntry.setUpdateDTime(currentDate);
			auditEntry.setUpdateUsername(governingLawBO.getCurrentUsername());

			// set audit entry
			governingLawDO.setAuditEntry(auditEntry);

			// save audit
			eventAuditDO = createEventAuditRecord(governingLawDO,
					Constants.EventCode.UPDATE_REFERENCE_CODE);
			serviceFactory.getEventAuditService()
					.saveEventAuditDO(eventAuditDO);

			// save wrecking company
			daoFactory.getGoverningLawDAO().update(governingLawDO);

		}

		catch (Exception e) {
			e.printStackTrace();
			throw new ErrorSavingException(e.getMessage());
		}

	}

	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW,rollbackFor={Exception.class,RuntimeException.class})
	public Integer saveGoverningLaw(GoverningLawBO governingLawBO)
			throws ErrorSavingException {
		GoverningLawDO governingLawDO = new GoverningLawDO(governingLawBO);

		AuditEntry auditEntry = new AuditEntry();
		Calendar calendar = Calendar.getInstance();
		Date currentDate = calendar.getTime();
		EventAuditDO eventAuditDO = new EventAuditDO();

		try {

			auditEntry.setCreateDTime(currentDate);
			auditEntry.setCreateUsername(governingLawBO.getCurrentUsername());

			// set the status
			StatusDO statusDO = new StatusDO();
			statusDO = daoFactory.getGoverningLawDAO().find(StatusDO.class,
					Constants.Status.ACTIVE);

			if (statusDO != null) {
				governingLawDO.setStatus(statusDO);
			} else {
				throw new InvalidFieldException(" Status is invalid.");
			}

			// set audit entry
			governingLawDO.setAuditEntry(auditEntry);

			// save wrecking company
			Integer id = (Integer) daoFactory.getGoverningLawDAO().save(
					governingLawDO);

			// save audit
			eventAuditDO = createEventAuditRecord(governingLawDO,
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

	private EventAuditDO createEventAuditRecord(GoverningLawDO governingLawDO,
			Integer eventCode) throws Exception {

		EventAuditDO eventAuditBO = new EventAuditDO();
		AuditEntry auditEntry = new AuditEntry();

		eventAuditBO.setEventCode(eventCode);

		eventAuditBO
				.setRefType1Code(Constants.EventRefTypeCode.REFERENCE_TABLE_NAME);
		eventAuditBO.setRefValue1(" roms_governing_law");

		eventAuditBO.setRefType2Code(Constants.EventRefTypeCode.REFERENCE_CODE);
		eventAuditBO.setRefValue2(governingLawDO.getLawId() + "");
		
		eventAuditBO.setComment("Governing Law: " + governingLawDO.getShortDesc());
		
		if (eventCode.equals(Constants.EventCode.UPDATE_REFERENCE_CODE)) {
			auditEntry.setCreateDTime(governingLawDO.getAuditEntry()
					.getUpdateDTime());
			auditEntry.setCreateUsername(governingLawDO.getAuditEntry()
					.getUpdateUsername());
		} else {
			auditEntry.setCreateDTime(governingLawDO.getAuditEntry()
					.getCreateDTime());
			auditEntry.setCreateUsername(governingLawDO.getAuditEntry()
					.getCreateUsername());
		}

		eventAuditBO.setComment("");

		eventAuditBO.setAuditEntry(auditEntry);

		return eventAuditBO;

	}

	/**
	 * This method checks database to see if this description already exists for
	 * this governingLaw it return 'true' if the description is previously used,
	 * ignoring case it returns false if the description has not been used
	 * previously
	 * 
	 * @param governingLawBO
	 * @return boolean
	 */
	@Override
	@Transactional(readOnly = true)
	public boolean descriptionExists(GoverningLawBO governingLawBO) {
		if (StringUtils.isNotBlank(governingLawBO.getDescription()))
			return daoFactory.getGoverningLawDAO().governingLawDescriptionExists(governingLawBO);
		
		return false;
	
	}

	/**
	 * This method checks database to see if this short description already
	 * exists for this governingLaw it return 'true' if the description is
	 * previously used, ignoring case it returns false if the description has
	 * not been used previously
	 * 
	 * @param governingLawBO
	 * @return boolean
	 */
	@Override
	@Transactional(readOnly = true)
	public boolean shortDescriptionExists(GoverningLawBO governingLawBO) {

		if (StringUtils.isNotBlank(governingLawBO.getShortDesc()))
			return daoFactory.getGoverningLawDAO().governingLawShortDescriptionExists(governingLawBO);
		
		return false;
	
	}

}
