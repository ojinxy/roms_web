package fsl.ta.toms.roms.dao;

import java.util.ArrayList;

import fsl.dao.DAO;
import fsl.ta.toms.roms.bo.DocumentViewBO;
import fsl.ta.toms.roms.bo.ScannedDocBO;
import fsl.ta.toms.roms.bo.SummonsBO;
import fsl.ta.toms.roms.dataobjects.SummonsDO;
import fsl.ta.toms.roms.search.criteria.impl.DocumentsCriteriaBO;

public interface SummonsDAO extends DAO {
	
	public ArrayList<DocumentViewBO> lookupSummons(DocumentsCriteriaBO criteria);

	public ArrayList<ScannedDocBO> lookupScannedDocsForSummonId(
			Integer summonsId);

	ArrayList<ScannedDocBO> lookupScannedDocsForRoadCheckOffenceId(
			Integer offenceId);

	SummonsBO lookupSummonsById(Integer summonsId);

	public SummonsDO findSummonsByRoadCheckOffenceOutcomeId(Integer roadCheckOffenceOutcomeId);
}
