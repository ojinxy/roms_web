/**
 * Created By: oanguin
 * Date: May 20, 2013
 *
 */
package fsl.ta.toms.roms.webservices;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import fsl.ta.toms.roms.dlwebservice.DriverLicenceDetails;
import fsl.ta.toms.roms.dlwebservice.FslDriversLicence;
import fsl.ta.toms.roms.dlwebservice.FslWebServiceException_Exception;
import fsl.ta.toms.roms.exception.RequiredFieldMissingException;

/**
 * @author oanguin
 * Created Date: May 20, 2013
 */
public class DLWebService extends SpringBeanAutowiringSupport implements Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Autowired
	FslDriversLicence dlWebservice;
	
	public DriverLicenceDetails getDriversLicence(String dl) throws FslWebServiceException_Exception, RequiredFieldMissingException
	{
		if(dl == null || dl.isEmpty())
			throw new RequiredFieldMissingException();
		
		return dlWebservice.getDriversLicenceI(dl);
	}
	

}
