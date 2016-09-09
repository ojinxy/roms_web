/**
 * Created By: oanguin
 * Date: Apr 22, 2013
 *
 */
package fsl.ta.toms.roms.dao;

import java.util.HashMap;
import java.util.List;

import fsl.dao.DAO;
import fsl.ta.toms.roms.bo.ConfigurationBO;
import fsl.ta.toms.roms.dataobjects.ConfigurationDO;
import fsl.ta.toms.roms.exception.InvalidFieldException;
import fsl.ta.toms.roms.search.criteria.impl.ConfigurationCriteriaBO;

/**
 * @author oanguin
 * Created Date: Apr 22, 2013
 */
public interface ConfigurationDAO extends DAO {

	public List<ConfigurationDO> getConfigurationReference(HashMap<String,String> filter,String status) throws InvalidFieldException;
	
	/**
	 * 
	 * @param cfg_code - This is the code which will be used to search for value
	 * @return String - containing value based on the configuration code.
	 */
	public String getConfigurationValue(String cfg_code);

	List<ConfigurationDO> lookupConfiguration(
			ConfigurationCriteriaBO configurationCriteriaBO);

	boolean configurationExists(String configurationId);

	boolean configurationDescriptionExists(ConfigurationBO configuration);
}
