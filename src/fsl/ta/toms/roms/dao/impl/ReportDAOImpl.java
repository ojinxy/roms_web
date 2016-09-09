/**
 * Created By: oanguin
 * Date: May 8, 2013
 *
 */
package fsl.ta.toms.roms.dao.impl;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.FlushMode;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.Type;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;

import fsl.ta.toms.roms.bo.CourtAppearanceReportResultsBO;
import fsl.ta.toms.roms.bo.CourtBO;
import fsl.ta.toms.roms.bo.CourtCasesOpenedReportBO;
import fsl.ta.toms.roms.bo.CourtCasesOpenedReportResultsBO;
import fsl.ta.toms.roms.bo.CourtScheduleReportBO;
import fsl.ta.toms.roms.bo.CourtScheduleReportResultsBO;
import fsl.ta.toms.roms.bo.EventAuditReportBO;
import fsl.ta.toms.roms.bo.EventAuditReportResultsBO;
import fsl.ta.toms.roms.bo.ITAExaminerStatisticsBO;
import fsl.ta.toms.roms.bo.JPStatisticsBO;
import fsl.ta.toms.roms.bo.PoliceOfficerStatisticsBO;
import fsl.ta.toms.roms.bo.RegionStatisticsBO;
import fsl.ta.toms.roms.bo.RoadOperationStatisticsBO;
import fsl.ta.toms.roms.bo.RoadOperationSummaryBO;
import fsl.ta.toms.roms.bo.RoadOperationSummaryResultsBO;
import fsl.ta.toms.roms.bo.RoadOperationTeamSummaryResults;
import fsl.ta.toms.roms.bo.RoadOperationsStatisticsReportBO;
import fsl.ta.toms.roms.bo.SummonsOutstandingReportBO;
import fsl.ta.toms.roms.bo.SummonsOutstandingReportResultsBO;
import fsl.ta.toms.roms.bo.SummonsReportBO;
import fsl.ta.toms.roms.bo.SummonsReportResultsBO;
import fsl.ta.toms.roms.bo.TAOfficerStatisticsBO;
import fsl.ta.toms.roms.bo.VehicleSeizedReportBO;
import fsl.ta.toms.roms.bo.VehicleSeizedReportResultsBO;
import fsl.ta.toms.roms.bo.WarningNoProsecutionDetailsBO;
import fsl.ta.toms.roms.bo.WarningNoProsecutionReportBO;
import fsl.ta.toms.roms.constants.Constants;
import fsl.ta.toms.roms.dao.ReportDAO;
import fsl.ta.toms.roms.dataobjects.ArteryDO;
import fsl.ta.toms.roms.dataobjects.AssignedPersonDO;
import fsl.ta.toms.roms.dataobjects.CDCategoryDO;
import fsl.ta.toms.roms.dataobjects.CDEventDO;
import fsl.ta.toms.roms.dataobjects.CDEventRefTypeDO;
import fsl.ta.toms.roms.dataobjects.CDPersonTypeDO;
import fsl.ta.toms.roms.dataobjects.ComplianceDO;
import fsl.ta.toms.roms.dataobjects.CourtAppearanceDO;
import fsl.ta.toms.roms.dataobjects.CourtCaseDO;
import fsl.ta.toms.roms.dataobjects.CourtDO;
import fsl.ta.toms.roms.dataobjects.DLCheckResultDO;
import fsl.ta.toms.roms.dataobjects.EventAuditDO;
import fsl.ta.toms.roms.dataobjects.ITAExaminerDO;
import fsl.ta.toms.roms.dataobjects.JPDO;
import fsl.ta.toms.roms.dataobjects.LMIS_TAOfficeLocationViewDO;
import fsl.ta.toms.roms.dataobjects.LMIS_UserViewDO;
import fsl.ta.toms.roms.dataobjects.OffenceDO;
import fsl.ta.toms.roms.dataobjects.OperationStrategyDO;
import fsl.ta.toms.roms.dataobjects.ParishDO;
import fsl.ta.toms.roms.dataobjects.PersonDO;
import fsl.ta.toms.roms.dataobjects.PoliceOfficerDO;
import fsl.ta.toms.roms.dataobjects.PoundDO;
import fsl.ta.toms.roms.dataobjects.RoadCheckDO;
import fsl.ta.toms.roms.dataobjects.RoadCheckOffenceOutcomeDO;
import fsl.ta.toms.roms.dataobjects.RoadOperationDO;
import fsl.ta.toms.roms.dataobjects.StatusDO;
import fsl.ta.toms.roms.dataobjects.StrategyDO;
import fsl.ta.toms.roms.dataobjects.SummonsDO;
import fsl.ta.toms.roms.dataobjects.TAStaffDO;
import fsl.ta.toms.roms.dataobjects.TeamDO;
import fsl.ta.toms.roms.dataobjects.VehicleCheckResultDO;
import fsl.ta.toms.roms.dataobjects.VehicleDO;
import fsl.ta.toms.roms.dataobjects.VehicleOwnerDO;
import fsl.ta.toms.roms.dataobjects.WarningNoProsecutionDO;
import fsl.ta.toms.roms.dataobjects.WarningNoticeDO;
import fsl.ta.toms.roms.dataobjects.WitnessWarningNoticeDO;
import fsl.ta.toms.roms.dataobjects.WreckingCompanyDO;
import fsl.ta.toms.roms.search.criteria.impl.CourtScheduleCriteriaBO;
import fsl.ta.toms.roms.search.criteria.impl.EventAuditReportCriteriaBO;
import fsl.ta.toms.roms.search.criteria.impl.OperationSummaryReportCriteriaBO;
import fsl.ta.toms.roms.search.criteria.impl.PerformanceStatisticsReportCriteriaBO;
import fsl.ta.toms.roms.search.criteria.impl.SummonsOutstandingReportCriteriaBO;
import fsl.ta.toms.roms.search.criteria.impl.SummonsReportCriteriaBO;
import fsl.ta.toms.roms.search.criteria.impl.VehicleSeizedReportCriteriaBO;
import fsl.ta.toms.roms.search.criteria.impl.WarningNoProReportCriteriaBO;
import fsl.ta.toms.roms.util.DateUtils;
import fsl.ta.toms.roms.util.NameUtil;
import fsl.ta.toms.roms.util.StringUtil;

/**
 * @author oanguin Created Date: May 8, 2013
 */
@Transactional
public class ReportDAOImpl extends ParentDAOImpl implements ReportDAO {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fsl.ta.toms.roms.dao.ReportDAO#vehicleSeizedReport(fsl.ta.toms.roms.search
	 * .criteria.impl.VehicleSeizedReportCriteriaBO)
	 */
	@Override
	public VehicleSeizedReportBO vehicleSeizedReport(
			VehicleSeizedReportCriteriaBO reportCriteria, String userName,
			String userRegion,
			ReportDisplayInformationDAOImpl reportDisplayInformation) {
		List<VehicleSeizedReportBO> vehicleSeizedReportBOList = new ArrayList<VehicleSeizedReportBO>();

		Criteria criteria = this.hibernateTemplate.getSessionFactory()
				.getCurrentSession()
				.createCriteria(WarningNoticeDO.class, "wn");

		/* Add Aliases */
		criteria.createAlias("roadOperation", "ro", Criteria.LEFT_JOIN);
		criteria.createAlias("taStaff", "tas", Criteria.LEFT_JOIN);
		criteria.createAlias("tas.person", "taPerson", Criteria.LEFT_JOIN);
		criteria.createAlias("offender", "of", Criteria.LEFT_JOIN);
		criteria.createAlias("pound", "p", Criteria.LEFT_JOIN);
		criteria.createAlias("wreckingCompany", "wc", Criteria.LEFT_JOIN);
		criteria.createAlias(
				"wn.roadCheckOffenceOutcome.roadCheckOffence.offence", "ofn",
				Criteria.LEFT_JOIN);
		criteria.createAlias(
				"wn.roadCheckOffenceOutcome.roadCheckOffence.roadCheck.compliance.vehicle",
				"v", Criteria.LEFT_JOIN);
		criteria.createAlias(
				"wn.roadCheckOffenceOutcome.roadCheckOffence.roadCheck.compliance.compliancyArtery",
				"art", Criteria.LEFT_JOIN);

		/* Add Filters */

		/*
		 * This is assuming that start date and end date are mandatory search
		 * fields and will not be NULL
		 */
		// criteria.add(Restrictions.between("seizureDtime",reportCriteria.getOffenceStartDate()
		// ,reportCriteria.getOffenceEndDate() ));

		Date reportStartDate = DateUtils.searchDateFormater(
				reportCriteria.getOffenceStartDate(),
				DateUtils.SEARCHDATETYPE.START);
		Date reportEndDate = DateUtils.searchDateFormater(
				reportCriteria.getOffenceEndDate(),
				DateUtils.SEARCHDATETYPE.END);

		criteria.add(Restrictions.ge("seizureDtime", reportStartDate));
		criteria.add(Restrictions.le("seizureDtime", reportEndDate));

		if (reportCriteria.getTAOfficeRegion() != null
				&& !reportCriteria.getTAOfficeRegion().isEmpty())
			criteria.add(Restrictions.eq("ro.officeLocCode", reportCriteria
					.getTAOfficeRegion().trim()));

		if (reportCriteria.getTAStaffId() != null
				&& !reportCriteria.getTAStaffId().isEmpty())
			criteria.add(Restrictions.eq("tas.staffId", reportCriteria
					.getTAStaffId().trim()));

		if (reportCriteria.getOffenderId() != null
				&& reportCriteria.getOffenderId() > 0)
			criteria.add(Restrictions.eq("of.personId",
					reportCriteria.getOffenderId()));

		if (StringUtil.isSet(reportCriteria.getOffenderFirstName())) {
			criteria.add(Restrictions.like("of.firstName",
					reportCriteria.getOffenderFirstName().trim(),
					MatchMode.ANYWHERE).ignoreCase());
		}

		if (StringUtil.isSet(reportCriteria.getOffenderLastName())) {
			criteria.add(Restrictions.like("of.lastName",
					reportCriteria.getOffenderLastName().trim(),
					MatchMode.ANYWHERE).ignoreCase());
		}

		if (StringUtil.isSet(reportCriteria.getOffenderTRN())) {
			criteria.add(Restrictions.eq("of.trnNbr", reportCriteria
					.getOffenderTRN().trim()));
		}

		if (reportCriteria.getPoundId() != null
				&& reportCriteria.getPoundId() > 0)
			criteria.add(Restrictions.eq("p.poundId",
					reportCriteria.getPoundId()));

		if (reportCriteria.getWreckingCompanyId() != null
				&& reportCriteria.getWreckingCompanyId() > 0)
			criteria.add(Restrictions.eq("wc.wreckingCompanyId",
					reportCriteria.getWreckingCompanyId()));

		if (reportCriteria.getRoadOperationId() != null
				&& reportCriteria.getRoadOperationId() > 0)
			criteria.add(Restrictions.eq("ro.roadOperationId",
					reportCriteria.getRoadOperationId()));

		if (StringUtil.isSet(reportCriteria.getRoadOperationName()))
			criteria.add(Restrictions.like("ro.operationName",
					reportCriteria.getRoadOperationName().trim(),
					MatchMode.ANYWHERE).ignoreCase());

		if (StringUtil.isSet(reportCriteria.getTAStaffTRN())) {
			criteria.add(Restrictions.eq("taPerson.trnNbr", reportCriteria
					.getTAStaffTRN().trim()));
		}

		criteria.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);

		List warningNotices = criteria.list();

		Iterator iterator = warningNotices.iterator();

		/* Get report criteria names and descriptions */
		reportCriteria.setTAOfficeDescription(this
				.getTAOfficeRegionDescription(reportCriteria
						.getTAOfficeRegion()));

		if (StringUtil.isSet(reportCriteria.getTAStaffId()))
			reportCriteria.setTAStaffName(this.getTAStaffName(reportCriteria
					.getTAStaffId()));
		else if (StringUtil.isSet(reportCriteria.getTAStaffTRN()))
			reportCriteria.setTAStaffName(this.getPersonName(reportCriteria
					.getTAStaffTRN()));

		if (reportCriteria.getOffenderId() != null
				&& reportCriteria.getOffenderId() > 0)
			reportCriteria.setOffenderName(this.getPersonName(reportCriteria
					.getOffenderId()));
		else if (StringUtil.isSet(reportCriteria.getOffenderTRN()))
			reportCriteria.setOffenderName(this.getPersonName(reportCriteria
					.getOffenderTRN()));

		reportCriteria.setPoundName(this.getPoundName(reportCriteria
				.getPoundId()));
		reportCriteria.setWreckingCompanyName(this
				.getWreckingCompanyName(reportCriteria.getWreckingCompanyId()));

		/*
		 * reportCriteria.setRoadOperationDesc(this.getRoadOperationName(
		 * reportCriteria.getRoadOperationId()));
		 */
		/* __________________________________________ */

		String stringStartDate = "";
		String stringEndDate = "";
		try {
			stringStartDate = DateUtils.getFormattedUtilDate(reportCriteria
					.getOffenceStartDate());
			stringEndDate = DateUtils.getFormattedUtilDate(reportCriteria
					.getOffenceEndDate());
		} catch (Exception exe) {

		}

		List<VehicleSeizedReportResultsBO> results = new ArrayList<VehicleSeizedReportResultsBO>();

		while (iterator.hasNext()) {
			Map map = (Map) iterator.next();

			WarningNoticeDO warningNotice = (WarningNoticeDO) map.get("wn");
			RoadOperationDO roadOperation = (RoadOperationDO) map.get("ro");
			TAStaffDO taStaff = (TAStaffDO) map.get("tas");
			PersonDO offender = (PersonDO) map.get("of");
			PoundDO pound = (PoundDO) map.get("p");
			WreckingCompanyDO wreckingCompany = (WreckingCompanyDO) map
					.get("wc");
			OffenceDO offence = (OffenceDO) map.get("ofn");
			VehicleDO vehicle = (VehicleDO) map.get("v");
			ArteryDO artery = (ArteryDO) map.get("art");

			/* Get List of Witnesses */
			Criteria criteriaWitness = this.hibernateTemplate
					.getSessionFactory().getCurrentSession()
					.createCriteria(WitnessWarningNoticeDO.class, "wwn");
			criteriaWitness.add(Restrictions.eq(
					"wwn.pk.warningNotice.warningNoticeId",
					warningNotice.getWarningNoticeId()));
			List<String> witnessNames = new ArrayList<String>();

			for (WitnessWarningNoticeDO wwnDO : (List<WitnessWarningNoticeDO>) criteriaWitness
					.list()) {
				witnessNames.add(NameUtil.getName(wwnDO.getPk().getWitness()
						.getFirstName(), wwnDO.getPk().getWitness()
						.getLastName()));
				// witnessNames.add(wwnDO.getPk().getWitness())
			}
			/* _______________________________________ */

			VehicleSeizedReportResultsBO result = new VehicleSeizedReportResultsBO(
					NameUtil.getName(offender.getFirstName(),
							offender.getLastName()),
					offence.getDescription(),
					offence.getShortDescription(),
					NameUtil.getName(taStaff.getPerson().getFirstName(),
							taStaff.getPerson().getLastName()),
					pound.getPoundName(),
					wreckingCompany.getCompanyName(),
					(StringUtil.isSet(vehicle.getModel()) ? vehicle.getModel()
							.trim() : "")
							+ "; "
							+ (StringUtil.isSet(vehicle.getMakeDescription()) ? vehicle
									.getMakeDescription().trim() : "")
							+ "; Plate #: "
							+ (StringUtil.isSet(vehicle.getPlateRegNo()) ? vehicle
									.getPlateRegNo().trim() : ""),
					artery.getShortDescription(),
					roadOperation.getOfficeLocCode(),
					roadOperation.getOperationName(), witnessNames,
					warningNotice.getSeizureDtime(),
					this.getTAOfficeRegionDescription(roadOperation
							.getOfficeLocCode()));

			results.add(result);

		}

		return new VehicleSeizedReportBO(userName, userRegion,
				reportDisplayInformation.applicationName,/*
														 * The name of the
														 * application from bean
														 * config
														 */
				reportDisplayInformation.vehicleSeizedReportTitle
						+ stringStartDate + " TO " + stringEndDate,/*
																	 * The name
																	 * of the
																	 * report is
																	 * from
																	 * wired
																	 * bean
																	 */
				reportStartDate, reportEndDate,
				reportCriteria.getSearchCriteriaString(), results,
				this.getTAOfficeRegionDescription(userRegion));
	}

	@Override
	public Serializable save(Object entity) throws DataAccessException {
		throw new UnsupportedOperationException("not supported");
	}

	@Override
	public void update(Object entity) throws DataAccessException {
		throw new UnsupportedOperationException("not supported");
	}

	@Override
	public void delete(Object entity) throws DataAccessException {
		throw new UnsupportedOperationException("not supported");
	}

	@Override
	public <T> List<T> findAll(Class<T> clazz) throws DataAccessException {
		throw new UnsupportedOperationException("not supported");
	}

	@Override
	public <T> T find(Class<T> clazz, Serializable id)
			throws DataAccessException {
		throw new UnsupportedOperationException("not supported");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fsl.ta.toms.roms.dao.ReportDAO#List<RoadOperationSummaryBO>
	 * operationSummaryReport(OperationSummaryReportCriteriaBO reportCriteria,
	 * String userName,ReportDisplayInformationDAOImpl reportDisplayInformation)
	 */
	@Override
	public RoadOperationSummaryBO operationSummaryReport(
			OperationSummaryReportCriteriaBO reportCriteria, String userName,
			String userRegion,
			ReportDisplayInformationDAOImpl reportDisplayInformation) {
		Criteria criteriaRoadOp = this.hibernateTemplate.getSessionFactory()
				.getCurrentSession()
				.createCriteria(RoadOperationDO.class, "ro");
		
		criteriaRoadOp.createAlias("ro.category", "cat");

		Criterion subCriteron = null;

		Criterion mainCriteron = null;

		if (reportCriteria.getOperationStartDate() != null
				&& reportCriteria.getOperationEndDate() != null) {
			mainCriteron = Restrictions.or(Restrictions.between(
					"ro.scheduledStartDtime", DateUtils.searchDateFormater(
							reportCriteria.getOperationStartDate(),
							DateUtils.SEARCHDATETYPE.START), DateUtils
							.searchDateFormater(
									reportCriteria.getOperationEndDate(),
									DateUtils.SEARCHDATETYPE.END)),
					Restrictions.between("ro.scheduledEndDtime", DateUtils
							.searchDateFormater(
									reportCriteria.getOperationStartDate(),
									DateUtils.SEARCHDATETYPE.START), DateUtils
							.searchDateFormater(
									reportCriteria.getOperationEndDate(),
									DateUtils.SEARCHDATETYPE.END)));
		}

		if (reportCriteria.getTAOfficeRegions() != null
				&& reportCriteria.getTAOfficeRegions().size() > 0) {

			subCriteron = Restrictions.in("ro.officeLocCode",
					reportCriteria.getTAOfficeRegions());

		}

		if (reportCriteria.getTeamLeadStaffIds() != null
				&& reportCriteria.getTeamLeadStaffIds().size() > 0)

		{

			/* Get List of all road operations with staff as team lead. */
			Criteria teamLeadsByStaffIdCrit = this.getSession().createCriteria(
					TeamDO.class, "team");
			teamLeadsByStaffIdCrit.add(Restrictions.in("team.teamLead.staffId",
					reportCriteria.getTeamLeadStaffIds()));
			teamLeadsByStaffIdCrit.setProjection(Projections
					.property("team.roadOperation.roadOperationId"));

			List<Integer> teamLeadsByStaffId = teamLeadsByStaffIdCrit.list();

			if (teamLeadsByStaffId != null && teamLeadsByStaffId.size() > 0) {
				if (subCriteron == null) {
					subCriteron = Restrictions.in("ro.roadOperationId",
							teamLeadsByStaffId);
				} else {
					subCriteron = Restrictions.and(subCriteron, Restrictions
							.in("ro.roadOperationId", teamLeadsByStaffId));
				}
			}
		}

		if (reportCriteria.getTeamLeadTRNs() != null
				&& reportCriteria.getTeamLeadTRNs().size() > 0) {
			/* Get List of all road operations with staff as team lead. */
			Criteria teamLeadsByTRNCrit = this.getSession().createCriteria(
					TeamDO.class, "team");
			teamLeadsByTRNCrit.createAlias("team.teamLead", "teamLead");
			teamLeadsByTRNCrit.createAlias("teamLead.person", "person");
			teamLeadsByTRNCrit.add(Restrictions.in("person.trnNbr",
					reportCriteria.getTeamLeadTRNs()));
			teamLeadsByTRNCrit.setProjection(Projections
					.property("team.roadOperation.roadOperationId"));

			List<Integer> teamLeadsByTRN = teamLeadsByTRNCrit.list();

			if (teamLeadsByTRN != null && teamLeadsByTRN.size() > 0) {
				if (subCriteron == null) {
					subCriteron = Restrictions.in("ro.roadOperationId",
							teamLeadsByTRNCrit.list());
				} else {
					subCriteron = Restrictions.and(subCriteron,
							Restrictions.in("ro.roadOperationId",
									teamLeadsByTRNCrit.list()));
				}
			}
		}

		if (reportCriteria.getOperationCategory() != null
				&& !reportCriteria.getOperationCategory().isEmpty()) {
			if (subCriteron == null) {
				subCriteron = Restrictions.eq("ro.category.categoryId",
						reportCriteria.getOperationCategory().trim())
						.ignoreCase();
			} else {
				subCriteron = Restrictions.and(
						subCriteron,
						Restrictions.eq("ro.category.categoryId",
								reportCriteria.getOperationCategory().trim())
								.ignoreCase());
			}
		}

		if (StringUtil.isSet(reportCriteria.getRoadOperationName())) {
			if (subCriteron == null) {
				subCriteron = Restrictions.like("ro.operationName",
						reportCriteria.getRoadOperationName().trim(),
						MatchMode.ANYWHERE).ignoreCase();
			} else {
				subCriteron = Restrictions.and(
						subCriteron,
						Restrictions.like("ro.operationName",
								reportCriteria.getRoadOperationName().trim(),
								MatchMode.ANYWHERE).ignoreCase());
			}
		}

		if (reportCriteria.getRoadOperationId() != null
				&& reportCriteria.getRoadOperationId() > 0) {
			if (subCriteron == null) {
				subCriteron = Restrictions.eq("ro.roadOperationId",
						reportCriteria.getRoadOperationId());
			} else {
				subCriteron = Restrictions.and(
						subCriteron,
						Restrictions.eq("ro.roadOperationId",
								reportCriteria.getRoadOperationId()));
			}
		}

		if (reportCriteria.getRoadOperationIds() != null
				&& !reportCriteria.getRoadOperationIds().isEmpty()) {
			if (subCriteron == null) {
				subCriteron = Restrictions.in("ro.roadOperationId",
						reportCriteria.getRoadOperationIds());
			} else {
				subCriteron = Restrictions.and(
						subCriteron,
						Restrictions.in("ro.roadOperationId",
								reportCriteria.getRoadOperationIds()));
			}
		}

		if (subCriteron == null && mainCriteron != null)
			criteriaRoadOp.add(mainCriteron);
		else if (mainCriteron == null && subCriteron != null)
			criteriaRoadOp.add(subCriteron);
		else if (mainCriteron != null && subCriteron != null)
			criteriaRoadOp.add(Restrictions.and(mainCriteron, subCriteron));
		else
			return null;

		/* Get report criteria names and descriptions */
		reportCriteria.setTAOfficeDescription(this
				.getTAOfficeRegionDescription(userRegion));

		if (reportCriteria.getTeamLeadStaffIds() != null
				&& reportCriteria.getTeamLeadStaffIds().size() > 0)
			reportCriteria.setTAStaffFullName(this
					.getTAStaffNames(reportCriteria.getTeamLeadStaffIds()));
		else if (reportCriteria.getTeamLeadTRNs() != null
				&& reportCriteria.getTeamLeadTRNs().size() > 0)
			reportCriteria.setTAStaffFullName(this
					.getPersonNamesWithTRN(reportCriteria.getTeamLeadTRNs()));

		reportCriteria
				.setOperationCategoryDescription(this
						.getOperationCategoryDesc(reportCriteria
								.getOperationCategory()));
		
		

		criteriaRoadOp.addOrder(Order.asc("ro.officeLocCode"));
		criteriaRoadOp.addOrder(Order.asc("cat.description"));
		criteriaRoadOp.addOrder(Order.asc("ro.actualStartDtime"));
		criteriaRoadOp.addOrder(Order.asc("ro.actualEndDtime"));
		criteriaRoadOp.addOrder(Order.asc("ro.operationName"));
		
		List<RoadOperationSummaryResultsBO> results = new ArrayList<RoadOperationSummaryResultsBO>();

		String stringStartDate = "";
		String stringEndDate = "";
		try {
			stringStartDate = DateUtils.getFormattedUtilDate(reportCriteria
					.getOperationStartDate());
			stringEndDate = DateUtils.getFormattedUtilDate(reportCriteria
					.getOperationEndDate());
		} catch (Exception exe) {

		}

		for (RoadOperationDO roadOp : (List<RoadOperationDO>) criteriaRoadOp
				.list()) {
			// Get counts that are needed to fill roadOpSummaryReportList
			Integer absentMembersCount = this.absentPersonCount(roadOp
					.getRoadOperationId());

			Integer teamMemberCount = this.teamMemberPersonCount(roadOp
					.getRoadOperationId());

			Integer summonsCount = this.summonsCount(roadOp
					.getRoadOperationId());

			Integer warningNoProsecutionCount = this
					.warningNoProsecutionCount(roadOp.getRoadOperationId());

			Integer warningNoticeCount = this.warningNoticeCount(roadOp
					.getRoadOperationId());

			Integer vehicleSeizedCount = this.roadCheckOutcomeCount(
					roadOp.getRoadOperationId(),
					Constants.OutcomeType.VEHICLE_SEIZURE);

			Integer complianceCount = this.complianceCount(roadOp
					.getRoadOperationId());

			Integer motorVehicleCheckCount = this.roadCheckTypeCount(
					roadOp.getRoadOperationId(),
					Constants.RoadCheckType.MOTOR_VEHICLE);

			Integer dlCheckCount = this.roadCheckTypeCount(
					roadOp.getRoadOperationId(),
					Constants.RoadCheckType.DRIVERS_LICENCE);

			Integer badgeCheckCount = this.roadCheckTypeCount(
					roadOp.getRoadOperationId(), Constants.RoadCheckType.BADGE);

			Integer citationCheckCount = this.roadCheckTypeCount(
					roadOp.getRoadOperationId(),
					Constants.RoadCheckType.CITATION);

			Integer roadLicenceCheckCount = this.roadCheckTypeCount(
					roadOp.getRoadOperationId(),
					Constants.RoadCheckType.ROAD_LICENCE);

			Integer otherCheckCount = this.roadCheckTypeCount(
					roadOp.getRoadOperationId(), Constants.RoadCheckType.OTHER);

			Integer countJPs = this.getCountOfPersonType(
					roadOp.getRoadOperationId(), Constants.PersonType.JP);

			Integer countPoliceOfficers = this.getCountOfPersonType(
					roadOp.getRoadOperationId(),
					Constants.PersonType.POLICE_OFFCER);

			Integer countTAInspectors = this.getCountOfPersonType(
					roadOp.getRoadOperationId(), Constants.PersonType.TA_STAFF);

			Integer countITAExaminers = this.getCountOfPersonType(
					roadOp.getRoadOperationId(),
					Constants.PersonType.ITA_EXAMINER);

			/*****
			 * UR-057 Got absent count for each person type
			 */
			
			Integer absentITACount = absentPersonTypeCount(roadOp.getRoadOperationId(), Constants.PersonType.ITA_EXAMINER);
			Integer absentTACount = absentPersonTypeCount(roadOp.getRoadOperationId(), Constants.PersonType.TA_STAFF);
			Integer absentPoliceCount = absentPersonTypeCount(roadOp.getRoadOperationId(), Constants.PersonType.POLICE_OFFCER);
			Integer absentJPCount = absentPersonTypeCount(roadOp.getRoadOperationId(), Constants.PersonType.JP);
			
			/* __________________________________________________________________________ */

			/*
			 * Get Duration of an road operation using actual start and end
			 * time.
			 */

			long operationDuration = 0;

			if (roadOp.getActualStartDtime() != null
					&& roadOp.getActualEndDtime() != null)
				operationDuration = DateUtils.getDateDiff(
						roadOp.getActualStartDtime(),
						roadOp.getActualEndDtime(), TimeUnit.MINUTES);

			/* ___________________________________________________________________ */

			RoadOperationSummaryResultsBO result = new RoadOperationSummaryResultsBO(
					roadOp.getAuditEntry().getCreateDTime()/* operationCreateDate */,
					roadOp.getScheduledEndDtime()/* scheduledEndDateTime */,
					roadOp.getScheduledStartDtime()/* scheduledStartDateTime */,
					roadOp.getActualStartDtime()/* actualStartDateTime */,
					roadOp.getActualEndDtime()/* actualEndDateTime */,
					roadOp.getStatus().getDescription()/* operationStatus */,
					roadOp.getCategory().getDescription()/* operationCategory */,
					roadOp.getOperationName()/* operationName */,
					summonsCount/* countSummonsIssued */,
					warningNoticeCount/* countWaningNoticesIssued */,
					vehicleSeizedCount/* countVehiclesSeized */,
					complianceCount/* countCompliancyActivitiesCommited */,
					motorVehicleCheckCount/* countMotorVehiclesChecked */,
					dlCheckCount/* countDrivesLicenceChecked */,
					badgeCheckCount/* countBadgesChecked */,
					citationCheckCount/* countCitationChecks */,
					roadLicenceCheckCount/* countRoadLicencesChecked */,
					otherCheckCount/* countOtherChecks */,
					(int) operationDuration/* durationOfOperationInMinutes */,
					absentMembersCount/* countAbsentMembers */,
					roadOp.getRoadOperationId()/* operationId */,
					this.roadCheckOutcomeCount(roadOp.getRoadOperationId(),
							Constants.OutcomeType.REMOVE_PLATES)/* countPlatesRemoved */,
					this.roadCheckOutcomeCount(roadOp.getRoadOperationId(),
							Constants.OutcomeType.WARNED_FOR_PROSECUTION)/* warningsForProcecution */,
					this.roadCheckOutcomeCount(roadOp.getRoadOperationId(),
							Constants.OutcomeType.ALL_IN_ORDER)/*
																 * Integer
																 * allInOrders
																 */,
					teamMemberCount /* count team members */, this
							.getRoadOpTeamSummaryResults(roadOp
									.getRoadOperationId()) /* team Summaries */,
					roadOp.getOfficeLocCode()/*TAOfficeRegion*/,
					this.getTAOfficeRegionDescription(roadOp.getOfficeLocCode())/*TAOfficeRegion*/);

			result.setCountWarningNoProsecutions(warningNoProsecutionCount);
			result.setCountITAExaminers(countITAExaminers);
			result.setCountPoliceOfficers(countPoliceOfficers);
			result.setCountJPs(countJPs);
			result.setCountTAInspectors(countTAInspectors);
			
			/*****
			 * UR-057 - Set absentee values for each assigned person group
			 */
			result.setCountAbsentITAExaminers(absentITACount);
			result.setCountAbsentJPs(absentJPCount);
			result.setCountAbsentPoliceOfficers(absentPoliceCount);
			result.setCountAbsentTAInspectors(absentTACount);
			 

			results.add(result);
		}

		return new RoadOperationSummaryBO(userName, userRegion,
				reportDisplayInformation.applicationName,
				reportDisplayInformation.roadOperationSummaryTitle
						+ stringStartDate + " TO " + stringEndDate,
				reportCriteria.getOperationStartDate(),
				reportCriteria.getOperationEndDate(),
				reportCriteria.getSearchCriteriaString(),
				this.getTAOfficeRegionDescription(userRegion), results);
	}

	/**
	 * This function returns a list of team performance for a road operation.
	 * 
	 * @param roadOpId
	 * @return
	 */
	private List<RoadOperationTeamSummaryResults> getRoadOpTeamSummaryResults(
			Integer roadOpId) {
		//System.out.println("In team summary function.");
		List<RoadOperationTeamSummaryResults> roadOpTeamResults = new ArrayList<RoadOperationTeamSummaryResults>();

		Criteria criteriaTeams = this.hibernateTemplate.getSessionFactory()
				.getCurrentSession().createCriteria(TeamDO.class, "team");

		criteriaTeams.add(Restrictions.eq("team.roadOperation.roadOperationId",
				roadOpId));

		criteriaTeams.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);

		Iterator criteriaTeamsIterator = criteriaTeams.list().iterator();

		while (criteriaTeamsIterator.hasNext()) {
			Map resultMap = (Map) criteriaTeamsIterator.next();

			TeamDO team = (TeamDO) resultMap.get("team");

			RoadOperationTeamSummaryResults roadOpTeamResult = new RoadOperationTeamSummaryResults(
					team.getTeamName()/* teamName */,
					NameUtil.getName(team.getTeamLead().getPerson()
							.getFirstName(), team.getTeamLead().getPerson()
							.getLastName())/* teamLeadName */,
					this.summonsTeamCount(roadOpId, team.getTeamId())/* countSummonsIssued */,
					this.warningNoticeTeamCount(roadOpId, team.getTeamId())/* countWaningNoticesIssued */,
					this.roadCheckOutcomeTeamCount(roadOpId,
							Constants.OutcomeType.WARNED_FOR_PROSECUTION,
							team.getTeamId())/* countVehiclesSeized */,
					this.complianceTeamCount(roadOpId, team.getTeamId())/* countCompliancyActivitiesCommited */,
					this.roadCheckTypeTeamCount(roadOpId,
							Constants.RoadCheckType.MOTOR_VEHICLE,
							team.getTeamId())/* countMotorVehiclesChecked */,
					this.roadCheckTypeTeamCount(roadOpId,
							Constants.RoadCheckType.DRIVERS_LICENCE,
							team.getTeamId())/* countDrivesLicenceChecked */,
					this.roadCheckTypeTeamCount(roadOpId,
							Constants.RoadCheckType.BADGE, team.getTeamId())/* countBadgesChecked */,
					this.roadCheckTypeTeamCount(roadOpId,
							Constants.RoadCheckType.CITATION, team.getTeamId())/* countCitationChecks */,
					this.roadCheckTypeTeamCount(roadOpId,
							Constants.RoadCheckType.ROAD_LICENCE,
							team.getTeamId())/* countRoadLicencesChecked */,
					this.roadCheckTypeTeamCount(roadOpId,
							Constants.RoadCheckType.OTHER, team.getTeamId())/* countOtherChecks */,
					this.absentPersonTeamCount(team.getTeamId())/* countAbsentMembers */,
					this.teamMemberPersonTeamCount(team.getTeamId())/* countTeamMembers */,
					roadOpId/* road operation id */, this
							.roadCheckOutcomeTeamCount(roadOpId,
									Constants.OutcomeType.REMOVE_PLATES,
									team.getTeamId())/* countPlatesRemoved */,
					this.roadCheckOutcomeTeamCount(roadOpId,
							Constants.OutcomeType.WARNED_FOR_PROSECUTION,
							team.getTeamId())/* warningsForProcecution */, this
							.roadCheckOutcomeTeamCount(roadOpId,
									Constants.OutcomeType.ALL_IN_ORDER,
									team.getTeamId())/* allInOrders */, team
							.getTeamId()/* teamId */);

			roadOpTeamResults.add(roadOpTeamResult);
		}

		return roadOpTeamResults;
	}

	/**
	 * This function is a convenience method which returns all the team ids for
	 * an road operation
	 * 
	 * @param roadOpId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<Integer> getTeamIdsForRoadOp(Integer roadOpId) {
		Criteria criteria = this.hibernateTemplate.getSessionFactory()
				.getCurrentSession().createCriteria(TeamDO.class, "team");

		criteria.add(Restrictions.eq("team.roadOperation.roadOperationId",
				roadOpId));

		criteria.setProjection(Projections.property("team.teamId"));

		return criteria.list();
	}

	private Integer absentPersonCount(Integer roadOperationId) {
		/* Get Count of Absent Persons */
		Criteria criteriaAssignedPersons = this.hibernateTemplate
				.getSessionFactory().getCurrentSession()
				.createCriteria(AssignedPersonDO.class, "a");

		List<Integer> teamIds = getTeamIdsForRoadOp(roadOperationId);

		if (teamIds != null && teamIds.size() > 0)
			criteriaAssignedPersons.add(Restrictions.in(
					"a.assignedPersonKey.team.teamId", teamIds));

		criteriaAssignedPersons.add(Restrictions.eq("a.attended", "n")
				.ignoreCase());
		criteriaAssignedPersons.setProjection(Projections.count("a.attended"));
		criteriaAssignedPersons.setFetchMode("a", FetchMode.LAZY);
		Iterator iterator = criteriaAssignedPersons.list().iterator();

		Integer AbsentMembersCount = (Integer) iterator.next();

		criteriaAssignedPersons.setFlushMode(FlushMode.ALWAYS);
		return AbsentMembersCount;
	}

	private Integer absentPersonTeamCount(Integer teamId) {
		/* Get Count of Absent Persons */
		Criteria criteriaAssignedPersons = this.hibernateTemplate
				.getSessionFactory().getCurrentSession()
				.createCriteria(AssignedPersonDO.class, "a");
		criteriaAssignedPersons.add(Restrictions.eq(
				"a.assignedPersonKey.team.teamId", teamId));
		criteriaAssignedPersons.add(Restrictions.eq("a.attended", "n")
				.ignoreCase());
		criteriaAssignedPersons.setProjection(Projections.count("a.attended"));
		criteriaAssignedPersons.setFetchMode("a", FetchMode.LAZY);
		Iterator iterator = criteriaAssignedPersons.list().iterator();

		Integer AbsentMembersCount = (Integer) iterator.next();

		criteriaAssignedPersons.setFlushMode(FlushMode.ALWAYS);
		return AbsentMembersCount;
	}

	private Integer teamMemberPersonCount(Integer roadOperationId) {
		/* Get Count of Absent Persons */
		Criteria criteriaAssignedPersons = this.hibernateTemplate
				.getSessionFactory().getCurrentSession()
				.createCriteria(AssignedPersonDO.class, "a");

		List<Integer> teamIds = this.getTeamIdsForRoadOp(roadOperationId);

		if (teamIds != null && teamIds.size() > 0)
			criteriaAssignedPersons.add(Restrictions.in(
					"a.assignedPersonKey.team.teamId", teamIds));

		criteriaAssignedPersons.setProjection(Projections.rowCount());
		criteriaAssignedPersons.setFetchMode("a", FetchMode.LAZY);
		Iterator iterator = criteriaAssignedPersons.list().iterator();

		Integer AbsentMembersCount = (Integer) iterator.next();

		criteriaAssignedPersons.setFlushMode(FlushMode.ALWAYS);
		return AbsentMembersCount;
	}

	private Integer teamMemberPersonTeamCount(Integer teamId) {
		/* Get Count of Absent Persons */
		Criteria criteriaAssignedPersons = this.hibernateTemplate
				.getSessionFactory().getCurrentSession()
				.createCriteria(AssignedPersonDO.class, "a");
		criteriaAssignedPersons.add(Restrictions.eq(
				"a.assignedPersonKey.team.teamId", teamId));

		criteriaAssignedPersons.setProjection(Projections.rowCount());
		criteriaAssignedPersons.setFetchMode("a", FetchMode.LAZY);
		Iterator iterator = criteriaAssignedPersons.list().iterator();

		Integer AbsentMembersCount = (Integer) iterator.next();

		criteriaAssignedPersons.setFlushMode(FlushMode.ALWAYS);
		return AbsentMembersCount;
	}

	private Integer summonsCount(Integer roadOperationId) {
		/* Get Count of Summons For Road Operation */
		Criteria criteriaSummons = this.hibernateTemplate.getSessionFactory()
				.getCurrentSession().createCriteria(SummonsDO.class, "s");

		criteriaSummons.createAlias("s.roadCheckOffenceOutcome", "rout",
				Criteria.LEFT_JOIN);
		criteriaSummons.createAlias("rout.roadCheckOffence", "roff",
				Criteria.LEFT_JOIN);
		criteriaSummons.createAlias("roff.roadCheck", "rchk",
				Criteria.LEFT_JOIN);
		criteriaSummons.createAlias("rchk.compliance", "comp",
				Criteria.LEFT_JOIN);
		criteriaSummons.createAlias("comp.roadOperation", "rop",
				Criteria.LEFT_JOIN);

		criteriaSummons.add(Restrictions.eq("rop.roadOperationId",
				roadOperationId));

		criteriaSummons.setProjection(Projections.rowCount());

		// criteriaSummons.setFetchMode("s", FetchMode.LAZY);
		// criteriaSummons.setFlushMode(FlushMode.ALWAYS);

		Iterator iterator = criteriaSummons.list().iterator();

		Integer summonsCount = (Integer) iterator.next();

		return summonsCount;
	}

	private List<Integer> getStaffPersonIdsForTeam(Integer teamId) {
		if (teamId != null && teamId > 0) {
			Criteria criteriaAssignedPersons = this.hibernateTemplate
					.getSessionFactory().getCurrentSession()
					.createCriteria(AssignedPersonDO.class, "a");
			criteriaAssignedPersons.add(Restrictions.eq(
					"a.assignedPersonKey.team.teamId", teamId));

			criteriaAssignedPersons.add(Restrictions.eq(
					"a.assignedPersonKey.personType.personTypeId",
					Constants.PersonType.TA_STAFF));

			criteriaAssignedPersons.setProjection(Projections
					.property("a.assignedPersonKey.person.personId"));

			return criteriaAssignedPersons.list();
		} else
			return null;
	}

	/**
	 * This function is a convince function to get staff Ids for team. It is
	 * dependent on <b>private List<Integer> getStaffPersonIdsForTeam(Integer
	 * teamId)</b>
	 * 
	 * @param teamId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<String> getStaffIdForTeam(Integer teamId) {
		if (teamId != null && teamId > 0) {
			List<Integer> teamPersonIds = this.getStaffPersonIdsForTeam(teamId);

			if (teamPersonIds != null && teamPersonIds.size() > 0) {

				Criteria criteria = this.hibernateTemplate.getSessionFactory()
						.getCurrentSession()
						.createCriteria(TAStaffDO.class, "staff");

				criteria.add(Restrictions.in("staff.person.personId",
						teamPersonIds));

				criteria.setProjection(Projections.property("staff.staffId"));

				return criteria.list();
			} else
				return null;
		} else
			return null;
	}

	/**
	 * This function returns a list of team lead ids based on the road operation
	 * id.
	 * 
	 * @param roadOp
	 * @return
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	private List<String> getTeamLeadIdsForRoadOp(Integer roadOpId) {
		Criteria criteria = this.hibernateTemplate.getSessionFactory()
				.getCurrentSession().createCriteria(TeamDO.class, "team");

		criteria.add(Restrictions.eq("team.roadOperation.roadOperationId",
				roadOpId));

		criteria.setProjection(Projections.property("team.teamLead.staffId"));

		return criteria.list();
	}

	private Integer summonsTeamCount(Integer roadOperationId, Integer teamId) {
		/* Get Count of Summons For Road Operation */
		Criteria criteriaSummons = this.hibernateTemplate.getSessionFactory()
				.getCurrentSession().createCriteria(SummonsDO.class, "s");

		criteriaSummons.createAlias("s.roadCheckOffenceOutcome", "rout",
				Criteria.LEFT_JOIN);
		criteriaSummons.createAlias("rout.roadCheckOffence", "roff",
				Criteria.LEFT_JOIN);
		criteriaSummons.createAlias("roff.roadCheck", "rchk",
				Criteria.LEFT_JOIN);
		criteriaSummons.createAlias("rchk.compliance", "comp",
				Criteria.LEFT_JOIN);
		criteriaSummons.createAlias("comp.roadOperation", "rop",
				Criteria.LEFT_JOIN);

		criteriaSummons.add(Restrictions.eq("rop.roadOperationId",
				roadOperationId));

		/* Get list of ta staff ids which are on a team. */
		Criteria criteria = this.hibernateTemplate.getSessionFactory()
				.getCurrentSession().createCriteria(SummonsDO.class, "team");

		if (this.getStaffIdForTeam(teamId) != null) {
			criteriaSummons.add(Restrictions.in("s.taStaff.staffId",
					this.getStaffIdForTeam(teamId)));

			criteriaSummons.setProjection(Projections.rowCount());

			criteriaSummons.setFetchMode("s", FetchMode.LAZY);
			criteriaSummons.setFlushMode(FlushMode.ALWAYS);

			Iterator iterator = criteriaSummons.list().iterator();

			Integer summonsCount = (Integer) iterator.next();

			return summonsCount;
		} else
			return 0;
	}

	private Integer summonsCount(Integer roadOperationId, Integer personId,
			String personType) {
		/* Get Count of Summons For Road Operation */
		Criteria criteriaSummons = this.hibernateTemplate.getSessionFactory()
				.getCurrentSession().createCriteria(SummonsDO.class, "s");

		criteriaSummons.createAlias("s.roadCheckOffenceOutcome", "rout",
				Criteria.LEFT_JOIN);
		criteriaSummons.createAlias("rout.roadCheckOffence", "roff",
				Criteria.LEFT_JOIN);
		criteriaSummons.createAlias("roff.roadCheck", "rchk",
				Criteria.LEFT_JOIN);
		criteriaSummons.createAlias("rchk.compliance", "comp",
				Criteria.LEFT_JOIN);
		criteriaSummons.createAlias("comp.roadOperation", "rop",
				Criteria.LEFT_JOIN);

		criteriaSummons.add(Restrictions.eq("rop.roadOperationId",
				roadOperationId));

		if (personType.equalsIgnoreCase(Constants.PersonType.JP)) {
			criteriaSummons.createAlias("s.justiceOfPeace", "jp");
			criteriaSummons.createAlias("jp.person", "person");
		} else if (personType.equalsIgnoreCase(Constants.PersonType.TA_STAFF)) {
			criteriaSummons.createAlias("s.taStaff", "ta");
			criteriaSummons.createAlias("ta.person", "person");
		} else {
			return -1;
		}

		criteriaSummons.add(Restrictions.eq("person.personId", personId));

		criteriaSummons.setProjection(Projections.rowCount());

		Iterator iterator = criteriaSummons.list().iterator();

		Integer summonsCount = (Integer) iterator.next();

		return summonsCount;
	}

	private Integer warningNoticeCount(Integer roadOperationId) {
		/* Get Count of Summons For Road Operation */
		Criteria criteriaWarningNotice = this.hibernateTemplate
				.getSessionFactory().getCurrentSession()
				.createCriteria(WarningNoticeDO.class, "w");

		criteriaWarningNotice.createAlias("w.roadCheckOffenceOutcome", "rout",
				Criteria.LEFT_JOIN);
		criteriaWarningNotice.createAlias("rout.roadCheckOffence", "roff",
				Criteria.LEFT_JOIN);
		criteriaWarningNotice.createAlias("roff.roadCheck", "rchk",
				Criteria.LEFT_JOIN);
		criteriaWarningNotice.createAlias("rchk.compliance", "comp",
				Criteria.LEFT_JOIN);
		criteriaWarningNotice.createAlias("comp.roadOperation", "rop",
				Criteria.LEFT_JOIN);

		criteriaWarningNotice.add(Restrictions.eq("rop.roadOperationId",
				roadOperationId));

		criteriaWarningNotice.setProjection(Projections.rowCount());

		criteriaWarningNotice.setFetchMode("w", FetchMode.LAZY);
		criteriaWarningNotice.setFlushMode(FlushMode.ALWAYS);
		Iterator iterator = criteriaWarningNotice.list().iterator();

		Integer warningNoticeCount = (Integer) iterator.next();

		return warningNoticeCount;
	}

	private Integer warningNoticeTeamCount(Integer roadOperationId,
			Integer teamId) {
		/* Get Count of Summons For Road Operation */
		Criteria criteriaWarningNotice = this.hibernateTemplate
				.getSessionFactory().getCurrentSession()
				.createCriteria(WarningNoticeDO.class, "w");

		criteriaWarningNotice.createAlias("w.roadCheckOffenceOutcome", "rout",
				Criteria.LEFT_JOIN);
		criteriaWarningNotice.createAlias("rout.roadCheckOffence", "roff",
				Criteria.LEFT_JOIN);
		criteriaWarningNotice.createAlias("roff.roadCheck", "rchk",
				Criteria.LEFT_JOIN);
		criteriaWarningNotice.createAlias("rchk.compliance", "comp",
				Criteria.LEFT_JOIN);
		criteriaWarningNotice.createAlias("comp.roadOperation", "rop",
				Criteria.LEFT_JOIN);

		criteriaWarningNotice.add(Restrictions.eq("rop.roadOperationId",
				roadOperationId));

		if (this.getStaffIdForTeam(teamId) != null) {
			criteriaWarningNotice.add(Restrictions.in("w.taStaff.staffId",
					this.getStaffIdForTeam(teamId)));

			criteriaWarningNotice.setProjection(Projections.rowCount());

			criteriaWarningNotice.setFetchMode("w", FetchMode.LAZY);
			criteriaWarningNotice.setFlushMode(FlushMode.ALWAYS);
			Iterator iterator = criteriaWarningNotice.list().iterator();

			Integer warningNoticeCount = (Integer) iterator.next();

			return warningNoticeCount;
		} else
			return 0;
	}

	private Integer warningNoticeCount(Integer roadOperationId,
			Integer personId, String personType) {
		/* Get Count of Summons For Road Operation */
		Criteria criteriaWarningNotice = this.hibernateTemplate
				.getSessionFactory().getCurrentSession()
				.createCriteria(WarningNoticeDO.class, "w");

		criteriaWarningNotice.createAlias("w.roadCheckOffenceOutcome", "rout",
				Criteria.LEFT_JOIN);
		criteriaWarningNotice.createAlias("rout.roadCheckOffence", "roff",
				Criteria.LEFT_JOIN);
		criteriaWarningNotice.createAlias("roff.roadCheck", "rchk",
				Criteria.LEFT_JOIN);
		criteriaWarningNotice.createAlias("rchk.compliance", "comp",
				Criteria.LEFT_JOIN);
		criteriaWarningNotice.createAlias("comp.roadOperation", "rop",
				Criteria.LEFT_JOIN);

		criteriaWarningNotice.add(Restrictions.eq("rop.roadOperationId",
				roadOperationId));

		if (personType.equalsIgnoreCase(Constants.PersonType.TA_STAFF)) {
			criteriaWarningNotice.createAlias("w.taStaff", "ta");
			criteriaWarningNotice.createAlias("ta.person", "p");
		} else {
			return -1;
		}

		criteriaWarningNotice.add(Restrictions.eq("p.personId", personId));

		criteriaWarningNotice.setProjection(Projections.rowCount());

		criteriaWarningNotice.setFetchMode("w", FetchMode.LAZY);
		criteriaWarningNotice.setFlushMode(FlushMode.ALWAYS);
		Iterator iterator = criteriaWarningNotice.list().iterator();

		Integer warningNoticeCount = (Integer) iterator.next();

		return warningNoticeCount;
	}

	private Integer warningNoProsecutionCount(Integer roadOperationId) {
		/* Get Count of Summons For Road Operation */
		Criteria criteriaWarningNoProsecution = this.hibernateTemplate
				.getSessionFactory().getCurrentSession()
				.createCriteria(WarningNoProsecutionDO.class, "w");

		criteriaWarningNoProsecution.createAlias("w.roadCheckOffenceOutcome",
				"rout", Criteria.LEFT_JOIN);
		criteriaWarningNoProsecution.createAlias("rout.roadCheckOffence",
				"roff", Criteria.LEFT_JOIN);
		criteriaWarningNoProsecution.createAlias("roff.roadCheck", "rchk",
				Criteria.LEFT_JOIN);
		criteriaWarningNoProsecution.createAlias("rchk.compliance", "comp",
				Criteria.LEFT_JOIN);
		criteriaWarningNoProsecution.createAlias("comp.roadOperation", "rop",
				Criteria.LEFT_JOIN);

		criteriaWarningNoProsecution.add(Restrictions.eq("rop.roadOperationId",
				roadOperationId));

		criteriaWarningNoProsecution.setProjection(Projections.rowCount());

		criteriaWarningNoProsecution.setFetchMode("w", FetchMode.LAZY);
		criteriaWarningNoProsecution.setFlushMode(FlushMode.ALWAYS);
		Iterator iterator = criteriaWarningNoProsecution.list().iterator();

		Integer warningNoProsecutionCount = (Integer) iterator.next();

		return warningNoProsecutionCount;
	}

	private Integer warningNoProsecutionTeamCount(Integer roadOperationId,
			Integer teamId) {
		/* Get Count of Summons For Road Operation */
		Criteria criteriaWarningNoProsecution = this.hibernateTemplate
				.getSessionFactory().getCurrentSession()
				.createCriteria(WarningNoProsecutionDO.class, "w");

		criteriaWarningNoProsecution.createAlias("w.roadCheckOffenceOutcome",
				"rout", Criteria.LEFT_JOIN);
		criteriaWarningNoProsecution.createAlias("rout.roadCheckOffence",
				"roff", Criteria.LEFT_JOIN);
		criteriaWarningNoProsecution.createAlias("roff.roadCheck", "rchk",
				Criteria.LEFT_JOIN);
		criteriaWarningNoProsecution.createAlias("rchk.compliance", "comp",
				Criteria.LEFT_JOIN);
		criteriaWarningNoProsecution.createAlias("comp.roadOperation", "rop",
				Criteria.LEFT_JOIN);

		criteriaWarningNoProsecution.add(Restrictions.eq("rop.roadOperationId",
				roadOperationId));

		if (this.getStaffIdForTeam(teamId) != null) {
			criteriaWarningNoProsecution.add(Restrictions.in(
					"w.taStaff.staffId", this.getStaffIdForTeam(teamId)));

			criteriaWarningNoProsecution.setProjection(Projections.rowCount());

			criteriaWarningNoProsecution.setFetchMode("w", FetchMode.LAZY);
			criteriaWarningNoProsecution.setFlushMode(FlushMode.ALWAYS);
			Iterator iterator = criteriaWarningNoProsecution.list().iterator();

			Integer warningNoProsecutionCount = (Integer) iterator.next();

			return warningNoProsecutionCount;
		} else
			return 0;
	}

	private Integer warningNoProsecutionCount(Integer roadOperationId,
			Integer personId, String personType) {
		/* Get Count of Summons For Road Operation */
		Criteria criteriaWarningNoProsecution = this.hibernateTemplate
				.getSessionFactory().getCurrentSession()
				.createCriteria(WarningNoticeDO.class, "w");

		criteriaWarningNoProsecution.createAlias("w.roadCheckOffenceOutcome",
				"rout", Criteria.LEFT_JOIN);
		criteriaWarningNoProsecution.createAlias("rout.roadCheckOffence",
				"roff", Criteria.LEFT_JOIN);
		criteriaWarningNoProsecution.createAlias("roff.roadCheck", "rchk",
				Criteria.LEFT_JOIN);
		criteriaWarningNoProsecution.createAlias("rchk.compliance", "comp",
				Criteria.LEFT_JOIN);
		criteriaWarningNoProsecution.createAlias("comp.roadOperation", "rop",
				Criteria.LEFT_JOIN);

		criteriaWarningNoProsecution.add(Restrictions.eq("rop.roadOperationId",
				roadOperationId));

		if (personType.equalsIgnoreCase(Constants.PersonType.TA_STAFF)) {
			criteriaWarningNoProsecution.createAlias("w.taStaff", "ta");
			criteriaWarningNoProsecution.createAlias("ta.person", "p");
		} else {
			return -1;
		}

		criteriaWarningNoProsecution.add(Restrictions
				.eq("p.personId", personId));

		criteriaWarningNoProsecution.setProjection(Projections.rowCount());

		criteriaWarningNoProsecution.setFetchMode("w", FetchMode.LAZY);
		criteriaWarningNoProsecution.setFlushMode(FlushMode.ALWAYS);
		Iterator iterator = criteriaWarningNoProsecution.list().iterator();

		Integer warningNoProsecutionCount = (Integer) iterator.next();

		return warningNoProsecutionCount;
	}

	private Integer complianceCount(Integer roadOperationId) {
		/* Get Count of Summons For Road Operation */
		Criteria criteriaCompliance = this.hibernateTemplate
				.getSessionFactory().getCurrentSession()
				.createCriteria(ComplianceDO.class, "c");
		criteriaCompliance.add(Restrictions.eq(
				"c.roadOperation.roadOperationId", roadOperationId));

		criteriaCompliance.setProjection(Projections.rowCount());

		criteriaCompliance.setFetchMode("c", FetchMode.LAZY);
		criteriaCompliance.setFlushMode(FlushMode.ALWAYS);
		Iterator iterator = criteriaCompliance.list().iterator();

		Integer complainceCount = (Integer) iterator.next();

		return complainceCount;
	}

	private Integer complianceTeamCount(Integer roadOperationId, Integer teamId) {
		/* Get Count of Summons For Road Operation */
		Criteria criteriaCompliance = this.hibernateTemplate
				.getSessionFactory().getCurrentSession()
				.createCriteria(ComplianceDO.class, "c");
		criteriaCompliance.add(Restrictions.eq(
				"c.roadOperation.roadOperationId", roadOperationId));

		if (this.getStaffIdForTeam(teamId) != null) {
			criteriaCompliance.add(Restrictions.in("c.taStaff.staffId",
					this.getStaffIdForTeam(teamId)));

			criteriaCompliance.setProjection(Projections.rowCount());

			criteriaCompliance.setFetchMode("c", FetchMode.LAZY);
			criteriaCompliance.setFlushMode(FlushMode.ALWAYS);
			Iterator iterator = criteriaCompliance.list().iterator();

			Integer complainceCount = (Integer) iterator.next();

			return complainceCount;
		} else
			return 0;
	}

	private Integer complianceCount(Integer roadOperationId, Integer personId,
			String personType) {
		/* Get Count of Summons For Road Operation */
		Criteria criteriaCompliance = this.hibernateTemplate
				.getSessionFactory().getCurrentSession()
				.createCriteria(ComplianceDO.class, "c");
		criteriaCompliance.add(Restrictions.eq(
				"c.roadOperation.roadOperationId", roadOperationId));

		if (personType.equalsIgnoreCase(Constants.PersonType.TA_STAFF)) {
			criteriaCompliance.createAlias("c.taStaff", "ta");
			criteriaCompliance.createAlias("ta.person", "p");
		} else {
			return -1;
		}

		criteriaCompliance.add(Restrictions.eq("p.personId", personId));

		criteriaCompliance.setProjection(Projections.rowCount());

		criteriaCompliance.setFetchMode("c", FetchMode.LAZY);
		criteriaCompliance.setFlushMode(FlushMode.ALWAYS);
		Iterator iterator = criteriaCompliance.list().iterator();

		Integer complainceCount = (Integer) iterator.next();

		return complainceCount;
	}

	private Integer roadCheckTypeCount(Integer roadOperationId, String typeId) {
		/* Get Count of Summons For Road Operation */
		Criteria criteriaRoadCheckType = this.hibernateTemplate
				.getSessionFactory().getCurrentSession()
				.createCriteria(RoadCheckDO.class, "r");

		criteriaRoadCheckType.createAlias("r.compliance", "c");
		criteriaRoadCheckType.createAlias("c.roadOperation", "ro");
		criteriaRoadCheckType.add(Restrictions.eq("ro.roadOperationId",
				roadOperationId));

		criteriaRoadCheckType.add(Restrictions.eq(
				"r.roadCheckType.roadCheckTypeId", typeId).ignoreCase());

		// criteriaRoadCheckType.list();
		criteriaRoadCheckType.setProjection(Projections.rowCount());

		Iterator iterator = criteriaRoadCheckType.list().iterator();

		Integer roadCheckTypeCount = (Integer) iterator.next();

		return roadCheckTypeCount;

		// return 0;
	}

	private Integer roadCheckTypeTeamCount(Integer roadOperationId,
			String typeId, Integer teamId) {
		/* Get Count of Summons For Road Operation */
		Criteria criteriaRoadCheckType = this.hibernateTemplate
				.getSessionFactory().getCurrentSession()
				.createCriteria(RoadCheckDO.class, "r");

		criteriaRoadCheckType.createAlias("r.compliance", "c");
		criteriaRoadCheckType.createAlias("c.roadOperation", "ro");
		criteriaRoadCheckType.add(Restrictions.eq("ro.roadOperationId",
				roadOperationId));

		criteriaRoadCheckType.add(Restrictions.eq(
				"r.roadCheckType.roadCheckTypeId", typeId).ignoreCase());

		if (this.getStaffIdForTeam(teamId) != null) {
			criteriaRoadCheckType.add(Restrictions.in("c.taStaff.staffId",
					this.getStaffIdForTeam(teamId)));

			// criteriaRoadCheckType.list();
			criteriaRoadCheckType.setProjection(Projections.rowCount());

			Iterator iterator = criteriaRoadCheckType.list().iterator();

			Integer roadCheckTypeCount = (Integer) iterator.next();

			return roadCheckTypeCount;
		} else
			return 0;

		//
	}

	private Integer roadCheckTypeCount(Integer roadOperationId, String typeId,
			Integer personId, String personType) {
		/* Get Count of Summons For Road Operation */
		Criteria criteriaRoadCheckType = this.hibernateTemplate
				.getSessionFactory().getCurrentSession()
				.createCriteria(RoadCheckDO.class, "r");

		criteriaRoadCheckType.createAlias("r.compliance", "c");
		criteriaRoadCheckType.createAlias("c.roadOperation", "ro");
		criteriaRoadCheckType.add(Restrictions.eq("ro.roadOperationId",
				roadOperationId));

		criteriaRoadCheckType.add(Restrictions.eq(
				"r.roadCheckType.roadCheckTypeId", typeId).ignoreCase());

		if (personType.equalsIgnoreCase(Constants.PersonType.TA_STAFF)) {
			criteriaRoadCheckType.createAlias("c.taStaff", "ta");
			criteriaRoadCheckType.createAlias("ta.person", "p");
		} else {
			return -1;
		}

		criteriaRoadCheckType.add(Restrictions.eq("p.personId", personId));

		// criteriaRoadCheckType.list();
		criteriaRoadCheckType.setProjection(Projections.rowCount());

		Iterator iterator = criteriaRoadCheckType.list().iterator();

		Integer roadCheckTypeCount = (Integer) iterator.next();

		return roadCheckTypeCount;

		// return 0;
	}

	/**
	 * This function get the count of a particular person type based on the road
	 * operation id
	 * 
	 * @param roadOperationId
	 * @param personType
	 * @return
	 */
	@SuppressWarnings("unused")
	private Integer getCountOfPersonType(Integer roadOperationId,
			String personType) {
		if (roadOperationId != null && personType != null) {
			Criteria criteriaAssignedPerson = this.hibernateTemplate
					.getSessionFactory().getCurrentSession()
					.createCriteria(AssignedPersonDO.class, "assp");

			criteriaAssignedPerson.add(Restrictions.in(
					"assp.assignedPersonKey.team.teamId",
					getTeamIdsForRoadOp(roadOperationId)));
			criteriaAssignedPerson.add(Restrictions.eq(
					"assp.assignedPersonKey.personType.personTypeId",
					personType));

			criteriaAssignedPerson.setProjection(Projections.rowCount());

			@SuppressWarnings("rawtypes")
			Iterator iterator = criteriaAssignedPerson.list().iterator();

			return (Integer) iterator.next();

		} else {
			return 0;
		}
	}

	/**
	 * This method returns a count of outcomes based on the input parameters.
	 * 
	 * @param roadOperationId
	 * @param outcomeTypeId
	 * @param personId
	 * @param personType
	 * @return
	 */
	private Integer roadCheckOutcomeCount(Integer roadOperationId,
			String outcomeTypeId, Integer personId, String personType) {
		Criteria criteriaCheckOutcome = this.hibernateTemplate
				.getSessionFactory()
				.getCurrentSession()
				.createCriteria(RoadCheckOffenceOutcomeDO.class, "roadCheckOut");

		criteriaCheckOutcome.createAlias("roadCheckOut.roadCheckOffence",
				"rChOff");
		criteriaCheckOutcome.createAlias("rChOff.roadCheck", "rCheck");
		criteriaCheckOutcome.createAlias("rCheck.compliance", "comp");
		criteriaCheckOutcome.createAlias("comp.roadOperation", "roadOp");

		criteriaCheckOutcome.add(Restrictions.eq("roadOp.roadOperationId",
				roadOperationId));

		criteriaCheckOutcome.add(Restrictions.eq(
				"roadCheckOut.outcomeType.outcomeTypeId", outcomeTypeId)
				.ignoreCase());

		if (personType.equalsIgnoreCase(Constants.PersonType.TA_STAFF)) {
			criteriaCheckOutcome.createAlias("comp.taStaff", "ta");
			criteriaCheckOutcome.createAlias("ta.person", "p");
		} else {
			return -1;
		}

		criteriaCheckOutcome.add(Restrictions.eq("p.personId", personId));

		criteriaCheckOutcome.setProjection(Projections.rowCount());

		Iterator iterator = criteriaCheckOutcome.list().iterator();

		return (Integer) iterator.next();

	}

	/**
	 * This method returns a count of outcomes based on the input parameters.
	 * 
	 * @param roadOperationId
	 * @param outcomeTypeId
	 * @return
	 */
	private Integer roadCheckOutcomeCount(Integer roadOperationId,
			String outcomeTypeId) {
		Criteria criteriaCheckOutcome = this.hibernateTemplate
				.getSessionFactory()
				.getCurrentSession()
				.createCriteria(RoadCheckOffenceOutcomeDO.class, "roadCheckOut");

		criteriaCheckOutcome.createAlias("roadCheckOut.roadCheckOffence",
				"rChOff");
		criteriaCheckOutcome.createAlias("rChOff.roadCheck", "rCheck");
		criteriaCheckOutcome.createAlias("rCheck.compliance", "comp");
		criteriaCheckOutcome.createAlias("comp.roadOperation", "roadOp");

		criteriaCheckOutcome.add(Restrictions.eq("roadOp.roadOperationId",
				roadOperationId));

		criteriaCheckOutcome.add(Restrictions.eq(
				"roadCheckOut.outcomeType.outcomeTypeId", outcomeTypeId)
				.ignoreCase());

		criteriaCheckOutcome.setProjection(Projections.rowCount());

		Iterator iterator = criteriaCheckOutcome.list().iterator();

		return (Integer) iterator.next();

	}

	private Integer roadCheckOutcomeTeamCount(Integer roadOperationId,
			String outcomeTypeId, Integer teamId) {
		Criteria criteriaCheckOutcome = this.hibernateTemplate
				.getSessionFactory()
				.getCurrentSession()
				.createCriteria(RoadCheckOffenceOutcomeDO.class, "roadCheckOut");

		criteriaCheckOutcome.createAlias("roadCheckOut.roadCheckOffence",
				"rChOff");
		criteriaCheckOutcome.createAlias("rChOff.roadCheck", "rCheck");
		criteriaCheckOutcome.createAlias("rCheck.compliance", "comp");
		criteriaCheckOutcome.createAlias("comp.roadOperation", "roadOp");

		if (this.getStaffIdForTeam(teamId) != null) {
			criteriaCheckOutcome.add(Restrictions.eq("roadOp.roadOperationId",
					roadOperationId));

			criteriaCheckOutcome.add(Restrictions.in("comp.taStaff.staffId",
					this.getStaffIdForTeam(teamId)));

			criteriaCheckOutcome.add(Restrictions.eq(
					"roadCheckOut.outcomeType.outcomeTypeId", outcomeTypeId)
					.ignoreCase());

			criteriaCheckOutcome.setProjection(Projections.rowCount());

			Iterator iterator = criteriaCheckOutcome.list().iterator();

			return (Integer) iterator.next();
		} else
			return 0;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see public List<SummonsOutstandingReportBO>
	 * summonsOutstandingReport(SummonsOutstandingReportCriteriaBO
	 * reportCriteria, String userName, String userRegion,
	 * ReportDisplayInformationDAOImpl reportDisplayInformation);
	 */
	@Override
	public SummonsOutstandingReportBO summonsOutstandingReport(
			SummonsOutstandingReportCriteriaBO reportCriteria, String userName,
			String userRegion,
			ReportDisplayInformationDAOImpl reportDisplayInformation) {
		Criteria criteria = this.hibernateTemplate.getSessionFactory()
				.getCurrentSession().createCriteria(SummonsDO.class, "s");
		criteria.createAlias("s.roadOperation", "ro", Criteria.LEFT_JOIN);
		criteria.createAlias("s.offender", "of", Criteria.LEFT_JOIN);
		criteria.createAlias("s.status", "st", Criteria.LEFT_JOIN);
		criteria.createAlias("ro.category", "c", Criteria.LEFT_JOIN);
		criteria.createAlias("s.taStaff", "ta", Criteria.LEFT_JOIN);
		criteria.createAlias("ta.person", "taPerson");

		Date reportStartDate = DateUtils.searchDateFormater(
				reportCriteria.getOperationStartDate(),
				DateUtils.SEARCHDATETYPE.START);
		Date reportEndDate = DateUtils.searchDateFormater(
				reportCriteria.getOperationEndDate(),
				DateUtils.SEARCHDATETYPE.END);

		Criterion mainCriterion = Restrictions.or(Restrictions.between(
				"ro.scheduledStartDtime", reportStartDate, reportEndDate),
				Restrictions.between("ro.scheduledEndDtime", reportStartDate,
						reportEndDate));

		/* Only summons which are not yet issued should be returned */
		List<String> summonsOutstandingStatusList = new ArrayList<String>();
		summonsOutstandingStatusList
				.add(fsl.ta.toms.roms.constants.Constants.DocumentStatus.CANCELLED);
		summonsOutstandingStatusList
				.add(fsl.ta.toms.roms.constants.Constants.DocumentStatus.WITHDRAWN);
		summonsOutstandingStatusList
				.add(fsl.ta.toms.roms.constants.Constants.DocumentStatus.SERVED);
		Criterion subCriterion = Restrictions.not(Restrictions.in(
				"st.statusId", summonsOutstandingStatusList));
		/* _________________________________________________________ */

		if (reportCriteria.getOffenderId() != null
				&& reportCriteria.getOffenderId() > 0) {
			subCriterion = Restrictions.and(
					subCriterion,
					Restrictions.eq("of.personId",
							reportCriteria.getOffenderId()));
		}

		if (StringUtil.isSet(reportCriteria.getOffenderTRN())) {
			subCriterion = Restrictions.and(subCriterion, Restrictions.eq(
					"of.trnNbr", reportCriteria.getOffenderTRN().trim()));
		}

		if (StringUtil.isSet(reportCriteria.getOffenderFirstName())) {
			subCriterion = Restrictions.and(
					subCriterion,
					Restrictions.like("of.firstName",
							reportCriteria.getOffenderFirstName().trim(),
							MatchMode.ANYWHERE).ignoreCase());
		}

		if (StringUtil.isSet(reportCriteria.getOffenderLastName())) {
			subCriterion = Restrictions.and(
					subCriterion,
					Restrictions.like("of.lastName",
							reportCriteria.getOffenderLastName().trim(),
							MatchMode.ANYWHERE).ignoreCase());
		}

		if (reportCriteria.getOperationCategory() != null
				&& !reportCriteria.getOperationCategory().isEmpty()) {
			subCriterion = Restrictions.and(subCriterion, Restrictions.eq(
					"c.categoryId", reportCriteria.getOperationCategory()
							.trim()));

		}

		if (reportCriteria.getRoadOperationId() != null
				&& reportCriteria.getRoadOperationId() > 0) {
			subCriterion = Restrictions.and(
					subCriterion,
					Restrictions.eq("ro.roadOperationId",
							reportCriteria.getRoadOperationId()));
		}

		if (reportCriteria.getTAOfficeRegion() != null
				&& !reportCriteria.getTAOfficeRegion().isEmpty()) {
			subCriterion = Restrictions.and(subCriterion, Restrictions.eq(
					"ro.officeLocCode", reportCriteria.getTAOfficeRegion()
							.trim()));
		}

		if (reportCriteria.getTAStaffId() != null
				&& !reportCriteria.getTAStaffId().isEmpty()) {
			subCriterion = Restrictions.and(subCriterion, Restrictions.eq(
					"ta.staffId", reportCriteria.getTAStaffId().trim()));
		}

		if (StringUtil.isSet(reportCriteria.getTAStaffTRN())) {
			subCriterion = Restrictions.and(subCriterion, Restrictions.eq(
					"taPerson.trnNbr", reportCriteria.getTAStaffTRN().trim()));
		}

		if (StringUtil.isSet(reportCriteria.getRoadOperationName())) {
			subCriterion = Restrictions.and(
					subCriterion,
					Restrictions.like("ro.operationName",
							reportCriteria.getRoadOperationName().trim(),
							MatchMode.ANYWHERE).ignoreCase());
		}

		criteria.add(Restrictions.and(mainCriterion, subCriterion));

		NameUtil nameUtil = new NameUtil();
		String stringStartDate = "";
		String stringEndDate = "";
		try {
			stringStartDate = DateUtils.getFormattedUtilDate(reportCriteria
					.getOperationStartDate());
			stringEndDate = DateUtils.getFormattedUtilDate(reportCriteria
					.getOperationEndDate());
		} catch (Exception exe) {

		}
		/* Get report criteria names and descriptions */

		if (reportCriteria.getOffenderId() != null
				&& reportCriteria.getOffenderId() > 0)
			reportCriteria.setOffenderName(this.getPersonName(reportCriteria
					.getOffenderId()));
		else if (StringUtil.isSet(reportCriteria.getOffenderTRN()))
			reportCriteria.setOffenderName(this.getPersonName(reportCriteria
					.getOffenderTRN()));

		reportCriteria
				.setOperationCategoryDesc(this
						.getOperationCategoryDesc(reportCriteria
								.getOperationCategory()));
		reportCriteria.setRoadOperationName(reportCriteria
				.getRoadOperationName());

		reportCriteria.setTAOfficeRegionDesc(this
				.getTAOfficeRegionDescription(reportCriteria
						.getTAOfficeRegion()));

		if (StringUtil.isSet(reportCriteria.getTAStaffId()))
			reportCriteria.setTAStaffName(this.getTAStaffName(reportCriteria
					.getTAStaffId()));
		else if (StringUtil.isSet(reportCriteria.getTAStaffTRN()))
			reportCriteria.setTAStaffName(this.getPersonName(reportCriteria
					.getTAStaffTRN()));

		/*
		 * reportCriteria.setRoadOperationDesc(this.getRoadOperationName(
		 * reportCriteria.getRoadOperationId()));
		 */
		/* __________________________________________ */

		SummonsOutstandingReportBO report = new SummonsOutstandingReportBO(
				userName, userRegion, reportDisplayInformation.applicationName,
				reportDisplayInformation.summonsOutstandingReportTitle
						+ stringStartDate + " TO " + stringEndDate,
				reportCriteria.getOperationStartDate(),
				reportCriteria.getOperationEndDate(),
				reportCriteria.getSearchCriteriaString(),
				getTAOfficeRegionDescription(userRegion));

		List<SummonsOutstandingReportResultsBO> summonsOutStReportList = new ArrayList<SummonsOutstandingReportResultsBO>();

		for (SummonsDO summons : (List<SummonsDO>) criteria.list()) {

			String offenderFullName = NameUtil.getName((StringUtil
					.isSet(summons.getOffender().getFirstName()) ? summons
					.getOffender().getFirstName().trim() : ""), (StringUtil
					.isSet(summons.getOffender().getLastName()) ? summons
					.getOffender().getLastName().trim() : ""));

			String offenceDescription = summons.getRoadCheckOffenceOutcome()
					.getRoadCheckOffence().getOffence().getDescription();

			String tAStaffFullName = NameUtil.getName(
					(StringUtil.isSet(summons.getTaStaff().getPerson()
							.getFirstName()) ? summons.getTaStaff().getPerson()
							.getFirstName().trim() : ""),
					(StringUtil.isSet(summons.getTaStaff().getPerson()
							.getLastName()) ? summons.getTaStaff().getPerson()
							.getLastName().trim() : ""));

			VehicleDO vehicle = summons.getRoadCheckOffenceOutcome()
					.getRoadCheckOffence().getRoadCheck().getCompliance()
					.getVehicle();
			String vehicleDetails = "";
			if (vehicle != null) {
				vehicleDetails = (StringUtil.isSet(vehicle.getModel()) ? vehicle
						.getModel().trim() : "")
						+ "; "
						+ (StringUtil.isSet(vehicle.getMakeDescription()) ? vehicle
								.getMakeDescription().trim() : "")
						+ "; Plate #: "
						+ (StringUtil.isSet(vehicle.getPlateRegNo()) ? vehicle
								.getPlateRegNo().trim() : "");
			}

			String locationOfOffence = summons.getRoadCheckOffenceOutcome()
					.getRoadCheckOffence().getRoadCheck().getCompliance()
					.getCompliancyArtery().getDescription();

			String tAOfficeRegion = summons.getRoadCheckOffenceOutcome()
					.getRoadCheckOffence().getRoadCheck().getCompliance()
					.getRoadOperation().getOfficeLocCode();

			String jPFullName = NameUtil.getName(
					(StringUtil.isSet(summons.getJusticeOfPeace().getPerson()
							.getFirstName()) ? summons.getJusticeOfPeace()
							.getPerson().getFirstName().trim() : ""),
					(StringUtil.isSet(summons.getJusticeOfPeace().getPerson()
							.getLastName()) ? summons.getJusticeOfPeace()
							.getPerson().getLastName() : ""));

			String roadOperationDetails = summons.getRoadCheckOffenceOutcome()
					.getRoadCheckOffence().getRoadCheck().getCompliance()
					.getRoadOperation().getOperationName();

			/* Get latest trial for the summons */
			Criteria criteriaCourtAppearance = this.hibernateTemplate
					.getSessionFactory().getCurrentSession()
					.createCriteria(CourtAppearanceDO.class, "CApp");

			criteriaCourtAppearance.createAlias("CApp.courtCase", "CCase");
			criteriaCourtAppearance.createAlias("CCase.summons", "Summ");

			criteriaCourtAppearance.add(Restrictions.eq("Summ.summonsId",
					summons.getSummonsId()));

			criteriaCourtAppearance.addOrder(Order.asc("CApp.CourtDTime"));
			criteriaCourtAppearance.addOrder(Order
					.asc("CApp.courtAppearanceId"));

			Integer courtAppListSize = criteriaCourtAppearance.list().size();
			String courtDetails = "";

			if (courtAppListSize > 0) {
				CourtAppearanceDO courtApp = (CourtAppearanceDO) criteriaCourtAppearance
						.list().get(courtAppListSize - 1);

				String stringTrialDate = "";
				SimpleDateFormat dt = new SimpleDateFormat(
						"yyyy-MMM-dd HH:mm:ss");
				try {
					stringTrialDate = dt.format(courtApp.getCourtDTime());

				} catch (Exception exe) {

				}

				courtDetails = String.format(
						"Court Appearance Date is %s at %s.", stringTrialDate,
						courtApp.getCourt().getDescription());
			}
			/* _______________________________ */

			SummonsOutstandingReportResultsBO summonsOutstanding = new SummonsOutstandingReportResultsBO(
					offenderFullName, offenceDescription, tAStaffFullName,
					vehicleDetails, locationOfOffence, tAOfficeRegion,
					jPFullName, roadOperationDetails, courtDetails,
					getTAOfficeRegionDescription(tAOfficeRegion),
					summons.getServedOnDate());

			summonsOutStReportList.add(summonsOutstanding);
		}

		report.setResults(summonsOutStReportList);
		return report;
	}

	@Override
	public SummonsReportBO summonsReport(
			SummonsReportCriteriaBO reportCriteria, String userName,
			String userRegion,
			ReportDisplayInformationDAOImpl reportDisplayInformation) {
		Criteria criteria = this.hibernateTemplate.getSessionFactory()
				.getCurrentSession().createCriteria(SummonsDO.class, "s");
		criteria.createAlias("s.roadOperation", "ro", Criteria.LEFT_JOIN);
		criteria.createAlias("s.offender", "of", Criteria.LEFT_JOIN);
		criteria.createAlias("s.status", "st", Criteria.LEFT_JOIN);
		criteria.createAlias("ro.category", "c", Criteria.LEFT_JOIN);
		criteria.createAlias("s.taStaff", "ta", Criteria.LEFT_JOIN);
		criteria.createAlias("ta.person", "taPerson", Criteria.LEFT_JOIN);

		Criterion mainCriterion = Restrictions.sqlRestriction("1=1");

		if (reportCriteria.getOperationStartDate() != null
				&& reportCriteria.getOperationEndDate() != null) {
			Date reportStartDate = DateUtils.searchDateFormater(
					reportCriteria.getOperationStartDate(),
					DateUtils.SEARCHDATETYPE.START);
			Date reportEndDate = DateUtils.searchDateFormater(
					reportCriteria.getOperationEndDate(),
					DateUtils.SEARCHDATETYPE.END);

			Criterion scheduledDate = Restrictions.or(Restrictions.between(
					"ro.scheduledStartDtime", reportStartDate, reportEndDate),
					Restrictions.between("ro.scheduledEndDtime",
							reportStartDate, reportEndDate));

			Criterion actualDate = Restrictions.or(Restrictions.between(
					"ro.actualStartDtime", reportStartDate, reportEndDate),
					Restrictions.between("ro.actualEndDtime", reportStartDate,
							reportEndDate));

			mainCriterion = Restrictions.and(mainCriterion,
					Restrictions.or(scheduledDate, actualDate));
		}

		if (reportCriteria.getOffenceStartDate() != null
				&& reportCriteria.getOffenceEndDate() != null) {
			mainCriterion = Restrictions.and(mainCriterion, Restrictions
					.between("s.offenceDtime", DateUtils.searchDateFormater(
							reportCriteria.getOffenceStartDate(),
							DateUtils.SEARCHDATETYPE.START), DateUtils
							.searchDateFormater(
									reportCriteria.getOffenceEndDate(),
									DateUtils.SEARCHDATETYPE.END)));
		}

		if (reportCriteria.getIssuedStartDate() != null
				&& reportCriteria.getIssuedEndDate() != null) {
			mainCriterion = Restrictions.and(mainCriterion, Restrictions
					.between("s.issueDate", DateUtils.searchDateFormater(
							reportCriteria.getIssuedStartDate(),
							DateUtils.SEARCHDATETYPE.START), DateUtils
							.searchDateFormater(
									reportCriteria.getIssuedEndDate(),
									DateUtils.SEARCHDATETYPE.END)));
		}

		if (reportCriteria.getPrintStartDate() != null
				&& reportCriteria.getPrintEndDate() != null) {
			mainCriterion = Restrictions.and(mainCriterion, Restrictions
					.between("s.printDtime", DateUtils.searchDateFormater(
							reportCriteria.getPrintStartDate(),
							DateUtils.SEARCHDATETYPE.START), DateUtils
							.searchDateFormater(
									reportCriteria.getPrintEndDate(),
									DateUtils.SEARCHDATETYPE.END)));

		}

		if (reportCriteria.getOffenderId() != null
				&& reportCriteria.getOffenderId() > 0) {
			mainCriterion = Restrictions.and(
					mainCriterion,
					Restrictions.eq("of.personId",
							reportCriteria.getOffenderId()));
		}

		if (StringUtil.isSet(reportCriteria.getOffenderTRN())) {
			mainCriterion = Restrictions.and(mainCriterion, Restrictions.eq(
					"of.trnNbr", reportCriteria.getOffenderTRN().trim()));
		}

		if (reportCriteria.getOperationCategory() != null
				&& !reportCriteria.getOperationCategory().isEmpty()) {
			mainCriterion = Restrictions.and(mainCriterion, Restrictions.eq(
					"c.categoryId", reportCriteria.getOperationCategory()
							.trim()));

		}

		if (reportCriteria.getRoadOperationId() != null
				&& reportCriteria.getRoadOperationId() > 0) {
			mainCriterion = Restrictions.and(
					mainCriterion,
					Restrictions.eq("ro.roadOperationId",
							reportCriteria.getRoadOperationId()));
		}

		if (reportCriteria.getTAOfficeRegion() != null
				&& !reportCriteria.getTAOfficeRegion().isEmpty()) {
			mainCriterion = Restrictions.and(mainCriterion, Restrictions.eq(
					"ro.officeLocCode", reportCriteria.getTAOfficeRegion()
							.trim()));
		}

		if (reportCriteria.getTAStaffId() != null
				&& !reportCriteria.getTAStaffId().isEmpty()) {
			mainCriterion = Restrictions.and(mainCriterion, Restrictions.eq(
					"ta.staffId", reportCriteria.getTAStaffId().trim()));
		}

		if (StringUtil.isSet(reportCriteria.getTAStaffTRN())) {
			mainCriterion = Restrictions.and(mainCriterion, Restrictions.eq(
					"taPerson.trnNbr", reportCriteria.getTAStaffTRN().trim()));
		}

		if (StringUtil.isSet(reportCriteria.getRoadOperationName())) {
			mainCriterion = Restrictions.and(
					mainCriterion,
					Restrictions.like("ro.operationName",
							reportCriteria.getRoadOperationName().trim(),
							MatchMode.ANYWHERE).ignoreCase());
		}

		if (StringUtil.isSet(reportCriteria.getStatus())) {
			mainCriterion = Restrictions.and(mainCriterion, Restrictions.eq(
					"s.status.statusId", reportCriteria.getStatus().trim()));
		}

		if (StringUtil.isSet(reportCriteria.getOffenderFirstName())) {
			mainCriterion = Restrictions.and(
					mainCriterion,
					Restrictions.like("of.firstName",
							reportCriteria.getOffenderFirstName().trim(),
							MatchMode.ANYWHERE).ignoreCase());
		}

		if (StringUtil.isSet(reportCriteria.getOffenderLastName())) {
			mainCriterion = Restrictions.and(
					mainCriterion,
					Restrictions.like("of.lastName",
							reportCriteria.getOffenderLastName().trim(),
							MatchMode.ANYWHERE).ignoreCase());
		}

		criteria.add(mainCriterion);

		String stringStartDate = "";
		String stringEndDate = "";
		String dateTypeUsed = "";
		Date startDate = null;
		Date endDate = null;
		try {
			if (reportCriteria.getOperationStartDate() != null
					&& reportCriteria.getOperationEndDate() != null) {
				startDate = reportCriteria.getOperationStartDate();
				endDate = reportCriteria.getOperationEndDate();
				stringStartDate = DateUtils.getFormattedUtilDate(reportCriteria
						.getOperationStartDate());
				stringEndDate = DateUtils.getFormattedUtilDate(reportCriteria
						.getOperationEndDate());

				dateTypeUsed = "Operation";
			} else if (reportCriteria.getOffenceStartDate() != null
					&& reportCriteria.getOffenceEndDate() != null) {
				startDate = reportCriteria.getOffenceStartDate();
				endDate = reportCriteria.getOffenceEndDate();
				stringStartDate = DateUtils.getFormattedUtilDate(reportCriteria
						.getOffenceStartDate());
				stringEndDate = DateUtils.getFormattedUtilDate(reportCriteria
						.getOffenceEndDate());

				dateTypeUsed = "Offence";
			} else if (reportCriteria.getPrintStartDate() != null
					&& reportCriteria.getPrintEndDate() != null) {
				startDate = reportCriteria.getPrintStartDate();
				endDate = reportCriteria.getPrintEndDate();
				stringStartDate = DateUtils.getFormattedUtilDate(reportCriteria
						.getPrintStartDate());
				stringEndDate = DateUtils.getFormattedUtilDate(reportCriteria
						.getPrintEndDate());

				dateTypeUsed = "Print";
			} else if (reportCriteria.getIssuedStartDate() != reportCriteria
					.getIssuedEndDate()) {
				startDate = reportCriteria.getIssuedStartDate();
				endDate = reportCriteria.getIssuedEndDate();
				stringStartDate = DateUtils.getFormattedUtilDate(reportCriteria
						.getIssuedStartDate());
				stringEndDate = DateUtils.getFormattedUtilDate(reportCriteria
						.getIssuedEndDate());

				dateTypeUsed = "Issued";
			}
		} catch (Exception exe) {

		}

		/* Get report criteria names and descriptions */
		if (reportCriteria.getOffenderId() != null
				&& reportCriteria.getOffenderId() > 0) {
			reportCriteria.setOffenderName(this.getPersonName(reportCriteria
					.getOffenderId()));
		} else if (StringUtil.isSet(reportCriteria.getOffenderTRN())) {
			reportCriteria.setOffenderName(this.getPersonName(reportCriteria
					.getOffenderTRN()));
		}

		reportCriteria
				.setOperationCategoryDesc(this
						.getOperationCategoryDesc(reportCriteria
								.getOperationCategory()));
		reportCriteria.setRoadOperationName(reportCriteria
				.getRoadOperationName());

		reportCriteria.setTAOfficeRegionDesc(this
				.getTAOfficeRegionDescription(reportCriteria
						.getTAOfficeRegion()));

		if (StringUtil.isSet(reportCriteria.getTAStaffId()))
			reportCriteria.setTAStaffName(this.getTAStaffName(reportCriteria
					.getTAStaffId()));
		else if (StringUtil.isSet(reportCriteria.getTAStaffTRN()))
			reportCriteria.setTAStaffName(this.getPersonName(reportCriteria
					.getTAStaffTRN()));
		/*
		 * reportCriteria.setRoadOperationDesc(this.getRoadOperationName(
		 * reportCriteria.getRoadOperationId()));
		 */
		/* __________________________________________ */

		SummonsReportBO report = new SummonsReportBO(userName, userRegion,
				reportDisplayInformation.applicationName,
				reportDisplayInformation.summonsReportTitle + "("
						+ dateTypeUsed + ") " + stringStartDate + " TO "
						+ stringEndDate, startDate, endDate,
				reportCriteria.getSearchCriteriaString(),
				getTAOfficeRegionDescription(userRegion));

		List<SummonsReportResultsBO> summonsReportList = new ArrayList<SummonsReportResultsBO>();

		for (SummonsDO summons : (List<SummonsDO>) criteria.list()) {

			String offenderFullName = NameUtil.getName((StringUtil
					.isSet(summons.getOffender().getFirstName()) ? summons
					.getOffender().getFirstName().trim() : ""), (StringUtil
					.isSet(summons.getOffender().getLastName()) ? summons
					.getOffender().getLastName().trim() : ""));

			String offenceDescription = summons.getRoadCheckOffenceOutcome()
					.getRoadCheckOffence().getOffence().getDescription();

			String tAStaffFullName = NameUtil.getName(
					(StringUtil.isSet(summons.getTaStaff().getPerson()
							.getFirstName()) ? summons.getTaStaff().getPerson()
							.getFirstName().trim() : ""),
					(StringUtil.isSet(summons.getTaStaff().getPerson()
							.getLastName()) ? summons.getTaStaff().getPerson()
							.getLastName().trim() : ""));

			VehicleDO vehicle = summons.getRoadCheckOffenceOutcome()
					.getRoadCheckOffence().getRoadCheck().getCompliance()
					.getVehicle();
			String vehicleDetails = "";
			if (vehicle != null) {
				vehicleDetails = (StringUtil.isSet(vehicle.getModel()) ? vehicle
						.getModel().trim() : "")
						+ "; "
						+ (StringUtil.isSet(vehicle.getMakeDescription()) ? vehicle
								.getMakeDescription().trim() : "")
						+ "; Plate #: "
						+ (StringUtil.isSet(vehicle.getPlateRegNo()) ? vehicle
								.getPlateRegNo().trim() : "");
			}

			String locationOfOffence = summons.getRoadCheckOffenceOutcome()
					.getRoadCheckOffence().getRoadCheck().getCompliance()
					.getCompliancyArtery().getDescription();

			String tAOfficeRegion = summons.getRoadCheckOffenceOutcome()
					.getRoadCheckOffence().getRoadCheck().getCompliance()
					.getRoadOperation().getOfficeLocCode();

			String jPFullName = NameUtil.getName(
					(StringUtil.isSet(summons.getJusticeOfPeace().getPerson()
							.getFirstName()) ? summons.getJusticeOfPeace()
							.getPerson().getFirstName().trim() : ""),
					(StringUtil.isSet(summons.getJusticeOfPeace().getPerson()
							.getLastName()) ? summons.getJusticeOfPeace()
							.getPerson().getLastName() : ""));

			String roadOperationDetails = summons.getRoadCheckOffenceOutcome()
					.getRoadCheckOffence().getRoadCheck().getCompliance()
					.getRoadOperation().getOperationName();

			/* Get latest trial for the summons */
			Criteria criteriaCourtAppearance = this.hibernateTemplate
					.getSessionFactory().getCurrentSession()
					.createCriteria(CourtAppearanceDO.class, "CApp");

			criteriaCourtAppearance.createAlias("CApp.courtCase", "CCase");
			criteriaCourtAppearance.createAlias("CCase.summons", "Summ");

			criteriaCourtAppearance.add(Restrictions.eq("Summ.summonsId",
					summons.getSummonsId()));

			criteriaCourtAppearance.addOrder(Order.asc("CApp.CourtDTime"));
			criteriaCourtAppearance.addOrder(Order
					.asc("CApp.courtAppearanceId"));

			Integer courtAppListSize = criteriaCourtAppearance.list().size();
			String courtDetails = "";

			if (courtAppListSize > 0) {
				CourtAppearanceDO courtApp = (CourtAppearanceDO) criteriaCourtAppearance
						.list().get(courtAppListSize - 1);

				String stringTrialDate = "";
				SimpleDateFormat dt = new SimpleDateFormat(
						"yyyy-MMM-dd HH:mm:ss");
				try {
					stringTrialDate = dt.format(courtApp.getCourtDTime());

				} catch (Exception exe) {

				}

				courtDetails = String.format(
						"Court Appearance Date is %s at %s.", stringTrialDate,
						courtApp.getCourt().getDescription());
			}
			/* _______________________________ */

			SummonsReportResultsBO summonsRptResult = new SummonsReportResultsBO(
					offenderFullName, offenceDescription, tAStaffFullName,
					vehicleDetails, locationOfOffence, tAOfficeRegion,
					jPFullName, roadOperationDetails, courtDetails,
					getTAOfficeRegionDescription(tAOfficeRegion),
					summons.getOffenceDtime(), summons.getServedOnDate(),
					summons.getPrintDtime(), summons.getStatus().getStatusId(),
					summons.getStatus().getDescription(),
					summons.getManualSerialNumber(), summons.getComment(),
					summons.getReason() != null ? summons.getReason()
							.getDescription() : null, summons.getReprintDtime());

			summonsReportList.add(summonsRptResult);
		}

		report.setResults(summonsReportList);
		return report;
	}

	public String getTAStaffName(String taStaffId) {
		if (StringUtil.isSet(taStaffId)) {
			TAStaffDO staff = (TAStaffDO) this.hibernateTemplate.get(
					TAStaffDO.class, taStaffId.trim());

			if (staff != null) {
				return NameUtil.getName(staff.getPerson().getFirstName(), staff
						.getPerson().getLastName());
			} else {
				return null;
			}

		} else {
			return null;
		}
	}

	public String getTAStaffNames(List<String> taStaffIdList) {
		StringBuilder taStaffList = new StringBuilder("");

		if (taStaffIdList != null && taStaffIdList.size() > 0) {
			for (String taStaffId : taStaffIdList) {
				taStaffList.append(this.getTAStaffName(taStaffId) + ", ");
			}
		} else
			return null;

		return taStaffList.toString();
	}

	public String getUserName(String userName) {
		if (StringUtil.isSet(userName)) {
			LMIS_UserViewDO user = (LMIS_UserViewDO) this.hibernateTemplate
					.get(LMIS_UserViewDO.class, userName.trim().toUpperCase());

			if (user != null) {
				return NameUtil
						.getName(user.getFirstName(), user.getLastName());
			} else {
				return null;
			}

		} else {
			return null;
		}
	}

	public String getTAOfficeRegionDescription(String officeLocCode) {

		if (StringUtil.isSet(officeLocCode)) {
			LMIS_TAOfficeLocationViewDO officeLocView = this.hibernateTemplate
					.get(LMIS_TAOfficeLocationViewDO.class,
							officeLocCode.trim());

			if (officeLocView != null)
				return officeLocView.getLocationDesc();
			else
				return null;
		} else {
			return null;
		}

	}


	public String getTAOfficeRegionDescription(CourtDO court) {

		if(court != null)
		{String officeLocCode = (court.getAddress()).getParish().getOfficeLocationCode();
		
		if (StringUtil.isSet(officeLocCode)) {
			LMIS_TAOfficeLocationViewDO officeLocView = this.hibernateTemplate
					.get(LMIS_TAOfficeLocationViewDO.class,
							officeLocCode.trim());

			if (officeLocView != null)
				return officeLocView.getLocationDesc();
			else
				return null;
		} else {
			return null;
		}
		}else
		{
			return null;
		}
		

	}

	
	public String getCourtDescription(Integer courtId) {

		if (courtId != null) {
			CourtDO court = this.hibernateTemplate.get(CourtDO.class, courtId);

			if (court != null)
				return court.getShortDesc();
			else
				return null;
		} else {
			return null;
		}

	}

	public String getPersonName(Integer personID) {
		if (personID != null) {

			PersonDO person = this.hibernateTemplate.get(PersonDO.class,
					personID);

			if (person != null)
				return NameUtil.getName(person.getFirstName(),
						person.getLastName());
			else
				return null;
		} else {
			return null;
		}

	}

	public String getPersonNames(List<Integer> personIds) {
		StringBuilder taStaffNames = new StringBuilder("");

		if (personIds != null && personIds.size() > 0) {
			for (Integer personId : personIds) {
				taStaffNames.append(this.getPersonName(personId) + ", ");
			}
		} else
			return null;

		return taStaffNames.toString();
	}

	public String getPersonNamesWithTRN(List<String> personIds) {
		StringBuilder taStaffNames = new StringBuilder("");

		if (personIds != null && personIds.size() > 0) {
			for (String personId : personIds) {
				taStaffNames.append(this.getPersonName(personId) + ", ");
			}
		} else
			return null;

		return taStaffNames.toString();
	}

	public String getPersonName(String trn) {
		if (StringUtil.isSet(trn)) {

			Criteria criteria = this.hibernateTemplate.getSessionFactory()
					.getCurrentSession().createCriteria(PersonDO.class);

			criteria.add(Restrictions.eq("trnNbr", trn.trim()));

			List<PersonDO> personList = criteria.list();

			if (!personList.isEmpty())
				return NameUtil.getName(personList.get(0).getFirstName(),
						personList.get(0).getLastName());
			else
				return null;
		} else {
			return null;
		}

	}

	public String getOperationCategoryDesc(String categoryID) {

		if (categoryID != null) {

			CDCategoryDO category = this.hibernateTemplate.get(
					CDCategoryDO.class, categoryID.trim());

			if (category != null)
				return category.getDescription();
			else
				return null;
		} else {
			return null;
		}

	}

	public String getRoadOperationName(Integer roadOpID) {
		if (roadOpID != null) {

			RoadOperationDO roadOp = this.hibernateTemplate.get(
					RoadOperationDO.class, roadOpID);

			if (roadOp != null)
				return roadOp.getOperationName();
			else
				return null;
		} else {
			return null;
		}

	}

	public String getWreckingCompanyName(Integer wreckingCompanyID) {
		if (wreckingCompanyID != null) {

			WreckingCompanyDO weckingComp = this.hibernateTemplate.get(
					WreckingCompanyDO.class, wreckingCompanyID);

			if (weckingComp != null)
				return weckingComp.getCompanyName();
			else
				return null;
		} else {
			return null;
		}

	}

	public String getEventDesc(Integer eventCode) {
		if (eventCode != null) {

			CDEventDO event = this.hibernateTemplate.get(CDEventDO.class,
					eventCode);

			if (event != null)
				return event.getEventDescription();
			else
				return null;
		} else {
			return null;
		}

	}

	public String getRefTypeDesc(String refTypeCode) {
		if (StringUtil.isSet(refTypeCode)) {

			CDEventRefTypeDO refType = this.hibernateTemplate.get(
					CDEventRefTypeDO.class, refTypeCode.trim());

			if (refType != null)
				return refType.getRefTypeDescription();
			else
				return null;
		} else {
			return null;
		}

	}

	public String getPoundName(Integer poundID)

	{
		if (poundID != null) {

			PoundDO pound = this.hibernateTemplate.get(PoundDO.class, poundID);

			if (pound != null)
				return pound.getPoundName();
			else
				return null;
		} else {
			return null;
		}

	}

	@Override
	public CourtScheduleReportBO courtScheduleReport(
			CourtScheduleCriteriaBO reportCriteria, String userName,
			String userRegion,
			ReportDisplayInformationDAOImpl reportDisplayInformation) {

		Criteria criteria = this.hibernateTemplate.getSessionFactory()
				.getCurrentSession()
				.createCriteria(CourtAppearanceDO.class, "CApp");

		/* Create aliases */
		criteria.createAlias("CApp.status", "st", Criteria.LEFT_JOIN);
		criteria.createAlias("CApp.court", "c", Criteria.LEFT_JOIN);
		criteria.createAlias("CApp.courtCase", "CCase", Criteria.LEFT_JOIN);
		criteria.createAlias("CCase.summons", "su", Criteria.LEFT_JOIN);
		criteria.createAlias("su.offender", "o", Criteria.LEFT_JOIN);
		criteria.createAlias("c.address", "courtAddress", Criteria.LEFT_JOIN);
		criteria.createAlias("courtAddress.parish", "courtParish", Criteria.LEFT_JOIN);
		criteria.createAlias(
				"su.roadCheckOffenceOutcome.roadCheckOffence.roadCheck.compliance.roadOperation",
				"r", Criteria.LEFT_JOIN);
		criteria.createAlias("su.taStaff", "tas", Criteria.LEFT_JOIN);
		criteria.createAlias("tas.person", "taPerson", Criteria.LEFT_JOIN);
		criteria.createAlias(
				"su.roadCheckOffenceOutcome.roadCheckOffence.offence",
				"off");
		/* _____________________________ */

		
		
		Criterion mainCriterion = Restrictions.between("CApp.courtDTime",
				DateUtils.searchDateFormater(
						reportCriteria.getTrialStartDate(),
						DateUtils.SEARCHDATETYPE.START), DateUtils
						.searchDateFormater(reportCriteria.getTrialEndDate(),
								DateUtils.SEARCHDATETYPE.END));

		Criterion subCriterion = Restrictions.eq("st.statusId",
				fsl.ta.toms.roms.constants.Constants.Status.COURT_CASE_OPEN)
				.ignoreCase();

		if (reportCriteria.getCourtId() != null
				&& reportCriteria.getCourtId() > 0) {
			subCriterion = Restrictions.and(subCriterion,
					Restrictions.eq("c.courtId", reportCriteria.getCourtId()));
		}

		if (reportCriteria.getOffenderId() != null
				&& reportCriteria.getOffenderId() > 0) {
			subCriterion = Restrictions.and(
					subCriterion,
					Restrictions.eq("o.personId",
							reportCriteria.getOffenderId()));
		}

		if (StringUtil.isSet(reportCriteria.getOffenderTRN())) {
			subCriterion = Restrictions.and(subCriterion, Restrictions.eq(
					"o.trnNbr", reportCriteria.getOffenderTRN().trim()));
		}

		if (StringUtil.isSet(reportCriteria.getOffenderFirstName())) {
			subCriterion = Restrictions.and(
					subCriterion,
					Restrictions.like("o.firstName",
							reportCriteria.getOffenderFirstName().trim(),
							MatchMode.ANYWHERE).ignoreCase());
		}

		if (StringUtil.isSet(reportCriteria.getOffenderLastName())) {
			subCriterion = Restrictions.and(
					subCriterion,
					Restrictions.like("o.lastName",
							reportCriteria.getOffenderLastName().trim(),
							MatchMode.ANYWHERE).ignoreCase());
		}

		if (reportCriteria.getRoadOperationId() != null
				&& reportCriteria.getRoadOperationId() > 0) {
			subCriterion = Restrictions.and(
					subCriterion,
					Restrictions.eq("r.roadOperationId",
							reportCriteria.getRoadOperationId()));
		}

//		if (reportCriteria.getTAOfficeRegions() != null
//				&& reportCriteria.getTAOfficeRegions().size() > 0) {
//			subCriterion = Restrictions.and(
//					subCriterion,
//					Restrictions.in("r.officeLocCode",
//							reportCriteria.getTAOfficeRegions()));
			
		/*Region should be filtered by the court appearances*/
			if (reportCriteria.getTAOfficeRegions() != null
					&& reportCriteria.getTAOfficeRegions().size() > 0) {
				subCriterion = Restrictions.and(
						subCriterion,
						Restrictions.in("courtParish.officeLocationCode",
								reportCriteria.getTAOfficeRegions()));
		}

		if (StringUtil.isSet(reportCriteria.getTAStaffId())) {
			subCriterion = Restrictions.and(subCriterion, Restrictions.eq(
					"tas.staffId", reportCriteria.getTAStaffId().trim()));
		}

		if (StringUtil.isSet(reportCriteria.getTAStaffTRN())) {
			subCriterion = Restrictions.and(subCriterion, Restrictions.eq(
					"taPerson.trnNbr", reportCriteria.getTAStaffTRN().trim()));
		}

		if (StringUtil.isSet(reportCriteria.getRoadOperationName())) {
			subCriterion = Restrictions.and(
					subCriterion,
					Restrictions.like("r.operationName",
							reportCriteria.getRoadOperationName().trim(),
							MatchMode.ANYWHERE).ignoreCase());
		}

		criteria.add(Restrictions.and(mainCriterion, subCriterion));

		criteria.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);

		
		//criteria.addOrder(Order.asc("r.officeLocCode"));
		criteria.addOrder(Order.asc("courtParish.officeLocationCode").ignoreCase());
		criteria.addOrder(Order.asc("c.shortDesc").ignoreCase());
		criteria.addOrder(Order.asc("CApp.courtDTime").ignoreCase());
		criteria.addOrder(Order.asc("taPerson.lastName").ignoreCase());
		criteria.addOrder(Order.asc("su.offenceDtime").ignoreCase()); 
		criteria.addOrder(Order.asc("o.lastName").ignoreCase());
		//criteria.addOrder(Order.asc("o.firstName").ignoreCase());
		//criteria.addOrder(Order.asc("off.description").ignoreCase());
		//criteria.addOrder(Order.asc("r.operationName").ignoreCase());
		
		
		List criteriaResults = criteria.list();

		Iterator iterator = criteriaResults.iterator();

		List<CourtScheduleReportResultsBO> results = new ArrayList<CourtScheduleReportResultsBO>();

		String stringStartDate = "";
		
		String stringEndDate = "";
		try {
			stringStartDate = DateUtils.getFormattedUtilDate(reportCriteria
					.getTrialStartDate());
			stringEndDate = DateUtils.getFormattedUtilDate(reportCriteria
					.getTrialEndDate());
		} catch (Exception exe) {

		}

		/* Get report criteria names and descriptions */
		reportCriteria.setTAOfficeDescription(this
				.getTAOfficeRegionDescription(userRegion));
		reportCriteria.setCourtDescription(this
				.getCourtDescription(reportCriteria.getCourtId()));

		if (reportCriteria.getOffenderId() != null
				&& reportCriteria.getOffenderId() > 0)
			reportCriteria.setOffenderFullName(this
					.getPersonName(reportCriteria.getOffenderId()));
		else if (StringUtil.isSet(reportCriteria.getOffenderTRN())) {
			reportCriteria.setOffenderFullName(this
					.getPersonName(reportCriteria.getOffenderTRN()));
		}

		reportCriteria.setTAOfficeDescription(this
				.getTAOfficeRegionDescription(userRegion));

		if (StringUtil.isSet(reportCriteria.getTAStaffId()))
			reportCriteria.setTAStaffFullName(this
					.getTAStaffName(reportCriteria.getTAStaffId()));
		else if (StringUtil.isSet(reportCriteria.getTAStaffTRN())) {
			reportCriteria.setTAStaffFullName(this.getPersonName(reportCriteria
					.getTAStaffTRN()));
		}

		while (iterator.hasNext()) {
			Map result = (Map) iterator.next();

			/* Map Results to DOs */
			CourtAppearanceDO courtApp = (CourtAppearanceDO) result.get("CApp");
			CourtCaseDO courtCase = (CourtCaseDO) result.get("CCase");
			StatusDO status = (StatusDO) result.get("st");
			CourtDO court = (CourtDO) result.get("c");
			SummonsDO summons = (SummonsDO) result.get("su");
			PersonDO offender = (PersonDO) result.get("o");
			RoadOperationDO roadOp = (RoadOperationDO) result.get("r");
			TAStaffDO taStaff = (TAStaffDO) result.get("tas");
			ParishDO courtParish = (ParishDO)result.get("courtParish");
			/* ____________________________ */

			CourtScheduleReportResultsBO courtSchedule = new CourtScheduleReportResultsBO(
					NameUtil.getName(offender.getFirstName(),
							offender.getLastName())/* offender Name */,
					NameUtil.getName(taStaff.getPerson().getFirstName(),
							taStaff.getPerson().getLastName())/* tAStaffFullName */,
					roadOp != null ? roadOp.getOperationName() : "Unscheduled Operation"/* roadOperationName */,
					summons.getRoadCheckOffenceOutcome().getRoadCheckOffence()
							.getOffence().getDescription()/* offenceDetails */,
					courtParish != null ? courtParish.getOfficeLocationCode() : ""/* tAOfficeRegion */,
					this.getTAOfficeRegionDescription(court != null ? court: null)/* tAOfficeRegionDescription */,
					courtCase.getStatus().getDescription()/* Court Case Status */,
					courtApp.getStatus().getDescription()/*
														 * Court Appearance
														 * Status
														 */, summons
							.getOffenceDtime()/* offenceDate */, courtApp
							.getCourtDTime()/* courtDate */, new CourtBO(court)/* court */);

			results.add(courtSchedule);

		}

		return new CourtScheduleReportBO(
				userName,
				userRegion/* region */,
				reportDisplayInformation.applicationName/* applicationName */,
				reportDisplayInformation.courtScheduleReportTitle
						+ stringStartDate + " TO " + stringEndDate/* reportName */,
				reportCriteria.getTrialStartDate()/* startDate */,
				reportCriteria.getTrialEndDate()/* endDate */,
				reportCriteria.getSearchCriteriaString()/* searchCriteria */,
				this.getTAOfficeRegionDescription(userRegion)/* regionDescription */,
				results);
	}

	@SuppressWarnings("unchecked")
	@Override
	public RoadOperationsStatisticsReportBO performanceSaisticsReport(
			PerformanceStatisticsReportCriteriaBO reportCriteria,
			String userName, String userRegion,
			ReportDisplayInformationDAOImpl reportDisplayInformation) {
		/* Specify search criteria for report */
		Criteria criteria = this.hibernateTemplate.getSessionFactory()
				.getCurrentSession()
				.createCriteria(RoadOperationDO.class, "roadOp");

		/* List of all aliases used */
		criteria.createAlias("roadOp.category", "category");

		/* _______________________ */

		/* Apply filters to search results */
		Criterion mainCriteron = Restrictions.or(Restrictions.between(
				"roadOp.scheduledStartDtime", DateUtils.searchDateFormater(
						reportCriteria.getStartDate(),
						DateUtils.SEARCHDATETYPE.START), DateUtils
						.searchDateFormater(reportCriteria.getEndDate(),
								DateUtils.SEARCHDATETYPE.END)), Restrictions
				.between("roadOp.scheduledEndDtime", DateUtils
						.searchDateFormater(reportCriteria.getStartDate(),
								DateUtils.SEARCHDATETYPE.START), DateUtils
						.searchDateFormater(reportCriteria.getEndDate(),
								DateUtils.SEARCHDATETYPE.END)));

		if (StringUtil.isSet(reportCriteria.getTAOfficeRegion())) {
			mainCriteron = Restrictions.and(mainCriteron, Restrictions.eq(
					"roadOp.officeLocCode", reportCriteria.getTAOfficeRegion()
							.trim()));
		}

		if (StringUtil.isSet(reportCriteria.getOperationCategory())) {
			mainCriteron = Restrictions.and(mainCriteron, Restrictions.eq(
					"category.categoryId", reportCriteria
							.getOperationCategory().trim()));
		}

		if (reportCriteria.getTeamLeadId() != null
				&& !reportCriteria.getTeamLeadId().isEmpty()) {
			List<Integer> roadOpIds = this
					.getListOfRoadOpIdsBasedOnTeamLead(reportCriteria
							.getTeamLeadId());

			if (roadOpIds != null)
				mainCriteron = Restrictions.and(mainCriteron,
						Restrictions.in("roadOp.roadOperationId", roadOpIds));
		}

		if (StringUtil.isSet(reportCriteria.getTeamLeadTRN())) {
			List<Integer> roadOpIds = this
					.getListOfRoadOpIdsBasedOnTeamLeadTRN(reportCriteria
							.getTeamLeadTRN());

			if (roadOpIds != null)
				mainCriteron = Restrictions.and(mainCriteron,
						Restrictions.in("roadOp.roadOperationId", roadOpIds));
		}

		if (StringUtil.isSet(reportCriteria.getRoadOperationName())) {
			mainCriteron = Restrictions.and(
					mainCriteron,
					Restrictions.like("roadOp.operationName",
							reportCriteria.getRoadOperationName(),
							MatchMode.ANYWHERE).ignoreCase());
		}

		if (reportCriteria.getRoadOperationId() != null
				&& reportCriteria.getRoadOperationId() > 0) {
			mainCriteron = Restrictions.and(
					mainCriteron,
					Restrictions.eq("roadOp.roadOperationId",
							reportCriteria.getRoadOperationId()));
		}

		if (StringUtil.isSet(reportCriteria.getTAStaffId())
				|| StringUtil.isSet(reportCriteria.getTAStaffTRN())) {
			Criteria criteriaTA = this.hibernateTemplate.getSessionFactory()
					.getCurrentSession().createCriteria(TAStaffDO.class, "ta");

			criteriaTA.createAlias("ta.person", "taPerson");

			if (StringUtil.isSet(reportCriteria.getTAStaffId()))
				criteriaTA.add(Restrictions.eq("ta.staffId", reportCriteria
						.getTAStaffId().trim()));

			if (StringUtil.isSet(reportCriteria.getTAStaffTRN()))
				criteriaTA.add(Restrictions.eq("taPerson.trnNbr",
						reportCriteria.getTAStaffTRN().trim()));

			TAStaffDO taStaff = null;

			List<TAStaffDO> staffList = criteriaTA.list();

			if (!staffList.isEmpty())
				taStaff = staffList.get(0);

			if (taStaff != null) {

				/* Get a list of all assigned persons for a road operation. */
				Criteria criteriaAssignedPersons = this.hibernateTemplate
						.getSessionFactory().getCurrentSession()
						.createCriteria(AssignedPersonDO.class, "assignedP");

				criteriaAssignedPersons.add(Restrictions.sqlRestriction(
						"{alias}.person_id = ?", taStaff.getPerson()
								.getPersonId(), Hibernate.INTEGER));

				List<Integer> roadOpsWithTAStaff = new ArrayList<Integer>();

				for (AssignedPersonDO assignee : (List<AssignedPersonDO>) criteriaAssignedPersons
						.list()) {
					roadOpsWithTAStaff.add(assignee.getAssignedPersonKey()
							.getTeam().getRoadOperation().getRoadOperationId());
				}

				mainCriteron = Restrictions.and(mainCriteron, Restrictions.in(
						"roadOp.roadOperationId", roadOpsWithTAStaff));
			} else {
				return null;
			}
		}

		/* ______________________________ */

		/*** Check which road operations employ the strategies selected ****/
		if (reportCriteria.getStrategyIds() != null
				&& !reportCriteria.getStrategyIds().isEmpty()) {
			//
			Criteria criteriaStrategies = this.hibernateTemplate
					.getSessionFactory().getCurrentSession()
					.createCriteria(OperationStrategyDO.class, "opStrat");

			// criteriaStrategies.createAlias("opStrat.operationStrategyKey.roadOperation",
			// "roadOp");
			// criteriaStrategies.createAlias("opStrat.operationStrategyKey.strategy",
			// "strat");

			criteriaStrategies
					.setProjection(Projections.distinct(Projections
							.property("opStrat.operationStrategyKey.roadOperation.roadOperationId")));

			criteriaStrategies.add(Restrictions.in(
					"opStrat.operationStrategyKey.strategy.strategyId",
					reportCriteria.getStrategyIds()));

			List<Integer> roadOpWithStratsList = criteriaStrategies.list();

			if (roadOpWithStratsList != null && !roadOpWithStratsList.isEmpty()) {
				mainCriteron = Restrictions.and(mainCriteron, Restrictions.in(
						"roadOp.roadOperationId", roadOpWithStratsList));
			} else {
				return null;
			}
		}
		/* ______________________________________________________________ */

		/*
		 * Create Return objects which are going to be filled during report
		 * processing.
		 */

		String stringStartDate = "";
		String stringEndDate = "";
		try {
			stringStartDate = DateUtils.getFormattedUtilDate(reportCriteria
					.getStartDate());
			stringEndDate = DateUtils.getFormattedUtilDate(reportCriteria
					.getEndDate());
		} catch (Exception exe) {

		}

		/* Get report criteria names and descriptions */
		reportCriteria.setTAOfficeDescription(this
				.getTAOfficeRegionDescription(reportCriteria
						.getTAOfficeRegion()));
		reportCriteria
				.setOperationCategoryDescription(this
						.getOperationCategoryDesc(reportCriteria
								.getOperationCategory()));

		if (StringUtil.isSet(reportCriteria.getTAStaffId()))
			reportCriteria.setTAStaffName(this.getTAStaffName(reportCriteria
					.getTAStaffId()));
		else if (StringUtil.isSet(reportCriteria.getTAStaffTRN()))
			reportCriteria.setTAStaffName(this.getPersonName(reportCriteria
					.getTAStaffTRN()));

		if (StringUtil.isSet(reportCriteria.getTeamLeadId()))
			reportCriteria.setTeamLeadName(this.getTAStaffName(reportCriteria
					.getTeamLeadId()));
		else if (StringUtil.isSet(reportCriteria.getTeamLeadTRN()))
			reportCriteria.setTeamLeadName(this.getPersonName(reportCriteria
					.getTeamLeadTRN()));

		if (reportCriteria.getStrategyIds() != null
				&& !reportCriteria.getStrategyIds().isEmpty()) {
			StringBuilder strategyDescriptions = new StringBuilder("");

			for (Integer strategyId : reportCriteria.getStrategyIds()) {
				StrategyDO strategyDO = this.hibernateTemplate.get(
						StrategyDO.class, strategyId);

				if (strategyDO != null) {
					if (!strategyDescriptions.toString().isEmpty())
						strategyDescriptions.append(", ");

					strategyDescriptions.append(strategyDO.getDescription());
				}
			}

			reportCriteria.setStrategyDescriptions(strategyDescriptions
					.toString());
		}

		RoadOperationsStatisticsReportBO roadOpReportStatsOuput = new RoadOperationsStatisticsReportBO(
				userName, userRegion, reportDisplayInformation.applicationName,
				reportDisplayInformation.getPerformanceStatisticsReportTitle()
						+ stringStartDate + " TO " + stringEndDate,
				reportCriteria.getStartDate(), reportCriteria.getEndDate(),
				reportCriteria.getSearchCriteriaString(),
				this.getTAOfficeRegionDescription(userRegion));

		List<RegionStatisticsBO> regionStats = new ArrayList<RegionStatisticsBO>();

		RegionStatisticsBO currentRegionStats = null;
		OperationSummaryReportCriteriaBO reportCriteriaForRoadOps = null;

		roadOpReportStatsOuput.setRegionStatistics(regionStats);

		/* ____________________________________ */

		/* Loop through list of road operations and get statistics for persons. */
		criteria.add(mainCriteron);
		criteria.addOrder(Order.asc("roadOp.officeLocCode"));
		criteria.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);

		List criteriaList = criteria.list();

		Iterator iterator = criteriaList.iterator();

		while (iterator.hasNext()) {
			Map map = (Map) iterator.next();

			final RoadOperationDO roadOpDO = (RoadOperationDO) map
					.get("roadOp");
			// TAStaffDO teamLead = (TAStaffDO)map.get("teamLead");

			if (currentRegionStats == null) {

				/* Create List of RoadOperationSummaryBO */
				reportCriteriaForRoadOps = new OperationSummaryReportCriteriaBO(
						reportCriteria.getStartDate(),
						reportCriteria.getEndDate(), new ArrayList<String>() {
							{
								add(roadOpDO.getOfficeLocCode());

							}
						}, this.getTeamLeadIdsForRoadOp(roadOpDO
								.getRoadOperationId()), roadOpDO.getCategory()
								.getCategoryId(), roadOpDO.getRoadOperationId());

				currentRegionStats = new RegionStatisticsBO(
						roadOpDO.getOfficeLocCode(),
						this.getTAOfficeRegionDescription(roadOpDO
								.getOfficeLocCode()));

				currentRegionStats.setRoadOpSummary(this
						.operationSummaryReport(reportCriteriaForRoadOps,
								userName, userRegion, reportDisplayInformation)
						.getResults());

				List<RoadOperationSummaryResultsBO> roadOpSummary = this
						.operationSummaryReport(reportCriteriaForRoadOps,
								userName, userRegion, reportDisplayInformation)
						.getResults();

				currentRegionStats.setRoadOpSummary(roadOpSummary);

				currentRegionStats
						.setRoadOperationStatistics(new ArrayList<RoadOperationStatisticsBO>());

				roadOpReportStatsOuput.getRegionStatistics().add(
						currentRegionStats);

			} else if (!currentRegionStats.getOfficeLocCode().equalsIgnoreCase(
					roadOpDO.getOfficeLocCode())) {

				reportCriteriaForRoadOps = new OperationSummaryReportCriteriaBO(
						reportCriteria.getStartDate(),
						reportCriteria.getEndDate(), new ArrayList<String>() {
							{
								add(roadOpDO.getOfficeLocCode());

							}
						}, null, roadOpDO.getCategory().getCategoryId(), null);

				currentRegionStats = new RegionStatisticsBO(
						roadOpDO.getOfficeLocCode(),
						this.getTAOfficeRegionDescription(roadOpDO
								.getOfficeLocCode()));
				;

				currentRegionStats.setRoadOpSummary(this
						.operationSummaryReport(reportCriteriaForRoadOps,
								userName, userRegion, reportDisplayInformation)
						.getResults());

				List<RoadOperationSummaryResultsBO> roadOpSummary = this
						.operationSummaryReport(reportCriteriaForRoadOps,
								userName, userRegion, reportDisplayInformation)
						.getResults();

				currentRegionStats.setRoadOpSummary(roadOpSummary);

				currentRegionStats
						.setRoadOperationStatistics(new ArrayList<RoadOperationStatisticsBO>());

				roadOpReportStatsOuput.getRegionStatistics().add(
						currentRegionStats);
			}

			RoadOperationStatisticsBO roadOpStats = new RoadOperationStatisticsBO(
					getListOfTeamLeadNamesBasedOnRoadOpId(roadOpDO
							.getRoadOperationId()),
					roadOpDO.getOperationName());

			roadOpStats
					.setITAExaminerSummary(new ArrayList<ITAExaminerStatisticsBO>());
			roadOpStats.setJPSummary(new ArrayList<JPStatisticsBO>());
			roadOpStats
					.setPoliceOfficerSummary(new ArrayList<PoliceOfficerStatisticsBO>());
			roadOpStats
					.setTAOfficerSummary(new ArrayList<TAOfficerStatisticsBO>());
			/* Get a list of all assigned persons for a road operation. */
			Criteria criteriaAssignedPersons = this.hibernateTemplate
					.getSessionFactory().getCurrentSession()
					.createCriteria(AssignedPersonDO.class, "assignedP");

			// criteriaAssignedPersons.add(Restrictions.sqlRestriction("{alias}.road_operation_id = ?",
			// roadOpDO.getRoadOperationId(), Hibernate.INTEGER));

			List<Integer> teamIds = this.getTeamIdsForRoadOp(roadOpDO
					.getRoadOperationId());

			if (teamIds != null && teamIds.size() > 0)
				criteriaAssignedPersons.add(Restrictions.in(
						"assignedPersonKey.team.teamId", teamIds));

			if (StringUtil.isSet(reportCriteria.getTAStaffId())) {
				TAStaffDO taStaff = (TAStaffDO) this.hibernateTemplate
						.getSessionFactory()
						.getCurrentSession()
						.get(TAStaffDO.class,
								reportCriteria.getTAStaffId().trim());

				if (taStaff != null) {

					criteriaAssignedPersons.add(Restrictions.sqlRestriction(
							"{alias}.person_id = ?", taStaff.getPerson()
									.getPersonId(), Hibernate.INTEGER));
				} else {
					return null;
				}
			}

			for (AssignedPersonDO assignedPerson : (List<AssignedPersonDO>) criteriaAssignedPersons
					.list()) {
				/* Looping through a list of persons based on road operation id. */

				PersonDO person = assignedPerson.getAssignedPersonKey()
						.getPerson();
				CDPersonTypeDO personType = assignedPerson
						.getAssignedPersonKey().getPersonType();

				if (personType.getPersonTypeId().toLowerCase()
						.equalsIgnoreCase(Constants.PersonType.JP)) {
					/* Get Statistics for JP Person */
					Criteria criteriaJP = this.hibernateTemplate
							.getSessionFactory().getCurrentSession()
							.createCriteria(JPDO.class);
					criteriaJP.add(Restrictions.eq("person.personId",
							person.getPersonId()));

					JPDO jp = (JPDO) criteriaJP.uniqueResult();

					JPStatisticsBO jpStatsBO = new JPStatisticsBO(
							NameUtil.getName(person.getFirstName(),
									person.getLastName())/* fullName */,
							jp.getRegNumber()/* regNumber */,
							this.summonsCount(roadOpDO.getRoadOperationId(),
									person.getPersonId(),
									Constants.PersonType.JP)/* countSummonsSigned */,
							assignedPerson.getAttended()/* attended */);

					roadOpStats.getJPSummary().add(jpStatsBO);

				} else if (personType.getPersonTypeId().equalsIgnoreCase(
						Constants.PersonType.TA_STAFF)) {
					/* Get Statistics for TA Staff. */
					Criteria criteriaTA = this.hibernateTemplate
							.getSessionFactory().getCurrentSession()
							.createCriteria(TAStaffDO.class);
					criteriaTA.add(Restrictions.eq("person.personId",
							person.getPersonId()));
					//System.out.println("Person ID is " + person.getPersonId());
					TAStaffDO ta = (TAStaffDO) criteriaTA.uniqueResult();

					TAOfficerStatisticsBO taStatsBO = new TAOfficerStatisticsBO(
							NameUtil.getName(person.getFirstName(),
									person.getLastName())/* fullName */,
							ta.getStaffTypeCode()/* staffType */,
							this.complianceCount(roadOpDO.getRoadOperationId(),
									person.getPersonId(),
									personType.getPersonTypeId())/* countCompliancyChecks */,
							this.roadCheckTypeCount(
									roadOpDO.getRoadOperationId(),
									Constants.RoadCheckType.MOTOR_VEHICLE,
									person.getPersonId(),
									personType.getPersonTypeId())/* countMVChecks */,
							this.roadCheckTypeCount(
									roadOpDO.getRoadOperationId(),
									Constants.RoadCheckType.DRIVERS_LICENCE,
									person.getPersonId(),
									personType.getPersonTypeId())/* countDLChecks */,
							this.roadCheckTypeCount(
									roadOpDO.getRoadOperationId(),
									Constants.RoadCheckType.BADGE,
									person.getPersonId(),
									personType.getPersonTypeId())/* countBadgeChecks */,
							this.roadCheckTypeCount(
									roadOpDO.getRoadOperationId(),
									Constants.RoadCheckType.CITATION,
									person.getPersonId(),
									personType.getPersonTypeId())/* countCitationChecks */,
							this.roadCheckTypeCount(
									roadOpDO.getRoadOperationId(),
									Constants.RoadCheckType.ROAD_LICENCE,
									person.getPersonId(),
									personType.getPersonTypeId())/* countRLChecks */,
							this.roadCheckTypeCount(
									roadOpDO.getRoadOperationId(),
									Constants.RoadCheckType.OTHER,
									person.getPersonId(),
									personType.getPersonTypeId())/* countOtherChecks */,
							this.warningNoticeCount(
									roadOpDO.getRoadOperationId(),
									person.getPersonId(),
									personType.getPersonTypeId())/* countWarningNoticesIssued */,
							this.summonsCount(roadOpDO.getRoadOperationId(),
									person.getPersonId(),
									personType.getPersonTypeId())/* countSummonsIssued */,
							this.roadCheckOutcomeCount(
									roadOpDO.getRoadOperationId(),
									Constants.OutcomeType.VEHICLE_SEIZURE,
									person.getPersonId(),
									personType.getPersonTypeId())/* countVehiclesSeized */,
							assignedPerson.getAttended()/* attended */,
							this.roadCheckOutcomeCount(
									roadOpDO.getRoadOperationId(),
									Constants.OutcomeType.REMOVE_PLATES,
									person.getPersonId(),
									personType.getPersonTypeId())/* countPlatesRemoved */,
							this.roadCheckOutcomeCount(
									roadOpDO.getRoadOperationId(),
									Constants.OutcomeType.WARNED_FOR_PROSECUTION,
									person.getPersonId(),
									personType.getPersonTypeId())/* warningsForProcecution */,
							this.roadCheckOutcomeCount(
									roadOpDO.getRoadOperationId(),
									Constants.OutcomeType.ALL_IN_ORDER,
									person.getPersonId(),
									personType.getPersonTypeId())/* allInOrders */,
							ta.getStaffId()/* staff id */);

					roadOpStats.getTAOfficerSummary().add(taStatsBO);
				} else if (personType.getPersonTypeId().equalsIgnoreCase(
						Constants.PersonType.ITA_EXAMINER)) {
					/* Get Statistics for ITA Examiner */
					Criteria criteriaITA = this.hibernateTemplate
							.getSessionFactory().getCurrentSession()
							.createCriteria(ITAExaminerDO.class);
					criteriaITA.add(Restrictions.eq("person.personId",
							person.getPersonId()));

					ITAExaminerDO ita = (ITAExaminerDO) criteriaITA
							.uniqueResult();

					ITAExaminerStatisticsBO itaStats = new ITAExaminerStatisticsBO(
							NameUtil.getName(person.getFirstName(),
									person.getLastName()),
							assignedPerson.getAttended(), ita.getExaminerId());

					roadOpStats.getITAExaminerSummary().add(itaStats);
				} else if (personType.getPersonTypeId().equalsIgnoreCase(
						Constants.PersonType.POLICE_OFFCER)) {
					/* Get Statistics for Police Officer */
					Criteria criteriaITA = this.hibernateTemplate
							.getSessionFactory().getCurrentSession()
							.createCriteria(PoliceOfficerDO.class);
					criteriaITA.add(Restrictions.eq("person.personId",
							person.getPersonId()));

					PoliceOfficerDO police = (PoliceOfficerDO) criteriaITA
							.uniqueResult();

					PoliceOfficerStatisticsBO policeStats = new PoliceOfficerStatisticsBO(
							NameUtil.getName(person.getFirstName(),
									person.getLastName()),
							assignedPerson.getAttended(),
							police.getPolOfficerCompNo());

					roadOpStats.getPoliceOfficerSummary().add(policeStats);
				}

			}
			/* ____________________________________ */

			currentRegionStats.getRoadOperationStatistics().add(roadOpStats);
		}
		/* ________________________________ */

		roadOpReportStatsOuput.setRegionStatistics(regionStats);
		return roadOpReportStatsOuput;
	}

	/**
	 * This method returns a list of team lead names based on the road operation
	 * id.
	 * 
	 * @param roadOpId
	 * @return
	 */
	private String getListOfTeamLeadNamesBasedOnRoadOpId(Integer roadOpId) {
		if (roadOpId != null && roadOpId > 0) {
			List<String> teamLeadIds = getTeamLeadIdsForRoadOp(roadOpId);

			if (teamLeadIds != null) {
				StringBuilder teamLeadNames = new StringBuilder("");

				for (String staffId : teamLeadIds) {
					if (StringUtil.isSet(teamLeadNames.toString()))
						teamLeadNames.append("; ");

					TAStaffDO staff = this.hibernateTemplate.load(
							TAStaffDO.class, staffId);
					teamLeadNames.append(NameUtil.getName(staff.getPerson()
							.getFirstName(), staff.getPerson().getLastName()));

				}

				return teamLeadNames.toString();
			} else
				return null;
		} else
			return null;
	}

	/**
	 * This is a convenience function which gets a list of road operation ids
	 * which the ta staff was involved in.
	 * 
	 * @param taStaffId
	 * @return
	 */
	private List<Integer> getListOfRoadOpIdsBasedOnTeamLead(String taStaffId) {
		if (StringUtil.isSet(taStaffId)) {
			Criteria criteria = this.hibernateTemplate.getSessionFactory()
					.getCurrentSession().createCriteria(TeamDO.class, "team");

			criteria.add(Restrictions.eq("team.teamLead.staffId",
					taStaffId.trim()).ignoreCase());

			criteria.setProjection(Projections
					.property("team.roadOperation.roadOperationId"));

			return criteria.list();
		} else
			return null;
	}

	private List<Integer> getListOfRoadOpIdsBasedOnTeamLeadTRN(String taStaffTRN) {
		if (StringUtil.isSet(taStaffTRN)) {
			Criteria criteriaTRN = this.hibernateTemplate.getSessionFactory()
					.getCurrentSession()
					.createCriteria(TAStaffDO.class, "staff");

			criteriaTRN.add(Restrictions.eq("staff.person.trnNbr",
					taStaffTRN.trim()).ignoreCase());

			criteriaTRN.setProjection(Projections.property("staff.staffId"));

			String staffId = (String) criteriaTRN.uniqueResult();

			if (StringUtil.isSet(staffId)) {
				Criteria criteria = this.hibernateTemplate.getSessionFactory()
						.getCurrentSession()
						.createCriteria(TeamDO.class, "team");

				criteria.add(Restrictions.eq("team.teamLead.staffId",
						staffId.trim()).ignoreCase());

				criteria.setProjection(Projections
						.property("team.roadOperation.roadOperationId"));

				return criteria.list();
			} else
				return null;
		} else
			return null;
	}

	@Override
	public EventAuditReportBO eventAuditeport(
			EventAuditReportCriteriaBO reportCriteria, String userName,
			String userRegion,
			ReportDisplayInformationDAOImpl reportDisplayInformation) {
		// StringBuilder hqlQuery = new
		// StringBuilder("select eAudit,event,refType1,refType2,userView,locView "
		// +
		// " from EventAuditDO eAudit, CDEventDO event, CDEventRefTypeDO refType1, CDEventRefTypeDO refType2, LMIS_UserViewDO userView, LMIS_TAOfficeLocationViewDO locView "
		// +
		// " where eAudit.event.eventCode = event.eventCode and ((eAudit.refType1.refTypeCode is null) or (eAudit.refType1.refTypeCode = refType1.refTypeCode))"
		// +
		// " and ((eAudit.refType2.refTypeCode is null) or (eAudit.refType2.refTypeCode = refType2.refTypeCode)) and lower(userView.username) = lower(eAudit.auditEntry.createUsername) "
		// +
		// " and userView.locationCode = locView.locationCode ");

//		StringBuilder hqlQuery = new StringBuilder(
//				"select eAudit,event,eAudit.refType1,eAudit.refType2,userView,locView "
//						+ " from  LMIS_UserViewDO userView, LMIS_TAOfficeLocationViewDO locView,CDEventDO event,EventAuditDO eAudit "
//						+ " LEFT JOIN CDEventRefTypeDO refType1 WITH eAudit.refType1.refTypeCode = refType1.refTypeCode "
//						+ " LEFT JOIN CDEventRefTypeDO refType2 WITH eAudit.refType2.refTypeCode = refType2.refTypeCode "
//						+ " where eAudit.event.eventCode = event.eventCode"
//						+ " and lower(userView.username) = lower(eAudit.auditEntry.createUsername) "
//						+ " and userView.locationCode = locView.locationCode ");

		StringBuilder hqlQuery = new StringBuilder(
				"select eAudit,event,eAudit.refType1,eAudit.refType2,userView,locView "
						+ " from  LMIS_UserViewDO userView, LMIS_TAOfficeLocationViewDO locView,CDEventDO event,EventAuditDO eAudit " 
						+ " LEFT JOIN eAudit.refType1 LEFT JOIN eAudit.refType2"
						+ " where eAudit.event.eventCode = event.eventCode"
						+ " and lower(userView.username) = lower(eAudit.auditEntry.createUsername) "
						+ " and userView.locationCode = locView.locationCode ");
		
		List<String> paramNames = new ArrayList<String>();

		List<Object> paramValues = new ArrayList<Object>();

		reportCriteria.setStartDate(DateUtils.searchDateFormater(
				reportCriteria.getStartDate(), DateUtils.SEARCHDATETYPE.START));
		reportCriteria.setEndDate(DateUtils.searchDateFormater(
				reportCriteria.getEndDate(), DateUtils.SEARCHDATETYPE.END));

		/* Mandatory Filters Start Here */
		hqlQuery.append(" and eAudit.auditEntry.createDTime BETWEEN :startDate and :endDate");
		paramNames.add("startDate");
		paramValues.add(reportCriteria.getStartDate());

		paramNames.add("endDate");
		paramValues.add(reportCriteria.getEndDate());

		/* Optional Filers start here */
		if (StringUtil.isSet(reportCriteria.getCreateUserName())) {
			hqlQuery.append(" and lower(eAudit.auditEntry.createUsername) = lower(:createUserName)");

			paramNames.add("createUserName");
			paramValues.add(reportCriteria.getCreateUserName().trim());

		}

		if (reportCriteria.getEventCode() != null
				&& reportCriteria.getEventCode() > 0) {
			hqlQuery.append(" and event.eventCode = :eventCode");

			paramNames.add("eventCode");
			paramValues.add(reportCriteria.getEventCode());

		}

		if (StringUtil.isSet(reportCriteria.getRefType())) {
			hqlQuery.append(" and (lower(refType1.refTypeCode) = lower(:refTypeCode) or  lower(refType2.refTypeCode) = lower(:refTypeCode)) ");

			paramNames.add("refTypeCode");
			paramValues.add(reportCriteria.getRefType().trim());

		}

		if (reportCriteria.getTAOfficeRegion() != null
				&& reportCriteria.getTAOfficeRegion().size() > 0) {
			hqlQuery.append(" and userView.locationCode in (:officeLocCode)");

			paramNames.add("officeLocCode");
			paramValues.add(reportCriteria.getTAOfficeRegion());

		}
		
		//Added Mar 28. - Search based on a particular LMIS username
		if (StringUtil.isSet(reportCriteria.getCreateUserName()))
		{
			hqlQuery.append(" and lower(userView.username) = lower('" + reportCriteria.getCreateUserName() + "')");
		}
		
		if (StringUtil.isSet(reportCriteria.getSortBy())) {
			if (reportCriteria.getSortBy().trim().equalsIgnoreCase("user")) {
				/*hqlQuery.append(" order by eAudit.auditEntry.createUsername");*/
				hqlQuery.append(" order by userView.lastName, userView.firstName");
				reportCriteria.setSortByDesc("User");
			} else if (reportCriteria.getSortBy().trim()
					.equalsIgnoreCase("eventdtime")) {
				hqlQuery.append(" order by eAudit.auditEntry.createDTime");
				reportCriteria.setSortByDesc("Event Date");
			} else if (reportCriteria.getSortBy().trim()
					.equalsIgnoreCase("location")) {
				hqlQuery.append(" order by locView.locationDesc");
				reportCriteria.setSortByDesc("Location");
			} else if (reportCriteria.getSortBy().trim()
					.equalsIgnoreCase("event")) {
				hqlQuery.append(" order by event.eventDescription");
				reportCriteria.setSortByDesc("Event");
			}
		} else {
			hqlQuery.append(" order by eAudit.auditEntry.createDTime");

			reportCriteria.setSortByDesc("Event Date");
		}

		String[] paramNameArray = new String[paramNames.size()];
		paramNames.toArray(paramNameArray);

		Object[] paramValuesArray = new Object[paramValues.size()];
		paramValues.toArray(paramValuesArray);

		List query = this.hibernateTemplate.findByNamedParam(
				hqlQuery.toString(), paramNameArray, paramValuesArray);

		Iterator iterator = query.iterator();

		List<EventAuditReportResultsBO> eventAuditList = new ArrayList<EventAuditReportResultsBO>();

		String stringStartDate = "";
		String stringEndDate = "";
		try {
			stringStartDate = DateUtils.getFormattedUtilDate(reportCriteria
					.getStartDate());
			stringEndDate = DateUtils.getFormattedUtilDate(reportCriteria
					.getEndDate());
		} catch (Exception exe) {

		}

		/* Get report criteria names and descriptions */
		reportCriteria.setTAOfficeDescription(userRegion);
		reportCriteria.setEventDescription(this.getEventDesc(reportCriteria
				.getEventCode()));
		reportCriteria.setRefTypeDesc(this.getRefTypeDesc(reportCriteria
				.getRefType()));
		reportCriteria.setUserFullName(this.getUserName(reportCriteria
				.getCreateUserName()));

		while (iterator.hasNext()) {
			Object[] objArray = (Object[]) iterator.next();

			EventAuditDO eventAudit = (EventAuditDO) objArray[0];

			CDEventDO eventType = (CDEventDO) objArray[1];

			CDEventRefTypeDO eventRefType1 = (CDEventRefTypeDO) objArray[2];

			CDEventRefTypeDO eventRefType2 = (CDEventRefTypeDO) objArray[3];

			LMIS_UserViewDO userView = (LMIS_UserViewDO) objArray[4];

			LMIS_TAOfficeLocationViewDO locView = (LMIS_TAOfficeLocationViewDO) objArray[5];

			
			EventAuditReportResultsBO results = new EventAuditReportResultsBO(
					eventAudit.getRefValue1()/* refType1Value */,
					eventRefType1 != null ? eventRefType1
							.getRefTypeDescription() : ""/* refType1Desc */,
					eventRefType1 != null ? eventRefType1.getRefLabel() : ""/* refType1Label */,
					eventAudit.getRefValue2()/* refType2Value */,
					eventRefType2 != null ? eventRefType2
							.getRefTypeDescription() : ""/* refType2Desc */,
					eventRefType2 != null ? eventRefType2.getRefLabel() : ""/* refType2Label */,
					eventAudit.getComment(),
					eventType.getEventDescription()/* eventDesc */,
					DateUtils.formatDate("yyyy-MM-dd h:mm a", eventAudit
							.getAuditEntry().getCreateDTime())/* eventDate */,
					eventAudit.getAuditEntry().getCreateUsername()/*
																 * Create User
																 * Name
																 */,
					locView != null ? locView.getLocationDesc() : "", this
							.getUserName(eventAudit.getAuditEntry()
									.getCreateUsername()));

			eventAuditList.add(results);
		}

		return new EventAuditReportBO(userName, userRegion,
				reportDisplayInformation.applicationName,
				reportDisplayInformation.getEventAuditReportTitle()
						+ stringStartDate + " TO " + stringEndDate,
				reportCriteria.getStartDate(), reportCriteria.getEndDate(),
				reportCriteria.getSearchCriteriaString(),
				this.getTAOfficeRegionDescription(userRegion), eventAuditList);
	}

	@Override
	public CourtCasesOpenedReportBO unclosedCourtCasesReport(
			CourtScheduleCriteriaBO reportCriteria, String userName,
			String userRegion,
			ReportDisplayInformationDAOImpl reportDisplayInformation) {

		Criteria criteria = this.hibernateTemplate.getSessionFactory()
				.getCurrentSession()
				.createCriteria(CourtAppearanceDO.class, "CApp");

		/* Create aliases */
		criteria.createAlias("CApp.status", "st");
		criteria.createAlias("CApp.court", "c");
		criteria.createAlias("CApp.courtCase", "CCase");
		criteria.createAlias("CCase.summons", "su");
		criteria.createAlias("su.offender", "o");
		criteria.createAlias("su.roadOperation", "r");
		criteria.createAlias("su.taStaff", "tas");
		criteria.createAlias("tas.person", "taPerson");
		/* _____________________________ */

		/*
		 * Criterion mainCriterion =
		 * Restrictions.between("CCase.auditEntry.createDTime",
		 * DateUtils.searchDateFormater
		 * (reportCriteria.getTrialStartDate(),DateUtils.SEARCHDATETYPE.START),
		 * DateUtils
		 * .searchDateFormater(reportCriteria.getTrialEndDate(),DateUtils
		 * .SEARCHDATETYPE.END));
		 */

		Criterion mainCriterion = Restrictions.in(
				"CCase.courtCaseId",
				getCourtCasesIdsForTrialDateRange(
						reportCriteria.getTrialStartDate(),
						reportCriteria.getTrialEndDate()));

		Criterion subCriterion = Restrictions.eq("CCase.status.statusId",
				fsl.ta.toms.roms.constants.Constants.Status.COURT_CASE_OPEN)
				.ignoreCase();

		if (reportCriteria.getCourtId() != null
				&& reportCriteria.getCourtId() > 0) {
			subCriterion = Restrictions.and(
					subCriterion,
					Restrictions.eq("CCase.court.courtId",
							reportCriteria.getCourtId()));
		}

		if (reportCriteria.getOffenderId() != null
				&& reportCriteria.getOffenderId() > 0) {
			subCriterion = Restrictions.and(
					subCriterion,
					Restrictions.eq("o.personId",
							reportCriteria.getOffenderId()));
		}

		if (reportCriteria.getRoadOperationId() != null
				&& reportCriteria.getRoadOperationId() > 0) {
			subCriterion = Restrictions.and(
					subCriterion,
					Restrictions.eq("r.roadOperationId",
							reportCriteria.getRoadOperationId()));
		}

		if (reportCriteria.getTAOfficeRegions() != null
				&& reportCriteria.getTAOfficeRegions().size() > 0) {
			subCriterion = Restrictions.and(
					subCriterion,
					Restrictions.in("r.officeLocCode",
							reportCriteria.getTAOfficeRegions()));
		}

		if (reportCriteria.getTAStaffId() != null
				&& !reportCriteria.getTAStaffId().isEmpty()) {
			subCriterion = Restrictions.and(subCriterion, Restrictions.eq(
					"tas.staffId", reportCriteria.getTAStaffId().trim()));
		}

		if (StringUtil.isSet(reportCriteria.getTAStaffTRN())) {
			subCriterion = Restrictions.and(subCriterion, Restrictions.eq(
					"taPerson.trnNbr", reportCriteria.getTAStaffTRN().trim()));
		}

		if (StringUtil.isSet(reportCriteria.getRoadOperationName())) {
			subCriterion = Restrictions.and(
					subCriterion,
					Restrictions.like("r.operationName",
							reportCriteria.getRoadOperationName().trim(),
							MatchMode.ANYWHERE).ignoreCase());
		}

		if (StringUtil.isSet(reportCriteria.getOffenderFirstName())) {
			subCriterion = Restrictions.and(
					subCriterion,
					Restrictions.like("o.firstName",
							reportCriteria.getOffenderFirstName().trim(),
							MatchMode.ANYWHERE).ignoreCase());
		}

		if (StringUtil.isSet(reportCriteria.getOffenderLastName())) {
			subCriterion = Restrictions.and(
					subCriterion,
					Restrictions.like("o.lastName",
							reportCriteria.getOffenderLastName().trim(),
							MatchMode.ANYWHERE).ignoreCase());
		}

		if (StringUtil.isSet(reportCriteria.getOffenderTRN())) {
			subCriterion = Restrictions.and(subCriterion, Restrictions.eq(
					"o.trnNbr", reportCriteria.getOffenderTRN().trim()));
		}

		criteria.add(Restrictions.and(mainCriterion, subCriterion));

		criteria.addOrder(Order.asc("CCase.courtCaseId"));
		criteria.addOrder(Order.asc("CApp.CourtDTime"));

		criteria.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);

		List criteriaResults = criteria.list();

		Iterator iterator = criteriaResults.iterator();

		List<CourtScheduleReportResultsBO> results = new ArrayList<CourtScheduleReportResultsBO>();

		String stringStartDate = "";
		String stringEndDate = "";
		try {
			stringStartDate = DateUtils.getFormattedUtilDate(reportCriteria
					.getTrialStartDate());
			stringEndDate = DateUtils.getFormattedUtilDate(reportCriteria
					.getTrialEndDate());
		} catch (Exception exe) {

		}

		/* Get report criteria names and descriptions */
		reportCriteria.setTAOfficeDescription(this
				.getTAOfficeRegionDescription(userRegion));
		reportCriteria.setCourtDescription(this
				.getCourtDescription(reportCriteria.getCourtId()));

		if (reportCriteria.getOffenderId() != null
				&& reportCriteria.getOffenderId() > 0)
			reportCriteria.setOffenderFullName(this
					.getPersonName(reportCriteria.getOffenderId()));
		else if (StringUtil.isSet(reportCriteria.getOffenderTRN()))
			reportCriteria.setOffenderFullName(this
					.getPersonName(reportCriteria.getOffenderTRN()));

		reportCriteria.setTAOfficeDescription(this
				.getTAOfficeRegionDescription(userRegion));

		if (StringUtil.isSet(reportCriteria.getTAStaffId()))
			reportCriteria.setTAStaffFullName(this
					.getTAStaffName(reportCriteria.getTAStaffId()));
		else if (StringUtil.isSet(reportCriteria.getTAStaffTRN()))
			reportCriteria.setTAStaffFullName(this
					.getTAStaffName(reportCriteria.getTAStaffTRN()));

		List<CourtCasesOpenedReportResultsBO> courtCaseList = new ArrayList<CourtCasesOpenedReportResultsBO>();

		CourtCasesOpenedReportResultsBO courtCaseResult = null;

		CourtCasesOpenedReportBO courtCaseReportResults = new CourtCasesOpenedReportBO(
				userName, userRegion, reportDisplayInformation.applicationName,
				reportDisplayInformation.getCourtCasesOpenedReportTitle()
						+ stringStartDate + " TO " + stringEndDate,
				reportCriteria.getTrialStartDate(),
				reportCriteria.getTrialEndDate(),
				reportCriteria.getSearchCriteriaString(),
				this.getTAOfficeRegionDescription(userRegion), courtCaseList);

		Integer currentCourtCaseID = -1;

		while (iterator.hasNext()) {
			Map result = (Map) iterator.next();

			/* Map Results to DOs */
			CourtAppearanceDO courtApp = (CourtAppearanceDO) result.get("CApp");
			CourtCaseDO courtCase = (CourtCaseDO) result.get("CCase");
			StatusDO status = (StatusDO) result.get("st");
			CourtDO court = (CourtDO) result.get("c");
			SummonsDO summons = (SummonsDO) result.get("su");
			PersonDO offender = (PersonDO) result.get("o");
			RoadOperationDO roadOp = (RoadOperationDO) result.get("r");
			TAStaffDO taStaff = (TAStaffDO) result.get("tas");
			/* ____________________________ */

			if (courtCaseResult == null) {
				courtCaseResult = new CourtCasesOpenedReportResultsBO(
						this.getPersonName(offender.getPersonId())/* offenderFullName */,
						this.getTAStaffName(taStaff.getStaffId())/* tAStaffFullName */,
						roadOp.getOperationName()/* roadOperationName */,
						summons.getRoadCheckOffenceOutcome()
								.getRoadCheckOffence().getOffence()
								.getDescription()/* offenceDetails */,
						roadOp.getOfficeLocCode()/* tAOfficeRegion */,
						this.getTAOfficeRegionDescription(roadOp
								.getOfficeLocCode())/* tAOfficeRegionDescription */,
						court.getDescription()/* courtDetails */, courtCase
								.getStatus() != null ? courtCase.getStatus()
								.getDescription() : ""/* Court Case Status */,
						courtCase.getVerdict() != null ? courtCase.getVerdict()
								.getVerdict_desc() : ""/* verdict */, summons
								.getRoadCheckOffenceOutcome()
								.getRoadCheckOffence().getAuditEntry()
								.getCreateDTime()/* offenceDate */, courtApp
								.getCourtDTime()/* courtDate */,
						new ArrayList<CourtAppearanceReportResultsBO>()/* courtAppearanceList */);

				courtCaseReportResults.getCourtCaseList().add(courtCaseResult);

			} else if (courtCaseResult != null
					&& currentCourtCaseID.intValue() != courtCase
							.getCourtCaseId().intValue()) {
				courtCaseResult = new CourtCasesOpenedReportResultsBO(
						this.getPersonName(offender.getPersonId())/* offenderFullName */,
						this.getTAStaffName(taStaff.getStaffId())/* tAStaffFullName */,
						roadOp.getOperationName()/* roadOperationName */,
						summons.getRoadCheckOffenceOutcome()
								.getRoadCheckOffence().getOffence()
								.getDescription()/* offenceDetails */,
						roadOp.getOfficeLocCode()/* tAOfficeRegion */,
						this.getTAOfficeRegionDescription(roadOp
								.getOfficeLocCode())/* tAOfficeRegionDescription */,
						court.getDescription()/* courtDetails */, courtCase
								.getStatus() != null ? courtCase.getStatus()
								.getDescription() : ""/* Court Case Status */,
						courtCase.getVerdict() != null ? courtCase.getVerdict()
								.getVerdict_desc() : ""/* verdict */, summons
								.getRoadCheckOffenceOutcome()
								.getRoadCheckOffence().getAuditEntry()
								.getCreateDTime()/* offenceDate */, courtApp
								.getCourtDTime()/* courtDate */,
						new ArrayList<CourtAppearanceReportResultsBO>()/* courtAppearanceList */);

				courtCaseReportResults.getCourtCaseList().add(courtCaseResult);

			}

			currentCourtCaseID = courtCase.getCourtCaseId();

			CourtAppearanceReportResultsBO courtAppearance = new CourtAppearanceReportResultsBO(
					courtApp.getStatus() != null ? courtApp.getStatus()
							.getDescription() : ""/* courtAppearanceStatus */,
					courtApp.getPlea() != null ? courtApp.getPlea()
							.getDescription() : ""/* plea */,
					courtApp.getComment()/* comment */,
					courtApp.getCourtDTime()/* CourtDTime */,
					courtApp.getCourtRuling() != null ? courtApp
							.getCourtRuling().getDescription() : ""/* courtRuling */,
					court.getDescription()/* courtDetails */);

			courtCaseResult.getCourtAppearanceList().add(courtAppearance);

		}

		return courtCaseReportResults;
	}

	/**
	 * This is a helper function which checks the starting trial date of court
	 * cases and returns all court case id as a list of integers based on the
	 * start and end date ranges.
	 * 
	 * @param startTrialDate
	 * @param endTrialDate
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<Integer> getCourtCasesIdsForTrialDateRange(
			Date startTrialDate, Date endTrialDate) {
		List<Integer> courtCaseIds = new ArrayList<Integer>();

		Criteria criteria = this.hibernateTemplate.getSessionFactory()
				.getCurrentSession()
				.createCriteria(CourtAppearanceDO.class, "capp");

		criteria.setProjection(Projections.distinct(Projections
				.property("courtCase.courtCaseId")));

		criteria.add(Restrictions.between("CourtDTime", DateUtils
				.searchDateFormater(startTrialDate,
						DateUtils.SEARCHDATETYPE.START), DateUtils
				.searchDateFormater(endTrialDate, DateUtils.SEARCHDATETYPE.END)));

		courtCaseIds = criteria.list();

		return courtCaseIds;
	}

	@Override
	public WarningNoProsecutionReportBO warningNoProsecutionReport(
			WarningNoProReportCriteriaBO reportCriteria, String userName,
			String userRegion,
			ReportDisplayInformationDAOImpl reportDisplayInformation) {
		Criteria criteria = this.hibernateTemplate.getSessionFactory()
				.getCurrentSession()
				.createCriteria(WarningNoProsecutionDO.class, "wnp");

		/* Create aliases */
		criteria.createAlias("wnp.roadOperation", "roadOp");
		criteria.createAlias("wnp.offender", "off");
		criteria.createAlias("wnp.taStaff", "staff");
		criteria.createAlias("wnp.status", "status");
		criteria.createAlias("roadOp.category", "cat");
		criteria.createAlias("staff.person", "taPerson");
		/* _____________ */

		Criterion mainCriterion = Restrictions.sqlRestriction("1=1");

		if (reportCriteria.getOperationStartDate() != null
				&& reportCriteria.getOperationEndDate() != null) {
			Date reportStartDate = DateUtils.searchDateFormater(
					reportCriteria.getOperationStartDate(),
					DateUtils.SEARCHDATETYPE.START);
			Date reportEndDate = DateUtils.searchDateFormater(
					reportCriteria.getOperationEndDate(),
					DateUtils.SEARCHDATETYPE.END);

			Criterion scheduledDate = Restrictions
					.or(Restrictions.between("roadOp.scheduledStartDtime",
							reportStartDate, reportEndDate), Restrictions
							.between("roadOp.scheduledEndDtime",
									reportStartDate, reportEndDate));

			Criterion actualDate = Restrictions.or(Restrictions.between(
					"roadOp.actualStartDtime", reportStartDate, reportEndDate),
					Restrictions.between("roadOp.actualEndDtime",
							reportStartDate, reportEndDate));

			mainCriterion = Restrictions.and(mainCriterion,
					Restrictions.or(scheduledDate, actualDate));
		}

		if (reportCriteria.getIssuedStartDate() != null
				&& reportCriteria.getIssuedEndDate() != null) {
			Date reportStartDate = DateUtils.searchDateFormater(
					reportCriteria.getIssuedStartDate(),
					DateUtils.SEARCHDATETYPE.START);
			Date reportEndDate = DateUtils.searchDateFormater(
					reportCriteria.getIssuedEndDate(),
					DateUtils.SEARCHDATETYPE.END);

			mainCriterion = Restrictions.and(mainCriterion, Restrictions
					.between("wnp.issueDate", reportStartDate, reportEndDate));
		}

		if (reportCriteria.getPrintStartDate() != null
				&& reportCriteria.getPrintEndDate() != null) {
			Date reportStartDate = DateUtils.searchDateFormater(
					reportCriteria.getPrintStartDate(),
					DateUtils.SEARCHDATETYPE.START);
			Date reportEndDate = DateUtils.searchDateFormater(
					reportCriteria.getPrintEndDate(),
					DateUtils.SEARCHDATETYPE.END);

			mainCriterion = Restrictions.and(mainCriterion, Restrictions
					.between("wnp.printDtime", reportStartDate, reportEndDate));
		}

		if (StringUtil.isSet(reportCriteria.getOffenderFirstName())) {
			mainCriterion = Restrictions.and(
					mainCriterion,
					Restrictions.like("off.firstName",
							reportCriteria.getOffenderFirstName().trim(),
							MatchMode.ANYWHERE).ignoreCase());
		}

		if (reportCriteria.getOffenderId() != null
				&& reportCriteria.getOffenderId() > 0) {
			mainCriterion = Restrictions.and(
					mainCriterion,
					Restrictions.eq("off.personId",
							reportCriteria.getOffenderId()));
		}

		if (StringUtil.isSet(reportCriteria.getOffenderLastName())) {
			mainCriterion = Restrictions.and(
					mainCriterion,
					Restrictions.like("off.lastName",
							reportCriteria.getOffenderLastName().trim(),
							MatchMode.ANYWHERE).ignoreCase());
		}

		if (StringUtil.isSet(reportCriteria.getOffenderTRN())) {
			mainCriterion = Restrictions.and(mainCriterion, Restrictions.eq(
					"off.trnNbr", reportCriteria.getOffenderTRN().trim()));
		}

		if (StringUtil.isSet(reportCriteria.getOperationCategory())) {
			mainCriterion = Restrictions.and(mainCriterion, Restrictions.eq(
					"cat.categoryId", reportCriteria.getOperationCategory()
							.trim()));
		}

		if (reportCriteria.getRoadOperationId() != null
				&& reportCriteria.getRoadOperationId() > 0) {
			mainCriterion = Restrictions.and(
					mainCriterion,
					Restrictions.eq("roadOp.roadOperationId",
							reportCriteria.getRoadOperationId()));
		}

		if (StringUtil.isSet(reportCriteria.getRoadOperationName())) {
			mainCriterion = Restrictions.and(
					mainCriterion,
					Restrictions.like("roadOp.operationName",
							reportCriteria.getRoadOperationName().trim(),
							MatchMode.ANYWHERE).ignoreCase());
		}

		if (StringUtil.isSet(reportCriteria.getStatus())) {
			mainCriterion = Restrictions.and(mainCriterion, Restrictions.eq(
					"status.statusId", reportCriteria.getStatus().trim()));
		}

		if (StringUtil.isSet(reportCriteria.getTAOfficeRegion())) {
			mainCriterion = Restrictions.and(mainCriterion, Restrictions.eq(
					"roadOp.officeLocCode", reportCriteria.getTAOfficeRegion()
							.trim()));
		}

		if (StringUtil.isSet(reportCriteria.getTAStaffId())) {
			mainCriterion = Restrictions.and(mainCriterion, Restrictions.eq(
					"staff.staffId", reportCriteria.getTAStaffId().trim()));
		}

		if (StringUtil.isSet(reportCriteria.getTAStaffTRN())) {
			mainCriterion = Restrictions.and(mainCriterion, Restrictions.eq(
					"taPerson.trnNbr", reportCriteria.getTAStaffTRN().trim()));
		}

		criteria.add(mainCriterion);

		String stringStartDate = "";
		String stringEndDate = "";
		String dateTypeUsed = "";
		Date startDate = null;
		Date endDate = null;
		try {
			if (reportCriteria.getOperationStartDate() != null
					&& reportCriteria.getOperationEndDate() != null) {
				startDate = reportCriteria.getOperationStartDate();
				endDate = reportCriteria.getOperationEndDate();
				stringStartDate = DateUtils.getFormattedUtilDate(reportCriteria
						.getOperationStartDate());
				stringEndDate = DateUtils.getFormattedUtilDate(reportCriteria
						.getOperationEndDate());

				dateTypeUsed = "Operation";
			} else if (reportCriteria.getPrintStartDate() != null
					&& reportCriteria.getPrintEndDate() != null) {
				startDate = reportCriteria.getPrintStartDate();
				endDate = reportCriteria.getPrintEndDate();
				stringStartDate = DateUtils.getFormattedUtilDate(reportCriteria
						.getPrintStartDate());
				stringEndDate = DateUtils.getFormattedUtilDate(reportCriteria
						.getPrintEndDate());

				dateTypeUsed = "Print";
			} else if (reportCriteria.getIssuedStartDate() != reportCriteria
					.getIssuedEndDate()) {
				startDate = reportCriteria.getIssuedStartDate();
				endDate = reportCriteria.getIssuedEndDate();
				stringStartDate = DateUtils.getFormattedUtilDate(reportCriteria
						.getIssuedStartDate());
				stringEndDate = DateUtils.getFormattedUtilDate(reportCriteria
						.getIssuedEndDate());

				dateTypeUsed = "Issued";
			}
		} catch (Exception exe) {

		}
		/* Get report criteria names and descriptions */
		if (reportCriteria.getOffenderId() != null
				&& reportCriteria.getOffenderId() > 0) {
			reportCriteria.setOffenderName(this.getPersonName(reportCriteria
					.getOffenderId()));
		} else if (StringUtil.isSet(reportCriteria.getOffenderTRN())) {
			reportCriteria.setOffenderName(this.getPersonName(reportCriteria
					.getOffenderTRN()));
		}

		reportCriteria
				.setOperationCategoryDesc(this
						.getOperationCategoryDesc(reportCriteria
								.getOperationCategory()));
		reportCriteria.setRoadOperationName(reportCriteria
				.getRoadOperationName());

		reportCriteria.setTAOfficeRegionDesc(this
				.getTAOfficeRegionDescription(reportCriteria
						.getTAOfficeRegion()));

		if (StringUtil.isSet(reportCriteria.getTAStaffId()))
			reportCriteria.setTAStaffName(this.getTAStaffName(reportCriteria
					.getTAStaffId()));
		else if (StringUtil.isSet(reportCriteria.getTAStaffTRN()))
			reportCriteria.setTAStaffName(this.getPersonName(reportCriteria
					.getTAStaffTRN()));

		criteria.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);

		Iterator iterator = criteria.list().iterator();

		List<WarningNoProsecutionDetailsBO> warningNoProDetailsList = new ArrayList<WarningNoProsecutionDetailsBO>();

		while (iterator.hasNext()) {
			Map result = (Map) iterator.next();

			RoadOperationDO roadOp = (RoadOperationDO) result.get("roadOp");
			PersonDO offender = (PersonDO) result.get("off");
			TAStaffDO taStaff = (TAStaffDO) result.get("staff");
			StatusDO status = (StatusDO) result.get("status");
			CDCategoryDO category = (CDCategoryDO) result.get("cat");
			WarningNoProsecutionDO warningNoPro = (WarningNoProsecutionDO) result
					.get("wnp");

			Integer roadCheckId = warningNoPro.getRoadCheckOffenceOutcome()
					.getRoadCheckOffence().getRoadCheck().getRoadCheckId();

			WarningNoProsecutionDetailsBO warningNoProItem = new WarningNoProsecutionDetailsBO(
					this.getPersonName(taStaff.getPerson().getPersonId())/* tAStaffFullName */,
					taStaff.getStaffId()/* tAStaffID */,
					this.getTeamLeadName(roadOp.getRoadOperationId(), taStaff
							.getPerson().getPersonId())/* tATeamLeadFullName */,
					this.getPersonName(offender.getPersonId())/* offenderFullName */,
					this.getDLNumFromDLCheck(roadCheckId)/* offendersDriverLicence */,
					this.getVehicleDetailsFromMVCheck(roadCheckId)/* offenderVehicleDetials */,
					this.getVehicleOwenersFullNames(roadCheckId)/* vehicleOwnerFullNames */,
					roadOp.getOperationName()/* roadOperationName */,
					warningNoPro.getRoadCheckOffenceOutcome()
							.getRoadCheckOffence().getRoadCheck()
							.getCompliance().getCompliancyArtery()
							.getShortDescription()/* locationOfOffence */,
					warningNoPro.getOffenceDtime()/* dateOfOffence */,
					warningNoPro.getManualSerialNumber()/* manualSerialNumber */,
					status.getDescription()/* status */,
					warningNoPro.getRoadCheckOffenceOutcome()
							.getRoadCheckOffence().getOffence()
							.getDescription()/* offenceDescription */,
					warningNoPro.getAllegation()/* allegation */, warningNoPro
							.getRoadCheckOffenceOutcome().getRoadCheckOffence()
							.getOffence().getShortDescription()/* offenceShortDescription */);

			warningNoProDetailsList.add(warningNoProItem);
		}

		return new WarningNoProsecutionReportBO(userName, userRegion,
				reportDisplayInformation.applicationName,
				reportDisplayInformation.warningNoticeNoProsecutionReportTitle
						+ " (" + dateTypeUsed + ") " + stringStartDate + " TO "
						+ stringEndDate, startDate, endDate,
				reportCriteria.getSearchCriteriaString(), userRegion,
				warningNoProDetailsList);
	}

	/**
	 * This function get the team lead name based on the road operation id and
	 * the staff id.
	 * 
	 * @param roadOp
	 * @param personId
	 * @return
	 */
	private String getTeamLeadName(Integer roadOpId, Integer personId) {
		Criteria criteria = this.hibernateTemplate.getSessionFactory()
				.getCurrentSession()
				.createCriteria(AssignedPersonDO.class, "assp");

		criteria.createAlias("assp.team", "team");

		criteria.add(Restrictions.eq("assp.roadOperation.roadOperationId",
				roadOpId));
		criteria.add(Restrictions.eq("assp.person.personId", personId));

		criteria.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);

		Iterator iterator = criteria.list().iterator();

		Map map = (Map) iterator.next();

		TeamDO team = (TeamDO) map.get("team");

		PersonDO teamLead = team.getTeamLead().getPerson();

		return NameUtil
				.getName(teamLead.getFirstName(), teamLead.getLastName());

	}

	/**
	 * This function gets a list of owners of a vehicle full names. These names
	 * are based on a vehicle which has been checked at a motor vehicle check.
	 * If no records are found <b>NULL</b> is returned.
	 * 
	 * @param roadCheckId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private String getVehicleOwenersFullNames(Integer roadCheckId) {
		if (roadCheckId != null && roadCheckId > 0) {
			Criteria criteria = this.hibernateTemplate.getSessionFactory()
					.getCurrentSession()
					.createCriteria(VehicleOwnerDO.class, "vehOwner");

			criteria.createAlias("vehOwner.vehicleCheckResult", "vehChkResult");
			criteria.createAlias("vehChkResult.roadCheck", "roadCheck");

			criteria.add(Restrictions.eq("roadCheck.roadCheckId", roadCheckId));

			StringBuilder ownerNames = new StringBuilder("");

			for (VehicleOwnerDO vehOwner : (List<VehicleOwnerDO>) criteria
					.list()) {
				if (StringUtil.isSet(ownerNames.toString())) {
					ownerNames.append("; ");
				}

				ownerNames.append(NameUtil.getName(vehOwner.getFirstName(),
						vehOwner.getLastName()));

			}

			return ownerNames.toString();
		} else {
			return null;
		}
	}

	/**
	 * This function returns a drivers license number if a Drivers License Check
	 * was carried out at a road check. If no DL check was done the this
	 * function will return <b>NULL</b>.
	 * 
	 * @param roadCheckId
	 * @return
	 */
	private String getDLNumFromDLCheck(Integer roadCheckId) {
		if (roadCheckId != null) {
			Criteria criteria = this.hibernateTemplate.getSessionFactory()
					.getCurrentSession()
					.createCriteria(DLCheckResultDO.class, "dlCheckResult");

			criteria.createAlias("dlCheckResult.roadCheck", "roadCheck");
			criteria.add(Restrictions.eq("roadCheck.roadCheckId", roadCheckId));

			DLCheckResultDO dlCheckResult = (DLCheckResultDO) criteria
					.uniqueResult();

			if (dlCheckResult != null)
				return dlCheckResult.getDlNo();
			else
				return null;

		} else {
			return null;
		}
	}

	/**
	 * This function returns a string containing the registration number, type
	 * of vehicle, make, model, engine number, chassis number and colour. This
	 * information is about the vehicle at the time of the check. If no data is
	 * found <b>NULL</b> is returned.
	 * 
	 * @param roadCheckId
	 * @return
	 */
	private String getVehicleDetailsFromMVCheck(Integer roadCheckId) {
		if (roadCheckId != null) {
			Criteria criteria = this.hibernateTemplate.getSessionFactory()
					.getCurrentSession()
					.createCriteria(VehicleCheckResultDO.class, "vehChkResult");

			criteria.createAlias("vehChkResult.roadCheck", "roadCheck");

			criteria.add(Restrictions.eq("roadCheck.roadCheckId", roadCheckId));

			VehicleCheckResultDO vehChkResult = (VehicleCheckResultDO) criteria
					.uniqueResult();

			StringBuilder vehicleDetails = new StringBuilder("");
			if (vehChkResult != null) {
				String vehRegNum = StringUtil.isSet(vehChkResult
						.getPlateRegNo()) ? vehChkResult.getPlateRegNo().trim()
						: "";
				String vehType = StringUtil.isSet(vehChkResult.getTypeDesc()) ? vehChkResult
						.getTypeDesc().trim() : "";
				String vehMake = StringUtil.isSet(vehChkResult
						.getMakeDescription()) ? vehChkResult
						.getMakeDescription().trim() : "";
				String vehModel = StringUtil.isSet(vehChkResult.getModel()) ? vehChkResult
						.getModel().trim() : "";
				String vehEngine = StringUtil.isSet(vehChkResult.getEngineNo()) ? vehChkResult
						.getEngineNo().trim() : "";
				String vehChasisNum = StringUtil.isSet(vehChkResult
						.getChassisNo()) ? vehChkResult.getChassisNo().trim()
						: "";
				String vehColour = StringUtil.isSet(vehChkResult.getColour()) ? vehChkResult
						.getColour().trim() : "";

				vehicleDetails.append("Registration #: " + vehRegNum + "; ");
				vehicleDetails.append("Type of Vehicle : " + vehType + "; ");
				vehicleDetails.append("Make : " + vehMake + "; ");
				vehicleDetails.append("Model : " + vehModel + "; ");
				vehicleDetails.append("Engine #: " + vehEngine + "; ");
				vehicleDetails.append("Chassis #: " + vehChasisNum + "; ");
				vehicleDetails.append("Colour : " + vehColour);
			}

			return vehicleDetails.toString();
		} else {
			return null;
		}
	}

	
	/***************************
	 * UR-057 
	 *********************/
	
	private Integer absentPersonTypeCount(Integer roadOperationId, String personType) {
		/* Get Count of Absent Persons */
		Criteria criteriaAssignedPersons = this.hibernateTemplate
				.getSessionFactory().getCurrentSession()
				.createCriteria(AssignedPersonDO.class, "a");

		List<Integer> teamIds = getTeamIdsForRoadOp(roadOperationId);

		if (teamIds != null && teamIds.size() > 0)
		{	criteriaAssignedPersons.add(Restrictions.in(
					"a.assignedPersonKey.team.teamId", teamIds));
		
		criteriaAssignedPersons.add(Restrictions.eq(
				"a.assignedPersonKey.personType.personTypeId", personType));

		}
		
		criteriaAssignedPersons.add(Restrictions.eq("a.attended", "n")
				.ignoreCase());
		criteriaAssignedPersons.setProjection(Projections.count("a.attended"));
		criteriaAssignedPersons.setFetchMode("a", FetchMode.LAZY);
		Iterator iterator = criteriaAssignedPersons.list().iterator();

		Integer AbsentPersonTypeCount = (Integer) iterator.next();

		criteriaAssignedPersons.setFlushMode(FlushMode.ALWAYS);
		return AbsentPersonTypeCount;
	}
}
