package fsl.ta.toms.roms.service;

import java.util.HashMap;
import java.util.List;

import fsl.ta.toms.roms.bo.WreckingCompanyBO;
import fsl.ta.toms.roms.exception.ErrorSavingException;
import fsl.ta.toms.roms.exception.InvalidFieldException;
import fsl.ta.toms.roms.search.criteria.impl.WreckingCompanyCriteriaBO;

/**
 * 
 * @author jreid
 * Created Date: May 20, 2013
 * 
 */
public interface WreckingCompanyService 
{
	public List<WreckingCompanyBO> getWreckingCompanyReference(HashMap<String,String> filter,String status) throws InvalidFieldException;

	List<WreckingCompanyBO> lookupWreckingCompany(
			WreckingCompanyCriteriaBO wreckingCompCriteriaBO);

	boolean wreckingCompanyExists(Integer wreckingCompanyId);

	public void updateWreckingCompany(WreckingCompanyBO wreckingCompanyBO) throws ErrorSavingException;

	public Integer saveWreckingCompany(WreckingCompanyBO wreckingCompanyBO) throws ErrorSavingException;

	boolean companyNameExists(WreckingCompanyBO wreckingCompanyBO);
	
	boolean trnBranchExists(WreckingCompanyBO wreckingCompanyBO);

	boolean shortDescriptionExists(WreckingCompanyBO wreckingCompanyBO);
}
