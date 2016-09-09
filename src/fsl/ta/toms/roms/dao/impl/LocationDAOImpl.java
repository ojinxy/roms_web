/**
 * Created By: oanguin
 * Date: Apr 19, 2013
 *
 */
package fsl.ta.toms.roms.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import fsl.dao.SpringHibernateDAOSupport;
import fsl.ta.toms.roms.bo.LocationBO;
import fsl.ta.toms.roms.dao.LocationDAO;
import fsl.ta.toms.roms.dataobjects.CourtDO;
import fsl.ta.toms.roms.dataobjects.LocationDO;
import fsl.ta.toms.roms.exception.InvalidFieldException;
import fsl.ta.toms.roms.search.criteria.impl.LocationCriteriaBO;

/**
 * @author oanguin Created Date: Apr 19, 2013
 */
public class LocationDAOImpl extends ParentDAOImpl implements
		SpringHibernateDAOSupport, LocationDAO {

	@SuppressWarnings("unchecked")
	@Override
	public List<LocationDO> getLocationReference(
			HashMap<String, String> filter, String status)
			throws InvalidFieldException {
		return (List<LocationDO>) super.getReference(LocationDO.class, status,
				filter);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<LocationDO> lookupLocations(
			LocationCriteriaBO locationCriteriaBO) {

		List<LocationDO> locationDOs = new ArrayList<LocationDO>();

		Criteria criteriaS = this.getSession().createCriteria(LocationDO.class);

		if (locationCriteriaBO.getLocationId() != null && !(locationCriteriaBO.getLocationId()<1) ) {
			criteriaS.add(Restrictions.eq("locationId",
					locationCriteriaBO.getLocationId()));
		}
		

		if (StringUtils.isNotBlank(locationCriteriaBO.getLocationDescription())) {
			criteriaS.add(Restrictions.like("description",
					"%" + locationCriteriaBO.getLocationDescription() + "%")
					.ignoreCase());
		}

		if (StringUtils.isNotBlank(locationCriteriaBO.getShortDesc())) {
			criteriaS
					.add(Restrictions.like("shortDesc",
							"%" + locationCriteriaBO.getShortDesc() + "%")
							.ignoreCase());
		}

		if (StringUtils.isNotBlank(locationCriteriaBO.getParishCode())) {
			criteriaS.add(Restrictions.eq("parish.parishCode",
					locationCriteriaBO.getParishCode()));
		}
		

		if (StringUtils.isNotBlank(locationCriteriaBO.getStatusId())) {
			criteriaS.add(Restrictions.eq("status.statusId",
					locationCriteriaBO.getStatusId()));
		}
		

		// add the ordering
		criteriaS.addOrder(Order.asc("shortDesc"));

		locationDOs = criteriaS.list();

		if (locationDOs == null || locationDOs.size() < 1) {
			return null;
		}
		
		for(LocationDO locationDO: locationDOs){
			if(locationDO.getAuditEntry().getUpdateUsername() != null)
				locationDO.getAuditEntry().setUpdateUsername(usernameToFullName(locationDO.getAuditEntry().getUpdateUsername()));
			else
				locationDO.getAuditEntry().setUpdateUsername(usernameToFullName(locationDO.getAuditEntry().getCreateUsername()));
					
					
		}

		return locationDOs;

	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean locationExists(Integer locationId) {

		List<LocationDO> locationDOs = new ArrayList<LocationDO>();

		Criteria criteriaS = this.getSession().createCriteria(LocationDO.class);

		criteriaS.add(Restrictions.eq("locationId", locationId));

		locationDOs = criteriaS.list();
		if (locationDOs == null || locationDOs.size() == 0) {
			return false;
		}

		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean locationDescriptionExists(LocationBO location) {

		List<LocationDO> locationDOs = new ArrayList<LocationDO>();

		Criteria criteriaS = this.getSession().createCriteria(LocationDO.class);

		criteriaS.add(Restrictions.eq("description", location.getParishDescription()).ignoreCase());
		
		if (location.getLocationId() != null && !(location.getLocationId()<1)) {
			criteriaS.add(Restrictions.ne("locationId",
					location.getLocationId()));
		}

		locationDOs = criteriaS.list();
		if (locationDOs == null || locationDOs.size() == 0) {
			return false;
		}

		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean locationShortDescriptionExists(LocationBO location) {

		List<LocationDO> locationDOs = new ArrayList<LocationDO>();

		Criteria criteriaS = this.getSession().createCriteria(LocationDO.class);

		criteriaS.add(Restrictions.eq("shortDesc", location.getShortDesc()).ignoreCase());
		
		criteriaS.add(Restrictions.eq("parish.parishCode", location.getParishCode()).ignoreCase());
		
		//System.err.println(" criteria: "+criteriaS.toString());
		
		if (location.getLocationId() != null) {
			criteriaS.add(Restrictions.ne("locationId",
					location.getLocationId()));
		}

		locationDOs = criteriaS.list();
		if (locationDOs == null || locationDOs.size() == 0) {
			return false;
		}

		return true;
	}

}
