/**
 * Created By: oanguin
 * Date: Apr 17, 2013
 *
 */
package fsl.ta.toms.roms.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import fsl.dao.DAO;
import fsl.ta.toms.roms.bo.RefCodeBO;
import fsl.ta.toms.roms.constants.Constants;
import fsl.ta.toms.roms.dao.CourtAppearanceDAO;
import fsl.ta.toms.roms.dao.CourtCaseDAO;
import fsl.ta.toms.roms.dao.DAOFactory;
import fsl.ta.toms.roms.dao.OtherRoleDAO;
import fsl.ta.toms.roms.dao.ReasonDAO;
import fsl.ta.toms.roms.dao.ScannedDocumentTypeDAO;
import fsl.ta.toms.roms.dao.impl.ArteryDAOImpl;
import fsl.ta.toms.roms.dao.impl.CategoryDAOImpl;
import fsl.ta.toms.roms.dao.impl.ConfigurationDAOImpl;
import fsl.ta.toms.roms.dao.impl.CourtDAOImpl;
import fsl.ta.toms.roms.dao.impl.CourtRulingDAOImpl;
import fsl.ta.toms.roms.dao.impl.EventDAOImpl;
import fsl.ta.toms.roms.dao.impl.ExcerptParameterMappingDAOImpl;
import fsl.ta.toms.roms.dao.impl.GoverningLawDAOImpl;
import fsl.ta.toms.roms.dao.impl.ITAExaminerDAOImpl;
import fsl.ta.toms.roms.dao.impl.JPDAOImpl;
import fsl.ta.toms.roms.dao.impl.LocationDAOImpl;
import fsl.ta.toms.roms.dao.impl.OffenceDAOImpl;
import fsl.ta.toms.roms.dao.impl.OutcomeTypeDAOImpl;
import fsl.ta.toms.roms.dao.impl.ParishDAOImpl;
import fsl.ta.toms.roms.dao.impl.PersonTypeDAOImpl;
import fsl.ta.toms.roms.dao.impl.PleaDAOImpl;
import fsl.ta.toms.roms.dao.impl.PoliceOfficerDAOImpl;
import fsl.ta.toms.roms.dao.impl.PoundDAOImpl;
import fsl.ta.toms.roms.dao.impl.RoadCheckTypeDAOImpl;
import fsl.ta.toms.roms.dao.impl.StatusDAOImpl;
import fsl.ta.toms.roms.dao.impl.StrategyDAOImpl;
import fsl.ta.toms.roms.dao.impl.TAOfficeLocationDAOImpl;
import fsl.ta.toms.roms.dao.impl.TAStaffDAOImpl;
import fsl.ta.toms.roms.dao.impl.TAVehicleDAOImpl;
import fsl.ta.toms.roms.dao.impl.VerdictDAOImpl;
import fsl.ta.toms.roms.dao.impl.WreckingCompanyDAOImpl;
import fsl.ta.toms.roms.dataobjects.ArteryDO;
import fsl.ta.toms.roms.dataobjects.CDCategoryDO;
import fsl.ta.toms.roms.dataobjects.CDCourtRulingDO;
import fsl.ta.toms.roms.dataobjects.CDEventDO;
import fsl.ta.toms.roms.dataobjects.CDOtherRoleDO;
import fsl.ta.toms.roms.dataobjects.CDOutcomeTypeDO;
import fsl.ta.toms.roms.dataobjects.CDPersonTypeDO;
import fsl.ta.toms.roms.dataobjects.CDPleaDO;
import fsl.ta.toms.roms.dataobjects.CDRoadCheckTypeDO;
import fsl.ta.toms.roms.dataobjects.CDVerdictDO;
import fsl.ta.toms.roms.dataobjects.ConfigurationDO;
import fsl.ta.toms.roms.dataobjects.CourtAppearanceDO;
import fsl.ta.toms.roms.dataobjects.CourtCaseDO;
import fsl.ta.toms.roms.dataobjects.CourtDO;
import fsl.ta.toms.roms.dataobjects.ExcerptParamMappingDO;
import fsl.ta.toms.roms.dataobjects.GoverningLawDO;
import fsl.ta.toms.roms.dataobjects.ITAExaminerDO;
import fsl.ta.toms.roms.dataobjects.JPDO;
import fsl.ta.toms.roms.dataobjects.LMIS_TAOfficeLocationViewDO;
import fsl.ta.toms.roms.dataobjects.LocationDO;
import fsl.ta.toms.roms.dataobjects.OffenceDO;
import fsl.ta.toms.roms.dataobjects.ParishDO;
import fsl.ta.toms.roms.dataobjects.PoliceOfficerDO;
import fsl.ta.toms.roms.dataobjects.PoundDO;
import fsl.ta.toms.roms.dataobjects.ReasonDO;
import fsl.ta.toms.roms.dataobjects.ReasonTypeDO;
import fsl.ta.toms.roms.dataobjects.ScannedDocumentTypeDO;
import fsl.ta.toms.roms.dataobjects.StatusDO;
import fsl.ta.toms.roms.dataobjects.StrategyDO;
import fsl.ta.toms.roms.dataobjects.TAStaffDO;
import fsl.ta.toms.roms.dataobjects.TAVehicleDO;
import fsl.ta.toms.roms.dataobjects.WreckingCompanyDO;
import fsl.ta.toms.roms.exception.InvalidFieldException;
import fsl.ta.toms.roms.exception.NoRecordFoundException;
import fsl.ta.toms.roms.exception.RequiredFieldMissingException;
import fsl.ta.toms.roms.search.criteria.impl.RefCodeCriteriaBO;
import fsl.ta.toms.roms.service.ReferenceCodeService;
import fsl.ta.toms.roms.util.NameUtil;
import fsl.ta.toms.roms.util.StringUtil;

/**
 * @author oanguin
 * Created Date: Apr 17, 2013
 */
public class ReferenceCodeServiceImpl implements ReferenceCodeService {

	private DAOFactory daoFactory;

	public void setDaoFactory(DAOFactory daoFactory) {
		this.daoFactory = daoFactory;
	}
	/**
	 *@param <code>RefCodeCriteriaBO</code> contains table name,rows needed, status and Hash Map with filters
	 * @throws NoRecordFoundException 
	 * @throws RequiredFieldMissingException 
	 *@returns RefCodeBO This function returns Reference codes based on the table type and criteria.
	 */
	@Override
	public List<RefCodeBO> getReferenceCode(RefCodeCriteriaBO criteriaBO) throws RequiredFieldMissingException, NoRecordFoundException,InvalidFieldException 
	{
		
		String tableName;
		DAO databaseObj;
		
		List<RefCodeBO> refCodeBOList = new ArrayList<RefCodeBO>(); 
		
		
		if(criteriaBO != null && StringUtil.isSet(criteriaBO.getTableName()))
		{
			
			tableName = criteriaBO.getTableName().trim().toLowerCase();
			
			if(tableName.equalsIgnoreCase("roms_cd_category"))
			{
				databaseObj = daoFactory.getCategoryDAO();
				
				List<CDCategoryDO> returnedDOList =  ((CategoryDAOImpl)databaseObj).getCategoryReference(
								criteriaBO.getFilter(), criteriaBO.getStatus());
				
				for(CDCategoryDO categoryDO : returnedDOList)
				{
					RefCodeBO refCode = new RefCodeBO(categoryDO.getCategoryId(),
								"",categoryDO.getDescription(),"",categoryDO.getStatus().getDescription());
					
					refCodeBOList.add(refCode);
				}
				
				
			}
			else if(tableName.equalsIgnoreCase("roms_strategy"))
			{
				databaseObj = daoFactory.getStrategyDAO();
				
				List<StrategyDO> returnedDOList =  ((StrategyDAOImpl)databaseObj).getStrategyReference(
						criteriaBO.getFilter(), criteriaBO.getStatus());

				
				for(StrategyDO strategyDO : returnedDOList)
				{
					RefCodeBO refCode = new RefCodeBO(
														strategyDO.getStrategyId().toString(),
														"",
														strategyDO.getDescription(),
														"",
														strategyDO.getStatus().getDescription()
													);
					
					refCodeBOList.add(refCode);
				}
			}
			else if(tableName.equalsIgnoreCase("roms_police_station"))
			{
				databaseObj = daoFactory.getPoliceOfficerDAO();
				
				List<String> returnedPoliceStations =  ((PoliceOfficerDAOImpl)databaseObj).getUniquePoliceStations();
				
				for(String policeStation : returnedPoliceStations)
				{
					RefCodeBO refCode = new RefCodeBO(
														policeStation,
														"",
														policeStation,
														"",
														""
													);
					
					refCodeBOList.add(refCode);
				}
			}
			else if(tableName.equalsIgnoreCase("roms_location"))
			{
				databaseObj = daoFactory.getLocationDAO();
				
				List<LocationDO> returnedDOList =  ((LocationDAOImpl)databaseObj)
						.getLocationReference(
								criteriaBO.getFilter(), criteriaBO.getStatus());
				

				
				for(LocationDO locationDO : returnedDOList)
				{
					RefCodeBO refCode = new RefCodeBO(
														locationDO.getLocationId().toString(),
														locationDO.getParish().getParishCode(),
														locationDO.getDescription(),
														locationDO.getShortDesc(),
														locationDO.getStatus().getDescription()
													);
					
					refCodeBOList.add(refCode);
				}
			}
			else if(tableName.equalsIgnoreCase("roms_artery"))
			{
				//System.out.println("HERE in artery.");
				databaseObj = daoFactory.getArteryDAO();
				
				List<ArteryDO> returnedDOList =  ((ArteryDAOImpl)databaseObj)
						.getArteryReference(
								criteriaBO.getFilter(), criteriaBO.getStatus());
				

				
				for(ArteryDO doObject : returnedDOList)
				{
					RefCodeBO refCode = new RefCodeBO(
														doObject.getArteryId().toString()/*Main ID*/,
														"" /*Alt ID*/,
														doObject.getDescription()/*Description*/,
														doObject.getShortDescription()/*Short Description*/,
														doObject.getStatus().getDescription()/*status*/
													);
					
					refCodeBOList.add(refCode);
				}
			}
			else if(tableName.equalsIgnoreCase("roms_ta_staff"))
			{
				databaseObj = daoFactory.getTAStaffDAO();
				
				List<TAStaffDO> returnedDOList =  ((TAStaffDAOImpl)databaseObj)
						.getTAStaffReference(
								criteriaBO.getFilter(), criteriaBO.getStatus());
				

				NameUtil nameUtil = new NameUtil();
				for(TAStaffDO doObject : returnedDOList)
				{
					RefCodeBO refCode = new RefCodeBO(
														doObject.getPerson().getPersonId().toString()/*Main ID*/,
														doObject.getStaffId() /*Alt ID*/,
														nameUtil.toString(doObject.getPerson().getFirstName(),
																doObject.getPerson().getLastName() /*Description - Full User Name*/, 
																doObject.getPerson().getMiddleName()),
														doObject.getLoginUsername()/*Short Description - Full Name*/,
														doObject.getStatus().getDescription()/*status*/
													);
					
					refCodeBOList.add(refCode);
				}
			}
			else if(tableName.equalsIgnoreCase("roms_jp"))
			{
				databaseObj = daoFactory.getJPDAO();
				
				List<JPDO> returnedDOList =  ((JPDAOImpl)databaseObj)
						.getJPReference(
								criteriaBO.getFilter(), criteriaBO.getStatus());
				

				NameUtil nameUtil = new NameUtil();
				for(JPDO doObject : returnedDOList)
				{
					RefCodeBO refCode = new RefCodeBO(
														doObject.getPerson().getPersonId().toString()/*Main ID*/,
														doObject.getRegNumber() /*Alt ID - reg number*/,
														nameUtil.toString(doObject.getPerson().getFirstName(),
																doObject.getPerson().getLastName(), 
																doObject.getPerson().getMiddleName())/*Description - Full Name*/,
														""/*Short Description*/,
														doObject.getStatus().getDescription()/*status*/
													);
					
					refCodeBOList.add(refCode);
				}
			}
			else if(tableName.equalsIgnoreCase("roms_police_officer"))
			{
				databaseObj = daoFactory.getPoliceOfficerDAO();
				
				List<PoliceOfficerDO> returnedDOList =  ((PoliceOfficerDAOImpl)databaseObj)
						.getPoliceOfficerReference(
								criteriaBO.getFilter(), criteriaBO.getStatus());
				

				NameUtil nameUtil = new NameUtil();
				for(PoliceOfficerDO doObject : returnedDOList)
				{
					RefCodeBO refCode = new RefCodeBO(
														doObject.getPerson().getPersonId().toString()/*Main ID*/,
														doObject.getPolOfficerCompNo().toString() /*Alt ID - Police Officer Comp #*/,
														nameUtil.toString(doObject.getPerson().getFirstName(),
																doObject.getPerson().getLastName(), 
																doObject.getPerson().getMiddleName())/*Description - Full Name*/,
														doObject.getRank()/*Short Description*/,
														doObject.getStatus().getDescription()/*status*/
													);
					
					refCodeBOList.add(refCode);
				}
			}
			else if(tableName.equalsIgnoreCase("roms_ita_examiner"))
			{
				databaseObj = daoFactory.getITAExaminerDAO();
				
				List<ITAExaminerDO> returnedDOList =  ((ITAExaminerDAOImpl)databaseObj)
						.getITAExaminerReference(
								criteriaBO.getFilter(), criteriaBO.getStatus());
				

				NameUtil nameUtil = new NameUtil();
				for(ITAExaminerDO doObject : returnedDOList)
				{
					RefCodeBO refCode = new RefCodeBO(
														doObject.getPerson().getPersonId().toString()/*Main ID*/,
														doObject.getIdNumber().toString() /*Alt ID - ID Number*/,
														nameUtil.toString(doObject.getPerson().getFirstName(),
																doObject.getPerson().getLastName(), 
																doObject.getPerson().getMiddleName())/*Description - Full Name*/,
														""/*Short Description*/,
														doObject.getStatus().getDescription()/*status*/
													);
					
					refCodeBOList.add(refCode);
				}
			}
			else if(tableName.equalsIgnoreCase("roms_cd_status"))
			{
				databaseObj = daoFactory.getStatusDAO();
				
				List<StatusDO> returnedDOList =  ((StatusDAOImpl)databaseObj)
						.getStatusReference(
								criteriaBO.getFilter(), criteriaBO.getStatus());
				

				
				for(StatusDO doObject : returnedDOList)
				{
					RefCodeBO refCode = new RefCodeBO(
														doObject.getStatusId().toString()/*Main ID*/,
														"" /*Alt ID*/,
														doObject.getDescription()/*Description - Full Name*/,
														""/*Short Description*/,
														""/*status*/
													);
					
					refCodeBOList.add(refCode);
			}
			}
			else if(tableName.equalsIgnoreCase("roms_court"))
			{
				databaseObj = daoFactory.getCourtDAO();
				
				List<CourtDO> returnedDOList =  ((CourtDAOImpl)databaseObj)
						.getCourtReference(
								criteriaBO.getFilter(), criteriaBO.getStatus());
				

				
				for(CourtDO doObject : returnedDOList)
				{
					RefCodeBO refCode = new RefCodeBO(
														doObject.getCourtId().toString()/*Main ID*/,
														doObject.getAddress().getParish().getParishCode() /*Alt ID*/,
														doObject.getDescription()/*Description - Full Name*/,
														doObject.getShortDesc()/*Short Description*/,
														doObject.getStatus().getDescription()/*status*/
													);
					
					refCodeBOList.add(refCode);
				}
			}
			else if(tableName.equalsIgnoreCase("roms_cd_court_ruling"))
			{
				databaseObj = daoFactory.getCourtRulingDAO();
				
				List<CDCourtRulingDO> returnedDOList =  ((CourtRulingDAOImpl)databaseObj)
						.getCourtRulingReference(
								criteriaBO.getFilter(), criteriaBO.getStatus());
				

				
				for(CDCourtRulingDO doObject : returnedDOList)
				{
					RefCodeBO refCode = new RefCodeBO(
														doObject.getRulingId()/*Main ID*/,
														doObject.getFinalRuling() + ""/*Alt ID used to store the final ruling flag*/,
														doObject.getDescription()/*Description - Full Name*/,
														""/*Short Description*/,
														doObject.getStatus().getDescription()/*status*/
													);
					
					refCodeBOList.add(refCode);
				}
			}
			else if(tableName.equalsIgnoreCase("roms_cd_plea"))
			{
				databaseObj = daoFactory.getPleaDAO();
				
				List<CDPleaDO> returnedDOList =  ((PleaDAOImpl)databaseObj)
						.getPleaReference(
								criteriaBO.getFilter(), criteriaBO.getStatus());
				

				
				for(CDPleaDO doObject : returnedDOList)
				{
					RefCodeBO refCode = new RefCodeBO(
														doObject.getPleaId().toString()/*Main ID*/,
														"" /*Alt ID*/,
														doObject.getDescription()/*Description - Full Name*/,
														""/*Short Description*/,
														doObject.getStatus().getDescription()/*status*/
													);
					
					refCodeBOList.add(refCode);
				}
			}
			else if(tableName.equalsIgnoreCase("roms_ta_vehicle"))
			{
				databaseObj = daoFactory.getTAVehicleDAO();
				
				List<TAVehicleDO> returnedDOList =  ((TAVehicleDAOImpl)databaseObj)
						.getTAVehicleReference(
								criteriaBO.getFilter(), criteriaBO.getStatus());
				

				
				for(TAVehicleDO doObject : returnedDOList)
				{
					RefCodeBO refCode = new RefCodeBO(
														doObject.getTaVehicleId().toString()/*Main ID*/,
														"" /*Alt ID*/,
														String.format("%s, %s, %s", doObject.getVehicle().getMakeDescription() != null?doObject.getVehicle().getMakeDescription():"",
																doObject.getVehicle().getModel() != null?doObject.getVehicle().getModel():"",
																doObject.getVehicle().getTypeDesc() != null ?doObject.getVehicle().getTypeDesc():"")/*Description - Make Model and Year*/,
														doObject.getVehicle().getPlateRegNo()/*Short Description - Plate Reg Number*/,
														doObject.getStatus().getDescription()/*status*/
													);
					
					refCodeBOList.add(refCode);
				}
			}
			else if(tableName.equalsIgnoreCase("roms_cd_outcome_type"))
			{
				databaseObj = daoFactory.getOutcomeTypeDAO();
				
				List<CDOutcomeTypeDO> returnedDOList =  ((OutcomeTypeDAOImpl)databaseObj)
						.getOutcomeTypeReference(
								criteriaBO.getFilter(), criteriaBO.getStatus());
				

				
				for(CDOutcomeTypeDO doObject : returnedDOList)
				{
					RefCodeBO refCode = new RefCodeBO(
														doObject.getOutcomeTypeId()/*Main ID*/,
														"" /*Alt ID*/,
														doObject.getDescription()/*Description - Make Model and Year*/,
														""/*Short Description - Plate Reg Number*/,
														doObject.getStatus().getDescription()/*status*/
													);
					
					refCodeBOList.add(refCode);
				}
			}
			else if(tableName.equalsIgnoreCase("roms_offence"))
			{
				databaseObj = daoFactory.getOffenceDAO();
				
				List<OffenceDO> returnedDOList =  ((OffenceDAOImpl)databaseObj)
						.getOffenceReference(
								criteriaBO.getFilter(), criteriaBO.getStatus());
				

				
				for(OffenceDO doObject : returnedDOList)
				{
					RefCodeBO refCode = new RefCodeBO(
														doObject.getOffenceId().toString()/*Main ID*/,
														(doObject.getRoadCheckType() != null && doObject.getRoadCheckType().getRoadCheckTypeId() != null) ? doObject.getRoadCheckType().getRoadCheckTypeId():null /*Alt ID*/,
														doObject.getDescription()/*Description - Make Model and Year*/,
														doObject.getShortDescription()/*Short Description - Plate Reg Number*/,
														doObject.getStatus().getDescription()/*status*/
													);
					
					refCodeBOList.add(refCode);
				}
			}
			else if(tableName.equalsIgnoreCase("roms_cd_road_check_type"))
			{
				databaseObj = daoFactory.getRoadCheckTypeDAO();
				
				List<CDRoadCheckTypeDO> returnedDOList =  ((RoadCheckTypeDAOImpl)databaseObj)
						.getRoadCheckTypeReference(
								criteriaBO.getFilter(), criteriaBO.getStatus());
				

				
				for(CDRoadCheckTypeDO doObject : returnedDOList)
				{
					RefCodeBO refCode = new RefCodeBO(
														doObject.getRoadCheckTypeId()/*Main ID*/,
														"" /*Alt ID*/,
														doObject.getDescription()/*Description - Make Model and Year*/,
														""/*Short Description - Plate Reg Number*/,
														doObject.getStatus().getDescription()/*status*/
													);
					
					refCodeBOList.add(refCode);
				}
			}
			else if(tableName.equalsIgnoreCase("roms_cd_person_type"))
			{
				databaseObj = daoFactory.getPersonTypeDAO();
				
				List<CDPersonTypeDO> returnedDOList =  ((PersonTypeDAOImpl)databaseObj)
						.getPersonTypeReference(
								criteriaBO.getFilter(), criteriaBO.getStatus());
				

				
				for(CDPersonTypeDO doObject : returnedDOList)
				{
					RefCodeBO refCode = new RefCodeBO(
														doObject.getPersonTypeId()/*Main ID*/,
														"" /*Alt ID*/,
														doObject.getDescription()/*Description - Make Model and Year*/,
														""/*Short Description - Plate Reg Number*/,
														doObject.getStatus().getDescription()/*status*/
													);
					
					refCodeBOList.add(refCode);
				}
			}
			else if(tableName.equalsIgnoreCase("roms_cd_event"))
			{
				databaseObj = daoFactory.getEventDAO();
				
				List<CDEventDO> returnedDOList =  ((EventDAOImpl)databaseObj)
						.getEventReference(
								criteriaBO.getFilter(), criteriaBO.getStatus());
				

				
				for(CDEventDO doObject : returnedDOList)
				{
					RefCodeBO refCode = new RefCodeBO(
														doObject.getEventCode().toString()/*Main ID*/,
														"" /*Alt ID*/,
														doObject.getEventDescription()/*Description - Make Model and Year*/,
														""/*Short Description - Plate Reg Number*/,
														doObject.getStatus().getDescription()/*status*/
													);
					
					refCodeBOList.add(refCode);
				}
			}
			else if(tableName.equalsIgnoreCase("roms_configuration"))
			{
				databaseObj = daoFactory.getConfigurationDAO();
				
				List<ConfigurationDO> returnedDOList =  ((ConfigurationDAOImpl)databaseObj)
						.getConfigurationReference(
								criteriaBO.getFilter(), criteriaBO.getStatus());
				

				
				for(ConfigurationDO doObject : returnedDOList)
				{
					RefCodeBO refCode = new RefCodeBO(
														doObject.getCfgCode()/*Main ID*/,
														doObject.getValue() /*Alt ID*/,
														doObject.getDescription()/*Description - Make Model and Year*/,
														""/*Short Description - Plate Reg Number*/,
														doObject.getStatus().getDescription()/*status*/
													);
					
					refCodeBOList.add(refCode);
				}
			}
			else if(tableName.equalsIgnoreCase("roms_governing_law"))
			{
				databaseObj = daoFactory.getGoverningLawDAO();
				
				List<GoverningLawDO> returnedDOList =  ((GoverningLawDAOImpl)databaseObj)
						.getGoverningLawReference(
								criteriaBO.getFilter(), criteriaBO.getStatus());
				

				
				for(GoverningLawDO doObject : returnedDOList)
				{
					RefCodeBO refCode = new RefCodeBO(
														doObject.getLawId().toString()/*Main ID*/,
														"" /*Alt ID*/,
														doObject.getDescription()/*Description - Make Model and Year*/,
														doObject.getShortDesc()/*Short Description - Plate Reg Number*/,
														doObject.getStatus().getDescription()/*status*/
													);
					
					refCodeBOList.add(refCode);
				}
			}
			else if(tableName.equalsIgnoreCase("roms_excerpt_param_mapping"))
			{
				databaseObj = daoFactory.getExcerptParameterMappingDAO();
				
				List<ExcerptParamMappingDO> returnedDOList =  ((ExcerptParameterMappingDAOImpl)databaseObj)
						.getExcerptParameterMappingReference(
								criteriaBO.getFilter(), criteriaBO.getStatus());
				

				
				for(ExcerptParamMappingDO doObject : returnedDOList)
				{
					RefCodeBO refCode = new RefCodeBO(
														doObject.getParamMapId().toString()/*Main ID*/,
														"" /*Alt ID*/,
														doObject.getParamDescription()/*Description - Make Model and Year*/,
														doObject.getParamName()/*Short Description - Param Name*/,
														""/*status*/
													);
					
					refCodeBOList.add(refCode);
				}
			}
			else if(tableName.equalsIgnoreCase("roms_parish"))
			{
				databaseObj = daoFactory.getParishDAO();
				
				List<ParishDO> returnedDOList =  ((ParishDAOImpl)databaseObj)
						.getParishReference(
								criteriaBO.getFilter(), criteriaBO.getStatus());
				

				
				for(ParishDO doObject : returnedDOList)
				{
					RefCodeBO refCode = new RefCodeBO(
														doObject.getParishCode()/*Main ID*/,
														doObject.getOfficeLocationCode() /*Alt ID*/,
														doObject.getDescription()/*Description - Make Model and Year*/,
														""/*Short Description - Param Name*/,
														doObject.getStatus().getDescription()/*status*/
													);
					
					refCodeBOList.add(refCode);
				}
			}
			else if(tableName.equalsIgnoreCase("roms_pound"))
			{
				databaseObj = daoFactory.getPoundDAO();
				
				List<PoundDO> returnedDOList =  ((PoundDAOImpl)databaseObj)
						.getPoundReference(
								criteriaBO.getFilter(), criteriaBO.getStatus());
				

				
				for(PoundDO doObject : returnedDOList)
				{
					RefCodeBO refCode = new RefCodeBO(
														doObject.getPoundId().toString()/*Main ID*/,
														doObject.getAddress().getParish().getParishCode() /*Alt ID*/,
														doObject.getPoundName()/*Description - Pound Name*/,
														doObject.getShortDesc()/*Short Description - Short Description*/,
														doObject.getStatus().getDescription()/*status*/
													);
					
					refCodeBOList.add(refCode);
				}
			}
			else if(tableName.equalsIgnoreCase("roms_wrecking_company"))
			{
				databaseObj = daoFactory.getWreckingCompanyDAO();
				
				List<WreckingCompanyDO> returnedDOList =  ((WreckingCompanyDAOImpl)databaseObj)
						.getWreckingCompanyReference(
								criteriaBO.getFilter(), criteriaBO.getStatus());
				

				
				for(WreckingCompanyDO doObject : returnedDOList)
				{
					RefCodeBO refCode = new RefCodeBO(
														doObject.getWreckingCompanyId().toString()/*Main ID*/,
														doObject.getAddress().getParish().getParishCode() /*Alt ID*/,
														doObject.getCompanyName().toString()/*Description - Pound Name*/,
														doObject.getShortDesc()/*Short Description - Short Description*/,
														""/*status*/
													);
					
					refCodeBOList.add(refCode);
				}
			}
			else if(tableName.equalsIgnoreCase("roms_cd_verdict"))
			{
				databaseObj = daoFactory.getVerdictDAO();
				
				List<CDVerdictDO> returnedDOList =  ((VerdictDAOImpl)databaseObj)
						.getVerdictReference(
								criteriaBO.getFilter(), criteriaBO.getStatus());
				

				
				for(CDVerdictDO doObject : returnedDOList)
				{
					RefCodeBO refCode = new RefCodeBO(
														doObject.getVerdict_code().toString()/*Main ID*/,
														"" /*Alt ID*/,
														doObject.getVerdict_desc()/*Description - Pound Name*/,
														""/*Short Description - Short Description*/,
														doObject.getStatus().getDescription()/*status*/
													);
					
					refCodeBOList.add(refCode);
				}
			}
			else if(tableName.equalsIgnoreCase("roms_lmis_ta_office_location_view"))
			{
				
				databaseObj = daoFactory.getTAOfficeLocationDAO();
				
				List<LMIS_TAOfficeLocationViewDO> returnedDOList =  ((TAOfficeLocationDAOImpl)databaseObj)
						.getTAOfficeLocationReference(
								criteriaBO.getFilter(), criteriaBO.getStatus());
				
		
				
				for(LMIS_TAOfficeLocationViewDO doObject : returnedDOList)
				{
					RefCodeBO refCode = new RefCodeBO(
														doObject.getLocationCode()/*Main ID*/,
														"" /*Alt ID*/,
														doObject.getLocationDesc()/*Description*/,
														""/*Short Description - Short Description*/,
														doObject.getStatusCode()/*status*/
													);
					
					refCodeBOList.add(refCode);
				}
				

				Collections.sort(refCodeBOList, new Comparator<RefCodeBO>() {
				    public int compare(RefCodeBO result1, RefCodeBO result2) {
				        return result1.getDescription().toLowerCase().compareTo(result2.getDescription().toLowerCase());
				    }
				});
			}
			else if(tableName.equalsIgnoreCase("roms_court_case"))
			{
							
				CourtCaseDAO courtCaseDAO = daoFactory.getCourtCaseDAO();
				
							
				List<CourtCaseDO> returnedDOList =  courtCaseDAO.getCourtCaseReference(criteriaBO.getFilter(), criteriaBO.getStatus());
				
		
				
				for(CourtCaseDO doObject : returnedDOList)
				{
					RefCodeBO refCode = new RefCodeBO(
														doObject.getCourtCaseId().toString()/*Main ID*/,
														doObject.getCourtCaseNo() /*Alt ID*/,
														StringUtil.isSet(doObject.getComment())? doObject.getComment().substring(0,doObject.getComment().length() < 30 ?doObject.getComment().length():30 ):""/*Description*/,
														""/*Short Description - Short Description*/,
														doObject.getStatus().getDescription()/*status*/
													);
					
					refCodeBOList.add(refCode);
				}
			}
			else if(tableName.equalsIgnoreCase("roms_court_appearance"))
			{
							
				CourtAppearanceDAO courtAppearanceDAO = daoFactory.getCourtAppearanceDAO();
				
							
				List<CourtAppearanceDO> returnedDOList =  courtAppearanceDAO.getCourtAppearanceReference(criteriaBO.getFilter(), criteriaBO.getStatus());
				
		
				
				for(CourtAppearanceDO doObject : returnedDOList)
				{
					RefCodeBO refCode = new RefCodeBO(
														doObject.getCourtAppearanceId().toString()/*Main ID*/,
														"" /*Alt ID*/,
														StringUtil.isSet(doObject.getComment())? doObject.getComment().substring(0,doObject.getComment().length() < 30 ?doObject.getComment().length():30 ):""/*Description*/,
														""/*Short Description - Short Description*/,
														doObject.getStatus().getDescription()/*status*/
													);
					
					refCodeBOList.add(refCode);
				}
			}
			else if(tableName.equalsIgnoreCase("roms_reason"))
			{
				//databaseObj = null;
				//databaseObj = daoFactory.getCourtAppearanceDAO();
				
				ReasonDAO reasonDAO = daoFactory.getReasonDAO();
				
				//System.err.println(" getting reasons ");
				
				//Displays only Reasons that are suppose to be visible to user
				String ignoreIsVisible = criteriaBO.getFilter().get("ignore_is_visible_flag");
				if(ignoreIsVisible == null){
					criteriaBO.getFilter().put("is_visible", Constants.YesNo.YES_LABEL_STR);
				}
					criteriaBO.getFilter().remove("ignore_is_visible_flag");
				List<ReasonDO> returnedDOList =  reasonDAO.getReasonReference(
								criteriaBO.getFilter(), criteriaBO.getStatus());
				
		
				
				for(ReasonDO doObject : returnedDOList)
				{
					RefCodeBO refCode = new RefCodeBO(
														doObject.getReasonId().toString()/*Main ID*/,
														doObject.getReasonType().getDescription() /*Alt ID - Type*/,
														doObject.getDescription()/*Description*/,
														""/*Short Description - Short Description*/,
														doObject.getStatus().getDescription()/*status*/
													);
					
					refCodeBOList.add(refCode);
				}
				
			}else if(tableName.equalsIgnoreCase("roms_reason_type"))
			{
				//databaseObj = null;
				//databaseObj = daoFactory.getCourtAppearanceDAO();
				
				ReasonDAO reasonDAO = daoFactory.getReasonDAO();
				
				
				
				List<ReasonTypeDO> returnedDOList =  reasonDAO.getReasonTypeReference(
								criteriaBO.getFilter(), criteriaBO.getStatus());
				
		
				
				for(ReasonTypeDO doObject : returnedDOList)
				{
					RefCodeBO refCode = new RefCodeBO(
														doObject.getReasonTypeCode().toString()/*Main ID*/,
														"" /*Alt ID - Type*/,
														doObject.getDescription()/*Description*/,
														"",/*Short Description - Short Description,*/
														doObject.getStatus()!=null?doObject.getStatus().getDescription():""
													);
					
					refCodeBOList.add(refCode);
				}
				}else if(tableName.equalsIgnoreCase("roms_cd_document_type"))
			{
				//databaseObj = null;
				//databaseObj = daoFactory.getCourtAppearanceDAO();
				
				ScannedDocumentTypeDAO scannedDocumentTypeDAO = daoFactory.getScannedDocumentTypeDAO();
				
				List<ScannedDocumentTypeDO> returnedDOList =  scannedDocumentTypeDAO.getScannedDocumentTypeReference(null, null);
						
				for(ScannedDocumentTypeDO doObject : returnedDOList)
				{
					RefCodeBO refCode = new RefCodeBO(
														doObject.getDocumentTypeCode()/*Main ID*/,
														"" /*Alt ID - Type*/,
														doObject.getDocumentTypeDescription()/*Description*/,
														""/*Short Description - Short Description*/,
														""/*status*/
													);
					
					refCodeBOList.add(refCode);
				}
			}else if(tableName.equalsIgnoreCase("roms_cd_other_role_observed"))
			{
				
				OtherRoleDAO otherRoleDAO = daoFactory.getOtherRoleDAO();
				
				List<CDOtherRoleDO> otherRoleDOList = otherRoleDAO.getOtherRoleReference(criteriaBO.getFilter(), criteriaBO.getStatus());
					
				
				//System.err.println("Other roles list: "+otherRoleDOList.size());
				
				for(CDOtherRoleDO doObject : otherRoleDOList)
				{
					RefCodeBO refCode = new RefCodeBO(
														doObject.getOtherRoleId()/*Main ID*/,
														"" /*Alt ID - Type*/,
														doObject.getDescription()/*Description*/,
														""/*Short Description - Short Description*/,
														""/*status*/
													);
					
					refCodeBOList.add(refCode);
				}
			}			
			
		}
		else
		{
			throw new RequiredFieldMissingException();
		}
		
		/*Throw no Records found if refCodeBOList is Empty*/
		if(refCodeBOList.size() < 1)
			throw new NoRecordFoundException();
		
		return refCodeBOList;
	}

}
