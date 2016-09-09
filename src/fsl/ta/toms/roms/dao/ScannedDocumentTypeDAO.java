package fsl.ta.toms.roms.dao;

import java.util.HashMap;
import java.util.List;

import fsl.dao.DAO;
import fsl.ta.toms.roms.dataobjects.ScannedDocumentTypeDO;
import fsl.ta.toms.roms.exception.InvalidFieldException;

public interface ScannedDocumentTypeDAO extends DAO {
	
	List<ScannedDocumentTypeDO> getScannedDocumentTypeReference(
			HashMap<String, String> filter, String status)
			throws InvalidFieldException;


}
