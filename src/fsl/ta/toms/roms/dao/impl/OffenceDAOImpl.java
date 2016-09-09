/**
 * Created By: oanguin
 * Date: Apr 22, 2013
 *
 */
package fsl.ta.toms.roms.dao.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import fsl.ta.toms.roms.bo.OffenceLawBO;
import fsl.ta.toms.roms.bo.OffenceParamBO;
import fsl.ta.toms.roms.dao.OffenceDAO;
import fsl.ta.toms.roms.dataobjects.AssignedVehicleDO;
import fsl.ta.toms.roms.dataobjects.CourtDO;
import fsl.ta.toms.roms.dataobjects.OffenceDO;
import fsl.ta.toms.roms.dataobjects.OffenceLawDO;
import fsl.ta.toms.roms.dataobjects.OffenceParamDO;
import fsl.ta.toms.roms.exception.InvalidFieldException;
import fsl.ta.toms.roms.search.criteria.impl.OffenceCriteriaBO;

/**
 * @author oanguin
 * Created Date: Apr 22, 2013
 */
public class OffenceDAOImpl extends ParentDAOImpl implements OffenceDAO
		 {

	/* (non-Javadoc)
	 * @see fsl.ta.toms.roms.dao.OffenceDAO#getOffenceReference(java.util.List, java.util.HashMap, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<OffenceDO> getOffenceReference(
			HashMap<String, String> filter, String status) throws InvalidFieldException 
	{
		
		
		
		List<OffenceDO> offenceList =  (List<OffenceDO>) super.getReference(OffenceDO.class, status, filter);
		//return (List<PoundDO>) super.getReference(PoundDO.class, status, filter);
		
		Collections.sort(offenceList, new Comparator<OffenceDO>() {
		    public int compare(OffenceDO result1, OffenceDO result2) {
		        return result1.getShortDescription().toLowerCase().compareTo(result2.getShortDescription().toLowerCase());
		    }
		});
		
		return offenceList;
		//return (List<OffenceDO>)super.getReference(OffenceDO.class, status, filter);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<OffenceDO> lookupOffence(OffenceCriteriaBO offenceCriteriaBO) {
		
		List<OffenceDO> offenceList = new ArrayList<OffenceDO>();
		Integer maxOffenceId = 9999;
	//	StringBuffer queryString = new StringBuffer("select new fsl.ta.toms.roms.bo.OffenceBO(o) from OffenceDO o");
		//queryString.append(" where (o.offenceId is not null)");
		
		Criteria criterias = this.getSession().createCriteria(OffenceDO.class);

		

		
		if(offenceCriteriaBO.getOffenceId() != null && offenceCriteriaBO.getOffenceId()>0){
			criterias.add(Restrictions.eq("offenceId", offenceCriteriaBO.getOffenceId()));
			//queryString.append(" and o.offenceId = ").append(offenceCriteriaBO.getOffenceId());
		}
		
		if(StringUtils.trimToNull(offenceCriteriaBO.getOffenceDescription()) != null){
			criterias.add(Restrictions.like("description",  "%" + offenceCriteriaBO.getOffenceDescription().trim() + "%" ).ignoreCase());
			//queryString.append(" and upper(o.description) like '%").append(quoteReplace(offenceCriteriaBO.getOffenceDescription().trim()).toUpperCase()).append("%'");
		}
		
		if(StringUtils.trimToNull(offenceCriteriaBO.getShortDescription()) != null){
			criterias.add(Restrictions.like("shortDescription",  "%"  +offenceCriteriaBO.getShortDescription().trim() + "%" ).ignoreCase());
			
			//queryString.append(" and upper(o.shortDescription) like '%").append(offenceCriteriaBO.getShortDescription().trim().toUpperCase()).append("%'");
		}
		
		if(StringUtils.trimToNull(offenceCriteriaBO.getRoadCheckTypeId()) != null){
			criterias.add(Restrictions.eq("roadCheckType.roadCheckTypeId",offenceCriteriaBO.getRoadCheckTypeId().trim()).ignoreCase());
			//queryString.append(" and o.roadCheckType.roadCheckTypeId = '").append(offenceCriteriaBO.getRoadCheckTypeId().trim()).append("'");
		}
		
		if(StringUtils.trimToNull(offenceCriteriaBO.getStatusId()) != null){
			criterias.add(Restrictions.eq("status.statusId",offenceCriteriaBO.getStatusId()).ignoreCase());
			
			//queryString.append(" and o.status.statusId = \"").append(offenceCriteriaBO.getStatusId()).append("\"");
		}
		
		criterias.add(Restrictions.ne("offenceId", maxOffenceId));
		//queryString.append(" order by o.offenceId");
		
		//offenceList =  hibernateTemplate.find(queryString.toString());
		
		criterias.addOrder(Order.asc("offenceId"));					
		

		offenceList = criterias.list();
		 
		if(offenceList==null || offenceList.size()<1){
			return null;
		}
		
		for(OffenceDO offenceDO: offenceList){
			if(offenceDO.getAuditEntry().getUpdateUsername() != null)
				offenceDO.getAuditEntry().setUpdateUsername(usernameToFullName(offenceDO.getAuditEntry().getUpdateUsername()));
			else
				offenceDO.getAuditEntry().setUpdateUsername(usernameToFullName(offenceDO.getAuditEntry().getCreateUsername()));
				
					
		}
		
		return offenceList;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean offenceDescriptionExist(String description, Integer offenceId) {
		List<OffenceDO> offenceBOList = new ArrayList<OffenceDO>();
		
		Criteria criterias = this.getSession().createCriteria(OffenceDO.class);

		//StringBuffer queryString = new StringBuffer(" from OffenceDO o");
		//queryString.append(" where upper(o.description) = '").append(description.toUpperCase()).append("'");
		
		criterias.add(Restrictions.eq("description",description.trim()).ignoreCase());
		
		if(offenceId != null && offenceId>0){
			criterias.add(Restrictions.ne("offenceId", offenceId));
			//queryString.append(" and o.offenceId <> ").append(offenceId);
		}
		offenceBOList = criterias.list();
		//offenceBOList =  hibernateTemplate.find(queryString.toString());
		 
		if(offenceBOList==null || offenceBOList.size()<1){
			return false;
		}
		
		return true;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean offenceShortDescriptionExist(String shortDescription, Integer offenceId) {
		List<OffenceDO> offenceBOList = new ArrayList<OffenceDO>();
		
		Criteria criterias = this.getSession().createCriteria(OffenceDO.class);
	
		criterias.add(Restrictions.eq("shortDescription",shortDescription.trim()).ignoreCase());
		
		if(offenceId != null && offenceId>0){
			criterias.add(Restrictions.ne("offenceId", offenceId));
		}
		
		offenceBOList = criterias.list(); 
		if(offenceBOList==null || offenceBOList.size()<1){
			return false;
		}
		
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<OffenceParamBO> findOffenceParams(Integer offenceId) {
        List<OffenceParamBO> offenceParamBOList = new ArrayList<OffenceParamBO>();
		
		String queryString = "select new fsl.ta.toms.roms.bo.OffenceParamBO(o) from OffenceParamDO o where o.offenceParamKey.offence.offenceId = :offenceId ";
		 
	
		
		offenceParamBOList = hibernateTemplate.findByNamedParam(queryString, "offenceId", offenceId);
			
		if(offenceParamBOList==null || offenceParamBOList.size()<1){
			return null;
		}
		
		return offenceParamBOList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<OffenceLawBO> findOffenceLaws(Integer offenceId) {
		 List<OffenceLawBO> offenceLawBOList = new ArrayList<OffenceLawBO>();
			
			String queryString = "select new fsl.ta.toms.roms.bo.OffenceLawBO(o) from OffenceLawDO o where o.offenceLawKey.offence.offenceId = :offenceId ";
			 
		
			
			offenceLawBOList = hibernateTemplate.findByNamedParam(queryString, "offenceId", offenceId);
				
			if(offenceLawBOList==null || offenceLawBOList.size()<1){
				return null;
			}
			
			return offenceLawBOList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public OffenceParamDO findOffenceParamsById(Integer paramId,
			Integer offenceId) {
		List<OffenceParamDO> offenceParamDOList = new ArrayList<OffenceParamDO>();
		
		String queryString = "from OffenceParamDO o where o.offenceParamKey.offence.offenceId = :offenceId " +
				"and o.offenceParamKey.excerptParamMapping.paramMapId = :paramMapId";
		 
	
		String[] paramNames = {"offenceId", "paramMapId"};
		Object[] values = {offenceId, paramId};
		
		offenceParamDOList = hibernateTemplate.findByNamedParam(queryString, paramNames, values);
			
		if(offenceParamDOList!=null && offenceParamDOList.size()==1){
			return offenceParamDOList.get(0);
		}
		
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public OffenceLawDO findOffenceLawsById(Integer lawId,
			Integer offenceId) {
		List<OffenceLawDO> offenceLawDOList = new ArrayList<OffenceLawDO>();
		
		String queryString = "from OffenceLawDO o where o.offenceLawKey.offence.offenceId = :offenceId " +
				"and o.offenceLawKey.governingLaw.lawId = :lawId";
		 
		String[] paramNames = {"offenceId", "lawId"};
		Object[] values = {offenceId, lawId};
		
		offenceLawDOList = hibernateTemplate.findByNamedParam(queryString, paramNames, values);
			
		if(offenceLawDOList!=null && offenceLawDOList.size()==1){
			return offenceLawDOList.get(0);
		}
		
		return null;
	}
	
	
	

}
