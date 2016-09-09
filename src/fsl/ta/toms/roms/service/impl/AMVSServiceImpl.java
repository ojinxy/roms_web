/**
 * Created By: oanguin
 * Date: May 17, 2013
 *
 */
package fsl.ta.toms.roms.service.impl;

import org.springframework.transaction.annotation.Transactional;

import fsl.ta.toms.roms.dao.ConfigurationDAO;
import fsl.ta.toms.roms.dao.DAOFactory;
import fsl.ta.toms.roms.service.AMVSService;

/**
 * @author oanguin
 * Created Date: May 17, 2013
 */
@Transactional
public class AMVSServiceImpl implements AMVSService 
{

	private DAOFactory daoFactory;
	
	public void setDaoFactory(DAOFactory daoFactory) {
		this.daoFactory = daoFactory;
	}
	
	/* (non-Javadoc)
	 * @see fsl.ta.toms.roms.service.AMVSService#getAMVSWebServiceUrl()
	 */
	@Override
	public String getAMVSWebServiceUrl() 
	{
		ConfigurationDAO config = this.daoFactory.getConfigurationDAO();
		
		return config.getConfigurationValue("AMVS_WEBSERV");
	}

}
