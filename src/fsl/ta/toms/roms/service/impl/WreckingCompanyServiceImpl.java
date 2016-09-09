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

import fsl.ta.toms.roms.bo.WreckingCompanyBO;
import fsl.ta.toms.roms.constants.Constants;
import fsl.ta.toms.roms.dao.DAOFactory;
import fsl.ta.toms.roms.dataobjects.AuditEntry;
import fsl.ta.toms.roms.dataobjects.EventAuditDO;
import fsl.ta.toms.roms.dataobjects.ParishDO;
import fsl.ta.toms.roms.dataobjects.StatusDO;
import fsl.ta.toms.roms.dataobjects.WreckingCompanyDO;
import fsl.ta.toms.roms.exception.ErrorSavingException;
import fsl.ta.toms.roms.exception.InvalidFieldException;
import fsl.ta.toms.roms.search.criteria.impl.WreckingCompanyCriteriaBO;
import fsl.ta.toms.roms.service.ServiceFactory;
import fsl.ta.toms.roms.service.WreckingCompanyService;

/**
 * 
 * @author jreid Created Date: May 20, 2013
 */

public class WreckingCompanyServiceImpl implements WreckingCompanyService {

	@Autowired
	DAOFactory daoFactory;

	@Autowired
	ServiceFactory serviceFactory;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fsl.ta.toms.roms.service.WreckingCompanyService#lookupWreckingCompany
	 * (fsl.ta.toms.roms.search.criteria.impl.WreckingCompanyCriteriaBO)
	 */
	
	@Override
	@Transactional(readOnly = true)
	public List<WreckingCompanyBO> lookupWreckingCompany(
			WreckingCompanyCriteriaBO wcCriteriaBO) {

		List<WreckingCompanyBO> companiesBOs = new ArrayList<WreckingCompanyBO>();
		List<WreckingCompanyDO> companiesDOs = this.daoFactory
				.getWreckingCompanyDAO().lookupWreckingCompany(wcCriteriaBO);

		if (companiesDOs == null || companiesDOs.isEmpty())
			return null;

		for (WreckingCompanyDO companyDO : companiesDOs) {
			WreckingCompanyBO companyBO = new WreckingCompanyBO(companyDO);
			companiesBOs.add(companyBO);
		}

		return companiesBOs;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fsl.ta.toms.roms.service.WreckingCompanyService#wreckingCompanyExists
	 * (java.lang.Integer)
	 */
	
	@Override
	@Transactional(readOnly = true)
	public boolean wreckingCompanyExists(Integer wreckingCompanyId) {
		return this.daoFactory.getWreckingCompanyDAO().wreckingCompanyExists(
				wreckingCompanyId);
	}

	@Override
	@Transactional(readOnly = true)
	public List<WreckingCompanyBO> getWreckingCompanyReference(
			HashMap<String, String> filter, String status) throws InvalidFieldException {

		List<WreckingCompanyBO> companiesBOs = new ArrayList<WreckingCompanyBO>();
		List<WreckingCompanyDO> companiesDOs = this.daoFactory
				.getWreckingCompanyDAO().getWreckingCompanyReference(filter,
						status);

		for (WreckingCompanyDO companyDO : companiesDOs) {
			WreckingCompanyBO companyBO = new WreckingCompanyBO(companyDO);
			companiesBOs.add(companyBO);
		}

		return companiesBOs;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW,rollbackFor={Exception.class,RuntimeException.class})
	public void updateWreckingCompany(WreckingCompanyBO wreckingCompanyBO)
			throws ErrorSavingException {

		Calendar calendar = Calendar.getInstance();
		Date currentDate = calendar.getTime();
		EventAuditDO eventAuditDO = new EventAuditDO();

		try {
			WreckingCompanyDO wreckingCompanyDO = this.daoFactory
					.getWreckingCompanyDAO().find(WreckingCompanyDO.class,
							wreckingCompanyBO.getWreckingCompanyId());
//System.err.println(" company id :" +wreckingCompanyBO.getWreckingCompanyId()+ ":" );
			
			if (wreckingCompanyDO == null)
				throw new InvalidFieldException(
						" Wrecker Company does not exist.");

			// this will update the updatable fields
			wreckingCompanyDO.updateWreckingCompanyDOFields(wreckingCompanyBO);

			AuditEntry auditEntry = wreckingCompanyDO.getAuditEntry();
			auditEntry.setUpdateDTime(currentDate);
			auditEntry
					.setUpdateUsername(wreckingCompanyBO.getCurrentUsername());

			ParishDO parishDO = new ParishDO();

			parishDO = daoFactory.getWreckingCompanyDAO().find(ParishDO.class,
					wreckingCompanyBO.getParishCode());

			if (parishDO != null) {
				wreckingCompanyDO.getAddress().setParish(parishDO);
			} else {
				throw new InvalidFieldException(" Parish is invalid.");
			}

			// set the status
			StatusDO statusDO = new StatusDO();
			statusDO = daoFactory.getWreckingCompanyDAO().find(StatusDO.class,wreckingCompanyBO.getStatusId());

			if (statusDO != null) {
				wreckingCompanyDO.setStatus(statusDO);
			} else {
				throw new InvalidFieldException(" Status is invalid.");
			}

			// set audit entry
			wreckingCompanyDO.setAuditEntry(auditEntry);

			// save audit
			eventAuditDO = createEventAuditRecord(wreckingCompanyDO,
					Constants.EventCode.UPDATE_REFERENCE_CODE);
			serviceFactory.getEventAuditService()
					.saveEventAuditDO(eventAuditDO);

			// save wrecking company
			daoFactory.getWreckingCompanyDAO().update(wreckingCompanyDO);

		}

		catch (Exception e) {
			e.printStackTrace();
			throw new ErrorSavingException(e.getMessage());
		}

	}

	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW,rollbackFor={Exception.class,RuntimeException.class})
	public Integer saveWreckingCompany(WreckingCompanyBO wreckingCompanyBO)
			throws ErrorSavingException {
		WreckingCompanyDO wreckingCompanyDO = new WreckingCompanyDO(
				wreckingCompanyBO);

		AuditEntry auditEntry = new AuditEntry();
		Calendar calendar = Calendar.getInstance();
		Date currentDate = calendar.getTime();
		EventAuditDO eventAuditDO = new EventAuditDO();

		try {

			auditEntry.setCreateDTime(currentDate);
			auditEntry
					.setCreateUsername(wreckingCompanyBO.getCurrentUsername());

			ParishDO parishDO = new ParishDO();

			parishDO = daoFactory.getWreckingCompanyDAO().find(ParishDO.class,
					wreckingCompanyBO.getParishCode());

			if (parishDO != null) {
				wreckingCompanyDO.getAddress().setParish(parishDO);
			} else {
				throw new InvalidFieldException(" Parish is invalid.");
			}

			// set the status
			StatusDO statusDO = new StatusDO();
			statusDO = daoFactory.getWreckingCompanyDAO().find(StatusDO.class,
					Constants.Status.ACTIVE);

			if (statusDO != null) {
				wreckingCompanyDO.setStatus(statusDO);
			} else {
				throw new InvalidFieldException(" Status is invalid.");
			}

			// set audit entry
			wreckingCompanyDO.setAuditEntry(auditEntry);

			// save wrecking company
			Integer id = (Integer) this.daoFactory.getWreckingCompanyDAO()
					.save(wreckingCompanyDO);

			// save audit
			eventAuditDO = createEventAuditRecord(wreckingCompanyDO,
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

	private EventAuditDO createEventAuditRecord(
			WreckingCompanyDO wreckingCompanyDO, Integer eventCode) {
		EventAuditDO eventAuditBO = new EventAuditDO();
		AuditEntry auditEntry = new AuditEntry();

		eventAuditBO.setEventCode(eventCode);

		eventAuditBO
				.setRefType1Code(Constants.EventRefTypeCode.REFERENCE_TABLE_NAME);
		eventAuditBO.setRefValue1(" roms_wreckingcompany");

		eventAuditBO.setComment("Wrecking Company Name: " + wreckingCompanyDO.getCompanyName());
		
		if (eventCode.equals(Constants.EventCode.UPDATE_REFERENCE_CODE)) {
			auditEntry.setCreateDTime(wreckingCompanyDO.getAuditEntry()
					.getUpdateDTime());
			auditEntry.setCreateUsername(wreckingCompanyDO.getAuditEntry()
					.getUpdateUsername());
		} else {
			auditEntry.setCreateDTime(wreckingCompanyDO.getAuditEntry()
					.getCreateDTime());
			auditEntry.setCreateUsername(wreckingCompanyDO.getAuditEntry()
					.getCreateUsername());
		}

		
		eventAuditBO.setAuditEntry(auditEntry);

		return eventAuditBO;

	}

	/**
	 * This method checks database to see if this description already exists for
	 * this wreckingCompany it return 'true' if the description is previously
	 * used, ignoring case it returns false if the description has not been used
	 * previously
	 * 
	 * @param 
	 * @return boolean
	 */
	@Override
	public boolean companyNameExists(WreckingCompanyBO wreckingCompanyBO) {
		if (StringUtils.isNotBlank(wreckingCompanyBO.getCompanyName()))
			return daoFactory.getWreckingCompanyDAO().wreckingCompanyNameExists(wreckingCompanyBO);
		
		return false;

	}

	/**
	 * This method checks database to see if this short description already
	 * exists for this wreckingCompany it return 'true' if the description is
	 * previously used, ignoring case it returns false if the description has
	 * not been used previously
	 * 
	 * @param 
	 * @return boolean
	 */
	@Override
	public boolean shortDescriptionExists(WreckingCompanyBO wreckingCompanyBO) {

		if (StringUtils.isNotBlank(wreckingCompanyBO.getShortDesc())) {
			return daoFactory.getWreckingCompanyDAO().wreckingCompanyShortDescriptionExists(wreckingCompanyBO);
			

		}
			return false;
	}

	@Override
    public boolean trnBranchExists(WreckingCompanyBO wreckingCompanyBO) {

          if (StringUtils.isNotBlank(wreckingCompanyBO.getTrnBranch()) && StringUtils.isNotBlank(wreckingCompanyBO.getTrnNbr()))
                return daoFactory.getWreckingCompanyDAO().trnBranchExists(wreckingCompanyBO);
                //return daoFactory.getWreckingCompanyDAO().wreckingCompanyNameExists(wreckingCompanyBO);
          
          return false;
    }

}
