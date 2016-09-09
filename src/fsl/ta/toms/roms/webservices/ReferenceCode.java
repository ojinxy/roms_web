/**
 * Created By: oanguin
 * Date: Apr 18, 2013
 *
 */
package fsl.ta.toms.roms.webservices;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import fsl.ta.toms.roms.bo.RefCodeBO;
import fsl.ta.toms.roms.exception.InvalidFieldException;
import fsl.ta.toms.roms.exception.NoRecordFoundException;
import fsl.ta.toms.roms.exception.RequiredFieldMissingException;
import fsl.ta.toms.roms.search.criteria.impl.RefCodeCriteriaBO;
import fsl.ta.toms.roms.service.ReferenceCodeService;
import fsl.ta.toms.roms.service.ServiceFactory;

/**
 * @author oanguin
 * Created Date: Apr 18, 2013
 */
@RequestMapping("/roms_rest/refCode")
@Controller
public class ReferenceCode extends SpringBeanAutowiringSupport  implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ReferenceCode()
	{
		super();
	}


	@Autowired
	private ServiceFactory serviceFactory;


	
	@RequestMapping("/getReferenceCode")
	public @ResponseBody List<RefCodeBO> getReferenceCode(@RequestBody RefCodeCriteriaBO criteriaBO) throws  RequiredFieldMissingException, NoRecordFoundException,InvalidFieldException
	{
		
		ReferenceCodeService service = serviceFactory.getReferenceCodeService();
		return service.getReferenceCode(criteriaBO);
		
		
		
	}
}
