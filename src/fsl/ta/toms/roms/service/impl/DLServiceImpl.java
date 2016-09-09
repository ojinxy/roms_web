/**
 * Created By: oanguin
 * Date: May 20, 2013
 *
 */
package fsl.ta.toms.roms.service.impl;

import org.springframework.transaction.annotation.Transactional;

import fsl.ta.toms.roms.dao.ConfigurationDAO;
import fsl.ta.toms.roms.dao.DAOFactory;
import fsl.ta.toms.roms.service.DLService;

/**
 * @author oanguin
 * Created Date: May 20, 2013
 */
@Transactional
public class DLServiceImpl implements DLService {

	private DAOFactory daoFactory;
	
	public void setDaoFactory(DAOFactory daoFactory) 
	{
		this.daoFactory = daoFactory;
	}
	/* (non-Javadoc)
	 * @see fsl.ta.toms.roms.service.DLService#getDLWebServiceURL()
	 */
	@Override
	public String getDLWebServiceURL() 
	{
		ConfigurationDAO config = this.daoFactory.getConfigurationDAO();
		return config.getConfigurationValue("DL_WEBSERV");
	}

}
