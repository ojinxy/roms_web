/**
 * Created By: jreid
 * Date: Apr 30, 2013
 *
 */
package fsl.ta.toms.roms.dao.impl;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

import fsl.ta.toms.roms.bo.DocumentViewBO;
import fsl.ta.toms.roms.bo.OffenceBO;
import fsl.ta.toms.roms.bo.PersonBO;
import fsl.ta.toms.roms.bo.RoadLicenceBO;
import fsl.ta.toms.roms.bo.ScannedDocBO;
import fsl.ta.toms.roms.bo.VehicleOwnerBO;
import fsl.ta.toms.roms.bo.WarningNoticeBO;
import fsl.ta.toms.roms.constants.Constants;
import fsl.ta.toms.roms.dao.DAOFactory;
import fsl.ta.toms.roms.dao.WarningNoticeDAO;
import fsl.ta.toms.roms.dataobjects.PoliceOfficerDO;
import fsl.ta.toms.roms.dataobjects.CDOtherRoleDO;
import fsl.ta.toms.roms.dataobjects.ScannedDocDO;
import fsl.ta.toms.roms.dataobjects.SummonsDO;
import fsl.ta.toms.roms.dataobjects.WarningNoticeDO;
import fsl.ta.toms.roms.search.criteria.impl.DocumentsCriteriaBO;
import fsl.ta.toms.roms.util.DateUtils;

/**
 * @author jreid Created Date: Apr 30, 2013
 */
public class WarningNoticeDAOImpl extends ParentDAOImpl implements WarningNoticeDAO {

	@Autowired
	DAOFactory	daoFactory;

	@Override
	public ArrayList<DocumentViewBO> lookupWarningNotice(DocumentsCriteriaBO criteria) {

		Criteria criteriaS = this.getSession().createCriteria(WarningNoticeDO.class);

		criteriaS.createAlias("roadCheckOffenceOutcome.roadCheckOffence.roadCheck.compliance", "c");
		criteriaS.createAlias("offender", "o");
		criteriaS.createAlias("taStaff.person", "ta");
		criteriaS.createAlias("roadCheckOffenceOutcome.roadCheckOffence.roadCheck.compliance.roadOperation", "ro", Criteria.LEFT_JOIN);

		/*if (criteria.getOffenceStartDateRange() != null && criteria.getOffenceEndDateRange() != null) {
			criteriaS.add(Restrictions.between("seizureDtime", criteria.getOffenceStartDateRange(), criteria.getOffenceEndDateRange()));
		}
		if (criteria.getOffenceStartDateRange() != null && criteria.getOffenceEndDateRange() == null) {
			criteriaS.add(Restrictions.eq("seizureDtime", criteria.getOffenceStartDateRange()));
		}
		if (criteria.getOffenceStartDateRange() == null && criteria.getOffenceEndDateRange() != null) {
			criteriaS.add(Restrictions.eq("seizureDtime", criteria.getOffenceEndDateRange()));
		}*/

		
		if (criteria.getOffenceStartDateRange() != null && criteria.getOffenceEndDateRange() != null) {
			//if(criteria.getOffenceStartDateRange().compareTo(criteria.getOffenceEndDateRange()) == 0 ){
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

		if (criteria.getComplianceId() != null && criteria.getComplianceId() > 0) {
			criteriaS.add(Restrictions.eq("c.complianceId", criteria.getComplianceId()));
		}
		/*
		 * if(criteria.getRoadOperationId() != null){
		 * criteriaS.add(Restrictions.eq(
		 * "roadCheckOffenceOutcome.roadCheckOffence.roadCheck.compliance.roadOperation.category.categoryId"
		 * , criteria.getRoadOperationId())); }
		 */
		// summons
		/*
		 * if (criteria.getAutomaticSerialNo() != null &&
		 * criteria.getAutomaticSerialNo() > 0) {
		 * criteriaS.add(Restrictions.eq("warningNoticeId",
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

		// if it is a manual summons
		/*
		 * if(criteria.isManualDocument()){
		 * criteriaS.add(Restrictions.isNotEmpty("manualSerialNumber")); }else{
		 * criteriaS.add(Restrictions.isEmpty("manualSerialNumber")); }
		 */

		// set the ordering
		criteriaS.addOrder(Order.desc("warningNoticeId"));

		// organise list
		ArrayList<WarningNoticeDO> results = (ArrayList<WarningNoticeDO>) criteriaS.list();
		ArrayList<DocumentViewBO> finalList = new ArrayList<DocumentViewBO>();

		// try to evict class to prevent object not found error
		/*
		 * this.getSession().getSessionFactory().evictQueries();
		 * this.getSession().getSessionFactory().evict(StatusDO.class);
		 */
		if (results == null || results.isEmpty())
			return null;

		// add the scanned documents
		for (WarningNoticeDO warningNotice : results) {
			// SummonsBO compositeSummon = new SummonsBO(summon);
			// compositeSummon.setScannedDocList(lookupScannedDocsForRoadCheckOffenceId(summon.getRoadCheckOffenceOutcome().getRoadCheckOffenceOutcomeId()));
			// finalList.add(compositeSummon);
			DocumentViewBO docView = new DocumentViewBO(warningNotice);
			
			String roleID = warningNotice.getRoadCheckOffenceOutcome().getRoadCheckOffence()
					.getRoadCheck().getCompliance().getPersonRole();
					
			if(StringUtils.trimToNull(roleID)!=null && Constants.PersonRole.OTHER.equalsIgnoreCase(roleID)){
				String otherRole = warningNotice.getRoadCheckOffenceOutcome().getRoadCheckOffence()
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
								
			}
			
			finalList.add(docView);

		}
		return finalList;

	}

	@Override
	public ArrayList<ScannedDocBO> lookupScannedDocsForWarningNoticeId(Integer warningNoticesId) {

		Criteria criteriaS = this.getSession().createCriteria(ScannedDocDO.class);
		criteriaS.add(Restrictions.eq("roadCheckOffenceOutcome.roadCheckOffenceOutcomeId", warningNoticesId));
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
	public WarningNoticeBO lookupWarningNoticeById(Integer warningNoticeId) {
		Criteria criteriaS = this.getSession().createCriteria(WarningNoticeDO.class);

		// summons
		if (warningNoticeId != null) {
			criteriaS.add(Restrictions.eq("warningNoticeId", warningNoticeId));
		}

		// organise list
		ArrayList<WarningNoticeDO> results = (ArrayList<WarningNoticeDO>) criteriaS.list();
		WarningNoticeBO finalWarningNotice = new WarningNoticeBO();

		if (results == null || results.isEmpty())
			return null;
		else {
			WarningNoticeDO warningNotice = results.get(0);
			finalWarningNotice = new WarningNoticeBO(warningNotice);
			finalWarningNotice.setScannedDocList(daoFactory.getSummonsDAO().lookupScannedDocsForRoadCheckOffenceId(warningNotice.getRoadCheckOffenceOutcome().getRoadCheckOffenceOutcomeId()));

			List<OffenceBO> offences = daoFactory.getRoadCompliancyDAO().getOffencesForDocument(warningNoticeId, Constants.DocumentType.WARNING_NOTICE,
				warningNotice.getRoadCheckOffenceOutcome().getRoadCheckOffence().getRoadCheck().getCompliance().getComplianceId());
			finalWarningNotice.setListOfOffences(offences);

			// set witnesses
			List<PersonBO> witnesses = daoFactory.getRoadCompliancyDAO().getWitnessesforCompliance(
				warningNotice.getRoadCheckOffenceOutcome().getRoadCheckOffence().getRoadCheck().getCompliance().getComplianceId());
			finalWarningNotice.setListOfWitnesses(witnesses);

			// set road licence
			RoadLicenceBO roadLicence = daoFactory.getRoadLicenceDAO().getRoadLicenceByPlateNoForDocumentView(finalWarningNotice.getVehicle().getPlateRegNo());
			finalWarningNotice.setRoadLicence(roadLicence);

			// set vehicle owners
			List<VehicleOwnerBO> owners = daoFactory.getRoadLicenceDAO().findVehicleOwnersByVehicleIDForDocumentView(finalWarningNotice.getVehicle().getVehicleId());
			finalWarningNotice.setListOfVehicleOwners(owners);

		}

		if(finalWarningNotice.getVehicleMoverPersonType() != null && finalWarningNotice.getVehicleMoverPersonType().equalsIgnoreCase("PO") && finalWarningNotice.getVehicleMoverBO() != null){
			finalWarningNotice.getVehicleMoverBO().setFullName(this.getPoliceOfficerFullNameForDisplay(finalWarningNotice.getVehicleMoverBO().getPersonId(), 
					finalWarningNotice.getVehicleMoverBO().getFullName()));
		}
		
		return finalWarningNotice;

	}
	
	public String getPoliceOfficerFullNameForDisplay(Integer personId, String vehicleMoverFullName){
		PoliceOfficerDO police = daoFactory.getPoliceOfficerDAO().getPoliceByPersonId(personId);
		
		if(police != null){
			//#{police.rank} #{police.fullName} - [ #{police.polOfficerCompNo} ] - #{police.stationDescription}
			String policeDisplayName = police.getRank() + " " + vehicleMoverFullName + " - [ " + police.getPolOfficerCompNo() + " ]" + " - " + police.getStationDescription();
			
			return policeDisplayName;
		}
		else{
			return null;
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public WarningNoticeDO findWarningNoticeByRoadCheckOffenceOutcomeId(Integer roadCheckOffenceOutcomeId) {
		List<WarningNoticeDO> warningNoticeList = new ArrayList<WarningNoticeDO>();
		
		String queryString = " from WarningNoticeDO o where o.roadCheckOffenceOutcome.roadCheckOffenceOutcomeId = :roadCheckOffenceOutcomeId";
		
		warningNoticeList = hibernateTemplate.findByNamedParam(queryString, "roadCheckOffenceOutcomeId", roadCheckOffenceOutcomeId);
			
		if(warningNoticeList!=null && warningNoticeList.size()==1){
			return warningNoticeList.get(0);
		}
		
		return null;
	}

	@Override
	public Integer getAssociatedWarningNotice(Integer roadCheckOffenceOutcomeId) {
		Integer refSeqNo = null;
		
		String queryString = "select ref_seq_no from roms_warning_notice"+
				" where road_check_offence_outcome_id = (select road_check_offence_outcome_id from roms_road_check_offence_outcome"+
				" where outcome_type_id='IW' and road_check_offence_id = (select road_check_offence_id  from roms_road_check_offence_outcome"+
				" where road_check_offence_outcome_id = "+roadCheckOffenceOutcomeId+"))";	
		
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		
		 Query query = session.createSQLQuery(queryString);
		 refSeqNo = (Integer) query.uniqueResult();
	
		 return refSeqNo;
	}

}
