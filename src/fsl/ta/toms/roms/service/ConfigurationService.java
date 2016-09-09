package fsl.ta.toms.roms.service;

import java.util.HashMap;
import java.util.List;

import fsl.ta.toms.roms.bo.ConfigurationBO;
import fsl.ta.toms.roms.exception.ErrorSavingException;
import fsl.ta.toms.roms.exception.InvalidFieldException;
import fsl.ta.toms.roms.search.criteria.impl.ConfigurationCriteriaBO;
/**
 * 
 * @author jreid
 * Created Date: May 24, 2013
 */
public interface ConfigurationService {
	public List<ConfigurationBO> getConfigurationReference(HashMap<String,String> filter,String status) throws InvalidFieldException;

	public boolean configurationExists(String configurationCode);


	List<ConfigurationBO> lookupConfigurations(
			ConfigurationCriteriaBO configurationCriteriaBO);

	String saveConfiguration(ConfigurationBO configurationBO) throws ErrorSavingException;

	void updateConfiguration(ConfigurationBO configurationBO) throws ErrorSavingException;

	boolean descriptionExists(ConfigurationBO configurationBO);

}
