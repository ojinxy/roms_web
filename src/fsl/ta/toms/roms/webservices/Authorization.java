package fsl.ta.toms.roms.webservices;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import fsl.ta.toms.roms.bo.AuthorizationBO;
import fsl.ta.toms.roms.constants.Constants;
import fsl.ta.toms.roms.exception.InvalidFieldException;
import fsl.ta.toms.roms.exception.RequiredFieldMissingException;
import fsl.ta.toms.roms.service.ServiceFactory;

public class Authorization extends SpringBeanAutowiringSupport{

	@Autowired
	private ServiceFactory serviceFactory;
	
	/**
	 * This method validates a Person password or pin depending on the Person Type
	 * @param authorizationBO
	 * @return
	 * @throws RequiredFieldMissingException
	 * @throws InvalidFieldException
	 */
	public boolean validatePerson(AuthorizationBO authorizationBO) throws RequiredFieldMissingException, InvalidFieldException{
		boolean isValid = false;
		
		if(StringUtils.isBlank(authorizationBO.getUsername()) || StringUtils.isBlank(authorizationBO.getPassword()) || StringUtils.isBlank(authorizationBO.getPersonType())){
			throw new RequiredFieldMissingException();
		}
		
		if(!authorizationBO.getPersonType().equalsIgnoreCase(Constants.PersonType.TA_STAFF)  && !authorizationBO.getPersonType().equalsIgnoreCase(Constants.PersonType.JP)){
			throw new InvalidFieldException();
		}
		
		if(authorizationBO.getPersonType().equalsIgnoreCase(Constants.PersonType.TA_STAFF)){
			isValid = serviceFactory.getAuthorizationService().validateTAStaff(authorizationBO);
		}
		else if(authorizationBO.getPersonType().equalsIgnoreCase(Constants.PersonType.JP)){
			isValid = serviceFactory.getAuthorizationService().validateJP(authorizationBO);
		}
		
		return isValid;
	}
	
	/**
	 * This method checks if a user has a specific permission
	 * @param authorizationBO
	 * @return
	 * @throws RequiredFieldMissingException
	 */
	public boolean hasPermission(AuthorizationBO authorizationBO) throws RequiredFieldMissingException{
		boolean hasPermission = false;
		
		if(StringUtils.isBlank(authorizationBO.getUsername())  || StringUtils.isBlank(authorizationBO.getPermission())){
			throw new RequiredFieldMissingException();
		}
		
		hasPermission = serviceFactory.getAuthorizationService().hasPermission(authorizationBO);
		
		return hasPermission;
	}
}
