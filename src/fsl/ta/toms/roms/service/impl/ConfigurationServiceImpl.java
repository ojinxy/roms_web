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
import fsl.ta.toms.roms.constants.Constants;
import fsl.ta.toms.roms.dao.DAOFactory;
import fsl.ta.toms.roms.dataobjects.AuditEntry;
import fsl.ta.toms.roms.dataobjects.ConfigurationDO;
import fsl.ta.toms.roms.dataobjects.EventAuditDO;
import fsl.ta.toms.roms.dataobjects.StatusDO;
import fsl.ta.toms.roms.exception.ErrorSavingException;
import fsl.ta.toms.roms.exception.InvalidFieldException;
import fsl.ta.toms.roms.search.criteria.impl.ConfigurationCriteriaBO;
import fsl.ta.toms.roms.service.ConfigurationService;
import fsl.ta.toms.roms.service.ServiceFactory;

/**
 * 
 * @author jreid Created Date: May 28, 2013
 */

public class ConfigurationServiceImpl implements ConfigurationService {

	@Autowired
	DAOFactory daoFactory;

	@Autowired
	ServiceFactory serviceFactory;

	@Override
	@Transactional(readOnly = true)
	public List<ConfigurationBO> getConfigurationReference(
			HashMap<String, String> filter, String status) throws InvalidFieldException {
		List<ConfigurationBO> configurationBOs = new ArrayList<ConfigurationBO>();
		List<ConfigurationDO> configurationDOs = this.daoFactory
				.getConfigurationDAO()
				.getConfigurationReference(filter, status);

		for (ConfigurationDO configurationDO : configurationDOs) {
			ConfigurationBO configurationBO = new ConfigurationBO(
					configurationDO);
			configurationBOs.add(configurationBO);
		}
		return configurationBOs;
	}

	@Override
	@Transactional(readOnly = true)
	public boolean configurationExists(String configurationCode) {
		return this.daoFactory.getConfigurationDAO().configurationExists(
				configurationCode);
	}

	@Override
	@Transactional(readOnly = true)
	public List<ConfigurationBO> lookupConfigurations(
			ConfigurationCriteriaBO configurationCriteriaBO) {

		List<ConfigurationBO> configurationBOs = new ArrayList<ConfigurationBO>();
		List<ConfigurationDO> configurationDOs = this.daoFactory
				.getConfigurationDAO().lookupConfiguration(
						configurationCriteriaBO);

		if (configurationDOs == null || configurationDOs.isEmpty())
			return null;

		for (ConfigurationDO configurationDO : configurationDOs) {
			ConfigurationBO configurationBO = new ConfigurationBO(
					configurationDO);
			configurationBOs.add(configurationBO);
		}
		return configurationBOs;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW,rollbackFor={Exception.class,RuntimeException.class})
	public void updateConfiguration(ConfigurationBO configurationBO)
			throws ErrorSavingException {

		// AuditEntry auditEntry = new AuditEntry();
		Calendar calendar = Calendar.getInstance();
		Date currentDate = calendar.getTime();
		EventAuditDO eventAuditDO = new EventAuditDO();

		try {
			ConfigurationDO configurationDO = daoFactory.getConfigurationDAO()
					.find(ConfigurationDO.class, configurationBO.getCfgCode());

			if (configurationDO == null)
				throw new InvalidFieldException(
						" Configuration does not exist.");

			// this will update the updatable fields
			configurationDO.updateConfigurationDOFields(configurationBO);

			// get the audit entry from saved object
			AuditEntry auditEntry = configurationDO.getAuditEntry();

			auditEntry.setUpdateDTime(currentDate);
			auditEntry.setUpdateUsername(configurationBO.getCurrentUsername());

			// set the status
			StatusDO statusDO = new StatusDO();
			statusDO = daoFactory.getConfigurationDAO().find(StatusDO.class,
					configurationBO.getStatusId());

			if (statusDO != null) {
				configurationDO.setStatus(statusDO);
			} else {
				throw new InvalidFieldException(" Status is invalid.");
			}

			// set audit entry
			configurationDO.setAuditEntry(auditEntry);

			// save audit
			eventAuditDO = createEventAuditRecord(configurationDO,
					Constants.EventCode.UPDATE_REFERENCE_CODE);
			serviceFactory.getEventAuditService()
					.saveEventAuditDO(eventAuditDO);

			// save wrecking company
			daoFactory.getConfigurationDAO().update(configurationDO);

		}

		catch (Exception e) {
			e.printStackTrace();
			throw new ErrorSavingException(e.getMessage());
		}

	}

	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW,rollbackFor={Exception.class,RuntimeException.class})
	public String saveConfiguration(ConfigurationBO configurationBO)
			throws ErrorSavingException {
		ConfigurationDO configurationDO = new ConfigurationDO(configurationBO);

		AuditEntry auditEntry = new AuditEntry();
		Calendar calendar = Calendar.getInstance();
		Date currentDate = calendar.getTime();
		EventAuditDO eventAuditDO = new EventAuditDO();

		try {
			if (StringUtils.isNotBlank(configurationBO.getCfgCode())
					&& configurationExists(configurationBO.getCfgCode())) {
				throw new InvalidFieldException(
						" Configuration with this code already exists");
			}

			auditEntry.setCreateDTime(currentDate);
			auditEntry.setCreateUsername(configurationBO.getCurrentUsername());

			// set the status
			StatusDO statusDO = new StatusDO();
			statusDO = daoFactory.getConfigurationDAO().find(StatusDO.class,
					Constants.Status.ACTIVE);

			if (statusDO != null) {
				configurationDO.setStatus(statusDO);
			} else {
				throw new InvalidFieldException(" Status is invalid.");
			}

			// set audit entry
			configurationDO.setAuditEntry(auditEntry);

			// save wrecking company
			daoFactory.getConfigurationDAO().save(
					configurationDO);

			// save audit
			eventAuditDO = createEventAuditRecord(configurationDO,
					Constants.EventCode.CREATE_REFERENCE_CODE);
			serviceFactory.getEventAuditService()
					.saveEventAuditDO(eventAuditDO);

			return configurationBO.getCfgCode();
		}

		catch (Exception e) {
			e.printStackTrace();
			throw new ErrorSavingException(e.getMessage());
		}
		// return null;
	}

	private EventAuditDO createEventAuditRecord(
			ConfigurationDO configurationDO, Integer eventCode)
			throws Exception {
		// try {
		EventAuditDO eventAuditBO = new EventAuditDO();
		AuditEntry auditEntry = new AuditEntry();

		eventAuditBO.setEventCode(eventCode);

		eventAuditBO
				.setRefType1Code(Constants.EventRefTypeCode.REFERENCE_TABLE_NAME);
		eventAuditBO.setRefValue1(" roms_configuration");

		eventAuditBO.setRefType2Code(Constants.EventRefTypeCode.REFERENCE_CODE);
		eventAuditBO.setRefValue2(configurationDO.getCfgCode() + "");

		eventAuditBO.setComment("Description: " + configurationDO.getDescription());
		
		if (eventCode.equals(Constants.EventCode.UPDATE_REFERENCE_CODE)) {
			auditEntry.setCreateDTime(configurationDO.getAuditEntry()
					.getUpdateDTime());
			auditEntry.setCreateUsername(configurationDO.getAuditEntry()
					.getUpdateUsername());
		} else {
			auditEntry.setCreateDTime(configurationDO.getAuditEntry()
					.getCreateDTime());
			auditEntry.setCreateUsername(configurationDO.getAuditEntry()
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
	@Transactional(readOnly = true)
	public boolean descriptionExists(ConfigurationBO configurationBO) {

		if (StringUtils.isNotBlank(configurationBO.getDescription()))
			return daoFactory.getConfigurationDAO().configurationDescriptionExists(configurationBO);
		
		return false;
	
	}

}
