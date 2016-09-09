package org.fsl.roms.service.action;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.lang.NumberUtils;
import org.apache.commons.lang.StringUtils;
import org.fsl.roms.constants.Constants;
import org.fsl.roms.constants.Constants.DocumentStatus;
import org.fsl.roms.constants.Constants.DocumentType;
import org.fsl.roms.constants.Constants.FileUpload;
import org.fsl.roms.constants.Constants.StatusType;
import org.fsl.roms.constants.Constants.UserPermissions;
import org.fsl.roms.constants.Constants.VERDICT;
import org.fsl.roms.constants.Constants.YesNo;
import org.fsl.roms.servlet.GenerateDocServlet;
import org.fsl.roms.servlet.ViewScannedDocServlet;
import org.fsl.roms.util.DateUtils;
import org.fsl.roms.view.AddressView;
import org.fsl.roms.view.VerifyDetailsSecurityView;
import org.fsl.trn.exceptions.InvalidTaxPayerException;
import org.fsl.trn.exceptions.TaxPayerClosedException;
import org.fsl.trn.exceptions.TaxPayerDeceasedException;
import org.fsl.trn.exceptions.TaxPayerRetiredException;
import org.fsl.trn.exceptions.TaxPayerUnintendedException;
import org.primefaces.component.inputtext.InputText;
import org.primefaces.component.selectbooleancheckbox.SelectBooleanCheckbox;
import org.primefaces.component.selectonemenu.SelectOneMenu;
import org.primefaces.event.CloseEvent;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.ToggleSelectEvent;
import org.primefaces.model.UploadedFile;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;
import org.springframework.webflow.execution.RequestContextHolder;

import fsl.ta.toms.roms.bo.AddressBO;
import fsl.ta.toms.roms.bo.CourtAppearanceBO;
import fsl.ta.toms.roms.bo.CourtBO;
import fsl.ta.toms.roms.bo.CourtCaseBO;
import fsl.ta.toms.roms.bo.DocumentViewBO;
import fsl.ta.toms.roms.bo.OffenceBO;
import fsl.ta.toms.roms.bo.PersonBO;
import fsl.ta.toms.roms.bo.RefCodeBO;
import fsl.ta.toms.roms.bo.ScannedDocBO;
import fsl.ta.toms.roms.bo.StaffUserMappingBO;
import fsl.ta.toms.roms.bo.SummonsBO;
import fsl.ta.toms.roms.bo.WarningNoProsecutionBO;
import fsl.ta.toms.roms.bo.WarningNoticeBO;
import fsl.ta.toms.roms.documents.view.SummonsDischargeAndReleaseFormView;
import fsl.ta.toms.roms.exception.ErrorSavingException;
import fsl.ta.toms.roms.exception.NoRecordFoundException;
import fsl.ta.toms.roms.exception.RequiredFieldMissingException;
import fsl.ta.toms.roms.search.criteria.impl.CourtCriteriaBO;
import fsl.ta.toms.roms.search.criteria.impl.DocumentsCriteriaBO;
import fsl.ta.toms.roms.search.criteria.impl.StaffUserMappingCriteriaBO;
import fsl.ta.toms.roms.trnwebservice.InvalidTrnBranchException_Exception;
import fsl.ta.toms.roms.trnwebservice.NotValidTrnTypeException_Exception;
//import java.util.Objects;
import fsl.ta.toms.roms.trnwebservice.SystemErrorException_Exception;

/**
 * 
 * @author jreid
 * 
 */

@Service
public class DocumentManagerServiceAction extends BaseServiceAction {

	private static String SUMMONS = "summons";
	private static String WARNING_NOTICE_NO_PROSECUTION = "warningNoProsecution";
	private static String WARNING_NOTICE = "warningNotice";
	private static String CRITERIA = "criteria";
	private static String DOCUMENT_IN_VIEW = "document";
	private static String DOCUMENT_IN_VIEW_COPY = "documentCopy";
	private static String DOCUMENTS_RESULT_LIST = "documents";

	private static String MODE = "mode";

	public DocumentManagerServiceAction() {
		super();
	}

	/************************************ SEARCH SCREEN ******************************************/
	/**
	 * This function searches based on Artery criteria
	 * 
	 * @param context
	 * @return
	 */
	public Event search(RequestContext context) {

		List<DocumentViewBO> documents = null;
		context.getFlowScope().put(WARNING_NOTICE_NO_PROSECUTION, null);
		context.getFlowScope().put(SUMMONS, null);
		context.getFlowScope().put(WARNING_NOTICE, null);
		context.getFlowScope().put(DOCUMENT_IN_VIEW, null);
		context.getFlowScope().put(DOCUMENTS_RESULT_LIST, null);

		try {

			DocumentsCriteriaBO criteria = (DocumentsCriteriaBO) context.getFlowScope().get(CRITERIA);

			StaffUserMappingBO staffBO = (StaffUserMappingBO) context.getFlowScope().get("staffBOForSearch");

			if (staffBO != null)
				criteria.setStaffId(staffBO.getStaffId());
			else
				criteria.setStaffId(null);

			if (validateForSearch(context)) {

				/* if (isSerialNoAutomatic(criteria.getSerialNumberEntered())) {
				 * System.err.println(" entered num is auto");
				 * criteria.setAutomaticSerialNo(Integer.valueOf(criteria.getSerialNumberEntered())); } else {
				 * criteria.setManualSerialNo(criteria.getSerialNumberEntered());
				 * System.err.println(" entered num is not auto"); } */

				documents = getDocumentsManagerService().search(criteria);

				if (documents == null || documents.isEmpty()) {
					addErrorMessage(context, "Norecordsfound");

					context.getFlowScope().put(DOCUMENTS_RESULT_LIST, documents);

					return error();

				} else if (documents.size() == 1) {

					context.getFlowScope().put(DOCUMENT_IN_VIEW, documents.get(0));

					context.getFlowScope().put(MODE, "update");

					context.getFlowScope().put(DOCUMENTS_RESULT_LIST, documents);

					// load
					loadDocumentDetails(context);

					return yes();
				}

				// more than one result
				// set the entire list in datatable
				context.getFlowScope().put(DOCUMENTS_RESULT_LIST, documents);
				return success();
			}else{
				return error();
			}

		} catch (Exception e) {
			e.printStackTrace();

			addErrorMessageText(context, e.getMessage());
			return error();
		}

		//return error();
	}

	/**
	 * This function searches based on Artery criteria
	 * 
	 * @param context
	 * @return
	 */
	public Event loadDocumentDetails(RequestContext context) {

		DocumentViewBO document = (DocumentViewBO) context.getFlowScope().get(DOCUMENT_IN_VIEW);

		context.getFlowScope().put("viewScannedDoc", false);
		context.getFlowScope().put("openReportWindow", false);
		context.getFlowScope().put("scannedDocToUpload", new ScannedDocBO());
		context.getFlowScope().put("manualScannedDocAttached", false);
		/* context.getFlowScope().put("currentUploadedFile", null); */

		context.getMessageContext().clearMessages();

		// set lists
		// getParishes(context);

		// set reasonList
		// context.getFlowScope().put("reasonList", getReasonsByModuleType("DC"));
		getTAStaffListByRegion(getRomsLoggedInUser().getLocationCode());
		//getJusticesOfPeaceListByRegion(getRomsLoggedInUser().getLocationCode());
		

		if (document == null) {
			addErrorMessage(context, "NoDetailsToDisplay");
			return error();
		}

		try {

			// set the doc type
			context.getFlowScope().put("docType", document.getDocumentTypeCode());
			
			// set JP List
			/*OAnguin - 8 Sept 2015
			 * Added code to fix error message of JPS 'No Jps exist for this parish. Please add JPs before continuing. showing up where JP is not needed.
			 * such as warning notice and waring notice no prosecution.
			 * */
			if(StringUtils.isNotBlank(document.getDocumentTypeCode()) && document.getDocumentTypeCode().equalsIgnoreCase(Constants.DocumentType.SUMMONS)){
				getJusticesOfPeaceListByOffenceParish(document.getOffenceParishCode());
			}
			
			if (StringUtils.isNotBlank(document.getDocumentTypeCode())) {

				if (document.getDocumentTypeCode().equalsIgnoreCase(Constants.DocumentType.SUMMONS)) {

					SummonsBO summonsBO = null;
					CourtCaseBO courtCaseBO = null;
					//XMLGregorianCalendar initialCourtDate = null;

					summonsBO = getDocumentsManagerService().getSummonsDetails(document.getAutomaticSerialNo());
					courtCaseBO = getDocumentsManagerService().getSummonsDetails(document.getAutomaticSerialNo()).getCourtCase();
					//initialCourtDate = getDocumentsManagerPortProxy().getInitialCourtDateByCourtCaseId(summonsBO.getCourtCase().getCourtCaseId());
					
					String associatedWarningNotice = getDocumentsManagerService().getAssociatedWarningNotice(summonsBO.getRoadCheckOffenceOutcomeCode());
					
					if (summonsBO.getServedAtAddressBO() == null) {
						summonsBO.setServedAtAddressBO(new AddressBO());
					}

					if (summonsBO.getDischargeWitness() == null) {
						summonsBO.setDischargeWitness(new PersonBO());
					}

					
					summonsBO.setOffenderRoleObserved(document.getOffenderRoleObserved());
					
					context.getFlowScope().put(SUMMONS, summonsBO);
					context.getFlowScope().put(WARNING_NOTICE, null);
					context.getFlowScope().put(WARNING_NOTICE_NO_PROSECUTION, null);
					context.getFlowScope().put("updatedCourtCase", courtCaseBO);
					context.getFlowScope().put("updatedCourtAppearance", courtCaseBO.getCourtAppearances().get(0));

					// refresh details
					document.setStatusDescription(summonsBO.getStatusDescription());
					document.setStatusId(summonsBO.getStatusId());
					document.setAuthorizedFlag(summonsBO.getAuthorizedFlag());
					document.setVerificationRequired(summonsBO.getVerificationRequired());
					document.setAllowUpload(summonsBO.getAllowUpload());
					document.setManualSerialNo(summonsBO.getManualSerialNo());
					document.setJpRegNumber(summonsBO.getJpRegNumber());
					document.setJpIdNumber(summonsBO.getJpIdNumber());
					document.setPrintCount(summonsBO.getPrintCount());
					document.setLastUpdateUser(summonsBO.getLastUpdateUser());
					context.getFlowScope().put(DOCUMENT_IN_VIEW, document);
					context.getFlowScope().put(DOCUMENT_IN_VIEW_COPY, document);
					//document.setInitialCourtDtime(initialCourtDate);
					context.getFlowScope().put("finalRuling", YesNo.NO_LABEL);
					context.getFlowScope().put("associatedWarningNotice", associatedWarningNotice);
					
					//get court object for the next court appearance
					CourtCriteriaBO courtCriteria = new CourtCriteriaBO();
					courtCriteria.setCourtId(courtCaseBO.getCourtAppearances().get(0).getCourtId());
					List<CourtBO> court = getMaintenanceService().lookupCourt(courtCriteria);
					context.getFlowScope().put("courtParish", court.get(0).getParishCode());
					

					// check if authorisation needs to be done
					if (((summonsBO.getAuthorizedFlag() != null && summonsBO.getAuthorizedFlag().equalsIgnoreCase(Constants.YesNo.NO_LABEL_STR + "")) || (summonsBO.getVerificationRequired() != null && summonsBO.getVerificationRequired().equalsIgnoreCase(Constants.YesNo.YES_LABEL_STR + "")))&& !summonsBO.getStatusId().equalsIgnoreCase(DocumentStatus.CANCELLED)) {
						FacesMessage msg = new FacesMessage("Document changes need to be verified before any action can be taken.");
						msg.setSeverity(FacesMessage.SEVERITY_WARN);
						FacesContext.getCurrentInstance().addMessage(null, msg);
					}

					// check if authorisation needs to be done
					/* if (summonsBO.getAuthorizedFlag() != null &&
					 * summonsBO.getAuthorizedFlag().equalsIgnoreCase(Constants.YesNo.NO_LABEL_STR + "")) { FacesMessage
					 * msg = new FacesMessage("Document changes need to be authorised.");
					 * msg.setSeverity(FacesMessage.SEVERITY_WARN); FacesContext.getCurrentInstance().addMessage(null,
					 * msg); } */
					// ensure JP details exists
					if (summonsBO.getJpIdNumber() == null && summonsBO.getStatusId().equalsIgnoreCase(DocumentStatus.PRINTED)) {
						FacesMessage msg = new FacesMessage("Justice of the Peace details needs to be added before document can be served.");
						msg.setSeverity(FacesMessage.SEVERITY_WARN);
						FacesContext.getCurrentInstance().addMessage(null, msg);
					}
					

					// check if verification needs to be done (document came from a terminated or cancelled road check)
					/* if (summonsBO.getVerificationRequired() != null &&
					 * summonsBO.getVerificationRequired().equalsIgnoreCase(Constants.YesNo.YES_LABEL_STR + "")) {
					 * FacesMessage msg = new
					 * FacesMessage("Document changes need to be verified before any action can be taken.");
					 * msg.setSeverity(FacesMessage.SEVERITY_WARN); FacesContext.getCurrentInstance().addMessage(null,
					 * msg); } */
						
						//check if initial court date is set
						/*if (initialCourtDate == null) {
//							FacesMessage msg = new FacesMessage("Initial Court Date needs to be set before any action can be taken.");
//							msg.setSeverity(FacesMessage.SEVERITY_WARN);
//							FacesContext.getCurrentInstance().addMessage(null, msg);
						}*/
						
						if(summonsBO.getScannedDocList() != null){
							List<ScannedDocBO> scannedDocs = summonsBO.getScannedDocList();
							for(ScannedDocBO doc:scannedDocs){
								if(doc.getDocType().equals(Constants.ScannedDocumentTypeShort.MANUAL_DOCUMENT)){
									context.getFlowScope().put("manualScannedDocAttached", true);
								}
							}
						}
					
					
				}

				if (document.getDocumentTypeCode().equalsIgnoreCase(Constants.DocumentType.WARNING_NOTICE)) {
					WarningNoticeBO warningNoticeBO = null;

					warningNoticeBO = getDocumentsManagerService().getWarningNoticeDetails(document.getAutomaticSerialNo());
					
					warningNoticeBO.setOffenderRoleObserved(document.getOffenderRoleObserved());
					
					context.getFlowScope().put(WARNING_NOTICE, warningNoticeBO);
					context.getFlowScope().put(SUMMONS, null);
					context.getFlowScope().put(WARNING_NOTICE_NO_PROSECUTION, null);

					// refresh details
					document.setStatusDescription(warningNoticeBO.getStatusDescription());
					document.setStatusId(warningNoticeBO.getStatusId());
					document.setAuthorizedFlag(warningNoticeBO.getAuthorizedFlag());
					document.setVerificationRequired(warningNoticeBO.getVerificationRequired());
					document.setAllowUpload(warningNoticeBO.getAllowUpload());
					document.setManualSerialNo(warningNoticeBO.getManualSerialNo());
					document.setPrintCount(warningNoticeBO.getPrintCount());
					document.setLastUpdateUser(warningNoticeBO.getLastUpdateUser());
					context.getFlowScope().put(DOCUMENT_IN_VIEW, document);
					context.getFlowScope().put(DOCUMENT_IN_VIEW_COPY, document);

					/* // check if authorisation needs to be done if (warningNoticeBO.getAuthorizedFlag() != null &&
					 * warningNoticeBO.getAuthorizedFlag().equalsIgnoreCase(Constants.YesNo.NO_LABEL_STR + "")) {
					 * FacesMessage msg = new FacesMessage("Document changes need to be authorised.");
					 * msg.setSeverity(FacesMessage.SEVERITY_WARN); FacesContext.getCurrentInstance().addMessage(null,
					 * msg); } // check if verification needs to be done (document came from a terminated or cancelled
					 * road check) if (warningNoticeBO.getVerificationRequired() != null &&
					 * warningNoticeBO.getVerificationRequired().equalsIgnoreCase(Constants.YesNo.YES_LABEL_STR + "")) {
					 * FacesMessage msg = new
					 * FacesMessage("Document changes need to be verified before any action can be taken.");
					 * msg.setSeverity(FacesMessage.SEVERITY_WARN); FacesContext.getCurrentInstance().addMessage(null,
					 * msg); } */

					if (((warningNoticeBO.getAuthorizedFlag() != null && warningNoticeBO.getAuthorizedFlag().equalsIgnoreCase(Constants.YesNo.NO_LABEL_STR + "")) || (warningNoticeBO.getVerificationRequired() != null && warningNoticeBO.getVerificationRequired().equalsIgnoreCase(Constants.YesNo.YES_LABEL_STR + "")))&& !warningNoticeBO.getStatusId().equalsIgnoreCase(DocumentStatus.CANCELLED)) {
						FacesMessage msg = new FacesMessage("Document changes need to be verified before any action can be taken.");
						msg.setSeverity(FacesMessage.SEVERITY_WARN);
						FacesContext.getCurrentInstance().addMessage(null, msg);
					}
					
					if(warningNoticeBO.getScannedDocList() != null){
						List<ScannedDocBO> scannedDocs = warningNoticeBO.getScannedDocList();
						for(ScannedDocBO doc:scannedDocs){
							if(doc.getDocType().equals(Constants.ScannedDocumentTypeShort.MANUAL_DOCUMENT)){
								context.getFlowScope().put("manualScannedDocAttached", true);
							}
						}
					}
				}

				if (document.getDocumentTypeCode().equalsIgnoreCase(Constants.DocumentType.WARNING_NOTICE_NO_PROSECUTION)) {
					WarningNoProsecutionBO warningNoProsecutionBO = null;

					warningNoProsecutionBO = getDocumentsManagerService().getWarningNoProsecutionDetails(document.getAutomaticSerialNo());
					
					warningNoProsecutionBO.setOffenderRoleObserved(document.getOffenderRoleObserved());
					
					context.getFlowScope().put(WARNING_NOTICE_NO_PROSECUTION, warningNoProsecutionBO);
					context.getFlowScope().put(SUMMONS, null);
					context.getFlowScope().put(WARNING_NOTICE, null);

					// refresh details
					// document = new DocumentViewBO(warningNoProsecutionBO);
					document.setStatusDescription(warningNoProsecutionBO.getStatusDescription());
					document.setStatusId(warningNoProsecutionBO.getStatusId());
					document.setAuthorizedFlag(warningNoProsecutionBO.getAuthorizedFlag());
					document.setVerificationRequired(warningNoProsecutionBO.getVerificationRequired());
					document.setAllowUpload(warningNoProsecutionBO.getAllowUpload());
					document.setManualSerialNo(warningNoProsecutionBO.getManualSerialNo());
					document.setPrintCount(warningNoProsecutionBO.getPrintCount());
					document.setLastUpdateUser(warningNoProsecutionBO.getLastUpdateUser());
					context.getFlowScope().put(DOCUMENT_IN_VIEW, document);
					context.getFlowScope().put(DOCUMENT_IN_VIEW_COPY, document);

					/* // check if authorisation needs to be done if (warningNoProsecutionBO.getAuthorizedFlag() != null
					 * && warningNoProsecutionBO.getAuthorizedFlag().equalsIgnoreCase(Constants.YesNo.NO_LABEL_STR +
					 * "")) { FacesMessage msg = new FacesMessage("Document changes need to be authorised.");
					 * msg.setSeverity(FacesMessage.SEVERITY_WARN); FacesContext.getCurrentInstance().addMessage(null,
					 * msg); } // check if verification needs to be done (document came from a terminated or cancelled
					 * road check) if (warningNoProsecutionBO.getVerificationRequired() != null &&
					 * warningNoProsecutionBO.getVerificationRequired().equalsIgnoreCase(Constants.YesNo.YES_LABEL_STR +
					 * "")) { FacesMessage msg = new
					 * FacesMessage("Document changes need to be verified before any action can be taken.");
					 * msg.setSeverity(FacesMessage.SEVERITY_WARN); FacesContext.getCurrentInstance().addMessage(null,
					 * msg); } */

					if (((warningNoProsecutionBO.getAuthorizedFlag() != null && warningNoProsecutionBO.getAuthorizedFlag().equalsIgnoreCase(Constants.YesNo.NO_LABEL_STR + "")) || (warningNoProsecutionBO.getVerificationRequired() != null && warningNoProsecutionBO.getVerificationRequired().equalsIgnoreCase(Constants.YesNo.YES_LABEL_STR + "")))&& !warningNoProsecutionBO.getStatusId().equalsIgnoreCase(DocumentStatus.CANCELLED)) {
						FacesMessage msg = new FacesMessage("Document changes need to be verified before any action can be taken.");
						msg.setSeverity(FacesMessage.SEVERITY_WARN);
						FacesContext.getCurrentInstance().addMessage(null, msg);
					}
					
					if(warningNoProsecutionBO.getScannedDocList() != null){
						List<ScannedDocBO> scannedDocs = warningNoProsecutionBO.getScannedDocList();
						for(ScannedDocBO doc:scannedDocs){
							if(doc.getDocType().equals(Constants.ScannedDocumentTypeShort.MANUAL_DOCUMENT)){
								context.getFlowScope().put("manualScannedDocAttached", true);
							}
						}
					}

				}
			} else {
				// System.err.println("Document type empty - debug.");
			}
			
			if(document.getStatusId().equalsIgnoreCase(DocumentStatus.CANCELLED) || document.getStatusId().equalsIgnoreCase(DocumentStatus.WITHDRAWN)){
				context.getMessageContext().clearMessages();
			}

		} catch (Exception e) {

			e.printStackTrace();
			addErrorMessage(context, "DocLoadError");

			return error();

		}
		return success();
	}

	/**
	 * From the search screen this interfaces with the print link on the data table Print multiple documents Documents
	 * must be in "created" state to be printed from this module
	 * 
	 * @param context
	 * @return success if all is well
	 */
	public Event printSelectedDocuments(RequestContext context) {

		ArrayList<DocumentViewBO> selectedDocuments = (ArrayList<DocumentViewBO>) context.getFlowScope().get("documentsSelected");
		HttpServletRequest req = (HttpServletRequest) context.getExternalContext().getNativeRequest();
		HttpSession session = req.getSession();

		if (selectedDocuments == null || selectedDocuments.isEmpty()) {
			context.getFlowScope().put("viewScannedDoc", false);
			context.getFlowScope().put("openReportWindow", false);
			context.getFlowScope().put("printDocumentFromList", false);
			addErrorMessage(context, "DocListEmpty");
			return error();
		} else {
			ArrayList<DocumentViewBO> tempDocuments = new ArrayList<DocumentViewBO>();
			tempDocuments.addAll(selectedDocuments);
			selectedDocuments.clear();

			// based on bug on the UI exclude all documents that are not in CREATED status
			for (DocumentViewBO doc : tempDocuments) {
				// System.err.println(" doc :" + doc.getStatusId());
				if (doc.getStatusId().equalsIgnoreCase(DocumentStatus.CREATED) && (doc.getVerificationRequired() == null || doc.getVerificationRequired().equals(YesNo.NO_LABEL_STR))) {
					selectedDocuments.add(doc);
					// System.err.println(" this one is created ");
				}
			}
			// System.err.println(" selected " + selectedDocuments);

			if (selectedDocuments == null || selectedDocuments.isEmpty()) {
				context.getFlowScope().put("viewScannedDoc", false);
				context.getFlowScope().put("openReportWindow", false);
				session.setAttribute(Constants.DocumentPrinting.DOCUMENT_LIST, selectedDocuments);
				context.getFlowScope().put("printDocumentFromList", false);
				addErrorMessage(context, "DocListEmpty");
				return error();
			}
		}

		try {

			session.setAttribute(Constants.DocumentPrinting.DOCUMENT_LIST, selectedDocuments);
			// set the current users details
			session.setAttribute(GenerateDocServlet.ROMS_LOGGED_IN_USER, getRomsLoggedInUser().getUsername());

			session.setAttribute(Constants.IS_MOBILE, this.isHandHeld(context));

			// update the documents to printed
			getDocumentsManagerService().printAllDocuments(selectedDocuments, null, null, getRomsLoggedInUser().getUsername());

			// re-search
			search(context);
			context.getMessageContext().clearMessages();
			addInfoMessageText(context, "Documents generated successfully.");

			context.getFlowScope().put("viewScannedDoc", false);
			context.getFlowScope().put("openReportWindow", false);
			context.getFlowScope().put("printDocumentFromList", true);
			context.getFlowScope().put("documentsSelected", new ArrayList<DocumentViewBO>());

		} catch (Exception e) {
			e.printStackTrace();
			addErrorMessageText(context, e.getMessage());
			return error();
		}

		return success();
	}

	/********************************** VIEW MODE ***********************************/
	/**
	 * Edit the editable details of the object
	 * 
	 * @param context
	 * @return
	 */
	public Event editDocument(RequestContext context) {

		DocumentViewBO document = (DocumentViewBO) context.getFlowScope().get(DOCUMENT_IN_VIEW);
		
		// get the list currently storing files form the flowscope
		ScannedDocBO scannedDocToUpload = (ScannedDocBO) context.getFlowScope().get("scannedDocToUpload");

		if (document == null) {
			addErrorMessage(context, "NoDetailsToDisplay");
			return error();
		}

		// set these values here so upload does not fail
		if (scannedDocToUpload != null) {
			scannedDocToUpload.setDocType("MAN");
			scannedDocToUpload.setDescription("Scanned Manual Document");
			context.getFlowScope().put("scannedDocToUpload", scannedDocToUpload);
		}

		try {
			if (StringUtils.isNotBlank(document.getDocumentTypeCode())) {

				if (document.getDocumentTypeCode().equalsIgnoreCase(Constants.DocumentType.SUMMONS)) {
					SummonsBO summonsBO = (SummonsBO) context.getFlowScope().get(SUMMONS);

					// validate then save
					if (validateForEdit(context)) {

						summonsBO.setNewStatusId(Constants.DocumentStatus.EDITED);
						summonsBO.setAuthorizedFlag(Constants.YesNo.NO_LABEL_STR + "");
						summonsBO.setCurrentUsername(getRomsLoggedInUser().getUsername());

						summonsBO = getDocumentsManagerService().updateSummons(summonsBO);

						// ensure that the document uploaded is saved
						if (StringUtils.isNotBlank(summonsBO.getManualSerialNo())) {
							summonsBO.setManualSerialNo(summonsBO.getManualSerialNo().toUpperCase());// 2014-06-30
							if (scannedDocToUpload != null && scannedDocToUpload.getFileContents() != null) {
								//remove previous Manual Scanned Doc
								if(summonsBO.getScannedDocList() != null){
									DocumentViewBO docview = new DocumentViewBO();
									
									docview = copyDocumentViewBO(docview,document);
									
									for(ScannedDocBO doc:summonsBO.getScannedDocList()){
										if(doc.getDocType().equals(Constants.ScannedDocumentTypeShort.MANUAL_DOCUMENT)){
											ScannedDocBO scannedDoc = new ScannedDocBO();
											copyFields(scannedDoc,doc);
											scannedDoc.setCurrentUsername(getRomsLoggedInUser().getUsername());
											getScannedDocUploadService().deleteScannedDoc(scannedDoc,docview);
										}
									}
								}
								//upload new Manual Scanned Doc
								Event after = uploadDocument(context);
								if (after.getId().equalsIgnoreCase("error"))
									return error();
							}
						}

						// reset the document details in view
						loadDocumentDetails(context);
						context.getFlowScope().put(MODE, "view");
						addInfoMessageWithParameter(context, "DocUpdatedSuccessfully", document.getDocumentTypeName());
						return success();

					} else {

						return error();
					}

				}

				if (document.getDocumentTypeCode().equalsIgnoreCase(Constants.DocumentType.WARNING_NOTICE)) {
					WarningNoticeBO warningNoticeBO = (WarningNoticeBO) context.getFlowScope().get(WARNING_NOTICE);

					// validate then save
					if (validateForEdit(context)) {

						warningNoticeBO.setNewStatusId(Constants.DocumentStatus.EDITED);
						warningNoticeBO.setAuthorizedFlag(Constants.YesNo.NO_LABEL_STR + "");
						warningNoticeBO.setCurrentUsername(getRomsLoggedInUser().getUsername());

						warningNoticeBO = getDocumentsManagerService().updateWarningNotice(warningNoticeBO);

						// ensure that the document uploaded is saved
						if (StringUtils.isNotBlank(warningNoticeBO.getManualSerialNo())) {
							warningNoticeBO.setManualSerialNo(warningNoticeBO.getManualSerialNo().toUpperCase());// 2014-06-30
							if (scannedDocToUpload != null && scannedDocToUpload.getFileContents() != null) {
								//remove previous Manual Scanned Doc
								if(warningNoticeBO.getScannedDocList() != null){
									DocumentViewBO docview = new DocumentViewBO();

									docview = copyDocumentViewBO(docview,document);
									
									for(ScannedDocBO doc:warningNoticeBO.getScannedDocList()){
										if(doc.getDocType().equals(Constants.ScannedDocumentTypeShort.MANUAL_DOCUMENT)){
											ScannedDocBO scannedDoc = new ScannedDocBO();
											copyFields(scannedDoc,doc);
											scannedDoc.setCurrentUsername(getRomsLoggedInUser().getUsername());
											getScannedDocUploadService().deleteScannedDoc(scannedDoc,docview);
										}
									}
								}
								//upload new Manual Scanned Doc
								Event after = uploadDocument(context);
								if (after.getId().equalsIgnoreCase("error"))
									return error();
							}
						}

						// reset the document details in view
						loadDocumentDetails(context);
						context.getFlowScope().put(MODE, "view");
						addInfoMessageWithParameter(context, "DocUpdatedSuccessfully", document.getDocumentTypeName());
						return success();
					} else {

						return error();
					}
				}

				if (document.getDocumentTypeCode().equalsIgnoreCase(Constants.DocumentType.WARNING_NOTICE_NO_PROSECUTION)) {
					WarningNoProsecutionBO warningNoProsecutionBO = (WarningNoProsecutionBO) context.getFlowScope().get(WARNING_NOTICE_NO_PROSECUTION);

					// validate then save
					if (validateForEdit(context)) {

						warningNoProsecutionBO.setAuthorizedFlag(Constants.YesNo.NO_LABEL_STR + "");
						warningNoProsecutionBO.setCurrentUsername(getRomsLoggedInUser().getUsername());
						warningNoProsecutionBO.setNewStatusId(Constants.DocumentStatus.EDITED);

						warningNoProsecutionBO = getDocumentsManagerService().updateWarningNoProsecution(warningNoProsecutionBO);

						// ensure that the document uploaded is saved
						if (StringUtils.isNotBlank(warningNoProsecutionBO.getManualSerialNo())) {
							warningNoProsecutionBO.setManualSerialNo(warningNoProsecutionBO.getManualSerialNo().toUpperCase());// 2014-06-30
							if (scannedDocToUpload != null && scannedDocToUpload.getFileContents() != null) {
								//remove previous Manual Scanned Doc
								if(warningNoProsecutionBO.getScannedDocList() != null){
									DocumentViewBO docview = new DocumentViewBO();

									docview = copyDocumentViewBO(docview,document);
									
									for(ScannedDocBO doc:warningNoProsecutionBO.getScannedDocList()){
										if(doc.getDocType().equals(Constants.ScannedDocumentTypeShort.MANUAL_DOCUMENT)){
											ScannedDocBO scannedDoc = new ScannedDocBO();
											copyFields(scannedDoc,doc);
											scannedDoc.setCurrentUsername(getRomsLoggedInUser().getUsername());
											getScannedDocUploadService().deleteScannedDoc(scannedDoc,docview);
										}
									}
								}
								//upload new Manual Scanned Doc
								Event after = uploadDocument(context);
								if (after.getId().equalsIgnoreCase("error"))
									return error();
							}
						}

						// reset the document details in view
						this.loadDocumentDetails(context);
						context.getFlowScope().put(MODE, "view");
						addInfoMessageWithParameter(context, "DocUpdatedSuccessfully", document.getDocumentTypeName());
						return success();
					} else {

						return error();
					}

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			addErrorMessageText(context, e.getMessage());
			return error();
		}

		addErrorMessage(context, "DocUpdateError");
		return error();

	}
	
	/*public Event addInitialCourtDate(RequestContext context) {
		DocumentViewBO document = (DocumentViewBO) context.getFlowScope().get(DOCUMENT_IN_VIEW);
		// reset the document details in view
		loadDocumentDetails(context);
		context.getFlowScope().put(MODE, "view");
		addInfoMessageWithParameter(context, "DocUpdatedSuccessfully", document.getDocumentTypeName());
		return success();
	}*/

	/**
	 * 
	 * @param context
	 * @return
	 */
	public Event cancelDocument(RequestContext context) {
		DocumentViewBO document = (DocumentViewBO) context.getFlowScope().get(DOCUMENT_IN_VIEW);

		if (document == null) {
			addErrorMessage(context, "NoDetailsToDisplay");
			return error();
		}

		try {
			if (StringUtils.isNotBlank(document.getDocumentTypeCode())) {

				if (document.getDocumentTypeCode().equalsIgnoreCase(Constants.DocumentType.SUMMONS)) {
					SummonsBO summonsBO = (SummonsBO) context.getFlowScope().get(SUMMONS);

					// validate then save
					if (validateForCancel(context)) {

						summonsBO.setNewStatusId(Constants.DocumentStatus.CANCELLED);
						summonsBO.setCurrentUsername(getRomsLoggedInUser().getUsername());
						summonsBO = getDocumentsManagerService().updateSummons(summonsBO);
						// update the details on screen
						addInfoMessageWithParameter(context, "DocCancelledSuccessfully", document.getDocumentTypeName());

						this.loadDocumentDetails(context);
						context.getFlowScope().put(MODE, "view");
						return success();

					} else {
						return error();
					}

				}

				if (document.getDocumentTypeCode().equalsIgnoreCase(Constants.DocumentType.WARNING_NOTICE)) {
					WarningNoticeBO warningNoticeBO = (WarningNoticeBO) context.getFlowScope().get(WARNING_NOTICE);

					// validate then save
					if (validateForCancel(context)) {
						warningNoticeBO.setNewStatusId(Constants.DocumentStatus.CANCELLED);
						warningNoticeBO.setCurrentUsername(getRomsLoggedInUser().getUsername());
						warningNoticeBO = getDocumentsManagerService().updateWarningNotice(warningNoticeBO);
						// update the details on screen
						addInfoMessageWithParameter(context, "DocCancelledSuccessfully", document.getDocumentTypeName());

						this.loadDocumentDetails(context);
						context.getFlowScope().put(MODE, "view");
						return success();
					} else {
						return error();
					}
				}

				if (document.getDocumentTypeCode().equalsIgnoreCase(Constants.DocumentType.WARNING_NOTICE_NO_PROSECUTION)) {
					WarningNoProsecutionBO warningNoProsecutionBO = (WarningNoProsecutionBO) context.getFlowScope().get(WARNING_NOTICE_NO_PROSECUTION);

					// validate then save
					if (validateForCancel(context)) {
						warningNoProsecutionBO.setNewStatusId(Constants.DocumentStatus.CANCELLED);
						warningNoProsecutionBO.setCurrentUsername(getRomsLoggedInUser().getUsername());
						warningNoProsecutionBO = getDocumentsManagerService().updateWarningNoProsecution(warningNoProsecutionBO);

						// update the details on screen
						addInfoMessageWithParameter(context, "DocCancelledSuccessfully", document.getDocumentTypeName());

						this.loadDocumentDetails(context);
						context.getFlowScope().put(MODE, "view");
						return success();

					} else {

						return error();
					}

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			addErrorMessageText(context, e.getMessage());

			return error();
		}

		// addErrorMessage(context, "DocCancelError");
		return error();
	}

	/**
	 * Called to serve document
	 * 
	 * @param context
	 * @return
	 */
	public Event serveDocument(RequestContext context) {

		DocumentViewBO document = (DocumentViewBO) context.getFlowScope().get(DOCUMENT_IN_VIEW);
		StaffUserMappingBO staffBO = (StaffUserMappingBO) context.getFlowScope().get("staffBOForServe");
		
		if (document == null) {
			addErrorMessage(context, "NoDetailsToDisplay");
			return error();
		}

		try {

			if (StringUtils.isNotBlank(document.getDocumentTypeCode())) {
				if (document.getDocumentTypeCode().equalsIgnoreCase(Constants.DocumentType.SUMMONS)) {
					SummonsBO summonsBO = (SummonsBO) context.getFlowScope().get(SUMMONS);
					if(staffBO != null)
						summonsBO.setServedByUserID(staffBO.getStaffId());
					// validate then save
					if (validateForServe(context)) {
						summonsBO.setNewStatusId(Constants.DocumentStatus.SERVED);
						summonsBO.setCurrentUsername(getRomsLoggedInUser().getUsername());
						summonsBO = getDocumentsManagerService().updateSummons(summonsBO);

						// update the details on the screen
						this.loadDocumentDetails(context);
						context.getFlowScope().put(MODE, "view");
					} else {
						//summonsBO.setServedOnDate(null);
						return error();
					}
				}

				if (document.getDocumentTypeCode().equalsIgnoreCase(Constants.DocumentType.WARNING_NOTICE)) {
					WarningNoticeBO warningNoticeBO = (WarningNoticeBO) context.getFlowScope().get(WARNING_NOTICE);
					if(staffBO != null)
						warningNoticeBO.setServedByUserID(staffBO.getStaffId());
					// validate then save
					if (validateForServe(context)) {
						warningNoticeBO.setNewStatusId(Constants.DocumentStatus.SERVED);
						warningNoticeBO.setCurrentUsername(getRomsLoggedInUser().getUsername());
						warningNoticeBO = getDocumentsManagerService().updateWarningNotice(warningNoticeBO);

						// update the details on the screen
						this.loadDocumentDetails(context);
						context.getFlowScope().put(MODE, "view");
					} else {
						//warningNoticeBO.setServedOnDate(null);
						return error();
					}
				}

				if (document.getDocumentTypeCode().equalsIgnoreCase(Constants.DocumentType.WARNING_NOTICE_NO_PROSECUTION)) {
					WarningNoProsecutionBO warningNoProsecution = (WarningNoProsecutionBO) context.getFlowScope().get(WARNING_NOTICE_NO_PROSECUTION);
					if(staffBO != null)
						warningNoProsecution.setServedByUserID(staffBO.getStaffId());
					// validate then save
					if (validateForServe(context)) {
						warningNoProsecution.setNewStatusId(Constants.DocumentStatus.SERVED);
						warningNoProsecution.setCurrentUsername(getRomsLoggedInUser().getUsername());
						warningNoProsecution = getDocumentsManagerService().updateWarningNoProsecution(warningNoProsecution);

						// update the details on the screen
						this.loadDocumentDetails(context);
						context.getFlowScope().put(MODE, "view");
					} else {
						//warningNoProsecution.setServedOnDate(null);
						return error();
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			addErrorMessageText(context, e.getMessage());
			return error();
		}
		addInfoMessage(context, "DocServedSuccessfully");
		return success();
	}

	/**
	 * The Document is withdrawn, cancel the case and the court appearance too
	 * 
	 * @param context
	 * @return
	 */
	public Event withdrawDocument(RequestContext context) {
		DocumentViewBO document = (DocumentViewBO) context.getFlowScope().get(DOCUMENT_IN_VIEW);

		if (document == null) {
			addErrorMessage(context, "NoDetailsToDisplay");
			return error();
		}

		try {

			if (StringUtils.isNotBlank(document.getDocumentTypeCode())) {

				if (document.getDocumentTypeCode().equalsIgnoreCase(Constants.DocumentType.SUMMONS)) {
					SummonsBO summonsBO = (SummonsBO) context.getFlowScope().get(SUMMONS);

					// validate then save
					if (validateForWithdraw(context)) {
						SummonsDischargeAndReleaseFormView releaseForm = null;

						summonsBO.setNewStatusId(Constants.DocumentStatus.WITHDRAWN);

						summonsBO.setCurrentUsername(getRomsLoggedInUser().getUsername());
						releaseForm = getDocumentsManagerService().withdrawSummons(summonsBO);

						// summonsBO.setStatusId(Constants.DocumentStatus.WITHDRAWN);
						// summonsBO.setStatusDescription("Withdrawn");

						// add success message to screen
						addInfoMessage(context, "DocWithdrawnSuccessfully");
						// reset the document details in view
						this.loadDocumentDetails(context);
						context.getFlowScope().put(MODE, "view");

					} else {
						return error();
					}
				}

			}
		} catch (Exception e) {

			e.printStackTrace();
			addErrorMessageText(context, e.getMessage());
			return error();
		}
		return success();
	}

	/**
	 * 
	 * @param context
	 * @return
	 */
	public Event verifyUserCredentials(RequestContext context) {
		DocumentViewBO document = (DocumentViewBO) context.getFlowScope().get(DOCUMENT_IN_VIEW);
		if (document == null) {
			addErrorMessage(context, "NoDetailsToDisplay");
			return error();
		}

		// get verify view
		VerifyDetailsSecurityView verifyView = (VerifyDetailsSecurityView) context.getFlowScope().get("verifyView");

		if (verifyView == null) {
			addErrorMessage(context, "EnterVerificationDetails");
			return error();
		}

		// set the actual permisson being Tested for
		/* verifyView.setPermission(UserPermissions.ROMS_DOC_MAN_JP_VERIFY); */

		verifyView.setLastUpdatedUser(document.getLastUpdateUser());
		if (document.getDocumentTypeCode().equalsIgnoreCase(Constants.DocumentType.SUMMONS)) {
			SummonsBO summonsBO = (SummonsBO) context.getFlowScope().get(SUMMONS);
			verifyView.setComment(summonsBO.getNewComment());
		}
		if (document.getDocumentTypeCode().equalsIgnoreCase(Constants.DocumentType.WARNING_NOTICE)) {
			WarningNoticeBO warningNoticeBO = (WarningNoticeBO) context.getFlowScope().get(WARNING_NOTICE);
			verifyView.setComment(warningNoticeBO.getNewComment());
		}
		if (document.getDocumentTypeCode().equalsIgnoreCase(Constants.DocumentType.WARNING_NOTICE_NO_PROSECUTION)) {
			WarningNoProsecutionBO warningNoProsecutionBO = (WarningNoProsecutionBO) context.getFlowScope().get(WARNING_NOTICE_NO_PROSECUTION);
			verifyView.setComment(warningNoProsecutionBO.getNewComment());
		}
		try {
			// validate then save
			if (validateOverrideDetails(context, verifyView)) {

				// after we have validated user details set it so it can be used
				document.setAuthorizeUsername(verifyView.getUserName());
				context.getFlowScope().put(DOCUMENT_IN_VIEW, document);

				// used for verification of details from Road check screen
				if (verifyView.getPermission().equals(UserPermissions.ROMS_DOC_MAN_JP_VERIFY)) {

					return verifyDocumentDetails(context);
					// context.getFlowScope().put(MODE, "jpverify");
				}

				// used to verify edits to document on document manager screen
				if (verifyView.getPermission().equals(UserPermissions.ROMS_DOC_MAN_VERIFY)) {
					return verifyDocumentEdits(context);
					// context.getFlowScope().put(MODE, "verify");

				}

				// addInfoMessageText(context, "Document changes now verified.");
			} else {
				// System.err.println(" error occurs on verify");
				return error();
			}
		} catch (Exception e) {

			e.printStackTrace();

			addErrorMessageText(context, e.getMessage());
			return error();
		}

		context.getFlowScope().put(MODE, "view");
		return success();
	}

	/**
	 * 
	 * @param context
	 * @return
	 */
	public Event verifyJPDetails(RequestContext context) {
		DocumentViewBO document = (DocumentViewBO) context.getFlowScope().get(DOCUMENT_IN_VIEW);

		if (document == null) {
			addErrorMessage(context, "NoDetailsToDisplay");
			return error();
		}

		try {
			// validate then save
			// if (validateOverrideDetails(context,verifyView)) {

			if (StringUtils.isNotBlank(document.getDocumentTypeCode())) {

				if (document.getDocumentTypeCode().equalsIgnoreCase(Constants.DocumentType.SUMMONS)) {
					SummonsBO summonsBO = (SummonsBO) context.getFlowScope().get(SUMMONS);
					summonsBO.setCurrentUsername(document.getAuthorizeUsername());// getRomsLoggedInUser().getUsername());
					summonsBO.setNewStatusId(Constants.DocumentStatus.AUTHORISED);
					summonsBO.setAuthorizedFlag(Constants.YesNo.YES_LABEL + "");
					summonsBO.setJPAuthorisationRequested(true);

					// update comment if not blank
					if (StringUtils.isNotBlank(summonsBO.getNewComment())) {
						summonsBO.setComment(formatDocumentComment(summonsBO.getNewComment(), summonsBO.getComment()));
					}
					summonsBO = getDocumentsManagerService().updateSummons(summonsBO);

				}

				addInfoMessageText(context, "JP Details now verified.");

			}
			// reset the document details in view
			this.loadDocumentDetails(context);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			addErrorMessageText(context, "There was an error verifying JP Details.");
			return error();
		}
		// System.err.println("Method called JP verify");
		return success();
	}

	/**
	 * 
	 * @param context
	 * @return
	 */
	public Event verifyDocumentDetails(RequestContext context) {
		DocumentViewBO document = (DocumentViewBO) context.getFlowScope().get(DOCUMENT_IN_VIEW);

		if (document == null) {
			addErrorMessage(context, "NoDetailsToDisplay");
			return error();
		}

		try {
			// validate then save

			if (StringUtils.isNotBlank(document.getDocumentTypeCode())) {

				if (document.getDocumentTypeCode().equalsIgnoreCase(Constants.DocumentType.SUMMONS)) {
					SummonsBO summonsBO = (SummonsBO) context.getFlowScope().get(SUMMONS);
					summonsBO.setCurrentUsername(document.getAuthorizeUsername());// getRomsLoggedInUser().getUsername());
					summonsBO.setNewStatusId(Constants.DocumentStatus.VERIFIED);
					// summonsBO.setVerificationRequired(Constants.YesNo.NO_LABEL_STR + "");
					// summonsBO.setSupervisorAuthorisationRequested(false);

					// update comment if not blank
					if (StringUtils.isNotBlank(summonsBO.getNewComment())) {
						summonsBO.setComment(formatDocumentComment(summonsBO.getNewComment(), summonsBO.getComment()));
					}
					summonsBO = getDocumentsManagerService().updateSummons(summonsBO);

					// update the details on screen
					// context.getFlowScope().put(SUMMONS, summonsBO);

				}

				if (document.getDocumentTypeCode().equalsIgnoreCase(Constants.DocumentType.WARNING_NOTICE)) {
					WarningNoticeBO warningNoticeBO = (WarningNoticeBO) context.getFlowScope().get(WARNING_NOTICE);

					warningNoticeBO.setCurrentUsername(document.getAuthorizeUsername());// getRomsLoggedInUser().getUsername());
					warningNoticeBO.setNewStatusId(Constants.DocumentStatus.VERIFIED);
					// warningNoticeBO.setSupervisorAuthorisationRequested(true);
					// warningNoticeBO.setVerificationRequired(Constants.YesNo.NO_LABEL_STR + "");

					// update comment if not blank
					if (StringUtils.isNotBlank(warningNoticeBO.getNewComment())) {
						warningNoticeBO.setComment(formatDocumentComment(warningNoticeBO.getNewComment(), warningNoticeBO.getComment()));
					}

					warningNoticeBO = getDocumentsManagerService().updateWarningNotice(warningNoticeBO);

					// update the details on screen
					// context.getFlowScope().put(WARNING_NOTICE, warningNoticeBO);

				}

				if (document.getDocumentTypeCode().equalsIgnoreCase(Constants.DocumentType.WARNING_NOTICE_NO_PROSECUTION)) {
					WarningNoProsecutionBO warningNoProsecutionBO = (WarningNoProsecutionBO) context.getFlowScope().get(WARNING_NOTICE_NO_PROSECUTION);

					warningNoProsecutionBO.setCurrentUsername(document.getAuthorizeUsername());// getRomsLoggedInUser().getUsername());
					warningNoProsecutionBO.setNewStatusId(Constants.DocumentStatus.VERIFIED);
					// warningNoProsecutionBO.setSupervisorAuthorisationRequested(true);
					// warningNoProsecutionBO.setVerificationRequired(Constants.YesNo.NO_LABEL_STR + "");

					// update comment if not blank
					if (StringUtils.isNotBlank(warningNoProsecutionBO.getNewComment())) {
						warningNoProsecutionBO.setComment(formatDocumentComment(warningNoProsecutionBO.getNewComment(), warningNoProsecutionBO.getComment()));
					}

					warningNoProsecutionBO = getDocumentsManagerService().updateWarningNoProsecution(warningNoProsecutionBO);

					// update the details on screen

				}
				// addInfoMessageText(context, "Document changes now verified.");
				// reset the document details in view
				this.loadDocumentDetails(context);
				addInfoMessageText(context, document.getDocumentTypeName() + " verified successfully.");
				context.getFlowScope().put(MODE, "view");
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			addErrorMessageText(context, "There was an error verifying document.");
			return error();
		}

		return success();
	}

	/**
	 * 
	 * @param context
	 * @return
	 */
	public Event verifyDocumentEdits(RequestContext context) {
		DocumentViewBO document = (DocumentViewBO) context.getFlowScope().get(DOCUMENT_IN_VIEW);

		if (document == null) {
			addErrorMessage(context, "NoDetailsToDisplay");
			return error();
		}

		try {
			// validate then save

			if (StringUtils.isNotBlank(document.getDocumentTypeCode())) {

				if (document.getDocumentTypeCode().equalsIgnoreCase(Constants.DocumentType.SUMMONS)) {
					SummonsBO summonsBO = (SummonsBO) context.getFlowScope().get(SUMMONS);
					summonsBO.setCurrentUsername(document.getAuthorizeUsername());// getRomsLoggedInUser().getUsername());
					summonsBO.setNewStatusId(Constants.DocumentStatus.AUTHORISED);
					// summonsBO.setAuthorizedFlag(Constants.YesNo.YES_LABEL_STR + "");
					// summonsBO.setSupervisorAuthorisationRequested(true);

					// update comment if not blank
					if (StringUtils.isNotBlank(summonsBO.getNewComment())) {
						summonsBO.setComment(formatOverrideDocumentComment(summonsBO.getNewComment(), summonsBO.getComment(), document.getAuthorizeUsername()));
					}
					summonsBO = getDocumentsManagerService().updateSummons(summonsBO);

					// update the details on screen
					// context.getFlowScope().put(SUMMONS, summonsBO);

				}

				if (document.getDocumentTypeCode().equalsIgnoreCase(Constants.DocumentType.WARNING_NOTICE)) {
					WarningNoticeBO warningNoticeBO = (WarningNoticeBO) context.getFlowScope().get(WARNING_NOTICE);

					warningNoticeBO.setCurrentUsername(document.getAuthorizeUsername());// getRomsLoggedInUser().getUsername());
					warningNoticeBO.setNewStatusId(Constants.DocumentStatus.AUTHORISED);
					// warningNoticeBO.setSupervisorAuthorisationRequested(true);
					// warningNoticeBO.setAuthorizedFlag(Constants.YesNo.YES_LABEL_STR + "");

					// update comment if not blank
					if (StringUtils.isNotBlank(warningNoticeBO.getNewComment())) {
						warningNoticeBO.setComment(formatOverrideDocumentComment(warningNoticeBO.getNewComment(), warningNoticeBO.getComment(), document.getAuthorizeUsername()));
					}

					warningNoticeBO = getDocumentsManagerService().updateWarningNotice(warningNoticeBO);

					// update the details on screen
					// context.getFlowScope().put(WARNING_NOTICE, warningNoticeBO);

				}

				if (document.getDocumentTypeCode().equalsIgnoreCase(Constants.DocumentType.WARNING_NOTICE_NO_PROSECUTION)) {
					WarningNoProsecutionBO warningNoProsecutionBO = (WarningNoProsecutionBO) context.getFlowScope().get(WARNING_NOTICE_NO_PROSECUTION);

					warningNoProsecutionBO.setCurrentUsername(document.getAuthorizeUsername());// getRomsLoggedInUser().getUsername());
					warningNoProsecutionBO.setNewStatusId(Constants.DocumentStatus.AUTHORISED);
					// warningNoProsecutionBO.setSupervisorAuthorisationRequested(true);
					// warningNoProsecutionBO.setAuthorizedFlag(Constants.YesNo.YES_LABEL_STR + "");

					// update comment if not blank
					if (StringUtils.isNotBlank(warningNoProsecutionBO.getNewComment())) {
						warningNoProsecutionBO.setComment(formatOverrideDocumentComment(warningNoProsecutionBO.getNewComment(), warningNoProsecutionBO.getComment(), document.getAuthorizeUsername()));
					}

					warningNoProsecutionBO = getDocumentsManagerService().updateWarningNoProsecution(warningNoProsecutionBO);

					// update the details on screen

				}
				// addInfoMessageText(context, "Document changes now verified.");
				// reset the document details in view
				this.loadDocumentDetails(context);
				addInfoMessageText(context, document.getDocumentTypeName() + " verified successfully.");
				context.getFlowScope().put(MODE, "view");
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			addErrorMessageText(context, "There was an error verifying document.");
			return error();
		}

		return success();
	}

	/**
	 * 
	 * @param context
	 * @return
	 */
	public Event denyUserEditVerification(RequestContext context) {
		DocumentViewBO document = (DocumentViewBO) context.getFlowScope().get(DOCUMENT_IN_VIEW);

		if (document == null) {
			addErrorMessage(context, "NoDetailsToDisplay");
			return error();
		}

		try {
			// validate then save
			if (StringUtils.isNotBlank(document.getDocumentTypeCode())) {

				if (document.getDocumentTypeCode().equalsIgnoreCase(Constants.DocumentType.SUMMONS)) {
					SummonsBO summonsBO = (SummonsBO) context.getFlowScope().get(SUMMONS);
					summonsBO.setCurrentUsername(document.getAuthorizeUsername());// getRomsLoggedInUser().getUsername());
					summonsBO.setNewStatusId(Constants.DocumentStatus.DENY_AUTHORISATION);
					// summonsBO.setAuthorizedFlag(Constants.YesNo.YES_LABEL + "");
					summonsBO.setSupervisorAuthorisationRequested(false);

					// update comment if not blank
					if (StringUtils.isNotBlank(summonsBO.getNewComment())) {
						summonsBO.setComment(formatDenyOverrideDocumentComment(summonsBO.getNewComment(), summonsBO.getComment(), document.getAuthorizeUsername()));
					} else {
						addErrorMessageWithParameter(context, "RequiredFields", "Comment");
						return error();
					}
					summonsBO = getDocumentsManagerService().updateSummons(summonsBO);

					// update the details on screen
					// context.getFlowScope().put(SUMMONS, summonsBO);

				}

				if (document.getDocumentTypeCode().equalsIgnoreCase(Constants.DocumentType.WARNING_NOTICE)) {
					WarningNoticeBO warningNoticeBO = (WarningNoticeBO) context.getFlowScope().get(WARNING_NOTICE);

					warningNoticeBO.setCurrentUsername(document.getAuthorizeUsername());// getRomsLoggedInUser().getUsername());
					warningNoticeBO.setNewStatusId(Constants.DocumentStatus.DENY_AUTHORISATION);
					warningNoticeBO.setSupervisorAuthorisationRequested(false);
					// warningNoticeBO.setAuthorizedFlag(Constants.YesNo.YES_LABEL + "");

					// update comment if not blank
					if (StringUtils.isNotBlank(warningNoticeBO.getNewComment())) {
						warningNoticeBO.setComment(formatDenyOverrideDocumentComment(warningNoticeBO.getNewComment(), warningNoticeBO.getComment(), document.getAuthorizeUsername()));
					} else {
						addErrorMessageWithParameter(context, "RequiredFields", "Comment");
						return error();
					}

					warningNoticeBO = getDocumentsManagerService().updateWarningNotice(warningNoticeBO);

					// update the details on screen
					// context.getFlowScope().put(WARNING_NOTICE, warningNoticeBO);

				}

				if (document.getDocumentTypeCode().equalsIgnoreCase(Constants.DocumentType.WARNING_NOTICE_NO_PROSECUTION)) {
					WarningNoProsecutionBO warningNoProsecutionBO = (WarningNoProsecutionBO) context.getFlowScope().get(WARNING_NOTICE_NO_PROSECUTION);

					warningNoProsecutionBO.setCurrentUsername(document.getAuthorizeUsername());// getRomsLoggedInUser().getUsername());
					warningNoProsecutionBO.setNewStatusId(Constants.DocumentStatus.DENY_AUTHORISATION);
					warningNoProsecutionBO.setSupervisorAuthorisationRequested(false);
					// warningNoProsecutionBO.setAuthorizedFlag(Constants.YesNo.YES_LABEL + "");

					// update comment if not blank
					if (StringUtils.isNotBlank(warningNoProsecutionBO.getNewComment())) {
						warningNoProsecutionBO.setComment(formatDenyOverrideDocumentComment(warningNoProsecutionBO.getNewComment(), warningNoProsecutionBO.getComment(), document.getAuthorizeUsername()));
					} else {
						addErrorMessageWithParameter(context, "RequiredFields", "Comment");
						return error();
					}

					warningNoProsecutionBO = getDocumentsManagerService().updateWarningNoProsecution(warningNoProsecutionBO);

					// update the details on screen
					// context.getFlowScope().put(WARNING_NOTICE_NO_PROSECUTION, warningNoProsecutionBO);

				}

				// reset the document details in view
				this.loadDocumentDetails(context);
				addInfoMessageText(context, document.getDocumentTypeName() + " changes not accepted.");
			}

		} catch (Exception e) {

			e.printStackTrace();
			addErrorMessageText(context, "There was an error verifying document.");
			return error();
		}
		context.getFlowScope().put(MODE, "view");
		return success();
	}

	/**
	 * 
	 * @param context
	 * @return
	 */
	public Event denyDocumentDetailsVerification(RequestContext context) {
		DocumentViewBO document = (DocumentViewBO) context.getFlowScope().get(DOCUMENT_IN_VIEW);

		if (document == null) {
			addErrorMessage(context, "NoDetailsToDisplay");
			return error();
		}

		try {
			// validate then save

			if (StringUtils.isNotBlank(document.getDocumentTypeCode())) {

				if (document.getDocumentTypeCode().equalsIgnoreCase(Constants.DocumentType.SUMMONS)) {
					SummonsBO summonsBO = (SummonsBO) context.getFlowScope().get(SUMMONS);
					summonsBO.setCurrentUsername(document.getAuthorizeUsername());// getRomsLoggedInUser().getUsername());
					summonsBO.setNewStatusId(Constants.DocumentStatus.DENY_AUTHORISATION);
					// summonsBO.setAuthorizedFlag(Constants.YesNo.YES_LABEL + "");
					// summonsBO.setAuthorizeUsername(value);
					summonsBO.setSupervisorAuthorisationRequested(false);

					// update comment if not blank
					if (StringUtils.isNotBlank(summonsBO.getNewComment())) {
						summonsBO.setComment(formatDenyOverrideDocumentComment(summonsBO.getNewComment(), summonsBO.getComment(), document.getAuthorizeUsername()));
					} else {
						addErrorMessageWithParameter(context, "RequiredFields", "Comment");
						return error();
					}
					summonsBO = getDocumentsManagerService().updateSummons(summonsBO);

					// update the details on screen
					// context.getFlowScope().put(SUMMONS, summonsBO);

				}

				if (document.getDocumentTypeCode().equalsIgnoreCase(Constants.DocumentType.WARNING_NOTICE)) {
					WarningNoticeBO warningNoticeBO = (WarningNoticeBO) context.getFlowScope().get(WARNING_NOTICE);

					warningNoticeBO.setCurrentUsername(document.getAuthorizeUsername());// getRomsLoggedInUser().getUsername());
					warningNoticeBO.setNewStatusId(Constants.DocumentStatus.DENY_AUTHORISATION);
					warningNoticeBO.setSupervisorAuthorisationRequested(false);
					// warningNoticeBO.setAuthorizedFlag(Constants.YesNo.YES_LABEL + "");

					// update comment if not blank
					if (StringUtils.isNotBlank(warningNoticeBO.getNewComment())) {
						warningNoticeBO.setComment(formatDenyOverrideDocumentComment(warningNoticeBO.getNewComment(), warningNoticeBO.getComment(), document.getAuthorizeUsername()));
					} else {
						addErrorMessageText(context, "Comment is required when rejecting edits.");
						return error();
					}

					warningNoticeBO = getDocumentsManagerService().updateWarningNotice(warningNoticeBO);

					// update the details on screen
					// context.getFlowScope().put(WARNING_NOTICE, warningNoticeBO);

				}

				if (document.getDocumentTypeCode().equalsIgnoreCase(Constants.DocumentType.WARNING_NOTICE_NO_PROSECUTION)) {
					WarningNoProsecutionBO warningNoProsecutionBO = (WarningNoProsecutionBO) context.getFlowScope().get(WARNING_NOTICE_NO_PROSECUTION);

					warningNoProsecutionBO.setCurrentUsername(document.getAuthorizeUsername());// getRomsLoggedInUser().getUsername());
					warningNoProsecutionBO.setNewStatusId(Constants.DocumentStatus.DENY_AUTHORISATION);
					warningNoProsecutionBO.setSupervisorAuthorisationRequested(false);
					// warningNoProsecutionBO.setAuthorizedFlag(Constants.YesNo.YES_LABEL + "");

					// update comment if not blank
					if (StringUtils.isNotBlank(warningNoProsecutionBO.getNewComment())) {
						warningNoProsecutionBO.setComment(formatDenyOverrideDocumentComment(warningNoProsecutionBO.getNewComment(), warningNoProsecutionBO.getComment(), document.getAuthorizeUsername()));
					} else {
						addErrorMessageWithParameter(context, "RequiredFields", "Comment");
						return error();
					}

					warningNoProsecutionBO = getDocumentsManagerService().updateWarningNoProsecution(warningNoProsecutionBO);

					// update the details on screen
					// context.getFlowScope().put(WARNING_NOTICE_NO_PROSECUTION, warningNoProsecutionBO);

				}

				// reset the document details in view
				this.loadDocumentDetails(context);
				addInfoMessageText(context, document.getDocumentTypeName() + " changes not accepted.");
			}

		} catch (Exception e) {

			e.printStackTrace();
			addErrorMessageText(context, "There was an error verifying document.");
			return error();
		}
		context.getFlowScope().put(MODE, "view");
		return success();
	}

	/**
	 * 
	 * @param context
	 * @return
	 */
	public Event verifyDenyUserCredentials(RequestContext context) {
		DocumentViewBO document = (DocumentViewBO) context.getFlowScope().get(DOCUMENT_IN_VIEW);
		if (document == null) {
			addErrorMessage(context, "NoDetailsToDisplay");
			return error();
		}

		// get verify view
		VerifyDetailsSecurityView verifyView = (VerifyDetailsSecurityView) context.getFlowScope().get("verifyView");

		if (verifyView == null) {
			addErrorMessage(context, "EnterVerificationDetails");
			return error();
		}

		// set the actual permisson being Tested for
		verifyView.setLastUpdatedUser(document.getLastUpdateUser());
		if (document.getDocumentTypeCode().equalsIgnoreCase(Constants.DocumentType.SUMMONS)) {
			SummonsBO summonsBO = (SummonsBO) context.getFlowScope().get(SUMMONS);
			verifyView.setComment(summonsBO.getNewComment());
		}
		if (document.getDocumentTypeCode().equalsIgnoreCase(Constants.DocumentType.WARNING_NOTICE)) {
			WarningNoticeBO warningNoticeBO = (WarningNoticeBO) context.getFlowScope().get(WARNING_NOTICE);
			verifyView.setComment(warningNoticeBO.getNewComment());
		}
		if (document.getDocumentTypeCode().equalsIgnoreCase(Constants.DocumentType.WARNING_NOTICE_NO_PROSECUTION)) {
			WarningNoProsecutionBO warningNoProsecutionBO = (WarningNoProsecutionBO) context.getFlowScope().get(WARNING_NOTICE_NO_PROSECUTION);
			verifyView.setComment(warningNoProsecutionBO.getNewComment());
		}
		try {
			// validate then save
			if (validateOverrideDetails(context, verifyView)) {

				// after we have validated user details set it so it can be used
				document.setAuthorizeUsername(verifyView.getUserName());
				context.getFlowScope().put(DOCUMENT_IN_VIEW, document);

				return denyUserEditVerification(context);

			} else {
				// System.err.println(" error occurs on verify");
				return error();
			}
		} catch (Exception e) {

			e.printStackTrace();

			addErrorMessageText(context, e.getMessage());
			return error();
		}

	}

	/**
	 * 
	 * @param context
	 * @return
	 */
	public Event uploadDocument2(RequestContext context) {
		DocumentViewBO document = (DocumentViewBO) context.getFlowScope().get(DOCUMENT_IN_VIEW);

		if (document == null) {
			addErrorMessage(context, "NoDetailsToDisplay");
			return error();
		}

		try {

			// get the list cuurently storing files form the flowscope
			List<ScannedDocBO> listOfScannedDocs = (List<ScannedDocBO>) context.getFlowScope().get(FileUpload.FILE_SCANNED_DOC_LIST);

			if (listOfScannedDocs == null || listOfScannedDocs.isEmpty()) {
				addErrorMessageText(context, "No file was uploaded");
				return error();
			}

			// If files exist then upload them
			if (StringUtils.isNotBlank(document.getDocumentTypeCode())) {
				for (ScannedDocBO fileToUpload : listOfScannedDocs) {
					ScannedDocBO scannedDoc = new ScannedDocBO();
					scannedDoc.setCurrentUsername(getRomsLoggedInUser().getUsername());
					scannedDoc.setRoadCheckOffenceOutcomeId(document.getRoadCheckOffenceOutcomeCode());
					scannedDoc.setRoadCheckOffence(document.getDocumentTypeCode());
					scannedDoc.setFileContents(fileToUpload.getFileContents());
					scannedDoc.setMimeType(fileToUpload.getMimeType());
					scannedDoc.setDocType(fileToUpload.getDocType());

					
					DocumentViewBO docview = new DocumentViewBO();
					copyFields(docview,document);
					
					// do the actual upload of file
					getScannedDocUploadService().uploadFile(scannedDoc, docview);
					// System.err.println(" after save scanned doc ");
					// reset the document details in view
					this.loadDocumentDetails(context);
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			addErrorMessageText(context, "There was an error uploading document.");
			return error();
		}
		addInfoMessageText(context, "Document uploaded successfully.");
		return success();
	}

	/**
	 * 
	 * @param context
	 * @return
	 */
	public Event uploadDocument(RequestContext context) {

		try {
			DocumentViewBO document = (DocumentViewBO) context.getFlowScope().get(DOCUMENT_IN_VIEW);

			if (document == null) {
				addErrorMessage(context, "NoDetailsToDisplay");
				return error();
			}

			// get the list currently storing files form the flowscope
			ScannedDocBO scannedDocToUpload = (ScannedDocBO) context.getFlowScope().get("scannedDocToUpload");

			if (scannedDocToUpload == null) {
				addErrorMessage(context, "ScannedDocEmpty");
				return error();
			}

			// document uploaded now
			if (scannedDocToUpload != null) {

				if (validateForUpload(context)) {

					ScannedDocBO scannedDoc = new ScannedDocBO();
					scannedDoc.setCurrentUsername(getRomsLoggedInUser().getUsername());
					scannedDoc.setRoadCheckOffenceOutcomeId(document.getRoadCheckOffenceOutcomeCode());
					// scannedDoc.setRoadCheckOffence(document.getDocumentTypeCode());
					scannedDoc.setFileContents(scannedDocToUpload.getFileContents());
					scannedDoc.setDescription(scannedDocToUpload.getDescription());
					scannedDoc.setMimeType(scannedDocToUpload.getMimeType());
					scannedDoc.setDocType(scannedDocToUpload.getDocType());

					DocumentViewBO docview = new DocumentViewBO();
					
					
					
					docview = copyDocumentViewBO(docview,document);
					/*//if(!document.getListOfOffences().get(0).getClass().equals(OffenceBO.class)){
						for(Object documentCopy : document.getListOfOffences()){
							OffenceBO offenceToCopy = new OffenceBO();
							
							copyFields(offenceToCopy, documentCopy);
							
							listConvertedOffences.add(offenceToCopy);
						}
					//}
					
					docview.getListOfOffences().clear();
					docview.getListOfOffences().addAll(listConvertedOffences);*/
					
					// do the actual upload of file
					try {
						getScannedDocUploadService().uploadFile(scannedDoc,docview);
						// empty variables
						context.getFlowScope().put("scannedDocToUpload", new ScannedDocBO());
						/* context.getFlowScope().put("currentUploadedFile", null); */
						addInfoMessageText(context, "Document uploaded successfully.");

						// load
						this.loadDocumentDetails(context);
						context.getFlowScope().put(MODE, "view");

					} catch (ErrorSavingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();

						addErrorMessageText(context, "There was a problem uploading document.");
						return error();

					} catch (RequiredFieldMissingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						addErrorMessageText(context, "All Fields are required.");
						return error();
					}
				} else {
					return error();
				}
			}
		} catch (Exception e) {

			e.printStackTrace();
			addErrorMessageText(context, "There was an error uploading document.");
			return error();
		}

		return success();
	}

	/**
	 * 
	 * @param context
	 * @return
	 */
	public Event printDocument(RequestContext context) {

		DocumentViewBO document = (DocumentViewBO) context.getFlowScope().get(DOCUMENT_IN_VIEW);

		if (document == null) {
			addErrorMessage(context, "NoDetailsToDisplay");
			return error();
		}

		try {

			if (StringUtils.isNotBlank(document.getDocumentTypeCode())) {

				if (document.getDocumentTypeCode().equalsIgnoreCase(Constants.DocumentType.SUMMONS)) {
					SummonsBO summonsBO = (SummonsBO) context.getFlowScope().get(SUMMONS);
					summonsBO.setNewStatusId(Constants.DocumentStatus.PRINTED);
					summonsBO.setCurrentUsername(getRomsLoggedInUser().getUsername());

					// do the update
					summonsBO = getDocumentsManagerService().updateSummons(summonsBO);
				}

				if (document.getDocumentTypeCode().equalsIgnoreCase(Constants.DocumentType.WARNING_NOTICE)) {
					WarningNoticeBO warningNotice = (WarningNoticeBO) context.getFlowScope().get(WARNING_NOTICE);
					warningNotice.setNewStatusId(Constants.DocumentStatus.PRINTED);
					warningNotice.setCurrentUsername(getRomsLoggedInUser().getUsername());

					// do the update
					warningNotice = getDocumentsManagerService().updateWarningNotice(warningNotice);
				}

				if (document.getDocumentTypeCode().equalsIgnoreCase(Constants.DocumentType.WARNING_NOTICE_NO_PROSECUTION)) {
					WarningNoProsecutionBO warningNoProsecution = (WarningNoProsecutionBO) context.getFlowScope().get(WARNING_NOTICE_NO_PROSECUTION);
					warningNoProsecution.setNewStatusId(Constants.DocumentStatus.PRINTED);
					warningNoProsecution.setCurrentUsername(getRomsLoggedInUser().getUsername());

					// do the update
					warningNoProsecution = getDocumentsManagerService().updateWarningNoProsecution(warningNoProsecution);
				}

				// reset the document details in view
				this.loadDocumentDetails(context);

				// set the current users details for servlet
				HttpServletRequest req = (HttpServletRequest) context.getExternalContext().getNativeRequest();
				HttpSession session = req.getSession();
				session.setAttribute(GenerateDocServlet.ROMS_LOGGED_IN_USER, getRomsLoggedInUser().getUsername());

				context.getFlowScope().put("printDocType", document.getDocumentTypeCode());
				context.getFlowScope().put("documentId", document.getAutomaticSerialNo());
				context.getFlowScope().put("viewScannedDoc", false);
				context.getFlowScope().put("mode", "view");
				context.getFlowScope().put("openReportWindow", true);
				addInfoMessageText(context, "Document printed successfully.");
			}
		} catch (Exception e) {
			addErrorMessageText(context, "There was an error while generating file.");
			e.printStackTrace();
			return error();
		}

		return success();
	}

	/**
	 * 
	 * @param context
	 * @return
	 */
	public Event rePrintDocument(RequestContext context) {

		DocumentViewBO document = (DocumentViewBO) context.getFlowScope().get(DOCUMENT_IN_VIEW);

		if (document == null) {
			addErrorMessage(context, "NoDetailsToDisplay");
			return error();
		}

		try {
			if (validateForReprint(context)) {

				if (StringUtils.isNotBlank(document.getDocumentTypeCode())) {

					if (document.getDocumentTypeCode().equalsIgnoreCase(Constants.DocumentType.SUMMONS)) {
						SummonsBO summonsBO = (SummonsBO) context.getFlowScope().get(SUMMONS);
						summonsBO.setNewStatusId(Constants.DocumentStatus.REPRINTED);
						summonsBO.setCurrentUsername(getRomsLoggedInUser().getUsername());
						summonsBO.setReprintComment(summonsBO.getNewComment());
						summonsBO.setComment(formatDocumentComment(summonsBO.getNewComment(), summonsBO.getComment()));

						// do the update
						summonsBO = getDocumentsManagerService().updateSummons(summonsBO);
					}

					if (document.getDocumentTypeCode().equalsIgnoreCase(Constants.DocumentType.WARNING_NOTICE)) {
						WarningNoticeBO warningNotice = (WarningNoticeBO) context.getFlowScope().get(WARNING_NOTICE);
						warningNotice.setNewStatusId(Constants.DocumentStatus.REPRINTED);
						warningNotice.setCurrentUsername(getRomsLoggedInUser().getUsername());
						warningNotice.setReprintComment(warningNotice.getNewComment());
						warningNotice.setComment(formatDocumentComment(warningNotice.getNewComment(), warningNotice.getComment()));
						
						// do the update
						warningNotice = getDocumentsManagerService().updateWarningNotice(warningNotice);
					}

					if (document.getDocumentTypeCode().equalsIgnoreCase(Constants.DocumentType.WARNING_NOTICE_NO_PROSECUTION)) {
						WarningNoProsecutionBO warningNoProsecution = (WarningNoProsecutionBO) context.getFlowScope().get(WARNING_NOTICE_NO_PROSECUTION);
						warningNoProsecution.setNewStatusId(Constants.DocumentStatus.REPRINTED);
						warningNoProsecution.setCurrentUsername(getRomsLoggedInUser().getUsername());
						warningNoProsecution.setReprintComment(warningNoProsecution.getNewComment());
						warningNoProsecution.setComment(formatDocumentComment(warningNoProsecution.getNewComment(), warningNoProsecution.getComment()));
			
						// do the update
						warningNoProsecution = getDocumentsManagerService().updateWarningNoProsecution(warningNoProsecution);
					}

					// reset the document details in view
					this.loadDocumentDetails(context);

					// set the current users details for servlet
					HttpServletRequest req = (HttpServletRequest) context.getExternalContext().getNativeRequest();
					HttpSession session = req.getSession();
					session.setAttribute(GenerateDocServlet.ROMS_LOGGED_IN_USER, getRomsLoggedInUser().getUsername());

					context.getFlowScope().put("printDocType", document.getDocumentTypeCode());
					context.getFlowScope().put("documentId", document.getAutomaticSerialNo());
					context.getFlowScope().put("viewScannedDoc", false);
					context.getFlowScope().put("openReportWindow", true);
					context.getFlowScope().put("mode", "view");
					addInfoMessageText(context, "Document printed successfully.");
				}
			} else {
				return error();
			}
		} catch (Exception e) {
			addErrorMessageText(context, "There was an error while generating file.");
			e.printStackTrace();
			return error();
		}

		return success();
	}

	/**
	 * This generates the Discharge form
	 * 
	 * @param context
	 * @return
	 */
	public Event printDischargeForm(RequestContext context) {
		DocumentViewBO document = (DocumentViewBO) context.getFlowScope().get(DOCUMENT_IN_VIEW);

		if (document == null) {
			addErrorMessage(context, "NoDetailsToDisplay");
			return error();
		}

		context.getFlowScope().put("viewScannedDoc", false);
		context.getFlowScope().put("openReportWindow", true);
		context.getFlowScope().put("printDocType", DocumentType.DISCHARGE_FORM);
		context.getFlowScope().put("documentId", document.getAutomaticSerialNo());

		HttpServletRequest req = (HttpServletRequest) context.getExternalContext().getNativeRequest();
		HttpSession session = req.getSession();
		session.setAttribute(GenerateDocServlet.ROMS_LOGGED_IN_USER, getRomsLoggedInUser().getUsername());

		addInfoMessageText(context, "Discharge Form generated successfully.");

		// reset the document details in view
		// this.loadDocumentDetails(context);

		return success();

	}

	/**
	 * 
	 * @param context
	 * @return
	 */
	public Event printScannedDocument(RequestContext context) {
		ScannedDocBO document = (ScannedDocBO) context.getFlowScope().get("scannedDocToView");

		
		// set the current users details
		HttpServletRequest req = (HttpServletRequest) context.getExternalContext().getNativeRequest();
		HttpSession session = req.getSession();

		// update the file and print
		ScannedDocBO scannedDoc = new ScannedDocBO();
		scannedDoc.setScannedDocId(document.getScannedDocId());
		scannedDoc.setCurrentUsername(getRomsLoggedInUser().getUsername());

		document.setCurrentUsername(getRomsLoggedInUser().getUsername());
		
		if (document.getReprintReasonCode() == null) {
			addErrorMessageWithParameter(context, "RequiredFields", "Reprint Reason");
			return error();
		}
		else
		{
			scannedDoc.setReprintReasonCode(document.getReprintReasonCode());
		}
		if (StringUtils.isBlank(document.getNewReprintComment())) {
			addErrorMessageWithParameter(context, "RequiredFields", "Comment");
			return error();
		} else {
			scannedDoc.setReprintComment(document.getNewReprintComment());
		}
		// get the actual file contents
		try {
			
			DocumentViewBO parentDoc = (DocumentViewBO) context.getFlowScope().get(DOCUMENT_IN_VIEW);
			
			DocumentViewBO docview = new DocumentViewBO();
			//copyFields(docview,parentDoc);
			docview = this.copyDocumentViewBO(docview, parentDoc);
			scannedDoc = getScannedDocUploadService().getFileContents(scannedDoc,docview);

			// catch error with file retrieval
			if (null == scannedDoc.getFileContents()) {

				addErrorMessageText(context, "File could not be retrieved from location.");
				return error();
			}
			
			

		} catch (ErrorSavingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		session.setAttribute(ViewScannedDocServlet.SCANNED_DOC, scannedDoc);

		// reload the data to the screen
		loadDocumentDetails(context);

		// reset the variables
		context.getFlowScope().put("viewScannedDoc", true);
		context.getFlowScope().put("openReportWindow", false);
		context.getFlowScope().put("scannedDocId", document.getScannedDocId());
		context.getFlowScope().put("mode", "view");

		addInfoMessageText(context, "Document generated successfully.");
		return success();
	}

	/**
	 * 
	 * @param context
	 * @return
	 */
	public Event deleteScannedDocument(RequestContext context) {
		ScannedDocBO document = (ScannedDocBO) context.getFlowScope().get("scannedDocToDelete");

		if (document == null) {
			addErrorMessageText(context, "Documents selected is empty.");
			return error();
		}

		try {
			ScannedDocBO scannedDocBO = new ScannedDocBO();
			scannedDocBO.setScannedDocId(document.getScannedDocId());
			scannedDocBO.setCurrentUsername(getRomsLoggedInUser().getUsername());
			scannedDocBO.setDocType(document.getDocType());
			scannedDocBO.setDescription(document.getDescription());

			
			DocumentViewBO parentDoc = (DocumentViewBO) context.getFlowScope().get(DOCUMENT_IN_VIEW);
			
			DocumentViewBO docview = new DocumentViewBO();
			copyFields(docview,parentDoc);
			
						
			getScannedDocUploadService().deleteScannedDoc(scannedDocBO,docview);

			addInfoMessageText(context, "Document has been deleted.");

		} catch (ErrorSavingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			addErrorMessageText(context, "There was an error deleting record.");
			return error();
		} catch (RequiredFieldMissingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			addErrorMessageText(context, "There was an error deleting record.");
			return error();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// reload the data to the screen
		loadDocumentDetails(context);

		return success();

	}

	public Event printSelectedScannedDocuments(RequestContext context) {

		ArrayList<ScannedDocBO> selectedDocuments = (ArrayList<ScannedDocBO>) context.getFlowScope().get("scannedDocumentsSelected");
		HttpServletRequest req = (HttpServletRequest) context.getExternalContext().getNativeRequest();
		HttpSession session = req.getSession();

		try {
			if (selectedDocuments == null || selectedDocuments.isEmpty()) {
				context.getFlowScope().put("viewScannedDoc", false);
				context.getFlowScope().put("openReportWindow", false);
				context.getFlowScope().put("printDocumentFromList", false);
				addErrorMessage(context, "DocListEmpty");
				return error();
			} else {

				// set the current users details
				session.setAttribute(GenerateDocServlet.ROMS_LOGGED_IN_USER, getRomsLoggedInUser().getUsername());

				session.setAttribute(Constants.IS_MOBILE, this.isHandHeld(context));

				ArrayList<ScannedDocBO> tempDocuments = new ArrayList<ScannedDocBO>();

				DocumentViewBO parentDoc = (DocumentViewBO) context.getFlowScope().get(DOCUMENT_IN_VIEW);
				
				for (ScannedDocBO scannedDoc : selectedDocuments) {
					ScannedDocBO scannedD = new ScannedDocBO();
					scannedD.setScannedDocId(scannedDoc.getScannedDocId());
					scannedD.setCurrentUsername(getRomsLoggedInUser().getUsername());

					DocumentViewBO docview = new DocumentViewBO();
					copyFields(docview,parentDoc);
					
					
					scannedD = getScannedDocUploadService().getFileContents(scannedD,docview);
					tempDocuments.add(scannedD);
					// catch error with file retrieval
					if (null == scannedDoc.getFileContents()) {

						addErrorMessageText(context, "File could not be retrieved from location.");
						return error();
					}
				}
				session.setAttribute(Constants.DocumentPrinting.DOCUMENT_LIST, tempDocuments);

				// update the documents to printed
				// getDocumentsManagerPortProxy().prin(selectedDocuments, null, null,
				// getRomsLoggedInUser().getUsername());

				// re-search
				search(context);
				context.getMessageContext().clearMessages();
				addInfoMessageText(context, "Documents generated successfully.");
				// reset the variables
				context.getFlowScope().put("viewScannedDoc", true);
				context.getFlowScope().put("openReportWindow", false);
				context.getFlowScope().put("scannedDocumentsSelected", new ArrayList<ScannedDocBO>());
			}
		} catch (Exception e) {
			e.printStackTrace();
			addErrorMessageText(context, e.getMessage());
			return error();
		}
		// reload the data to the screen
		loadDocumentDetails(context);
		return success();
	}

	/**
	 * 
	 * @param context
	 * @return
	 */
	public Event viewCourtAppearance(RequestContext context) {
		DocumentViewBO document = (DocumentViewBO) context.getFlowScope().get(DOCUMENT_IN_VIEW);
		ArrayList<DocumentViewBO> selectedDocuments = (ArrayList<DocumentViewBO>) context.getFlowScope().get("documentsSelected");

		ArrayList<DocumentViewBO> documentstoPrint = new ArrayList<DocumentViewBO>();

		if (selectedDocuments == null) {
			addErrorMessageText(context, "Documents selected is empty.");
			return error();
		}

		try {

			if (StringUtils.isNotBlank(document.getDocumentTypeCode())) {

				if (document.getDocumentTypeCode().equalsIgnoreCase(Constants.DocumentType.SUMMONS)) {
					SummonsBO summonsBO = (SummonsBO) context.getFlowScope().get(SUMMONS);

					// validate then save

				}
			}
		} catch (Exception e) {
			addErrorMessageText(context, e.getMessage());
			e.printStackTrace();
			return error();
		}

		return success();

	}

	/**
	 * 
	 * @param context
	 * @return
	 */
	public Event cancelAction(RequestContext context) {
		// System.err.println("Method called cancel");
		context.getFlowScope().put("viewScannedDoc", false);
		context.getFlowScope().put("openReportWindow", false);
		context.getFlowScope().put("scannedDocToUpload", new ScannedDocBO());
		/* context.getFlowScope().put("currentUploadedFile", null); */
		context.getFlowScope().put(MODE, "view");
		System.err.println(" now loading from cancel action");
		this.loadDocumentDetails(context);

		return success();

	}

	public Event backAction(RequestContext context) {
		// System.err.println("Method called cancel");
		DocumentsCriteriaBO criteria = (DocumentsCriteriaBO) context.getFlowScope().get(CRITERIA);
		
		StaffUserMappingBO staffBO = new StaffUserMappingBO();
		List<StaffUserMappingBO> listOfStaffBOs = new ArrayList<StaffUserMappingBO>();
		StaffUserMappingCriteriaBO staffCriteriaBO = new StaffUserMappingCriteriaBO();
		if(!StringUtils.isBlank(criteria.getStaffId())){
			
		
			staffCriteriaBO.setStaffId(criteria.getStaffId());
			
			try {
				listOfStaffBOs = getStaffUserMappingService().lookupStaffUserMappings(staffCriteriaBO);
				if(listOfStaffBOs != null && listOfStaffBOs.size() > 0){
					staffBO = listOfStaffBOs.get(0);
				}
			} catch (NoRecordFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			context.getFlowScope().put("staffBOForSearch",staffBO);
		}
		context.getFlowScope().put("viewScannedDoc", false);
		context.getFlowScope().put("openReportWindow", false);
		context.getFlowScope().put("scannedDocToUpload", new ScannedDocBO());
		/* context.getFlowScope().put("currentUploadedFile", null); */
		context.getFlowScope().put(MODE, "view");
		System.err.println(" now loading from cancel action");
		context.getMessageContext().clearMessages();
		return success();

	}

	/********************************* GENERATE REPORT *******************************/
	/* public Event generate(RequestContext context){ try{ context.getFlowScope().put("openReportWindow", "false");
	 * getReportInPdf(context); return success(); } catch(Exception e){ context.getFlowScope().put("openReportWindow",
	 * "false"); logger.error(e.toString()); addErrorMessage(context, "UndeterminedError"); return error(); } } private
	 * void getReportInPdf(RequestContext context) { Map<String, Object> parameters = getReportParameters(context);
	 * String fileName ="/documentTemplates/Form1.jasper"; try { HttpServletRequest req = ((ServletRequestAttributes)
	 * RequestContextHolder .getRequestAttributes()).getRequest(); HttpSession session = req.getSession(); // Fill the
	 * report using an empty data source JasperPrint print = JasperFillManager.fillReport(fileName, parameters, new
	 * JRBeanCollectionDataSource(null)); session.setAttribute(GenerateDocServlet.SAVE_VIEW_DIALOG_REQUEST_PARAMETER,
	 * true); session.setAttribute( GenerateDocServlet.PDF_FILE_NAME_SESSION_ATTRIBUTE, fileName );
	 * session.setAttribute( GenerateDocServlet.JASPER_PRINT_LIST_REQUEST_PARAMETER, fileName );
	 * context.getFlowScope().put("openReportWindow", "false"); } catch (Exception exception) {
	 * exception.printStackTrace(); } } private Map<String, Object> getReportParameters(RequestContext context) {
	 * Map<String, Object> parameters = new HashMap<String, Object>(); parameters.put("location", "x");
	 * parameters.put("printDate", DateUtils.formatDate("yyyy-MM-dd hh:mm a", new Date())); parameters.put("userName",
	 * getRomsLoggedInUser().getUsername()); return parameters; } */

	/**
	 * If the serial number is all numbers, it is automatic
	 * 
	 * @return
	 */
	public boolean isSerialNoAutomatic(String serialNo) {
		boolean pass = true;

		if (!StringUtils.isEmpty(serialNo)) {
			try {

				Integer num = Integer.valueOf(serialNo);

				if (num == null)
					pass = false;

			} catch (Exception e) {
				pass = false;
				e.printStackTrace();
			}

		} else {
			pass = false;
		}

		return pass;
	}

	/***************************************** VALIDATE **************************************/
	public boolean validateForSearch(RequestContext context) {

		boolean pass = true;

		DocumentsCriteriaBO criteria = (DocumentsCriteriaBO) context.getFlowScope().get(CRITERIA);

		if (criteria == null) {
			addErrorMessageText(context, "Criteria is empty.");
			pass = false;
			return pass;
		}

		if (isCriteriaEmpty(criteria)) {
			addErrorMessageText(context, "Please enter at least one of the following: Reference No, Manual Reference No, Offence Date, Operation Name or Operation Date");
			
			//addErrorMessageText(context, "Please enter at least one ");
			
			pass = false;
			return pass;
		}

		if (StringUtils.isNotBlank(criteria.getReferenceNo())) {
			criteria.setReferenceNo(criteria.getReferenceNo().trim());
			if (!NumberUtils.isNumber(criteria.getReferenceNo())) {
				addErrorMessageText(context, "Reference number should be numeric.");
				pass = false;
			}
		}
		if (StringUtils.isNotBlank(criteria.getReferenceNo()) || 
				StringUtils.isNotBlank(criteria.getManualSerialNo())) {
			if(criteria.getDocumentType().equalsIgnoreCase("all")){
				addErrorMessageWithParameter(context, "RequiredFields", "Document Type");
				pass = false;
			}
		}
		/* if (StringUtils.isNotBlank(criteria.getSerialNumberEntered())) { Integer autoSerial =
		 * Integer.valueOf(criteria.getSerialNumberEntered().trim()); if (autoSerial == null) {
		 * criteria.setManualSerialNo(criteria.getSerialNumberEntered().trim()); } else {
		 * criteria.setAutomaticSerialNo(autoSerial); } } */

		// test offence dates
		pass = pass && validateStartNEndDate(context, criteria.getOffenceStartDateRange(), 
				criteria.getOffenceEndDateRange(), "Offence", false);

		if (StringUtils.isNotBlank(criteria.getTrnNbr())) {
			criteria.setTrnNbr(criteria.getTrnNbr().trim());
		}

		if (StringUtils.isNotBlank(criteria.getOffenderFirstName())) {
			criteria.setOffenderFirstName(criteria.getOffenderFirstName().trim());
		}

		if (StringUtils.isNotBlank(criteria.getOffenderLastName())) {
			criteria.setOffenderLastName(criteria.getOffenderLastName().trim());
		}

		if (StringUtils.isNotBlank(criteria.getOffenderMiddleName())) {
			criteria.setOffenderMiddleName(criteria.getOffenderMiddleName().trim());
		}

		// test operation dates
		pass = validateStartNEndDate(context, criteria.getOperationStartDateRange(), 
				criteria.getOperationEndDateRange(), "Operation", false) && pass;

		return pass;
	}

	/**
	 * 
	 * @param context
	 * @return
	 */
	public boolean validateForEdit(RequestContext context) {
		boolean pass = true;

		DocumentViewBO document = (DocumentViewBO) context.getFlowScope().get(DOCUMENT_IN_VIEW);

		DocumentViewBO documentCopy = (DocumentViewBO) context.getFlowScope().get(DOCUMENT_IN_VIEW_COPY);
		String origin = null;

		ScannedDocBO scannedDocToUpload = (ScannedDocBO) context.getFlowScope().get("scannedDocToUpload");
		
		Boolean manualDocAttached = (Boolean) context.getFlowScope().get("manualScannedDocAttached");

		if (document == null || documentCopy == null) {
			pass = false;
			addErrorMessageText(context, "Select valid document for edit.");
			return pass;
		}

		if (documentCopy != null) {
			origin = documentCopy.getOrigin();
		}

		try {
			if (document.getDocumentTypeCode().equalsIgnoreCase(Constants.DocumentType.SUMMONS)) {

				SummonsBO summonsBO = (SummonsBO) context.getFlowScope().get(SUMMONS);
				System.err.println(" summons  origin " + summonsBO.getOrigin());
				
				// validate the origin
				if (StringUtils.isBlank(summonsBO.getOrigin())) {
					pass = false;
					addErrorMessageWithParameter(context, "RequiredFields", "Origin");
				} else {
					// if the document is manual then
					if (summonsBO.getOrigin().equalsIgnoreCase(DocumentType.ORIGIN_MANUAL)) {
						if ((summonsBO.getJpIdNumber() == documentCopy.getJpIdNumber()) 
								&& StringUtils.isBlank(summonsBO.getNewComment()) 
								&& (summonsBO.getManualSerialNo().equalsIgnoreCase(documentCopy.getManualSerialNo())) 
								//&& (summonsBO.getOrigin().equalsIgnoreCase(origin))
								&& (scannedDocToUpload != null && scannedDocToUpload.getFileContents() == null)) {
							pass = false;
							addErrorMessageText(context, "No changes were made to document.");
						}
						// validate the manual serial number
						if (StringUtils.isBlank(summonsBO.getManualSerialNo())) {
							pass = false;
							addErrorMessageWithParameter(context, "RequiredFields", "Manual Reference No.");
						}
						if (StringUtils.isNotBlank(summonsBO.getManualSerialNo())) {
							boolean alreadyExists;

							alreadyExists = getDocumentsManagerService().manualSerialExists(summonsBO.getManualSerialNo(), summonsBO.getAutomaticSerialNo(), document.getDocumentTypeCode());

							if (alreadyExists) {
								pass = false;
								addErrorMessageText(context, document.getDocumentTypeName() + " already exists with the Manual Reference No. " + summonsBO.getManualSerialNo().toUpperCase());
							}

							// check scanned doc
							/*if (StringUtils.isNotBlank(summonsBO.getManualSerialNo()) && (documentCopy.getManualSerialNo() == null || !(documentCopy.getManualSerialNo().equalsIgnoreCase(summonsBO.getManualSerialNo())))) {

								if (scannedDocToUpload == null || scannedDocToUpload.getFileContents() == null) {
									addErrorMessageWithParameter(context, "RequiredFields", "Attachment");
									pass = false;
								}
							}*/
						} /*else {// a manual serial number is required
							
						}*/
						if(manualDocAttached == false){
							if (scannedDocToUpload == null || scannedDocToUpload.getFileContents() == null) {
								addErrorMessageWithParameter(context, "RequiredFields", "Manual Document");
								pass = false;
							}
						}
					}
					if (summonsBO.getOrigin().equalsIgnoreCase(DocumentType.ORIGIN_AUTOMATIC)) {
						if(StringUtils.isBlank(summonsBO.getManualSerialNo())){ //if not switching from manual to sys gen
							if (summonsBO.getJpIdNumber() == documentCopy.getJpIdNumber() 
									&& StringUtils.isBlank(summonsBO.getNewComment())){ 
									//&& (summonsBO.getManualSerialNo() == documentCopy.getManualSerialNo()) 
									//&& (summonsBO.getOrigin().equalsIgnoreCase(origin))) {
								pass = false;
								addErrorMessageText(context, "No changes were made to document.");
							}
						}
						if(pass){
							// an automatic document should not have a manual serial number
							summonsBO.setManualSerialNo(null);
							scannedDocToUpload = null;
						}
					}

					// if the type is changed to automatic from manual then add warning
					if (summonsBO.getOrigin().equalsIgnoreCase(DocumentType.ORIGIN_AUTOMATIC) && documentCopy.getOrigin().equalsIgnoreCase(DocumentType.ORIGIN_MANUAL)) {
						addInfoMessageText(context, "Please note that Document Uploads associated with this document will be discarded on save.");
					}

					

					/*if (StringUtils.isBlank(summonsBO.getJpRegNumber()) && StringUtils.isNotBlank(documentCopy.getJpRegNumber())) {
						addErrorMessageText(context, "A JP is required, please enter valid details.");
						pass = false;
					}*/
					
					if (summonsBO.getJpIdNumber()==null && documentCopy.getJpIdNumber()!=null) {
						addErrorMessageText(context, "A JP is required, please enter valid details.");
						pass = false;
					}
					
					if(pass){
						if (StringUtils.isNotBlank(summonsBO.getNewComment())) {
							summonsBO.setComment(formatDocumentComment(summonsBO.getNewComment(), summonsBO.getComment()));
						}
					}
				}

			}

			if (document.getDocumentTypeCode().equalsIgnoreCase(Constants.DocumentType.WARNING_NOTICE)) {
				WarningNoticeBO warningNoticeBO = (WarningNoticeBO) context.getFlowScope().get(WARNING_NOTICE);
				
				
				// validate the origin
				if (StringUtils.isBlank(warningNoticeBO.getOrigin())) {
					pass = false;
					addErrorMessageWithParameter(context, "RequiredFields", "Origin");
				} else {
					// if the document is manual then
					if (warningNoticeBO.getOrigin().equalsIgnoreCase(DocumentType.ORIGIN_MANUAL)) {
						if (StringUtils.isBlank(warningNoticeBO.getNewComment()) 
								&& (warningNoticeBO.getManualSerialNo().equalsIgnoreCase(documentCopy.getManualSerialNo())) 
								//&& (warningNoticeBO.getOrigin().equalsIgnoreCase(origin))
								&& (scannedDocToUpload != null && scannedDocToUpload.getFileContents() == null)) {
							pass = false;
							addErrorMessageText(context, "No changes were made to document.");
						}
						// validate the manual serial number
						if (StringUtils.isBlank(warningNoticeBO.getManualSerialNo())) {
							pass = false;
							addErrorMessageWithParameter(context, "RequiredFields", "Manual Reference No.");
						}
						
						if (StringUtils.isNotBlank(warningNoticeBO.getManualSerialNo())) {
							boolean alreadyExists;

							alreadyExists = getDocumentsManagerService().manualSerialExists(warningNoticeBO.getManualSerialNo(), warningNoticeBO.getAutomaticSerialNo(), document.getDocumentTypeCode());

							if (alreadyExists) {
								pass = false;
								addErrorMessageText(context, document.getDocumentTypeName() + " already exists with the Manual Reference No. " + warningNoticeBO.getManualSerialNo().toUpperCase());
							}

							// check scanned doc
							/*if (StringUtils.isNotBlank(warningNoticeBO.getManualSerialNo()) && (documentCopy.getManualSerialNo() == null || !(documentCopy.getManualSerialNo().equalsIgnoreCase(warningNoticeBO.getManualSerialNo())))) {

								if (scannedDocToUpload == null || scannedDocToUpload.getFileContents() == null) {
									addErrorMessageWithParameter(context, "RequiredFields", "Attachment");
									pass = false;
								}
							}*/
						} /*else {// a manual serial number is required
							pass = false;
							addErrorMessageWithParameter(context, "RequiredFields", "Manual Reference No.");
						}*/
						if(manualDocAttached == false){
							if (scannedDocToUpload == null || scannedDocToUpload.getFileContents() == null) {
								addErrorMessageWithParameter(context, "RequiredFields", "Manual Document");
								pass = false;
							}
						}
					}
					if (warningNoticeBO.getOrigin().equalsIgnoreCase(DocumentType.ORIGIN_AUTOMATIC)) {
						if(StringUtils.isBlank(warningNoticeBO.getManualSerialNo())){ //if not switching from manual to sys gen
							if (StringUtils.isBlank(warningNoticeBO.getNewComment())){
									//&& (warningNoticeBO.getOrigin().equalsIgnoreCase(origin))) 
									
								pass = false;
								addErrorMessageText(context, "No changes were made to document.");
							}
						}
						
						if(pass){
							// an automatic document should not have a manual serial number
							warningNoticeBO.setManualSerialNo(null);
							scannedDocToUpload = null;
						}
					}

					// if the type is changed to automatic from manual then add warning
					if (warningNoticeBO.getOrigin().equalsIgnoreCase(DocumentType.ORIGIN_AUTOMATIC) && documentCopy.getOrigin().equalsIgnoreCase(DocumentType.ORIGIN_MANUAL)) {
						addInfoMessageText(context, "Please note that Document Uploads associated with this document will be discarded on save.");
					}
					
					if(pass){
						if (StringUtils.isNotBlank(warningNoticeBO.getNewComment())) {
							warningNoticeBO.setComment(formatDocumentComment(warningNoticeBO.getNewComment(), warningNoticeBO.getComment()));
						}
					}

				}

			}

			if (document.getDocumentTypeCode().equalsIgnoreCase(Constants.DocumentType.WARNING_NOTICE_NO_PROSECUTION)) {

				WarningNoProsecutionBO warningNoProsecution = (WarningNoProsecutionBO) context.getFlowScope().get(WARNING_NOTICE_NO_PROSECUTION);

				if (StringUtils.isBlank(warningNoProsecution.getNewComment()) && StringUtils.isBlank(warningNoProsecution.getManualSerialNo()) && (warningNoProsecution.getOrigin().equalsIgnoreCase(origin))) {
					pass = false;
					addErrorMessageText(context, "No changes were made to document.");
				}
				// validate the origin
				if (StringUtils.isBlank(warningNoProsecution.getOrigin())) {
					pass = false;
					addErrorMessageWithParameter(context, "RequiredFields", "Origin");
				} else {
					// if the document is manual then
					if (warningNoProsecution.getOrigin().equalsIgnoreCase(DocumentType.ORIGIN_MANUAL)) {
						if (StringUtils.isBlank(warningNoProsecution.getNewComment()) 
								&& (warningNoProsecution.getManualSerialNo().equalsIgnoreCase(documentCopy.getManualSerialNo())) 
								//&& (warningNoProsecution.getOrigin().equalsIgnoreCase(origin))
								&& (scannedDocToUpload != null && scannedDocToUpload.getFileContents() == null)) {
							pass = false;
							addErrorMessageText(context, "No changes were made to document.");
						}
						// validate the manual serial number
						if (StringUtils.isBlank(warningNoProsecution.getManualSerialNo())) {
							pass = false;
							addErrorMessageWithParameter(context, "RequiredFields", "Manual Reference No.");
						}
						
						if (StringUtils.isNotBlank(warningNoProsecution.getManualSerialNo())) {
							boolean alreadyExists;

							alreadyExists = getDocumentsManagerService().manualSerialExists(warningNoProsecution.getManualSerialNo(), warningNoProsecution.getAutomaticSerialNo(), document.getDocumentTypeCode());

							if (alreadyExists) {
								pass = false;
								addErrorMessageText(context, document.getDocumentTypeName() + " already exists with the Manual Reference No. " + warningNoProsecution.getManualSerialNo().toUpperCase());
							}

							// check scanned doc
							/*if (StringUtils.isNotBlank(warningNoProsecution.getManualSerialNo()) && (documentCopy.getManualSerialNo() == null || !(documentCopy.getManualSerialNo().equalsIgnoreCase(warningNoProsecution.getManualSerialNo())))) {

								if (scannedDocToUpload == null || scannedDocToUpload.getFileContents() == null) {
									addErrorMessageWithParameter(context, "RequiredFields", "Attachment");
									pass = false;
								}
							}
						} else {// a manual serial number is required
							pass = false;
							addErrorMessageWithParameter(context, "RequiredFields", "Manual Reference No.");
						}*/
							
						
						}
						if(manualDocAttached == false){
							if (scannedDocToUpload == null || scannedDocToUpload.getFileContents() == null) {
								addErrorMessageWithParameter(context, "RequiredFields", "Manual Document");
								pass = false;
							}
						}
					}
					if (warningNoProsecution.getOrigin().equalsIgnoreCase(DocumentType.ORIGIN_AUTOMATIC)) {
						
						if(StringUtils.isBlank(warningNoProsecution.getManualSerialNo())){ //if not switching from manual to sys gen
							if (StringUtils.isBlank(warningNoProsecution.getNewComment())){ 
									//&& (warningNoProsecution.getOrigin().equalsIgnoreCase(origin))) {
								pass = false;
								addErrorMessageText(context, "No changes were made to document.");
							}
						}
						
						if(pass){
							// an automatic document should not have a manual serial number
							warningNoProsecution.setManualSerialNo(null);
							scannedDocToUpload = null;
						}
					}

					// if the type is changed to automatic from manual then add warning
					if (warningNoProsecution.getOrigin().equalsIgnoreCase(DocumentType.ORIGIN_AUTOMATIC) && documentCopy.getOrigin().equalsIgnoreCase(DocumentType.ORIGIN_MANUAL)) {
						addInfoMessageText(context, "Please note that Document Uploads associated with this document will be discarded on save.");
					}
					
					if(pass){
						if (StringUtils.isNotBlank(warningNoProsecution.getNewComment())) {
							warningNoProsecution.setComment(formatDocumentComment(warningNoProsecution.getNewComment(), warningNoProsecution.getComment()));
						}
					}

					}

				}
			
		} catch (RequiredFieldMissingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		return pass;
	}

	/**
	 * A manual serial number should not be repeated for a document type eg. one manual serial number used per summons
	 * 
	 * @param actionEvent
	 * @return
	 */
	public boolean manualSerialExists(AjaxBehaviorEvent actionEvent) {

		RequestContext context = org.springframework.webflow.execution.RequestContextHolder.getRequestContext();

		InputText selected = (InputText) actionEvent.getSource();
		String manualNo = (String) selected.getValue();

		boolean pass = true;
		boolean alreadyExists = false;

		if (StringUtils.isBlank(manualNo)) {
			addErrorMessageText(context, "Enter a valid Manual Reference No.");
			return false;
		}

		DocumentViewBO document = (DocumentViewBO) context.getFlowScope().get(DOCUMENT_IN_VIEW);

		try {
			if (StringUtils.isNotBlank(document.getDocumentTypeCode()) && document.getAutomaticSerialNo() != null) {

				alreadyExists = getDocumentsManagerService().manualSerialExists(manualNo, document.getAutomaticSerialNo(), document.getDocumentTypeCode());
			}
		} catch (RequiredFieldMissingException e) {

			e.printStackTrace();

		}

		if (alreadyExists) {
			pass = false;
			addErrorMessageText(context, "Document already exists with the Manual Reference No. " + manualNo.toUpperCase());
		}
		return pass;
	}

	/**
	 * 
	 * @return
	 */
	public boolean validateForServe(RequestContext context) {
		boolean pass = true;

		DocumentViewBO document = (DocumentViewBO) context.getFlowScope().get(DOCUMENT_IN_VIEW);

		if (document.getDocumentTypeCode().equalsIgnoreCase(Constants.DocumentType.SUMMONS)) {

			SummonsBO summonsBO = (SummonsBO) context.getFlowScope().get(SUMMONS);
			
			if (StringUtils.isBlank(summonsBO.getServedByUserID())) {
				addErrorMessageWithParameter(context, "RequiredFields", "Served by TA Staff");
				pass = false;
			}

			if (summonsBO.getServedOnDate() == null) {
				addErrorMessageWithParameter(context, "RequiredFields", "Date Served");
				pass = false;
			} else {
				if (DateUtils.isDateInFutureDateType(summonsBO.getServedOnDate())) {
					addErrorMessageText(context, "Date Served cannot be a future date.");
					pass = false;
				}

				if (summonsBO.getPrintDtime() != null && DateUtils.before((summonsBO.getServedOnDate()), (summonsBO.getPrintDtime()))) {
					addErrorMessageText(context, "Date Served cannot be before Printed Date.");
					pass = false;
				}
			}

			if (summonsBO.getServedAtAddressBO() != null) {
				
				//populate addressview to be used by global validation method - 20150616 @kpowell
				AddressView summonsAddress = new AddressView(summonsBO.getServedAtAddressBO().getMark(), summonsBO.getServedAtParish(), summonsBO.getServedAtAddressBO().getPoBoxNumber(),
						summonsBO.getServedAtAddressBO().getPoLocation(), summonsBO.getServedAtAddressBO().getStreetName(), summonsBO.getServedAtAddressBO().getStreetNumber(),
						null, null, null,summonsBO.getServedAtAddressBO().getParish());
				
				//global address validation - kpowell
				boolean errorFoundInAddress  = validateAddress(context, summonsAddress);
		        
		        if(errorFoundInAddress){
		              pass = false;
		        }
		        

				/* if (StringUtils.isBlank(summonsBO.getServedAtAddressBO().getPoLocationName())) {
				 * addErrorMessageWithParameter(context, "RequiredFields", "P.O. Location Name"); pass = false; } */

				if (pass) {
					summonsBO.setServedAtAddress(getAddressLine1(summonsBO.getServedAtAddressBO().getStreetName(), summonsBO.getServedAtAddressBO().getMark(), summonsBO.getServedAtAddressBO().getStreetNumber()) + ", " + getAddressLine2(summonsBO.getServedAtAddressBO().getPoBoxNumber(), summonsBO.getServedAtAddressBO().getPoLocation(), null));
				}

			} else {
				addErrorMessageWithParameter(context, "RequiredFields", "Served At Address");
				pass = false;
			}


		}

		if (document.getDocumentTypeCode().equalsIgnoreCase(Constants.DocumentType.WARNING_NOTICE)) {
			WarningNoticeBO warningNoticeBO = (WarningNoticeBO) context.getFlowScope().get(WARNING_NOTICE);
			
			if (StringUtils.isBlank(warningNoticeBO.getServedByUserID())) {
				addErrorMessageWithParameter(context, "RequiredFields", "Served By TA Staff");
				pass = false;
			}
			
			if (warningNoticeBO.getServedOnDate() == null) {
				addErrorMessageWithParameter(context, "RequiredFields", "Date Served");
				pass = false;
			} else {
				if (DateUtils.isDateInFutureDateType(warningNoticeBO.getServedOnDate())) {
					addErrorMessageText(context, "Date Served cannot be a future date.");
					pass = false;
				}

				if (warningNoticeBO.getPrintDtime() != null && DateUtils.before((warningNoticeBO.getServedOnDate()), (warningNoticeBO.getPrintDtime()))) {
					addErrorMessageText(context, "Date Served cannot be before Printed Date.");
					pass = false;
				}
			}

			
		}

		if (document.getDocumentTypeCode().equalsIgnoreCase(Constants.DocumentType.WARNING_NOTICE_NO_PROSECUTION)) {
			WarningNoProsecutionBO warningNoProsecution = (WarningNoProsecutionBO) context.getFlowScope().get(WARNING_NOTICE_NO_PROSECUTION);
			
			if (StringUtils.isBlank(warningNoProsecution.getServedByUserID())) {
				addErrorMessageWithParameter(context, "RequiredFields", "Served By TA Staff");
				pass = false;
			}
			
			if (warningNoProsecution.getServedOnDate() == null) {
				addErrorMessageWithParameter(context, "RequiredFields", "Date Served");
				pass = false;
			} else {
				if (DateUtils.isDateInFutureDateType(warningNoProsecution.getServedOnDate())) {
					addErrorMessageText(context, "Date Served cannot be a future date.");
					pass = false;
				}

				if (warningNoProsecution.getPrintDtime() != null && DateUtils.before((warningNoProsecution.getServedOnDate()), (warningNoProsecution.getPrintDtime()))) {
					addErrorMessageText(context, "Date Served cannot be before Printed Date.");
					pass = false;
				}
			}

			
		}

		return pass;
	}

	/**
	 * 
	 * @return
	 */
	public boolean validateForCancel(RequestContext context) {
		boolean pass = true;

		DocumentViewBO document = (DocumentViewBO) context.getFlowScope().get(DOCUMENT_IN_VIEW);

		if (document.getDocumentTypeCode().equalsIgnoreCase(Constants.DocumentType.SUMMONS)) {

			SummonsBO summonsBO = (SummonsBO) context.getFlowScope().get(SUMMONS);

			if (summonsBO.getReasonCode() == null || summonsBO.getReasonCode() == 0) {
				addErrorMessageWithParameter(context, "RequiredFields", "Reason");
				pass = false;
			}

			if (StringUtils.isBlank(summonsBO.getNewComment())) {
				addErrorMessageWithParameter(context, "RequiredFields", "Comment");
				pass = false;

			} else {
				summonsBO.setComment(formatDocumentComment(summonsBO.getNewComment(), summonsBO.getComment()));
			}

		}

		if (document.getDocumentTypeCode().equalsIgnoreCase(Constants.DocumentType.WARNING_NOTICE)) {
			WarningNoticeBO warningNoticeBO = (WarningNoticeBO) context.getFlowScope().get(WARNING_NOTICE);

			if (warningNoticeBO.getReasonCode() == null || warningNoticeBO.getReasonCode() == 0) {
				addErrorMessageWithParameter(context, "RequiredFields", "Reason");
				pass = false;
			}

			if (StringUtils.isBlank(warningNoticeBO.getNewComment())) {
				addErrorMessageWithParameter(context, "RequiredFields", "Comment");
				pass = false;
			} else {
				warningNoticeBO.setComment(formatDocumentComment(warningNoticeBO.getNewComment(), warningNoticeBO.getComment()));
			}

		}

		if (document.getDocumentTypeCode().equalsIgnoreCase(Constants.DocumentType.WARNING_NOTICE_NO_PROSECUTION)) {
			WarningNoProsecutionBO warningNoProsecution = (WarningNoProsecutionBO) context.getFlowScope().get(WARNING_NOTICE_NO_PROSECUTION);

			if (warningNoProsecution.getReasonCode() == null || warningNoProsecution.getReasonCode() == 0) {
				addErrorMessageWithParameter(context, "RequiredFields", "Reason");
				pass = false;
			}

			if (StringUtils.isBlank(warningNoProsecution.getNewComment())) {
				addErrorMessageWithParameter(context, "RequiredFields", "Comment");
				pass = false;
			} else {
				warningNoProsecution.setComment(formatDocumentComment(warningNoProsecution.getNewComment(), warningNoProsecution.getComment()));
			}

		}

		// System.err.println(" Pass = " + pass);
		return pass;
	}

	/**
	 * 
	 * @return
	 */
	public boolean validateForReprint(RequestContext context) {
		boolean pass = true;

		DocumentViewBO document = (DocumentViewBO) context.getFlowScope().get(DOCUMENT_IN_VIEW);

		if (document.getDocumentTypeCode().equalsIgnoreCase(Constants.DocumentType.SUMMONS)) {

			SummonsBO summonsBO = (SummonsBO) context.getFlowScope().get(SUMMONS);

			if (summonsBO.getReprintReasonCode() == null || summonsBO.getReprintReasonCode() == 0) {
				addErrorMessageWithParameter(context, "RequiredFields", "Reprint Reason");
				pass = false;
			}
			if (StringUtils.isBlank(summonsBO.getNewComment())) {
				addErrorMessageWithParameter(context, "RequiredFields", "Comment");
				pass = false;
			} 

		}

		if (document.getDocumentTypeCode().equalsIgnoreCase(Constants.DocumentType.WARNING_NOTICE)) {
			WarningNoticeBO warningNoticeBO = (WarningNoticeBO) context.getFlowScope().get(WARNING_NOTICE);

			if (warningNoticeBO.getReprintReasonCode() == null || warningNoticeBO.getReprintReasonCode() == 0) {
				addErrorMessageWithParameter(context, "RequiredFields", "Reprint Reason");
				pass = false;
			}

			if (StringUtils.isBlank(warningNoticeBO.getNewComment())) {
				addErrorMessageWithParameter(context, "RequiredFields", "Comment");
				pass = false;
			} 

		}

		if (document.getDocumentTypeCode().equalsIgnoreCase(Constants.DocumentType.WARNING_NOTICE_NO_PROSECUTION)) {
			WarningNoProsecutionBO warningNoProsecution = (WarningNoProsecutionBO) context.getFlowScope().get(WARNING_NOTICE_NO_PROSECUTION);

			if (warningNoProsecution.getReprintReasonCode() == null || warningNoProsecution.getReprintReasonCode() == 0) {
				addErrorMessageWithParameter(context, "RequiredFields", "Reprint Reason");
				pass = false;
			}

			if (StringUtils.isBlank(warningNoProsecution.getNewComment())) {
				addErrorMessageWithParameter(context, "RequiredFields", "Comment");
				pass = false;
			} 

		}

		return pass;
	}

	/**
	 * 
	 * @return
	 */
	public boolean validateForWithdraw(RequestContext context) {
		boolean pass = true;

		DocumentViewBO document = (DocumentViewBO) context.getFlowScope().get(DOCUMENT_IN_VIEW);

		if (document.getDocumentTypeCode().equalsIgnoreCase(Constants.DocumentType.SUMMONS)) {

			SummonsBO summonsBO = (SummonsBO) context.getFlowScope().get(SUMMONS);

			// update comment if not blank
			if (StringUtils.isNotBlank(summonsBO.getNewComment())) {
				summonsBO.setComment(formatDocumentComment(summonsBO.getNewComment(), summonsBO.getComment()));
			}

			if (summonsBO.getDischargeWitness() == null) {
				addErrorMessageWithParameter(context, "RequiredFields", "Discharge Witness");
				pass = false;

			} else {
				if (StringUtils.isBlank(summonsBO.getDischargeWitness().getTrnNbr())) {
					addErrorMessageWithParameter(context, "RequiredFields", "TRN");
					pass = false;
				} else {
					String trn = (String) context.getFlowScope().get("trnSearchValue");

					System.err.println(" testing trns " + trn + "/" + summonsBO.getDischargeWitness().getTrnNbr());

					if (trn != null && !(trn.equalsIgnoreCase(summonsBO.getDischargeWitness().getTrnNbr()))) {
						addErrorMessageText(context, "TRN does not match user details.");
						pass = false;
					}
				}
				
				if (StringUtils.isBlank(summonsBO.getDischargeWitness().getOccupation())) {
					addErrorMessageWithParameter(context, "RequiredFields", "Occupation");
					pass = false;
				}
				
				if (StringUtils.isBlank(summonsBO.getDischargeWitness().getLastName())) {
					addErrorMessageWithParameter(context, "RequiredFields", "Last Name");
					pass = false;
				}
				
				if (StringUtils.isBlank(summonsBO.getDischargeWitness().getFirstName())) {
					addErrorMessageWithParameter(context, "RequiredFields", "First Name");
					pass = false;
				}

				//populate addressview to be used by global validation method - 20150616 @kpowell
				AddressView withdrawAddress = new AddressView(summonsBO.getDischargeWitness().getMarkText(), summonsBO.getDischargeWitness().getParishCode(), summonsBO.getDischargeWitness().getPoBoxNo(),
						summonsBO.getDischargeWitness().getPoLocationName(), summonsBO.getDischargeWitness().getStreetName(), summonsBO.getDischargeWitness().getStreetNo(),
						null, null, null,summonsBO.getDischargeWitness().getParishDesc());
				
				//global address validation - kpowell
				boolean errorFoundInAddress  = validateAddress(context, withdrawAddress);
		        
		        if(errorFoundInAddress){
		              pass = false;
		        }
		        
				/*if (StringUtils.isBlank(summonsBO.getDischargeWitness().getTelephoneCell()) && StringUtils.isBlank(summonsBO.getDischargeWitness().getTelephoneHome()) && StringUtils.isBlank(summonsBO.getDischargeWitness().getTelephoneWork())) {
					addErrorMessageWithParameter(context, "RequiredFields", "At Least one contact number");
					pass = false;
				}*/

				/* if (StringUtils.isBlank(summonsBO.getDischargeWitness().getPoLocationName())) {
				 * addErrorMessageWithParameter(context, "RequiredFields", "P.O. Location Name"); pass = false; } */

				/*if (StringUtils.isNotBlank(summonsBO.getDischargeWitness().getTelephoneCell()) && StringUtils.isNotBlank(summonsBO.getDischargeWitness().getTelephoneHome()) && StringUtils.isNotBlank(summonsBO.getDischargeWitness().getTelephoneWork())) {

					String phone1 = summonsBO.getDischargeWitness().getTelephoneCell().replace("_", "").replace("-", "");
					String phone2 = summonsBO.getDischargeWitness().getTelephoneHome().replace("_", "").replace("-", "");
					String phone3 = summonsBO.getDischargeWitness().getTelephoneWork().replace("_", "").replace("-", "");

					if (StringUtils.isBlank(phone1.trim()) && StringUtils.isBlank(phone2.trim()) && StringUtils.isBlank(phone3.trim())) {
						addErrorMessageWithParameter(context, "RequiredFields", "At Least One(1) Phone Number");
						pass = false;
					}

				}*/
				/*
				 * The above commented out code has been removed and replaced by the code below
				 */
				
				String phone1 = summonsBO.getDischargeWitness().getTelephoneCell();
				String phone2 = summonsBO.getDischargeWitness().getTelephoneHome();
				String phone3 = summonsBO.getDischargeWitness().getTelephoneWork();
				
				if (StringUtils.isBlank(phone1.trim()) && StringUtils.isBlank(phone2.trim()) && StringUtils.isBlank(phone3.trim())) {
					addErrorMessageWithParameter(context, "RequiredFields", "At least one(1) Telephone #");
					pass = false;
				}
				
				if (!StringUtils.isBlank(summonsBO.getDischargeWitness().getTelephoneHome()) && summonsBO.getDischargeWitness().getTelephoneHome().length() < 13) {
					addErrorMessageWithParameter(context, "Telephone", "Home");
					pass = false;
				} 
				if (!StringUtils.isBlank(summonsBO.getDischargeWitness().getTelephoneCell()) && summonsBO.getDischargeWitness().getTelephoneCell().length() < 13) {
					addErrorMessageWithParameter(context, "Telephone", "Cell");
					pass = false;
				} 
				if (!StringUtils.isBlank(summonsBO.getDischargeWitness().getTelephoneWork()) && summonsBO.getDischargeWitness().getTelephoneWork().length() < 13) {
					addErrorMessageWithParameter(context, "Telephone", "Work");
					pass = false;
				} 
				
				
				
				if (summonsBO.getReasonCode() == null || summonsBO.getReasonCode() == 0) {
					addErrorMessageWithParameter(context, "RequiredFields", "Withdrawal Reason");
					pass = false;
				}
				
				if (StringUtils.isBlank(summonsBO.getNewComment())){
					addErrorMessageWithParameter(context, "RequiredFields", "Comment");
					pass = false;
				}

			}
		}

		return pass;
	}

	/**
	 * 
	 * @return
	 */
	public boolean validateCourtAppearanceSave(RequestContext context) {

		boolean pass = true;

		CourtAppearanceBO courtAppearanceBO = (CourtAppearanceBO) context.getFlowScope().get("newCourtAppearance");

		CourtAppearanceBO updatedAppearanceBO = (CourtAppearanceBO) context.getFlowScope().get("updatedCourtAppearance");
		
		String courtParish = (String) context.getFlowScope().get("courtParish");
		//SummonsBO summonsBO = (SummonsBO) context.getFlowScope().get(SUMMONS);

		if (updatedAppearanceBO == null) {
			addErrorMessageText(context, "Previous Court Appearance cannot be empty.");
			pass = false;
		}

		if (updatedAppearanceBO != null) {
			if (StringUtils.isBlank(updatedAppearanceBO.getCourtRulingId())) {
				addErrorMessageText(context, "Please update current court appearance before attempting to create a  new one.");
				pass = false;
				return pass;
			}
		}

		if (courtAppearanceBO == null) {
			addErrorMessageText(context, "Enter Court Appearance details.");
			pass = false;
		}

		/* if (courtAppearanceBO.getCourtCaseId() == null) { addErrorMessageWithParameter(context, "RequiredFields",
		 * "Court Case Id"); pass = false; } */
		// System.err.println(" Court Id for update " + courtAppearanceBO.getCourtId());
		
		if (StringUtils.isBlank(courtParish)){
			addErrorMessageWithParameter(context, "RequiredFields", "Parish");
			pass = false;
		}
		
		if (courtAppearanceBO.getCourtId() == null) {
			addErrorMessageWithParameter(context, "RequiredFields", "Court");
			pass = false;
		}

		if (courtAppearanceBO.getCourtDate() == null) {
			addErrorMessageWithParameter(context, "RequiredFields", "Court Date");
			pass = false;
		} else {
			// jreid 2014-05-28 they can be backdating a court appearanc eon data entrey
//			if (DateUtils.isDateInPast(courtAppearanceBO.getCourtDate())) {
//				addErrorMessageText(context, "Court Date cannot be a past date.");
//				pass = false;
//			}

			// validate that this court date cannot be before initial court date
			if (updatedAppearanceBO.getCourtDate() != null) {

					if (courtAppearanceBO.getCourtDate().before(updatedAppearanceBO.getCourtDate())) {
						addErrorMessageText(context, "Court Date must be after the previous Court Appearance.");
						pass = false;
					}
				
			}
			
			

			int resultFromCourtValidation = DateUtils.validateCourtDate(courtAppearanceBO.getCourtDate(), DateUtils.stringToDate("1990-01-01"));

			switch (resultFromCourtValidation) {
			case 0:
				// do nothing
				// result = true;
				break;

			case 1:
				context.getMessageContext().addMessage(new MessageBuilder().error().defaultText("Court Date must be three(3) clear working days from the Offence Date.").build());
				pass = false;
				break;

			case 2:
				context.getMessageContext().addMessage(new MessageBuilder().error().defaultText("Court Date cannot be a weekend.").build());
				pass = false;
				break;

			case 3:
				context.getMessageContext().addMessage(new MessageBuilder().error().defaultText("Court Date cannot be a holiday.").build());
				pass = false;
				break;

			default:
				// do nothing
				// result = true;
				break;
			}
		}

		return pass;
	}

	public boolean validateForCourtCaseNumOverride(RequestContext context) {
		boolean pass = true;

		SummonsBO summonsBO = (SummonsBO) context.getFlowScope().get(SUMMONS);
		CourtCaseBO updatedCourtCase = (CourtCaseBO) context.getFlowScope().get("updatedCourtCase");

		if (summonsBO == null) {
			addErrorMessageText(context, "Summons  is empty.");
			pass = false;
		}

		CourtCaseBO courtCase = (CourtCaseBO) summonsBO.getCourtCase();

		if (courtCase == null) {
			addErrorMessageText(context, "Court case is empty.");
			pass = false;
		}

		if (StringUtils.isBlank(updatedCourtCase.getCourtCaseNo())) {
			addErrorMessageWithParameter(context, "RequiredFields", "Court Case No.");
			pass = false;
			updatedCourtCase.setCourtCaseNo(courtCase.getCourtCaseNo());
		} 
		
		if (summonsBO.getOverrideReason() == null || summonsBO.getOverrideReason() == 0) {
			addErrorMessageWithParameter(context, "RequiredFields", "Reason");
			pass = false;
		}
		
		if (StringUtils.isBlank(courtCase.getNewComments())) {
			addErrorMessageWithParameter(context, "RequiredFields", "Comment");
			pass = false;
		}
		
		if(pass){
			courtCase.setCourtCaseNo(updatedCourtCase.getCourtCaseNo().trim());
			
			courtCase.setComment(formatDocumentComment(courtCase.getNewComments(), courtCase.getComment()));
			
			List<SelectItem> reasonList = (List<SelectItem>) context.getFlowScope().get("reasonList");
			if (reasonList != null) {
				for (SelectItem x : reasonList) {
					// System.err.println(" values " + x.getValue() + " " + summonsBO.getOverrideReason());
					if (Integer.valueOf(x.getValue().toString()).equals(summonsBO.getOverrideReason())) {
						String s = courtCase.getComment().concat("; Reason: " + x.getLabel()) + ";";
						courtCase.setComment(s);
					}
				}
			}
			
			
		}
		

		// update value on object
		/* if (pass) { summonsBO.setCourtCase(courtCase); context.getFlowScope().put("SUMMONS", summonsBO); } */

		return pass;
	}

	/*public boolean validateForCourtAppearanceDateOverride(RequestContext context) {
		boolean pass = true;

		SummonsBO summonsBO = (SummonsBO) context.getFlowScope().get(SUMMONS);
		XMLGregorianCalendar newDate = (XMLGregorianCalendar) context.getFlowScope().get("editCourtDate");

		if (summonsBO == null) {
			addErrorMessageText(context, "Summons  is empty.");
			pass = false;
		}

		CourtCaseBO courtCase = (CourtCaseBO) summonsBO.getCourtCase();

		if (courtCase == null) {
			addErrorMessageText(context, "Court case is empty.");
			pass = false;
		}

		CourtAppearanceBO courtAppearanceBO = (CourtAppearanceBO) context.getFlowScope().get("updatedCourtAppearance");

		if (courtAppearanceBO == null) {
			addErrorMessageText(context, "Court Appearance details is empty.");
			pass = false;
		}

		if (newDate == null) {
			addErrorMessageWithParameter(context, "RequiredFields", "New Court Date");
			pass = false;
		} else {

			// VALIDATE THE COURT DATE FOR 3 DAYS CLEAR, HOLIDAYS AND WEEKENDS
			int resultFromCourtValidation = DateUtils.validateCourtDate(newDate.toGregorianCalendar().getTime(), summonsBO.getIssueDate().toGregorianCalendar().getTime());
			switch (resultFromCourtValidation) {
			case 0:
				// do nothing
				// result = true;
				break;

			case 1:
				context.getMessageContext().addMessage(new MessageBuilder().error().defaultText("Court Date must be three(3) clear working days from the Offence Date.").build());
				pass = false;
				break;

			case 2:
				context.getMessageContext().addMessage(new MessageBuilder().error().defaultText("Court Date cannot be a weekend.").build());
				pass = false;
				break;

			case 3:
				context.getMessageContext().addMessage(new MessageBuilder().error().defaultText("Court Date cannot be a holiday.").build());
				pass = false;
				break;

			default:
				// do nothing
				// result = true;
				break;
			}
			
			if (newDate != null && DateUtils.before(DateUtils.getXMLGregorianCalendarAsDate(newDate),
					DateUtils.getXMLGregorianCalendarAsDate(summonsBO.getServedOnDate()))) {
				addErrorMessageText(context, "Court Date must be after the Served Date.");
				pass = false;
			}

			List<CourtAppearanceBO> courtAppList = courtCase.getCourtAppearances();
			CourtAppearanceBO previous = null;

			if (courtAppList != null && !courtAppList.isEmpty() && courtAppList.size() > 1) {
				previous = courtAppList.get(courtAppList.size() - 2);
			}

			if (previous != null) {
				// validate that this court date cannot be before previous court date
				if (previous.getCourtDate() != null) {
					if (newDate.toGregorianCalendar().getTime().before(previous.getCourtDate().toGregorianCalendar().getTime())) {
						addErrorMessageText(context, "Court Date must be after the previous Court Appearance.");
						pass = false;
					}
				}

			}
		}
		
		if (summonsBO.getOverrideReason() == null || summonsBO.getOverrideReason() == 0) {
			addErrorMessageWithParameter(context, "RequiredFields", "Reason");
			pass = false;
		}

		if (StringUtils.isBlank(courtCase.getNewComments())) {
			addErrorMessageWithParameter(context, "RequiredFields", "Comment");
			pass = false;
		} 

		
		if(pass)
		{
			courtAppearanceBO.setComment(formatDocumentComment(courtCase.getNewComments(), courtAppearanceBO.getComment()));
			courtAppearanceBO.setCourtDate(newDate);
			
			
			List<SelectItem> reasonList = (List<SelectItem>) context.getFlowScope().get("reasonList");
			if (reasonList != null) {
				for (SelectItem x : reasonList) {
					// System.err.println(" values " + x.getValue() + " " + summonsBO.getOverrideReason());
					if (Integer.valueOf(x.getValue().toString()).equals(summonsBO.getOverrideReason())) {
						String s = courtAppearanceBO.getComment().concat("; Reason: " + x.getLabel());
						courtAppearanceBO.setComment(s);
					}
				}
			}
		}
		else{
			courtAppearanceBO.setCourtDate(summonsBO.getCourtCase().getNextAppearanceCourtDate());
		}
		// update value on object
		 if (pass) { summonsBO.setCourtCase(courtCase); context.getFlowScope().put("SUMMONS", summonsBO); } 

		return pass;
	}*/
	
	//Validate For Court Appearance Court and Court Date Override
	public boolean validateForCourtAppearanceCourtOverride(RequestContext context){
		boolean pass = true;
		
		SummonsBO summonsBO = (SummonsBO) context.getFlowScope().get(SUMMONS);
		String courtParish = (String) context.getFlowScope().get("courtParish");
		Date newDate = (Date) context.getFlowScope().get("editCourtDate");

		if (summonsBO == null) {
			addErrorMessageText(context, "Summons  is empty.");
			pass = false;
		}

		CourtCaseBO courtCase = (CourtCaseBO) summonsBO.getCourtCase();

		if (courtCase == null) {
			addErrorMessageText(context, "Court case is empty.");
			pass = false;
		}

		CourtAppearanceBO courtAppearanceBO = (CourtAppearanceBO) context.getFlowScope().get("updatedCourtAppearance");

		if (courtAppearanceBO == null) {
			addErrorMessageText(context, "Court Appearance details is empty.");
			pass = false;
		}
		
		if (courtAppearanceBO.getCourtId() == null &&
				newDate == null) {
			addErrorMessageText(context, "New Court and/or New Court Date are required.");
			pass = false;
		}
		
		/*if(StringUtils.isBlank(courtParish)){
			addErrorMessageWithParameter(context, "RequiredFields", "Parish");
			pass = false;
		}*/
		
	/*	if (courtAppearanceBO.getCourtId() == null) {
			addErrorMessageWithParameter(context, "RequiredFields", "New Court");
			pass = false;
		}*/
		
		//validate court date
		/*if (newDate == null) {
			addErrorMessageWithParameter(context, "RequiredFields", "New Court Date");
			pass = false;
		}*/ 
		if (newDate != null){

			// VALIDATE THE COURT DATE FOR 3 DAYS CLEAR, HOLIDAYS AND WEEKENDS
			int resultFromCourtValidation = DateUtils.validateCourtDate(newDate, summonsBO.getIssueDate());
			switch (resultFromCourtValidation) {
			case 0:
				// do nothing
				// result = true;
				break;

			case 1:
				context.getMessageContext().addMessage(new MessageBuilder().error().defaultText("Court Date must be three(3) clear working days from the Offence Date.").build());
				pass = false;
				break;

			case 2:
				context.getMessageContext().addMessage(new MessageBuilder().error().defaultText("Court Date cannot be a weekend.").build());
				pass = false;
				break;

			case 3:
				context.getMessageContext().addMessage(new MessageBuilder().error().defaultText("Court Date cannot be a holiday.").build());
				pass = false;
				break;

			default:
				// do nothing
				// result = true;
				break;
			}
			
			if (newDate != null && DateUtils.before(newDate,summonsBO.getServedOnDate())) {
				addErrorMessageText(context, "Court Date must be after the Served Date.");
				pass = false;
			}

			List<CourtAppearanceBO> courtAppList = courtCase.getCourtAppearances();
			CourtAppearanceBO previous = null;

			if (courtAppList != null && !courtAppList.isEmpty() && courtAppList.size() > 1) {
				//previous = courtAppList.get(courtAppList.size() - 2);
				previous = courtAppList.get(1);
			}

			if (previous != null) {
				// validate that this court date cannot be before previous court date
				if (previous.getCourtDate() != null) {
					if (newDate.before(previous.getCourtDate())) {
						addErrorMessageText(context, "Court Date must be after the previous Court Appearance.");
						pass = false;
					}
				}

			}
		}
		
		if (summonsBO.getOverrideReason() == null || summonsBO.getOverrideReason() == 0) {
			addErrorMessageWithParameter(context, "RequiredFields", "Reason");
			pass = false;
		} 
		
		if (StringUtils.isBlank(courtCase.getNewComments())) {
			addErrorMessageWithParameter(context, "RequiredFields", "Comment");
			pass = false;
		} 

		if(pass)
		{
			courtAppearanceBO.setComment(formatDocumentComment(courtCase.getNewComments(), courtAppearanceBO.getComment()));
			if(newDate != null)
				courtAppearanceBO.setCourtDate(newDate);
			
			if(courtAppearanceBO.getCourtId() == null){
				courtAppearanceBO.setCourtId(summonsBO.getCourtCase().
						getCourtAppearances().get(0).getCourtId());
			}else{
				CourtCriteriaBO criteriaBO = new CourtCriteriaBO();
				criteriaBO.setCourtId(courtAppearanceBO.getCourtId());
				CourtBO newCourt = null;
				try {
					newCourt = getMaintenanceService().
							lookupCourt(criteriaBO).get(0);
					
					if(newCourt != null){
						courtAppearanceBO.setCourtName(newCourt.getShortDescription());
					}if(newCourt != null){
						courtAppearanceBO.setCourtName(newCourt.getShortDescription());
					}
				} catch (NoRecordFoundException e) {
					logger.error("Doc Man",e);

				} catch (RequiredFieldMissingException e) {
					logger.error("Doc Man",e);
				}
				
			}
			
			List<SelectItem> reasonList = (List<SelectItem>) context.getFlowScope().get("reasonList");
			if (reasonList != null) {
				for (SelectItem x : reasonList) {
					// System.err.println(" values " + x.getValue() + " " + summonsBO.getOverrideReason());
					if (Integer.valueOf(x.getValue().toString()).equals(summonsBO.getOverrideReason())) {
						String s = courtAppearanceBO.getComment().concat("; Reason: " + x.getLabel());
						courtAppearanceBO.setComment(s);
					}
				}
			}
		}


		
		return pass;
	}

	/**
	 * 
	 * @return
	 */
	public boolean validateCourtAppearanceUpdate(RequestContext context, Boolean override) {

		boolean pass = true;
		Date currentDate = new Date(System.currentTimeMillis());

		CourtAppearanceBO courtAppearanceBO = (CourtAppearanceBO) context.getFlowScope().get("updatedCourtAppearance");

		if (courtAppearanceBO == null) {
			addErrorMessageText(context, "Enter Court Appearance details.");
			pass = false;
		}

		// validate the court case object first to ensure the case # is added
		if (!validateCourtCaseUpdate(context)) {
			pass = false;
		}

		if (courtAppearanceBO.getCourtCaseId() == null) {
			addErrorMessageWithParameter(context, "RequiredFields", "Court Case Id");
			pass = false;
		}

		if (courtAppearanceBO.getCourtDate() == null) {
			addErrorMessageWithParameter(context, "RequiredFields", "Court Date");
			pass = false;
		} else {
			/* if (DateUtils.isDateInPast(courtAppearanceBO.getCourtDate())) { addErrorMessageText(context,
			 * "Court Date cannot be a past date."); pass = false; } */
			SummonsBO summonsBO = (SummonsBO) context.getFlowScope().get(SUMMONS);

			if (summonsBO == null) {
				addErrorMessageText(context, "Summons  is empty.");
				pass = false;
			}
		
			

			CourtCaseBO courtCase = (CourtCaseBO) summonsBO.getCourtCase();

			if (courtCase == null) {
				addErrorMessageText(context, "Court case is empty.");
				pass = false;
			}

			
			/* List<CourtAppearanceBO> courtAppList = courtCase.getCourtAppearances(); CourtAppearanceBO previous =
			 * null; if (courtAppList != null && !courtAppList.isEmpty() && courtAppList.size() > 1) { previous =
			 * courtAppList.get(courtAppList.size() - 2); } */

			// VALIDATE THE COURT DATE FOR 3 DAYS CLEAR, HOLIDAYS AND WEEKENDS
			int resultFromCourtValidation = DateUtils.validateCourtDate(courtAppearanceBO.getCourtDate(), DateUtils.stringToDate("1990-01-01"));
			switch (resultFromCourtValidation) {
			case 0:
				// do nothing
				// result = true;
				break;

			case 1:
				context.getMessageContext().addMessage(new MessageBuilder().error().defaultText("Court Date must be three(3) clear working days from the Offence Date.").build());
				pass = false;
				break;

			case 2:
				context.getMessageContext().addMessage(new MessageBuilder().error().defaultText("Court Date cannot be a weekend.").build());
				pass = false;
				break;

			case 3:
				context.getMessageContext().addMessage(new MessageBuilder().error().defaultText("Court Date cannot be a holiday.").build());
				pass = false;
				break;

			default:
				// do nothing
				// result = true;
				break;
			}
			
			//KG 2015-09-04
			if (courtAppearanceBO.getCourtId() == null){
				courtAppearanceBO.setCourtId(courtCase.getCourtAppearances().get(0).getCourtId());
			}
			System.err.println("court id: "+courtAppearanceBO.getCourtId());
			if (courtAppearanceBO.getCourtId() == null) {
				addErrorMessageWithParameter(context, "RequiredFields", "Court");
				pass = false;
			}
			
			if (courtAppearanceBO.getCourtDate().after(currentDate)) {
				addErrorMessageText(context, "Cannot update Court Appearance for a future Court Date.");
				pass = false;
				
				courtAppearanceBO.setPleaId(null);
				courtAppearanceBO.setVerdictId(null);
				courtAppearanceBO.setCourtRulingId(null);
				courtCase.setNewComments(null);
				courtAppearanceBO.setInspectorAttended(null);
			}
			else
			{

			/* if (previous != null) { // validate that this court date cannot be before previous court date if
			 * (previous.getCourtDate() != null) { if
			 * (courtAppearanceBO.getCourtDate().toGregorianCalendar().getTime().before
			 * (previous.getCourtDate().toGregorianCalendar().getTime())) { addErrorMessageText(context,
			 * "Court Date cannot be before Court Date of previous appearance."); pass = false; } } } else { // there is
			 * only one court appearance // addErrorMessageText(context,
			 * "Cannot update Court Date of initial Court Appearance."); // pass = false; // as of June 13,2014 can
			 * override court appearance initial date if (override) { if (courtAppearanceBO.getCourtDate() != null &&
			 * DateUtils.before(DateUtils.getXMLGregorianCalendarAsDate(courtAppearanceBO.getCourtDate()), new Date()))
			 * { addErrorMessageText(context, "Court Date must be a future date."); pass = false; } } } */
		
			
				
		
				// System.err.println(" plea" + courtAppearanceBO.getPleaId());
				/* if (courtAppearanceBO.getPleaId() == null || courtAppearanceBO.getPleaId() == 0) {
				 * addErrorMessageWithParameter(context, "RequiredFields", "Plea"); pass = false; } */
				
				
				
		
		//		if (StringUtils.isBlank(courtAppearanceBO.getCourtRulingId()) && courtAppearanceBO.getVerdictId() != null && !courtAppearanceBO.getVerdictId().equals(VERDICT.WITHDRAWN)) {
		//			addErrorMessageWithParameter(context, "RequiredFields", "Court Ruling ");
		//			pass = false;
		//		}
				
				
//				if(courtAppearanceBO.getVerdictId() != null && !courtAppearanceBO.getVerdictId().equals(VERDICT.WITHDRAWN) && courtAppearanceBO.getCourtRulingId() == null)//if verdict is not "withdrawn"
//				{
//						addErrorMessageWithParameter(context, "RequiredFields", "Court Ruling");
//						pass = false;
//				}
//				else
//				{
//					if (courtAppearanceBO.getCourtRulingId() == null && courtAppearanceBO.getVerdictId() == null) {
//						addErrorMessageWithParameter(context, "RequiredFields", "Court Ruling");
//						pass = false;
//					}
//				}
				
				if(override){
					if (summonsBO.getOverrideReason() == null){
						addErrorMessageWithParameter(context, "RequiredFields", "Reason");
						pass = false;
					}
				}
				
				
				if (courtAppearanceBO.getCourtRulingId() == null) {
					addErrorMessageWithParameter(context, "RequiredFields", "Court Ruling");
					pass = false;
				}
		
				// jreid 2014-05-09
				/*if (courtAppearanceBO.getPleaId() != null && courtAppearanceBO.getPleaId() == Constants.PLEA.GUILTY && courtAppearanceBO.getVerdictId() == null) {
					addErrorMessageWithParameter(context, "RequiredFields", "Verdict ");
					pass = false;
				}
		
				if (courtAppearanceBO.getPleaId() != null && courtAppearanceBO.getVerdictId() != null) {
					if (courtAppearanceBO.getPleaId().equals(Constants.PLEA.GUILTY) && courtAppearanceBO.getVerdictId().equals(Constants.VERDICT.NOT_GUILTY)) {
						addErrorMessageWithParameter(context, "RequiredFields", "Verdict ");
						pass = false;
					}
				}*/
		
				if (courtAppearanceBO.getPleaId() != null && StringUtils.isNotBlank(courtAppearanceBO.getCourtRulingId())) {
					if (courtAppearanceBO.getVerdictId() != null)
					{
					// Cannot understand this
//						if (courtAppearanceBO.getPleaId().equals(Constants.PLEA.GUILTY) && courtAppearanceBO.getVerdictId().equals(Constants.VERDICT.NOT_GUILTY)) {
//							SelectOneMenu menu = new SelectOneMenu();
//							// System.err.println(" ruling " + courtAppearanceBO.getCourtRulingId());
//							menu.setValue(courtAppearanceBO.getCourtRulingId());
//			
//							AjaxBehaviorEvent event = new AjaxBehaviorEvent(menu, null);
//			
//							Event response = validateCourtRuling(event);
//							if (response == error()) {
//								// System.err.println("Validate court ruling failed.");
//								pass = false;
//							}
//						}
			
						// both are guilty
						if (courtAppearanceBO.getPleaId().equals(Constants.PLEA.GUILTY) && courtAppearanceBO.getVerdictId().equals(Constants.VERDICT.GUILTY)) {
							List<RefCodeBO> rulings = (List<RefCodeBO>) context.getFlowScope().get("courtRulingListForUpdate");
			
							if (!isCourtRulingFinal(courtAppearanceBO.getCourtRulingId())) {
								addErrorMessageWithParameter(context, "RequiredFields", "For a Guilty Plea a Final Court Ruling ");
								pass = false;
							}
							
							
						}
					}
				}
				if(courtAppearanceBO.getVerdictId() == null && isCourtRulingFinal(courtAppearanceBO.getCourtRulingId())){
					addErrorMessageWithParameter(context, "RequiredFields", "Verdict ");
					pass = false;
				}
				
				if (courtAppearanceBO.getInspectorAttended() == null) {
					addErrorMessageWithParameter(context, "RequiredFields", "Inspector Attended ");
					pass = false;
				}
				
				if(courtAppearanceBO.getCourtRulingId()!=null && isCourtRulingFinal(courtAppearanceBO.getCourtRulingId())){
					Double fine = null;
					
					if(courtCase.getFinePaidFlag() == null){
							courtCase.setFinePaidFlag("false");
					}
						
					if(courtCase.getTimeServedFlag() == null){
							courtCase.setTimeServedFlag("false");
					}
					
				
					if(courtCase.getFineAmount()!= null){
						fine = new Double(courtCase.getFineAmount().doubleValue());
					}
					if((fine != null && fine ==0)|| fine == null){
						if(courtCase.getFinePaidFlag() != null && (courtCase.getFinePaidFlag().equalsIgnoreCase("true")||
								courtCase.getFinePaidFlag().equalsIgnoreCase("y"))){
							addErrorMessageWithParameter(context, "RequiredFields", "Fine Amount ");
							pass = false;
						}
					}
					
					
					if((courtCase.getSentencePeriodDay() == null || courtCase.getSentencePeriodDay() ==0)
							&& (courtCase.getSentencePeriodMonth() == null || courtCase.getSentencePeriodMonth() ==0)
							&& (courtCase.getSentencePeriodYear() == null || courtCase.getSentencePeriodYear() ==0)){
						if(courtCase.getTimeServedFlag()!=null && (courtCase.getTimeServedFlag().equalsIgnoreCase("true")||
								courtCase.getTimeServedFlag().equalsIgnoreCase("y"))){
							addErrorMessageWithParameter(context, "RequiredFields", "Sentence Period ");
							pass = false;
						}
					}

					if ((fine != null && fine > 0)
							|| (courtCase.getSentencePeriodDay() != null && courtCase
									.getSentencePeriodDay() > 0)
							|| (courtCase.getSentencePeriodMonth() != null && courtCase
									.getSentencePeriodMonth() > 0)
							|| (courtCase.getSentencePeriodYear() != null && courtCase
									.getSentencePeriodYear() > 0)) {
						if ((courtCase.getFinePaidFlag() == null && courtCase.getTimeServedFlag() == null )||
							((courtCase.getTimeServedFlag() != null && (courtCase.getTimeServedFlag().equalsIgnoreCase("false") || courtCase.getTimeServedFlag().equalsIgnoreCase("n")))
									&&(courtCase.getFinePaidFlag() != null && (courtCase.getFinePaidFlag().equalsIgnoreCase("false") || courtCase.getFinePaidFlag().equalsIgnoreCase("n")))))
						{
							addErrorMessageWithParameter(context,"RequiredFields", "Sentence Outcome ");
							pass = false;
						}
					}
					
					if(fine != null && fine<0) {
						addErrorMessageText(context, "Fine Amount must be greater than zero.");
						pass = false;
					}
					

					if((courtCase.getSentencePeriodDay() != null && courtCase.getSentencePeriodDay() <0)
							|| (courtCase.getSentencePeriodMonth() != null && courtCase.getSentencePeriodMonth() <0)
							|| (courtCase.getSentencePeriodYear() != null && courtCase.getSentencePeriodYear() <0)){
						addErrorMessageText(context, "Sentenced Served must be greater than zero.");
						pass = false;
					}
					/*if(courtCase.getPrisonSentenceEndDate() != null &&
							courtCase.getPrisonSentenceStartDate() == null){
						addErrorMessageWithParameter(context, "RequiredFields", "Prison Sentence Start Date ");
						pass = false;
					}
					
					if(courtCase.getPrisonSentenceStartDate() != null &&
							courtCase.getPrisonSentenceEndDate() == null){
						addErrorMessageWithParameter(context, "RequiredFields", "Prison Sentence End Date ");
						pass = false;
					}
					
					if(courtCase.getPrisonSentenceEndDate() != null 
							&& courtCase.getPrisonSentenceStartDate() != null
							&& (DateUtils.getXMLGregorianCalendarAsDate(courtCase.getPrisonSentenceStartDate())
									.after(DateUtils.getXMLGregorianCalendarAsDate(courtCase.getPrisonSentenceEndDate())))){
	
						addErrorMessageText(context, "End Date cannot be before Start Date.");
						pass = false;
					}
					
					if(courtCase.getPrisonSentenceStartDate() != null){
						if(courtCase.getPrisonSentenceStartDate().toGregorianCalendar().getTime().before(courtAppearanceBO.getCourtDate().toGregorianCalendar().getTime())){
							addErrorMessageText(context, "Prison Sentence Start Date cannot be before the current Court Appearance Date.");
							pass = false;
						}
					}*/
					
				}
				/*
				 * Check if a court ruling is final and if so that a verdict code has
				 * been set for that final court ruling
				 */
//				if (isCourtRulingFinal(courtAppearanceBO.getCourtRulingId()) && courtAppearanceBO.getVerdictId() == null) {
//					addErrorMessageText(context, "You need to enter a verdict for final court rulings. ");
//					pass = false;
//					
//				}
				
				
				if(override){
					if (StringUtils.trimToNull(summonsBO.getCourtCase().getNewComments()) == null){
						addErrorMessageWithParameter(context, "RequiredFields", "New Comment");
						pass = false;
					}
				}					
				
				if(pass)
				{
					if (StringUtils.isNotBlank(courtCase.getNewComments())) {
						// update comment if not blank
						courtCase.setComment(formatDocumentComment(courtCase.getNewComments(), courtCase.getComment()));
					} 
					
					
					if (StringUtils.isNotBlank(courtCase.getNewComments())) {
						courtAppearanceBO.setComment(formatDocumentComment(courtCase.getNewComments(), courtAppearanceBO.getComment()));
					}
					
					if(courtAppearanceBO.getCourtRulingId()!=null && isCourtRulingFinal(courtAppearanceBO.getCourtRulingId())){
						if(courtCase.getFinePaidFlag() != null){
							if(courtCase.getFinePaidFlag().equalsIgnoreCase("true")){
								courtCase.setFinePaidFlag("Y");
							}
							if(courtCase.getFinePaidFlag().equalsIgnoreCase("false")){
								courtCase.setFinePaidFlag("N");
							}
						}
							
						if(courtCase.getTimeServedFlag() != null){
							if(courtCase.getTimeServedFlag().equalsIgnoreCase("true"))
							{
								courtCase.setTimeServedFlag("Y");
							}
							if(courtCase.getTimeServedFlag().equalsIgnoreCase("false")){
								courtCase.setTimeServedFlag("N");
							}
						}
					}
				}

						
					
				}
		}

		return pass;

	}


	/**
	 * 
	 * @return
	 */
	public boolean validateCourtCaseUpdate(RequestContext context) {

		boolean pass = true;

		SummonsBO summonsBO = (SummonsBO) context.getFlowScope().get(SUMMONS);

		if (summonsBO == null) {
			addErrorMessageText(context, "Summons  is empty.");
			pass = false;
		}

		CourtCaseBO courtCase = (CourtCaseBO) summonsBO.getCourtCase();

		if (courtCase == null) {
			addErrorMessageText(context, "Court case is empty.");
			pass = false;
		}

		if (StringUtils.isBlank(courtCase.getCourtCaseNo())) {
			addErrorMessageWithParameter(context, "RequiredFields", "Court Case No.");
			pass = false;
		} else {
			courtCase.setCourtCaseNo(courtCase.getCourtCaseNo().trim());

		}

		/* if (StringUtils.isBlank(courtCase.getNewComments())) { addErrorMessageWithParameter(context,
		 * "RequiredFields", "Comment"); pass = false; } else { */
//		if (StringUtils.isNotBlank(courtCase.getNewComments())) {
//			// update comment if not blank
//			courtCase.setComment(formatDocumentComment(courtCase.getNewComments(), courtCase.getComment()));
//		} else {
//
//		}

		return pass;

	}

	/**
	 * 
	 * @return
	 */
	public boolean validateForUpload(RequestContext context) {
		boolean pass = true;

		ScannedDocBO scannedDocToUpload = (ScannedDocBO) context.getFlowScope().get("scannedDocToUpload");

		if (scannedDocToUpload != null) {

			if (StringUtils.isBlank(scannedDocToUpload.getDocType())) {
				addErrorMessageWithParameter(context, "RequiredFields", "Document Type");
				pass = false;
			} else {
				/* if(scannedDocToUpload.getDocType().equalsIgnoreCase( ScannedDocumentType.MANUAL_DOCUMENT)){
				 * if(StringUtils.isBlank(scannedDocToUpload.get)){ addErrorMessageWithParameter(context,
				 * "RequiredFields", "Street Name"); pass = false; } } */
			}
			
			if (StringUtils.isBlank(scannedDocToUpload.getDescription())) {
				addErrorMessageWithParameter(context, "RequiredFields", "Description");
				pass = false;
			}
			
			if (scannedDocToUpload.getFileContents() == null) {
				addErrorMessageWithParameter(context, "RequiredFields", "Attachment");
				pass = false;
			}

		} else {
			addErrorMessageWithParameter(context, "RequiredFields", "File Details");
			pass = false;
		}

		return pass;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isCriteriaEmpty(DocumentsCriteriaBO criteria) {
		boolean isEmpty = true;

		/*if ((!StringUtils.isEmpty(criteria.getDocumentType()) && !criteria.getDocumentType().equalsIgnoreCase("All")))
			isEmpty = false;*/

		/*if (!StringUtils.isEmpty(criteria.getDocumentStatus()))
			isEmpty = false;*/

		if (!StringUtils.isEmpty(criteria.getReferenceNo()))
			isEmpty = false;

		if (!StringUtils.isEmpty(criteria.getManualSerialNo()))
			isEmpty = false;

		/* if (StringUtils.isEmpty(criteria.getServedByStaffId())) isEmpty = false; if
		 * (criteria.getServedStartDateRange() != null) isEmpty = false; if (criteria.getServedEndDateRange() != null)
		 * isEmpty = false; */

		/*if (!StringUtils.isEmpty(criteria.getStaffId()))
			isEmpty = false;*/

		if (!StringUtils.isEmpty(criteria.getOperationName()))
			isEmpty = false;

		if (criteria.getOperationStartDateRange() != null)
			isEmpty = false;

		if (criteria.getOperationEndDateRange() != null)
			isEmpty = false;

		/*if (!StringUtils.isEmpty(criteria.getTrnNbr()))
			isEmpty = false;*/

		/*if (!StringUtils.isEmpty(criteria.getOffenderFirstName()))
			isEmpty = false;

		if (!StringUtils.isEmpty(criteria.getOffenderMiddleName()))
			isEmpty = false;

		if (!StringUtils.isEmpty(criteria.getOffenderLastName()))
			isEmpty = false;*/

		if (criteria.getOffenceStartDateRange() != null)
			isEmpty = false;

		if (criteria.getOffenceEndDateRange() != null)
			isEmpty = false;

		return isEmpty;
	}

	/**
	 * Used to retrieve statuses based of TYPE from table roms_statuses
	 * 
	 * @param context
	 * @return
	 */
	public void getDocManagerStatuses(RequestContext context) {
		List<RefCodeBO> refcodes = null;

		try {

			HashMap<String, String> fields = new HashMap<String, String>();
			fields.put("type", StatusType.DOCUMENT_MANAGER);

			refcodes = getRefInfo("roms_cd_status", fields);

			
			/* if (StringUtils.isNotBlank(docType) && !docType.equalsIgnoreCase(DocumentType.SUMMONS)) { // remove the
			 * withdrawn status refcodes.remove(5); } */
			DocumentsCriteriaBO criteria = (DocumentsCriteriaBO) context.getFlowScope().get("criteria");
			
			//System.err.println("refcode size: "+refcodes.size() );
			
			if(criteria != null){
				String docType = criteria.getDocumentType(); 
				
				if (StringUtils.isNotBlank(docType) && 
						!docType.equalsIgnoreCase(DocumentType.SUMMONS)&&!docType.equalsIgnoreCase(DocumentType.ALL_DOCUMENTS)) {
					refcodes.remove(5);
				}
			}
			
			//System.err.println("refcode size: "+refcodes.size() );
			
			context.getFlowScope().put("statusList", refcodes);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/****************************** COURT DETAILS *************************************************/

	public Event viewCourtDetails(RequestContext context) {

		SummonsBO summonsBO = (SummonsBO) context.getFlowScope().get(SUMMONS);

		if (summonsBO == null) {
			addErrorMessageText(context, "Summons  is empty.");
			return error();
		}

		CourtCaseBO courtCase = (CourtCaseBO) summonsBO.getCourtCase();

		if (courtCase == null) {
			addErrorMessageText(context, "Court case is empty.");
			return error();
		}

		if (!summonsBO.getStatusId().equalsIgnoreCase(DocumentStatus.SERVED) && !summonsBO.getStatusId().equalsIgnoreCase(DocumentStatus.CANCELLED) && !summonsBO.getStatusId().equalsIgnoreCase(DocumentStatus.WITHDRAWN) && !summonsBO.getStatusId().equalsIgnoreCase(DocumentStatus.CLOSED)) {
			FacesMessage msg = new FacesMessage("Document needs to be served before court details can be edited.");
			msg.setSeverity(FacesMessage.SEVERITY_WARN);
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}

		if (summonsBO.getStatusId().equalsIgnoreCase(DocumentStatus.SERVED) && StringUtils.isBlank(courtCase.getCourtCaseNo()) && !summonsBO.getStatusId().equalsIgnoreCase(DocumentStatus.CANCELLED) && !summonsBO.getStatusId().equalsIgnoreCase(DocumentStatus.WITHDRAWN) && !summonsBO.getStatusId().equalsIgnoreCase(DocumentStatus.CLOSED)) {
			FacesMessage msg = new FacesMessage("Add Court Case No# before details can be edited.");
			msg.setSeverity(FacesMessage.SEVERITY_WARN);
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	
		
		return success();
	}

	/**
	 * 
	 * @param context
	 * @return
	 */
	public Event saveCourtAppearanceDetails(RequestContext context) {

		SummonsBO summonsBO = (SummonsBO) context.getFlowScope().get(SUMMONS);

		if (summonsBO == null) {
			addErrorMessageText(context, "Summons  is empty.");
			return error();
		}

		CourtCaseBO courtCase = (CourtCaseBO) summonsBO.getCourtCase();

		if (courtCase == null) {
			addErrorMessageText(context, "Court case is empty.");
			return error();
		}

		CourtAppearanceBO newCourtAppearance = (CourtAppearanceBO) context.getFlowScope().get("newCourtAppearance");

		if (newCourtAppearance == null) {
			addErrorMessageText(context, "Enter Court Appearance details.");
			return error();
		}

		try {

			// validate then save
			if (validateCourtAppearanceSave(context)) {
				newCourtAppearance.setCurrentUserName(getRomsLoggedInUser().getUsername());
				newCourtAppearance.setSummonsId(summonsBO.getAutomaticSerialNo());
				newCourtAppearance.setCourtCaseId(courtCase.getCourtCaseId());
				newCourtAppearance.setComment(formatDocumentComment(newCourtAppearance.getComment(),""));
				getDocumentsManagerService().saveCourtAppearance(newCourtAppearance);
				addInfoMessageText(context, "Court appearance was saved successfully.");

				context.getFlowScope().put("modeCC", "viewCC");
				context.getFlowScope().put("updatedCourtAppearance", newCourtAppearance);
				this.loadDocumentDetails(context);
			}

		} catch (Exception e) {
			addErrorMessageText(context, e.getMessage());
			e.printStackTrace();
			return error();
		}

		return success();

	}

	/**
	 * 
	 * @param context
	 * @return
	 */
	public Event updateCourtAppearanceDetails(RequestContext context) {
		SummonsBO summonsBO = (SummonsBO) context.getFlowScope().get(SUMMONS);

		if (summonsBO == null) {
			addErrorMessageText(context, "Summons  is empty.");
			return error();
		}

		CourtCaseBO courtCase = (CourtCaseBO) summonsBO.getCourtCase();

		if (courtCase == null) {
			addErrorMessageText(context, "Court case is empty.");
			return error();
		}

		// CourtAppearanceBO newCourtAppearance = (CourtAppearanceBO) context.getFlowScope().get("newCourtAppearance");
		CourtAppearanceBO updatedCourtAppearance = (CourtAppearanceBO) context.getFlowScope().get("updatedCourtAppearance");

		if (updatedCourtAppearance == null) {
			addErrorMessageText(context, "Enter Court Appearance details.");
			return error();
		}

		try {

			// validate then save
			if (validateCourtAppearanceUpdate(context, false)) {
				
				updatedCourtAppearance.setCurrentUserName(getRomsLoggedInUser().getUsername());
				courtCase.setCurrentUserName(getRomsLoggedInUser().getUsername());
				// newCourtAppearance.setSummonsId(summonsBO
				// .getAutomaticSerialNo());
				// newCourtAppearance.setCourtCaseId(courtCase.getCourtCaseId());
				getDocumentsManagerService().updateCourtAppearance(updatedCourtAppearance);
				//this method is updating court appearance so do not update comment on the court case
				courtCase.setComment(null);
				if(updatedCourtAppearance.getCourtRulingId() !=null && isCourtRulingFinal(updatedCourtAppearance.getCourtRulingId())) {	
					//update court case fields like fines etc.
					getDocumentsManagerService().updateCourtCase(courtCase);
				}
				else
				{
					clearCourtCaseDetails(courtCase);
					updatedCourtAppearance.setVerdictId(null);
					//clearSentenceDate();
					getDocumentsManagerService().updateCourtCase(courtCase);
				}

				// if no details on the new appearance is entered then dont show new section
				/* if (newCourtAppearance == null) { context.getFlowScope().put("showNewCourtAppearance", "false"); }
				 * else { context.getFlowScope().put("showNewCourtAppearance", "true"); } */

				context.getFlowScope().put("modeCC", "viewCC");
				this.loadDocumentDetails(context);
				context.getMessageContext().clearMessages();
				addInfoMessageText(context, "Court appearance was updated successfully.");

			}
			else
			{
				//updatedCourtAppearance.setCourtRulingId(null);
				//context.getFlowScope().put("finalRuling", YesNo.NO_LABEL);
			}
			

		} catch (Exception e) {
			addErrorMessageText(context, e.getMessage());
			e.printStackTrace();
			return error();
		}

		return success();

	}

	/**
	 * 
	 * @param context
	 * @return
	 */
	public Event cancelUpdateCourtCase(RequestContext context) {


		this.loadDocumentDetails(context);

		context.getFlowScope().put("modeCC", "viewCC");

		/* context.getFlowScope().put(SUMMONS, "viewCC"); */
		return success();

	}

	/**
	 * 
	 * @param context
	 * @return
	 */
	public Event updateCourtCase(RequestContext context) {

		SummonsBO summonsBO = (SummonsBO) context.getFlowScope().get(SUMMONS);
		if (summonsBO == null) {
			addErrorMessageText(context, "Summons  is empty.");
			return error();
		}

		CourtCaseBO courtCase = (CourtCaseBO) summonsBO.getCourtCase();
		if (courtCase == null) {
			addErrorMessageText(context, "Court case is empty.");
			return error();
		}

		try {
			// validate then save
			if (validateCourtCaseUpdate(context)) {
				courtCase.setCurrentUserName(getRomsLoggedInUser().getUsername());
				if (StringUtils.isNotBlank(courtCase.getNewComments())) {
					// update comment if not blank
					courtCase.setComment(formatDocumentComment(courtCase.getNewComments(), courtCase.getComment()));
				} else {

				}
				
				getDocumentsManagerService().updateCourtCase(courtCase);
				addInfoMessageText(context, "Court case details updated successfully.");

				// update the comments field on screen
								
				courtCase.setNewComments(null);
				summonsBO.setCourtCase(courtCase);
				context.getFlowScope().put(SUMMONS, summonsBO);
				
				
			} else {
				return error();
			}

		} catch (Exception e) {
			addErrorMessageText(context, e.getMessage());
			e.printStackTrace();
			return error();
		}
		// System.err.println("Method called update court app");
		context.getFlowScope().put("modeCC", "viewCC");
		return success();

	}

	/**
	 * 
	 * @param context
	 * @return
	 */
	public Event overrideCourtCaseDetails(RequestContext context) {

		System.err.println(" override called");
		SummonsBO summonsBO = (SummonsBO) context.getFlowScope().get(SUMMONS);
		
		if (summonsBO == null) {
			addErrorMessageText(context, "Summons  is empty.");
			return error();
		}

		CourtCaseBO courtCase = (CourtCaseBO) summonsBO.getCourtCase();
		if (courtCase == null) {
			addErrorMessageText(context, "Court case is empty.");
			return error();
		}

		/* CourtAppearanceBO updatedCourtAppearance = (CourtAppearanceBO)
		 * context.getFlowScope().get("updatedCourtAppearance"); if (updatedCourtAppearance == null) {
		 * addErrorMessageText(context, "Enter Court Appearance details."); return error(); } else {
		 * courtCase.getCourtAppearances().set(0, updatedCourtAppearance); //
		 * context.getFlowScope().put("updatedCourtAppearance", ) } */
		/* boolean override = false; String mode = (String) context.getFlowScope().get("modeCC"); if
		 * (StringUtils.isNotBlank(mode) && mode.contains("override")) override = true; */

		try {
			// validate then save
			if (validateForCourtCaseNumOverride(context)) {
				// update the last court appearance
				courtCase.getCourtAppearances().clear();
				System.err.println(" Appearance List After Clear " + courtCase.getCourtAppearances());

				courtCase.setCurrentUserName(getRomsLoggedInUser().getUsername());
				getDocumentsManagerService().updateCourtCase(courtCase); // overrideCourtCaseDetails(courtCase);

				// update the comments field on screen
				courtCase.setNewComments(null);
				summonsBO.setCourtCase(courtCase);
				// context.getFlowScope().put(SUMMONS, summonsBO);
				this.loadDocumentDetails(context);
				addInfoMessageText(context, "Court case details updated successfully.");
			} else {
				return error();
			}

		} catch (Exception e) {
			addErrorMessageText(context, e.getMessage());
			e.printStackTrace();
			return error();
		}
		System.err.println("Method called update court app1");
		context.getFlowScope().put("modeCC", "viewCC");
		return success();

	}

	/**
	 * 
	 * @param context
	 * @return
	 */
	public Event overrideCourtAppearanceDetails(RequestContext context) {

		System.err.println(" override called");
		SummonsBO summonsBO = (SummonsBO) context.getFlowScope().get(SUMMONS);
		
		if (summonsBO == null) {
			addErrorMessageText(context, "Summons  is empty.");
			return error();
		}
		/*else
		{
			if (summonsBO.getOverrideReason() == null){
				addErrorMessageWithParameter(context, "RequiredFields", "Reason");
				return error();
			}
			
			
			
			if (StringUtils.trimToNull(summonsBO.getCourtCase().getNewComments()) == null){
				addErrorMessageWithParameter(context, "RequiredFields", "New Comment");
				return error();
				
			}
		}*/

		CourtCaseBO courtCase = (CourtCaseBO) summonsBO.getCourtCase();
		if (courtCase == null) {
			addErrorMessageText(context, "Court case is empty.");
			return error();
		}

		CourtAppearanceBO updatedCourtAppearance = (CourtAppearanceBO) context.getFlowScope().get("updatedCourtAppearance");
		
		if (updatedCourtAppearance == null) {
			addErrorMessageText(context, "Enter Court Appearance details.");
			return error();
		} else {
			
			
			courtCase.getCourtAppearances().set(0, updatedCourtAppearance);
			// context.getFlowScope().put("updatedCourtAppearance", )

		}
		
		//boolean overrideDate = false;
		boolean overrideCourt = false;
		String mode = (String) context.getFlowScope().get("modeCC");
		System.err.println(" mode is " + mode);
		/*if (StringUtils.isNotBlank(mode) && mode.contains("overrideCD"))
			overrideDate = true;*/
		
		if(StringUtils.isNotBlank(mode) && mode.equalsIgnoreCase("overrideC")) //override court name
			overrideCourt = true;
		
		try {
			/*if (overrideDate) {

				if (validateForCourtAppearanceDateOverride(context)) {

					// update the last court appearance
					courtCase.setCurrentUserName(getRomsLoggedInUser().getUsername());
	
					getDocumentsManagerPortProxy().overrideCourtCaseDetails(courtCase);

					// update the comments field on screen
					courtCase.setNewComments(null);
					summonsBO.setCourtCase(courtCase);
					// context.getFlowScope().put(SUMMONS, summonsBO);
					this.loadDocumentDetails(context);
					addInfoMessageText(context, "Court Date updated successfully.");
				} else {
					
					return error();
				}
			} else {*/
				if(overrideCourt){//if only the court or court date is being updated for the court appearance
					if (validateForCourtAppearanceCourtOverride(context)) {
						courtCase.setCurrentUserName(getRomsLoggedInUser().getUsername());
						//set verdict and ruling to null to prevent the whole court appearance from being updated
						//in CourtAppearanceDAOIImpl
						//TODO use flag instead of nulling out fields
						courtCase.getCourtAppearances().get(0).setVerdictId(null);
						courtCase.getCourtAppearances().get(0).setCourtRulingId(null);
						clearCourtCaseDetails(courtCase);
						
						getDocumentsManagerService().overrideCourtCaseDetails(courtCase);

						// update the comments field on screen
						courtCase.setNewComments(null);
						summonsBO.setCourtCase(courtCase);
						// context.getFlowScope().put(SUMMONS, summonsBO);
						this.loadDocumentDetails(context);
						addInfoMessageText(context, "Court Appearance updated successfully.");
					}else {
						
						return error();
					}
				}else{
					if (validateCourtAppearanceUpdate(context,true)) {
						
							courtCase.setCurrentUserName(getRomsLoggedInUser().getUsername());
							if(updatedCourtAppearance.getCourtRulingId() != null){
								if(!isCourtRulingFinal(updatedCourtAppearance.getCourtRulingId())){
									//clearSentenceDate();
									clearCourtCaseDetails(courtCase);
									updatedCourtAppearance.setVerdictId(null);
								}
							}
							getDocumentsManagerService().overrideCourtCaseDetails(courtCase);
		
							// update the comments field on screen
							courtCase.setNewComments(null);
							summonsBO.setCourtCase(courtCase);
							// context.getFlowScope().put(SUMMONS, summonsBO);
							this.loadDocumentDetails(context);
							addInfoMessageText(context, "Court Case details updated successfully.");
						
						
					} else {
	
						return error();
					}
				}
			

		} catch (Exception e) {
			addErrorMessageText(context, e.getMessage());
			e.printStackTrace();
			return error();
		}
		System.err.println("Method called update court app2");
		context.getFlowScope().put("modeCC", "viewCC");
		return success();

	}

	/**
	 * 
	 * @param actionEvent
	 * @return
	 */
	public Event updateCourtCaseDetails(AjaxBehaviorEvent actionEvent) {

		RequestContext context = org.springframework.webflow.execution.RequestContextHolder.getRequestContext();
		SummonsBO summonsBO = (SummonsBO) context.getFlowScope().get(SUMMONS);
		if (summonsBO == null) {
			addErrorMessageText(context, "Summons  is empty.");
			return error();
		}

		CourtCaseBO courtCase = (CourtCaseBO) summonsBO.getCourtCase();
		if (courtCase == null) {
			addErrorMessageText(context, "Court case is empty.");
			return error();
		}

		try {

			// validate then save
			if (validateCourtCaseUpdate(context)) {
				courtCase.setCurrentUserName(getRomsLoggedInUser().getUsername());
				if (StringUtils.isNotBlank(courtCase.getNewComments())) {
					// update comment if not blank
					courtCase.setComment(formatDocumentComment(courtCase.getNewComments(), courtCase.getComment()));
				} else {

				}
				getDocumentsManagerService().updateCourtCase(courtCase);
				addInfoMessageText(context, "Court case details updated successfully.");

				// update the comments field on screen
				courtCase.setNewComments(null);
				summonsBO.setCourtCase(courtCase);
				context.getFlowScope().put(SUMMONS, summonsBO);
			} else {
				return error();
			}

		} catch (Exception e) {
			addErrorMessageText(context, e.getMessage());
			e.printStackTrace();
			return error();
		}
		// System.err.println("Method called update court app");
		context.getFlowScope().put("modeCC", "viewCC");
		return success();

	}

	public Event cancelUpdateCourtCase(AjaxBehaviorEvent actionEvent) {

		RequestContext context = org.springframework.webflow.execution.RequestContextHolder.getRequestContext();
		this.loadDocumentDetails(context);
		context.getFlowScope().put("modeCC", "viewCC");

		return success();

	}

	/**
	 * 
	 * @param context
	 * @return
	 */
	public Event clearCourtAppearanceDetails(RequestContext context) {

		context.getFlowScope().put("viewScannedDoc", false);
		context.getFlowScope().put("openReportWindow", false);
		context.getFlowScope().put("scannedDocToUpload", new ScannedDocBO());
		/* context.getFlowScope().put("currentUploadedFile", null); */
		context.getFlowScope().put("newCourtAppearance", new CourtAppearanceBO());
		context.getFlowScope().put("updatedCourtAppearance", new CourtAppearanceBO());
		context.getFlowScope().put("modeCC", "");

		return success();

	}

	/**
	 * Helper function to add new comments to old comments so that they cannot be overridden
	 * 
	 * @param newComment
	 * @param oldComment
	 * @return
	 */
	public String formatDocumentComment(String newComment, String oldComment) {

		Date date = new Date(System.currentTimeMillis());
		String comment = null;
		try {
			if (StringUtils.isNotBlank(newComment) && StringUtils.isBlank(oldComment)) {
				//return "<b>(" + DateUtils.getFormattedUtilDate(date) + "/" + getRomsLoggedInUser().getUsername() + ")</b>:<br/>" + newComment.trim();
				return "(" + DateUtils.getFormattedUtilDate(date) + "/" + getRomsLoggedInUser().getUsername() + "): " + newComment.trim();
			}

			if (StringUtils.isBlank(newComment) && StringUtils.isNotBlank(oldComment)) {
				return oldComment;
			}

			// when retrieving full comment then replace slash with new line
			// character
			if (StringUtils.isNotBlank(newComment) && StringUtils.isNotBlank(oldComment)) {

				//comment = oldComment + ";<b>(" + DateUtils.getFormattedUtilDate(date) + "/" + getRomsLoggedInUser().getUsername() + ")</b>:<br/>" + newComment.trim();
				comment = oldComment + "(" + DateUtils.getFormattedUtilDate(date) + "/" + getRomsLoggedInUser().getUsername() + "): " + newComment.trim();

			}
		} catch (ParseException e) {

			e.printStackTrace();
		}
		return comment;

	}

	/**
	 * Helper function to add new comments to old comments so that they cannot be overridden
	 * 
	 * @param newComment
	 * @param oldComment
	 * @param username
	 *            of person doing the overriding
	 * @return
	 */
	public String formatOverrideDocumentComment(String newComment, String oldComment, String userName) {

		Date date = new Date(System.currentTimeMillis());
		String comment = null;
		try {
			if (StringUtils.isNotBlank(newComment) && StringUtils.isBlank(oldComment)) {
				return "(" + DateUtils.getFormattedUtilDate(date) + "/Verified by " + userName + "): " + newComment.trim();
			}

			if (StringUtils.isBlank(newComment) && StringUtils.isNotBlank(oldComment)) {
				return oldComment;
			}

			// when retrieving full comment then replace slash with new line
			// character
			if (StringUtils.isNotBlank(newComment) && StringUtils.isNotBlank(oldComment)) {

				comment = oldComment + "(" + DateUtils.getFormattedUtilDate(date) + "/ Verified by " + userName + "): " + newComment.trim();

			}
		} catch (ParseException e) {

			e.printStackTrace();
		}
		return comment;

	}

	/**
	 * Helper function to add new comments to old comments so that they cannot be overridden
	 * 
	 * @param newComment
	 * @param oldComment
	 * @param username
	 *            of person doing the overriding
	 * @return
	 */
	public String formatDenyOverrideDocumentComment(String newComment, String oldComment, String userName) {

		Date date = new Date(System.currentTimeMillis());
		String comment = null;
		try {
			if (StringUtils.isNotBlank(newComment) && StringUtils.isBlank(oldComment)) {
				return "(" + DateUtils.getFormattedUtilDate(date) + "/Details Not Accepted by " + userName + "): " + newComment.trim();
			}

			if (StringUtils.isBlank(newComment) && StringUtils.isNotBlank(oldComment)) {
				return oldComment;
			}

			// when retrieving full comment then replace slash with new line
			// character
			if (StringUtils.isNotBlank(newComment) && StringUtils.isNotBlank(oldComment)) {

				comment = oldComment + "(" + DateUtils.getFormattedUtilDate(date) + "/ Details Not Accepted by " + userName + "): " + newComment.trim();

			}
		} catch (ParseException e) {

			e.printStackTrace();
		}
		return comment;

	}

	/**
	 * 
	 * @param actionEvent
	 */
	public void validatePlea(AjaxBehaviorEvent actionEvent) {

		RequestContext context = org.springframework.webflow.execution.RequestContextHolder.getRequestContext();
		CourtAppearanceBO updatedCourtAppearance = (CourtAppearanceBO) context.getFlowScope().get("updatedCourtAppearance");
		Boolean isFirstAppearance = (Boolean) context.getFlowScope().get("isFirstCourtAppearance");
		try {
			SelectOneMenu selected = (SelectOneMenu) actionEvent.getSource();
			Integer code = (Integer) selected.getValue();

			if (code == null) {
				return;
			}

			if (updatedCourtAppearance == null) {
				return;
			}

			if (code.equals(Constants.PLEA.GUILTY)) {
				// context.getFlowScope().put("modeCA", arg1);
				FilterCourtRulings(true);
				updatedCourtAppearance.setVerdictId(Constants.VERDICT.GUILTY);
			}
			
			if (code.equals(Constants.PLEA.NOT_GUILTY)) {
				if(isFirstAppearance){
					FilterCourtRulings(false);
					context.getFlowScope().put("finalRuling",
						YesNo.NO_LABEL_STR);
				}else{
					getCourtRulings(context);
				}
				updatedCourtAppearance.setVerdictId(null);
			}
			context.getFlowScope().put("updatedCourtAppearance", updatedCourtAppearance);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * @param actionEvent
	 */
	public void validateVerdict(AjaxBehaviorEvent actionEvent) {
		RequestContext context = org.springframework.webflow.execution.RequestContextHolder.getRequestContext();
		CourtAppearanceBO updatedCourtAppearance = (CourtAppearanceBO) context.getFlowScope().get("updatedCourtAppearance");

		try {
			SelectOneMenu selected = (SelectOneMenu) actionEvent.getSource();
			Integer code = (Integer) selected.getValue();

			if (code == null) {
				return;
			}

			if (updatedCourtAppearance == null) {
				return;
			}

			// if it is withdrawn court ruling not needed
//			if (code.equals(Constants.VERDICT.WITHDRAWN)) {
//				// context.getFlowScope().put("modeCA", arg1);
//				updatedCourtAppearance.setCourtRulingId(null);
//			}

			// ensure that verdict details is updated on screen
			updatedCourtAppearance.setVerdictId(code);

			context.getFlowScope().put("updatedCourtAppearance", updatedCourtAppearance);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param actionEvent
	 * @return
	 */
	public Event validateCourtRuling(AjaxBehaviorEvent actionEvent) {

		RequestContext context = org.springframework.webflow.execution.RequestContextHolder.getRequestContext();
		CourtAppearanceBO updatedCourtAppearance = (CourtAppearanceBO) context.getFlowScope().get("updatedCourtAppearance");

		List<RefCodeBO> rulings = (List<RefCodeBO>) context.getFlowScope().get("courtRulingListForUpdate");

		try {
			SelectOneMenu selected = (SelectOneMenu) actionEvent.getSource();
			String code = (String) selected.getValue();

			if (updatedCourtAppearance == null) {
				addErrorMessageText(context, "Select edit from Court Appearance List.");
				return error();
			}

			if (StringUtils.isBlank(code) && updatedCourtAppearance != null && updatedCourtAppearance.getVerdictId() != null && !updatedCourtAppearance.getVerdictId().equals(VERDICT.WITHDRAWN)) {
				addErrorMessageText(context, "Choose valid Court Ruling.");
				return error();
			}
			context.getFlowScope().put("finalRuling", YesNo.NO_LABEL);
			for (RefCodeBO ref : rulings) {

				if (ref.getCode().equalsIgnoreCase(code)) {// this is the ruling
															// that was selected
					// System.err.println(" final ruling flag ::" +
					// ref.getAltId());

					// set the message on screen
					if (ref.getAltId().equalsIgnoreCase(
							Constants.YesNo.YES_LABEL_STR)) {
						context.getFlowScope().put("finalRuling",
								YesNo.YES_LABEL_STR);
					}

					if (updatedCourtAppearance.getPleaId() != null
							&& updatedCourtAppearance.getPleaId().equals(
									Constants.PLEA.GUILTY)
							&& ref.getAltId().equalsIgnoreCase(
									Constants.YesNo.NO_LABEL_STR)) {// if
																	// guilty
																	// //
																	// ruling
						addErrorMessageWithParameter(context, "RequiredFields",
								"For a Guilty Plea a Final Ruling ");
						return error();
					}

					if (updatedCourtAppearance.getVerdictId() != null
							&& ref.getAltId().equalsIgnoreCase(
									Constants.YesNo.NO_LABEL_STR)) {// if
																	// guilty
																	// Verdict
																	// then
																	// MUST
																	// final
																	// ruling
						addErrorMessageWithParameter(context, "RequiredFields",
								"For a Verdict a Final Ruling ");
						return error();
					}

					if (updatedCourtAppearance.getVerdictId() == null
							&& ref.getAltId().equalsIgnoreCase(
									Constants.YesNo.YES_LABEL_STR)) {// if
																		// guilty
																		// Verdict
																		// then
																		// MUST
																		// final
																		// ruling
						addErrorMessageWithParameter(context, "RequiredFields",
								"For a Final Court Ruling a Verdict ");
						return error();
					}

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			addErrorMessageText(context, "Choose valid Court Ruling.");
			return error();
		}
		return success();
	}
	
	public void validateSentenceServedFine(AjaxBehaviorEvent actionEvent) {
		RequestContext context = org.springframework.webflow.execution.RequestContextHolder.getRequestContext();
		SummonsBO summonsBO = (SummonsBO) context.getFlowScope().get(SUMMONS);
		CourtCaseBO courtCase = null;

		try {
			if (summonsBO != null){
				courtCase = (CourtCaseBO) summonsBO.getCourtCase();
			}
			if(courtCase != null){
				SelectBooleanCheckbox selected = (SelectBooleanCheckbox) actionEvent.getSource();
				String selectedValue = selected.getValue().toString();
							
				courtCase.setFinePaidFlag(selectedValue);
			}
			
		}
		catch(Exception e){
			logger.error(e.getMessage());
		}
	}
	
	public void validateSentenceServedTime(AjaxBehaviorEvent actionEvent) {
		RequestContext context = org.springframework.webflow.execution.RequestContextHolder.getRequestContext();
		SummonsBO summonsBO = (SummonsBO) context.getFlowScope().get(SUMMONS);
		CourtCaseBO courtCase = null;

		try {
			if (summonsBO != null){
				courtCase = (CourtCaseBO) summonsBO.getCourtCase();
			}
			if(courtCase != null){
				SelectBooleanCheckbox selected = (SelectBooleanCheckbox) actionEvent.getSource();
				String selectedValue = selected.getValue().toString();
				
				courtCase.setTimeServedFlag(selectedValue);
			}
			
		}
		catch(Exception e){
			logger.error(e.getMessage());
		}
	}

	/************************************** FILE UPLOAD ******************************************/
	/**
	 * VERY IMPORTANT This allows upload of document to scanned doc module
	 * 
	 * @param event
	 */
	public void handleFileUpload(FileUploadEvent event) {
		RequestContext context = RequestContextHolder.getRequestContext();
		UploadedFile uploadedFile = event.getFile();
		// System.err.println(" handle upload called");

		DocumentViewBO document = (DocumentViewBO) context.getFlowScope().get(DOCUMENT_IN_VIEW);

		if (document == null) {
			addErrorMessage(context, "NoDetailsToDisplay");
		}

		if (uploadedFile != null) {

			// get the bytes and save it in the request context
			byte[] bytesOfData = uploadedFile.getContents();

			// get the list currently storing files form the flowscope
			ScannedDocBO scannedDocToUpload = (ScannedDocBO) context.getFlowScope().get("scannedDocToUpload");

			// document uploaded now
			if (scannedDocToUpload != null) {
				// save the document now..
				scannedDocToUpload.setFileContents(bytesOfData);
				scannedDocToUpload.setMimeType(uploadedFile.getContentType());

				context.getFlowScope().put("scannedDocToUpload", scannedDocToUpload);

				context.getFlowScope().put("uploadedFileName", uploadedFile.getFileName());

				FacesMessage msg = new FacesMessage("File " + uploadedFile.getFileName() + " uploaded successfully.");
				FacesContext.getCurrentInstance().addMessage(null, msg);
			}

		} else {
			FacesMessage msg = new FacesMessage("Failed", "Please upload a valid file!");
			FacesContext.getCurrentInstance().addMessage(null, msg);

		}
	}

	/**
	 * VERY IMPORTANT This allows upload of document to scanned doc module
	 * 
	 * @param event
	 */
	public void handleFileUpload2(FileUploadEvent event) {
		RequestContext context = RequestContextHolder.getRequestContext();
		UploadedFile uploadedFile = event.getFile();
		// System.err.println(" handle upload called");

		DocumentViewBO document = (DocumentViewBO) context.getFlowScope().get(DOCUMENT_IN_VIEW);

		if (document == null) {
			addErrorMessage(context, "NoDetailsToDisplay");
		}

		if (uploadedFile != null) {

			// get the bytes and save it in the request context
			byte[] bytesOfData = uploadedFile.getContents();

			// get the list currently storing files form the flowscope
			ScannedDocBO scannedDocToUpload = (ScannedDocBO) context.getFlowScope().get("scannedDocToUpload");

			// document uploaded now
			if (scannedDocToUpload != null) {
				// save the document now..
				ScannedDocBO scannedDoc = new ScannedDocBO();
				scannedDoc.setCurrentUsername(getRomsLoggedInUser().getUsername());
				scannedDoc.setRoadCheckOffenceOutcomeId(document.getRoadCheckOffenceOutcomeCode());
				// scannedDoc.setRoadCheckOffence(document.getDocumentTypeCode());
				scannedDoc.setFileContents(bytesOfData);
				scannedDoc.setDescription(scannedDocToUpload.getDescription());
				scannedDoc.setMimeType(uploadedFile.getContentType());
				scannedDoc.setDocType(scannedDocToUpload.getDocType());

				
				DocumentViewBO docview = new DocumentViewBO();
				try {
					copyFields(docview,document);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				// do the actual upload of file
				try {
					getScannedDocUploadService().uploadFile(scannedDoc, docview);
					context.getFlowScope().put("scannedDocToUpload", new ScannedDocBO());
					FacesMessage msg = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
					FacesContext.getCurrentInstance().addMessage(null, msg);

				} catch (ErrorSavingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					FacesMessage msg = new FacesMessage("Failed", "Please upload a valid file!");
					FacesContext.getCurrentInstance().addMessage(null, msg);

				} catch (RequiredFieldMissingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					FacesMessage msg = new FacesMessage("Failed", "Please upload a valid file!");
					FacesContext.getCurrentInstance().addMessage(null, msg);

				}
				// System.err.println(" after save scanned doc ");

			}

		} else {
			FacesMessage msg = new FacesMessage("Failed", "Please upload a valid file!");
			FacesContext.getCurrentInstance().addMessage(null, msg);

		}
	}

	public void handleDialogClose(CloseEvent event) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, event.getComponent().getId() + " closed", "successful close!");

		facesContext.addMessage(null, message);
		RequestContext context = RequestContextHolder.getRequestContext();

		context.getFlowScope().put("viewScannedDoc", false);
		context.getFlowScope().put("openReportWindow", false);
		context.getFlowScope().put("scannedDocToUpload", new ScannedDocBO());
		/* context.getFlowScope().put("currentUploadedFile", null); */
		context.getFlowScope().put(MODE, "view");
		//System.err.println(" now loading from cancel action");
		// this.cancelAction(context);
		this.loadDocumentDetails(context);
		context.getFlowScope().put("modeCC", "viewCC");
		


	}

	/**
	 * 
	 * @param event
	 */
	public void handleDocumentSelect(SelectEvent event) {
		// System.err.println(" handling select row ");
		Object item = event.getObject();
		RequestContext context = RequestContextHolder.getRequestContext();

		DocumentViewBO document = null;

		if (item != null) {
			document = (DocumentViewBO) item;
			// System.err.println(" document I have clicked " + document.getReferenceNo());
		}

		context.getFlowScope().put(DOCUMENT_IN_VIEW, document);
		context.getFlowScope().put(DOCUMENT_IN_VIEW_COPY, document);
	}

	/**
	 * 
	 * @param event
	 */
	public void handleDocumentToggleSelect(ToggleSelectEvent event) {
		// System.err.println(" handling select row ");
		Object item = event.getSource();
		// System.err.println(" what is the source " + event.getSource());
		RequestContext context = RequestContextHolder.getRequestContext();

		ArrayList<DocumentViewBO> selectedDocuments = (ArrayList<DocumentViewBO>) context.getFlowScope().get("documentsSelected");
		// System.err.println(" checking for the docs selected");
		if (selectedDocuments == null || selectedDocuments.isEmpty()) {
			// System.err.println(" nothing in list, dont show print button");
			return;
		} else {
			ArrayList<DocumentViewBO> tempDocuments = new ArrayList<DocumentViewBO>();
			tempDocuments.addAll(selectedDocuments);
			selectedDocuments.clear();

			// based on bug on the UI exclude all documents that are not in CREATED status
			for (DocumentViewBO doc : tempDocuments) {
				// System.err.println(" doc :" + doc.getStatusId());
				if (doc.getStatusId().equalsIgnoreCase(DocumentStatus.CREATED) && (doc.getVerificationRequired() == null || doc.getVerificationRequired().equals(YesNo.NO_LABEL_STR))) {

					selectedDocuments.add(doc);
					// System.err.println(" this one is created ");
				}
			}

			if (selectedDocuments == null || selectedDocuments.isEmpty()) {
				// System.err.println(" nothing in list after removal, dont show print button");
				context.getFlowScope().put("documentsSelected", selectedDocuments);
				// context.getViewScope().put("documentsSelected", selectedDocuments);
				return;
			}
		}

		// System.err.println(" selected toggle " + selectedDocuments);

		context.getFlowScope().put("documentsSelected", selectedDocuments);

	}

	/********************* Populate By TRN **************************/
	public Event searchTrn(RequestContext context) {

		try {
			String trn = null;
			DocumentViewBO document = (DocumentViewBO) context.getFlowScope().get(DOCUMENT_IN_VIEW);

			if (document.getDocumentTypeCode().equalsIgnoreCase(Constants.DocumentType.SUMMONS)) {

				SummonsBO summonsBO = (SummonsBO) context.getFlowScope().get(SUMMONS);

				if (summonsBO.getDischargeWitness() == null) {
					addErrorMessageWithParameter(context, "RequiredFields", "Discharge Witness Details");

				} else {
					if (StringUtils.isBlank(summonsBO.getDischargeWitness().getTrnNbr())) {
						addErrorMessageWithParameter(context, "RequiredFields", "TRN");
						summonsBO.setDischargeWitness(new PersonBO());
						context.getFlowScope().put("trnSearchValue", "");
						return error();
					} else {

						trn = summonsBO.getDischargeWitness().getTrnNbr().replaceAll("-", "").trim();
						context.getFlowScope().put("trnSearchValue", trn);
						if (StringUtils.isBlank(trn)) {
							addErrorMessageWithParameter(context, "RequiredFields", "TRN");
							summonsBO.setDischargeWitness(new PersonBO());
							summonsBO.getDischargeWitness().setTrnNbr(trn);
							return error();
						}
					}

					// check ROMS database first
					PersonBO personBO = getDocumentsManagerService().getPersonByTRN(trn);

					// if not in roms database the go to TRN webservice
					if (personBO == null) {

						PersonBO personBOTRN;
						personBOTRN = getTRNWebService().getPersonBOByTrn(Integer.parseInt(trn));

						// user does not exist at all, return error
						if (personBOTRN == null) {
							addErrorMessage(context, "Norecordfound");
							return error();
						} else {
							// copt trn database data
							summonsBO.getDischargeWitness().setFirstName(personBOTRN.getFirstName());
							summonsBO.getDischargeWitness().setLastName(personBOTRN.getLastName());
							summonsBO.getDischargeWitness().setMiddleName(personBOTRN.getMiddleName());
							summonsBO.getDischargeWitness().setOccupation(personBOTRN.getOccupation());
							summonsBO.getDischargeWitness().setMarkText(personBOTRN.getMarkText());
							summonsBO.getDischargeWitness().setParishCode(personBOTRN.getParishCode());
							summonsBO.getDischargeWitness().setPoBoxNo(personBOTRN.getPoBoxNo());
							summonsBO.getDischargeWitness().setPoLocationName(personBOTRN.getPoLocationName());
							summonsBO.getDischargeWitness().setStreetName(personBOTRN.getStreetName());
							summonsBO.getDischargeWitness().setStreetNo(personBOTRN.getStreetNo());

							context.getFlowScope().put(SUMMONS, summonsBO);

						}
					} else {
						// copy roms database person object
						summonsBO.setDischargeWitness(personBO);

						context.getFlowScope().put(SUMMONS, summonsBO);
					}
				}

			}

		} catch (NumberFormatException e) {
			addErrorMessageText(context, "Invalid Trn");
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
		} catch (Exception e) {
			e.printStackTrace();
			addErrorMessageText(context, "TRN is invalid.");
			return error();
		}
		return success();
	}

	/*public void getPrisonDuration() {

		//RequestContext context = org.springframework.webflow.execution.RequestContextHolder.getRequestContext();
		SummonsBO summonsBO = (SummonsBO)  RequestContextHolder
				.getRequestContext().getFlowScope().get(SUMMONS);
		if(summonsBO != null){
			if(summonsBO.getCourtCase().getPrisonSentenceStartDate() != null && summonsBO.getCourtCase().getPrisonSentenceEndDate() != null){
				Date start = DateUtils.getXMLGregorianCalendarAsDate(summonsBO.getCourtCase().getPrisonSentenceStartDate());
				Date end = DateUtils.getXMLGregorianCalendarAsDate(summonsBO.getCourtCase().getPrisonSentenceEndDate());
			
				calculateDuration(start, end);
			}
		}
	}*/
	
	/*public void clearSentenceDate(){
		RequestContext context = RequestContextHolder.getRequestContext();
		context.getFlowScope().put("summons.courtCase.prisonSentenceEndDate", null);
		context.getFlowScope().put("summons.courtCase.prisonSentenceStartDate", null);
		
		context.getFlowScope().put("durationDays", 0);
		context.getFlowScope().put("durationYears", 0);
		context.getFlowScope().put("durationMonths", 0);
		context.getFlowScope().put("durationWeeks", 0);
	}*/
	
	/*public void calculatePrisonDuration() {
		SummonsBO summons = (SummonsBO) RequestContextHolder
				.getRequestContext().getFlowScope().get(this.SUMMONS);


		if(summons.getCourtCase().getPrisonSentenceStartDate() != null && summons.getCourtCase().getPrisonSentenceEndDate() != null){
			
			Date start = summons.getCourtCase().getPrisonSentenceStartDate().toGregorianCalendar().getTime();
			Date end = summons.getCourtCase().getPrisonSentenceEndDate().toGregorianCalendar().getTime();
	
			
//			roadOperation.setActualNumOfDays(getDurationNumOfDays(start, end));
//			roadOperation.setActualNumOfHours(getDurationNumOfHours(start, end));
//			roadOperation.setActualNumOfMinutes(getDurationNumOfMinutes(start, end));		
//	
//			RequestContextHolder.getRequestContext().getFlowScope()
//					.put("operation", roadOperation);
//			
//			RequestContextHolder.getRequestContext().getFlowScope()
//			.put("operationActualNumOfDays", roadOperation.getActualNumOfDays());
//			
//			RequestContextHolder.getRequestContext().getFlowScope()
//			.put("operationActualNumOfHours", roadOperation.getActualNumOfHours());
//			
//			RequestContextHolder.getRequestContext().getFlowScope()
//			.put("operationActualNumOfMinutes", roadOperation.getActualNumOfMinutes());
		}
		
	}

	*/
	
	public void changeCourtParish() {
		RequestContext context = RequestContextHolder.getRequestContext();
		String parish = (String) context.getFlowScope().get("courtParish");
		getCourtsByParish(parish);
	}
	
	public Event cancelUpdateCourt(RequestContext context) {


		this.loadDocumentDetails(context);

		context.getFlowScope().put("modeCC", "updateCA");

		/* context.getFlowScope().put(SUMMONS, "viewCC"); */
		return success();

	}
	
	public Event cancelUpdateCourt(AjaxBehaviorEvent actionEvent) {

		RequestContext context = org.springframework.webflow.execution.RequestContextHolder.getRequestContext();
		this.loadDocumentDetails(context);
		context.getFlowScope().put("modeCC", "updateCA");

		return success();

	}
	
	private DocumentViewBO copyDocumentViewBO(DocumentViewBO docview, DocumentViewBO document){
		try {
			copyFields(docview,document);
		} catch (Exception e) {
			logger.error("Document Manager",e);
		}
		
		//if(document.getListOfOffences()!=null && !document.getListOfOffences().get(0).getClass().equals(OffenceBO.class)){
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
		
		return docview;
	}
	
	private void clearCourtCaseDetails(CourtCaseBO courtCase){
		courtCase.setFineAmount(null);
		courtCase.setSentencePeriodDay(null);
		courtCase.setSentencePeriodMonth(null);
		courtCase.setSentencePeriodYear(null);
		courtCase.setFinePaidFlag(null);
		courtCase.setTimeServedFlag(null);
	}
}
