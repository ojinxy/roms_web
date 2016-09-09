package fsl.ta.toms.roms.dao;

import java.util.List;

import fsl.dao.DAO;
import fsl.ta.toms.roms.dataobjects.LMIS_GroupPermissionsViewDO;
import fsl.ta.toms.roms.dataobjects.LMIS_GroupViewDO;
import fsl.ta.toms.roms.dataobjects.LMIS_UserViewDO;

public interface UserDAO extends DAO {
	void saveOrUpdate(Object entity);

	LMIS_UserViewDO findUser(String username);

	List<LMIS_GroupViewDO> getUserGroups(String userID);

	boolean hasPermission(String userNameTxt, String permission);

	List<LMIS_GroupPermissionsViewDO> getGroupResources(final String groupId);

	List<String> getUserPermissions(String userNameTxt);
}
