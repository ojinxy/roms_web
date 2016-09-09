package org.fsl.roms.service.action;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import javax.faces.event.ValueChangeEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.xerces.impl.dv.util.Base64;
import org.fsl.application.ApplicationProperties;
import org.fsl.converter.PersonBOForRoadCompConverter;
import org.fsl.datatable.DataTableMemory;
import org.fsl.roms.businessobject.RoadOperationDetailsSumary;
import org.fsl.roms.constants.Constants;
import org.fsl.roms.util.DateUtils;
import org.fsl.roms.util.NameUtil;
import org.fsl.roms.util.PhoneNumberUtil;
import org.fsl.roms.util.StringUtil;
import org.fsl.roms.view.AddressView;
import org.fsl.roms.view.AssociatedDocView;
import org.fsl.roms.view.BadgeNameSearchView;
import org.fsl.roms.view.BadgeTableBean;
import org.fsl.roms.view.ObjectUtils;
import org.fsl.roms.view.OffenceOutcomeView;
import org.fsl.roms.view.RecordOffenceOutcomeView;
import org.fsl.roms.view.RoadCheckInitiateView;
import org.fsl.roms.view.RoadCheckReviewSummaryBean;
import org.fsl.roms.view.RoadCheckVerifyIdView;
import org.fsl.roms.view.SupportingDetailsView;
import org.fsl.roms.view.WitnessView;
import org.fsl.ta.toms.roms.beans.ApplicationRunTimeStorage;
import org.fsl.trn.exceptions.InvalidTaxPayerException;
import org.fsl.trn.exceptions.TaxPayerClosedException;
import org.fsl.trn.exceptions.TaxPayerDeceasedException;
import org.fsl.trn.exceptions.TaxPayerRetiredException;
import org.fsl.trn.exceptions.TaxPayerUnintendedException;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.ToggleSelectEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.webflow.engine.RequestControlContext;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;
import org.springframework.webflow.execution.RequestContextHolder;

import fsl.ta.toms.roms.bo.ArteryBO;
import fsl.ta.toms.roms.bo.AssignedTeamDetailsBO;
import fsl.ta.toms.roms.bo.AuditEntryBO;
import fsl.ta.toms.roms.bo.AuthorizationBO;
import fsl.ta.toms.roms.bo.BadgeBO;
import fsl.ta.toms.roms.bo.BadgeCheckResultBO;
import fsl.ta.toms.roms.bo.CitationCheckResultBO;
import fsl.ta.toms.roms.bo.CitationOffenceBO;
import fsl.ta.toms.roms.bo.ComplianceBO;
import fsl.ta.toms.roms.bo.ComplianceDetailsBO;
import fsl.ta.toms.roms.bo.ComplianceParamBO;
import fsl.ta.toms.roms.bo.CompliancyCheckBO;
import fsl.ta.toms.roms.bo.CourtBO;
import fsl.ta.toms.roms.bo.DLCheckResultBO;
import fsl.ta.toms.roms.bo.DocumentViewBO;
import fsl.ta.toms.roms.bo.ExcerptParamMappingBO;
import fsl.ta.toms.roms.bo.ITAExaminerBO;
import fsl.ta.toms.roms.bo.JPBO;
import fsl.ta.toms.roms.bo.LocationBO;
import fsl.ta.toms.roms.bo.OffenceBO;
import fsl.ta.toms.roms.bo.OffenceMandatoryOutcomeBO;
import fsl.ta.toms.roms.bo.ParishBO;
import fsl.ta.toms.roms.bo.PersonBO;
import fsl.ta.toms.roms.bo.PoliceOfficerBO;
import fsl.ta.toms.roms.bo.PoundBO;
import fsl.ta.toms.roms.bo.RefCodeBO;
import fsl.ta.toms.roms.bo.RoadCheckBO;
import fsl.ta.toms.roms.bo.RoadCheckOffenceOutcomeBO;
import fsl.ta.toms.roms.bo.RoadLicCheckResultBO;
import fsl.ta.toms.roms.bo.RoadLicVehCheckResultBO;
import fsl.ta.toms.roms.bo.RoadOperationBO;
import fsl.ta.toms.roms.bo.RoadOperationOtherDetailsBO;
import fsl.ta.toms.roms.bo.ScannedDocBO;
import fsl.ta.toms.roms.bo.StaffUserMappingBO;
import fsl.ta.toms.roms.bo.TAStaffBO;
import fsl.ta.toms.roms.bo.VehicleBO;
import fsl.ta.toms.roms.bo.VehicleCheckResultBO;
import fsl.ta.toms.roms.bo.VehicleOwnerBO;
import fsl.ta.toms.roms.bo.WreckingCompanyBO;
import fsl.ta.toms.roms.exception.ErrorSavingException;
import fsl.ta.toms.roms.exception.InvalidFieldException;
import fsl.ta.toms.roms.exception.NoRecordFoundException;
import fsl.ta.toms.roms.exception.RequiredFieldMissingException;
import fsl.ta.toms.roms.search.criteria.impl.ArteryCriteriaBO;
import fsl.ta.toms.roms.search.criteria.impl.CourtCriteriaBO;
import fsl.ta.toms.roms.search.criteria.impl.ITAExaminerCriteriaBO;
import fsl.ta.toms.roms.search.criteria.impl.LocationCriteriaBO;
import fsl.ta.toms.roms.search.criteria.impl.ParishCriteriaBO;
import fsl.ta.toms.roms.search.criteria.impl.PoundCriteriaBO;
import fsl.ta.toms.roms.search.criteria.impl.RefCodeCriteriaBO;
import fsl.ta.toms.roms.search.criteria.impl.RoadCompliancyCriteriaBO;
import fsl.ta.toms.roms.search.criteria.impl.RoadOperationCriteriaBO;
import fsl.ta.toms.roms.search.criteria.impl.WreckingCompanyCriteriaBO;
import fsl.ta.toms.roms.ticketwebservice.TrafficTicket;
import fsl.ta.toms.roms.trnwebservice.InvalidTrnBranchException_Exception;
import fsl.ta.toms.roms.trnwebservice.NotValidTrnTypeException_Exception;
import fsl.ta.toms.roms.trnwebservice.SystemErrorException_Exception;
import fsl.ta.toms.roms.trnwebservice.TrnDTO;
import fsl.ta.toms.roms.webservices.Maintenance;
import fsl.ta.toms.roms.webservices.ReferenceCode;
import fsl.ta.toms.roms.webservices.RoadCompliancy;
import fsl.ta.toms.roms.webservices.RoadOperation;

@Service
public class RoadCheckServiceAction extends BaseServiceAction  {

	private final String ROAD_CHECK_REVIEW_BEAN = "roadCheckReviewSummaryBean";
	private final String ROAD_CHECK_INIT_VIEW_OPERATION_DATE_SET = "opDateSet";
	
	@Autowired
	ApplicationRunTimeStorage applicationRunTimeStorage;
	
	@Autowired
	DocumentManagerServiceAction documentManagerServiceAction;

	@Autowired
	public DocumentManagerServiceAction getDocumentManagerServiceAction() {
		return documentManagerServiceAction;
	}

	@Autowired
	public void setDocumentManagerServiceAction(DocumentManagerServiceAction documentManagerServiceAction) {
		this.documentManagerServiceAction = documentManagerServiceAction;
	}

	public Event clearRoadCheckVerifyIdView(RequestContext context) {
		RoadCheckVerifyIdView roadCheckVerifyIdView = (RoadCheckVerifyIdView) context.getFlowScope().get("verifyIdView");
		roadCheckVerifyIdView.clear();
		return success();
	}

	public Event cancelRoadCheck(RequestContext context) {
		RoadCheckInitiateView roadCheckInitiateView = (RoadCheckInitiateView) context.getFlowScope().get("initiateView");

		if (roadCheckInitiateView.isFromSearch() == false) {
			clearRoadCheckVerifyIdView(context);
			clearRoadCheckDetails(context);
			return error();
		}
		return success();
	}

	public Event backFromInititate(RequestContext context) {
		RoadCheckInitiateView roadCheckInitiateView = (RoadCheckInitiateView) context.getFlowScope().get("initiateView");

		if (roadCheckInitiateView.isFromSearch() == false) {
			return error();
		}

		return success();
	}

	public Event clearRoadCheckDetails(RequestContext context) {
		RoadCheckInitiateView roadCheckInitiateView = new RoadCheckInitiateView();
		roadCheckInitiateView.clear();
		RecordOffenceOutcomeView recordOffenceOutcomeView = new RecordOffenceOutcomeView();
		SupportingDetailsView supportingDetailsView = new SupportingDetailsView();

		context.getFlowScope().put("initiateView", roadCheckInitiateView);
		context.getFlowScope().put("recordOffenceOutcomeView", recordOffenceOutcomeView);
		context.getFlowScope().put("supportingDetailsView", supportingDetailsView);
		return success();
	}

	private RoadCheckInitiateView setDetailsToInitiateRoadCheck(RequestContext context) {

		
		RoadCheckInitiateView roadCheckInitiateView = new RoadCheckInitiateView();
		roadCheckInitiateView.clear();
				
		roadCheckInitiateView.setMobileDevice(isHandHeld(context));

		
		if (roadCheckInitiateView.isMobileDevice()) {
			roadCheckInitiateView.setActivityType("S");
			roadCheckInitiateView.setOperationName("Operation");// Set Operation Name NEED TO set

			// Get Assigned Road Operation BO
			RoadCheckVerifyIdView roadCheckVerify = (RoadCheckVerifyIdView) context.getFlowScope().get("verifyIdView");
			
			roadCheckInitiateView.setRoadOperationBO(returnRoadOperationBO(getRomsLoggedInUser().getCurrentRoadOperationId() == null ? roadCheckVerify.getCurrentRoadOperationId()
					: getRomsLoggedInUser().getCurrentRoadOperationId()) );


			TAStaffBO loggedInStaff = new TAStaffBO();
			try {
				loggedInStaff = getRoadOperationService().getStaffByUsername(getRomsLoggedInUser().getUsername());
			} catch (NoRecordFoundException e) {

				e.printStackTrace();
			} catch (RequiredFieldMissingException e) {

				e.printStackTrace();
			}
			if (loggedInStaff != null) {
				roadCheckInitiateView.setTaStaffBO(loggedInStaff);
			}

			roadCheckInitiateView.setActivityType("S");
			context.getFlowScope().put("initiateView", roadCheckInitiateView);


		}


		return roadCheckInitiateView;
	}

	private RoadOperationBO returnRoadOperationBO(Integer operationId) {
		try {
			RoadOperationCriteriaBO roadOpCriteria = new RoadOperationCriteriaBO();
			roadOpCriteria.setRoadOperationId(operationId);// need to change value
			List<RoadOperationBO> roadOps = new ArrayList<RoadOperationBO>();
			roadOps = getRoadOperationService().lookupRoadOperation(roadOpCriteria);

			if (roadOps != null && roadOps.size() == 1) {
				return roadOps.get(0);
			}
		} catch (InvalidFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoRecordFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RequiredFieldMissingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	public Event searchTrn(RequestContext context) {
		try {
			RoadCheckVerifyIdView roadCheckVerifyIdView = (RoadCheckVerifyIdView) context.getFlowScope().get("verifyIdView");

			if (StringUtils.isBlank(roadCheckVerifyIdView.getTrn())) {
				addErrorMessageWithParameter(context, "RequiredFields", "Trn");
				return error();
			}
			
			//Checks if the TRN is a business
            if(isBusinessTRN(roadCheckVerifyIdView.getTrn())){
                  addErrorMessageText(context, "Invalid TRN");
                  return error();
            }


			// check ROMS database first
			PersonBO personBO = getDocumentsManagerService().getPersonByTRN(roadCheckVerifyIdView.getTrn());
			TrnDTO trnDTO = getTRNWebService().getrnDTOByTrn(Integer.parseInt(roadCheckVerifyIdView.getTrn())); //Check TRN Service as well for name changes
			RoadCheckInitiateView roadCheckInitiateView = setDetailsToInitiateRoadCheck(context);
			if (personBO == null) {

				// PersonBO personBO;

				// personBO =
				// getTRNWebServicePortProxy().getPersonBOByTrn(Integer.parseInt(roadCheckVerifyIdView.getTrn()));

				if (trnDTO == null) {
					addErrorMessage(context, "Norecordfound");
					return error();
				} else {
					if (trnDTO.isBusinessTrn()) {
						addErrorMessageText(context, "Invalid TRN");
						return error();
					}

					this.setDetails(roadCheckInitiateView, trnDTO);

					roadCheckInitiateView.setTrn(trnDTO.getNbrTrn());
					roadCheckInitiateView.setLastName(WordUtils.capitalize(trnDTO.getLastName().toLowerCase(), Constants.STRING_DELIM_ARRAY));

					roadCheckInitiateView.setPhotoURL(this.getPictureFromBIMS(trnDTO.getNbrTrn(), roadCheckInitiateView));
					if (StringUtil.isSet(roadCheckInitiateView.getPhotoURL()))
						roadCheckInitiateView.setShowPhoto(true);

					if (trnDTO.getFirstName() != null) {
						roadCheckInitiateView.setFirstName(WordUtils.capitalize(trnDTO.getFirstName().toLowerCase(), Constants.STRING_DELIM_ARRAY));
					}
					if (trnDTO.getMiddleName() != null) {
						roadCheckInitiateView.setMiddleName(WordUtils.capitalize(trnDTO.getMiddleName().toLowerCase(), Constants.STRING_DELIM_ARRAY));
					}

				}
			} else {

				this.setDetails(roadCheckInitiateView, personBO);

				/*Set name and picture information based on TRN data. Which should have the latest.*/
				/*roadCheckInitiateView.setTrn(personBO.getTrnNbr());
				roadCheckInitiateView.setLastName(WordUtils.capitalize(personBO.getLastName().toLowerCase(), Constants.STRING_DELIM_ARRAY));

				roadCheckInitiateView.setPhotoURL(this.getPictureFromBIMS(personBO.getTrnNbr(), roadCheckInitiateView));
				if (StringUtil.isSet(roadCheckInitiateView.getPhotoURL()))
					roadCheckInitiateView.setShowPhoto(true);

				if (personBO.getFirstName() != null) {
					roadCheckInitiateView.setFirstName(WordUtils.capitalize(personBO.getFirstName().toLowerCase(), Constants.STRING_DELIM_ARRAY));
				}
				if (personBO.getMiddleName() != null) {
					roadCheckInitiateView.setMiddleName(WordUtils.capitalize(personBO.getMiddleName().toLowerCase(), Constants.STRING_DELIM_ARRAY));
				}*/
				roadCheckInitiateView.setTrn(trnDTO.getNbrTrn());
				roadCheckInitiateView.setLastName(WordUtils.capitalize(trnDTO.getLastName().toLowerCase(), Constants.STRING_DELIM_ARRAY));

				roadCheckInitiateView.setPhotoURL(this.getPictureFromBIMS(trnDTO.getNbrTrn(), roadCheckInitiateView));
				if (StringUtil.isSet(roadCheckInitiateView.getPhotoURL()))
					roadCheckInitiateView.setShowPhoto(true);

				if (trnDTO.getFirstName() != null) {
					roadCheckInitiateView.setFirstName(WordUtils.capitalize(trnDTO.getFirstName().toLowerCase(), Constants.STRING_DELIM_ARRAY));
				}
				if (trnDTO.getMiddleName() != null) {
					roadCheckInitiateView.setMiddleName(WordUtils.capitalize(trnDTO.getMiddleName().toLowerCase(), Constants.STRING_DELIM_ARRAY));
				}
			}
			roadCheckInitiateView.setFromSearch(false);
			context.getFlowScope().put("initiateView", roadCheckInitiateView);
			return success();

		} catch (NumberFormatException e) {
			addErrorMessageText(context, "Invalid TRN");
			e.printStackTrace();
			return error();
		} catch (InvalidTrnBranchException_Exception e) {
			addErrorMessage(context, "Norecordfound");
			e.printStackTrace();
			return error();
		} catch (NotValidTrnTypeException_Exception e) {
			addErrorMessage(context, "Norecordfound");
			e.printStackTrace();
			return error();
		} catch (SystemErrorException_Exception e) {
			addErrorMessage(context, "search.failure");
			e.printStackTrace();
			return error();
		} catch (TaxPayerClosedException e) {
			addErrorMessage(context, "TRN.Status.Closed");
			e.printStackTrace();
			return error();
		} catch (TaxPayerUnintendedException e) {
			addErrorMessage(context, "TRN.Status.Invalid");
			e.printStackTrace();
			return error();
		} catch (TaxPayerDeceasedException e) {
			addErrorMessage(context, "TRN.Status.Deceased");
			e.printStackTrace();
			return error();
		} catch (TaxPayerRetiredException e) {
			addErrorMessage(context, "TRN.Status.Retired");
			e.printStackTrace();
			return error();
		} catch (InvalidTaxPayerException e) {
			addErrorMessage(context, "TRN.Status.Invalid");
			e.printStackTrace();
			return error();
		}

		catch (Exception e) {
			e.printStackTrace();
			addErrorMessage(context, "search.failure");
			return error();
			// TODO: handle exception
		}
	}
	
	//Adds zeros to TRN
    public static String doTRNBumpUp(String trnNbr) {
           int trnLen = 9;
           String trnVal = "";
           //Pads the TRN with the required number of zeros
           trnVal = StringUtils.leftPad(trnNbr, trnLen, "0");
           return trnVal;
    }
    
    //Checks if TRN is a business TRN
    public boolean isBusinessTRN(String trnNbr){
          String tmpTRN = "";
          boolean isBiz = false;
          if(!StringUtils.isBlank(trnNbr)){
                tmpTRN = doTRNBumpUp(trnNbr); 
          }
          if(!StringUtils.isBlank(tmpTRN)){
                if(tmpTRN.startsWith("0", 0)){
                      isBiz = true;
                }
          }
          
          return isBiz;
    }


	
	
	
	/**
	 * Convenience Method used to get picture when TRN is used for person search
	 * 
	 * @param trn
	 * @return
	 */
	private String getPictureFromBIMS(String trn, RoadCheckInitiateView roadCheckInitiateView) {
		//String noCache = "&nocache=" + Math.random(); // hack to prevent caching
		roadCheckInitiateView.setDriverBadge(false);
		roadCheckInitiateView.setConductionBadge(false);
		roadCheckInitiateView.setBimsBadgeType("");

		String photoUrl = "";
		try {
			
				List<BadgeBO> badges = this.getBIMSWebService().getBadgeDetailsDriverConductor(trn);
				
				byte[] photoImage = null;
				
				if(badges == null || badges.size() < 1){
					return "";
				}
				
				for(BadgeBO badge : badges){
					
					if(badge.getBadgeDesc().toLowerCase().startsWith("c")){
						roadCheckInitiateView.setConductionBadge(true);
						if(photoImage == null && badge.getPhotoImg() != null){
							photoImage = badge.getPhotoImg();
						}
						
					}
					
					if(badge.getBadgeDesc().toLowerCase().startsWith("d")){
						roadCheckInitiateView.setDriverBadge(true);
						if(photoImage == null && badge.getPhotoImg() != null){
							photoImage = badge.getPhotoImg();
						}
						
					}
				}

	
			// Photo
			if (photoImage != null) {

				String encodedString = Base64.encode(photoImage);
				photoUrl = "data:image/jpg;base64," + encodedString;
			}

		} catch (NoRecordFoundException e) {
			e.printStackTrace();

		} catch (RequiredFieldMissingException e) {
			e.printStackTrace();
		}

//		try {
//			BadgeBO badge = this.getBIMSWebServicePortProxy().getBadgeDetails(trn, "d");
//
//			if (badge != null) {
//				roadCheckInitiateView.setDriverBadge(true);
//			}
//			// Photo
//			if (badge.getPhotoImg() != null) {
//				// photoUrl = "/PhotoServlet?badgeNo=" + badge.getTrn() + "&badgeType=" + badge.getBadgeDesc()+
//				// "&serviceType=" + "BIMS" + noCache;
//				String encodedString = Base64.encode(badge.getPhotoImg());
//				photoUrl = "data:image/jpg;base64," + encodedString;
//			}
//
//		} catch (fsl.ta.toms.roms.webservices.bims.NoRecordFoundException e1) {
//			e1.printStackTrace();
//
//		} catch (fsl.ta.toms.roms.webservices.bims.NoRecordFoundException e1) {
//			e1.printStackTrace();
//		}

		if (roadCheckInitiateView.isDriverBadge() && roadCheckInitiateView.isConductionBadge()) {
			roadCheckInitiateView.setBimsBadgeType("Conductor" + "\n" + "Driver");
		} else if (roadCheckInitiateView.isConductionBadge()) {
			roadCheckInitiateView.setBimsBadgeType("Conductor");
		} else if (roadCheckInitiateView.isDriverBadge()) {
			roadCheckInitiateView.setBimsBadgeType("Driver");
		} else {
			roadCheckInitiateView.setBimsBadgeType("None");
		}
		return photoUrl;
	}

	/**
	 * This is a helper function which sets details for person on the view By O. Anguin
	 * 
	 * @param roadCheckInView
	 */
	public void setDetails(RoadCheckInitiateView roadCheckInView, TrnDTO person) {
		/* roadCheckInView.setHomePhoneNo("876-555-7777");
		 * roadCheckInView.getAddressView().setAddressLine1(StringUtil.isSet
		 * (person.getAddressLine1())?person.getAddressLine1():"12 Main Street");
		 * roadCheckInView.getAddressView().setAddressLine2(person.getAddressLine2());
		 * roadCheckInView.getAddressView().setMarkText(person.getMarkText());
		 * roadCheckInView.getAddressView().setParish(person.getParishCode());
		 * roadCheckInView.getAddressView().setPoBoxNo(person.getPoBoxNo());
		 * roadCheckInView.getAddressView().setPoLocationName(person.getPoLocationName());
		 * roadCheckInView.getAddressView
		 * ().setStreetName(StringUtil.isSet(person.getStreetName())?person.getStreetName():"Main Street");
		 * roadCheckInView
		 * .getAddressView().setStreetNo(StringUtil.isSet(person.getStreetNo())?person.getStreetNo():"12");
		 * //roadCheckInView.setBadgeType("C"); */

		if (person != null) {
			roadCheckInView.getAddressView().setMarkText(person.getAddrMarkText());
			roadCheckInView.getAddressView().setParish(person.getAddrParishCode());

			roadCheckInView.getAddressView().setPoBoxNo(person.getAddrPoBoxNo());
			roadCheckInView.getAddressView().setPoLocationName(person.getAddrPoLocName());
			roadCheckInView.getAddressView().setStreetName(person.getAddrStreetName());
			roadCheckInView.getAddressView().setStreetNo(person.getAddrStreetNo());
		}

	}

	public void setDetails(RoadCheckInitiateView roadCheckInView, PersonBO person) {

		if (person != null) {
			roadCheckInView.getAddressView().setMarkText(person.getMarkText());
			roadCheckInView.getAddressView().setParish(person.getParishCode());

			roadCheckInView.getAddressView().setPoBoxNo(person.getPoBoxNo());
			roadCheckInView.getAddressView().setPoLocationName(person.getPoLocationName());
			roadCheckInView.getAddressView().setStreetName(person.getStreetName());
			roadCheckInView.getAddressView().setStreetNo(person.getStreetNo());
			roadCheckInView.setMobilePhoneNo(person.getTelephoneCell());
			roadCheckInView.setHomePhoneNo(person.getTelephoneHome());
			roadCheckInView.setWorkPhoneNo(person.getTelephoneWork());
		}

	}

	public Event searchBIMS(RequestContext context) {
		RoadCheckVerifyIdView roadCheckVerifyIdView = (RoadCheckVerifyIdView) context.getFlowScope().get("verifyIdView");
		try {



			roadCheckVerifyIdView.setShowDialog(false);
			context.getFlowScope().put("verifyIdView", roadCheckVerifyIdView);
			context.getFlowScope().put("badges", new BadgeTableBean());

			boolean error = false;
			if (StringUtils.isBlank(roadCheckVerifyIdView.getFirstName())) {
				addErrorMessageWithParameter(context, "RequiredFields", "First Name");
				error = true;
			}

			if (StringUtils.isBlank(roadCheckVerifyIdView.getLastName())) {
				addErrorMessageWithParameter(context, "RequiredFields", "Last Name");
				error = true;
			}

			if (error == true) {
				return error();
			}

			NameUtil nameUtil = new NameUtil();
			// DataTableMemory dataTable = null;

			List<BadgeBO> badgeBOList = null;
			List<BadgeNameSearchView> badgeNameSearchViewList = new ArrayList<BadgeNameSearchView>();
			BadgeNameSearchView badgeNameSearchView = new BadgeNameSearchView();

			badgeBOList = getBIMSWebService().getBadgeByPersonDetails(roadCheckVerifyIdView.getFirstName(), roadCheckVerifyIdView.getMiddleName(), roadCheckVerifyIdView.getLastName(), getRomsLoggedInUser().getUsername());

			if (badgeBOList == null || badgeBOList.size() <= 0) {
				addErrorMessage(context, "Norecordfound");
				roadCheckVerifyIdView.setDisableInitiateBtn(false);
				roadCheckVerifyIdView.setShowDialog(false);
				context.getFlowScope().put("verifyIdView", roadCheckVerifyIdView);
				return error();
			}

			if (badgeBOList != null && badgeBOList.size() > 0) {

				// dataTable = new DataTableMemory(badgeBOList,
				// ((DataTableMemory)context.getFlowScope().put("dataTable", dataTable)).rowsPerPage,
				// badgeBOList.size());
				// context.getFlowScope().put("dataTable", dataTable);
				// String noCache = "&nocache=" + Math.random(); //hack to prevent caching
				for (BadgeBO badge : badgeBOList) {
					badgeNameSearchView = new BadgeNameSearchView();
					badgeNameSearchView.setFirstName(WordUtils.capitalize(badge.getFirstName().toLowerCase(), Constants.STRING_DELIM_ARRAY));
					badgeNameSearchView.setLastName(WordUtils.capitalize(badge.getLastName().toLowerCase(), Constants.STRING_DELIM_ARRAY));
					if (badge.getMidName() != null) {
						badgeNameSearchView.setMidName(WordUtils.capitalize(badge.getMidName().toLowerCase(), Constants.STRING_DELIM_ARRAY));
					}
					badgeNameSearchView.setFullName(nameUtil.getLastNameCapsFirstNameMiddleName(badge.getFirstName(), badge.getLastName(), badge.getMidName()));
					badgeNameSearchView.setTrn(badge.getTrn());
					badgeNameSearchView.setBadgeDesc(badge.getBadgeDesc());
					// Photo
					if (badge.getPhotoImg() != null) {
						// badgeNameSearchView.setPhoto(new DefaultStreamedContent(new
						// ByteArrayInputStream(badge.getPhotoImg()), "image/png"));
						// badgeNameSearchView.setPhotoURL("/PhotoServlet?badgeNo=" + badge.getTrn() + "&badgeType=" +
						// badge.getBadgeDesc() + "&serviceType=" + "BIMS"+ noCache);
						String encodedString = Base64.encode(badge.getPhotoImg());
						badgeNameSearchView.setPhotoURL("data:image/jpg;base64," + encodedString);
					}
					badgeNameSearchViewList.add(badgeNameSearchView);
				}

				context.getFlowScope().put("badges", new BadgeTableBean(badgeNameSearchViewList));
				roadCheckVerifyIdView.setNameSearchTitle("Name Search: " + nameUtil.getLastNameCapsFirstNameMiddleName(roadCheckVerifyIdView.getFirstName(), roadCheckVerifyIdView.getLastName(), roadCheckVerifyIdView.getMiddleName()));
				roadCheckVerifyIdView.setShowDialog(true);
				roadCheckVerifyIdView.setDisableInitiateBtn(false);
				context.getFlowScope().put("verifyIdView", roadCheckVerifyIdView);

				return error();

			}
			if (badgeBOList != null && badgeBOList.size() == 1) {
				RoadCheckInitiateView roadCheckInitiateView = setDetailsToInitiateRoadCheck(context);
				roadCheckInitiateView.setTrn(badgeBOList.get(0).getTrn());
				roadCheckInitiateView.setFirstName(WordUtils.capitalize(badgeBOList.get(0).getFirstName().toLowerCase(), Constants.STRING_DELIM_ARRAY));
				roadCheckInitiateView.setLastName(WordUtils.capitalize(badgeBOList.get(0).getLastName().toLowerCase(), Constants.STRING_DELIM_ARRAY));

				PersonBO personBO = getDocumentsManagerService().getPersonByTRN(roadCheckInitiateView.getTrn());
				if (personBO == null) {
					this.setDetails(roadCheckInitiateView, this.getTRNWebService().getrnDTOByTrn(Integer.parseInt(roadCheckInitiateView.getTrn())));

				} else {
					this.setDetails(roadCheckInitiateView, personBO);

				}

				if (badgeBOList.get(0).getMidName() != null) {
					roadCheckInitiateView.setMiddleName(WordUtils.capitalize(badgeBOList.get(0).getMidName().toLowerCase(), Constants.STRING_DELIM_ARRAY));
				}

				// Photo
				// String noCache = "&nocache=" + Math.random(); //hack to prevent caching
				if (badgeBOList.get(0).getPhotoImg() != null) {
					// badgeNameSearchView.setPhotoURL("/PhotoServlet?badgeNo=" + badgeBOList.get(0).getTrn() +
					// "&badgeType=" + badgeBOList.get(0).getBadgeDesc()+ noCache);
					// roadCheckInitiateView.setPhotoURL("/PhotoServlet?badgeNo=" + badgeBOList.get(0).getTrn() +
					// "&badgeType=" + badgeBOList.get(0).getBadgeDesc() + "&serviceType=" + "BIMS"+ noCache);

					String encodedString = Base64.encode(badgeBOList.get(0).getPhotoImg());
					roadCheckInitiateView.setPhotoURL("data:image/jpg;base64," + encodedString);

					roadCheckInitiateView.setShowPhoto(true);
				}
				getPictureFromBIMS(badgeBOList.get(0).getTrn(), roadCheckInitiateView);
				roadCheckInitiateView.setFromSearch(false);
				context.getFlowScope().put("initiateView", roadCheckInitiateView);

			}

			return success();

		} catch (NoRecordFoundException e) {
			e.printStackTrace();
			roadCheckVerifyIdView.setDisableInitiateBtn(false);
			context.getFlowScope().put("verifyIdView", roadCheckVerifyIdView);
			addErrorMessage(context, "Norecordfound");
			return error();

		}

		catch (TaxPayerClosedException e) {
			addErrorMessage(context, "TRN.Status.Closed");
			e.printStackTrace();
			return error();
		} catch (TaxPayerUnintendedException e) {
			addErrorMessage(context, "TRN.Status.Invalid");
			e.printStackTrace();
			return error();
		} catch (TaxPayerDeceasedException e) {
			addErrorMessage(context, "TRN.Status.Deceased");
			e.printStackTrace();
			return error();
		} catch (TaxPayerRetiredException e) {
			addErrorMessage(context, "TRN.Status.Retired");
			e.printStackTrace();
			return error();
		} catch (InvalidTaxPayerException e) {
			addErrorMessage(context, "TRN.Status.Invalid");
			e.printStackTrace();
			return error();
		}

		catch (Exception e) {
			e.printStackTrace();
			addErrorMessage(context, "search.failure");
			return error();
			// TODO: handle exception
		}

	}

	public Event selectBadge(RequestContext context) {
		try {
			BadgeTableBean badgeTableBean = (BadgeTableBean) context.getFlowScope().get("badges");

			RoadCheckInitiateView roadCheckInitiateView = setDetailsToInitiateRoadCheck(context);
			roadCheckInitiateView.setTrn(badgeTableBean.getSelectedBadge().getTrn());
			roadCheckInitiateView.setFirstName(WordUtils.capitalize(badgeTableBean.getSelectedBadge().getFirstName().toLowerCase(), Constants.STRING_DELIM_ARRAY));
			roadCheckInitiateView.setLastName(WordUtils.capitalize(badgeTableBean.getSelectedBadge().getLastName().toLowerCase(), Constants.STRING_DELIM_ARRAY));
			if (badgeTableBean.getSelectedBadge().getMidName() != null) {
				roadCheckInitiateView.setMiddleName(WordUtils.capitalize(badgeTableBean.getSelectedBadge().getMidName().toLowerCase(), Constants.STRING_DELIM_ARRAY));
			}

			// Photo
			String noCache = "&nocache=" + Math.random(); // hack to prevent caching
			if (badgeTableBean.getSelectedBadge().getPhotoURL() != null) {
				// badgeNameSearchView.setPhotoURL("/PhotoServlet?badgeNo=" + badgeBOList.get(0).getTrn() +
				// "&badgeType=" + badgeBOList.get(0).getBadgeDesc()+ noCache);
				roadCheckInitiateView.setPhotoURL(badgeTableBean.getSelectedBadge().getPhotoURL());

				roadCheckInitiateView.setShowPhoto(true);
			}

			// this.setDetails(roadCheckInitiateView,
			// this.getTRNWebServicePortProxy().getrnDTOByTrn(Integer.parseInt(roadCheckInitiateView.getTrn())));
			PersonBO personBO = getDocumentsManagerService().getPersonByTRN(roadCheckInitiateView.getTrn());
			if (personBO == null) {
				this.setDetails(roadCheckInitiateView, this.getTRNWebService().getrnDTOByTrn(Integer.parseInt(roadCheckInitiateView.getTrn())));

			} else {
				this.setDetails(roadCheckInitiateView, personBO);

			}
			getPictureFromBIMS(badgeTableBean.getSelectedBadge().getTrn(), roadCheckInitiateView);
			roadCheckInitiateView.setFromSearch(false);
			context.getFlowScope().put("initiateView", roadCheckInitiateView);

			return success();

		}

		catch (TaxPayerClosedException e) {
			addErrorMessage(context, "TRN.Status.Closed");
			e.printStackTrace();
			return error();
		} catch (TaxPayerUnintendedException e) {
			addErrorMessage(context, "TRN.Status.Invalid");
			e.printStackTrace();
			return error();
		} catch (TaxPayerDeceasedException e) {
			addErrorMessage(context, "TRN.Status.Deceased");
			e.printStackTrace();
			return error();
		} catch (TaxPayerRetiredException e) {
			addErrorMessage(context, "TRN.Status.Retired");
			e.printStackTrace();
			return error();
		} catch (InvalidTaxPayerException e) {
			addErrorMessage(context, "TRN.Status.Invalid");
			e.printStackTrace();
			return error();
		}

		catch (Exception e) {
			addErrorMessage(context, "search.failure");
			e.printStackTrace();
			return error();
			// TODO: handle exception
		}

	}

	public Event deleteMVRow(RequestContext context) {
		try {
			ArrayList<OffenceOutcomeView> offenceOutcomes = (ArrayList<OffenceOutcomeView>) context.getFlowScope().get("mvOffenceOutcomes");

			RecordOffenceOutcomeView recordOffenceOutcomeView = (RecordOffenceOutcomeView) context.getFlowScope().get("recordOffenceOutcomeView");

			deleteRow(offenceOutcomes, recordOffenceOutcomeView.isMvSelectAllOffenceOutcome());
			recordOffenceOutcomeView.setMvSelectAllOffenceOutcome(false);
			return success();
		} catch (Exception e) {
			e.printStackTrace();
			;
			return error();
		}
	}

	public Event deleteDLRow(RequestContext context) {
		try {
			ArrayList<OffenceOutcomeView> offenceOutcomes = (ArrayList<OffenceOutcomeView>) context.getFlowScope().get("dlOffenceOutcomes");

			RecordOffenceOutcomeView recordOffenceOutcomeView = (RecordOffenceOutcomeView) context.getFlowScope().get("recordOffenceOutcomeView");

			deleteRow(offenceOutcomes, recordOffenceOutcomeView.isDlSelectAllOffenceOutcome());
			recordOffenceOutcomeView.setDlSelectAllOffenceOutcome(false);
			return success();
		} catch (Exception e) {
			e.printStackTrace();
			;
			return error();
		}
	}

	public Event deleteRLRow(RequestContext context) {
		try {
			ArrayList<OffenceOutcomeView> offenceOutcomes = (ArrayList<OffenceOutcomeView>) context.getFlowScope().get("rlOffenceOutcomes");

			RecordOffenceOutcomeView recordOffenceOutcomeView = (RecordOffenceOutcomeView) context.getFlowScope().get("recordOffenceOutcomeView");

			deleteRow(offenceOutcomes, recordOffenceOutcomeView.isRlSelectAllOffenceOutcome());
			recordOffenceOutcomeView.setRlSelectAllOffenceOutcome(false);
			return success();
		} catch (Exception e) {
			e.printStackTrace();
			;
			return error();
		}
	}

	public Event deleteCHRow(RequestContext context) {
		try {
			ArrayList<OffenceOutcomeView> offenceOutcomes = (ArrayList<OffenceOutcomeView>) context.getFlowScope().get("chOffenceOutcomes");

			RecordOffenceOutcomeView recordOffenceOutcomeView = (RecordOffenceOutcomeView) context.getFlowScope().get("recordOffenceOutcomeView");

			deleteRow(offenceOutcomes, recordOffenceOutcomeView.isChSelectAllOffenceOutcome());
			recordOffenceOutcomeView.setChSelectAllOffenceOutcome(false);
			return success();
		} catch (Exception e) {
			e.printStackTrace();
			;
			return error();
		}
	}

	public Event deleteBdgRow(RequestContext context) {
		try {
			ArrayList<OffenceOutcomeView> offenceOutcomes = (ArrayList<OffenceOutcomeView>) context.getFlowScope().get("bdgOffenceOutcomes");

			RecordOffenceOutcomeView recordOffenceOutcomeView = (RecordOffenceOutcomeView) context.getFlowScope().get("recordOffenceOutcomeView");

			deleteRow(offenceOutcomes, recordOffenceOutcomeView.isBdgSelectAllOffenceOutcome());
			recordOffenceOutcomeView.setBdgSelectAllOffenceOutcome(false);
			return success();
		} catch (Exception e) {
			e.printStackTrace();
			;
			return error();
		}
	}

	public Event deleteOtherRow(RequestContext context) {
		try {
			ArrayList<OffenceOutcomeView> offenceOutcomes = (ArrayList<OffenceOutcomeView>) context.getFlowScope().get("otOffenceOutcomes");

			RecordOffenceOutcomeView recordOffenceOutcomeView = (RecordOffenceOutcomeView) context.getFlowScope().get("recordOffenceOutcomeView");

			deleteRow(offenceOutcomes, recordOffenceOutcomeView.isOtSelectAllOffenceOutcome());
			recordOffenceOutcomeView.setOtSelectAllOffenceOutcome(false);
			return success();
		} catch (Exception e) {
			e.printStackTrace();
			;
			return error();
		}
	}

	public void deleteRow(ArrayList<OffenceOutcomeView> offenceOutcomes, boolean selectAllOffenceOutccome) {
		try {

			Iterator<OffenceOutcomeView> iterator = offenceOutcomes.iterator();
			if (offenceOutcomes != null) {
				while (iterator.hasNext()) {
					OffenceOutcomeView offenceOutcomeView = iterator.next();
					if (offenceOutcomeView.isRowSelected()) {
						iterator.remove();
					}
				}

			}

		}

		catch (Exception e) {
			e.printStackTrace();
			;
		}

	}

	@SuppressWarnings("unchecked")
	public Event addRow(RequestContext context) {
		try {

			ArrayList<OffenceOutcomeView> offOutcomes = (ArrayList<OffenceOutcomeView>) context.getFlowScope().get("offenceOutcomes");

			OffenceOutcomeView offenceOutcomeView = new OffenceOutcomeView();

			offOutcomes.add(0, offenceOutcomeView);
			context.getFlowScope().put("offenceOutcomes", offOutcomes);

			return error();

			/* List<OffenceBO> listAllOffence= (List<OffenceBO> )context.getFlowScope().get("allOffenceList");
			 * Iterator<OffenceBO> iterator = listAllOffence.iterator(); if(offOutcomes!=null){ for(OffenceOutcomeView
			 * offOut : offOutcomes){ while(iterator.hasNext()){ OffenceBO offence = iterator.next();
			 * if(offence.getOffenceId().equals(offOut.getOffenceId())){ iterator.remove(); } } } }
			 * context.getFlowScope().put("offenceList", listAllOffence); */
			// return success();
		} catch (Exception e) {
			e.printStackTrace();
			;
			return error();
		}
	}

	public void indicateActionMV(RequestContext context) {
		try {
			RecordOffenceOutcomeView recordOffenceOutcomeView = (RecordOffenceOutcomeView) context.getFlowScope().get("recordOffenceOutcomeView");
			ArrayList<OffenceOutcomeView> offOutcomes = (ArrayList<OffenceOutcomeView>) context.getFlowScope().get("mvOffenceOutcomes");
			
			logger.info("here " + recordOffenceOutcomeView.getMvActionToBeTaken());
			
			recordOffenceOutcomeView.setMvOpenRecordCount(0);
			if (recordOffenceOutcomeView.getMvActionToBeTaken().equalsIgnoreCase("R")) {
				List<OffenceOutcomeView> offenceOutcomes1 = new ArrayList<OffenceOutcomeView>();
				OffenceOutcomeView offenceOutcomeView1 = new OffenceOutcomeView();
				offenceOutcomes1.add(offenceOutcomeView1);
				context.getFlowScope().put("mvOffenceOutcomes", offenceOutcomes1);

				// recordOffenceOutcomeView.setOpenRecordCount(1);
				// recordOffenceOutcomeView.setDisableBadgeTab(true);
				// recordOffenceOutcomeView.setDisableTableBtn(true);
				// OffenceOutcomeView offenceOutcomeView =new OffenceOutcomeView();

				// offOutcomes.add(0, offenceOutcomeView);
			} else {
				deleteRow(offOutcomes, true);
			}

		} catch (Exception e) {
			e.printStackTrace();
			;

		}
	}

	public void selectQueriesToPerform() {
		RequestContext context = RequestContextHolder.getRequestContext();
		RoadCheckInitiateView roadCheckInitiateView = (RoadCheckInitiateView) context.getFlowScope().get("initiateView");

		if (roadCheckInitiateView != null) {

			if (roadCheckInitiateView.getDlNo() != null) {
				
				logger.info("dl no: " + roadCheckInitiateView.getDlNo());
			}
			if (roadCheckInitiateView.getBadgeNo() != null) {
				
				logger.info("badge no: " + roadCheckInitiateView.getBadgeNo());
				// roadCheckInitiateView.setDlNo(roadCheckInitiateView.getDlNo());
				roadCheckInitiateView.setBadgeNo(roadCheckInitiateView.getBadgeNo());
			}
		}
	}

	public void indicateActionMV() {
		try {
			RequestContext context = RequestContextHolder.getRequestContext();
			RecordOffenceOutcomeView recordOffenceOutcomeView = (RecordOffenceOutcomeView) context.getFlowScope().get("recordOffenceOutcomeView");
			ArrayList<OffenceOutcomeView> offOutcomes = (ArrayList<OffenceOutcomeView>) context.getFlowScope().get("mvOffenceOutcomes");

			this.clearSuportingDetails(context);
			
			recordOffenceOutcomeView.setMvOpenRecordCount(0);
			if (recordOffenceOutcomeView.getMvActionToBeTaken().equalsIgnoreCase("R")) {
				List<OffenceOutcomeView> offenceOutcomes1 = new ArrayList<OffenceOutcomeView>();
				OffenceOutcomeView offenceOutcomeView1 = new OffenceOutcomeView();

				List<OffenceBO> offenceList = (List<OffenceBO>) context.getFlowScope().get("mvOffenceList");
				if (offenceList.size() == 0) {
					offenceOutcomeView1.setOffenceId(Constants.DEFAULT_OFFENCE);
					offenceOutcomeView1.setOffenceShortDescription(Constants.DEFAULT_OFFENCE_SHORT_DESCRIPTION);
					offenceOutcomeView1.setOffenceDescription(Constants.DEFAULT_OFFENCE_DESCRIPTION);
				} else {
					RoadCheckInitiateView roadCheckInitiateView = (RoadCheckInitiateView) context.getFlowScope().get("initiateView");

//					if (roadCheckInitiateView.getActivityType().equalsIgnoreCase("U")) {
//						offenceOutcomeView1.setWarnForProsecution(true);
//					}
				}

				offenceOutcomes1.add(offenceOutcomeView1);
				context.getFlowScope().put("mvOffenceOutcomes", offenceOutcomes1);

				// recordOffenceOutcomeView.setOpenRecordCount(1);
				// recordOffenceOutcomeView.setDisableBadgeTab(true);
				// recordOffenceOutcomeView.setDisableTableBtn(true);
				// OffenceOutcomeView offenceOutcomeView =new OffenceOutcomeView();

				// offOutcomes.add(0, offenceOutcomeView);
			} else {
				// deleteRow(offOutcomes, true);
				context.getFlowScope().put("mvOffenceOutcomes", new ArrayList<OffenceOutcomeView>());
			}

		} catch (Exception e) {
			e.printStackTrace();
			;

		}
	}

	public void indicateActionDL() {
		try {
			RequestContext context = RequestContextHolder.getRequestContext();
			RecordOffenceOutcomeView recordOffenceOutcomeView = (RecordOffenceOutcomeView) context.getFlowScope().get("recordOffenceOutcomeView");
			ArrayList<OffenceOutcomeView> offOutcomes = (ArrayList<OffenceOutcomeView>) context.getFlowScope().get("dlOffenceOutcomes");

			this.clearSuportingDetails(context);
			
			recordOffenceOutcomeView.setDlOpenRecordCount(0);
			if (recordOffenceOutcomeView.getDlActionToBeTaken().equalsIgnoreCase("R")) {
				List<OffenceOutcomeView> offenceOutcomes2 = new ArrayList<OffenceOutcomeView>();
				OffenceOutcomeView offenceOutcomeView2 = new OffenceOutcomeView();

				List<OffenceBO> offenceList = (List<OffenceBO>) context.getFlowScope().get("dlOffenceList");
				if (offenceList.size() == 0) {
					offenceOutcomeView2.setOffenceId(Constants.DEFAULT_OFFENCE);
					offenceOutcomeView2.setOffenceShortDescription(Constants.DEFAULT_OFFENCE_SHORT_DESCRIPTION);
					offenceOutcomeView2.setOffenceDescription(Constants.DEFAULT_OFFENCE_DESCRIPTION);
				} else {
					RoadCheckInitiateView roadCheckInitiateView = (RoadCheckInitiateView) context.getFlowScope().get("initiateView");

//					if (roadCheckInitiateView.getActivityType().equalsIgnoreCase("U")) {
//						offenceOutcomeView2.setWarnForProsecution(true);
//					}
				}

				offenceOutcomes2.add(offenceOutcomeView2);
				context.getFlowScope().put("dlOffenceOutcomes", offenceOutcomes2);

			} else {
				// deleteRow(offOutcomes, true);
				context.getFlowScope().put("dlOffenceOutcomes", new ArrayList<OffenceOutcomeView>());
			}

		} catch (Exception e) {
			e.printStackTrace();
			;

		}
	}

	public void indicateActionRL() {
		try {
			RequestContext context = RequestContextHolder.getRequestContext();
			RecordOffenceOutcomeView recordOffenceOutcomeView = (RecordOffenceOutcomeView) context.getFlowScope().get("recordOffenceOutcomeView");
			ArrayList<OffenceOutcomeView> offOutcomes = (ArrayList<OffenceOutcomeView>) context.getFlowScope().get("rlOffenceOutcomes");

			this.clearSuportingDetails(context);
			
			recordOffenceOutcomeView.setRlOpenRecordCount(0);
			if (recordOffenceOutcomeView.getRlActionToBeTaken().equalsIgnoreCase("R")) {
				List<OffenceOutcomeView> offenceOutcomes3 = new ArrayList<OffenceOutcomeView>();
				OffenceOutcomeView offenceOutcomeView3 = new OffenceOutcomeView();
				
				List<OffenceBO> offenceList = (List<OffenceBO>) context.getFlowScope().get("rlOffenceList");
				if (offenceList.size() == 0) {
					offenceOutcomeView3.setOffenceId(Constants.DEFAULT_OFFENCE);
					offenceOutcomeView3.setOffenceShortDescription(Constants.DEFAULT_OFFENCE_SHORT_DESCRIPTION);
					offenceOutcomeView3.setOffenceDescription(Constants.DEFAULT_OFFENCE_DESCRIPTION);
				} else {
					RoadCheckInitiateView roadCheckInitiateView = (RoadCheckInitiateView) context.getFlowScope().get("initiateView");

//					if (roadCheckInitiateView.getActivityType().equalsIgnoreCase("U")) {
//						offenceOutcomeView3.setWarnForProsecution(true);
//					}
				}
				offenceOutcomes3.add(offenceOutcomeView3);
				context.getFlowScope().put("rlOffenceOutcomes", offenceOutcomes3);

			} else {
				// deleteRow(offOutcomes, true);
				context.getFlowScope().put("rlOffenceOutcomes", new ArrayList<OffenceOutcomeView>());
			}

		} catch (Exception e) {
			e.printStackTrace();
			;

		}
	}

	public void indicateActionBdg() {
		try {
			RequestContext context = RequestContextHolder.getRequestContext();
			RecordOffenceOutcomeView recordOffenceOutcomeView = (RecordOffenceOutcomeView) context.getFlowScope().get("recordOffenceOutcomeView");
			ArrayList<OffenceOutcomeView> offOutcomes = (ArrayList<OffenceOutcomeView>) context.getFlowScope().get("bdgOffenceOutcomes");

			this.clearSuportingDetails(context);
			
			recordOffenceOutcomeView.setBdgOpenRecordCount(0);
			if (recordOffenceOutcomeView.getBdgActionToBeTaken().equalsIgnoreCase("R")) {
				List<OffenceOutcomeView> offenceOutcomes4 = new ArrayList<OffenceOutcomeView>();
				OffenceOutcomeView offenceOutcomeView4 = new OffenceOutcomeView();

				List<OffenceBO> offenceList = (List<OffenceBO>) context.getFlowScope().get("bdgOffenceList");
				if (offenceList.size() == 0) {
					offenceOutcomeView4.setOffenceId(Constants.DEFAULT_OFFENCE);
					offenceOutcomeView4.setOffenceShortDescription(Constants.DEFAULT_OFFENCE_SHORT_DESCRIPTION);
					offenceOutcomeView4.setOffenceDescription(Constants.DEFAULT_OFFENCE_DESCRIPTION);
				} else {
					RoadCheckInitiateView roadCheckInitiateView = (RoadCheckInitiateView) context.getFlowScope().get("initiateView");

//					if (roadCheckInitiateView.getActivityType().equalsIgnoreCase("U")) {
//						offenceOutcomeView4.setWarnForProsecution(true);
//					}
				}
				offenceOutcomes4.add(offenceOutcomeView4);
				context.getFlowScope().put("bdgOffenceOutcomes", offenceOutcomes4);

			} else {
				// deleteRow(offOutcomes, true);
				context.getFlowScope().put("bdgOffenceOutcomes", new ArrayList<OffenceOutcomeView>());
			}

		} catch (Exception e) {
			e.printStackTrace();
			;

		}
	}

	public void indicateActionCH() {
		try {
			RequestContext context = RequestContextHolder.getRequestContext();
			RecordOffenceOutcomeView recordOffenceOutcomeView = (RecordOffenceOutcomeView) context.getFlowScope().get("recordOffenceOutcomeView");
			ArrayList<OffenceOutcomeView> offOutcomes = (ArrayList<OffenceOutcomeView>) context.getFlowScope().get("chOffenceOutcomes");

			this.clearSuportingDetails(context);
			
			recordOffenceOutcomeView.setChOpenRecordCount(0);
			if (recordOffenceOutcomeView.getChActionToBeTaken().equalsIgnoreCase("R")) {
				List<OffenceOutcomeView> offenceOutcomes5 = new ArrayList<OffenceOutcomeView>();
				OffenceOutcomeView offenceOutcomeView5 = new OffenceOutcomeView();

				List<OffenceBO> offenceList = (List<OffenceBO>) context.getFlowScope().get("chOffenceList");
				if (offenceList.size() == 0) {
					offenceOutcomeView5.setOffenceId(Constants.DEFAULT_OFFENCE);
					offenceOutcomeView5.setOffenceShortDescription(Constants.DEFAULT_OFFENCE_SHORT_DESCRIPTION);
					offenceOutcomeView5.setOffenceDescription(Constants.DEFAULT_OFFENCE_DESCRIPTION);
				} else {
					RoadCheckInitiateView roadCheckInitiateView = (RoadCheckInitiateView) context.getFlowScope().get("initiateView");

//					if (roadCheckInitiateView.getActivityType().equalsIgnoreCase("U")) {
//						offenceOutcomeView5.setWarnForProsecution(true);
//					}
				}
				offenceOutcomes5.add(offenceOutcomeView5);
				context.getFlowScope().put("chOffenceOutcomes", offenceOutcomes5);

			} else {
				// deleteRow(offOutcomes, true);
				context.getFlowScope().put("chOffenceOutcomes", new ArrayList<OffenceOutcomeView>());
			}

		} catch (Exception e) {
			e.printStackTrace();
			;

		}
	}

	public Event addMVRow(RequestContext context) {
		try {

			ArrayList<OffenceOutcomeView> offOutcomes = (ArrayList<OffenceOutcomeView>) context.getFlowScope().get("mvOffenceOutcomes");

			OffenceOutcomeView offenceOutcomeView = new OffenceOutcomeView();
			RecordOffenceOutcomeView recordOffenceOutcomeView = (RecordOffenceOutcomeView) context.getFlowScope().get("recordOffenceOutcomeView");

			// recordOffenceOutcomeView.setOpenRecordCount(recordOffenceOutcomeView.getOpenRecordCount() + 1);

			RoadCheckInitiateView roadCheckInitiateView = (RoadCheckInitiateView) context.getFlowScope().get("initiateView");

//			if (roadCheckInitiateView.getActivityType().equalsIgnoreCase("U")) {
//				offenceOutcomeView.setWarnForProsecution(true);
//			}
			offOutcomes.add(0, offenceOutcomeView);
			context.getFlowScope().put("mvOffenceOutcomes", offOutcomes);

			return success();
		} catch (Exception e) {
			e.printStackTrace();
			;
			return error();
		}
	}

	public Event addDLRow(RequestContext context) {
		try {

			ArrayList<OffenceOutcomeView> offOutcomes = (ArrayList<OffenceOutcomeView>) context.getFlowScope().get("dlOffenceOutcomes");

			OffenceOutcomeView offenceOutcomeView = new OffenceOutcomeView();

			RoadCheckInitiateView roadCheckInitiateView = (RoadCheckInitiateView) context.getFlowScope().get("initiateView");

//			if (roadCheckInitiateView.getActivityType().equalsIgnoreCase("U")) {
//				offenceOutcomeView.setWarnForProsecution(true);
//			}

			offOutcomes.add(0, offenceOutcomeView);
			context.getFlowScope().put("dlOffenceOutcomes", offOutcomes);

			return success();
		} catch (Exception e) {
			e.printStackTrace();
			;
			return error();
		}
	}

	public Event addRLRow(RequestContext context) {
		try {

			ArrayList<OffenceOutcomeView> offOutcomes = (ArrayList<OffenceOutcomeView>) context.getFlowScope().get("rlOffenceOutcomes");

			OffenceOutcomeView offenceOutcomeView = new OffenceOutcomeView();

			RoadCheckInitiateView roadCheckInitiateView = (RoadCheckInitiateView) context.getFlowScope().get("initiateView");

//			if (roadCheckInitiateView.getActivityType().equalsIgnoreCase("U")) {
//				offenceOutcomeView.setWarnForProsecution(true);
//			}

			offOutcomes.add(0, offenceOutcomeView);
			context.getFlowScope().put("rlOffenceOutcomes", offOutcomes);

			return success();
		} catch (Exception e) {
			e.printStackTrace();
			;
			return error();
		}
	}

	public Event addCHRow(RequestContext context) {
		try {

			ArrayList<OffenceOutcomeView> offOutcomes = (ArrayList<OffenceOutcomeView>) context.getFlowScope().get("chOffenceOutcomes");

			OffenceOutcomeView offenceOutcomeView = new OffenceOutcomeView();

			RoadCheckInitiateView roadCheckInitiateView = (RoadCheckInitiateView) context.getFlowScope().get("initiateView");

//			if (roadCheckInitiateView.getActivityType().equalsIgnoreCase("U")) {
//				offenceOutcomeView.setWarnForProsecution(true);
//			}

			offOutcomes.add(0, offenceOutcomeView);
			context.getFlowScope().put("chOffenceOutcomes", offOutcomes);

			return success();
		} catch (Exception e) {
			e.printStackTrace();
			;
			return error();
		}
	}

	public Event addBdgRow(RequestContext context) {
		try {

			ArrayList<OffenceOutcomeView> offOutcomes = (ArrayList<OffenceOutcomeView>) context.getFlowScope().get("bdgOffenceOutcomes");

			OffenceOutcomeView offenceOutcomeView = new OffenceOutcomeView();

			RoadCheckInitiateView roadCheckInitiateView = (RoadCheckInitiateView) context.getFlowScope().get("initiateView");

//			if (roadCheckInitiateView.getActivityType().equalsIgnoreCase("U")) {
//				offenceOutcomeView.setWarnForProsecution(true);
//			}

			offOutcomes.add(0, offenceOutcomeView);
			context.getFlowScope().put("bdgOffenceOutcomes", offOutcomes);

			return success();
		} catch (Exception e) {
			e.printStackTrace();
			;
			return error();
		}
	}

	public Event addOtherRow(RequestContext context) {
		try {

			ArrayList<OffenceOutcomeView> offOutcomes = (ArrayList<OffenceOutcomeView>) context.getFlowScope().get("otOffenceOutcomes");

			OffenceOutcomeView offenceOutcomeView = new OffenceOutcomeView();

			RoadCheckInitiateView roadCheckInitiateView = (RoadCheckInitiateView) context.getFlowScope().get("initiateView");

//			if (roadCheckInitiateView.getActivityType().equalsIgnoreCase("U")) {
//				offenceOutcomeView.setWarnForProsecution(true);
//			}

			offOutcomes.add(0, offenceOutcomeView);
			context.getFlowScope().put("otOffenceOutcomes", offOutcomes);

			return success();
		} catch (Exception e) {
			e.printStackTrace();
			;
			return error();
		}
	}

	public void initRowEdit(RowEditEvent event) {
		OffenceOutcomeView selectedoOffenceOutcomeView = (OffenceOutcomeView) event.getObject();

		RequestContext context = RequestContextHolder.getRequestContext();

		ArrayList<Integer> selectedOffOuutes = (ArrayList<Integer>) context.getFlowScope().get("offenceOutcomesSelected");

		List<OffenceBO> listAllOffence = (List<OffenceBO>) context.getFlowScope().get("allOffenceList");

		ArrayList<OffenceOutcomeView> offOutcomes = (ArrayList<OffenceOutcomeView>) context.getFlowScope().get("offenceOutcomes");
		Iterator<OffenceBO> iterator = listAllOffence.iterator();
		if (offOutcomes != null) {
			for (OffenceOutcomeView offOut : offOutcomes) {
				iterator = listAllOffence.iterator();
				logger.info("off out: " + offOut.getOffenceId());
				while (iterator.hasNext()) {
					OffenceBO offence = iterator.next();
					logger.info("offence.getOffenceId()");
					if (offence.getOffenceId().equals(offOut.getOffenceId()) && !offence.getOffenceId().equals(selectedoOffenceOutcomeView.getOffenceId())) {
						logger.info("remove offence.getOffenceId()");
						iterator.remove();
						break;
					}
				}

			}
		}
		context.getFlowScope().put("offenceList", listAllOffence);
		selectedOffOuutes.add(selectedoOffenceOutcomeView.getOffenceId());

		context.getFlowScope().put("offenceOutcomesSelected", selectedOffOuutes);

	}

	private List<Integer> returnIntegerOffenceList(ArrayList<OffenceOutcomeView> offenceOutcomeList) {
		List<Integer> offenceIntgegerList = new ArrayList<Integer>();

		for (OffenceOutcomeView offOutcome : offenceOutcomeList) {
			offenceIntgegerList.add(offOutcome.getOffenceId());
		}

		return offenceIntgegerList;

	}

	private List<Integer> returnIntegerParamList(List<ExcerptParamMappingBO> paramMappingBOList) {
		List<Integer> paramIntgegerList = new ArrayList<Integer>();

		for (ExcerptParamMappingBO param : paramMappingBOList) {
			paramIntgegerList.add(param.getParamMapId());
		}

		return paramIntgegerList;
	}

	private List<String> returnStringParamList(List<ExcerptParamMappingBO> paramMappingBOList) {
		List<String> paramStringList = new ArrayList<String>();

		for (ExcerptParamMappingBO param : paramMappingBOList) {
			paramStringList.add(param.getParamName());
		}

		return paramStringList;
	}

	public boolean validateRow(OffenceOutcomeView selectedoOffenceOutcomeView, ArrayList<OffenceOutcomeView> offOutcomes) {
		RequestContext context = RequestContextHolder.getRequestContext();
		boolean error = false;

		RecordOffenceOutcomeView recordOffenceOutcomeView = (RecordOffenceOutcomeView) context.getFlowScope().get("recordOffenceOutcomeView");

		RoadCheckInitiateView roadCheckInitiateView = (RoadCheckInitiateView) context.getFlowScope().get("initiateView");
		
		List<Integer> offMandatoryOutcomeIntegerList = (List<Integer>) context.getFlowScope().get("offMandatoryOutcomeIntegerList");
		List<OffenceMandatoryOutcomeBO> offMandatoryOutcomeList = (List<OffenceMandatoryOutcomeBO>) context.getFlowScope().get("offMandatoryOutcomeList");

				
		List<ExcerptParamMappingBO> paramMappingBOList = null;
		
		//System.err.println("Selected offence Id: "+selectedoOffenceOutcomeView.getOffenceId());
		
		List<Integer> selectedOffence = new  ArrayList<Integer>();
		
		selectedOffence.add(selectedoOffenceOutcomeView.getOffenceId());
		
		
		try {
			paramMappingBOList = getRoadCompliancyService().getRequiredExcerptParams(selectedOffence);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		if (selectedoOffenceOutcomeView.getOffenceId() == null || selectedoOffenceOutcomeView.getOffenceId() == 0) {
			addErrorMessage(context, "OffenceRequired");
			error = true;
		}

		else {
			List<Integer> offenceIntgegerList = returnIntegerOffenceList(offOutcomes);
			offenceIntgegerList.remove(selectedoOffenceOutcomeView.getOffenceId());// remove current object
			if (offenceIntgegerList.contains(selectedoOffenceOutcomeView.getOffenceId())) {
				addErrorMessage(context, "OffenceSelectedAlready");
				error = true;
			}
		}
		if (selectedoOffenceOutcomeView.isIssueSummons() == false && selectedoOffenceOutcomeView.isIssuewWarningNotice() == false && selectedoOffenceOutcomeView.isIssueWarningNP() == false && selectedoOffenceOutcomeView.isWarnForProsecution() == false && selectedoOffenceOutcomeView.isRemovePlate() == false) {
			addErrorMessage(context, "OutcomeRequired");
			error = true;
		}

		List<String> paramStringList = new ArrayList<String>();
		if (paramMappingBOList != null) {
			paramStringList = returnStringParamList(paramMappingBOList);
			if (paramStringList.contains(Constants.OffenceParam.LICENCE_TYPE) || paramStringList.contains(Constants.OffenceParam.ROUTE) || paramStringList.contains(Constants.OffenceParam.SEATING_CAPACITY) || paramStringList.contains(Constants.OffenceParam.ROUTE_END) || paramStringList.contains(Constants.OffenceParam.ROUTE_START)) {
	
				if (roadCheckInitiateView.isRlQueryDone() == false || (roadCheckInitiateView.isRlQueryDone() && roadCheckInitiateView.isNoRlFound()) ) {
					addErrorMessageText(context, "No road  licence found – offence requires road licence information.");
					
					error = true;
				}
	
			}
		}
		
		/* if(offOutcomes!=null){ for(OffenceOutcomeView offOut : offOutcomes){
		 * //if(offOut.getOffenceId().equals(selectedoOffenceOutcomeView.getOffenceId())){
		 * if(offOut.isIssuewWarningNotice()){ offOut.setIssueSummons(true); } //} } } */

		

		String offManErrorMsg = "";
		String offenceDesc = "";
		boolean offManError = false;
		boolean outcomeSelected = false;
		int outcomeCount = 0;
		int outcomeCountWithError = 0;
		if (offMandatoryOutcomeIntegerList != null) {
			if (offMandatoryOutcomeIntegerList.contains(selectedoOffenceOutcomeView.getOffenceId())) { //if offence has mandatory outcome
				// offManErrorMsg = offManOut.getOffenceDescription() + " mandatory outcome is ";
				for (OffenceMandatoryOutcomeBO offManOut : offMandatoryOutcomeList) {//counts total mandatory outcome for the offence
					if (offManOut.getOffenceId().equals(selectedoOffenceOutcomeView.getOffenceId())) {
						outcomeCount++;
					}
				}
				for (OffenceMandatoryOutcomeBO offManOut : offMandatoryOutcomeList) {
					if (offManOut.getOffenceId().equals(selectedoOffenceOutcomeView.getOffenceId())) {
						offenceDesc = "The mandatory outcome for "+offManOut.getOffenceDescription();
//						outcomeCountWithError++;
						if (offManOut.getOutcomeTypeId().equalsIgnoreCase(Constants.OutcomeType.ISSUE_SUMMONS)) {
							outcomeCountWithError++;
							if (outcomeSelected) {
								if (outcomeCount == outcomeCountWithError) {
									offManErrorMsg = offManErrorMsg + " and Issue Summons";
								} else {
									offManErrorMsg = offManErrorMsg + ", Issue Summons";
								}
							} else {
								offManErrorMsg = offManErrorMsg + "Issue Summons";
							}
							if (selectedoOffenceOutcomeView.isIssueSummons() == false) {
								offManError = true;
							}
							outcomeSelected = true;
						}
						if (offManOut.getOutcomeTypeId().equalsIgnoreCase(Constants.OutcomeType.ISSUE_WARNING_NOTICE)) {
							outcomeCountWithError++;
							if (outcomeSelected) {
								if (outcomeCount == outcomeCountWithError) {
									offManErrorMsg = offManErrorMsg + " and Issue Warning Notice";
								} else {
									offManErrorMsg = offManErrorMsg + ", Issue Warning Notice";
								}
							} else {
								offManErrorMsg = offManErrorMsg + " Issue Warning Notice";
							}
							if (selectedoOffenceOutcomeView.isIssuewWarningNotice() == false) {
								offManError = true;
							}
							outcomeSelected = true;
						}

						if (offManOut.getOutcomeTypeId().equalsIgnoreCase(Constants.OutcomeType.REMOVE_PLATES)) {
							outcomeCountWithError++;
							if (outcomeSelected) {
								if (outcomeCount == outcomeCountWithError) {
									offManErrorMsg = offManErrorMsg + " and Remove Plates";
								} else {
									offManErrorMsg = offManErrorMsg + ", Remove Plates";
								}
							} else {
								offManErrorMsg = offManErrorMsg + "Remove Plates";
							}
							if (selectedoOffenceOutcomeView.isRemovePlate() == false) {
								offManError = true;
							}
							outcomeSelected = true;
						}

						if (offManOut.getOutcomeTypeId().equalsIgnoreCase(Constants.OutcomeType.WARNED_FOR_PROSECUTION)) {
							outcomeCountWithError++;
							if (outcomeSelected) {
								if (outcomeCount == outcomeCountWithError) {
									offManErrorMsg = offManErrorMsg + " and Warning for Prosecution";
								} else {
									offManErrorMsg = offManErrorMsg + ", Warning for Prosecution";
								}

							} else {
								offManErrorMsg = offManErrorMsg + "Warning for Prosecution";
							}
							if (selectedoOffenceOutcomeView.isWarnForProsecution() == false) {
								offManError = true;
							}
							outcomeSelected = true;
						}

						if (offManOut.getOutcomeTypeId().equalsIgnoreCase(Constants.OutcomeType.WARNED_NO_PROSECUTION)) {
							outcomeCountWithError++;
							if (outcomeSelected) {
								if (outcomeCount == outcomeCountWithError) {
									offManErrorMsg = offManErrorMsg + " and Warning No Prosecution";
								} else {
									offManErrorMsg = offManErrorMsg + ", Warning No Prosecution";
								}
							} else {
								offManErrorMsg = offManErrorMsg + "Warning No Prosecution";
							}
							if (selectedoOffenceOutcomeView.issueWarningNP == false) {
								offManError = true;
							}
							outcomeSelected = true;
						}
					}
				}
			}
			/*else{
				if (selectedoOffenceOutcomeView.isIssuewWarningNotice()){
					if(!(selectedoOffenceOutcomeView.isIssueSummons() || selectedoOffenceOutcomeView.isWarnForProsecution())) {
						//selectedoOffenceOutcomeView.setIssueSummons(true);
						addErrorMessage(context, "IssueWarningNotice");
						error = true;
					}
				}
			}*/
		}
		

		if (offManError == true) {
			if (outcomeCount == 1) {
				addErrorMessageText(context, offenceDesc + " is " + offManErrorMsg + ".");
			} else {
				addErrorMessageText(context, offenceDesc + " are " + offManErrorMsg + ".");
			}
			error = true;
		}

		
		if (selectedoOffenceOutcomeView.isIssuewWarningNotice()){
			if(!(selectedoOffenceOutcomeView.isIssueSummons() || selectedoOffenceOutcomeView.isWarnForProsecution())) {
				//selectedoOffenceOutcomeView.setIssueSummons(true);
				addErrorMessage(context, "IssueWarningNotice");
				error = true;
			}
		}
		if ((selectedoOffenceOutcomeView.isIssueSummons()) && (selectedoOffenceOutcomeView.isIssueWarningNP() || selectedoOffenceOutcomeView.isWarnForProsecution())) {
			addErrorMessage(context, "IssueSummonsAndWNMatrix");
			error = true;
		}

		/* if(selectedoOffenceOutcomeView.isIssuewWarningNotice() && (selectedoOffenceOutcomeView.isIssueWarningNP() ||
		 * selectedoOffenceOutcomeView.isWarnForProsecution())){ addErrorMessage(context, "IssueWarningNoticeMatrix");
		 * error=true; } */

		else if (selectedoOffenceOutcomeView.isIssueWarningNP() && (selectedoOffenceOutcomeView.isIssueSummons() || selectedoOffenceOutcomeView.isIssuewWarningNotice() || selectedoOffenceOutcomeView.isWarnForProsecution())) {
			addErrorMessage(context, "WNPMantrix");
			error = true;
		}

		else if (selectedoOffenceOutcomeView.isWarnForProsecution() && (selectedoOffenceOutcomeView.isIssueSummons() || selectedoOffenceOutcomeView.isIssueWarningNP())) {
			addErrorMessage(context, "WFPMantrix");
			error = true;
		}

		return error;
	}

	public void initRowEditMV(RowEditEvent event) {
		RequestContext context = RequestContextHolder.getRequestContext();
		RecordOffenceOutcomeView recordOffenceOutcomeView = (RecordOffenceOutcomeView) context.getFlowScope().get("recordOffenceOutcomeView");
		OffenceOutcomeView selectedoOffenceOutcomeView = (OffenceOutcomeView) event.getObject();

		OffenceOutcomeView offenceOutcomeView = new OffenceOutcomeView();
		offenceOutcomeView.setValues(selectedoOffenceOutcomeView);
		recordOffenceOutcomeView.setCurrentMVOffenceOutcomeView(offenceOutcomeView);

		recordOffenceOutcomeView.setMvOpenRecordCount(recordOffenceOutcomeView.getMvOpenRecordCount() + 1);
		// System.err.println("initRowEditMV recordOffenceOutcomeView.getOpenRecordCount(): " +
		// recordOffenceOutcomeView.getMvOpenRecordCount());
	}

	public void initRowEditDL(RowEditEvent event) {
		RequestContext context = RequestContextHolder.getRequestContext();
		RecordOffenceOutcomeView recordOffenceOutcomeView = (RecordOffenceOutcomeView) context.getFlowScope().get("recordOffenceOutcomeView");

		OffenceOutcomeView selectedoOffenceOutcomeView = (OffenceOutcomeView) event.getObject();

		OffenceOutcomeView offenceOutcomeView = new OffenceOutcomeView();
		offenceOutcomeView.setValues(selectedoOffenceOutcomeView);
		recordOffenceOutcomeView.setCurrentDLOffenceOutcomeView(offenceOutcomeView);

		recordOffenceOutcomeView.setDlOpenRecordCount(recordOffenceOutcomeView.getDlOpenRecordCount() + 1);
		// System.err.println("initRowEditMV recordOffenceOutcomeView.getOpenRecordCount(): " +
		// recordOffenceOutcomeView.getMvOpenRecordCount());
	}

	public void initRowEditRL(RowEditEvent event) {
		RequestContext context = RequestContextHolder.getRequestContext();
		RecordOffenceOutcomeView recordOffenceOutcomeView = (RecordOffenceOutcomeView) context.getFlowScope().get("recordOffenceOutcomeView");

		OffenceOutcomeView selectedoOffenceOutcomeView = (OffenceOutcomeView) event.getObject();

		logger.info("initRowEditRL");
		/* recordOffenceOutcomeView.setCurrentRLOffenceOutcomeView(selectedoOffenceOutcomeView);
		 * System.err.println("Summons: " + recordOffenceOutcomeView.getCurrentRLOffenceOutcomeView().isIssueSummons());
		 * System.err.println("Warning Notice: " +
		 * recordOffenceOutcomeView.getCurrentRLOffenceOutcomeView().isIssuewWarningNotice());
		 * System.err.println("WNP: " + recordOffenceOutcomeView.getCurrentRLOffenceOutcomeView().isIssueWarningNP());
		 * System.err.println("WFP: " +
		 * recordOffenceOutcomeView.getCurrentRLOffenceOutcomeView().isWarnForProsecution());
		 * System.err.println("Plate: " + recordOffenceOutcomeView.getCurrentRLOffenceOutcomeView().isRemovePlate()); */
		OffenceOutcomeView offenceOutcomeView = new OffenceOutcomeView();
		offenceOutcomeView.setValues(selectedoOffenceOutcomeView);
		recordOffenceOutcomeView.setCurrentRLOffenceOutcomeView(offenceOutcomeView);
		/* System.err.println("Summons: " + recordOffenceOutcomeView.getCurrentRLOffenceOutcomeView().isIssueSummons());
		 * System.err.println("Warning Notice: " +
		 * recordOffenceOutcomeView.getCurrentRLOffenceOutcomeView().isIssuewWarningNotice());
		 * System.err.println("WNP: " + recordOffenceOutcomeView.getCurrentRLOffenceOutcomeView().isIssueWarningNP());
		 * System.err.println("WFP: " +
		 * recordOffenceOutcomeView.getCurrentRLOffenceOutcomeView().isWarnForProsecution());
		 * System.err.println("Plate: " + recordOffenceOutcomeView.getCurrentRLOffenceOutcomeView().isRemovePlate()); */
		recordOffenceOutcomeView.setRlOpenRecordCount(recordOffenceOutcomeView.getRlOpenRecordCount() + 1);
		// System.err.println("initRowEditMV recordOffenceOutcomeView.getOpenRecordCount(): " +
		// recordOffenceOutcomeView.getMvOpenRecordCount());
	}

	public void initRowEditBdg(RowEditEvent event) {
		RequestContext context = RequestContextHolder.getRequestContext();
		RecordOffenceOutcomeView recordOffenceOutcomeView = (RecordOffenceOutcomeView) context.getFlowScope().get("recordOffenceOutcomeView");

		OffenceOutcomeView selectedoOffenceOutcomeView = (OffenceOutcomeView) event.getObject();

		OffenceOutcomeView offenceOutcomeView = new OffenceOutcomeView();
		offenceOutcomeView.setValues(selectedoOffenceOutcomeView);
		recordOffenceOutcomeView.setCurrentBdgOffenceOutcomeView(offenceOutcomeView);

		recordOffenceOutcomeView.setBdgOpenRecordCount(recordOffenceOutcomeView.getBdgOpenRecordCount() + 1);
		// System.err.println("initRowEditMV recordOffenceOutcomeView.getOpenRecordCount(): " +
		// recordOffenceOutcomeView.getMvOpenRecordCount());
	}

	public void initRowEditCH(RowEditEvent event) {
		RequestContext context = RequestContextHolder.getRequestContext();
		RecordOffenceOutcomeView recordOffenceOutcomeView = (RecordOffenceOutcomeView) context.getFlowScope().get("recordOffenceOutcomeView");

		OffenceOutcomeView selectedoOffenceOutcomeView = (OffenceOutcomeView) event.getObject();

		OffenceOutcomeView offenceOutcomeView = new OffenceOutcomeView();
		offenceOutcomeView.setValues(selectedoOffenceOutcomeView);
		recordOffenceOutcomeView.setCurrentCHOffenceOutcomeView(offenceOutcomeView);

		recordOffenceOutcomeView.setChOpenRecordCount(recordOffenceOutcomeView.getChOpenRecordCount() + 1);
		// System.err.println("initRowEditMV recordOffenceOutcomeView.getOpenRecordCount(): " +
		// recordOffenceOutcomeView.getMvOpenRecordCount());
	}

	public void initRowEditOT(RowEditEvent event) {
		RequestContext context = RequestContextHolder.getRequestContext();
		RecordOffenceOutcomeView recordOffenceOutcomeView = (RecordOffenceOutcomeView) context.getFlowScope().get("recordOffenceOutcomeView");

		OffenceOutcomeView selectedoOffenceOutcomeView = (OffenceOutcomeView) event.getObject();

		OffenceOutcomeView offenceOutcomeView = new OffenceOutcomeView();
		offenceOutcomeView.setValues(selectedoOffenceOutcomeView);
		recordOffenceOutcomeView.setCurrentOTOffenceOutcomeView(offenceOutcomeView);

		recordOffenceOutcomeView.setOtOpenRecordCount(recordOffenceOutcomeView.getOtOpenRecordCount() + 1);
		// System.err.println("initRowEditMV recordOffenceOutcomeView.getOpenRecordCount(): " +
		// recordOffenceOutcomeView.getMvOpenRecordCount());
	}

	private void setSelectedOffenceDescription(List<OffenceBO> offenceList, OffenceOutcomeView selectedoOffenceOutcomeView) {

		for (OffenceBO offence : offenceList) {
			if (offence.getOffenceId().equals(selectedoOffenceOutcomeView.getOffenceId())) {
				selectedoOffenceOutcomeView.setOffenceDescription(offence.getOffenceDescription());
				selectedoOffenceOutcomeView.setOffenceShortDescription(offence.getShortDescription());
				return;

			}
		}
	}

	public void editMVSelectedOffenceOutcome(RowEditEvent event) {
		RequestContext context = RequestContextHolder.getRequestContext();
		OffenceOutcomeView selectedoOffenceOutcomeView = (OffenceOutcomeView) event.getObject();
		RecordOffenceOutcomeView recordOffenceOutcomeView = (RecordOffenceOutcomeView) context.getFlowScope().get("recordOffenceOutcomeView");
		ArrayList<OffenceOutcomeView> offenceOutcomess = (ArrayList<OffenceOutcomeView>) context.getFlowScope().get("mvOffenceOutcomes");
		recordOffenceOutcomeView.setMvTableError(validateRow(selectedoOffenceOutcomeView, offenceOutcomess));

		if (recordOffenceOutcomeView.isMvTableError() == false) {
			// System.err.println("valid");
			recordOffenceOutcomeView.setMvOpenRecordCount(recordOffenceOutcomeView.getMvOpenRecordCount() - 1);
			// System.err.println("recordOffenceOutcomeView.getOpenRecordCount(): " +
			// recordOffenceOutcomeView.getOpenRecordCount());
			// recordOffenceOutcomeView.setDisableBadgeTab(false);
			// recordOffenceOutcomeView.setDisableTableBtn(false);
			if (selectedoOffenceOutcomeView.isFirstEdit() == true) {
				selectedoOffenceOutcomeView.setFirstEdit(false);
			}
			List<OffenceBO> mvOffenceList = (List<OffenceBO>) context.getFlowScope().get("mvOffenceList");
			setSelectedOffenceDescription(mvOffenceList, selectedoOffenceOutcomeView);
		}

	}

	public void editDLSelectedOffenceOutcome(RowEditEvent event) {
		RequestContext context = RequestContextHolder.getRequestContext();
		OffenceOutcomeView selectedoOffenceOutcomeView = (OffenceOutcomeView) event.getObject();
		RecordOffenceOutcomeView recordOffenceOutcomeView = (RecordOffenceOutcomeView) context.getFlowScope().get("recordOffenceOutcomeView");

		ArrayList<OffenceOutcomeView> offenceOutcomess = (ArrayList<OffenceOutcomeView>) context.getFlowScope().get("dlOffenceOutcomes");
		recordOffenceOutcomeView.setDlTableError(validateRow(selectedoOffenceOutcomeView, offenceOutcomess));

		if (recordOffenceOutcomeView.isDlTableError() == false) {
			recordOffenceOutcomeView.setDlOpenRecordCount(recordOffenceOutcomeView.getDlOpenRecordCount() - 1);

			if (selectedoOffenceOutcomeView.isFirstEdit() == true) {
				selectedoOffenceOutcomeView.setFirstEdit(false);
			}
			List<OffenceBO> mvOffenceList = (List<OffenceBO>) context.getFlowScope().get("dlOffenceList");
			setSelectedOffenceDescription(mvOffenceList, selectedoOffenceOutcomeView);
		}

	}

	public void editRLSelectedOffenceOutcome(RowEditEvent event) {
		RequestContext context = RequestContextHolder.getRequestContext();
		OffenceOutcomeView selectedoOffenceOutcomeView = (OffenceOutcomeView) event.getObject();

		RecordOffenceOutcomeView recordOffenceOutcomeView = (RecordOffenceOutcomeView) context.getFlowScope().get("recordOffenceOutcomeView");
		ArrayList<OffenceOutcomeView> offenceOutcomess = (ArrayList<OffenceOutcomeView>) context.getFlowScope().get("rlOffenceOutcomes");
		recordOffenceOutcomeView.setRlTableError(validateRow(selectedoOffenceOutcomeView, offenceOutcomess));

		if (recordOffenceOutcomeView.isRlTableError() == false) {
			recordOffenceOutcomeView.setRlOpenRecordCount(recordOffenceOutcomeView.getRlOpenRecordCount() - 1);

			if (selectedoOffenceOutcomeView.isFirstEdit() == true) {
				selectedoOffenceOutcomeView.setFirstEdit(false);
			}
			List<OffenceBO> mvOffenceList = (List<OffenceBO>) context.getFlowScope().get("rlOffenceList");
			setSelectedOffenceDescription(mvOffenceList, selectedoOffenceOutcomeView);
		}
	}

	public void editBdgSelectedOffenceOutcome(RowEditEvent event) {
		RequestContext context = RequestContextHolder.getRequestContext();
		OffenceOutcomeView selectedoOffenceOutcomeView = (OffenceOutcomeView) event.getObject();
		RecordOffenceOutcomeView recordOffenceOutcomeView = (RecordOffenceOutcomeView) context.getFlowScope().get("recordOffenceOutcomeView");
		ArrayList<OffenceOutcomeView> offenceOutcomess = (ArrayList<OffenceOutcomeView>) context.getFlowScope().get("bdgOffenceOutcomes");
		recordOffenceOutcomeView.setBdgTableError(validateRow(selectedoOffenceOutcomeView, offenceOutcomess));
		if (recordOffenceOutcomeView.isBdgTableError() == false) {
			recordOffenceOutcomeView.setBdgOpenRecordCount(recordOffenceOutcomeView.getBdgOpenRecordCount() - 1);

			if (selectedoOffenceOutcomeView.isFirstEdit() == true) {
				selectedoOffenceOutcomeView.setFirstEdit(false);
			}

			List<OffenceBO> mvOffenceList = (List<OffenceBO>) context.getFlowScope().get("bdgOffenceList");
			setSelectedOffenceDescription(mvOffenceList, selectedoOffenceOutcomeView);
		}
	}

	public void editOtSelectedOffenceOutcome(RowEditEvent event) {
		RequestContext context = RequestContextHolder.getRequestContext();
		OffenceOutcomeView selectedoOffenceOutcomeView = (OffenceOutcomeView) event.getObject();
		RecordOffenceOutcomeView recordOffenceOutcomeView = (RecordOffenceOutcomeView) context.getFlowScope().get("recordOffenceOutcomeView");
		ArrayList<OffenceOutcomeView> offenceOutcomess = (ArrayList<OffenceOutcomeView>) context.getFlowScope().get("otOffenceOutcomes");
		recordOffenceOutcomeView.setOtTableError(validateRow(selectedoOffenceOutcomeView, offenceOutcomess));

		if (recordOffenceOutcomeView.isOtTableError() == false) {
			recordOffenceOutcomeView.setOtOpenRecordCount(recordOffenceOutcomeView.getOtOpenRecordCount() - 1);

			if (selectedoOffenceOutcomeView.isFirstEdit() == true) {
				selectedoOffenceOutcomeView.setFirstEdit(false);
			}
			List<OffenceBO> mvOffenceList = (List<OffenceBO>) context.getFlowScope().get("otherOffenceList");
			setSelectedOffenceDescription(mvOffenceList, selectedoOffenceOutcomeView);
		}

	}

	public void editChSelectedOffenceOutcome(RowEditEvent event) {
		RequestContext context = RequestContextHolder.getRequestContext();
		OffenceOutcomeView selectedoOffenceOutcomeView = (OffenceOutcomeView) event.getObject();
		RecordOffenceOutcomeView recordOffenceOutcomeView = (RecordOffenceOutcomeView) context.getFlowScope().get("recordOffenceOutcomeView");
		ArrayList<OffenceOutcomeView> offenceOutcomess = (ArrayList<OffenceOutcomeView>) context.getFlowScope().get("chOffenceOutcomes");
		recordOffenceOutcomeView.setChTableError(validateRow(selectedoOffenceOutcomeView, offenceOutcomess));

		if (recordOffenceOutcomeView.isChTableError() == false) {
			recordOffenceOutcomeView.setChOpenRecordCount(recordOffenceOutcomeView.getChOpenRecordCount() - 1);

			if (selectedoOffenceOutcomeView.isFirstEdit() == true) {
				selectedoOffenceOutcomeView.setFirstEdit(false);
			}
			List<OffenceBO> mvOffenceList = (List<OffenceBO>) context.getFlowScope().get("chOffenceList");
			setSelectedOffenceDescription(mvOffenceList, selectedoOffenceOutcomeView);
		}

	}

	public void cancelMVEditSelectedOffenceOutcome(RowEditEvent event) {
		RequestContext context = RequestContextHolder.getRequestContext();
		OffenceOutcomeView selectedoOffenceOutcomeView = (OffenceOutcomeView) event.getObject();

		ArrayList<OffenceOutcomeView> offenceOutcomess = (ArrayList<OffenceOutcomeView>) context.getFlowScope().get("mvOffenceOutcomes");
		RecordOffenceOutcomeView recordOffenceOutcomeView = (RecordOffenceOutcomeView) context.getFlowScope().get("recordOffenceOutcomeView");

		if (selectedoOffenceOutcomeView.isFirstEdit() == true) {
			// System.err.println("first edit");
			offenceOutcomess.remove(0);
			// recordOffenceOutcomeView.setMvOpenRecordCount(recordOffenceOutcomeView.getMvOpenRecordCount()-1);
		}
		/* else{ recordOffenceOutcomeView.setMvTableError(validateRow(selectedoOffenceOutcomeView, offenceOutcomess));
		 * System.err.println("not first: error: " + recordOffenceOutcomeView.isMvTableError());
		 * if(recordOffenceOutcomeView.isMvTableError() == false){
		 * recordOffenceOutcomeView.setMvOpenRecordCount(recordOffenceOutcomeView.getMvOpenRecordCount()-1); } } */
		recordOffenceOutcomeView.setMvOpenRecordCount(recordOffenceOutcomeView.getMvOpenRecordCount() - 1);
		selectedoOffenceOutcomeView.setValues(recordOffenceOutcomeView.getCurrentMVOffenceOutcomeView());
	}

	public void cancelDLEditSelectedOffenceOutcome(RowEditEvent event) {
		RequestContext context = RequestContextHolder.getRequestContext();
		OffenceOutcomeView selectedoOffenceOutcomeView = (OffenceOutcomeView) event.getObject();
		RecordOffenceOutcomeView recordOffenceOutcomeView = (RecordOffenceOutcomeView) context.getFlowScope().get("recordOffenceOutcomeView");
		ArrayList<OffenceOutcomeView> offenceOutcomess = (ArrayList<OffenceOutcomeView>) context.getFlowScope().get("dlOffenceOutcomes");
		// recordOffenceOutcomeView.setDlTableError(validateRow(selectedoOffenceOutcomeView, offenceOutcomess));

		if (selectedoOffenceOutcomeView.isFirstEdit() == true) {
			offenceOutcomess.remove(0);
		}
		recordOffenceOutcomeView.setDlOpenRecordCount(recordOffenceOutcomeView.getDlOpenRecordCount() - 1);
		selectedoOffenceOutcomeView.setValues(recordOffenceOutcomeView.getCurrentDLOffenceOutcomeView());
	}

	public void cancelRLEditSelectedOffenceOutcome(RowEditEvent event) {
		RequestContext context = RequestContextHolder.getRequestContext();
		OffenceOutcomeView selectedoOffenceOutcomeView = (OffenceOutcomeView) event.getObject();
		RecordOffenceOutcomeView recordOffenceOutcomeView = (RecordOffenceOutcomeView) context.getFlowScope().get("recordOffenceOutcomeView");
		ArrayList<OffenceOutcomeView> offenceOutcomess = (ArrayList<OffenceOutcomeView>) context.getFlowScope().get("rlOffenceOutcomes");
		// recordOffenceOutcomeView.setRlTableError(validateRow(selectedoOffenceOutcomeView, offenceOutcomess));

		if (selectedoOffenceOutcomeView.isFirstEdit() == true) {
			offenceOutcomess.remove(0);
		} else {
			/* System.err.println("Cancel");event.getBehavior().toString(); System.err.println("Summons: " +
			 * recordOffenceOutcomeView.getCurrentRLOffenceOutcomeView().isIssueSummons());
			 * System.err.println("Warning Notice: " +
			 * recordOffenceOutcomeView.getCurrentRLOffenceOutcomeView().isIssuewWarningNotice());
			 * System.err.println("WNP: " +
			 * recordOffenceOutcomeView.getCurrentRLOffenceOutcomeView().isIssueWarningNP()); System.err.println("WFP: "
			 * + recordOffenceOutcomeView.getCurrentRLOffenceOutcomeView().isWarnForProsecution());
			 * System.err.println("Plate: " +
			 * recordOffenceOutcomeView.getCurrentRLOffenceOutcomeView().isRemovePlate()); */
			selectedoOffenceOutcomeView.setValues(recordOffenceOutcomeView.getCurrentRLOffenceOutcomeView());

		}
		recordOffenceOutcomeView.setRlOpenRecordCount(recordOffenceOutcomeView.getRlOpenRecordCount() - 1);

	}

	public void cancelBdgEditSelectedOffenceOutcome(RowEditEvent event) {
		RequestContext context = RequestContextHolder.getRequestContext();
		OffenceOutcomeView selectedoOffenceOutcomeView = (OffenceOutcomeView) event.getObject();
		RecordOffenceOutcomeView recordOffenceOutcomeView = (RecordOffenceOutcomeView) context.getFlowScope().get("recordOffenceOutcomeView");
		ArrayList<OffenceOutcomeView> offenceOutcomess = (ArrayList<OffenceOutcomeView>) context.getFlowScope().get("bdgOffenceOutcomes");
		// recordOffenceOutcomeView.setBdgTableError(validateRow(selectedoOffenceOutcomeView, offenceOutcomess));

		if (selectedoOffenceOutcomeView.isFirstEdit() == true) {
			offenceOutcomess.remove(0);
		}
		recordOffenceOutcomeView.setBdgOpenRecordCount(recordOffenceOutcomeView.getBdgOpenRecordCount() - 1);
		selectedoOffenceOutcomeView.setValues(recordOffenceOutcomeView.getCurrentBdgOffenceOutcomeView());

	}

	public void cancelOtherEditSelectedOffenceOutcome(RowEditEvent event) {
		RequestContext context = RequestContextHolder.getRequestContext();
		OffenceOutcomeView selectedoOffenceOutcomeView = (OffenceOutcomeView) event.getObject();
		RecordOffenceOutcomeView recordOffenceOutcomeView = (RecordOffenceOutcomeView) context.getFlowScope().get("recordOffenceOutcomeView");
		ArrayList<OffenceOutcomeView> offenceOutcomess = (ArrayList<OffenceOutcomeView>) context.getFlowScope().get("otOffenceOutcomes");
		// recordOffenceOutcomeView.setOtTableError(validateRow(selectedoOffenceOutcomeView, offenceOutcomess));

		if (selectedoOffenceOutcomeView.isFirstEdit() == true) {
			offenceOutcomess.remove(0);
		}
		recordOffenceOutcomeView.setOtOpenRecordCount(recordOffenceOutcomeView.getOtOpenRecordCount() - 1);
		selectedoOffenceOutcomeView.setValues(recordOffenceOutcomeView.getCurrentOTOffenceOutcomeView());

	}

	public void cancelChEditSelectedOffenceOutcome(RowEditEvent event) {
		RequestContext context = RequestContextHolder.getRequestContext();
		OffenceOutcomeView selectedoOffenceOutcomeView = (OffenceOutcomeView) event.getObject();
		RecordOffenceOutcomeView recordOffenceOutcomeView = (RecordOffenceOutcomeView) context.getFlowScope().get("recordOffenceOutcomeView");
		ArrayList<OffenceOutcomeView> offenceOutcomess = (ArrayList<OffenceOutcomeView>) context.getFlowScope().get("chOffenceOutcomes");
		// recordOffenceOutcomeView.setChTableError(validateRow(selectedoOffenceOutcomeView, offenceOutcomess));

		if (selectedoOffenceOutcomeView.isFirstEdit() == true) {
			offenceOutcomess.remove(0);
		}
		recordOffenceOutcomeView.setChOpenRecordCount(recordOffenceOutcomeView.getChOpenRecordCount() - 1);
		selectedoOffenceOutcomeView.setValues(recordOffenceOutcomeView.getCurrentCHOffenceOutcomeView());

	}

	public ArrayList<OffenceOutcomeView> selectAllOffenceOutcomes(ArrayList<OffenceOutcomeView> offenceOutcomes, boolean selectAll) {

		if (offenceOutcomes != null) {
			for (OffenceOutcomeView offOut : offenceOutcomes) {
				offOut.setRowSelected(selectAll);
			}
		}

		return offenceOutcomes;
	}

	public void selectAllMVOffenceOutcomes(RequestContext context) {

		ArrayList<OffenceOutcomeView> offenceOutcomes = (ArrayList<OffenceOutcomeView>) context.getFlowScope().get("mvOffenceOutcomes");

		RecordOffenceOutcomeView recordOffenceOutcomeView = (RecordOffenceOutcomeView) context.getFlowScope().get("recordOffenceOutcomeView");

		if (recordOffenceOutcomeView.isMvSelectAllOffenceOutcome()) {
			recordOffenceOutcomeView.setMvOffenceOutcomeSelected(true);
		} else {
			recordOffenceOutcomeView.setMvOffenceOutcomeSelected(false);
		}

		selectAllOffenceOutcomes(offenceOutcomes, recordOffenceOutcomeView.isMvSelectAllOffenceOutcome());
		/* if(offenceOutcomes!=null){ for(OffenceOutcomeView offOut : offenceOutcomes){
		 * offOut.setRowSelected(recordOffenceOutcomeView.isMvSelectAllOffenceOutcome()); } } */

		context.getFlowScope().put("mvOffenceOutcomes", offenceOutcomes);

	}

	public void selectAllDLOffenceOutcomes(RequestContext context) {

		ArrayList<OffenceOutcomeView> offenceOutcomes = (ArrayList<OffenceOutcomeView>) context.getFlowScope().get("dlOffenceOutcomes");

		RecordOffenceOutcomeView recordOffenceOutcomeView = (RecordOffenceOutcomeView) context.getFlowScope().get("recordOffenceOutcomeView");

		if (recordOffenceOutcomeView.isDlSelectAllOffenceOutcome()) {
			recordOffenceOutcomeView.setDlOffenceOutcomeSelected(true);
		} else {
			recordOffenceOutcomeView.setDlOffenceOutcomeSelected(false);
		}

		selectAllOffenceOutcomes(offenceOutcomes, recordOffenceOutcomeView.isDlSelectAllOffenceOutcome());

		// context.getFlowScope().put("mvOffenceList", offenceOutcomes);

	}

	public void selectAllRLOffenceOutcomes(RequestContext context) {

		ArrayList<OffenceOutcomeView> offenceOutcomes = (ArrayList<OffenceOutcomeView>) context.getFlowScope().get("rlOffenceOutcomes");

		RecordOffenceOutcomeView recordOffenceOutcomeView = (RecordOffenceOutcomeView) context.getFlowScope().get("recordOffenceOutcomeView");

		if (recordOffenceOutcomeView.isRlSelectAllOffenceOutcome()) {
			recordOffenceOutcomeView.setRlOffenceOutcomeSelected(true);
		} else {
			recordOffenceOutcomeView.setRlOffenceOutcomeSelected(false);
		}
		selectAllOffenceOutcomes(offenceOutcomes, recordOffenceOutcomeView.isRlSelectAllOffenceOutcome());

		// context.getFlowScope().put("mvOffenceList", offenceOutcomes);

	}

	public void selectAllBdgOffenceOutcomes(RequestContext context) {

		ArrayList<OffenceOutcomeView> offenceOutcomes = (ArrayList<OffenceOutcomeView>) context.getFlowScope().get("bdgOffenceOutcomes");

		RecordOffenceOutcomeView recordOffenceOutcomeView = (RecordOffenceOutcomeView) context.getFlowScope().get("recordOffenceOutcomeView");

		if (recordOffenceOutcomeView.isBdgSelectAllOffenceOutcome()) {
			recordOffenceOutcomeView.setBdgOffenceOutcomeSelected(true);
		} else {
			recordOffenceOutcomeView.setBdgOffenceOutcomeSelected(false);
		}
		selectAllOffenceOutcomes(offenceOutcomes, recordOffenceOutcomeView.isBdgSelectAllOffenceOutcome());

		// context.getFlowScope().put("mvOffenceList", offenceOutcomes);

	}

	public void selectAllOtherOffenceOutcomes(RequestContext context) {

		ArrayList<OffenceOutcomeView> offenceOutcomes = (ArrayList<OffenceOutcomeView>) context.getFlowScope().get("otOffenceOutcomes");

		RecordOffenceOutcomeView recordOffenceOutcomeView = (RecordOffenceOutcomeView) context.getFlowScope().get("recordOffenceOutcomeView");

		if (recordOffenceOutcomeView.isOtSelectAllOffenceOutcome()) {
			recordOffenceOutcomeView.setOtOffenceOutcomeSelected(true);
		} else {
			recordOffenceOutcomeView.setOtOffenceOutcomeSelected(false);
		}
		selectAllOffenceOutcomes(offenceOutcomes, recordOffenceOutcomeView.isOtSelectAllOffenceOutcome());

		// context.getFlowScope().put("mvOffenceList", offenceOutcomes);

	}

	public void selectAllChOffenceOutcomes(RequestContext context) {

		ArrayList<OffenceOutcomeView> offenceOutcomes = (ArrayList<OffenceOutcomeView>) context.getFlowScope().get("chOffenceOutcomes");

		RecordOffenceOutcomeView recordOffenceOutcomeView = (RecordOffenceOutcomeView) context.getFlowScope().get("recordOffenceOutcomeView");

		if (recordOffenceOutcomeView.isChSelectAllOffenceOutcome()) {
			recordOffenceOutcomeView.setChOffenceOutcomeSelected(true);
		} else {
			recordOffenceOutcomeView.setChOffenceOutcomeSelected(false);
		}
		selectAllOffenceOutcomes(offenceOutcomes, recordOffenceOutcomeView.isChSelectAllOffenceOutcome());

		// context.getFlowScope().put("mvOffenceList", offenceOutcomes);

	}

	public void selectChOffenceOutcomes(RequestContext context) {

		ArrayList<OffenceOutcomeView> offenceOutcomes = (ArrayList<OffenceOutcomeView>) context.getFlowScope().get("chOffenceOutcomes");

		RecordOffenceOutcomeView recordOffenceOutcomeView = (RecordOffenceOutcomeView) context.getFlowScope().get("recordOffenceOutcomeView");
		int rowSelectCount = rowsSelectedCount(offenceOutcomes);

		if (rowSelectCount > 0) {
			recordOffenceOutcomeView.setChOffenceOutcomeSelected(true);
		} else {
			recordOffenceOutcomeView.setChOffenceOutcomeSelected(false);
		}

	}

	private int rowsSelectedCount(ArrayList<OffenceOutcomeView> offenceOutcomes) {
		int rowSelectCount = 0;
		for (OffenceOutcomeView offOutcome : offenceOutcomes) {
			if (offOutcome.isRowSelected()) {
				rowSelectCount++;
			}
		}

		return rowSelectCount;
	}

	public void selectOtOffenceOutcomes(RequestContext context) {

		ArrayList<OffenceOutcomeView> offenceOutcomes = (ArrayList<OffenceOutcomeView>) context.getFlowScope().get("otOffenceOutcomes");

		RecordOffenceOutcomeView recordOffenceOutcomeView = (RecordOffenceOutcomeView) context.getFlowScope().get("recordOffenceOutcomeView");

		int rowSelectCount = rowsSelectedCount(offenceOutcomes);
		if (rowSelectCount > 0) {
			recordOffenceOutcomeView.setOtOffenceOutcomeSelected(true);
		} else {
			recordOffenceOutcomeView.setOtOffenceOutcomeSelected(false);
		}

	}

	public void selectDlOffenceOutcomes(RequestContext context) {

		ArrayList<OffenceOutcomeView> offenceOutcomes = (ArrayList<OffenceOutcomeView>) context.getFlowScope().get("dlOffenceOutcomes");

		RecordOffenceOutcomeView recordOffenceOutcomeView = (RecordOffenceOutcomeView) context.getFlowScope().get("recordOffenceOutcomeView");

		int rowSelectCount = rowsSelectedCount(offenceOutcomes);
		if (rowSelectCount > 0) {
			recordOffenceOutcomeView.setDlOffenceOutcomeSelected(true);
		} else {
			recordOffenceOutcomeView.setDlOffenceOutcomeSelected(false);
		}

	}

	public void selectRlOffenceOutcomes(RequestContext context) {
		ArrayList<OffenceOutcomeView> offenceOutcomes = (ArrayList<OffenceOutcomeView>) context.getFlowScope().get("rlOffenceOutcomes");

		RecordOffenceOutcomeView recordOffenceOutcomeView = (RecordOffenceOutcomeView) context.getFlowScope().get("recordOffenceOutcomeView");

		int rowSelectCount = rowsSelectedCount(offenceOutcomes);
		if (rowSelectCount > 0) {
			recordOffenceOutcomeView.setRlOffenceOutcomeSelected(true);
		} else {
			recordOffenceOutcomeView.setRlOffenceOutcomeSelected(false);
		}

	}

	public void selectBdgOffenceOutcomes(RequestContext context) {

		ArrayList<OffenceOutcomeView> offenceOutcomes = (ArrayList<OffenceOutcomeView>) context.getFlowScope().get("bdgOffenceOutcomes");

		RecordOffenceOutcomeView recordOffenceOutcomeView = (RecordOffenceOutcomeView) context.getFlowScope().get("recordOffenceOutcomeView");

		int rowSelectCount = rowsSelectedCount(offenceOutcomes);
		if (rowSelectCount > 0) {
			recordOffenceOutcomeView.setBdgOffenceOutcomeSelected(true);
		} else {
			recordOffenceOutcomeView.setBdgOffenceOutcomeSelected(false);
		}

	}

	public void selectMvOffenceOutcomes(RequestContext context) {
		ArrayList<OffenceOutcomeView> offenceOutcomes = (ArrayList<OffenceOutcomeView>) context.getFlowScope().get("mvOffenceOutcomes");

		RecordOffenceOutcomeView recordOffenceOutcomeView = (RecordOffenceOutcomeView) context.getFlowScope().get("recordOffenceOutcomeView");

		int rowSelectCount = rowsSelectedCount(offenceOutcomes);
		if (rowSelectCount > 0) {
			recordOffenceOutcomeView.setMvOffenceOutcomeSelected(true);
		} else {
			recordOffenceOutcomeView.setMvOffenceOutcomeSelected(false);
		}

	}

	public void selectWarningNotice(RequestContext context) {

		ArrayList<Integer> selectedOffOuutes = (ArrayList<Integer>) context.getFlowScope().get("offenceOutcomesSelected");

		ArrayList<OffenceOutcomeView> offOuutes = (ArrayList<OffenceOutcomeView>) context.getFlowScope().get("offenceOutcomes");

		logger.info("selectedOffOuutes: " + selectedOffOuutes.size());

		if (offOuutes != null && selectedOffOuutes != null) {
			for (OffenceOutcomeView offOut : offOuutes) {
				if (selectedOffOuutes.contains(offOut.getOffenceId())) {
					logger.info("offOut.getOffenceId(): " + offOut.getOffenceId());
					logger.info("offOut.isIssuewWarningNotice(): " + offOut.isIssuewWarningNotice());
					
					if (offOut.isIssuewWarningNotice()) {
						offOut.setIssuewWarningNotice(true);
						offOut.setIssueSummons(true);
					}
				}

			}
		}

		context.getFlowScope().put("offenceOutcomes", offOuutes);

		selectedOffOuutes.removeAll(selectedOffOuutes);// to be removed added because row closes

		context.getFlowScope().put("offenceOutcomesSelected", selectedOffOuutes);// to be removed
	}

	public Event addFirstWitness(RequestContext context) {
		SupportingDetailsView supportingDetailsView = (SupportingDetailsView) context.getFlowScope().get("supportingDetailsView");
		supportingDetailsView.setCurrentWitness(new WitnessView());
		supportingDetailsView.setShowWitnessDialog(true);
		supportingDetailsView.setUpdateWitness(false);
		return success();
	}

	public Event cancelWitness(RequestContext context) {
		SupportingDetailsView supportingDetailsView = (SupportingDetailsView) context.getFlowScope().get("supportingDetailsView");
		supportingDetailsView.setShowWitnessDialog(false);
		supportingDetailsView.setCurrentWitness(new WitnessView());
		supportingDetailsView.setUpdateWitness(false);
		return success();
	}

	private boolean validateWitnessDetails(WitnessView witnessView, RequestContext context) {
		boolean error = false;
		String errMsg = "";
		if (StringUtils.isBlank(witnessView.getLastName()) || StringUtils.isBlank(witnessView.getFirstName())) {
			addErrorMessageWithParameter(context, "RequiredFields", "Full Name");
			error = true;
			// errMsg="Full Name is required.\n";
		}

		//global address validation - kpowell
		boolean errorFoundInAddress  = validateAddress(context, witnessView.getAddressView());
        
        if(errorFoundInAddress){
              error = true;
        }
		
		// Validate Phone numbers
		if (!StringUtils.isBlank(witnessView.getMobilePhoneNo()) && !witnessView.getMobilePhoneNo().equals("(___)___-____")) {
			if (!PhoneNumberUtil.validateAllPhoneNumbers(witnessView.getMobilePhoneNo())) {
				addErrorMessageText(context, "Mobile Contact Number is not valid. E.g.(555)555-5555");
				error = true;
			}
		}
		if (!StringUtils.isBlank(witnessView.getHomePhoneNo()) && !witnessView.getHomePhoneNo().equals("(___)___-____")) {
			if (!PhoneNumberUtil.validateAllPhoneNumbers(witnessView.getHomePhoneNo())) {
				addErrorMessageText(context, "Home Contact Number is not valid. E.g.(555)555-5555");
				error = true;
			}
		}
		if (!StringUtils.isBlank(witnessView.getWorkPhoneNo()) && !witnessView.getWorkPhoneNo().equals("(___)___-____")) {
			if (!PhoneNumberUtil.validateAllPhoneNumbers(witnessView.getWorkPhoneNo())) {
				addErrorMessageText(context, "Work Contact Number is not valid. E.g.(555)555-5555");
				error = true;
			}
		}

		// return error;
		return error;
	}

	public void selectAllWitness(RequestContext context) {

		SupportingDetailsView supportingDetailsView = (SupportingDetailsView) context.getFlowScope().get("supportingDetailsView");

		if (supportingDetailsView.getWitnessList() != null && supportingDetailsView.getWitnessList().size() > 0) {
			for (WitnessView witness : supportingDetailsView.getWitnessList()) {
				witness.setRowSelected(supportingDetailsView.isSelectAllWitness());
			}
		}
	}

	public Event selectWitnessRow(RequestContext context) {
		SupportingDetailsView supportingDetailsView = (SupportingDetailsView) context.getFlowScope().get("supportingDetailsView");
		supportingDetailsView.setCurrentWitness(supportingDetailsView.getSelectedWitness());
		supportingDetailsView.setShowWitnessDialog(true);
		supportingDetailsView.setUpdateWitness(true);
		return success();
	}

	public String withnessFullName(WitnessView witnessView) {
		NameUtil util = new NameUtil();
		return util.getLastNameCapsFirstNameMiddleName(witnessView.getFirstName(), witnessView.getLastName(), witnessView.getMiddleName());
	}

	public String withnessPhoneNumber(WitnessView witnessView) {
		String phoneNumbers = "";

		if (StringUtils.isBlank(witnessView.getMobilePhoneNo()) && StringUtils.isBlank(witnessView.getWorkPhoneNo()) && StringUtils.isBlank(witnessView.getHomePhoneNo())) {
			return "Not provided";
		}

		if (StringUtils.isNotBlank(witnessView.getMobilePhoneNo())) {
			phoneNumbers = phoneNumbers + "(M) " + witnessView.getMobilePhoneNo() + "; ";
		}

		if (StringUtils.isNotBlank(witnessView.getHomePhoneNo())) {
			phoneNumbers = phoneNumbers + "(H) " + witnessView.getHomePhoneNo() + "; ";
		}

		if (StringUtils.isNotBlank(witnessView.getWorkPhoneNo())) {
			phoneNumbers = phoneNumbers + "(W) " + witnessView.getWorkPhoneNo() + ";";
		}

		return phoneNumbers;
	}

	public Event deleteWitness(RequestContext context) {

		logger.info("In Delete Wit Function");
		try {
			SupportingDetailsView supportingDetailsView = (SupportingDetailsView) context.getFlowScope().get("supportingDetailsView");

			Iterator<WitnessView> iterator = supportingDetailsView.getWitnessList().iterator();
			if (supportingDetailsView.getWitnessList() != null) {
				while (iterator.hasNext()) {
					WitnessView witnessView = iterator.next();
					if (witnessView.isRowSelected()) {
						iterator.remove();
					}
				}

			}
			supportingDetailsView.setSelectAllWitness(false);
			return success();
		} catch (Exception e) {
			e.printStackTrace();
			;
			return error();
		}
	}

	private String getParishDescriptionSelected(String parishCode) {
		RequestContext context = RequestContextHolder.getRequestContext();

		List<ParishBO> parishList = (List<ParishBO>) context.getFlowScope().get("parishList");

		for (ParishBO parish : parishList) {
			if (parish.getParishCode().equalsIgnoreCase(parishCode)) {
				return parish.getDescription();
			}
		}

		return "";
	}

	public Event saveWitness(RequestContext context) {
		SupportingDetailsView supportingDetailsView = (SupportingDetailsView) context.getFlowScope().get("supportingDetailsView");

		boolean error = validateWitnessDetails(supportingDetailsView.getCurrentWitness(), context);

		if (error == false) {
			supportingDetailsView.getCurrentWitness().setParishDesc(getParishDescriptionSelected(supportingDetailsView.getCurrentWitness().getAddressView().getParish()));
			supportingDetailsView.setShowWitnessDialog(false);
			if (supportingDetailsView.isUpdateWitness() == false) {
				supportingDetailsView.getWitnessList().add(supportingDetailsView.getCurrentWitness());
			}

			supportingDetailsView.setUpdateWitness(false);

			supportingDetailsView.getCurrentWitness().getAddressView().setAddressLine1(getAddressLine1(supportingDetailsView.getCurrentWitness().getAddressView().getStreetName(), supportingDetailsView.getCurrentWitness().getAddressView().getMarkText(), supportingDetailsView.getCurrentWitness().getAddressView().getStreetNo()));
			supportingDetailsView.getCurrentWitness().getAddressView().setAddressLine2(getAddressLine2(supportingDetailsView.getCurrentWitness().getAddressView().getPoBoxNo(), supportingDetailsView.getCurrentWitness().getAddressView().getPoLocationName(), supportingDetailsView.getCurrentWitness().getParishDesc()));
			// supportingDetailsView.getCurrentWitness().getAddressView().setAddressLine2(getAddressLine2(supportingDetailsView.getCurrentWitness().getAddressView().getPoBoxNo(),
			// supportingDetailsView.getCurrentWitness().getAddressView().getPoLocationName(),
			// supportingDetailsView.getCurrentWitness().getAddressView().getParish()));
		} else {
			supportingDetailsView.setShowWitnessDialog(true);
			return error();
		}

		return success();
	}

	private List<Integer> addToOffenceList(List<Integer> offenceList, List<Integer> additionalList) {
		for (Integer addOff : additionalList) {
			offenceList.add(addOff);
		}

		return offenceList;
	}

	@SuppressWarnings("unchecked")
	private boolean validateRoadCheckIndicateActions(RequestContext context) {
		boolean error = false;
		RecordOffenceOutcomeView recordOffenceOutcomeView = (RecordOffenceOutcomeView) context.getFlowScope().get("recordOffenceOutcomeView");
		RoadCheckInitiateView roadCheckInitiateView = (RoadCheckInitiateView) context.getFlowScope().get("initiateView");

		if (roadCheckInitiateView.isDlQuery()) {
			if (recordOffenceOutcomeView.getDlActionToBeTaken() == null || recordOffenceOutcomeView.getDlActionToBeTaken().equalsIgnoreCase("")) {
				addErrorMessageWithParameter(context, "IndicateAction", "Driver's Licence - ");
				error = true;
			} else if (recordOffenceOutcomeView.getDlActionToBeTaken().equalsIgnoreCase("R")) {
				ArrayList<OffenceOutcomeView> dlOffenceOutcomes = (ArrayList<OffenceOutcomeView>) context.getFlowScope().get("dlOffenceOutcomes");
				logger.info("dlOffenceOutcomes.size()" + dlOffenceOutcomes.size());
				if (dlOffenceOutcomes.size() <= 0) {
					addErrorMessageWithParameter(context, "OffencesAndOutcomesRequired", "Driver's Licence - ");
					error = true;
				} else if (dlOffenceOutcomes.size() == 1 && dlOffenceOutcomes.get(0).getOffenceId().intValue() == Constants.DEFAULT_OFFENCE.intValue()) {
					addErrorMessageWithParameter(context, "OffencesAndOutcomesRequired", "Driver's Licence - ");
					error = true;
				}

			}
		}

		if (recordOffenceOutcomeView.getMvActionToBeTaken() == null || recordOffenceOutcomeView.getMvActionToBeTaken().equalsIgnoreCase("")) {
			addErrorMessageWithParameter(context, "IndicateAction", "Motor Vehicle - ");
			error = true;
		} else if (recordOffenceOutcomeView.getMvActionToBeTaken().equalsIgnoreCase("R")) {
			ArrayList<OffenceOutcomeView> mvOffenceOutcomes = (ArrayList<OffenceOutcomeView>) context.getFlowScope().get("mvOffenceOutcomes");

			if (mvOffenceOutcomes.size() <= 0) {
				addErrorMessageWithParameter(context, "OffencesAndOutcomesRequired", "Motor Vehicle - ");
				error = true;
			} else if (mvOffenceOutcomes.size() == 1 && mvOffenceOutcomes.get(0).getOffenceId().intValue() == Constants.DEFAULT_OFFENCE.intValue()) {
				addErrorMessageWithParameter(context, "OffencesAndOutcomesRequired", "Motor Vehicle - ");
				error = true;
			}

		}

		if (roadCheckInitiateView.isRoadLicQuery()) {
			if (recordOffenceOutcomeView.getRlActionToBeTaken() == null || recordOffenceOutcomeView.getRlActionToBeTaken().equalsIgnoreCase("")) {
				addErrorMessageWithParameter(context, "IndicateAction", "Road Licence - ");
				error = true;
			} else if (recordOffenceOutcomeView.getRlActionToBeTaken().equalsIgnoreCase("R")) {
				ArrayList<OffenceOutcomeView> rlOffenceOutcomes = (ArrayList<OffenceOutcomeView>) context.getFlowScope().get("rlOffenceOutcomes");

				if (rlOffenceOutcomes.size() <= 0) {
					addErrorMessageWithParameter(context, "OffencesAndOutcomesRequired", "Road Licence - ");
					error = true;
				} else if (rlOffenceOutcomes.size() == 1 && rlOffenceOutcomes.get(0).getOffenceId().intValue() == Constants.DEFAULT_OFFENCE.intValue()) {
					addErrorMessageWithParameter(context, "OffencesAndOutcomesRequired", "Road Licence - ");
					error = true;
				}

			}
		}

		if (roadCheckInitiateView.isBadgeQuery()) {
			if (recordOffenceOutcomeView.getBdgActionToBeTaken() == null || recordOffenceOutcomeView.getBdgActionToBeTaken().equalsIgnoreCase("")) {
				addErrorMessageWithParameter(context, "IndicateAction", "Badge - ");
				error = true;
			} else if (recordOffenceOutcomeView.getBdgActionToBeTaken().equalsIgnoreCase("R")) {
				ArrayList<OffenceOutcomeView> bdgOffenceOutcomes = (ArrayList<OffenceOutcomeView>) context.getFlowScope().get("bdgOffenceOutcomes");

				if (bdgOffenceOutcomes.size() <= 0) {
					addErrorMessageWithParameter(context, "OffencesAndOutcomesRequired", "Badge - ");
					error = true;
				} else if (bdgOffenceOutcomes.size() == 1 && bdgOffenceOutcomes.get(0).getOffenceId().intValue() == Constants.DEFAULT_OFFENCE.intValue()) {
					addErrorMessageWithParameter(context, "OffencesAndOutcomesRequired", "Badge - ");
					error = true;
				}

			}
		}

		if (roadCheckInitiateView.isCitationHistQuery()) {
			if (recordOffenceOutcomeView.getChActionToBeTaken() == null || recordOffenceOutcomeView.getChActionToBeTaken().equalsIgnoreCase("")) {
				addErrorMessageWithParameter(context, "IndicateAction", "Citation History - ");
				error = true;
			} else if (recordOffenceOutcomeView.getChActionToBeTaken().equalsIgnoreCase("R")) {
				ArrayList<OffenceOutcomeView> chOffenceOutcomes = (ArrayList<OffenceOutcomeView>) context.getFlowScope().get("chOffenceOutcomes");

				if (chOffenceOutcomes.size() <= 0) {
					addErrorMessageWithParameter(context, "OffencesAndOutcomesRequired", "Citation History - ");
					error = true;
				} else if (chOffenceOutcomes.size() == 1 && chOffenceOutcomes.get(0).getOffenceId().intValue() == Constants.DEFAULT_OFFENCE.intValue()) {
					addErrorMessageWithParameter(context, "OffencesAndOutcomesRequired", "Citation History - ");
					error = true;
				}

			}
		}

		return error;
	}

	public Event completeAndProceedToReview(RequestContext context) {

		RecordOffenceOutcomeView recordOffenceOutcomeView = (RecordOffenceOutcomeView) context.getFlowScope().get("recordOffenceOutcomeView");
		try {

			logger.info("inside completeAndProceedToReview()");
			RoadCheckInitiateView roadCheckInitiateView = (RoadCheckInitiateView) context.getFlowScope().get("initiateView");
			recordOffenceOutcomeView.setRecordOffOutcomeErr(false);
			boolean error = false;

			error = validateRoadCheckIndicateActions(context);
			if (error == true) {
				recordOffenceOutcomeView.setRecordOffOutcomeErr(true);
				// System.err.println("Error occured"+ recordOffenceOutcomeView.isRecordOffOutcomeErr());
				return error();
			}
			roadCheckInitiateView.setCompleteRecordOffOutcome(false);
			roadCheckInitiateView.setCompleteSupportingDetails(true);

			context.getFlowScope().put("supportingDetailsExists", false);			
			
			outcomeActionToTake(context,recordOffenceOutcomeView);
			
			return success();
		} catch (Exception e) {
			// TODO: handle exception
			// System.err.println("Error Caught");
			recordOffenceOutcomeView.setRecordOffOutcomeErr(true);
			e.printStackTrace();
			addErrorMessage(RequestContextHolder.getRequestContext(), "SystemError");
			return error();
		}
	}
	
	public void clearSuportingDetails(RequestContext context){
		// put in supporting details view
		RoadCheckInitiateView roadCheckInitiateView = (RoadCheckInitiateView) context.getFlowScope().get("initiateView");
		roadCheckInitiateView.setCompleteRecordOffOutcome(false);
		roadCheckInitiateView.setCompleteSupportingDetails(false);
		roadCheckInitiateView.setCompleteReviewSummary(false);
		
		context.getFlowScope().put("supportingDetailsExists", false);	
		context.getFlowScope().put("supportingDetailsView", new SupportingDetailsView());
		context.getFlowScope().put("roadCheckReviewSummaryBean", new RoadCheckReviewSummaryBean());
	}
	

	@SuppressWarnings("unchecked")
	public Event proceedToSupportingDetails(RequestContext context) {

		RecordOffenceOutcomeView recordOffenceOutcomeView = (RecordOffenceOutcomeView) context.getFlowScope().get("recordOffenceOutcomeView");
		
		ArrayList<OffenceOutcomeView> mvOffenceOutcomes = (ArrayList<OffenceOutcomeView>) context.getFlowScope().get("mvOffenceOutcomes");

		ArrayList<OffenceOutcomeView> dlOffenceOutcomes = (ArrayList<OffenceOutcomeView>) context.getFlowScope().get("dlOffenceOutcomes");

		ArrayList<OffenceOutcomeView> rlOffenceOutcomes = (ArrayList<OffenceOutcomeView>) context.getFlowScope().get("rlOffenceOutcomes");

		ArrayList<OffenceOutcomeView> bdgOffenceOutcomes = (ArrayList<OffenceOutcomeView>) context.getFlowScope().get("bdgOffenceOutcomes");

		ArrayList<OffenceOutcomeView> chOffenceOutcomes = (ArrayList<OffenceOutcomeView>) context.getFlowScope().get("chOffenceOutcomes");

		ArrayList<OffenceOutcomeView> otherOffenceOutcomes = (ArrayList<OffenceOutcomeView>) context.getFlowScope().get("otOffenceOutcomes");
		
		RoadCheckInitiateView roadCheckInitiateView = (RoadCheckInitiateView) context.getFlowScope().get("initiateView");
		
		//removed this condition to address an issue noted on July 23, 2015 where user could proceed to add supporting details
		//even if no outcome is selected, and the vehicle info different box is checked.
//		if(mvOffenceOutcomes.size() < 1 && dlOffenceOutcomes.size() < 1 && rlOffenceOutcomes.size() < 1 && bdgOffenceOutcomes.size() < 1 
//				&& chOffenceOutcomes.size() < 1 && otherOffenceOutcomes.size() < 1 && roadCheckInitiateView.isVehicleInfoDifferent()){
//			return success();
//			
//		}
		recordOffenceOutcomeView.setRecordOffOutcomeErr(false);
		boolean error = false;

		error = validateRoadCheckIndicateActions(context);

		/* if(error == true){ recordOffenceOutcomeView.setRecordOffOutcomeErr(true); return error(); } */

		
		SupportingDetailsView supportingDetailsView = (SupportingDetailsView) context.getFlowScope().get("supportingDetailsView");



		// RecordOffenceOutcomeView recordOffenceOutcomeView = (RecordOffenceOutcomeView)context.getFlowScope().get(
		// "recordOffenceOutcomeView");

		List<Integer> offenceIntegerList = new ArrayList<Integer>();
		List<Integer> mvOffenceIntegerList = new ArrayList<Integer>();
		List<Integer> rlOffenceIntegerList = new ArrayList<Integer>();
		List<Integer> dlOffenceIntegerList = new ArrayList<Integer>();
		List<Integer> bdgOffenceIntegerList = new ArrayList<Integer>();
		List<Integer> chOffenceIntegerList = new ArrayList<Integer>();
		List<Integer> otOffenceIntegerList = new ArrayList<Integer>();
		int wnSelected = 0;
		int warningNPSelected = 0;
		int summonsSelected = 0;

		int totalWNSelected = 0;
		int totalWarningNPSelected = 0;
		int totalSummonsSelected = 0;

		if (mvOffenceOutcomes != null && mvOffenceOutcomes.size() > 0) {
			wnSelected = wariningNoticeSelected(mvOffenceOutcomes);
			warningNPSelected = wariningNoticeNPSelected(mvOffenceOutcomes);
			summonsSelected = summonsSelected(mvOffenceOutcomes);
			mvOffenceIntegerList = returnIntegerOffenceList(mvOffenceOutcomes);
			// offenceIntegerList.addAll(mvOffenceIntegerList);
			offenceIntegerList = addToOffenceList(offenceIntegerList, mvOffenceIntegerList);
			// System.err.println("mv size: " + offenceIntegerList.size());

			// System.err.println("MV wnSelected: " + wnSelected);
			// System.err.println("totalWNSelected: " + totalWNSelected);
			totalWNSelected = totalWNSelected + wnSelected;
			totalWarningNPSelected = totalWarningNPSelected + warningNPSelected;
			totalSummonsSelected = totalSummonsSelected + summonsSelected;
		}

		if (dlOffenceOutcomes != null && dlOffenceOutcomes.size() > 0) {

			wnSelected = wariningNoticeSelected(dlOffenceOutcomes);
			warningNPSelected = wariningNoticeNPSelected(dlOffenceOutcomes);
			summonsSelected = summonsSelected(dlOffenceOutcomes);

			dlOffenceIntegerList = returnIntegerOffenceList(dlOffenceOutcomes);
			// offenceIntegerList.addAll(dlOffenceIntegerList);
			offenceIntegerList = addToOffenceList(offenceIntegerList, dlOffenceIntegerList);
			// System.err.println(" DL wnSelected: " + wnSelected);
			// System.err.println("totalWNSelected: " + totalWNSelected);
			totalWNSelected = totalWNSelected + wnSelected;
			totalWarningNPSelected = totalWarningNPSelected + warningNPSelected;
			totalSummonsSelected = totalSummonsSelected + summonsSelected;
		}

		if (rlOffenceOutcomes != null && rlOffenceOutcomes.size() > 0) {

			wnSelected = wariningNoticeSelected(rlOffenceOutcomes);
			warningNPSelected = wariningNoticeNPSelected(rlOffenceOutcomes);
			summonsSelected = summonsSelected(rlOffenceOutcomes);

			rlOffenceIntegerList = returnIntegerOffenceList(rlOffenceOutcomes);
			// offenceIntegerList.addAll(rlOffenceIntegerList);
			offenceIntegerList = addToOffenceList(offenceIntegerList, rlOffenceIntegerList);
			// System.err.println("RL wnSelected: " + wnSelected);
			// System.err.println("totalWNSelected: " + totalWNSelected);
			totalWNSelected = totalWNSelected + wnSelected;
			totalWarningNPSelected = totalWarningNPSelected + warningNPSelected;
			totalSummonsSelected = totalSummonsSelected + summonsSelected;
		}

		if (bdgOffenceOutcomes != null && bdgOffenceOutcomes.size() > 0) {

			wnSelected = wariningNoticeSelected(bdgOffenceOutcomes);
			warningNPSelected = wariningNoticeNPSelected(bdgOffenceOutcomes);
			summonsSelected = summonsSelected(bdgOffenceOutcomes);

			bdgOffenceIntegerList = returnIntegerOffenceList(bdgOffenceOutcomes);
			// offenceIntegerList.addAll(bdgOffenceIntegerList);
			offenceIntegerList = addToOffenceList(offenceIntegerList, bdgOffenceIntegerList);
			// System.err.println("BDG wnSelected: " + wnSelected);
			// System.err.println("totalWNSelected: " + totalWNSelected);
			totalWNSelected = totalWNSelected + wnSelected;
			totalWarningNPSelected = totalWarningNPSelected + warningNPSelected;
			totalSummonsSelected = totalSummonsSelected + summonsSelected;
		}

		if (chOffenceOutcomes != null && chOffenceOutcomes.size() > 0) {

			wnSelected = wariningNoticeSelected(chOffenceOutcomes);
			warningNPSelected = wariningNoticeNPSelected(chOffenceOutcomes);
			summonsSelected = summonsSelected(chOffenceOutcomes);

			chOffenceIntegerList = returnIntegerOffenceList(chOffenceOutcomes);
			// offenceIntegerList.addAll(chOffenceIntegerList);
			offenceIntegerList = addToOffenceList(offenceIntegerList, chOffenceIntegerList);
			// System.err.println("CH wnSelected: " + wnSelected);
			// System.err.println("totalWNSelected: " + totalWNSelected);
			totalWNSelected = totalWNSelected + wnSelected;
			totalWarningNPSelected = totalWarningNPSelected + warningNPSelected;
			totalSummonsSelected = totalSummonsSelected + summonsSelected;
		}
		if (otherOffenceOutcomes != null && otherOffenceOutcomes.size() > 0) {

			wnSelected = wariningNoticeSelected(otherOffenceOutcomes);
			warningNPSelected = wariningNoticeNPSelected(otherOffenceOutcomes);
			summonsSelected = summonsSelected(otherOffenceOutcomes);

			otOffenceIntegerList = returnIntegerOffenceList(otherOffenceOutcomes);
			// offenceIntegerList.addAll(otOffenceIntegerList);
			offenceIntegerList = addToOffenceList(offenceIntegerList, otOffenceIntegerList);
			// System.err.println("OT wnSelected: " + wnSelected);
			// System.err.println("totalWNSelected: " + totalWNSelected);
			totalWNSelected = totalWNSelected + wnSelected;
			totalWarningNPSelected = totalWarningNPSelected + warningNPSelected;
			totalSummonsSelected = totalSummonsSelected + summonsSelected;
		}

		// get offence parameter
		try {
			// System.err.println("totalWNSelected: " + totalWNSelected);
			// System.err.println("totalWarningNPSelected: " + totalWarningNPSelected);
			if (totalWNSelected > 10) {
				addErrorMessageWithParameter(context, "NoMoreThanTenOffence", "Issue Warning Notice");
				error = true;
			}

			if (totalWarningNPSelected > 10) {
				addErrorMessageWithParameter(context, "NoMoreThanTenOffence", "Warning No Prosecution");
				error = true;
			}

			supportingDetailsView.setDirectiveParamRequired(false);
			supportingDetailsView.setInspectorParamRequired(false);
			supportingDetailsView.setNoOfPassengersRequired(false);
			supportingDetailsView.setIssueToOwner(false);
			List<ExcerptParamMappingBO> paramMappingBOList = getRoadCompliancyService().getRequiredExcerptParams(offenceIntegerList);
			context.getFlowScope().put("offenceParamList", paramMappingBOList);
			// List<Integer> paramIntegerList = new ArrayList<Integer>();
			List<String> paramStringList = new ArrayList<String>();
			if (paramMappingBOList != null) {
				// paramIntegerList = returnIntegerParamList(paramMappingBOList);
				paramStringList = returnStringParamList(paramMappingBOList);
				if (paramStringList.contains(Constants.OffenceParam.DIRECTIVE)) {
					supportingDetailsView.setDirectiveParamRequired(true);
				}
				if (paramStringList.contains(Constants.OffenceParam.INSPECTOR)) {
					supportingDetailsView.setInspectorParamRequired(true);
				}
				if (paramStringList.contains(Constants.OffenceParam.NO_OF_PASSENGERS)) {
					supportingDetailsView.setNoOfPassengersRequired(true);
				}

				if (paramStringList.contains(Constants.OffenceParam.LICENCE_TYPE) || paramStringList.contains(Constants.OffenceParam.ROUTE) || paramStringList.contains(Constants.OffenceParam.SEATING_CAPACITY) || paramStringList.contains(Constants.OffenceParam.ROUTE_END) || paramStringList.contains(Constants.OffenceParam.ROUTE_START)) {

					if (roadCheckInitiateView.isRlQueryDone() == false || (roadCheckInitiateView.isRlQueryDone() && roadCheckInitiateView.isNoRlFound()) ) {
						addErrorMessageText(context, "No Road Licence found - Please remove all selected offence(s) that require(s) Road Licence information");
						recordOffenceOutcomeView.setRecordOffOutcomeErr(true);
						error = true;
					}

				}
			}
		} catch (RequiredFieldMissingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		if (totalWNSelected > 0) {
			recordOffenceOutcomeView.setWarningNoticeSelected(true);
		} else {
			recordOffenceOutcomeView.setWarningNoticeSelected(false);
		}

		if (totalSummonsSelected > 0) {
			recordOffenceOutcomeView.setSummonsSelected(true);
			
			//check if mvOffenceOutcomes has an issueToOwner Offence
			if(checkIssueToOwner(mvOffenceOutcomes)){
				supportingDetailsView.setIssueToOwner(true);
			}
			else{
				//check if rlOffenceOutcomes has an issueToOwner Offence
				if(checkIssueToOwner(rlOffenceOutcomes)){
					supportingDetailsView.setIssueToOwner(true);
				}
			}
			
		} else {
			recordOffenceOutcomeView.setSummonsSelected(false);
		}

		supportingDetailsView.setAllegationRequired(false);
		if (totalWNSelected > 0 || totalWarningNPSelected > 0) {
			supportingDetailsView.setAllegationRequired(true);
		}

		supportingDetailsView.setRegionalOperation(false);
		if (!roadCheckInitiateView.isCompleteRecordOffOutcome()) {
			supportingDetailsView.setCourtId(null);
			supportingDetailsView.setCourtDateTime(null);
		}
		if (roadCheckInitiateView.getRoadOperationBO() != null && roadCheckInitiateView.getRoadOperationBO().getRoadOperationId() != null && roadCheckInitiateView.getRoadOperationBO().getRoadOperationId() > 0) {
			if (roadCheckInitiateView.getRoadOperationBO().getCategoryId().equalsIgnoreCase(Constants.Category.REGIONAL)) {
				supportingDetailsView.setRegionalOperation(true);

				supportingDetailsView.setCourtDateTime(roadCheckInitiateView.getRoadOperationBO().getCourtDTime());
				if (roadCheckInitiateView.getRoadOperationBO().getCourtBO() != null) {
					supportingDetailsView.setCourtId(roadCheckInitiateView.getRoadOperationBO().getCourtBO().getCourtId());
				}

			} else {
				if (!roadCheckInitiateView.isCompleteRecordOffOutcome()) {
					supportingDetailsView.setCourtId(null);
					supportingDetailsView.setCourtDateTime(null);
				}

			}
		}

		// get parish of Offence

		ArteryCriteriaBO arteryCriteriaBO = new ArteryCriteriaBO();
		// arteryCriteriaBO.setArteryId(roadCheckInitiateView.getOffencePlaceId());
		try {
			// List<ArteryBO> selectedArtery =
			// getMaintenancePortProxy().lookupArtery(arteryCriteriaBO);

			if (roadCheckInitiateView.getOffencePlace() != null && roadCheckInitiateView.getOffencePlace().getLocationId() != null) {
				LocationCriteriaBO locationCriteriaBO = new LocationCriteriaBO();
				locationCriteriaBO.setLocationId(roadCheckInitiateView.getOffencePlace().getLocationId());
				List<LocationBO> locationBOs = getMaintenanceService().lookupLocation(locationCriteriaBO);

				if (locationBOs != null && locationBOs.size() == 1) {
					roadCheckInitiateView.setOffenceParish(locationBOs.get(0).getParishCode());
					
					//TODO Load list of parishes related to the
					
				}

			}

		} catch (NoRecordFoundException e) {
			
			e.printStackTrace();
		} catch (RequiredFieldMissingException e) {
			
			e.printStackTrace();
		}

		if (error == true) {
			recordOffenceOutcomeView.setRecordOffOutcomeErr(true);
			return error();
		}
		
		if (roadCheckInitiateView.getActivityType().equalsIgnoreCase("U")) {
			
			List<ITAExaminerBO> itaExaminerBOList = new ArrayList<ITAExaminerBO>();
			List<RefCodeBO> policeRefcodes = new ArrayList<RefCodeBO>();
			
			ITAExaminerCriteriaBO itaCriteria = new ITAExaminerCriteriaBO();
			RefCodeCriteriaBO codes = new RefCodeCriteriaBO();
			
			itaCriteria.setStatusId(Constants.Status.ACTIVE);
			codes.setTableName("roms_police_officer");
			codes.setStatus(Constants.Status.ACTIVE);
			
			ReferenceCode refCodeService = new ReferenceCode();
			
			try{
				itaExaminerBOList.addAll(getMaintenanceService().lookupITAExaminer(itaCriteria));
				policeRefcodes.addAll(refCodeService.getReferenceCode(codes));
			}
			catch(Exception e){
				logger.error("Road Check",e);
			}
			
			context.getFlowScope().put("itaListAll", itaExaminerBOList);
			context.getFlowScope().put("policeListAll", policeRefcodes);
			
		}

		context.getFlowScope().put("supportingDetailsView", supportingDetailsView);
		context.getFlowScope().put("recordOffenceOutcomeView", recordOffenceOutcomeView);
		roadCheckInitiateView.setCompleteRecordOffOutcome(true);
		return success();
	}

	/**
	 * This function gets a list of parishes based on the parishCode parameter 
	 * @param ParishCode
	 * @return
	 */
	private List<ParishBO> getListOfParishesForOfficeRegionOfParish(String parishCode){
		
		if(parishCode == null){
			return null;
		}
		
		ParishCriteriaBO parishCriteria = new ParishCriteriaBO();
		parishCriteria.setParishCode(parishCode);
		
		try{
			
			List<ParishBO> parishList = this.getMaintenanceService().lookupParish(parishCriteria);
			
			if(parishList == null || parishList.size() < 1){
				return null;
			}
			else{
				String currentOfficeLocationOfParish = parishList.get(0).getOfficeLocationCode();
				parishCriteria.setParishCode(null);
				parishCriteria.getOfficeLocationCode().add(currentOfficeLocationOfParish);
				return this.getMaintenanceService().lookupParish(parishCriteria);
			}
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	private HashMap<String, String> returnOffenceParamMap(List<ExcerptParamMappingBO> offenceParams, RequestContext context) {
		ExcerptParamMappingBO offenceParamMap = new ExcerptParamMappingBO();
		RoadCheckInitiateView roadCheckInitiateView = (RoadCheckInitiateView) context.getFlowScope().get("initiateView");
		SupportingDetailsView supportingDetailsView = (SupportingDetailsView) context.getFlowScope().get("supportingDetailsView");
		RoadLicCheckResultBO roadLicCheckResultBO = (RoadLicCheckResultBO) context.getFlowScope().get("rlResult");
		VehicleCheckResultBO vehicleCheckResultBO = (VehicleCheckResultBO) context.getFlowScope().get("mvResult");
		NameUtil nameUtil = new NameUtil();

		//CompliancyCheckBO.ExcerptParams.Entry entry = (new CompliancyCheckBO.ExcerptParams.Entry());
		//HashMap<String, String> excerptParam = null;
		HashMap<String,String> entry = new HashMap<String,String>();
		String keyName = "";
		String value = "";
		for (ExcerptParamMappingBO key : offenceParams) {
			//entry = (new CompliancyCheckBO.ExcerptParams.Entry());
			keyName = key.getParamName();
			// replace the parameters with the data coming over
			if (key.getParamName().equalsIgnoreCase(Constants.OffenceParam.ARTERY)) {
				if (StringUtils.isNotBlank(roadCheckInitiateView.getOffencePlace().getShortDescription()))
					value = roadCheckInitiateView.getOffencePlace().getShortDescription();
				else
					value="<span style=\"color:white\">__________________</span>";
				// offenceParamMap.put(key.getParamName(),
				// roadCheckInitiateView.getOffencePlace().getArteryDescription());
				// offenceExcerpt.replaceAll("[" + key + "]", document.getOffenceLocation());

			}
			if (key.getParamName().equalsIgnoreCase(Constants.OffenceParam.DIRECTIVE)) {
				if (StringUtils.isNotBlank(supportingDetailsView.getDirectiveParam()))
					value=supportingDetailsView.getDirectiveParam();
				else
					value="<span style=\"color:white\">__________________</span>";
				// offenceParamMap.put(key.getParamName(), supportingDetailsView.getDirectiveParam());

			}
			if (key.getParamName().equalsIgnoreCase(Constants.OffenceParam.DRIVER_NAME)) {
				if (StringUtils.isNotBlank(roadCheckInitiateView.getLastName()))
					value=nameUtil.getFirstNameLastName(roadCheckInitiateView.getFirstName(), roadCheckInitiateView.getLastName());
				else
					value="<span style=\"color:white\">__________________</span>";
				// offenceParamMap.put(key.getParamName(),
				// nameUtil.getLastNameCapsFirstNameMiddleName(roadCheckInitiateView.getFirstName(),
				// roadCheckInitiateView.getLastName(), roadCheckInitiateView.getMiddleName()));

			}
			if (key.getParamName().equalsIgnoreCase(Constants.OffenceParam.INSPECTOR)) {
				if (StringUtils.isNotBlank(supportingDetailsView.getInspectorParam().getLastName()))
					value=nameUtil.getFirstNameLastName(supportingDetailsView.getInspectorParam().getFirstName(), supportingDetailsView.getInspectorParam().getLastName());
				else
					value="<span style=\"color:white\">__________________</span>";
				// offenceParamMap.put(key.getParamName(), supportingDetailsView.getInspectorParam().getFullName());
			}

			if (key.getParamName().equalsIgnoreCase(Constants.OffenceParam.LICENCE_TYPE)) {
				if (StringUtils.isNotBlank(roadLicCheckResultBO.getLicenceType()))
					value=roadLicCheckResultBO.getLicenceType();
				else
					value="<span style=\"color:white\">__________________</span>";
				// offenceParamMap.put(key.getParamName(), supportingDetailsView.getDirectiveParam());

			}
			if (key.getParamName().equalsIgnoreCase(Constants.OffenceParam.NO_OF_PASSENGERS)) {
				if (supportingDetailsView.getNoOfPassengers() != null)
					value=supportingDetailsView.getNoOfPassengers() + "";
				else
					value="<span style=\"color:white\">__________________</span>";
				// offenceParamMap.put(key.getParamName(), supportingDetailsView.getNoOfPassengers()+"");
			}

			if (key.getParamName().equalsIgnoreCase(Constants.OffenceParam.PARISH_OF_OFFENCE)) {
				if (StringUtils.isNotBlank(roadCheckInitiateView.getOffenceParish()))
					value=getParishDescriptionSelected(roadCheckInitiateView.getOffenceParish());
				else
					value="<span style=\"color:white\">__________________</span>";
				// offenceParamMap.put(key.getParamName(), roadCheckInitiateView.getOffenceParish());

			}
			if (key.getParamName().equalsIgnoreCase(Constants.OffenceParam.PARISH_OF_OFFENDER)) {
				if (StringUtils.isNotBlank(roadCheckInitiateView.getAddressView().getParish()))
					value=getParishDescriptionSelected(roadCheckInitiateView.getAddressView().getParish());
				else
					value="<span style=\"color:white\">__________________</span>";
				// offenceParamMap.put(key.getParamName(), roadLicCheckResultBO.get);

			}
			if (key.getParamName().equalsIgnoreCase(Constants.OffenceParam.PLATE_NUMBER)) {
				if (StringUtils.isNotBlank(roadCheckInitiateView.getPlateRegNo()))
					value=roadCheckInitiateView.getPlateRegNo().toUpperCase();
				else
					value="<span style=\"color:white\">__________________</span>";
				// offenceParamMap.put(key.getParamName(), roadCheckInitiateView.getPlateRegNo());

			}
			if (key.getParamName().equalsIgnoreCase(Constants.OffenceParam.ROUTE)) {
				if (StringUtils.isNotBlank(roadLicCheckResultBO.getLicenceRoute()))
					value=roadLicCheckResultBO.getLicenceRoute();
				else
					value="<span style=\"color:white\">__________________</span>";
				// offenceParamMap.put(key.getParamName(), roadLicCheckResultBO.getLicenceRoute());

			}
			if (key.getParamName().equalsIgnoreCase(Constants.OffenceParam.ROUTE_END)) {
				if (StringUtils.isNotBlank(roadLicCheckResultBO.getRouteEnd()))
					value=roadLicCheckResultBO.getRouteEnd();
				else
					value="<span style=\"color:white\">__________________</span>";
				// offenceExcerpt.replaceAll("[" + key + "]", document.getRoadLicence().get);

			}
			if (key.getParamName().equalsIgnoreCase(Constants.OffenceParam.ROUTE_START)) {
				if (StringUtils.isNotBlank(roadLicCheckResultBO.getRouteStart()))
					value=roadLicCheckResultBO.getRouteStart();
				else
					value="<span style=\"color:white\">__________________</span>";
				// offenceExcerpt.replaceAll("[" + key + "]", document.get);

			}

			if (key.getParamName().equalsIgnoreCase(Constants.OffenceParam.SEATING_CAPACITY)) {
				if (roadLicCheckResultBO.getSeatCapacity() != null)
					value=roadLicCheckResultBO.getSeatCapacity() + "";
				else
					value="<span style=\"color:white\">__________________</span>";
				// offenceParamMap.put(key.getParamName(), vehicleCheckResultBO.get);

			}
			if (key.getParamName().equalsIgnoreCase(Constants.OffenceParam.VEHICLE_TYPE)) {
				if (StringUtils.isNotBlank(vehicleCheckResultBO != null ? vehicleCheckResultBO.getTypeDesc() : roadCheckInitiateView.getType()))
				{
					if(vehicleCheckResultBO != null)
						value=vehicleCheckResultBO.getTypeDesc() ;
					else
						value=roadCheckInitiateView.getType() ;
						
				}
				else
					value="<span style=\"color:white\">__________________</span>";
				// offenceParamMap.put(key.getParamName(), vehicleCheckResultBO.getTypeDesc());
			}

			//excerptParam.put(entry);
		}

		return entry;
	}

	private int wariningNoticeSelected(ArrayList<OffenceOutcomeView> offenceOutcomes) {
		boolean selected = false;
		int count = 0;
		// System.err.println("offenceOutcomes size: " + offenceOutcomes.size());
		for (OffenceOutcomeView offOut : offenceOutcomes) {
			// System.err.println("offOut.isIssuewWarningNotice(): " + offOut.isIssuewWarningNotice());
			if (offOut.isIssuewWarningNotice()) {
				// System.err.println("isIssuewWarningNotice()");
				selected = true;
				count++;
			}
		}
		return count;
	}

	private int summonsSelected(ArrayList<OffenceOutcomeView> offenceOutcomes) {
		boolean selected = false;
		int count = 0;
		for (OffenceOutcomeView offOut : offenceOutcomes) {
			if (offOut.isIssueSummons()) {
				selected = true;
				count++;
			}
			if (offOut.isWarnForProsecution()) {
				selected = true;
				count++;
			}
		}
		return count;
	}
	
	//check if list of offence has a summons to be sent to the owner of the vehicle (offence #35 or #36)
	private boolean checkIssueToOwner(ArrayList<OffenceOutcomeView> offenceOutcomes){
		boolean issueToOwner = false;
		for (OffenceOutcomeView offOut : offenceOutcomes) {
			if(offOut.offenceId.intValue() == Constants.OffenceAidAndAbbet.NO_ROAD_LIC.intValue() ||
					offOut.offenceId.intValue() == Constants.OffenceAidAndAbbet.NO_PPV_INSUR.intValue())
			{
				issueToOwner = true;
			}
		}
		return issueToOwner;
	}

	private int wariningNoticeNPSelected(ArrayList<OffenceOutcomeView> offenceOutcomes) {
		boolean selected = false;
		int count = 0;
		for (OffenceOutcomeView offOut : offenceOutcomes) {
			if (offOut.issueWarningNP) {
				selected = true;
				count++;
			}
		}
		return count;
	}

	public Event initiate(RequestContext context) {
		try {
			boolean error = false;
			RoadCheckVerifyIdView roadCheckVerifyIdView = (RoadCheckVerifyIdView) context.getFlowScope().get("verifyIdView");
			if (StringUtils.isBlank(roadCheckVerifyIdView.getFirstName())) {
				addErrorMessageWithParameter(context, "RequiredFields", "First Name");
				error = true;
			}

			if (StringUtils.isBlank(roadCheckVerifyIdView.getLastName())) {
				addErrorMessageWithParameter(context, "RequiredFields", "Last Name");
				error = true;
			}

			if (error == true) {
				return error();
			}

			roadCheckVerifyIdView.setShowDialog(false);
			RoadCheckInitiateView roadCheckInitiateView = setDetailsToInitiateRoadCheck(context);


			roadCheckInitiateView.setFirstName(WordUtils.capitalize(roadCheckVerifyIdView.getFirstName().toLowerCase(), Constants.STRING_DELIM_ARRAY));
			roadCheckInitiateView.setLastName(WordUtils.capitalize(roadCheckVerifyIdView.getLastName().toLowerCase(), Constants.STRING_DELIM_ARRAY));
			if (roadCheckVerifyIdView.getMiddleName() != null) {
				roadCheckInitiateView.setMiddleName(WordUtils.capitalize(roadCheckVerifyIdView.getMiddleName().toLowerCase(), Constants.STRING_DELIM_ARRAY));
			}

			context.getFlowScope().put("initiateView", roadCheckInitiateView);
			context.getFlowScope().put("verifyIdView", roadCheckVerifyIdView);

			return success();

		}

		catch (Exception e) {
			e.printStackTrace();
			return error();
			// TODO: handle exception
		}

	}

	public Event findActiveOperationForUser(RequestContext context) {
		RoadOperation roadOpService = new RoadOperation();
		roadOpService = getRoadOperationService();
		String username = getRomsLoggedInUser().getUsername();
		RoadOperationBO roadOp = new RoadOperationBO();

		logger.info("inside findActiveOperationForUser()");
		if (isHandHeldNoFlow()) {// only necessary when on a mobile device
			RoadCheckVerifyIdView roadCheckVerifyIdView = (RoadCheckVerifyIdView) context.getFlowScope().get("verifyIdView");
			try {
				roadOp = roadOpService.findActiveOperationForUser(username);
				// set the road operation details
				// RoadOperationBO roadOp =
				// daoFactory.getRoadOperationDAO().findActiveOperationForUser(userDO.getUsername());

				if (roadOp != null) {
					logger.info("Active Operation found");
					roadCheckVerifyIdView.setCurrentRoadOperationName(roadOp.getOperationName());
					roadCheckVerifyIdView.setCurrentRoadOperationId(roadOp.getRoadOperationId());

					if (roadOp.getScheduledStartDate() != null)
						roadCheckVerifyIdView.setCurrentRoadOperationStartDateNTime(roadOp.getScheduledStartDate());

					if (roadOp.getScheduledEndDate() != null)
						roadCheckVerifyIdView.setCurrentRoadOperationEndDateNTime(roadOp.getScheduledEndDate());

					/*If the user is logged on and their assigend operation changes or there was not an assigend operation*/
					updateRomsLoggedInUser(roadOp);
					
					
					context.getFlowScope().put("verifyIdView", roadCheckVerifyIdView);
				} else {
					// addmessage--NoActiveOperationForExistsForUser
					addErrorMessageWithParameter(context, "NoActiveOperationExistsForUser", username);

				}
			} catch (NoRecordFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				addErrorMessageWithParameter(context, "NoActiveOperationExistsForUser", username);

			} catch (RequiredFieldMissingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				addErrorMessage(RequestContextHolder.getRequestContext(), "SystemError");

			}

		}
		return success();

	}
	
	public Event findActiveOperationForUser() {
		RoadOperation roadOpService = new RoadOperation();
		roadOpService = getRoadOperationService();
		String username = getRomsLoggedInUser().getUsername();
		RoadOperationBO roadOp = new RoadOperationBO();

		
		if (isHandHeldNoFlow()) {// only necessary when on a mobile device
			
			try {
				roadOp = roadOpService.findActiveOperationForUser(username);
				// set the road operation details
				// RoadOperationBO roadOp =
				// daoFactory.getRoadOperationDAO().findActiveOperationForUser(userDO.getUsername());

				if (roadOp != null) {
	
					updateRomsLoggedInUser(roadOp);
		
					
					
					
				} 
			} catch (NoRecordFoundException e) {
		

			} catch (RequiredFieldMissingException e) {
			

			}

		}
		return success();

	}
	
	private void updateRomsLoggedInUser(RoadOperationBO roadOp){
		
		if(roadOp != null){
			/*If the user is logged on and their assigend operation changes or there was not an assigend operation*/
			if(this.getRomsLoggedInUser().getCurrentRoadOperationId() == null || 
					this.getRomsLoggedInUser().getCurrentRoadOperationId().intValue() != roadOp.getRoadOperationId().intValue()){
				this.getRomsLoggedInUser().setCurrentRoadOperationEndDateNTime(roadOp.getScheduledEndDtime());
				this.getRomsLoggedInUser().setCurrentRoadOperationId(roadOp.getRoadOperationId());
				this.getRomsLoggedInUser().setCurrentRoadOperationLocation(roadOp.getOfficeLocCode());
				this.getRomsLoggedInUser().setCurrentRoadOperationName(roadOp.getOperationName());
				this.getRomsLoggedInUser().setCurrentRoadOperationStartDateNTime(roadOp.getScheduledStartDtime());
			}
		}
	}

	public Event getParishList(RequestContext context) {
		// retrieve parishes
		List<RefCodeBO> listRefCode = super.getRefInfo("roms_parish");
		List<ParishBO> listParish = new ArrayList<ParishBO>();
		ParishBO parishBO = new ParishBO();

		if (listRefCode != null) {
			for (RefCodeBO parishRecord : listRefCode) {
				parishBO = new ParishBO();

				parishBO.setDescription(parishRecord.getDescription());
				parishBO.setParishCode(parishRecord.getCode());

				// System.err.println(parishRecord.getCode() + " - " + parishRecord.getDescription());
				listParish.add(parishBO);

			}
		}

		context.getFlowScope().put("parishList", listParish);

		return success();
	}

	public Event getCourtList(RequestContext context) {
		// retrieve parishes
		RoadCheckInitiateView roadCheckInitiateView = (RoadCheckInitiateView) context.getFlowScope().get("initiateView");
		CourtCriteriaBO courtCriteria = new CourtCriteriaBO();
		courtCriteria.setStatusId(Constants.Status.ACTIVE);
//		courtCriteria.setParishCode(roadCheckInitiateView.getOffenceParish());
//		HashMap<String, String> criteria = new HashMap<String, String>();
//		criteria.put("status_id", Constants.Status.ACTIVE);
//		criteria.put("parish_code", roadCheckInitiateView.getOffenceParish());
//		List<RefCodeBO> listRefCode = super.getRefInfo("roms_court", criteria);
		
		Set<String> selParCodes = new HashSet<String>();
		//if Kingston parish is in the list, add St. Andrew parish and vice versa
		try
		{
			if(roadCheckInitiateView.getOffenceParish().equals(Constants.ParishKingstonAndStAndrew.KINGSTON) || roadCheckInitiateView.getOffenceParish().equals(Constants.ParishKingstonAndStAndrew.ST_ANDREW)){
				selParCodes = Constants.MergeKingstonAndStAndrew();
			}else{
				selParCodes.add(roadCheckInitiateView.getOffenceParish());
			}
		}
		catch(Exception e)
		{
			logger.error("Road Check",e);
		}
		List<CourtBO> listCourt = new ArrayList<CourtBO>();
		
		for (String string : selParCodes) {
			courtCriteria.setParishCode(string);
			try {
				listCourt.addAll(getMaintenanceService().lookupCourt(courtCriteria));
			} catch (NoRecordFoundException e) {
				logger.error("Road Check",e);

			} catch (RequiredFieldMissingException e) {
				logger.error("Road Check",e);

			}
		}
//		CourtBO courtBO = new CourtBO();
//
//		if (listRefCode != null) {
//			for (RefCodeBO courtRecord : listRefCode) {
//				courtBO = new CourtBO();
//
//				courtBO.setDescription(courtRecord.getDescription());
//				courtBO.setShortDescription(courtRecord.getShortDescription());
//				courtBO.setCourtId(Integer.parseInt(courtRecord.getCode()));
//
//				// System.err.println(parishRecord.getCode() + " - " + parishRecord.getDescription());
//				listCourt.add(courtBO);
//
//			}
//		}
		

		context.getFlowScope().put("courtList", listCourt);

		return success();
	}

	@SuppressWarnings("unchecked")
	public Event getOperationList(RequestContext context) {

		List<RoadOperationBO> roadOps = new ArrayList<RoadOperationBO>();

		try {
				roadOps = getRoadOperationService().lookupAllRoadOperationForRoadCheck();
				//ExternalContextHolder.getExternalContext().getSessionMap().put(Constants.ROAD_OPERATION_LIST,roadOps);
				applicationRunTimeStorage.getValues().put(Constants.ROAD_OPERATION_LIST, roadOps);

		} catch (NoRecordFoundException e) {
			
			e.printStackTrace();
		}
		
		
		context.getFlowScope().put("roadOperationList", roadOps);

		return success();
	}
	
	/**
	 * This function set DDL and Badge Number to the TRN if the trn is set in the road check initiate object
	 * @param context
	 * @return
	 */
	public Event setDriversLicenceAndBadge(RequestContext context){
		
		RoadCheckInitiateView roadCheckInitiateView = (RoadCheckInitiateView) context.getFlowScope().get("initiateView");
		
		if(StringUtil.isSet(roadCheckInitiateView.getTrn())){
			if(StringUtils.trimToNull(roadCheckInitiateView.getDlNo())==null){			
				roadCheckInitiateView.setDlNo(roadCheckInitiateView.getTrn());
			}
			if(StringUtils.trimToNull(roadCheckInitiateView.getBadgeNo())==null){
				roadCheckInitiateView.setBadgeNo(roadCheckInitiateView.getTrn());
			}
		}
		
		return success();
	}

	public Event getOffencePlaceList(RequestContext context) {
		HashMap<String, String> criteria = new HashMap<String, String>();
		criteria.put("status_id", Constants.Status.ACTIVE);
		List<RefCodeBO> listRefCode = super.getRefInfo("roms_artery", criteria);
		List<ArteryBO> listArtery = new ArrayList<ArteryBO>();
		ArteryBO arteryBO = new ArteryBO();

		if (listRefCode != null) {
			for (RefCodeBO arteryRecord : listRefCode) {
				arteryBO = new ArteryBO();

				arteryBO.setArteryDescription(arteryRecord.getDescription());
				arteryBO.setArteryId(Integer.parseInt(arteryRecord.getCode()));

				// System.err.println(parishRecord.getCode() + " - " + parishRecord.getDescription());
				listArtery.add(arteryBO);

			}
		}

		context.getFlowScope().put("offencePlaceList", listArtery);

		return success();
	}

	@SuppressWarnings("unchecked")
	public Event getOffenceArteryList(RequestContext context) {
		RoadCheckInitiateView roadCheckInitiateView = (RoadCheckInitiateView) context.getFlowScope().get("initiateView");

		List<ArteryBO> searchArtryList = new ArrayList<ArteryBO>();
		try {
			if (roadCheckInitiateView.getRoadOperationBO() != null && roadCheckInitiateView.getRoadOperationBO().getRoadOperationId() != null && roadCheckInitiateView.getRoadOperationBO().getRoadOperationId() > 0) {
				
				//searchArtryList = (List<ArteryBO>) ExternalContextHolder.getExternalContext().getSessionMap().get(Constants.ALL_ARTERY_LIST + roadCheckInitiateView.getRoadOperationBO().getRoadOperationId());
				
				searchArtryList = (List<ArteryBO>) applicationRunTimeStorage.getValues().get(Constants.ALL_ARTERY_LIST + roadCheckInitiateView.getRoadOperationBO().getRoadOperationId());
				
				if(searchArtryList == null || searchArtryList.size() < 1){
					
					searchArtryList = getRoadOperationService().lookupAllOperationArtery(roadCheckInitiateView.getRoadOperationBO().getRoadOperationId());
					
					//ExternalContextHolder.getExternalContext().getSessionMap().put(Constants.ALL_ARTERY_LIST + roadCheckInitiateView.getRoadOperationBO().getRoadOperationId(),searchArtryList);
					
					applicationRunTimeStorage.getValues().put(Constants.ALL_ARTERY_LIST + roadCheckInitiateView.getRoadOperationBO().getRoadOperationId(),searchArtryList);
				}
				
			} else {
				
				//searchArtryList = (List<ArteryBO>) ExternalContextHolder.getExternalContext().getSessionMap().get(Constants.ALL_ARTERY_LIST);
				
				searchArtryList = (List<ArteryBO>)applicationRunTimeStorage.getValues().get(Constants.ALL_ARTERY_LIST);
				
				if(searchArtryList == null || searchArtryList.size() < 1){
					searchArtryList = getRoadOperationService().findArteryList();
					//ExternalContextHolder.getExternalContext().getSessionMap().put(Constants.ALL_ARTERY_LIST,searchArtryList);
					applicationRunTimeStorage.getValues().put(Constants.ALL_ARTERY_LIST,searchArtryList);
				}
				
			}

		} catch (NoRecordFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RequiredFieldMissingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		context.getFlowScope().put("offencePlaceList", searchArtryList);

		return success();
	}

	public void getListsForOperationMobile(){
		if(this.isHandHeldNoFlow()){
			this.getListsForOperation(true);
		}
	}
	/* public Event getListsForOperation(RequestContext context){ RoadCheckInitiateView roadCheckInitiateView =
	 * (RoadCheckInitiateView)context.getFlowScope().get("initiateView"); getTAStaffList(context);
	 * getOffenceArteryList(context); roadCheckInitiateView.setOffencePlaceId(null);
	 * roadCheckInitiateView.setTaStaffBO(new TAStaffBO()); return success(); } */

	public void getListsForOperation(Boolean fromViewState) {
		
		RequestContext context = RequestContextHolder.getRequestContext();		
		
		RoadCheckInitiateView roadCheckInitiateView = (RoadCheckInitiateView) context.getFlowScope().get("initiateView");

		if (roadCheckInitiateView.getActivityType().equalsIgnoreCase("U")) {
			roadCheckInitiateView.setRoadOperationBO(new RoadOperationBO());
			//this.addWarningMessage(context, "UnscheduledOperation");
		}
		getTAStaffList(context);
		getOffenceArteryList(context);
		
		//ROMS1.0-203
		getOperationList(context);

		if (roadCheckInitiateView.getRoadOperationBO() != null) {
			if (roadCheckInitiateView.getRoadOperationBO().getStatusId() != null) {
				if (roadCheckInitiateView.getRoadOperationBO().getStatusId().equalsIgnoreCase(Constants.Status.ROAD_OPERATION_TERMINATED)) {
					addInfoMessageText(context, roadCheckInitiateView.getRoadOperationBO().getOperationName() + " has been Terminated");
				}
			}

			
			if (roadCheckInitiateView.getRoadOperationBO().getCategoryId() != null) {
				if (roadCheckInitiateView.getRoadOperationBO().getCategoryId().equalsIgnoreCase(Constants.Category.REGIONAL) && !roadCheckInitiateView.isMobileDevice()) {
					
					if(fromViewState){
						Boolean viewStateSet = (Boolean) context.getFlowScope().get(this.ROAD_CHECK_INIT_VIEW_OPERATION_DATE_SET);
						
						if( (viewStateSet == null || ! viewStateSet) && (roadCheckInitiateView.getOffenceDate()  == null)){
							roadCheckInitiateView.setOffenceDate(roadCheckInitiateView.getRoadOperationBO().getScheduledStartDate());
							context.getFlowScope().put(this.ROAD_CHECK_INIT_VIEW_OPERATION_DATE_SET,true);
						}
						
					}
					else{
						roadCheckInitiateView.setOffenceDate(roadCheckInitiateView.getRoadOperationBO().getScheduledStartDate());
						context.getFlowScope().put(this.ROAD_CHECK_INIT_VIEW_OPERATION_DATE_SET,true);
					}
					
				}
			}
		}



	}

	public Event getTAStaffList(RequestContext context) {
		RoadCheckInitiateView roadCheckInitiateView = (RoadCheckInitiateView) context.getFlowScope().get("initiateView");

		List<TAStaffBO> searchListStaff = new ArrayList<TAStaffBO>();
		// List<TAStaffBO> listStaff = new
		// ArrayList<TAStaffBO>();
		TAStaffBO staffBO = new TAStaffBO();
		try {

			if (roadCheckInitiateView.getRoadOperationBO().getRoadOperationId() != null && roadCheckInitiateView.getRoadOperationBO().getRoadOperationId() > 0) {

				searchListStaff = getRoadOperationService().findAssignedStaffForRoadOperation(roadCheckInitiateView.getRoadOperationBO().getRoadOperationId());

				/* if(searchListStaff != null) { for( TAStaffBO staffRecord :
				 * searchListStaff) { staffBO = new TAStaffBO();
				 * staffBO.setFullName(staffRecord.getFullName()+ " [" + staffRecord.getStaffId() + "]");
				 * staffBO.setStaffId(staffRecord.getStaffId()); //System.err.println(parishRecord.getCode() + " - " +
				 * parishRecord.getDescription()); listStaff.add(staffBO); } } */
				List<TAStaffBO> searchListStaffTemp = new ArrayList<TAStaffBO>();
				searchListStaffTemp.addAll(searchListStaff);
				
				if(searchListStaff != null){
					for(TAStaffBO staffBo : searchListStaffTemp){
						if(staffBo.getAttended() != null)
						{
							if(!staffBo.getAttended()){
								searchListStaff.remove(staffBo);
							}
						}
					}
				}
			} else {
				searchListStaff = getRoadOperationService().getAllROMSStaff();
			}

		} catch (NoRecordFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RequiredFieldMissingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Collections.sort(searchListStaff, new Comparator<TAStaffBO>() {
			public int compare(TAStaffBO result1, TAStaffBO result2) {
				return result1.getFullName().compareTo(result2.getFullName());
			}
		});
		context.getFlowScope().put("staffList", searchListStaff);
		// context.getFlowScope().put("staffList", listStaff);

		return success();
	}

	@SuppressWarnings("unchecked")
	public List<RoadOperationBO> roadCheckRoadOpAutoComplete(String term) {

		RequestContext context = RequestContextHolder.getRequestContext();

		List<RoadOperationBO> roadOps = (List<RoadOperationBO>) context.getFlowScope().get("roadOperationList");

		List<RoadOperationBO> returnList = new ArrayList<RoadOperationBO>();

		for (RoadOperationBO operation : roadOps) {
			if (operation.getOperationName().trim().toLowerCase().contains(term.toLowerCase())) {
				returnList.add(operation);
			}
		}

		return returnList;
	}

	@SuppressWarnings("unchecked")
	public List<TAStaffBO> roadCheckTaStaffAutoComplete(String term) {

		RequestContext context = RequestContextHolder.getRequestContext();

		List<TAStaffBO> taStaffList = (List<TAStaffBO>) context.getFlowScope().get("staffList");

		List<TAStaffBO> returnList = new ArrayList<TAStaffBO>();

		for (TAStaffBO staff : taStaffList) {
			// if(staff.getFullName().trim().toLowerCase().contains(term.toLowerCase()))
			if (staff.getFullName().trim().toLowerCase().startsWith(term.toLowerCase())) {
				returnList.add(staff);
			}
		}

		return returnList;
	}

	@SuppressWarnings("unchecked")
	public List<ArteryBO> roadCheckArteryAutoComplete(String term) {

		RequestContext context = RequestContextHolder.getRequestContext();

		List<ArteryBO> arteryList = (List<ArteryBO>) context.getFlowScope().get("offencePlaceList");

		List<ArteryBO> returnList = new ArrayList<ArteryBO>();

		for (ArteryBO artery : arteryList) {
			if (artery.getShortDescription().trim().toLowerCase().contains(term.toLowerCase())) {
				returnList.add(artery);
			}
		}
		
		Collections.sort(returnList, new Comparator<ArteryBO>() {
			public int compare(ArteryBO result1, ArteryBO result2) {
				return result1.getShortDescription().toLowerCase().compareTo(result2.getShortDescription().toLowerCase());
			}
		});
		

		return returnList;
	}
	
	@SuppressWarnings("unchecked")
	public List<PersonBOForRoadCompConverter> roadCheckItaStaffAutoComplete(String term) {

		RequestContext context = RequestContextHolder.getRequestContext();

		List<ITAExaminerBO> taStaffList = (List<ITAExaminerBO>) context.getFlowScope().get("itaListAll");

		List<PersonBOForRoadCompConverter> returnList = new ArrayList<PersonBOForRoadCompConverter>();
		
		for(ITAExaminerBO staff : taStaffList)
		{
			if (staff.getPersonBO().getFullName().trim().toLowerCase().startsWith(term.toLowerCase())) {
			PersonBOForRoadCompConverter person = new PersonBOForRoadCompConverter();
			
				person.setFullName(staff.getPersonBO().getFullName() + " [" + staff.getExaminerId() + "]");
				person.setPersonId(staff.getPersonBO().getPersonId());

				returnList.add(person);
			}
			
		}

	

		return returnList;
	}
	
	@SuppressWarnings("unchecked")
	public List<PersonBOForRoadCompConverter> roadCheckTaStaffAutoCompleteSupportingDetails(String term) {

		RequestContext context = RequestContextHolder.getRequestContext();

		List<TAStaffBO> taStaffList = (List<TAStaffBO>) context.getFlowScope()
				.get("staffList");

		List<PersonBOForRoadCompConverter> returnList = new ArrayList<PersonBOForRoadCompConverter>();

		for (TAStaffBO staff : taStaffList) {
			if (staff.getFullName().trim().toLowerCase()
					.startsWith(term.toLowerCase())) {
				PersonBOForRoadCompConverter person = new PersonBOForRoadCompConverter();

				person.setFullName(staff.getFullName() + " [" + staff.getStaffId() + "]");
				person.setPersonId(staff.getPersonId());
				
				returnList.add(person);
			}

		}

		return returnList;
	}
	
	@SuppressWarnings("unchecked")
	public List<PersonBOForRoadCompConverter> roadCheckPoliceStaffAutoComplete(String term) {

		RequestContext context = RequestContextHolder.getRequestContext();

		List<RefCodeBO> policeStaffList = (List<RefCodeBO>) context.getFlowScope().get("policeListAll");

		List<PersonBOForRoadCompConverter> returnList = new ArrayList<PersonBOForRoadCompConverter>();
		
		for(RefCodeBO staff : policeStaffList)
		{
			if (staff.getDescription().trim().toLowerCase().startsWith(term.toLowerCase())) {
			PersonBOForRoadCompConverter person = new PersonBOForRoadCompConverter();
			
				person.setFullName(staff.getDescription() + " - " + staff.getShortDescription() + " [" + staff.getAltId() + "]");
				person.setPersonId(Integer.parseInt(staff.getCode()));

				returnList.add(person);
			}
			
		}

	

		return returnList;
	}

	public Event getWreckingList(RequestContext context) {
		// retrieve wrecking company
		RoadCheckInitiateView roadCheckInitiateView = (RoadCheckInitiateView) context.getFlowScope().get("initiateView");
		SupportingDetailsView supDetails = (SupportingDetailsView) context.getFlowScope().get("supportingDetailsView");
		HashMap<String, String> criteria = new HashMap<String, String>();

		criteria.put("status_id", Constants.Status.ACTIVE);
		criteria.put("parish_code", supDetails.getSelectedWreckerParishCode()== null || supDetails.getSelectedWreckerParishCode().isEmpty()
				? roadCheckInitiateView.getOffenceParish() :  supDetails.getSelectedWreckerParishCode());
		List<RefCodeBO> listRefCode = super.getRefInfo("roms_wrecking_company", criteria);
		List<WreckingCompanyBO> listWrecking = new ArrayList<WreckingCompanyBO>();
		WreckingCompanyBO wreckingBO = new WreckingCompanyBO();

		if (listRefCode != null) {
			for (RefCodeBO wreckingRecord : listRefCode) {
				wreckingBO = new WreckingCompanyBO();

				wreckingBO.setCompanyName(wreckingRecord.getDescription());
				wreckingBO.setWreckingCompanyId(Integer.parseInt(wreckingRecord.getCode()));

				// System.err.println(parishRecord.getCode() + " - " + parishRecord.getDescription());
				listWrecking.add(wreckingBO);

			}
		}

		context.getFlowScope().put("wreckingList", listWrecking);

		return success();
	}

	public Event getWreckingParishList(RequestContext context) {
		// retrieve parishes based on office location code for parish
		RoadCheckInitiateView roadCheckInitiateView = (RoadCheckInitiateView) context.getFlowScope().get("initiateView");
		SupportingDetailsView supDetails = (SupportingDetailsView) context.getFlowScope().get("supportingDetailsView");
		
		String wreckerParishToUseForFilter = supDetails.getSelectedWreckerParishCode() == null || supDetails.getSelectedWreckerParishCode().isEmpty()?
			roadCheckInitiateView.getOffenceParish():supDetails.getSelectedWreckerParishCode();
		
		supDetails.setSelectedWreckerParishCode(wreckerParishToUseForFilter);
		
		context.getFlowScope().put("wreckingParishList", this.getListOfParishesForOfficeRegionOfParish(wreckerParishToUseForFilter));

		return success();
	}
	
	public Event getPoundParishList(RequestContext context) {
		// retrieve all parishes
		if(context.getFlowScope().get("parishList") == null){
			this.getParishList(context);
		}
		
		RoadCheckInitiateView roadCheckInitiateView = (RoadCheckInitiateView) context.getFlowScope().get("initiateView");
		SupportingDetailsView supDetails = (SupportingDetailsView) context.getFlowScope().get("supportingDetailsView");
		
		if(supDetails.getSelectedPoundParishCode() == null || supDetails.getSelectedPoundParishCode().isEmpty()){
			supDetails.setSelectedPoundParishCode(roadCheckInitiateView.getOffenceParish());
		}
		

		return success();
	}
	
	public Event getPoundList(RequestContext context) {

		RoadCheckInitiateView roadCheckInitiateView = (RoadCheckInitiateView) context.getFlowScope().get("initiateView");
		SupportingDetailsView supDetails = (SupportingDetailsView) context.getFlowScope().get("supportingDetailsView");
		HashMap<String, String> criteria = new HashMap<String, String>();
		criteria.put("status_id", Constants.Status.ACTIVE);
		criteria.put("parish_code",supDetails.getSelectedPoundParishCode() == null || supDetails.getSelectedPoundParishCode().isEmpty() 
				? roadCheckInitiateView.getOffenceParish():supDetails.getSelectedPoundParishCode());
		List<RefCodeBO> listRefCode = super.getRefInfo("roms_pound", criteria);
		List<PoundBO> listPound = new ArrayList<PoundBO>();
		PoundBO poundBO = new PoundBO();

		if (listRefCode != null) {
			for (RefCodeBO poundRecord : listRefCode) {
				poundBO = new PoundBO();

				poundBO.setPoundName(poundRecord.getDescription());
				poundBO.setPoundId(Integer.parseInt(poundRecord.getCode()));

				listPound.add(poundBO);

			}
			
			if(listRefCode.size() == 1){
				supDetails.setPoundId(Integer.parseInt(listRefCode.get(0).getCode()));
			}
		}

		context.getFlowScope().put("poundList", listPound);

		return success();
	}

	public Event initializeOffenceList(RequestContext context) {
		List<OffenceBO> listOffence = new ArrayList<OffenceBO>();
		List<OffenceMandatoryOutcomeBO> offenceMandatoryOutcomeBOList = null;
		RoadCheckInitiateView initiateView = (RoadCheckInitiateView) context.getFlowScope().get("initiateView");
		try {
			offenceMandatoryOutcomeBOList = getRoadCompliancyService().getActiveOffenceMandatoryOutcomeList();
			List<Integer> offenceMandatoryOutcomeIntgerList = returnOffenceMandatoryOutcomeIntegerList(offenceMandatoryOutcomeBOList);
			context.getFlowScope().put("offMandatoryOutcomeList", offenceMandatoryOutcomeBOList);
			context.getFlowScope().put("offMandatoryOutcomeIntegerList", offenceMandatoryOutcomeIntgerList);
		} catch (NoRecordFoundException e) {
			context.getFlowScope().put("offMandatoryOutcomeList", null);
			context.getFlowScope().put("offMandatoryOutcomeIntegerList", null);

		}

		// set the offence list
		 listOffence = getOffenceList(Constants.RoadCheckType.DRIVERS_LICENCE);
		//listOffence = filterOffenceListBasedOnActivity(getOffenceList(Constants.RoadCheckType.DRIVERS_LICENCE), offenceMandatoryOutcomeBOList, initiateView);

		context.getFlowScope().put("dlOffenceList", listOffence);

		 listOffence = getOffenceList(Constants.RoadCheckType.MOTOR_VEHICLE);
		//listOffence = filterOffenceListBasedOnActivity(getOffenceList(Constants.RoadCheckType.MOTOR_VEHICLE), offenceMandatoryOutcomeBOList, initiateView);

		if(isOwnerDriver(context) || context.getFlowScope().get("mvResult") == null){
			
			List<OffenceBO>wrkList = new ArrayList<OffenceBO>();
			
			for(OffenceBO offenceBO:listOffence){
				if(offenceBO.getOffenceId().intValue()!= Constants.OffenceAidAndAbbet.NO_PPV_INSUR.intValue() && 
						offenceBO.getOffenceId().intValue()!= Constants.OffenceAidAndAbbet.NO_ROAD_LIC.intValue()){
					wrkList.add(offenceBO);
				}
			}
			listOffence = wrkList;
		}		
		context.getFlowScope().put("mvOffenceList", listOffence);

		listOffence = getOffenceList(Constants.RoadCheckType.ROAD_LICENCE);
		//listOffence = filterOffenceListBasedOnActivity(getOffenceList(Constants.RoadCheckType.ROAD_LICENCE), offenceMandatoryOutcomeBOList, initiateView);

		if(isOwnerDriver(context) || context.getFlowScope().get("mvResult") == null){
		
		List<OffenceBO>wrkList = new ArrayList<OffenceBO>();
		
		for(OffenceBO offenceBO:listOffence){
			if(offenceBO.getOffenceId().intValue()!= Constants.OffenceAidAndAbbet.NO_PPV_INSUR.intValue() && 
					offenceBO.getOffenceId().intValue()!= Constants.OffenceAidAndAbbet.NO_ROAD_LIC.intValue()){
				wrkList.add(offenceBO);
			}
		}
		listOffence = wrkList;
		}
		
		context.getFlowScope().put("rlOffenceList", listOffence);

		listOffence = getOffenceList(Constants.RoadCheckType.BADGE);
		//listOffence = filterOffenceListBasedOnActivity(getOffenceList(Constants.RoadCheckType.BADGE), offenceMandatoryOutcomeBOList, initiateView);

		context.getFlowScope().put("bdgOffenceList", listOffence);

		listOffence = getOffenceList(Constants.RoadCheckType.CITATION);
		//listOffence = filterOffenceListBasedOnActivity(getOffenceList(Constants.RoadCheckType.CITATION), offenceMandatoryOutcomeBOList, initiateView);

		context.getFlowScope().put("chOffenceList", listOffence);

		listOffence = getOffenceList(Constants.RoadCheckType.OTHER);
		//listOffence = filterOffenceListBasedOnActivity(getOffenceList(Constants.RoadCheckType.OTHER), offenceMandatoryOutcomeBOList, initiateView);

		context.getFlowScope().put("otherOffenceList", listOffence);

		List<OffenceOutcomeView> dlOffenceOutcomes = (List<OffenceOutcomeView>) context.getFlowScope().get("dlOffenceOutcomes");
		//dlOffenceOutcomes = removeFromSelectedOutcomesIfUnselectable(dlOffenceOutcomes, initiateView);

		List<OffenceOutcomeView> rlOffenceOutcomes = (List<OffenceOutcomeView>) context.getFlowScope().get("rlOffenceOutcomes");
		//rlOffenceOutcomes = removeFromSelectedOutcomesIfUnselectable(rlOffenceOutcomes, initiateView);

		List<OffenceOutcomeView> mvOffenceOutcomes = (List<OffenceOutcomeView>) context.getFlowScope().get("mvOffenceOutcomes");
		//mvOffenceOutcomes = removeFromSelectedOutcomesIfUnselectable(mvOffenceOutcomes, initiateView);

		List<OffenceOutcomeView> bdgOffenceOutcomes = (List<OffenceOutcomeView>) context.getFlowScope().get("bdgOffenceOutcomes");
		//bdgOffenceOutcomes = removeFromSelectedOutcomesIfUnselectable(bdgOffenceOutcomes, initiateView);

		List<OffenceOutcomeView> chOffenceOutcomes = (List<OffenceOutcomeView>) context.getFlowScope().get("chOffenceOutcomes");
		//chOffenceOutcomes = removeFromSelectedOutcomesIfUnselectable(chOffenceOutcomes, initiateView);

		List<OffenceOutcomeView> otOffenceOutcomes = (List<OffenceOutcomeView>) context.getFlowScope().get("otOffenceOutcomes");
		//otOffenceOutcomes = removeFromSelectedOutcomesIfUnselectable(otOffenceOutcomes, initiateView);

		return success();
	}

	
	private boolean isOwnerDriver(RequestContext context){
		boolean ownerIsDriver = false;
		
		RoadCheckInitiateView initiateView = (RoadCheckInitiateView) context.getFlowScope().get("initiateView");
		VehicleCheckResultBO mvResult = (VehicleCheckResultBO) context.getFlowScope().get("mvResult");
		
		if(mvResult!=null){
			for(VehicleOwnerBO ownerBO: mvResult.getVehicleOwners()){
				if(ownerBO.getTrnNbr().equalsIgnoreCase(initiateView.getTrn())){
					ownerIsDriver = true;
					break;
				}
			}
		}
		
		return ownerIsDriver;		
	}
	
	/**
	 * This function checks if there are any offence in the list that have a manditory outcome which is not allowed
	 * based on the activity type scheduled or unscheduled.
	 * 
	 * @param offenceListToFilter
	 * @param offenceMandatoryOutcomeBOList
	 * @param initiateView
	 * @return
	 */
	public List<OffenceBO> filterOffenceListBasedOnActivity(List<OffenceBO> offenceListToFilter, List<OffenceMandatoryOutcomeBO> offenceMandatoryOutcomeBOList, RoadCheckInitiateView initiateView) {
		if (initiateView.getActivityType().equalsIgnoreCase("u") && offenceMandatoryOutcomeBOList != null) {
			List<OffenceBO> offenceListToRemove = new ArrayList<OffenceBO>();
			// At this point filter list
			for (OffenceMandatoryOutcomeBO offenceManOut : offenceMandatoryOutcomeBOList) {
				for (OffenceBO offenceToFilter : offenceListToFilter) {
					if (offenceToFilter.getOffenceId().intValue() == offenceManOut.getOffenceId().intValue()) {
						if (!offenceManOut.getOutcomeTypeId().equalsIgnoreCase(Constants.OutcomeType.WARNED_FOR_PROSECUTION)) {
							offenceListToRemove.add(offenceToFilter);
						}
					}
				}
			}

			offenceListToFilter.removeAll(offenceListToRemove);
		}

		return offenceListToFilter;

	}

	/**
	 * This function removes selected offence outcomes if they are unselectable based on the wheeter the road check is
	 * scheduled or not.
	 * 
	 * @param offenceOutcomeToFilter
	 * @param initiateView
	 * @return
	 */
	public List<OffenceOutcomeView> removeFromSelectedOutcomesIfUnselectable(List<OffenceOutcomeView> offenceOutcomeToFilter, RoadCheckInitiateView initiateView) {
		if (initiateView.getActivityType().equalsIgnoreCase("u") && offenceOutcomeToFilter != null && offenceOutcomeToFilter.size() > 0) {
			List<OffenceOutcomeView> offenceOutcomeListToRemove = new ArrayList<OffenceOutcomeView>();
			// At this point filter list
			for (OffenceOutcomeView offenceOut : offenceOutcomeToFilter) {
				if (offenceOut.issueSummons || offenceOut.issueWarningNP || offenceOut.issuewWarningNotice || offenceOut.removePlate) {
					offenceOutcomeListToRemove.add(offenceOut);
				}
			}

			offenceOutcomeToFilter.removeAll(offenceOutcomeListToRemove);
		}

		return offenceOutcomeToFilter;

	}

	private List<Integer> returnOffenceMandatoryOutcomeIntegerList(List<OffenceMandatoryOutcomeBO> offenceMandatoryOutcomeBOList) {

		List<Integer> offenceMandatoryOutcomeIntgerList = new ArrayList<Integer>();

		for (OffenceMandatoryOutcomeBO offManOut : offenceMandatoryOutcomeBOList) {
			offenceMandatoryOutcomeIntgerList.add(offManOut.getOffenceId());
		}

		return offenceMandatoryOutcomeIntgerList;
	}

	public List<OffenceBO> getOffenceList(String roadCheckType) {
		// retrieve offences
		HashMap<String, String> criteria = new HashMap<String, String>();
		criteria.put("road_check_type_id", roadCheckType);
		criteria.put("status_id", Constants.Status.ACTIVE);
		List<RefCodeBO> listRefCode = super.getRefInfo("roms_offence", criteria);
		List<OffenceBO> listOffence = new ArrayList<OffenceBO>();
		OffenceBO offenceBO = new OffenceBO();

		if (listRefCode != null) {

			for (RefCodeBO offenceRecord : listRefCode) {
				offenceBO = new OffenceBO();

				offenceBO.setOffenceDescription(offenceRecord.getDescription());
				offenceBO.setOffenceId(Integer.parseInt(offenceRecord.getCode()));
				offenceBO.setShortDescription(offenceRecord.getShortDescription());
				listOffence.add(offenceBO);

			}
		}

		return listOffence;
	}

	private boolean validateInitiateRequiredFields(RequestContext context, boolean checkMVFields) {
		RoadCheckInitiateView roadCheckInitiateView = (RoadCheckInitiateView) context.getFlowScope().get("initiateView");
		boolean error = false;
		Calendar calendar = Calendar.getInstance();
		Date currentDate = calendar.getTime();

		if (StringUtils.isBlank(roadCheckInitiateView.getActivityType())) {
			addErrorMessageWithParameter(context, "RequiredFields", "Road Check Activity");
			error = true;
		}

		// if(StringUtils.isBlank(roadCheckInitiateView.getOperationName())){
		if (roadCheckInitiateView.getActivityType().equalsIgnoreCase("S") && (roadCheckInitiateView.getRoadOperationBO() == null || roadCheckInitiateView.getRoadOperationBO().getRoadOperationId() == null)) {
			addErrorMessageWithParameter(context, "RequiredFields", "Operation Name");
			error = true;
		}

		// if(StringUtils.isBlank(roadCheckInitiateView.getOffencePlace())){
		if (roadCheckInitiateView.getOffencePlace() == null || roadCheckInitiateView.getOffencePlace().getArteryId() == null) {
			addErrorMessageWithParameter(context, "RequiredFields", "Place of Offence");
			error = true;
		}
		if (roadCheckInitiateView.getOffenceDate() == null) {
			addErrorMessageWithParameter(context, "RequiredFields", "Offence Date");
			error = true;
		} else {

			XMLGregorianCalendar today = null;
			// Date convertedDate = null;

			// SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
			/* try { convertedDate = dateFormat.parse(StringUtil.getCurrentDateString()); } catch (ParseException e1) {
			 * // TODO Auto-generated catch block e1.printStackTrace(); } */
			// convertedDate = dateFormat.getCalendar().getTime();

			try {
				today = DateUtils.getXMLGregorianCalendar(currentDate);
			} catch (DatatypeConfigurationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// System.err.println("The date " + offDate);

			if (roadCheckInitiateView.getOffenceDate().after(currentDate)) {
				addErrorMessageText(context, "Offence Date cannot be after Today's Date and Time");
				error = true;
			}
			boolean offDateError = false;
			Date startDate = null;
			Date endDate = null;
			
			if (roadCheckInitiateView.getActivityType().equalsIgnoreCase("S")) {
				
				if (roadCheckInitiateView.getRoadOperationBO() != null && roadCheckInitiateView.getRoadOperationBO().getRoadOperationId() != null) {
					
					Date offDate = roadCheckInitiateView.getRoadOperationBO().getStatusId().equalsIgnoreCase(Constants.Status.ROAD_OPERATION_STARTED) ? 
							returnDateTimeInMinutesOnlyDateType(roadCheckInitiateView.getOffenceDate()) : returnDateTimeInMinutesOnlyDateType(roadCheckInitiateView.getOffenceDate());
							
					if (roadCheckInitiateView.getRoadOperationBO().getActualStartDate() != null) {
						
						startDate = roadCheckInitiateView.getRoadOperationBO().getStatusId().equalsIgnoreCase(Constants.Status.ROAD_OPERATION_STARTED) ? 
								returnDateTimeInMinutesOnlyDateType(roadCheckInitiateView.getRoadOperationBO().getActualStartDate()) : 
									returnDateTimeInMinutesOnlyDateType(roadCheckInitiateView.getRoadOperationBO().getActualStartDate());
								
						if (offDate.compareTo(startDate) < 0) {
							offDateError = true;
						}
					} else {
						if (startDate == null) {
							startDate = roadCheckInitiateView.getRoadOperationBO().getStatusId().equalsIgnoreCase(Constants.Status.ROAD_OPERATION_STARTED) ? 
									returnDateOnlyDateType(roadCheckInitiateView.getRoadOperationBO().getScheduledStartDate()) :
										returnDateTimeInMinutesOnlyDateType(roadCheckInitiateView.getRoadOperationBO().getScheduledStartDate());
						}
						if (offDate.compareTo(startDate) < 0) {
							offDateError = true;
						}
					}

					if (roadCheckInitiateView.getRoadOperationBO().getActualEndDate() != null) {
						endDate = roadCheckInitiateView.getRoadOperationBO().getStatusId().equalsIgnoreCase(Constants.Status.ROAD_OPERATION_STARTED) ? 
								returnDateOnlyDateType(roadCheckInitiateView.getRoadOperationBO().getActualEndDate()) :
									returnDateTimeInMinutesOnlyDateType(roadCheckInitiateView.getRoadOperationBO().getActualEndDate());
								
						if (offDate.compareTo(endDate) > 0) {
							offDateError = true;
						}
					} else {
						if (endDate == null) {
							
							Calendar calRoadEndDate = Calendar.getInstance();
							calRoadEndDate.setTime(roadCheckInitiateView.getRoadOperationBO().getScheduledEndDate());
							calRoadEndDate.set(Calendar.HOUR_OF_DAY, 23);
							calRoadEndDate.set(Calendar.MINUTE, 59);
							calRoadEndDate.set(Calendar.SECOND, 59);
							endDate = roadCheckInitiateView.getRoadOperationBO().getStatusId().equalsIgnoreCase(Constants.Status.ROAD_OPERATION_STARTED) ?
									returnDateTimeInMinutesOnlyDateType(calRoadEndDate.getTime()) :
										returnDateTimeInMinutesOnlyDateType(roadCheckInitiateView.getRoadOperationBO().getScheduledEndDate());
						}
						if (offDate.compareTo(endDate) > 0) {
							offDateError = true;
						}
					}
				}

				if (offDateError == true) {
					try {
						error = true;
						if (startDate.compareTo(endDate) == 0) {
							addErrorMessageText(context, "Offence Date must be " + DateUtils.getFormattedUtilDate(startDate) + " for " + roadCheckInitiateView.getRoadOperationBO().getOperationName());
						} else {
							//addErrorMessageText(context, "Offence Date must be between " + DateUtils.getFormattedUtilDate(startDate) + " and " + DateUtils.getFormattedUtilDate(endDate) + " for " + roadCheckInitiateView.getRoadOperationBO().getOperationName());
							addErrorMessageText(context, "Offence Date must be between " + (roadCheckInitiateView.getRoadOperationBO().getStatusId().equalsIgnoreCase(Constants.Status.ROAD_OPERATION_STARTED) ? 
									DateUtils.getFormattedUtilLongDate(startDate) : DateUtils.getFormattedUtilLongDate(startDate)) + " and " + (roadCheckInitiateView.getRoadOperationBO().getStatusId().equalsIgnoreCase(Constants.Status.ROAD_OPERATION_STARTED) ? 
											DateUtils.getFormattedUtilLongDate(endDate) : DateUtils.getFormattedUtilLongDate(endDate)) + " for " + roadCheckInitiateView.getRoadOperationBO().getOperationName());
						}
					} catch (Exception e) {
						logger.error("Road Check",e);
					}
				}
			}

		}

		// if(StringUtils.isBlank(roadCheckInitiateView.getInspector())){
		if (roadCheckInitiateView.getTaStaffBO() == null || StringUtils.isBlank(roadCheckInitiateView.getTaStaffBO().getStaffId())) {
			addErrorMessageWithParameter(context, "RequiredFields", "Inspector");
			error = true;
		}

		if (StringUtils.isBlank(roadCheckInitiateView.getRoleObserved())) {
			addErrorMessageWithParameter(context, "RequiredFields", "Role Observed");
			error = true;
		}else if("T".equalsIgnoreCase(roadCheckInitiateView.getRoleObserved()) 
				&& StringUtils.isBlank(roadCheckInitiateView.getOtherRoleId())){
			addErrorMessageWithParameter(context, "RequiredFields", "Other Role Observed");
			error = true;
			
		}
		
		//global address validation - kpowell

		boolean errorFoundInAddress  = validateAddress(context, roadCheckInitiateView.getAddressView());
            
        if(errorFoundInAddress){
              error = true;
        }


		/* if(StringUtils.isBlank(roadCheckInitiateView.getMobilePhoneNo()) &&
		 * StringUtils.isBlank(roadCheckInitiateView.getHomePhoneNo()) &&
		 * StringUtils.isBlank(roadCheckInitiateView.getWorkPhoneNo())){ addErrorMessageWithParameter(context,
		 * "RequiredFields", "At least one Contact Number"); error=true; } */


		// Validate Phone numbers
		logger.info("Mobile Phone " + roadCheckInitiateView.getMobilePhoneNo());
		if (!StringUtils.isBlank(roadCheckInitiateView.getMobilePhoneNo()) && !roadCheckInitiateView.getMobilePhoneNo().equals("(___)___-____")) {
			if (!PhoneNumberUtil.validateAllPhoneNumbers(roadCheckInitiateView.getMobilePhoneNo())) {
				addErrorMessageText(context, "Mobile Contact Number is not valid. E.g.(555)555-5555");
				error = true;
			}
		}
		if (!StringUtils.isBlank(roadCheckInitiateView.getHomePhoneNo()) && !roadCheckInitiateView.getHomePhoneNo().equals("(___)___-____")) {
			if (!PhoneNumberUtil.validateAllPhoneNumbers(roadCheckInitiateView.getHomePhoneNo())) {
				addErrorMessageText(context, "Home Contact Number is not valid. E.g.(555)555-5555");
				error = true;
			}
		}
		if (!StringUtils.isBlank(roadCheckInitiateView.getWorkPhoneNo()) && !roadCheckInitiateView.getWorkPhoneNo().equals("(___)___-____")) {
			if (!PhoneNumberUtil.validateAllPhoneNumbers(roadCheckInitiateView.getWorkPhoneNo())) {
				addErrorMessageText(context, "Work Contact Number is not valid. E.g.(555)555-5555");
				error = true;
			}
		}
		
		if (StringUtils.isBlank(roadCheckInitiateView.getPlateRegNo())) {
			addErrorMessageWithParameter(context, "RequiredFields", "Plate No.");
			error = true;
		}

		if (roadCheckInitiateView.isDisableMotorVehicleFields() == false && checkMVFields == true) {

//			if (StringUtils.isBlank(roadCheckInitiateView.getChassisNo())) {
//				addErrorMessageWithParameter(context, "RequiredFields", "Chassis No.");
//				error = true;
//			}

//			if (StringUtils.isBlank(roadCheckInitiateView.getEngineNo())) {
//				addErrorMessageWithParameter(context, "RequiredFields", "Engine No.");
//				error = true;
//			}

			if (StringUtils.isBlank(roadCheckInitiateView.getMakeDescription())) {
				addErrorMessageWithParameter(context, "RequiredFields", "Make");
				error = true;
			}

			if (StringUtils.isBlank(roadCheckInitiateView.getModel())) {
				addErrorMessageWithParameter(context, "RequiredFields", "Model");
				error = true;
			}

			if (StringUtils.isBlank(roadCheckInitiateView.getType())) {
				addErrorMessageWithParameter(context, "RequiredFields", "Type");
				error = true;
			}

			if (StringUtils.isBlank(roadCheckInitiateView.getColour())) {
				addErrorMessageWithParameter(context, "RequiredFields", "Colour");
				error = true;
			}

			if (roadCheckInitiateView.getYear() == null) {
				addErrorMessageWithParameter(context, "RequiredFields", "Year");
				error = true;
			} else {
				// validate MV year
				int firstYear = 1800;
				int lastYear = Calendar.getInstance().get(Calendar.YEAR) + 1;
				// System.err.println("lastYear: " + lastYear);
				if (roadCheckInitiateView.getYear().intValue() < firstYear || roadCheckInitiateView.getYear().intValue() > lastYear) {
					addErrorMessage(context, "InvalidYear");
					error = true;
				}
			}

		}

		if (checkMVFields == true) {
			if (roadCheckInitiateView.isBadgeQuery()) {
				if (StringUtils.isBlank(roadCheckInitiateView.getBadgeNo())) {
					addErrorMessageWithParameter(context, "RequiredFields", "Badge #");
					error = true;
				}
			}

			if (roadCheckInitiateView.isDlQuery()) {
				if (StringUtils.isBlank(roadCheckInitiateView.getDlNo())) {
					addErrorMessageWithParameter(context, "RequiredFields", "Driver Licence #");
					error = true;
				}
			}

//			if (roadCheckInitiateView.isRoadLicQuery()) {
//				if (StringUtils.isBlank(roadCheckInitiateView.getRoadLicNo())) {
//					addErrorMessageWithParameter(context, "RequiredFields", "Road Licence #");
//					error = true;
//				}
//			}
		}
		return error;
	}

	private ComplianceBO pupulateComplianceBO(RoadCheckInitiateView roadCheckInitiateView, ComplianceBO complianceBO) {
		// ComplianceBO complianceBO = new ComplianceBO();

		complianceBO.setComplianceDate(roadCheckInitiateView.getOffenceDate());
		
		complianceBO.setVehicleInfoDifferent(roadCheckInitiateView.isVehicleInfoDifferent());
		// complianceBO.setIs("N");
		complianceBO.setStatusId(Constants.Status.ROAD_CHECK_INCOMPLETE);
		PersonBO personBO = new PersonBO();
		VehicleBO vehicleBO = new VehicleBO();
		personBO.setTrnNbr(roadCheckInitiateView.getTrn());
		personBO.setFirstName(roadCheckInitiateView.getFirstName());
		personBO.setLastName(roadCheckInitiateView.getLastName());
		personBO.setMiddleName(roadCheckInitiateView.getMiddleName());

		personBO.setMarkText(roadCheckInitiateView.getAddressView().getMarkText());
		personBO.setParishCode(roadCheckInitiateView.getAddressView().getParish());
		// personBO.setParishCode("12");
		personBO.setPoBoxNo(roadCheckInitiateView.getAddressView().getPoBoxNo());
		personBO.setPoLocationName(roadCheckInitiateView.getAddressView().getPoLocationName());
		personBO.setStreetName(roadCheckInitiateView.getAddressView().getStreetName());
		personBO.setStreetNo(roadCheckInitiateView.getAddressView().getStreetNo());

		personBO.setTelephoneCell(roadCheckInitiateView.getMobilePhoneNo());
		personBO.setTelephoneHome(roadCheckInitiateView.getHomePhoneNo());
		personBO.setTelephoneWork(roadCheckInitiateView.getWorkPhoneNo());
		personBO.setCurrentUserName(getRomsLoggedInUser().getUsername());
		
		complianceBO.setCurrentUserName(getRomsLoggedInUser().getUsername()); // need to change
		complianceBO.setRoadOperation(roadCheckInitiateView.getRoadOperationBO().getRoadOperationId());
		complianceBO.setCompliancyArteryid(roadCheckInitiateView.getOffencePlace().getArteryId());
		complianceBO.setTaStaff(roadCheckInitiateView.getTaStaffBO().getStaffId());
		complianceBO.setPersonRole(roadCheckInitiateView.getRoleObserved());
		
		//only set if other role is observed
		if(roadCheckInitiateView.getRoleObserved()!=null&&
				Constants.PersonRole.OTHER.equalsIgnoreCase(roadCheckInitiateView.getRoleObserved()))
		{
			complianceBO.setOtherRole(StringUtils.trimToNull(roadCheckInitiateView.getOtherRoleId()));
		}else{
			complianceBO.setOtherRole(null);
		}
		complianceBO.setPerson(personBO);

		
		
		if (roadCheckInitiateView.getComplianceId() != null) {
			logger.info("Chasiss Number is : " + roadCheckInitiateView.getChassisNo());
			vehicleBO.setChassisNo(roadCheckInitiateView.getChassisNo());
			vehicleBO.setColour(roadCheckInitiateView.getColour());
			vehicleBO.setEngineNo(roadCheckInitiateView.getEngineNo());
			vehicleBO.setMakeDescription(roadCheckInitiateView.getMakeDescription());
			vehicleBO.setModel(roadCheckInitiateView.getModel());
			vehicleBO.setPlateRegNo(roadCheckInitiateView.getPlateRegNo());
			vehicleBO.setYear(roadCheckInitiateView.getYear());
			vehicleBO.setTypeDesc(roadCheckInitiateView.getType());
			vehicleBO.setOwnerName(roadCheckInitiateView.getOwnerName());
			vehicleBO.setCurrentUserName(getRomsLoggedInUser().getUsername());
			
			complianceBO.setVehicle(vehicleBO);
			
		}

		return complianceBO;
	}

	public Event queryMV(RequestContext context) {
		
		
		RoadCheckInitiateView roadCheckInitiateView = (RoadCheckInitiateView) context.getFlowScope().get("initiateView");

		boolean terminated = false;
		try {
			if (roadCheckInitiateView.getRoadOperationBO() != null && roadCheckInitiateView.getRoadOperationBO().getStatusId() != null) {
				if (roadCheckInitiateView.getRoadOperationBO().getStatusId().equalsIgnoreCase(Constants.Status.ROAD_OPERATION_TERMINATED)) {
					if (roadCheckInitiateView.isMobileDevice()) {
						addErrorMessageText(context, roadCheckInitiateView.getRoadOperationBO().getOperationName() + " has been Terminated, cannot create a Road Check.");
						terminated = true;
						return error();
					}
				}
			}

			if (terminated == false && roadCheckInitiateView.getRoadOperationBO() != null && roadCheckInitiateView.getRoadOperationBO().getRoadOperationId() != null) {
				RoadOperationBO roadOperationBO = new RoadOperationBO();
				roadOperationBO = returnRoadOperationBO(roadCheckInitiateView.getRoadOperationBO().getRoadOperationId());
				if (roadOperationBO != null && roadOperationBO.getStatusId() != null) {
					if (roadOperationBO.getStatusId().equalsIgnoreCase(Constants.Status.ROAD_OPERATION_TERMINATED)) {
						if (roadCheckInitiateView.isMobileDevice()) {
							addErrorMessageText(context, roadOperationBO.getOperationName() + " has been Terminated, cannot create a Road Check.");
							terminated = true;
							return error();
						}
					}
				}

			}

			if(roadCheckInitiateView.isByPassValidation()){
				roadCheckInitiateView.setByPassValidation(false);
			}
			else{
				if (validateInitiateRequiredFields(context, false)) {
					return error();
				}
			}

			ComplianceBO complianceBO = new ComplianceBO();
			complianceBO = pupulateComplianceBO(roadCheckInitiateView, complianceBO);
			
			if (roadCheckInitiateView.getComplianceId() == null) {
				
				complianceBO = getRoadCompliancyService().saveCompliance(complianceBO);
				roadCheckInitiateView.setComplianceId(complianceBO.getComplianceId());
			}
			else{
				/*It is possible that values for the compliance may have changed from the UI so send the
				 * the compliance object to the service to try and update.*/
				complianceBO.setComplianceId(roadCheckInitiateView.getComplianceId() );
				getRoadCompliancyService().saveCompliance(complianceBO);
			}
			 
			// reset Motor Vehicle Values
			roadCheckInitiateView.setMakeDescription("");
			roadCheckInitiateView.setModel("");
			roadCheckInitiateView.setYear(null);
			roadCheckInitiateView.setColour("");
			roadCheckInitiateView.setChassisNo("");
			roadCheckInitiateView.setEngineNo("");
			roadCheckInitiateView.setLicenceType("");
			roadCheckInitiateView.setRouteEnd("");
			roadCheckInitiateView.setRouteStart("");
			roadCheckInitiateView.setRoadLicExpiryDate(null);
			roadCheckInitiateView.setRoadLicNo("");
			roadCheckInitiateView.setNoRlFound(false);
			roadCheckInitiateView.setType("");
			roadCheckInitiateView.setPrevRoadLicNo(roadCheckInitiateView.getRoadLicNo());
			


			VehicleCheckResultBO vehicleCheckResultBO = new VehicleCheckResultBO();
			vehicleCheckResultBO.setComplianceID(roadCheckInitiateView.getComplianceId());
			vehicleCheckResultBO.setPlateRegNo(roadCheckInitiateView.getPlateRegNo());
			vehicleCheckResultBO.setCurrentUserName(getRomsLoggedInUser().getUsername());


			RoadLicVehCheckResultBO roadLicVehCheckResultBO =  this.getRoadCompliancyService().performRoadLicMotorVehicleCheck(vehicleCheckResultBO);
			//vehicleCheckResultBO = getRoadCompliancyPortProxy().performMotorVehicleCheck(vehicleCheckResultBO);
			
			if (roadLicVehCheckResultBO == null) 
			{
				roadCheckInitiateView.setDisableMotorVehicleFields(false);
				roadCheckInitiateView.setPrevPlateRegNo(roadCheckInitiateView.getPlateRegNo());
				addWarningMessageWithParameter(context, "NoMVRecordfound", roadCheckInitiateView.getPlateRegNo());
				addWarningMessageWithParameter(context, "NoRLRecordfoundForPlate", roadCheckInitiateView.getPlateRegNo());
				roadCheckInitiateView.setVehicleInfoDifferent(true);
				return error();
			}
			
			vehicleCheckResultBO = roadLicVehCheckResultBO.getVehicleCheckResult();
			roadCheckInitiateView.setRlQueryDone(true);
			roadCheckInitiateView.setRoadLicQuery(true);
		
			
			Calendar today = new GregorianCalendar();
			
			if(roadLicVehCheckResultBO.getVehicleCheckResult() == null)
			{
				roadCheckInitiateView.setDisableMotorVehicleFields(false);
				roadCheckInitiateView.setChassisNo("");
				roadCheckInitiateView.setEngineNo("");
				roadCheckInitiateView.setPrevPlateRegNo(roadCheckInitiateView.getPlateRegNo());
				addWarningMessageWithParameter(context, "NoMVRecordfound", roadCheckInitiateView.getPlateRegNo());
				
				roadCheckInitiateView.setVehicleInfoDifferent(true);
			}
	
			
			vehicleCheckResultBO = roadLicVehCheckResultBO.getVehicleCheckResult();
			
			
			if(vehicleCheckResultBO != null)
			{
				roadCheckInitiateView.setDisableMotorVehicleFields(true);
				roadCheckInitiateView.setChassisNo(vehicleCheckResultBO.getChassisNo());
				roadCheckInitiateView.setEngineNo(vehicleCheckResultBO.getEngineNo());
				roadCheckInitiateView.setMakeDescription(vehicleCheckResultBO.getMakeDescription());
				roadCheckInitiateView.setModel(vehicleCheckResultBO.getModel());
				roadCheckInitiateView.setYear(vehicleCheckResultBO.getYear());
				roadCheckInitiateView.setColour(vehicleCheckResultBO.getColour());
				roadCheckInitiateView.setType(vehicleCheckResultBO.getTypeDesc());
				
				try{
				
					VehicleOwnerBO vehicleOwnerBO =	vehicleCheckResultBO.getVehicleOwners().get(0);
					
					String ownerName = "";
							
					NameUtil nameUtil = new NameUtil();
					
					ownerName = nameUtil.getLastNameCapsFirstNameMiddleName
							(vehicleOwnerBO.getFirstName(), vehicleOwnerBO.getLastName(),"");		
							
					roadCheckInitiateView.setOwnerName(ownerName);
					
				}catch(Exception ex){
					System.err.println("No owner found ");					
				}
				roadCheckInitiateView.setVehicleInfoDifferent(false);
				
				

			}
			
			
			
			/*Set Road License Related Information*/
			if(roadLicVehCheckResultBO.getRoadLicCheckResult() != null && (roadLicVehCheckResultBO.getRoadLicCheckResult().getLicenceNo() != null && roadLicVehCheckResultBO.getRoadLicCheckResult().getLicenceNo() > 0))
			{
				roadCheckInitiateView.setRouteEnd(roadLicVehCheckResultBO.getRoadLicCheckResult().getRouteEnd());
				roadCheckInitiateView.setRouteStart(roadLicVehCheckResultBO.getRoadLicCheckResult().getRouteStart());
				roadCheckInitiateView.setLicenceType(roadLicVehCheckResultBO.getRoadLicCheckResult().getLicenceType());
				roadCheckInitiateView.setRoadLicExpiryDate(roadLicVehCheckResultBO.getRoadLicCheckResult().getExpiryDate());
				roadCheckInitiateView.setRoadLicNo(roadLicVehCheckResultBO.getRoadLicCheckResult().getLicenceNo() != null ? roadLicVehCheckResultBO.getRoadLicCheckResult().getLicenceNo().toString() : null);
				roadCheckInitiateView.setPrevRoadLicNo(roadLicVehCheckResultBO.getRoadLicCheckResult().getLicenceNo() != null ? roadLicVehCheckResultBO.getRoadLicCheckResult().getLicenceNo().toString() : null);
				
				/*if(roadLicVehCheckResultBO.getRoadLicCheckResult().getExpiryDate()!=null&& 
						 today.after(roadLicVehCheckResultBO.getRoadLicCheckResult().getExpiryDate().toGregorianCalendar())){
						roadCheckInitiateView.setRlCkFl(true);
				}*/
				
				
				
				
			
				
			
			}
			else
			{
				
				roadCheckInitiateView.setNoRlFound(true);
				addWarningMessageWithParameter(context, "NoRLRecordfoundForPlate", roadCheckInitiateView.getPlateRegNo());
			}

			context.getFlowScope().put("rlResult", roadLicVehCheckResultBO.getRoadLicCheckResult());
			
			context.getFlowScope().put("initiateView", roadCheckInitiateView);
			context.getFlowScope().put("mvResult", vehicleCheckResultBO);

			roadCheckInitiateView.setPrevPlateRegNo(roadCheckInitiateView.getPlateRegNo());

			
			
			return success();

		} catch (ErrorSavingException e) {
			
			addErrorMessage(context, "SystemError");
			e.printStackTrace();
			return error();
		} catch (InvalidFieldException e) {
			
			addErrorMessage(context, "InvalidParameter");
			e.printStackTrace();
			return error();
		} catch (NoRecordFoundException e) {
			roadCheckInitiateView.setDisableMotorVehicleFields(false);
			roadCheckInitiateView.setPrevPlateRegNo(roadCheckInitiateView.getPlateRegNo());
			addWarningMessageWithParameter(context, "NoMVRecordfound", roadCheckInitiateView.getPlateRegNo());
			e.printStackTrace();
			return error();
		} catch (RequiredFieldMissingException e) {
			addErrorMessage(context, "SystemError");
			e.printStackTrace();
			return error();
		} catch (Exception e) {
			addErrorMessage(context, "SystemError");
			e.printStackTrace();
			roadCheckInitiateView.setDisableMotorVehicleFields(false);
			roadCheckInitiateView.setPrevPlateRegNo(roadCheckInitiateView.getPlateRegNo());
			addWarningMessageWithParameter(context, "QueryResultError", "Road Licence");
			roadCheckInitiateView.setRoadLicQuery(false);
			return error();
		}

	}
	
	public void quickQueryMV(RequestContext context)
	{
		context.getFlowScope().put("mvResult", null);
		context.getFlowScope().put("rlResult", null);
		
		
		RoadCheckInitiateView roadCheckInitiateView = (RoadCheckInitiateView) context.getFlowScope().get("initiateView");
		
		roadCheckInitiateView.setChassisNo("");
		roadCheckInitiateView.setColour("");
		roadCheckInitiateView.setType("");
		roadCheckInitiateView.setMakeDescription("");
		roadCheckInitiateView.setModel("");
		roadCheckInitiateView.setYear(null);
		roadCheckInitiateView.setEngineNo("");
		
		VehicleCheckResultBO vehicleCheckResultBO = new VehicleCheckResultBO();
		vehicleCheckResultBO.setComplianceID(roadCheckInitiateView.getComplianceId());
		vehicleCheckResultBO.setPlateRegNo(roadCheckInitiateView.getPlateRegNo());
		vehicleCheckResultBO.setCurrentUserName(getRomsLoggedInUser().getUsername());
		
		vehicleCheckResultBO.setOnActiveRoadOperation((this.getRomsLoggedInUser().getCurrentRoadOperationId() != null && this.getRomsLoggedInUser().getCurrentRoadOperationId() > 0)? 
				true : false);
		
		try
		{
			RoadLicVehCheckResultBO roadLicVehCheckResultBO =  this.getRoadCompliancyService().performRoadLicMotorVehicleQuery(vehicleCheckResultBO);
			
			if(roadLicVehCheckResultBO.getRoadLicCheckResult() != null && (roadLicVehCheckResultBO.getRoadLicCheckResult().getLicenceNo() != null && roadLicVehCheckResultBO.getRoadLicCheckResult().getLicenceNo() > 0))
			{
				roadCheckInitiateView.setNoRlFound(false);
			}
			else
			{
				roadCheckInitiateView.setNoRlFound(true);
			}
			
			if(roadLicVehCheckResultBO.getVehicleCheckResult() != null)
			{
				roadCheckInitiateView.setNoVehicleFound(false);
				
				vehicleCheckResultBO = roadLicVehCheckResultBO.getVehicleCheckResult();
				
				roadCheckInitiateView.setChassisNo(vehicleCheckResultBO.getChassisNo());
				roadCheckInitiateView.setColour(vehicleCheckResultBO.getColour());
				roadCheckInitiateView.setType(vehicleCheckResultBO.getTypeDesc());
				roadCheckInitiateView.setMakeDescription(vehicleCheckResultBO.getMakeDescription());
				roadCheckInitiateView.setModel(vehicleCheckResultBO.getModel());
				roadCheckInitiateView.setYear(vehicleCheckResultBO.getYear());
				roadCheckInitiateView.setEngineNo(vehicleCheckResultBO.getEngineNo());
			}
			else
			{
				roadCheckInitiateView.setNoVehicleFound(true);
			}
			
			highlightFields(roadCheckInitiateView,roadLicVehCheckResultBO.getRoadLicCheckResult(),roadLicVehCheckResultBO.getVehicleCheckResult());
			
			context.getFlowScope().put("mvResult", roadLicVehCheckResultBO.getVehicleCheckResult());
			context.getFlowScope().put("rlResult", roadLicVehCheckResultBO.getRoadLicCheckResult());
			
			
		}
		catch (ErrorSavingException e)
		{
		
			e.printStackTrace();
			addErrorMessage(context, "SystemError");
		} 
		catch (InvalidFieldException e)
		{
			
			e.printStackTrace();
			addErrorMessage(context, "InvalidParameter");
		} 
		catch (NoRecordFoundException e)
		{
			e.printStackTrace();
			
			if(e.getMessage().contains("private vehicle"))
			{
				addWarningMessageText(context, e.getMessage());
			}
			else
			{
				addWarningMessageWithParameter(context, "NoRLRecordfoundForPlate",roadCheckInitiateView.getPlateRegNo());
			}
			
		}
		catch (RequiredFieldMissingException e)
		{
			e.printStackTrace();
			addErrorMessage(context, "SystemError");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			addErrorMessageWithParameter(context, "QueryResultError", "Road Licence");
		}
		
		
	}

	public void highlightFields(RoadCheckInitiateView roadCheckInitiateView,RoadLicCheckResultBO roadLicCheckResultBO,VehicleCheckResultBO vehicleCheckResultBO)
	{
		Date today = Calendar.getInstance().getTime();
		
		roadCheckInitiateView.setbGCkFl(false);
		roadCheckInitiateView.setDlCkFl(false);
		roadCheckInitiateView.setRlCkFl(false);
		roadCheckInitiateView.setRlInCkFl(false);
		roadCheckInitiateView.setRlFtCkFl(false);
		roadCheckInitiateView.setmVCkFl(false);
		roadCheckInitiateView.setInCkFl(false);
		roadCheckInitiateView.setfTCkFl(false);
		roadCheckInitiateView.setRlExpire(false);
		
		if(roadLicCheckResultBO != null)
		{
			if(roadLicCheckResultBO.getExpiryDate()!=null&& 
					 today.after(roadLicCheckResultBO.getExpiryDate())){
					roadCheckInitiateView.setRlCkFl(true);
					roadCheckInitiateView.setRlExpire(true);
			}
			
			try {
				if (today.after(roadLicCheckResultBO.getInsuranceExpDate())) {
					roadCheckInitiateView.setRlInCkFl(true);
				}
			} catch (Exception ex) {
				logger.info("Insurance exp date is null.");
			}

			try {
				if (today.after(roadLicCheckResultBO.getFitnessExpDate())) {
					roadCheckInitiateView.setRlFtCkFl(true);
				}
			} catch (Exception ex) {
				logger.info("Fitness exp date is null.");
			}
		}
		
		if(vehicleCheckResultBO != null)
		{
			//System.err.println("MVC expired: "+today.after(vehicleCheckResultBO.getMvrcExpiryDate().toGregorianCalendar()));
			
			if(vehicleCheckResultBO.getMvrcExpiryDate()!=null&& today.after(vehicleCheckResultBO.getMvrcExpiryDate())){
				roadCheckInitiateView.setmVCkFl(true);
			}
			
			if(vehicleCheckResultBO.getInsuranceInfo()!=null&& vehicleCheckResultBO.getInsuranceInfo().getExpiryDate() != null&& today.after(vehicleCheckResultBO.getInsuranceInfo().getExpiryDate())){
				//roadCheckInitiateView.setmVCkFl(true);
				roadCheckInitiateView.setInCkFl(true);
			}
			
			if(vehicleCheckResultBO.getFitnessInfo()!=null && vehicleCheckResultBO.getFitnessInfo().getExpiryDate() != null&& today.after(vehicleCheckResultBO.getFitnessInfo().getExpiryDate())){
				//roadCheckInitiateView.setmVCkFl(true);
				roadCheckInitiateView.setfTCkFl(true);
			}
		}
		
	}
	
	public Event validateInitiate(RequestContext context) {
		boolean error = validatePerformOrProceed(context);
		if (error) {
			return error();
		}
		
		RecordOffenceOutcomeView recordOffenceOutcomeView = (RecordOffenceOutcomeView) context.getFlowScope().get("recordOffenceOutcomeView");
		outcomeActionToTake(context, recordOffenceOutcomeView);
		
		return success();
	}

	private boolean validatePerformOrProceed(RequestContext context) {
		RoadCheckInitiateView roadCheckInitiateView = (RoadCheckInitiateView) context.getFlowScope().get("initiateView");

		boolean error = false;

		if (!roadCheckInitiateView.getPlateRegNo().equalsIgnoreCase(roadCheckInitiateView.getPrevPlateRegNo())) {
			addErrorMessage(context, "NoMVQuery");
			error = true;
		}
		
		//KG
		if (roadCheckInitiateView.isDlQuery() || roadCheckInitiateView.isBadgeQuery()) {
			if("T".equalsIgnoreCase(roadCheckInitiateView.getRoleObserved())){
				addErrorMessage(context, "InvalidRoleObserved");
				error = true;
			}
		}
		//

		if (validateInitiateRequiredFields(context, true) || error == true) {
			error = true;
		}
	
		/* if(error==false){ RoadOperationCriteriaBO roadOpCriteria = new RoadOperationCriteriaBO();
		 * roadOpCriteria.setRoadOperationId(roadCheckInitiateView.getOperationId()); List<RoadOperationBO> roadOps =
		 * new ArrayList<RoadOperationBO>(); try { roadOps =
		 * getRoadOperationPortProxy().lookupRoadOperation(roadOpCriteria); if(roadOps!=null && roadOps.size()==1){
		 * roadCheckInitiateView.setRoadOperationBO(roadOps.get(0)); } } catch
		 * (InvalidFieldException e) { // TODO Auto-generated catch
		 * block e.printStackTrace(); } catch
		 * (NoRecordFoundException e) { // TODO Auto-generated
		 * catch block e.printStackTrace(); } catch
		 * (RequiredFieldMissingException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); } } */
		return error;
	}

	public void initiateOffenceOutcomes(RequestContext context) {
		// initialize the individual query offence outcome list

		/* List<OffenceOutcomeView> offenceOutcomes1 = new ArrayList<OffenceOutcomeView>(); OffenceOutcomeView
		 * offenceOutcomeView1 =new OffenceOutcomeView(); offenceOutcomes1.add(offenceOutcomeView1);
		 * List<OffenceOutcomeView> offenceOutcomes2 = new ArrayList<OffenceOutcomeView>(); OffenceOutcomeView
		 * offenceOutcomeView2 =new OffenceOutcomeView(); offenceOutcomes2.add(offenceOutcomeView2);
		 * List<OffenceOutcomeView> offenceOutcomes3 = new ArrayList<OffenceOutcomeView>(); OffenceOutcomeView
		 * offenceOutcomeView3 =new OffenceOutcomeView(); offenceOutcomes3.add(offenceOutcomeView3);
		 * List<OffenceOutcomeView> offenceOutcomes4 = new ArrayList<OffenceOutcomeView>(); OffenceOutcomeView
		 * offenceOutcomeView4 =new OffenceOutcomeView(); offenceOutcomes4.add(offenceOutcomeView4);
		 * List<OffenceOutcomeView> offenceOutcomes5 = new ArrayList<OffenceOutcomeView>(); OffenceOutcomeView
		 * offenceOutcomeView5 =new OffenceOutcomeView(); offenceOutcomes5.add(offenceOutcomeView5);
		 * List<OffenceOutcomeView> offenceOutcomes6 = new ArrayList<OffenceOutcomeView>(); OffenceOutcomeView
		 * offenceOutcomeView6 =new OffenceOutcomeView(); offenceOutcomes6.add(offenceOutcomeView6); */
		context.getFlowScope().put("mvOffenceOutcomes", new ArrayList<OffenceOutcomeView>());
		context.getFlowScope().put("dlOffenceOutcomes", new ArrayList<OffenceOutcomeView>());
		context.getFlowScope().put("rlOffenceOutcomes", new ArrayList<OffenceOutcomeView>());
		context.getFlowScope().put("bdgOffenceOutcomes", new ArrayList<OffenceOutcomeView>());
		context.getFlowScope().put("chOffenceOutcomes", new ArrayList<OffenceOutcomeView>());
		// context.getFlowScope().put("otOffenceOutcomes", offenceOutcomes6);
		context.getFlowScope().put("otOffenceOutcomes", new ArrayList<OffenceOutcomeView>());// should start empty
	}

	/**
	 * This function takes care of all the set up needed if the perform road check is being called from
	 * the query motor vehicle screen.
	 * @param context
	 */
	public void fromQuickQuery(RequestContext context)
	{
		RoadCheckInitiateView roadCheckInitiateView = (RoadCheckInitiateView) context.getFlowScope().get("initiateView");
		
		RoadCheckInitiateView fromQuickQuery = (RoadCheckInitiateView) context.getFlowScope().get("initiateViewFromQuickQuery");
		
		try
		{
			/*The application only needs to do this once. For first entry from quick query.*/
			if(fromQuickQuery != null && (roadCheckInitiateView.getRoadOperationBO() == null || roadCheckInitiateView.getRoadOperationBO().getRoadOperationId() == null || roadCheckInitiateView.getRoadOperationBO().getRoadOperationId() < 1))
			{
				roadCheckInitiateView.setPlateRegNo(fromQuickQuery.getPlateRegNo());
				roadCheckInitiateView.setRoleObserved("D");
				roadCheckInitiateView.setTaStaffBO(getRoadOperationService().getStaffByUsername(getRomsLoggedInUser().getUsername()));
				roadCheckInitiateView.setActivityType("S");
				
				roadCheckInitiateView.setRoadOperationBO(returnRoadOperationBO(getRomsLoggedInUser().getCurrentRoadOperationId()));				
				
				if(!validateAddress(roadCheckInitiateView.getAddressView())){
					roadCheckInitiateView.setByPassValidation(true);
				}else{
					roadCheckInitiateView.setByPassValidation(false);
				}
				
				context.getFlowScope().put("initiateView",roadCheckInitiateView);
				
				getListsForOperation(true);								
				
				this.queryMV(context);
			
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	public Event proceedToReviewSummary(RequestContext context) {

		try {
			boolean error = false;
			RecordOffenceOutcomeView recordOffenceOutcomeView = (RecordOffenceOutcomeView) context.getFlowScope().get("recordOffenceOutcomeView");
			SupportingDetailsView supportingDetailsView = (SupportingDetailsView) context.getFlowScope().get("supportingDetailsView");
			RoadCheckInitiateView roadCheckInitiateView = (RoadCheckInitiateView) context.getFlowScope().get("initiateView");

			if (recordOffenceOutcomeView.isWarningNoticeSelected()) {
				
				if (supportingDetailsView.getSelectedPoundParishCode() == null || supportingDetailsView.getSelectedPoundParishCode().isEmpty())
				{
					error = true;
					addErrorMessageWithParameter(context, "RequiredFields", "Parish of Pound");
				}
				
				if (supportingDetailsView.getPoundId() == null || supportingDetailsView.getPoundId() == 0)
				{
					error = true;
					addErrorMessageWithParameter(context, "RequiredFields", "Pound");
				}
				

				if (StringUtils.isEmpty(supportingDetailsView.getVehicleMoverType())) 
				{
					error = true;
					addErrorMessageWithParameter(context, "RequiredFields", "Vehicle Moved By");
				} 
				else 
				{
					if (supportingDetailsView.getVehicleMoverType().equalsIgnoreCase("WD")) 
					{
						if (supportingDetailsView.getWreckingCompanyId() == null || supportingDetailsView.getWreckingCompanyId() == 0) 
						{
							error = true;
							addErrorMessageWithParameter(context, "RequiredFields", "Wrecker Company");
						}
						
						if (supportingDetailsView.getSelectedWreckerParishCode() == null || supportingDetailsView.getSelectedWreckerParishCode() == "") 
						{
							error = true;
							addErrorMessageWithParameter(context, "RequiredFields", "Wrecker Company Parish");
						}

						if (!(supportingDetailsView.getVehicleMoverWreckerBO() == null || supportingDetailsView.getVehicleMoverWreckerBO().getPersonId() == null || supportingDetailsView.getVehicleMoverWreckerBO().getPersonId() == 0)) 
						{
							supportingDetailsView.setVehicleMoverBO(supportingDetailsView.getVehicleMoverWreckerBO());
						} 							

						System.err.println("before validation checks::"+StringUtils.isEmpty(supportingDetailsView.getWreckerVehicleBO().getPlateRegNo())+ ObjectUtils.objectToString(supportingDetailsView.getWreckerVehicleBO() ));
						if( /*(supportingDetailsView.getWreckerVehicleBO() == null || supportingDetailsView.getWreckerVehicleBO().getVehicleId() == null 
								|| supportingDetailsView.getWreckerVehicleBO().getVehicleId() == 0
								|| */
								StringUtils.isEmpty(supportingDetailsView.getWreckerVehicleBO().getPlateRegNo()) )
						{
							error = true;
							addErrorMessageWithParameter(context, "RequiredFields", "Plate Registration Number(Wrecker Vehicle)");
							System.err.println("plate no required::"+StringUtils.isEmpty(supportingDetailsView.getWreckerVehicleBO().getPlateRegNo())+ ObjectUtils.objectToString(supportingDetailsView.getWreckerVehicleBO() ));
							//clear values in the person object
							VehicleBO newVehicle = new VehicleBO();
							newVehicle.setPlateRegNo(supportingDetailsView.getWreckerVehicleBO().getPlateRegNo());
							supportingDetailsView.setWreckerVehicleBO(newVehicle);
							
						}else{
							if(StringUtils.isNotEmpty(supportingDetailsView.getWreckerVehicleBO().getPlateRegNo()) && 
									(!StringUtils.equalsIgnoreCase(supportingDetailsView.getWreckerVehicleBO().getPlateRegNo(), supportingDetailsView.getPrevWreckerPlateRegNo()))){
								addErrorMessageWithParameter(context, "NoPlateSearchPerformed","Wrecker Vehicle");
								VehicleBO newVehicle = new VehicleBO();
								newVehicle.setPlateRegNo(supportingDetailsView.getWreckerVehicleBO().getPlateRegNo());
								supportingDetailsView.setWreckerVehicleBO(newVehicle);
								error = true;
							}
						}
						
						//wrecker dln validations
						if(StringUtils.isBlank(supportingDetailsView.getVehicleMoverWreckerBO().getDlNo())){								
							//once field wrecker driver is empty ; then remove details
							//clear values in the person object
							PersonBOForRoadCompConverter mover = new PersonBOForRoadCompConverter();
							mover.setDlNo(supportingDetailsView.getVehicleMoverWreckerBO().getDlNo());
							supportingDetailsView.setVehicleMoverWreckerBO(mover);
							supportingDetailsView.setVehicleMoverBO(null);
							System.err.println("wrecker dln is empty");
							
						}else //once wrecker dln is not empty that ensure that person details have been retrieved
						if(!StringUtils.isBlank(supportingDetailsView.getVehicleMoverWreckerBO().getDlNo()) &&
								(supportingDetailsView.getVehicleMoverWreckerBO().getTrnNbr()==null 
								|| (supportingDetailsView.getVehicleMoverWreckerBO().getTrnNbr()!=null && !supportingDetailsView.getVehicleMoverWreckerBO().getDlNo().equals(supportingDetailsView.getVehicleMoverWreckerBO().getTrnNbr())))
								)
						{
							error = true;
							addErrorMessageWithParameter(context, "NoDLSearchPerformed","Wrecker Driver");
							//clear values in the person object
							PersonBOForRoadCompConverter mover = new PersonBOForRoadCompConverter();
							mover.setDlNo(supportingDetailsView.getVehicleMoverWreckerBO().getDlNo());
							supportingDetailsView.setVehicleMoverWreckerBO(mover);
							supportingDetailsView.setVehicleMoverBO(null);
							System.err.println("wrecker dln search must be performed");
							
						}

					} 
					else if (supportingDetailsView.getVehicleMoverType().equalsIgnoreCase("OT")) 
					{											
						if(supportingDetailsView.getVehicleMoverOTBO()==null || supportingDetailsView.getVehicleMoverOTBO().getDlNo() == null ||
								StringUtils.isBlank(supportingDetailsView.getVehicleMoverOTBO().getDlNo())){
						/*if (supportingDetailsView.getVehicleMoverOTBO() == null || supportingDetailsView.getVehicleMoverOTBO().getPersonId() == null 
								|| supportingDetailsView.getVehicleMoverOTBO().getPersonId() == 0) */
						
							error = true;
							addErrorMessageWithParameter(context, "RequiredFields", "Other Driver's Licence Number");
							//clear values in the person object
							PersonBOForRoadCompConverter otherMover = new PersonBOForRoadCompConverter();
							otherMover.setDlNo(supportingDetailsView.getVehicleMoverOTBO().getDlNo());
							supportingDetailsView.setVehicleMoverOTBO(otherMover);
							supportingDetailsView.setVehicleMoverBO(null);
						} 
						
						//dln not empty AND (trn[search not performed] is empty OR (trn is not empty  AND dln!=trn)) 
						else if(!StringUtils.isBlank(supportingDetailsView.getVehicleMoverOTBO().getDlNo()) &&
								(supportingDetailsView.getVehicleMoverOTBO().getTrnNbr()==null 
								|| (supportingDetailsView.getVehicleMoverOTBO().getTrnNbr()!=null && !supportingDetailsView.getVehicleMoverOTBO().getDlNo().equals(supportingDetailsView.getVehicleMoverOTBO().getTrnNbr())))
								){
							
							error = true;
							addErrorMessageWithParameter(context, "NoDLSearchPerformed","Other Driver");
							
							//clear values in the person object
							PersonBOForRoadCompConverter otherMover = new PersonBOForRoadCompConverter();
							otherMover.setDlNo(supportingDetailsView.getVehicleMoverOTBO().getDlNo());
							supportingDetailsView.setVehicleMoverOTBO(otherMover);
							supportingDetailsView.setVehicleMoverBO(null);
						}
						else{
							supportingDetailsView.setVehicleMoverBO(supportingDetailsView.getVehicleMoverOTBO());
						}

					} 
					else if (supportingDetailsView.getVehicleMoverType().equalsIgnoreCase("IE")) 
					{
						if (supportingDetailsView.getVehicleMoverITABO() == null || supportingDetailsView.getVehicleMoverITABO().getPersonId() == null 
								|| supportingDetailsView.getVehicleMoverITABO().getPersonId() == 0) 
						{
							error = true;
							addErrorMessageWithParameter(context, "RequiredFields", "ITA Examiner Driver");
						} 
						else 
						{
							supportingDetailsView.setVehicleMoverBO(supportingDetailsView.getVehicleMoverITABO());
						}

					} 
					else if (supportingDetailsView.getVehicleMoverType().equalsIgnoreCase("TA")) 
					{
						if (supportingDetailsView.getVehicleMoverTABO() == null || supportingDetailsView.getVehicleMoverTABO().getPersonId() == null 
								|| supportingDetailsView.getVehicleMoverTABO().getPersonId() == 0) 
						{
							error = true;
							addErrorMessageWithParameter(context, "RequiredFields", "TA Inspector Driver");
						} 
						else
						{
							supportingDetailsView.setVehicleMoverBO(supportingDetailsView.getVehicleMoverTABO());
						}

					} 
					else if (supportingDetailsView.getVehicleMoverType().equalsIgnoreCase("PO")) 
					{
						if (supportingDetailsView.getVehicleMoverPOBO() == null || supportingDetailsView.getVehicleMoverPOBO().getPersonId() == null 
								|| supportingDetailsView.getVehicleMoverPOBO().getPersonId() == 0) 
						{
							error = true;
							addErrorMessageWithParameter(context, "RequiredFields", "Police Officer Driver");
						} 
						else 
						{
							supportingDetailsView.setVehicleMoverBO(supportingDetailsView.getVehicleMoverPOBO());
						}

					}

				}

			}

			if (supportingDetailsView.isAllegationRequired()) {
				if (StringUtils.isBlank(supportingDetailsView.getAllegation())) {
					error = true;
					addErrorMessageWithParameter(context, "RequiredFields", "Allegation");
				}
			}
			if (supportingDetailsView.isDirectiveParamRequired()) {
				if (StringUtils.isBlank(supportingDetailsView.getDirectiveParam())) {
					error = true;
					addErrorMessageWithParameter(context, "RequiredFields", "Directive of Inspector");
				}
			}
			if (supportingDetailsView.isInspectorParamRequired()) {
				if (supportingDetailsView.getInspectorParam() == null || supportingDetailsView.getInspectorParam().getStaffId() == null) {
					error = true;
					addErrorMessageWithParameter(context, "RequiredFields", "Inspector Threatened");
				}
			}

			if (supportingDetailsView.isNoOfPassengersRequired()) {
				if (supportingDetailsView.getNoOfPassengers() == null) {
					error = true;
					addErrorMessageWithParameter(context, "RequiredFields", "No. of Passengers");
				}
			}

			if (recordOffenceOutcomeView.isSummonsSelected()) {

				if (supportingDetailsView.getCourtId() == null || supportingDetailsView.getCourtId() == 0) {
					error = true;
					addErrorMessageWithParameter(context, "RequiredFields", "Court");
				}
				if (supportingDetailsView.getCourtDateTime() == null)
				{
					error = true;
					addErrorMessageWithParameter(context, "RequiredFields", "Court Date");
				} 
				else 
				{
					try 
					{
						// System.err.println("roadCheckInitiateView.getOffenceDate(): " +
						// roadCheckInitiateView.getOffenceDate());
						// System.err.println("court date" +
						// returnCourtDateOnly(supportingDetailsView.getCourtDateTime()));
						getRoadOperationService().validateCourtDateRest(roadCheckInitiateView.getOffenceDate(),
								supportingDetailsView.getCourtDateTime(),"Offence Date");
						
						
					} catch (InvalidFieldException ie) {
						context.getMessageContext().addMessage(new MessageBuilder().error().defaultText(ie.getMessage()).build());

						error = true;
					} catch (RequiredFieldMissingException re) {
						context.getMessageContext().addMessage(new MessageBuilder().error().defaultText(re.getMessage()).build());
						error = true;
					}
				}
				
				if(supportingDetailsView.isIssueToOwner())
				{
					if(supportingDetailsView.getOwner() == null){
						error = true;
						addErrorMessageWithParameter(context, "RequiredFields", "Vehicle Owner");
					}/*else{
						System.err.println("Owner: "+supportingDetailsView.getOwner().getAddress());
					}*/
				}
			}

			/**Removed -- adding a witness is now optional see RFC-0004
			//if (recordOffenceOutcomeView.isWarningNoticeSelected()) {
			//	if (supportingDetailsView.getWitnessList().size() <= 0) {
			//		addErrorMessageWithParameter(context, "RequiredFields", "At least one Witness");
			//		error = true;
			//	}
			}*/
			
			if(roadCheckInitiateView.isVehicleInfoDifferent())
			{
				 if(! StringUtil.isSet(supportingDetailsView.getComment()))
				 {
					 addErrorMessageWithParameter(context, "RequiredFields", "Comment");
					 error = true;
					 
				 }
			}
			
			if (error == true) {
				return error();
			}

			List<ExcerptParamMappingBO> offenceParams = (List<ExcerptParamMappingBO>) context.getFlowScope().get("offenceParamList");
			supportingDetailsView.setOffenceParamMap(returnOffenceParamMap(offenceParams, context));

			roadCheckInitiateView.setCompleteSupportingDetails(true);
			return success();
		} catch (Exception e) {
			e.printStackTrace();
			return error();
		}

	}
	
	//TO BE REMOVED
	private XMLGregorianCalendar returnDateOnly(Date date) {
		XMLGregorianCalendar gregCourtDate = null;
		Date convertedDate = null;

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String courtDateString = DateUtils.formatDate("yyyy-MM-dd", date);
		try {
			convertedDate = dateFormat.parse(courtDateString);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			gregCourtDate = DateUtils.getXMLGregorianCalendar(convertedDate);
		} catch (DatatypeConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return gregCourtDate;

	}
	
	private Date returnDateOnlyDateType(Date date) {
		
		Date convertedDate = null;

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String courtDateString = DateUtils.formatDate("yyyy-MM-dd", date);
		try {
			convertedDate = dateFormat.parse(courtDateString);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}


		return convertedDate;

	}
	
	//TO BE REMOVED
	private XMLGregorianCalendar returnDateTimeInMinutesOnly(Date date) {
		XMLGregorianCalendar gregConvertDate = null;
		Date convertedDate = null;

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String convertDateString = DateUtils.formatDate("yyyy-MM-dd HH:mm", date);
		try {
			convertedDate = dateFormat.parse(convertDateString);
		} catch (ParseException e1) {
			logger.error("Road Check",e1);
		}
		try {
			gregConvertDate = DateUtils.getXMLGregorianCalendar(convertedDate);
		} catch (DatatypeConfigurationException e1) {
			logger.error("Road Check",e1);
		}

		return gregConvertDate;

	}
	
	private Date returnDateTimeInMinutesOnlyDateType(Date date) {
		//XMLGregorianCalendar gregConvertDate = null;
		Date convertedDate = null;

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String convertDateString = DateUtils.formatDate("yyyy-MM-dd HH:mm", date);
		try {
			convertedDate = dateFormat.parse(convertDateString);
		} catch (ParseException e1) {
			logger.error("Road Check",e1);
		}
		

		return convertedDate;

	}

	public Event proceedWithoutQuery(RequestContext context) {
		try {
			RoadCheckInitiateView roadCheckInitiateView = (RoadCheckInitiateView) context.getFlowScope().get("initiateView");

			if (validatePerformOrProceed(context)) {
				return error();
			}

			// Update Compliance Record
			ComplianceBO complianceBO = new ComplianceBO();
			complianceBO = pupulateComplianceBO(roadCheckInitiateView, complianceBO);
			complianceBO.setComplianceId(roadCheckInitiateView.getComplianceId());
			try {
				getRoadCompliancyService().saveCompliance(complianceBO);
			} catch (ErrorSavingException e) {
				addErrorMessage(context, "SystemError");
				e.printStackTrace();
			} catch (RequiredFieldMissingException e) {
				addErrorMessage(context, "SystemError");
				e.printStackTrace();
			}

			if (roadCheckInitiateView.isRoadCheckProceeded() == false) {
				initiateOffenceOutcomes(context);
				roadCheckInitiateView.setRoadCheckProceeded(true);
			}
			roadCheckInitiateView.setCompleteInitiate(true);
			
			RecordOffenceOutcomeView recordOffenceOutcomeView = (RecordOffenceOutcomeView) context.getFlowScope().get("recordOffenceOutcomeView");
			
			outcomeActionToTake(context, recordOffenceOutcomeView);
			
			context.getFlowScope().put("recordOffenceOutcomeView", recordOffenceOutcomeView);
			
			return success();
		} catch (Exception e) {
			addErrorMessage(context, "SystemError");
			e.printStackTrace();
			return error();
		}

	}



	public Event performQueries(RequestContext context) {
		Calendar today = new GregorianCalendar();
		try {
			RoadCheckInitiateView roadCheckInitiateView = (RoadCheckInitiateView) context.getFlowScope().get("initiateView");
			RecordOffenceOutcomeView recordOffenceOutcomeView = (RecordOffenceOutcomeView) context.getFlowScope().get("recordOffenceOutcomeView");

			Boolean isHandHeld = this.isHandHeld(context);
			
			if (validatePerformOrProceed(context)) {
				return error();
			}
			// remove this
			/* if(roadCheckInitiateView.getActivityType().equalsIgnoreCase("S")){ initiateOffenceOutcomes(context);
			 * return success(); } */

			// Update Compliance Record
			ComplianceBO complianceBO = new ComplianceBO();
			complianceBO = pupulateComplianceBO(roadCheckInitiateView, complianceBO);
			complianceBO.setComplianceId(roadCheckInitiateView.getComplianceId());
			getRoadCompliancyService().saveCompliance(complianceBO);


			/*if (roadCheckInitiateView.isDlQuery() && !(roadCheckInitiateView.getDlNo().equalsIgnoreCase(roadCheckInitiateView.getPrevDlNo()))) {*/
				if (roadCheckInitiateView.isDlQuery()) {	

				try {
					// System.err.println("isDlQuery");
					roadCheckInitiateView.setPrevDlNo(roadCheckInitiateView.getDlNo());
					roadCheckInitiateView.setNoDLFound(false);
					DLCheckResultBO DLCheckResultBO = new DLCheckResultBO();
					DLCheckResultBO.setComplianceID(roadCheckInitiateView.getComplianceId());
					DLCheckResultBO.setDlNo(roadCheckInitiateView.getDlNo());
					DLCheckResultBO.setCurrentUserName(getRomsLoggedInUser().getUsername()); // Change this

					DLCheckResultBO = getRoadCompliancyService().performDriversLicenceCheck(DLCheckResultBO);

					if (DLCheckResultBO == null) {
						roadCheckInitiateView.setNoDLFound(true);
						roadCheckInitiateView.setDlQueryDone(true);
						
						roadCheckInitiateView.setDlCkFl(true);
						
						// addErrorMessageWithParameter(context, "NoDLRecordfound",
						// roadCheckInitiateView.getRoadLicNo());
						// return error();
					} else {
						if (DLCheckResultBO.getPhotograph() != null) {
							// String noCache = "&nocache=" + Math.random();
							// recordOffenceOutcomeView.setDlPhotoURL("/PhotoServlet?dlNo=" + DLCheckResultBO.getDlNo()
							// + "&serviceType=" + "DL"+ noCache);

							String encodedString = Base64.encode(DLCheckResultBO.getPhotograph());
							recordOffenceOutcomeView.setDlPhotoURL("data:image/jpg;base64," + encodedString);

						}
						
						/*if(DLCheckResultBO.getExpiryDate()!=null&& today.after(DLCheckResultBO.getExpiryDate().toGregorianCalendar())){
							roadCheckInitiateView.setDlCkFl(true);
						}*/

					}
					
					context.getFlowScope().put("dlResult", DLCheckResultBO);
					
					
					
					roadCheckInitiateView.setDlQueryDone(true);
				}

				catch (NoRecordFoundException e) 
				{

					roadCheckInitiateView.setNoDLFound(true);
					roadCheckInitiateView.setDlQueryDone(true);

				}

				catch (Exception e) {
					addWarningMessageWithParameter(context, "QueryResultError", "Driver's Licence");
					roadCheckInitiateView.setDlQuery(false);
					e.printStackTrace();
				}

			}

			//if (roadCheckInitiateView.isBadgeQuery() && !(roadCheckInitiateView.getBadgeNo().equalsIgnoreCase(roadCheckInitiateView.getPrevBadgeNo()))) {
			if (roadCheckInitiateView.isBadgeQuery()) {
				// System.err.println("isBadgeQuery");
				//BadgeCheckResultBO badgeCheckResultBO = new BadgeCheckResultBO();
				try {
					roadCheckInitiateView.setPrevBadgeNo(roadCheckInitiateView.getBadgeNo());
					roadCheckInitiateView.setNoBadgeFound(false);
					BadgeCheckResultBO badgeCheckResultBO = new BadgeCheckResultBO();
					badgeCheckResultBO.setComplianceID(roadCheckInitiateView.getComplianceId());
					badgeCheckResultBO.setBadgeNumber(roadCheckInitiateView.getBadgeNo());
					badgeCheckResultBO.setBadgeType(roadCheckInitiateView.getRoleObserved());
					badgeCheckResultBO.setCurrentUserName(getRomsLoggedInUser().getUsername()); // Change this

					badgeCheckResultBO = getRoadCompliancyService().performBadgeCheck(badgeCheckResultBO);

					if (badgeCheckResultBO == null) {
						roadCheckInitiateView.setNoBadgeFound(true);
						roadCheckInitiateView.setBadgeQueryDone(true);
						roadCheckInitiateView.setbGCkFl(true);
						// addErrorMessageWithParameter(context, "NoBadgeRecordfound",
						// roadCheckInitiateView.getRoadLicNo());
						// return error();
					} else {

						// String noCache = "&nocache=" + Math.random();

						// recordOffenceOutcomeView.setBadgePhotoURL("/PhotoServlet?badgeNo=" +
						// badgeCheckResultBO.getBadgeNumber() + "&badgeType=" +
						// badgeCheckResultBO.getBadgeTypeDescription()+ "&serviceType=" + "BIMS" +noCache);

						String encodedString = Base64.encode(badgeCheckResultBO.getPhotograph());
						recordOffenceOutcomeView.setBadgePhotoURL("data:image/jpg;base64," + encodedString);

//						context.getFlowScope().put("recordOffenceOutcomeView", recordOffenceOutcomeView);
						context.getFlowScope().put("badgeResult", badgeCheckResultBO);
						
						/*if(badgeCheckResultBO.getExpiryDate()!=null&& today.after(badgeCheckResultBO.getExpiryDate().toGregorianCalendar())){
							roadCheckInitiateView.setbGCkFl(true);
						}*/
					}

					roadCheckInitiateView.setBadgeQueryDone(true);
				}

				catch (NoRecordFoundException e) {
					// TODO Auto-generated catch block
					// addErrorMessageWithParameter(context, "NoBadgeRecordfound", roadCheckInitiateView.getBadgeNo()+
					// "");
					roadCheckInitiateView.setNoBadgeFound(true);
					roadCheckInitiateView.setBadgeQueryDone(true);
					// return error();
				} catch (Exception e) {
					addWarningMessageWithParameter(context, "QueryResultError", "Badge");
					roadCheckInitiateView.setBadgeQuery(false);
					e.printStackTrace();
				}

			}

			/*if (roadCheckInitiateView.isCitationHistQuery() && (!(roadCheckInitiateView.getDlNo().equalsIgnoreCase(roadCheckInitiateView.getPrevDlNoCH())) || !(roadCheckInitiateView.getPlateRegNo().equalsIgnoreCase(roadCheckInitiateView.getPrevPlateRegNoCH())))) {*/
			if (roadCheckInitiateView.isCitationHistQuery()) {
				// System.err.println("isCitationHistQuery");
				try {
					CitationCheckResultBO citationCheckResultBO = new CitationCheckResultBO();
					citationCheckResultBO.setComplianceID(roadCheckInitiateView.getComplianceId());
					citationCheckResultBO.setTrnNbr(roadCheckInitiateView.getTrn());
					citationCheckResultBO.setDlNo(roadCheckInitiateView.getDlNo());
					citationCheckResultBO.setRegPlateNo(roadCheckInitiateView.getPlateRegNo());
					citationCheckResultBO.setCurrentUserName(getRomsLoggedInUser().getUsername()); // Change this
					
					citationCheckResultBO.setIncludeTTMSResults(roadCheckInitiateView.getIncludeTTMSResults());
					
					citationCheckResultBO = getRoadCompliancyService().performCitationHistoryCheck(citationCheckResultBO,isHandHeld);

					roadCheckInitiateView.setNoCitationFound(false);
					roadCheckInitiateView.setPrevDlNoCH(roadCheckInitiateView.getDlNo());
					roadCheckInitiateView.setPrevPlateRegNoCH(roadCheckInitiateView.getPlateRegNo());

					// Set Labels
					String personLabel = "";
					NameUtil util = new NameUtil();
					String fullName = util.getLastNameCapsFirstNameMiddleName(roadCheckInitiateView.getFirstName(), roadCheckInitiateView.getLastName(), roadCheckInitiateView.getMiddleName());
					personLabel = "Offences Related to PERSON with Name: " + fullName;

					if (StringUtils.isNotBlank(roadCheckInitiateView.getTrn())) {
						personLabel = personLabel + "; TRN: " + roadCheckInitiateView.getTrn();
					}
					if (StringUtils.isNotBlank(roadCheckInitiateView.getDlNo())) {
						personLabel = personLabel + "; DL No.: " + roadCheckInitiateView.getDlNo();
					}
					roadCheckInitiateView.setCitationPersonLabel(personLabel);

					roadCheckInitiateView.setCitationVehicleLabel("Offences Related to MOTOR VEHICLE with Plate No.: " + roadCheckInitiateView.getPlateRegNo());

					if (citationCheckResultBO == null) {
						roadCheckInitiateView.setCitationHistQueryDone(true);
						roadCheckInitiateView.setNoCitationFound(true);
						// addErrorMessageWithParameter(context, "NoCitationRecordfound",
						// roadCheckInitiateView.getRoadLicNo());
						// return error();
					} else {

						List<CitationOffenceBO> personOffences = new ArrayList<CitationOffenceBO>();
						List<CitationOffenceBO> vehicleOffences = new ArrayList<CitationOffenceBO>();

//						if (citationCheckResultBO.getCitationOffences() != null) {
//
//							for (CitationOffenceBO citOff : citationCheckResultBO.getCitationOffences()) {
//								if (citOff.getDlNo() != null) {
//									if (citOff.getDlNo().equalsIgnoreCase(roadCheckInitiateView.getDlNo())) {
//										personOffences.add(citOff);
//									}
//								} else if (citOff.getTrnNbr() != null) {
//									if (citOff.getTrnNbr().equalsIgnoreCase(roadCheckInitiateView.getTrn())) {
//										personOffences.add(citOff);
//									}
//								}
//
//								if (citOff.getPlateRegNo() != null) {
//									if (citOff.getPlateRegNo().equalsIgnoreCase(roadCheckInitiateView.getPlateRegNo())) {
//										vehicleOffences.add(citOff);
//									}
//								}
//							}
//						}

						personOffences = citationCheckResultBO.getCitationOffences();
						vehicleOffences = citationCheckResultBO.getCitationOffencesVehicle();
						
						context.getFlowScope().put("citResult", citationCheckResultBO);
						context.getFlowScope().put("ttmsOffences", sortTrafficTicketOffence(citationCheckResultBO.getTrafficTickets()));
						context.getFlowScope().put("taVehicleOffences", sortCitationOffence(vehicleOffences));
						context.getFlowScope().put("taPersonOffences", sortCitationOffence(personOffences));

						// set person offences

					}
					// Need to add data tables for both tickets and Ta Offences
					// context.getFlowScope().put("badgeResult", citationCheckResultBO);

					roadCheckInitiateView.setCitationHistQueryDone(true);

				}

				catch (Exception e) {
					// TODO Auto-generated catch block
					addWarningMessageWithParameter(context, "QueryResultError", "Citation History");
					roadCheckInitiateView.setCitationHistQuery(false);
					e.printStackTrace();
					// return error();
				}

			}
			if (roadCheckInitiateView.isRoadCheckProceeded() == false) {
				initiateOffenceOutcomes(context);
				roadCheckInitiateView.setRoadCheckProceeded(true);
			}

			roadCheckInitiateView.setPerformQuery(true);
			roadCheckInitiateView.setCompleteInitiate(true);
			
			
			outcomeActionToTake(context,recordOffenceOutcomeView);
			context.getFlowScope().put("recordOffenceOutcomeView", recordOffenceOutcomeView);
						
			return success();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			addErrorMessage(context, "SystemError");
			e.printStackTrace();
			return error();
		}
	}

	private void outcomeActionToTake(RequestContext context,RecordOffenceOutcomeView recordOffenceOutcomeView){
		//RecordOffenceOutcomeView recordOffenceOutcomeView = (RecordOffenceOutcomeView) context.getFlowScope().get("recordOffenceOutcomeView");
		RoadCheckInitiateView roadCheckInitiateView = (RoadCheckInitiateView) context.getFlowScope().get("initiateView");
		
		Date today = Calendar.getInstance().getTime();
				
		VehicleCheckResultBO mvResult = (VehicleCheckResultBO) context.getFlowScope().get("mvResult");
		
		DLCheckResultBO dlResult = (DLCheckResultBO)context.getFlowScope().get("dlResult");
		
		BadgeCheckResultBO badgeResult = (BadgeCheckResultBO)context.getFlowScope().get("badgeResult");
		
		RoadLicCheckResultBO rlResult = (RoadLicCheckResultBO)context.getFlowScope().get("rlResult");
		
		roadCheckInitiateView.setbGCkFl(false);
		roadCheckInitiateView.setDlCkFl(false);
		roadCheckInitiateView.setRlCkFl(false);
		roadCheckInitiateView.setRlInCkFl(false);
		roadCheckInitiateView.setRlFtCkFl(false);
		roadCheckInitiateView.setmVCkFl(false);
		roadCheckInitiateView.setInCkFl(false);
		roadCheckInitiateView.setfTCkFl(false);
		roadCheckInitiateView.setRlExpire(false);
		
			recordOffenceOutcomeView.setMvActionToBeTaken(null);
			context.getFlowScope().put("mvOffenceOutcomes", getInitialOffenceList());
		
			context.getFlowScope().put("dlOffenceOutcomes", getInitialOffenceList());
			recordOffenceOutcomeView.setDlActionToBeTaken(null);
		
			context.getFlowScope().put("bdgOffenceOutcomes", getInitialOffenceList());
			recordOffenceOutcomeView.setBdgActionToBeTaken(null);
		
			context.getFlowScope().put("rlOffenceOutcomes", getInitialOffenceList());
			recordOffenceOutcomeView.setRlActionToBeTaken(null);
		
		
		
		
		
		if(roadCheckInitiateView.isBadgeQuery())
		{
			if(roadCheckInitiateView.isBadgeQueryDone()&&roadCheckInitiateView.isNoBadgeFound()){
				roadCheckInitiateView.setbGCkFl(true);
			}else{
				
				if(badgeResult==null)
						roadCheckInitiateView.setNoBadgeFound(true);
				
				if(badgeResult==null||
					badgeResult.getExpiryDate()==null ||
					today.after(badgeResult.getExpiryDate()))
				{				
					roadCheckInitiateView.setbGCkFl(true);
				}
		}	}
		
		if(roadCheckInitiateView.isDlQuery())			
		{
			if(roadCheckInitiateView.isDlQueryDone()&&roadCheckInitiateView.isNoDLFound()){
				roadCheckInitiateView.setDlCkFl(true);
			}
			
			if(dlResult==null)
					roadCheckInitiateView.setNoDLFound(true);
			
			if(dlResult==null||
				dlResult.getExpiryDate()==null||
				today.after(dlResult.getExpiryDate()))
			{
				roadCheckInitiateView.setDlCkFl(true);
			}
		}
		
				
		try{
			if(rlResult!=null)
			{
				
				if(rlResult.getLicenceNo()==null)
				{
						roadCheckInitiateView.setRlCkFl(true);
				}
								
				if(rlResult.getExpiryDate()==null|| 
						 today.after(rlResult.getExpiryDate()))
				{
						roadCheckInitiateView.setRlCkFl(true);
						roadCheckInitiateView.setRlExpire(true);
				}
				
				try {
					if (today.after(rlResult.getInsuranceExpDate())) {
						roadCheckInitiateView.setRlInCkFl(true);
					}
				} catch (Exception ex) {
					logger.info("Insurance exp date is null.");
				}
	
				try {
					if (today.after(rlResult.getFitnessExpDate())) {
						roadCheckInitiateView.setRlFtCkFl(true);
					}
				} catch (Exception ex) {
					logger.info("Fitness exp date is null.");
				}
				
				
			}else{
				roadCheckInitiateView.setRlCkFl(true);
			}
		}catch(Exception ex){
			ex.printStackTrace();
			roadCheckInitiateView.setRlCkFl(true);
		}
		if(mvResult!=null){
			if(mvResult.getMvrcExpiryDate()!=null&& today.after(mvResult.getMvrcExpiryDate())){
				roadCheckInitiateView.setmVCkFl(true);				
			}
			
			if(mvResult.getInsuranceInfo()!=null&&today.after(mvResult.getInsuranceInfo().getExpiryDate())){
				//roadCheckInitiateView.setmVCkFl(true);
				roadCheckInitiateView.setInCkFl(true);
			}
			
			if(mvResult.getFitnessInfo()!=null&&today.after(mvResult.getFitnessInfo().getExpiryDate())){
				//roadCheckInitiateView.setmVCkFl(true);
				roadCheckInitiateView.setfTCkFl(true);
			}			
		
		}
		
		/*if(roadCheckInitiateView.ismVCkFl())
		{
			recordOffenceOutcomeView.setMvActionToBeTaken(Constants.ActionToTake.RECORD_OFFENCE);
			context.getFlowScope().put("mvOffenceOutcomes", getInitialOffenceList());
		}*/
		
		if(roadCheckInitiateView.isNoDLFound()){
			logger.info("DL result is null");
			context.getFlowScope().put("dlOffenceOutcomes", getInitialOffenceList());
			recordOffenceOutcomeView.setDlActionToBeTaken(Constants.ActionToTake.RECORD_OFFENCE);
		}
		if(roadCheckInitiateView.isbGCkFl()){
			context.getFlowScope().put("bdgOffenceOutcomes", getInitialOffenceList());
			recordOffenceOutcomeView.setBdgActionToBeTaken(Constants.ActionToTake.RECORD_OFFENCE);
		}
		
		if(roadCheckInitiateView.isRlCkFl())
		{
			//Conductor cannot be charge ROMS1.0U-004
			if(!Constants.PersonRole.CONDUCTOR.equalsIgnoreCase(roadCheckInitiateView.getRoleObserved())
					&&!Constants.PersonRole.OTHER.equalsIgnoreCase(roadCheckInitiateView.getRoleObserved())){
				context.getFlowScope().put("rlOffenceOutcomes", getInitialOffenceList());
				recordOffenceOutcomeView.setRlActionToBeTaken(Constants.ActionToTake.RECORD_OFFENCE);
			}
			else{
				roadCheckInitiateView.setRlCkFl(false);
			}
		}
		
		
		context.getFlowScope().put("mvResult",mvResult);
		
		context.getFlowScope().put("dlResult",dlResult);
		
		context.getFlowScope().put("badgeResult",badgeResult);
		
		context.getFlowScope().put("rlResult",rlResult);
		
		 context.getFlowScope().put("initiateView",roadCheckInitiateView);
		
	}
	
	private List<OffenceOutcomeView> getInitialOffenceList(){
		RequestContext context = RequestContextHolder.getRequestContext();
		
		RoadCheckInitiateView roadCheckInitiateView = (RoadCheckInitiateView) context.getFlowScope().get("initiateView");
		
		
		List<OffenceOutcomeView> offenceOutcomes1 = new ArrayList<OffenceOutcomeView>();
		OffenceOutcomeView offenceOutcomeView1 = new OffenceOutcomeView();
		offenceOutcomeView1.setOffenceId(Constants.DEFAULT_OFFENCE.intValue());
		

//		if( "U".equalsIgnoreCase(roadCheckInitiateView.getActivityType())){
//			offenceOutcomeView1.setWarnForProsecution(true);
//		}
		
		//offenceOutcomes1.add(offenceOutcomeView1);
		
		
		return offenceOutcomes1;
	}
	
	public boolean isCanProcedToReview(){
				
		RequestContext context = RequestContextHolder.getRequestContext();
		
		//OffenceOutcomeView offenceOutcomeView = null;
		
		try{	
		List<OffenceOutcomeView> offenceOutcomes1 = null;
		
		offenceOutcomes1 = 	(List<OffenceOutcomeView>) context.getFlowScope().get("mvOffenceOutcomes");
		
		if( offenceOutcomes1 !=null && isInitialOffenceContent(offenceOutcomes1)){
			return true;			
		}
				
		offenceOutcomes1 = 	(List<OffenceOutcomeView>) context.getFlowScope().get("dlOffenceOutcomes");
		
		if( offenceOutcomes1 !=null && isInitialOffenceContent(offenceOutcomes1)){
			return true;			
		}
		
		offenceOutcomes1 = 	(List<OffenceOutcomeView>) context.getFlowScope().get("bdgOffenceOutcomes");
		
		if( offenceOutcomes1 !=null && isInitialOffenceContent(offenceOutcomes1)){
			return true;			
		}
		
		offenceOutcomes1 = 	(List<OffenceOutcomeView>) context.getFlowScope().get("rlOffenceOutcomes");
		
		if(offenceOutcomes1 !=null && isInitialOffenceContent(offenceOutcomes1)){
			return true;			
		}
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return false;
	}
	
	private boolean isInitialOffenceContent(List<OffenceOutcomeView> offenceOutcomes1){
		//OffenceOutcomeView offenceOutcomeView = null;
		
		boolean isDefault = false;
		
		if(!offenceOutcomes1.isEmpty()){
			for(OffenceOutcomeView offenceOutcomeView: offenceOutcomes1){
				
				if (offenceOutcomeView.getOffenceId()!=null&&offenceOutcomeView.getOffenceId()== Constants.DEFAULT_OFFENCE.intValue() ){
					isDefault =  true;
					break;
				}
			}
		}
		
		
		return isDefault;
	}
	
	
	private List<CitationOffenceBO> sortCitationOffence(List<CitationOffenceBO> citationOffenceBOs) {

		Collections.sort(citationOffenceBOs, Collections.reverseOrder(new Comparator<CitationOffenceBO>() {
			public int compare(CitationOffenceBO result1, CitationOffenceBO result2) {
				return result1.getOffenceDateTime().compareTo(result2.getOffenceDateTime());
			}
		}));

		return citationOffenceBOs;
	}

	private List<TrafficTicket> sortTrafficTicketOffence(List<TrafficTicket> trafficTickets) {

		Collections.sort(trafficTickets, Collections.reverseOrder(new Comparator<TrafficTicket>() {
			public int compare(TrafficTicket result1, TrafficTicket result2) {
				return result1.getIssueDate().compareTo(result2.getIssueDate());
			}
		}));

		return trafficTickets;
	}

	public void startCheck(SelectEvent event) {

		// StrategyBO selected = (StrategyBO)event.getObject();;
		// System.err.println("Row selected"+selected.getStrategyDescription());
	}

	public Event getRoadCheckSearchResults(RequestContext context) {
		Boolean pass = true;
		logger.info("In getRoadCheckSearchResults UI function.");
		RoadCompliancy roadCheckProxy = this.getRoadCompliancyService();

		RoadCompliancyCriteriaBO roadCompCriteria = (RoadCompliancyCriteriaBO) context.getFlowScope().get("criteria");

		StaffUserMappingBO staffMapping = (StaffUserMappingBO) context.getFlowScope().get("staffBOForSearch");

		if (staffMapping != null)
			roadCompCriteria.setStaffID(staffMapping.getStaffId());
		else
			roadCompCriteria.setStaffID(null);

		DataTableMemory dataTable = null;

		try {

			if (!this.isFilterAdded(roadCompCriteria)) {

				//this.addErrorMessage(context, "AtleastOneSearchCriteriaReq");
				this.addErrorMessageText(context, "Please enter at least one of the following: Road Check Date, Operation Name or Operation Date.");
				
				return success();
			}

			/*if (!this.isBothDatesAdded(roadCompCriteria, context)) {
				//this.addErrorMessageText(context, "Both Operation dates are needed for the search.");

				//return error();
				pass = false;
			}*/
			
			// test offence dates
			pass = validateStartNEndDate(context, roadCompCriteria.getRoadCheckStartDateRange(), 
					roadCompCriteria.getRoadCheckEndDateRange(), "Road Check", false) && pass;
			/*
			if (roadCompCriteria.getRoadCheckStartDateRange() != null && roadCompCriteria.getRoadCheckEndDateRange() != null && roadCompCriteria.getRoadCheckEndDateRange().compare(roadCompCriteria.getRoadCheckStartDateRange()) == DatatypeConstants.LESSER) {
				this.addErrorMessageWithParameter(context, "EndDateBeforeStartDate", "Road Check");

				//return error();
				pass = false;
			}

			
			if(roadCompCriteria.getRoadCheckStartDateRange() != null
					&&roadCompCriteria.getRoadCheckEndDateRange() != null){
				if(!BaseServiceAction.validateDateRangeLimit
						(DateUtils.toDate(roadCompCriteria.getRoadCheckStartDateRange()),
								DateUtils.toDate(roadCompCriteria.getRoadCheckEndDateRange()))){
					this.addErrorMessageWithParameter(context,
							"DateRangeLimit", "Road Check");

					//return error();
					pass = false;
				}
			}
			*/
			pass = validateStartNEndDate(context, roadCompCriteria.getActualStartDateRange(), 
					roadCompCriteria.getActualEndDateRange(), "Operation", false) && pass;
			

			/*
			if (roadCompCriteria.getActualStartDateRange() != null && roadCompCriteria.getActualEndDateRange() != null && roadCompCriteria.getActualEndDateRange().compare(roadCompCriteria.getActualStartDateRange()) == DatatypeConstants.LESSER) {
				this.addErrorMessageWithParameter(context, "EndDateBeforeStartDate", "Operation");

				//return error();
				pass = false;
			}
			
			if(roadCompCriteria.getActualStartDateRange() != null
					&& roadCompCriteria.getActualEndDateRange() != null){
				if(!BaseServiceAction.validateDateRangeLimit
						(DateUtils.toDate(roadCompCriteria.getActualStartDateRange()),
								DateUtils.toDate(roadCompCriteria.getActualEndDateRange()))){
					this.addErrorMessageWithParameter(context,
							"DateRangeLimit", "Operation");

					//return error();
					pass = false;
				}
			}*/

			if(pass){
				List<ComplianceBO> compList = roadCheckProxy.lookupRoadCompliance(roadCompCriteria);
	
				dataTable = new DataTableMemory(compList, ((DataTableMemory) context.getFlowScope().put("dataTable", dataTable)).rowsPerPage, compList.size(), "Road Checks");
	
				dataTable.setSortOrder("desc");
	
				dataTable.setSortBy("Created");
	
				context.getFlowScope().put("dataTable", dataTable);
			}

		} catch (NoRecordFoundException exe) {
			//exe.printStackTrace();
			logger.error("Road Check", exe);
			dataTable = new DataTableMemory(new ArrayList<RoadOperationDetailsSumary>(), 10);
			context.getFlowScope().put("dataTable", dataTable);

			this.addErrorMessage(context, "Norecordsfound");

			return success(exe);
		} catch (Exception e) {

			dataTable = new DataTableMemory(new ArrayList<RoadOperationDetailsSumary>(), 10);
			context.getFlowScope().put("dataTable", dataTable);

			this.addErrorMessage(context, "SystemError");

			//e.printStackTrace();
			logger.error("Road Check", e);
			return success();
		}
		
		if(pass)
			return success();
		else
			return error();
	}

	private boolean isFilterAdded(RoadCompliancyCriteriaBO roadCheckSearchCrit) {
		/*if (StringUtils.isEmpty(roadCheckSearchCrit.getTrn()))
			if (StringUtils.isEmpty(roadCheckSearchCrit.getFirstName() + roadCheckSearchCrit.getLastName()))*/
				if (StringUtils.isEmpty(roadCheckSearchCrit.getOperationName()))
					/*if (StringUtils.isEmpty(roadCheckSearchCrit.getStaffID()))*/
						if (roadCheckSearchCrit.getActualStartDateRange() == null && roadCheckSearchCrit.getActualEndDateRange() == null)
							if (roadCheckSearchCrit.getRoadCheckStartDateRange() == null && roadCheckSearchCrit.getRoadCheckEndDateRange() == null)
								/*if (StringUtils.isEmpty(roadCheckSearchCrit.getStatus()))
									if (StringUtils.isEmpty(roadCheckSearchCrit.getRegion()))*/
										return false;

		return true;

	}

	private boolean isBothDatesAdded(RoadCompliancyCriteriaBO roadCheckSearchCrit, RequestContext context) {
		if ((roadCheckSearchCrit.getActualStartDateRange() != null && roadCheckSearchCrit.getActualEndDateRange() == null) || (roadCheckSearchCrit.getActualStartDateRange() == null && roadCheckSearchCrit.getActualEndDateRange() != null)) {
			this.addErrorMessageText(context, "Both Operation dates are needed for the search.");
			return false;
		}else if ((roadCheckSearchCrit.getRoadCheckStartDateRange() != null && roadCheckSearchCrit.getRoadCheckEndDateRange() == null) || (roadCheckSearchCrit.getRoadCheckStartDateRange() == null && roadCheckSearchCrit.getRoadCheckEndDateRange() != null)) {
			this.addErrorMessageText(context, "Both Road Check dates are needed for the search.");
			return false;
		}else
			return true;
	}

	/* Paging function for road check table details. */

	public void changePageSize(ValueChangeEvent e) {
		RequestContext requestContext = RequestContextHolder.getRequestContext();
		RequestControlContext context = (RequestControlContext) requestContext;

		DataTableMemory dataTable = (DataTableMemory) context.getFlowScope().get("dataTable");

		dataTable.changePageSize((Integer) e.getNewValue());

	}

	@SuppressWarnings("unchecked")
	public void sortBy(ValueChangeEvent e) {

		if (e != null && e.getNewValue() == null)
			return;

		RequestContext requestContext = RequestContextHolder.getRequestContext();
		RequestControlContext context = (RequestControlContext) requestContext;

		final String sortOrder = context.getFlowScope().getString("sortOrder");

		final DataTableMemory dataTable = (DataTableMemory) context.getFlowScope().get("dataTable");

		if (dataTable == null || dataTable.getDataList() == null || dataTable.getDataList().size() < 1)
			return;

		if (dataTable.getDataList().get(0).getClass().equals(ComplianceBO.class)) {
			List<ComplianceBO> complianceBOList = dataTable.getDataList();

			if (e != null ? ((String) e.getNewValue()).trim().equalsIgnoreCase("Operation Name") : dataTable.getSortBy().trim().equalsIgnoreCase("Operation Name")) {
				Collections.sort(complianceBOList, new Comparator<ComplianceBO>() {
					@Override
					public int compare(ComplianceBO a, ComplianceBO b) {
						int pos = (a.getRoadOperationBO().getOperationName().trim().compareToIgnoreCase(b.getRoadOperationBO().getOperationName().trim()));

						if (dataTable.getSortOrder().equalsIgnoreCase("desc")) {
							if (pos != 0)
								pos = pos * -1;
						}

						return pos;

					}
				});

			} else if (e != null ? ((String) e.getNewValue()).trim().equalsIgnoreCase("Offender Name") : dataTable.getSortBy().trim().equalsIgnoreCase("Offender Name")) {

				Collections.sort(complianceBOList, new Comparator<ComplianceBO>() {

					@Override
					public int compare(ComplianceBO a, ComplianceBO b) {
						int pos = (a.getPerson().getLastName().trim().compareToIgnoreCase(b.getPerson().getLastName()));

						if (dataTable.getSortOrder().equalsIgnoreCase("desc")) {
							if (pos != 0)
								pos = pos * -1;
						}

						return pos;
					}
				});

			} else if (e != null ? ((String) e.getNewValue()).trim().equalsIgnoreCase("Created") : dataTable.getSortBy().trim().equalsIgnoreCase("Created")) {

				Collections.sort(complianceBOList, new Comparator<ComplianceBO>() {

					@Override
					public int compare(ComplianceBO a, ComplianceBO b) {

						int pos = a.getCreatedDate().compareTo(b.getCreatedDate());

						if (dataTable.getSortOrder().equalsIgnoreCase("desc")) {
							if (pos != 0)
								pos = pos * -1;
						}

						return pos;
					}
				});

			} else if (e != null ? ((String) e.getNewValue()).trim().equalsIgnoreCase("Operation Start Date") : dataTable.getSortBy().trim().equalsIgnoreCase("Operation Start Date")) {

				Collections.sort(complianceBOList, new Comparator<ComplianceBO>() {

					@Override
					public int compare(ComplianceBO a, ComplianceBO b) {
						int pos = 0;

						if ((a.getRoadOperation() != null && a.getRoadOperation() > 0) && (b.getRoadOperation() != null && b.getRoadOperation() > 0))
							pos = a.getRoadOperationBO().getScheduledStartDate().compareTo(b.getRoadOperationBO().getScheduledStartDate());

						if (dataTable.getSortOrder().equalsIgnoreCase("desc")) {
							if (pos != 0)
								pos = pos * -1;
						}

						return pos;
					}
				});

			} else if (e != null ? ((String) e.getNewValue()).trim().equalsIgnoreCase("Status") : dataTable.getSortBy().trim().equalsIgnoreCase("Status")) {

				Collections.sort(complianceBOList, new Comparator<ComplianceBO>() {

					@Override
					public int compare(ComplianceBO a, ComplianceBO b) {

						int pos = a.getStatusId().trim().compareToIgnoreCase(b.getStatusId().trim());

						if (dataTable.getSortOrder().equalsIgnoreCase("desc")) {
							if (pos != 0)
								pos = pos * -1;
						}

						return pos;
					}
				});

			} else if (e != null ? ((String) e.getNewValue()).trim().equalsIgnoreCase("Road Check Date") : dataTable.getSortBy().trim().equalsIgnoreCase("Road Check Date")) {

				Collections.sort(complianceBOList, new Comparator<ComplianceBO>() {

					@Override
					public int compare(ComplianceBO a, ComplianceBO b) {
						int pos = 0;

						pos = a.getComplianceDate().compareTo(b.getComplianceDate());

						if (dataTable.getSortOrder().equalsIgnoreCase("desc")) {
							if (pos != 0)
								pos = pos * -1;
						}

						return pos;
					}
				});

			}

		} else if (dataTable.getDataList().get(0).getClass().equals(RoadOperationDetailsSumary.class)) {

			logger.info("In road Op sort by Op Name.");
			List<RoadOperationDetailsSumary> roadOpBOList = dataTable.getDataList();

			if (e != null ? ((String) e.getNewValue()).trim().equalsIgnoreCase("Operation Name") : dataTable.getSortBy().trim().equalsIgnoreCase("Operation Name")) {
				Collections.sort(roadOpBOList, new Comparator<RoadOperationDetailsSumary>() {
					@Override
					public int compare(RoadOperationDetailsSumary a, RoadOperationDetailsSumary b) {
						int pos = (a.getRoadOp().getOperationName().trim().compareToIgnoreCase(b.getRoadOp().getOperationName().trim()));

						if (dataTable.getSortOrder().equalsIgnoreCase("desc")) {
							if (pos != 0)
								pos = pos * -1;
						}

						return pos;
					}
				});

			} else if (e != null ? ((String) e.getNewValue()).trim().equalsIgnoreCase("Category") : dataTable.getSortBy().trim().equalsIgnoreCase("Category")) {

				Collections.sort(roadOpBOList, new Comparator<RoadOperationDetailsSumary>() {

					@Override
					public int compare(RoadOperationDetailsSumary a, RoadOperationDetailsSumary b) {
						int pos = (a.getRoadOp().getCategoryDescription().trim().compareToIgnoreCase(b.getRoadOp().getCategoryDescription().trim()));

						if (dataTable.getSortOrder().equalsIgnoreCase("desc")) {
							if (pos != 0)
								pos = pos * -1;
						}

						return pos;
					}
				});

			}
		}

		dataTable.refreshPageElements();

	}

	public Event sortOrderASC(RequestContext context) {
		DataTableMemory dataTable = (DataTableMemory) context.getFlowScope().get("dataTable");

		dataTable.setSortOrder("asc");

		this.sortBy(null);

		return success();
	}

	public Event sortOrderDESC(RequestContext context) {

		DataTableMemory dataTable = (DataTableMemory) context.getFlowScope().get("dataTable");

		dataTable.setSortOrder("desc");

		this.sortBy(null);

		return success();
	}

	/**
	 * This function checks the all offence outcome views to see if there is a difference in count of the other outcome
	 * view. If there is a difference in the counts then the all offence outcome view is set to null so the combine
	 * offence outcomes can take place again.
	 * 
	 * @param context
	 * @return <b>false</b> if counts are different and <b>true</> if counts are the same.
	 */
	private boolean isAllOffenceOutcomeTheSame(RequestContext context) {
		if (context.getFlowScope().get("allOffenceOutcomeViews") != null) {
			List<OffenceOutcomeView> allOffenceOutcomeViews = (List<OffenceOutcomeView>) context.getFlowScope().get("allOffenceOutcomeViews");

			int countAllOffenceOutcomeView = allOffenceOutcomeViews.size();

			int countCombinedOffenceOutcomeViews = 0;

			List<OffenceOutcomeView> mvOffenceOutcomeViews = (List<OffenceOutcomeView>) context.getFlowScope().get("mvOffenceOutcomes");

			if (mvOffenceOutcomeViews != null)
				countCombinedOffenceOutcomeViews += mvOffenceOutcomeViews.size();

			List<OffenceOutcomeView> dlOffenceOutcomeViews = (List<OffenceOutcomeView>) context.getFlowScope().get("dlOffenceOutcomes");

			if (dlOffenceOutcomeViews != null)
				countCombinedOffenceOutcomeViews += dlOffenceOutcomeViews.size();

			List<OffenceOutcomeView> roadOffenceOutcomeViews = (List<OffenceOutcomeView>) context.getFlowScope().get("rlOffenceOutcomes");

			if (roadOffenceOutcomeViews != null)
				countCombinedOffenceOutcomeViews += roadOffenceOutcomeViews.size();

			List<OffenceOutcomeView> badgeOffenceOutcomeViews = (List<OffenceOutcomeView>) context.getFlowScope().get("bdgOffenceOutcomes");

			if (badgeOffenceOutcomeViews != null)
				countCombinedOffenceOutcomeViews += badgeOffenceOutcomeViews.size();

			List<OffenceOutcomeView> citationOffenceOutcomeViews = (List<OffenceOutcomeView>) context.getFlowScope().get("chOffenceOutcomes");

			if (citationOffenceOutcomeViews != null)
				countCombinedOffenceOutcomeViews += citationOffenceOutcomeViews.size();

			List<OffenceOutcomeView> otherOffenceOutcomeViews = (List<OffenceOutcomeView>) context.getFlowScope().get("otOffenceOutcomes");

			if (otherOffenceOutcomeViews != null)
				countCombinedOffenceOutcomeViews += otherOffenceOutcomeViews.size();

			if (countCombinedOffenceOutcomeViews == countAllOffenceOutcomeView)
				return true;
			else
				return false;

		} else {
			return false;
		}
	}

	/**
	 * This function combines all of the road check offense outcome into one array list this array list is used to
	 * populate the offense outcome list. It is also used to do count for the different offenses to be used in the
	 * offense summary list.
	 * 
	 * @param context
	 */
	public void combineOffenceOutcomes(RequestContext context) {
		try {
			
			RoadCheckInitiateView roadCheckInaView = (RoadCheckInitiateView) context.getFlowScope().get("initiateView");
			
			roadCheckInaView.getAddressView().setParishDescription(this.getParishDesc(roadCheckInaView.getAddressView().getParish()));
			
			RoadCheckReviewSummaryBean roadCheckReviewSummaryBean = context.getFlowScope().get("roadCheckReviewSummaryBean") != null ?
					(RoadCheckReviewSummaryBean)context.getFlowScope().get("roadCheckReviewSummaryBean") : new RoadCheckReviewSummaryBean(false);
			
			if (roadCheckInaView.getRoleObserved().equalsIgnoreCase("c")) {
				roadCheckReviewSummaryBean.setRoleObserved("Conductor");
			} else if (roadCheckInaView.getRoleObserved().equalsIgnoreCase("d")) {
				roadCheckReviewSummaryBean.setRoleObserved("Driver");
			} else if (roadCheckInaView.getRoleObserved().equalsIgnoreCase("o")) {
				roadCheckReviewSummaryBean.setRoleObserved("Owner");
			} else if (roadCheckInaView.getRoleObserved().equalsIgnoreCase("t")) {
					
				HashMap filter = new HashMap<String, String>();
				filter.put("other_role_observed_id", roadCheckInaView.getOtherRoleId());
				
				List<RefCodeBO> roleObserved = getRefInfo("roms_cd_other_role_observed", filter);
				
				roadCheckReviewSummaryBean.setRoleObserved("Other - "+roleObserved.get(0).getDescription());
			}
			
			
			/* Get information to go into supporting details portion of the page. */
			SupportingDetailsView supportingDetails = (SupportingDetailsView) context.getFlowScope().get("supportingDetailsView");

			Maintenance mainProxy = this.getMaintenanceService();

			if (supportingDetails.getPoundId() != null && supportingDetails.getPoundId() > 0) {
				PoundCriteriaBO poundCriteria = new PoundCriteriaBO();

				poundCriteria.setPoundId(supportingDetails.getPoundId());

				List<PoundBO> listOfPounds = mainProxy.lookupPound(poundCriteria);

				if (listOfPounds.size() > 0)
					roadCheckReviewSummaryBean.setPound(listOfPounds.get(0));
			}

			if (supportingDetails.getWreckingCompanyId() != null && supportingDetails.getWreckingCompanyId() > 0) {
				WreckingCompanyCriteriaBO wreckingCompanyCriteria = new WreckingCompanyCriteriaBO();

				wreckingCompanyCriteria.setWreckingCompanyId(supportingDetails.getWreckingCompanyId());

				List<WreckingCompanyBO> wreckingCompanyList = mainProxy.lookupWreckingCompany(wreckingCompanyCriteria);

				if (wreckingCompanyList.size() > 0)
					roadCheckReviewSummaryBean.setWreckingCompany(wreckingCompanyList.get(0));

			}

			if (supportingDetails.getCourtId() != null && supportingDetails.getCourtId() > 0) 
			{
				CourtCriteriaBO courtCriteria = new CourtCriteriaBO();

				courtCriteria.setCourtId(supportingDetails.getCourtId());

				List<CourtBO> courtList = mainProxy.lookupCourt(courtCriteria);

				if (courtList.size() > 0)
					roadCheckReviewSummaryBean.setCourt(courtList.get(0));
				
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(supportingDetails.getCourtDateTime());
				calendar.set(Calendar.HOUR_OF_DAY, 10);
				//supportingDetails.getCourtDateTime().setHour(10);
				supportingDetails.setCourtDateTime(calendar.getTime());

			}

			if (context.getFlowScope().get("allOffenceOutcomeViews") == null || !isAllOffenceOutcomeTheSame(context)) {

				// Get a list of all offenses from the reference code web service
				ReferenceCode refCodeProxy = this.getReferenceCodeService();

				RefCodeCriteriaBO refCodeCrit = new RefCodeCriteriaBO();

				refCodeCrit.setTableName("roms_offence");

				List<RefCodeBO> refCodeList = refCodeProxy.getReferenceCode(refCodeCrit);

				/* Create list with all offences. */
				List<OffenceOutcomeView> allOffenceOutcomeViews = new ArrayList<OffenceOutcomeView>();

				List<OffenceOutcomeView> firstTenOffenceOutcomeViews = new ArrayList<OffenceOutcomeView>();


				// Create object to store list queries performed and results.
				List<Entry<String, String>> listOfQueriesPerformed = new ArrayList<Entry<String, String>>();

				/* Create list to store Associated Document Views. Summons will automically be added to associated
				 * document view list while two seperate objects will be created for Warning Notice and Warning No
				 * Prosecution. Offences will be added to the offence list of the later two. */
				List<AssociatedDocView> associatedDocViews = new ArrayList<AssociatedDocView>();

				AssociatedDocView warningNoticeAssociatedDocView = new AssociatedDocView(Constants.DocumentType.WARNING_NOTICE_STRING/* documentType */, new ArrayList<String>()/* associatedOffences */, false/* printed */, null/* serialNum */, null/* index */, new ArrayList<Integer>()/* offenceOutcomeIDs */, Constants.DocumentType.WARNING_NOTICE/* documentTypeCode */);

				AssociatedDocView warningNoProsecutionDocView = new AssociatedDocView(Constants.DocumentType.WARNING_NOTICE_NO_PROSECUTION_STRING/* documentType */, new ArrayList<String>()/* associatedOffences */, false/* printed */, null/* serialNum */, null/* index */, new ArrayList<Integer>()/* offenceOutcomeIDs */, Constants.DocumentType.WARNING_NOTICE_NO_PROSECUTION/* documentTypeCode */);

				/* Process Motor Vehicle Offence Outcomes */
				List<OffenceOutcomeView> mvOffenceOutcomeViews = (List<OffenceOutcomeView>) context.getFlowScope().get("mvOffenceOutcomes");

				if (mvOffenceOutcomeViews != null) {
					this.updateListOfOffences(allOffenceOutcomeViews, listOfQueriesPerformed, firstTenOffenceOutcomeViews, mvOffenceOutcomeViews, "Motor Vehicle", refCodeList, associatedDocViews, warningNoticeAssociatedDocView, warningNoProsecutionDocView, Constants.RoadCheckType.MOTOR_VEHICLE);

				}

				/* Process Driver Licences Offence Outcomes */
				List<OffenceOutcomeView> dlOffenceOutcomeViews = (List<OffenceOutcomeView>) context.getFlowScope().get("dlOffenceOutcomes");

				if (dlOffenceOutcomeViews != null && roadCheckInaView.isDlQuery()) {

					this.updateListOfOffences(allOffenceOutcomeViews, listOfQueriesPerformed, firstTenOffenceOutcomeViews, dlOffenceOutcomeViews, "Driver's Licence", refCodeList, associatedDocViews, warningNoticeAssociatedDocView, warningNoProsecutionDocView, Constants.RoadCheckType.DRIVERS_LICENCE);

				}

				/* Process Road Licences Offence Outcomes */
				List<OffenceOutcomeView> roadOffenceOutcomeViews = (List<OffenceOutcomeView>) context.getFlowScope().get("rlOffenceOutcomes");

				if (roadOffenceOutcomeViews != null && roadCheckInaView.isRoadLicQuery()) {
					this.updateListOfOffences(allOffenceOutcomeViews, listOfQueriesPerformed, firstTenOffenceOutcomeViews, roadOffenceOutcomeViews, "Road Licence", refCodeList, associatedDocViews, warningNoticeAssociatedDocView, warningNoProsecutionDocView, Constants.RoadCheckType.ROAD_LICENCE);

				}

				/* Process Badge Check Offence Outcomes */
				List<OffenceOutcomeView> badgeOffenceOutcomeViews = (List<OffenceOutcomeView>) context.getFlowScope().get("bdgOffenceOutcomes");

				if (badgeOffenceOutcomeViews != null && roadCheckInaView.isBadgeQuery()) {
					this.updateListOfOffences(allOffenceOutcomeViews, listOfQueriesPerformed, firstTenOffenceOutcomeViews, badgeOffenceOutcomeViews, "Badge", refCodeList, associatedDocViews, warningNoticeAssociatedDocView, warningNoProsecutionDocView, Constants.RoadCheckType.BADGE);
				}

				/* Process Citation History Offence Outcomes */
				List<OffenceOutcomeView> citationOffenceOutcomeViews = (List<OffenceOutcomeView>) context.getFlowScope().get("chOffenceOutcomes");

				if (citationOffenceOutcomeViews != null && roadCheckInaView.isCitationHistQuery()) {
					this.updateListOfOffences(allOffenceOutcomeViews, listOfQueriesPerformed, firstTenOffenceOutcomeViews, citationOffenceOutcomeViews, "Citation History", refCodeList, associatedDocViews, warningNoticeAssociatedDocView, warningNoProsecutionDocView, Constants.RoadCheckType.CITATION);

				}

				/* Process Other Offence Outcomes */
				@SuppressWarnings("unchecked")
				List<OffenceOutcomeView> otherOffenceOutcomeViews = (List<OffenceOutcomeView>) context.getFlowScope().get("otOffenceOutcomes");

				if (otherOffenceOutcomeViews != null && !otherOffenceOutcomeViews.isEmpty()) {
					this.updateListOfOffences(allOffenceOutcomeViews, listOfQueriesPerformed, firstTenOffenceOutcomeViews, otherOffenceOutcomeViews, "Other", refCodeList, associatedDocViews, warningNoticeAssociatedDocView, warningNoProsecutionDocView, Constants.RoadCheckType.OTHER);

				}

				// put all offence outcomes in flow scope
				context.getFlowScope().put("allOffenceOutcomeViews", allOffenceOutcomeViews);

				// put first 10 offence outcomes in flow scope
				context.getFlowScope().put("firstTenOffenceOutcomeViews", firstTenOffenceOutcomeViews);

				context.getFlowScope().put("currentOffenceOutcomeList", firstTenOffenceOutcomeViews);

				/* Put list of offence queries in flow scope */
				context.getFlowScope().put("listOfQueriesPerformed", listOfQueriesPerformed);

				/* Put associated docViews together and add it to RoadCheckReviewSummary Bean. */
				roadCheckReviewSummaryBean.setMobile(this.isHandHeld(context));

				roadCheckReviewSummaryBean.setAssociatedDocViews(associatedDocViews);
				
				for (AssociatedDocView docView : associatedDocViews) {
					if (docView.getDocumentType().equalsIgnoreCase(Constants.DocumentType.SUMMONS_STRING)) {
						roadCheckReviewSummaryBean.summonsPresent = true;

						continue;
					}
				}

				/* ___________________________________________________________________ */

				context.getFlowScope().put("roadCheckReviewSummaryBean", roadCheckReviewSummaryBean);

			}

		} catch (Exception exe) {
			exe.printStackTrace();

			this.addErrorMessageText(context, exe.getMessage());
		}

	}

	/**
	 * This is a generic function which takes different list and populates them with data based on the road check type.
	 * It also builds UI string based on the roadCheckType
	 * 
	 * @param allOffenceOutcomeViews
	 * @param listOfQueriesPerformed
	 * @param firstTenOffenceOutcomeViews
	 * @param outcomeViewsBeingProcessed
	 * @param roadCheckType
	 * @param refCodeList
	 * @return
	 */
	public void updateListOfOffences(List<OffenceOutcomeView> allOffenceOutcomeViews, List<Entry<String, String>> listOfQueriesPerformed, List<OffenceOutcomeView> firstTenOffenceOutcomeViews, List<OffenceOutcomeView> outcomeViewsBeingProcessed, String roadCheckType, List<RefCodeBO> refCodeList, List<AssociatedDocView> associatedDocViews, AssociatedDocView warningNoticeAssociatedDocView, AssociatedDocView warningNoProsecutionDocView, String constantsDocType) {
		Integer offenceCount = 0;
		String offenceQueriesPerformedMsg = "";

		for (OffenceOutcomeView offenceOutcome : outcomeViewsBeingProcessed) {
			offenceOutcome.setRoadCheckType(constantsDocType);

			if (offenceOutcome.getOffenceId() != null && offenceOutcome.getOffenceId() > 0) {
				for (RefCodeBO refCode : refCodeList) {
					if (Integer.parseInt(refCode.getCode()) == offenceOutcome.getOffenceId()) {
						offenceOutcome.setOffenceShortDescription(refCode.getShortDescription());

						offenceCount++;

						this.assignOffencesToAssociatedDocViews(associatedDocViews, warningNoticeAssociatedDocView, warningNoProsecutionDocView, offenceOutcome);

						break;
					}
				}

				allOffenceOutcomeViews.add(offenceOutcome);

				if (firstTenOffenceOutcomeViews.size() < 10)
					firstTenOffenceOutcomeViews.add(offenceOutcome);

			}
		}

		// Check offence count to create text to be displayed on screen
		if (offenceCount > 0) {

			offenceQueriesPerformedMsg = "[" + offenceCount + "] Offence(s)";

			listOfQueriesPerformed.add(new java.util.AbstractMap.SimpleEntry<String, String>(roadCheckType, offenceQueriesPerformedMsg));

		} else {
			listOfQueriesPerformed.add(new java.util.AbstractMap.SimpleEntry<String, String>(roadCheckType, "All is in Order"));
		}
	}

	/**
	 * This is a helper class which looks at the offence outcome object and places the offence in the relevant
	 * associated document view.
	 * 
	 * @param associatedDocViews
	 * @param warningNoticeAssociatedDocView
	 * @param warningNoProsecutionDocView
	 * @param OffenceOutcomeView
	 */
	public void assignOffencesToAssociatedDocViews(List<AssociatedDocView> associatedDocViews, AssociatedDocView warningNoticeAssociatedDocView, AssociatedDocView warningNoProsecutionDocView, OffenceOutcomeView offenceOutcome) {
		if (offenceOutcome.issueSummons || offenceOutcome.warnForProsecution) {
			// Issue summons so add to associatedDocViews list
			AssociatedDocView docView = new AssociatedDocView(Constants.DocumentType.SUMMONS_STRING/* documentType */, Arrays.asList(offenceOutcome.getOffenceShortDescription())/* associatedOffences */, false/* printed */, null/* serialNum */, associatedDocViews.size()/* index */, Arrays.asList(offenceOutcome.getOffenceId()) /* offenceOutcomeIds */, Constants.DocumentType.SUMMONS/* documentTypeCode */);

			associatedDocViews.add(docView);
		}

		if (offenceOutcome.issueWarningNP) {
			// Issue warning no prosecution so if warningNoProsecutionDocView is null instaniate it and add offence
			if (warningNoProsecutionDocView.getIndex() == null) {
				warningNoProsecutionDocView.setIndex(associatedDocViews.size());

				associatedDocViews.add(warningNoProsecutionDocView);
			}

			warningNoProsecutionDocView.getAssociatedOffences().add(offenceOutcome.getOffenceShortDescription());

			warningNoProsecutionDocView.getOffenceIDs().add(offenceOutcome.getOffenceId());

		}

		if (offenceOutcome.issuewWarningNotice) {
			// Issue warning no prosecution so if warningNoProsecutionDocView is null instaniate it and add offence
			if (warningNoticeAssociatedDocView.getIndex() == null) {
				warningNoticeAssociatedDocView.setIndex(associatedDocViews.size());

				associatedDocViews.add(warningNoticeAssociatedDocView);
			}

			warningNoticeAssociatedDocView.getAssociatedOffences().add(offenceOutcome.getOffenceShortDescription());

			warningNoticeAssociatedDocView.getOffenceIDs().add(offenceOutcome.getOffenceId());

		}
	}

	public void showAllOffenceOutcomes() {

		RequestContext requestContext = RequestContextHolder.getRequestContext();

		RoadCheckReviewSummaryBean roadCheckReviewSummaryBean = (RoadCheckReviewSummaryBean) requestContext.getFlowScope().get("roadCheckReviewSummaryBean");

		if (roadCheckReviewSummaryBean.getShowAllQueriesPerformed()) {
			List<OffenceOutcomeView> allOffenceOutcomes = (List<OffenceOutcomeView>) requestContext.getFlowScope().get("allOffenceOutcomeViews");

			requestContext.getFlowScope().put("currentOffenceOutcomeList", allOffenceOutcomes);
		} else {
			List<OffenceOutcomeView> firstTenOffenceOutcomeViews = (List<OffenceOutcomeView>) requestContext.getFlowScope().get("firstTenOffenceOutcomeViews");

			requestContext.getFlowScope().put("currentOffenceOutcomeList", firstTenOffenceOutcomeViews);

		}

	}

	/**
	 * This function uploads a file based on the selected file.
	 * 
	 * @param context
	 */
	public void uploadAssociatedDoc(RequestContext context) {
		try {
			
			logger.info("In Upload Associated Document Function.");

			RoadCheckReviewSummaryBean roadCheckSummaryBean = (RoadCheckReviewSummaryBean) context.getFlowScope().get("roadCheckReviewSummaryBean");
			
			logger.info("The selected document index is " + roadCheckSummaryBean.getSelectedAssociatedDocView().getIndex());
		} catch (Exception exe) {
			exe.printStackTrace();

			this.addErrorMessageText(context, exe.getMessage());
		}
	}

	/**
	 * This function handles the upload of files for the associated documents
	 */
	public void handleFileUpload(FileUploadEvent event) {

		RequestContext context = RequestContextHolder.getRequestContext();

		RoadCheckReviewSummaryBean roadCheckSummaryBean = (RoadCheckReviewSummaryBean) context.getFlowScope().get("roadCheckReviewSummaryBean");

		roadCheckSummaryBean.getSelectedAssociatedDocView().setUploadedFile(event.getFile());
	}

	public Event uploadDoc(RequestContext context) {
		RoadCheckReviewSummaryBean roadCheckSummaryBean = (RoadCheckReviewSummaryBean) context.getFlowScope().get("roadCheckReviewSummaryBean");

		boolean serialNumRepeated = false;

		if (StringUtil.isSet(roadCheckSummaryBean.getSelectedAssociatedDocView().getSerialNum())) {
			for (AssociatedDocView docView : roadCheckSummaryBean.getAssociatedDocViews()) {

				if (docView.serialNum != null && docView.getIndex().intValue() != roadCheckSummaryBean.selectedAssociatedDocView.getIndex().intValue() && docView.serialNum.equalsIgnoreCase(roadCheckSummaryBean.selectedAssociatedDocView.serialNum))
					serialNumRepeated = true;

			}

			if (!serialNumRepeated) {
				// Check document manager web service to see if the document serial number has been used
				try {
					serialNumRepeated = this.getDocumentsManagerService().manualSerialExists(roadCheckSummaryBean.getSelectedAssociatedDocView().getSerialNum(), null, roadCheckSummaryBean.getSelectedAssociatedDocView().getDocumentTypeCode());
				} catch (RequiredFieldMissingException e) {
					roadCheckSummaryBean.setShouldYouUpload(false);
					this.addErrorMessageText(context, "There was an error validating the manual serial number. Please try again.");

					return success();
				}
			}

			if (serialNumRepeated) {
				roadCheckSummaryBean.setShouldYouUpload(false);
				this.addErrorMessageText(context, "This serial number has already been used!");

				return success();
			} else {
				roadCheckSummaryBean.setShouldYouUpload(true);

				return error();
			}
		} else {
			roadCheckSummaryBean.setShouldYouUpload(false);
			this.addErrorMessageText(context, "Please enter a serial number.");
			return error();
		}

	}

	public void cancelUploadDoc(RequestContext context) {
		RoadCheckReviewSummaryBean roadCheckSummaryBean = (RoadCheckReviewSummaryBean) context.getFlowScope().get("roadCheckReviewSummaryBean");

		AssociatedDocView selectedDoc = roadCheckSummaryBean.getSelectedAssociatedDocView();

		selectedDoc.setSerialNum(null);
		selectedDoc.setComment("");
		selectedDoc.setUploadedFile(null);

	}

	/**
	 * This function returns true if the Road Check can be completed.
	 * If the road check is for an operation the operation needs to be not in a terminated state on the tablet
	 * or in between the actual start dates.
	 * @param roadCheckInaView
	 * @return
	 */
	private boolean checkIfRoadCheckCanBeCompleted(RoadCheckInitiateView roadCheckInaView){
		if(roadCheckInaView.getRoadOperationBO() != null 
				&& roadCheckInaView.getRoadOperationBO().getRoadOperationId() != null 
				&& roadCheckInaView.getRoadOperationBO().getRoadOperationId() > 0){
			
			RoadOperationCriteriaBO criteria = new RoadOperationCriteriaBO();
			criteria.setRoadOperationId(roadCheckInaView.getRoadOperationBO().getRoadOperationId());
			
			RoadOperationBO roadOp = null;
			
			List<RoadOperationBO> roadOps = null;
			
			try{
				roadOps = this.getRoadOperationService().lookupRoadOperation(criteria);
			}
			catch(Exception e){
				logger.error("Check if Road Check can be completed.", e);
			}
			
			if(roadOps == null || roadOps.size() < 0){
				return false;
			}
			else{
				roadOp = roadOps.get(0);
			}
			
			roadCheckInaView.setRoadOperationBO(roadOp);
			
			if(this.isHandHeldNoFlow() && roadOp.getStatusId().equalsIgnoreCase(Constants.Status.ROAD_OPERATION_TERMINATED)){
				this.addErrorMessageText(RequestContextHolder.getRequestContext(), 
						"Road Check cannot be completed because the Road Operation: " + roadOp.getOperationName() + " is " + Constants.Status.ROAD_OPERATION_TERMINATED_DESC + ".");
				
				return false;
			}
			
			return ! validateInitiateRequiredFields(RequestContextHolder.getRequestContext(),false);
			
		}
		else{
			return true;
		}
		
	}
	/**
	 * This function calls the record offense outcome of the road compliance web service.
	 * 
	 * @param context
	 * @return
	 */

	@SuppressWarnings("unchecked")
	public Event completeRoadCheck(RequestContext context) {
		RoadCheckReviewSummaryBean roadCheckReviewSummaryBean = (RoadCheckReviewSummaryBean) context.getFlowScope().get("roadCheckReviewSummaryBean");

		RoadCheckInitiateView roadCheckInaView = (RoadCheckInitiateView) context.getFlowScope().get("initiateView");
		
		if(!checkIfRoadCheckCanBeCompleted(roadCheckInaView)){
		
			Boolean continueRoadCheck = (Boolean) context.getFlowScope().get("continueRoadCheck");

			if (continueRoadCheck != null && !continueRoadCheck) {
				return new Event(this, "terminated_road_op");
			} else {
				return new Event(this, "terminated_road_op_from_search");
			}
		}
		
		RoadCompliancyCriteriaBO compCrit = new RoadCompliancyCriteriaBO();
		compCrit.setComplianceId(roadCheckInaView.getComplianceId());
		
		/* Get information to go into supporting details portion of the page. */
		SupportingDetailsView supportingDetails = (SupportingDetailsView) context.getFlowScope().get("supportingDetailsView");

		RoadCompliancy roadCompProxy = this.getRoadCompliancyService();

		boolean offencesSelected = false;
		boolean documentsGenerated = false;
		
		try 
		{
			
			/*Check if comment field is mandatory and if so validate for it*/
			if(roadCheckInaView.isVehicleInfoDifferent())
			{
				if(StringUtils.isEmpty(supportingDetails.getComment()))
				{
					throw new Exception("Comment is mandatory where observed vehicle information is different than vehicle details displayed by the application.");
				}
			}

			roadCheckInaView.setCompleteReviewSummary(false);

			ComplianceBO compliance = roadCompProxy.lookupRoadCompliance(compCrit).get(0);

			if (compliance.getStatusId() == null || compliance.getStatusId().equalsIgnoreCase(Constants.Status.ROAD_CHECK_INCOMPLETE)) {
				
				/* If there are offences under the other category create road check for other */
				/* Get Other Outcome Views */
				List<OffenceOutcomeView> otOffenceOutcomeViews = (List<OffenceOutcomeView>) context.getFlowScope().get("otOffenceOutcomes");

				if (otOffenceOutcomeViews != null && otOffenceOutcomeViews.size() > 0) {
					this.getRoadCompliancyService().createOtherCheck(compliance.getComplianceId(), this.getRomsLoggedInUser().getUsername());
				}

				/* Get compliancy Details from web service in order to get road check IDS they should be passed by the
				 * road check results however when a check fails an exception is thrown and no road check id is
				 * returned. */
				RoadCompliancyCriteriaBO compCriteria = new RoadCompliancyCriteriaBO();

				compCriteria.setComplianceId(roadCheckInaView.getComplianceId());

				List<ComplianceBO> listCompliancyBO = this.getRoadCompliancyService().lookupRoadCompliance(compCriteria);

				ComplianceBO complianceBO = listCompliancyBO.get(0);

				/* Get all the check types from the road compliancy look up web service function */
				RoadCheckBO mvRoadCheck = null;
				RoadCheckBO dlRoadCheck = null;
				RoadCheckBO otRoadCheck = null;
				RoadCheckBO rlRoadCheck = null;
				RoadCheckBO chRoadCheck = null;
				RoadCheckBO bdgRoadCheck = null;

				for (RoadCheckBO roadCheck : complianceBO.getListOfRoadChecks()) {
					if (roadCheck.getRoadCheckTypeID().trim().equalsIgnoreCase(Constants.RoadCheckType.BADGE))
						bdgRoadCheck = roadCheck;
					else if (roadCheck.getRoadCheckTypeID().trim().equalsIgnoreCase(Constants.RoadCheckType.CITATION))
						chRoadCheck = roadCheck;
					else if (roadCheck.getRoadCheckTypeID().trim().equalsIgnoreCase(Constants.RoadCheckType.DRIVERS_LICENCE))
						dlRoadCheck = roadCheck;
					else if (roadCheck.getRoadCheckTypeID().trim().equalsIgnoreCase(Constants.RoadCheckType.MOTOR_VEHICLE))
						mvRoadCheck = roadCheck;
					else if (roadCheck.getRoadCheckTypeID().trim().equalsIgnoreCase(Constants.RoadCheckType.OTHER))
						otRoadCheck = roadCheck;
					else if (roadCheck.getRoadCheckTypeID().trim().equalsIgnoreCase(Constants.RoadCheckType.ROAD_LICENCE))
						rlRoadCheck = roadCheck;

				}
				
				List<CompliancyCheckBO> compliancyCheckBOList = new ArrayList<CompliancyCheckBO>();


				List<DocumentViewBO> savedDocViews = new ArrayList<DocumentViewBO>();

				/* Get All Outcome Views */
				List<OffenceOutcomeView> allOffenceOutcomeViews = (List<OffenceOutcomeView>) context.getFlowScope().get("allOffenceOutcomeViews");

				if(allOffenceOutcomeViews != null && allOffenceOutcomeViews.size() > 0)
				{
					offencesSelected = true;
				}
				
				/* Get MV Outcome Views */
				List<OffenceOutcomeView> mvOffenceOutcomeViews = (List<OffenceOutcomeView>) context.getFlowScope().get("mvOffenceOutcomes");

				/* Get Badge Outcome Views */
				List<OffenceOutcomeView> bdgOffenceOutcomeViews = (List<OffenceOutcomeView>) context.getFlowScope().get("bdgOffenceOutcomes");

				/* Get DL Outcome Views */
				List<OffenceOutcomeView> dlOffenceOutcomeViews = (List<OffenceOutcomeView>) context.getFlowScope().get("dlOffenceOutcomes");

				/* Get Road Licenses Outcome Views */
				List<OffenceOutcomeView> rlOffenceOutcomeViews = (List<OffenceOutcomeView>) context.getFlowScope().get("rlOffenceOutcomes");

				/* Get Citation Outcome Views */
				List<OffenceOutcomeView> chOffenceOutcomeViews = (List<OffenceOutcomeView>) context.getFlowScope().get("chOffenceOutcomes");

				if (mvOffenceOutcomeViews != null && mvOffenceOutcomeViews.size() > 0 && mvRoadCheck != null) {
					CompliancyCheckBO compliancyCheckBOMV = new CompliancyCheckBO();
					
					this.populateHeader(compliancyCheckBOMV,allOffenceOutcomeViews,supportingDetails,context);
					
					compliancyCheckBOMV.setRoadCheckID(mvRoadCheck.getRoadCheckId());

					compliancyCheckBOMV.getListOfRoadChkOffOutcome().addAll(this.updateOffenceOutcomeList(mvOffenceOutcomeViews, supportingDetails, roadCheckInaView));
					
					compliancyCheckBOList.add(compliancyCheckBOMV);
					
				}

				if (bdgOffenceOutcomeViews != null && bdgOffenceOutcomeViews.size() > 0 && bdgRoadCheck != null) {

					CompliancyCheckBO compliancyCheckBOBDG = new CompliancyCheckBO();
					
					this.populateHeader(compliancyCheckBOBDG,allOffenceOutcomeViews,supportingDetails,context);
					
					compliancyCheckBOBDG.setRoadCheckID(bdgRoadCheck.getRoadCheckId());

					compliancyCheckBOBDG.getListOfRoadChkOffOutcome().addAll(this.updateOffenceOutcomeList(bdgOffenceOutcomeViews, supportingDetails, roadCheckInaView));

					compliancyCheckBOList.add(compliancyCheckBOBDG);
					
				}

				if (dlOffenceOutcomeViews != null && dlOffenceOutcomeViews.size() > 0 && dlRoadCheck != null) {

					CompliancyCheckBO compliancyCheckBODL = new CompliancyCheckBO();
					
					this.populateHeader(compliancyCheckBODL,allOffenceOutcomeViews,supportingDetails,context);
					
					compliancyCheckBODL.setRoadCheckID(dlRoadCheck.getRoadCheckId());
					
					compliancyCheckBODL.getListOfRoadChkOffOutcome().addAll(this.updateOffenceOutcomeList(dlOffenceOutcomeViews, supportingDetails, roadCheckInaView));

					compliancyCheckBOList.add(compliancyCheckBODL);
					
				}

				if (otOffenceOutcomeViews != null && otOffenceOutcomeViews.size() > 0 && otRoadCheck != null) {

					CompliancyCheckBO compliancyCheckBOOTH = new CompliancyCheckBO();
					
					this.populateHeader(compliancyCheckBOOTH,allOffenceOutcomeViews,supportingDetails,context);
					
					compliancyCheckBOOTH.setRoadCheckID(otRoadCheck.getRoadCheckId());

					compliancyCheckBOOTH.getListOfRoadChkOffOutcome().addAll(this.updateOffenceOutcomeList(otOffenceOutcomeViews, supportingDetails, roadCheckInaView));

					compliancyCheckBOList.add(compliancyCheckBOOTH);
					
				}

				if (chOffenceOutcomeViews != null && chOffenceOutcomeViews.size() > 0 && chRoadCheck != null) {

					CompliancyCheckBO compliancyCheckBOCH = new CompliancyCheckBO();
					
					this.populateHeader(compliancyCheckBOCH,allOffenceOutcomeViews,supportingDetails,context);
					
					compliancyCheckBOCH.setRoadCheckID(chRoadCheck.getRoadCheckId());

					compliancyCheckBOCH.getListOfRoadChkOffOutcome().addAll(this.updateOffenceOutcomeList(chOffenceOutcomeViews, supportingDetails, roadCheckInaView));

					compliancyCheckBOList.add(compliancyCheckBOCH);
					
				}

				if (rlOffenceOutcomeViews != null && rlOffenceOutcomeViews.size() > 0 && rlRoadCheck != null) {

					CompliancyCheckBO compliancyCheckBORL = new CompliancyCheckBO();
					
					this.populateHeader(compliancyCheckBORL,allOffenceOutcomeViews,supportingDetails,context);
					
					compliancyCheckBORL.setRoadCheckID(rlRoadCheck.getRoadCheckId());
					
					compliancyCheckBORL.getListOfRoadChkOffOutcome().addAll(this.updateOffenceOutcomeList(rlOffenceOutcomeViews, supportingDetails, roadCheckInaView));

					compliancyCheckBOList.add(compliancyCheckBORL);
				}
				
				
				// Set compliance to is complete
				List<ComplianceBO> listCompliancyBOForUpdate = this.getRoadCompliancyService().lookupRoadCompliance(compCriteria);

				ComplianceBO complianceToUpdate = listCompliancyBOForUpdate.get(0);
				// updated other fields on the complianceBO
				complianceToUpdate = pupulateComplianceBO(roadCheckInaView, complianceToUpdate);

				complianceToUpdate.setComment(supportingDetails.getComment());

				complianceToUpdate.setStatusId(Constants.Status.ROAD_CHECK_COMPLETE);

				complianceToUpdate.setCurrentUserName(this.getRomsLoggedInUser().getUsername());

				List<RefCodeBO> refCodesExcerptParam = this.getRefInfo("roms_excerpt_param_mapping");
				List<ComplianceParamBO> listOfComplianceParams = new ArrayList<ComplianceParamBO>();

				if (supportingDetails.getInspectorParam() != null && !StringUtils.isEmpty(supportingDetails.getInspectorParam().getStaffId())) {
					ComplianceParamBO complianceParam = new ComplianceParamBO();

					complianceParam.setComplianceId(complianceToUpdate.getComplianceId());

					for (RefCodeBO refCode : refCodesExcerptParam) {
						if (refCode.getShortDescription().toLowerCase().startsWith("inspector_threatened")) {

							complianceParam.setParamMapId(Integer.parseInt(refCode.getCode()));

							break;
						}
					}

					complianceParam.setParamValue(supportingDetails.getInspectorParam().getFullName());

					listOfComplianceParams.add(complianceParam);

				}

				if (!StringUtils.isEmpty(supportingDetails.getDirectiveParam())) {
					ComplianceParamBO complianceParam = new ComplianceParamBO();

					complianceParam.setComplianceId(complianceToUpdate.getComplianceId());

					for (RefCodeBO refCode : refCodesExcerptParam) {
						if (refCode.getShortDescription().toLowerCase().startsWith("directive_of_inspector")) {

							complianceParam.setParamMapId(Integer.parseInt(refCode.getCode()));

							break;
						}
					}

					complianceParam.setParamValue(supportingDetails.getDirectiveParam());

					listOfComplianceParams.add(complianceParam);

				}

				if (supportingDetails.getNoOfPassengers() != null) {
					ComplianceParamBO complianceParam = new ComplianceParamBO();

					complianceParam.setComplianceId(complianceToUpdate.getComplianceId());

					for (RefCodeBO refCode : refCodesExcerptParam) {
						if (refCode.getShortDescription().toLowerCase().startsWith("number_of_passengers")) {

							complianceParam.setParamMapId(Integer.parseInt(refCode.getCode()));

							break;
						}
					}

					complianceParam.setParamValue(supportingDetails.getNoOfPassengers().toString());

					listOfComplianceParams.add(complianceParam);

				}

				if(supportingDetails.getCourtDateTime() != null)
				{
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(supportingDetails.getCourtDateTime());
					calendar.set(Calendar.HOUR_OF_DAY, 10);
					calendar.set(Calendar.MINUTE, 0);
					calendar.set(Calendar.SECOND, 0);
					
					/*supportingDetails.setCourtDateTime(calendar.getTime());
					supportingDetails.getCourtDateTime().setHours(10);
					supportingDetails.getCourtDateTime().setMinute(0);
					supportingDetails.getCourtDateTime().setSecond(0);*/
					supportingDetails.setCourtDateTime(calendar.getTime());
					 
					complianceToUpdate.setCourtDate(supportingDetails.getCourtDateTime()); 
				    
					 
				}
				
				if(supportingDetails.getCourtId() != null){
					CourtBO court = new CourtBO();
					court.setCourtId(supportingDetails.getCourtId());
					complianceToUpdate.setCourt(court);
				}
				 
				
				if (listOfComplianceParams.size() > 0){
					complianceToUpdate.getListOfComplianceParams().addAll(listOfComplianceParams);
				}

				//call web service method to save documents and update compliance object
				savedDocViews.addAll(roadCompProxy.recordOffenceOutcome(compliancyCheckBOList, complianceToUpdate));
				
				if(savedDocViews != null && savedDocViews.size()>0)
				{
					documentsGenerated = true;
				}
			
				
				this.uploadScannedDocuments(savedDocViews, roadCheckReviewSummaryBean.associatedDocViews);

				roadCheckReviewSummaryBean.setShouldGoToPrepareAndPrint(true);

				roadCheckReviewSummaryBean.setDocViews(savedDocViews);
				
				
				context.getFlowScope().put("roadCheckReviewSummaryBean", roadCheckReviewSummaryBean);

				roadCheckInaView.setCompleteReviewSummary(true);
				return success();
				
				/*Code was commented out by Oneal Anguin on 20 November 2014, Discusions with Cassandra mentioned that Road Checks on Terminated Operations
				 * are treated thje same way and should go to prepare screen*/
//				if (complianceToUpdate.getRoadOperationBO() != null && complianceToUpdate.getRoadOperationBO().getRoadOperationId() != null && complianceToUpdate.getRoadOperationBO().getStatusId().trim().equalsIgnoreCase(Constants.Status.ROAD_OPERATION_TERMINATED)) {
//					Boolean continueRoadCheck = (Boolean) context.getFlowScope().get("continueRoadCheck");
//
//					if (continueRoadCheck != null && !continueRoadCheck) {
//						return new Event(this, "terminated_road_op");
//					} else {
//						return new Event(this, "terminated_road_op_from_search");
//					}
//
//				} else {
//					roadCheckInaView.setCompleteReviewSummary(true);
//					return success();
//				}
			} else {
				/* At this point the compliance has been saved already and more than likely it is a refresh of the
				 * browser or something related to that so this segment will go back to the web service and get the
				 * documents to be printed. */
				ComplianceDetailsBO compDetails = roadCompProxy.getComplianceDetails(roadCheckInaView.getComplianceId());
				roadCheckReviewSummaryBean.setShouldGoToPrepareAndPrint(true);
				roadCheckReviewSummaryBean.setDocViews(compDetails.getListOfDocuments());
				roadCheckInaView.setCompleteReviewSummary(true);
				return success();

			}

		} catch (Exception exe) {
			exe.printStackTrace();
			if(offencesSelected && !documentsGenerated)
			{
				this.addErrorMessage(RequestContextHolder.getRequestContext(), "SystemError");
			}else
			{
				this.addErrorMessageText(context, exe.getMessage() != null ? exe.getMessage() : exe.toString());
			}
			roadCheckReviewSummaryBean.setShouldGoToPrepareAndPrint(false);
			return error();
		} finally {
			/* Get The Compliance Object After offences are saved to be used in the Road Check Review Page */

			try {
				context.getFlowScope().put("complianceBO", roadCompProxy.lookupRoadCompliance(compCrit).get(0));
			} catch (Exception e) 
			{
			
				e.printStackTrace();
			}
		}

	}

	private void populateHeader(CompliancyCheckBO compliancyCheckBO, List<OffenceOutcomeView> allOffenceOutcomeViews, SupportingDetailsView supportingDetails, RequestContext context) {
		if (this.isHandHeld(context))
			compliancyCheckBO.setInHouse(false);
		else
			compliancyCheckBO.setInHouse(true);

		compliancyCheckBO.setCurrentUserName(this.getRomsLoggedInUser().getUsername());
	
	
		if (allOffenceOutcomeViews.size() < 1)
			compliancyCheckBO.setOutcomeTypeID(Constants.OutcomeType.ALL_IN_ORDER);
		else
			compliancyCheckBO.setOutcomeTypeID("");

		compliancyCheckBO.setExcerptParams(supportingDetails.getOffenceParamMap());

	// @jreid 2014-01-23
	if (StringUtils.isNotBlank(supportingDetails.getAllegation())) {

		compliancyCheckBO.setAllegation(supportingDetails.getAllegation());
	}
		
	}

	/**
	 * This is a helper function which looks at the created passed in road check offence outcomes and compares with the
	 * associatedDocs to create ScannedDOC using the scanned document web service.
	 * 
	 * @param savedDocViews
	 * @param associatedDocs
	 * @throws fsl.ta.toms.roms.webservices.scanneddocupload.NoRecordFoundException
	 * @throws fsl.ta.toms.roms.webservices.scanneddocupload.ErrorSavingException
	 */
	public void uploadScannedDocuments(List<DocumentViewBO> savedDocViews, List<AssociatedDocView> associatedDocs) throws ErrorSavingException, RequiredFieldMissingException {
		for (AssociatedDocView associatedDoc : associatedDocs) {
			// find out if any off the offence ids from road check offence outcome match the offence ids in the
			// associated doc
			if (associatedDoc.getUploadedFile() != null) {
				for (DocumentViewBO docView : savedDocViews) {
					if (docView.getDocumentTypeCode().equalsIgnoreCase(associatedDoc.getDocumentTypeCode())) {
						List<Integer> offenceIds = new ArrayList<Integer>();
						
						if(docView.getListOfOffences().get(0) instanceof OffenceBO ){
					
							for (OffenceBO offence : docView.getListOfOffences()) {
								offenceIds.add(offence.getOffenceId());
							}
						}
						else{
							
							for (Object offence : docView.getListOfOffences()) {
								offenceIds.add(((OffenceBO)offence).getOffenceId());
							}
						}
						
						
						if (CollectionUtils.containsAny(associatedDoc.getOffenceIDs(), offenceIds)) {
							// Found a match. Create the Scanned Document object and call the web service then break
							// from the inner loop.
							ScannedDocBO scannedDoc = new ScannedDocBO();

							scannedDoc.setCurrentUsername(this.getRomsLoggedInUser().getUsername());

							scannedDoc.setFileContents(associatedDoc.getUploadedFile().getContents());

							scannedDoc.setMimeType(associatedDoc.getUploadedFile().getContentType());

							scannedDoc.setDescription("Scanned Manual Document");

							scannedDoc.setRoadCheckOffenceOutcomeId(docView.getRoadCheckOffenceOutcomeCode());

							scannedDoc.setDocType(associatedDoc.getDocumentType());

							scannedDoc.setManualSerialNo(associatedDoc.getSerialNum());

							try {
								DocumentViewBO documentView = new DocumentViewBO();
								//copyFields(documentView,docView);
								documentView = copyDocumentViewBO(documentView,docView);
								
								documentView.setManualSerialNo(associatedDoc.getSerialNum());
								
								this.getScannedDocUploadService().uploadFile(scannedDoc,documentView);
							} catch (Exception e) {
								// In the event that one document cannot be uploaded continue to process the rest of the
								// documents.
								e.printStackTrace();
							}

							break;
						}
					}
				}
			}
		}
	}

	/**
	 * This is a helper function that adds the list of offences to the roadCheckOffenceOutcomeBO list
	 * 
	 * @param roadCheckOffenceOutcomeBOList
	 * @param listOfOffences
	 * @param supportingDetails
	 * @param roadCheckInaView
	 */
	private List<RoadCheckOffenceOutcomeBO> updateOffenceOutcomeList(List<OffenceOutcomeView> allOffenceOutcomeViews, SupportingDetailsView supportingDetails, RoadCheckInitiateView roadCheckInaView) {
		try{
			
			List<RoadCheckOffenceOutcomeBO> roadCheckOffenceOutcomeBOListOUT = new ArrayList<RoadCheckOffenceOutcomeBO>();
			
			for (OffenceOutcomeView offenceOutcomeView : allOffenceOutcomeViews) {
	
				/* This portion of the code looks to split the offence outcome based on if warning notice, wearing no
				 * prosecution, etc. is true */
				if (offenceOutcomeView.issueSummons) {
					RoadCheckOffenceOutcomeBO offenceOutcomeBOSummons = new RoadCheckOffenceOutcomeBO();
	
					offenceOutcomeBOSummons.setCourtDateTime(supportingDetails.getCourtDateTime());
	
					offenceOutcomeBOSummons.setCourtId(supportingDetails.getCourtId());
	
					offenceOutcomeBOSummons.setOffenceDateTime(roadCheckInaView.getOffenceDate());
	
					offenceOutcomeBOSummons.setOffenceId(offenceOutcomeView.getOffenceId());
	
					offenceOutcomeBOSummons.setPoundID(supportingDetails.getPoundId());
	
					offenceOutcomeBOSummons.setWreckingCoID(supportingDetails.getWreckingCompanyId());
	
					offenceOutcomeBOSummons.setVehicleMoverPersonID(supportingDetails.getVehicleMoverBO() != null ? supportingDetails.getVehicleMoverBO().getPersonId() : null);
	
					offenceOutcomeBOSummons.setWreckerVehicleID(supportingDetails.getWreckerVehicleBO() != null ? supportingDetails.getWreckerVehicleBO().getVehicleId() : null);
	
					offenceOutcomeBOSummons.setVehicleMoverPersonType(supportingDetails.getVehicleMoverType());
	
					offenceOutcomeBOSummons.setIssueDate(roadCheckInaView.getOffenceDate());
	
					// offenceOutcomeBOSummons.setJpRegNum("123456");//request failing on on jp reg number
	
					offenceOutcomeBOSummons.setOutcomeTypeID(Constants.OutcomeType.ISSUE_SUMMONS);
	
					
					if(offenceOutcomeView.offenceId.intValue() == Constants.OffenceAidAndAbbet.NO_ROAD_LIC.intValue() ||
							offenceOutcomeView.offenceId.intValue() == Constants.OffenceAidAndAbbet.NO_PPV_INSUR.intValue()){
						
						offenceOutcomeBOSummons.setOwner(supportingDetails.getOwner());
					}
					
					
					
					roadCheckOffenceOutcomeBOListOUT.add(offenceOutcomeBOSummons);
				}
	
				if (offenceOutcomeView.issueWarningNP) {
					RoadCheckOffenceOutcomeBO offenceOutcomeBOWarningNP = new RoadCheckOffenceOutcomeBO();
	
					offenceOutcomeBOWarningNP.setCourtDateTime(supportingDetails.getCourtDateTime());
	
					offenceOutcomeBOWarningNP.setCourtId(supportingDetails.getCourtId());
	
					offenceOutcomeBOWarningNP.setOffenceDateTime(roadCheckInaView.getOffenceDate());
	
					offenceOutcomeBOWarningNP.setOffenceId(offenceOutcomeView.getOffenceId());
	
					offenceOutcomeBOWarningNP.setPoundID(supportingDetails.getPoundId());
	
					offenceOutcomeBOWarningNP.setWreckingCoID(supportingDetails.getWreckingCompanyId());
	
					offenceOutcomeBOWarningNP.setVehicleMoverPersonID(supportingDetails.getVehicleMoverBO() != null ? supportingDetails.getVehicleMoverBO().getPersonId() : null);
	
					offenceOutcomeBOWarningNP.setWreckerVehicleID(supportingDetails.getWreckerVehicleBO() != null ? supportingDetails.getWreckerVehicleBO().getVehicleId() : null);
	
					offenceOutcomeBOWarningNP.setVehicleMoverPersonType(supportingDetails.getVehicleMoverType());
	
					offenceOutcomeBOWarningNP.setIssueDate(roadCheckInaView.getOffenceDate());
	
					// offenceOutcomeBOWarningNP.setJpRegNum("123456");//request failing on on jp reg number
	
					offenceOutcomeBOWarningNP.setOutcomeTypeID(Constants.OutcomeType.WARNED_NO_PROSECUTION);
	
					roadCheckOffenceOutcomeBOListOUT.add(offenceOutcomeBOWarningNP);
				}
	
				if (offenceOutcomeView.issuewWarningNotice) {
					RoadCheckOffenceOutcomeBO offenceOutcomeBOWarningNotice = new RoadCheckOffenceOutcomeBO();
	
					// set wittnesses on warning notice offences
					List<PersonBO> personList = this.convertWitnessListToPersonList(supportingDetails.getWitnessList());
					if (personList != null)
						offenceOutcomeBOWarningNotice.getWitnesses().addAll(personList);
	
					offenceOutcomeBOWarningNotice.setCourtDateTime(supportingDetails.getCourtDateTime());
	
					offenceOutcomeBOWarningNotice.setCourtId(supportingDetails.getCourtId());
	
					offenceOutcomeBOWarningNotice.setOffenceDateTime(roadCheckInaView.getOffenceDate());
	
					offenceOutcomeBOWarningNotice.setOffenceId(offenceOutcomeView.getOffenceId());
	
					offenceOutcomeBOWarningNotice.setPoundID(supportingDetails.getPoundId());
	
					offenceOutcomeBOWarningNotice.setWreckingCoID(supportingDetails.getWreckingCompanyId());
	
					offenceOutcomeBOWarningNotice.setVehicleMoverPersonID(supportingDetails.getVehicleMoverBO() != null ? supportingDetails.getVehicleMoverBO().getPersonId() : null);
	
					offenceOutcomeBOWarningNotice.setWreckerVehicleID(supportingDetails.getWreckerVehicleBO() != null ? supportingDetails.getWreckerVehicleBO().getVehicleId() : null);
	
					offenceOutcomeBOWarningNotice.setVehicleMoverPersonType(supportingDetails.getVehicleMoverType());
	
					offenceOutcomeBOWarningNotice.setIssueDate(roadCheckInaView.getOffenceDate());
	
					// offenceOutcomeBOWarningNotice.setJpRegNum("123456");//request failing on on jp reg number
	
					offenceOutcomeBOWarningNotice.setOutcomeTypeID(Constants.OutcomeType.ISSUE_WARNING_NOTICE);
	
					roadCheckOffenceOutcomeBOListOUT.add(offenceOutcomeBOWarningNotice);
				}
	
				if (offenceOutcomeView.removePlate) {
					RoadCheckOffenceOutcomeBO offenceOutcomeBORemovePlate = new RoadCheckOffenceOutcomeBO();
	
					offenceOutcomeBORemovePlate.setCourtDateTime(supportingDetails.getCourtDateTime());
	
					offenceOutcomeBORemovePlate.setCourtId(supportingDetails.getCourtId());
	
					offenceOutcomeBORemovePlate.setOffenceDateTime(roadCheckInaView.getOffenceDate());
	
					offenceOutcomeBORemovePlate.setOffenceId(offenceOutcomeView.getOffenceId());
	
					offenceOutcomeBORemovePlate.setPoundID(supportingDetails.getPoundId());
	
					offenceOutcomeBORemovePlate.setWreckingCoID(supportingDetails.getWreckingCompanyId());
	
					offenceOutcomeBORemovePlate.setVehicleMoverPersonID(supportingDetails.getVehicleMoverBO().getPersonId());
	
					offenceOutcomeBORemovePlate.setWreckerVehicleID(supportingDetails.getWreckerVehicleBO().getVehicleId());
	
					offenceOutcomeBORemovePlate.setVehicleMoverPersonType(supportingDetails.getVehicleMoverType());
	
					offenceOutcomeBORemovePlate.setIssueDate(roadCheckInaView.getOffenceDate());
	
					// offenceOutcomeBORemovePlate.setJpRegNum("123456");//request failing on on jp reg number
	
					offenceOutcomeBORemovePlate.setOutcomeTypeID(Constants.OutcomeType.REMOVE_PLATES);
	
					roadCheckOffenceOutcomeBOListOUT.add(offenceOutcomeBORemovePlate);
				}
	
				if (offenceOutcomeView.warnForProsecution) {
					RoadCheckOffenceOutcomeBO offenceOutcomeBOWarningForProsecution = new RoadCheckOffenceOutcomeBO();
	
					offenceOutcomeBOWarningForProsecution.setCourtDateTime(supportingDetails.getCourtDateTime());
	
					offenceOutcomeBOWarningForProsecution.setCourtId(supportingDetails.getCourtId());
	
					offenceOutcomeBOWarningForProsecution.setOffenceDateTime(roadCheckInaView.getOffenceDate());
	
					offenceOutcomeBOWarningForProsecution.setOffenceId(offenceOutcomeView.getOffenceId());
	
					offenceOutcomeBOWarningForProsecution.setPoundID(supportingDetails.getPoundId());
	
					offenceOutcomeBOWarningForProsecution.setWreckingCoID(supportingDetails.getWreckingCompanyId());
					
					if(supportingDetails.getVehicleMoverBO() != null)
						offenceOutcomeBOWarningForProsecution.setVehicleMoverPersonID(supportingDetails.getVehicleMoverBO().getPersonId());
	
					offenceOutcomeBOWarningForProsecution.setWreckerVehicleID(supportingDetails.getWreckerVehicleBO().getVehicleId());
	
					offenceOutcomeBOWarningForProsecution.setVehicleMoverPersonType(supportingDetails.getVehicleMoverType());
	
					offenceOutcomeBOWarningForProsecution.setIssueDate(roadCheckInaView.getOffenceDate());
	
					// offenceOutcomeBOWarningForProsecution.setJpRegNum("123456");//request failing on on jp reg number
	
					offenceOutcomeBOWarningForProsecution.setOutcomeTypeID(Constants.OutcomeType.WARNED_FOR_PROSECUTION);
	
				
					if(offenceOutcomeView.offenceId.intValue() == Constants.OffenceAidAndAbbet.NO_ROAD_LIC.intValue() ||
							offenceOutcomeView.offenceId.intValue() == Constants.OffenceAidAndAbbet.NO_PPV_INSUR.intValue()){
						
						offenceOutcomeBOWarningForProsecution.setOwner(supportingDetails.getOwner());
					}
					
					
					roadCheckOffenceOutcomeBOListOUT.add(offenceOutcomeBOWarningForProsecution);
				}
	
			}
			
			return roadCheckOffenceOutcomeBOListOUT;
		}catch(Exception ex){
			ex.printStackTrace();
			return null;
		}
		
		
	}

	/**
	 * This function returns a list of which are created from a list of witness objects. This list will be utilized by
	 * the offence outome creation where a warning notice is being issued.
	 * 
	 * @param witnessList
	 * @return
	 */
	public List<PersonBO> convertWitnessListToPersonList(List<WitnessView> witnessList) {
		if (witnessList != null && witnessList.size() > 0) {
			List<PersonBO> personList = new ArrayList<PersonBO>();

			for (WitnessView witness : witnessList) {
				PersonBO person = new PersonBO();

				person.setFirstName(witness.getFirstName());
				person.setLastName(witness.getLastName());
				person.setMiddleName(witness.getMiddleName());
				person.setMarkText(witness.getAddressView().getMarkText());
				person.setAddressLine1(witness.getAddressView().getAddressLine1());
				person.setAddressLine2(witness.getAddressView().getAddressLine2());
				person.setAddressLine2NL(witness.getAddressView().getAddressLine2NL());
				person.setParishCode(witness.getAddressView().getParish());
				person.setPoBoxNo(witness.getAddressView().getPoBoxNo());
				person.setPoLocationName(witness.getAddressView().getPoLocationName());
				person.setStreetName(witness.getAddressView().getStreetName());
				person.setStreetNo(witness.getAddressView().getStreetNo());
				person.setTelephoneCell(witness.getMobilePhoneNo());
				person.setTelephoneHome(witness.getHomePhoneNo());
				person.setTelephoneWork(witness.getWorkPhoneNo());
				person.setCurrentUserName(this.getRomsLoggedInUser().getUsername());

				personList.add(person);
			}

			return personList;
		} else {
			return null;
		}
	}

	public void setOpenReportWindow(Boolean openReportWindow, RequestContext context) {
		try {
			RoadCheckReviewSummaryBean roadCheckReviewSummaryBean = (RoadCheckReviewSummaryBean) context.getFlowScope().get("roadCheckReviewSummaryBean");

			roadCheckReviewSummaryBean.setOpenReportWindow(openReportWindow);

			List<DocumentViewBO> documentViews = new ArrayList<DocumentViewBO>();
			
			if (!roadCheckReviewSummaryBean.isViewDetailsScreen) {
				for (DocumentViewBO docView : roadCheckReviewSummaryBean.getDocViews()) {
					if (docView.getJpIdNumber() == null && roadCheckReviewSummaryBean.getjPIdNum() != null) {
						docView.setJpIdNumber(roadCheckReviewSummaryBean.getjPIdNum());

					}

					// Check if offence date is NULL which is normally is for warning notice if so set it to the create
					// date of record
					if (docView.getOffenceDtime() == null)
						docView.setOffenceDtime(roadCheckReviewSummaryBean.getComplianceDate());

					DocumentViewBO convertedDoc = convertFromRoadCompDocViewToDocManDocView(docView);

					documentViews.add(convertedDoc);

				}
			} else if (roadCheckReviewSummaryBean.isViewDetailsScreen) {
				for (DocumentViewBO docView : roadCheckReviewSummaryBean.getSelectedDocViews()) {
					if (docView.getJpIdNumber() == null && roadCheckReviewSummaryBean.getjPIdNum() != null) {
						docView.setJpIdNumber(roadCheckReviewSummaryBean.getjPIdNum());

					}

					// Check if offence date is NULL which is normally is for warning notice if so set it to the create
					// date of record
					if (docView.getOffenceDtime() == null)
						docView.setOffenceDtime(roadCheckReviewSummaryBean.getComplianceDate());

					documentViews.add(convertFromRoadCompDocViewToDocManDocView(docView));
					

				}
			}

			HttpServletRequest req = (HttpServletRequest) context.getExternalContext().getNativeRequest();
			HttpSession session = req.getSession();
			boolean isHandHeld = this.isHandHeld(context);// is handheld implies user is doing road check on the road
			session.setAttribute(Constants.IS_MOBILE, isHandHeld);
			session.setAttribute(Constants.DocumentPrinting.DOCUMENT_LIST, documentViews);
			session.setAttribute("romsLoggedInUser", this.getRomsLoggedInUser().getUsername());

			if (roadCheckReviewSummaryBean.summonsPresent)
				this.setIdNumbersOnSummons(documentViews, roadCheckReviewSummaryBean.getjPIdNum(), isHandHeld);
			// above changed by jreid 2014-06-27 for new addJP method

			roadCheckReviewSummaryBean.setAlreadyPrinted(true);

			
			context.getFlowScope().put("roadCheckReviewSummaryBean", roadCheckReviewSummaryBean);

		} catch (Exception exe) {

			exe.printStackTrace();
			this.addErrorMessageText(context, "There were some errors in processing your request.");
		}

	}

	/* private void setRegNumbersOnSummons(List<DocumentViewBO>
	 * roadCompDocView, String regNumber) throws
	 * ErrorSavingException,
	 * NoRecordFoundException,
	 * NoRecordFoundException { List<Integer>
	 * summonsSerialNums = new ArrayList<Integer>(); for(DocumentViewBO
	 * docView : roadCompDocView) {
	 * if(docView.getDocumentTypeCode().trim().equalsIgnoreCase(Constants.DocumentType.SUMMONS) &&
	 * docView.getJpIdNumber()!= null && ! StringUtils.isEmpty(regNumber) ) {
	 * summonsSerialNums.add(docView.getAutomaticSerialNo()); } } if(summonsSerialNums.size() > 0) {
	 * this.getDocumentsManagerPortProxy().addJPDetailsFromRoadCheck(summonsSerialNums, regNumber, isHandHeld,
	 * userName); } } */

	private void setIdNumbersOnSummons(List<DocumentViewBO> roadCompDocView, Integer IdNumber, boolean isHandHeld) throws ErrorSavingException, NoRecordFoundException, RequiredFieldMissingException {
		List<Integer> summonsSerialNums = new ArrayList<Integer>();

		for (DocumentViewBO docView : roadCompDocView) {
			if (docView.getDocumentTypeCode().trim().equalsIgnoreCase(Constants.DocumentType.SUMMONS) && docView.getJpIdNumber() != null && IdNumber != null) {
				summonsSerialNums.add(docView.getAutomaticSerialNo());
			}

		}

		if (summonsSerialNums.size() > 0) {

			this.getDocumentsManagerService().addJPDetailsFromRoadCheck(summonsSerialNums, IdNumber, isHandHeld, this.getRomsLoggedInUser().getUsername());
		}
	}

	@SuppressWarnings("static-access")
	private DocumentViewBO convertFromRoadCompDocViewToDocManDocView(DocumentViewBO roadCompDocView) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

		DocumentViewBO docManagerDocView = new DocumentViewBO();

		try {
			this.copyFields(docManagerDocView, roadCompDocView);

			List<OffenceBO> docOffence = new ArrayList<OffenceBO>();

			if (roadCompDocView.getListOfOffences().size() > 0) {

				//if (roadCompDocView.getListOfOffences().get(0) instanceof OffenceBO) {
					for (Object offence : roadCompDocView.getListOfOffences()) {
						OffenceBO copyOffence = new OffenceBO();

						this.copyFields(copyOffence, offence);

						docOffence.add(copyOffence);
					}

					docManagerDocView.getListOfOffences().clear();

					docManagerDocView.getListOfOffences().addAll(docOffence);
				//}

			}

			if (roadCompDocView.getListOfVehicleOwners().size() > 0) {

				List<VehicleOwnerBO> vehicleOwners = new ArrayList<VehicleOwnerBO>();

				//if (roadCompDocView.getListOfVehicleOwners().get(0) instanceof VehicleOwnerBO) {
					for (Object vehilceOwner : roadCompDocView.getListOfVehicleOwners()) {
						VehicleOwnerBO copyVehicleOwner = new VehicleOwnerBO();
						this.copyFields(copyVehicleOwner, vehilceOwner);

						vehicleOwners.add(copyVehicleOwner);
					}

					docManagerDocView.getListOfVehicleOwners().clear();
					docManagerDocView.getListOfVehicleOwners().addAll(vehicleOwners);
				//}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return docManagerDocView;
	}

	public void verifyJPPopUp(RequestContext context) throws Exception {
		@SuppressWarnings("unchecked")
		List<JPBO> jPList = (List<JPBO>) context.getFlowScope().get("jpList");
		
		logger.info("inside verifyJPPopUp() ");
		RoadCheckInitiateView roadCheckInaView = (RoadCheckInitiateView) context.getFlowScope().get("initiateView");

		RoadCheckReviewSummaryBean roadCheckReviewSummaryBean = (RoadCheckReviewSummaryBean) context.getFlowScope().get("roadCheckReviewSummaryBean");

		roadCheckReviewSummaryBean.setjPPin("");
		// roadCheckReviewSummaryBean.setjPRegNum("");
		roadCheckReviewSummaryBean.setjPIdNum(null);

		if (jPList == null) {
			jPList = new ArrayList<JPBO>();

			RoadOperationOtherDetailsBO roadOpDetails = null;

			if (roadCheckInaView.getRoadOperationBO() != null && roadCheckInaView.getRoadOperationBO().getRoadOperationId() != null)
				roadOpDetails = this.getRoadOperationService().findRoadOperationOtherDetails(roadCheckInaView.getRoadOperationBO().getRoadOperationId());

			if (this.isHandHeld(context)) {
				/* If on mobile device filter JP based on selected resources */
				for (AssignedTeamDetailsBO team : roadOpDetails.getAssignedTeamDetailsBOList()) {
					for (JPBO jp : team.getJpList()) {
						JPBO jpCopy = new JPBO();
						BaseServiceAction.copyFields(jpCopy, jp);
						PersonBO copyPerson = new PersonBO();
						BaseServiceAction.copyFields(copyPerson, jp.getPersonBO());
						jpCopy.setPersonBO(copyPerson);
						jPList.add(jpCopy);									
					}
				}		
				
				Collections.sort(jPList, new Comparator<JPBO>() {
					public int compare(JPBO result1, JPBO result2) {
						return result1.getPersonBO().getLastName().trim().compareToIgnoreCase(result2.getPersonBO().getLastName().trim());
					}
				});
					context.getFlowScope().put("jpList", jPList);
			} else if (!this.isHandHeld(context)) {
				/* This is an in house road check */
				//JpCriteriaBO jpCriteria = new JpCriteriaBO();
				
				logger.info("inside !this.isHandHeld(context) ");
				
				getJusticesOfPeaceListByOffenceParish(roadCheckInaView.getOffencePlace().getParishCode());

				Constants.UserPermissions userPerm = new Constants.UserPermissions();

//				if (roadCheckInaView.getRoadOperationBO() == null || roadCheckInaView.getRoadOperationBO().getRoadOperationId() == null) {
//					System.err.println("This is an unscheduled operation ");
//					/* This is an unscheduled operation */
//					jpCriteria.getRegionCodes().add(this.getRomsLoggedInUser().getLocationCode());
//					System.err.println("jpCriteria.getRegionCodes() size"+ jpCriteria.getRegionCodes().size());
//
//				} else {
//					System.err.println("This is an scheduled operation ");
//					/* This is an scheduled operation */
//					if (roadCheckInaView.getRoadOperationBO() != null)
//						//jpCriteria.getRegionCodes().addAll(roadCheckInaView.getRoadOperationBO().getOfficeLocCodeList());
//						jpCriteria.getRegionCodes().addAll(roadOpDetails.getOperationRegionList());
//					System.err.println("jpCriteria.getRegionCodes() size"+ jpCriteria.getRegionCodes().size());
//				}

				//jPList = this.getMaintenancePortProxy().lookupJP(jpCriteria);
				
				//System.err.println("jPList size"+ jPList.size());
			}

			

			//context.getFlowScope().put("jPList", jPList);

		}
		
		logger.info("inside verifyJPPopUp() ");
	}

	
public boolean validateForReprint(RequestContext context) {
		boolean pass = true;
		RoadCheckReviewSummaryBean roadCheckReviewSummaryBean = (RoadCheckReviewSummaryBean) context.getFlowScope().get("roadCheckReviewSummaryBean");

			

			if (StringUtils.trimToNull(roadCheckReviewSummaryBean.getRoadCheckReprintReasonCode())== null ) {
				addErrorMessageWithParameter(context, "RequiredFields", "Reprint Reason");
				pass = false;
			}
			if (StringUtils.isBlank(roadCheckReviewSummaryBean.getRoadCheckReprintComment())) {
				addErrorMessageWithParameter(context, "RequiredFields", "Comment");
				pass = false;
			}
			
			return pass;

		
}
	public Event verifyJP(RequestContext context) {
		
		
		RoadCheckReviewSummaryBean roadCheckReviewSummaryBean = (RoadCheckReviewSummaryBean) context.getFlowScope().get("roadCheckReviewSummaryBean");
	
		/*ONLY done when reprint reason is needed*/
		if(roadCheckReviewSummaryBean.isReprintReasonNeeded()){
			if(!validateForReprint(context)){
				logger.info("error");
				roadCheckReviewSummaryBean.setShouldYouPrint(false);
				return error();
			}
		}
		/*Ricardo Thompson
		 * 3/3/2015 
		 *This block was commented out as requested in Ticket Request 148231
		 *
		 */
		if (this.isHandHeld(context) && !roadCheckReviewSummaryBean.allSelectedSummonsSigned) {
			AuthorizationBO auth = new AuthorizationBO();
			if(roadCheckReviewSummaryBean.getjPIdNum() != null){
				//System.err.println("JP PIN " + roadCheckReviewSummaryBean.getjPIdNum().toString());
				auth.setUsername(roadCheckReviewSummaryBean.getjPIdNum().toString());
			}else{
				auth.setUsername("");
			}
			
			//auth.setPassword(roadCheckReviewSummaryBean.getjPPin());
		
	
			if (validateJPAuth(auth.getUsername(), auth.getPassword(), context)) {
				roadCheckReviewSummaryBean.setShouldYouPrint(false);
				return error();
			}else{
				auth.setUsername(roadCheckReviewSummaryBean.jPIdNum.toString());
				auth.setPersonType("jp");
				try {
					this.printAllDocuments(roadCheckReviewSummaryBean, context);
					roadCheckReviewSummaryBean.setAlreadyPrinted(true);
					roadCheckReviewSummaryBean.setShouldYouPrint(true);
					
					} catch (NumberFormatException e) {
						this.addErrorMessage(context, "SystemError");
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						this.addErrorMessage(context, "SystemError");
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						this.addErrorMessage(context, "SystemError");
						e.printStackTrace();
					} catch (NoSuchMethodException e) {
						this.addErrorMessage(context, "SystemError");
						e.printStackTrace();
					} catch (ErrorSavingException e) {
						this.addErrorMessage(context, "SystemError");
						e.printStackTrace();
					} catch (NoRecordFoundException e) {
						this.addErrorMessage(context, "SystemError");
						e.printStackTrace();
					/*} catch (RequiredFieldMissingException e) {
						this.addErrorMessage(context, "SystemError");
						e.printStackTrace();*/
					}catch (Exception e) {
						this.addErrorMessageText(context, "An error has occured while printing document(s).");
						e.printStackTrace();
					}
			}
	
		/*	try {
				// jreid 2014-07-01
				// QA advised that message is required when no pin is entered
				if (StringUtils.isBlank(roadCheckReviewSummaryBean.getjPPin())) {
					this.addErrorMessageText(context, "JP pin is required.");
				} else {
					boolean correctPin = this.getAuthorizationPortProxy().validatePerson(auth);
	
					if (correctPin) {
						this.printAllDocuments(roadCheckReviewSummaryBean, context);
	
						roadCheckReviewSummaryBean.setAlreadyPrinted(true);
						roadCheckReviewSummaryBean.setShouldYouPrint(true);
	
					} else {
						roadCheckReviewSummaryBean.setShouldYouPrint(false);
						this.addErrorMessageText(context, "The pin entered is not correct.");
					}
				}
			} catch (fsl.ta.toms.roms.webservices.authorization.NoRecordFoundException e) {
				this.addErrorMessageText(context, "JP and JP PIN required.");
				e.printStackTrace();
			} catch (Exception e) {
				this.addErrorMessageText(context, "An error has occured while printing document(s).");
				e.printStackTrace();
			}*/
		} else {
	
			try {
				this.printAllDocuments(roadCheckReviewSummaryBean, context);
				roadCheckReviewSummaryBean.setShouldYouPrint(true);
				// In house do not validate the JP
			} catch (NumberFormatException e) {
				this.addErrorMessage(context, "SystemError");
			} catch (IllegalAccessException e) {
				this.addErrorMessage(context, "SystemError");
	
			} catch (InvocationTargetException e) {
				this.addErrorMessage(context, "SystemError");
	
			} catch (NoSuchMethodException e) {
				this.addErrorMessage(context, "SystemError");
			} catch (ErrorSavingException e) {
				this.addErrorMessage(context, "SystemError");
			} catch (NoRecordFoundException e) {
				this.addErrorMessage(context, "SystemError");
	
			} catch (RequiredFieldMissingException e) {
				this.addErrorMessage(context, "FieldsWithAnAsterisk");
			}
	
		}//End bracket for jp pin validation else block 
		
		roadCheckReviewSummaryBean.setAlreadyPrinted(true);
		context.getFlowScope().put("roadCheckReviewSummaryBean", roadCheckReviewSummaryBean);
		return success();
		
	}
	
	public boolean validateJPAuth(String username, String password, RequestContext context) {
		Boolean error = false;
		MessageContext messages = context.getMessageContext();
	
		if (StringUtils.isBlank(username)) {
			messages.addMessage(new MessageBuilder().error().code("RequiredFields").arg("JP").build());
			error = true;
		}
	
		/*if (StringUtils.isBlank(password)) {
			messages.addMessage(new MessageBuilder().error().code("RequiredFields").arg("JP PIN").build());
			error = true;
		}*/
	
		return error;
	}


	public void printAllDocuments(RoadCheckReviewSummaryBean roadCheckReviewSummaryBean, RequestContext context) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, NumberFormatException, ErrorSavingException, NoRecordFoundException, RequiredFieldMissingException {
		List<DocumentViewBO> docsToPrint = new ArrayList<DocumentViewBO>();

		for (DocumentViewBO doc : (roadCheckReviewSummaryBean.isViewDetailsScreen ? roadCheckReviewSummaryBean.getSelectedDocViews() : roadCheckReviewSummaryBean.getDocViews())) {
			
			if(roadCheckReviewSummaryBean.isViewDetailsScreen)
			{
				//(coming out of discussions on Sept 7, 2015 relating to fixing issues logged to freedcamp)
				doc.setJpIdNumber(roadCheckReviewSummaryBean.getjPIdNum());
			}
			
			docsToPrint.add(this.convertFromRoadCompDocViewToDocManDocView(doc));

		}

		this.getDocumentsManagerService().printAllDocuments(docsToPrint, 
				roadCheckReviewSummaryBean.getRoadCheckReprintReasonCode() != null ? 
						Integer.parseInt(roadCheckReviewSummaryBean.getRoadCheckReprintReasonCode()) : null, 
						roadCheckReviewSummaryBean.getRoadCheckReprintComment(), this.getRomsLoggedInUser().getUsername());

		// At this point all the relevant documents are printable and setOpenReportWindow() function should take care of
		// the rest adding JP etc.
		this.setOpenReportWindow(true, context);
	}

	public void setAlreadyPrinted(RequestContext context) {
		RoadCheckReviewSummaryBean roadCheckReviewSummaryBean = (RoadCheckReviewSummaryBean) context.getFlowScope().get("roadCheckReviewSummaryBean");

		roadCheckReviewSummaryBean.setAlreadyPrinted(true);
	}

	/**
	 * This function is used to pull all the details for the road check in a Road Check Review Summary Bean which will
	 * be used to display the relevant information on the page.
	 * 
	 * @param context
	 */
	public Event getDetails(RequestContext context) {
		RoadCheckReviewSummaryBean roadCheckReviewSummaryBean = (RoadCheckReviewSummaryBean) context.getFlowScope().get("roadCheckReviewSummaryBean");

		if (roadCheckReviewSummaryBean == null)
			roadCheckReviewSummaryBean = new RoadCheckReviewSummaryBean();

		DataTableMemory dataTable = null;

		ComplianceBO compliance = null;

		if (context.getFlowScope().get("fromRoadCheck") != null && (Boolean) context.getFlowScope().get("fromRoadCheck")) {
			compliance = (ComplianceBO) context.getFlowScope().get("complianceBO");

		} else {
			roadCheckReviewSummaryBean = new RoadCheckReviewSummaryBean();
			dataTable = (DataTableMemory) context.getFlowScope().get("dataTable");
			compliance = (ComplianceBO) dataTable.getPageElements().getSelectedRow();
			context.getFlowScope().put("complianceBO", compliance);
		}

		try {
			ComplianceDetailsBO complianceDetails = this.getRoadCompliancyService().getComplianceDetails(compliance.getComplianceId());

			RoadCompliancyCriteriaBO complianceCriteria = new RoadCompliancyCriteriaBO();

			complianceCriteria.setComplianceId(compliance.getComplianceId());

			/*BadgeBO badge = null;

			try {
				badge = this.getBIMSWebServicePortProxy().getBadgeDetails(compliance.getPerson().getTrnNbr(), compliance.getPersonRole().substring(0, 1).toLowerCase());
			} catch (Exception exe) {

			}*/

			/* Set person details on the road check initiateView */

			RoadCheckInitiateView initiateView = new RoadCheckInitiateView();

			RoadOperationBO roadOp = new RoadOperationBO();

			BaseServiceAction.copyFields(roadOp, compliance.getRoadOperationBO());

			initiateView.setRoadOperationBO(roadOp);

			TAStaffBO roadOpTAStaff = new TAStaffBO();

			this.copyFields(roadOpTAStaff, compliance.getTaStaffBO());

			initiateView.setTaStaffBO(roadOpTAStaff);

			initiateView.setOffenceDate(compliance.getComplianceDate());

			/*if (badge != null) {

				String encodedString = Base64.encode(badge.getPhotoImg());
				initiateView.setPhotoURL("data:image/jpg;base64," + encodedString);

				if (badge.getBadgeDesc().toLowerCase().startsWith("c")) {
					initiateView.setConductionBadge(true);
				} else {
					initiateView.setDriverBadge(true);
				}
			}*/
			
			String encodedUrl = getPictureFromBIMS(compliance.getPerson().getTrnNbr(), initiateView);
			initiateView.setPhotoURL(encodedUrl);
			
			if (!StringUtils.isEmpty(compliance.getPersonRole())) {
				
				String personRole = "";
				
				if (compliance.getPersonRole().equalsIgnoreCase("c")) {
					personRole = "Conductor";
				} else if (compliance.getPersonRole().equalsIgnoreCase("d")) {
					personRole = "Driver";
				} else if (compliance.getPersonRole().equalsIgnoreCase("o")) {
					personRole = "Owner";
				} else if (compliance.getPersonRole().equalsIgnoreCase("t")) {
					if(StringUtils.isNotEmpty(compliance.getOtherRole())){
						HashMap filter = new HashMap<String, String>();
						filter.put("other_role_observed_id", compliance.getOtherRole());
						
						List<RefCodeBO> roleObserved = getRefInfo("roms_cd_other_role_observed", filter);
						
						personRole = "Other - "+roleObserved.get(0).getDescription();
					}else{
						personRole = "Other";
					}
				
				}
								
				roadCheckReviewSummaryBean.setRoleObserved(personRole);
			}

			initiateView.setTrn(compliance.getPerson().getTrnNbr());

			initiateView.setFirstName(compliance.getPerson().getFirstName());

			initiateView.setMiddleName(compliance.getPerson().getMiddleName());

			initiateView.setLastName(compliance.getPerson().getLastName());

			AddressView addressView = new AddressView();

			addressView.setMarkText(compliance.getPerson().getMarkText());

			addressView.setParish(compliance.getPerson().getParishCode());
			
			addressView.setParishDescription(compliance.getPerson().getParishDesc());

			addressView.setPoBoxNo(compliance.getPerson().getPoBoxNo());

			addressView.setPoLocationName(compliance.getPerson().getPoLocationName());

			addressView.setStreetName(compliance.getPerson().getStreetName());

			addressView.setStreetNo(compliance.getPerson().getStreetNo());

			addressView.setAddressLine1(compliance.getPerson().getAddressLine1());

			addressView.setAddressLine2(compliance.getPerson().getAddressLine2());

			addressView.setAddressLine2NL(compliance.getPerson().getAddressLine2NL());

			initiateView.setAddressView(addressView);

			initiateView.setHomePhoneNo(compliance.getPerson().getTelephoneHome());

			initiateView.setWorkPhoneNo(compliance.getPerson().getTelephoneWork());

			initiateView.setMobilePhoneNo(compliance.getPerson().getTelephoneCell());

			if (compliance.getCompliancyArteryid() != null && compliance.getCompliancyArteryid() > 0) {
				ArteryCriteriaBO arteryCrit = new ArteryCriteriaBO();

				arteryCrit.setArteryId(compliance.getCompliancyArteryid());

				ArteryBO roadOpArtery = new ArteryBO();
				ArteryBO maintArtery = this.getMaintenanceService().lookupArtery(arteryCrit).get(0);

				this.copyFields(roadOpArtery, maintArtery);
				initiateView.setOffencePlace(roadOpArtery);
			}

			/* Create a list of all the offences */
			List<OffenceOutcomeView> allOffenceOutcomeViews = new ArrayList<OffenceOutcomeView>();

			List<OffenceOutcomeView> firstTenOffenceOutcomeViews = new ArrayList<OffenceOutcomeView>();

			/* Create list of recorded */

			// Create object to store list queries performed and results.
			List<Entry<String, String>> listOfQueriesPerformed = new ArrayList<Entry<String, String>>();

			for (RoadCheckBO roadCheck : compliance.getListOfRoadChecks()) {
				Integer count = 0;

				for (OffenceBO offence : compliance.getListOfOffences()) {
					if (offence.getRoadCheckTypeId() != null && offence.getRoadCheckTypeId().trim().equalsIgnoreCase(roadCheck.getRoadCheckTypeID().trim()))
						count++;

				}

				String roadCheckType = "";

				if (roadCheck.getRoadCheckTypeID().trim().equalsIgnoreCase(Constants.RoadCheckType.BADGE))
					roadCheckType = "Badge";
				else if (roadCheck.getRoadCheckTypeID().trim().equalsIgnoreCase(Constants.RoadCheckType.CITATION)) {
					roadCheckType = "Citation";

				} else if (roadCheck.getRoadCheckTypeID().trim().equalsIgnoreCase(Constants.RoadCheckType.DRIVERS_LICENCE))
					roadCheckType = "Drivers Licence";
				else if (roadCheck.getRoadCheckTypeID().trim().equalsIgnoreCase(Constants.RoadCheckType.MOTOR_VEHICLE))
					roadCheckType = "Motor Vehicle";
				else if (roadCheck.getRoadCheckTypeID().trim().equalsIgnoreCase(Constants.RoadCheckType.OTHER))
					roadCheckType = "Other";
				else if (roadCheck.getRoadCheckTypeID().trim().equalsIgnoreCase(Constants.RoadCheckType.ROAD_LICENCE))
					roadCheckType = "Road Licence";

				if (count > 0)
					listOfQueriesPerformed.add(new java.util.AbstractMap.SimpleEntry<String, String>(roadCheckType, " [" + count + "] Offence(s)"));
				else
					listOfQueriesPerformed.add(new java.util.AbstractMap.SimpleEntry<String, String>(roadCheckType, "All is in order"));
			}

			for (OffenceBO offence : compliance.getListOfOffences()) {

				/* Create and add an offence outcome view to a list of all offences */
				OffenceOutcomeView offenceOutView = new OffenceOutcomeView();

				offenceOutView.setOffenceId(offence.getOffenceId());
				offenceOutView.setOffenceShortDescription(offence.getShortDescription());
				offenceOutView.setRoadCheckType(offence.getRoadCheckTypeId());

				for (RoadCheckOffenceOutcomeBO outcome : complianceDetails.getListOfOutcomes()) {
					if (outcome.getOffenceBO().getOffenceId().equals(offenceOutView.getOffenceId())) {
						if (outcome.getOutcomeTypeID().trim().equalsIgnoreCase(Constants.OutcomeType.ISSUE_SUMMONS))
							offenceOutView.setIssueSummons(true);
						else if (outcome.getOutcomeTypeID().trim().equalsIgnoreCase(Constants.OutcomeType.ISSUE_WARNING_NOTICE))
							offenceOutView.setIssuewWarningNotice(true);
						else if (outcome.getOutcomeTypeID().trim().equalsIgnoreCase(Constants.OutcomeType.REMOVE_PLATES))
							offenceOutView.setRemovePlate(true);
						else if (outcome.getOutcomeTypeID().trim().equalsIgnoreCase(Constants.OutcomeType.WARNED_FOR_PROSECUTION))
							offenceOutView.setWarnForProsecution(true);
						else if (outcome.getOutcomeTypeID().trim().equalsIgnoreCase(Constants.OutcomeType.WARNED_NO_PROSECUTION))
							offenceOutView.setIssueWarningNP(true);
					}
				}

				allOffenceOutcomeViews.add(offenceOutView);

				if (firstTenOffenceOutcomeViews.size() < 10)
					firstTenOffenceOutcomeViews.add(offenceOutView);

			}

			/* Add List of Document to display in the context */
			roadCheckReviewSummaryBean.setDocViews(complianceDetails.getListOfDocuments());

			/* Get all supporting details */
			if (complianceDetails.getPoundDetails() != null) {
				roadCheckReviewSummaryBean.setPound(new PoundBO());

				roadCheckReviewSummaryBean.getPound().setMarkText(complianceDetails.getPoundDetails().getMarkText());
				roadCheckReviewSummaryBean.getPound().setNumberOfLots(complianceDetails.getPoundDetails().getNumberOfLots());
				roadCheckReviewSummaryBean.getPound().setNumberOfParkingSpaces(complianceDetails.getPoundDetails().getNumberOfParkingSpaces());
				roadCheckReviewSummaryBean.getPound().setParishCode(complianceDetails.getPoundDetails().getParishCode());
				roadCheckReviewSummaryBean.getPound().setParishName(complianceDetails.getPoundDetails().getParishName());
				roadCheckReviewSummaryBean.getPound().setPoBoxNo(complianceDetails.getPoundDetails().getPoBoxNo());
				roadCheckReviewSummaryBean.getPound().setPoLocationName(complianceDetails.getPoundDetails().getPoLocationName());
				roadCheckReviewSummaryBean.getPound().setPoundName(complianceDetails.getPoundDetails().getPoundName());
				roadCheckReviewSummaryBean.getPound().setShortDesc(complianceDetails.getPoundDetails().getShortDesc());
				roadCheckReviewSummaryBean.getPound().setStatusDescription(complianceDetails.getPoundDetails().getStatusDescription());
				roadCheckReviewSummaryBean.getPound().setStatusId(complianceDetails.getPoundDetails().getStatusId());
				roadCheckReviewSummaryBean.getPound().setStreetName(complianceDetails.getPoundDetails().getStreetName());
				roadCheckReviewSummaryBean.getPound().setStreetNo(complianceDetails.getPoundDetails().getStreetNo());
				roadCheckReviewSummaryBean.getPound().setTelephoneCell(complianceDetails.getPoundDetails().getTelephoneCell());
				roadCheckReviewSummaryBean.getPound().setTelephoneHome(complianceDetails.getPoundDetails().getTelephoneHome());
				roadCheckReviewSummaryBean.getPound().setTelephoneWork(complianceDetails.getPoundDetails().getTelephoneWork());
			}

			if (complianceDetails.getWreckingCompanyDetails() != null) {
				roadCheckReviewSummaryBean.setWreckingCompany(new WreckingCompanyBO());

				roadCheckReviewSummaryBean.getWreckingCompany().setCompanyName(complianceDetails.getWreckingCompanyDetails().getCompanyName());
				roadCheckReviewSummaryBean.getWreckingCompany().setContactPersonFirstName(complianceDetails.getWreckingCompanyDetails().getContactPersonFirstName());
				roadCheckReviewSummaryBean.getWreckingCompany().setContactPersonLastName(complianceDetails.getWreckingCompanyDetails().getContactPersonLastName());
				roadCheckReviewSummaryBean.getWreckingCompany().setContactPersonMiddleName(complianceDetails.getWreckingCompanyDetails().getContactPersonMiddleName());
				roadCheckReviewSummaryBean.getWreckingCompany().setMarkText(complianceDetails.getWreckingCompanyDetails().getMarkText());
				roadCheckReviewSummaryBean.getWreckingCompany().setParishCode(complianceDetails.getWreckingCompanyDetails().getParishCode());
				roadCheckReviewSummaryBean.getWreckingCompany().setParishName(complianceDetails.getWreckingCompanyDetails().getParishName());
				roadCheckReviewSummaryBean.getWreckingCompany().setPoBoxNo(complianceDetails.getWreckingCompanyDetails().getPoBoxNo());
				roadCheckReviewSummaryBean.getWreckingCompany().setPoLocationName(complianceDetails.getWreckingCompanyDetails().getPoLocationName());
				roadCheckReviewSummaryBean.getWreckingCompany().setShortDesc(complianceDetails.getWreckingCompanyDetails().getShortDesc());
				roadCheckReviewSummaryBean.getWreckingCompany().setStatusDescription(complianceDetails.getWreckingCompanyDetails().getStatusDescription());
				roadCheckReviewSummaryBean.getWreckingCompany().setStatusId(complianceDetails.getWreckingCompanyDetails().getStatusId());
				roadCheckReviewSummaryBean.getWreckingCompany().setStreetName(complianceDetails.getWreckingCompanyDetails().getStreetName());
				roadCheckReviewSummaryBean.getWreckingCompany().setStreetNo(complianceDetails.getWreckingCompanyDetails().getStreetNo());
				roadCheckReviewSummaryBean.getWreckingCompany().setTelephoneCell(complianceDetails.getWreckingCompanyDetails().getTelephoneCell());
				roadCheckReviewSummaryBean.getWreckingCompany().setTelephoneHome(complianceDetails.getWreckingCompanyDetails().getTelephoneHome());
				roadCheckReviewSummaryBean.getWreckingCompany().setTelephoneWork(complianceDetails.getWreckingCompanyDetails().getTelephoneWork());
				roadCheckReviewSummaryBean.getWreckingCompany().setTrnBranch(complianceDetails.getWreckingCompanyDetails().getTrnBranch());
				roadCheckReviewSummaryBean.getWreckingCompany().setTrnNbr(complianceDetails.getWreckingCompanyDetails().getTrnNbr());
				roadCheckReviewSummaryBean.getWreckingCompany().setWreckingCompanyId(complianceDetails.getWreckingCompanyDetails().getWreckingCompanyId());

			}

			if (compliance.getVehicle() != null) {
				initiateView.setColour(compliance.getVehicle().getColour());

				initiateView.setEngineNo(compliance.getVehicle().getEngineNo() != null ? compliance.getVehicle().getEngineNo() : "");

				initiateView.setMakeDescription(compliance.getVehicle().getMakeDescription());

				initiateView.setPlateRegNo(compliance.getVehicle().getPlateRegNo());

				initiateView.setModel(compliance.getVehicle().getModel());

				initiateView.setChassisNo(compliance.getVehicle().getChassisNo() != null ? compliance.getVehicle().getChassisNo() : "");

				initiateView.setYear(compliance.getVehicle().getYear());

			}

			SupportingDetailsView supportingDetailsView = new SupportingDetailsView();

			if (complianceDetails.getListOfCourts() != null && complianceDetails.getListOfCourts().size() > 0) {
				CourtBO court = complianceDetails.getListOfCourts().get(0);

				roadCheckReviewSummaryBean.setCourt(new CourtBO());

				roadCheckReviewSummaryBean.getCourt().setPoLocationName(court.getPoLocationName());

				roadCheckReviewSummaryBean.getCourt().setShortDescription(court.getShortDescription());

				roadCheckReviewSummaryBean.getCourt().setParishName(court.getParishName());

				if(compliance.getRoadOperationBO() != null && compliance.getRoadOperationBO().getCourtDTime() != null)
				{
					supportingDetailsView.setCourtDateTime(compliance.getCourtDate());
				}
				
				
				if(supportingDetailsView.getCourtDateTime() == null && compliance.getCourtDate() != null) 
				{
					supportingDetailsView.setCourtDateTime(compliance.getCourtDate());
				}
				
				if(supportingDetailsView.getCourtDateTime() == null)
				{
					for(RoadCheckOffenceOutcomeBO outcome : complianceDetails.getListOfOutcomes())
					{
						if(outcome.getCourtDateTime() != null)
						{
							supportingDetailsView.setCourtDateTime(outcome.getCourtDateTime());
							break;
						}
					}
				}

			}

			if (complianceDetails.getVehicleMoverType() != null) {
				supportingDetailsView.setVehicleMoverType(complianceDetails.getVehicleMoverType());
			} else {
				supportingDetailsView.setVehicleMoverType(null);
			}

			if (complianceDetails.getVehicleMover() != null) {
				supportingDetailsView.setVehicleMoverBO(complianceDetails.getVehicleMover());
			} else {
				supportingDetailsView.setVehicleMoverBO(null);
			}

			if (complianceDetails.getWreckerVehicle() != null) {
				supportingDetailsView.setWreckerVehicleBO(complianceDetails.getWreckerVehicle());
			} else {
				supportingDetailsView.setWreckerVehicleBO(null);
			}

			for (ComplianceParamBO comParam : complianceDetails.getListOfComplianceParams()) {
				if (comParam.getParamName().toLowerCase().startsWith("directive_of_inspector"))
					supportingDetailsView.setDirectiveParam(comParam.getParamValue());
				else if (comParam.getParamName().toLowerCase().startsWith("inspector_threatened")) {
					TAStaffBO inspectorThreatened = new TAStaffBO();

					inspectorThreatened.setFullName(comParam.getParamValue());

					supportingDetailsView.setInspectorParam(inspectorThreatened);

				} else if (comParam.getParamName().toLowerCase().startsWith("number_of_passengers"))
					supportingDetailsView.setNoOfPassengers(Integer.parseInt(comParam.getParamValue()));

			}

			supportingDetailsView.setAllegation(complianceDetails.getAllegation());

			if (complianceDetails.getListOfDocuments().size() > 0 && complianceDetails.getListOfDocuments().get(0).getCourtDtime() != null)
				complianceDetails.getListOfDocuments().get(0).getCourtDtime();

			if (complianceDetails.getListOfWitnesses().size() > 0) {
				RecordOffenceOutcomeView recordOffenceOutcomeView = new RecordOffenceOutcomeView();

				recordOffenceOutcomeView.setWarningNoticeSelected(true);

				context.getFlowScope().put("recordOffenceOutcomeView", recordOffenceOutcomeView);

				supportingDetailsView.setWitnessList(new ArrayList<WitnessView>());

				for (PersonBO witnessRoadComp : complianceDetails.getListOfWitnesses()) {
					WitnessView witness = new WitnessView();

					witness.setFirstName(witnessRoadComp.getFirstName());
					witness.setMiddleName(witnessRoadComp.getMiddleName());
					witness.setLastName(witnessRoadComp.getLastName());

					witness.setAddressView(new AddressView());

					witness.getAddressView().setAddressLine1(witnessRoadComp.getAddressLine1());
					witness.getAddressView().setAddressLine2(witnessRoadComp.getAddressLine2());
					witness.getAddressView().setAddressLine2NL(witnessRoadComp.getAddressLine2NL());
					witness.getAddressView().setMarkText(witnessRoadComp.getMarkText());
					witness.getAddressView().setParish(witnessRoadComp.getParishDescription());
					witness.getAddressView().setPoBoxNo(witnessRoadComp.getPoBoxNo());
					witness.getAddressView().setPoLocationName(witnessRoadComp.getPoLocationName());
					witness.getAddressView().setStreetName(witnessRoadComp.getStreetName());
					witness.getAddressView().setStreetNo(witnessRoadComp.getStreetNo());
					witness.setParishDesc(witnessRoadComp.getParishDesc());
					
					witness.setHomePhoneNo(witnessRoadComp.getTelephoneHome());
					witness.setMobilePhoneNo(witnessRoadComp.getTelephoneCell());
					witness.setWorkPhoneNo(witnessRoadComp.getTelephoneWork());

					supportingDetailsView.getWitnessList().add(witness);

				}
			}

			
			if(compliance.getAidAbetVehicleOwner()!=null){
				supportingDetailsView.setIssueToOwner(true);
				
				VehicleOwnerBO ownerBO = new VehicleOwnerBO();
				
				ownerBO.setTrnNbr(compliance.getAidAbetVehicleOwner().getTrnNbr());
				ownerBO.setFirstName(compliance.getAidAbetVehicleOwner().getFirstName());
				ownerBO.setLastName(compliance.getAidAbetVehicleOwner().getLastName());
				
				supportingDetailsView.setOwner(ownerBO);
				
			}else{
				supportingDetailsView.setIssueToOwner(false);
			}
				
			
			
			supportingDetailsView.setComment(compliance.getComment());

			// put in supporting details view
			context.getFlowScope().put("supportingDetailsView", supportingDetailsView);

			// put all offence outcomes in flow scope
			context.getFlowScope().put("allOffenceOutcomeViews", allOffenceOutcomeViews);

			// put first 10 offence outcomes in flow scope
			context.getFlowScope().put("firstTenOffenceOutcomeViews", firstTenOffenceOutcomeViews);

			context.getFlowScope().put("currentOffenceOutcomeList", firstTenOffenceOutcomeViews);

			/* Put list of offence queries in flow scope */
			context.getFlowScope().put("listOfQueriesPerformed", listOfQueriesPerformed);

			context.getFlowScope().put("initiateView", initiateView);

			roadCheckReviewSummaryBean.setViewDetailsScreen(true);

			context.getFlowScope().put("roadCheckReviewSummaryBean", roadCheckReviewSummaryBean);

			return success();

		} catch (Exception e) {

			e.printStackTrace();
			this.addErrorMessageText(context, "Sorry there was an error retrieving road check details.");

			return error();
		}

	}

	public void updateDocumentInTable(RequestContext context, DocumentViewBO documentFromRoadCheck) {

		RoadCheckReviewSummaryBean roadCheckReviewSummaryBean = (RoadCheckReviewSummaryBean) context.getFlowScope().get("roadCheckReviewSummaryBean");

		for (DocumentViewBO docView : roadCheckReviewSummaryBean.getDocViews()) {
			if (docView.getAutomaticSerialNo().equals(documentFromRoadCheck.getAutomaticSerialNo())) {
				try {
					this.copyFields(docView, documentFromRoadCheck);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	public DocumentViewBO getDocumentViewFromDocMan(RequestContext context) {
		RoadCheckReviewSummaryBean roadCheckReviewSummaryBean = (RoadCheckReviewSummaryBean) context.getFlowScope().get("roadCheckReviewSummaryBean");

		DocumentViewBO documentFromRoadCheck = new DocumentViewBO();

		try {
			BaseServiceAction.copyFields(documentFromRoadCheck, roadCheckReviewSummaryBean.getSelectedDocViews().get(0));

			//set the role observed on the document to view
			documentFromRoadCheck.setOffenderRoleObserved(roadCheckReviewSummaryBean.getRoleObserved());
			
			
			return documentFromRoadCheck;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public void checkSelectedDocuments(RequestContext context) 
	{
		
		logger.info("In Check Selected Documents Function!");
		
		removeUprintableDocuments();

		RoadCheckReviewSummaryBean roadCheckReviewSummaryBean = (RoadCheckReviewSummaryBean) context.getFlowScope().get("roadCheckReviewSummaryBean");

		int countUnsignedSummons = 0;

		int countNeedReprintReason = 0;

		for (DocumentViewBO doc : roadCheckReviewSummaryBean.selectedDocViews) {
			if (doc.getDocumentTypeCode().trim().equalsIgnoreCase(Constants.DocumentType.SUMMONS) && (doc.getJpIdNumber() == null)) {
				countUnsignedSummons++;
			}

			if (doc.getDocumentTypeCode().trim().equalsIgnoreCase(Constants.DocumentType.SUMMONS)) {
				roadCheckReviewSummaryBean.setSummonsPresent(true);
			}

			if (doc.getPrintCount() != null && doc.getPrintCount() > 0) {
				countNeedReprintReason++;
			}
		}

		if (countNeedReprintReason > 0) {
			context.getFlowScope().put("reasonList", getReasonsByModuleType(Constants.ReasonTypeCode.DOCUMENT_REPRINTED));

			roadCheckReviewSummaryBean.setReprintReasonNeeded(true);
		} else {
			roadCheckReviewSummaryBean.setReprintReasonNeeded(false);
		}

		if (countUnsignedSummons > 0) {
			roadCheckReviewSummaryBean.setAllSelectedSummonsSigned(false);

			try {

				verifyJPPopUp(context);
			} catch (Exception e) {

				e.printStackTrace();
			}

		} else {
			roadCheckReviewSummaryBean.setAllSelectedSummonsSigned(true);
		}
	}
	
	public void printAllDocs(RequestContext context)
	{
		logger.info("In Print All Docs Function!");
		RoadCheckReviewSummaryBean roadCheckReviewSummaryBean = (RoadCheckReviewSummaryBean) context.getFlowScope().get("roadCheckReviewSummaryBean");
		
		if(roadCheckReviewSummaryBean.getSelectedDocViews() == null)
		{
			roadCheckReviewSummaryBean.setSelectedDocViews(new ArrayList<DocumentViewBO>());
		}
		
		for(DocumentViewBO doc : roadCheckReviewSummaryBean.getDocViews())
		{
			roadCheckReviewSummaryBean.getSelectedDocViews().add(doc);
		}
		
		context.getFlowScope().put("roadCheckReviewSummaryBean", roadCheckReviewSummaryBean);
		
		checkSelectedDocuments(context);
	}

	public void checkDocumentsFromPrepareAndPrint(RequestContext context) {
		RoadCheckReviewSummaryBean roadCheckReviewSummaryBean = (RoadCheckReviewSummaryBean) context.getFlowScope().get("roadCheckReviewSummaryBean");

		int countUnsignedSummons = 0;

		for (DocumentViewBO doc : roadCheckReviewSummaryBean.docViews) {
			if (doc.getDocumentTypeCode().trim().equalsIgnoreCase(Constants.DocumentType.SUMMONS) && doc.getJpIdNumber() == null) {
				countUnsignedSummons++;
			}

			if (doc.getDocumentTypeCode().trim().equalsIgnoreCase(Constants.DocumentType.SUMMONS)) {
				roadCheckReviewSummaryBean.setSummonsPresent(true);
			}

		}

		if (countUnsignedSummons > 0) {
			roadCheckReviewSummaryBean.setAllSelectedSummonsSigned(false);

			try {

				verifyJPPopUp(context);
			} catch (Exception e) {

				e.printStackTrace();
			}

		} else {
			roadCheckReviewSummaryBean.setAllSelectedSummonsSigned(true);
		}
	}

	public void addJP(RequestContext context) {
		RoadCheckReviewSummaryBean roadCheckReviewSummaryBean = (RoadCheckReviewSummaryBean) context.getFlowScope().get("roadCheckReviewSummaryBean");

		roadCheckReviewSummaryBean.setShouldYouPrint(true);
	}

	public ComplianceBO getCurrentComplianceBO(RequestContext context) {

		DataTableMemory dataTable = (DataTableMemory) context.getFlowScope().get("dataTable");

		ComplianceBO compliance = (ComplianceBO) dataTable.getPageElements().getSelectedRow();

		return compliance;
	}

	public Event continueRoadCheck(RequestContext context) {
		ComplianceBO compliance = (ComplianceBO) context.getFlowScope().get("complianceBO");

		if (compliance != null) {
			try {
		
				/* Get compliance Details */
				ComplianceDetailsBO complianceDetails = null;

				try {
					
					logger.info("compliance.getComplianceId(): " + compliance.getComplianceId());
					complianceDetails = this.getRoadCompliancyService().getComplianceDetails(compliance.getComplianceId());
				} catch (Exception e) {
					e.printStackTrace();
				}

				RoadCheckInitiateView initiateView = new RoadCheckInitiateView();

				try {
					initiateView.setFromSearch(true);
					initiateView.setComplianceId(compliance.getComplianceId());

					if(compliance.getCompliancyArteryid() != null && compliance.getCompliancyArteryid() > 0){
						ArteryCriteriaBO arteryCrit = new ArteryCriteriaBO();
	
						arteryCrit.setArteryId(compliance.getCompliancyArteryid());
	
						List<ArteryBO> arteryList = this.getMaintenanceService().lookupArtery(arteryCrit);
	
						if (arteryList.size() > 0) {
							ArteryBO artery = convertMainArteryToRoadOpArtery(arteryList.get(0));
	
							initiateView.setOffencePlace(artery);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				initiateView.setTaStaffBO(this.convertCompStaffToRoadOpStaff(compliance.getTaStaffBO()));

				initiateView.setOffenceDate(compliance.getComplianceDate());

				initiateView.setActivityType((compliance.getRoadOperationBO() == null || compliance.getRoadOperationBO().getRoadOperationId() == null) ? "U" : "S");

				RoadOperationBO roadOp = this.convertCompRoadOpToRoadOpRoadOp(compliance.getRoadOperationBO());
				initiateView.setRoadOperationBO(roadOp);

				AddressView addressView = new AddressView();

				addressView.setMarkText(compliance.getPerson().getMarkText());
				addressView.setParish(compliance.getPerson().getParishCode());
				addressView.setPoBoxNo(compliance.getPerson().getPoBoxNo());
				addressView.setPoLocationName(compliance.getPerson().getPoLocationName());
				addressView.setStreetName(compliance.getPerson().getStreetName());
				addressView.setStreetNo(compliance.getPerson().getStreetNo());

				initiateView.setAddressView(addressView);
				
				initiateView.setMobilePhoneNo(compliance.getPerson().getTelephoneCell());
				initiateView.setHomePhoneNo(compliance.getPerson().getTelephoneHome());
				initiateView.setWorkPhoneNo(compliance.getPerson().getTelephoneWork());
				
				

				BadgeBO badge = null;

				// get badge information from bims web service
				/*try {
					badge = this.getBIMSWebServicePortProxy().getBadgeDetails(compliance.getPerson().getTrnNbr(),
					compliance.getPersonRole());
					//String badgeString = getPictureFromBIMS(compliance.getPerson().getTrnNbr(), initiateView);
					initiateView.setBadgeNo(badge.getBadgeNo().toString());

					 if(badge.getBadgeDesc().toLowerCase().startsWith("d")) initiateView.setDriverBadge(true); else
					 if(badge.getBadgeDesc().toLowerCase().startsWith("c")) initiateView.setConductionBadge(true); 

					// Photo
					String noCache = "&nocache=" + Math.random(); //hack to prevent caching
					if(badge.getPhotoImg()!=null) { initiateView.setPhotoURL("/PhotoServlet?badgeNo=" +
						badge.getTrn() + "&badgeType=" + badge.getBadgeDesc() + "&serviceType=" + "BIMS"+ noCache);
						String encodedString = Base64.encode(badge.getPhotoImg());
						initiateView.setPhotoURL("data:image/jpg;base64,"+encodedString);
						initiateView.setShowPhoto(true); } 
				} catch (Exception e) {
					e.printStackTrace();
				}*/

				initiateView.setTrn(compliance.getPerson().getTrnNbr());
				String encodedUrl = getPictureFromBIMS(compliance.getPerson().getTrnNbr(), initiateView);
				
				if(StringUtil.isSet(encodedUrl)){
					initiateView.setShowPhoto(true);
					initiateView.setPhotoURL(encodedUrl);
				}

				if (compliance.getPersonRole().toLowerCase().startsWith("d"))
					initiateView.setRoleObserved("D");
				else if (compliance.getPersonRole().toLowerCase().startsWith("c"))
					initiateView.setRoleObserved("C");
				else if (compliance.getPersonRole().toLowerCase().startsWith("o"))
					initiateView.setRoleObserved("O");
				else if (compliance.getPersonRole().toLowerCase().startsWith("t"))
					initiateView.setRoleObserved("T");
				
				initiateView.setOtherRoleId(compliance.getOtherRole());
				

				initiateView.setFirstName(compliance.getPerson().getFirstName());
				initiateView.setLastName(compliance.getPerson().getLastName());
				initiateView.setMiddleName(compliance.getPerson().getMiddleName());
				
				initiateView.setVehicleInfoDifferent(compliance.getVehicleInfoDifferent());

				RecordOffenceOutcomeView recordOffenceOutcomeView = (RecordOffenceOutcomeView) context.getFlowScope().get("recordOffenceOutcomeView");
				/* Get Type of road checks carried out */
				for (RoadCheckBO roadCheck : compliance.getListOfRoadChecks()) {
					
					/*Check if query was performed before. Check check box if query was performed before*/
					if(roadCheck.getRoadCheckTypeID().equalsIgnoreCase(Constants.RoadCheckType.BADGE) && complianceDetails != null){
						initiateView.setBadgeQuery(true);
					}else if(roadCheck.getRoadCheckTypeID().equalsIgnoreCase(Constants.RoadCheckType.DRIVERS_LICENCE) && complianceDetails != null){
						initiateView.setDlQuery(true);
					}
					
					//System.err.println("roadCheck.getRoadCheckTypeID()::"+ roadCheck.getRoadCheckTypeID());
					if (roadCheck.getRoadCheckTypeID().equalsIgnoreCase(Constants.RoadCheckType.BADGE) && complianceDetails != null) {

						initiateView.setBadgeQueryDone(true);
						if(complianceDetails.getBadgeCheckResult() != null){

							initiateView.setPrevBadgeNo(complianceDetails.getBadgeCheckResult().getBadgeNumber());
	
							initiateView.setBadgeNo(complianceDetails.getBadgeCheckResult().getBadgeNumber());
	
							BadgeCheckResultBO badgeCheckResultBO = new BadgeCheckResultBO();
							badgeCheckResultBO.setComplianceID(initiateView.getComplianceId());
							badgeCheckResultBO.setBadgeNumber(initiateView.getBadgeNo());
							badgeCheckResultBO.setBadgeType(initiateView.getRoleObserved());
							badgeCheckResultBO.setCurrentUserName(getRomsLoggedInUser().getUsername()); // Change this
	
							try {
								badgeCheckResultBO = getRoadCompliancyService().performBadgeCheck(badgeCheckResultBO);
							} catch (NoRecordFoundException e) {
								initiateView.setNoBadgeFound(true);
	
							}
							if (badgeCheckResultBO == null) {
								initiateView.setNoBadgeFound(true);
								initiateView.setBadgeQueryDone(true);
	
							} else {
	
								String encodedString = Base64.encode(badgeCheckResultBO.getPhotograph());
								recordOffenceOutcomeView.setBadgePhotoURL("data:image/jpg;base64," + encodedString);
	
								context.getFlowScope().put("recordOffenceOutcomeView", recordOffenceOutcomeView);
								context.getFlowScope().put("badgeResult", badgeCheckResultBO);
							}
	
							context.getFlowScope().put("badgeResult", badgeCheckResultBO);
						}

						

					} else if (roadCheck.getRoadCheckTypeID().equalsIgnoreCase(Constants.RoadCheckType.ROAD_LICENCE) && complianceDetails != null )
					{
						initiateView.setRoadLicQuery(true);

						initiateView.setRlQueryDone(true);
						
						
						
						if(complianceDetails.getRoadLicCheckResult() != null  
								&& (complianceDetails.getRoadLicCheckResult().getLicenceNo() != null && complianceDetails.getRoadLicCheckResult().getLicenceNo() > 0))
						{
							initiateView.setPrevRoadLicNo(complianceDetails.getRoadLicCheckResult().getLicenceNo().toString());
	
							initiateView.setRoadLicNo(complianceDetails.getRoadLicCheckResult().getLicenceNo().toString());
							
							initiateView.setRoadLicenceOwners(complianceDetails.getRoadLicCheckResult().getRoadLicenceOwners());
							initiateView.setSeatCapacity(complianceDetails.getRoadLicCheckResult().getSeatCapacity());
							initiateView.setRouteStart(complianceDetails.getRoadLicCheckResult().getRouteStart());
							initiateView.setRouteEnd(complianceDetails.getRoadLicCheckResult().getRouteEnd());
							initiateView.setStatus(complianceDetails.getRoadLicCheckResult().getStatus());
							initiateView.setRouteStart(complianceDetails.getRoadLicCheckResult().getRouteStart());
							initiateView.setRouteEnd(complianceDetails.getRoadLicCheckResult().getRouteEnd());
							initiateView.setLicenceType(complianceDetails.getRoadLicCheckResult().getLicenceType());
							initiateView.setRoadLicExpiryDate(complianceDetails.getRoadLicCheckResult().getExpiryDate());
							
						}
						else
						{
							
							initiateView.setNoRlFound(true);
						}

						context.getFlowScope().put("rlResult", complianceDetails.getRoadLicCheckResult());

					} else if (roadCheck.getRoadCheckTypeID().equalsIgnoreCase(Constants.RoadCheckType.DRIVERS_LICENCE) && complianceDetails != null) {

						initiateView.setDlQueryDone(true);
						if(complianceDetails.getDlCheckResult() != null){
							initiateView.setPrevDlNo(complianceDetails.getDlCheckResult().getDlNo());
	
							initiateView.setPrevDlNoCH(complianceDetails.getDlCheckResult().getDlNo());
	
							initiateView.setDlNo(complianceDetails.getDlCheckResult().getDlNo());
	
							DLCheckResultBO DLCheckResultBO = new DLCheckResultBO();
							DLCheckResultBO.setComplianceID(compliance.getComplianceId());
							DLCheckResultBO.setDlNo(complianceDetails.getDlCheckResult().getDlNo());
							DLCheckResultBO.setCurrentUserName(getRomsLoggedInUser().getUsername());
	
							DLCheckResultBO = getRoadCompliancyService().performDriversLicenceCheck(DLCheckResultBO);
	
							if (DLCheckResultBO == null) {
								initiateView.setNoDLFound(true);
								initiateView.setDlQueryDone(true);
								// addErrorMessageWithParameter(context, "NoDLRecordfound",
								// roadCheckInitiateView.getRoadLicNo());
								// return error();
							} else {
								if (DLCheckResultBO.getPhotograph() != null) {
									// String noCache = "&nocache=" + Math.random();
									// recordOffenceOutcomeView.setDlPhotoURL("/PhotoServlet?dlNo=" +
									// DLCheckResultBO.getDlNo() + "&serviceType=" + "DL"+ noCache);
	
									String encodedString = Base64.encode(DLCheckResultBO.getPhotograph());
									recordOffenceOutcomeView.setDlPhotoURL("data:image/jpg;base64," + encodedString);
	
								}
	
							}
							context.getFlowScope().put("dlResult", DLCheckResultBO);
						}

						// context.getFlowScope().put("dlResult", complianceDetails.getDlCheckResult());

					} else if (roadCheck.getRoadCheckTypeID().equalsIgnoreCase(Constants.RoadCheckType.CITATION) && complianceDetails != null) {
						CitationCheckResultBO citationCheckResultBO = complianceDetails.getCitationCheckResult();

						initiateView.setCitationHistQuery(true);

						initiateView.setCitationHistQueryDone(true);

						if (citationCheckResultBO == null) {

							initiateView.setNoCitationFound(true);

						} else {
							
							initiateView.setIncludeTTMSResults(citationCheckResultBO.getIncludeTTMSResults());
							
							if(StringUtils.isEmpty(initiateView.getDlNo()))
							{
								initiateView.setPrevDlNo(citationCheckResultBO.getDlNo());

								initiateView.setPrevDlNoCH(citationCheckResultBO.getDlNo());

								initiateView.setDlNo(citationCheckResultBO.getDlNo());
							}

							List<CitationOffenceBO> personOffences = new ArrayList<CitationOffenceBO>();
							List<CitationOffenceBO> vehicleOffences = new ArrayList<CitationOffenceBO>();

//							if (citationCheckResultBO.getCitationOffences() != null) 
//							{
//
//								for (CitationOffenceBO citOff : citationCheckResultBO.getCitationOffences()) {
//									if (citOff.getDlNo() != null) {
//										if (citOff.getDlNo().equalsIgnoreCase(initiateView.getDlNo())) {
//											personOffences.add(citOff);
//										}
//									} else if (citOff.getTrnNbr() != null) {
//										if (citOff.getTrnNbr().equalsIgnoreCase(initiateView.getTrn())) {
//											personOffences.add(citOff);
//										}
//									}
//
//									if (citOff.getPlateRegNo() != null) {
//										if (citOff.getPlateRegNo().equalsIgnoreCase(initiateView.getPlateRegNo())) {
//											vehicleOffences.add(citOff);
//										}
//									}
//								}
//							}
							
							personOffences = citationCheckResultBO.getCitationOffences();
							vehicleOffences = citationCheckResultBO.getCitationOffencesVehicle();
							

							context.getFlowScope().put("ttmsOffences", citationCheckResultBO.getTrafficTickets());
							context.getFlowScope().put("taVehicleOffences", vehicleOffences);
							context.getFlowScope().put("taPersonOffences", personOffences);

						}

						String personLabel = "";
						NameUtil util = new NameUtil();
						String fullName = util.getLastNameCapsFirstNameMiddleName(compliance.getPerson().getFirstName(), compliance.getPerson().getLastName(), compliance.getPerson().getMiddleName());
						personLabel = "Offences Related to PERSON with Name: " + fullName;

						if (StringUtils.isNotBlank(compliance.getPerson().getTrnNbr())) {
							personLabel = personLabel + "; TRN: " + compliance.getPerson().getTrnNbr();
						}
						if (StringUtils.isNotBlank(complianceDetails.getCitationCheckResult().getDlNo())) {
							personLabel = personLabel + "; DL No.: " + complianceDetails.getCitationCheckResult().getDlNo();
						}
						initiateView.setCitationPersonLabel(personLabel);

						initiateView.setCitationVehicleLabel("Offences Related to MOTOR VEHICLE with Plate No.: " + (compliance.getVehicle() != null ? compliance.getVehicle().getPlateRegNo() : ""));

					} else if (roadCheck.getRoadCheckTypeID().equalsIgnoreCase(Constants.RoadCheckType.MOTOR_VEHICLE) && complianceDetails != null && complianceDetails.getVehicleCheckResult() != null) {

						//System.err.println("inside Constants.RoadCheckType.MOTOR_VEHICLE");
						initiateView.setChassisNo(complianceDetails.getVehicleCheckResult().getChassisNo());
						initiateView.setColour(complianceDetails.getVehicleCheckResult().getColour());
						initiateView.setEngineNo(complianceDetails.getVehicleCheckResult().getEngineNo());
						initiateView.setMakeDescription(complianceDetails.getVehicleCheckResult().getMakeDescription());
						initiateView.setModel(complianceDetails.getVehicleCheckResult().getModel());
						initiateView.setPlateRegNo(complianceDetails.getVehicleCheckResult().getPlateRegNo());
						initiateView.setPrevPlateRegNo(complianceDetails.getVehicleCheckResult().getPlateRegNo());
						initiateView.setPrevPlateRegNoCH(complianceDetails.getVehicleCheckResult().getPlateRegNo());
						initiateView.setYear(complianceDetails.getVehicleCheckResult().getYear());
						initiateView.setType(complianceDetails.getVehicleCheckResult().getTypeDesc());

						
						//System.err.println("complianceDetails.getVehicleCheckResult().getPlateRegNo()"+complianceDetails.getVehicleCheckResult().getPlateRegNo());
						context.getFlowScope().put("mvResult", complianceDetails.getVehicleCheckResult());
						initiateView.setDisableMotorVehicleFields(true);

					}else if (roadCheck.getRoadCheckTypeID().equalsIgnoreCase(Constants.RoadCheckType.MOTOR_VEHICLE) && complianceDetails != null && complianceDetails.getVehicleCheckResult() == null) {
						/*MV check is always done*/
					//	System.err.println("inside Constants.RoadCheckType.MOTOR_VEHICLE");
						initiateView.setChassisNo(compliance.getVehicle().getChassisNo());
						initiateView.setColour(compliance.getVehicle().getColour());
						initiateView.setEngineNo(compliance.getVehicle().getEngineNo());
						initiateView.setMakeDescription(compliance.getVehicle().getMakeDescription());
						initiateView.setModel(compliance.getVehicle().getModel());
						initiateView.setPlateRegNo(compliance.getVehicle().getPlateRegNo());
						initiateView.setPrevPlateRegNo(compliance.getVehicle().getPlateRegNo());
						initiateView.setPrevPlateRegNoCH(compliance.getVehicle().getPlateRegNo());
						initiateView.setYear(compliance.getVehicle().getYear());
						initiateView.setType(compliance.getVehicle().getTypeDesc());
												
						context.getFlowScope().put("mvResult",null);
						initiateView.setDisableMotorVehicleFields(false);
					

					}

				}

				
				
				//System.err.println("complianceDetails.getVehicleCheckResult()"+ complianceDetails.getVehicleCheckResult().getFitnessInfo().getExamDepot());
				//System.err.println("complianceDetails.getVehicleCheckResult().getPlateRegNo()"+complianceDetails.getVehicleCheckResult().getPlateRegNo());

				initiateView.setCompleteInitiate(true);
				context.getFlowScope().put("initiateView", initiateView);
				context.getFlowScope().put("continueRoadCheck", true);
				context.getFlowScope().put("recordOffenceOutcomeView", recordOffenceOutcomeView);

			} catch (Exception e) {
				e.printStackTrace();

				this.addErrorMessageText(context, "Error retrieving details.");

				return error();
			}

		}

		return success();
	}

	private ArteryBO convertMainArteryToRoadOpArtery(ArteryBO mainArtery) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		if (mainArtery != null) {
			ArteryBO roadOpArtery = new ArteryBO();

			roadOpArtery.setArteryDescription(mainArtery.getArteryDescription());
			roadOpArtery.setArteryId(mainArtery.getArteryId());
			roadOpArtery.setAuditEntryBO(this.convertMainAuditToRoadOpAudit(mainArtery.getAuditEntryBO()));
			roadOpArtery.setCreatedtime(mainArtery.getCreatedtime());
			roadOpArtery.setCreateusername(mainArtery.getCreateusername());
			roadOpArtery.setCurrentUsername(mainArtery.getCurrentUsername());
			roadOpArtery.setDistance(mainArtery.getDistance());
			roadOpArtery.setEndlatitude(mainArtery.getEndlatitude());
			roadOpArtery.setEndlongitude(mainArtery.getEndlongitude());
			// roadOpArtery.setLatitude(mainArtery.getStartlatitude());
			roadOpArtery.setLocationDescription(mainArtery.getLocationDescription());
			roadOpArtery.setLocationId(mainArtery.getLocationId());
			// roadOpArtery.setLongitude(mainArtery.getStartlongitude());
			roadOpArtery.setParishCode(mainArtery.getParishCode());
			roadOpArtery.setParishDescription(mainArtery.getParishDescription());
			roadOpArtery.setPoints(mainArtery.getPoints());
			roadOpArtery.setSelected(mainArtery.isSelected());
			roadOpArtery.setShortDescription(mainArtery.getShortDescription());
			roadOpArtery.setStartlatitude(mainArtery.getStartlongitude());
			roadOpArtery.setStartlongitude(mainArtery.getStartlongitude());
			roadOpArtery.setStatusDescription(mainArtery.getStatusDescription());
			roadOpArtery.setStatusId(mainArtery.getStatusId());

			return roadOpArtery;
		} else
			return null;

	}

	private RoadOperationBO convertCompRoadOpToRoadOpRoadOp(RoadOperationBO roadOpComp) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		if (roadOpComp != null) {
			RoadOperationBO roadOp = new RoadOperationBO();

			roadOp.setActualEndDate(roadOpComp.getActualEndDate());
			roadOp.setActualEndDtime(roadOpComp.getActualEndDtime());
			roadOp.setActualEndTime(roadOpComp.getActualEndTime());
			roadOp.setActualStartDate(roadOpComp.getActualStartDate());
			roadOp.setActualStartDtime(roadOpComp.getActualStartDtime());
			roadOp.setActualStartTime(roadOpComp.getActualStartTime());
			roadOp.setAuthorized(roadOpComp.getAuthorized());
			roadOp.setBackDated(roadOpComp.getBackDated());
			roadOp.setCategoryDescription(roadOpComp.getCategoryDescription());
			roadOp.setCategoryId(roadOpComp.getCategoryId());
			roadOp.setComment(roadOpComp.getComment());
			roadOp.setCourtBO(this.convertCompCourtToRoadOpCourt(roadOpComp.getCourtBO()));

			org.apache.commons.beanutils.PropertyUtilsBean utilCopy = new PropertyUtilsBean();

			roadOp.setCourtDate(roadOpComp.getCourtDate());
			roadOp.setCourtDTime(roadOpComp.getCourtDTime());
			roadOp.setCourtTime(roadOpComp.getCourtTime());
			roadOp.setCurrentUsername(roadOpComp.getCurrentUsername());

			roadOp.setOfficeLocCode(roadOpComp.getOfficeLocCode());
			roadOp.setOperationName(roadOpComp.getOperationName());

			roadOp.setReasonId(roadOpComp.getReasonId());
			roadOp.setRoadOperationId(roadOpComp.getRoadOperationId());

			roadOp.setScheduledEndDate(roadOpComp.getScheduledEndDate());
			roadOp.setScheduledEndDtime(roadOpComp.getScheduledEndDtime());
			roadOp.setScheduledEndTime(roadOpComp.getScheduledEndTime());
			roadOp.setScheduledStartDate(roadOpComp.getScheduledStartDate());
			roadOp.setScheduledStartDtime(roadOpComp.getScheduledStartDtime());
			roadOp.setScheduledStartTime(roadOpComp.getScheduledStartTime());
			roadOp.setStatusDescription(roadOpComp.getStatusDescription());
			roadOp.setStatusId(roadOpComp.getStatusId());

			roadOp.setScheduler(this.convertCompStaffToRoadOpStaff(roadOpComp.getScheduler()));

			roadOp.setStatusDtime(roadOpComp.getStatusDtime());

			return roadOp;
		} else
			return null;

	}

	private CourtBO convertCompCourtToRoadOpCourt(CourtBO courtComp) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		if (courtComp != null) {
			CourtBO courtROP = new CourtBO();

			try {
				this.copyFields(courtROP, courtComp);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// org.apache.commons.beanutils.PropertyUtilsBean utilCopy = new PropertyUtilsBean();
			//
			// courtROP.setCourtId(courtComp.getCourtId());
			// courtROP.setCurrentUsername(courtComp.getCurrentUsername());
			// courtROP.setDescription(courtComp.getDescription());
			// courtROP.setMarkText(courtComp.getMarkText());
			// courtROP.setParishCode(courtComp.getParishCode());
			// courtROP.setParishName(courtComp.getParishName());
			// courtROP.setPoBoxNo(courtComp.getPoBoxNo());
			// courtROP.setPoLocationName(courtComp.getPoLocationName());
			// courtROP.setShortDescription(courtComp.getShortDescription());
			// courtROP.setStatusDescription(courtComp.getStatusDescription());
			// courtROP.setStatusId(courtComp.getStatusId());
			// courtROP.setStreetName(courtComp.getStreetNo());
			// courtROP.setStreetNo(courtComp.getStreetNo());
			// //utilCopy.copyProperties(courtROP, courtComp);
			//
			// courtROP.setAuditEntryBO(this.convertCompAuditToRoadOpAudit(courtComp.getAuditEntryBO()));
			//
			return courtROP;
		} else
			return null;
	}

	private TAStaffBO convertCompStaffToRoadOpStaff(TAStaffBO compStaff) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		if (compStaff != null) {
			TAStaffBO roadOpStaff = new TAStaffBO();

			try {
				this.copyFields(roadOpStaff, compStaff);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// org.apache.commons.beanutils.PropertyUtilsBean utilCopy = new PropertyUtilsBean();
			//
			// utilCopy.copyProperties(roadOpStaff, compStaff);

			return roadOpStaff;
		} else
			return null;
	}

	private AuditEntryBO convertCompAuditToRoadOpAudit(AuditEntryBO auditComp) {
		if (auditComp != null) {
			AuditEntryBO auditROP = new AuditEntryBO();

			auditROP.setCreateDTime(auditComp.getCreateDTime());
			auditROP.setCreateUsername(auditComp.getCreateUsername());
			auditROP.setUpdateDTime(auditComp.getUpdateDTime());
			auditROP.setUpdateUsername(auditComp.getUpdateUsername());

			return auditROP;
		} else
			return null;
	}

	private AuditEntryBO convertMainAuditToRoadOpAudit(AuditEntryBO auditMain) {
		if (auditMain != null) {
			AuditEntryBO auditROP = new AuditEntryBO();

			auditROP.setCreateDTime(auditMain.getCreateDTime());
			auditROP.setCreateUsername(auditMain.getCreateUsername());
			auditROP.setUpdateDTime(auditMain.getUpdateDTime());
			auditROP.setUpdateUsername(auditMain.getUpdateUsername());

			return auditROP;
		} else
			return null;
	}

	public Boolean showCancel() {
		RequestContext context = RequestContextHolder.getRequestContext();

		RoadCheckReviewSummaryBean roadCheckReviewSummaryBean = (RoadCheckReviewSummaryBean) context.getFlowScope().get(this.ROAD_CHECK_REVIEW_BEAN);

		Boolean showCancel = true;

		ComplianceBO compliance = (ComplianceBO) context.getFlowScope().get("complianceBO");

		if (!compliance.getStatusId().equalsIgnoreCase(Constants.Status.ROAD_CHECK_CANCELLED)) {
			for (DocumentViewBO doc : roadCheckReviewSummaryBean.getDocViews()) {
				/* Only Show Cancel Button Where all documents are printed or created */
				if (!doc.getStatusId().equalsIgnoreCase(Constants.DocumentStatus.PRINTED) && !doc.getStatusId().equalsIgnoreCase(Constants.DocumentStatus.CREATED)) {
					showCancel = false;
				}
			}
		} else {
			showCancel = false;
		}

		return showCancel;
	}

	public Boolean showPrintDocuments(){
		RequestContext context = RequestContextHolder.getRequestContext();
		Boolean showPrintDocuments = true;
		
		ComplianceBO compliance = (ComplianceBO) context.getFlowScope().get("complianceBO");
		if (compliance.getStatusId().equalsIgnoreCase(Constants.Status.ROAD_CHECK_CANCELLED)) {
			showPrintDocuments=false;
		}
		return showPrintDocuments;
	}
	
	public Boolean showVerify() {
		RequestContext context = RequestContextHolder.getRequestContext();

		RoadCheckReviewSummaryBean roadCheckReviewSummaryBean = (RoadCheckReviewSummaryBean) context.getFlowScope().get(this.ROAD_CHECK_REVIEW_BEAN);

		Boolean showVerify = false;

		ComplianceBO compliance = (ComplianceBO) context.getFlowScope().get("complianceBO");

		if (!compliance.getStatusId().equalsIgnoreCase(Constants.Status.ROAD_CHECK_CANCELLED)) {
			for (DocumentViewBO doc : roadCheckReviewSummaryBean.getDocViews()) {
				/* Only Show Cancel Button Where all documents are printed or created */
				if (doc.getVerificationRequired() != null && doc.getVerificationRequired().equalsIgnoreCase("y")) {
					showVerify = true;
				}
			}
		}

		return showVerify;
	}

	public Boolean showServed() {
		RequestContext context = RequestContextHolder.getRequestContext();

		RoadCheckReviewSummaryBean roadCheckReviewSummaryBean = (RoadCheckReviewSummaryBean) context.getFlowScope().get(this.ROAD_CHECK_REVIEW_BEAN);

		Boolean showServed = false;

		ComplianceBO compliance = (ComplianceBO) context.getFlowScope().get("complianceBO");
		

		if (!compliance.getStatusId().equalsIgnoreCase(Constants.Status.ROAD_CHECK_CANCELLED))
		{
			for (DocumentViewBO doc : roadCheckReviewSummaryBean.getDocViews()) {
				/* Only Show Served Button Where all documents are printed, JP is assigned and verification is not
				 * required */
//				if (doc.getStatusId().equalsIgnoreCase(Constants.DocumentStatus.PRINTED) && ((doc.getVerificationRequired() != null && doc.getVerificationRequired().equalsIgnoreCase("n")) || doc.getVerificationRequired() == null)
//						&& (doc.getAuthorizedFlag() != null && doc.getAuthorizedFlag().equalsIgnoreCase("y")) ) 
//				{
//					if (doc.getDocumentTypeCode().equalsIgnoreCase(Constants.DocumentType.SUMMONS) && doc.getJpIdNumber() != null)
//						showServed = true;
//					else if (!doc.getDocumentTypeCode().equalsIgnoreCase(Constants.DocumentType.SUMMONS))
//						showServed = true;
//
//				}
				
				if(documentServable(doc))
					showServed = true;
			}
		}

		return showServed;

	}

	public void toggleSelectDocs_old(ToggleSelectEvent event) {
		RequestContext context = RequestContextHolder.getRequestContext();

		RoadCheckReviewSummaryBean roadCheckReviewSummaryBean = (RoadCheckReviewSummaryBean) context.getFlowScope().get(this.ROAD_CHECK_REVIEW_BEAN);

		if (!event.isSelected()) {
			// Then remove all docs from selected docs
			roadCheckReviewSummaryBean.getSelectedDocViews().clear();
		} else {
			// Add all docs to selected doc list;
			roadCheckReviewSummaryBean.getSelectedDocViews().clear();

			for (DocumentViewBO doc : roadCheckReviewSummaryBean.getDocViews()) {
				roadCheckReviewSummaryBean.getSelectedDocViews().add(doc);
			}
		}
	}

	public void toggleSelectDocs(ToggleSelectEvent event) {
		RequestContext context = RequestContextHolder.getRequestContext();

		RoadCheckReviewSummaryBean roadCheckReviewSummaryBean = (RoadCheckReviewSummaryBean) context.getFlowScope().get(this.ROAD_CHECK_REVIEW_BEAN);

		if (event.isSelected()) {
			// Loop through selected docs and remove ones that cannot be printed
			ComplianceBO compliance = (ComplianceBO) context.getFlowScope().get("complianceBO");

			if (!compliance.getStatusId().equalsIgnoreCase(Constants.Status.ROAD_CHECK_CANCELLED)) {
				for (DocumentViewBO doc : roadCheckReviewSummaryBean.getSelectedDocViews()) {
					/* Only Show Print Button Where all selected documents are verified */
					if ((doc.getVerificationRequired() != null && doc.getVerificationRequired().equalsIgnoreCase("y")) || (doc.getAuthorizedFlag() != null && doc.getAuthorizedFlag().equalsIgnoreCase("n"))) {
						roadCheckReviewSummaryBean.getSelectedDocViews().remove(doc);

					}
				}
			} else {
				roadCheckReviewSummaryBean.getSelectedDocViews().clear();
			}
		}

	}

	public Boolean showPrintButton() {
		RequestContext context = RequestContextHolder.getRequestContext();

		RoadCheckReviewSummaryBean roadCheckReviewSummaryBean = (RoadCheckReviewSummaryBean) context.getFlowScope().get(this.ROAD_CHECK_REVIEW_BEAN);

		Boolean showPrint = true;

		ComplianceBO compliance = (ComplianceBO) context.getFlowScope().get("complianceBO");

		if (!compliance.getStatusId().equalsIgnoreCase(Constants.Status.ROAD_CHECK_CANCELLED)) {
			for (DocumentViewBO doc : roadCheckReviewSummaryBean.getSelectedDocViews()) {
				/* Only Show Print Button Where all selected documents are verified and the document has not been served */

				showPrint = documentPrintable(doc);

			}
		} else {
			showPrint = false;
		}

		return showPrint;

	}

	public boolean documentPrintable(DocumentViewBO doc) 
	{

		Boolean showPrint = true;

		RequestContext context = RequestContextHolder.getRequestContext();

		ComplianceBO compliance = (ComplianceBO) context.getFlowScope().get("complianceBO");

		if (compliance.getStatusId().equalsIgnoreCase(Constants.Status.ROAD_CHECK_CANCELLED))
			showPrint = false;

		if ((doc.getVerificationRequired() != null && doc.getVerificationRequired().equalsIgnoreCase("y")) || (doc.getAuthorizedFlag() != null && doc.getAuthorizedFlag().equalsIgnoreCase("n")) || (!StringUtils.isEmpty(doc.getStatusId()) && (!doc.getStatusId().equalsIgnoreCase(Constants.DocumentStatus.CREATED) && !doc.getStatusId().equalsIgnoreCase(Constants.DocumentStatus.PRINTED)))) 
		{
			showPrint = false;

		}
		

		return showPrint;
	}

	/**
	 * At this point persuant to RFC 10 if the road operation is unscheduled and no document has been printed then you cannot print a single document
	 * @return
	 */
	public boolean onlyPrintAllDocs()
	{
		
		RequestContext context = RequestContextHolder.getRequestContext();
		
		RoadCheckReviewSummaryBean roadCheckReviewSummaryBean = (RoadCheckReviewSummaryBean) context.getFlowScope().get(this.ROAD_CHECK_REVIEW_BEAN);
		
		boolean printAllDocs = false;
		
		ComplianceBO compliance = (ComplianceBO) context.getFlowScope().get("complianceBO");
		
		if(compliance.getRoadOperationBO() == null || compliance.getRoadOperation() == null || compliance.getRoadOperation() < 1)
		{
			for(DocumentViewBO document : roadCheckReviewSummaryBean.getDocViews())
			{
				if(document.getPrintCount() == null || document.getPrintCount() < 1)
				{
					printAllDocs = true;
					
					continue;
				}
					
			}
		}
		
		return printAllDocs;
	}
	
	/**
	 * This is a helper function which checks is all document are verified.
	 * @return
	 */
	public boolean allDocsVerified()
	{
		
		RequestContext context = RequestContextHolder.getRequestContext();
		
		RoadCheckReviewSummaryBean roadCheckReviewSummaryBean = (RoadCheckReviewSummaryBean) context.getFlowScope().get(this.ROAD_CHECK_REVIEW_BEAN);
		
		boolean allDocsVerified = true;
		
		ComplianceBO compliance = (ComplianceBO) context.getFlowScope().get("complianceBO");
		
		if(compliance.getRoadOperationBO() == null || compliance.getRoadOperation() == null || compliance.getRoadOperation() < 1)
		{
			for(DocumentViewBO doc : roadCheckReviewSummaryBean.getDocViews())
			{
				if((doc.getVerificationRequired() != null && doc.getVerificationRequired().equalsIgnoreCase("y")) || (doc.getAuthorizedFlag() != null && doc.getAuthorizedFlag().equalsIgnoreCase("n")))
				{
					allDocsVerified = false;
					
					continue;
				}
					
			}
		}
		
		return allDocsVerified;
	}

	public void removeUprintableDocuments() {
		RequestContext context = RequestContextHolder.getRequestContext();

		RoadCheckReviewSummaryBean roadCheckReviewSummaryBean = (RoadCheckReviewSummaryBean) context.getFlowScope().get(this.ROAD_CHECK_REVIEW_BEAN);

		List<DocumentViewBO> docsToBeRemoved = new ArrayList<DocumentViewBO>();

		for (DocumentViewBO doc : roadCheckReviewSummaryBean.getSelectedDocViews()) 
		{

			if(! this.onlyPrintAllDocs())
			{
				if (!documentPrintable(doc))
				{
					docsToBeRemoved.add(doc);
				}
			}

		}

		roadCheckReviewSummaryBean.getSelectedDocViews().removeAll(docsToBeRemoved);

	}

	public Boolean allowPrintSelection(DocumentViewBO doc) {
		if (doc != null) {
			RequestContext context = RequestContextHolder.getRequestContext();

			Boolean showPrint = true;

			ComplianceBO compliance = (ComplianceBO) context.getFlowScope().get("complianceBO");

			if (!compliance.getStatusId().equalsIgnoreCase(Constants.Status.ROAD_CHECK_CANCELLED)) {

				/* Only Show Print Button Where all selected documents are verified */
				if ((doc.getVerificationRequired() != null && doc.getVerificationRequired().equalsIgnoreCase("y")) || (doc.getAuthorizedFlag() != null && doc.getAuthorizedFlag().equalsIgnoreCase("n"))) {
					showPrint = false;

				}

			} else {
				showPrint = false;
			}

			return showPrint;
		} else {
			return false;
		}

	}

	private boolean validateForCancel(String reasonCode, String comment){
		RequestContext context = RequestContextHolder.getRequestContext();
		boolean valid= true;
		if (StringUtils.isBlank(reasonCode)) {
			addErrorMessageWithParameter(context, "RequiredFields", "Reason");
			valid = false;
		}

		if (StringUtils.isBlank(comment)) {
			addErrorMessageWithParameter(context, "RequiredFields", "Comment");
			valid = false;

		}

		return valid;
	}
	
	public Event cancelRoadCheckAndDocs(RequestContext context) {
		try {
			ComplianceBO compliance = (ComplianceBO) context.getFlowScope().get("complianceBO");

			RoadCheckReviewSummaryBean roadCheckReviewSummaryBean = (RoadCheckReviewSummaryBean) context.getFlowScope().get(this.ROAD_CHECK_REVIEW_BEAN);

			//compliance.setStatusId(Constants.Status.ROAD_CHECK_CANCELLED);

			compliance.setCurrentUserName(this.getRomsLoggedInUser().getUsername());

			//this.getRoadCompliancyPortProxy().saveCompliance(compliance);

			List<DocumentViewBO> docsToCancel = new ArrayList<DocumentViewBO>();

			for (DocumentViewBO doc : roadCheckReviewSummaryBean.getDocViews()) {
				docsToCancel.add(this.convertFromRoadCompDocViewToDocManDocView(doc));

			}
			boolean valid = validateForCancel(roadCheckReviewSummaryBean.getRoadCheckCancelReasonCode(), roadCheckReviewSummaryBean.getRoadCheckCancelComment());
			if(valid){
				compliance.setStatusId(Constants.Status.ROAD_CHECK_CANCELLED);
				
				compliance.setReasonId(roadCheckReviewSummaryBean.getRoadCheckCancelReasonCode() != null? Integer.parseInt(roadCheckReviewSummaryBean.getRoadCheckCancelReasonCode()):null);
				
				compliance.setComment(roadCheckReviewSummaryBean.getRoadCheckCancelComment());
				
				this.getRoadCompliancyService().saveCompliance(compliance);
				/*Only need to cancel documents if there are documents to cancel.*/
				if(docsToCancel.size() > 0){
					this.getDocumentsManagerService().cancelAllDocuments(docsToCancel, this.getRomsLoggedInUser().getUsername(), roadCheckReviewSummaryBean.getRoadCheckCancelReasonCode(), roadCheckReviewSummaryBean.getRoadCheckCancelComment());
				}
				this.getDetails(context);
			}
			else{
				return error();
			}
			return success();
		} catch (Exception exc) {
			exc.printStackTrace();

			this.addErrorMessage(context, "SystemError");
			return error();
		}

	}

	public Event verifyAllDocuments(RequestContext context) {
		try {

			RoadCheckReviewSummaryBean roadCheckReviewSummaryBean = (RoadCheckReviewSummaryBean) context.getFlowScope().get(this.ROAD_CHECK_REVIEW_BEAN);

			List<DocumentViewBO> docsToVerify = new ArrayList<DocumentViewBO>();
			for (DocumentViewBO doc : roadCheckReviewSummaryBean.getDocViews()) {
				roadCheckReviewSummaryBean.getVerifyView().setLastUpdatedUser(doc.getLastUpdateUser());
				roadCheckReviewSummaryBean.getVerifyView().setComment(roadCheckReviewSummaryBean.getRoadCheckVerifyComment());

				if (doc.getVerificationRequired().equalsIgnoreCase("y") && !documentManagerServiceAction.validateOverrideDetails(context, roadCheckReviewSummaryBean.getVerifyView())) {
					// If there are any documents which cannot be verified by the user then do not continue

					//kpowell:2014-09-29- UR-054 msg changed from "Documents are in the list which can not be verified."
					//this.addWarningMessageText(context, "Verification not allowed by same user who created/edited the document(s)");
					return error();
				}

				if (doc.getVerificationRequired().equalsIgnoreCase("y")) {
					docsToVerify.add(this.convertFromRoadCompDocViewToDocManDocView(doc));
				}

			}

			this.getDocumentsManagerService().verifyAllDocuments(docsToVerify, roadCheckReviewSummaryBean.getRoadCheckVerifyComment(), roadCheckReviewSummaryBean.verifyView.getUserName());

			this.getDetails(context);

			this.addInfoMessage(context, "DocsVerifiedSuccessfully");
			
			if(roadCheckReviewSummaryBean != null && roadCheckReviewSummaryBean.getSelectedDocViews() != null){
				roadCheckReviewSummaryBean.getSelectedDocViews().clear();
			}
			

			return success();

		} catch (ErrorSavingException e) {
			this.addErrorMessageText(context, e.getMessage());
			return error();
		} catch (NoRecordFoundException e) {
			this.addErrorMessageText(context, e.getMessage());
			return error();
		} catch (RequiredFieldMissingException e) {
			this.addErrorMessageText(context, e.getMessage());
			return error();
		} catch (Exception e) {
			e.printStackTrace();
			return error();
		}
	}
	
	public Event verifyDenyAllDocuments(RequestContext context) {
		try {

			RoadCheckReviewSummaryBean roadCheckReviewSummaryBean = (RoadCheckReviewSummaryBean) context.getFlowScope().get(this.ROAD_CHECK_REVIEW_BEAN);

			List<DocumentViewBO> docsToVerify = new ArrayList<DocumentViewBO>();
			for (DocumentViewBO doc : roadCheckReviewSummaryBean.getDocViews()) {
				roadCheckReviewSummaryBean.getVerifyView().setLastUpdatedUser(doc.getLastUpdateUser());
				roadCheckReviewSummaryBean.getVerifyView().setComment(roadCheckReviewSummaryBean.getRoadCheckVerifyComment());

				if (doc.getVerificationRequired().equalsIgnoreCase("y") && !documentManagerServiceAction.validateOverrideDetails(context, roadCheckReviewSummaryBean.getVerifyView())) {
					// If there are any documents which cannot be verified by the user then do not continue

					//kpowell:2014-09-29- UR-054 msg changed from "Documents are in the list which can not be verified."
					//this.addWarningMessageText(context, "Verification not allowed by same user who created/edited the document(s)");
					return error();
				}

				if (doc.getVerificationRequired().equalsIgnoreCase("y")) {
					docsToVerify.add(this.convertFromRoadCompDocViewToDocManDocView(doc));
				}

			}

			this.getDocumentsManagerService().denyVerificationAllDocuments(docsToVerify, roadCheckReviewSummaryBean.getRoadCheckVerifyComment(), roadCheckReviewSummaryBean.verifyView.getUserName());
			this.getDetails(context);
			roadCheckReviewSummaryBean.setRoadCheckVerifyComment("");
			this.addInfoMessage(context, "DenyDocsVerification");
			return success();
	
		} catch (ErrorSavingException e) {
			this.addErrorMessageText(context, e.getMessage());
			return error();
		} catch (NoRecordFoundException e) {
			this.addErrorMessageText(context, e.getMessage());
			return error();
		} catch (RequiredFieldMissingException e) {
			this.addErrorMessageText(context, e.getMessage());
			return error();
		} catch (Exception e) {
			e.printStackTrace();
			return error();
		}
	}

	
	//check if document list contains at least one summons
	public String checkIfSummonsPresent(RequestContext context){
		
		RoadCheckReviewSummaryBean roadCheckReviewSummaryBean = (RoadCheckReviewSummaryBean) context.getFlowScope().get(this.ROAD_CHECK_REVIEW_BEAN);

		for (DocumentViewBO doc : roadCheckReviewSummaryBean.getDocViews()) {
			
			if(documentServable(doc) && doc.getDocumentTypeCode().equalsIgnoreCase(Constants.DocumentType.SUMMONS)){
				return "yes";
			}				
		
		}
		return "no";
	}
	
	public Event serveAllDocuments(RequestContext context) {
		try {

			System.err.println("inside serveAllDocuments");
			RoadCheckReviewSummaryBean roadCheckReviewSummaryBean = (RoadCheckReviewSummaryBean) context.getFlowScope().get(this.ROAD_CHECK_REVIEW_BEAN);

			List<DocumentViewBO> docsToVerify = new ArrayList<DocumentViewBO>();
			for (DocumentViewBO doc : roadCheckReviewSummaryBean.getDocViews()) {
				
				
//				if (doc.getStatusId().equalsIgnoreCase(Constants.DocumentStatus.PRINTED) && ((doc.getVerificationRequired() != null && doc.getVerificationRequired().equalsIgnoreCase("n")) || doc.getVerificationRequired() == null)
//						&& (doc.getAuthorizedFlag() != null && doc.getAuthorizedFlag().equalsIgnoreCase("y")) ) 
//				{
//					if (doc.getDocumentTypeCode().equalsIgnoreCase(Constants.DocumentType.SUMMONS) && doc.getJpIdNumber() != null)
//						docsToVerify.add(this.convertFromRoadCompDocViewToDocManDocView(doc));
//
//				}
				
				if(documentServable(doc))
					docsToVerify.add(this.convertFromRoadCompDocViewToDocManDocView(doc));
			
			}

			StaffUserMappingBO staffBO = (StaffUserMappingBO) context.getFlowScope().get("staffBOForServe");
			if(staffBO!=null){
				roadCheckReviewSummaryBean.getServedByDetails().setServedByUserID(staffBO.getStaffId());
			}
			
			boolean canserve = validateForServe(docsToVerify, roadCheckReviewSummaryBean.getServedByDetails().getServedByUserID(), roadCheckReviewSummaryBean.servedByDetails.getServedOnDate(), roadCheckReviewSummaryBean.getServedByDetails().getServedAtAddressBO(), this.getRomsLoggedInUser().getUsername(),context);
			
			if(canserve){
				
				//populate addressview object that is located in documentmanager
				fsl.ta.toms.roms.util.AddressView docAddress = new fsl.ta.toms.roms.util.AddressView();
				docAddress.setAddressLine1(roadCheckReviewSummaryBean.getServedByDetails().getServedAtAddressBO().getAddressLine1());
				docAddress.setAddressLine2(roadCheckReviewSummaryBean.getServedByDetails().getServedAtAddressBO().getAddressLine2());
				docAddress.setMarkText(roadCheckReviewSummaryBean.getServedByDetails().getServedAtAddressBO().getMarkText());
				docAddress.setParish(roadCheckReviewSummaryBean.getServedByDetails().getServedAtAddressBO().getParish());
				docAddress.setParishDescription(roadCheckReviewSummaryBean.getServedByDetails().getServedAtAddressBO().getParishDescription());
				docAddress.setPoBoxNo(roadCheckReviewSummaryBean.getServedByDetails().getServedAtAddressBO().getPoBoxNo());
				docAddress.setPoLocationName(roadCheckReviewSummaryBean.getServedByDetails().getServedAtAddressBO().getPoLocationName());
				docAddress.setStreetName(roadCheckReviewSummaryBean.getServedByDetails().getServedAtAddressBO().getStreetName());
				docAddress.setStreetNo(roadCheckReviewSummaryBean.getServedByDetails().getServedAtAddressBO().getStreetNo());
				
				this.getDocumentsManagerService().serveAllDocuments(docsToVerify, roadCheckReviewSummaryBean.getServedByDetails().getServedByUserID(), 
						roadCheckReviewSummaryBean.servedByDetails.getServedOnDate(), docAddress, 
						roadCheckReviewSummaryBean.getServedByDetails().getServedAtAddressBO().getMarkText() + ", " + 
						roadCheckReviewSummaryBean.getServedByDetails().getServedAtAddressBO().getStreetNo() + " " + 
						roadCheckReviewSummaryBean.getServedByDetails().getServedAtAddressBO().getStreetName(), 
						this.getRomsLoggedInUser().getUsername());
				
				
				this.getDetails(context);
				
				this.addInfoMessageText(context, "Documents Served successfully");
				
				if(roadCheckReviewSummaryBean != null && roadCheckReviewSummaryBean.getSelectedDocViews() != null){
					roadCheckReviewSummaryBean.getSelectedDocViews().clear();
				}
				
				return success();
			}else{
				return error();
			}
			
		} catch (Exception exc) {
			exc.printStackTrace();

			this.addErrorMessage(context, "SystemError");
			
			return error();
		}
	}
	
	

	

	private boolean validateForServe(
			List<DocumentViewBO>docsToVerify,
			String servedByUserID, Date servedOnDate,
			AddressView address, String username, RequestContext context) {
		
		System.err.println("inside validateForServe");
		boolean pass = true;

		DocumentViewBO document = docsToVerify.get(0);
		String summonsDocTypePresent = (String) context.getFlowScope().get("summonsDocTypePresent");
		
		
		if (docsToVerify == null || docsToVerify.isEmpty()){
			addErrorMessageText(context,"List of Documents to serve is empty.");
			pass = false;			
		}else{
		
				if (StringUtils.isBlank(servedByUserID)){
					addErrorMessageWithParameter(context, "RequiredFields", "Served by TA Staff");
					pass = false;
				}
				
				if (servedOnDate == null) {
					addErrorMessageWithParameter(context, "RequiredFields", "Date Served");
					pass = false;
				} else {
					if (DateUtils.isDateInFutureDateType(servedOnDate)) {
						addErrorMessageText(context, "Date Served cannot be a future date.");
						pass = false;
					}
		
					if (document.getPrintDtime() != null && DateUtils.before(servedOnDate, document.getPrintDtime())) {
						addErrorMessageText(context, "Date Served cannot be before Printed Date.");
						pass = false;
					}
				}
				
				
				//Only validate address for Road Check when at least one summons is in the list of documents to serve --@kpowell2015-08-10
				if(summonsDocTypePresent.equalsIgnoreCase("yes")){
					System.err.println("inside Only validate address for Road Check");
					//global address validation - kpowell
					boolean errorFoundInAddress  = validateAddress(context, address);
			            
			        if(errorFoundInAddress){
			              pass = false;
			        }				
				}
				/*if (StringUtils.isBlank(username)){
				 * this.addErrorMessage(context, "SystemError");
					pass = false;
				}*/

		}
		
		
		return pass;
	}
	
	/**
	 * This function returns true if the document passed in is servable and false otherwise
	 * @param doc
	 * @return
	 */
	private boolean documentServable(DocumentViewBO doc)
	{
		
		if (doc.getStatusId().equalsIgnoreCase(Constants.DocumentStatus.PRINTED) && ((doc.getVerificationRequired() != null && doc.getVerificationRequired().equalsIgnoreCase("n")) || doc.getVerificationRequired() == null)
				&& ( (doc.getAuthorizedFlag() != null && doc.getAuthorizedFlag().equalsIgnoreCase("y")) || doc.getAuthorizedFlag() == null)  ) 
		{
			if (doc.getDocumentTypeCode().equalsIgnoreCase(Constants.DocumentType.SUMMONS) && doc.getJpIdNumber() != null)
				return true;
			else if(!doc.getDocumentTypeCode().equalsIgnoreCase(Constants.DocumentType.SUMMONS))
				return true;
			else
				return false;

		}
		else
			return false;
	}

	public void setCurrentRoadOp(RequestContext context) {
		if (this.isHandHeld(context)) {
			RoadCompliancyCriteriaBO criteria = (RoadCompliancyCriteriaBO) context.getFlowScope().get("criteria");

			criteria.setOperationName(this.getRomsLoggedInUser().getCurrentRoadOperationName());
		}
	}

	public Event getVehicleMoverFromDLN(RequestContext context) {

		SupportingDetailsView supDetails = (SupportingDetailsView) context.getFlowScope().get("supportingDetailsView");
		RoadCheckInitiateView roadCheckInitiateView = (RoadCheckInitiateView) context.getFlowScope().get("initiateView");		
		
		String vehicleMoverType ="";

		if (supDetails.getVehicleMoverType().equalsIgnoreCase(Constants.PersonType.OTHER)) {
			supDetails.setVehicleMoverDLN(supDetails.getVehicleMoverOTBO().getDlNo());
			vehicleMoverType= "Other";
		} else if (supDetails.getVehicleMoverType().equalsIgnoreCase(Constants.PersonType.WRECKERDRIVER)) {
			supDetails.setVehicleMoverDLN(supDetails.getVehicleMoverWreckerBO().getDlNo());
			vehicleMoverType= "Wrecker";
		}
		
		
		

		if (StringUtils.isEmpty(supDetails.getVehicleMoverDLN())) {

			supDetails.setVehicleMoverBO(null);
			this.addErrorMessageWithParameter(context, "RequiredFields", vehicleMoverType+ " Driver's Licence Number");
			return error();
		} else {
			
			//relocated validation to only occur once a value exists in th edln field
			if((supDetails.getVehicleMoverDLN().equalsIgnoreCase(roadCheckInitiateView.getTrn()) || 
					(! StringUtils.isEmpty(roadCheckInitiateView.getDlNo()) && supDetails.getVehicleMoverDLN().equalsIgnoreCase(roadCheckInitiateView.getDlNo()))) 
					&& (!supDetails.getVehicleMoverType().equalsIgnoreCase(Constants.PersonType.OTHER)))
			{
					this.addErrorMessage(context, "WreckerDLN");
					
					supDetails.setVehicleMoverDLN("");
					return error();
			}
			
			PersonBO returningPerson;
			try {
				returningPerson = this.getRoadCompliancyService().getPersonFromDriversLicense(supDetails.getVehicleMoverDLN(), this.getRomsLoggedInUser().getUsername());

				if (supDetails.getVehicleMoverType().equalsIgnoreCase(Constants.PersonType.OTHER)) {

					supDetails.setVehicleMoverOTBO(new PersonBOForRoadCompConverter(returningPerson));
				} else if (supDetails.getVehicleMoverType().equalsIgnoreCase(Constants.PersonType.WRECKERDRIVER)) {

					supDetails.setVehicleMoverWreckerBO(new PersonBOForRoadCompConverter(returningPerson));
				}

				supDetails.setVehicleMoverBO(returningPerson);

				return success();
			} catch (NoRecordFoundException e) {
				if (supDetails.getVehicleMoverWreckerBO() != null)
					supDetails.getVehicleMoverWreckerBO().setDlNo(null);

				if (supDetails.getVehicleMoverOTBO() != null)
					supDetails.getVehicleMoverOTBO().setDlNo(null);

				supDetails.setVehicleMoverBO(null);
				this.addErrorMessageText(context, e.getMessage());
				e.printStackTrace();
				return error();

			} catch (RequiredFieldMissingException e) {
				if (supDetails.getVehicleMoverWreckerBO() != null)
					supDetails.getVehicleMoverWreckerBO().setDlNo(null);

				if (supDetails.getVehicleMoverOTBO() != null)
					supDetails.getVehicleMoverOTBO().setDlNo(null);

				supDetails.setVehicleMoverBO(null);
				this.addErrorMessageText(context, e.getMessage());
				e.printStackTrace();
				return error();
			}

		}

	}

	public void changeWhoCarriedVehicle() {
		RequestContext context = RequestContextHolder.getRequestContext();

		SupportingDetailsView supDetails = (SupportingDetailsView) context.getFlowScope().get("supportingDetailsView");

		if (supDetails.getVehicleMoverType().equalsIgnoreCase(Constants.PersonType.OTHER)) {
			supDetails.setVehicleMoverBO(supDetails.getVehicleMoverOTBO());
		} else if (supDetails.getVehicleMoverType().equalsIgnoreCase(Constants.PersonType.WRECKERDRIVER)) {
			supDetails.setVehicleMoverBO(supDetails.getVehicleMoverWreckerBO());
		} else if (supDetails.getVehicleMoverType().equalsIgnoreCase(Constants.PersonType.ITA_EXAMINER)) {
			supDetails.setVehicleMoverBO(supDetails.getVehicleMoverITABO());
		} else if (supDetails.getVehicleMoverType().equalsIgnoreCase(Constants.PersonType.POLICE_OFFCER)) {
			supDetails.setVehicleMoverBO(supDetails.getVehicleMoverPOBO());
		} else if (supDetails.getVehicleMoverType().equalsIgnoreCase(Constants.PersonType.TA_STAFF)) {
			supDetails.setVehicleMoverBO(supDetails.getVehicleMoverTABO());
		}

		if (!supDetails.getVehicleMoverType().equalsIgnoreCase(Constants.PersonType.WRECKERDRIVER)) {
			// Clear out wrecker related information
			supDetails.setWreckerVehicleBO(new VehicleBO());
			supDetails.setWreckingCompanyId(null);
			RoadCheckReviewSummaryBean roadCheckReviewSummaryBean = (RoadCheckReviewSummaryBean) context.getFlowScope().get("roadCheckReviewSummaryBean");

			roadCheckReviewSummaryBean.setWreckingCompany(null);
		}
	}
	
	public void changeWreckerParish(){
		
		RequestContext context = RequestContextHolder.getRequestContext();

		SupportingDetailsView supDetails = (SupportingDetailsView) context.getFlowScope().get("supportingDetailsView");
		
		supDetails.setWreckingCompanyId(null);
		
		if(supDetails.getSelectedWreckerParishCode() != null && !supDetails.getSelectedWreckerParishCode().isEmpty()){
			this.getWreckingList(context);
		}
		else{
			supDetails.setWreckingCompanyId(null);
			context.getFlowScope().put("wreckingList",null);
		}
	}
	
	public void changePoundParish(){
		
		RequestContext context = RequestContextHolder.getRequestContext();

		SupportingDetailsView supDetails = (SupportingDetailsView) context.getFlowScope().get("supportingDetailsView");
		
		supDetails.setWreckingCompanyId(null);
		
		if(supDetails.getSelectedPoundParishCode() != null && !supDetails.getSelectedPoundParishCode().isEmpty()){
			this.getPoundList(context);
		}
		else{
			supDetails.setPoundId(null);
			context.getFlowScope().put("poundList",null);
		}
	}
	

	public Event searchWreckerVehcile(RequestContext context) {

		SupportingDetailsView supDetails = (SupportingDetailsView) context.getFlowScope().get("supportingDetailsView");
		RoadCheckInitiateView roadCheckInitiateView = (RoadCheckInitiateView) context.getFlowScope().get("initiateView");

		if (StringUtils.isEmpty(supDetails.getWreckerVehicleBO().getPlateRegNo())) {
			this.addErrorMessageWithParameter(context, "RequiredFields", "Plate Registration Number(Wrecker Vehicle)");

			return error();
		}
		else if(supDetails.getWreckerVehicleBO().getPlateRegNo().equalsIgnoreCase(roadCheckInitiateView.getPlateRegNo())){
			
			this.addErrorMessage(context, "WreckerVehicle");
			supDetails.getWreckerVehicleBO().setPlateRegNo("");
			return error();
		}
		else {
			try {

				supDetails.setWreckerVehicleBO(this.getRoadCompliancyService().searchForWreckerVehicle(supDetails.getWreckerVehicleBO().getPlateRegNo()));
				supDetails.setPrevWreckerPlateRegNo(supDetails.getWreckerVehicleBO().getPlateRegNo());
				return success();
			} catch (NoRecordFoundException e) {
				if (supDetails.getWreckerVehicleBO() != null)
					supDetails.getWreckerVehicleBO().setPlateRegNo(null);

				this.addErrorMessageText(context, e.getMessage());
				e.printStackTrace();
				return error();
			} catch (RequiredFieldMissingException e) {
				if (supDetails.getWreckerVehicleBO() != null)
					supDetails.getWreckerVehicleBO().setPlateRegNo(null);

				this.addErrorMessageText(context, e.getMessage());
				e.printStackTrace();
				return error();
			}
		}
	}

	public void getSupportingDetailsForVehicleMoverList(RequestContext context) {
		RoadCheckInitiateView roadCheckInitiateView = (RoadCheckInitiateView) context.getFlowScope().get("initiateView");
		
		if(roadCheckInitiateView.getRoadOperationBO() == null || roadCheckInitiateView.getRoadOperationBO().getRoadOperationId() == null || roadCheckInitiateView.getRoadOperationBO().getRoadOperationId() < 1)
		{
			//Unscheduled operation
			return;
		}

		try {
			RoadOperationOtherDetailsBO roadOpDetails = this.getRoadOperationService().findRoadOperationOtherDetails(roadCheckInitiateView.getRoadOperationBO().getRoadOperationId());

			List<PersonBOForRoadCompConverter> vehicleMoverTAStaffList = new ArrayList<PersonBOForRoadCompConverter>();

			List<PersonBOForRoadCompConverter> vehicleMoverITAStaffList = new ArrayList<PersonBOForRoadCompConverter>();

			List<PersonBOForRoadCompConverter> vehicleMoverPoliceStaffList = new ArrayList<PersonBOForRoadCompConverter>();

			List<PersonBOForRoadCompConverter> personList = new ArrayList<PersonBOForRoadCompConverter>();

			for (AssignedTeamDetailsBO assignedTeam : roadOpDetails.getAssignedTeamDetailsBOList()) {
				for (TAStaffBO taStaff : assignedTeam.getTaStaffList()) {

					PersonBOForRoadCompConverter person = new PersonBOForRoadCompConverter();
					if(taStaff != null)
					{
						if(taStaff.getAttended()){
							person.setFullName(taStaff.getFullName() + " [" + taStaff.getStaffId() + "]");
							person.setPersonId(taStaff.getPersonId());
		
							vehicleMoverTAStaffList.add(person);
						
							personList.add(person);
						}
					}
				}

				for (ITAExaminerBO examiner : assignedTeam.getItaExaminerList()) {

					PersonBOForRoadCompConverter person = new PersonBOForRoadCompConverter();
					
					if(examiner != null)
					{
						if(examiner.getAttended()){
							person.setFullName(examiner.getPersonBO().getFullName() + " [" + examiner.getExaminerId() + "]");
							person.setPersonId(examiner.getPersonBO().getPersonId());
		
							vehicleMoverITAStaffList.add(person);
							personList.add(person);
						}
					}
				}

				for (PoliceOfficerBO police : assignedTeam.getPoliceOfficerList()) {

					PersonBOForRoadCompConverter person = new PersonBOForRoadCompConverter();
					
					if(police != null)
					{
						if(police.getAttended()){
							person.setFullName(police.getFullName() + " - " + police.getRank() + " [" + police.getPolOfficerCompNo() + "]");
							person.setPersonId(police.getPersonID());
		
							vehicleMoverPoliceStaffList.add(person);
							personList.add(person);
						}
					}
				}
			}

			context.getFlowScope().put("vehicleMoverTAStaffList", vehicleMoverTAStaffList);
			context.getFlowScope().put("vehicleMoverITAStaffList", vehicleMoverITAStaffList);
			context.getFlowScope().put("vehicleMoverPoliceStaffList", vehicleMoverPoliceStaffList);
			context.getFlowScope().put("emptyPerson", new PersonBOForRoadCompConverter());
			context.getFlowScope().put("personList", personList);
		} catch (InvalidFieldException e) {

			e.printStackTrace();
		} catch (NoRecordFoundException e) {

			e.printStackTrace();
		} catch (RequiredFieldMissingException e) {

			e.printStackTrace();
		} catch (Exception e) {

			e.printStackTrace();
		}

	}
	
	public boolean isDriversLicenceNumAndTrnEntered()
	{
		
		RequestContext context = RequestContextHolder.getRequestContext();
		
		boolean isDriversLicenceNumAndTrnEntered = false;
		
		RoadCheckInitiateView roadCheckInitiateView = (RoadCheckInitiateView) context.getFlowScope().get("initiateView");
		
		if(StringUtil.isSet( roadCheckInitiateView.getTrn()) || StringUtil.isSet( roadCheckInitiateView.getDlNo()))
		{
			isDriversLicenceNumAndTrnEntered = true;
		}
		
		return isDriversLicenceNumAndTrnEntered;
	}
	
	public void toggleTrafficTicketCheckAllowed(RequestContext context){
		
		RoadCheckInitiateView roadCheckInitiateView = (RoadCheckInitiateView) context.getFlowScope().get("initiateView");
		
		if(! StringUtil.isSet(roadCheckInitiateView.getDlNo())){
			roadCheckInitiateView.setIncludeTTMSResults(false);
		}
	}
	
	private DocumentViewBO copyDocumentViewBO(DocumentViewBO docview, DocumentViewBO document){
		try {
			copyFields(docview,document);
		} catch (Exception e) {
			logger.error("Document Manager",e);
		}
		
		//if(document.getListOfOffences()!=null && !document.getListOfOffences().get(0).getClass().equals(fsl.ta.toms.roms.webservices.scanneddocupload.OffenceBO.class)){
			List<OffenceBO> listConvertedOffences = new ArrayList<OffenceBO>();
			
			for(Object documentCopy : document.getListOfOffences()){
				OffenceBO offenceToCopy = new OffenceBO();
				
				try {
					copyFields(offenceToCopy, documentCopy);
				} catch (Exception e) {
					logger.error("Document Manager",e);
				}
				
				listConvertedOffences.add(offenceToCopy);
			}
			
			docview.getListOfOffences().clear();
			docview.getListOfOffences().addAll(listConvertedOffences);
		//}
		if (document.getListOfVehicleOwners().size() > 0) {

			List<VehicleOwnerBO> vehicleOwners = new ArrayList<VehicleOwnerBO>();

			//if (roadCompDocView.getListOfVehicleOwners().get(0) instanceof VehicleOwnerBO) {
				for (Object vehilceOwner : document.getListOfVehicleOwners()) {
					VehicleOwnerBO copyVehicleOwner = new VehicleOwnerBO();
					try
					{
						this.copyFields(copyVehicleOwner, vehilceOwner);
					} catch (Exception e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					vehicleOwners.add(copyVehicleOwner);
				}

				docview.getListOfVehicleOwners().clear();
				docview.getListOfVehicleOwners().addAll(vehicleOwners);
			//}
		}	
			
		
		return docview;
	}
	
	/**
	 * @author oanguin
	 * @param roadLicCehclRslt
	 * @return true if the insurance details should be diplayed, false otherwise
	 */
	public boolean shouldInuranceDetialsDisplay(RoadLicCheckResultBO roadLicChklRslt)
	{
		boolean display = false;
		
		if(StringUtils.isNotEmpty(roadLicChklRslt.getInsuranceComp())
				|| roadLicChklRslt.getInsuranceExpDate() != null
				|| roadLicChklRslt.getInsuranceIssueDate() != null)
		{
			display = true;
		}
		
		return display;
	}
}
