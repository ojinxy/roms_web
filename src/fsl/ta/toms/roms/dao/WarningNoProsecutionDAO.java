package fsl.ta.toms.roms.dao;

import java.util.ArrayList;

import fsl.dao.DAO;
import fsl.ta.toms.roms.bo.DocumentViewBO;
import fsl.ta.toms.roms.bo.ScannedDocBO;
import fsl.ta.toms.roms.bo.WarningNoProsecutionBO;
import fsl.ta.toms.roms.dataobjects.WarningNoProsecutionDO;
import fsl.ta.toms.roms.search.criteria.impl.DocumentsCriteriaBO;

/**
 * 
 * @author jreid
 * Created Date: Oct 11, 2013
 */
public interface WarningNoProsecutionDAO extends DAO {
	
	/**
	 * 
	 * @param criteria
	 * @return
	 */
	public ArrayList<DocumentViewBO> lookupWarningNoProsecution(DocumentsCriteriaBO criteria);

	
	/**
	 * 
	 * @param warningNoProsecutionId
	 * @return
	 */
	public ArrayList<ScannedDocBO> lookupScannedDocsForWarningNoProsecutionId(
			Integer warningNoProsecutionId);


	WarningNoProsecutionBO lookupWarningNoProsecutionById(
			Integer warningNoProsecutionId);

	public WarningNoProsecutionDO findWarningNoProsByRoadCheckOffenceOutcomeId(Integer roadCheckOffenceOutcomeId);
	
}
