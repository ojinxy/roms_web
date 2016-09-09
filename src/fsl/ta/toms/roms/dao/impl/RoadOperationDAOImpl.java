package fsl.ta.toms.roms.dao.impl;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import fsl.ta.toms.roms.bo.ArteryBO;
import fsl.ta.toms.roms.bo.AssignedPersonBO;
import fsl.ta.toms.roms.bo.AssignedVehicleBO;
import fsl.ta.toms.roms.bo.ITAExaminerBO;
import fsl.ta.toms.roms.bo.JPBO;
import fsl.ta.toms.roms.bo.LocationBO;
import fsl.ta.toms.roms.bo.OperationArteryBO;
import fsl.ta.toms.roms.bo.OperationLocationBO;
import fsl.ta.toms.roms.bo.OperationStrategyBO;
import fsl.ta.toms.roms.bo.ParishBO;
import fsl.ta.toms.roms.bo.PersonBO;
import fsl.ta.toms.roms.bo.PoliceOfficerBO;
import fsl.ta.toms.roms.bo.RoadOperationBO;
import fsl.ta.toms.roms.bo.StrategyBO;
import fsl.ta.toms.roms.bo.StrategyRuleBO;
import fsl.ta.toms.roms.bo.TAStaffBO;
import fsl.ta.toms.roms.bo.TAVehicleBO;
import fsl.ta.toms.roms.constants.Constants;
import fsl.ta.toms.roms.dao.RoadOperationDAO;
import fsl.ta.toms.roms.dataobjects.ArteryDO;
import fsl.ta.toms.roms.dataobjects.AssignedPersonDO;
import fsl.ta.toms.roms.dataobjects.AssignedVehicleDO;
import fsl.ta.toms.roms.dataobjects.AuditEntry;
import fsl.ta.toms.roms.dataobjects.ComplianceDO;
import fsl.ta.toms.roms.dataobjects.CourtAppearanceDO;
import fsl.ta.toms.roms.dataobjects.ITAExaminerDO;
import fsl.ta.toms.roms.dataobjects.JPDO;
import fsl.ta.toms.roms.dataobjects.LocationDO;
import fsl.ta.toms.roms.dataobjects.OperationRegionDO;
import fsl.ta.toms.roms.dataobjects.OperationStrategyDO;
import fsl.ta.toms.roms.dataobjects.OperationStrategyKey;
import fsl.ta.toms.roms.dataobjects.ParishDO;
import fsl.ta.toms.roms.dataobjects.PersonDO;
import fsl.ta.toms.roms.dataobjects.PoliceOfficerDO;
import fsl.ta.toms.roms.dataobjects.RoadOperationDO;
import fsl.ta.toms.roms.dataobjects.StrategyDO;
import fsl.ta.toms.roms.dataobjects.SummonsDO;
import fsl.ta.toms.roms.dataobjects.TAStaffDO;
import fsl.ta.toms.roms.dataobjects.TAVehicleDO;
import fsl.ta.toms.roms.dataobjects.TeamArteryDO;
import fsl.ta.toms.roms.dataobjects.TeamDO;
import fsl.ta.toms.roms.dataobjects.TeamLocationDO;
import fsl.ta.toms.roms.dataobjects.TeamParishDO;
import fsl.ta.toms.roms.dataobjects.WarningNoProsecutionDO;
import fsl.ta.toms.roms.dataobjects.WarningNoticeDO;
import fsl.ta.toms.roms.exception.NoRecordFoundException;
import fsl.ta.toms.roms.search.criteria.impl.AvailableResourceCriteriaBO;
import fsl.ta.toms.roms.search.criteria.impl.RoadOperationCriteriaBO;
import fsl.ta.toms.roms.util.DateUtils;
import fsl.ta.toms.roms.util.StringUtil;
import fsl.ta.toms.roms.webservices.RoadOperation;

public class RoadOperationDAOImpl extends ParentDAOImpl implements RoadOperationDAO{




	@SuppressWarnings("unchecked")
	@Override
	public List<RoadOperationBO> lookupRoadOperation(
			RoadOperationCriteriaBO roadOperationCriteriaBO) {
		
			List<RoadOperationBO> roadOperationList = new ArrayList<RoadOperationBO>();
			
			StringBuffer queryString = new StringBuffer("select new fsl.ta.toms.roms.bo.RoadOperationBO(o) from RoadOperationDO o");
					
					queryString.append(" where o.roadOperationId in (select distinct o.roadOperationId from RoadOperationDO o");
					if(roadOperationCriteriaBO.getStrategyId()!=null && roadOperationCriteriaBO.getStrategyId()>0){
						queryString.append(", OperationStrategyDO s");
					}
					if(StringUtils.trimToNull(roadOperationCriteriaBO.getParishCode())!=null || (roadOperationCriteriaBO.getLocationId()!=null && roadOperationCriteriaBO.getLocationId()>0)){
						queryString.append(", TeamLocationDO l");
					}
					if(roadOperationCriteriaBO.getArteryId()!=null && roadOperationCriteriaBO.getArteryId()>0){
						queryString.append(", TeamArteryDO a");
					}
					if(StringUtils.trimToNull(roadOperationCriteriaBO.getTeamLeadStaffId()) != null || (roadOperationCriteriaBO.getArteryId()!=null && roadOperationCriteriaBO.getArteryId()>0) || (StringUtils.trimToNull(roadOperationCriteriaBO.getParishCode())!=null || (roadOperationCriteriaBO.getLocationId()!=null && roadOperationCriteriaBO.getLocationId()>0))){
						queryString.append(", TeamDO t");
					}
			queryString.append(" where (o.roadOperationId is not null)");
			
			if(roadOperationCriteriaBO.getRoadOperationId() != null && roadOperationCriteriaBO.getRoadOperationId()>0){
				queryString.append(" and o.roadOperationId = ").append(roadOperationCriteriaBO.getRoadOperationId());
			}
			
			//Operation Name
			if (StringUtils.trimToNull(roadOperationCriteriaBO.getOperationName()) != null) {
				
				queryString.append(" and upper(o.operationName) like '%").append(StringUtil.quoteReplace(roadOperationCriteriaBO.getOperationName().trim()).toUpperCase()).append("%'");
			}
			
			//Category
			if (StringUtils.trimToNull(roadOperationCriteriaBO.getCategoryId()) != null) {
				
				queryString.append(" and upper(o.category.categoryId) = '").append(StringUtil.quoteReplace(roadOperationCriteriaBO.getCategoryId().trim()).toUpperCase()).append("'");
			}
			
			//Strategy
			if (roadOperationCriteriaBO.getStrategyId() != null && roadOperationCriteriaBO.getStrategyId()>0) {
				queryString.append(" and o.roadOperationId = s.operationStrategyKey.roadOperation.roadOperationId and s.operationStrategyKey.strategy.strategyId = ").append(roadOperationCriteriaBO.getStrategyId());
			}
			
			//Team Lead
			if (StringUtils.trimToNull(roadOperationCriteriaBO.getTeamLeadStaffId()) != null) {
				
				//queryString.append(" and upper(o.teamLead.staffId) = '").append(StringUtil.quoteReplace(roadOperationCriteriaBO.getTeamLeadStaffId().trim()).toUpperCase()).append("'");
				queryString.append(" and o.roadOperationId = t.roadOperation.roadOperationId and t.teamLead.staffId ='").append(StringUtil.quoteReplace(roadOperationCriteriaBO.getTeamLeadStaffId().trim()).toUpperCase()).append("'");
			}
			
			//Scheduler
			if (StringUtils.trimToNull(roadOperationCriteriaBO.getSchedulerStaffId()) != null) {
				
				queryString.append(" and upper(o.scheduler.staffId) = '").append(StringUtil.quoteReplace(roadOperationCriteriaBO.getSchedulerStaffId().trim()).toUpperCase()).append("'");
			}
			
			//Office Location
			if (StringUtils.trimToNull(roadOperationCriteriaBO.getOfficeLocCode()) != null) {
				
				queryString.append(" and upper(o.officeLocCode) = '").append(StringUtil.quoteReplace(roadOperationCriteriaBO.getOfficeLocCode()).toUpperCase()).append("'");
			}
			
			
			//if parish or location selected
			if(StringUtils.trimToNull(roadOperationCriteriaBO.getParishCode())!=null || (roadOperationCriteriaBO.getLocationId()!=null && roadOperationCriteriaBO.getLocationId()>0)){
				queryString.append(" and o.roadOperationId = t.roadOperation.roadOperationId and t.teamId = l.operationLocationKey.team.teamId");
			}
			
			//Parish
			if (StringUtils.trimToNull(roadOperationCriteriaBO.getParishCode()) != null && (roadOperationCriteriaBO.getLocationId() == null || roadOperationCriteriaBO.getLocationId()<1)) {
				
				//queryString.append(" and o.roadOperationId = l.operationLocationKey.roadOperation.roadOperationId and upper(l.operationLocationKey.location.parish.parishCode) = '").append(StringUtil.quoteReplace(roadOperationCriteriaBO.getParishCode()).toUpperCase()).append("'");
				queryString.append("  and upper(l.operationLocationKey.location.parish.parishCode) = '").append(StringUtil.quoteReplace(roadOperationCriteriaBO.getParishCode()).toUpperCase()).append("'");
			}
			
			//Location
			if (roadOperationCriteriaBO.getLocationId() != null && roadOperationCriteriaBO.getLocationId()>0) {
				
				//queryString.append(" and o.roadOperationId = l.operationLocationKey.roadOperation.roadOperationId and l.operationLocationKey.location.locationId = ").append(roadOperationCriteriaBO.getLocationId());
				queryString.append(" and l.operationLocationKey.location.locationId = ").append(roadOperationCriteriaBO.getLocationId());

			}
			
			//Artery
			if (roadOperationCriteriaBO.getArteryId() != null && roadOperationCriteriaBO.getArteryId()>0) {
				
				queryString.append(" and o.roadOperationId = t.roadOperation.roadOperationId and t.teamId = a.operationArteryKey.team.teamId and a.operationArteryKey.artery.arteryId = ").append(roadOperationCriteriaBO.getArteryId());
			}
			
			//isBackDated is true
			if (roadOperationCriteriaBO.isBackDated() == true) {
				
				queryString.append(" and upper(o.backDated) = ").append("'Y'");
			}
			
			//isAuthorized is false
			if (roadOperationCriteriaBO.isUnAuthorized() == true) {			
				queryString.append(" and upper(o.authorized) = ").append("'N'");
			}
			
			
			//Status
			if (StringUtils.trimToNull(roadOperationCriteriaBO.getStatusId()) != null) {
				
				queryString.append(" and upper(o.status.statusId) = '").append(StringUtil.quoteReplace(roadOperationCriteriaBO.getStatusId()).toUpperCase()).append("'");
			}
			
			
			//Operation Start Date & Start Time
			if (roadOperationCriteriaBO.getOperationStartDate() != null) {
				try {
					if(StringUtils.trimToNull(roadOperationCriteriaBO.getOperationStartTime()) != null){
						String formattedDate = DateUtils.formatDate("yyyy-MM-dd", roadOperationCriteriaBO.getOperationStartDate());
						formattedDate =  formattedDate + " " + roadOperationCriteriaBO.getOperationStartTime();
					
						if(roadOperationCriteriaBO.isScheduledDTime()==true){
							queryString.append(" and o.scheduledStartDtime = '").append(formattedDate).append("'");
						}
						else{
							queryString.append(" and o.actualStartDtime = '").append(formattedDate).append("'");
						}
					}else{
						Date startDate = null;
						
							startDate = DateUtils.utilDateToSqlDate(roadOperationCriteriaBO.getOperationStartDate());
							if(roadOperationCriteriaBO.isScheduledDTime()==true){
								queryString.append(" and Date(o.scheduledStartDtime) = '").append(startDate).append("'");	
							}
							else{
								queryString.append(" and Date(o.actualStartDtime) = '").append(startDate).append("'");
							}
						
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			//Operation End Date & End Time
			if (roadOperationCriteriaBO.getOperationEndDate() != null) {
				try {
					if(StringUtils.trimToNull(roadOperationCriteriaBO.getOperationEndTime()) != null){
						String formattedDate = DateUtils.formatDate("yyyy-MM-dd", roadOperationCriteriaBO.getOperationEndDate());
						formattedDate =  formattedDate + " " + roadOperationCriteriaBO.getOperationEndTime();
					
						if(roadOperationCriteriaBO.isScheduledDTime()==true){
							queryString.append(" and o.scheduledEndDtime = '").append(formattedDate).append("'");
						}
						else{
							queryString.append(" and o.actualEndDtime = '").append(formattedDate).append("'");
						}
					}else{
						Date startDate = null;
						
						startDate = DateUtils.utilDateToSqlDate(roadOperationCriteriaBO.getOperationEndDate());
						if(roadOperationCriteriaBO.isScheduledDTime()==true){
							queryString.append(" and Date(o.scheduledEndDtime) = '").append(startDate).append("'");	
						}
						else{
							queryString.append(" and Date(o.actualEndDtime) = '").append(startDate).append("'");
						}
						
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			queryString.append(")");
			queryString.append(" order by o.scheduledStartDtime desc");
			
			roadOperationList =  hibernateTemplate.find(queryString.toString());
		 
			return roadOperationList;
		
	}

	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<AssignedPersonBO> findAssignedPerson(Integer roadOperationId) {
		List<AssignedPersonBO> assignedPersonList = new ArrayList<AssignedPersonBO>();
		
		String queryString = "select new fsl.ta.toms.roms.bo.AssignedPersonBO(o)" +
				" from AssignedPersonDO o, TeamDO t where o.assignedPersonKey.team.teamId = t.teamId and t.roadOperation.roadOperationId = :roadOperationId" + 
				" order by o.assignedPersonKey.personType.personTypeId";

		assignedPersonList = hibernateTemplate.findByNamedParam(queryString, "roadOperationId", roadOperationId);
			
		/*if(assignedPersonList==null || assignedPersonList.size()==0){
			return null;
		}*/
		
		return assignedPersonList;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<AssignedPersonBO> findAssignedPersonByTeam(Integer roadOperationId, Integer teamId) {
		List<AssignedPersonBO> assignedPersonList = new ArrayList<AssignedPersonBO>();
		
		String queryString = "select new fsl.ta.toms.roms.bo.AssignedPersonBO(o)" +
				" from AssignedPersonDO o, TeamDO t where o.assignedPersonKey.team.teamId = t.teamId and t.roadOperation.roadOperationId = :roadOperationId and t.teamId = :teamId" + 
				" order by o.assignedPersonKey.personType.personTypeId";
		 
		
		String[] paramNames = {"roadOperationId", "teamId"};
		Object[] values = {roadOperationId, teamId};
		assignedPersonList = hibernateTemplate.findByNamedParam(queryString, paramNames, values);
			
		/*if(assignedPersonList==null || assignedPersonList.size()==0){
			return null;
		}*/
		
		return assignedPersonList;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<TAStaffBO> findAssignedStaffByTeam(Integer roadOperationId, Integer teamId){
		List<TAStaffBO> assignedPersonList = new ArrayList<TAStaffBO>();
		
		String queryString = "select new fsl.ta.toms.roms.bo.TAStaffBO(s,o)" +
				" from AssignedPersonDO o,TeamDO t, TAStaffDO s where " +
				"o.assignedPersonKey.team.teamId = t.teamId and " +
				"t.roadOperation.roadOperationId = :roadOperationId and " +
				"o.assignedPersonKey.person.personId = s.person.personId" +
				" and o.assignedPersonKey.personType.personTypeId = :personType" + 
				" and t.teamId = :teamId";
		
		 
		String[] paramNames = {"roadOperationId", "personType", "teamId"};
		Object[] values = {roadOperationId, Constants.PersonType.TA_STAFF, teamId};
		assignedPersonList = hibernateTemplate.findByNamedParam(queryString, paramNames, values);
			
		/*if(assignedPersonList==null || assignedPersonList.size()==0){
			return null;
		}*/
		
		return assignedPersonList;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<TAStaffBO> findAssignedStaffForRoadOperation(Integer roadOperationId){
		List<TAStaffBO> assignedPersonList = new ArrayList<TAStaffBO>();
		
		String queryString = "select new fsl.ta.toms.roms.bo.TAStaffBO(s,o)" +
				" from AssignedPersonDO o,TeamDO t, TAStaffDO s where " +
				"t.roadOperation.roadOperationId = :roadOperationId and " +
				"o.assignedPersonKey.person.personId = s.person.personId" +
				" and o.assignedPersonKey.personType.personTypeId = :personType" +
				" order by s.person.lastName, s.person.firstName, s.person.middleName";
		
		 
		String[] paramNames = {"roadOperationId", "personType"};
		Object[] values = {roadOperationId, Constants.PersonType.TA_STAFF};
		assignedPersonList = hibernateTemplate.findByNamedParam(queryString, paramNames, values);
			
		/*if(assignedPersonList==null || assignedPersonList.size()==0){
			return null;
		}*/
		
		return assignedPersonList;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<JPBO> findAssignedJPByTeam(Integer roadOperationId, Integer teamId){
		List<JPBO> assignedPersonList = new ArrayList<JPBO>();
		
		String queryString = "select new fsl.ta.toms.roms.bo.JPBO(j,o)" +
				" from AssignedPersonDO o,TeamDO t, JPDO j where " +
				"o.assignedPersonKey.team.teamId = t.teamId and " +
				"t.roadOperation.roadOperationId = :roadOperationId and " +
				"o.assignedPersonKey.person.personId = j.person.personId" +
				" and o.assignedPersonKey.personType.personTypeId = :personType" + 
				" and t.teamId = :teamId";
		
		 
		String[] paramNames = {"roadOperationId", "personType", "teamId"};
		Object[] values = {roadOperationId, Constants.PersonType.JP, teamId};
		assignedPersonList = hibernateTemplate.findByNamedParam(queryString, paramNames, values);
			
		/*if(assignedPersonList==null || assignedPersonList.size()==0){
			return null;
		}*/
		
		return assignedPersonList;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ITAExaminerBO> findAssignedITAExaminerByTeam(Integer roadOperationId, Integer teamId){
		List<ITAExaminerBO> assignedPersonList = new ArrayList<ITAExaminerBO>();
		
		String queryString = "select new fsl.ta.toms.roms.bo.ITAExaminerBO(i,o)" +
				" from AssignedPersonDO o,TeamDO t, ITAExaminerDO i where " +
				"o.assignedPersonKey.team.teamId = t.teamId and " +
				"t.roadOperation.roadOperationId = :roadOperationId and " +
				"o.assignedPersonKey.person.personId = i.person.personId" +
				" and o.assignedPersonKey.personType.personTypeId = :personType" + 
				" and t.teamId = :teamId";
		
		 
		String[] paramNames = {"roadOperationId", "personType", "teamId"};
		Object[] values = {roadOperationId, Constants.PersonType.ITA_EXAMINER, teamId};
		assignedPersonList = hibernateTemplate.findByNamedParam(queryString, paramNames, values);
			
		/*if(assignedPersonList==null || assignedPersonList.size()==0){
			return null;
		}*/
		
		return assignedPersonList;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<PoliceOfficerBO> findAssignedPoliceOfficerByTeam(Integer roadOperationId, Integer teamId){
		List<PoliceOfficerBO> assignedPersonList = new ArrayList<PoliceOfficerBO>();
		
		String queryString = "select new fsl.ta.toms.roms.bo.PoliceOfficerBO(p,o)" +
				" from AssignedPersonDO o,TeamDO t, PoliceOfficerDO p where " +
				"o.assignedPersonKey.team.teamId = t.teamId and " +
				"t.roadOperation.roadOperationId = :roadOperationId and " +
				"o.assignedPersonKey.person.personId = p.person.personId" +
				" and o.assignedPersonKey.personType.personTypeId = :personType" + 
				" and t.teamId = :teamId";
		
		 
		String[] paramNames = {"roadOperationId", "personType", "teamId"};
		Object[] values = {roadOperationId, Constants.PersonType.POLICE_OFFCER, teamId};
		assignedPersonList = hibernateTemplate.findByNamedParam(queryString, paramNames, values);
			
		/*if(assignedPersonList==null || assignedPersonList.size()==0){
			return null;
		}*/
		
		return assignedPersonList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ArteryBO> findArteryByTeam(Integer roadOperationId, Integer teamId) {
		List<ArteryBO> operationArteryList = new ArrayList<ArteryBO>();
		
		String queryString = "select new fsl.ta.toms.roms.bo.ArteryBO(a)" +
				" from TeamArteryDO o, TeamDO t, ArteryDO a where " +
				"o.operationArteryKey.team.teamId = t.teamId " +
				"and t.roadOperation.roadOperationId = :roadOperationId " +
				"and o.operationArteryKey.artery.arteryId = a.arteryId" +
				" and t.teamId = :teamId";
		 
		String[] paramNames = {"roadOperationId", "teamId"};
		Object[] values = {roadOperationId, teamId};
		operationArteryList = hibernateTemplate.findByNamedParam(queryString, paramNames, values);
			
		/*if(operationArteryList==null || operationArteryList.size()==0){
			return null;
		}*/
		
		return operationArteryList;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<LocationBO> findLocationByTeam(Integer roadOperationId, Integer teamId) {
		List<LocationBO> operationLocationList = new ArrayList<LocationBO>();
		
		String queryString = "select new fsl.ta.toms.roms.bo.LocationBO(l)" +
				" from TeamLocationDO o, TeamDO t, LocationDO l where" +
				" o.operationLocationKey.team.teamId = t.teamId " +
				"and t.roadOperation.roadOperationId = :roadOperationId " +
				"and o.operationLocationKey.location.locationId = l.locationId" +
				" and t.teamId = :teamId";
		 
		String[] paramNames = {"roadOperationId", "teamId"};
		Object[] values = {roadOperationId, teamId};
		operationLocationList = hibernateTemplate.findByNamedParam(queryString, paramNames, values);
			
		/*if(operationLocationList==null || operationLocationList.size()==0){
			return null;
		}*/
		
		return operationLocationList;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ParishBO> findParishByTeam(Integer roadOperationId, Integer teamId) {

		List<ParishBO> operationParishList = new ArrayList<ParishBO>();
		
		String queryString = "select new fsl.ta.toms.roms.bo.ParishBO(l)" +
				" from TeamParishDO o, TeamDO t, ParishDO l where" +
				" o.teamParishKey.team.teamId = t.teamId " +
				"and o.teamParishKey.parish.parishCode = l.parishCode" +
				" and t.teamId = :teamId";
		 
		operationParishList = hibernateTemplate.findByNamedParam(queryString, "teamId", teamId);
		
		/*if(operationParishList==null || operationParishList.size()==0){
			return null;
		}*/
		
		return operationParishList;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<String> findOperationRegions(Integer roadOperationId) {

		List<String> operationRegionList = new ArrayList<String>();
		
		String queryString = "select r.operationRegionKey.loc_code" +
				" from OperationRegionDO r where" +
				" r.operationRegionKey.roadOperation.roadOperationId = :roadOperationId ";
		 
		operationRegionList = hibernateTemplate.findByNamedParam(queryString, "roadOperationId", roadOperationId);
		
		/*if(operationRegionList==null || operationRegionList.size()==0){
			return null;
		}*/
		
		return operationRegionList;
	}
	
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<StrategyBO> findStrategyForOperation(Integer roadOperationId) {
		List<StrategyBO> operationStrategyList = new ArrayList<StrategyBO>();
		
		String queryString = "select new fsl.ta.toms.roms.bo.StrategyBO(s)" +
				" from OperationStrategyDO o, StrategyDO s where " +
				"o.operationStrategyKey.roadOperation.roadOperationId = :roadOperationId"+
				" and o.operationStrategyKey.strategy.strategyId = s.strategyId";
		 
		
		operationStrategyList = hibernateTemplate.findByNamedParam(queryString, "roadOperationId", roadOperationId);
			
		/*if(operationStrategyList==null || operationStrategyList.size()==0){
			return null;
		}*/
		
		return operationStrategyList;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<TAVehicleBO> findAssignedTAVehicleByTeam(Integer roadOperationId, Integer teamId) {
		List<TAVehicleBO> assignedVehicleList = new ArrayList<TAVehicleBO>();
		
		String queryString = "select new fsl.ta.toms.roms.bo.TAVehicleBO(v,o)" +
				" from AssignedVehicleDO o, TeamDO t, TAVehicleDO v where " +
				"o.assignedVehicleKey.team.teamId = t.teamId " +
				"and t.roadOperation.roadOperationId = :roadOperationId " +
				"and o.assignedVehicleKey.taVehicle.taVehicleId = v.taVehicleId" +
				" and t.teamId = :teamId";
		 
		String[] paramNames = {"roadOperationId", "teamId"};
		Object[] values = {roadOperationId, teamId};
		assignedVehicleList = hibernateTemplate.findByNamedParam(queryString, paramNames, values);
			
		/*if(assignedVehicleList==null || assignedVehicleList.size()==0){
			return null;
		}*/
		
		return assignedVehicleList;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<AssignedPersonBO> findAssignedTeamPersonByType(Integer teamId, String personType){
		List<AssignedPersonBO> assignedPersonList = new ArrayList<AssignedPersonBO>();
		
		String queryString = "select new fsl.ta.toms.roms.bo.AssignedPersonBO(o)" +
				" from AssignedPersonDO o where o.assignedPersonKey.team.teamId = :teamId" +
				" and o.assignedPersonKey.personType.personTypeId = :personType";
		 
		String[] paramNames = {"teamId", "personType"};
		Object[] values = {teamId, personType};
		assignedPersonList = hibernateTemplate.findByNamedParam(queryString, paramNames, values);
			
		/*if(assignedPersonList==null || assignedPersonList.size()==0){
			return null;
		/}*/
		
		return assignedPersonList;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<AssignedVehicleBO> findAssignedVehicle(Integer roadOperationId) {
		List<AssignedVehicleBO> assignedVehicleList = new ArrayList<AssignedVehicleBO>();
		
		String queryString = "select new fsl.ta.toms.roms.bo.AssignedVehicleBO(o)" +
				" from AssignedVehicleDO o, TeamDO t where o.assignedVehicleKey.team.teamId = t.teamId and t.roadOperation.roadOperationId = :roadOperationId";
		 
		
		assignedVehicleList = hibernateTemplate.findByNamedParam(queryString, "roadOperationId", roadOperationId);
			
		/*if(assignedVehicleList==null || assignedVehicleList.size()==0){
			return null;
		}*/
		
		return assignedVehicleList;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<AssignedVehicleBO> findAssignedTeamVehicle(Integer teamId) {
		List<AssignedVehicleBO> assignedVehicleList = new ArrayList<AssignedVehicleBO>();
		
		String queryString = "select new fsl.ta.toms.roms.bo.AssignedVehicleBO(o)" +
				" from AssignedVehicleDO o where o.assignedVehicleKey.team.teamId = :teamId";
		 
		
		assignedVehicleList = hibernateTemplate.findByNamedParam(queryString, "teamId", teamId);
			
		/*if(assignedVehicleList==null || assignedVehicleList.size()==0){
			return null;
		}*/
		
		return assignedVehicleList;
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public List<AssignedVehicleBO> findAssignedVehicleByTeam(Integer roadOperationId, Integer teamId) {
		List<AssignedVehicleBO> assignedVehicleList = new ArrayList<AssignedVehicleBO>();
		
		String queryString = "select new fsl.ta.toms.roms.bo.AssignedVehicleBO(o)" +
				" from AssignedVehicleDO o, TeamDO t where o.assignedVehicleKey.team.teamId = t.teamId and t.roadOperation.roadOperationId = :roadOperationId and t.teamId = :teamId";
		 
		String[] paramNames = {"roadOperationId", "teamId"};
		Object[] values = {roadOperationId, teamId};
		assignedVehicleList = hibernateTemplate.findByNamedParam(queryString, paramNames, values);
			
		/*if(assignedVehicleList==null || assignedVehicleList.size()==0){
			return null;
		}*/
		
		return assignedVehicleList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<OperationStrategyBO> findOperationStrategy(
			Integer roadOperationId) {
		List<OperationStrategyBO> operationStrategyList = new ArrayList<OperationStrategyBO>();
		
		String queryString = "select new fsl.ta.toms.roms.bo.OperationStrategyBO(o)" +
				" from OperationStrategyDO o where o.operationStrategyKey.roadOperation.roadOperationId = :roadOperationId";
		 
		
		operationStrategyList = hibernateTemplate.findByNamedParam(queryString, "roadOperationId", roadOperationId);
			
		/*if(operationStrategyList==null || operationStrategyList.size()==0){
			return null;
		}*/
		
		return operationStrategyList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<OperationLocationBO> findOperationLocation(
			Integer roadOperationId) {
		List<OperationLocationBO> operationLocationList = new ArrayList<OperationLocationBO>();
		
		String queryString = "select new fsl.ta.toms.roms.bo.OperationLocationBO(o)" +
				" from TeamLocationDO o, TeamDO t where o.operationLocationKey.team.teamId = t.teamId and t.roadOperation.roadOperationId = :roadOperationId";
		 
		
		operationLocationList = hibernateTemplate.findByNamedParam(queryString, "roadOperationId", roadOperationId);
			
		/*if(operationLocationList==null || operationLocationList.size()==0){
			return null;
		}*/
		
		return operationLocationList;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<OperationLocationBO> findOperationLocationByTeam(Integer roadOperationId, Integer teamId) {
		List<OperationLocationBO> operationLocationList = new ArrayList<OperationLocationBO>();
		
		String queryString = "select new fsl.ta.toms.roms.bo.OperationLocationBO(o)" +
				" from TeamLocationDO o, TeamDO t where o.operationLocationKey.team.teamId = t.teamId and t.roadOperation.roadOperationId = :roadOperationId and t.teamId = :teamId";
		 
		String[] paramNames = {"roadOperationId", "teamId"};
		Object[] values = {roadOperationId, teamId};
		operationLocationList = hibernateTemplate.findByNamedParam(queryString, paramNames, values);
			
		/*if(operationLocationList==null || operationLocationList.size()==0){
			return null;
		}*/
		
		return operationLocationList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<OperationArteryBO> findOperationArtery(Integer roadOperationId) {
		List<OperationArteryBO> operationArteryList = new ArrayList<OperationArteryBO>();
		
		String queryString = "select new fsl.ta.toms.roms.bo.OperationArteryBO(o)" +
				" from TeamArteryDO o, TeamDO t where o.operationArteryKey.team.teamId = t.teamId and t.roadOperation.roadOperationId = :roadOperationId";
		 
		
		operationArteryList = hibernateTemplate.findByNamedParam(queryString, "roadOperationId", roadOperationId);
			
		/*if(operationArteryList==null || operationArteryList.size()==0){
			return null;
		}*/
		
		return operationArteryList;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<OperationArteryBO> findOperationArteryByTeam(Integer roadOperationId, Integer teamId) {
		List<OperationArteryBO> operationArteryList = new ArrayList<OperationArteryBO>();
		
		String queryString = "select new fsl.ta.toms.roms.bo.OperationArteryBO(o)" +
				" from TeamArteryDO o, TeamDO t where o.operationArteryKey.team.teamId = t.teamId and t.roadOperation.roadOperationId = :roadOperationId and t.teamId = :teamId";
		 
		String[] paramNames = {"roadOperationId", "teamId"};
		Object[] values = {roadOperationId, teamId};
		operationArteryList = hibernateTemplate.findByNamedParam(queryString, paramNames, values);
			
		/*if(operationArteryList==null || operationArteryList.size()==0){
			return null;
		}*/
		
		return operationArteryList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public TAStaffBO findTAStaffByPersonID(Integer personId) {
		List<TAStaffBO> staffList = new ArrayList<TAStaffBO>();
		
		String queryString = "select new fsl.ta.toms.roms.bo.TAStaffBO(o)" +
				" from TAStaffDO o where o.person.personId = :personId";
		 
		
		staffList = hibernateTemplate.findByNamedParam(queryString, "personId", personId);
			
		if(staffList!=null && staffList.size()==1){
			return staffList.get(0);
		}
		
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public JPBO findJPByPersonID(Integer personId) {
		List<JPBO> jpList = new ArrayList<JPBO>();
		
		String queryString = "select new fsl.ta.toms.roms.bo.JPBO(o)" +
				" from JPDO o where o.person.personId = :personId";
		 
		
		jpList = hibernateTemplate.findByNamedParam(queryString, "personId", personId);
			
		if(jpList!=null && jpList.size()==1){
			return jpList.get(0);
		}
		
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ITAExaminerBO findITAExaminerByPersonID(Integer personId) {
		List<ITAExaminerBO> itaExaminerList = new ArrayList<ITAExaminerBO>();
		
		String queryString = "select new fsl.ta.toms.roms.bo.ITAExaminerBO(o)" +
				" from ITAExaminerDO o where o.person.personId = :personId";
		 
		
		itaExaminerList = hibernateTemplate.findByNamedParam(queryString, "personId", personId);
			
		if(itaExaminerList!=null && itaExaminerList.size()==1){
			return itaExaminerList.get(0);
		}
		
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public PoliceOfficerBO findPoliceOfficerByPersonID(Integer personId) {
		List<PoliceOfficerBO> policeOfficerList = new ArrayList<PoliceOfficerBO>();
		
		String queryString = "select new fsl.ta.toms.roms.bo.PoliceOfficerBO(o)" +
				" from PoliceOfficerDO o where o.person.personId = :personId";
		 
		
		policeOfficerList = hibernateTemplate.findByNamedParam(queryString, "personId", personId);
			
		if(policeOfficerList!=null && policeOfficerList.size()==1){
			return policeOfficerList.get(0);
		}
		
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TAStaffBO> getAvailableTAStaff(
			AvailableResourceCriteriaBO availableResourceCriteriaBO) {
		
		List<TAStaffBO> staffList = new ArrayList<TAStaffBO>();
			
		String formattedStartDate = DateUtils.formatDate("yyyy-MM-dd", availableResourceCriteriaBO.getScheduledStartDate());
		String formattedStartDateTime =  formattedStartDate + " " + availableResourceCriteriaBO.getScheduledStartTime();
	
		String formattedEndDate = DateUtils.formatDate("yyyy-MM-dd", availableResourceCriteriaBO.getScheduledEndDate());
		String formattedEndDateTime =  formattedEndDate + " " + availableResourceCriteriaBO.getScheduledEndTime();
	
//		String regionList=returnCommaDelimitedStringList(availableResourceCriteriaBO.getOfficeLocCodeList());
//		
//		StringBuffer queryString = new StringBuffer("select new fsl.ta.toms.roms.bo.TAStaffBO(r)" +
//				" from TAStaffDO r" +
//				" where r.person.personId not in (select a.assignedPersonKey.person.personId" +
//				" from AssignedPersonDO a, RoadOperationDO o, TeamDO t where t.roadOperation.roadOperationId = o.roadOperationId and a.assignedPersonKey.team.teamId = t.teamId");
//		queryString.append(" and ((o.scheduledStartDtime <= '").append(formattedStartDateTime).append("' and o.scheduledEndDtime > '").append(formattedStartDateTime).append("')"); 
//		queryString.append(" OR (o.scheduledStartDtime < '").append(formattedEndDateTime).append("' and o.scheduledEndDtime >= '").append(formattedEndDateTime).append("')");
//		queryString.append(" OR (o.scheduledStartDtime > '").append(formattedStartDateTime).append("' and o.scheduledEndDtime < '").append(formattedEndDateTime).append("'))");
//		
//		queryString.append(" and o.status.statusId not in ('" + Constants.Status.ROAD_OPERATION_CANCELLED + "', '"+ Constants.Status.ROAD_OPERATION_TERMINATED +"','" + 
//				Constants.Status.ROAD_OPERATION_CLOSED + "', '" + Constants.Status.ROAD_OPERATION_COMPLETED + "')");
//		if(availableResourceCriteriaBO.getRoadOperationId() != null && availableResourceCriteriaBO.getRoadOperationId() >0 )
//		{//R.B. added this condition, to eliminate the current operation being looked at
//			queryString.append(" and o.roadOperationId not in (" + availableResourceCriteriaBO.getRoadOperationId() + "))");
//		}else{
//			queryString.append(")");
//		}
//		
//		queryString.append(" and r.status.statusId = '").append(Constants.Status.ACTIVE).append("'");
//		
//		/*if(availableResourceCriteriaBO.getCategoryId().equalsIgnoreCase(Constants.Category.REGIONAL)){
//			queryString.append(" and UPPER(r.officeLocationCode) = '" +  availableResourceCriteriaBO.getOfficeLocCode().toUpperCase() + "'");
//		}*/
//	
//		//added by kpowell: 2014-05-14
//		//only apply officeLocationCode for Regional Operations
//		if(availableResourceCriteriaBO.getCategoryId().equalsIgnoreCase(Constants.Category.REGIONAL)){
//			queryString.append(" and UPPER(r.officeLocationCode) IN (" +  regionList + ")");
//		}
//		queryString.append(" order by upper(r.person.lastName), upper(r.person.firstName)");
//		
//		
//		staffList = hibernateTemplate.find(queryString.toString());

		
//		System.err.println("Available: "+ availableResourceCriteriaBO);
		
		try{
		
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
				//String startDateTime = dateFormat.format(dateFormat.parse(formattedStartDateTime));		
				//String endDateTime = dateFormat.format(dateFormat.parse(formattedEndDateTime));
				
				
				
//				System.err.println("Start date time: "+dateFormat.parse(formattedStartDateTime));
//				System.err.println("End date time: "+dateFormat.parse(formattedEndDateTime));
				
									
				/*String [] paramNames = {"startDate","endDate", "cat", "region"};*/
				
				String office = (availableResourceCriteriaBO.getOfficeLocCodeList()!=null
						&&	!availableResourceCriteriaBO.getOfficeLocCodeList().isEmpty())?availableResourceCriteriaBO.getOfficeLocCodeList().get(0):"";
				
//				System.err.println("Office: "+office);
				
				/*Object [] values = {dateFormat.parse(formattedStartDateTime) 
						,dateFormat.parse(formattedEndDateTime)
						,availableResourceCriteriaBO.getCategoryId()
						,office};*/
				
				/*List result = hibernateTemplate.
						findByNamedQueryAndNamedParam("getAvailableTA", paramNames, values);*/
				
			
				Query query = this.hibernateTemplate.getSessionFactory().getCurrentSession().createSQLQuery(
						"execute procedure roms_sp_available_ta(:startDate,:endDate,:cat,:region)")
						.addEntity(TAStaffDO.class)
						.setParameter("startDate", dateFormat.parse(formattedStartDateTime))
						.setParameter("endDate", dateFormat.parse(formattedEndDateTime))
						.setParameter("cat", availableResourceCriteriaBO.getCategoryId())
						.setParameter("region", office);
						
				//System.err.println("SP Query: "+query.toString());		
				
				List result = query.list();
				
				for(int i=0; i<result.size(); i++){
				        TAStaffDO taStaff = (TAStaffDO) result.get(i);
				        //System.err.println("Staff: "+taStaff.getStaffId()+" - "+taStaff.getLoginUsername());
				        
				        staffList.add(new TAStaffBO(taStaff));
				}

		
		}catch (Exception e) {
			e.printStackTrace();
		}

		/*if(staffList==null || staffList.size()==0){
			return null;
		}*/
		
		return staffList;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Integer> getAvailableTAStaffIds(
			AvailableResourceCriteriaBO availableResourceCriteriaBO) {
		
		List<Integer> staffListInteger = new ArrayList<Integer>();
			
		String formattedStartDate = DateUtils.formatDate("yyyy-MM-dd", availableResourceCriteriaBO.getScheduledStartDate());
		String formattedStartDateTime =  formattedStartDate + " " + availableResourceCriteriaBO.getScheduledStartTime();
	
		String formattedEndDate = DateUtils.formatDate("yyyy-MM-dd", availableResourceCriteriaBO.getScheduledEndDate());
		String formattedEndDateTime =  formattedEndDate + " " + availableResourceCriteriaBO.getScheduledEndTime();
	
		
		try{
		
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
				
				String office = (availableResourceCriteriaBO.getOfficeLocCodeList()!=null
						&&	!availableResourceCriteriaBO.getOfficeLocCodeList().isEmpty())?availableResourceCriteriaBO.getOfficeLocCodeList().get(0):"";
				
			
				Query query = this.hibernateTemplate.getSessionFactory().getCurrentSession().createSQLQuery(
						"execute procedure roms_sp_available_ta_ids(:startDate,:endDate,:cat,:region)")
						.setParameter("startDate", dateFormat.parse(formattedStartDateTime))
						.setParameter("endDate", dateFormat.parse(formattedEndDateTime))
						.setParameter("cat", availableResourceCriteriaBO.getCategoryId())
						.setParameter("region", office);
							
				
				List result = query.list();
				
				for(int i=0; i<result.size(); i++){
				        Integer taStaffId = (Integer) result.get(i);
				        
				        staffListInteger.add(taStaffId);
				}

		
		}catch (Exception e) {
			e.printStackTrace();
		}

		/*if(staffListInteger==null || staffListInteger.size()==0){
			return null;
		}*/
		
		return staffListInteger;
	}
	
	private String returnCommaDelimitedStringList(List<String> stringList){
		String commaDelimitedList="";
		
		int listSize = stringList.size();
		
		for(int i=0; i<listSize; i++){
			commaDelimitedList = commaDelimitedList + "'" +stringList.get(i).toUpperCase();
			if(i<listSize-1){
				commaDelimitedList = commaDelimitedList + "', ";
			}
			else{
				commaDelimitedList = commaDelimitedList + "'";
			}
		}
		return commaDelimitedList;
	}

	@Override
	public List<JPBO> getAvailableJP(
			AvailableResourceCriteriaBO availableResourceCriteriaBO) {
		List<JPBO> jpList = new ArrayList<JPBO>();
		
		String formattedStartDate = DateUtils.formatDate("yyyy-MM-dd", availableResourceCriteriaBO.getScheduledStartDate());
		String formattedStartDateTime =  formattedStartDate + " " + availableResourceCriteriaBO.getScheduledStartTime();
	
		String formattedEndDate = DateUtils.formatDate("yyyy-MM-dd", availableResourceCriteriaBO.getScheduledEndDate());
		String formattedEndDateTime =  formattedEndDate + " " + availableResourceCriteriaBO.getScheduledEndTime();
	
		String parishList=returnCommaDelimitedStringList(availableResourceCriteriaBO.getParishCodeList());
			
//		StringBuffer queryString = new StringBuffer("select new fsl.ta.toms.roms.bo.JPBO(r)" +
//				" from JPDO r" +
//				" where r.person.personId not in (select a.assignedPersonKey.person.personId" +
//				" from AssignedPersonDO a, RoadOperationDO o, TeamDO t where t.roadOperation.roadOperationId = o.roadOperationId and a.assignedPersonKey.team.teamId = t.teamId");
//		queryString.append(" and ((o.scheduledStartDtime <= '").append(formattedStartDateTime).append("' and o.scheduledEndDtime > '").append(formattedStartDateTime).append("')"); 
//		queryString.append(" OR (o.scheduledStartDtime < '").append(formattedEndDateTime).append("' and o.scheduledEndDtime >= '").append(formattedEndDateTime).append("')");
//		queryString.append(" OR (o.scheduledStartDtime > '").append(formattedStartDateTime).append("' and o.scheduledEndDtime < '").append(formattedEndDateTime).append("'))"); 
//		
//		queryString.append(" and o.status.statusId not in ('" + Constants.Status.ROAD_OPERATION_CANCELLED + "', '"+ Constants.Status.ROAD_OPERATION_TERMINATED +"','" + 
//				Constants.Status.ROAD_OPERATION_CLOSED + "', '" + Constants.Status.ROAD_OPERATION_COMPLETED + "')");
//		
//		if(availableResourceCriteriaBO.getRoadOperationId() != null && availableResourceCriteriaBO.getRoadOperationId() >0 )
//		{//R.B. added this condition, to eliminate the current operation being looked at
//			queryString.append(" and o.roadOperationId not in (" + availableResourceCriteriaBO.getRoadOperationId() + "))");
//		}else{
//			queryString.append(")");
//		}
//		
//		queryString.append(" and r.status.statusId = '").append(Constants.Status.ACTIVE).append("'");
//		
//		queryString.append(" and r.parish.parishCode IN (" +  parishList + ")");
//		
//		queryString.append(" order by upper(r.person.lastName), upper(r.person.firstName)");
//		
//		
//		jpList = hibernateTemplate.find(queryString.toString());

		
		//System.err.println("Available: "+ availableResourceCriteriaBO);
		
		try{
		
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
								
				
				//System.err.println("Start date time: "+dateFormat.parse(formattedStartDateTime));
				//System.err.println("End date time: "+dateFormat.parse(formattedEndDateTime));
				
				
				//System.err.println("Parish List: "+returnCommaDelimitedStringList(availableResourceCriteriaBO.getParishCodeList()));
				
							
				
				
				Query query = this.hibernateTemplate.getSessionFactory().getCurrentSession().createSQLQuery(
						"execute procedure roms_sp_available_jp(:startDate,:endDate,LIST{"+parishList+"})")
						.addEntity(JPDO.class)
						.setParameter("startDate", dateFormat.parse(formattedStartDateTime))
						.setParameter("endDate", dateFormat.parse(formattedEndDateTime))
						;
						
				//System.err.println("SP Query: "+query.toString());		
				
				List result = query.list();
				
				for(int i=0; i<result.size(); i++){
					JPDO jpDO = (JPDO) result.get(i);
				      //  System.err.println("JP: "+jpDO.getIdNumber()+" - "+jpDO.getPerson().getFirstName());
				        
				        jpList.add(new JPBO(jpDO));
				}

		
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		
		
		
		
		
		/*if(jpList==null || jpList.size()==0){
			return null;
		}*/
		
		return jpList;
	}
	
	@Override
	public List<Integer> getAvailableJPIds(
			AvailableResourceCriteriaBO availableResourceCriteriaBO) {
		List<Integer> jpList = new ArrayList<Integer>();
		
		String formattedStartDate = DateUtils.formatDate("yyyy-MM-dd", availableResourceCriteriaBO.getScheduledStartDate());
		String formattedStartDateTime =  formattedStartDate + " " + availableResourceCriteriaBO.getScheduledStartTime();
	
		String formattedEndDate = DateUtils.formatDate("yyyy-MM-dd", availableResourceCriteriaBO.getScheduledEndDate());
		String formattedEndDateTime =  formattedEndDate + " " + availableResourceCriteriaBO.getScheduledEndTime();
	
		String parishList=returnCommaDelimitedStringList(availableResourceCriteriaBO.getParishCodeList());
			

		
		try{
		
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");

				
				Query query = this.hibernateTemplate.getSessionFactory().getCurrentSession().createSQLQuery(
						"execute procedure roms_sp_available_jp_ids(:startDate,:endDate,LIST{"+parishList+"})")
						.setParameter("startDate", dateFormat.parse(formattedStartDateTime))
						.setParameter("endDate", dateFormat.parse(formattedEndDateTime))
						;
							
				
				List result = query.list();
				
				for(int i=0; i<result.size(); i++){
					Integer jpDOId = (Integer) result.get(i);
				      //  System.err.println("JP: "+jpDO.getIdNumber()+" - "+jpDO.getPerson().getFirstName());
				        
				        jpList.add(jpDOId );
				}

		
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		
		
		
		
		
		/*if(jpList==null || jpList.size()==0){
			return null;
		}*/
		
		return jpList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ITAExaminerBO> getAvailableITAExaminer(
			AvailableResourceCriteriaBO availableResourceCriteriaBO) {
	
		List<ITAExaminerBO> itaExaminerList = new ArrayList<ITAExaminerBO>();
		
		String formattedStartDate = DateUtils.formatDate("yyyy-MM-dd", availableResourceCriteriaBO.getScheduledStartDate());
		String formattedStartDateTime =  formattedStartDate + " " + availableResourceCriteriaBO.getScheduledStartTime();
	
		String formattedEndDate = DateUtils.formatDate("yyyy-MM-dd", availableResourceCriteriaBO.getScheduledEndDate());
		String formattedEndDateTime =  formattedEndDate + " " + availableResourceCriteriaBO.getScheduledEndTime();
	
		
//		String regionList=returnCommaDelimitedStringList(availableResourceCriteriaBO.getOfficeLocCodeList());
//		
//		StringBuffer queryString = new StringBuffer("select new fsl.ta.toms.roms.bo.ITAExaminerBO(r)" +
//				" from ITAExaminerDO r" +
//				" where r.person.personId not in (select a.assignedPersonKey.person.personId" +
//				" from AssignedPersonDO a, RoadOperationDO o, TeamDO t where t.roadOperation.roadOperationId = o.roadOperationId and a.assignedPersonKey.team.teamId = t.teamId");
//		queryString.append(" and ((o.scheduledStartDtime <= '").append(formattedStartDateTime).append("' and o.scheduledEndDtime > '").append(formattedStartDateTime).append("')"); 
//		queryString.append(" OR (o.scheduledStartDtime < '").append(formattedEndDateTime).append("' and o.scheduledEndDtime >= '").append(formattedEndDateTime).append("')");
//		queryString.append(" OR (o.scheduledStartDtime > '").append(formattedStartDateTime).append("' and o.scheduledEndDtime < '").append(formattedEndDateTime).append("'))");
//		
//		queryString.append(" and o.status.statusId not in ('" + Constants.Status.ROAD_OPERATION_CANCELLED + "', '"+ Constants.Status.ROAD_OPERATION_TERMINATED +"','" + 
//				Constants.Status.ROAD_OPERATION_CLOSED + "', '" + Constants.Status.ROAD_OPERATION_COMPLETED + "')");
//		
//		if(availableResourceCriteriaBO.getRoadOperationId() != null && availableResourceCriteriaBO.getRoadOperationId() >0 )
//		{//R.B. added this condition, to eliminate the current operation being looked at
//			queryString.append(" and o.roadOperationId not in (" + availableResourceCriteriaBO.getRoadOperationId() + "))");
//		}else{
//			queryString.append(")");
//		}
//		
//		queryString.append(" and r.status.statusId = '").append(Constants.Status.ACTIVE).append("'");
//		
//		/*if(availableResourceCriteriaBO.getCategoryId().equalsIgnoreCase(Constants.Category.REGIONAL)){
//			queryString.append(" and UPPER(r.officeLocationCode) = '" +  availableResourceCriteriaBO.getOfficeLocCode().toUpperCase() + "'");
//		}*/
//	
//		//TODO added by kpowell: 2014-05-14
//		//only apply officeLocationCode for Regional Operations
//		if(availableResourceCriteriaBO.getCategoryId().equalsIgnoreCase(Constants.Category.REGIONAL)){
//			queryString.append(" and UPPER(r.officeLocationCode) IN (" +  regionList + ")");
//		}		
//				
//		
//		
//		queryString.append(" order by upper(r.person.lastName), upper(r.person.firstName)");
//		
//		
//		itaExaminerList = hibernateTemplate.find(queryString.toString());
		
		
//		System.err.println("Available: "+ availableResourceCriteriaBO);
		
		try{
		
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
				//String startDateTime = dateFormat.format(dateFormat.parse(formattedStartDateTime));		
				//String endDateTime = dateFormat.format(dateFormat.parse(formattedEndDateTime));
				
				
				
//				System.err.println("Start date time: "+dateFormat.parse(formattedStartDateTime));
//				System.err.println("End date time: "+dateFormat.parse(formattedEndDateTime));
				
									
				/*String [] paramNames = {"startDate","endDate", "cat", "region"};*/
				
				String office = (availableResourceCriteriaBO.getOfficeLocCodeList()!=null
						&&	!availableResourceCriteriaBO.getOfficeLocCodeList().isEmpty())?availableResourceCriteriaBO.getOfficeLocCodeList().get(0):"";
				
//				System.err.println("Office: "+office);
				
				/*Object [] values = {dateFormat.parse(formattedStartDateTime) 
						,dateFormat.parse(formattedEndDateTime)
						,availableResourceCriteriaBO.getCategoryId()
						,office};*/
				
				/*List result = hibernateTemplate.
						findByNamedQueryAndNamedParam("getAvailableTA", paramNames, values);*/
				
			
				Query query = this.hibernateTemplate.getSessionFactory().getCurrentSession().createSQLQuery(
						"execute procedure roms_sp_available_ita(:startDate,:endDate,:cat,:region)")
						.addEntity(ITAExaminerDO.class)
						.setParameter("startDate", dateFormat.parse(formattedStartDateTime))
						.setParameter("endDate", dateFormat.parse(formattedEndDateTime))
						.setParameter("cat", availableResourceCriteriaBO.getCategoryId())
						.setParameter("region", office);
						
//				System.err.println("SP Query: "+query.toString());		
				
				List result = query.list();
				
				for(int i=0; i<result.size(); i++){
					ITAExaminerDO examinerDO = (ITAExaminerDO) result.get(i);
				       // System.err.println("ITA: "+examinerDO.getExaminerId()+" - "+examinerDO.getPerson().getFirstName());
				        
				        itaExaminerList.add(new ITAExaminerBO(examinerDO));
				}

		
		}catch (Exception e) {
			e.printStackTrace();
		}

		
		
		
		
		

		/*if(itaExaminerList==null || itaExaminerList.size()==0){
			return null;
		}*/
		
		return itaExaminerList;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Integer> getAvailableITAExaminerIds(
			AvailableResourceCriteriaBO availableResourceCriteriaBO) {
	
		List<Integer> itaExaminerList = new ArrayList<Integer>();
		
		String formattedStartDate = DateUtils.formatDate("yyyy-MM-dd", availableResourceCriteriaBO.getScheduledStartDate());
		String formattedStartDateTime =  formattedStartDate + " " + availableResourceCriteriaBO.getScheduledStartTime();
	
		String formattedEndDate = DateUtils.formatDate("yyyy-MM-dd", availableResourceCriteriaBO.getScheduledEndDate());
		String formattedEndDateTime =  formattedEndDate + " " + availableResourceCriteriaBO.getScheduledEndTime();
	
		
		
		try{
		
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");

				
				String office = (availableResourceCriteriaBO.getOfficeLocCodeList()!=null
						&&	!availableResourceCriteriaBO.getOfficeLocCodeList().isEmpty())?availableResourceCriteriaBO.getOfficeLocCodeList().get(0):"";
				
				
			
				Query query = this.hibernateTemplate.getSessionFactory().getCurrentSession().createSQLQuery(
						"execute procedure roms_sp_available_ita_ids(:startDate,:endDate,:cat,:region)")
						.setParameter("startDate", dateFormat.parse(formattedStartDateTime))
						.setParameter("endDate", dateFormat.parse(formattedEndDateTime))
						.setParameter("cat", availableResourceCriteriaBO.getCategoryId())
						.setParameter("region", office);
						
//				System.err.println("SP Query: "+query.toString());		
				
				List result = query.list();
				
				for(int i=0; i<result.size(); i++){
					Integer examinerDOId = (Integer) result.get(i);
				       // System.err.println("ITA: "+examinerDO.getExaminerId()+" - "+examinerDO.getPerson().getFirstName());
				        
				        itaExaminerList.add(examinerDOId);
				}

		
		}catch (Exception e) {
			e.printStackTrace();
		}

		
		
		
		
		

		/*if(itaExaminerList==null || itaExaminerList.size()==0){
			return null;
		}*/
		
		return itaExaminerList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PoliceOfficerBO> getAvailablePoliceOfficerOld(
			AvailableResourceCriteriaBO availableResourceCriteriaBO) {
		List<PoliceOfficerBO> itaExaminerList = new ArrayList<PoliceOfficerBO>();
		
		String formattedStartDate = DateUtils.formatDate("yyyy-MM-dd", availableResourceCriteriaBO.getScheduledStartDate());
		String formattedStartDateTime =  formattedStartDate + " " + availableResourceCriteriaBO.getScheduledStartTime();
	
		String formattedEndDate = DateUtils.formatDate("yyyy-MM-dd", availableResourceCriteriaBO.getScheduledEndDate());
		String formattedEndDateTime =  formattedEndDate + " " + availableResourceCriteriaBO.getScheduledEndTime();
	
		StringBuffer queryString = new StringBuffer("select new fsl.ta.toms.roms.bo.PoliceOfficerBO(r)" +
				" from PoliceOfficerDO r" +
				" where r.person.personId not in (select a.assignedPersonKey.person.personId" +
				" from AssignedPersonDO a, RoadOperationDO o, TeamDO t where t.roadOperation.roadOperationId = o.roadOperationId and a.assignedPersonKey.team.teamId = t.teamId");
		queryString.append(" and ((o.scheduledStartDtime <= '").append(formattedStartDateTime).append("' and o.scheduledEndDtime > '").append(formattedStartDateTime).append("')"); 
		queryString.append(" OR (o.scheduledStartDtime < '").append(formattedEndDateTime).append("' and o.scheduledEndDtime >= '").append(formattedEndDateTime).append("')");
		queryString.append(" OR (o.scheduledStartDtime > '").append(formattedStartDateTime).append("' and o.scheduledEndDtime < '").append(formattedEndDateTime).append("'))");
		
		queryString.append(" and o.status.statusId not in ('" + Constants.Status.ROAD_OPERATION_CANCELLED + "', '"+ Constants.Status.ROAD_OPERATION_TERMINATED +"','" + 
				Constants.Status.ROAD_OPERATION_CLOSED + "', '" + Constants.Status.ROAD_OPERATION_COMPLETED + "')");
		
		if(availableResourceCriteriaBO.getRoadOperationId() != null && availableResourceCriteriaBO.getRoadOperationId() >0 )
		{//R.B. added this condition, to eliminate the current operation being looked at
			queryString.append(" and o.roadOperationId not in (" + availableResourceCriteriaBO.getRoadOperationId() + "))");
		}else{
			queryString.append(")");
		}
		
		queryString.append(" and r.status.statusId = '").append(Constants.Status.ACTIVE).append("'"); 
		
		queryString.append(" order by upper(r.person.lastName), upper(r.person.firstName)");
		
		
		itaExaminerList = hibernateTemplate.find(queryString.toString());

		/*if(itaExaminerList==null || itaExaminerList.size()==0){
			return null;
		}*/
		
		return itaExaminerList;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<PoliceOfficerBO> getAvailablePoliceOfficer(AvailableResourceCriteriaBO availableResourceCriteriaBO) throws NoRecordFoundException 
	{

		
		
		List<PoliceOfficerBO> policeBOList = new ArrayList<PoliceOfficerBO>();
		
		String formattedStartDate = DateUtils.formatDate("yyyy-MM-dd", availableResourceCriteriaBO.getScheduledStartDate());
		String formattedStartDateTime =  formattedStartDate + " " + availableResourceCriteriaBO.getScheduledStartTime();
	
		String formattedEndDate = DateUtils.formatDate("yyyy-MM-dd", availableResourceCriteriaBO.getScheduledEndDate());
		String formattedEndDateTime =  formattedEndDate + " " + availableResourceCriteriaBO.getScheduledEndTime();
		
		
//		System.err.println("Available: "+ availableResourceCriteriaBO);
		
		try{
		
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
				//String startDateTime = dateFormat.format(dateFormat.parse(formattedStartDateTime));		
				//String endDateTime = dateFormat.format(dateFormat.parse(formattedEndDateTime));
				
				System.err.println("Start date time: "+dateFormat.parse(formattedStartDateTime));
				System.err.println("End date time: "+dateFormat.parse(formattedEndDateTime));
				
				
				Query query = this.hibernateTemplate.getSessionFactory().getCurrentSession().createSQLQuery(
						"execute procedure roms_sp_available_po(:startDate,:endDate,:ia_officer_first_name,:ia_officer_last_name,:ia_officer_comp_no,:ia_officer_station,:ia_use_max_filter)")
						.addEntity(PoliceOfficerDO.class)
						.setParameter("startDate", dateFormat.parse(formattedStartDateTime))
						.setParameter("endDate", dateFormat.parse(formattedEndDateTime))
						.setParameter("ia_officer_first_name", availableResourceCriteriaBO.getPoliceOfficerFirstName())
						.setParameter("ia_officer_last_name", availableResourceCriteriaBO.getPoliceOfficerLastName())
						.setParameter("ia_officer_comp_no", availableResourceCriteriaBO.getPoliceOfficerCompNum())
						.setParameter("ia_officer_station", availableResourceCriteriaBO.getPoliceStation())
						.setParameter("ia_use_max_filter", availableResourceCriteriaBO.getUsePoliceMaxFilter())
						;
				
						
					
//				System.err.println("SP Query: "+query.toString());		
				
				List result = query.list();
				
				for(int i=0; i<result.size(); i++){
					PoliceOfficerDO officerDO = (PoliceOfficerDO) result.get(i);
//				        System.err.println("Police: "+officerDO.getPolOfficerCompNo()+" - "+officerDO.getPerson().getFirstName());
				        
				        policeBOList.add(new PoliceOfficerBO(officerDO));
				}

		
		}
		catch (Exception e) {
			
			logger.error("Road Operation", e);
			
			if(e.getCause().getMessage().toLowerCase().contains("sorry")){
				throw new NoRecordFoundException(e.getCause().getMessage());
			}
			
		}
		
		
		
		
		return policeBOList;
		
		

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Integer> getAvailablePoliceOfficerIds(AvailableResourceCriteriaBO availableResourceCriteriaBO) throws NoRecordFoundException 
	{

		
		
		List<Integer> policeBOList = new ArrayList<Integer>();
		
		String formattedStartDate = DateUtils.formatDate("yyyy-MM-dd", availableResourceCriteriaBO.getScheduledStartDate());
		String formattedStartDateTime =  formattedStartDate + " " + availableResourceCriteriaBO.getScheduledStartTime();
	
		String formattedEndDate = DateUtils.formatDate("yyyy-MM-dd", availableResourceCriteriaBO.getScheduledEndDate());
		String formattedEndDateTime =  formattedEndDate + " " + availableResourceCriteriaBO.getScheduledEndTime();
		
		
//		System.err.println("Available: "+ availableResourceCriteriaBO);
		
		try{
		
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
				//String startDateTime = dateFormat.format(dateFormat.parse(formattedStartDateTime));		
				//String endDateTime = dateFormat.format(dateFormat.parse(formattedEndDateTime));
				
				System.err.println("Start date time: "+dateFormat.parse(formattedStartDateTime));
				System.err.println("End date time: "+dateFormat.parse(formattedEndDateTime));
				
				
				Query query = this.hibernateTemplate.getSessionFactory().getCurrentSession().createSQLQuery(
						"execute procedure roms_sp_available_po_ids(:startDate,:endDate,:ia_officer_first_name,:ia_officer_last_name,:ia_officer_comp_no,:ia_officer_station,:ia_use_max_filter)")
						.setParameter("startDate", dateFormat.parse(formattedStartDateTime))
						.setParameter("endDate", dateFormat.parse(formattedEndDateTime))
						.setParameter("ia_officer_first_name", availableResourceCriteriaBO.getPoliceOfficerFirstName())
						.setParameter("ia_officer_last_name", availableResourceCriteriaBO.getPoliceOfficerLastName())
						.setParameter("ia_officer_comp_no", availableResourceCriteriaBO.getPoliceOfficerCompNum())
						.setParameter("ia_officer_station", availableResourceCriteriaBO.getPoliceStation())
						.setParameter("ia_use_max_filter", availableResourceCriteriaBO.getUsePoliceMaxFilter())
						;
				
						
					
//				System.err.println("SP Query: "+query.toString());		
				
				List result = query.list();
				
				for(int i=0; i<result.size(); i++){
					Integer officerDOId = (Integer) result.get(i);
//				        System.err.println("Police: "+officerDO.getPolOfficerCompNo()+" - "+officerDO.getPerson().getFirstName());
				        
				        policeBOList.add(officerDOId);
				}

		
		}
		catch (Exception e) {
			
			logger.error("Road Operation", e);
			
			if(e.getCause().getMessage().toLowerCase().contains("sorry")){
				throw new NoRecordFoundException(e.getCause().getMessage());
			}
			
		}
		
		
		
		
		return policeBOList;
		
		

	}


	@SuppressWarnings("unchecked")
	@Override
	public List<TAVehicleBO> getAvailableTAVehicle(
			AvailableResourceCriteriaBO availableResourceCriteriaBO) {
		List<TAVehicleBO> taVehicleBOLst = new ArrayList<TAVehicleBO>();
		
		String formattedStartDate = DateUtils.formatDate("yyyy-MM-dd", availableResourceCriteriaBO.getScheduledStartDate());
		String formattedStartDateTime =  formattedStartDate + " " + availableResourceCriteriaBO.getScheduledStartTime();
	
		String formattedEndDate = DateUtils.formatDate("yyyy-MM-dd", availableResourceCriteriaBO.getScheduledEndDate());
		String formattedEndDateTime =  formattedEndDate + " " + availableResourceCriteriaBO.getScheduledEndTime();

		
		try{
		
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
				
				String office = (availableResourceCriteriaBO.getOfficeLocCodeList()!=null
						&&	!availableResourceCriteriaBO.getOfficeLocCodeList().isEmpty())?availableResourceCriteriaBO.getOfficeLocCodeList().get(0):"";
		
				Query query = this.hibernateTemplate.getSessionFactory().getCurrentSession().createSQLQuery(
						"execute procedure roms_sp_available_vehicle(:startDate,:endDate,:cat,:region)")
						.addEntity(TAVehicleDO.class)
						.setParameter("startDate", dateFormat.parse(formattedStartDateTime))
						.setParameter("endDate", dateFormat.parse(formattedEndDateTime))
						.setParameter("cat", availableResourceCriteriaBO.getCategoryId())
						.setParameter("region", office);
						
				//System.err.println("SP Query: "+query.toString());		
				
				List result = query.list();
				
				for(int i=0; i<result.size(); i++){
					TAVehicleDO taVehicle = (TAVehicleDO) result.get(i);
			        taVehicleBOLst.add(new TAVehicleBO(taVehicle));
				}

		
		}catch (Exception e) {
			e.printStackTrace();
		}
				

		/*if(taVehicleBOLst==null || taVehicleBOLst.size()==0){
			return null;
		}*/
		
		return taVehicleBOLst;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Integer> getAvailableTAVehicleIds(
			AvailableResourceCriteriaBO availableResourceCriteriaBO) {
		List<Integer> taVehicleBOLst = new ArrayList<Integer>();
		
		String formattedStartDate = DateUtils.formatDate("yyyy-MM-dd", availableResourceCriteriaBO.getScheduledStartDate());
		String formattedStartDateTime =  formattedStartDate + " " + availableResourceCriteriaBO.getScheduledStartTime();
	
		String formattedEndDate = DateUtils.formatDate("yyyy-MM-dd", availableResourceCriteriaBO.getScheduledEndDate());
		String formattedEndDateTime =  formattedEndDate + " " + availableResourceCriteriaBO.getScheduledEndTime();

		
		try{
		
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
				
				String office = (availableResourceCriteriaBO.getOfficeLocCodeList()!=null
						&&	!availableResourceCriteriaBO.getOfficeLocCodeList().isEmpty())?availableResourceCriteriaBO.getOfficeLocCodeList().get(0):"";
		
				Query query = this.hibernateTemplate.getSessionFactory().getCurrentSession().createSQLQuery(
						"execute procedure roms_sp_available_vehicle_ids(:startDate,:endDate,:cat,:region)")
						.setParameter("startDate", dateFormat.parse(formattedStartDateTime))
						.setParameter("endDate", dateFormat.parse(formattedEndDateTime))
						.setParameter("cat", availableResourceCriteriaBO.getCategoryId())
						.setParameter("region", office);
						
				//System.err.println("SP Query: "+query.toString());		
				
				List result = query.list();
				
				for(int i=0; i<result.size(); i++){
					Integer taVehicleId = (Integer) result.get(i);
			        taVehicleBOLst.add(taVehicleId);
				}

		
		}catch (Exception e) {
			e.printStackTrace();
		}
				

		/*if(taVehicleBOLst==null || taVehicleBOLst.size()==0){
			return null;
		}*/
		
		return taVehicleBOLst;
	}

	/**
	 * @author kpowell 2014-12-16
	 * @updated to use the stored procedure which retrieves a list of person that have been assigned in the period specified
	 * this ensures that if the sp is modified the function will also be affected.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean isPersonAvailable(AvailableResourceCriteriaBO availableResourceCriteriaBO) {
		
/*		String formattedStartDate = DateUtils.formatDate("yyyy-MM-dd", availableResourceCriteriaBO.getScheduledStartDate());
		String formattedStartDateTime =  formattedStartDate + " " + availableResourceCriteriaBO.getScheduledStartTime();
	
		String formattedEndDate = DateUtils.formatDate("yyyy-MM-dd", availableResourceCriteriaBO.getScheduledEndDate());
		String formattedEndDateTime =  formattedEndDate + " " + availableResourceCriteriaBO.getScheduledEndTime();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");	
		try {
			
		//System.err.println("execute procedure roms_sp_get_assigned_resource("+dateFormat.parse(formattedStartDateTime)+", "+dateFormat.parse(formattedEndDateTime));
		Query query = this.hibernateTemplate.getSessionFactory().getCurrentSession().createSQLQuery(
					"execute procedure roms_sp_is_resource_available(:startDate,:endDate,'P', :resourceId, :currentOperationId)")
					//.addEntity(Integer.class)
					.setParameter("startDate", dateFormat.parse(formattedStartDateTime))
					.setParameter("endDate", dateFormat.parse(formattedEndDateTime))
					.setParameter("resourceId", availableResourceCriteriaBO.getResourceId())
					.setParameter("currentOperationId", availableResourceCriteriaBO.getRoadOperationId());*/
				
		//fix made to query the temp table that would be created by the executing the stored procedure above
		//kpowell- 2015-01-21
		/*Query availablepersonsList = this.hibernateTemplate.getSessionFactory().getCurrentSession().createSQLQuery(
				"select person_id from temp_persons");//.addEntity(Integer.class);
*/		//
		
		/*Integer result = (Integer)query.list().get(0);//returns the first assigned operation not including the current op
		if(result != null){
		//if(result.contains(availableResourceCriteriaBO.getResourceId())){
			return false; //person is not available
		}
		
		
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}*/
		
		return isResourceAvailable(availableResourceCriteriaBO, new Character('P'));
	}

	/**
	 * Allows reuse & for ease of maitenance
	 * @param availableResourceCriteriaBO
	 * @param resourceType
	 * @return
	 */
	private boolean isResourceAvailable(AvailableResourceCriteriaBO availableResourceCriteriaBO, Character resourceType){
		
		String formattedStartDate = DateUtils.formatDate("yyyy-MM-dd", availableResourceCriteriaBO.getScheduledStartDate());
		String formattedStartDateTime =  formattedStartDate + " " + availableResourceCriteriaBO.getScheduledStartTime();
	
		String formattedEndDate = DateUtils.formatDate("yyyy-MM-dd", availableResourceCriteriaBO.getScheduledEndDate());
		String formattedEndDateTime =  formattedEndDate + " " + availableResourceCriteriaBO.getScheduledEndTime();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");	
		
		Query query;
		try {
			query = this.hibernateTemplate.getSessionFactory().getCurrentSession().createSQLQuery(
					"execute procedure roms_sp_is_resource_available(:startDate,:endDate,:resourceType, :resourceId, :currentOperationId)")
					//.addEntity(Integer.class)
					.setParameter("startDate", dateFormat.parse(formattedStartDateTime))
					.setParameter("endDate", dateFormat.parse(formattedEndDateTime))
					.setParameter("resourceId", availableResourceCriteriaBO.getResourceId())
					.setParameter("currentOperationId", availableResourceCriteriaBO.getRoadOperationId())
					.setParameter("resourceType", resourceType);
			
			
			
			//fix made to query the temp table that would be created by the executing the stored procedure above
			//kpowell- 2015-01-21
			/*Query availablepersonsList = this.hibernateTemplate.getSessionFactory().getCurrentSession().createSQLQuery(
							"select person_id from temp_persons");//.addEntity(Integer.class);
			 */		//

			Integer result = (Integer)query.list().get(0);//returns the first assigned operation not including the current op
			if(result != null){
				//if(result.contains(availableResourceCriteriaBO.getResourceId())){
				return false; //resource is not available
			}
			
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean isTAVehicleAvailable(AvailableResourceCriteriaBO availableResourceCriteriaBO) {		
		return isResourceAvailable(availableResourceCriteriaBO, new Character('V'));
	}
	
	private String returnCommaDelimitedIntegerList(List<Integer> stringList){
		String commaDelimitedList="";
		
		int listSize = stringList.size();
		
		for(int i=0; i<listSize; i++){
			commaDelimitedList = commaDelimitedList + stringList.get(i);
			if(i<listSize-1){
				commaDelimitedList = commaDelimitedList + ", ";
			}
		}
		return commaDelimitedList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<StrategyRuleBO> getOperationStrategyRule(
			List<Integer> strategyIdList) {
		
		List<StrategyRuleBO> strategyRuleList = new ArrayList<StrategyRuleBO>();
		
		String strategyList = returnCommaDelimitedIntegerList(strategyIdList);
		
		StringBuffer queryString = new StringBuffer("select new fsl.ta.toms.roms.bo.StrategyRuleBO(" +
				"r.strategyRuleKey.personType.personTypeId, max(r.minimum), max(r.maximum))" +
				" from StrategyRuleDO r");
		
		queryString.append(" where r.strategyRuleKey.strategy.strategyId IN (" +  strategyList + ")");
		
	
		queryString.append(" group by r.strategyRuleKey.personType.personTypeId");
		
		
		strategyRuleList = hibernateTemplate.find(queryString.toString());

		/*if(strategyRuleList==null || strategyRuleList.size()==0){
			return null;
		}*/
		
		return strategyRuleList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean isVehicleRequired(List<Integer> strategyIdList) {
		List<StrategyDO> strategyDOList = new ArrayList<StrategyDO>();
		
		String strategyList = returnCommaDelimitedIntegerList(strategyIdList);
		
		StringBuffer queryString = new StringBuffer(" from StrategyDO s");
		
		queryString.append(" where s.strategyId IN (" +  strategyList + ")");
		
		queryString.append(" and s.vehicleRequired = ").append("'Y'");;
		
		
		strategyDOList = hibernateTemplate.find(queryString.toString());

		if(strategyDOList==null || strategyDOList.size()==0){
			return false;
		}
		
		return true;
	}

	@Override
	public Integer saveRoadOperation(RoadOperationDO roadOperationDO) {
		Integer roadOperationId = (Integer) (hibernateTemplate.save(roadOperationDO));
		
		return roadOperationId;
	}

	@Override
	public Integer saveTeam(TeamDO teamDO) {
		Integer teamId = (Integer) (hibernateTemplate.save(teamDO));
		
		return teamId;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<AssignedPersonBO> findAssignedPersonByPersonType(
			Integer roadOperationId, String personTypeId) {
		List<AssignedPersonBO> assignedPersonList = new ArrayList<AssignedPersonBO>();
		
		String queryString = "select new fsl.ta.toms.roms.bo.AssignedPersonBO(o)" +
				" from AssignedPersonDO o, TeamDO t where o.assignedPersonKey.team.teamId = t.teamId and t.roadOperation.roadOperationId = :roadOperationId" +
				" and o.assignedPersonKey.personType.personTypeId = :personTypeId";
		 
		
		String[] paramNames = {"roadOperationId", "personTypeId"};
		Object[] values = {roadOperationId, personTypeId};
		
		assignedPersonList = hibernateTemplate.findByNamedParam(queryString, paramNames, values);
			
		/*if(assignedPersonList==null || assignedPersonList.size()<=0){
			return null;
		}*/
		
		return assignedPersonList;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<AssignedPersonBO> findAssignedTeamPersonByPersonType(Integer teamId, String personTypeId){
		List<AssignedPersonBO> assignedPersonList = new ArrayList<AssignedPersonBO>();
		
		String queryString = "select new fsl.ta.toms.roms.bo.AssignedPersonBO(o)" +
				" from AssignedPersonDO o where o.assignedPersonKey.team.teamId = :teamId" +
				" and o.assignedPersonKey.personType.personTypeId = :personTypeId";
		 
		
		String[] paramNames = {"teamId", "personTypeId"};
		Object[] values = {teamId, personTypeId};
		
		assignedPersonList = hibernateTemplate.findByNamedParam(queryString, paramNames, values);
			
		/*if(assignedPersonList==null || assignedPersonList.size()<=0){
			return null;
		}*/
		
		return assignedPersonList;
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public TeamArteryDO findAssignedArteryById(Integer arteryId,
			Integer teamId) {
		List<TeamArteryDO> assignedArteryList = new ArrayList<TeamArteryDO>();
		
		String queryString = "from TeamArteryDO o where o.operationArteryKey.team.teamId = :teamId " +
				"and o.operationArteryKey.artery.arteryId = :arteryId";
		 
		
		String[] paramNames = {"teamId", "arteryId"};
		Object[] values = {teamId, arteryId};
		
		assignedArteryList = hibernateTemplate.findByNamedParam(queryString, paramNames, values);
			
		if(assignedArteryList!=null && assignedArteryList.size()==1){
			return assignedArteryList.get(0);
		}
		
		return null;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public TeamLocationDO findAssignedLocationById(Integer locationId,
			Integer teamId) {
		List<TeamLocationDO>  assignedLocationList = new ArrayList<TeamLocationDO>();
		
		String queryString = "from TeamLocationDO o where o.operationLocationKey.team.teamId = :teamId " +
				"and o.operationLocationKey.location.locationId = :locationId";
		 
		
		String[] paramNames = {"teamId", "locationId"};
		Object[] values = {teamId, locationId};
		
		assignedLocationList = hibernateTemplate.findByNamedParam(queryString, paramNames, values);
			
		if(assignedLocationList!=null && assignedLocationList.size()==1){
			return assignedLocationList.get(0);
		}
		
		return null;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public TeamParishDO findAssignedParishById(String parishCode,Integer teamId) {
		List<TeamParishDO>  assignedParishList = new ArrayList<TeamParishDO>();
		
		String queryString = "from TeamParishDO o where o.teamParishKey.team.teamId = :teamId " +
				"and o.teamParishKey.parish.parishCode = :parishCode";
		 
		
		String[] paramNames = {"teamId", "parishCode"};
		Object[] values = {teamId, parishCode};
		
		assignedParishList = hibernateTemplate.findByNamedParam(queryString, paramNames, values);
			
		if(assignedParishList!=null && assignedParishList.size()==1){
			return assignedParishList.get(0);
		}
		
		return null;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public AssignedPersonDO findAssignedPersonById(Integer personId,
			Integer teamId, String personTypeId) {
		List<AssignedPersonDO> assignedPersonList = new ArrayList<AssignedPersonDO>();
		
		String queryString = "from AssignedPersonDO o where o.assignedPersonKey.team.teamId = :teamId " +
				" and o.assignedPersonKey.personType.personTypeId = :personTypeId and o.assignedPersonKey.person.personId = :personId";
		 
		
		String[] paramNames = {"teamId", "personTypeId", "personId"};
		Object[] values = {teamId, personTypeId, personId};
		
		assignedPersonList = hibernateTemplate.findByNamedParam(queryString, paramNames, values);
			
		if(assignedPersonList!=null && assignedPersonList.size()==1){
			return assignedPersonList.get(0);
		}
		
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public AssignedVehicleDO findAssignedVehicleById(Integer taVehicleId,
			Integer teamId) {
		List<AssignedVehicleDO> assignedVehicleList = new ArrayList<AssignedVehicleDO>();
		
		String queryString = "from AssignedVehicleDO o where o.assignedVehicleKey.team.teamId = :teamId " +
				" and o.assignedVehicleKey.taVehicle.taVehicleId= :taVehicleId";
		 
		
		String[] paramNames = {"teamId", "taVehicleId"};
		Object[] values = {teamId, taVehicleId};
		
		assignedVehicleList = hibernateTemplate.findByNamedParam(queryString, paramNames, values);
			
		if(assignedVehicleList!=null && assignedVehicleList.size()==1){
			return assignedVehicleList.get(0);
		}
		
		return null;
	}
	
	

	@SuppressWarnings("unchecked")
	@Override
	public RoadOperationBO findRoadOperationByName(String operationName) {
		List<RoadOperationBO> roadOperationList = new ArrayList<RoadOperationBO>();
		
		String queryString = "select new fsl.ta.toms.roms.bo.RoadOperationBO(o)" +
				" from RoadOperationDO o where upper(o.operationName) = :operationName ";
		 
		
			roadOperationList = hibernateTemplate.findByNamedParam(queryString, "operationName", operationName.toUpperCase());
			
		if(roadOperationList!=null && roadOperationList.size()==1){
			return roadOperationList.get(0);
		}
		
		return null;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<LocationDO> returnLocationDOList(List<Integer> locationIdList) {
		List<LocationDO> locationDOList = new ArrayList<LocationDO>();
		
		String locationList = returnCommaDelimitedIntegerList(locationIdList);
		
		StringBuffer queryString = new StringBuffer(" from LocationDO l");
		
		queryString.append(" where l.locationId IN (" +  locationList + ")");
		
				
		locationDOList = hibernateTemplate.find(queryString.toString());

		if(locationDOList!=null && locationDOList.size()>0){
			return locationDOList;
		}
		
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<StrategyDO> returnStrategyDOList(List<Integer> strategyIdList) {
		List<StrategyDO> strategyDOList = new ArrayList<StrategyDO>();
		
		String strategyList = returnCommaDelimitedIntegerList(strategyIdList);
		
		StringBuffer queryString = new StringBuffer(" from StrategyDO s");
		
		queryString.append(" where s.strategyId IN (" +  strategyList + ")");
		
				
		strategyDOList = hibernateTemplate.find(queryString.toString());

		if(strategyDOList!=null && strategyDOList.size()>0){
			return strategyDOList;
		}
		
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean isArteryRequired(List<Integer> strategyIdList) {
		List<StrategyDO> strategyDOList = new ArrayList<StrategyDO>();
		
		String strategyList = returnCommaDelimitedIntegerList(strategyIdList);
		
		StringBuffer queryString = new StringBuffer(" from StrategyDO s");
		
		queryString.append(" where s.strategyId IN (" +  strategyList + ")");
		
		queryString.append(" and s.arteryRequired = ").append("'Y'");	
		
		strategyDOList = hibernateTemplate.find(queryString.toString());

		if(strategyDOList!=null && strategyDOList.size()>0){
			return true;
		}
		
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ArteryDO> returnArteryDOList(List<Integer> arteryIdList) {
		List<ArteryDO> strategyDOList = new ArrayList<ArteryDO>();
		
		String arteryList = returnCommaDelimitedIntegerList(arteryIdList);
		
		StringBuffer queryString = new StringBuffer(" from ArteryDO a");
		
		queryString.append(" where a.arteryId IN (" +  arteryList + ")");
		
				
		strategyDOList = hibernateTemplate.find(queryString.toString());

		if(strategyDOList!=null && strategyDOList.size()>0){
			return strategyDOList;
		}
		
		return null;
	}



	@Override
	public List<RoadOperationBO> lookupRoadOperationWithDateRange(
			RoadOperationCriteriaBO roadOperationCriteriaBO) 
	{
		List<RoadOperationBO> roadOperationList = new ArrayList<RoadOperationBO>();
		
		StringBuffer queryString = new StringBuffer("select new fsl.ta.toms.roms.bo.RoadOperationBO(o) from RoadOperationDO o");
				
				queryString.append(" where o.roadOperationId in (select distinct o.roadOperationId from RoadOperationDO o");
				if(roadOperationCriteriaBO.getStrategyId()!=null && roadOperationCriteriaBO.getStrategyId()>0){
					queryString.append(", OperationStrategyDO s");
				}
				if(StringUtils.trimToNull(roadOperationCriteriaBO.getParishCode())!=null || (roadOperationCriteriaBO.getLocationId()!=null && roadOperationCriteriaBO.getLocationId()>0)){
					queryString.append(", TeamLocationDO l");
				}
				if(roadOperationCriteriaBO.getArteryId()!=null && roadOperationCriteriaBO.getArteryId()>0){
					queryString.append(", TeamArteryDO a");
				}
				if(StringUtils.trimToNull(roadOperationCriteriaBO.getTeamLeadStaffId()) != null || (roadOperationCriteriaBO.getArteryId()!=null && roadOperationCriteriaBO.getArteryId()>0) || (StringUtils.trimToNull(roadOperationCriteriaBO.getParishCode())!=null || (roadOperationCriteriaBO.getLocationId()!=null && roadOperationCriteriaBO.getLocationId()>0))){
					queryString.append(", TeamDO t");
				}
		queryString.append(" where (o.roadOperationId is not null)");
		
		if(roadOperationCriteriaBO.getRoadOperationId() != null && roadOperationCriteriaBO.getRoadOperationId()>0){
			queryString.append(" and o.roadOperationId = ").append(roadOperationCriteriaBO.getRoadOperationId());
		}
		
		//Operation Name
		if (StringUtils.trimToNull(roadOperationCriteriaBO.getOperationName()) != null) {
			
			queryString.append(" and upper(o.operationName) like '%").append(StringUtil.quoteReplace(roadOperationCriteriaBO.getOperationName().trim()).toUpperCase()).append("%'");
		}
		
		//Category
		if (StringUtils.trimToNull(roadOperationCriteriaBO.getCategoryId()) != null) {
			
			queryString.append(" and upper(o.category.categoryId) = '").append(StringUtil.quoteReplace(roadOperationCriteriaBO.getCategoryId().trim()).toUpperCase()).append("'");
		}
		
		//Strategy
		if (roadOperationCriteriaBO.getStrategyId() != null && roadOperationCriteriaBO.getStrategyId()>0) {
			queryString.append(" and o.roadOperationId = s.operationStrategyKey.roadOperation.roadOperationId and s.operationStrategyKey.strategy.strategyId = ").append(roadOperationCriteriaBO.getStrategyId());
		}
		
		//Team Lead
		if (StringUtils.trimToNull(roadOperationCriteriaBO.getTeamLeadStaffId()) != null) {
			queryString.append(" and o.roadOperationId = t.roadOperation.roadOperationId and t.teamLead.staffId ='").append(StringUtil.quoteReplace(roadOperationCriteriaBO.getTeamLeadStaffId().trim()).toUpperCase()).append("'");
		}
		
		//Scheduler
		if (StringUtils.trimToNull(roadOperationCriteriaBO.getSchedulerStaffId()) != null) {
			
			queryString.append(" and upper(o.scheduler.staffId) = '").append(StringUtil.quoteReplace(roadOperationCriteriaBO.getSchedulerStaffId().trim()).toUpperCase()).append("'");
		}
		
		//Office Location
		if (StringUtils.trimToNull(roadOperationCriteriaBO.getOfficeLocCode()) != null) {
			
			queryString.append(" and upper(o.officeLocCode) = '").append(StringUtil.quoteReplace(roadOperationCriteriaBO.getOfficeLocCode()).toUpperCase()).append("'");
		}
		

		//if parish or location selected
		if(StringUtils.trimToNull(roadOperationCriteriaBO.getParishCode())!=null || (roadOperationCriteriaBO.getLocationId()!=null && roadOperationCriteriaBO.getLocationId()>0)){
			queryString.append(" and o.roadOperationId = t.roadOperation.roadOperationId and t.teamId = l.operationLocationKey.team.teamId");
		}
		
		//Parish
		if (StringUtils.trimToNull(roadOperationCriteriaBO.getParishCode()) != null && (roadOperationCriteriaBO.getLocationId() == null || roadOperationCriteriaBO.getLocationId()<1)) {
			
			//queryString.append(" and o.roadOperationId = l.operationLocationKey.roadOperation.roadOperationId and upper(l.operationLocationKey.location.parish.parishCode) = '").append(StringUtil.quoteReplace(roadOperationCriteriaBO.getParishCode()).toUpperCase()).append("'");
			queryString.append("  and upper(l.operationLocationKey.location.parish.parishCode) = '").append(StringUtil.quoteReplace(roadOperationCriteriaBO.getParishCode()).toUpperCase()).append("'");
		}
		
		//Location
		if (roadOperationCriteriaBO.getLocationId() != null && roadOperationCriteriaBO.getLocationId()>0) {
			
			//queryString.append(" and o.roadOperationId = l.operationLocationKey.roadOperation.roadOperationId and l.operationLocationKey.location.locationId = ").append(roadOperationCriteriaBO.getLocationId());
			queryString.append(" and l.operationLocationKey.location.locationId = ").append(roadOperationCriteriaBO.getLocationId());

		}
		
		//Artery
		if (roadOperationCriteriaBO.getArteryId() != null && roadOperationCriteriaBO.getArteryId()>0) {
			
			queryString.append(" and o.roadOperationId = t.roadOperation.roadOperationId and t.teamId = a.operationArteryKey.team.teamId and a.operationArteryKey.artery.arteryId = ").append(roadOperationCriteriaBO.getArteryId());
		}
		

		
		//isBackDated is true
		if (roadOperationCriteriaBO.isBackDated() == true) {
			
			queryString.append(" and upper(o.backDated) = ").append("'Y'");
		}
		
		//isAuthorized is false
		//modified by kpowell 2015-12-03; variable rename is recommended
		if (roadOperationCriteriaBO.isUnAuthorized() == true) {			
			queryString.append(" and upper(o.authorized) = ").append("'Y'");
		}
		
		
		//Status
		if (StringUtils.trimToNull(roadOperationCriteriaBO.getStatusId()) != null) {
			
			queryString.append(" and upper(o.status.statusId) = '").append(StringUtil.quoteReplace(roadOperationCriteriaBO.getStatusId()).toUpperCase()).append("'");
		}
		
		
		//Operation Start Date & Start Time - End Date & End Time
		if (roadOperationCriteriaBO.getOperationStartDate() != null && roadOperationCriteriaBO.getOperationEndDate() != null)
		{
			try 
			{
				if(StringUtils.trimToNull(roadOperationCriteriaBO.getOperationStartTime()) != null && StringUtils.trimToNull(roadOperationCriteriaBO.getOperationEndTime()) != null)
				{
					String formattedStartDate = DateUtils.formatDate("yyyy-MM-dd", roadOperationCriteriaBO.getOperationStartDate());
					formattedStartDate =  formattedStartDate + " " + roadOperationCriteriaBO.getOperationStartTime();
				
					String formattedEndDate = DateUtils.formatDate("yyyy-MM-dd", roadOperationCriteriaBO.getOperationEndDate());
					formattedEndDate =  formattedEndDate + " " + roadOperationCriteriaBO.getOperationEndTime();
					
					if(roadOperationCriteriaBO.isScheduledDTime()==true)
					{
						queryString.append(" and ( o.scheduledStartDtime between '").append(formattedStartDate)
							.append("' and '").append(formattedEndDate).append("' or ");
						
						queryString.append(" o.scheduledEndDtime between '").append(formattedStartDate)
							.append("' and '").append(formattedEndDate).append(" ) ");
						
					}
					else
					{
						queryString.append(" and ( o.actualStartDtime between '").append(formattedStartDate)
							.append("' and '").append(formattedEndDate).append("' or ");
					
						queryString.append(" o.actualEndDtime between '").append(formattedStartDate)
							.append("' and '").append(formattedEndDate).append("' ) ");
					}
				}
				else
				{
						Date startDate = null;
					
						startDate = DateUtils.utilDateToSqlDate(roadOperationCriteriaBO.getOperationStartDate());
						
						Date endDate = null;
						
						endDate = DateUtils.utilDateToSqlDate(roadOperationCriteriaBO.getOperationEndDate());
						
						if(roadOperationCriteriaBO.isScheduledDTime()==true)
						{
							queryString.append(" and ( Date(o.scheduledStartDtime) between '").append(startDate).append("' and '")
								.append(endDate).append("' or ");	
							
							queryString.append(" Date(o.scheduledEndDtime) between '").append(startDate).append("' and '")
								.append(endDate).append("' ) ");
						}
						else
						{
							queryString.append(" and ( Date(o.actualStartDtime) between '").append(startDate).append("' and '")
							.append(endDate).append("' or ");	
						
							queryString.append(" Date(o.actualEndDtime) between '").append(startDate).append("' and '")
								.append(endDate).append("' ) ");
						}
					
				}
			} 
			catch (ParseException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		

		queryString.append(")");
		queryString.append(" order by o.auditEntry.createDTime desc");
		
		roadOperationList =  hibernateTemplate.find(queryString.toString());
	 
		return roadOperationList;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<TeamDO> findOperationTeams(Integer roadOperationId) {
		List<TeamDO> teamList = new ArrayList<TeamDO>();
		
		String queryString = "from TeamDO t" +
				" where t.roadOperation.roadOperationId = :roadOperationId";
		 
		
		teamList = hibernateTemplate.findByNamedParam(queryString, "roadOperationId", roadOperationId);
			
		/*if(teamList==null || teamList.size()==0){
			return null;
		}*/
		
		return teamList;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ParishDO> findRegionParishes(String officeLocationCode) {
		List<ParishDO> parishList = new ArrayList<ParishDO>();
		
		String queryString = "from ParishDO p" +
				" where p.officeLocationCode = :officeLocationCode";
		 
		
		parishList = hibernateTemplate.findByNamedParam(queryString, "officeLocationCode", officeLocationCode);
			
		/*if(parishList==null || parishList.size()==0){
			return null;
		}*/
		
		return parishList;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<LocationBO> returnOperationLocationList(Integer roadOperationId) {
		List<LocationBO> locationDOList = new ArrayList<LocationBO>();
		
		
		StringBuffer queryString = new StringBuffer("select new fsl.ta.toms.roms.bo.LocationBO(l.operationLocationKey.location) from TeamLocationDO l" +
				" where l.operationLocationKey.team.roadOperation.roadOperationId = :roadOperationId");
		
		
				
		locationDOList = hibernateTemplate.findByNamedParam(queryString.toString(), "roadOperationId", roadOperationId);

		if(locationDOList!=null && locationDOList.size()>0){
			return locationDOList;
		}
		
		return null;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<ArteryBO> findOperationArteryList(List<Integer> locationIdList, String arteryStr) {
		List<ArteryBO> arteryList = new ArrayList<ArteryBO>();
		
		String locationList = returnCommaDelimitedIntegerList(locationIdList);
		
		StringBuffer queryString = new StringBuffer("select new fsl.ta.toms.roms.bo.ArteryBO(a) from ArteryDO a");
		
		queryString.append(" where upper(a.description) like '%").append(StringUtil.quoteReplace(arteryStr.trim()).toUpperCase()).append("%'");
		
		queryString.append(" and a.location.locationId IN (" +  locationList + ")");
		
				
		arteryList = hibernateTemplate.find(queryString.toString());

		if(arteryList!=null && arteryList.size()>0){
			return arteryList;
		}
		
		return null;
	}
	
	
	@Override
	@SuppressWarnings("unchecked")
	public List<RoadOperationBO> findOperationForRoadCheckList(List<String> statusIdList, String operationStr) {
		List<RoadOperationBO> operationList = new ArrayList<RoadOperationBO>();
		
		String statusList = returnCommaDelimitedStringList(statusIdList);
		
		StringBuffer queryString = new StringBuffer("select new fsl.ta.toms.roms.bo.RoadOperationBO(o) from RoadOperationDO o");
		
		queryString.append(" where upper(o.operationName) like '%").append(StringUtil.quoteReplace(operationStr.trim()).toUpperCase()).append("%'");
		
		queryString.append(" and o.status.statusId IN (" +  statusList + ")");
		
				
		operationList = hibernateTemplate.find(queryString.toString());

		if(operationList!=null && operationList.size()>0){
			return operationList;
		}
		
		return null;
	}



	/* (non-Javadoc)
	 * @see fsl.ta.toms.roms.dao.RoadOperationDAO#findOfficeLocCodeList(java.lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<String> findOfficeLocCodeList(Integer roadOperationId) 
	{
		Criteria criteria = this.hibernateTemplate.getSessionFactory()
				.getCurrentSession()
				.createCriteria(OperationRegionDO.class, "opRegion");		

		
		/*Filter result based on road operation ID*/
		criteria.add(Restrictions.eq("opRegion.operationRegionKey.roadOperation.roadOperationId", roadOperationId));
		
		List<String> officeLocCodes = new ArrayList<String>();
		
		criteria.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
		
		Iterator iterator = criteria.list().iterator();
		
		while(iterator.hasNext())
		{
			Map map = (Map) iterator.next();
			
			OperationRegionDO opRegion = (OperationRegionDO)map.get("opRegion");			
			

			officeLocCodes.add(opRegion.getOperationRegionKey().getLoc_code());
		}
		
		
		return officeLocCodes;
	}
	
	/**
	 * This function is a convenience method which returns all the team ids for an road operation
	 * @param roadOpId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<Integer> getTeamIdsForRoadOp(Integer roadOpId)
	{
		Criteria criteria = this.hibernateTemplate.getSessionFactory().getCurrentSession().createCriteria(TeamDO.class,"team");
		
		criteria.add(Restrictions.eq("team.roadOperation.roadOperationId", roadOpId));
		
		criteria.setProjection(Projections.property("team.teamId"));
		
		return criteria.list();
	}

	@Override
	@SuppressWarnings("unchecked")
	public RoadOperationBO findActiveOperationForUser(String userName) {
		List<RoadOperationBO> roadOperationList = new ArrayList<RoadOperationBO>();
		
		
		String currentDate = DateUtils.formatDate("yyyy-MM-dd", Calendar.getInstance().getTime());	
		//System.err.println("currentDate: " + currentDate);
		StringBuffer queryString = new StringBuffer("select new fsl.ta.toms.roms.bo.RoadOperationBO(a.assignedPersonKey.team.roadOperation) " +
				"from TAStaffDO s, AssignedPersonDO a" +
				" where a.assignedPersonKey.team.teamId IN (select t.teamId from RoadOperationDO o, TeamDO t " +
				" where t.roadOperation.roadOperationId = o.roadOperationId and o.status.statusId = :started " +
				 " and Date(o.scheduledEndDtime) >= '" + currentDate + "')" +
				" and s.loginUsername = :userName and a.assignedPersonKey.person.personId = s.person.personId" + 
				" and lower(a.attended) = 'y'" +//added by kpowell :2014-11-20- ensure absent resource cannot perform road check on tablet
				 " order by a.assignedPersonKey.team.roadOperation.actualStartDtime desc");///ensure the most recent started operation is displayed on the tablet
		
		
		String[] paramNames = {"userName", "started"};
		Object[] values = {userName, Constants.Status.ROAD_OPERATION_STARTED};
		
		roadOperationList = hibernateTemplate.findByNamedParam(queryString.toString(), paramNames, values);
		
		//System.err.println("Size: " + roadOperationList.size());
		if(roadOperationList!=null && roadOperationList.size()>0){
			return roadOperationList.get(0);
		}
		
		return null;
	}



	@Override
	public List<RoadOperationBO> findOperationForRoadCheckList(
			List<String> statusIdList) {
		List<RoadOperationBO> operationList = new ArrayList<RoadOperationBO>();
		
		String statusList = returnCommaDelimitedStringList(statusIdList);
		
		StringBuffer queryString = new StringBuffer("select new fsl.ta.toms.roms.bo.RoadOperationBO(o) from RoadOperationDO o");
		
		queryString.append(" where o.status.statusId IN (" +  statusList + ")");
		queryString.append(" order by upper(o.operationName)");
				
		operationList = hibernateTemplate.find(queryString.toString());

		if(operationList!=null && operationList.size()>0){
			return operationList;
		}
		
		return null;
	}



	@Override
	public List<ArteryBO> findOperationArteryList(List<Integer> locationIdList, Integer operationId) 
	{
		List<ArteryBO> arteryList = new ArrayList<ArteryBO>();
		
		String locationList = returnCommaDelimitedIntegerList(locationIdList);
		
		StringBuffer queryString = new StringBuffer("select new fsl.ta.toms.roms.bo.ArteryBO(a) from ArteryDO a");
			
		//queryString.append(" where a.location.locationId IN (" +  locationList + ")");
		
		
		if(operationId != null)
		{
			List<Integer> teamIds = this.getTeamIdsForRoadOp(operationId);
			
			//modified by kpowell: to ensure that all arteries are retrieved both for 
			//arteries selected and location without arteries 
			List<Integer> arteryIds = findLocationArteryList(locationIdList,teamIds);
			/*List<Integer> arteryIds = getArteryIdsForTeams(teamIds);*/
			
			if(arteryIds.size() > 0)
			{
				queryString.append(" where a.arteryId IN (" +   returnCommaDelimitedIntegerList(arteryIds) + ")");
			}
			else{
				queryString.append(" where a.location.locationId IN (" +  locationList + ")");
			}
		}
		
		queryString.append(" order by upper(a.description)");
				
		arteryList = hibernateTemplate.find(queryString.toString());

		if(arteryList!=null && arteryList.size()>0){
			return arteryList;
		}
		
		return null;
	}
	
	/**
	 * @author kpowell
	 * @param locationIdList
	 * @param teamIds
	 * @return
	 */
	public List<Integer> findLocationArteryList(List<Integer> locationIdList, List<Integer> teamIds) 
	{
		//System.err.println("inside findLocationArteryList()");
		List<Integer> arteryList = null;
		List<Integer> allArteryListForTeam = new ArrayList<Integer>();
		StringBuffer queryString= new StringBuffer();
		Set<Integer> uniquesIds = new HashSet<Integer>();
		
		for(Integer locId : locationIdList){
			if(!uniquesIds.contains(locId)){
				uniquesIds.add(locId);
				arteryList = new ArrayList<Integer>();
				//check if artery exist for teamIds/locId
				queryString = new StringBuffer("select distinct teamArt.operationArteryKey.artery.arteryId from TeamArteryDO teamArt");				
				queryString.append(" where teamArt.operationArteryKey.team.teamId IN (" +  returnCommaDelimitedIntegerList(teamIds) + ")");
				queryString.append(" and teamArt.operationArteryKey.artery.location.locationId = " +  locId );				
				arteryList = hibernateTemplate.find(queryString.toString());			
				
				
				if(arteryList.size()==0){
					//if no arteries exists for selected location then add all arteries for location
					queryString= new StringBuffer();
					//System.err.println("LocationId:"+ locId+" no artery were selected");
					queryString = new StringBuffer("select arteryId from ArteryDO a");				
					queryString.append(" where a.location.locationId = " +  locId );
					arteryList = hibernateTemplate.find(queryString.toString());
				}
				
				allArteryListForTeam.addAll(arteryList);		
			
				//System.err.println("Location :"+ locId + " ["+arteryList.size()+"]");
			}else{
	            //System.err.println("duplicate found::"+ locId);
	            
	        }
			
		}
		//System.err.println("exit findLocationArteryList()");
		return allArteryListForTeam;
		
	}
	
	private List<Integer> getArteryIdsForTeams(List<Integer> teamsIds)
	{
		Criteria criteria = this.hibernateTemplate.getSessionFactory().getCurrentSession().createCriteria(TeamArteryDO.class,"teamArt");
		
		criteria.add(Restrictions.in("teamArt.operationArteryKey.team.teamId", teamsIds));
		
		criteria.setProjection(Projections.property("teamArt.operationArteryKey.artery.arteryId"));
		
		return criteria.list();
		
	}
	
	@Override
	public List<ArteryBO> findArteryList() 
	{
		List<ArteryBO> arteryList = new ArrayList<ArteryBO>();
		
		
		StringBuffer queryString = new StringBuffer("select new fsl.ta.toms.roms.bo.ArteryBO(a) from ArteryDO a");
		queryString.append(" order by upper(a.description)");	
				
		arteryList = hibernateTemplate.find(queryString.toString());

		if(arteryList!=null && arteryList.size()>0){
			return arteryList;
		}
		
		return null;
	}
	
	public void saveAssPerson(AssignedPersonDO assignedPersonDO)
	{
		//hibernateTemplate.flush();
		hibernateTemplate.save(assignedPersonDO);
		
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<OperationStrategyDO> getOpStrategyListDO(Integer roadOperationId)
	{
		 Criteria criteriaOpStrat = this.hibernateTemplate.getSessionFactory().getCurrentSession().createCriteria(OperationStrategyDO.class, "opStrat");
		
		 criteriaOpStrat.add(Restrictions.eq("opStrat.operationStrategyKey.roadOperation.roadOperationId", roadOperationId));
		 
		List<OperationStrategyDO> operationStrategyListCurrentDO = criteriaOpStrat.list();
		 
		 return operationStrategyListCurrentDO;
		 
		
	}



	@SuppressWarnings("unchecked")
	@Override
	public List<TeamParishDO> getTeamParishListDO(Integer teamId)
	{
		Criteria criteriaTeamParish = this.hibernateTemplate.getSessionFactory().getCurrentSession().createCriteria(TeamParishDO.class, "teamParish");
		
		criteriaTeamParish.add(Restrictions.eq("teamParish.teamParishKey.team.teamId", teamId));
		
		
		return criteriaTeamParish.list();
	}



	@SuppressWarnings("unchecked")
	@Override
	public List<TeamLocationDO> getTeamLocationListDO(Integer teamId)
	{
		Criteria criteriaTeamLocation = this.hibernateTemplate.getSessionFactory().getCurrentSession().createCriteria(TeamLocationDO.class, "teamLocation");
		
		criteriaTeamLocation.add(Restrictions.eq("teamLocation.operationLocationKey.team.teamId", teamId));
		
		
		return criteriaTeamLocation.list();
	}



	@SuppressWarnings("unchecked")
	@Override
	public List<TeamArteryDO> getTeamArteryListDO(Integer teamId)
	{
		Criteria criteriaTeamArtery = this.hibernateTemplate.getSessionFactory().getCurrentSession().createCriteria(TeamArteryDO.class, "teamArtery");
		
		criteriaTeamArtery.add(Restrictions.eq("teamArtery.operationArteryKey.team.teamId", teamId));
		
		
		return criteriaTeamArtery.list();
	}



	@Override
	public String checkIfThereAreUnprocessedDocuments(Integer roadOpId)
	{
		try{
		StringBuilder messageOfUnprocessedDocuments = new StringBuilder();
		
		int countOfUnprocessedDocuments = 0;
		int countOfUnprocessedSummons = 0;
		int countOfUnprocessedWarningNotices = 0;
		int countOfUnprocessedWarningNoProsecution = 0;
		
		Criteria criteriaSummons = this.hibernateTemplate.getSessionFactory().getCurrentSession().createCriteria(SummonsDO.class, "s");
		
		criteriaSummons.createAlias("s.roadCheckOffenceOutcome", "rcoo");
		criteriaSummons.createAlias("rcoo.roadCheckOffence", "rco");
		criteriaSummons.createAlias("rco.roadCheck", "rc");
		criteriaSummons.createAlias("rc.compliance", "c");
		criteriaSummons.createAlias("c.roadOperation", "ro");
		criteriaSummons.createAlias("s.status", "st");
		
		criteriaSummons.add(Restrictions.eq("ro.roadOperationId", roadOpId));
		criteriaSummons.add(Restrictions.eq("st.statusId", Constants.DocumentStatus.CREATED));
		
		countOfUnprocessedSummons = criteriaSummons.list().size();
		
		Criteria criteriaWarningNotice = this.hibernateTemplate.getSessionFactory().getCurrentSession().createCriteria(WarningNoticeDO.class, "w");
		
		criteriaWarningNotice.createAlias("w.roadCheckOffenceOutcome", "rcoo");
		criteriaWarningNotice.createAlias("rcoo.roadCheckOffence", "rco");
		criteriaWarningNotice.createAlias("rco.roadCheck", "rc");
		criteriaWarningNotice.createAlias("rc.compliance", "c");
		criteriaWarningNotice.createAlias("c.roadOperation", "ro");
		criteriaWarningNotice.createAlias("w.status", "st");
		
		criteriaWarningNotice.add(Restrictions.eq("ro.roadOperationId", roadOpId));
		criteriaWarningNotice.add(Restrictions.eq("st.statusId", Constants.DocumentStatus.CREATED));
		
		countOfUnprocessedWarningNotices = criteriaWarningNotice.list().size();
		
		Criteria criteriaWarningNoProsecution = this.hibernateTemplate.getSessionFactory().getCurrentSession().createCriteria(WarningNoProsecutionDO.class, "w");
		
		criteriaWarningNoProsecution.createAlias("w.roadCheckOffenceOutcome", "rcoo");
		criteriaWarningNoProsecution.createAlias("rcoo.roadCheckOffence", "rco");
		criteriaWarningNoProsecution.createAlias("rco.roadCheck", "rc");
		criteriaWarningNoProsecution.createAlias("rc.compliance", "c");
		criteriaWarningNoProsecution.createAlias("c.roadOperation", "ro");
		criteriaWarningNoProsecution.createAlias("w.status", "st");
		
		criteriaWarningNoProsecution.add(Restrictions.eq("ro.roadOperationId", roadOpId));
		criteriaWarningNoProsecution.add(Restrictions.eq("st.statusId", Constants.DocumentStatus.CREATED));
		
		 countOfUnprocessedWarningNoProsecution = criteriaWarningNoProsecution.list().size();
		
		 countOfUnprocessedDocuments = countOfUnprocessedSummons + countOfUnprocessedWarningNoProsecution + countOfUnprocessedWarningNotices;
		 
		 if(countOfUnprocessedDocuments > 0) {
			 messageOfUnprocessedDocuments.append("Please note that the operation cannot be closed because there are ");
			 
			 if(countOfUnprocessedSummons > 0){
				 messageOfUnprocessedDocuments.append("[ " + countOfUnprocessedSummons + " ]" + " unprinted Summons");
			 }
			 
			 if(countOfUnprocessedWarningNoProsecution > 0){
				 if(messageOfUnprocessedDocuments.toString().contains("unprinted")){
					 messageOfUnprocessedDocuments.append(", ");
				 }
				 messageOfUnprocessedDocuments.append("[ " + countOfUnprocessedWarningNoProsecution + " ]" + " unprinted Warning No Prosecutions");
			 }
			 
			 if(countOfUnprocessedWarningNotices > 0){
				 if(messageOfUnprocessedDocuments.toString().contains("unprinted")){
					 messageOfUnprocessedDocuments.append(", ");
				 }
				 messageOfUnprocessedDocuments.append("[ " + countOfUnprocessedWarningNotices + " ]" + " unprinted Warning Notices");
			 }
			 
			 if(messageOfUnprocessedDocuments != null && messageOfUnprocessedDocuments.length() > 0){
				 messageOfUnprocessedDocuments.append(".");
				 
				 if(messageOfUnprocessedDocuments.toString().contains(",")){
					 messageOfUnprocessedDocuments.replace(messageOfUnprocessedDocuments.lastIndexOf(","), messageOfUnprocessedDocuments.lastIndexOf(",") + 1, " and ");
				 }
			 }
			 return  messageOfUnprocessedDocuments.toString();
		 }
		 else {
			 
			 return null;
		 }
		}catch(Exception ex){
			logger.error("Cancel Road Operation", ex);
		}
		return null;
		
	}
	
	@Override
	public java.util.Date getFirstOffenceDate(Integer roadOpId){
		Criteria criteria = this.hibernateTemplate.getSessionFactory().getCurrentSession().createCriteria(ComplianceDO.class, "roadOpComp");
		
		//create aliases
		criteria.createAlias("roadOpComp.roadOperation", "roadOperation");
		criteria.createAlias("roadOpComp.status", "status");
				
		criteria.add(Restrictions.eq("roadOperation.roadOperationId", roadOpId));
		criteria.add(Restrictions.eq("status.statusId", Constants.Status.ROAD_CHECK_COMPLETE));
		criteria.setProjection(Projections.max("offenceDateTime"));			
		
		return (java.util.Date)criteria.uniqueResult();

	}

	
	
	@Override
	public java.util.Date getLastOffenceDate(Integer roadOpId){		
		Criteria criteria = this.hibernateTemplate.getSessionFactory().getCurrentSession().createCriteria(ComplianceDO.class, "roadOpComp");
		
		//create aliases
		criteria.createAlias("roadOpComp.roadOperation", "roadOperation");
		criteria.createAlias("roadOpComp.status", "status");
				
		criteria.add(Restrictions.eq("roadOperation.roadOperationId", roadOpId));
		criteria.add(Restrictions.eq("status.statusId", Constants.Status.ROAD_CHECK_COMPLETE));
		criteria.setProjection(Projections.min("offenceDateTime"));			
		
		return (java.util.Date)criteria.uniqueResult();
	}
	
}
