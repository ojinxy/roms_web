package fsl.ta.toms.roms.service;

import java.util.List;

import fsl.ta.toms.roms.bo.LMISGroupPermissionsViewBO;
import fsl.ta.toms.roms.bo.LMISGroupViewBO;
import fsl.ta.toms.roms.bo.LMISUserViewBO;
import fsl.ta.toms.roms.exception.RequiredFieldMissingException;


public interface UserService {

	/**
	 * Ensure user has entered valid password 
	 * @param userNameTxt
	 * @param passwordTxt
	 * @return
	 */
	boolean validUser(String userNameTxt, String passwordTxt);

	/**
	 * Ensure that user have the needed permission
	 * @param userNameTxt
	 * @param permission
	 * @return
	 */
	boolean hasPermision(String userNameTxt, String permission);

	/**
	 * Return user object 
	 * @param userNameTxt
	 * @return
	 */
	LMISUserViewBO findUser(String userNameTxt);

	/**
	 * Ensure user has entered valid password and that they have the needed permission
	 * @param userNameTxt
	 * @param passwordTxt
	 * @param permission
	 * @return
	 */
	boolean validOverride(String userNameTxt, String passwordTxt,
			String permission);

	/**
	 * Get the groups that the user is associated in 
	 * @param userID
	 * @return
	 */
	List<LMISGroupViewBO> getUserGroups(String userID);

	
	/**
	 * Get the resources associated with the group id
	 * @param groupID
	 * @return
	 */
	List<LMISGroupPermissionsViewBO> getGroupResources(String groupID);

	/**
	 * Get the permissions for the user as string array
	 * @param userID
	 * @return
	 */
	List<String> findUserPermissionStrings(String userID);

	/**
	 * Saves the user log-in event
	 * @param userNameTxt
	 * @return
	 * @throws RequiredFieldMissingException
	 */
	boolean userLogIn(String userNameTxt) throws RequiredFieldMissingException;

	/**
	 * Saves the user log out event
	 * @param userNameTxt
	 * @return
	 * @throws RequiredFieldMissingException
	 */
	boolean userLogOut(String userNameTxt) throws RequiredFieldMissingException;
		
	
}
