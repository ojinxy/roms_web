package org.fsl.roms.service.action;

import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.wst.common.internal.emf.utilities.StringUtil;
import org.fsl.application.ApplicationProperties;
import org.fsl.datatable.DataTableMemory;
import org.fsl.roms.constants.Constants;
import org.fsl.roms.constants.Constants.DocumentType;
import org.fsl.roms.constants.Constants.ScannedDocumentType;
import org.fsl.roms.constants.Constants.ScannedDocumentTypeShort;
import org.fsl.roms.security.ROMSUserDetails;
import org.fsl.roms.util.DateUtils;
import org.fsl.roms.view.AddressView;
import org.fsl.roms.view.RoadCheckInitiateView;
import org.fsl.roms.view.VerifyDetailsSecurityView;
import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.primefaces.component.selectonemenu.SelectOneMenu;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DeviceUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.webflow.action.FormAction;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;
import org.springframework.webflow.execution.RequestContextHolder;

import fsl.ta.toms.roms.bo.ArteryBO;
import fsl.ta.toms.roms.bo.BIMSStaffViewBO;
import fsl.ta.toms.roms.bo.CourtAppearanceBO;
import fsl.ta.toms.roms.bo.CourtBO;
import fsl.ta.toms.roms.bo.CourtCaseBO;
import fsl.ta.toms.roms.bo.ExcerptParamMappingBO;
import fsl.ta.toms.roms.bo.ITAExaminerBO;
import fsl.ta.toms.roms.bo.JPBO;
import fsl.ta.toms.roms.bo.LMISUserViewBO;
import fsl.ta.toms.roms.bo.OffenceBO;
import fsl.ta.toms.roms.bo.PersonBO;
import fsl.ta.toms.roms.bo.PoundBO;
import fsl.ta.toms.roms.bo.RefCodeBO;
import fsl.ta.toms.roms.bo.RoadOperationBO;
import fsl.ta.toms.roms.bo.StaffUserMappingBO;
import fsl.ta.toms.roms.bo.SummonsBO;
import fsl.ta.toms.roms.bo.TAStaffBO;
import fsl.ta.toms.roms.bo.TAVehicleBO;
import fsl.ta.toms.roms.bo.VehicleBO;
import fsl.ta.toms.roms.bo.WreckingCompanyBO;
import fsl.ta.toms.roms.exception.NoRecordFoundException;
import fsl.ta.toms.roms.search.criteria.impl.ArteryCriteriaBO;
import fsl.ta.toms.roms.search.criteria.impl.CourtCriteriaBO;
import fsl.ta.toms.roms.search.criteria.impl.ITAExaminerCriteriaBO;
import fsl.ta.toms.roms.search.criteria.impl.JPCriteriaBO;
import fsl.ta.toms.roms.search.criteria.impl.LocationCriteriaBO;
import fsl.ta.toms.roms.search.criteria.impl.OffenceCriteriaBO;
import fsl.ta.toms.roms.search.criteria.impl.PoundCriteriaBO;
import fsl.ta.toms.roms.search.criteria.impl.RefCodeCriteriaBO;
import fsl.ta.toms.roms.search.criteria.impl.RoadOperationCriteriaBO;
import fsl.ta.toms.roms.search.criteria.impl.StaffUserMappingCriteriaBO;
import fsl.ta.toms.roms.search.criteria.impl.TAVehicleCriteriaBO;
import fsl.ta.toms.roms.search.criteria.impl.WreckingCompanyCriteriaBO;
import fsl.ta.toms.roms.webservices.AMVSWebService;
import fsl.ta.toms.roms.webservices.Authorization;
import fsl.ta.toms.roms.webservices.BIMSWebService;
import fsl.ta.toms.roms.webservices.DocumentsManager;
import fsl.ta.toms.roms.webservices.Maintenance;
import fsl.ta.toms.roms.webservices.ReferenceCode;
import fsl.ta.toms.roms.webservices.Report;
import fsl.ta.toms.roms.webservices.RoadCompliancy;
import fsl.ta.toms.roms.webservices.RoadOperation;
import fsl.ta.toms.roms.webservices.ScannedDocUpload;
import fsl.ta.toms.roms.webservices.StaffUserMapping;
import fsl.ta.toms.roms.webservices.TRNWebService;


/**
 * @author jreid
 */

@Service
public class BaseServiceAction extends FormAction {

	
	
	List<StaffUserMappingBO> staffUserMaps;

	// Stores the information of the currently logged in user
	ROMSUserDetails romsLoggedInUser;	
	
	private static Properties messageProps; 
	private static ResourceBundle messages = ResourceBundle.getBundle("messages");
	
	protected Logger logger = LogManager.getLogger(this.getClass().getName());
	/**
	 * @return the romsLoggedInUser
	 */
	public ROMSUserDetails getRomsLoggedInUser() {
		return (ROMSUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

	/**
	 * @param romsLoggedInUser
	 *            the romsLoggedInUser to set
	 */
	public void setRomsLoggedInUser(ROMSUserDetails romsLoggedInUser) {
		this.romsLoggedInUser = romsLoggedInUser;
	}

	public BaseServiceAction() {
		super();

	}

	/**
	 * Retrieves parish codes and descriptions from table roms_parish
	 * 
	 * @param context
	 * @return
	 */
	public Event getParishes(RequestContext context) {
		List<RefCodeBO> refcodes = null;
		List<SelectItem> parishList = new ArrayList<SelectItem>();

		try {
			refcodes = getRefInfo("roms_parish");

			for (RefCodeBO codeBo : refcodes) {
				parishList.add(new SelectItem(codeBo.getCode(), codeBo.getDescription()));
			}
			context.getFlowScope().put("parishList", parishList);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return success();
	}

	/**
	 * Retrieves pounds from table roms_pound
	 * 
	 * @param context
	 * @return
	 */
	public Event getPounds(RequestContext context) {
		List<RefCodeBO> refcodes = null;
		List<SelectItem> poundList = new ArrayList<SelectItem>();
		
		try {
			refcodes = getRefInfo("roms_pound");

			Collections.sort(refcodes, new Comparator<RefCodeBO>() {
				public int compare(RefCodeBO result1, RefCodeBO result2) {
					return result1.getDescription().toLowerCase().compareTo(result2.getDescription().toLowerCase());
				}
			});
		
			for (RefCodeBO codeBo : refcodes) {
				poundList.add(new SelectItem(codeBo.getCode(), codeBo.getDescription()+" - "+codeBo.getShortDescription()));
			}
			context.getFlowScope().put("poundList", poundList);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return success();
	}
	
	public List<SelectItem> getPoundsShadow(){
		List<RefCodeBO> refcodes = null;
		List<SelectItem> poundList = new ArrayList<SelectItem>();

		try {
			refcodes = getRefInfo("roms_pound");

			Collections.sort(refcodes, new Comparator<RefCodeBO>() {
				public int compare(RefCodeBO result1, RefCodeBO result2) {
					return result1.getDescription().toLowerCase().compareTo(result2.getDescription().toLowerCase());
				}
			});
			
			
			for (RefCodeBO codeBo : refcodes) {
				poundList.add(new SelectItem(codeBo.getCode(), codeBo.getDescription()+" - "+codeBo.getShortDescription()));
			}
			

		} catch (Exception e) {
			e.printStackTrace();
		}
		return poundList;
	}
	
	public Event getPoliceStations(RequestContext context){
		List<RefCodeBO> refcodes = null;
		List<SelectItem> stationList = new ArrayList<SelectItem>();

		try {
			refcodes = getRefInfo("roms_police_station");

			for (RefCodeBO codeBo : refcodes) {
				stationList.add(new SelectItem(codeBo.getDescription(), codeBo.getDescription()));
			}
			
			
			context.getViewScope().put("policeStations", stationList);

		} catch (Exception e) {
			logger.error("Base Service Action", e);
		}
		return success();
	}
	
	/**
	 * Returns parish description based on parish code
	 * 
	 * @param officeLocationCode
	 * @return String
	 */
	public String getParishDesc(String parishCode) {
		List<RefCodeBO> refcodes = null;
		String parishDesc = "";

		refcodes = getRefInfo("roms_parish");
		for (RefCodeBO codeBo : refcodes) {
			if (StringUtil.stringsEqual(parishCode, codeBo.getCode())) {
				parishDesc = codeBo.getDescription();
				break;
			}
		}
		return parishDesc;
	}

	/**
	 * Returns location description based on location code
	 * 
	 * @param LocationCode
	 * @return int
	 */
	public String getLocationDesc(int locationCode) {
		List<RefCodeBO> refcodes = null;
		String locationDesc = "";
		refcodes = getRefInfo("roms_location");
		for (RefCodeBO codeBo : refcodes) {
			if (StringUtil.stringsEqual(Integer.toString(locationCode), codeBo.getCode())) {
				locationDesc = codeBo.getDescription();
				break;
			}
		}
		return locationDesc;
	}

	/**
	 * Used to instantiate TaVehicle subclasses
	 * 
	 * @param context
	 * 
	 */
	public void initTaVehicle(RequestContext context) {
		TAVehicleBO taVehicle = (TAVehicleBO) context.getFlowScope().get("taVehicle");
		taVehicle.setVehicle(new VehicleBO());
		TAVehicleCriteriaBO criteria = (TAVehicleCriteriaBO) context.getFlowScope().get("criteria");
		criteria.setVehicle(new VehicleBO());

	}

	/**
	 * Used to instantiate TaVehicle subclasses
	 * 
	 * @param context
	 * 
	 */
	public void initJP(RequestContext context) {
		JPBO jp = (JPBO) context.getFlowScope().get("jp");
		jp.setPersonBO(new PersonBO());

	}

	/**
	 * Used to instantiate ITA_Examiner subclasses
	 * 
	 * @param context
	 * 
	 */
	public void initITAExaminer(RequestContext context) {
		ITAExaminerBO ita = (ITAExaminerBO) context.getFlowScope().get("itaExaminer");
		ita.setPersonBO(new PersonBO());

		// ita.getPersonBO().setMarkText(new AddressView().getMarkText());
	}

	/**
	 * Used to retrieves office location codes and descriptions from table roms_lmis_ta_office_location_view
	 * 
	 * @param context
	 * @return
	 */
	public Event getOfficeLocation(RequestContext context) {

		List<RefCodeBO> refcodes = null;
		List<SelectItem> officeLocationList = new ArrayList<SelectItem>();
		List<SelectItem> officeLocationListAll = new ArrayList<SelectItem>();

		try {
			refcodes = getRefInfo("roms_lmis_ta_office_location_view");

			for (RefCodeBO codeBo : refcodes) {

				officeLocationListAll.add(new SelectItem(codeBo.getCode(), codeBo.getDescription()));
				
				if (codeBo.getStatus().equals("A")) {
					officeLocationList.add(new SelectItem(codeBo.getCode(), codeBo.getDescription()));
				}
			}
			context.getFlowScope().put("officeLocationList", officeLocationList);
			context.getFlowScope().put("officeLocationListAll", officeLocationListAll);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return success();
	}

	/**
	 * Returns office location description based on office location code
	 * 
	 * @param officeLocationCode
	 * @return String
	 */
	public String getOfficeLocationDesc(String officeLocationCode) {
		List<RefCodeBO> refcodes = null;
		String officeLocation = "";

		refcodes = getRefInfo("roms_lmis_ta_office_location_view");

		if (refcodes != null) {
			for (RefCodeBO codeBo : refcodes) {
				if (StringUtil.stringsEqual(officeLocationCode, codeBo.getCode())) {
					officeLocation = codeBo.getDescription();
					break;
				}
			}
		}
		return officeLocation;
	}

	/**
	 * Used to retrieves location codes and descriptions from table roms_location
	 * 
	 * @param context
	 * @return
	 */
	public Event getLocations(RequestContext context) {

		List<RefCodeBO> refcodes = null;
		List<SelectItem> locationList = new ArrayList<SelectItem>();

		try {

			refcodes = getRefInfo("roms_location");
			
			for (RefCodeBO codeBo : refcodes) {
				locationList.add(new SelectItem(codeBo.getCode(), codeBo.getShortDescription()));
			}

			context.getFlowScope().put("locationList", locationList);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return success();
	}

	public void cascadeLocations() {
		RequestContext context = RequestContextHolder.getRequestContext();
		// System.err.println("get Location list called");
		LocationCriteriaBO criteria = (LocationCriteriaBO) context.getFlowScope().get("criteria");
		List<SelectItem> locationList = new ArrayList<SelectItem>();
		List<RefCodeBO> refcodes = null;
		HashMap<String, String> fields = new HashMap<String, String>();
		// System.out.println("Parish code frm criteria artery " + criteria.getParishCode());

		try {
			if (StringUtils.isBlank(criteria.getParishCode())) {
				/* refcodes = getRefInfo("roms_location"); for (RefCodeBO codeBo : refcodes) { locationList.add(new
				 * SelectItem(codeBo.getCode(), codeBo .getDescription())); } context.getFlowScope().put("locationList",
				 * locationList); */
				getLocations(context);
			} else {
				fields.put("parish_code", criteria.getParishCode());
				refcodes = getRefInfo("roms_location", fields);
				if (refcodes != null) {
					for (RefCodeBO codeBo : refcodes) {
						locationList.add(new SelectItem(codeBo.getCode(), codeBo.getShortDescription()));
					}
				}
				context.getFlowScope().put("locationList", locationList);
			}
		} catch (Exception e) {
			List<SelectItem> locationEmptyList = new ArrayList<SelectItem>();
			context.getFlowScope().put("locationList", locationEmptyList);
			e.printStackTrace();
		}
	}

	/**
	 * Used to retrieves location codes and descriptions from table roms_location
	 * 
	 * @param context
	 * @return
	 */
	public Event getCourts(RequestContext context) {

		List<RefCodeBO> refcodes = null;
		List<SelectItem> courtList = new ArrayList<SelectItem>();

		try {

			refcodes = getRefInfo("roms_court");

			Collections.sort(refcodes, new Comparator<RefCodeBO>() {
				public int compare(RefCodeBO result1, RefCodeBO result2) {
					return result1.getShortDescription().toLowerCase().compareTo(result2.getShortDescription().toLowerCase());
				}
			});

			for (RefCodeBO codeBo : refcodes) {
				if (codeBo.getShortDescription() != null) {

					courtList.add(new SelectItem(codeBo.getShortDescription(), codeBo.getShortDescription()));
				}

			}

			context.getFlowScope().put("courtList", courtList);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return success();
	}
	
	public Event getCourtsByParish(String parishCode) {
		RequestContext context = RequestContextHolder.getRequestContext();
		CourtCriteriaBO courtCriteria = new CourtCriteriaBO();
		courtCriteria.setStatusId(Constants.Status.ACTIVE);
		
		Set<String> selParCodes = new HashSet<String>();
		//if Kingston parish is in the list, add St. Andrew parish and vice versa
		try
		{
			if(parishCode.equals(Constants.ParishKingstonAndStAndrew.KINGSTON) || parishCode.equals(Constants.ParishKingstonAndStAndrew.ST_ANDREW)){
				selParCodes = Constants.MergeKingstonAndStAndrew();
			}else{
				selParCodes.add(parishCode);
			}
		}
		catch(Exception e)
		{
			logger.error("Base Service Action",e);
		}
		List<CourtBO> listCourt = new ArrayList<CourtBO>();
		
		for (String string : selParCodes) {
			courtCriteria.setParishCode(string);
			try {
				listCourt.addAll(getMaintenanceService().lookupCourt(courtCriteria));
			} catch (fsl.ta.toms.roms.exception.NoRecordFoundException e) {
				logger.error("Base Service Action",e);

			} catch (fsl.ta.toms.roms.exception.RequiredFieldMissingException e) {
				logger.error("Base Service Action",e);

			}
		}	

		context.getFlowScope().put("courtsByParish", listCourt);

		return success();
	}
	
	

	/**
	 * Used to retrieves Reason codes and descriptions from table roms_reason_type
	 * 
	 * @param context
	 * @return
	 */
	public Event getReasonCodes(RequestContext context) {

		List<RefCodeBO> refcodes = null;
		List<SelectItem> reasonCodes = new ArrayList<SelectItem>();

		try {

			refcodes = getRefInfo("roms_reason_type");

			for (RefCodeBO codeBo : refcodes) {
				// System.out.println("status " + codeBo.getStatus());
				if (codeBo.getStatus().equalsIgnoreCase("Active"))
					reasonCodes.add(new SelectItem(codeBo.getCode(), codeBo.getDescription()));
			}

			if (refcodes == null || refcodes.isEmpty()) {
				addWarningMessageText(context, "No Reasons exist. Please add Reasons before continuing.");
			}

			context.getFlowScope().put("reasonCodesList", reasonCodes);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return success();
	}

	public String getReasonDescriptions(String val) {

		List<RefCodeBO> refcodes = null;
		HashMap<String, String> codeDesc = new HashMap<String, String>();

		try {

			refcodes = getRefInfo("roms_reason_type");

			for (RefCodeBO codeBo : refcodes) {
				// System.out.println("status " + codeBo.getStatus());
				if (codeBo.getStatus().equalsIgnoreCase("Active"))
					codeDesc.put(codeBo.getCode(), codeBo.getDescription());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return codeDesc.get(val);
	}

	/**
	 * Used to retrieves location codes and descriptions from table roms_location
	 * 
	 * @param context
	 * @return
	 */
	public Event getLocationsFilterbyParishCode(RequestContext context, String parishCode) {

		List<RefCodeBO> refcodes = null;
		List<SelectItem> locationList = new ArrayList<SelectItem>();

		try {
			HashMap filter = new HashMap<String, String>();
			filter.put("parish_code", parishCode);

			refcodes = getRefInfo("roms_location", filter);

			for (RefCodeBO codeBo : refcodes) {
				locationList.add(new SelectItem(codeBo.getCode(), codeBo.getDescription()));
			}

			context.getFlowScope().put("locationList", locationList);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return success();
	}

	/**
	 * Used to retrieves Road Check Type codes and descriptions from table roms_cd_road_check_type
	 * 
	 * @param context
	 * @return
	 */
	public Event getRoadCheckType(RequestContext context) {
		List<RefCodeBO> refcodes = null;
		List<SelectItem> roadCheckList = new ArrayList<SelectItem>();
		
		try {
			refcodes = getRefInfo("roms_cd_road_check_type");

			Collections.sort(refcodes, new Comparator<RefCodeBO>() {
				public int compare(RefCodeBO result1, RefCodeBO result2) {
					return result1.getDescription().toLowerCase().compareTo(result2.getDescription().toLowerCase());
				}
			});
			
			for (RefCodeBO codeBo : refcodes) {
				roadCheckList.add(new SelectItem(codeBo.getCode(), codeBo.getDescription()));
			}
			context.getFlowScope().put("roadCheckList", roadCheckList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return success();
	}

	/**
	 * Used to retrieve Governing Laws from table governingLaw
	 * 
	 * @param context
	 * @return
	 */
	public Event getGoverningLaws(RequestContext context) {
		/* List<GoverningLawBO> lawSource = new ArrayList<GoverningLawBO>(); List<GoverningLawBO> lawTarget = new
		 * ArrayList<GoverningLawBO>(); */
		List<RefCodeBO> refcodes = null;
		// List<RefCodeBO> governingLawsList = new ArrayList<RefCodeBO>();
		// DualList<GoverningLawBO> laws = new
		// DualList<GoverningLawBO>(source,target);
		try {
			refcodes = getRefInfo("roms_governing_law");

			/* for(RefCodeBO codeBo : refcodes) { governingLawsList.add(new SelectItem(codeBo.getCode(),
			 * codeBo.getShortDescription())); } */
			context.getFlowScope().put("governingLawsList", refcodes);

		} catch (Exception e) {

			e.printStackTrace();
		}
		return success();
	}

	/**
	 * Used to retrieve Offence Params
	 * 
	 * @param context
	 * @return
	 */
	public Event getExcerptOffenceParams(RequestContext context) {
		/* List<GoverningLawBO> lawSource = new ArrayList<GoverningLawBO>(); List<GoverningLawBO> lawTarget = new
		 * ArrayList<GoverningLawBO>(); */
		List<RefCodeBO> refcodes = null;
		// List<RefCodeBO> governingLawsList = new ArrayList<RefCodeBO>();
		// DualList<GoverningLawBO> laws = new
		// DualList<GoverningLawBO>(source,target);
		List<ExcerptParamMappingBO> offenceParamList = new ArrayList<ExcerptParamMappingBO>();
		try {
			refcodes = getRefInfo("roms_excerpt_param_mapping");

			/* for(RefCodeBO codeBo : refcodes) { governingLawsList.add(new SelectItem(codeBo.getCode(),
			 * codeBo.getShortDescription())); } */
			// offenceParamList = getMaintenancePortProxy().getOffenceParams(offenceId)
			context.getFlowScope().put("excerptOffenceParamsList", refcodes);

		} catch (Exception e) {

			e.printStackTrace();
		}
		return success();
	}

	/**
	 * Used to retrieves Strategy codes and descriptions from table roms_strategy
	 * 
	 * @param context
	 * @return
	 */
	public Event getStrategies(RequestContext context) {
		List<RefCodeBO> refcodes = null;
		List<SelectItem> roadCheckList = new ArrayList<SelectItem>();

		try {
			refcodes = getRefInfo("roms_strategy");

			Collections.sort(refcodes, new Comparator<RefCodeBO>() {
				public int compare(RefCodeBO result1, RefCodeBO result2) {
					return result1.getDescription().toLowerCase().compareTo(result2.getDescription().toLowerCase());
				}
			});
			
			
			for (RefCodeBO codeBo : refcodes) {
				roadCheckList.add(new SelectItem(codeBo.getCode(), codeBo.getDescription()));
			}
			context.getFlowScope().put("strategyList", roadCheckList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return success();
	}

	/**
	 * Returns Reference codes based on the table name.
	 * 
	 * @param tableName
	 * @return RefCodeBO
	 */
	protected List<RefCodeBO> getRefInfo(String tableName) {
		List<RefCodeBO> refcodes = null;

		if (StringUtils.isBlank(tableName)) {
			return refcodes;
		}
		try {
			RefCodeCriteriaBO codes = new RefCodeCriteriaBO();

			codes.setTableName(tableName);

			refcodes = getReferenceCodeService().getReferenceCode(codes);
			if (refcodes != null) {
				Collections.sort(refcodes, new Comparator<RefCodeBO>() {
					public int compare(RefCodeBO result1, RefCodeBO result2) {
						return result1.getShortDescription().toLowerCase().compareTo(result2.getShortDescription().toLowerCase());
					}
				});
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return refcodes;
	}

	
	/**
	 * Returns Reference codes based on the table name.
	 * 
	 * @param tableName
	 * @return RefCodeBO
	 */
	protected List<RefCodeBO> getActivRefInfo(String tableName) {
		List<RefCodeBO> refcodes = null;

		if (StringUtils.isBlank(tableName)) {
			return refcodes;
		}
		try {
			RefCodeCriteriaBO codes = new RefCodeCriteriaBO();

			codes.setTableName(tableName);
			codes.setStatus(Constants.Status.ACTIVE);

			refcodes = getReferenceCodeService().getReferenceCode(codes);
			if (refcodes != null) {
				Collections.sort(refcodes, new Comparator<RefCodeBO>() {
					public int compare(RefCodeBO result1, RefCodeBO result2) {
						return result1.getShortDescription().toLowerCase().compareTo(result2.getShortDescription().toLowerCase());
					}
				});
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return refcodes;
	}
	
	/**
	 * Returns Reference codes based on the table name.
	 * 
	 * @param tableName
	 * @return RefCodeBO
	 */
	public List<RefCodeBO> getRefInfo(String tableName, HashMap queryFields) {
		List<RefCodeBO> refcodes = null;

		if (StringUtils.isBlank(tableName)) {
			return refcodes;
		}
		try {
			HashMap<String, String> filter = new HashMap<String, String>();
			
			Iterator iterator = queryFields.entrySet().iterator();

			while (iterator.hasNext()) {

				Map.Entry pairs = (Map.Entry) iterator.next();

				/*Entry entry = new Entry();
				entry.setKey((String) pairs.getKey());
				entry.setValue((String) pairs.getValue());*/

				filter.put((String) pairs.getKey(), (String) pairs.getValue());
			}

			RefCodeCriteriaBO codes = new RefCodeCriteriaBO();

			codes.setTableName(tableName);
			codes.setFilter(filter);

			refcodes = getReferenceCodeService().getReferenceCode(codes);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return refcodes;
	}

	/**
	 * Used to retrieve courts
	 * 
	 * @param context
	 * @return
	 */
	public Event getActiveCourts(RequestContext context) {

		List<CourtBO> courtList = new ArrayList<CourtBO>();
		CourtCriteriaBO courtCriteria = new CourtCriteriaBO();
		courtCriteria.setStatusId(Constants.Status.ACTIVE);

		try {
			courtList = getMaintenanceService().lookupCourt(courtCriteria);
			Collections.sort(courtList, new Comparator<CourtBO>() {
				@Override
				public int compare(CourtBO result1, CourtBO result2) {
					return result1.getShortDescription().toLowerCase().compareTo(result2.getShortDescription().toLowerCase());
				}
			});
			
		} catch (fsl.ta.toms.roms.exception.NoRecordFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (fsl.ta.toms.roms.exception.RequiredFieldMissingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		context.getFlowScope().put("courtList", courtList);

		return success();
	}

	/**
	 * Used to retrieve Parishes based on regions
	 * 
	 * @param
	 * @return
	 */

	public void getParishListfrmRegion() {
		RequestContext context = RequestContextHolder.getRequestContext();
		ITAExaminerBO itaExaminer = (ITAExaminerBO) context.getFlowScope().get("itaExaminer");
		// ItaExaminerCriteriaBO criteria = (ItaExaminerCriteriaBO) context.getFlowScope().get("criteria");

		List<SelectItem> parishList = new ArrayList<SelectItem>();
		List<RefCodeBO> refcodes = null;
		HashMap<String, String> fields = new HashMap<String, String>();
		
		logger.info("Parish code " + itaExaminer.getOfficeLocationCode());

		try {
			if(StringUtils.isBlank(itaExaminer.getOfficeLocationCode()) || !isRegionActive())
			{
				//getParishes(context);
				context.getFlowScope().put("parishList", parishList);
				context.getFlowScope().put("disable", "true");
			}else{
				if(!StringUtils.isEmpty(itaExaminer.getOfficeLocationCode())){
				fields.put("office_loc_code", itaExaminer.getOfficeLocationCode());
				refcodes = getRefInfo("roms_parish", fields);
				}else{
					refcodes = getRefInfo("roms_parish");
				}
			
				for (RefCodeBO codeBo : refcodes) {
					parishList.add(new SelectItem(codeBo.getCode(), codeBo
							.getDescription()));
					
				}
				context.getFlowScope().put("parishList", parishList);
				context.getFlowScope().put("disable", "false");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	@SuppressWarnings("unchecked")
	public Boolean isRegionActive(){
		RequestContext context = RequestContextHolder.getRequestContext();
		ITAExaminerBO itaExaminer = (ITAExaminerBO) context.getFlowScope().get("itaExaminer");
		List<SelectItem> activeLocations = (ArrayList<SelectItem>)context.getFlowScope().get("officeLocationList");
		logger.info("Locations " + activeLocations.get(0).getValue());
		for(int x = 0;x< activeLocations.size();x++){
			if(activeLocations.get(x).getValue().equals(itaExaminer.getOfficeLocationCode())){
				logger.info("Inside is if");
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * 
	 * @param context
	 * @return
	 */
	public Event getCourtListByUserRegion(RequestContext context) {
		List<CourtBO> courtList = new ArrayList<CourtBO>();

		CourtCriteriaBO courtCriteria = new CourtCriteriaBO();
		courtCriteria.setStatusId(Constants.Status.ACTIVE);

		List<RefCodeBO> parishes = null;
		parishes = getRefInfo("roms_parish");

		if (parishes != null) {
			for (RefCodeBO parish : parishes) {

				if (parish.getAltId().equalsIgnoreCase(getRomsLoggedInUser().getLocationCode())) {
					// System.err.println(" pulling for parish " + parish.getCode());
					List<CourtBO> courtList2 = null;
					courtCriteria.setParishCode(parish.getCode());
					try {
						courtList2 = getMaintenanceService().lookupCourt(courtCriteria);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					courtList.addAll(courtList2);
				}
			}
		}

		// sort the list alphabetically
		if (courtList != null && !courtList.isEmpty()) {
			Collections.sort(courtList, new Comparator<CourtBO>() {
				public int compare(CourtBO result1, CourtBO result2) {
					return result1.getDescription().toLowerCase().compareTo(result2.getDescription().toLowerCase());
				}
			});
		}

		context.getFlowScope().put("courtListByParish", courtList);
		return success();
	}

	/**
	 * Used to retrieve JPS from table roms_strategy
	 * 
	 * @param context
	 * @return
	 */
	public Event getJusticesOfPeace(RequestContext context) {

		List<JPBO> jpList = new ArrayList<JPBO>();
		JPCriteriaBO jpCriteria = new JPCriteriaBO();
		jpCriteria.setStatusId(Constants.Status.ACTIVE);

		Maintenance maintenanceService = getMaintenanceService();

		try {
			jpList = maintenanceService.lookupJP(jpCriteria);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (jpList == null || jpList.isEmpty()) {
			addWarningMessageText(context, "No JPs exist. Please add JPs before continuing.");
		}
		context.getFlowScope().put("jpList", jpList);

		return success();
	}

	/**
	 * Used to retrieve Reasons based of TYPE from table roms_reason
	 * 
	 * @param context
	 * @return
	 */
	public List<SelectItem> getReasonsByModuleType(String moduleType) {
		RequestContext context = RequestContextHolder.getRequestContext();
		List<RefCodeBO> refcodes = null;
		List<SelectItem> reasonList = new ArrayList<SelectItem>();

		try {
			HashMap<String, String> fields = new HashMap<String, String>();
			fields.put("reason_type_code", moduleType);

			refcodes = getRefInfo("roms_reason", fields);

			if (refcodes != null && !refcodes.isEmpty()) {
				for (RefCodeBO codeBo : refcodes) {
					reasonList.add(new SelectItem(codeBo.getCode(), codeBo.getDescription()));
				}

				context.getFlowScope().put("reasonList", reasonList);
			} else {
				context.getFlowScope().put("reasonList", reasonList);
				addWarningMessageText(context, "No Reasons exist. Please add Reasons before continuing.");

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return reasonList;
	}

	public List<SelectItem> getActiveReasonsByModuleType(String moduleType) {
		RequestContext context = RequestContextHolder.getRequestContext();
		List<RefCodeBO> refcodes = null;
		List<SelectItem> reasonList = new ArrayList<SelectItem>();

		try {
			HashMap<String, String> fields = new HashMap<String, String>();
			fields.put("reason_type_code", moduleType);
			fields.put("status_id", Constants.Status.ACTIVE);

			refcodes = getRefInfo("roms_reason", fields);

			if (refcodes != null && !refcodes.isEmpty()) {
				for (RefCodeBO codeBo : refcodes) {
					reasonList.add(new SelectItem(codeBo.getCode(), codeBo.getDescription()));
				}

				context.getFlowScope().put("reasonList", reasonList);
			} else {
				context.getFlowScope().put("reasonList", reasonList);
				addWarningMessageText(context, "No Reasons exist. Please add Reasons before continuing.");

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return reasonList;
	}

	/**
	 * Used to retrieve statuses based of TYPE from table roms_location Used to retrieve statuses based of TYPE from
	 * table roms_location
	 * 
	 * @param context
	 * @return
	 */
	/* public Event getStatusesByModuleType(RequestContext context) { List<RefCodeBO> refcodes = null; List<SelectItem>
	 * statusList = new ArrayList<SelectItem>(); try { HashMap<String, String> fields = new HashMap<String, String>();
	 * //fields.put("type", "Summons"); refcodes = getRefInfo("roms_cd_status", fields); for (RefCodeBO codeBo :
	 * refcodes) { statusList.add(new SelectItem(codeBo.getCode(), codeBo .getDescription())); }
	 * context.getFlowScope().put("statusList", statusList); } catch (Exception e) { } return success(); } */

	/**
	 * Used to retrieve statuses based of TYPE from table roms_location
	 * 
	 * @param context
	 * @return
	 */
	public void getStatusesByModuleType(AjaxBehaviorEvent actionEvent) {
		RequestContext context = RequestContextHolder.getRequestContext();
		List<RefCodeBO> refcodes = null;
		SelectOneMenu selected = (SelectOneMenu) actionEvent.getSource();

		try {
			HashMap<String, String> fields = new HashMap<String, String>();
			fields.put("type", (String) selected.getValue());

			refcodes = getRefInfo("roms_cd_status", fields);

			context.getFlowScope().put("statusList", refcodes);

		} catch (Exception e) {
			e.printStackTrace();
		}
		// return success();
	}

	/**
	 * This function places a list of TA Staff in a a drop down list with the TA-Staff ID as the key while their proper
	 * name and id is used for display value.
	 * 
	 * @param context
	 */
	public void getStaffList(RequestContext context) {

		List<BIMSStaffViewBO> staffLists = new ArrayList<BIMSStaffViewBO>();

		try {
			staffLists = getStaffUserMappingService().lookupActiveStaff();

			if (staffLists == null || staffLists.isEmpty()) {
				addWarningMessageText(context, "No Staff exist. Please add Staff before continuing.");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		List<SelectItem> staffList = new ArrayList<SelectItem>();

		for (BIMSStaffViewBO staff : staffLists) {

			String label = staff.getFullName() + " [" + staff.getStaffId() + "]";

			String value = staff.getStaffId();

			staffList.add(new SelectItem(value, label));
		}

		context.getFlowScope().put("staffDropDown", staffList);

	}

	/**
	 * This function places a list of TA Staff in a a drop down list with the TA-Staff ID as the key while their proper
	 * name and id is used for display value.
	 * 
	 * @param context
	 */
	public void getLMISUSers(RequestContext context) {

		List<LMISUserViewBO> staffLists = new ArrayList<LMISUserViewBO>();

		try {
			staffLists = getStaffUserMappingService().lookupActiveUsers();

		} catch (Exception e) {
			e.printStackTrace();
		}

		List<SelectItem> staffList = new ArrayList<SelectItem>();

		for (LMISUserViewBO staff : staffLists) {

			String label = staff.getLastName() + ", " + staff.getFirstName() + " [" + staff.getUsername() + "]";

			String value = staff.getUsername();

			staffList.add(new SelectItem(value, label));
		}

		context.getFlowScope().put("staffDropDown", staffList);

	}

	public Event getTAStaffList(RequestContext context) {

		List<TAStaffBO> searchListStaff = new ArrayList<TAStaffBO>();

		try {
			searchListStaff = getRoadOperationService().getAllROMSStaff();
		} catch (NoRecordFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Collections.sort(searchListStaff, new Comparator<TAStaffBO>() {
			public int compare(TAStaffBO result1, TAStaffBO result2) {
				return result1.getFullName().compareTo(result2.getFullName());
			}
		});
		context.getFlowScope().put("staffList", searchListStaff);

		return success();
	}

	@SuppressWarnings("unchecked")
	public List<String> taStaffAutoComplete(String term) {

		RequestContext context = RequestContextHolder.getRequestContext();

		List<TAStaffBO> taStaffList = (List<TAStaffBO>) context.getFlowScope().get("staffList");

		if (taStaffList == null) {
			getTAStaffList(context);
			taStaffList = (List<TAStaffBO>) context.getFlowScope().get("staffList");
		}

		List<String> returnList = new ArrayList<String>();

		for (TAStaffBO staff : taStaffList) {
			if (staff.getFullName().trim().toLowerCase().contains(term.toLowerCase())) {
				returnList.add(staff.getStaffId());
			}
		}

		return returnList;
	}

	/**
	 * 
	 * @param context
	 * @return
	 */
	public Event getTAOfficeRegions(RequestContext context) {

		// HashMap<String, String> filter = new HashMap<String, String>();

		// filter.put("loc_code", this.getRomsLoggedInUser().getLocationCode());
		List<RefCodeBO> listOfficeRegions = this.getRefInfo("roms_lmis_ta_office_location_view");

		List<SelectItem> selectListOfficeRegions = new ArrayList<SelectItem>();

		for (RefCodeBO refCode : listOfficeRegions) {
			selectListOfficeRegions.add(new SelectItem(refCode.getCode(), refCode.getDescription()));

		}

		context.getFlowScope().put("officeRegionList", selectListOfficeRegions);

		return success();
	}

	/**
	 * 
	 * @param context
	 * @return
	 */
	public Event getCategoryList(RequestContext context) {
		List<RefCodeBO> listCatogries = this.getRefInfo("roms_cd_category");

		List<SelectItem> selectListCatagories = new ArrayList<SelectItem>();

		if (listCatogries != null) {

			for (RefCodeBO refCode : listCatogries) {
				selectListCatagories.add(new SelectItem(refCode.getCode(), refCode.getDescription()));

			}
		}
		context.getFlowScope().put("categoryList", selectListCatagories);
		return success();
	}
	
	
	public List<SelectItem> getOtherRoleList() {
		List<RefCodeBO> listOtherRole = this.getRefInfo("roms_cd_other_role_observed");

		Collections.sort(listOtherRole, new Comparator<RefCodeBO>() {
			public int compare(RefCodeBO result1, RefCodeBO result2) {
				return result1.getDescription().toLowerCase().compareTo(result2.getDescription().toLowerCase());
			}
		});
		
		
		List<SelectItem> selectListOtherRole = new ArrayList<SelectItem>();

		if (listOtherRole != null) {

			for (RefCodeBO refCode : listOtherRole) {
				selectListOtherRole.add(new SelectItem(refCode.getCode(), refCode.getDescription()));

			}
		}
		//context.getFlowScope().put("otherRoleList", selectListOtherRole);
		return selectListOtherRole;
	}
	
	

	/**
	 * 
	 * @param context
	 * @return
	 */
	public Event getStatusList(RequestContext context) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("type", "road operation");

		List<RefCodeBO> listStatus = this.getRefInfo("roms_cd_status", map);

		List<SelectItem> selectListStatus = new ArrayList<SelectItem>();

		for (RefCodeBO refCode : listStatus) {
			selectListStatus.add(new SelectItem(refCode.getCode(), refCode.getDescription()));

		}

		context.getFlowScope().put("statusList", selectListStatus);
		return success();
	}

	/**
	 * 
	 * @param context
	 * @return
	 */
	public Event getVehcileMoversList(RequestContext context) {

		List<RefCodeBO> personTypeList = this.getRefInfo("roms_cd_person_type");

		Collections.sort(personTypeList, new Comparator<RefCodeBO>() {
			public int compare(RefCodeBO result1, RefCodeBO result2) {
				return result1.getDescription().toLowerCase().compareTo(result2.getDescription().toLowerCase());
			}
		});
		
		
		List<SelectItem> selectListVehicleMovers = new ArrayList<SelectItem>();

		for (RefCodeBO personType : personTypeList) {
			if (personType.getStatus().toLowerCase().startsWith("act") && !personType.getCode().toLowerCase().startsWith("df") && !personType.getCode().toLowerCase().startsWith("jp") && !personType.getCode().toLowerCase().startsWith("wt"))
				selectListVehicleMovers.add(new SelectItem(personType.getCode(), personType.getDescription()));

		}

		context.getFlowScope().put("vehcileMoversList", selectListVehicleMovers);

		return success();
	}

	/**
	 * 
	 * @param type
	 * @return
	 */
	public Event getStatusListByType(String type) {
		RequestContext context = RequestContextHolder.getRequestContext();

		List<RefCodeBO> refcodes = null;
		List<SelectItem> statusList = new ArrayList<SelectItem>();

		try {
			HashMap<String, String> fields = new HashMap<String, String>();
			fields.put("type", type);

			refcodes = getRefInfo("roms_cd_status", fields);

			for (RefCodeBO codeBo : refcodes) {
				statusList.add(new SelectItem(codeBo.getCode(), codeBo.getDescription()));
			}

			context.getFlowScope().put("statusList", statusList);

		} catch (Exception e) {
		}
		return success();
	}

	/**
	 * 
	 * @param context
	 */
	public void getComplianceStatusList(RequestContext context) {
		this.getStatusListByType(Constants.StatusType.ROAD_CHECK);
	}

	/**
	 * Retrieves parish codes and descriptions from table roms_parish
	 * 
	 * @param context
	 * @return
	 */
	public Event getCourtRulings(RequestContext context) {
		List<RefCodeBO> refcodes = null;
		List<SelectItem> courtRulingList = new ArrayList<SelectItem>();

		try {
			refcodes = getRefInfo("roms_cd_court_ruling");

			Collections.sort(refcodes, new Comparator<RefCodeBO>() {
				public int compare(RefCodeBO result1, RefCodeBO result2) {
					return result1.getDescription().toLowerCase().compareTo(result2.getDescription().toLowerCase());
				}
			});
			
			
			for (RefCodeBO codeBo : refcodes) {
				courtRulingList.add(new SelectItem(codeBo.getCode(), codeBo.getDescription()));
			}

			if (refcodes == null || refcodes.isEmpty()) {
				addWarningMessageText(context, "No Court Ruling exist. Please add Court Ruling before continuing.");
			}

			context.getFlowScope().put("courtRulingList", courtRulingList);

		} catch (Exception e) {
		}
		return success();
	}

	/**
	 * 
	 * @param context
	 * @return
	 */
	public Event getCourtRulingsListForCourtCaseScreen(RequestContext context) {
		List<RefCodeBO> refcodes = null;

		try {
			HashMap filter = new HashMap<String, String>();
			filter.put("status_id", Constants.Status.ACTIVE);

			refcodes = getRefInfo("roms_cd_court_ruling", filter);

			if (refcodes == null || refcodes.isEmpty()) {
				addWarningMessageText(context, "No Court Ruling exist. Please add Court Ruling before continuing.");
			}

			context.getFlowScope().put("courtRulingListForUpdate", refcodes);

		} catch (Exception e) {
		}
		return success();
	}

	/**
	 * 
	 * @param context
	 * @return
	 */
	public boolean isCourtRulingFinal(String rulingID) {
		List<RefCodeBO> refcodes = null;
		if (StringUtils.isBlank(rulingID))
			return false;

		refcodes = getRefInfo("roms_cd_court_ruling");
		if (refcodes != null && !refcodes.isEmpty()) {
			for (RefCodeBO code : refcodes) {
				if (code.getCode().equalsIgnoreCase(rulingID)) {
					if (code.getAltId().equalsIgnoreCase(Constants.YesNo.YES_LABEL_STR)) {
						return true;
					}
				}
			}
		}

		return false;
	}
	
	public void FilterCourtRulings(Boolean isFinal){
		RequestContext context = RequestContextHolder.getRequestContext();
		List<RefCodeBO> refcodes = null;

		try {
			HashMap filter = new HashMap<String, String>();
			filter.put("status_id", Constants.Status.ACTIVE);
			if(isFinal)
				filter.put("final_ruling", "Y");
			else
				filter.put("final_ruling", "N");

			refcodes = getRefInfo("roms_cd_court_ruling", filter);

			if (refcodes == null || refcodes.isEmpty()) {
				addWarningMessageText(context, "No Court Ruling exist. Please add Court Ruling before continuing.");
			}

			context.getFlowScope().put("courtRulingList", refcodes);

		} catch (Exception e) {
		}
	}
	
	/**
	 * 
	 * @param context
	 * @return
	 */
	public void isFirstCourtAppearance (RequestContext context) 
	{
		boolean isFirstCourtAppearance = false;
		SummonsBO summonsBO = (SummonsBO) context.getFlowScope().get("summons");

		if (summonsBO == null) {
			addErrorMessageText(context, "Summons  is empty.");
			isFirstCourtAppearance = false;
		}
		else{
			CourtCaseBO courtCase = (CourtCaseBO) summonsBO.getCourtCase();
	
			if (courtCase == null) {
				addErrorMessageText(context, "Court case is empty.");
				isFirstCourtAppearance = false;
			}
			else{
				List<CourtAppearanceBO> courtAppList = courtCase.getCourtAppearances();
				if (courtAppList.size()==1){
					isFirstCourtAppearance = true;
				}
			}
		}
		context.getFlowScope().put("isFirstCourtAppearance", isFirstCourtAppearance);
	}
	
	
	/**
	 * 
	 * @param context
	 * @return
	 */
	public String showFinalCourtRulingLabel(String rulingID) {

		if (isCourtRulingFinal(rulingID)) {
			return Constants.YesNo.YES_LABEL_STR;
		}


		return Constants.YesNo.NO_LABEL_STR;
	}

	/**
	 * 
	 * @param context
	 * @return
	 */
	public Event getVerdicts(RequestContext context) {
		List<RefCodeBO> refcodes = null;
		List<SelectItem> parishList = new ArrayList<SelectItem>();

		try {
			// refcodes = getRefInfo("roms_cd_verdict");

			HashMap filter = new HashMap<String, String>();
			filter.put("status_id", Constants.Status.ACTIVE);

			refcodes = getRefInfo("roms_cd_verdict", filter);
			if (refcodes == null || refcodes.isEmpty()) {
				addWarningMessageText(context, "No Verdict exist. Please add Verdicts before continuing.");
			}
			for (RefCodeBO codeBo : refcodes) {
				parishList.add(new SelectItem(codeBo.getCode(), codeBo.getDescription()));
			}
			context.getFlowScope().put("verdictList", parishList);

		} catch (Exception e) {
		}
		return success();
	}

	/**
	 * 
	 * @param context
	 * @return
	 */
	public Event getPleas(RequestContext context) {
		List<RefCodeBO> refcodes = null;
		List<SelectItem> parishList = new ArrayList<SelectItem>();

		try {
			// refcodes = getRefInfo("roms_cd_plea");
			HashMap filter = new HashMap<String, String>();
			filter.put("status_id", Constants.Status.ACTIVE);

			refcodes = getRefInfo("roms_cd_plea", filter);


			for (RefCodeBO codeBo : refcodes) {
				parishList.add(new SelectItem(codeBo.getCode(), codeBo.getDescription()));
			}

			if (refcodes == null || refcodes.isEmpty()) {
				addWarningMessageText(context, "No Pleas exist. Please add Pleas before continuing.");
			}

			context.getFlowScope().put("pleaList", parishList);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return success();
	}

	/**
	 * 
	 * @param region
	 * @return
	 */
	public Event getTAStaffListByRegion(String region) {

		RequestContext context = RequestContextHolder.getRequestContext();

		StaffUserMappingCriteriaBO criteria = new StaffUserMappingCriteriaBO();
		criteria.setOfficeLocationCode(region);

		List<StaffUserMappingBO> staffLists = new ArrayList<StaffUserMappingBO>();
		List<SelectItem> staffListsS = new ArrayList<SelectItem>();

		try {
			staffLists = getStaffUserMappingService().lookupStaffUserMappings(criteria);

			if (staffLists == null || staffLists.isEmpty()) {
				staffListsS = new ArrayList<SelectItem>();

				addWarningMessageText(context, "No Staff exist for this region. Please add Staff before continuing.");

			} else {

				for (StaffUserMappingBO staff : staffLists) {

					String label = staff.getFullName() + " [" + staff.getStaffId() + "]";

					String value = staff.getStaffId();

					staffListsS.add(new SelectItem(value, label));
				}

			}

			context.getFlowScope().put("staffByRegionList", staffListsS);

		} catch (Exception e) {
			e.printStackTrace();
			return error();
		}

		return success();
	}

	/**
	 * 
	 * @param region
	 * @return
	 */
	public Event getJusticesOfPeaceListByRegion(String region) {

		List<JPBO> jpList = new ArrayList<JPBO>();

		RequestContext context = RequestContextHolder.getRequestContext();

		Maintenance maintenanceProxy = getMaintenanceService();

		try {
			jpList = maintenanceProxy.getJPListByRegion(region);

			if (jpList == null || jpList.isEmpty()) {
				addWarningMessageText(context, "No Jps exist for this region. Please add JPs before continuing.");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		context.getFlowScope().put("jpByRegionList", jpList);

		return success();
	}
	
	public Event getJusticesOfPeaceListByOffenceParish(String parishCode) {

		List<JPBO> jpList = new ArrayList<JPBO>();

		RequestContext context = RequestContextHolder.getRequestContext();

		Maintenance maintenanceProxy = getMaintenanceService();
		
		JPCriteriaBO jpCriteria = new JPCriteriaBO();
		jpCriteria.setStatusId(Constants.Status.ACTIVE);
		
		Set<String> selParCodes = new HashSet<String>();
		//if Kingston parish is in the list, add St. Andrew parish and vice versa
		if(parishCode.equals(Constants.ParishKingstonAndStAndrew.KINGSTON) || parishCode.equals(Constants.ParishKingstonAndStAndrew.ST_ANDREW)){
			selParCodes = Constants.MergeKingstonAndStAndrew();
		}else{
			selParCodes.add(parishCode);
		}

		for (String string : selParCodes) {
			jpCriteria.setParishCode(string);
			try {
				jpList.addAll(maintenanceProxy.lookupJP(jpCriteria));			
			} catch (Exception e) {
				
			}
		}
		
		if (jpList == null || jpList.isEmpty()) {
			addWarningMessageText(context, "No Jps exist for this parish. Please add JPs before continuing.");
		}
		else
			
		
		Collections.sort(jpList, new Comparator<JPBO>() {
			public int compare(JPBO result1, JPBO result2) {
				return result1.getPersonBO().getLastName().trim().compareToIgnoreCase(result2.getPersonBO().getLastName().trim());
			}
		});
		
		context.getFlowScope().put("jpList", jpList);

		return success();
	}

	/**
	 * Retrieves parish codes and descriptions from table roms_parish
	 * 
	 * @param context
	 * @return
	 */
	public Event getScannedDocumentTypes(RequestContext context) {
		List<RefCodeBO> refcodes = new ArrayList<RefCodeBO>();
		List<SelectItem> docList = new ArrayList<SelectItem>();

		try {
			refcodes = getRefInfo("roms_cd_document_type");

			if (refcodes != null) {
				for (RefCodeBO codeBo : refcodes) {
					if (!codeBo.getCode().equalsIgnoreCase(ScannedDocumentType.MANUAL_DOCUMENT)) {
						docList.add(new SelectItem(codeBo.getCode(), codeBo.getDescription()));
					}
				}

				context.getFlowScope().put("scanDocTypeList", docList);
			} else {
				context.getFlowScope().put("scanDocTypeList", getDocumentTypeList());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return success();
	}

	public String getScannedDocumentDescriptionByCode(String code) {
		List<SelectItem> refcodes = null;
		// System.err.println(" code " + code);
		if (StringUtils.isBlank(code))
			return "";

		try {
			refcodes = getDocumentTypeList2();

			if (refcodes != null) {
				for (SelectItem codeBo : refcodes) {
					if (codeBo.getValue().toString().equalsIgnoreCase(code)) {
						// System.err.println(" descr " + codeBo.getLabel());
						return codeBo.getLabel();
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/****************************************** Webservice Proxies ********************************/
	/**
	 * 
	 * @return
	 */
	public ReferenceCode getReferenceCodeService() {
		ReferenceCode service = new ReferenceCode();

		return service;
	}

	/**
	 * 
	 * @return
	 */
	public DocumentsManager getDocumentsManagerService() {
		DocumentsManager service = new DocumentsManager();

		return service;
	}

	/**
	 * 
	 * @return
	 */
	public BIMSWebService getBIMSWebService() {
		BIMSWebService service = new BIMSWebService();

		return service;
	}

	/**
	 * 
	 * @return
	 */
	public TRNWebService getTRNWebService() {
		TRNWebService service = new TRNWebService();

		return service;
	}

	/**
	 * 
	 * @return
	 */
	public RoadOperation getRoadOperationService() {
		RoadOperation roadOpService = new RoadOperation();

		
		return roadOpService;

	}

	/**
	 * 
	 * @return
	 */
	public StaffUserMapping getStaffUserMappingService() {
		StaffUserMapping service = new StaffUserMapping();

		return service;

	}

	/**
	 * 
	 * @return
	 */
	public RoadCompliancy getRoadCompliancyService() {
		RoadCompliancy service = new RoadCompliancy();

		return service;

	}

	/**
	 * 
	 * @return
	 */
	public Maintenance getMaintenanceService() {
		Maintenance service = new Maintenance();

		return service;

	}

	/**
	 * 
	 * @return
	 */
	public fsl.ta.toms.roms.webservices.ROMSUserDetails getROMSUserDetailsService() {
		fsl.ta.toms.roms.webservices.ROMSUserDetails service = new fsl.ta.toms.roms.webservices.ROMSUserDetails();

		return service;

	}

	/**
	 * 
	 * @return
	 */
	public AMVSWebService getAMVSWebService() {
		AMVSWebService service = new AMVSWebService();

		return service;

	}

	/**
	 * 
	 * @return
	 */
	public ScannedDocUpload getScannedDocUploadService() {
		ScannedDocUpload service = new ScannedDocUpload();

		return service;

	}

	public Authorization getAuthorizationService() {
		Authorization service = new Authorization();

		return service;

	}

	public Report getReportService() {
		Report service = new Report();

		return service;

	}

	/****************************************** AUTO COMPLETE *************************************/

	/**
	 * 
	 * @param term
	 * @return
	 */
	public List<String> roadOperationNameAutoComplete(String term) {

		RoadOperationCriteriaBO roadOpCriteria = new RoadOperationCriteriaBO();
		roadOpCriteria.setOperationName(term);

		List<String> returnList = new ArrayList<String>();

		List<RoadOperationBO> roadOps = new ArrayList<RoadOperationBO>();

		try {
			roadOps = getRoadOperationService().lookupRoadOperation(roadOpCriteria);

			for (RoadOperationBO roadOp : roadOps)
				returnList.add(roadOp.getOperationName());

		} catch (Exception e) {
			e.printStackTrace();
		}

		return returnList;
	}

	/**
	 * 
	 * @param term
	 * @return
	 */
	public List<StaffUserMappingBO> taStaffNameAutoComplete(String term) {

		StaffUserMappingCriteriaBO criteria = new StaffUserMappingCriteriaBO();
		List<StaffUserMappingBO> returnList = null;
		try {
			this.staffUserMaps = this.getStaffUserMappingService().lookupStaffUserMappings(criteria);

			returnList = new ArrayList<StaffUserMappingBO>();

			for (StaffUserMappingBO staff : staffUserMaps) {
				if (staff.getLastName().trim().toLowerCase().startsWith(term.toLowerCase())) {
					returnList.add(staff);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return returnList;
	}

	/**
	 * 
	 * @param term
	 * @return
	 */
	public List<String> jPNameAutoComplete(String term) {

		JPCriteriaBO criteria = new JPCriteriaBO();
		criteria.setLastName(term);

		List<JPBO> jpLists = new ArrayList<JPBO>();

		try {
			jpLists = getMaintenanceService().lookupJP(criteria);

		} catch (Exception e) {
			e.printStackTrace();
		}

		List<String> returnList = new ArrayList<String>();

		for (JPBO jp : jpLists)
			if (jp.getPersonBO().getLastName().toLowerCase().trim().startsWith(term.toLowerCase())) {
				if (jp.getPersonBO().getMiddleName() != null) {
					returnList.add(jp.getPersonBO().getLastName().toUpperCase() + ", " + jp.getPersonBO().getFirstName() + " " + jp.getPersonBO().getMiddleName() + " [" + jp.getRegNumber() + "]");
				} else {
					jp.getPersonBO().setMiddleName("");
					returnList.add(jp.getPersonBO().getLastName().toUpperCase() + ", " + jp.getPersonBO().getFirstName() + " " + jp.getPersonBO().getMiddleName() + " [" + jp.getRegNumber() + "]");
				}
			}

		return returnList;
	}
	
	/**
	 * 
	 * @param term
	 * @return
	 */
	public List<String> taVehicleAutoComplete(String term) {

		TAVehicleCriteriaBO criteria = new TAVehicleCriteriaBO();
		VehicleBO vehicleBO = new VehicleBO();
		vehicleBO.setPlateRegNo(term);
		criteria.setVehicle(vehicleBO);

		List<TAVehicleBO> taVehicleLists = new ArrayList<TAVehicleBO>();

		try {
			taVehicleLists = getMaintenanceService().lookupTAVehicle(criteria);

		} catch (Exception e) {
			e.printStackTrace();
		}

		List<String> returnList = new ArrayList<String>();

		for (TAVehicleBO vehicle : taVehicleLists){
			if (vehicle.getVehicle().getPlateRegNo().startsWith(term.toLowerCase()))
				returnList.add(vehicle.getVehicle().getPlateRegNo() + "-"+vehicle.getVehicle().getMakeDescription()+" ("+vehicle.getVehicle().getYear()+")");
		}
		return returnList;
	}
	
	/**
	 * 
	 * @param term
	 * @return
	 */
	public List<String> wreckerCoAutoComplete(String term) {

		WreckingCompanyCriteriaBO criteria = new WreckingCompanyCriteriaBO();
		WreckingCompanyBO wreckingBO = new WreckingCompanyBO();
		
		List<WreckingCompanyBO> wreckerLists = new ArrayList<WreckingCompanyBO>();

		try {
			wreckerLists = getMaintenanceService().lookupWreckingCompany(criteria);

		} catch (Exception e) {
			e.printStackTrace();
		}

		List<String> returnList = new ArrayList<String>();

		for (WreckingCompanyBO wreckerCo : wreckerLists){
			if(wreckerCo.getCompanyName().toLowerCase().startsWith(term.toLowerCase()))
				returnList.add(wreckerCo.getCompanyName());
		}
		return returnList;
	}
	
	/**
	 * 
	 * @param term
	 * @return
	 */
	public List<String> poundAutoComplete(String term) {

		PoundCriteriaBO criteria = new PoundCriteriaBO();
		PoundBO poundBO = new PoundBO();
		
		List<PoundBO> poundLists = new ArrayList<PoundBO>();

		try {
			poundLists = getMaintenanceService().lookupPound(criteria);

		} catch (Exception e) {
			e.printStackTrace();
		}

		List<String> returnList = new ArrayList<String>();

		for (PoundBO pound : poundLists){
			if(pound.getPoundName().toLowerCase().startsWith(term.toLowerCase()))
				returnList.add(pound.getPoundName());
		}
		return returnList;
	}
	
	/**
	 * 
	 * @param term
	 * @return
	 */
	public List<String> courtAutoComplete(String term) {

		CourtCriteriaBO criteria = new CourtCriteriaBO();
		CourtBO courtBO = new CourtBO();
		
		List<CourtBO> courtLists = new ArrayList<CourtBO>();

		try {
			courtLists = getMaintenanceService().lookupCourt(criteria);

		} catch (Exception e) {
			e.printStackTrace();
		}

		List<String> returnList = new ArrayList<String>();

		for (CourtBO court : courtLists){
			if (court.getShortDescription().toLowerCase().startsWith(term.toLowerCase()))
				returnList.add(court.getShortDescription());
		}
		return returnList;
	}
	/**
	 * 
	 * @param term
	 * @return
	 */
	public List<String> offenceAutoComplete(String term) {

		OffenceCriteriaBO criteria = new OffenceCriteriaBO();
		
		criteria.setShortDescription(term);

		List<OffenceBO> offenceLists = new ArrayList<OffenceBO>();

		try {
			offenceLists = getMaintenanceService().lookupOffence(criteria);

		} catch (Exception e) {
			e.printStackTrace();
		}

		List<String> returnList = new ArrayList<String>();

		for (OffenceBO offence : offenceLists){
			if (offence.getShortDescription().toLowerCase().startsWith(term.toLowerCase()))
				returnList.add(offence.getShortDescription());
		}
		return returnList;
	}
	
	/**
	 * 
	 * @param term
	 * @return
	 */
	public List<String> ItaExaminerNameAutoComplete(String term) {

		ITAExaminerCriteriaBO criteria = new ITAExaminerCriteriaBO();
		criteria.setLastName(term);
		String middleName = "";
		List<ITAExaminerBO> examinerLists = new ArrayList<ITAExaminerBO>();

		try {
			examinerLists = getMaintenanceService().lookupITAExaminer(criteria);

		} catch (Exception e) {
			e.printStackTrace();
		}

		List<String> returnList = new ArrayList<String>();

		for (ITAExaminerBO examiner : examinerLists)
			if (examiner.getPersonBO().getLastName().toLowerCase().trim().startsWith(term.toLowerCase())) {
				if(!StringUtils.isBlank(examiner.getPersonBO().getMiddleName())){
					middleName = " "+examiner.getPersonBO().getMiddleName();
				}
				returnList.add(examiner.getPersonBO().getLastName().toUpperCase() + ", " + examiner.getPersonBO().getFirstName() + middleName);
			}
		return returnList;
	}

	/**
	 * 
	 * @param term
	 * @return
	 */
	public List<String> ArteryAutoComplete(String term) {

		ArteryCriteriaBO criteria = new ArteryCriteriaBO();
		criteria.setShortDescription(term);

		List<ArteryBO> arteryLists = new ArrayList<ArteryBO>();

		try {
			arteryLists = getMaintenanceService().lookupArtery(criteria);

		} catch (Exception e) {
			e.printStackTrace();
		}

		List<String> returnList = new ArrayList<String>();

		for (ArteryBO artery : arteryLists)
			if (artery.getShortDescription().toLowerCase().trim().startsWith(term.toLowerCase())) {
				returnList.add(artery.getShortDescription());
			}
		return returnList;
	}

	/**
	 * 
	 * @param term
	 * @return
	 */
	public List<StaffUserMappingBO> taStaffNameNIDAutoComplete(String term) {

		StaffUserMappingCriteriaBO criteria = new StaffUserMappingCriteriaBO();
		criteria.setLastName(term);

		List<StaffUserMappingBO> staffLists = new ArrayList<StaffUserMappingBO>();

		try {
			staffLists = getStaffUserMappingService().lookupStaffUserMappings(criteria);

			if (staffLists == null)
				staffLists = new ArrayList<StaffUserMappingBO>();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return staffLists;
	}

	/******************************************* Messages ****************************************/

	/**
	 * 
	 * @param context
	 * @param messageCode
	 * @return
	 */
	public RequestContext addInfoMessage(RequestContext context, String messageCode) {
		context.getMessageContext().addMessage(new MessageBuilder().info().code(messageCode).build());
		return context;
	}

	/**
	 * 
	 * @param context
	 * @param messageCode
	 * @return
	 */
	public RequestContext addErrorMessage(RequestContext context, String messageCode) {
		context.getMessageContext().addMessage(new MessageBuilder().error().code(messageCode).build());

		return context;
	}

	/**
	 * 
	 * @param context
	 * @param messageCode
	 * @return
	 */
	public RequestContext addFatalMessage(RequestContext context, String messageCode) {
		context.getMessageContext().addMessage(new MessageBuilder().fatal().code(messageCode).build());
		return context;
	}

	/**
	 * 
	 * @param context
	 * @param messageString
	 * @return
	 */
	public RequestContext addInfoMessageText(RequestContext context, String messageString) {
		context.getMessageContext().addMessage(new MessageBuilder().info().defaultText(messageString).build());
		return context;
	}

	/**
	 * 
	 * @param context
	 * @param messageString
	 * @return
	 */
	public RequestContext addErrorMessageText(RequestContext context, String messageString) {
		context.getMessageContext().addMessage(new MessageBuilder().error().defaultText(messageString).build());

		return context;
	}

	/**
	 * 
	 * @param context
	 * @param messageString
	 * @return
	 */
	public RequestContext addErrorMessageWithParameter(RequestContext context, String messageString, String parameter) {
		context.getMessageContext().addMessage(new MessageBuilder().error().code(messageString).arg(parameter).build());
		return context;
	}

	/**
	 * 
	 * @param context
	 * @param messageString
	 * @return
	 */
	public RequestContext addFatalMessageText(RequestContext context, String messageString) {
		context.getMessageContext().addMessage(new MessageBuilder().fatal().defaultText(messageString).build());
		return context;
	}

	/**
	 * 
	 * @param context
	 * @param messageString
	 * @return
	 */
	public RequestContext addInfoMessageWithParameter(RequestContext context, String messageString, String parameter) {

		context.getMessageContext().addMessage(new MessageBuilder().info().code(messageString).arg(parameter).build());
		return context;
	}

	public RequestContext addWarningMessage(RequestContext context, String messageCode) {
		context.getMessageContext().addMessage(new MessageBuilder().warning().code(messageCode).build());

		return context;
	}

	public RequestContext addWarningMessageWithParameter(RequestContext context, String messageString, String parameter) {

		context.getMessageContext().addMessage(new MessageBuilder().warning().code(messageString).arg(parameter).build());
		return context;
	}

	/**
	 * 
	 * @param context
	 * @param messageString
	 * @return
	 */
	public RequestContext addWarningMessageText(RequestContext context, String messageString) {
		context.getMessageContext().addMessage(new MessageBuilder().warning().defaultText(messageString).build());

		return context;
	}

	/************************************ MOBILE RELATED ***************************/

	/**
	 * This function uses UAgentInfor to detect if the device access the application is a mobile. It uses the user-agent
	 * and Accept header data.
	 * 
	 * @return
	 */
	public Boolean isHandHeld(RequestContext context) {

		HttpServletRequest req = (HttpServletRequest) context.getExternalContext().getNativeRequest();

		Device currentDevice = DeviceUtils.getCurrentDevice(req);

		if (currentDevice != null) {
			context.getFlowScope().put("isHandHeld", currentDevice.isTablet() || currentDevice.isMobile());
			Boolean isMobile = new Boolean(currentDevice.isTablet() || currentDevice.isMobile());

			if (!isMobile) {
				/* TEST URL to ensure it is a desktop. If the URL has the mobile url from configuration value then set
				 * mobile to true. */
				BaseServiceAction baseAction = new BaseServiceAction();

				HashMap<String, String> filter = new HashMap<String, String>();

				filter.put("cfg_code", "mobile_url");

				List<RefCodeBO> refInfos = baseAction.getRefInfo("roms_configuration", filter);

				if (refInfos.size() > 0) {
					String mobileUrl = refInfos.get(0).getAltId();

					if (!StringUtils.isEmpty(mobileUrl) && req.getRequestURL().toString().toLowerCase().contains(mobileUrl.toLowerCase().trim())) {
						isMobile = true;
					}

				} else {
					return null;
				}

			}

			return isMobile;
		} else {
			return null;
		}
	}

	/**
	 * This function uses UAgentInfor to detect if the device access the application is a mobile. It uses the user-agent
	 * and Accept header data. This function is used where there is no active web flow being used in context.
	 * 
	 * @return
	 */
	public static Boolean isHandHeldNoFlow() {

		RequestContext context = RequestContextHolder.getRequestContext();

		HttpServletRequest req = (HttpServletRequest) context.getExternalContext().getNativeRequest();

		Device currentDevice = DeviceUtils.getCurrentDevice(req);

		if (currentDevice != null) {
			context.getFlowScope().put("isHandHeld", currentDevice.isTablet() || currentDevice.isMobile());
			Boolean isMobile = new Boolean(currentDevice.isTablet() || currentDevice.isMobile());

			if (!isMobile) {
				/* TEST URL to ensure it is a desktop. If the URL has the mobile url from configuration value then set
				 * mobile to true. */
				BaseServiceAction baseAction = new BaseServiceAction();

				HashMap<String, String> filter = new HashMap<String, String>();

				filter.put("cfg_code", "mobile_url");

				List<RefCodeBO> refInfos = baseAction.getRefInfo("roms_configuration", filter);

				if (refInfos.size() > 0) {
					String mobileUrl = refInfos.get(0).getAltId();

					// System.out.println("Mobile URL: " + mobileUrl);

					// System.out.println("Request URL: " + req.getRequestURL());

					if (!StringUtils.isEmpty(mobileUrl) && req.getRequestURL().toString().toLowerCase().contains(mobileUrl.toLowerCase().trim())) {
						isMobile = true;
					}

				} else {
					return null;
				}

			}

			return isMobile;
		} else {
			return null;
		}
	}

	public static boolean isMobile(HttpServletRequest req) {
		boolean isMobile = false;

		RequestContext context = RequestContextHolder.getRequestContext();

		// HttpServletRequest req = (HttpServletRequest) context.getExternalContext().getNativeRequest();

		Device currentDevice = DeviceUtils.getCurrentDevice(req);

		if (currentDevice != null) {
			isMobile = currentDevice.isTablet() || currentDevice.isMobile();

			if (!isMobile) {
				BaseServiceAction baseAction = new BaseServiceAction();

				HashMap<String, String> filter = new HashMap<String, String>();

				filter.put("cfg_code", "mobile_url");

				List<RefCodeBO> refInfos = baseAction.getRefInfo("roms_configuration", filter);

				if (refInfos.size() > 0) {
					String mobileUrl = refInfos.get(0).getAltId();

					if (!StringUtils.isEmpty(mobileUrl) && req.getRequestURL().toString().toLowerCase().contains(mobileUrl.toLowerCase().trim())) {
						isMobile = true;
					}

				}
			}
		}

		return isMobile;
	}

	/*********************** PAGING RELATED FUNCTION ********************************************/
	public Event next(String tableName) {
		RequestContext context = RequestContextHolder.getRequestContext();

		DataTableMemory dataTable = (DataTableMemory) context.getFlowScope().get(tableName);

		dataTable.nextPage();

		return success();
	}

	public Event prev(String tableName) {
		RequestContext context = RequestContextHolder.getRequestContext();

		DataTableMemory dataTable = (DataTableMemory) context.getFlowScope().get(tableName);

		dataTable.previousPage();

		return success();
	}

	public Event first(String tableName) {
		RequestContext context = RequestContextHolder.getRequestContext();

		DataTableMemory dataTable = (DataTableMemory) context.getFlowScope().get(tableName);

		dataTable.firstPage();

		return success();
	}

	public Event last(String tableName) {
		RequestContext context = RequestContextHolder.getRequestContext();

		DataTableMemory dataTable = (DataTableMemory) context.getFlowScope().get(tableName);

		dataTable.lastPage();

		return success();
	}

	/*************************** Data Formating Related *************************************************/

	/**
	 * 
	 * @param xmlCal
	 * @return This function returns a string formated version of the XMLGregorianCaledar the format of the string is
	 *         'yyyy-MM-dd @ h:mm a' i.e. '2013-12-31 @ 12:45 am'
	 */
	public String formatXMLGrogrialCalDate(XMLGregorianCalendar xmlCal) {

		if (xmlCal != null) {
			Date date = xmlCal.toGregorianCalendar().getTime();

			SimpleDateFormat dateFormatDatePart = new SimpleDateFormat("yyyy-MM-dd");

			SimpleDateFormat dateFormatTimePart = new SimpleDateFormat("h:mm a");

			return dateFormatDatePart.format(date) + " " + dateFormatTimePart.format(date).toLowerCase();

		} else {
			return "";
		}
	}

	public String formatXMLGrogrialCalDateOnly(XMLGregorianCalendar xmlCal) {

		if (xmlCal != null) {
			Date date = xmlCal.toGregorianCalendar().getTime();

			String dateStr = DateUtils.formatDate("yyyy-MM-dd", date);

			return dateStr;

		} else {
			return "";
		}
	}

	public String formatDateTime(Date date) {

		if (date != null) {
			SimpleDateFormat dateFormatDatePart = new SimpleDateFormat("yyyy-MM-dd");

			SimpleDateFormat dateFormatTimePart = new SimpleDateFormat("h:mm a");

			return dateFormatDatePart.format(date) + " " + dateFormatTimePart.format(date).toLowerCase();

		} else {
			return "";
		}
	}
	
	public String formatDate(Date date) {

		if (date != null) {
			SimpleDateFormat dateFormatDatePart = new SimpleDateFormat("yyyy-MM-dd");

			return dateFormatDatePart.format(date);

		} else {
			return "";
		}
	}

	/**
	 * @Mod RThompson
	 * @param xmlCal
	 * @return This function returns a string formated version of the XMLGregorianCaledar the format of the string is
	 *         'yyyy-MM-dd' i.e. '2013-01-23'
	 */
	public String formatXMLGregorianToDate(XMLGregorianCalendar xmlCal) {

		if (xmlCal != null) {
			Date date = xmlCal.toGregorianCalendar().getTime();

			SimpleDateFormat dateFormatDatePart = new SimpleDateFormat("yyyy-MM-dd");

			// SimpleDateFormat dateFormatTimePart = new
			// SimpleDateFormat("hh:mma");

			return dateFormatDatePart.format(date);

		} else {
			return "";
		}
	}
	
	//Calculate Duration
		public void calculateDuration(Date start, Date end) {
			RequestContext context = RequestContextHolder.getRequestContext();
			
			LocalDate startDate = LocalDate.fromDateFields(start);
			LocalDate endDate = LocalDate.fromDateFields(end);
			Period datePeriod = new Period(startDate,endDate);
			int durationDays = datePeriod.getDays();
			int durationWeeks = datePeriod.getWeeks();
			int durationMonths = datePeriod.getMonths();
			int durationYears = datePeriod.getYears();

			context.getFlowScope().put("durationDays", durationDays);
			context.getFlowScope().put("durationYears", durationYears);
			context.getFlowScope().put("durationMonths", durationMonths);
			context.getFlowScope().put("durationWeeks", durationWeeks);
		}
		
		public static boolean validateDateRangeLimit(Date start, Date end){
			boolean pass = true;
			
			long days = DateUtils.getDateDiff(start, end, TimeUnit.DAYS);
			
			if(days > 366)
				pass=false;
			
			return pass;
		}
		
		

	/****************************** VALIDATE OVERRIDE *************************************/
	/**
	 * 
	 * @param view
	 * @return
	 */
	public boolean validateOverrideDetails(RequestContext context, VerifyDetailsSecurityView view) {
		boolean passed = true;

		if (StringUtils.isBlank(view.getUserName())) {
			addErrorMessageWithParameter(context, "RequiredFields", "Username");
			passed = false;
		}

		if (StringUtils.isBlank(view.getPassword())) {
			addErrorMessageWithParameter(context, "RequiredFields", "Password");
			passed = false;
		}

		if (StringUtils.isBlank(view.getPermission())) {
			addErrorMessageWithParameter(context, "RequiredFields", "Permission");
			passed = false;
		}
		
		if (StringUtils.isBlank(view.getComment())) {
			addErrorMessageWithParameter(context, "RequiredFields", "Comment");
			passed = false;
		}

		if (StringUtils.isNotBlank(view.getLastUpdatedUser()) && StringUtils.isNotBlank(view.getUserName()) && view.getLastUpdatedUser().equalsIgnoreCase(view.getUserName())) {
			//"User Who Last Updated Document Cannot Verify Document."
			addErrorMessageText(context,"Verification not allowed by same user who created/edited the document(s)" );			
			passed = false;
		}

		/* if(StringUtils.isBlank(this.permission)){ context.getMessageContext() .addMessage( (MessageResolver) new
		 * MessageBuilder().error().code("RequiredFields").arg("Permission")); passed = false; } */

		if (StringUtils.isNotBlank(view.getPassword()) && StringUtils.isNotBlank(view.getUserName())) {// &&
																										// StringUtils.isNotBlank(this.permission)){

			try {
				boolean passed1 = getROMSUserDetailsService().validUser(view.getUserName(), view.getPassword());// &&
				// proxy.hasPermision(this.userName,
				// this.permission);
				// username/password does not validate
				if (!passed1) {
					addErrorMessageWithParameter(context, "RequiredFields", "Valid Username/Password ");
					passed = false;
				}
				// if it validates
				if (passed) {
					boolean passed2 = getROMSUserDetailsService().validOverride(view.getUserName(), view.getPassword(), view.getPermission());

					if (!passed2) {
						passed = false;
						addErrorMessageText(context, "User does not have the requisite permission.");
					}
				}

			} catch (Exception ex) {
				ex.printStackTrace();
				passed = false;
			}
		}

		return passed;
	}

	/****************************** VALIDATE OVERRIDE *************************************/
	public boolean validateStartNEndDate(RequestContext context, Date startDate, Date endDate, String fieldName, Boolean allowFutureDates) {

		boolean pass = true;

		/* if (startDate == null) { context.getMessageContext().addMessage( new
		 * MessageBuilder().error().code("RequiredFields") .arg( fieldName + " Start Date").build()); return false; } if
		 * (endDate == null) { context.getMessageContext().addMessage( new
		 * MessageBuilder().error().code("RequiredFields") .arg(fieldName + " End Date").build()); return false; } */

		if (startDate == null && endDate != null) {
			context.getMessageContext().addMessage(new MessageBuilder().error().code("RequiredFields").arg(fieldName + " Start Date").build());
			pass = false;
		}

		if (endDate == null && startDate != null) {
			context.getMessageContext().addMessage(new MessageBuilder().error().code("RequiredFields").arg(fieldName + " End Date").build());
			pass = false;
		}

		if (endDate != null && startDate != null) {
			if (endDate.before(startDate)) {
				this.addErrorMessageWithParameter(context,"EndBeforeStart", fieldName);
				pass = false;
			}

			/* if (endDate.equals((startDate))) { context.getMessageContext().addMessage( new
			 * MessageBuilder().error().defaultText(fieldName + " Start Date Cannot Be Same As End Date.") .build());
			 * return false; } */

			// test if date period is greater than one year
			Calendar calEndDate = Calendar.getInstance();
			Calendar calStartDate = Calendar.getInstance();
			calEndDate.setTime(endDate);
			calStartDate.setTime(startDate);
			if ((calEndDate.getTimeInMillis() - DateUtils.MILLISECONDS_IN_YEAR) > calStartDate.getTimeInMillis()) {
				//context.getMessageContext().addMessage(new MessageBuilder().error().defaultText(fieldName + " End Date Cannot Be More Than a Year Past Start Date.").build());
				this.addErrorMessageWithParameter(context,"DateRangeLimit", fieldName);
				pass = false;
			}
			/*if ((endDate.toGregorianCalendar().getTimeInMillis() - DateUtils.MILLISECONDS_IN_YEAR) > startDate.toGregorianCalendar().getTimeInMillis()) {
				context.getMessageContext().addMessage(new MessageBuilder().error().defaultText(fieldName + " End Date Cannot Be More Than a Year Past Start Date.").build());
				return false;
			}*/
			
			if(allowFutureDates == false){
				if(DateUtils.isDateInFutureDateType(startDate))
				{	
					this.addErrorMessageWithParameter(context,"FutureDate", fieldName+" Start Date");
					pass = false;
				}
				
				if(DateUtils.isDateInFutureDateType(endDate))
				{	
					this.addErrorMessageWithParameter(context,"FutureDate", fieldName+" End Date");
					pass = false;
				}
			}
			
			

		}

		// default
		return pass;

	}

	/************************************** ADDRES RELATED ******************************************/

	/* Address Formating Related Functions */
	public String getAddressLine1(String streetName, String addrMark, String addrStreetNo) {

		String address = "";
		if (addrMark != null && !"".equals(addrMark)) {
			address = addrMark.trim();
		}
		if (addrStreetNo != null && !"".equals(addrStreetNo.trim())) {
			if ("".equals(address)) {
				address = addrStreetNo.trim();
			} else {
				address = address + ", " + addrStreetNo.trim();
			}
		}
		if (streetName != null && !"".equals(streetName.trim())) {
			if ("".equals(address)) {
				address = streetName.trim();
			} else {
				address = address + " " + streetName.trim() + "";
			}
		}

		return WordUtils.capitalize(address.toLowerCase(), Constants.STRING_DELIM_ARRAY);
	}

	public String getAddressLine2(String poBoxNo, String poLocationName, String parishDescription) {
		boolean kingston = false;
		String address = "";

		if (poBoxNo != null && !"".equals(poBoxNo.trim())) {
			if ("".equals(address)) {
				address = " P.O. Box " + poBoxNo.trim();
			} else {
				address = address + ", P.O. Box " + poBoxNo.trim();
			}
		}

		if (poLocationName != null && !"".equals(poLocationName.trim())) {
			String po = poLocationName.toUpperCase();
			// if po box is not there then put a comma before the poLocName
			if (StringUtils.isBlank(poLocationName) && StringUtils.isNotBlank(address)) {
				address = address + ",";
			}

			if (po.contains("KINGSTON")) {
				kingston = true;
				// if kingston is in the name then put it
				if ("".equals(address)) {
					address = poLocationName.trim();
				} else {
					address = address + " " + poLocationName.trim();
				}
			} else {
				if ("".equals(address)) {
					address = poLocationName.trim();
				} else {
					address = address + " " + poLocationName.trim();
				}
			}
		}

		if (parishDescription != null) {
			if (parishDescription != null && !"".equals(parishDescription.trim())) {
				String par = parishDescription.toUpperCase();
				if (par.contains("KINGSTON")) {
					// if kingston was not put before then put it
					// set variable to true
					if (kingston == false) {
						// kingston = true;
						if ("".equals(address)) {
							address = parishDescription.trim();
						} else {
							address = address + ", " + parishDescription.trim();
						}
					}

				} else {
					if ("".equals(address)) {
						address = parishDescription.trim();
					} else {
						address = address + ", " + parishDescription.trim();
					}
				}
			}
		}

		return WordUtils.capitalize(address.toLowerCase(), Constants.STRING_DELIM_ARRAY);

	}

	public String getAddressLine2WithNewLine(String poBoxNo, String poLocationName, String parishDescription) {
		boolean kingston = false;
		String address = "";

		if (poBoxNo != null && !"".equals(poBoxNo.trim())) {
			if ("".equals(address)) {
				address = " P.O. Box " + poBoxNo.trim();
			} else {
				address = address + ", P.O. Box " + poBoxNo.trim();
			}
		}

		if (poLocationName != null && !"".equals(poLocationName.trim())) {
			String po = poLocationName.toUpperCase();
			// if po box is not there then put a comma before the poLocName
			if (StringUtils.isBlank(poLocationName) && StringUtils.isNotBlank(address)) {
				address = address + "<br/>";
			}

			if (po.contains("KINGSTON")) {
				kingston = true;
				// if kingston is in the name then put it
				if ("".equals(address)) {
					address = poLocationName.trim();
				} else {
					address = address + " " + poLocationName.trim();
				}
			} else {
				if ("".equals(address)) {
					address = poLocationName.trim();
				} else {
					address = address + " " + poLocationName.trim();
				}
			}
		}

		if (parishDescription != null) {
			if (parishDescription != null && !"".equals(parishDescription.trim())) {
				String par = parishDescription.toUpperCase();
				if (par.contains("KINGSTON")) {
					// if kingston was not put before then put it
					// set variable to true
					if (kingston == false) {
						// kingston = true;
						if ("".equals(address)) {
							address = parishDescription.trim();
						} else {
							address = address + '\n' + parishDescription.trim();
						}
					}

				} else {
					if ("".equals(address)) {
						address = parishDescription.trim();
					} else {
						address = address + '\n' + parishDescription.trim();
					}
				}
			}
		}

		return WordUtils.capitalize(address.toLowerCase(), Constants.STRING_DELIM_ARRAY);
	}

	public List<StaffUserMappingBO> getStaffUserMaps() {
		return staffUserMaps;
	}

	public void setStaffUserMaps(List<StaffUserMappingBO> staffUserMaps) {
		this.staffUserMaps = staffUserMaps;
	}

	/**
	 * This function copies all properties from the source to the target. Including nested objects, nested list of
	 * objects, etc
	 * 
	 * @param target
	 *            <b>The object that the data is being copied to.</b>
	 * @param source
	 *            <b>The object that the data is being copied from.</b>
	 * @throws Exception
	 */
	public static <T> void copyFields(T target, T source) throws Exception {
		if (target == null || source == null)
			throw new NullPointerException();

		Class<?> clazz = source.getClass();

		for (java.lang.reflect.Field field : clazz.getDeclaredFields()) {
			field.setAccessible(true);

			Object value = field.get(source);
			try {
				java.lang.reflect.Field toSetField = target.getClass().getDeclaredField(field.getName());
				toSetField.setAccessible(true);
				toSetField.set(target, value);
				// field.set(target, value);
			} catch (Exception exe) {

			}
		}
	}

	/*********************** CREATE LISTS *************************/

	public List<SelectItem> getDocumentTypeList2() {
		List<SelectItem> list = new ArrayList<SelectItem>();

		list.add(new SelectItem(ScannedDocumentTypeShort.MANUAL_DOCUMENT, ScannedDocumentType.MANUAL_DOCUMENT));
		list.add(new SelectItem(ScannedDocumentTypeShort.STATEMENT_DOCUMENT, ScannedDocumentType.STATEMENT_DOCUMENT));
		list.add(new SelectItem(ScannedDocumentTypeShort.EVIDENCE_DOCUMENT, ScannedDocumentType.EVIDENCE_DOCUMENT));
		list.add(new SelectItem(ScannedDocumentTypeShort.SIGNED_DOCUMENT, ScannedDocumentType.SIGNED_DOCUMENT));
		list.add(new SelectItem(ScannedDocumentTypeShort.DISCHARGE_FORM, ScannedDocumentType.DISCHARGE_FORM));

		return list;
	}

	/**
	 * create lists for Scanned Doc screen
	 * 
	 * @return
	 */
	public List<SelectItem> getDocumentTypeList() {
		List<SelectItem> list = new ArrayList<SelectItem>();

		// list.add(new SelectItem(ScannedDocumentTypeShort.MANUAL_DOCUMENT, ScannedDocumentType.MANUAL_DOCUMENT));
		list.add(new SelectItem(ScannedDocumentTypeShort.STATEMENT_DOCUMENT, ScannedDocumentType.STATEMENT_DOCUMENT));
		list.add(new SelectItem(ScannedDocumentTypeShort.EVIDENCE_DOCUMENT, ScannedDocumentType.EVIDENCE_DOCUMENT));
		list.add(new SelectItem(ScannedDocumentTypeShort.SIGNED_DOCUMENT, ScannedDocumentType.SIGNED_DOCUMENT));
		list.add(new SelectItem(ScannedDocumentTypeShort.DISCHARGE_FORM, ScannedDocumentType.DISCHARGE_FORM));

		return list;
	}

	/**
	 * create lists for Scanned Doc screen
	 * 
	 * @return
	 */
	public List<SelectItem> getDocumentTypeList(String documentType) {
		List<SelectItem> list = new ArrayList<SelectItem>();

		/* list.add(new SelectItem(ScannedDocumentTypeShort.MANUAL_DOCUMENT, ScannedDocumentType.MANUAL_DOCUMENT)); */
		list.add(new SelectItem(ScannedDocumentTypeShort.STATEMENT_DOCUMENT, ScannedDocumentType.STATEMENT_DOCUMENT));
		list.add(new SelectItem(ScannedDocumentTypeShort.EVIDENCE_DOCUMENT, ScannedDocumentType.EVIDENCE_DOCUMENT));
		list.add(new SelectItem(ScannedDocumentTypeShort.SIGNED_DOCUMENT, ScannedDocumentType.SIGNED_DOCUMENT));

		if (DocumentType.SUMMONS.equalsIgnoreCase(documentType)) {
			list.add(new SelectItem(ScannedDocumentTypeShort.DISCHARGE_FORM, ScannedDocumentType.DISCHARGE_FORM));
		}

		return list;
	}

	public Boolean hasSpecialPermissions() {
		if (getRomsLoggedInUser().hasPermission("ROLE_ROMS_ROAD_OP_SCHEDULE_SPC"))
			return true;
		else
			return false;
	}

	public Logger getLogger()
	{
		return logger;
	}

	public void setLogger(Logger logger)
	{
		this.logger = logger;
	}
	
	public String getPoliceOfficerNameForDisplay(Integer personId){
		
		HashMap<String,String> filter = new HashMap<String,String>();
		
		filter.put("person_id",personId.toString() );
		
		List<RefCodeBO> refCodes = this.getRefInfo("roms_police_officer", filter);
		
		if(refCodes != null && refCodes.size() > 0){
			return refCodes.get(0).getShortDescription() + " " + refCodes.get(0).getDescription() + " - " + " [" + refCodes.get(0).getAltId() + "]";
		}
		else{
			return null;
		}
		
		
	}
	
	/**
	 * @author kpowell
	 * Address Validation
	 */
	public boolean validateAddress(RequestContext context, AddressView address){
		int errorsFound = 0;

		
		System.err.println("validateAddress");

		if (StringUtils.isBlank(address.getParish())) {
			addErrorMessageWithParameter(context, "RequiredFields", "Parish");
			errorsFound++;
			//errorFound = true;
		}
		
		if (StringUtils.isBlank(address.getStreetName()) && StringUtils.isBlank(address.getMarkText())&& StringUtils.isBlank(address.getPoLocationName())) {
			addErrorMessageWithParameter(context, "RequiredFields", "Street Name, Mark or PO Location");
			errorsFound++;
			//errorFound = true;
		}else{
			
			if (!StringUtils.isBlank(address.getStreetNo()) && StringUtils.isBlank(address.getStreetName())) {
				addErrorMessageWithParameter(context, "RequiredFields", "Street Name");
				errorsFound++;
				//errorFound = true;
			}
			
			if (!StringUtils.isBlank(address.getPoBoxNo()) && StringUtils.isBlank(address.getPoLocationName())) {
				addErrorMessageWithParameter(context, "RequiredFields", "PO Location");
				errorsFound++;
				//errorFound = true;
			}
		}

		
		if(errorsFound >0){
			return true;
		}else{
			return false;
		}
	
	}
	
	public boolean validateAddress(AddressView address){
		int errorsFound = 0;


		if (StringUtils.isBlank(address.getParish())) {
			errorsFound++;
		}
		
		if (StringUtils.isBlank(address.getStreetName()) && StringUtils.isBlank(address.getMarkText())&& StringUtils.isBlank(address.getPoLocationName())) {
			errorsFound++;
		}else{
			
			if (!StringUtils.isBlank(address.getStreetNo()) && StringUtils.isBlank(address.getStreetName())) {
				errorsFound++;
			}
			
			if (!StringUtils.isBlank(address.getPoBoxNo()) && StringUtils.isBlank(address.getPoLocationName())) {
				errorsFound++;
			}
		}

		
		if(errorsFound >0){
			return true;
		}else{
			return false;
		}
	
	}
	
	
	/**
	 * 
	 * @param context
	 * @param messageString
	 * @return
	 */
	public String buildErrorMessageWithParameters(String messageString, String[] parameter) {
		
		Object[] objArray = parameter;
		MessageFormat formatter = new MessageFormat("");
		formatter.applyPattern(messages.getString(messageString));
		String errorOutput = "";
		if(parameter == null || parameter.length < 1){
			errorOutput = formatter.toPattern();
		}else{
			errorOutput = formatter.format(objArray);
		}
		
		
		return errorOutput;
	}
}
