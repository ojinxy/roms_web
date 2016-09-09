package fsl.ta.toms.roms.service;

import java.util.ArrayList;
import java.util.List;

import fsl.ta.toms.roms.bo.DocumentViewBO;
import fsl.ta.toms.roms.bo.ScannedDocBO;
import fsl.ta.toms.roms.bo.SummonsBO;
import fsl.ta.toms.roms.documents.view.SummonsDischargeAndReleaseFormView;
import fsl.ta.toms.roms.documents.view.SummonsView;
import fsl.ta.toms.roms.exception.ErrorSavingException;
import fsl.ta.toms.roms.exception.InvalidFieldException;
import fsl.ta.toms.roms.exception.RequiredFieldMissingException;
import fsl.ta.toms.roms.search.criteria.impl.DocumentsCriteriaBO;

public interface SummonsService {

	/**
	 * This method saves the summons
	 * @param summons
	 * @return
	 * @throws ErrorSavingException
	 */
	public Integer saveSummons(SummonsBO summons) throws ErrorSavingException;
	
	/**
	 * This method updates the summons
	 * @param summons
	 * @return
	 * @throws ErrorSavingException
	 * @throws InvalidFieldException
	 */
	public SummonsBO updateSummons(SummonsBO summons) throws ErrorSavingException, InvalidFieldException;
	
	/**
	 * This method lookups summons based on the criteria
	 * @param criteria
	 * @return
	 */
	public ArrayList<DocumentViewBO> lookupSummons(DocumentsCriteriaBO criteria);
	
	/**
	 * This method saves the summonsScannedDocuments by iterating through the list of items
	 * @param summonsDocList
	 */
	void processScannedSummonsDocuments(List<ScannedDocBO> summonsDocList);

	/**
	 * This method pulls a summons and the associated documents based on the summons ID
	 * @param summonsId
	 * @return
	 */
	public SummonsBO getSummonsDetailsById(Integer summonsId);



	SummonsDischargeAndReleaseFormView generateSummonsDischargeForm(
			SummonsBO summonsBO) throws ErrorSavingException;

	SummonsView generateSummons(SummonsBO summons);

	SummonsView reGenerateSummons(SummonsBO summons) throws ErrorSavingException;


	void addJPDetailsFromRoadCheck(List<Integer> summonsIds, Integer jpIdNo, boolean isHandHeld, String updateUser) throws ErrorSavingException, InvalidFieldException, RequiredFieldMissingException;

	SummonsView getSummonsView(SummonsBO summonsBO);
	
	
}
