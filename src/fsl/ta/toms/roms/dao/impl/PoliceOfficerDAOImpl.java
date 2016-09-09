/**
 * Created By: oanguin
 * Date: Apr 20, 2013
 *
 */
package fsl.ta.toms.roms.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import fsl.dao.SpringHibernateDAOSupport;
import fsl.ta.toms.roms.dao.PoliceOfficerDAO;
import fsl.ta.toms.roms.dataobjects.PoliceOfficerDO;
import fsl.ta.toms.roms.exception.InvalidFieldException;

/**
 * @author oanguin
 * Created Date: Apr 20, 2013
 */
public class PoliceOfficerDAOImpl extends ParentDAOImpl implements
		PoliceOfficerDAO, SpringHibernateDAOSupport {

	/* (non-Javadoc)
	 * @see fsl.ta.toms.roms.dao.PoliceOfficerDAO#getPoliceOfficerReference(java.util.List, java.util.HashMap, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<PoliceOfficerDO> getPoliceOfficerReference(
			 HashMap<String, String> filter, String status) throws InvalidFieldException 
	{
		return (List<PoliceOfficerDO>)super.getReference(PoliceOfficerDO.class, status, filter);
	}

	@Override
	public List<String> getUniquePoliceStations()
	{
		Criteria criteria = this.getSession().createCriteria(PoliceOfficerDO.class,"police");
		
		criteria.setProjection(Projections.distinct(Projections.property("stationDescription")));
		
		return criteria.list();
	}

	@Override
	public PoliceOfficerDO getPoliceByPersonId(Integer personId)
	{
		if(personId != null && personId > 0){
			Criteria criteria = this.getSession().getSessionFactory().getCurrentSession().createCriteria(PoliceOfficerDO.class,"police");
			
			criteria.createAlias("police.person", "person");
			
			criteria.add(Restrictions.eq("person.personId", personId));
			
			return (PoliceOfficerDO) criteria.uniqueResult();
		}
		else{
			return null;
		}
	}

}
