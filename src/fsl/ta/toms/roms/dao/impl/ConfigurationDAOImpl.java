/**
 * Created By: oanguin
 * Date: Apr 22, 2013
 *
 */
package fsl.ta.toms.roms.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import fsl.ta.toms.roms.bo.ConfigurationBO;
import fsl.ta.toms.roms.dao.ConfigurationDAO;
import fsl.ta.toms.roms.dataobjects.ConfigurationDO;
import fsl.ta.toms.roms.exception.InvalidFieldException;
import fsl.ta.toms.roms.search.criteria.impl.ConfigurationCriteriaBO;

/**
 * @author oanguin
 * Created Date: Apr 22, 2013
 */
public class ConfigurationDAOImpl extends ParentDAOImpl implements
		ConfigurationDAO {

	

	/* (non-Javadoc)
	 * @see fsl.ta.toms.roms.dao.ConfigurationDAO#getConfigurationReference(java.util.List, java.util.HashMap, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ConfigurationDO> getConfigurationReference(
			 HashMap<String, String> filter, String status) throws InvalidFieldException 
	{
		return (List<ConfigurationDO>)super.getReference(ConfigurationDO.class, status, filter);
	}

	@Override
	public String getConfigurationValue(String cfg_code) 
	{
		ConfigurationDO configuration = (ConfigurationDO)this.hibernateTemplate.getSessionFactory().getCurrentSession().get(ConfigurationDO.class, cfg_code);
		
		return configuration.getValue();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ConfigurationDO> lookupConfiguration(
			ConfigurationCriteriaBO configurationCriteriaBO) {

		List<ConfigurationDO> configurationList = new ArrayList<ConfigurationDO>();
		
		Criteria criteriaS = this.getSession().createCriteria(ConfigurationDO.class);
		
		if(configurationCriteriaBO.getCfgCode() != null ){
			criteriaS.add(Restrictions.eq("cfgCode", configurationCriteriaBO.getCfgCode()).ignoreCase());
		}
		
		if(configurationCriteriaBO.getSelectable() != null ){
			criteriaS.add(Restrictions.eq("selectable", configurationCriteriaBO.getSelectable()));
	}
		
		if(configurationCriteriaBO.getDataType() != null ){
			criteriaS.add(Restrictions.eq("dataType", configurationCriteriaBO.getDataType()));
	}
		
		if(StringUtils.isNotBlank(configurationCriteriaBO.getValue())){
			criteriaS.add(Restrictions.like("value",  "%" + configurationCriteriaBO.getValue() + "%").ignoreCase());
		}
		
		if(StringUtils.isNotBlank(configurationCriteriaBO.getDescription())){
			criteriaS.add(Restrictions.like("description", "%" +configurationCriteriaBO.getDescription()+ "%" ).ignoreCase());		
		}
		
		if(StringUtils.isNotBlank(configurationCriteriaBO.getStatusId())){
			criteriaS.add(Restrictions.eq("status.statusId", configurationCriteriaBO.getStatusId()));
		}
		
		if(configurationCriteriaBO.getStartDate() != null  && configurationCriteriaBO.getEndDate() == null){
			criteriaS.add(Restrictions.eq("startDate", configurationCriteriaBO.getStartDate()));
		}
		
		if(configurationCriteriaBO.getStartDate() == null  && configurationCriteriaBO.getEndDate() != null){
			criteriaS.add(Restrictions.eq("endDate", configurationCriteriaBO.getEndDate()));
		}
		
		if(configurationCriteriaBO.getStartDate() != null  && configurationCriteriaBO.getEndDate() != null){
			criteriaS.add(Restrictions.or(Restrictions.between("startDate",configurationCriteriaBO.getStartDate() ,configurationCriteriaBO.getEndDate()),Restrictions.between("endDate", configurationCriteriaBO.getStartDate(), configurationCriteriaBO.getEndDate())));
		}
		
		//add the ordering 
		criteriaS.addOrder(Order.asc("description"));					
		
		configurationList =  criteriaS.list();
		 
		if(configurationList==null || configurationList.size()<1){
			return null;
		}
		
		return configurationList;
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean configurationExists(String configurationId) {
		List<ConfigurationDO> configurationDO = new ArrayList<ConfigurationDO>();
		
		Criteria criteriaS = this.getSession().createCriteria(ConfigurationDO.class);
		criteriaS.add(Restrictions.eq("cfgCode", configurationId).ignoreCase());

		configurationDO = criteriaS.list();
		
		if(configurationDO==null || configurationDO.size()==0){
			return false;
		}
		
		return true;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean configurationDescriptionExists(ConfigurationBO configuration) {
		
		List<ConfigurationDO> configurationDO = new ArrayList<ConfigurationDO>();
		
		Criteria criteriaS = this.getSession().createCriteria(ConfigurationDO.class);
		
		criteriaS.add(Restrictions.eq("description", configuration.getDescription()).ignoreCase());
		
		if(configuration.getCfgCode() != null ){
			criteriaS.add(Restrictions.ne("cfgCode", configuration.getCfgCode()).ignoreCase());
		}
		
		configurationDO = criteriaS.list();
		
		if(configurationDO==null || configurationDO.size()==0){
			return false;
		}
		
		return true;
	}
}
