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

import fsl.ta.toms.roms.bo.ReasonBO;
import fsl.ta.toms.roms.constants.Constants;
import fsl.ta.toms.roms.dao.ReasonDAO;
import fsl.ta.toms.roms.dataobjects.CourtDO;
import fsl.ta.toms.roms.dataobjects.ReasonDO;
import fsl.ta.toms.roms.dataobjects.ReasonTypeDO;
import fsl.ta.toms.roms.exception.InvalidFieldException;
import fsl.ta.toms.roms.search.criteria.impl.ReasonCriteriaBO;

public class ReasonDAOImpl extends ParentDAOImpl implements ReasonDAO{

	@SuppressWarnings("unchecked")
	@Override
	public List<ReasonDO> lookupReason(ReasonCriteriaBO reasonCriteriaBO) {
		
		List<ReasonDO> reasonList = new ArrayList<ReasonDO>();
		
		Criteria criterias = this.getSession().createCriteria(ReasonDO.class);
		criterias.createAlias("reasonType", "rt");
		
		//StringBuffer queryString = new StringBuffer("select new fsl.ta.toms.roms.bo.ReasonBO(o) from ReasonDO o");
		//queryString.append(" where (o.reasonId is not null)");
		
		if(reasonCriteriaBO.getReasonId() != null && reasonCriteriaBO.getReasonId()>0){
			criterias.add(Restrictions.eq("reasonId", reasonCriteriaBO.getReasonId()));
			//queryString.append(" and o.reasonId = ").append(reasonCriteriaBO.getReasonId());
		}
		
		if(StringUtils.trimToNull(reasonCriteriaBO.getReasonDescription()) != null){
			criterias.add(Restrictions.like("description",  "%" + reasonCriteriaBO.getReasonDescription().trim() + "%" ).ignoreCase());
			//queryString.append(" and upper(o.description) like '%").append(reasonCriteriaBO.getReasonDescription().trim().toUpperCase()).append("%'");
		}
		
		if(StringUtils.trimToNull(reasonCriteriaBO.getType()) != null){
			criterias.add(Restrictions.eq("rt.reasonTypeCode",reasonCriteriaBO.getType().trim()).ignoreCase());
			//queryString.append(" and upper(o.type) = '").append(reasonCriteriaBO.getType().trim()).append("'");
		}
		
		
		if(StringUtils.trimToNull(reasonCriteriaBO.getStatusId()) != null){
			criterias.add(Restrictions.eq("status.statusId",reasonCriteriaBO.getStatusId().trim()).ignoreCase());
			//queryString.append(" and o.status.statusId = '").append(reasonCriteriaBO.getStatusId()).append("'");
		}
		
		//Check if Reason is to be displayed on UI
		criterias.add(Restrictions.eq("isVisible",Constants.YesNo.YES_LABEL_STR));
		
		//queryString.append(" order by o.reasonId");
		
		//reasonList =  hibernateTemplate.find(queryString.toString());
		 
		criterias.addOrder(Order.asc("description").ignoreCase());					
		reasonList = criterias.list();
			
		if(reasonList==null || reasonList.size()<1){
			return null;
		}
		
		for(ReasonDO reasonDO: reasonList){
			if(reasonDO.getAuditEntry().getUpdateUsername() != null)
				reasonDO.getAuditEntry().setUpdateUsername(usernameToFullName(reasonDO.getAuditEntry().getUpdateUsername()));
			else
				reasonDO.getAuditEntry().setUpdateUsername(usernameToFullName(reasonDO.getAuditEntry().getCreateUsername()));
		}
		
		return reasonList;
	}

	@Override
	public Integer saveReasonDO(ReasonDO reasonDO) {
		Integer reasonId = (Integer) (hibernateTemplate.save(reasonDO));
		
		return reasonId;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ReasonDO> getReasonReference(
			HashMap<String, String> filter, String status) throws InvalidFieldException 
	{
		List<ReasonDO> reasons =  (List<ReasonDO>)super.getReference(ReasonDO.class, status, filter);
		
		
		Collections.sort(reasons, new Comparator<ReasonDO>() {
		    public int compare(ReasonDO result1, ReasonDO result2) {
		        return result1.getDescription().compareTo(result2.getDescription());
		    }
		});
		return reasons;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ReasonTypeDO> getReasonTypeReference(
			HashMap<String, String> filter, String status) throws InvalidFieldException 
	{
		List<ReasonTypeDO> reasons =  (List<ReasonTypeDO>)super.getReference(ReasonTypeDO.class, status, filter);
		
		
		Collections.sort(reasons, new Comparator<ReasonTypeDO>() {
		    public int compare(ReasonTypeDO result1, ReasonTypeDO result2) {
		        return result1.getDescription().compareTo(result2.getDescription());
		    }
		});
		return reasons;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean reasonDescExistForSelectedType(ReasonBO reasonBO) {
		List<ReasonDO> reasonBOList = new ArrayList<ReasonDO>();
		
	/*	StringBuffer queryString = new StringBuffer(" from ReasonDO o");
		queryString.append(" where upper(o.description) = '").append(reasonBO.getReasonDescription().toUpperCase()).append("'");
		queryString.append(" and o.type = '").append(reasonBO.getType()).append("'");
		*/
		
		Criteria criterias = this.getSession().createCriteria(ReasonDO.class);
		criterias.createAlias("reasonType", "rt");
		
		criterias.add(Restrictions.eq("description",reasonBO.getReasonDescription().trim()).ignoreCase());
		criterias.add(Restrictions.eq("rt.reasonTypeCode",reasonBO.getType().trim()).ignoreCase());
		
		if(reasonBO.getReasonId() != null && reasonBO.getReasonId()>0){
			criterias.add(Restrictions.ne("reasonId", reasonBO.getReasonId()));
			//queryString.append(" and o.reasonId <> ").append(reasonBO.getReasonId());
		}
		
		//reasonBOList =  hibernateTemplate.find(queryString.toString());
		 
		reasonBOList = criterias.list(); 
		if(reasonBOList==null || reasonBOList.size()<1){
			return false;
		}
		
		return true;
	}

}
