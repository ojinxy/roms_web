package fsl.ta.toms.roms.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import fsl.ta.toms.roms.bo.LMISGroupPermissionsViewBO;
import fsl.ta.toms.roms.bo.LMISGroupViewBO;
import fsl.ta.toms.roms.bo.LMISUserViewBO;
import fsl.ta.toms.roms.bo.RoadOperationBO;
import fsl.ta.toms.roms.constants.Constants;
import fsl.ta.toms.roms.dao.DAOFactory;
import fsl.ta.toms.roms.dao.UserDAO;
import fsl.ta.toms.roms.dataobjects.EventAuditDO;
import fsl.ta.toms.roms.dataobjects.LMIS_GroupPermissionsViewDO;
import fsl.ta.toms.roms.dataobjects.LMIS_GroupViewDO;
import fsl.ta.toms.roms.dataobjects.LMIS_TAOfficeLocationViewDO;
import fsl.ta.toms.roms.dataobjects.LMIS_UserViewDO;
import fsl.ta.toms.roms.exception.RequiredFieldMissingException;
import fsl.ta.toms.roms.security.Encryptor;
import fsl.ta.toms.roms.service.EventAuditService;
import fsl.ta.toms.roms.service.ServiceFactory;
import fsl.ta.toms.roms.service.UserService;

public class UserServiceImpl implements UserService {

	@Autowired
	DAOFactory		daoFactory;

	@Autowired
	ServiceFactory	serviceFactory;

	@Override
	public boolean validUser(String userNameTxt, String passwordTxt) {

		UserDAO userDAO = daoFactory.getUserDAO();

		LMIS_UserViewDO userBO = userDAO.findUser(userNameTxt);

		if (userBO != null) {
			if (userBO.getPasswordHash().equals(Encryptor.encrypt(passwordTxt))) {
				return true;
			}
		}

		return false;
	}

	@Override
	public boolean hasPermision(String userNameTxt, String permission) {
		UserDAO userDAO = daoFactory.getUserDAO();
		if (userDAO.hasPermission(userNameTxt, permission)) {
			return true;
		}

		return false;
	}

	@Override
	public LMISUserViewBO findUser(String userNameTxt) {

		UserDAO userDAO = daoFactory.getUserDAO();

		LMIS_UserViewDO userDO = userDAO.findUser(userNameTxt);

		if (userDO != null) {

			LMISUserViewBO userBO = new LMISUserViewBO(userDO);

			LMIS_TAOfficeLocationViewDO userLocationViewBO = new LMIS_TAOfficeLocationViewDO();
			userLocationViewBO = (LMIS_TAOfficeLocationViewDO) daoFactory.getUserDAO().find(LMIS_TAOfficeLocationViewDO.class, userDO.getLocationCode());

			// set the user
			if (userLocationViewBO != null)
				userBO.setLocationDescription(userLocationViewBO.getLocationDesc());

			// set the list of permissions
			List<String> permissions = daoFactory.getUserDAO().getUserPermissions(userDO.getUsername());
			if (permissions != null && !permissions.isEmpty())
				userBO.setPermissions(permissions);

			// set the road operation details
			RoadOperationBO roadOp = daoFactory.getRoadOperationDAO().findActiveOperationForUser(userDO.getUsername());

			if (roadOp != null) {
				userBO.setCurrentRoadOperationName(roadOp.getOperationName());
				userBO.setCurrentRoadOperationId(roadOp.getRoadOperationId());

				if (roadOp.getScheduledStartDate() != null)
					userBO.setCurrentRoadOperationStartDateNTime(roadOp.getScheduledStartDate());

				if (roadOp.getScheduledEndDate() != null)
					userBO.setCurrentRoadOperationEndDateNTime(roadOp.getScheduledEndDate());
			}

			return userBO;
		}

		return null;
	}

	@Override
	public boolean validOverride(String userNameTxt, String passwordTxt, String permission) {

		return (validUser(userNameTxt, passwordTxt) && hasPermision(userNameTxt, permission));
	}

	@Override
	public List<LMISGroupViewBO> getUserGroups(String userID) {

		UserDAO userDAO = daoFactory.getUserDAO();

		if (StringUtils.isBlank(userID))
			return null;

		List<LMIS_GroupViewDO> groups = userDAO.getUserGroups(userID);
		List<LMISGroupViewBO> groupsView = new ArrayList<LMISGroupViewBO>();

		for (LMIS_GroupViewDO item : groups) {

			groupsView.add(new LMISGroupViewBO(item));
		}

		return groupsView;
	}

	@Override
	public List<LMISGroupPermissionsViewBO> getGroupResources(String groupID) {

		UserDAO userDAO = daoFactory.getUserDAO();

		if (StringUtils.isBlank(groupID))
			return null;

		List<LMIS_GroupPermissionsViewDO> groups = userDAO.getGroupResources(groupID);
		List<LMISGroupPermissionsViewBO> groupsView = new ArrayList<LMISGroupPermissionsViewBO>();

		for (LMIS_GroupPermissionsViewDO item : groups) {

			groupsView.add(new LMISGroupPermissionsViewBO(item));
		}

		return groupsView;
	}

	@Override
	public List<String> findUserPermissionStrings(String userID) {

		//UserDAO userDAO = daoFactory.getUserDAO();

		if (StringUtils.isBlank(userID))
			return null;

		List<String> perms = daoFactory.getUserDAO().getUserPermissions(userID);

		return perms;
	}

	/******************* USER EVENT AUDIT ************************/
	@Override
	/**
	 * 
	 * @param userNameTxt
	 * @return
	 * @throws RequiredFieldMissingException
	 */
	public boolean userLogIn(String userNameTxt) throws RequiredFieldMissingException {

		EventAuditService service = serviceFactory.getEventAuditService();

		if (StringUtils.isBlank(userNameTxt))
			throw new RequiredFieldMissingException("User Name is required.");
		
		EventAuditDO audit = new EventAuditDO();
		audit.getAuditEntry().setCreateUsername(userNameTxt);
		audit.setEventCode(Constants.EventCode.LOG_IN);
		
		return service.saveEventAuditDO(audit);

	}

	@Override
	/**
	 * 
	 * @param userNameTxt
	 * @return
	 * @throws RequiredFieldMissingException
	 */
	public boolean userLogOut(String userNameTxt) throws RequiredFieldMissingException {

		EventAuditService service = serviceFactory.getEventAuditService();

		if (StringUtils.isBlank(userNameTxt))
			throw new RequiredFieldMissingException("User Name is required.");

		EventAuditDO audit = new EventAuditDO();
		audit.getAuditEntry().setCreateUsername(userNameTxt);
		audit.setEventCode(Constants.EventCode.LOG_OUT);
		
		return service.saveEventAuditDO(audit);
	}
}
