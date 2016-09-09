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

import fsl.ta.toms.roms.bo.PoundBO;
import fsl.ta.toms.roms.dao.PoundDAO;
import fsl.ta.toms.roms.dataobjects.CourtDO;
import fsl.ta.toms.roms.dataobjects.PoundDO;
import fsl.ta.toms.roms.exception.InvalidFieldException;
import fsl.ta.toms.roms.search.criteria.impl.PoundCriteriaBO;

/**
 * @author oanguin Created Date: Apr 22, 2013
 */
public class PoundDAOImpl extends ParentDAOImpl implements PoundDAO {

	/*
	 * (non-Javadoc)
	 * 
	 * @see fsl.ta.toms.roms.dao.PoundDAO#getPoundReference(java.util.List,
	 * java.util.HashMap, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<PoundDO> getPoundReference(HashMap<String, String> filter,
			String status) throws InvalidFieldException {
		
		List<PoundDO> poundList =  (List<PoundDO>) super.getReference(PoundDO.class, status, filter);
		//return (List<PoundDO>) super.getReference(PoundDO.class, status, filter);
		
		Collections.sort(poundList, new Comparator<PoundDO>() {
		    public int compare(PoundDO result1, PoundDO result2) {
		        return result1.getPoundName().toLowerCase().compareTo(result2.getPoundName().toLowerCase());
		    }
		});
		
		return poundList;
	}

	@SuppressWarnings("unchecked")
	@Override
	/**
	 * @author jreid
	 */
	public List<PoundDO> lookupPounds(PoundCriteriaBO poundCriteriaBO) {

		List<PoundDO> poundList = new ArrayList<PoundDO>();
		Criteria criteria = this.getSession().createCriteria(PoundDO.class);

		if (poundCriteriaBO.getPoundId() != null) {
			criteria.add(Restrictions.eq("poundId",
					poundCriteriaBO.getPoundId()));
		}

		if (StringUtils.isNotBlank(poundCriteriaBO.getPoundName())) {
			criteria.add(Restrictions.like("poundName",
					"%" + poundCriteriaBO.getPoundName() + "%").ignoreCase());
		}

		if (StringUtils.isNotBlank(poundCriteriaBO.getParishCode())) {
			criteria.add(Restrictions.eq("address.parish.parishCode",
					poundCriteriaBO.getParishCode()));
		}

		if (StringUtils.isNotBlank(poundCriteriaBO.getShortDesc())) {
			criteria.add(Restrictions.like("shortDesc",
					"%" + poundCriteriaBO.getShortDesc() + "%").ignoreCase());
		}

		if (StringUtils.isNotBlank(poundCriteriaBO.getStatusId())) {
			criteria.add(Restrictions.eq("status.statusId",
					poundCriteriaBO.getStatusId()));
		}

		if (poundCriteriaBO.getNumberOfLots() != null) {
			criteria.add(Restrictions.eq("numberOfLots",
					poundCriteriaBO.getNumberOfLots()));
		}

		if (poundCriteriaBO.getNumberOfParkingSpaces() != null) {
			criteria.add(Restrictions.eq("numberOfParkingSpaces",
					poundCriteriaBO.getNumberOfParkingSpaces()));
		}

		// add the ordering
		criteria.addOrder(Order.asc("poundName"));

		poundList = criteria.list();

		if (poundList == null || poundList.size() < 1) {
			return null;
		}
		
		for(PoundDO poundDO: poundList){
			if(poundDO.getAuditEntry().getUpdateUsername() != null)
				poundDO.getAuditEntry().setUpdateUsername(usernameToFullName(poundDO.getAuditEntry().getUpdateUsername()));
			else
				poundDO.getAuditEntry().setUpdateUsername(usernameToFullName(poundDO.getAuditEntry().getCreateUsername()));
				
					
		}

		return poundList;

	}

	/**
	 * @author jreid
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean poundExists(Integer poundId) {
		List<PoundDO> pounds = new ArrayList<PoundDO>();

		Criteria criteria = this.getSession().createCriteria(PoundDO.class);

		criteria.add(Restrictions.eq("poundId", poundId));

		pounds = criteria.list();

		if (pounds == null || pounds.size() == 0) {
			return false;
		}

		return true;
	}

	/**
	 * @author jreid
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean poundShortDescriptionExists(PoundBO pound) {
		List<PoundDO> pounds = new ArrayList<PoundDO>();

		Criteria criteria = this.getSession().createCriteria(PoundDO.class);

		criteria.add(Restrictions.eq("shortDesc", pound.getShortDesc())
				.ignoreCase());
		
		criteria.add(Restrictions.eq("poundName", pound.getPoundName())
				.ignoreCase());
		
		//criteria.add(Restrictions.conjunction().)

		if (pound.getPoundId() != null) {
			criteria.add(Restrictions.ne("poundId", pound.getPoundId()));
		}

		pounds = criteria.list();

		if (pounds == null || pounds.size() == 0) {
			return false;
		}

		return true;
	}

	/**
	 * @author jreid
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean poundNameExists(PoundBO pound) {
		List<PoundDO> pounds = new ArrayList<PoundDO>();

		Criteria criteria = this.getSession().createCriteria(PoundDO.class);

		criteria.add(Restrictions.eq("poundName", pound.getPoundName()).ignoreCase());
		
		if (pound.getPoundId() != null) {
			criteria.add(Restrictions.ne("poundId",
					pound.getPoundId()));
		}

		pounds = criteria.list();

		if (pounds == null || pounds.size() == 0) {
			return false;
		}

		return true;
	}
}
