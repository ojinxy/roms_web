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
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import fsl.ta.toms.roms.bo.ArteryBO;
import fsl.ta.toms.roms.dao.ArteryDAO;
import fsl.ta.toms.roms.dataobjects.ArteryDO;
import fsl.ta.toms.roms.exception.InvalidFieldException;
import fsl.ta.toms.roms.search.criteria.impl.ArteryCriteriaBO;

/**
 * @author oanguin Created Date: Apr 19, 2013
 */
public class ArteryDAOImpl extends ParentDAOImpl implements ArteryDAO {

	/*
	 * (non-Javadoc)
	 * 
	 * @see fsl.ta.toms.roms.dao.ArteryDAO#getCategoryReference(java.util.List,
	 * java.util.HashMap, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ArteryDO> getArteryReference(HashMap<String, String> filter,
			String status) throws InvalidFieldException {

		return (List<ArteryDO>)super.getReference(ArteryDO.class, status, filter);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ArteryDO> lookupArteryDO(ArteryCriteriaBO arteryCriteriaBO) {

		List<ArteryDO> arteryDOs = new ArrayList<ArteryDO>();
		
		
		
		Criteria criteriaS = this.getSession().createCriteria(ArteryDO.class);
		criteriaS.createAlias("location", "l");
		criteriaS.createAlias("location.parish", "lp");
		criteriaS.createAlias("status", "s");
		
		if (StringUtils.isNotBlank(arteryCriteriaBO.getArteryDescription())) {
			criteriaS.add(Restrictions.like("description", "%" + arteryCriteriaBO.getArteryDescription() + "%" ).ignoreCase());
		}

		if (arteryCriteriaBO.getArteryId() != null) {
			criteriaS.add(Restrictions.eq("arteryId", arteryCriteriaBO.getArteryId()));
		}

		if (StringUtils.isNotBlank(arteryCriteriaBO.getShortDescription())) {
			criteriaS.add(Restrictions.like("shortDescription", "%" +arteryCriteriaBO.getShortDescription() +"%" ).ignoreCase());
		}
		
		if (arteryCriteriaBO.getLocationId() != null && !(arteryCriteriaBO.getLocationId()<1) ) {
			criteriaS.add(Restrictions.eq("l.locationId", arteryCriteriaBO.getLocationId()));
		}
		
		if (StringUtils.isNotBlank(arteryCriteriaBO.getParishCode())) {
			criteriaS.add(Restrictions.eq("lp.parishCode", arteryCriteriaBO.getParishCode()));
		}
		
		
		if (StringUtils.isNotBlank(arteryCriteriaBO.getStatusId())) {
			criteriaS.add(Restrictions.eq("s.statusId",
					arteryCriteriaBO.getStatusId()));
		}
		
		
		//add the ordering 
		criteriaS.addOrder(Order.asc("shortDescription"));					
		
		
		arteryDOs = criteriaS.list();
				
		if (arteryDOs == null || arteryDOs.size() < 1) {
			return null;
		}
		
		for(ArteryDO arteryDO: arteryDOs){
			if(arteryDO.getAuditEntry().getUpdateUsername() != null)
				arteryDO.getAuditEntry().setUpdateUsername(usernameToFullName(arteryDO.getAuditEntry().getUpdateUsername()));
			else
				arteryDO.getAuditEntry().setUpdateUsername(usernameToFullName(arteryDO.getAuditEntry().getCreateUsername()));
		}

		return arteryDOs;

	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean arteryExists(Integer arteryId) {
		List<ArteryDO> arteryDOs = new ArrayList<ArteryDO>();

		Criteria criteriaS = this.getSession().createCriteria(ArteryDO.class);

		criteriaS.add(Restrictions.eq("arteryId", arteryId));

		arteryDOs = criteriaS.list();

		if (arteryDOs == null || arteryDOs.size() == 0) {
			return false;
		}

		return true;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean arteryDescriptionExists(ArteryBO artery) {
		List<ArteryDO> arteryDOs = new ArrayList<ArteryDO>();
		
		Criteria criteriaS = this.getSession().createCriteria(ArteryDO.class);
		//System.err.println("DAO imlpl Artery Description "+artery.getArteryDescription() + "short "+artery.getShortDescription() + "id " + artery.getArteryId() );
		criteriaS.add(Restrictions.eq("description", artery.getArteryDescription()).ignoreCase());
		
		if (artery.getArteryId() != null) {
			criteriaS.add(Restrictions.ne("arteryId", artery.getArteryId()));
		}

		arteryDOs = criteriaS.list();
		
		if (arteryDOs == null || arteryDOs.size() == 0) {
			return false;
		}

		return true;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean arteryShortDescriptionExists(ArteryBO artery) {
		List<ArteryDO> arteryDOs = new ArrayList<ArteryDO>();

		Criteria criteriaS = this.getSession().createCriteria(ArteryDO.class);
		criteriaS.createAlias("location", "l");
		criteriaS.createAlias("location.parish", "lp");
		
		
		criteriaS.add(Restrictions.eq("shortDescription", artery.getShortDescription()).ignoreCase());
		
		//criteriaS.add(Restrictions.eq("description", artery.getArteryDescription()));
		
		criteriaS.add(Restrictions.eq("l.locationId", artery.getLocationId()));
		
		
		
		if (artery.getArteryId() != null) {
			criteriaS.add(Restrictions.ne("arteryId", artery.getArteryId()));
		}

		arteryDOs = criteriaS.list();

		if (arteryDOs == null || arteryDOs.size() == 0) {
			return false;
		}

		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean arteryLocationExists(ArteryBO artery) {
		List<ArteryDO> arteryDOs = new ArrayList<ArteryDO>();

		Criteria criteriaS = this.getSession().createCriteria(ArteryDO.class);
		criteriaS.createAlias("location", "l");
		
		criteriaS.add(Restrictions.eq("l.locationId", artery.getLocationId()));
		
		if (artery.getArteryId() != null) {
			criteriaS.add(Restrictions.ne("arteryId", artery.getArteryId()));
		}

		arteryDOs = criteriaS.list();

		if (arteryDOs == null || arteryDOs.size() == 0) {
			return false;
		}

		return true;
	}

	@Override
	public String usernameToFullName(String username) {
		return  ((ParentDAOImpl)this).usernameToFullName(username);
		
	}

}
