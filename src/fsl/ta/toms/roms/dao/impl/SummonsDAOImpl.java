/**
 * Created By: jreid
 * Date: Apr 30, 2013
 *
 */
package fsl.ta.toms.roms.dao.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;

import fsl.ta.toms.roms.bo.DocumentViewBO;
import fsl.ta.toms.roms.bo.OperationArteryBO;
import fsl.ta.toms.roms.bo.RoadLicenceBO;
import fsl.ta.toms.roms.bo.ScannedDocBO;
import fsl.ta.toms.roms.bo.SummonsBO;
import fsl.ta.toms.roms.bo.VehicleOwnerBO;
import fsl.ta.toms.roms.constants.Constants;
import fsl.ta.toms.roms.dao.DAOFactory;
import fsl.ta.toms.roms.dao.SummonsDAO;
import fsl.ta.toms.roms.dataobjects.CDOtherRoleDO;
import fsl.ta.toms.roms.dataobjects.ScannedDocDO;
import fsl.ta.toms.roms.dataobjects.StatusDO;
import fsl.ta.toms.roms.dataobjects.SummonsDO;
import fsl.ta.toms.roms.search.criteria.impl.DocumentsCriteriaBO;
import fsl.ta.toms.roms.util.DateUtils;

/**
 * @author jreid Created Date: Apr 30, 2013
 */
public class SummonsDAOImpl extends ParentDAOImpl implements SummonsDAO {
	@Autowired
	DAOFactory	daoFactory;

	@Override
	public ArrayList<DocumentViewBO> lookupSummons(DocumentsCriteriaBO criteria) {
		Criteria criteriaS = this.getSession().createCriteria(SummonsDO.class);
		criteriaS.createAlias("offender", "o");
		criteriaS.createAlias("taStaff.person", "ta");
		criteriaS.createAlias("roadCheckOffenceOutcome.roadCheckOffence.roadCheck.compliance.roadOperation", "ro", Criteria.LEFT_JOIN);

		// criteriaS.createAlias("roadCheckOffenceOutcome.roadCheckOffence.roadCheck.compliance",
		// "co");
		// jp
		/*
		 * if(StringUtils.isNotBlank(criteria.getJpFirstName())){
		 * criteriaS.add(Restrictions.like("justiceOfPeace.person.firstName",
		 * "%" + criteria.getJpFirstName()+ "%" )); }
		 * if(StringUtils.isNotBlank(criteria.getJpLastName())){
		 * criteriaS.add(Restrictions.like("justiceOfPeace.person.lastName", "%"
		 * + criteria.getJpLastName()+ "%" )); }
		 * if(StringUtils.isNotBlank(criteria.getJpRegNumber())){
		 * criteriaS.add(Restrictions.eq("justiceOfPeace.regNumber",
		 * criteria.getJpRegNumber())); }
		 */

		// offence
		/*
		 * if(StringUtils.isNotBlank(criteria.getOffenceDescription())){
		 * criteriaS.add(Restrictions.eq(
		 * "roadCheckOffenceOutcome.roadCheckOffence.offence.description",
		 * criteria.getOffenceDescription())); } if(criteria.getOffenceId() !=
		 * null){ criteriaS.add(Restrictions.eq(
		 * "roadCheckOffenceOutcome.roadCheckOffence.offence.offenceId",
		 * criteria.getOffenceId())); }
		 */
		if (criteria.getOffenceStartDateRange() != null && criteria.getOffenceEndDateRange() != null) {			
			//if(criteria.getOffenceStartDateRange().compareTo(criteria.getOffenceEndDateRange()) == 0 ){
				criteriaS.add(Restrictions.ge("offenceDtime", criteria.getOffenceStartDateRange()));
				criteriaS.add(Restrictions.lt("offenceDtime", DateUtils.addOneDay(criteria.getOffenceEndDateRange())));
			//}else{			
			//	criteriaS.add(Restrictions.between("offenceDtime", criteria.getOffenceStartDateRange(), criteria.getOffenceEndDateRange()));
			//}
		}
		if (criteria.getOffenceStartDateRange() != null && criteria.getOffenceEndDateRange() == null) {
			criteriaS.add(Restrictions.ge("offenceDtime", criteria.getOffenceStartDateRange()));
			criteriaS.add(Restrictions.lt("offenceDtime", DateUtils.addOneDay(criteria.getOffenceStartDateRange())));
		}
		if (criteria.getOffenceStartDateRange() == null && criteria.getOffenceEndDateRange() != null) {
			criteriaS.add(Restrictions.ge("offenceDtime", criteria.getOffenceEndDateRange()));
			criteriaS.add(Restrictions.lt("offenceDtime", DateUtils.addOneDay(criteria.getOffenceEndDateRange())));
		}

		// offender
		if (StringUtils.isNotBlank(criteria.getOffenderFirstName())) {
			criteriaS.add(Restrictions.like("o.firstName", "%" + criteria.getOffenderFirstName() + "%").ignoreCase());
		}
		if (StringUtils.isNotBlank(criteria.getOffenderLastName())) {
			criteriaS.add(Restrictions.like("o.lastName", "%" + criteria.getOffenderLastName() + "%").ignoreCase());
		}
		if (StringUtils.isNotBlank(criteria.getOffenderMiddleName())) {
			criteriaS.add(Restrictions.like("o.middleName", "%" + criteria.getOffenderMiddleName() + "%").ignoreCase());
		}
		if (StringUtils.isNotBlank(criteria.getTrnNbr())) {
			criteriaS.add(Restrictions.eq("o.trnNbr", criteria.getTrnNbr()));
		}

		// staff
		if (StringUtils.isNotBlank(criteria.getStaffFirstName())) {
			criteriaS.add(Restrictions.like("ta.firstName", "%" + criteria.getStaffFirstName() + "%").ignoreCase());
		}
		if (StringUtils.isNotBlank(criteria.getStaffLastName())) {
			criteriaS.add(Restrictions.like("ta.lastName", "%" + criteria.getStaffLastName() + "%").ignoreCase());
		}
		if (StringUtils.isNotBlank(criteria.getStaffId())) {
			criteriaS.add(Restrictions.eq("taStaff.staffId", criteria.getStaffId()));
		}

		// operation
		/*
		 * if(StringUtils.isNotBlank(criteria.getOperationCategory())){
		 * criteriaS.add(Restrictions.eq("ro.category.categoryId",
		 * criteria.getOperationCategory())); }
		 */

		
			if (StringUtils.isNotBlank(criteria.getOperationName())) {
				criteriaS.add(Restrictions.like("ro.operationName", "%" + criteria.getOperationName() + "%").ignoreCase());
			}

			if (criteria.getOperationStartDateRange() != null && criteria.getOperationEndDateRange() == null) {
				criteriaS.add(Restrictions.ge("ro.actualStartDtime", criteria.getOperationStartDateRange()));
				criteriaS.add(Restrictions.lt("ro.actualStartDtime", DateUtils.addOneDay(criteria.getOperationStartDateRange())));
				//criteriaS.add(Restrictions.eq("ro.actualStartDtime", criteria.getOperationStartDateRange()));
			}
			if (criteria.getOperationStartDateRange() == null && criteria.getOperationEndDateRange() != null) {
				criteriaS.add(Restrictions.ge("ro.actualStartDtime", criteria.getOperationEndDateRange()));
				criteriaS.add(Restrictions.lt("ro.actualStartDtime", DateUtils.addOneDay(criteria.getOperationEndDateRange())));
				//criteriaS.add(Restrictions.eq("ro.actualStartDtime", criteria.getOperationEndDateRange()));
			}

			if (criteria.getOperationStartDateRange() != null && criteria.getOperationEndDateRange() != null) {
				
				criteriaS.add(Restrictions.ge("ro.actualStartDtime", criteria.getOperationStartDateRange()));
				criteriaS.add(Restrictions.lt("ro.actualStartDtime", DateUtils.addOneDay(criteria.getOperationEndDateRange())));
				
				//criteriaS.add(Restrictions.between("ro.actualStartDtime", criteria.getOperationStartDateRange(), criteria.getOperationEndDateRange()));
			}
//System.err.println(" road op details search ");
	

		/*
		 * if (StringUtils.isNotBlank(criteria.getOperationName())) {
		 * criteriaS.add(Restrictions.like("co.roadOperation.operationName", "%"
		 * + criteria.getOperationName() + "%")); } if
		 * (criteria.getOperationStartDateRange() != null &&
		 * criteria.getOperationEndDateRange() == null) {
		 * criteriaS.add(Restrictions.eq("co.roadOperation.actualStartDtime",
		 * criteria.getOperationStartDateRange())); } if
		 * (criteria.getOperationStartDateRange() == null &&
		 * criteria.getOperationEndDateRange() != null) {
		 * criteriaS.add(Restrictions.eq("co.roadOperation.actualStartDtime",
		 * criteria.getOperationEndDateRange())); } if
		 * (criteria.getOperationStartDateRange() != null &&
		 * criteria.getOperationEndDateRange() != null) {
		 * criteriaS.add(Restrictions
		 * .between("co.roadOperation.actualStartDtime",
		 * criteria.getOperationStartDateRange(),
		 * criteria.getOperationEndDateRange())); }
		 */
		/*
		 * if(criteria.getRoadOperationId() != null){
		 * criteriaS.add(Restrictions.eq("ro.category.categoryId",
		 * criteria.getRoadOperationId())); }
		 */
		// summons
		/*
		 * if (criteria.getAutomaticSerialNo() != null &&
		 * criteria.getAutomaticSerialNo() > 0) {
		 * criteriaS.add(Restrictions.eq("summonsId",
		 * criteria.getAutomaticSerialNo())); }
		 */
		if (StringUtils.isNotBlank(criteria.getReferenceNo())) {
			criteriaS.add(Restrictions.eq("referenceNo", Integer.parseInt(criteria.getReferenceNo())));
		}
		if (StringUtils.isNotBlank(criteria.getManualSerialNo())) {
			criteriaS.add(Restrictions.like("manualSerialNumber", "%" + criteria.getManualSerialNo() + "%").ignoreCase());
		}
		if (StringUtils.isNotBlank(criteria.getDocumentStatus())) {
			criteriaS.add(Restrictions.eq("status.statusId", criteria.getDocumentStatus()));
		}

		// set the ordering
		criteriaS.addOrder(Order.desc("summonsId"));


		//System.err.println("Criteria: "+criteriaS.toString());
		
		// organise list
		ArrayList<SummonsDO> results = (ArrayList<SummonsDO>) criteriaS.list();

		ArrayList<DocumentViewBO> finalList = new ArrayList<DocumentViewBO>();

		if (results == null || results.isEmpty())
			return null;

		// add the scanned documents
		for (SummonsDO summon : results) {
			// SummonsBO compositeSummon = new SummonsBO(summon);
			// compositeSummon.setScannedDocList(lookupScannedDocsForRoadCheckOffenceId(summon.getRoadCheckOffenceOutcome().getRoadCheckOffenceOutcomeId()));
			// finalList.add(compositeSummon);
			DocumentViewBO docView = new DocumentViewBO(summon);
			
			String roleID = summon.getRoadCheckOffenceOutcome().getRoadCheckOffence()
			.getRoadCheck().getCompliance().getPersonRole();
			
			

			
			//System.err.println("Role Id: "+roleID);
			
			if(StringUtils.trimToNull(roleID)!=null && Constants.PersonRole.OTHER.equalsIgnoreCase(roleID)){
				String otherRole = summon.getRoadCheckOffenceOutcome().getRoadCheckOffence()
						.getRoadCheck().getCompliance().getOtherRole();
				
				/*Criteria roleCritera = this.getSession().createCriteria(CDOtherRoleDO.class);
				
				roleCritera.add(Restrictions.eq("otherRoleId", otherRole));*/
				//System.err.println("Other role: "+otherRole);
				
				if(!StringUtils.isEmpty(otherRole))
				{
					CDOtherRoleDO cdotherRole = this.find(CDOtherRoleDO.class, otherRole);
				
				
					if(cdotherRole!=null){
						//System.err.println(" Discription: "+cdotherRole.getDescription());
						
						docView.setOffenderRoleObserved("Other - "+ cdotherRole.getDescription());
					}
				}else{
					docView.setOffenderRoleObserved("Other");
				}
								
			}
			
			
			
			finalList.add(docView);

		}
		return finalList;

	}

	@Override
	public SummonsBO lookupSummonsById(Integer summonsId) {
		Criteria criteriaS = this.getSession().createCriteria(SummonsDO.class);

		// summons
		if (summonsId != null) {
			criteriaS.add(Restrictions.eq("summonsId", summonsId));
		}

		// organise list
		ArrayList<SummonsDO> results = (ArrayList<SummonsDO>) criteriaS.list();
		SummonsBO finalSummons = new SummonsBO();

		if (results == null || results.isEmpty())
			return null;
		else {
			finalSummons = new SummonsBO(results.get(0));
			finalSummons.setScannedDocList(lookupScannedDocsForRoadCheckOffenceId(results.get(0).getRoadCheckOffenceOutcome().getRoadCheckOffenceOutcomeId()));

			if(finalSummons.getVehicle() != null)
			{	
				// set road licence
				RoadLicenceBO roadLicence = daoFactory.getRoadLicenceDAO().getRoadLicenceByPlateNoForDocumentView(finalSummons.getVehicle().getPlateRegNo());
				finalSummons.setRoadLicence(roadLicence);
	
				// set vehicle owners
				List<VehicleOwnerBO> owners = daoFactory.getRoadLicenceDAO().findVehicleOwnersByVehicleIDForDocumentView(finalSummons.getVehicle().getVehicleId());
				finalSummons.setListOfVehicleOwners(owners);
			}
		}

		return finalSummons;

	}

	@Override
	public ArrayList<ScannedDocBO> lookupScannedDocsForSummonId(Integer summonsId) {

		Criteria criteriaS = this.getSession().createCriteria(ScannedDocDO.class);
		criteriaS.add(Restrictions.eq("roadCheckOffenceOutcome.summons.summonsId", summonsId));
		criteriaS.addOrder(Order.asc("auditEntry.createDTime"));

		ArrayList<ScannedDocDO> results = (ArrayList<ScannedDocDO>) criteriaS.list();

		ArrayList<ScannedDocBO> finalList = new ArrayList<ScannedDocBO>();

		if (results == null && results.isEmpty())
			return null;

		for (ScannedDocDO doc : results) {
			finalList.add(new ScannedDocBO(doc));
		}

		return finalList;
	}

	@Override
	public ArrayList<ScannedDocBO> lookupScannedDocsForRoadCheckOffenceId(Integer offenceId) {

		Criteria criteriaS = this.getSession().createCriteria(ScannedDocDO.class);
		criteriaS.add(Restrictions.eq("roadCheckOffenceOutcome.roadCheckOffenceOutcomeId", offenceId));
		criteriaS.addOrder(Order.desc("auditEntry.createDTime"));

		ArrayList<ScannedDocDO> results = (ArrayList<ScannedDocDO>) criteriaS.list();

		ArrayList<ScannedDocBO> finalList = new ArrayList<ScannedDocBO>();

		if (results == null && results.isEmpty())
			return null;

		for (ScannedDocDO doc : results) {
			finalList.add(new ScannedDocBO(doc));
		}

		return finalList;
	}

	@Override
	public Serializable save(Object entity) throws DataAccessException {

		Session session = getSession();
		getSession().setFlushMode(FlushMode.ALWAYS);

		return session.save(entity);
	}
	
	@SuppressWarnings("unchecked")
	public SummonsDO findSummonsByRoadCheckOffenceOutcomeId(Integer roadCheckOffenceOutcomeId) {
		//System.err.println("DAO roadCheckOffenceOutcomeId: " + roadCheckOffenceOutcomeId);
		List<SummonsDO> summonsList = new ArrayList<SummonsDO>();
		
		String queryString = "select o from SummonsDO o where o.roadCheckOffenceOutcome.roadCheckOffenceOutcomeId = :roadCheckOffenceOutcomeId";
		
		summonsList = hibernateTemplate.findByNamedParam(queryString, "roadCheckOffenceOutcomeId", roadCheckOffenceOutcomeId);
			
		if(summonsList!=null && summonsList.size()==1){
			return summonsList.get(0);
		}
		
		return null;
	}

}
