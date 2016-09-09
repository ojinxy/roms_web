/**
 * Created By: jreid
 * Date: Apr 30, 2013
 *
 */
package fsl.ta.toms.roms.dao.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

import fsl.ta.toms.roms.bo.DocumentViewBO;
import fsl.ta.toms.roms.bo.OffenceBO;
import fsl.ta.toms.roms.bo.RoadLicenceBO;
import fsl.ta.toms.roms.bo.ScannedDocBO;
import fsl.ta.toms.roms.bo.VehicleOwnerBO;
import fsl.ta.toms.roms.bo.WarningNoProsecutionBO;
import fsl.ta.toms.roms.constants.Constants;
import fsl.ta.toms.roms.dao.DAOFactory;
import fsl.ta.toms.roms.dao.WarningNoProsecutionDAO;
import fsl.ta.toms.roms.dataobjects.CDOtherRoleDO;
import fsl.ta.toms.roms.dataobjects.ScannedDocDO;
import fsl.ta.toms.roms.dataobjects.StatusDO;
import fsl.ta.toms.roms.dataobjects.WarningNoProsecutionDO;
import fsl.ta.toms.roms.search.criteria.impl.DocumentsCriteriaBO;
import fsl.ta.toms.roms.util.DateUtils;

/**
 * @author oanguin Created Date: August 7, 2013
 */
public class WarningNoProsecutionDAOImpl extends ParentDAOImpl implements WarningNoProsecutionDAO {

	@Autowired
	DAOFactory	daoFactory;

	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<DocumentViewBO> lookupWarningNoProsecution(DocumentsCriteriaBO criteria) {
		Criteria criteriaS = this.getSession().createCriteria(WarningNoProsecutionDO.class);

		criteriaS.createAlias("roadCheckOffenceOutcome.roadCheckOffence.roadCheck.compliance", "c");
		criteriaS.createAlias("offender", "o");
		criteriaS.createAlias("taStaff.person", "ta");
		criteriaS.createAlias("roadCheckOffenceOutcome.roadCheckOffence.roadCheck.compliance.roadOperation", "ro", Criteria.LEFT_JOIN);

		/*
		 * if(criteria.getOffenceStartDateRange() != null &&
		 * criteria.getOffenceEndDateRange() != null){
		 * criteriaS.add(Restrictions
		 * .between("seizureDtime",criteria.getOffenceStartDateRange(),
		 * criteria.getOffenceEndDateRange())); }
		 * if(criteria.getOffenceStartDateRange() != null &&
		 * criteria.getOffenceEndDateRange() == null){
		 * criteriaS.add(Restrictions
		 * .eq("seizureDtime",criteria.getOffenceStartDateRange())); }
		 * if(criteria.getOffenceStartDateRange() == null &&
		 * criteria.getOffenceEndDateRange() != null){
		 * criteriaS.add(Restrictions
		 * .eq("seizureDtime",criteria.getOffenceEndDateRange())); }
		 */

		if (criteria.getOffenceStartDateRange() != null && criteria.getOffenceEndDateRange() != null) {
			
		//	if(criteria.getOffenceStartDateRange().compareTo(criteria.getOffenceEndDateRange()) == 0 ){
				criteriaS.add(Restrictions.ge("c.offenceDateTime", criteria.getOffenceStartDateRange()));
				criteriaS.add(Restrictions.lt("c.offenceDateTime", DateUtils.addOneDay(criteria.getOffenceEndDateRange())));
			//}else{
			//	criteriaS.add(Restrictions.between("c.offenceDateTime", criteria.getOffenceStartDateRange(), criteria.getOffenceEndDateRange()));
			//}
		}
		if (criteria.getOffenceStartDateRange() != null && criteria.getOffenceEndDateRange() == null) {
			criteriaS.add(Restrictions.ge("c.offenceDateTime", criteria.getOffenceStartDateRange()));
			criteriaS.add(Restrictions.lt("c.offenceDateTime", DateUtils.addOneDay(criteria.getOffenceStartDateRange())));
		}
		if (criteria.getOffenceStartDateRange() == null && criteria.getOffenceEndDateRange() != null) {
			criteriaS.add(Restrictions.ge("c.offenceDateTime", criteria.getOffenceEndDateRange()));
			criteriaS.add(Restrictions.lt("c.offenceDateTime", DateUtils.addOneDay(criteria.getOffenceEndDateRange())));
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
		 */if (StringUtils.isNotBlank(criteria.getOperationName())) {
			criteriaS.add(Restrictions.like("ro.operationName", "%" + criteria.getOperationName() + "%").ignoreCase());
		}
		/*
		 * if(StringUtils.isNotBlank(criteria.getOperationOffice())){
		 * criteriaS.add(Restrictions.eq("ro.officeLocCode",
		 * criteria.getOperationOffice())); }
		 */
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
		/*
		 * if(criteria.getRoadOperationId() != null){
		 * criteriaS.add(Restrictions.eq("ro.category.categoryId",
		 * criteria.getRoadOperationId())); }
		 */
		// summons
		/*
		 * if (criteria.getAutomaticSerialNo() != null &&
		 * criteria.getAutomaticSerialNo() > 0) {
		 * criteriaS.add(Restrictions.eq("warningNoProsecutionID",
		 * criteria.getAutomaticSerialNo())); }
		 */
		if (StringUtils.isNotBlank(criteria.getReferenceNo())) {
			criteriaS.add(Restrictions.eq("referenceNo", Integer.parseInt(criteria.getReferenceNo())));
		}
		if (StringUtils.isNotBlank(criteria.getManualSerialNo())) {
			criteriaS.add(Restrictions.like("manualSerialNumber", "%" + criteria.getManualSerialNo() + "%" ).ignoreCase());
		}
		if (StringUtils.isNotBlank(criteria.getDocumentStatus())) {
			criteriaS.add(Restrictions.eq("status.statusId", criteria.getDocumentStatus()));
		}

		if (criteria.getComplianceId() != null && criteria.getComplianceId() > 0) {
			criteriaS.add(Restrictions.eq("c.complianceId", criteria.getComplianceId()));
		}

		// if it is a manual summons
		/*
		 * if(criteria.isManualDocument()){
		 * criteriaS.add(Restrictions.isNotEmpty("manualSerialNumber")); }else{
		 * criteriaS.add(Restrictions.isEmpty("manualSerialNumber")); }
		 */

		// set the ordering
		criteriaS.addOrder(Order.desc("warningNoProsecutionID"));

		// organise list
		ArrayList<WarningNoProsecutionDO> results = (ArrayList<WarningNoProsecutionDO>) criteriaS.list();
		ArrayList<DocumentViewBO> finalList = new ArrayList<DocumentViewBO>();

		// try to evict class to prevent object not found error
		this.getSession().getSessionFactory().evictQueries();
		this.getSession().getSessionFactory().evict(StatusDO.class);

		if (results == null || results.isEmpty())
			return null;

		// add the scanned documents
		for (WarningNoProsecutionDO WarningNoProsecution : results) {
			// SummonsBO compositeSummon = new SummonsBO(summon);
			// compositeSummon.setScannedDocList(lookupScannedDocsForRoadCheckOffenceId(summon.getRoadCheckOffenceOutcome().getRoadCheckOffenceOutcomeId()));
			// finalList.add(compositeSummon);
			DocumentViewBO docView = new DocumentViewBO(WarningNoProsecution);
			
			String roleID = WarningNoProsecution.getRoadCheckOffenceOutcome().getRoadCheckOffence()
					.getRoadCheck().getCompliance().getPersonRole();
					
					if(StringUtils.trimToNull(roleID)!=null && Constants.PersonRole.OTHER.equalsIgnoreCase(roleID)){
						String otherRole = WarningNoProsecution.getRoadCheckOffenceOutcome().getRoadCheckOffence()
								.getRoadCheck().getCompliance().getOtherRole();
						
						/*Criteria roleCritera = this.getSession().createCriteria(CDOtherRoleDO.class);
						
						roleCritera.add(Restrictions.eq("otherRoleId", otherRole));*/
						if(!StringUtils.isEmpty(otherRole))
						{
							CDOtherRoleDO cdotherRole = this.find(CDOtherRoleDO.class, otherRole);
							
							if(cdotherRole!=null){
								docView.setOffenderRoleObserved("Other - "+cdotherRole.getDescription());
							}
						}else{
							docView.setOffenderRoleObserved("Other");
						} 
						System.out.println();				
					}
			
			
			
			
			finalList.add(docView);

		}
		return finalList;

	}

	@Override
	public ArrayList<ScannedDocBO> lookupScannedDocsForWarningNoProsecutionId(Integer warningNoProsecutionIdId) {

		WarningNoProsecutionDO warningNoPro = (WarningNoProsecutionDO) this.hibernateTemplate.getSessionFactory().getCurrentSession().get(WarningNoProsecutionDO.class, warningNoProsecutionIdId);

		if (warningNoPro != null) {
			Criteria criteriaS = this.getSession().createCriteria(ScannedDocDO.class);
			criteriaS.add(Restrictions.eq("roadCheckOffenceOutcome.roadCheckOffenceOutcomeId", warningNoPro.getRoadCheckOffenceOutcome().getRoadCheckOffenceOutcomeId()));
			criteriaS.addOrder(Order.desc("auditEntry.createDTime"));

			ArrayList<ScannedDocDO> results = (ArrayList<ScannedDocDO>) criteriaS.list();

			ArrayList<ScannedDocBO> finalList = new ArrayList<ScannedDocBO>();

			if (results == null && results.isEmpty())
				return null;

			for (ScannedDocDO doc : results) {
				finalList.add(new ScannedDocBO(doc));
			}

			return finalList;
		} else {
			return null;
		}
	}

	@Override
	public WarningNoProsecutionBO lookupWarningNoProsecutionById(Integer warningNoProsecutionId) {
		Criteria criteriaS = this.getSession().createCriteria(WarningNoProsecutionDO.class);

		// warningNoProsecutionId
		if (warningNoProsecutionId != null) {
			criteriaS.add(Restrictions.eq("warningNoProsecutionID", warningNoProsecutionId));
		}

		// organise list
		ArrayList<WarningNoProsecutionDO> results = (ArrayList<WarningNoProsecutionDO>) criteriaS.list();
		WarningNoProsecutionBO finalWarningNoProsecution = new WarningNoProsecutionBO();

		if (results == null || results.isEmpty())
			return null;
		else {
			WarningNoProsecutionDO WarningNoProsecution = results.get(0);
			finalWarningNoProsecution = new WarningNoProsecutionBO(WarningNoProsecution);
			finalWarningNoProsecution.setScannedDocList(daoFactory.getSummonsDAO().lookupScannedDocsForRoadCheckOffenceId(
				WarningNoProsecution.getRoadCheckOffenceOutcome().getRoadCheckOffenceOutcomeId()));

			// set offences
			List<OffenceBO> offences = daoFactory.getRoadCompliancyDAO().getOffencesForDocument(warningNoProsecutionId, Constants.DocumentType.WARNING_NOTICE_NO_PROSECUTION,
				WarningNoProsecution.getRoadCheckOffenceOutcome().getRoadCheckOffence().getRoadCheck().getCompliance().getComplianceId());
			finalWarningNoProsecution.setListOfOffences(offences);

			// set roar licence
			RoadLicenceBO roadLicence = daoFactory.getRoadLicenceDAO().getRoadLicenceByPlateNoForDocumentView(finalWarningNoProsecution.getVehicle().getPlateRegNo());
			finalWarningNoProsecution.setRoadLicence(roadLicence);

			// set vehicle owners
			List<VehicleOwnerBO> owners = daoFactory.getRoadLicenceDAO().findVehicleOwnersByVehicleIDForDocumentView(finalWarningNoProsecution.getVehicle().getVehicleId());
			finalWarningNoProsecution.setListOfVehicleOwners(owners);

		}

		return finalWarningNoProsecution;

	}

	@SuppressWarnings("unchecked")
	public WarningNoProsecutionDO findWarningNoProsByRoadCheckOffenceOutcomeId(Integer roadCheckOffenceOutcomeId) {
		List<WarningNoProsecutionDO> warningNoProsList = new ArrayList<WarningNoProsecutionDO>();
		
		String queryString = " from WarningNoProsecutionDO o where o.roadCheckOffenceOutcome.roadCheckOffenceOutcomeId = :roadCheckOffenceOutcomeId";
		
		warningNoProsList = hibernateTemplate.findByNamedParam(queryString, "roadCheckOffenceOutcomeId", roadCheckOffenceOutcomeId);
			
		if(warningNoProsList!=null && warningNoProsList.size()==1){
			return warningNoProsList.get(0);
		}
		
		return null;
	}
}
