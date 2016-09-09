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

import fsl.ta.toms.roms.bo.GoverningLawBO;
import fsl.ta.toms.roms.dao.GoverningLawDAO;
import fsl.ta.toms.roms.dataobjects.GoverningLawDO;
import fsl.ta.toms.roms.exception.InvalidFieldException;
import fsl.ta.toms.roms.search.criteria.impl.GoverningLawCriteriaBO;

/**
 * @author oanguin
 * Created Date: Apr 22, 2013
 */
public class GoverningLawDAOImpl extends ParentDAOImpl implements
		GoverningLawDAO {

	
	/* (non-Javadoc)
	 * @see fsl.ta.toms.roms.dao.GoverningLawDAO#getGoverningLawReference(java.util.List, java.util.HashMap, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<GoverningLawDO> getGoverningLawReference(
			HashMap<String, String> filter, String status) throws InvalidFieldException 
	{
		return (List<GoverningLawDO>)super.getReference(GoverningLawDO.class, status, filter);
	}
	

	
	
/**
 * @author jreid
 */
	@SuppressWarnings("unchecked")
	@Override
	public List<GoverningLawDO> lookupGoverningLaw(
			GoverningLawCriteriaBO governingLawCriteriaBO) {

		List<GoverningLawDO> governingLawBOs = new ArrayList<GoverningLawDO>();
		
		Criteria criteriaS = this.getSession().createCriteria(GoverningLawDO.class);
		
			
		if(governingLawCriteriaBO.getLawId() != null){
			criteriaS.add(Restrictions.eq("lawId", governingLawCriteriaBO.getLawId()));
		}
		
		if(StringUtils.isNotBlank(governingLawCriteriaBO.getShortDesc())){
			criteriaS.add(Restrictions.like("shortDesc","%" + governingLawCriteriaBO.getShortDesc()+ "%" ).ignoreCase());
		}
		
		if(StringUtils.isNotBlank(governingLawCriteriaBO.getDescription())){
			criteriaS.add(Restrictions.like("description", "%" + governingLawCriteriaBO.getDescription()+ "%" ).ignoreCase());		
		}
		
		if(StringUtils.isNotBlank(governingLawCriteriaBO.getStatusId())){
			criteriaS.add(Restrictions.eq("status.statusId", governingLawCriteriaBO.getStatusId()));
		}
		
		//add the ordering 
		criteriaS.addOrder(Order.asc("description"));	
		
		governingLawBOs =  criteriaS.list();
		 
		if(governingLawBOs==null || governingLawBOs.size()<1){
			return null;
		}
		
		return governingLawBOs;
		
	}

	/**
	 * @author jreid
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean governingLawExists(Integer governingLawId) {
		List<GoverningLawDO> governingLaws = new ArrayList<GoverningLawDO>();
		
		Criteria criteriaS = this.getSession().createCriteria(GoverningLawDO.class);
		criteriaS.add(Restrictions.eq("lawId", governingLawId));

		governingLaws =  criteriaS.list();
		
		if(governingLaws==null || governingLaws.size()==0){
			return false;
		}
		
		return true;
	}
	
	/**
	 * @author jreid
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean governingLawDescriptionExists(GoverningLawBO govLaw) {
		List<GoverningLawDO> governingLaws = new ArrayList<GoverningLawDO>();
		
		Criteria criteriaS = this.getSession().createCriteria(GoverningLawDO.class);
		criteriaS.add(Restrictions.eq("description", govLaw.getDescription()).ignoreCase());
		
		if(govLaw.getLawId() != null){
			criteriaS.add(Restrictions.ne("lawId", govLaw.getLawId()));
		}

		governingLaws =  criteriaS.list();
		
		if(governingLaws==null || governingLaws.size()==0){
			return false;
		}
		
		return true;
	}
	
	/**
	 * @author jreid
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean governingLawShortDescriptionExists(GoverningLawBO govLaw) {
		List<GoverningLawDO> governingLaws = new ArrayList<GoverningLawDO>();
		
		Criteria criteriaS = this.getSession().createCriteria(GoverningLawDO.class);
		criteriaS.add(Restrictions.eq("shortDesc", govLaw.getShortDesc()).ignoreCase());
		
		if(govLaw.getLawId() != null){
			criteriaS.add(Restrictions.ne("lawId", govLaw.getLawId()));
		}

		governingLaws =  criteriaS.list();
		
		if(governingLaws==null || governingLaws.size()==0){
			return false;
		}
		
		return true;
	}

}
