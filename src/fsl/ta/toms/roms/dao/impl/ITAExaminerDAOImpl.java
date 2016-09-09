/**
 * Created By: oanguin
 * Date: Apr 20, 2013
 *
 */
package fsl.ta.toms.roms.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;

import fsl.ta.toms.roms.bo.ITAExaminerBO;
import fsl.ta.toms.roms.dao.ITAExaminerDAO;
import fsl.ta.toms.roms.dataobjects.CourtDO;
import fsl.ta.toms.roms.dataobjects.ITAExaminerDO;
import fsl.ta.toms.roms.exception.InvalidFieldException;
import fsl.ta.toms.roms.search.criteria.impl.ITAExaminerCriteriaBO;
import fsl.ta.toms.roms.util.StringUtil;

/**
 * @author oanguin
 * Created Date: Apr 20, 2013
 */
public class ITAExaminerDAOImpl extends ParentDAOImpl implements
		ITAExaminerDAO {

	

	/* (non-Javadoc)
	 * @see fsl.ta.toms.roms.dao.ITAExaminerDAO#getITAExaminerReference(java.util.List, java.util.HashMap, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ITAExaminerDO> getITAExaminerReference(
			HashMap<String, String> filter, String status) throws InvalidFieldException 
	{
		return (List<ITAExaminerDO>)super.getReference(ITAExaminerDO.class, status, filter);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ITAExaminerBO> lookupITAExaminer(
			ITAExaminerCriteriaBO itaExaminerCriteriaBO) {

		
		//System.err.println("ITA Criteria: "+itaExaminerCriteriaBO.toString());
		
		List<ITAExaminerBO> itaExaminerList = new ArrayList<ITAExaminerBO>();
		
		StringBuffer queryString = new StringBuffer("select new fsl.ta.toms.roms.bo.ITAExaminerBO(o) from ITAExaminerDO o");
		queryString.append(" where (o.idNumber is not null)");
		
		/*if(StringUtils.trimToNull(itaExaminerCriteriaBO.getIdNumber()) != null){
			queryString.append(" and upper(o.idNumber) = '").append(StringUtil.quoteReplace(itaExaminerCriteriaBO.getIdNumber().trim()).toUpperCase()).append("'");
		}*/
		
		if(StringUtils.trimToNull(itaExaminerCriteriaBO.getExaminerId()) != null){
			queryString.append(" and upper(o.examinerId) = '").append(StringUtil.quoteReplace(itaExaminerCriteriaBO.getExaminerId().trim()).toUpperCase()).append("'");
		}
		
		if(StringUtils.trimToNull(itaExaminerCriteriaBO.getTrnNbr()) != null){
			queryString.append(" and o.person.trnNbr = '").append(StringUtil.quoteReplace(itaExaminerCriteriaBO.getTrnNbr().trim())).append("'");
		}
		
		if(StringUtils.trimToNull(itaExaminerCriteriaBO.getFirstName()) != null){
			queryString.append(" and upper(o.person.firstName) like '%").append(StringUtil.quoteReplace(itaExaminerCriteriaBO.getFirstName().trim()).toUpperCase()).append("%'");
		}
		
		if(StringUtils.trimToNull(itaExaminerCriteriaBO.getLastName()) != null){
			queryString.append(" and upper(o.person.lastName) like '%").append(StringUtil.quoteReplace(itaExaminerCriteriaBO.getLastName().trim()).toUpperCase()).append("%'");
		}
		
		if(StringUtils.trimToNull(itaExaminerCriteriaBO.getMiddleName()) != null){
			queryString.append(" and upper(o.person.middleName) like '%").append(StringUtil.quoteReplace(itaExaminerCriteriaBO.getMiddleName().trim()).toUpperCase()).append("%'");
		}
		
		if(StringUtils.trimToNull(itaExaminerCriteriaBO.getOfficeLocationCode()) != null){
			queryString.append(" and upper(o.officeLocationCode) = '").append(StringUtil.quoteReplace(itaExaminerCriteriaBO.getOfficeLocationCode().trim()).toUpperCase()).append("'");
		}
		
		if(StringUtils.trimToNull(itaExaminerCriteriaBO.getStatusId()) != null){
			queryString.append(" and upper(o.status.statusId) = '").append(StringUtil.quoteReplace(itaExaminerCriteriaBO.getStatusId().trim()).toUpperCase()).append("'");
		}
		
		
		queryString.append(" order by o.person.lastName, o.person.firstName");
		
		
		//System.err.println("ita query: "+queryString.toString());
		
		itaExaminerList =  hibernateTemplate.find(queryString.toString());
		 
		if(itaExaminerList==null || itaExaminerList.size()<1){
			return null;
		}
		
		for(ITAExaminerBO itaExamBO: itaExaminerList){
			if(itaExamBO.getAuditEntryBO().getUpdateUsername() != null)
				itaExamBO.getAuditEntryBO().setUpdateUsername(usernameToFullName(itaExamBO.getAuditEntryBO().getUpdateUsername()));
			else
				itaExamBO.getAuditEntryBO().setUpdateUsername(usernameToFullName(itaExamBO.getAuditEntryBO().getCreateUsername()));
		}
		
		return itaExaminerList;
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean itaExaminerExistWithTRN(String trnNbr) {
		List<ITAExaminerDO> itaExaminerDO = new ArrayList<ITAExaminerDO>();
		
		StringBuffer queryString = new StringBuffer(" from ITAExaminerDO o where o.person.trnNbr = :trnNbr");
		
		itaExaminerDO = hibernateTemplate.findByNamedParam(queryString.toString(), "trnNbr", StringUtil.quoteReplace(trnNbr));
		
		if(itaExaminerDO==null || itaExaminerDO.size()==0){
			return false;
		}
		
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ITAExaminerDO findByExaminerId(String idNumber) {
		List<ITAExaminerDO> itaExaminerDOList = null;
		
		StringBuffer queryString = new StringBuffer(" from ITAExaminerDO o where o.examinerId = :examinerId");
		
		itaExaminerDOList = hibernateTemplate.findByNamedParam(queryString.toString(), "examinerId", StringUtil.quoteReplace(idNumber));
		
		if(itaExaminerDOList!=null && !itaExaminerDOList.isEmpty()){
			return itaExaminerDOList.get(0);
		}
		
		return null;
	
	}

	

}
