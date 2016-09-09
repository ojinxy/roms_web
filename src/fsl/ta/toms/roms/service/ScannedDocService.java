package fsl.ta.toms.roms.service;

import java.io.IOException;
import java.util.List;

import fsl.ta.toms.roms.bo.DocumentViewBO;
import fsl.ta.toms.roms.bo.ScannedDocBO;
import fsl.ta.toms.roms.dataobjects.ScannedDocDO;
import fsl.ta.toms.roms.exception.ErrorSavingException;

public interface ScannedDocService {

	boolean replaceScannedDoc(ScannedDocBO scannedDoc) throws ErrorSavingException;

	ScannedDocBO saveScannedDoc(ScannedDocBO scannedDoc, DocumentViewBO document) throws IOException, Exception;

	ScannedDocBO lookupScannedDoc(Integer id);

	boolean processScannedDocuments(List<ScannedDocBO> scannedDocList);

	ScannedDocBO getFileContents(ScannedDocBO scannedDoc) throws Exception;

	List<ScannedDocBO> retrieveScannedDocByOutcomeId(ScannedDocBO scannedDoc);

	ScannedDocBO printScannedDoc(ScannedDocBO scannedDoc,DocumentViewBO document);

	ScannedDocBO deleteScannedDoc(ScannedDocBO scannedDoc,DocumentViewBO document) throws Exception;

	ScannedDocBO deleteManualScannedManualDoc(ScannedDocBO scannedDocBO,DocumentViewBO document) throws Exception;

}
