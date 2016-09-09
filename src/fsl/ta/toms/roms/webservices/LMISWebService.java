/**
 * Created By: oanguin
 * Date: May 3, 2013
 *
 */
package fsl.ta.toms.roms.webservices;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import fsl.ta.toms.roms.bo.RoadLicenceBO;
import fsl.ta.toms.roms.exception.NoRecordFoundException;
import fsl.ta.toms.roms.exception.RequiredFieldMissingException;
import fsl.ta.toms.roms.service.LMISService;
import fsl.ta.toms.roms.service.ServiceFactory;

/**
 * @author oanguin
 * Created Date: May 3, 2013
 */
public class LMISWebService extends SpringBeanAutowiringSupport implements
		Serializable 
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	private ServiceFactory serviceFactory;

	public LMISWebService() 
	{
		super();
		// TODO Auto-generated constructor stub
	}
	
	public RoadLicenceBO getRoadLicenceDetails(Integer roadLicenceNo) throws RequiredFieldMissingException, NoRecordFoundException
	{
		LMISService lmisService = this.serviceFactory.getLMISService();
		
		RoadLicenceBO roadLicenceBO = lmisService.getRoadLicenceDetails(roadLicenceNo);
		
		return roadLicenceBO;
	}
	
}
