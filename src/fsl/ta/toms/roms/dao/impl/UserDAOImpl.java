package fsl.ta.toms.roms.dao.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import fsl.ta.toms.roms.dao.UserDAO;
import fsl.ta.toms.roms.dataobjects.LMIS_GroupPermissionsViewDO;
import fsl.ta.toms.roms.dataobjects.LMIS_GroupViewDO;
import fsl.ta.toms.roms.dataobjects.LMIS_UserViewDO;

public class UserDAOImpl extends ParentDAOImpl implements UserDAO{
	
	
	@Override
	public LMIS_UserViewDO findUser(String username) {
		LMIS_UserViewDO results = null;
		if(StringUtils.trimToNull(username)!=null){
			results = hibernateTemplate.get(LMIS_UserViewDO.class, StringUtils.trimToNull(username).toUpperCase());	
		}
		return results; 
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<LMIS_GroupViewDO> getUserGroups(final String id) {
		return (List) 
		hibernateTemplate.execute(
			new HibernateCallback() {
				public Object doInHibernate(Session session) {
					
					String queryString = 
						"select lmisgroup from LMIS_GroupViewDO lmisgroup, LMIS_UserGroupViewDO usergroup where " +
						"upper(usergroup.userName) = :id and " +
						"usergroup.groupName = lmisgroup.groupName ";
					
					Query query = session.createQuery(queryString);
					
					query.setString("id", id.trim().toUpperCase());
					
					List<LMIS_GroupViewDO> groups = (List<LMIS_GroupViewDO>) query.list(); 
					
					return groups;					
				}
			}
		);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<LMIS_GroupPermissionsViewDO> getGroupResources(final String groupId) {
		return (List) 
		hibernateTemplate.execute(
			new HibernateCallback() {
				public Object doInHibernate(Session session) {
					//System.err.println("GROUP ID: " + id);
					String queryString = 
						"Select group_perm from LMIS_ResourceViewDO resource, LMIS_GroupPermissionsViewDO group_perm where " +
						"group_perm.groupName = :id and " +
						"group_perm.resourceKey = resource.resourceKey";
					
					Query query = session.createQuery(queryString);
					
					query.setString("id", groupId);
					
					List<LMIS_GroupPermissionsViewDO> resourceList = (List<LMIS_GroupPermissionsViewDO>) query.list(); 
										
					return resourceList;
					
				}
			}
		);
	}
	
	@Override
	public boolean hasPermission(String userNameTxt, String permission) {
	
		
		
		String queryString = 
			"select usergroup from LMIS_GroupPermissionsViewDO groupperm, LMIS_UserGroupViewDO usergroup where " +
			"upper(usergroup.userName) = '"+userNameTxt.trim().toUpperCase()+"' and " +
			"usergroup.groupName = groupperm.groupName and " +
			"groupperm.resourceKey = '"+permission+"'";
		
		
		List rslt = hibernateTemplate.find(queryString);
		if(rslt!=null&&!rslt.isEmpty()){
			return true;
		}		
		
		return false;
	}
	
	
	
	@Override
	public List<String> getUserPermissions(String userNameTxt) {
			
		String queryString = 
			"select groupperm.resourceKey from LMIS_GroupPermissionsViewDO groupperm, LMIS_UserGroupViewDO usergroup where " +
			"upper(usergroup.userName) = '"+userNameTxt.trim().toUpperCase()+"' and " +
			"usergroup.groupName = groupperm.groupName";		
		
		List<String> rslt = hibernateTemplate.find(queryString);
	
		//System.err.println(rslt);			
		
		return rslt;
	}
	
	
	

}
