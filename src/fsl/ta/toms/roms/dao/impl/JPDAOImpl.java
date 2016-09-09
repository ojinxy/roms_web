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

import fsl.ta.toms.roms.bo.JPBO;
import fsl.ta.toms.roms.constants.Constants.Status;
import fsl.ta.toms.roms.dao.JPDAO;
import fsl.ta.toms.roms.dataobjects.JPDO;
import fsl.ta.toms.roms.exception.InvalidFieldException;
import fsl.ta.toms.roms.search.criteria.impl.JPCriteriaBO;
import fsl.ta.toms.roms.util.StringUtil;

/**
 * @author oanguin
 * Created Date: Apr 20, 2013
 */
public class JPDAOImpl extends ParentDAOImpl implements
		JPDAO {

		

	/* (non-Javadoc)
	 * @see fsl.ta.toms.roms.dao.JPDAO#getTAStaffReference(java.util.List, java.util.HashMap, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<JPDO> getJPReference(
			HashMap<String, String> filter, String status) throws InvalidFieldException 
	{
		return (List<JPDO>)super.getReference(JPDO.class, status, filter);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<JPBO> lookupJP(JPCriteriaBO jpCriteriaBO) {
		List<JPBO> jpList = new ArrayList<JPBO>();
		//System.err.println("JP: "+jpCriteriaBO);
		StringBuffer queryString = new StringBuffer("select new fsl.ta.toms.roms.bo.JPBO(o) from JPDO o");
		queryString.append(" where (o.regNumber is not null)");
		
		if(StringUtils.trimToNull(jpCriteriaBO.getRegNumber()) != null){
			queryString.append(" and upper(o.regNumber) = '").append(StringUtil.quoteReplace(jpCriteriaBO.getRegNumber().trim()).toUpperCase()).append("'");
		}
		
		if(StringUtils.trimToNull(jpCriteriaBO.getTrnNbr()) != null){
			queryString.append(" and o.person.trnNbr = '").append(StringUtil.quoteReplace(jpCriteriaBO.getTrnNbr().trim())).append("'");
		}
		
		if(StringUtils.trimToNull(jpCriteriaBO.getFirstName()) != null){
			queryString.append(" and upper(o.person.firstName) like '%").append(StringUtil.quoteReplace(jpCriteriaBO.getFirstName().trim()).toUpperCase()).append("%'");
		}
		
		if(StringUtils.trimToNull(jpCriteriaBO.getLastName()) != null){
			queryString.append(" and upper(o.person.lastName) like '%").append(StringUtil.quoteReplace(jpCriteriaBO.getLastName().trim()).toUpperCase()).append("%'");
		}
		
		if(StringUtils.trimToNull(jpCriteriaBO.getMiddleName()) != null){
			queryString.append(" and upper(o.person.middleName) like '%").append(StringUtil.quoteReplace(jpCriteriaBO.getMiddleName().trim()).toUpperCase()).append("%'");
		}
		
		if(StringUtils.trimToNull(jpCriteriaBO.getParishCode()) != null){
			queryString.append(" and o.parish.parishCode = '").append(StringUtil.quoteReplace(jpCriteriaBO.getParishCode())).append("'");
		}
		
		if(StringUtils.trimToNull(jpCriteriaBO.getStatusId()) != null)
		{
			queryString.append(" and upper(o.status.statusId) = '").append(StringUtil.quoteReplace(jpCriteriaBO.getStatusId().trim()).toUpperCase()).append("'");
		}
		
		if(jpCriteriaBO.getRegionCodes() != null && !jpCriteriaBO.getRegionCodes().isEmpty())
		{
			queryString.append(" and o.parish.officeLocationCode in (");
			
			boolean first = true;
			for(String regionCode : jpCriteriaBO.getRegionCodes())
			{
				if(first)
				{
					queryString.append("'" + regionCode + "'");
					first = false;
				}
				else
					queryString.append(",'" + regionCode + "'");
			}
			
			queryString.append(")");
		}
		
		
		queryString.append(" order by o.person.lastName, o.person.firstName");
		
		//System.err.println("Query Str: "+queryString);
		
		jpList =  hibernateTemplate.find(queryString.toString());
		 
		if(jpList==null || jpList.size()<1){
			return null;
		}
		
		for(JPBO jpBO: jpList){
			if(jpBO.getAuditEntryBO().getUpdateUsername() != null)
				jpBO.getAuditEntryBO().setUpdateUsername(usernameToFullName(jpBO.getAuditEntryBO().getUpdateUsername()));
			else
				jpBO.getAuditEntryBO().setUpdateUsername(usernameToFullName(jpBO.getAuditEntryBO().getCreateUsername()));
					
		}
		
		return jpList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean jpExistWithTRN(String trnNbr) {
		List<JPDO> jpdo = new ArrayList<JPDO>();
		
		StringBuffer queryString = new StringBuffer(" from JPDO o where o.person.trnNbr = :trnNbr");
		
		jpdo = hibernateTemplate.findByNamedParam(queryString.toString(), "trnNbr", StringUtil.quoteReplace(trnNbr));
		
		if(jpdo==null || jpdo.size()==0){
			return false;
		}
		
		return true;
	}
	
	@Override
	public List<JPBO> getJPListByRegion(String regionCode) {
		List<JPBO> jpdo = new ArrayList<JPBO>();
		
		//System.err.println("getJPList");
		StringBuffer queryString = new StringBuffer(" select new fsl.ta.toms.roms.bo.JPBO(o) from JPDO o " +
				"where o.parish.parishCode IN (Select parishCode from ParishDO where officeLocationCode = :regionCode) " +
				"and o.status.statusId = '" + Status.ACTIVE + "'");
		
		jpdo = hibernateTemplate.findByNamedParam(queryString.toString(), "regionCode", regionCode);
		
		if(jpdo==null || jpdo.size()==0){
			return null;
		}
		
		return jpdo;
	}

	@Override
	public JPDO findByRegNumber(String regNumber) {
		
		StringBuffer queryString = new StringBuffer(" from JPDO o where o.regNumber = :regNbr");
		
		List<JPDO> jpdo = hibernateTemplate.findByNamedParam(queryString.toString(), "regNbr", StringUtil.quoteReplace(regNumber));
		
		if(jpdo!=null && !jpdo.isEmpty()){
			return jpdo.get(0);
		}
		
		return null;

	}

}
