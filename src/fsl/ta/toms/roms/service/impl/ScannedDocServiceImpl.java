/**
 * Created By: jreid
 * Date: Apr 30, 2013
 *
 */
package fsl.ta.toms.roms.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.fsl.filemanager.FileManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import fsl.ta.toms.roms.bo.DocumentViewBO;
import fsl.ta.toms.roms.bo.ScannedDocBO;
import fsl.ta.toms.roms.constants.Constants;
import fsl.ta.toms.roms.constants.Constants.DocumentType;
import fsl.ta.toms.roms.dao.DAOFactory;
import fsl.ta.toms.roms.dataobjects.AuditEntry;
import fsl.ta.toms.roms.dataobjects.CDEventDO;
import fsl.ta.toms.roms.dataobjects.EventAuditDO;
import fsl.ta.toms.roms.dataobjects.ReasonDO;
import fsl.ta.toms.roms.dataobjects.RoadCheckOffenceOutcomeDO;
import fsl.ta.toms.roms.dataobjects.ScannedDocDO;
import fsl.ta.toms.roms.dataobjects.ScannedDocHistoryDO;
import fsl.ta.toms.roms.dataobjects.StatusDO;
import fsl.ta.toms.roms.dataobjects.ScannedDocumentTypeDO;
import fsl.ta.toms.roms.dataobjects.SummonsDO;
import fsl.ta.toms.roms.dataobjects.WarningNoProsecutionDO;
import fsl.ta.toms.roms.dataobjects.WarningNoticeDO;
import fsl.ta.toms.roms.exception.ErrorSavingException;
import fsl.ta.toms.roms.service.ScannedDocService;
import fsl.ta.toms.roms.service.ServiceFactory;
import fsl.ta.toms.roms.util.StringUtil;

/**
 * @author jreid Created Date: Apr 30, 2013
 */
public class ScannedDocServiceImpl extends SpringBeanAutowiringSupport implements ScannedDocService {
	@Autowired
	DAOFactory		daoFactory;

	@Autowired
	ServiceFactory	serviceFactory;

	public ScannedDocServiceImpl() {
		super();
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW,rollbackFor={Exception.class,RuntimeException.class})
	public ScannedDocBO saveScannedDoc(ScannedDocBO scannedDoc,DocumentViewBO document) throws Exception {
		if (scannedDoc == null)
			return null;

		Calendar calendar = Calendar.getInstance();
		Date currentDate = calendar.getTime();

		// create file manager
		FileManager transferClass = new FileManager();

		// try to save the file on the filesystem
		String fileName = transferClass.saveFileToFileSystem(scannedDoc.getFileContents());
		scannedDoc.setRelativePath(fileName);

		//System.err.println(" Mime type doc : " + scannedDoc.getMimeType() + " / " + scannedDoc.getRelativePath());

		ScannedDocDO scannedDocToSave = new ScannedDocDO(scannedDoc);

		// update the database
		AuditEntry auditEntry = new AuditEntry();

		// eventAuditBO.set)(new Date(scannedDoc.getIssueDate().getTime()));
		auditEntry.setCreateUsername(scannedDoc.getCurrentUsername());
		auditEntry.setCreateDTime(currentDate);
		scannedDocToSave.setAuditEntry(auditEntry);

		// set the road operation outcome
		RoadCheckOffenceOutcomeDO roadCheckOffenceOutcomeDO = this.daoFactory.getWarningNoticeDAO().find(RoadCheckOffenceOutcomeDO.class, scannedDoc.getRoadCheckOffenceOutcomeId());
		if (roadCheckOffenceOutcomeDO != null)
			scannedDocToSave.setRoadCheckOffenceOutcome(roadCheckOffenceOutcomeDO);

		Integer scannedDocId = (Integer) this.daoFactory.getParishDAO().save(scannedDocToSave);
		
		scannedDocToSave = this.daoFactory.getParishDAO().find(ScannedDocDO.class, scannedDocId);


		//Update Documents with Manual Serial No
		StatusDO status = this.daoFactory.getSummonsDAO().find(StatusDO.class, Constants.DocumentStatus.PRINTED);
		if(scannedDoc.getDocType().equalsIgnoreCase(Constants.DocumentType.SUMMONS_STRING)){
			
			SummonsDO summonsDO = new SummonsDO();
			
			summonsDO = this.daoFactory.getSummonsDAO().findSummonsByRoadCheckOffenceOutcomeId(roadCheckOffenceOutcomeDO.getRoadCheckOffenceOutcomeId());
			
			//summonsDO = this.daoFactory.getSummonsDAO().find(SummonsDO.class, 222);
			if(summonsDO!=null){
				summonsDO.setManualSerialNumber(scannedDoc.getManualSerialNo());
				summonsDO.getAuditEntry().setUpdateDTime(currentDate);
				summonsDO.getAuditEntry().setUpdateUsername(scannedDoc.getCurrentUsername());
				summonsDO.setStatus(status);
				summonsDO.setPrintDtime(summonsDO.getAuditEntry().getCreateDTime());
				summonsDO.setPrintCount(1);
				this.daoFactory.getScannedDocDAO().update(summonsDO);
			}
			
			scannedDocToSave.setDocType(Constants.ScannedDocumentTypeShort.MANUAL_DOCUMENT);
			
			this.daoFactory.getParishDAO().update(scannedDocToSave);
			
		}
		else if(scannedDoc.getDocType().equalsIgnoreCase(Constants.DocumentType.WARNING_NOTICE_STRING)){
			WarningNoticeDO warningNoticeDO = new WarningNoticeDO();
		
			warningNoticeDO = this.daoFactory.getWarningNoticeDAO().findWarningNoticeByRoadCheckOffenceOutcomeId(roadCheckOffenceOutcomeDO.getRoadCheckOffenceOutcomeId());
		
			if(warningNoticeDO!=null){
				warningNoticeDO.setManualSerialNumber(scannedDoc.getManualSerialNo());
				warningNoticeDO.getAuditEntry().setUpdateDTime(currentDate);
				warningNoticeDO.getAuditEntry().setUpdateUsername(scannedDoc.getCurrentUsername());
				warningNoticeDO.setStatus(status);
				warningNoticeDO.setPrintDtime(warningNoticeDO.getAuditEntry().getCreateDTime());
				warningNoticeDO.setPrintCount(1);
				this.daoFactory.getScannedDocDAO().update(warningNoticeDO);
			}
			
			scannedDocToSave.setDocType(Constants.ScannedDocumentTypeShort.MANUAL_DOCUMENT);
			
			this.daoFactory.getParishDAO().update(scannedDocToSave);
		}
		else if(scannedDoc.getDocType().equalsIgnoreCase(Constants.DocumentType.WARNING_NOTICE_NO_PROSECUTION_STRING)){
			WarningNoProsecutionDO warningNoProsecutionDO = new WarningNoProsecutionDO();
			
			warningNoProsecutionDO = this.daoFactory.getWarningNoProsecutionDAO().findWarningNoProsByRoadCheckOffenceOutcomeId(roadCheckOffenceOutcomeDO.getRoadCheckOffenceOutcomeId());
			
			if(warningNoProsecutionDO!=null){
				warningNoProsecutionDO.setManualSerialNumber(scannedDoc.getManualSerialNo());
				warningNoProsecutionDO.getAuditEntry().setUpdateDTime(currentDate);
				warningNoProsecutionDO.getAuditEntry().setUpdateUsername(scannedDoc.getCurrentUsername());
				warningNoProsecutionDO.setStatus(status);
				warningNoProsecutionDO.setPrintDtime(warningNoProsecutionDO.getAuditEntry().getCreateDTime());
				warningNoProsecutionDO.setPrintCount(1);
				this.daoFactory.getScannedDocDAO().update(warningNoProsecutionDO);
			}
			
			scannedDocToSave.setDocType(Constants.ScannedDocumentTypeShort.MANUAL_DOCUMENT);
			
			this.daoFactory.getParishDAO().update(scannedDocToSave);
		}

		scannedDoc.setFileContents(null);
		scannedDoc.setScannedDocId(scannedDocId);

		
		
		// save audit
		EventAuditDO eventAuditDO = createEventAuditRecord(scannedDocToSave,document);
		// this.daoFactory.getWarningNoticeDAO().save(eventAuditDO);
		this.serviceFactory.getEventAuditService().saveEventAuditDO(eventAuditDO);
				
		
		
		return scannedDoc;

	}

	@Override
	@Transactional
	public ScannedDocBO deleteScannedDoc(ScannedDocBO scannedDocBO, DocumentViewBO document) throws Exception {

		if (scannedDocBO == null)
			return null;

		Calendar calendar = Calendar.getInstance();
		Date currentDate = calendar.getTime();

		ScannedDocDO docToDelete = daoFactory.getScannedDocDAO().findScannedDocById(scannedDocBO.getScannedDocId());

		if (docToDelete != null) {

			ScannedDocBO scannedDocBONew = new ScannedDocBO(docToDelete);

			ScannedDocHistoryDO scannedDocHistoryDO = new ScannedDocHistoryDO(scannedDocBONew);
			// set the road operation outcome
			RoadCheckOffenceOutcomeDO roadCheckOffenceOutcomeDO = this.daoFactory.getWarningNoticeDAO().find(RoadCheckOffenceOutcomeDO.class,
				docToDelete.getRoadCheckOffenceOutcome().getRoadCheckOffenceOutcomeId());
			if (roadCheckOffenceOutcomeDO != null)
				scannedDocHistoryDO.setRoadCheckOffenceOutcome(roadCheckOffenceOutcomeDO);

			// save the history file
			scannedDocHistoryDO.setCreateUsername(scannedDocBO.getCurrentUsername());
			scannedDocHistoryDO.setCreateDTime(currentDate);

			Serializable scannedDocHistoryID = daoFactory.getScannedDocDAO().save(scannedDocHistoryDO);

			// if successful then remove the reference file
			if (scannedDocHistoryID != null) {
				// deleted
				daoFactory.getScannedDocDAO().delete(docToDelete);

				// save audit
				EventAuditDO eventAuditDO = createEventAuditRecord(scannedDocBO.getScannedDocId(), scannedDocBO.getDocType(),(Integer) scannedDocHistoryID, scannedDocBO.getCurrentUsername(), document);
				// save the event audit
				this.serviceFactory.getEventAuditService().saveEventAuditDO(eventAuditDO);
			}
		}

		return scannedDocBO;
	}
	
	
	
	@Override
	@Transactional
	public ScannedDocBO deleteManualScannedManualDoc(ScannedDocBO scannedDocBO,DocumentViewBO document) throws Exception {

		if (scannedDocBO == null)
			return null;

		Calendar calendar = Calendar.getInstance();
		Date currentDate = calendar.getTime();

		//get all the scanned docs for this outcome type
		ScannedDocDO docToDelete = daoFactory.getScannedDocDAO().findManualScannedDocumentByOutcomeId(scannedDocBO.getRoadCheckOffenceOutcomeId());
		
	
		if (docToDelete != null) {

			ScannedDocBO scannedDocBONew = new ScannedDocBO(docToDelete);

			ScannedDocHistoryDO scannedDocHistoryDO = new ScannedDocHistoryDO(scannedDocBONew);
			// set the road operation outcome
			RoadCheckOffenceOutcomeDO roadCheckOffenceOutcomeDO = this.daoFactory.getWarningNoticeDAO().find(RoadCheckOffenceOutcomeDO.class,
				docToDelete.getRoadCheckOffenceOutcome().getRoadCheckOffenceOutcomeId());
			if (roadCheckOffenceOutcomeDO != null)
				scannedDocHistoryDO.setRoadCheckOffenceOutcome(roadCheckOffenceOutcomeDO);

			// save the history file
			scannedDocHistoryDO.setCreateUsername(scannedDocBO.getCurrentUsername());
			scannedDocHistoryDO.setCreateDTime(currentDate);

			Serializable scannedDocHistoryID = daoFactory.getScannedDocDAO().save(scannedDocHistoryDO);

			// if successful then remove the reference file
			if (scannedDocHistoryID != null) {
				// deleted
				daoFactory.getScannedDocDAO().delete(docToDelete);

				// save audit
				EventAuditDO eventAuditDO = createEventAuditRecord(scannedDocBO.getScannedDocId(), scannedDocBO.getDocType(), (Integer) scannedDocHistoryID, scannedDocBO.getCurrentUsername(),document);
				// save the event audit
				this.serviceFactory.getEventAuditService().saveEventAuditDO(eventAuditDO);
			}
		}

		return scannedDocBO;
	}

	@Override
	public ScannedDocBO getFileContents(ScannedDocBO scannedDoc) throws Exception {

		if (scannedDoc == null)
			return null;

		// ScannedDocTransfer transferClass = new ScannedDocTransfer();
		// create file manager
		FileManager transferClass = new FileManager();

		byte[] fileContents = transferClass.readFileFromFileSystem(scannedDoc.getRelativePath());

		scannedDoc.setFileContents(fileContents);
		return scannedDoc;

	}

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public ScannedDocBO printScannedDoc(ScannedDocBO scannedDoc,DocumentViewBO document) {
		EventAuditDO eventAuditDO = null;
		try {

			/***************** UPDATE THE SUMMONS **********************/
			ScannedDocDO scannedDocDO = daoFactory.getScannedDocDAO().find(ScannedDocDO.class, scannedDoc.getScannedDocId());

			if (scannedDocDO == null)
				throw new ErrorSavingException("Scanned Document with this ID does not exist.");

			if (scannedDocDO != null) {

				scannedDocDO.setPrintDTime(Calendar.getInstance().getTime());
				scannedDocDO.setPrintUsername(scannedDoc.getCurrentUsername());
				scannedDocDO.setReprintReason(daoFactory.getScannedDocDAO().find(ReasonDO.class, scannedDoc.getReprintReasonCode()));
				scannedDocDO.setReprintComment(scannedDoc.getReprintComment());
				// set the print count
				if (scannedDocDO.getPrintCount() != null)
					scannedDocDO.setPrintCount(scannedDocDO.getPrintCount().intValue() + 1);
				else
					scannedDocDO.setPrintCount(1);

				// update
				daoFactory.getScannedDocDAO().update(scannedDocDO);
				// update the summons record
				scannedDoc = new ScannedDocBO(scannedDocDO);

				// get the file contents
				getFileContents(scannedDoc);
				
				// create audit trail
				eventAuditDO = createEventAuditRecord(scannedDocDO,document);
				eventAuditDO.setEventCode(Constants.EventCode.REPRINT_SCANNED_DOCUMENT);
				
				String scannedDocType = scannedDocDO.getDocType();
				String scannedDocTypeDesc = "";
				
				if(StringUtil.isSet(scannedDocType))
				{
					ScannedDocumentTypeDO  scannedDocTypeDO = (ScannedDocumentTypeDO)daoFactory.getScannedDocDAO().load(ScannedDocumentTypeDO.class, scannedDocType);
					
					try{
				
						scannedDocTypeDesc = scannedDocTypeDO.getDocumentTypeDescription();
					}
					catch(Exception e){
						e.printStackTrace();
						scannedDocTypeDesc = scannedDocType;
					}
						
				}
				
				/*eventAuditDO.setRefType2Code(Constants.EventRefTypeCode.SCANNED_DOC_TYPE);
				eventAuditDO.setRefValue2(scannedDocTypeDesc);*/
				
				
				String origin="";
				if(document != null)
				{
					origin = document.getOrigin();
				}
				String comment = "Scanned Doc. Type:"+
						scannedDocTypeDesc+"; ";
				
				comment = comment + "Reason: " + scannedDocDO.getReprintReason().getDescription();
				
				if(StringUtil.isSet(scannedDoc.getReprintComment()))
				{
					comment = comment + "; Comment: " + scannedDoc.getReprintComment();
				}
				eventAuditDO.setComment(comment);
				
				
				// save audit
				if (eventAuditDO != null) {
					eventAuditDO.setAuditEntry(new AuditEntry());
					eventAuditDO.getAuditEntry().setCreateDTime(Calendar.getInstance().getTime());
					eventAuditDO.getAuditEntry().setCreateUsername(scannedDocDO.getPrintUsername());
					
					this.serviceFactory.getEventAuditService().saveEventAuditDO(eventAuditDO);
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return scannedDoc;

	}

	@Override
	@Transactional
	public List<ScannedDocBO> retrieveScannedDocByOutcomeId(ScannedDocBO scannedDoc) {

		List<ScannedDocDO> scannedDocs = new ArrayList<ScannedDocDO>();
		List<ScannedDocBO> scannedDocBOs = new ArrayList<ScannedDocBO>();

		scannedDocs = this.daoFactory.getScannedDocDAO().findScannedDocsByOutcomeId(scannedDoc.getRoadCheckOffenceOutcomeId());

		for (ScannedDocDO doc : scannedDocs) {
			scannedDocBOs.add(new ScannedDocBO(doc));
		}

		return scannedDocBOs;

	}

	@Override
	@Transactional
	public boolean replaceScannedDoc(ScannedDocBO scannedDoc) throws ErrorSavingException {

		ScannedDocDO scannedDocToSave = this.daoFactory.getScannedDocDAO().find(ScannedDocDO.class, scannedDoc.getScannedDocId());

		if (scannedDocToSave == null)
			throw new ErrorSavingException("Summons with this ID does not exist.");

		Calendar calendar = Calendar.getInstance();
		Date currentDate = calendar.getTime();

		return false;

	}

	@Override
	@Transactional(readOnly = true)
	public ScannedDocBO lookupScannedDoc(Integer id) {

		ScannedDocDO results = null;

		try {
			results = this.daoFactory.getScannedDocDAO().find(ScannedDocDO.class, id);

		} catch (Exception e) {
			e.printStackTrace();

		}

		if (results != null)
			return new ScannedDocBO(results);

		return null;
		// return results;
	}

	@Override
	@Transactional
	public boolean processScannedDocuments(List<ScannedDocBO> scannedDocList) {

		Calendar calendar = Calendar.getInstance();
		Date currentDate = calendar.getTime();

		if (scannedDocList == null || scannedDocList.isEmpty())
			return false;

		// iterate through scanned document list
		for (ScannedDocBO scannedDoc : scannedDocList) {
			// create new scanned Doc business object
			ScannedDocDO scannedDocToSave = new ScannedDocDO(scannedDoc);

			AuditEntry auditEntry = new AuditEntry();
			auditEntry.setCreateDTime(currentDate);
			auditEntry.setCreateUsername(scannedDoc.getCurrentUsername());
			auditEntry.setUpdateDTime(currentDate);
			auditEntry.setUpdateUsername(scannedDoc.getCurrentUsername());
			scannedDocToSave.setAuditEntry(auditEntry);
			// save object
			daoFactory.getWarningNoticeDAO().saveOrUpdate(scannedDocToSave);
		}

		return true;
	}

	/**
	 * 
	 * @param scannedDoc
	 * @return 
	 */
	private EventAuditDO createEventAuditRecord(ScannedDocDO scannedDoc,DocumentViewBO document) {

		EventAuditDO eventAuditBO = new EventAuditDO();

		if (scannedDoc.getVersionNbr() == 0) {
			eventAuditBO.setEventCode(Constants.EventCode.UPLOAD_SCANNED_DOCUMENT);
			eventAuditBO.setEvent(new CDEventDO(Constants.EventCode.UPLOAD_SCANNED_DOCUMENT));
		} else {
			eventAuditBO.setEventCode(Constants.EventCode.DELETE_SCANNED_DOCUMENT);
			eventAuditBO.setEvent(new CDEventDO(Constants.EventCode.DELETE_SCANNED_DOCUMENT));
		}

		
		//String parentDocDetails = daoFactory.getScannedDocDAO().getParentDocOfScannedDoc(scannedDoc.getScannedDocId());
		
		if(document != null)
		{
			String origin = "";
			String docType = document.getDocumentTypeCode();
			if(StringUtils.isNotBlank(document.getManualSerialNo())){
				origin = DocumentType.ORIGIN_MANUAL;
			}
						
			eventAuditBO.setRefType1Code(Constants.EventRefTypeCode.DOCUMENT_TYPE);
			eventAuditBO.setRefValue1(document.getDocumentTypeName());
				
			eventAuditBO.setRefType2Code(Constants.EventRefTypeCode.REFERENCE_NUMBER);
			eventAuditBO.setRefValue2(document.getReferenceNo() + "");
		
			if(origin.equalsIgnoreCase(DocumentType.ORIGIN_MANUAL))
			{
				eventAuditBO.setRefValue2(document.getManualSerialNo() + "");
			}
		}
		
		String scannedDocType  = scannedDoc.getDocType();
		String scannedDocTypeDesc = "";
		System.err.println("Scann Doc Code " + scannedDocType);
		if(StringUtil.isSet(scannedDocType))
		{
			ScannedDocumentTypeDO  scannedDocTypeDO = (ScannedDocumentTypeDO)daoFactory.getScannedDocDAO().load(ScannedDocumentTypeDO.class, scannedDocType);
			
			try{
			/*if(scannedDocTypeDO != null && scannedDocTypeDO.getDocumentTypeDescription() != null)
			{*/
				scannedDocTypeDesc = scannedDocTypeDO.getDocumentTypeDescription();
			//}else{
			}
			catch(Exception e){
				e.printStackTrace();
				scannedDocTypeDesc = scannedDocType;
			}
				
		}
		eventAuditBO.setComment("Scanned Document Type: " + scannedDocTypeDesc);
	
		eventAuditBO.setAuditEntry(scannedDoc.getAuditEntry());

		return eventAuditBO;
	}

	/**
	 * 
	 * @param scannedDocId
	 * @param id
	 * @param userName
	 * @return
	 */
	private EventAuditDO createEventAuditRecord(Integer scannedDocId,String scanDocType, Integer id, String userName,DocumentViewBO document) {
		Calendar calendar = Calendar.getInstance();
		Date currentDate = calendar.getTime();

		EventAuditDO eventAuditBO = new EventAuditDO();
		StringBuffer comment = new StringBuffer();
		
		eventAuditBO.setEventCode(Constants.EventCode.DELETE_SCANNED_DOCUMENT);
		eventAuditBO.setEvent(new CDEventDO(Constants.EventCode.DELETE_SCANNED_DOCUMENT));

		
		if(document != null)
		{
			eventAuditBO.setRefType1Code(Constants.EventRefTypeCode.DOCUMENT_TYPE);
			eventAuditBO.setRefValue1(document.getDocumentTypeName());
				
			eventAuditBO.setRefType2Code(Constants.EventRefTypeCode.REFERENCE_NUMBER);
			eventAuditBO.setRefValue2(document.getReferenceNo() + "");
	
		}
		
		String scannedDocTypeDesc = "";
		
		if(StringUtil.isSet(scanDocType))
		{
			ScannedDocumentTypeDO  scannedDocTypeDO = (ScannedDocumentTypeDO)daoFactory.getScannedDocDAO().load(ScannedDocumentTypeDO.class, scanDocType);
			
			try{
		
				scannedDocTypeDesc = scannedDocTypeDO.getDocumentTypeDescription();
			}
			catch(Exception e){
				e.printStackTrace();
				scannedDocTypeDesc = scanDocType;
			}
				
		}
		eventAuditBO.setComment("Scanned Document Type: " + scannedDocTypeDesc);
		
		
		eventAuditBO.getAuditEntry().setCreateUsername(userName);
		eventAuditBO.getAuditEntry().setCreateDTime(currentDate);

		return eventAuditBO;
	}

}
