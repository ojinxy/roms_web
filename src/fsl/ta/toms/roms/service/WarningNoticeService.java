package fsl.ta.toms.roms.service;

import java.util.ArrayList;
import java.util.List;

import fsl.ta.toms.roms.bo.DocumentViewBO;
import fsl.ta.toms.roms.bo.ScannedDocBO;
import fsl.ta.toms.roms.bo.WarningNoticeBO;
import fsl.ta.toms.roms.documents.view.NoticeView;
import fsl.ta.toms.roms.exception.ErrorSavingException;
import fsl.ta.toms.roms.search.criteria.impl.DocumentsCriteriaBO;

public interface WarningNoticeService {

	public Integer saveWarningNotice(WarningNoticeBO summons) throws ErrorSavingException;
	
	public WarningNoticeBO updateWarningNotice(WarningNoticeBO summons) throws ErrorSavingException;
	
	public ArrayList<DocumentViewBO> lookupWarningNotice(DocumentsCriteriaBO criteria);
	
	void processScannedWarningNoticeDocuments(List<ScannedDocBO> summonsDocList);

	public WarningNoticeBO getWarningNoticeDetailsById(Integer summonsId);

	NoticeView generateWarningNotice(WarningNoticeBO warningNotice);

	NoticeView reGenerateWarningNotice(WarningNoticeBO warningNotice);

	NoticeView getNoticeView(WarningNoticeBO warningNoticeBO);

	public String getAssociatedWarningNotice(Integer roadCheckOffenceOutcomeId);
	
	
}
