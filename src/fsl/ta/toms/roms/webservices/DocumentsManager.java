package fsl.ta.toms.roms.webservices;

import java.io.File;
import java.io.Serializable;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletContext;

import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import fsl.ta.toms.roms.bo.AddressBO;
import fsl.ta.toms.roms.bo.CourtAppearanceBO;
import fsl.ta.toms.roms.bo.CourtCaseBO;
import fsl.ta.toms.roms.bo.DocumentBytesBO;
import fsl.ta.toms.roms.bo.DocumentViewBO;
import fsl.ta.toms.roms.bo.DocumentViewBOList;
import fsl.ta.toms.roms.bo.DocumentsToPrintBO;
import fsl.ta.toms.roms.bo.PersonBO;
import fsl.ta.toms.roms.bo.ScannedDocBO;
import fsl.ta.toms.roms.bo.ServeAllDocsRestBO;
import fsl.ta.toms.roms.bo.SummonsBO;
import fsl.ta.toms.roms.bo.WarningNoProsecutionBO;
import fsl.ta.toms.roms.bo.WarningNoticeBO;
import fsl.ta.toms.roms.constants.Constants;
import fsl.ta.toms.roms.constants.Constants.DocumentStatus;
import fsl.ta.toms.roms.constants.Constants.DocumentType;
import fsl.ta.toms.roms.constants.Constants.YesNo;
import fsl.ta.toms.roms.documents.view.NoticeView;
import fsl.ta.toms.roms.documents.view.SummonsDischargeAndReleaseFormView;
import fsl.ta.toms.roms.documents.view.SummonsView;
import fsl.ta.toms.roms.exception.ErrorSavingException;
import fsl.ta.toms.roms.exception.InvalidFieldException;
import fsl.ta.toms.roms.exception.NoRecordFoundException;
import fsl.ta.toms.roms.exception.RequiredFieldMissingException;
import fsl.ta.toms.roms.search.criteria.impl.DocumentsCriteriaBO;
import fsl.ta.toms.roms.service.CourtAppearanceService;
import fsl.ta.toms.roms.service.CourtCaseService;
import fsl.ta.toms.roms.service.ServiceFactory;
import fsl.ta.toms.roms.service.SummonsService;
import fsl.ta.toms.roms.service.WarningNoProsecutionService;
import fsl.ta.toms.roms.service.WarningNoticeService;
import fsl.ta.toms.roms.util.AddressView;
import fsl.ta.toms.roms.util.DateUtils;
import fsl.ta.toms.roms.util.GenericUtil;
import fsl.ta.toms.roms.util.StringUtil;


/**
 * 
 * @author jreid Created Date: Dec 10, 2013
 */

@Controller
@RequestMapping("/roms_rest/docMan")
public class DocumentsManager extends SpringBeanAutowiringSupport {
	private static String SUMMONS_PAGE_1 = "WEB-INF/documentTemplates/summons.jasper";// "WEB-INF/documentTemplates/SummonsPage1.jasper";
	private static String WARNING_NOTICE = "WEB-INF/documentTemplates/WarningNotice.jasper";
	private static String WARNING_NOTICE_NO_PROSECUTION = "WEB-INF/documentTemplates/WarningNoProsecution.jasper";
	private static int NOTICE_COPIES = 6;
	
	@Autowired
	private ServiceFactory	serviceFactory;
	
  @Autowired
    private ServletContext servletContext;

	/*******************
	 * SEARCH
	 * 
	 * @throws RequiredFieldMissingException
	 ******************************/
	public List<DocumentViewBO> search(DocumentsCriteriaBO criteria) throws RequiredFieldMissingException {

		List<DocumentViewBO> documents = new ArrayList<DocumentViewBO>();

		if (criteria == null)
			throw new RequiredFieldMissingException("Search Details is required");

		if (criteria.getDocumentType() != null) {

			if (criteria.getDocumentType().equalsIgnoreCase(Constants.DocumentType.SUMMONS)) {
				documents = serviceFactory.getSummonsService().lookupSummons(criteria);
			} else

			if (criteria.getDocumentType().equalsIgnoreCase(Constants.DocumentType.WARNING_NOTICE)) {
				documents = serviceFactory.getWarningNoticeService().lookupWarningNotice(criteria);
			} else

			if (criteria.getDocumentType().equalsIgnoreCase(Constants.DocumentType.WARNING_NOTICE_NO_PROSECUTION)) {
				documents = serviceFactory.getWarningNoProsecutionService().lookupWarningNoProsecution(criteria);
			} else {

				List<DocumentViewBO> tempDocuments = null;

				
				try {
					// add all warning no prosecution
					tempDocuments = serviceFactory.getWarningNoProsecutionService().lookupWarningNoProsecution(criteria);
				} catch (Exception e) {
					e.printStackTrace();

				}

				if (tempDocuments != null)
					documents.addAll(tempDocuments);
				
				
				try {
					tempDocuments = serviceFactory.getWarningNoticeService().lookupWarningNotice(criteria);
				} catch (Exception e) {
					e.printStackTrace();
				}

				// ass warning notices
				if (tempDocuments != null)
					documents.addAll(tempDocuments);

				try {
					// add all summons
					tempDocuments = serviceFactory.getSummonsService().lookupSummons(criteria);
				} catch (Exception e) {
					e.printStackTrace();
				}

				if (tempDocuments != null)
					documents.addAll(tempDocuments);

				

			}
		}

		return documents;
	}

	/**
	 * 
	 * @param warningNotice
	 * @return
	 * @throws ErrorSavingException
	 * @throws RequiredFieldMissingException
	 */
	public Integer createWarningNotice(WarningNoticeBO warningNotice) throws ErrorSavingException, RequiredFieldMissingException {

		WarningNoticeService warningNoticeService = serviceFactory.getWarningNoticeService();

		if (warningNotice == null)
			throw new ErrorSavingException("Warning Notice details cannot be empty.");

		// System.err.println("warningNotice.getRoadOperationId():: " +
		// warningNotice.getRoadOperationId());

		/*
		 * if (warningNotice.getRoadOperationId() == null) throw new
		 * RequiredFieldMissingException( "Road Operation Id is required");
		 */
		if (warningNotice.getRoadCheckOffenceOutcomeCode() == null)
			throw new RequiredFieldMissingException("Road Check Offence Outcome Code is required");

		/*
		 * if (warningNotice.getIsManualWarningNotice() != null &&
		 * warningNotice.getIsManualWarningNotice().booleanValue() == true &&
		 * warningNotice.getManualSerialNo() == null) throw new
		 * RequiredFieldMissingException( "Manual Serial No is required.");
		 */
		if (StringUtils.isBlank(warningNotice.getTaStaffId()))
			throw new RequiredFieldMissingException("Staff Id is required.");

		if (warningNotice.getPoundId() == null)
			throw new RequiredFieldMissingException("Pound Id is required.");

//		if (warningNotice.getWreckingCompanyId() == null)
//			throw new RequiredFieldMissingException("Wrecking Company is required.");

		if (warningNotice.getOffenderId() == null)
			throw new RequiredFieldMissingException("Offender Id is required");

		if (StringUtils.isBlank(warningNotice.getTaStaffId()))
			throw new RequiredFieldMissingException("TA Staff Id is required");

		if (StringUtils.isBlank(warningNotice.getCurrentUsername()))
			throw new RequiredFieldMissingException("Add/Update User is required.");

		if (StringUtils.isBlank(warningNotice.getStatusId()))
			throw new RequiredFieldMissingException("Status Code is required.");

		Integer serializable = null;
		try {
			serializable = warningNoticeService.saveWarningNotice(warningNotice);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ErrorSavingException("Error saving warningNotice.");
		}

		return serializable;
	}

	/**
	 * 
	 * @param criteria
	 * @return
	 * @throws NoRecordFoundException
	 * @throws RequiredFieldMissingException
	 */
	public ArrayList<DocumentViewBO> lookupWarningNotice(DocumentsCriteriaBO criteria) throws NoRecordFoundException, RequiredFieldMissingException {

		WarningNoticeService warningNoticeService = serviceFactory.getWarningNoticeService();

		if (criteria == null)
			throw new RequiredFieldMissingException("Please enter a search criteria.");

		ArrayList<DocumentViewBO> listOfwarningNotice = warningNoticeService.lookupWarningNotice(criteria);

		if (listOfwarningNotice == null || listOfwarningNotice.isEmpty())
			throw new NoRecordFoundException("No Records Found.");

		return listOfwarningNotice;
	}

	/**
	 * 
	 * @param warningNotice
	 * @param statusCode
	 * @throws ErrorSavingException
	 * @throws RequiredFieldMissingException
	 */
	public WarningNoticeBO updateWarningNotice(WarningNoticeBO warningNotice) throws ErrorSavingException, RequiredFieldMissingException {

		WarningNoticeService warningNoticeService = serviceFactory.getWarningNoticeService();

		// double check for empty fields
		if (warningNotice == null)
			throw new ErrorSavingException("Warning Notice details cannot be empty");

		if (warningNotice.getAutomaticSerialNo() == null)
			throw new RequiredFieldMissingException("Summons Id is required");

		if (warningNotice.getRoadCheckOffenceOutcomeCode() == null)
			throw new RequiredFieldMissingException("Road Check Offence Outcome Code is required");

		if (warningNotice.getOrigin() != null && warningNotice.getOrigin().equalsIgnoreCase(DocumentType.ORIGIN_MANUAL) && warningNotice.getManualSerialNo() == null)
			throw new RequiredFieldMissingException("Manual Serial No is required.");

		if (warningNotice.getOffenderId() == null)
			throw new RequiredFieldMissingException("Offender Id is required");

		if (warningNotice.getTaStaffId() == null)
			throw new RequiredFieldMissingException("TA Staff Id is required");

		if (warningNotice.getStatusId() == null)
			throw new RequiredFieldMissingException("Status Code is required.");

		if (StringUtils.isBlank(warningNotice.getCurrentUsername()))
			throw new RequiredFieldMissingException("Add/Update User is required.");

		// logics
		// general modification
		// can't alter except cancel if already printed
		/*
		 * if (!warningNotice.getNewStatusId().equals(
		 * Constants.DocumentStatus.CANCELLED) &&
		 * !warningNotice.getNewStatusId().equals(
		 * Constants.DocumentStatus.SERVED) &&
		 * (warningNotice.getStatusId().equals(
		 * Constants.DocumentStatus.PRINTED) || warningNotice
		 * .getStatusId().equals( Constants.DocumentStatus.REPRINTED))) { throw
		 * new ErrorSavingException(
		 * "Warning No  already printed and cannot be modified."); }
		 */

		// printing
		if (warningNotice.getNewStatusId().equals(Constants.DocumentStatus.PRINTED) && StringUtils.isNotBlank(warningNotice.getManualSerialNo())) {
			throw new ErrorSavingException("Manual Warning Notice cannot be printed.");
		}

		/*
		 * if (warningNotice.getNewStatusId().equals(
		 * Constants.DocumentStatus.REPRINTED) && warningNotice.getReasonCode()
		 * == null) { throw new
		 * ErrorSavingException("Reason is required for reprint."); }
		 */

		// cancellation
		if (warningNotice.getNewStatusId().equals(Constants.DocumentStatus.CANCELLED) && warningNotice.getReasonCode() == null) {
			throw new ErrorSavingException("Reason is required for cancellation.");
		}

		if (warningNotice.getNewStatusId().equals(Constants.DocumentStatus.CANCELLED) && StringUtils.isBlank(warningNotice.getNewComment())) {
			throw new ErrorSavingException("Comment is required for cancellation.");
		}

		if (warningNotice.getNewStatusId().equals(Constants.DocumentStatus.DENY_AUTHORISATION) && StringUtils.isBlank(warningNotice.getNewComment())) {
			throw new ErrorSavingException("Comment is required when rejecting edits.");
		}

		try {
			warningNotice = warningNoticeService.updateWarningNotice(warningNotice);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ErrorSavingException("Error updating Warning Notice.");
		}

		return warningNotice;
	}

	/**
	 * GENERATES WARNING NOTICE FORM DETAILS FOR PRINT
	 * 
	 * @param warningNoProsecution
	 * @return NoticeView
	 * @throws RequiredFieldMissingException
	 */
	public NoticeView generateWarningNoticeForm(WarningNoticeBO warningNotice) throws RequiredFieldMissingException {

		// System.err.println(" web service is calledd22");
		WarningNoticeService warningNoticeService = serviceFactory.getWarningNoticeService();

		// double check for empty fields
		if (warningNotice == null)
			throw new RequiredFieldMissingException("Warning No Prosecution details cannot be empty");

		if (warningNotice.getAutomaticSerialNo() == null)
			throw new RequiredFieldMissingException("warningNotice Id is required");

		if (warningNotice.getRoadCheckOffenceOutcomeCode() == null)
			throw new RequiredFieldMissingException("Road Check Offence Outcome Code is required");

		// if this warningNotice is a manual warningNotice then Id should not be
		// empty
		/*
		 * if (warningNotice.getIsManualWarningNotice() &&
		 * warningNotice.getManualSerialNo() == null) throw new
		 * RequiredFieldMissingException( "Manual Serial No is required.");
		 */
		if (warningNotice.getOffenderId() == null)
			throw new RequiredFieldMissingException("Offender Id is required");

		if (warningNotice.getTaStaffId() == null)
			throw new RequiredFieldMissingException("TA Staff Id is required");

		if (warningNotice.getOffenceDtime() == null)
			throw new RequiredFieldMissingException("Offence Date is required");

		if (StringUtils.isBlank(warningNotice.getCurrentUsername()))
			throw new RequiredFieldMissingException("Add/Update User is required.");

		if (warningNotice.getStatusId() == null)
			throw new RequiredFieldMissingException("Status Code is required.");

		NoticeView warningNoticeView = null;

		// logics
		// general modification
		// if teh warningNotice is in one of these states, it cannot be modified
		if (warningNotice.getStatusId().equals(Constants.DocumentStatus.CANCELLED) || warningNotice.getStatusId().equals(Constants.DocumentStatus.WITHDRAWN)
			|| warningNotice.getStatusId().equals(Constants.DocumentStatus.SERVED) || warningNotice.getStatusId().equals(Constants.DocumentStatus.DISCHARGED_FORM_PRINTED)) {
			throw new RequiredFieldMissingException("Warning No Prosecution is " + warningNotice.getStatusDescription() + " and cannot be printed.");
		} else {
			try {
//				if (warningNotice.getPrintCount() == null || warningNotice.getPrintCount().intValue() == 0) {
//
//					warningNoticeView = warningNoticeService.generateWarningNotice(warningNotice);
//				} else {
//					warningNoticeView = warningNoticeService.reGenerateWarningNotice(warningNotice);
//				}
				warningNoticeView = warningNoticeService.getNoticeView(warningNotice);

			} catch (Exception e) {
				e.printStackTrace();
				throw new RequiredFieldMissingException(e.getMessage());
			}
		}
		return warningNoticeView;

	}

	/**
	 * This method saves the uploaded warningNotice documents
	 * 
	 * @param scannedDocList
	 * @throws ErrorSavingException
	 */
	public void processScannedWarningNoticeDocuments(List<ScannedDocBO> scannedDocList) throws ErrorSavingException {

		WarningNoticeService warningNoticeService = serviceFactory.getWarningNoticeService();

		if (scannedDocList == null || scannedDocList.isEmpty())
			throw new ErrorSavingException("No scanned document have been selected.");

		try {
			warningNoticeService.processScannedWarningNoticeDocuments(scannedDocList);

		} catch (Exception e) {
			e.printStackTrace();
			throw new ErrorSavingException("Error updating Warning Notice.");
		}
	}

	/**
	 * @throws RequiredFieldMissingException
	 * @throws ErrorSavingException
	 */
	public WarningNoticeBO getWarningNoticeDetails(Integer warningNoticeId) throws NoRecordFoundException, RequiredFieldMissingException, ErrorSavingException {
		WarningNoticeService warningNoticeService = serviceFactory.getWarningNoticeService();
		WarningNoticeBO warningNoticeDetails = null;

		if (warningNoticeId == null)
			throw new RequiredFieldMissingException("Warning Notice Id is required.");

		try {
			warningNoticeDetails = warningNoticeService.getWarningNoticeDetailsById(warningNoticeId);

			if (warningNoticeDetails == null)
				throw new NoRecordFoundException("No Records Found.");

		} catch (Exception e) {
			e.printStackTrace();
			throw new ErrorSavingException("Error retrieving warning notice.");
		}
		
		return warningNoticeDetails;
	}

	/**
	 * 
	 * @param warningNoProsecution
	 * @return
	 * @throws ErrorSavingException
	 * @throws RequiredFieldMissingException
	 */
	public Integer createWarningNoProsecution(WarningNoProsecutionBO warningNoProsecution) throws ErrorSavingException, RequiredFieldMissingException {

		WarningNoProsecutionService warningNoProsecutionService = serviceFactory.getWarningNoProsecutionService();

		if (warningNoProsecution == null)
			throw new ErrorSavingException("Warning No Prosecution details cannot be empty");

		/*
		 * if (warningNoProsecution.getRoadOperationId() == null) throw new
		 * RequiredFieldMissingException( "Road COperation Id is required");
		 */
		if (warningNoProsecution.getRoadCheckOffenceOutcomeCode() == null)
			throw new RequiredFieldMissingException("Road Check Offence Outcome Code is required");

		/*
		 * if (warningNoProsecution.getIsManualWarningNoProsecution() &&
		 * warningNoProsecution.getManualSerialNo() == null) throw new
		 * RequiredFieldMissingException( "Manual Serial No is required.");
		 */
		if (!StringUtil.isSet(warningNoProsecution.getTaStaffId()))
			throw new RequiredFieldMissingException("Staff Id is required.");

		if (warningNoProsecution.getOffenderId() == null)
			throw new RequiredFieldMissingException("Offender Id is required");

		if (!StringUtil.isSet(warningNoProsecution.getTaStaffId()))
			throw new RequiredFieldMissingException("TA Staff Id is required");

		if (!StringUtil.isSet(warningNoProsecution.getCurrentUsername()))
			throw new RequiredFieldMissingException("Add/Update User is required.");

		if (!StringUtil.isSet(warningNoProsecution.getStatusId()))
			throw new RequiredFieldMissingException("Status Code is required.");

		if (warningNoProsecution.getOffenceDtime() == null)
			throw new RequiredFieldMissingException("Offence Date Time is required.");

		Integer serializable = null;

		try {
			serializable = warningNoProsecutionService.saveWarningNoProsecution(warningNoProsecution);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ErrorSavingException("Error saving warningNoProsecution.");
		}

		return serializable;
	}

	/**
	 * 
	 * @param criteria
	 * @return
	 * @throws NoRecordFoundException
	 * @throws RequiredFieldMissingException
	 */
	public ArrayList<DocumentViewBO> lookupWarningNoProsecution(DocumentsCriteriaBO criteria) throws NoRecordFoundException, RequiredFieldMissingException {

		WarningNoProsecutionService warningNoProsecutionService = serviceFactory.getWarningNoProsecutionService();

		if (criteria == null)
			throw new RequiredFieldMissingException("Please enter a search criteria.");

		ArrayList<DocumentViewBO> listOfwarningNoProsecution = warningNoProsecutionService.lookupWarningNoProsecution(criteria);

		if (listOfwarningNoProsecution == null || listOfwarningNoProsecution.isEmpty())
			throw new NoRecordFoundException("No Records Found.");

		return listOfwarningNoProsecution;
	}

	/**
	 * 
	 * @param warningNoProsecution
	 * @param statusCode
	 * @throws ErrorSavingException
	 * @throws RequiredFieldMissingException
	 */
	public WarningNoProsecutionBO updateWarningNoProsecution(WarningNoProsecutionBO warningNoProsecution) throws ErrorSavingException, RequiredFieldMissingException {

		WarningNoProsecutionService warningNoProsecutionService = serviceFactory.getWarningNoProsecutionService();

		// double check for empty fields
		if (warningNoProsecution == null)
			throw new ErrorSavingException("Warning No Prosecution details cannot be empty");

		if (warningNoProsecution.getAutomaticSerialNo() == null)
			throw new RequiredFieldMissingException("Warning No Prosecution Id is required");

		if (warningNoProsecution.getRoadCheckOffenceOutcomeCode() == null)
			throw new RequiredFieldMissingException("Road Check Offence Outcome Code is required");

		if (warningNoProsecution.getOrigin() != null && warningNoProsecution.getOrigin().equalsIgnoreCase(DocumentType.ORIGIN_MANUAL) && warningNoProsecution.getManualSerialNo() == null)
			throw new RequiredFieldMissingException("Manual Reference No is required.");

		if (warningNoProsecution.getOffenderId() == null)
			throw new RequiredFieldMissingException("Offender Id is required");

		if (warningNoProsecution.getTaStaffId() == null)
			throw new RequiredFieldMissingException("TA Staff Id is required");

		if (warningNoProsecution.getStatusId() == null)
			throw new RequiredFieldMissingException("Status Code is required.");

		if (StringUtils.isBlank(warningNoProsecution.getCurrentUsername()))
			throw new RequiredFieldMissingException("Add/Update User is required.");

		if (warningNoProsecution.getOffenceDtime() == null)
			throw new RequiredFieldMissingException("Offence Date Time is required.");

		// logics
		// general modification
		// can't alter except cancel if already printed
		/*
		 * if (!warningNoProsecution.getStatusId().equals(
		 * Constants.DocumentStatus.CANCELLED) &&
		 * !warningNoProsecution.getNewStatusId().equals(
		 * Constants.DocumentStatus.SERVED) &&
		 * (warningNoProsecution.getStatusId().equals(
		 * Constants.DocumentStatus.PRINTED) || warningNoProsecution
		 * .getStatusId().equals( Constants.DocumentStatus.REPRINTED))) { throw
		 * new ErrorSavingException(
		 * "Warning No Prosecution already printed and cannot be modified."); }
		 */

		// printing
		if (warningNoProsecution.getNewStatusId().equals(Constants.DocumentStatus.PRINTED) && StringUtils.isNotBlank(warningNoProsecution.getManualSerialNo())) {
			throw new ErrorSavingException("Manual Warning No Prosecutions cannot be printed.");
		}

		/*
		 * if (warningNoProsecution.getNewStatusId().equals(
		 * Constants.DocumentStatus.REPRINTED) &&
		 * warningNoProsecution.getReasonCode() == null) { throw new
		 * ErrorSavingException("Reason is required for reprint."); }
		 */

		// cancellation
		if (warningNoProsecution.getNewStatusId().equals(Constants.DocumentStatus.CANCELLED) && warningNoProsecution.getReasonCode() == null) {
			throw new ErrorSavingException("Reason is required for cancellation.");
		}

		if (warningNoProsecution.getNewStatusId().equals(Constants.DocumentStatus.CANCELLED) && StringUtils.isBlank(warningNoProsecution.getNewComment())) {
			throw new ErrorSavingException("Comment is required for cancellation.");
		}

		if (warningNoProsecution.getNewStatusId().equals(Constants.DocumentStatus.DENY_AUTHORISATION) && StringUtils.isBlank(warningNoProsecution.getNewComment())) {
			throw new ErrorSavingException("Comment is required when rejecting edits.");
		}

		try {
			warningNoProsecution = warningNoProsecutionService.updateWarningNoProsecution(warningNoProsecution);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ErrorSavingException("Error updating Warning Notice No Prosecution.");
		}

		return warningNoProsecution;
	}

	/**
	 * GENERATES WARNING NO PROSECUTION FORM DETAILS FOR PRINT
	 * 
	 * @param warningNoProsecution
	 * @return NoticeView
	 * @throws RequiredFieldMissingException
	 */
	public NoticeView generateWarningNoProsecutionForm(WarningNoProsecutionBO warningNoProsecution) throws RequiredFieldMissingException {
		// System.err.println(" web service is calledd");

		WarningNoProsecutionService warningNoProsecutionService = serviceFactory.getWarningNoProsecutionService();

		// double check for empty fields
		if (warningNoProsecution == null)
			throw new RequiredFieldMissingException("Warning No Prosecution details cannot be empty");

		if (warningNoProsecution.getAutomaticSerialNo() == null)
			throw new RequiredFieldMissingException("warningNoProsecution Id is required");

		if (warningNoProsecution.getRoadCheckOffenceOutcomeCode() == null)
			throw new RequiredFieldMissingException("Road Check Offence Outcome Code is required");

		// if this warningNoProsecution is a manual warningNoProsecution then Id
		// should not be empty
		/*
		 * if (warningNoProsecution.getIsManualWarningNoProsecution() != null &&
		 * warningNoProsecution.getIsManualWarningNoProsecution()
		 * .booleanValue() == true && warningNoProsecution.getManualSerialNo()
		 * == null) throw new RequiredFieldMissingException(
		 * "Manual Serial No is required.");
		 */
		if (warningNoProsecution.getOffenderId() == null)
			throw new RequiredFieldMissingException("Offender Id is required");

		if (warningNoProsecution.getTaStaffId() == null)
			throw new RequiredFieldMissingException("TA Staff Id is required");

		if (warningNoProsecution.getOffenceDtime() == null)
			throw new RequiredFieldMissingException("Offence Date is required");

		if (StringUtils.isBlank(warningNoProsecution.getCurrentUsername()))
			throw new RequiredFieldMissingException("Add/Update User is required.");

		if (warningNoProsecution.getStatusId() == null)
			throw new RequiredFieldMissingException("Status Code is required.");

		NoticeView warningNoProsecutionView = null;

		// logics
		// general modification
		// if teh warningNoProsecution is in one of these states, it cannot be
		// modified
		if (warningNoProsecution.getStatusId().equals(Constants.DocumentStatus.CANCELLED) || warningNoProsecution.getStatusId().equals(Constants.DocumentStatus.WITHDRAWN)
			|| warningNoProsecution.getStatusId().equals(Constants.DocumentStatus.SERVED) || warningNoProsecution.getStatusId().equals(Constants.DocumentStatus.DISCHARGED_FORM_PRINTED)) {
			throw new RequiredFieldMissingException("Warning No Prosecution is " + warningNoProsecution.getStatusDescription() + " and cannot be printed.");
		} else {
			try {
//				if (warningNoProsecution.getPrintCount() == null || warningNoProsecution.getPrintCount().intValue() == 0) {
//
//					warningNoProsecutionView = warningNoProsecutionService.generateWarningNoProsecution(warningNoProsecution);
//				} else {
//					warningNoProsecutionView = warningNoProsecutionService.reGenerateWarningNoProsecution(warningNoProsecution);
//				}
				warningNoProsecutionView = warningNoProsecutionService.getNoticeView(warningNoProsecution);

			} catch (Exception e) {
				e.printStackTrace();
				throw new RequiredFieldMissingException(e.getMessage());
			}
		}
		return warningNoProsecutionView;

	}

	/**
	 * This method saves the uploaded warningNotice documents
	 * 
	 * @param scannedDocList
	 * @throws ErrorSavingException
	 */
	public void processScannedWarningNoProsecutionDocuments(List<ScannedDocBO> scannedDocList) throws ErrorSavingException {

		WarningNoProsecutionService warningNoProsecutionService = serviceFactory.getWarningNoProsecutionService();

		if (scannedDocList == null || scannedDocList.isEmpty())
			throw new ErrorSavingException("No scanned document have been selected.");

		try {
			warningNoProsecutionService.processScannedWarningNoProsecutionDocuments(scannedDocList);

		} catch (Exception e) {
			e.printStackTrace();
			throw new ErrorSavingException("Error updating Warning Notice No Prosecution.");
		}
	}

	/**
	 * @throws RequiredFieldMissingException
	 * @throws ErrorSavingException
	 */
	public WarningNoProsecutionBO getWarningNoProsecutionDetails(Integer warningNoProsecutionId) throws NoRecordFoundException, RequiredFieldMissingException, ErrorSavingException {
		WarningNoProsecutionService warningNoProsecutionService = serviceFactory.getWarningNoProsecutionService();
		WarningNoProsecutionBO warningNoProsecutionDetails = null;

		if (warningNoProsecutionId == null)
			throw new RequiredFieldMissingException("Warning No Prosecution Id is required.");

		try {
			warningNoProsecutionDetails = warningNoProsecutionService.getWarningNoProsecutionDetailsById(warningNoProsecutionId);

			if (warningNoProsecutionDetails == null)
				throw new NoRecordFoundException("No Records Found.");

		} catch (Exception e) {
			e.printStackTrace();
			throw new ErrorSavingException("Error retrieving Warning No Prosecution.");
		}
		return warningNoProsecutionDetails;
	}

	/********************************************************************
 * *******************************************************************/

	/**
	 * This method is the interface of the webservice and it is what is called
	 * to create a summons
	 * 
	 * @param summons
	 * @return
	 * @throws ErrorSavingException
	 * @throws RequiredFieldMissingException
	 */
	public SummonsBO createSummons(SummonsBO summons) throws ErrorSavingException, RequiredFieldMissingException {

		SummonsService summonsService = serviceFactory.getSummonsService();

		if (summons == null)
			throw new ErrorSavingException("Summons details cannot be empty");

		/*
		 * if (summons.getRoadOperationId() == null) throw new
		 * RequiredFieldMissingException( "Road Operation Id is required");
		 */
		if (summons.getRoadCheckOffenceOutcomeCode() == null)
			throw new RequiredFieldMissingException("Road Check Offence Outcome Code is required");

		// if this is a manual summons and the Serial Number is empty
		/*
		 * if (summons.isManualSummons() && summons.getManualSerialNo() == null)
		 * throw new RequiredFieldMissingException(
		 * "Manual Serial No is required.");
		 */
		if (summons.getOffenderId() == null)
			throw new RequiredFieldMissingException("Offender Id is required");

		if (summons.getCourtCode() == null)
			throw new RequiredFieldMissingException("Court is required");

		/************************* COURT DATE *************************/
		// court date cannot be in future
		if (summons.getCourtDtime() == null)
			throw new RequiredFieldMissingException("Court Date is required");

		if (summons.getCourtDtime() != null && summons.getOffenceDtime() != null) {
			try {
				DateUtils.validateCourtDate(summons.getCourtDtime(), summons.getOffenceDtime());
			} catch (InvalidFieldException e) {

				e.printStackTrace();
				throw new RequiredFieldMissingException(e.getMessage());
			}
		}

		/******************** end court date *************************/

		if (StringUtils.isBlank(summons.getTaStaffId()))
			throw new RequiredFieldMissingException("TA Staff Id is required");

		// System.err.println("'" + summons.getTaStaffId() + "'");

		/*
		 * if (summons.getJpRegNumber() == null) throw new
		 * RequiredFieldMissingException(
		 * "Justice of the Peace Reg. No. is required");
		 */

		if (summons.getOffenceDtime() == null)
			throw new RequiredFieldMissingException("Offence Date is required");

		if (StringUtils.isBlank(summons.getCurrentUsername()))
			throw new RequiredFieldMissingException("Add/Update User is required.");

		if (StringUtils.isBlank(summons.getStatusId()))
			throw new RequiredFieldMissingException("Status Code is required.");

		// save the id after the object is saved
		Integer serializable = null;
		try {

			serializable = summonsService.saveSummons(summons);
			summons.setAutomaticSerialNo(serializable);

		} catch (Exception e) {
			e.printStackTrace();
			throw new ErrorSavingException(e.getMessage());
		}

		return summons;
	}

	/**
	 * Interface to look for summons based on the criteria
	 * 
	 * @param criteria
	 * @return
	 * @throws NoRecordFoundException
	 * @throws RequiredFieldMissingException
	 * @throws ErrorSavingException
	 */
	public ArrayList<DocumentViewBO> lookupSummons(DocumentsCriteriaBO criteria) throws NoRecordFoundException, RequiredFieldMissingException, ErrorSavingException {

		try {
			SummonsService summonsService = serviceFactory.getSummonsService();

			if (criteria == null)
				throw new RequiredFieldMissingException("Please enter a search criteria.");

			ArrayList<DocumentViewBO> listOfsummons = summonsService.lookupSummons(criteria);

			if (listOfsummons == null || listOfsummons.isEmpty())
				throw new NoRecordFoundException("No Records Found.");

			return listOfsummons;

		} catch (Exception e) {
			e.printStackTrace();
			throw new NoRecordFoundException(e.getMessage());
		}

	}

	/**
	 * Update the summons based on status code sent forward
	 * 
	 * @param summons
	 * @param statusCode
	 * @throws ErrorSavingException
	 * @throws RequiredFieldMissingException
	 */
	public SummonsBO updateSummons(SummonsBO summons) throws ErrorSavingException, RequiredFieldMissingException {

		SummonsService summonsService = serviceFactory.getSummonsService();

		// double check for empty fields
		if (summons == null)
			throw new ErrorSavingException("Summons details cannot be empty");

		if (summons.getAutomaticSerialNo() == null)
			throw new RequiredFieldMissingException("Summons Id is required");

		if (summons.getRoadCheckOffenceOutcomeCode() == null)
			throw new RequiredFieldMissingException("Road Check Offence Outcome Code is required");

		// if this summons is a manual summons then Id should not be empty
		if (summons.getOrigin() != null && summons.getOrigin().equalsIgnoreCase(DocumentType.ORIGIN_MANUAL) && summons.getManualSerialNo() == null)
			throw new RequiredFieldMissingException("Manual Serial No is required.");

		if (summons.getOffenderId() == null)
			throw new RequiredFieldMissingException("Offender Id is required");

		if (summons.getTaStaffId() == null)
			throw new RequiredFieldMissingException("TA Staff Id is required");

		/*
		 * if (summons.getJpRegNumber() == null) throw new
		 * RequiredFieldMissingException(
		 * "Justice of the Peace Reg. No. is required");
		 */
		if (summons.getOffenceDtime() == null)
			throw new RequiredFieldMissingException("Offence Date is required");

		if (StringUtils.isBlank(summons.getCurrentUsername()))
			throw new RequiredFieldMissingException("Add/Update User is required.");

		if (summons.getStatusId() == null)
			throw new RequiredFieldMissingException("Status Code is required.");

		if (summons.getNewStatusId() == null)
			throw new RequiredFieldMissingException("Update Status Code is required.");

		if (StringUtils.isBlank(summons.getCurrentUsername()))
			throw new RequiredFieldMissingException("User Name is required.");

		// logics
		// general modification
		// if teh summons is not in one of these states, it cannot be modified
		if (summons.getStatusId().equals(Constants.DocumentStatus.CANCELLED) || summons.getStatusId().equals(Constants.DocumentStatus.WITHDRAWN)) {
			throw new ErrorSavingException("Summons is " + summons.getStatusDescription() + " and cannot be updated.");

		}

		// can't alter except CANCEL or REPRINT if already printed
		/*
		 * if (!summons.getNewStatusId()
		 * .equals(Constants.DocumentStatus.WITHDRAWN) &&
		 * !summons.getNewStatusId().equals( Constants.DocumentStatus.SERVED) &&
		 * !summons.getNewStatusId().equals( Constants.DocumentStatus.SERVED) &&
		 * (summons.getStatusId().equals( Constants.DocumentStatus.REPRINTED) ||
		 * summons .getStatusId().equals(Constants.DocumentStatus.PRINTED))) {
		 * throw new ErrorSavingException(
		 * "Summons already printed and cannot be modified."); }
		 */

		// printing
		if (summons.getNewStatusId().equals(Constants.DocumentStatus.PRINTED) && summons.getManualSerialNo() != null) {
			throw new ErrorSavingException("Manual Summons cannot be generated. Please print uploaded copy.");
		}

		// reprinting
		if (summons.getNewStatusId().equals(Constants.DocumentStatus.REPRINTED) && (!summons.getStatusId().equals(Constants.DocumentStatus.PRINTED))) {
			throw new ErrorSavingException("Summons cannot be reprinted from this state.");
		}
		/*
		 * if
		 * (summons.getNewStatusId().equals(Constants.DocumentStatus.REPRINTED)
		 * && summons.getReasonCode() == null) { throw new
		 * ErrorSavingException("Reason is required for reprint."); }
		 */

		// authorisation
		if (summons.isJPPinAuthorised() && summons.getJpIdNumber() == null) {
			throw new ErrorSavingException("JP Pin is required for authorisation.");
		}

		// cancellation
		if (summons.getNewStatusId().equals(Constants.DocumentStatus.CANCELLED)) {
			if (summons.getReasonCode() == null) {
				throw new ErrorSavingException("Reason is required for cancellation.");
			}

			if (StringUtils.isBlank(summons.getNewComment())) {
				throw new ErrorSavingException("Comment is required for cancellation.");
			}

		}

		// withdraw summons
		if (summons.getNewStatusId().equals(Constants.DocumentStatus.WITHDRAWN)) {
			if (summons.getDischargeWitness() == null) {
				throw new ErrorSavingException("Discharge Witness is required for withdrawing summons.");
			} else {
				if (StringUtils.isBlank(summons.getDischargeWitness().getLastName())) {
					throw new RequiredFieldMissingException("Witness Last Name is required for withdrawing summons.");
				}
			}
			if (summons.getReasonCode() == null) {
				throw new ErrorSavingException("Reason for Withdrawal is required for withdrawing summons.");
			}

			if (StringUtils.isBlank(summons.getComment())) {
				throw new ErrorSavingException("Comment is required for withdrawing summons.");
			}
		}

		// serve summons
		if (summons.getNewStatusId().equals(Constants.DocumentStatus.SERVED)) {
			if (summons.getServedByUserID() == null) {
				throw new ErrorSavingException("Served By User is required for serving summons.");
			}
			if (summons.getServedOnDate() == null) {
				throw new ErrorSavingException("Served Date is required for serving summons.");
			}
			//System.err.println(" date in web service : " + summons.getServedOnDate());

			if (StringUtils.isBlank(summons.getServedAtParish())) {
				throw new ErrorSavingException("Served parish is required for serving summons.");
			}
			if (StringUtils.isBlank(summons.getServedAtAddress())) {
				throw new ErrorSavingException("Served Address is required for serving summons.");
			}
		}

		if (summons.getNewStatusId().equals(Constants.DocumentStatus.DENY_AUTHORISATION) && StringUtils.isBlank(summons.getNewComment())) {
			throw new ErrorSavingException("Comment is required when rejecting edits.");
		}

		/*
		 * if(summons.getStatusId().equals(Constants.Status.Dis) &&
		 * summons.getComment() == null) { throw new
		 * ErrorSavingException("Comment is required for cancellation."); }
		 */

		// summons.setStatusId(statusCode);

		try {
			summons = summonsService.updateSummons(summons);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ErrorSavingException(e.getMessage());
		}

		return summons;
	}

	/**
	 * GENERATES DISCHARGE FORM DETAILS FOR PRINT
	 * 
	 * @param summons
	 * @return
	 * @throws RequiredFieldMissingException
	 * @throws ErrorSavingException
	 */
	public SummonsDischargeAndReleaseFormView withdrawSummons(SummonsBO summons) throws RequiredFieldMissingException, ErrorSavingException {

		SummonsService summonsService = serviceFactory.getSummonsService();

		// double check for empty fields
		if (summons == null)
			throw new RequiredFieldMissingException("Summons details cannot be empty");

		if (summons.getAutomaticSerialNo() == null)
			throw new RequiredFieldMissingException("Summons Id is required");

		if (summons.getRoadCheckOffenceOutcomeCode() == null)
			throw new RequiredFieldMissingException("Road Check Offence Outcome Code is required");

		// if this summons is a manual summons then Id should not be empty
		/*
		 * if (summons.isManualSummons() && summons.getManualSerialNo() == null)
		 * throw new RequiredFieldMissingException(
		 * "Manual Serial No is required.");
		 */
		if (summons.getOffenderId() == null)
			throw new RequiredFieldMissingException("Offender Id is required");

		if (summons.getTaStaffId() == null)
			throw new RequiredFieldMissingException("TA Staff Id is required");

		/*
		 * if (summons.getJpRegNumber() == null) throw new
		 * RequiredFieldMissingException(
		 * "Justice of the Peace Reg. No. is required");
		 */
		if (summons.getOffenceDtime() == null)
			throw new RequiredFieldMissingException("Offence Date is required");

		if (StringUtils.isBlank(summons.getCurrentUsername()))
			throw new RequiredFieldMissingException("Add/Update User is required.");

		if (summons.getStatusId() == null)
			throw new RequiredFieldMissingException("Status Code is required.");

		/**  **/
		if (summons.getStatusId().equals(Constants.DocumentStatus.CANCELLED) || summons.getStatusId().equals(Constants.DocumentStatus.WITHDRAWN)) {
			throw new ErrorSavingException("Summons is " + summons.getStatusDescription() + " and cannot be updated.");
		}

		//
		SummonsDischargeAndReleaseFormView dischargeAndReleaseFormView = null;

		try {

			// withdrawal
			// if
			// (summons.getNewStatusId().equals(Constants.DocumentStatus.WITHDRAWN)){
			if (summons.getReasonCode() == null || summons.getReasonCode() == 0) {
				throw new RequiredFieldMissingException("Reason is required for withdrawal.");
			}
			if (StringUtils.isBlank(summons.getComment())) {
				throw new RequiredFieldMissingException("Comment is required for withdrawal.");
			}

			if (summons.getDischargeWitness() != null) {
								
				AddressView documentAddress = new AddressView(summons.getDischargeWitness().getMarkText(), summons.getDischargeWitness().getParishCode(), summons.getDischargeWitness().getPoBoxNo(),
						summons.getDischargeWitness().getPoLocationName(), summons.getDischargeWitness().getStreetName(), summons.getDischargeWitness().getStreetNo(),
	                       null, null, null,summons.getDischargeWitness().getParishDescription());
	           
	           //global address validation - kpowell
	           String errorFoundInAddress  = GenericUtil.validateAddress(documentAddress);
	       
		       if(errorFoundInAddress!= null){
		           throw new RequiredFieldMissingException(errorFoundInAddress);
		       }
		       //
				if (StringUtils.isBlank(summons.getDischargeWitness().getFirstName())) {
					throw new RequiredFieldMissingException("Witness First Name is required for withdrawal.");
				}

				if (StringUtils.isBlank(summons.getDischargeWitness().getLastName())) {
					throw new RequiredFieldMissingException("Witness Last Name is required for withdrawal.");
				}

				if (StringUtils.isBlank(summons.getDischargeWitness().getOccupation())) {
					throw new RequiredFieldMissingException("Witness Occupation is required for withdrawal.");
				}

			}

			// withdraw the summons
			summonsService.updateSummons(summons);

			// if the user wants the discharge form printed now
			if (summons.getStatusId().equals(Constants.DocumentStatus.WITHDRAWN) && summons.isPrintDischargeFormRequested()) {
				dischargeAndReleaseFormView = summonsService.generateSummonsDischargeForm(summons);
			}
			// }
		} catch (Exception e) {
			e.printStackTrace();
			throw new ErrorSavingException(e.getMessage());
		}

		return dischargeAndReleaseFormView;
	}

	/**
	 * 
	 * @param summons
	 * @return
	 * @throws RequiredFieldMissingException
	 * @throws ErrorSavingException
	 */
	/*
	 * public SummonsDischargeAndReleaseFormView cancelSummons( SummonsBO
	 * summons) throws RequiredFieldMissingException, ErrorSavingException {
	 * SummonsService summonsService = serviceFactory.getSummonsService(); //
	 * double check for empty fields if (summons == null) throw new
	 * RequiredFieldMissingException( "Summons details cannot be empty"); if
	 * (summons.getAutomaticSerialNo() == null) throw new
	 * RequiredFieldMissingException("Summons Id is required"); if
	 * (summons.getRoadCheckOffenceOutcomeCode() == null) throw new
	 * RequiredFieldMissingException(
	 * "Road Check Offence Outcome Code is required"); // if this summons is a
	 * manual summons then Id should not be empty if (summons.isManualSummons()
	 * != null && summons.isManualSummons().booleanValue() == true &&
	 * summons.getManualSerialNo() == null) throw new
	 * RequiredFieldMissingException( "Manual Serial No is required."); if
	 * (summons.getOffenderId() == null) throw new
	 * RequiredFieldMissingException("Offender Id is required"); if
	 * (summons.getTaStaffId() == null) throw new
	 * RequiredFieldMissingException("TA Staff Id is required"); if
	 * (summons.getJpRegNumber() == null) throw new
	 * RequiredFieldMissingException(
	 * "Justice of the Peace Reg. No. is required"); if
	 * (summons.getOffenceDtime() == null) throw new
	 * RequiredFieldMissingException("Offence Date is required"); if
	 * (StringUtils.isBlank(summons.getCurrentUsername())) throw new
	 * RequiredFieldMissingException( "Add/Update User is required."); if
	 * (summons.getStatusId() == null) throw new
	 * RequiredFieldMissingException("Status Code is required.");
	 *//**  **/
	/*
	 * if (summons.getStatusId().equals(Constants.DocumentStatus.CANCELLED) ||
	 * summons.getStatusId().equals( Constants.DocumentStatus.WITHDRAWN)) {
	 * throw new ErrorSavingException("Summons is " +
	 * summons.getStatusDescription() + " and cannot be updated."); } //
	 * SummonsDischargeAndReleaseFormView dischargeAndReleaseFormView = null;
	 * try { // withdrawal if
	 * (summons.getNewStatusId().equals(Constants.DocumentStatus.WITHDRAWN)){
	 * if(summons.getReasonCode() == null) { throw new
	 * RequiredFieldMissingException( "Reason is required for withdrawal."); }
	 * if (StringUtils.isBlank(summons.getComment())) { throw new
	 * RequiredFieldMissingException( "Comment is required for withdrawal."); }
	 * if(StringUtils.isBlank(summons.getDischargeWitness().getStreetName())) {
	 * throw new RequiredFieldMissingException(
	 * "Witness address is required for withdrawal."); }
	 * if(StringUtils.isBlank(summons.getDischargeWitness().getFirstName())) {
	 * throw new RequiredFieldMissingException(
	 * "Witness First Name is required for withdrawal."); }
	 * if(StringUtils.isBlank(summons.getDischargeWitness().getLastName() )) {
	 * throw new RequiredFieldMissingException(
	 * "Witness Last Name is required for withdrawal."); }
	 * if(StringUtils.isBlank(summons.getDischargeWitness().getOccupation())) {
	 * throw new RequiredFieldMissingException(
	 * "Witness Occupation is required for withdrawal."); } //withdraw the
	 * summons summonsService .updateSummons(summons); // if the user wants the
	 * discharge form printed now if (summons.getStatusId().equals(
	 * Constants.DocumentStatus.WITHDRAWN) &&
	 * summons.isPrintDischargeFormRequested()) { dischargeAndReleaseFormView =
	 * summonsService .generateSummonsDischargeForm(summons); } } } catch
	 * (Exception e) { e.printStackTrace(); throw new
	 * ErrorSavingException(e.getMessage()); } return
	 * dischargeAndReleaseFormView; }
	 */

	/**
	 * GENERATES DISCHARE FORM DETAILS FOR PRINT
	 * 
	 * @param summons
	 * @return
	 * @throws RequiredFieldMissingException
	 */
	public SummonsDischargeAndReleaseFormView generateSummonsDischargeAndReleaseForm(SummonsBO summons) throws RequiredFieldMissingException {

		SummonsService summonsService = serviceFactory.getSummonsService();

		// double check for empty fields
		if (summons == null)
			throw new RequiredFieldMissingException("Summons details cannot be empty");

		if (summons.getAutomaticSerialNo() == null)
			throw new RequiredFieldMissingException("Summons Id is required");

		if (summons.getRoadCheckOffenceOutcomeCode() == null)
			throw new RequiredFieldMissingException("Road Check Offence Outcome Code is required");

		// if this summons is a manual summons then Id should not be empty
		/*
		 * if (summons.isManualSummons() && summons.getManualSerialNo() == null)
		 * throw new RequiredFieldMissingException(
		 * "Manual Serial No is required.");
		 */
		if (summons.getOffenderId() == null)
			throw new RequiredFieldMissingException("Offender Id is required");

		if (summons.getTaStaffId() == null)
			throw new RequiredFieldMissingException("TA Staff Id is required");

		/*
		 * if (summons.getJpRegNumber() == null) throw new
		 * RequiredFieldMissingException(
		 * "Justice of the Peace Reg. No. is required");
		 */
		if (summons.getOffenceDtime() == null)
			throw new RequiredFieldMissingException("Offence Date is required");

		if (StringUtils.isBlank(summons.getCurrentUsername()))
			throw new RequiredFieldMissingException("Add/Update User is required.");

		if (summons.getStatusId() == null)
			throw new RequiredFieldMissingException("Status Code is required.");

		SummonsDischargeAndReleaseFormView dischargeAndReleaseFormView = null;

		// logics
		// general modification
		// if the summons is not in one of these states, it cannot be modified
		if (!summons.getStatusId().equals(Constants.DocumentStatus.WITHDRAWN)) {
			throw new RequiredFieldMissingException("Summons needs to be withdrawn before form can be printed.");
		} else {
			/*
			 * if(summons.getReasonCode() == null) { throw new
			 * RequiredFieldMissingException(
			 * "Reason is required for withdrawal."); } if (summons.getComment()
			 * == null) { throw new RequiredFieldMissingException(
			 * "Comment is required for cancellation."); }
			 * if(summons.getDischargeWitness().getStreetName() == null) { throw
			 * new RequiredFieldMissingException(
			 * "Witness address is required for withdrawal."); }
			 * if(summons.getDischargeWitness().getFirstName() == null) { throw
			 * new RequiredFieldMissingException(
			 * "Witness First Name is required for withdrawal."); }
			 * if(summons.getDischargeWitness().getLastName() == null) { throw
			 * new RequiredFieldMissingException(
			 * "Witness Last Name is required for withdrawal."); }
			 * if(summons.getDischargeWitness().getOccupation() == null) { throw
			 * new RequiredFieldMissingException(
			 * "Witness Occupation is required for withdrawal."); }
			 */

			try {
				dischargeAndReleaseFormView = summonsService.generateSummonsDischargeForm(summons);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RequiredFieldMissingException(e.getMessage());
			}
		}
		return dischargeAndReleaseFormView;

	}

	/**
	 * GENERATES SUMMONS FORM DETAILS FOR PRINT
	 * 
	 * @param summons
	 * @return
	 * @throws RequiredFieldMissingException
	 */
	public SummonsView generateSummonsForm(SummonsBO summons) throws RequiredFieldMissingException {

		SummonsService summonsService = serviceFactory.getSummonsService();

		// double check for empty fields
		if (summons == null)
			throw new RequiredFieldMissingException("Summons details cannot be empty");

		if (summons.getAutomaticSerialNo() == null)
			throw new RequiredFieldMissingException("Summons Id is required");

		if (summons.getRoadCheckOffenceOutcomeCode() == null)
			throw new RequiredFieldMissingException("Road Check Offence Outcome Code is required");

		// if this summons is a manual summons then Id should not be empty
		/*
		 * if (summons.isManualSummons() && summons.getManualSerialNo() == null)
		 * throw new RequiredFieldMissingException(
		 * "Manual Serial No is required.");
		 */
		if (summons.getOffenderId() == null)
			throw new RequiredFieldMissingException("Offender Id is required");

		if (summons.getTaStaffId() == null)
			throw new RequiredFieldMissingException("TA Staff Id is required");

		/*
		 * if (summons.getJpRegNumber() == null) throw new
		 * RequiredFieldMissingException(
		 * "Justice of the Peace Reg. No. is required");
		 */
		if (summons.getOffenceDtime() == null)
			throw new RequiredFieldMissingException("Offence Date is required");

		if (StringUtils.isBlank(summons.getCurrentUsername()))
			throw new RequiredFieldMissingException("Add/Update User is required.");

		if (summons.getStatusId() == null)
			throw new RequiredFieldMissingException("Status Code is required.");

		SummonsView summonsView = null;

		// logics
		// general modification
		// if teh summons is not in one of these states, it cannot be modified
		if (summons.getStatusId().equals(Constants.DocumentStatus.CANCELLED) || summons.getStatusId().equals(Constants.DocumentStatus.WITHDRAWN)
			|| summons.getStatusId().equals(Constants.DocumentStatus.SERVED) || summons.getStatusId().equals(Constants.DocumentStatus.DISCHARGED_FORM_PRINTED)) {
			throw new RequiredFieldMissingException("Summons is " + summons.getStatusDescription() + " and cannot be printed.");
		} else {
			try {
//				if (summons.getPrintCount() == null || summons.getPrintCount().intValue() == 0) {
//
//					summonsView = summonsService.generateSummons(summons);
//				} else {
//					summonsView = summonsService.reGenerateSummons(summons);
//				}
				summonsView = summonsService.getSummonsView(summons);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RequiredFieldMissingException(e.getMessage());
			}
		}
		return summonsView;

	}

	/**
	 * This method saves the uploaded summons documents
	 * 
	 * @param scannedDocList
	 * @throws ErrorSavingException
	 */
	public List<ScannedDocBO> processScannedSummonsDocuments(List<ScannedDocBO> scannedDocList) throws ErrorSavingException {

		SummonsService summonsService = serviceFactory.getSummonsService();

		if (scannedDocList == null || scannedDocList.isEmpty())
			throw new ErrorSavingException("No scanned document have been selected.");

		try {
			summonsService.processScannedSummonsDocuments(scannedDocList);

		} catch (Exception e) {
			e.printStackTrace();
			throw new ErrorSavingException("Error updating summons.");
		}

		return scannedDocList;
	}

	/**
	 * @throws RequiredFieldMissingException
	 * @throws ErrorSavingException
	 */
	public SummonsBO getSummonsDetails(Integer automaticSerialNo) throws NoRecordFoundException, RequiredFieldMissingException, ErrorSavingException {
		SummonsService summonsService = serviceFactory.getSummonsService();
		SummonsBO summonsDetails = null;

		if (automaticSerialNo == null)
			throw new RequiredFieldMissingException("Summons Id is required.");

		try {
			summonsDetails = summonsService.getSummonsDetailsById(automaticSerialNo);

			if (summonsDetails == null)
				throw new NoRecordFoundException("No Summons found with that id.");

		} catch (Exception e) {
			e.printStackTrace();
			throw new ErrorSavingException("Error retrieving summons.");
		}
		return summonsDetails;
	}

	/**
	 * @throws RequiredFieldMissingException
	 * @throws ErrorSavingException
	 *//*
	public void addJPDetailsFromRoadCheck(List<Integer> summonsIds, Integer jpIdNo) throws NoRecordFoundException, RequiredFieldMissingException, ErrorSavingException {

		SummonsService summonsService = serviceFactory.getSummonsService();

		if (summonsIds == null || summonsIds.isEmpty())
			throw new RequiredFieldMissingException("List of Summons to update is required.");

		if (jpIdNo ==null)
			throw new RequiredFieldMissingException("Justice of the Peace details is required.");

		try {
			summonsService.addJPDetailsFromRoadCheck(summonsIds, jpIdNo);

		} catch (Exception e) {
			e.printStackTrace();
			throw new ErrorSavingException(e.getMessage());
		}

	}
*/
	
	
	/**
	 * 
	 * @param summonsIds
	 * @param jpRegNo
	 * @param isHandHeld  is handheld implies user is doing road check on the road
	 * @param updateUser
	 * @throws NoRecordFoundException
	 * @throws RequiredFieldMissingException
	 * @throws ErrorSavingException
	 */
	public void addJPDetailsFromRoadCheck(List<Integer> summonsIds, Integer jpIdNo, boolean isHandHeld, String updateUser) throws NoRecordFoundException, RequiredFieldMissingException, ErrorSavingException {

		SummonsService summonsService = serviceFactory.getSummonsService();

		if (summonsIds == null || summonsIds.isEmpty())
			throw new RequiredFieldMissingException("List of Summons to update is required.");

		if (jpIdNo==null)
			throw new RequiredFieldMissingException("Justice of the Peace details is required.");
		
		if (updateUser == null || StringUtils.isBlank(updateUser))
			throw new RequiredFieldMissingException("Update user is required.");
		
		
		try {
			//summonsService.addJPDetailsFromRoadCheck(summonsIds, jpIdNo,isHandHeld, updateUser );
			summonsService.addJPDetailsFromRoadCheck(summonsIds, jpIdNo,isHandHeld, updateUser );
		} catch (Exception e) {
			e.printStackTrace();
			throw new ErrorSavingException(e.getMessage());
		}

	}
	
	@RequestMapping("/addJPDetailsFromRoadCheckRest")
	public @ResponseBody void addJPDetailsFromRoadCheckRest(@RequestParam String summonsIds, @RequestParam Integer jpIdNo, @RequestParam boolean isHandHeld, @RequestParam String updateUser) throws NoRecordFoundException, RequiredFieldMissingException, ErrorSavingException {
		try {
			ObjectMapper mapper;
			mapper = new ObjectMapper();
			mapper.configure(MapperFeature.USE_ANNOTATIONS, false);
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			mapper.configure(MapperFeature.REQUIRE_SETTERS_FOR_GETTERS, true);				
			mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm"));
			
			List<Integer> summonsIdList = mapper.readValue(URLDecoder.decode(summonsIds,"UTF-8"), new TypeReference<List<Integer>>() { });
			
			this.addJPDetailsFromRoadCheck(summonsIdList, jpIdNo, isHandHeld, updateUser);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/********************************** COURT CASE **************************************/
	/**
	 * 
	 * @param courtAppearanceBO
	 * @return
	 * @throws RequiredFieldMissingException
	 */
	public CourtAppearanceBO saveCourtAppearances(CourtAppearanceBO previousCourtAppearanceBO, CourtAppearanceBO courtAppearanceBO) throws RequiredFieldMissingException {

		CourtAppearanceService courtAppearanceService = serviceFactory.getCourtAppearanceService();

		// double check for empty fields
		if (courtAppearanceBO == null)
			throw new RequiredFieldMissingException("Court Appearance details cannot be empty");

		if (courtAppearanceBO.getCourtCaseId() == null)
			throw new RequiredFieldMissingException("Court Case Id is required");

		if (courtAppearanceBO.getCourtDate() == null)
			throw new RequiredFieldMissingException("Court Date is required");

		if (courtAppearanceBO.getCourtId() == null)
			throw new RequiredFieldMissingException("Court is required");

		if (courtAppearanceBO.getCurrentUserName() == null)
			throw new RequiredFieldMissingException("Current User Id is required");

		/*
		 * if (courtAppearanceBO.getSummonsId() == null) throw new
		 * RequiredFieldMissingException("Summons ID is required");
		 */

		try {

			Serializable id = courtAppearanceService.saveCourtAppearance(courtAppearanceBO);
			if (id != null)
				courtAppearanceBO.setCourtAppearanceId((Integer) id);

		} catch (Exception e) {
			e.printStackTrace();
			throw new RequiredFieldMissingException(e.getMessage());
		}

		return courtAppearanceBO;

	}

	/**
	 * 
	 * @param courtAppearanceBO
	 * @return
	 * @throws RequiredFieldMissingException
	 */
	public CourtAppearanceBO saveCourtAppearance(CourtAppearanceBO courtAppearanceBO) throws RequiredFieldMissingException {

		CourtAppearanceService courtAppearanceService = serviceFactory.getCourtAppearanceService();

		// double check for empty fields
		if (courtAppearanceBO == null)
			throw new RequiredFieldMissingException("Court Appearance details cannot be empty");

		if (courtAppearanceBO.getCourtCaseId() == null)
			throw new RequiredFieldMissingException("Court Case Id is required");

		if (courtAppearanceBO.getCourtDate() == null)
			throw new RequiredFieldMissingException("Court Date is required");

		if (courtAppearanceBO.getCourtDate() != null) {
			try {
				// add place holder issue date so that 3 days in futer will pass
				DateUtils.validateCourtDate(courtAppearanceBO.getCourtDate(), DateUtils.stringToDate("1990-01-01"));
			} catch (InvalidFieldException e) {

				e.printStackTrace();
				throw new RequiredFieldMissingException(e.getMessage());
			}
		}

		if (courtAppearanceBO.getCourtId() == null)
			throw new RequiredFieldMissingException("Court is required");

		if (courtAppearanceBO.getCurrentUserName() == null)
			throw new RequiredFieldMissingException("Current User Id is required");

		/*
		 * if (courtAppearanceBO.getSummonsId() == null) throw new
		 * RequiredFieldMissingException("Summons ID is required");
		 */

		try {

			Serializable id = courtAppearanceService.saveCourtAppearance(courtAppearanceBO);
			if (id != null)
				courtAppearanceBO.setCourtAppearanceId((Integer) id);

		} catch (Exception e) {
			e.printStackTrace();
			throw new RequiredFieldMissingException(e.getMessage());
		}

		return courtAppearanceBO;

	}

	/**
	 * 
	 * @param courtAppearanceBO
	 * @return
	 * @throws RequiredFieldMissingException
	 */
	public CourtAppearanceBO updateCourtAppearances(CourtAppearanceBO currentCourtAppearance, CourtAppearanceBO newCourtAppearance) throws RequiredFieldMissingException {

		CourtAppearanceService courtAppearanceService = serviceFactory.getCourtAppearanceService();

		// double check for empty fields
		if (currentCourtAppearance == null)
			throw new RequiredFieldMissingException("Court Appearance details cannot be empty");

		if (currentCourtAppearance.getCourtCaseId() == null)
			throw new RequiredFieldMissingException("Court Case Id is required");

		if (currentCourtAppearance.getCourtDate() == null)
			throw new RequiredFieldMissingException("Court Date is required");

		if (currentCourtAppearance.getCourtId() == null)
			throw new RequiredFieldMissingException("Court is required");

		if (currentCourtAppearance.getCurrentUserName() == null)
			throw new RequiredFieldMissingException("Current User Id is required");

		/*
		 * if (currentCourtAppearance.getSummonsId() == null) throw new
		 * RequiredFieldMissingException("Summons ID is required");
		 */

		/*if (currentCourtAppearance.getPleaId() == null)
			throw new RequiredFieldMissingException("Plea is required");
*/
		try {

			courtAppearanceService.updateCourtAppearance(currentCourtAppearance, newCourtAppearance);
			/*
			 * if (id != null) courtAppearanceBO.setCourtAppearanceId((Integer)
			 * id);
			 */
		} catch (Exception e) {
			e.printStackTrace();
			throw new RequiredFieldMissingException(e.getMessage());
		}

		return currentCourtAppearance;

	}

	/**
	 * 
	 * @param courtAppearanceBO
	 * @return
	 * @throws RequiredFieldMissingException
	 */
	public CourtAppearanceBO updateCourtAppearance(CourtAppearanceBO courtAppearanceBO) throws RequiredFieldMissingException {

		CourtAppearanceService courtAppearanceService = serviceFactory.getCourtAppearanceService();

		// double check for empty fields
		if (courtAppearanceBO == null)
			throw new RequiredFieldMissingException("Court Appearance details cannot be empty");

		if (courtAppearanceBO.getCourtCaseId() == null)
			throw new RequiredFieldMissingException("Court Case Id is required");

		if (courtAppearanceBO.getCourtDate() == null)
			throw new RequiredFieldMissingException("Court Date is required");

		if (courtAppearanceBO.getCourtId() == null)
			throw new RequiredFieldMissingException("Court is required");

		if (courtAppearanceBO.getCurrentUserName() == null)
			throw new RequiredFieldMissingException("Current User Id is required");

		/*
		 * if (courtAppearanceBO.getSummonsId() == null) throw new
		 * RequiredFieldMissingException("Summons ID is required");
		 */

	/*	if (courtAppearanceBO.getPleaId() == null)
			throw new RequiredFieldMissingException("Plea is required");
*/
		try {

			courtAppearanceService.updateCourtAppearance(courtAppearanceBO, null);
			/*
			 * if (id != null) courtAppearanceBO.setCourtAppearanceId((Integer)
			 * id);
			 */
		} catch (Exception e) {
			e.printStackTrace();
			throw new RequiredFieldMissingException(e.getMessage());
		}

		return courtAppearanceBO;

	}

	/**
	 * 
	 * @param courtCase
	 * @return
	 * @throws RequiredFieldMissingException
	 */
	public CourtCaseBO updateCourtCase(CourtCaseBO courtCase) throws RequiredFieldMissingException {

		CourtCaseService courtCaseService = serviceFactory.getCourtCaseService();

		// double check for empty fields
		if (courtCase == null)
			throw new RequiredFieldMissingException("Court Case details cannot be empty.");

		if (courtCase.getCourtCaseId() == null)
			throw new RequiredFieldMissingException("Court Case Id is required");

		if (StringUtils.isBlank(courtCase.getCourtCaseNo()))
			throw new RequiredFieldMissingException("Court Case No. is required");

		/*
		 * if (StringUtils.isBlank(courtCase.getComment())) throw new
		 * RequiredFieldMissingException("Comment is required");
		 */
		try {

			courtCaseService.updateCourtCase(courtCase);
			/*
			 * if (id != null) courtAppearanceBO.setCourtAppearanceId((Integer)
			 * id);
			 */
		} catch (Exception e) {
			e.printStackTrace();
			throw new RequiredFieldMissingException(e.getMessage());
		}

		return courtCase;

	}
	
	
	/**
	 * 
	 * @param courtCase
	 * @return
	 * @throws RequiredFieldMissingException
	 */
	public CourtCaseBO overrideCourtCaseDetails(CourtCaseBO courtCase) throws RequiredFieldMissingException {

		CourtCaseService courtCaseService = serviceFactory.getCourtCaseService();

		// double check for empty fields
		if (courtCase == null)
			throw new RequiredFieldMissingException("Court Case details cannot be empty.");

		if (courtCase.getCourtCaseId() == null)
			throw new RequiredFieldMissingException("Court Case Id is required");

		if (StringUtils.isBlank(courtCase.getCourtCaseNo()))
			throw new RequiredFieldMissingException("Court Case No. is required");

		/*
		 * if (StringUtils.isBlank(courtCase.getComment())) throw new
		 * RequiredFieldMissingException("Comment is required");
		 */
		try {

			courtCaseService.overrideCourtCaseDetails(courtCase);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RequiredFieldMissingException(e.getMessage());
		}

		return courtCase;

	}


	/***************************** Batch Processing From Road Check ***********************************/
	/**
	 * 
	 * @param documents
	 * @param jpRegNo
	 * @throws NoRecordFoundException
	 * @throws RequiredFieldMissingException
	 * @throws ErrorSavingException
	 */
	public void verifyAllDocuments(List<DocumentViewBO> documents, String comment, String updateUser) throws NoRecordFoundException, RequiredFieldMissingException, ErrorSavingException {

		if (documents == null || documents.isEmpty())
			throw new RequiredFieldMissingException("List of Documents to update is required.");

		if (StringUtils.isBlank(updateUser))
			throw new RequiredFieldMissingException("Update User is required.");

		if (StringUtils.isBlank(comment))
			throw new RequiredFieldMissingException("Comment is required.");

		/*if (StringUtils.isBlank(updateUser))
			throw new RequiredFieldMissingException("Update User is required.");*/

		try {
			for (DocumentViewBO document : documents) {

				if (StringUtils.isNotBlank(document.getDocumentTypeCode())) {

					if (document.getDocumentTypeCode().equalsIgnoreCase(Constants.DocumentType.SUMMONS)) {

						// SummonsBO summons = (SummonsBO) document;
						SummonsBO summons = serviceFactory.getSummonsService().getSummonsDetailsById(document.getAutomaticSerialNo());
						summons.setComment(formatOverrideDocumentComment(comment, summons.getComment(), updateUser));
						summons.setNewComment(comment);
						summons.setCurrentUsername(updateUser);

						if (summons.getVerificationRequired() == null)
							summons.setVerificationRequired(YesNo.YES_LABEL_STR);

						summons.setNewStatusId(DocumentStatus.VERIFIED);
						// summons.setSupervisorAuthorisationRequested(true);

						serviceFactory.getSummonsService().updateSummons(summons);
					}

					if (document.getDocumentTypeCode().equalsIgnoreCase(Constants.DocumentType.WARNING_NOTICE)) {

						// WarningNoticeBO warningNoticeBO = (WarningNoticeBO)
						// document;
						WarningNoticeBO warningNoticeBO = this.serviceFactory.getWarningNoticeService().getWarningNoticeDetailsById(document.getAutomaticSerialNo());
						warningNoticeBO.setComment(formatOverrideDocumentComment(comment, warningNoticeBO.getComment(), updateUser));
						warningNoticeBO.setNewComment(comment);
						warningNoticeBO.setCurrentUsername(updateUser);
						if (warningNoticeBO.getVerificationRequired() == null)
							warningNoticeBO.setVerificationRequired(YesNo.YES_LABEL_STR);

						warningNoticeBO.setNewStatusId(DocumentStatus.VERIFIED);
						// warningNoticeBO.setSupervisorAuthorisationRequested(true);

						serviceFactory.getWarningNoticeService().updateWarningNotice(warningNoticeBO);
					}

					if (document.getDocumentTypeCode().equalsIgnoreCase(Constants.DocumentType.WARNING_NOTICE_NO_PROSECUTION)) {

						// WarningNoProsecutionBO warningNoProsecutionBO =
						// (WarningNoProsecutionBO) document;
						WarningNoProsecutionBO warningNoProsecutionBO = this.serviceFactory.getWarningNoProsecutionService().getWarningNoProsecutionDetailsById(document.getAutomaticSerialNo());
						warningNoProsecutionBO.setComment(formatOverrideDocumentComment(comment, warningNoProsecutionBO.getComment(), updateUser));
						warningNoProsecutionBO.setNewComment(comment);
						warningNoProsecutionBO.setCurrentUsername(updateUser);

						if (warningNoProsecutionBO.getVerificationRequired() == null)
							warningNoProsecutionBO.setVerificationRequired(YesNo.YES_LABEL_STR);

						warningNoProsecutionBO.setNewStatusId(DocumentStatus.VERIFIED);
						// warningNoProsecutionBO.setSupervisorAuthorisationRequested(true);

						serviceFactory.getWarningNoProsecutionService().updateWarningNoProsecution(warningNoProsecutionBO);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new ErrorSavingException(e.getMessage());
		}

	}

	/**
	 * 
	 * @param documents
	 * @param jpRegNo
	 * @throws NoRecordFoundException
	 * @throws RequiredFieldMissingException
	 * @throws ErrorSavingException
	 */
	public void denyVerificationAllDocuments(List<DocumentViewBO> documents, String comment, String updateUser) throws NoRecordFoundException, RequiredFieldMissingException, ErrorSavingException {

		if (documents == null || documents.isEmpty())
			throw new RequiredFieldMissingException("List of Documents to update is required.");

		if (StringUtils.isBlank(comment))
			throw new RequiredFieldMissingException("Comment is required for a rejection.");

		if (StringUtils.isBlank(updateUser))
			throw new RequiredFieldMissingException("Update User is required.");

		try {
			for (DocumentViewBO document : documents) {

				if (StringUtils.isNotBlank(document.getDocumentTypeCode())) {

					if (document.getDocumentTypeCode().equalsIgnoreCase(Constants.DocumentType.SUMMONS)) {

						SummonsBO summons = serviceFactory.getSummonsService().getSummonsDetailsById(document.getAutomaticSerialNo());
						summons.setComment(formatDenyOverrideDocumentComment(comment, summons.getComment(), updateUser));
						summons.setNewComment(comment);
						summons.setCurrentUsername(updateUser);
						// summons.setVerificationRequired(YesNo.YES_LABEL_STR);
						summons.setNewStatusId(DocumentStatus.DENY_AUTHORISATION);
						// summons.setSupervisorAuthorisationRequested(false);

						serviceFactory.getSummonsService().updateSummons(summons);
					}

					if (document.getDocumentTypeCode().equalsIgnoreCase(Constants.DocumentType.WARNING_NOTICE)) {

						WarningNoticeBO warningNoticeBO = this.serviceFactory.getWarningNoticeService().getWarningNoticeDetailsById(document.getAutomaticSerialNo());
						warningNoticeBO.setComment(formatDenyOverrideDocumentComment(comment, warningNoticeBO.getComment(), updateUser));
						warningNoticeBO.setNewComment(comment);
						warningNoticeBO.setCurrentUsername(updateUser);
						// warningNoticeBO.setVerificationRequired(YesNo.YES_LABEL_STR);
						warningNoticeBO.setNewStatusId(DocumentStatus.DENY_AUTHORISATION);
						// warningNoticeBO.setSupervisorAuthorisationRequested(false);

						serviceFactory.getWarningNoticeService().updateWarningNotice(warningNoticeBO);
					}

					if (document.getDocumentTypeCode().equalsIgnoreCase(Constants.DocumentType.WARNING_NOTICE_NO_PROSECUTION)) {

						WarningNoProsecutionBO warningNoProsecutionBO = this.serviceFactory.getWarningNoProsecutionService().getWarningNoProsecutionDetailsById(document.getAutomaticSerialNo());
						warningNoProsecutionBO.setComment(formatDenyOverrideDocumentComment(comment, warningNoProsecutionBO.getComment(), updateUser));
						warningNoProsecutionBO.setNewComment(comment);
						warningNoProsecutionBO.setCurrentUsername(updateUser);
						// warningNoProsecutionBO.setVerificationRequired(YesNo.YES_LABEL_STR);
						warningNoProsecutionBO.setNewStatusId(DocumentStatus.DENY_AUTHORISATION);
						// warningNoProsecutionBO.setSupervisorAuthorisationRequested(false);

						serviceFactory.getWarningNoProsecutionService().updateWarningNoProsecution(warningNoProsecutionBO);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new ErrorSavingException(e.getMessage());
		}

	}
	
	 @RequestMapping("/serveAllDocuments")
	 public @ResponseBody void serveAllDocumentsRest(@RequestBody ServeAllDocsRestBO serveAllDocsRestBO) throws NoRecordFoundException,
     RequiredFieldMissingException, ErrorSavingException{
		 serveAllDocuments(serveAllDocsRestBO.documents, serveAllDocsRestBO.servedByTAUser, serveAllDocsRestBO.servedDate, 
				 serveAllDocsRestBO.docAddress, serveAllDocsRestBO.servedAddress, serveAllDocsRestBO.updateUser);
	 }

	
	
	private boolean checkIfSummonsPresent(List<DocumentViewBO> documents){	
			for (DocumentViewBO doc : documents) {				
				if(doc.getDocumentTypeCode().equalsIgnoreCase(Constants.DocumentType.SUMMONS)){
					return true;
				}				
			
			}
			return false;
		}

	/**
     * ROAD CHECK METHOD TO SERVE ALL DOCUMENTS
      * @param documents
     * @param jpRegNo
     * @throws NoRecordFoundException
     * @throws RequiredFieldMissingException
     * @throws ErrorSavingException
     */
     public void serveAllDocuments(List<DocumentViewBO> documents, String servedByTAUser, Date servedDate, AddressView docAddress, String servedAddress, String updateUser) throws NoRecordFoundException,
           RequiredFieldMissingException, ErrorSavingException {

           if (documents == null || documents.isEmpty())
                 throw new RequiredFieldMissingException("List of Documents to update is required.");

           if (StringUtils.isBlank(servedByTAUser))
                 throw new RequiredFieldMissingException("Served By Staff is required.");

           if (servedDate == null)
                 throw new RequiredFieldMissingException("Served at Date is required.");

           /*if (StringUtils.isBlank(servedParish))
                 throw new RequiredFieldMissingException("Parish is required.");

           if (StringUtils.isBlank(servedAddress))
                 throw new RequiredFieldMissingException("Served at Address is required.");*/
           
           //if document list contains at least one summons, then address details become manadatory
           if(checkIfSummonsPresent(documents)){
	           AddressView documentAddress = new AddressView(docAddress.getMarkText(), docAddress.getParish(), docAddress.getPoBoxNo(),
	                       docAddress.getPoLocationName(), docAddress.getStreetName(), docAddress.getStreetNo(),
	                       null, null, null,docAddress.getParishDescription());
	           
	           //global address validation - kpowell
	           String errorFoundInAddress  = GenericUtil.validateAddress(documentAddress);
	       
		       if(errorFoundInAddress!= null){
		           throw new RequiredFieldMissingException(errorFoundInAddress);
		       }
		       //
           }

           if (StringUtils.isBlank(updateUser))
                 throw new RequiredFieldMissingException("Update User is required.");

           try {
                 for (DocumentViewBO document : documents) {
                       if (StringUtils.isNotBlank(document.getDocumentTypeCode())) {

                             if (document.getDocumentTypeCode().equalsIgnoreCase(Constants.DocumentType.SUMMONS)) {

                                   // SummonsBO summons = (SummonsBO) document;
                                   SummonsBO summons = this.serviceFactory.getSummonsService().getSummonsDetailsById(document.getAutomaticSerialNo());
                                   summons.setCurrentUsername(updateUser);
                                   summons.setServedAtAddress(servedAddress);
                                   summons.setServedAtParish(docAddress.getParish());
                                   summons.setServedByUserID(servedByTAUser);
                                   summons.setServedOnDate(servedDate);
                                   summons.setNewStatusId(DocumentStatus.SERVED);
                                   
                                   
                                   //set served Address
                                   AddressBO serveAtAddressBO = new AddressBO();
                                   serveAtAddressBO.setMark(docAddress.getMarkText());
                                   serveAtAddressBO.setPoLocation(docAddress.getPoLocationName());
                                   serveAtAddressBO.setParish(docAddress.getParishDescription());
                                   serveAtAddressBO.setParish_code(docAddress.getParish());
                                   serveAtAddressBO.setStreetName(docAddress.getStreetName());
                                   serveAtAddressBO.setPoBoxNumber(docAddress.getPoBoxNo());
                                   serveAtAddressBO.setStreetNumber(docAddress.getStreetNo());                                   
                                		   
                                   summons.setServedAtAddressBO(serveAtAddressBO);

                                   serviceFactory.getSummonsService().updateSummons(summons);
                             }

                             if (document.getDocumentTypeCode().equalsIgnoreCase(Constants.DocumentType.WARNING_NOTICE)) {

                                   // WarningNoticeBO warningNoticeBO = (WarningNoticeBO)
                                   // document;
                                   WarningNoticeBO warningNoticeBO = this.serviceFactory.getWarningNoticeService().getWarningNoticeDetailsById(document.getAutomaticSerialNo());
                                   warningNoticeBO.setCurrentUsername(updateUser);
                                   // warningNoticeBO.setVerificationRequired(YesNo.YES_LABEL_STR);
                                   warningNoticeBO.setNewStatusId(DocumentStatus.SERVED);
                                   warningNoticeBO.setServedByUserID(servedByTAUser);
                                   warningNoticeBO.setServedOnDate(servedDate);
                             
                                   // warningNoticeBO.setSupervisorAuthorisationRequested(false);

                                   serviceFactory.getWarningNoticeService().updateWarningNotice(warningNoticeBO);
                             }

                             if (document.getDocumentTypeCode().equalsIgnoreCase(Constants.DocumentType.WARNING_NOTICE_NO_PROSECUTION)) {

                                   // WarningNoProsecutionBO warningNoProsecutionBO =
                                   // (WarningNoProsecutionBO) document;
                                   WarningNoProsecutionBO warningNoProsecutionBO = this.serviceFactory.getWarningNoProsecutionService().getWarningNoProsecutionDetailsById(document.getAutomaticSerialNo());
                                   warningNoProsecutionBO.setCurrentUsername(updateUser);
                                   // warningNoProsecutionBO.setVerificationRequired(YesNo.YES_LABEL_STR);
                                   warningNoProsecutionBO.setNewStatusId(DocumentStatus.SERVED);
                                   // warningNoProsecutionBO.setSupervisorAuthorisationRequested(false);
                                   warningNoProsecutionBO.setServedByUserID(servedByTAUser);
                                   warningNoProsecutionBO.setServedOnDate(servedDate);

                                   serviceFactory.getWarningNoProsecutionService().updateWarningNoProsecution(warningNoProsecutionBO);
                             }
                       }
                 }
           } catch (Exception e) {
                 e.printStackTrace();
                 throw new ErrorSavingException(e.getMessage());
           }

     }


	/**
	 * 
	 * @param documents - JSON representation of list of DocumentViewBOs
	 * This method was created to allow the passing of a List of DocumentViewBOs
	 * Due to a type erasure issue present when the list was passed. 
	 */
	@RequestMapping("/printAllDocumentsMobile")
	public @ResponseBody DocumentBytesBO printAllDocumentsMobile
	(@RequestBody DocumentViewBOList documents, @RequestParam Integer reasonForReprint, 
			@RequestParam String reprintComment, @RequestParam String updateUser,
			@RequestParam String officeLocationDesc) throws NoRecordFoundException,
		RequiredFieldMissingException, ErrorSavingException {

		List<DocumentViewBO> updatedList = new ArrayList<DocumentViewBO>();
		DocumentBytesBO docBytesBo = new DocumentBytesBO();

		 try {
			 
			 /* ObjectMapper mapper;
			mapper = new ObjectMapper();
			mapper.configure(MapperFeature.USE_ANNOTATIONS, false);
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			mapper.configure(MapperFeature.REQUIRE_SETTERS_FOR_GETTERS, true);
					
			mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm"));

		        List<DocumentViewBO> docList = mapper.readValue(URLDecoder.decode(documents,"UTF-8"), new TypeReference<List<DocumentViewBO>>() { });*/
		        
		        updatedList = printAllDocuments(documents, reasonForReprint, reprintComment, updateUser);
		        if(updatedList != null)
		        	docBytesBo = getDocumentByteList(updatedList, updateUser, officeLocationDesc);
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
		
		return docBytesBo;
	}

	
	public  List<DocumentViewBO> printAllDocuments( List<DocumentViewBO> documents,  Integer reasonForReprint,  String reprintComment,  String updateUser) throws NoRecordFoundException,
		RequiredFieldMissingException, ErrorSavingException {

		List<DocumentViewBO> updatedList = new ArrayList<DocumentViewBO>();
	
		if (documents == null || documents.isEmpty())
			throw new RequiredFieldMissingException("List of Documents to update is required.");

		/*
		 * if (StringUtils.isNotBlank(reasonForReprint)){ reason =
		 * Integer.valueOf(reasonForReprint); }
		 */

		if (StringUtils.isBlank(updateUser))
			throw new RequiredFieldMissingException("Update User is required.");

		try {
			for (DocumentViewBO document : documents) {
				if (StringUtils.isNotBlank(document.getDocumentTypeCode())) {

					if (document.getDocumentTypeCode().equalsIgnoreCase(Constants.DocumentType.SUMMONS)) {

						SummonsBO summons = this.serviceFactory.getSummonsService().getSummonsDetailsById(document.getAutomaticSerialNo());
						summons.setCurrentUsername(updateUser);
						summons.setReprintReasonCode(reasonForReprint);
						summons.setReprintComment(reprintComment);
											
						if((summons.getPrintCount() != null ? summons.getPrintCount() : 0) > 0)
						{
							summons.setNewStatusId(DocumentStatus.REPRINTED);	
							summons.setComment(formatDocumentComment(summons.getReprintComment(), summons.getComment(), updateUser));
							
							if (reasonForReprint == null || reasonForReprint == 0){
								throw new RequiredFieldMissingException("Reprint reason is required.");		
						}							
								
						}else{
							summons.setNewStatusId(DocumentStatus.PRINTED);	
						}
						summons.setJpIdNumber(document.getJpIdNumber());
						updatedList.add(serviceFactory.getSummonsService().updateSummons(summons));
					}

					if (document.getDocumentTypeCode().equalsIgnoreCase(Constants.DocumentType.WARNING_NOTICE)) {

						WarningNoticeBO warningNoticeBO = this.serviceFactory.getWarningNoticeService().getWarningNoticeDetailsById(document.getAutomaticSerialNo());
						warningNoticeBO.setCurrentUsername(updateUser);
						warningNoticeBO.setReprintReasonCode(reasonForReprint);
						warningNoticeBO.setReprintComment(reprintComment);
												
						if((warningNoticeBO.getPrintCount() != null ?warningNoticeBO.getPrintCount() : 0) > 0){
							warningNoticeBO.setNewStatusId(DocumentStatus.REPRINTED);	
							warningNoticeBO.setComment(formatDocumentComment(warningNoticeBO.getReprintComment(), warningNoticeBO.getComment(), updateUser));
							
							if (reasonForReprint == null){
								throw new RequiredFieldMissingException("Reprint reason is required.");		
							}							
							
						}else{
							warningNoticeBO.setNewStatusId(DocumentStatus.PRINTED);	
						}			
					
						updatedList.add(serviceFactory.getWarningNoticeService().updateWarningNotice(warningNoticeBO));
					}

					if (document.getDocumentTypeCode().equalsIgnoreCase(Constants.DocumentType.WARNING_NOTICE_NO_PROSECUTION)) {

						WarningNoProsecutionBO warningNoProsecutionBO = this.serviceFactory.getWarningNoProsecutionService().getWarningNoProsecutionDetailsById(document.getAutomaticSerialNo());
						warningNoProsecutionBO.setCurrentUsername(updateUser);
						warningNoProsecutionBO.setReprintReasonCode(reasonForReprint);
						warningNoProsecutionBO.setReprintComment(reprintComment);
												
						if((warningNoProsecutionBO.getPrintCount() != null ? warningNoProsecutionBO.getPrintCount() : 0) > 0){
							warningNoProsecutionBO.setNewStatusId(DocumentStatus.REPRINTED);	
							warningNoProsecutionBO.setComment(formatDocumentComment(warningNoProsecutionBO.getReprintComment(), warningNoProsecutionBO.getComment(), updateUser));
							
							if (reasonForReprint == null){
								throw new RequiredFieldMissingException("Reprint reason is required.");		
							}		

						} else {
							warningNoProsecutionBO.setNewStatusId(DocumentStatus.PRINTED);
						}

						updatedList.add(serviceFactory.getWarningNoProsecutionService().updateWarningNoProsecution(warningNoProsecutionBO));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new ErrorSavingException(e.getMessage());

		}
		return updatedList;

	}

	
	
	/**
	 * 
	 * @param documents
	 * @param updateUser
	 * @throws NoRecordFoundException
	 * @throws RequiredFieldMissingException
	 * @throws ErrorSavingException
	 */
	public void cancelAllDocuments(List<DocumentViewBO> documents, String updateUser, String reasonForCancellation, String cancellationComment) throws NoRecordFoundException,
		RequiredFieldMissingException, ErrorSavingException {

		if (documents == null || documents.isEmpty())
			throw new RequiredFieldMissingException("List of Documents to update is required.");

		if (StringUtils.isBlank(updateUser))
			throw new RequiredFieldMissingException("Update User is required");

		if (StringUtils.isBlank(reasonForCancellation))
			throw new RequiredFieldMissingException("Reason for Cancellation is required");

		if (StringUtils.isBlank(cancellationComment))
			throw new RequiredFieldMissingException("Cancellation Comment is required");

		try {
			for (DocumentViewBO document : documents) {

				if (StringUtils.isNotBlank(document.getDocumentTypeCode())) {

					if (document.getDocumentTypeCode().equalsIgnoreCase(Constants.DocumentType.SUMMONS)) {

						// SummonsBO summons = (SummonsBO) document;
						// summons.setCurrentUsername(updateUser);

						// summons.setNewStatusId(DocumentStatus.CANCELLED);

						SummonsBO summons = serviceFactory.getSummonsService().getSummonsDetailsById(document.getAutomaticSerialNo());

						summons.setCurrentUsername(updateUser);

						summons.setNewStatusId(DocumentStatus.CANCELLED);

						summons.setReasonCode(Integer.parseInt(reasonForCancellation));

						summons.setComment(cancellationComment);

						serviceFactory.getSummonsService().updateSummons(summons);
					}

					if (document.getDocumentTypeCode().equalsIgnoreCase(Constants.DocumentType.WARNING_NOTICE)) {

						// WarningNoticeBO warningNoticeBO = (WarningNoticeBO)
						// document;
						// warningNoticeBO.setCurrentUsername(updateUser);

						// warningNoticeBO.setNewStatusId(DocumentStatus.CANCELLED);
						WarningNoticeBO warningNoticeBO = this.getWarningNoticeDetails(document.getAutomaticSerialNo());

						warningNoticeBO.setCurrentUsername(updateUser);

						warningNoticeBO.setNewStatusId(DocumentStatus.CANCELLED);

						warningNoticeBO.setComment(cancellationComment);

						warningNoticeBO.setReasonCode(Integer.parseInt(reasonForCancellation));

						serviceFactory.getWarningNoticeService().updateWarningNotice(warningNoticeBO);
					}

					if (document.getDocumentTypeCode().equalsIgnoreCase(Constants.DocumentType.WARNING_NOTICE_NO_PROSECUTION)) {

						// WarningNoProsecutionBO warningNoProsecutionBO =
						// (WarningNoProsecutionBO) document;
						// warningNoProsecutionBO.setCurrentUsername(updateUser);

						// warningNoProsecutionBO.setNewStatusId(DocumentStatus.CANCELLED);

						WarningNoProsecutionBO warningNoProsecutionBO = this.getWarningNoProsecutionDetails(document.getAutomaticSerialNo());

						warningNoProsecutionBO.setCurrentUsername(updateUser);

						warningNoProsecutionBO.setNewStatusId(DocumentStatus.CANCELLED);

						warningNoProsecutionBO.setComment(cancellationComment);

						warningNoProsecutionBO.setReasonCode(Integer.parseInt(reasonForCancellation));

						serviceFactory.getWarningNoProsecutionService().updateWarningNoProsecution(warningNoProsecutionBO);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new ErrorSavingException(e.getMessage());
		}

	}

	/******************************************* MISCELLANEOUS **************************************/

	/**
	 * 
	 * @param manualSerialNo
	 * @param autoSerialNo
	 * @param documentType
	 * @return
	 * @throws RequiredFieldMissingException
	 */
	public boolean manualSerialExists(String manualSerialNo, Integer autoSerialNo, String documentType) throws RequiredFieldMissingException {

		boolean exists = false;

		if (StringUtils.isBlank(manualSerialNo))
			throw new RequiredFieldMissingException("Manual Ref No. is required");

		/*if (autoSerialNo == null)
			throw new RequiredFieldMissingException("Auto Ref No. is required");*/

		if (StringUtils.isBlank(documentType))
			throw new RequiredFieldMissingException("Document Type is required");

		DocumentsCriteriaBO docCriteria = new DocumentsCriteriaBO();
		docCriteria.setManualSerialNo(manualSerialNo);

		List<DocumentViewBO> documents = null;

		if (documentType.equalsIgnoreCase(Constants.DocumentType.SUMMONS)) {

			documents = this.serviceFactory.getSummonsService().lookupSummons(docCriteria);
		}

		if (documentType.equalsIgnoreCase(Constants.DocumentType.WARNING_NOTICE_NO_PROSECUTION)) {

			documents = this.serviceFactory.getWarningNoProsecutionService().lookupWarningNoProsecution(docCriteria);
		}

		if (documentType.equalsIgnoreCase(Constants.DocumentType.WARNING_NOTICE)) {

			documents = this.serviceFactory.getWarningNoticeService().lookupWarningNotice(docCriteria);
		}

		// check the items
		if (documents != null && !documents.isEmpty()) {
			//exists = true;

			for (DocumentViewBO documentViewBO : documents) {
				
				if ((autoSerialNo != null) && !(documentViewBO.getAutomaticSerialNo().equals(autoSerialNo))) 
				{
					//System.err.println(" this doc " + documentViewBO.getAutomaticSerialNo() + " AUTO " + autoSerialNo);
					exists = true;
					return exists;
				}
				else if(autoSerialNo == null)
				{
					exists = true;
					return exists;
				}
			}

		}

		return exists;
	}

	/*********************
	 * FOR DISCHARGE WITNESS PULL DETAILS FROM ROMS BEFORE GOING TRN
	 * 
	 * @throws RequiredFieldMissingException
	 *****************/
	
	@RequestMapping("/getPersonByTRN")
	public @ResponseBody PersonBO getPersonByTRN(@RequestParam String trn) throws RequiredFieldMissingException {

		if (StringUtils.isBlank(trn)) {
			throw new RequiredFieldMissingException("TRN is required");
		}

		PersonBO personBO = serviceFactory.getPersonService().findPersonByTRN(trn);
		if (personBO != null) {
			return personBO;
		}
		return null;
	}
	
	public Date getInitialCourtDateByCourtCaseId(Integer courtCaseId)
			throws RequiredFieldMissingException{
	
		if (courtCaseId==null) {
			throw new RequiredFieldMissingException("Court Case ID is required");
		}
		
		Date courtDate = serviceFactory.getCourtAppearanceService().getInitialCourtDateByCourtCaseId(courtCaseId);
		
		
		return courtDate;
		
	}
	
	public String getAssociatedWarningNotice(Integer roadCheckOffenceOutcomeId)
			throws RequiredFieldMissingException{
	
		if (roadCheckOffenceOutcomeId==null) {
			throw new RequiredFieldMissingException("Road Check Offence Outcome Id is required");
		}
		
		String refSeqNo = serviceFactory.getWarningNoticeService().getAssociatedWarningNotice(roadCheckOffenceOutcomeId);
		
		
		return refSeqNo;
		
	}
	
	
	
	/**
	 * Helper function to add new comments to old comments so that they cannot be overridden
	 * 
	 * @param newComment
	 * @param oldComment
	 * @return
	 */
	public String formatDocumentComment(String newComment, String oldComment, String updateUser) {

		Date date = new Date(System.currentTimeMillis());
		String comment = null;
		try {
			if (StringUtils.isNotBlank(newComment) && StringUtils.isBlank(oldComment)) {
				//return "<b>(" + DateUtils.getFormattedUtilDate(date) + "/" + getRomsLoggedInUser().getUsername() + ")</b>:<br/>" + newComment.trim();
				return "(" + DateUtils.getFormattedUtilDate(date) + "/" + updateUser + "): " + newComment.trim();
			}

			if (StringUtils.isBlank(newComment) && StringUtils.isNotBlank(oldComment)) {
				return oldComment;
			}

			// when retrieving full comment then replace slash with new line
			// character
			if (StringUtils.isNotBlank(newComment) && StringUtils.isNotBlank(oldComment)) {

				//comment = oldComment + ";<b>(" + DateUtils.getFormattedUtilDate(date) + "/" + getRomsLoggedInUser().getUsername() + ")</b>:<br/>" + newComment.trim();
				comment = oldComment + ";(" + DateUtils.getFormattedUtilDate(date) + "/" + updateUser + "): " + newComment.trim();

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
				return "(" + DateUtils.getFormattedUtilDate(date) + "/ Verified by " + userName + "): " + newComment.trim();
			}

			if (StringUtils.isBlank(newComment) && StringUtils.isNotBlank(oldComment)) {
				return oldComment;
			}

			// when retrieving full comment then replace slash with new line
			// character
			if (StringUtils.isNotBlank(newComment) && StringUtils.isNotBlank(oldComment)) {

				comment = oldComment + ";(" + DateUtils.getFormattedUtilDate(date) + "/ Verified by " + userName + "): " + newComment.trim();

			}
		} catch (ParseException e) {

			e.printStackTrace();
		}
		return comment;

	}
	
	public String formatDenyOverrideDocumentComment(String newComment, String oldComment, String userName) {

		Date date = new Date(System.currentTimeMillis());
		String comment = null;
		try {
			if (StringUtils.isNotBlank(newComment) && StringUtils.isBlank(oldComment)) {
				return "(" + DateUtils.getFormattedUtilDate(date) + "/ Details Not Accepted by " + userName + "): " + newComment.trim();
			}

			if (StringUtils.isBlank(newComment) && StringUtils.isNotBlank(oldComment)) {
				return oldComment;
			}

			// when retrieving full comment then replace slash with new line
			// character
			if (StringUtils.isNotBlank(newComment) && StringUtils.isNotBlank(oldComment)) {

				comment = oldComment + ";(" + DateUtils.getFormattedUtilDate(date) + "/ Details Not Accepted by " + userName + "): " + newComment.trim();

			}
		} catch (ParseException e) {

			e.printStackTrace();
		}
		return comment;

	}
	
	@RequestMapping("/getDocumentByteList")
	public DocumentBytesBO getDocumentByteList(List<DocumentViewBO> documentList, String romsLoggedInUser, String officeLocationDesc){
		List<byte[]> allBytes = new ArrayList<byte[]>();
		DocumentBytesBO docBytesBo = new DocumentBytesBO();
		
		
		if (documentList == null || documentList.isEmpty()) {
			return docBytesBo;
		}
		try {
			ObjectMapper mapper;
			mapper = new ObjectMapper();
			mapper.configure(MapperFeature.USE_ANNOTATIONS, false);
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			mapper.configure(MapperFeature.REQUIRE_SETTERS_FOR_GETTERS, true);				
			mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm"));
			
			
			//List<DocumentsToPrintBO> docIdList = mapper.readValue(URLDecoder.decode(documentList,"UTF-8"), new TypeReference<List<DocumentsToPrintBO>>() { });
			//DocumentViewBO document = new DocumentViewBO();
			for (DocumentViewBO document : documentList) {
//				if(docToPrint.getDocumentTypeCode().equalsIgnoreCase(DocumentType.SUMMONS)){
//					document = getSummonsDetails(docToPrint.getAutomaticSerialNo());
//				}
//				if(docToPrint.getDocumentTypeCode().equalsIgnoreCase(DocumentType.WARNING_NOTICE)){
//					document = getWarningNoticeDetails(docToPrint.getAutomaticSerialNo());
//				}
//				if(docToPrint.getDocumentTypeCode().equalsIgnoreCase(DocumentType.WARNING_NOTICE_NO_PROSECUTION)){
//					document = getWarningNoProsecutionDetails(docToPrint.getAutomaticSerialNo());
//				}
				
				if (StringUtils.isNotBlank(document.getDocumentTypeCode()) && document.getAutomaticSerialNo() != null) {
					// will hold the path to the file
					String relativeWebPath = null;
					// the file with the jasper report
					File reportFile = null;
					// will hold the byte stream
					byte[] bytes = null;

					//********************** Summons *************************//*
					if (document.getDocumentTypeCode().equalsIgnoreCase(DocumentType.SUMMONS)) {
						relativeWebPath = SUMMONS_PAGE_1;
						String absoluteDiskPath = servletContext.getRealPath(relativeWebPath);
						
						reportFile = new File(absoluteDiskPath);
						
						//**************** GET THE DETAILS *****************//*
						SummonsBO summons = getSummonsDetails(Integer.valueOf(document.getAutomaticSerialNo()));

						if (summons != null) {

							summons.setCurrentUsername(romsLoggedInUser);

							SummonsView summonsView = generateSummonsForm(summons);

							if (summonsView != null) {
								// list with datasource beans
								List<SummonsView> datasourceList = new ArrayList<SummonsView>();

								datasourceList.add(summonsView);
								
								bytes = JasperRunManager.runReportToPdf(reportFile.getPath(), new HashMap(), new JRBeanCollectionDataSource(datasourceList));

								if (bytes != null) {
									// writeImage(bytes, request, response);//
									// write
									// response
									allBytes.add(bytes);
								}
							} // discharge form null
						} // summons null

					}
					//********************** Warning notice *************************//*
					if (document.getDocumentTypeCode().equalsIgnoreCase(DocumentType.WARNING_NOTICE)) {
						relativeWebPath = WARNING_NOTICE;
						String absoluteDiskPath = servletContext.getRealPath(relativeWebPath);
						reportFile = new File(absoluteDiskPath);

						//**************** GET THE DETAILS *****************//*
						WarningNoticeBO warningNoticeBO = getWarningNoticeDetails(Integer.valueOf(document.getAutomaticSerialNo()));

						if (warningNoticeBO != null) {
							
							warningNoticeBO.setCurrentUsername(romsLoggedInUser);

							NoticeView warningNoticeView = generateWarningNoticeForm(warningNoticeBO);

							if (warningNoticeView != null) {
								warningNoticeView.setTaStaffAssignedLocation(WordUtils.capitalize(officeLocationDesc));
								// list with datasource beans
								List<NoticeView> datasourceList = new ArrayList<NoticeView>();

								int x = 0;
								while (x < NOTICE_COPIES) {
									datasourceList.add(warningNoticeView);
									x++;
								}

								bytes = JasperRunManager.runReportToPdf(reportFile.getPath(), new HashMap(), new JRBeanCollectionDataSource(datasourceList));

								if (bytes != null) {
									// writeImage(bytes, request, response);//
									// write
									// response
									allBytes.add(bytes);
								}
							} // form null
						} // summons null

					}
					//********************** Waring No Prosecution *************************//*
					if (document.getDocumentTypeCode().equalsIgnoreCase(DocumentType.WARNING_NOTICE_NO_PROSECUTION)) {
						relativeWebPath = WARNING_NOTICE_NO_PROSECUTION;
						// get the complete file name on disk
						String absoluteDiskPath = servletContext.getRealPath(relativeWebPath);
						reportFile = new File(absoluteDiskPath);

						//**************** GET THE DETAILS *****************//*
						WarningNoProsecutionBO warningNoProsecutionBO = getWarningNoProsecutionDetails(Integer.valueOf(document.getAutomaticSerialNo()));

						if (warningNoProsecutionBO != null) {

							warningNoProsecutionBO.setCurrentUsername(romsLoggedInUser);

							NoticeView warningNoProsecutionView = generateWarningNoProsecutionForm(warningNoProsecutionBO);

							if (warningNoProsecutionView != null) {
								warningNoProsecutionView.setTaStaffAssignedLocation(WordUtils.capitalize(officeLocationDesc));

								// list with datasource beans
								List<NoticeView> datasourceList = new ArrayList<NoticeView>();

								int x = 0;
								while (x < NOTICE_COPIES) {
									datasourceList.add(warningNoProsecutionView);
									x++;
								}

								bytes = JasperRunManager.runReportToPdf(reportFile.getPath(), new HashMap(), new JRBeanCollectionDataSource(datasourceList));

								if (bytes != null) {
									// writeImage(bytes, request, response);//
									// write
									// response
									allBytes.add(bytes);
								}
							} // discharge form null
						} // summons null

					}
				}
			}	
		}
		catch(Exception e){
			e.printStackTrace();
		}

		docBytesBo.setDocBytes(allBytes);
		return docBytesBo;

	}
	
	
	
}
