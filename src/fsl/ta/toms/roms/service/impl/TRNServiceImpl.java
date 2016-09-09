/**
 * Created By: oanguin
 * Date: May 17, 2013
 *
 */
package fsl.ta.toms.roms.service.impl;

import org.springframework.transaction.annotation.Transactional;

import fsl.ta.toms.roms.dao.ConfigurationDAO;
import fsl.ta.toms.roms.dao.DAOFactory;
import fsl.ta.toms.roms.service.TRNService;

/**
 * @author oanguin
 * Created Date: May 17, 2013
 * 
 * 
 */
@Transactional
public class TRNServiceImpl implements TRNService {

	private DAOFactory daoFactory;
	
	public void setDaoFactory(DAOFactory daoFactory) {
		this.daoFactory = daoFactory;
	}
	/* (non-Javadoc)
	 * @see fsl.ta.toms.roms.service.TRNService#getTRNWebServiceURL()
	 */
	@Override
	public String getTRNWebServiceURL()
	{
		ConfigurationDAO config = this.daoFactory.getConfigurationDAO();
		return config.getConfigurationValue("TRN_WEBSERV");
	}

}
