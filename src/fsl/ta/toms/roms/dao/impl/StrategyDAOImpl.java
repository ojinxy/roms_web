package fsl.ta.toms.roms.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import fsl.ta.toms.roms.bo.StrategyRuleBO;
import fsl.ta.toms.roms.dao.StrategyDAO;
import fsl.ta.toms.roms.dataobjects.CourtDO;
import fsl.ta.toms.roms.dataobjects.OffenceDO;
import fsl.ta.toms.roms.dataobjects.StrategyDO;
import fsl.ta.toms.roms.dataobjects.StrategyRuleDO;
import fsl.ta.toms.roms.exception.InvalidFieldException;
import fsl.ta.toms.roms.search.criteria.impl.StrategyCriteriaBO;

public class StrategyDAOImpl extends ParentDAOImpl implements StrategyDAO {


	
	@SuppressWarnings("unchecked")
	@Override
	public List<StrategyDO> getStrategyReference(
			HashMap<String, String> filter, String status) throws InvalidFieldException 
	{
		
		return (List<StrategyDO>)super.getReference(StrategyDO.class, status, filter);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<StrategyDO> lookupStrategy(StrategyCriteriaBO strategyCriteriaBO) {
		List<StrategyDO> strategyList = new ArrayList<StrategyDO>();
		
	
		//StringBuffer queryString = new StringBuffer("select new fsl.ta.toms.roms.bo.StrategyBO(o) from StrategyDO o");
		//queryString.append(" where (o.strategyId is not null)");
		
		Criteria criterias = this.getSession().createCriteria(StrategyDO.class);

		
		
		if(strategyCriteriaBO.getStrategyId() != null && strategyCriteriaBO.getStrategyId()>0){
			criterias.add(Restrictions.eq("strategyId", strategyCriteriaBO.getStrategyId()));
			//queryString.append(" and o.strategyId = ").append(strategyCriteriaBO.getStrategyId());
		}
		
		if(StringUtils.trimToNull(strategyCriteriaBO.getStrategyDescription()) != null){
			criterias.add(Restrictions.like("description",  "%" + strategyCriteriaBO.getStrategyDescription().trim() + "%" ).ignoreCase());
			//queryString.append(" and upper(o.description) like '%").append(strategyCriteriaBO.getStrategyDescription().trim().toUpperCase()).append("%'");
		}
		
				
		if(StringUtils.trimToNull(strategyCriteriaBO.getStatusId()) != null){
			criterias.add(Restrictions.eq("status.statusId",strategyCriteriaBO.getStatusId().trim()).ignoreCase());
			//queryString.append(" and o.status.statusId = '").append(strategyCriteriaBO.getStatusId()).append("'");
		}
		
		
		//queryString.append(" order by o.strategyId");
		
		//strategyList =  hibernateTemplate.find(queryString.toString());
		 
		criterias.addOrder(Order.asc("description"));					
		strategyList = criterias.list();
		
		if(strategyList==null || strategyList.size()<1){
			return null;
		}
		
		for(StrategyDO strategyDO: strategyList){
			if(strategyDO.getAuditEntry().getUpdateUsername() != null)
				strategyDO.getAuditEntry().setUpdateUsername(usernameToFullName(strategyDO.getAuditEntry().getUpdateUsername()));
			else
				strategyDO.getAuditEntry().setUpdateUsername(usernameToFullName(strategyDO.getAuditEntry().getCreateUsername()));
				
					
		}
		
		return strategyList;
	}

	@Override
	public Integer saveStrategyDO(StrategyDO strategyDO) {
		Integer strategyId = (Integer) (hibernateTemplate.save(strategyDO));
		
		return strategyId;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<StrategyRuleBO> getStrategyRulesForStrategy(Integer strategyId) {
		List<StrategyRuleBO> strategyRuleList = new ArrayList<StrategyRuleBO>();
		
		StringBuffer queryString = new StringBuffer(" select new fsl.ta.toms.roms.bo.StrategyRuleBO(o) " +
				"from StrategyRuleDO o where strategyRuleKey.strategy.strategyId = :strategyId " +
				"order by o.strategyRuleKey.personType.description");
		
		strategyRuleList = hibernateTemplate.findByNamedParam(queryString.toString(), "strategyId", strategyId);
		
		if(strategyRuleList==null || strategyRuleList.size()==0){
			return null;
		}
		
		return strategyRuleList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public StrategyRuleDO findStrategyRuleById(Integer strategyId,
			String personTypeId) {
		List<StrategyRuleDO> strategyRule = new ArrayList<StrategyRuleDO>();
		
		String queryString = " from StrategyRuleDO o where o.strategyRuleKey.strategy.strategyId = :strategyId " +
				" and o.strategyRuleKey.personType.personTypeId = :personTypeId";
		 
		
		String[] paramNames = {"strategyId", "personTypeId"};
		Object[] values = {strategyId, personTypeId};
		
		strategyRule = hibernateTemplate.findByNamedParam(queryString, paramNames, values);
			
		if(strategyRule!=null && strategyRule.size()==1){
			return strategyRule.get(0);
		}
		
		return null;
	}


	@SuppressWarnings("unchecked")
	@Override
	public boolean strategyDescriptionExist(String description, Integer strategyId) {
		List<StrategyDO> strategyBOList = new ArrayList<StrategyDO>();
		
		//StringBuffer queryString = new StringBuffer(" from StrategyDO o");
		//queryString.append(" where upper(o.description) = '").append(description.toUpperCase()).append("'");
		
		Criteria criterias = this.getSession().createCriteria(StrategyDO.class);
		
		criterias.add(Restrictions.eq("description",description.trim()).ignoreCase());
		
		
		if(strategyId != null && strategyId>0){
			criterias.add(Restrictions.ne("strategyId", strategyId));
			//queryString.append(" and o.strategyId <> ").append(strategyId);
		}
		
		//strategyBOList =  hibernateTemplate.find(queryString.toString());
		strategyBOList = criterias.list();
		if(strategyBOList==null || strategyBOList.size()<1){
			return false;
		}
		
		return true;
	}

}
