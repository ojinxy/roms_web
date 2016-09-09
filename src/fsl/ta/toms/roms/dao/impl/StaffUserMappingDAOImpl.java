package fsl.ta.toms.roms.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;

import fsl.ta.toms.roms.bo.BIMSStaffViewBO;
import fsl.ta.toms.roms.bo.LMISUserViewBO;
import fsl.ta.toms.roms.bo.StaffUserMappingBO;
import fsl.ta.toms.roms.bo.TAStaffBO;
import fsl.ta.toms.roms.bo.TAStaffTypeBO;
import fsl.ta.toms.roms.constants.Constants;
import fsl.ta.toms.roms.dao.StaffUserMappingDAO;
import fsl.ta.toms.roms.dataobjects.PersonDO;
import fsl.ta.toms.roms.dataobjects.TAStaffDO;
import fsl.ta.toms.roms.dataobjects.WreckingCompanyDO;
import fsl.ta.toms.roms.search.criteria.impl.StaffUserMappingCriteriaBO;
import fsl.ta.toms.roms.util.StringUtil;

public class StaffUserMappingDAOImpl extends ParentDAOImpl implements StaffUserMappingDAO{

	

	@SuppressWarnings("unchecked")
	@Override
	public List<BIMSStaffViewBO> lookupAllStaff() {
		List<BIMSStaffViewBO> staffViewList = new ArrayList<BIMSStaffViewBO>();
		
		String queryString = "select new fsl.ta.toms.roms.bo.BIMSStaffViewBO(s.staffId, s.trnNbr, " +
				"s.inspRegNo, s.staffType, s.firstName, s.middleName, s.lastName, s.genderCode, " +
				"s.locationCode, s.badgeStatusCode, s.badgeUpdateDTime, s.staffUpdateDTime) " +
				"from BIMS_StaffView s order by UPPER(s.lastName), UPPER(s.firstName)";
		
		
		staffViewList =  hibernateTemplate.find(queryString);
			 
		return staffViewList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BIMSStaffViewBO> lookupActiveStaff() {
		List<BIMSStaffViewBO> staffViewList = new ArrayList<BIMSStaffViewBO>();
		
		String queryString = "select new fsl.ta.toms.roms.bo.BIMSStaffViewBO(s.staffId, s.trnNbr, " +
				"s.inspRegNo, s.staffType, s.firstName, s.middleName, s.lastName, s.genderCode, " +
				"s.locationCode, s.badgeStatusCode, s.badgeUpdateDTime, s.staffUpdateDTime) " +
				"from BIMS_StaffView s " +
				"where s.badgeStatusCode IN ('" + Constants.BadgeStatusCode.PRINTED + "', '" + Constants.BadgeStatusCode.DELIVERED + "') " +
				"order by UPPER(s.lastName), UPPER(s.firstName)";
		
		
		staffViewList =  hibernateTemplate.find(queryString);
			 
		return staffViewList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<LMISUserViewBO> lookupAllUsers() {
		List<LMISUserViewBO> userViewList = new ArrayList<LMISUserViewBO>();
		
		String queryString = "select new fsl.ta.toms.roms.bo.LMISUserViewBO(u.username, u.firstName, " +
				"u.lastName, u.status, u.locationCode) " +
				"from LMIS_UserViewDO u order by UPPER(u.username)";
		
		
		userViewList =  hibernateTemplate.find(queryString);
			 
		return userViewList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<LMISUserViewBO> lookupActiveUsers() {
		List<LMISUserViewBO> userViewList = new ArrayList<LMISUserViewBO>();
		
		String queryString = "select new fsl.ta.toms.roms.bo.LMISUserViewBO(u.username, u.firstName, " +
				"u.lastName, u.status, u.locationCode) " +
				"from LMIS_UserViewDO u " +
				"where u.status = '" + Constants.LMISUserStatusCode.ACTIVE + "' " +
				"order by UPPER(u.lastName)";
		
		
		userViewList =  hibernateTemplate.find(queryString);
			 
		return userViewList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<LMISUserViewBO> lookupAllUnmappedUsers() {
		List<LMISUserViewBO> userViewList = new ArrayList<LMISUserViewBO>();
		
		String queryString = "select new fsl.ta.toms.roms.bo.LMISUserViewBO(u.username, u.firstName, " +
				"u.lastName, u.status, u.locationCode) " +
				"from LMIS_UserViewDO u " +
				"where u.username NOT IN (select s.loginUsername from TAStaffDO s) " + 
				"order by UPPER(u.username)";
		
		
		userViewList =  hibernateTemplate.find(queryString);
			 
		return userViewList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<LMISUserViewBO> lookupActiveUnmappedUsers() {
		List<LMISUserViewBO> userViewList = new ArrayList<LMISUserViewBO>();
		
		String queryString = "select new fsl.ta.toms.roms.bo.LMISUserViewBO(u.username, u.firstName, " +
				"u.lastName, u.status, u.locationCode) " +
				"from LMIS_UserViewDO u " +
				"where u.status = '" + Constants.LMISUserStatusCode.ACTIVE + "' " +
				"and u.username NOT IN (select s.loginUsername from TAStaffDO s) " + 
				"order by UPPER(u.username)";
		
		
		userViewList =  hibernateTemplate.find(queryString);
			 
		return userViewList;
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public List<StaffUserMappingBO> lookupStaffUserMappings(
			StaffUserMappingCriteriaBO staffUserMappingCriteriaBO) {
		List<StaffUserMappingBO> staffUserMappingList = new ArrayList<StaffUserMappingBO>();
		
		StringBuffer queryString = new StringBuffer("select new fsl.ta.toms.roms.bo.StaffUserMappingBO(s.staffId, s.person.firstName, s.person.middleName, s.person.lastName, " +
				"t.staffTypeDesc, l.locationDesc, s.loginUsername) " +
				"from TAStaffDO s,  LMIS_TAOfficeLocationViewDO l, BIMS_CDStaffTypeView t " +
				"where s.officeLocationCode = l.locationCode and s.staffTypeCode = t.staffTypeCode");
		
		
		//Staff ID
		if (StringUtils.trimToNull(staffUserMappingCriteriaBO.getStaffId()) != null) {
			
			queryString.append(" and upper(s.staffId) = '").append(StringUtil.quoteReplace(staffUserMappingCriteriaBO.getStaffId().trim()).toUpperCase()).append("'");
		}
		//First Name		
		if (StringUtils.trimToNull(staffUserMappingCriteriaBO.getFirstName()) != null) {
			
			queryString.append(" and upper(s.person.firstName) like '%").append(StringUtil.quoteReplace(staffUserMappingCriteriaBO.getFirstName().trim()).toUpperCase()).append("%'");
		}
		//Middle Name
		if (StringUtils.trimToNull(staffUserMappingCriteriaBO.getMidName()) != null) {
			
			queryString.append(" and upper(s.person.middleName) like '%").append(StringUtil.quoteReplace(staffUserMappingCriteriaBO.getMidName().trim()).toUpperCase()).append("%'");
		}
		//Last Name
		if (StringUtils.trimToNull(staffUserMappingCriteriaBO.getLastName()) != null) {
			
			queryString.append(" and upper(s.person.lastName) like '%").append(StringUtil.quoteReplace(staffUserMappingCriteriaBO.getLastName().trim()).toUpperCase()).append("%'");
		}
		//Staff Type
		if (StringUtils.trimToNull(staffUserMappingCriteriaBO.getStaffType()) != null) {
			
			queryString.append(" and s.staffTypeCode = '").append(staffUserMappingCriteriaBO.getStaffType()).append("'");
		}
		//Office Location
		if (StringUtils.trimToNull(staffUserMappingCriteriaBO.getOfficeLocationCode()) != null) {
			
			queryString.append(" and s.officeLocationCode = '").append(staffUserMappingCriteriaBO.getOfficeLocationCode()).append("'");
		}
		//User Name
		if (StringUtils.trimToNull(staffUserMappingCriteriaBO.getLoginUsername()) != null) {
			
			queryString.append(" and upper(s.loginUsername) like '%").append(staffUserMappingCriteriaBO.getLoginUsername().trim().toUpperCase()).append("%'");
		}
		
		
		queryString.append(" order by UPPER(s.person.lastName), UPPER(s.person.firstName)");
				
		
		staffUserMappingList =  hibernateTemplate.find(queryString.toString());
			 
		return staffUserMappingList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<StaffUserMappingBO> lookupTAStaff(
			StaffUserMappingCriteriaBO staffUserMappingCriteriaBO) {
		List<StaffUserMappingBO> staffUserMappingList = new ArrayList<StaffUserMappingBO>();
		String regionList=returnCommaDelimitedStringList(staffUserMappingCriteriaBO.getSelectedRegions());
		StringBuffer queryString = new StringBuffer("select new fsl.ta.toms.roms.bo.StaffUserMappingBO(v) " +
				"from ROMS_AllStaffView v where (v.staffId is not null)");
		
		
		//Staff ID
		if (StringUtils.trimToNull(staffUserMappingCriteriaBO.getStaffId()) != null) {
			
			queryString.append(" and upper(v.staffId) = '").append(StringUtil.quoteReplace(staffUserMappingCriteriaBO.getStaffId().trim()).toUpperCase()).append("'");
		}
		//First Name		
		if (StringUtils.trimToNull(staffUserMappingCriteriaBO.getFirstName()) != null) {
			
			queryString.append(" and upper(v.firstName) like '%").append(StringUtil.quoteReplace(staffUserMappingCriteriaBO.getFirstName().trim()).toUpperCase()).append("%'");
		}
		//Middle Name
		if (StringUtils.trimToNull(staffUserMappingCriteriaBO.getMidName()) != null) {
			
			queryString.append(" and upper(v.middleName) like '%").append(StringUtil.quoteReplace(staffUserMappingCriteriaBO.getMidName().trim()).toUpperCase()).append("%'");
		}
		//Last Name
		if (StringUtils.trimToNull(staffUserMappingCriteriaBO.getLastName()) != null) {
			
			queryString.append(" and upper(v.lastName) like '%").append(StringUtil.quoteReplace(staffUserMappingCriteriaBO.getLastName().trim()).toUpperCase()).append("%'");
		}
		//Staff Type
		if (StringUtils.trimToNull(staffUserMappingCriteriaBO.getStaffType()) != null) {
			
			queryString.append(" and v.staffTypeCode = '").append(staffUserMappingCriteriaBO.getStaffType()).append("'");
		}
		//Office Location
		if (StringUtils.trimToNull(staffUserMappingCriteriaBO.getOfficeLocationCode()) != null) {
			
			queryString.append(" and v.officeLocationCode = '").append(staffUserMappingCriteriaBO.getOfficeLocationCode()).append("'");
		}
		//Region List
				if (staffUserMappingCriteriaBO.getSelectedRegions() != null && staffUserMappingCriteriaBO.getSelectedRegions().size()>0) {
					
					queryString.append(" and v.officeLocationCode IN (" +  regionList + ")");
				}
		//User Name
		if (StringUtils.trimToNull(staffUserMappingCriteriaBO.getLoginUsername()) != null) {
			
			queryString.append(" and upper(v.loginUsername) like '%").append(staffUserMappingCriteriaBO.getLoginUsername().trim().toUpperCase()).append("%'");
		}
		
		
		queryString.append(" order by UPPER(v.lastName), UPPER(v.firstName), UPPER(v.middleName)");
				
		
		staffUserMappingList =  hibernateTemplate.find(queryString.toString());
		
		if (staffUserMappingList == null || staffUserMappingList.size() < 1) {
			return null;
		}
		
		for(StaffUserMappingBO staffUserMappingBO: staffUserMappingList){
			
			
			if(staffUserMappingBO.getAuditEntryBO().getUpdateUsername() != null)
			{
				staffUserMappingBO.getAuditEntryBO().setUpdateUsername(usernameToFullName(staffUserMappingBO.getAuditEntryBO().getUpdateUsername()));
			}
			else
			{
				if(staffUserMappingBO.getAuditEntryBO().getCreateUsername() != null)
				{
					staffUserMappingBO.getAuditEntryBO().setUpdateUsername(usernameToFullName(staffUserMappingBO.getAuditEntryBO().getCreateUsername()));
				}
			}
				
			continue;
					
		}
			 
		return staffUserMappingList;
	}
	
	private String returnCommaDelimitedStringList(List<String> stringList){
		String commaDelimitedList="";
		
		int listSize = stringList.size();
		
		for(int i=0; i<listSize; i++){
			if(stringList.get(i)!=null){
				commaDelimitedList = commaDelimitedList + "'" +stringList.get(i).toUpperCase();
				if(i<listSize-1){
					commaDelimitedList = commaDelimitedList + "', ";
				}
				else{
					commaDelimitedList = commaDelimitedList + "'";
				}
			}
		}
		return commaDelimitedList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean isUserMapped(String username) {
		List<TAStaffDO> taStaffList = new ArrayList<TAStaffDO>();
		String uname = username.toUpperCase();
		
		String queryString = " from TAStaffDO s where UPPER(s.loginUsername) = :uname";
		queryString += " and s.status.statusId = 'ACT'";
		
		BIMSStaffViewBO bimsStaffViewBO = new BIMSStaffViewBO(); 
		
		taStaffList = hibernateTemplate.findByNamedParam(queryString, "uname", uname);
			
		if(taStaffList!=null && taStaffList.size()>0){
			
			//additional check for instances where staff id has been changed in BIMS
			TAStaffDO tastaff = new TAStaffDO();
			tastaff = taStaffList.get(0);
			bimsStaffViewBO = findTAStaffByStaffId(tastaff.getStaffId());
			
			if(bimsStaffViewBO == null)
			{
				return false;
			}
			
			return true;
		}
		
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean staffExists(String staffId) {
		List<TAStaffDO> taStaffList = new ArrayList<TAStaffDO>();
		String id = StringUtil.quoteReplace(staffId.toUpperCase());
		
		String queryString = " from TAStaffDO s where UPPER(s.staffId) = :staffId";
		 
		
		taStaffList = hibernateTemplate.findByNamedParam(queryString, "staffId", id);
			
		if(taStaffList!=null && taStaffList.size()>0){
			return true;
		}
		
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean isStaffMapped(String staffId) {
		List<TAStaffDO> taStaffList = new ArrayList<TAStaffDO>();
		String id = StringUtil.quoteReplace(staffId.toUpperCase());
		
		String queryString = " from TAStaffDO s where UPPER(s.staffId) = :staffId and (s.loginUsername is not null)";
		queryString += " and s.status.statusId = 'ACT'"; 
		
		taStaffList = hibernateTemplate.findByNamedParam(queryString, "staffId", id);
			
		if(taStaffList!=null && taStaffList.size()>0){
			return true;
		}
		
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public TAStaffBO getStaffByUsername(String username) {
		List<TAStaffBO> taStaffList = new ArrayList<TAStaffBO>();
		String uname = username.toUpperCase();
//		System.err.println("Getting staff by username: "+username);
		String queryString = "select new fsl.ta.toms.roms.bo.TAStaffBO(s)" +
				" from TAStaffDO s where UPPER(s.loginUsername) = :uname";
		queryString += " and s.status.statusId = 'ACT'";
		
		taStaffList = hibernateTemplate.findByNamedParam(queryString, "uname", uname);
			
		if(taStaffList!=null && taStaffList.size()==1){
			return taStaffList.get(0);
		}
		
		return null;
	}

	@Override
	public Integer savePersonDO(PersonDO personDO) {
		Integer personId = (Integer) (hibernateTemplate.save(personDO));
		
		return personId;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BIMSStaffViewBO> lookupAllUnmappedStaff() {
		List<BIMSStaffViewBO> staffViewList = new ArrayList<BIMSStaffViewBO>();
		
		String queryString = "select new fsl.ta.toms.roms.bo.BIMSStaffViewBO(s.staffId, s.trnNbr, " +
				"s.inspRegNo, s.staffType, s.firstName, s.middleName, s.lastName, s.genderCode, " +
				"s.locationCode, s.badgeStatusCode, s.badgeUpdateDTime, s.staffUpdateDTime) " +
				"from BIMS_StaffView s " +
				"where s.staffId NOT IN (select o.staffId from TAStaffDO o where o.loginUsername is not null) " + 
				"order by UPPER(s.lastName), UPPER(s.firstName)";
		
		
		staffViewList =  hibernateTemplate.find(queryString);
			 
		return staffViewList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BIMSStaffViewBO> lookupActiveUnmappedStaff() {
		List<BIMSStaffViewBO> staffViewList = new ArrayList<BIMSStaffViewBO>();
		
		String queryString = "select new fsl.ta.toms.roms.bo.BIMSStaffViewBO(s.staffId, s.trnNbr, " +
				"s.inspRegNo, s.staffType, s.firstName, s.middleName, s.lastName, s.genderCode, " +
				"s.locationCode, s.badgeStatusCode, s.badgeUpdateDTime, s.staffUpdateDTime) " +
				"from BIMS_StaffView s " +
				"where s.badgeStatusCode IN ('" + Constants.BadgeStatusCode.PRINTED + "', '" + Constants.BadgeStatusCode.DELIVERED + "') " +
				"and s.staffId NOT IN (select o.staffId from TAStaffDO o where o.loginUsername is not null) " + 
				"order by UPPER(s.lastName), UPPER(s.firstName)";
		
		
		staffViewList =  hibernateTemplate.find(queryString);
			 
		return staffViewList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public PersonDO findPersonByTRN(String trn) {
		List<PersonDO> personList = new ArrayList<PersonDO>();
		
		String queryString = " from PersonDO p where p.trnNbr = :trn";
		 
		
		personList = hibernateTemplate.findByNamedParam(queryString, "trn", StringUtil.quoteReplace(trn));
			
		if(personList!=null && personList.size()==1){
			return personList.get(0);
		}
		
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public BIMSStaffViewBO findTAStaffByStaffId(String staffId) {
		List<BIMSStaffViewBO> staffList = new ArrayList<BIMSStaffViewBO>();
		
		String queryString = "select new fsl.ta.toms.roms.bo.BIMSStaffViewBO(s.staffId, s.trnNbr, " +
				"s.inspRegNo, s.staffType, s.firstName, s.middleName, s.lastName, s.genderCode, " +
				"s.locationCode, s.badgeStatusCode, s.badgeUpdateDTime, s.staffUpdateDTime) " +
				"from BIMS_StaffView s where s.staffId = :staffId";
		 
		
		staffList = hibernateTemplate.findByNamedParam(queryString, "staffId", StringUtil.quoteReplace(staffId));
			
		if(staffList!=null && staffList.size()==1){
			return staffList.get(0);
		}
		
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean taStaffExistWithTRN(String trnNbr) {
		List<TAStaffDO> itaExaminerDO = new ArrayList<TAStaffDO>();
		
		StringBuffer queryString = new StringBuffer(" from TAStaffDO o where o.person.trnNbr = :trnNbr");
		
		itaExaminerDO = hibernateTemplate.findByNamedParam(queryString.toString(), "trnNbr", StringUtil.quoteReplace(trnNbr));
		
		if(itaExaminerDO==null || itaExaminerDO.size()==0){
			return false;
		}
		
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TAStaffBO> getAllROMSStaff() {
		List<TAStaffBO> taStaffList = new ArrayList<TAStaffBO>();
		
		String queryString = "select new fsl.ta.toms.roms.bo.TAStaffBO(s)" +
				" from TAStaffDO s";
		 
		
		taStaffList = hibernateTemplate.find(queryString);
			
		if(taStaffList==null || taStaffList.size()<=0){
			return null;
		}
		
		return taStaffList;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<TAStaffTypeBO> lookupBIMSStaffType() {
		List<TAStaffTypeBO> staffTypeList = new ArrayList<TAStaffTypeBO>();
		
		String queryString = "select new fsl.ta.toms.roms.bo.TAStaffTypeBO(t) " +
				"from BIMS_CDStaffTypeView t order by UPPER(t.staffTypeDesc)";
		
		
		staffTypeList =  hibernateTemplate.find(queryString);
			 
		return staffTypeList;
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<LMISUserViewBO> lookupUsersByCriteria(String term) {
		List<LMISUserViewBO> userViewList = new ArrayList<LMISUserViewBO>();
	
		StringBuffer queryString = new StringBuffer("select new fsl.ta.toms.roms.bo.LMISUserViewBO(u.username, u.firstName, " +
				"u.lastName, u.status, u.locationCode) " +
				"from LMIS_UserViewDO u ");
		queryString.append("where upper(u.username) like '%").append(StringUtil.quoteReplace(term.trim()).toUpperCase()).append("%' ");

		queryString.append("order by UPPER(u.username)");
		
		
		userViewList =  hibernateTemplate.find(queryString.toString());
			 
		return userViewList;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<LMISUserViewBO> lookupAllUnmappedUsersByCriteria(String term) {
		List<LMISUserViewBO> userViewList = new ArrayList<LMISUserViewBO>();
		List<String> staffUserNameList = getAllROMSStaffUserName();
		getAllROMSStaff();
		String staffUserList = returnCommaDelimitedStringList(staffUserNameList);
		StringBuffer queryString = new StringBuffer("select new fsl.ta.toms.roms.bo.LMISUserViewBO(u.username, u.firstName, " +
				"u.lastName, u.status, u.locationCode) " +
				"from LMIS_UserViewDO u " +
				"where u.username NOT IN ("+ staffUserList+ ") " );
		queryString.append("and upper(u.username) like '%").append(StringUtil.quoteReplace(term.trim()).toUpperCase()).append("%' ");

		queryString.append("order by UPPER(u.username)");
		
		
		userViewList =  hibernateTemplate.find(queryString.toString());
			 
		return userViewList;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<LMISUserViewBO> lookupAllUnmappedUsersByCriteriaExceptSpecificUser(String term, String staffId) {
		List<LMISUserViewBO> userViewList = new ArrayList<LMISUserViewBO>();
		List<String> staffUserNameList = getAllROMSStaffUserNameExceptSpecific(staffId);
		String staffUserList="";
		if(staffUserNameList!=null){
			 staffUserList = returnCommaDelimitedStringList(staffUserNameList);
		}
		
		StringBuffer queryString = new StringBuffer("select new fsl.ta.toms.roms.bo.LMISUserViewBO(u.username, u.firstName, " +
				"u.lastName, u.status, u.locationCode) " +
				"from LMIS_UserViewDO u where ");
				if(!staffUserList.equals("")){
					queryString.append(" u.username NOT IN ("+ staffUserList+ ") and " );
				}
				
		queryString.append(" upper(u.username) like '%").append(StringUtil.quoteReplace(term.trim()).toUpperCase()).append("%' ");

		queryString.append("order by UPPER(u.username)");
		
		
		userViewList =  hibernateTemplate.find(queryString.toString());
			 
		return userViewList;
	}
	
	@SuppressWarnings("unchecked")
	private List<String> getAllROMSStaffUserName(){
		List<String> staffUserNameList = new ArrayList<String>();
		StringBuffer queryString = new StringBuffer("select s.loginUsername from TAStaffDO s");
		
		staffUserNameList = hibernateTemplate.find(queryString.toString());
		
		
		return staffUserNameList;
	}
	
	
	@SuppressWarnings("unchecked")
	private List<String> getAllROMSStaffUserNameExceptSpecific(String staffId){
		List<String> staffUserNameList = new ArrayList<String>();
		StringBuffer queryString = new StringBuffer("select s.loginUsername from TAStaffDO s where s.staffId <> '").append(staffId).append("' ");
		
		queryString.append(" and s.status.statusId = 'ACT'");
		
		//System.err.println("User id: "+ staffId);
		//System.err.println("query: "+queryString.toString());
		staffUserNameList = hibernateTemplate.find(queryString.toString());
			
		if(staffUserNameList!=null && staffUserNameList.size()==0){
			return null;
		}
		
		if(staffUserNameList==null){
			return null;
		}
		
		//System.err.println("Staff: "+staffUserNameList);
		return staffUserNameList;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public LMISUserViewBO lookupLMISUser(String username) {
		List<LMISUserViewBO> userViewList = new ArrayList<LMISUserViewBO>();
		
		String queryString = "select new fsl.ta.toms.roms.bo.LMISUserViewBO(u.username, u.firstName, " +
				"u.lastName, u.status, u.locationCode) " +
				"from LMIS_UserViewDO u where u.username = :username";
		
		
		
		userViewList = hibernateTemplate.findByNamedParam(queryString, "username", username);
			 
		if(userViewList!=null && userViewList.size()==1){
			return userViewList.get(0);
		}
		return null;
	}
}
