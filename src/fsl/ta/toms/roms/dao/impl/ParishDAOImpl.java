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

import fsl.ta.toms.roms.bo.ParishBO;
import fsl.ta.toms.roms.dao.ParishDAO;
import fsl.ta.toms.roms.dataobjects.CourtDO;
import fsl.ta.toms.roms.dataobjects.ParishDO;
import fsl.ta.toms.roms.exception.InvalidFieldException;
import fsl.ta.toms.roms.search.criteria.impl.ParishCriteriaBO;

/**
 * @author oanguin Created Date: Apr 22, 2013
 */
public class ParishDAOImpl extends ParentDAOImpl implements ParishDAO {

	/*
	 * (non-Javadoc)
	 * 
	 * @see fsl.ta.toms.roms.dao.ParishDAO#getParishReference(java.util.List,
	 * java.util.HashMap, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ParishDO> getParishReference(HashMap<String, String> filter,
			String status) throws InvalidFieldException 
	{
		List<ParishDO> parishes = (List<ParishDO>) super.getReference(ParishDO.class, status, filter);
		
		
		Collections.sort(parishes, new Comparator<ParishDO>() {
		    public int compare(ParishDO result1, ParishDO result2) {
		        return result1.getDescription().toLowerCase().compareTo(result2.getDescription().toLowerCase());
		    }
		});
		
		return parishes;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ParishDO> lookupParish(ParishCriteriaBO parishCriteria) {

		List<ParishDO> parishDOs = new ArrayList<ParishDO>();

		Criteria criteria = this.getSession().createCriteria(ParishDO.class);

		if (StringUtils.isNotBlank(parishCriteria.getParishCode())) {
			criteria.add(Restrictions.eq("parishCode",
					parishCriteria.getParishCode()).ignoreCase());
		}

		if (StringUtils.isNotBlank(parishCriteria.getDescription())) {
			criteria.add(Restrictions.like("description",
					"%" +parishCriteria.getDescription() + "%" ).ignoreCase());
		}

		// region
		//if (StringUtils.isNotBlank(parishCriteria.getOfficeLocationCode())) {
		if (parishCriteria.getOfficeLocationCode() != null) {
			if (parishCriteria.getOfficeLocationCode().size() >0)
			{
				criteria.add(Restrictions.in("officeLocationCode",
					parishCriteria.getOfficeLocationCode()));
			}
		}

		if (StringUtils.isNotBlank(parishCriteria.getStatusId())) {
			criteria.add(Restrictions.eq("status.statusId",
					parishCriteria.getStatusId()));
		}

		// add the ordering
		criteria.addOrder(Order.asc("description"));

		parishDOs = criteria.list();

		if (parishDOs == null || parishDOs.size() < 1) {
			return null;
		}
		
		for(ParishDO parishDO: parishDOs){
			if(parishDO.getAuditEntry().getUpdateUsername() != null)
				parishDO.getAuditEntry().setUpdateUsername(usernameToFullName(parishDO.getAuditEntry().getUpdateUsername()));
			else
				parishDO.getAuditEntry().setUpdateUsername(usernameToFullName(parishDO.getAuditEntry().getCreateUsername()));
				
					
		}

		return parishDOs;

	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean parishExist(String parishCode) {
		List<ParishDO> parishDOs = new ArrayList<ParishDO>();

		Criteria criteria = this.getSession().createCriteria(ParishDO.class);

		criteria.add(Restrictions.eq("parishCode", parishCode).ignoreCase());

		parishDOs =  criteria.list();
		
		if (parishDOs == null || parishDOs.size() == 0) {
			return false;
		}

		return true;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean parishDescriptionExist(ParishBO parish) {
		List<ParishDO> parishDOs = new ArrayList<ParishDO>();

		Criteria criteria = this.getSession().createCriteria(ParishDO.class);

		criteria.add(Restrictions.eq("description", parish.getDescription()).ignoreCase());
		
		if (StringUtils.isNotBlank(parish.getParishCode())) {
			criteria.add(Restrictions.ne("parishCode",
					parish.getParishCode()).ignoreCase());
		}

		parishDOs =  criteria.list();
		
		if (parishDOs == null || parishDOs.size() == 0) {
			return false;
		}

		return true;
	}

}
