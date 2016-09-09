package fsl.ta.toms.roms.dao.impl;

import java.util.ArrayList;
import java.util.List;

import fsl.ta.toms.roms.bo.AuthorizationBO;
import fsl.ta.toms.roms.dao.AuthorizationDAO;
import fsl.ta.toms.roms.dataobjects.LMIS_GroupPermissionsViewDO;

public class AuthorizationDAOImpl extends ParentDAOImpl implements AuthorizationDAO{
	

	@SuppressWarnings("unchecked")
	@Override
	public boolean hasPermission(AuthorizationBO authorizationBO) {
		List<LMIS_GroupPermissionsViewDO> groupPermissionList = new ArrayList<LMIS_GroupPermissionsViewDO>();		
		String permission = authorizationBO.getPermission().toUpperCase();
		String username = authorizationBO.getUsername().toUpperCase();
		
		String queryString = "select gp from LMIS_UserViewDO u, LMIS_UserGroupViewDO ug, LMIS_GroupPermissionsViewDO gp " +
				"where u.username = ug.userName and ug.groupName = gp.groupName " +
				"and UPPER(u.username) = :username and UPPER(gp.resourceKey) = :permission";
		 
		String[] paramNames = {"username", "permission"};
		Object[] values = {username, permission};
		
		groupPermissionList = hibernateTemplate.findByNamedParam(queryString, paramNames, values);
			
		if(groupPermissionList!=null && groupPermissionList.size()>0){
			return true;
		}
		
		return false;
	}

}
