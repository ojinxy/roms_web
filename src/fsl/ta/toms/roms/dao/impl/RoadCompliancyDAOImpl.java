package fsl.ta.toms.roms.dao.impl;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import fsl.ta.toms.roms.bo.BadgeCheckResultBO;
import fsl.ta.toms.roms.bo.CitationCheckResultBO;
import fsl.ta.toms.roms.bo.CitationOffenceBO;
import fsl.ta.toms.roms.bo.ComplianceBO;
import fsl.ta.toms.roms.bo.ComplianceParamBO;
import fsl.ta.toms.roms.bo.ConfigurationBO;
import fsl.ta.toms.roms.bo.CourtBO;
import fsl.ta.toms.roms.bo.DLCheckResultBO;
import fsl.ta.toms.roms.bo.DocumentViewBO;
import fsl.ta.toms.roms.bo.ExcerptParamMappingBO;
import fsl.ta.toms.roms.bo.OffenceBO;
import fsl.ta.toms.roms.bo.OffenceMandatoryOutcomeBO;
import fsl.ta.toms.roms.bo.PersonBO;
import fsl.ta.toms.roms.bo.PoundBO;
import fsl.ta.toms.roms.bo.RoadCheckBO;
import fsl.ta.toms.roms.bo.RoadCheckOffenceOutcomeBO;
import fsl.ta.toms.roms.bo.RoadLicCheckResultBO;
import fsl.ta.toms.roms.bo.RoadLicenceDriverConductorBO;
import fsl.ta.toms.roms.bo.RoadLicenceOwnerBO;
import fsl.ta.toms.roms.bo.TicketCheckResultBO;
import fsl.ta.toms.roms.bo.VehicleCheckResultBO;
import fsl.ta.toms.roms.bo.VehicleOwnerBO;
import fsl.ta.toms.roms.bo.WarningNoticeBO;
import fsl.ta.toms.roms.bo.WreckingCompanyBO;
import fsl.ta.toms.roms.constants.Constants;
import fsl.ta.toms.roms.constants.Constants.OutcomeType;
import fsl.ta.toms.roms.dao.RoadCompliancyDAO;
import fsl.ta.toms.roms.dao.RoadLicenceDAO;
import fsl.ta.toms.roms.dataobjects.BadgeCheckResultDO;
import fsl.ta.toms.roms.dataobjects.CitationCheckDO;
import fsl.ta.toms.roms.dataobjects.ComplianceDO;
import fsl.ta.toms.roms.dataobjects.ConfigurationDO;
import fsl.ta.toms.roms.dataobjects.CourtAppearanceDO;
import fsl.ta.toms.roms.dataobjects.DLCheckResultDO;
import fsl.ta.toms.roms.dataobjects.LMIS_ApplicationView;
import fsl.ta.toms.roms.dataobjects.LMIS_licenceView;
import fsl.ta.toms.roms.dataobjects.LicenceDriverConductorDO;
import fsl.ta.toms.roms.dataobjects.LicenceOwnerDO;
import fsl.ta.toms.roms.dataobjects.PersonDO;
import fsl.ta.toms.roms.dataobjects.ROMS_CitationOffenceView;
import fsl.ta.toms.roms.dataobjects.RoadLicCheckResultDO;
import fsl.ta.toms.roms.dataobjects.ScannedDocDO;
import fsl.ta.toms.roms.dataobjects.TicketCheckResultDO;
import fsl.ta.toms.roms.dataobjects.VehicleCheckResultDO;
import fsl.ta.toms.roms.dataobjects.VehicleCheckResultOwnerDO;
import fsl.ta.toms.roms.dataobjects.VehicleDO;
import fsl.ta.toms.roms.dataobjects.VehicleFitnessDO;
import fsl.ta.toms.roms.dataobjects.VehicleInsuranceDO;
import fsl.ta.toms.roms.dataobjects.VehicleOwnerDO;
import fsl.ta.toms.roms.dataobjects.WarningNoProsecutionDO;
import fsl.ta.toms.roms.dataobjects.WarningNoticeDO;
import fsl.ta.toms.roms.search.criteria.impl.PersonCriteriaBO;
import fsl.ta.toms.roms.search.criteria.impl.RoadCompliancyCriteriaBO;
import fsl.ta.toms.roms.search.criteria.impl.VehicleCriteriaBO;
import fsl.ta.toms.roms.service.LMISService;
import fsl.ta.toms.roms.util.DateUtils;
import fsl.ta.toms.roms.util.StringUtil;

public class RoadCompliancyDAOImpl extends ParentDAOImpl implements  RoadCompliancyDAO {

	
	
	
	/********************************************************************************************************/
	
	@SuppressWarnings("unchecked")
	@Override
	public List<CitationOffenceBO> findCitationOffences(String trn, String dln, String licencePlate, boolean isHandHeld) {
		
		List<CitationOffenceBO> citations = new ArrayList<CitationOffenceBO>();
		
		//System.err.println("Plate num " + licencePlate);
		StringBuffer queryString = new StringBuffer("select new fsl.ta.toms.roms.bo.CitationOffenceBO(a.offenceDateTime,a.roadCheckOffenceID," +
				"a.offenceDescription,a.offenceShortDesc,a.offenceType,a.courtDate,a.courtOutcome," +
				"a.trnNbr,a.dlNo,a.caseStatus,a.plateNumber,a.personRole,a.offenderFirstName,a.offenderLastName,a.offenderMidName,a.otherRoleId,a.otherRoleDesc)" +
		        " from ROMS_CitationOffenceView a where 1=1");
		
			if(isHandHeld){
				queryString.append(" and upper(a.caseStatus) not in ('")
						   .append(Constants.DocumentStatus.CANCELLED+"', '")
						   .append(Constants.DocumentStatus.WITHDRAWN+"', '")
						   .append(Constants.Status.COURT_CASE_CANCELLED+"', '")
						   .append(Constants.Status.COURT_CASE_WITHDRAWN+"') ");
			}	
		
			queryString.append(" and a.caseStatus is not null ");
		//System.err.println("queryStr: "+queryString.toString());
			
		String trimmedLicPlate = StringUtil.isSet(licencePlate) ? licencePlate.trim() : null;
		
		boolean isPlateNumberAdded = false;
		
		//TRN entered
		if ((StringUtil.isSet(trn))) {
			String trimmedTRN = trn.trim();
			
			queryString.append(" and a.trnNbr = '").append(trimmedTRN+ "'");
			
			if(!isPlateNumberAdded && StringUtil.isSet(trimmedLicPlate))
			{
				queryString.append(" or lower(a.plateNumber) = '").append(trimmedLicPlate.toLowerCase()+ "'");
				
				isPlateNumberAdded = true;
			}
		}
		
		//DLN entered
		if (StringUtil.isSet(dln)) {
			String trimmedDLN = dln.trim();
			
			if ((StringUtil.isSet(trn))){
				queryString.append(" and (a.dlNo is null or a.dlNo = '").append(trimmedDLN + "')");
			}
			else{
				queryString.append(" and (a.dlNo = '").append(trimmedDLN + "')");
			}
			
			if(!isPlateNumberAdded && StringUtil.isSet(trimmedLicPlate))
			{
				queryString.append(" or lower(a.plateNumber) = '").append(trimmedLicPlate.toLowerCase()+ "'");
				
				isPlateNumberAdded = true;
			}
		}
		
		
		
		//LICENCE PLATE entered
		if (StringUtil.isSet(licencePlate)) {
			
			if(!isPlateNumberAdded && StringUtil.isSet(trimmedLicPlate))
			{
				queryString.append(" and lower(a.plateNumber) = '").append(trimmedLicPlate.toLowerCase()+ "'");
				
			}
		}
				
		
		queryString.append(" order by offenceDateTime DESC ");
		
		Query query = this.hibernateTemplate.getSessionFactory().getCurrentSession().createQuery(queryString.toString());
		
		query.setFirstResult(0);
		
		int limit = getMaxROMSOffences();
		
		if(limit < 1 || limit > 200)
		{
			limit = 50;
		}
		
		
		query.setMaxResults(limit);
		
		
		citations = query.list();
		
		
		return citations;
	}
	
	private int getMaxROMSOffences()
	{
		int maxRomsOffences = 50;
		
		try
		{
			Criteria criteria = this.hibernateTemplate.getSessionFactory().getCurrentSession().createCriteria(ConfigurationDO.class,"config");
			
			criteria.add(Restrictions.eq("config.cfgCode", Constants.Configuration.MAX_ROMS_OFFENCES));
			
			ConfigurationDO configDO = (ConfigurationDO) criteria.uniqueResult();
			
			maxRomsOffences = Integer.parseInt(configDO.getValue());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		
		return maxRomsOffences;
	}

	
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<PersonDO> findPersonByCriteria(PersonCriteriaBO criteria) {
		StringBuffer queryString = new StringBuffer();
		
		queryString.append(" from PersonDO p where 1=1 ");
		
		//Person ID
		if (StringUtils.trimToNull(criteria.getPersonId()) != null) 
		{queryString.append(" and upper(p.personId) = '").append(criteria.getPersonId().toUpperCase()).append("'");}
			
		//TRN
		if (StringUtils.trimToNull(criteria.getTrnNbr()) != null) 
		{queryString.append(" and upper(p.trnNbr) = '").append(criteria.getTrnNbr().toUpperCase()).append("'");}
			
		//First Name
		if (StringUtils.trimToNull(criteria.getFirstName()) != null) 
		{queryString.append(" and upper(p.firstName) like '%").append(criteria.getFirstName().toUpperCase()).append("%'");}
				
		//Last Name
		if (StringUtils.trimToNull(criteria.getLastName()) != null) 
		{queryString.append(" and upper(p.lastName) like '%").append(criteria.getLastName().toUpperCase()).append("%'");}
		
		//Middle Name
		if (StringUtils.trimToNull(criteria.getMiddleName()) != null) 
		{queryString.append(" and upper(p.middleName) like '%").append(criteria.getMiddleName().toUpperCase()).append("%'");}
				
				
		return hibernateTemplate.find(queryString.toString());
		
	}
	
	
	public Serializable savePerson(PersonDO personDO){
		return hibernateTemplate.save(personDO);
		
	}
	
	
	public boolean updatePerson(PersonDO personDO)
	{
		try{
			hibernateTemplate.update(personDO);
			return true;
		}catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
		
	}
	
	/*************************************************************************************************************/
	
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<VehicleDO> findVehiclesByCriteria(VehicleCriteriaBO criteria) {
		StringBuffer queryString = new StringBuffer();
		
		queryString.append(" from VehicleDO v where 1=1 ");
		
		//Vehicle ID
		if (StringUtils.trimToNull(criteria.getVehicleId()) != null) 
		{queryString.append(" and upper(v.vehicleId) = '").append(criteria.getVehicleId().toUpperCase()).append("'");}
			
		//Plate Reg No
		if (StringUtils.trimToNull(criteria.getPlateRegNo()) != null) 
		{queryString.append(" and upper(v.plateRegNo) = '").append(criteria.getPlateRegNo().toUpperCase()).append("'");}
			
		//Engine Num
		if (StringUtils.trimToNull(criteria.getEngineNo()) != null) 
		{queryString.append(" and upper(v.engineNo) = '").append(criteria.getEngineNo().toUpperCase()).append("'");}
				
		//Chassis Num
		if (StringUtils.trimToNull(criteria.getChassisNo()) != null) 
		{queryString.append(" and upper(v.chassisNo) = '").append(criteria.getChassisNo().toUpperCase()).append("'");}
		
		
		//Colour
		if (StringUtils.trimToNull(criteria.getColour()) != null) 
		{queryString.append(" and upper(v.colour) = '").append(criteria.getColour().toUpperCase()).append("'");}
				
		//Year
		if (criteria.getYear() != null) 
		{queryString.append(" and v.year = ").append(criteria.getYear());}
		
		return hibernateTemplate.find(queryString.toString());
		
	}
	
	

	
	@SuppressWarnings("unchecked")
	@Override
	public VehicleCheckResultDO findLatestVehicleForPlateNo(String plateNo) {
		
		VehicleCheckResultDO result= null;
		
		/*Specify search criteria for search*/
		Criteria criteria = this.hibernateTemplate.getSessionFactory().getCurrentSession().createCriteria(VehicleCheckResultDO.class,"VehicleCheckRes");
		
		/*List of all aliases used*/
		criteria.createAlias("VehicleCheckRes.roadCheck", "roadCheck");
		criteria.createAlias("roadCheck.compliance", "compliance");
		criteria.createAlias("compliance.vehicle", "vehicle");
		
		criteria.add(Restrictions.eq("vehicle.plateRegNo", plateNo).ignoreCase());
		
		criteria.addOrder(Order.desc("vehicle.auditEntry.createDTime"));
		
		criteria.addOrder(Order.desc("vehicle.auditEntry.updateDTime"));
		
		List<VehicleCheckResultDO> listVehicleDO = criteria.list();
		
		if(listVehicleDO != null )
		{
			if(!listVehicleDO.isEmpty() || listVehicleDO.size() > 0)
			{
				result = listVehicleDO.get(0);
				
			}
		}
		
		return result;
		
		
	}
	
	
	
	 public boolean updateVehicle(VehicleDO vehicleDO)
	 {
		 try{
				hibernateTemplate.update(vehicleDO);
				return true;
			}catch(Exception e)
			{
				e.printStackTrace();
				return false;
			}
			
	 }
	 
	 public boolean updateVehicleOwner(VehicleOwnerDO vehicleOwnerDO)
	 {
		 try{
				hibernateTemplate.update(vehicleOwnerDO);
				return true;
			}catch(Exception e)
			{
				e.printStackTrace();
				return false;
			}
			
	 }
	 
	 @SuppressWarnings("unchecked")
	 @Override
	public List<VehicleOwnerBO> findVehicleOwnersByPlate(String plateNo) 
	{
			StringBuffer queryString = new StringBuffer();
			
			queryString.append("Select new fsl.ta.toms.roms.bo.VehicleOwnerBO(o) from VehicleCheckResultOwnerDO o where o.vehicleCheckResult.plateRegNo = '" + plateNo +"'");
			
			return hibernateTemplate.find(queryString.toString());
			
	}
	 
	 
	 
	 public Serializable saveVehicle(VehicleDO vehicleDO)
	 {
			return hibernateTemplate.save(vehicleDO);
	 }
	 
	 
	 
	 
	 @SuppressWarnings("unchecked")
	 @Override
	public List<VehicleCheckResultOwnerDO> findVehicleOwnersForCheck(Integer vehicleCheckId) 
	{
			StringBuffer queryString = new StringBuffer();
			
			queryString.append(" from VehicleCheckResultOwnerDO o where o.vehicleCheckResult.vehicleCheckId = " + vehicleCheckId);
			
			return hibernateTemplate.find(queryString.toString());
			
	}
	 
	 
	 @SuppressWarnings("unchecked")
	 @Override
	public List<VehicleInsuranceDO> findVehicleInsuranceForCheck(Integer vehicleCheckId) 
	{
			StringBuffer queryString = new StringBuffer();
			
			queryString.append(" from VehicleInsuranceDO i where i.vehicleCheckResult.vehicleCheckId = " + vehicleCheckId);
			
			return hibernateTemplate.find(queryString.toString());
			
	}
	 
	 
	@SuppressWarnings("unchecked")
	@Override
	public List<VehicleFitnessDO> findVehicleFitnessForCheck(Integer vehicleCheckId) 
	{
			StringBuffer queryString = new StringBuffer();
			
			queryString.append(" from VehicleFitnessDO f where f.vehicleCheckResult.vehicleCheckId = " + vehicleCheckId);
			
			return hibernateTemplate.find(queryString.toString());
			
	}
	 
	 /*****************************************************************************************************************/
	 
	 public boolean updateCompliance(ComplianceDO complianceDO)
	 {
		 try{
				hibernateTemplate.update(complianceDO);
				return true;
			}catch(Exception e)
			{
				e.printStackTrace();
				return false;
			}
			
	 }
	 
	 
	 public Serializable saveCompliance(ComplianceDO complianceDO)
	 {
			return hibernateTemplate.save(complianceDO);
	 }
	 
	 
	 /*****************************************************************************************************************/
	 
	 public List<ComplianceBO> lookupRoadComplianceActivities(RoadCompliancyCriteriaBO criteria) {
		 	
		 	StringBuffer queryString = new StringBuffer("select new fsl.ta.toms.roms.bo.ComplianceBO(c) from ComplianceDO c where 1=1");
		 
			//TRN
			if (StringUtils.trimToNull(criteria.getTrn()) != null) 
			{queryString.append(" and c.person.trnNbr = '").append(criteria.getTrn()).append("'"); }
				
			//FirstName
			if (StringUtils.trimToNull(criteria.getFirstName()) != null) 
			{queryString.append(" and upper(c.person.firstName) LIKE '").append(criteria.getFirstName().trim().toUpperCase()).append("%'");}
					
			//MioddleName
			if (StringUtils.trimToNull(criteria.getMiddleName()) != null) 
			{queryString.append(" and upper(c.person.middleName) LIKE '").append(criteria.getMiddleName().trim().toUpperCase()).append("%'");}
			
			//LastName
			if (StringUtils.trimToNull(criteria.getLastName()) != null) 
			{queryString.append(" and upper(c.person.lastName) LIKE '").append(criteria.getLastName().trim().toUpperCase()).append("%'");}
			
			
			//Operation Name
			if (StringUtils.trimToNull(criteria.getOperationName()) != null) 
			{queryString.append(" and upper(c.roadOperation.operationName) like '%").append(criteria.getOperationName().trim().toUpperCase()).append("%'");}
			
			//TA Staff
			if (StringUtils.trimToNull(criteria.getStaffID()) != null) 
			{queryString.append(" and c.taStaff.staffId = '").append(criteria.getStaffID().trim()).append("'");}
			
			
			//Region
			if (StringUtils.trimToNull(criteria.getRegion()) != null) 
			{queryString.append(" and upper(c.compliancyArtery.location.parish.officeLocationCode) = '").append(criteria.getRegion().trim().toUpperCase()).append("'");}
			
			
			//Compliance Id
			if (criteria.getComplianceId() != null && criteria.getComplianceId() > 1) 
			{queryString.append(" and c.complianceId = ").append(criteria.getComplianceId()); }
			
			//Road Operation ID
			if(criteria.getOperationID() != null && criteria.getOperationID() > 0)
				queryString.append(" and c.roadOperation.roadOperationId = '").append(criteria.getOperationID()).append("'");
			
			//Status
			if (StringUtils.trimToNull(criteria.getStatus()) != null) 
			{
				queryString.append(" and c.status.statusId = '").append(criteria.getStatus()).append("'");
			
			}
			
			
			//Operation Start Date
			// Road Check Date
			try {
					/**SCHEDULED**/
					if (criteria.getScheduledStartDateRange() != null && criteria.getScheduledEndDateRange() != null)  
					{
						
						queryString.append(" and (date(c.roadOperation.scheduledStartDtime) between '")
						.append(DateUtils.utilDateToSqlDate(criteria.getScheduledStartDateRange()))
						.append("' and '")
						.append(DateUtils.utilDateToSqlDate(criteria.getScheduledEndDateRange()))
						
						.append("'  or  ");
						
						
						queryString.append(" date(c.roadOperation.scheduledEndDtime) between '")
						.append(DateUtils.utilDateToSqlDate(criteria.getScheduledStartDateRange()))
						.append("' and '")
						.append(DateUtils.utilDateToSqlDate(criteria.getScheduledEndDateRange())).append("')");
					}
					
					if (criteria.getActualStartDateRange() != null && criteria.getActualEndDateRange() != null)  
					{
						queryString.append(" and (date(c.roadOperation.actualStartDtime) between '")
						.append(DateUtils.utilDateToSqlDate(criteria.getActualStartDateRange()))
						.append("' and '")
						.append(DateUtils.utilDateToSqlDate(criteria.getActualEndDateRange()))
						
						.append("'  or  ");
						
						
						queryString.append(" date(c.roadOperation.actualEndDtime) between '")
						.append(DateUtils.utilDateToSqlDate(criteria.getActualStartDateRange()))
						.append("' and '")
						.append(DateUtils.utilDateToSqlDate(criteria.getActualEndDateRange())).append("')");
						
						
						
						
					}
					
					if (criteria.getRoadCheckStartDateRange() != null && criteria.getRoadCheckEndDateRange() != null)  
					{
						
						queryString.append(" and (date(c.offenceDateTime) between '")
						.append(DateUtils.utilDateToSqlDate(criteria.getRoadCheckStartDateRange()))
						.append("' and '")
						.append(DateUtils.utilDateToSqlDate(criteria.getRoadCheckEndDateRange()))
						.append("') ");
						
					}
					
					queryString.append(" order by c.auditEntry.createDTime DESC");
				
				} catch (ParseException e) {
					
					e.printStackTrace();
				}
				//System.out.println("This is the query string " + queryString.toString());
				return hibernateTemplate.find(queryString.toString());
}


	 
	 
	 
	 public List<RoadCheckOffenceOutcomeBO> getOffenceOutcomeforCompliance(Integer complianceId)
	 {
		 
		 	StringBuffer queryString = new StringBuffer("select new fsl.ta.toms.roms.bo.RoadCheckOffenceOutcomeBO(r) from RoadCheckOffenceOutcomeDO r ");
			
			queryString.append(" where r.roadCheckOffence.roadCheck.compliance.complianceId = " + complianceId);
			
			return hibernateTemplate.find(queryString.toString());
		 
		 
	 }
	 
	 
	 public List<OffenceBO> getOffencesForDocument(Integer documentId, String docType, Integer complianceID)
	 {

		 if(docType.equals(Constants.DocumentType.SUMMONS))		
		 {	 
			 StringBuffer queryString = new StringBuffer("select new fsl.ta.toms.roms.bo.OffenceBO(o.roadCheckOffenceOutcome.roadCheckOffence.offence)" );
			 
			 queryString.append(" from SummonsDO o where o.summonsId =");
			 
			 queryString.append(documentId);
			 
			 return hibernateTemplate.find(queryString.toString());
		 }else
		 {
		 	StringBuffer queryString = new StringBuffer("select new fsl.ta.toms.roms.bo.OffenceBO(o.roadCheckOffence.offence)" );
		 
		 	queryString.append(" from RoadCheckOffenceOutcomeDO o where o.roadCheckOffence.roadCheck.compliance.complianceId =" + complianceID); 
		 	
		 	
		 	if(docType.equals(Constants.DocumentType.WARNING_NOTICE))		
		 	{	queryString.append(" and o.outcomeType.outcomeTypeId in ('" + OutcomeType.ISSUE_WARNING_NOTICE + "','" + OutcomeType.VEHICLE_SEIZURE + "')");}
			
		 	if(docType.equals(Constants.DocumentType.WARNING_NOTICE_NO_PROSECUTION))		
		 	{	queryString.append(" and o.outcomeType.outcomeTypeId in ('" + OutcomeType.WARNED_NO_PROSECUTION + "')");}
		 	
			
			
			return hibernateTemplate.find(queryString.toString());
		 
		 } 
	 }
	 
	 
	 public List<DocumentViewBO> getDocumentsforCompliance(Integer complianceId)
	 {
		 List<DocumentViewBO> result = new ArrayList<DocumentViewBO>();
		 
		 //Get Summons Info
		 StringBuffer queryString = new StringBuffer("select new fsl.ta.toms.roms.bo.DocumentViewBO(s) " +  
		 		"from SummonsDO s ");
		 
		 queryString.append(" LEFT JOIN s.justiceOfPeace LEFT JOIN s.justiceOfPeace.person");	
		 queryString.append(" where s.roadCheckOffenceOutcome.roadCheckOffence.roadCheck.compliance.complianceId = " + complianceId);
			
		 result.addAll(hibernateTemplate.find(queryString.toString()));
		
		 
		 //Get Warning Notice Info
		 queryString = new StringBuffer("select new fsl.ta.toms.roms.bo.DocumentViewBO( s "+
		 		") " +  
		 		"from WarningNoticeDO s ");
			
		 queryString.append(" where s.roadCheckOffenceOutcome.roadCheckOffence.roadCheck.compliance.complianceId = " + complianceId);
			
		 result.addAll(hibernateTemplate.find(queryString.toString()));
		 
		 
		 //Get Warning No Prosecution  Info
		 queryString = new StringBuffer("select new fsl.ta.toms.roms.bo.DocumentViewBO( s "+
		 		") " +  
		 		"from WarningNoProsecutionDO s ");
			
		 queryString.append(" where s.roadCheckOffenceOutcome.roadCheckOffence.roadCheck.compliance.complianceId = " + complianceId);
			
		 result.addAll(hibernateTemplate.find(queryString.toString()));
		 
		 
		 return result;
	 }
	 
	
	 
	 
	 public WreckingCompanyBO getWreckingCoforCompliance(Integer complianceId)
	 {
		 List<WreckingCompanyBO> list = new ArrayList<WreckingCompanyBO>();
		 
		 StringBuffer queryString = new StringBuffer("select new fsl.ta.toms.roms.bo.WreckingCompanyBO(w.wreckingCompany) from WarningNoticeDO w ");
			
		 queryString.append(" where w.roadCheckOffenceOutcome.roadCheckOffence.roadCheck.compliance.complianceId = " + complianceId);
			
		 list =  hibernateTemplate.find(queryString.toString());
		 
			
		if(list!=null && list.size()==1){
			return list.get(0);
		}
			
			return null;
	 }
	 
	 
	 public PoundBO getPoundforCompliance(Integer complianceId)
	 {
		 List<PoundBO> list = new ArrayList<PoundBO>();
		 
		 StringBuffer queryString = new StringBuffer("select new fsl.ta.toms.roms.bo.PoundBO(w.pound) from WarningNoticeDO w ");
			
		 queryString.append(" where w.roadCheckOffenceOutcome.roadCheckOffence.roadCheck.compliance.complianceId = " + complianceId);
			
		 list =  hibernateTemplate.find(queryString.toString());
		 
			
			if(list!=null && list.size()==1){
				return list.get(0);
			}
				
				return null;
		 
	 }
	 
	 
	 public List<PersonBO> getWitnessesforCompliance(Integer complianceId)
	 {
		 
		 StringBuffer queryString = new StringBuffer("select distinct new fsl.ta.toms.roms.bo.PersonBO(w.pk.witness) from WitnessWarningNoticeDO w ");
			
		 queryString.append(" where w.pk.warningNotice.roadCheckOffenceOutcome.roadCheckOffence.roadCheck.compliance.complianceId  = " + complianceId);
			
		 return hibernateTemplate.find(queryString.toString());
		 
		 
	 }
	 
	 
	 public List<CourtBO> getCourtforCompliance(Integer complianceId)
	 {
		 
		 StringBuffer queryString = new StringBuffer("select new fsl.ta.toms.roms.bo.CourtBO(c.court) from CourtAppearanceDO c ");
			
		 queryString.append(" where c.courtCase.summons.roadCheckOffenceOutcome.roadCheckOffence.roadCheck.compliance.complianceId  = " + complianceId);
			
		 return hibernateTemplate.find(queryString.toString());
		 
		 
	 }

	 public List<ExcerptParamMappingBO> findParamsforOffence(String offences)
	 {
		 
		 StringBuffer queryString = new StringBuffer("select distinct new fsl.ta.toms.roms.bo.ExcerptParamMappingBO(o.offenceParamKey.excerptParamMapping) " +
		 		"from OffenceParamDO o ");
			
		 queryString.append(" where o.offenceParamKey.offence.offenceId  in (" + offences + ")");
			
		 return hibernateTemplate.find(queryString.toString());
		 
		 
	 }
	 
	 
	 public List<OffenceBO> getOffencesforCompliance(Integer complianceId)
	 {
		 
		 StringBuffer queryString = new StringBuffer("select distinct new fsl.ta.toms.roms.bo.OffenceBO(o.offence) " +
		 		"from RoadCheckOffenceDO o ");
			
		 queryString.append(" where o.roadCheck.compliance.complianceId  = " + complianceId);
			
		 return hibernateTemplate.find(queryString.toString());
		 
		 
	 }
	 
	 
	 public List<RoadCheckBO> getRoadChecksforCompliance(Integer complianceId)
	 {
		 
		 StringBuffer queryString = new StringBuffer("select distinct new fsl.ta.toms.roms.bo.RoadCheckBO(r) " +
		 		"from RoadCheckDO r ");
			
		 queryString.append(" where r.compliance.complianceId  = " + complianceId);
		 
		 queryString.append(" and r.roadCheckId in (select max(rInner.roadCheckId) from RoadCheckDO rInner where rInner.compliance.complianceId  = " + complianceId+ 
				 " GROUP BY rInner.roadCheckType.roadCheckTypeId)");
			
		 return hibernateTemplate.find(queryString.toString());
		 
		 
	 }




	@SuppressWarnings("unchecked")
	@Override
	public List<OffenceMandatoryOutcomeBO> getActiveOffenceMandatoryOutcomeList() {
List<OffenceMandatoryOutcomeBO> offenceMandatoryOutcomeList = new ArrayList<OffenceMandatoryOutcomeBO>();
		
		String queryString ="select new fsl.ta.toms.roms.bo.OffenceMandatoryOutcomeBO(o)"+
		"from OffenceMandatoryOutcomeDO o" +
				" where o.status.statusId = :statusId";
		 
		
		offenceMandatoryOutcomeList = hibernateTemplate.findByNamedParam(queryString, "statusId", Constants.Status.ACTIVE);
			
		if(offenceMandatoryOutcomeList==null || offenceMandatoryOutcomeList.size()==0){
			return null;
		}
		
		return offenceMandatoryOutcomeList;
	}
	 
	 

	 public boolean deletePreviousOwners(Integer vehicleID)
	 {
		 
		 StringBuffer queryString = new StringBuffer();
			
		queryString.append(" from VehicleOwnerDO v where v.vehicle.vehicleId = " + vehicleID);
			
		List<VehicleOwnerDO> vehOwners = hibernateTemplate.find(queryString.toString());

		try{
			if(vehOwners != null)
			{
				for (VehicleOwnerDO vehicleOwnerDO : vehOwners) 
				{
					hibernateTemplate.delete(vehicleOwnerDO);
				}
				
				 return true;
			}else
			{
				return false;
			}
		}catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
		

	 }




	@Override
	public BadgeCheckResultBO getBadgeCheckResult(Integer complianceId) 
	{
		/*Specify search criteria for search*/
		Criteria criteria = this.hibernateTemplate.getSessionFactory().getCurrentSession().createCriteria(BadgeCheckResultDO.class,"badgeCheckRes");
		
		/*List of all aliases used*/
		criteria.createAlias("badgeCheckRes.roadCheck", "roadCheck");
		criteria.createAlias("roadCheck.compliance", "compliance");
		
		//Add Ordering to get latest resutl
		criteria.addOrder(Order.desc("badgeCheckRes.auditEntry.createDTime"));
		
		criteria.add(Restrictions.eq("compliance.complianceId", complianceId));
		
		BadgeCheckResultDO badgeCheckResultDO = null;
		
		ArrayList<BadgeCheckResultDO> list = (ArrayList<BadgeCheckResultDO>) criteria.list();
		
		if(list.size() > 0)
			 badgeCheckResultDO = list.get(0);
		
		
		
		if( badgeCheckResultDO != null)
			return  new BadgeCheckResultBO(badgeCheckResultDO, null);
		else
			return null;
		
		
		
		
		
	}




	@Override
	public RoadLicCheckResultBO getRoadLicCheckResult(Integer complianceId) 
	{
		/*Specify search criteria for search*/
		Criteria criteria = this.hibernateTemplate.getSessionFactory().getCurrentSession().createCriteria(RoadLicCheckResultDO.class,"RoadLicCheckRes");
		
		/*List of all aliases used*/
		criteria.createAlias("RoadLicCheckRes.roadCheck", "roadCheck");
		criteria.createAlias("roadCheck.compliance", "compliance");
		
		criteria.add(Restrictions.eq("compliance.complianceId", complianceId));
		
		//Order by to get latest check result
		criteria.addOrder(Order.desc("RoadLicCheckRes.auditEntry.createDTime"));
		
		RoadLicCheckResultDO roadLicCheckResultDO = null ;
		
		ArrayList<RoadLicCheckResultDO> list = (ArrayList<RoadLicCheckResultDO>) criteria.list();
		
		
		ArrayList<LicenceOwnerDO> listLicOwners = new ArrayList<LicenceOwnerDO>();
		if(list.size() > 0)
		{
			roadLicCheckResultDO = list.get(0);
			
			Criteria criteriaRoadLicOwners = this.hibernateTemplate.getSessionFactory().getCurrentSession().createCriteria(LicenceOwnerDO.class,"licOwn");
			criteriaRoadLicOwners.createAlias("licOwn.roadLicCheck", "roadLicCheck");
			criteriaRoadLicOwners.add(Restrictions.eq("roadLicCheck.roadLicCheckId", roadLicCheckResultDO.getRoadLicCheckId()));
			
			listLicOwners = (ArrayList<LicenceOwnerDO>) criteriaRoadLicOwners.list();
			
			
		}
		
		RoadLicCheckResultBO roadLicCheckBO = null;
		
		if( roadLicCheckResultDO != null && roadLicCheckResultDO.getLicenceNo() != null && roadLicCheckResultDO.getLicenceNo() > 0)
		{
			roadLicCheckBO = new RoadLicCheckResultBO(roadLicCheckResultDO);
			
			List<RoadLicenceOwnerBO> licenceOwners = new ArrayList<RoadLicenceOwnerBO>(); 
			for(LicenceOwnerDO licOwner : listLicOwners)
			{
				//RoadLicenceOwnerBO roadLicOwner = new RoadLicenceOwnerBO(roadLicCheckBO.getLicenceNo(), licOwner.getFirstName(), licOwner.getLastName());
				
				RoadLicenceOwnerBO roadLicOwner = new RoadLicenceOwnerBO(licOwner);
				licenceOwners.add(roadLicOwner );
			}
			
			roadLicCheckBO.setRoadLicenceOwners(licenceOwners);
			roadLicCheckBO.setStatus(roadLicCheckResultDO.getStatus_desc());
			
			LMIS_licenceView licenceDO = this.find(LMIS_licenceView.class, roadLicCheckBO.getLicenceNo());
			
			if(licenceDO != null)
			{
				roadLicCheckBO.setSeatCapacity(licenceDO.getSeatCapacity());
				roadLicCheckBO.setRouteEnd(licenceDO.getRouteEnd());
				roadLicCheckBO.setRouteStart(licenceDO.getRouteStart());
			}
			
			roadLicCheckBO.setFitnessExpDate(roadLicCheckResultDO.getFitnessExpDate());
			roadLicCheckBO.setFitnessIssueDate(roadLicCheckResultDO.getFitnessIssueDate());
			roadLicCheckBO.setFitnessNumber(roadLicCheckResultDO.getFitnessNbr());
			
			roadLicCheckBO.setInsuranceComp(roadLicCheckResultDO.getInsuranceComp());
			roadLicCheckBO.setInsuranceExpDate(roadLicCheckResultDO.getInsuranceExpDate());
			roadLicCheckBO.setInsuranceIssueDate(roadLicCheckResultDO.getInsuranceIssueDate());
			
			roadLicCheckBO.setIssueDate(roadLicCheckResultDO.getIssueDate());
			roadLicCheckBO.setControlNumber(roadLicCheckResultDO.getControlNbr() != null ? roadLicCheckResultDO.getControlNbr().toString() : null);
			
			
			List<LicenceDriverConductorDO> driverAndConductorsDO = new ArrayList<LicenceDriverConductorDO>();
			
			Criteria criteriaDriveConduct = this.hibernateTemplate.getSessionFactory().getCurrentSession().createCriteria(LicenceDriverConductorDO.class,"lcd");
			criteriaDriveConduct.createAlias("lcd.roadLicCheck", "rlc");
			criteriaDriveConduct.add(Restrictions.eq("rlc.roadLicCheckId", roadLicCheckResultDO.getRoadLicCheckId()));
			
			driverAndConductorsDO = criteriaDriveConduct.list();
			
			if(driverAndConductorsDO != null && driverAndConductorsDO.size() > 0)
			{
				for(LicenceDriverConductorDO ldcDO : driverAndConductorsDO)
				{
					RoadLicenceDriverConductorBO roadLicDriverConductorBO = new RoadLicenceDriverConductorBO();
					roadLicDriverConductorBO.setBadgeNumber(ldcDO.getBadgeNumber());
					roadLicDriverConductorBO.setBadgeType(ldcDO.getBadgeType());
					roadLicDriverConductorBO.setFirstName(ldcDO.getFirstName());
					roadLicDriverConductorBO.setLastName(ldcDO.getLastName());
					
					if(roadLicCheckBO.getAssignedDriverConductor() == null)
					{
						roadLicCheckBO.setAssignedDriverConductor(new ArrayList<RoadLicenceDriverConductorBO>());
					}
					
					roadLicCheckBO.getAssignedDriverConductor().add(roadLicDriverConductorBO);
					
				}
				
			}
			
			
					
			return  roadLicCheckBO;
		}
		else if(roadLicCheckResultDO != null && roadLicCheckResultDO.getApplNo() != null && roadLicCheckResultDO.getApplNo() > 0){
			
			roadLicCheckBO = new RoadLicCheckResultBO(roadLicCheckResultDO);
			
			return  roadLicCheckBO;
		}
		else
			return null;
	}




	@Override
	public VehicleCheckResultDO getVehicleCheckResult(Integer complianceId) 
	{
		/*Specify search criteria for search*/
		Criteria criteria = this.hibernateTemplate.getSessionFactory().getCurrentSession().createCriteria(VehicleCheckResultDO.class,"VehicleCheckRes");
		
		/*List of all aliases used*/
		criteria.createAlias("VehicleCheckRes.roadCheck", "roadCheck");
		criteria.createAlias("roadCheck.compliance", "compliance");
		
		criteria.add(Restrictions.eq("compliance.complianceId", complianceId));
		
		//Order by create date to get latest result
		criteria.addOrder(Order.desc("VehicleCheckRes.auditEntry.createDTime"));
		
		VehicleCheckResultDO vehicleCheckResultDO = null;
		
		ArrayList<VehicleCheckResultDO> list = (ArrayList<VehicleCheckResultDO>) criteria.list();
		
		if(list.size() > 0)
			vehicleCheckResultDO = list.get(0);
		
		
		if( vehicleCheckResultDO != null)
			return  vehicleCheckResultDO;
		else
			return null;
	}




	@Override
	public DLCheckResultBO getDLCheckResult(Integer complianceId) 
	{
		/*Specify search criteria for search*/
		Criteria criteria = this.hibernateTemplate.getSessionFactory().getCurrentSession().createCriteria(DLCheckResultDO.class,"DLCheckRes");
		
		/*List of all aliases used*/
		criteria.createAlias("DLCheckRes.roadCheck", "roadCheck");
		criteria.createAlias("roadCheck.compliance", "compliance");
		
		criteria.add(Restrictions.eq("compliance.complianceId", complianceId));
		
		//Order by create date to get latest result
		criteria.addOrder(Order.desc("DLCheckRes.auditEntry.createDTime"));
		
		DLCheckResultDO dlCheckResultDO = null;
		
		ArrayList<DLCheckResultDO> list = (ArrayList<DLCheckResultDO>) criteria.list();
		
		if(list.size() > 0)
			dlCheckResultDO = list.get(0);
		
		
		if( dlCheckResultDO != null)
			return  new DLCheckResultBO(dlCheckResultDO);
		else
			return null;
	}
	
	@Override
	public CitationCheckDO getCitationCheckDO(Integer complianceId) 
	{
		/*Specify search criteria for search*/
		Criteria criteria = this.hibernateTemplate.getSessionFactory().getCurrentSession().createCriteria(CitationCheckDO.class,"CitationCheck");
		
		/*List of all aliases used*/
		criteria.createAlias("CitationCheck.roadCheck", "roadCheck");
		criteria.createAlias("roadCheck.compliance", "compliance");
		
		criteria.add(Restrictions.eq("compliance.complianceId", complianceId));
		
		CitationCheckDO citationCheckDO = null;
		
		ArrayList<CitationCheckDO> list = (ArrayList<CitationCheckDO>) criteria.list();
		
		if(list.size() > 0)
			citationCheckDO = list.get(0);
		
		
		if( citationCheckDO != null)
			return  citationCheckDO;
		else
			return null;
	}




	@Override
	/**
	 * @author oanguin
	 * This function searches the database to see if a person exists with the drivers licenses number in the database.
	 * If no person with that drivers licenses is found then NULL is returned
	 */
	public PersonDO getPersonFromDriversLicense(String dlNo)
	{
		/*Specify search criteria for search*/
		Criteria criteria = this.hibernateTemplate.getSessionFactory().getCurrentSession().createCriteria(PersonDO.class,"person");
		
		criteria.add(Restrictions.eq("dlNo", dlNo));
		
		@SuppressWarnings("unchecked")
		List<PersonDO> persons = (ArrayList<PersonDO>)criteria.list();
		
		if(persons.size() > 0)
			return persons.get(0);
		else
			return null;
		
		
		
		
	}




	@Override
	public WarningNoticeDO getWarningNoticeForRoadCheck(Integer complianceId)
	{
		// TODO Auto-generated method stub
		/*Specify search criteria for search*/
		Criteria criteria = this.hibernateTemplate.getSessionFactory().getCurrentSession().createCriteria(WarningNoticeDO.class,"wn");
		
		/*Add aliases*/
		criteria.createAlias("wn.roadCheckOffenceOutcome", "roadOffOut");
		criteria.createAlias("roadOffOut.roadCheckOffence", "roadOff");
		criteria.createAlias("roadOff.roadCheck", "roadCh");
		criteria.createAlias("roadCh.compliance", "comp");
		
		criteria.add(Restrictions.eq("comp.complianceId", complianceId));
		
		List<WarningNoticeDO> warningNotices = criteria.list();
		
		if(warningNotices.size() > 0)
			return warningNotices.get(0);
		else
			return null;
	}




	@Override
	public List<ComplianceParamBO> getCompliancePamsforCompliance(
			Integer complianceId) {
		StringBuffer queryString = new StringBuffer();
		
		queryString.append("Select new fsl.ta.toms.roms.bo.ComplianceParamBO(o) from ComplianceParamDO o where o.complianceParamKey.complianceDO.complianceId = " + complianceId);
		
		return hibernateTemplate.find(queryString.toString());
		
	}




	@Override
	public Date getMinCourtAppearnceDateForCompliancy(Integer complianceId)
	{
		Criteria criteria = this.hibernateTemplate.getSessionFactory().getCurrentSession().createCriteria(CourtAppearanceDO.class, "courtAp");
		
		//create aliases
		criteria.createAlias("courtAp.courtCase", "courtCase");
		criteria.createAlias("courtCase.summons", "summons");
		criteria.createAlias("summons.roadCheckOffenceOutcome", "roadCkOffOut");
		criteria.createAlias("roadCkOffOut.outcomeType", "outcome");
		criteria.createAlias("roadCkOffOut.roadCheckOffence", "roadChkOff");
		criteria.createAlias("roadChkOff.roadCheck", "roadCheck");
		criteria.createAlias("roadCheck.compliance", "compliance");
		
		criteria.add(Restrictions.eq("compliance.complianceId", complianceId));
		criteria.add(Restrictions.eq("outcome.outcomeTypeId", Constants.OutcomeType.ISSUE_SUMMONS));
		
		criteria.setProjection(Projections.min("courtDTime"));
		
		return (Date)criteria.uniqueResult();
		
	}

	@Override
	public List<ConfigurationBO> getMaxCitationValues()
	{
		Criteria criteria = this.hibernateTemplate.getSessionFactory().getCurrentSession().createCriteria(ConfigurationDO.class, "config");
		
		criteria.add(Restrictions.in("config.cfgCode", new String[]{Constants.Configuration.MAX_ROMS_OFFENCES,Constants.Configuration.MAX_TTMS_OFFENCES}));
		
		List<ConfigurationDO> configDOs = criteria.list();
		
		List<ConfigurationBO> configBOs = new ArrayList<ConfigurationBO>();
		
		for(ConfigurationDO configDO : configDOs)
		{
			configBOs.add(new ConfigurationBO(configDO));
		}
		
		return configBOs;
	}

	@Override
	public Integer getTotalROMSOffences(String trn, String dln, String... plateNumber){
		
		if(trn == null && dln == null && plateNumber == null){
			return null;
		}
		Criteria criteria = this.hibernateTemplate.getSessionFactory().getCurrentSession().createCriteria(ROMS_CitationOffenceView.class, "roms_cit_view");
		
		if(trn != null && dln != null){
			criteria.add(Restrictions.or(Restrictions.eq("trnNbr", trn).ignoreCase(), Restrictions.eq("dlNo", dln).ignoreCase()) );
		}
		else if(trn != null){
			criteria.add(Restrictions.eq("trnNbr", trn).ignoreCase() );
		}
		else if(dln != null){
			criteria.add(Restrictions.eq("dlNo", dln).ignoreCase() );
		}
		else if(plateNumber != null && plateNumber[0] != null){
			criteria.add(Restrictions.eq("plateNumber", plateNumber[0]).ignoreCase() );
		}
		
		//Case restriction based on discrepancy 212 - This allows correct total count to be returned by not counting documents other than summons
		criteria.add(Restrictions.isNotNull("caseStatus"));
		criteria.setProjection(Projections.rowCount());
		
		return (Integer)criteria.uniqueResult();
		
		
	}





	/**
	 * Needed for document view screens 
	 */
	@Override
	public List<VehicleOwnerDO> findVehicleOwnersByTRN(String trnNo, String branch) {
	
		List<VehicleOwnerDO> vehicleDOList = new ArrayList<VehicleOwnerDO>();
		
		String queryString = "select distinct o from VehicleOwnerDO o" +
				" where o.trnNbr = :trnNo and o.trnBranch = :branch ";
		
		
		String[] paramNames = {"trnNo", "branch"};
		Object[] values = {trnNo, branch};
		
		vehicleDOList = hibernateTemplate.findByNamedParam(queryString,paramNames, values);
		
		return vehicleDOList;
	}

	@Override
	public WarningNoProsecutionDO getWarningNoProsecutionForRoadCheck(
			Integer complianceId) {
		// TODO Auto-generated method stub
		/* Specify search criteria for search */
		Criteria criteria = this.hibernateTemplate.getSessionFactory()
				.getCurrentSession()
				.createCriteria(WarningNoProsecutionDO.class, "wn");

		/* Add aliases */
		criteria.createAlias("wn.roadCheckOffenceOutcome", "roadOffOut");
		criteria.createAlias("roadOffOut.roadCheckOffence", "roadOff");
		criteria.createAlias("roadOff.roadCheck", "roadCh");
		criteria.createAlias("roadCh.compliance", "comp");

		criteria.add(Restrictions.eq("comp.complianceId", complianceId));

		List<WarningNoProsecutionDO> warningNotices = criteria.list();

		if (warningNotices.size() > 0)
			return warningNotices.get(0);
		else
			return null;
	}




	 
	 
}

