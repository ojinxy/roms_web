package fsl.ta.toms.roms.dao;

import java.io.Serializable;
import java.util.List;

import fsl.dao.DAO;
import fsl.ta.toms.roms.bo.DocumentViewBO;
import fsl.ta.toms.roms.dataobjects.ScannedDocDO;


public interface ScannedDocDAO extends DAO {
	
	public Serializable saveScannedDoc(ScannedDocDO scannedDocDo);
	
	public ScannedDocDO findScannedDocById(Integer scannedDocId);
	
	public boolean deleteScannedDocById(Integer scannedDocId);

	List<ScannedDocDO> findScannedDocsByOutcomeId(Integer outcomeId);

	boolean updateScannedDocDO(ScannedDocDO scannedDoc);

	ScannedDocDO findManualScannedDocumentByOutcomeId(Integer outcomeId);

	

	 
}
