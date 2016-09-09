/**
 * Created By: jreid
 * Date: Nov 13, 2013
 *
 */
package fsl.ta.toms.roms.webservices;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import fsl.ta.toms.roms.bo.LMISGroupPermissionsViewBO;
import fsl.ta.toms.roms.bo.LMISGroupViewBO;
import fsl.ta.toms.roms.bo.LMISUserViewBO;
import fsl.ta.toms.roms.exception.RequiredFieldMissingException;
import fsl.ta.toms.roms.service.ServiceFactory;
import fsl.ta.toms.roms.service.UserService;

/**
 * @author jreid Created Date: Nov 13, 2013
 */

@Controller
@RequestMapping("/roms_rest/userDetail")
public class ROMSUserDetails extends SpringBeanAutowiringSupport {

	@Autowired
	private ServiceFactory	serviceFactory;

	/**
	 * 
	 * @param userNameTxt
	 * @param passwordTxt
	 * @return
	 * @throws RequiredFieldMissingException
	 */
	@RequestMapping(value="/isUserValid")
	public @ResponseBody boolean validUser(@RequestParam String userNameTxt,@RequestParam String passwordTxt) throws RequiredFieldMissingException {
		
		
		System.err.println("isUserValid called");
		
		UserService userService = serviceFactory.getUserService();

		if (StringUtils.isBlank(userNameTxt))
			throw new RequiredFieldMissingException("User Name is required.");

		if (StringUtils.isBlank(passwordTxt))
			throw new RequiredFieldMissingException("Password is required.");

		return userService.validUser(userNameTxt, passwordTxt);

	}

	/**
	 * 
	 * @param userNameTxt
	 * @param permission
	 * @return
	 * @throws RequiredFieldMissingException
	 */
	@RequestMapping(value="/hasPermision")
	public @ResponseBody boolean hasPermision(@RequestParam String userNameTxt, @RequestParam String permission) throws RequiredFieldMissingException {
		UserService userService = serviceFactory.getUserService();

		if (StringUtils.isBlank(userNameTxt))
			throw new RequiredFieldMissingException("User Name is required.");

		if (StringUtils.isBlank(permission))
			throw new RequiredFieldMissingException("Permission is required.");

		return userService.hasPermision(userNameTxt, permission);

	}

	/**
	 * 
	 * @param userNameTxt
	 * @return
	 * @throws RequiredFieldMissingException
	 */
	@RequestMapping(value="/findUser")
	public @ResponseBody LMISUserViewBO findUser(@RequestParam String userNameTxt) throws RequiredFieldMissingException {

		UserService userService = serviceFactory.getUserService();

		if (StringUtils.isBlank(userNameTxt))
			throw new RequiredFieldMissingException("User Name is required.");

		return userService.findUser(userNameTxt);

	}

	/**
	 * 
	 * @param userNameTxt
	 * @param passwordTxt
	 * @param permission
	 * @return
	 * @throws RequiredFieldMissingException
	 */
	public boolean validOverride(String userNameTxt, String passwordTxt, String permission) throws RequiredFieldMissingException {

		UserService userService = serviceFactory.getUserService();

		if (StringUtils.isBlank(userNameTxt))
			throw new RequiredFieldMissingException("User Name is required.");

		if (StringUtils.isBlank(passwordTxt))
			throw new RequiredFieldMissingException("Password is required.");

		if (StringUtils.isBlank(permission))
			throw new RequiredFieldMissingException("Permission is required.");

		return userService.validOverride(userNameTxt, passwordTxt, permission);
	}

	/**
	 * 
	 * @param userNameTxt
	 * @return
	 * @throws RequiredFieldMissingException
	 */
	public List<LMISGroupViewBO> findUserGroups(String userNameTxt) throws RequiredFieldMissingException {

		UserService userService = serviceFactory.getUserService();

		if (StringUtils.isBlank(userNameTxt))
			throw new RequiredFieldMissingException("User Name is required.");

		return userService.getUserGroups(userNameTxt);

	}

	/**
	 * 
	 * @param groupId
	 * @return
	 * @throws RequiredFieldMissingException
	 */
	public List<LMISGroupPermissionsViewBO> findUserPermissions(String groupId) throws RequiredFieldMissingException {

		UserService userService = serviceFactory.getUserService();

		if (StringUtils.isBlank(groupId))
			throw new RequiredFieldMissingException("User Name is required.");

		return userService.getGroupResources(groupId);

	}

	public List<String> findUserPermissionStrings(String userId) throws RequiredFieldMissingException {

		UserService userService = serviceFactory.getUserService();

		if (StringUtils.isBlank(userId))
			throw new RequiredFieldMissingException("User Name is required.");

		return userService.findUserPermissionStrings(userId);
	}

	/************************** Handle Log in and Log Out **********************/

	/**
	 * 
	 * @param userNameTxt
	 * @return
	 * @throws RequiredFieldMissingException
	 */
	
	@RequestMapping(value="/doUserLogIn")
	public @ResponseBody boolean userLogIn(@RequestParam String userNameTxt) throws RequiredFieldMissingException {

		UserService userService = serviceFactory.getUserService();

		if (StringUtils.isBlank(userNameTxt))
			throw new RequiredFieldMissingException("User Name is required.");

		return userService.userLogIn(userNameTxt);

	}

	/**
	 * 
	 * @param userNameTxt
	 * @return
	 * @throws RequiredFieldMissingException
	 */
	@RequestMapping(value="/doUserLogOut")
	public @ResponseBody boolean userLogOut(@RequestParam String userNameTxt) throws RequiredFieldMissingException {

		UserService userService = serviceFactory.getUserService();

		if (StringUtils.isBlank(userNameTxt))
			throw new RequiredFieldMissingException("User Name is required.");

		return userService.userLogOut(userNameTxt);

	}
}
