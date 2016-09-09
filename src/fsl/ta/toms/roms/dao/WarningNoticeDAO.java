package fsl.ta.toms.roms.dao;

import java.util.ArrayList;

import fsl.dao.DAO;
import fsl.ta.toms.roms.bo.DocumentViewBO;
import fsl.ta.toms.roms.bo.WarningNoticeBO;
import fsl.ta.toms.roms.bo.ScannedDocBO;
import fsl.ta.toms.roms.dataobjects.WarningNoticeDO;
import fsl.ta.toms.roms.search.criteria.impl.DocumentsCriteriaBO;
import fsl.ta.toms.roms.search.criteria.impl.WarningNoticeCriteriaBO;

public interface WarningNoticeDAO extends DAO {
	
/*	public ArrayList<WarningNoticeBO> lookupWarningNotice(WarningNoticeCriteriaBO criteria);

*/	
	ArrayList<ScannedDocBO> lookupScannedDocsForWarningNoticeId(
			Integer warningNoticesId);


	ArrayList<DocumentViewBO> lookupWarningNotice(DocumentsCriteriaBO criteria);


	WarningNoticeBO lookupWarningNoticeById(Integer warningNoticeId);

	public WarningNoticeDO findWarningNoticeByRoadCheckOffenceOutcomeId(Integer roadCheckOffenceOutcomeId);


	Integer getAssociatedWarningNotice(Integer roadCheckOffenceOutcomeId);
}
