package fsl.ta.toms.roms.service;

import java.util.ArrayList;
import java.util.List;

import fsl.ta.toms.roms.bo.DocumentViewBO;
import fsl.ta.toms.roms.bo.ScannedDocBO;
import fsl.ta.toms.roms.bo.WarningNoProsecutionBO;
import fsl.ta.toms.roms.documents.view.NoticeView;
import fsl.ta.toms.roms.exception.ErrorSavingException;
import fsl.ta.toms.roms.search.criteria.impl.DocumentsCriteriaBO;


public interface WarningNoProsecutionService 
{

	public Integer saveWarningNoProsecution(WarningNoProsecutionBO summons) throws ErrorSavingException;
	
	public WarningNoProsecutionBO updateWarningNoProsecution(WarningNoProsecutionBO warningNoProsecution) throws ErrorSavingException;
	
	public ArrayList<DocumentViewBO> lookupWarningNoProsecution(DocumentsCriteriaBO criteria);
	
	void processScannedWarningNoProsecutionDocuments(List<ScannedDocBO> warningNoProsecutionDocList);

	public WarningNoProsecutionBO getWarningNoProsecutionDetailsById(Integer warningNoProsecutionId);

	NoticeView generateWarningNoProsecution(
			WarningNoProsecutionBO warningNoProsecution);

	NoticeView reGenerateWarningNoProsecution(
			WarningNoProsecutionBO warningNoProsecution);

	NoticeView getNoticeView(WarningNoProsecutionBO warningNoProsecutionBO);
	
	
}
