/**
 * Created By: oanguin
 * Date: Apr 20, 2013
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

import fsl.ta.toms.roms.bo.CourtBO;
import fsl.ta.toms.roms.dao.CourtDAO;
import fsl.ta.toms.roms.dataobjects.ArteryDO;
import fsl.ta.toms.roms.dataobjects.CourtDO;
import fsl.ta.toms.roms.exception.InvalidFieldException;
import fsl.ta.toms.roms.search.criteria.impl.CourtCriteriaBO;

/**
 * @author oanguin Created Date: Apr 20, 2013
 */
public class CourtDAOImpl extends ParentDAOImpl implements CourtDAO {

	/*
	 * (non-Javadoc)
	 * 
	 * @see fsl.ta.toms.roms.dao.CourtDAO#getCourtReference(java.util.List,
	 * java.util.HashMap, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<CourtDO> getCourtReference(HashMap<String, String> filter,
			String status) throws InvalidFieldException 
	{
		List<CourtDO> courtList = (List<CourtDO>)super.getReference(CourtDO.class, status, filter);
		//return (List<CourtDO>)super.getReference(CourtDO.class, status, filter);
		
		Collections.sort(courtList, new Comparator<CourtDO>() {
		    public int compare(CourtDO result1, CourtDO result2) {
		        return result1.getDescription().toLowerCase().compareTo(result2.getDescription().toLowerCase());
		    }
		});
		
		return courtList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CourtDO> lookupCourt(CourtCriteriaBO courtCriteriaBO) {

		List<CourtDO> courtList = new ArrayList<CourtDO>();

		Criteria criteriaS = this.getSession().createCriteria(CourtDO.class);

		if (courtCriteriaBO.getCourtId() != null) {
			criteriaS.add(Restrictions.eq("courtId",
					courtCriteriaBO.getCourtId()));
		}
		
		
		if (StringUtils.isNotBlank(courtCriteriaBO.getParishCode()))
		{
			criteriaS.add(Restrictions.eq("address.parish.parishCode",
					courtCriteriaBO.getParishCode()));
		}
		
		if (StringUtils.isNotBlank(courtCriteriaBO.getShortDesc())) {
			criteriaS.add(Restrictions.like("shortDesc",
					"%" +courtCriteriaBO.getShortDesc() + "%" ).ignoreCase());
		}
		

		if (StringUtils.isNotBlank(courtCriteriaBO.getDescription())) {
			criteriaS.add(Restrictions.like("description",
					"%" + courtCriteriaBO.getDescription()+ "%" ).ignoreCase());
		}

		if (StringUtils.isNotBlank(courtCriteriaBO.getStatusId())) {
			criteriaS.add(Restrictions.eq("status.statusId",
					courtCriteriaBO.getStatusId()));
		}

		// add the ordering
		criteriaS.addOrder(Order.asc("shortDesc").ignoreCase());

		courtList = criteriaS.list();

		if (courtList == null || courtList.size() < 1) {
			return null;
		}
		
		for(CourtDO courtDO: courtList){
			if(courtDO.getAuditEntry().getUpdateUsername() != null)
				courtDO.getAuditEntry().setUpdateUsername(usernameToFullName(courtDO.getAuditEntry().getUpdateUsername()));
			else
				courtDO.getAuditEntry().setUpdateUsername(usernameToFullName(courtDO.getAuditEntry().getCreateUsername()));
					
		}

		return courtList;

	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean courtExists(Integer courtId) {
		List<CourtDO> courtDO = new ArrayList<CourtDO>();

		Criteria criteriaS = this.getSession().createCriteria(CourtDO.class);
		criteriaS.add(Restrictions.ne("courtId", courtId));

		courtDO = criteriaS.list();

		if (courtDO == null || courtDO.size() == 0) {
			return false;
		}

		return true;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean courtShortDescExists(CourtBO court) {
		List<CourtDO> courtDO = new ArrayList<CourtDO>();

		Criteria criteriaS = this.getSession().createCriteria(CourtDO.class);
		criteriaS.add(Restrictions.eq("shortDesc", court.getShortDescription()).ignoreCase());
		
		if (court.getCourtId() != null) {
			criteriaS.add(Restrictions.ne("courtId",
					court.getCourtId()));
		}

		courtDO = criteriaS.list();

		if (courtDO == null || courtDO.size() == 0) {
			return false;
		}

		return true;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean courtDescriptionExists(CourtBO court) {
		List<CourtDO> courtDO = new ArrayList<CourtDO>();

		Criteria criteriaS = this.getSession().createCriteria(CourtDO.class);
		criteriaS.add(Restrictions.eq("description", court.getDescription()).ignoreCase());
		
		if (court.getCourtId() != null) {
			criteriaS.add(Restrictions.eq("courtId",
					court.getCourtId()));
		}

		courtDO = criteriaS.list();

		if (courtDO == null || courtDO.size() == 0) {
			return false;
		}

		return true;
	}

}
