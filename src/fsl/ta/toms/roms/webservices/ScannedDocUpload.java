/**
 * Created By: jreid
 * Date: Apr 29, 2013
 *
 */
package fsl.ta.toms.roms.webservices;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import fsl.ta.toms.roms.bo.DocumentViewBO;
import fsl.ta.toms.roms.bo.ScannedDocBO;
import fsl.ta.toms.roms.exception.ErrorSavingException;
import fsl.ta.toms.roms.exception.RequiredFieldMissingException;
import fsl.ta.toms.roms.service.ServiceFactory;

/**
 * 
 * @author jreid Created Date: May 9, 2013
 */
public class ScannedDocUpload extends SpringBeanAutowiringSupport {

	@Autowired
	private ServiceFactory serviceFactory;

	/**
	 * 
	 * @param scannedDoc
	 * @return
	 * @throws ErrorSavingException
	 * @throws RequiredFieldMissingException
	 */
	public ScannedDocBO uploadFile(ScannedDocBO scannedDoc, DocumentViewBO document)
			throws ErrorSavingException, RequiredFieldMissingException {

		if (scannedDoc == null)
			throw new RequiredFieldMissingException("Error while saving.");

		if (StringUtils.isBlank(scannedDoc.getCurrentUsername()))
			throw new RequiredFieldMissingException("Current User is required.");

		if (scannedDoc.getFileContents().length == 0)
			throw new RequiredFieldMissingException("File is required.");
		
		if (StringUtils.isBlank(scannedDoc.getMimeType()))
			throw new RequiredFieldMissingException("Mime Type is required.");
		
		/*if (StringUtils.isBlank(scannedDoc.getDocType()))
			throw new RequiredFieldMissingException("Doc Type is required.");
		
		*/if (StringUtils.isBlank(scannedDoc.getDescription()))
			throw new RequiredFieldMissingException("Description is required.");
		
		
		if (scannedDoc.getRoadCheckOffenceOutcomeId() == null)
			throw new RequiredFieldMissingException("Road Check is required.");
		
		
		/*if (StringUtils.isBlank(scannedDoc.getRelativePath()))
			throw new RequiredFieldMissingException("Mime Type is required.");
*/


		ScannedDocBO doc = null;
			
		try {
			doc = serviceFactory.getScannedDocService()
					.saveScannedDoc(scannedDoc,document);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ErrorSavingException(e.getMessage());
		}

		return doc;
	}
	
	/**
	 * 
	 * @param scannedDoc
	 * @return
	 * @throws ErrorSavingException
	 * @throws RequiredFieldMissingException
	 */
	public 	ScannedDocBO  getFileContents(ScannedDocBO scannedDoc,DocumentViewBO document)
			throws ErrorSavingException, RequiredFieldMissingException {

		if (scannedDoc == null)
			throw new RequiredFieldMissingException("Error while retrieving file.");

		if (StringUtils.isBlank(scannedDoc.getCurrentUsername()))
			throw new RequiredFieldMissingException("Current User is required.");
		
		if (scannedDoc.getScannedDocId() == null)
			throw new RequiredFieldMissingException("Scanned Doc Id is required.");

		/*if (StringUtils.isBlank(scannedDoc.getMimeType()))
			throw new RequiredFieldMissingException("Mime Type is required.");
		
		if (StringUtils.isBlank(scannedDoc.getDocType()))
			throw new RequiredFieldMissingException("Doc Type is required.");
		
		if (scannedDoc.getRoadCheckOffenceOutcomeId() == null)
			throw new RequiredFieldMissingException("Road Check is required.");
		*/
		ScannedDocBO doc = null;

		try {
			//doc =  serviceFactory.getScannedDocService().getFileContents(scannedDoc);
			doc =  serviceFactory.getScannedDocService().printScannedDoc(scannedDoc,document);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ErrorSavingException(e.getMessage());
		}
		return doc;
	}
	
	/**
	 * 
	 * @param scannedDocID
	 * @return
	 * @throws ErrorSavingException
	 * @throws RequiredFieldMissingException
	 */
	public 	ScannedDocBO  getScannedDocById(Integer scannedDocID)
			throws ErrorSavingException, RequiredFieldMissingException {

		if (scannedDocID == null)
			throw new RequiredFieldMissingException("Scanned document ID.");
		
		
		ScannedDocBO doc = null;

		try {
			//doc =  serviceFactory.getScannedDocService().getFileContents(scannedDoc);
			doc =  serviceFactory.getScannedDocService().lookupScannedDoc(scannedDocID);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ErrorSavingException(e.getMessage());
		}
		return doc;
	}
	
	/**
	 * 
	 * @param scannedDoc
	 * @return
	 * @throws ErrorSavingException
	 * @throws RequiredFieldMissingException
	 */
	public ScannedDocBO deleteScannedDoc(ScannedDocBO scannedDoc,DocumentViewBO document)
			throws ErrorSavingException, RequiredFieldMissingException {

		if (scannedDoc == null)
			throw new RequiredFieldMissingException("Error while saving.");
		
	/*	if (StringUtils.isBlank(scannedDoc.getCurrentUsername()))
			throw new RequiredFieldMissingException("Current User is required.");
		
		if (scannedDoc.getScannedDocId() == null)
			throw new RequiredFieldMissingException("Scanned Doc Id is required.");
		
		
		if (scannedDoc.getRoadCheckOffenceOutcomeId() == null)
			throw new RequiredFieldMissingException("Road Check  Offence is required.");*/
		

		ScannedDocBO doc = null;
			
		try {
			doc = serviceFactory.getScannedDocService()
					.deleteScannedDoc(scannedDoc,document);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new ErrorSavingException(e.getMessage());
		}

		return doc;
	}
	
	/**
	 * 
	 * @param scannedDoc
	 * @return
	 * @throws ErrorSavingException
	 * @throws RequiredFieldMissingException
	 */
	public ScannedDocBO deleteScannedManualDoc(ScannedDocBO scannedDoc,DocumentViewBO document)
			throws ErrorSavingException, RequiredFieldMissingException {

		if (scannedDoc == null)
			throw new RequiredFieldMissingException("Error while saving.");
		
	/*	if (StringUtils.isBlank(scannedDoc.getCurrentUsername()))
			throw new RequiredFieldMissingException("Current User is required.");
		
		if (scannedDoc.getScannedDocId() == null)
			throw new RequiredFieldMissingException("Scanned Doc Id is required.");
		
		
		if (scannedDoc.getRoadCheckOffenceOutcomeId() == null)
			throw new RequiredFieldMissingException("Road Check  Offence is required.");*/
		

		ScannedDocBO doc = null;
			
		try {
			doc = serviceFactory.getScannedDocService()
					.deleteScannedDoc(scannedDoc, document);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new ErrorSavingException(e.getMessage());
		}

		return doc;
	}



}
