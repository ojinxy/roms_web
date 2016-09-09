/**
 * Created By: oanguin
 * Date: Apr 26, 2013
 *
 */
package fsl.ta.toms.roms.webservices;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import fsl.ta.toms.roms.bo.CourtAppearanceBO;
import fsl.ta.toms.roms.bo.CourtCaseBO;
import fsl.ta.toms.roms.exception.ErrorSavingException;
import fsl.ta.toms.roms.exception.InvalidFieldException;
import fsl.ta.toms.roms.exception.NoRecordFoundException;
import fsl.ta.toms.roms.exception.RequiredFieldMissingException;
import fsl.ta.toms.roms.search.criteria.impl.CourtAppearanceCriteriaBO;
import fsl.ta.toms.roms.search.criteria.impl.CourtCaseCriteriaBO;
import fsl.ta.toms.roms.service.CourtCaseService;
import fsl.ta.toms.roms.service.RecordingCourtOutcomeService;
import fsl.ta.toms.roms.service.ServiceFactory;
import fsl.ta.toms.roms.util.DateUtils;
import fsl.ta.toms.roms.util.StringUtil;

/**
 * @author oanguin
 * Created Date: Apr 26, 2013
 */
public class RecordingCourtOutcome_ extends SpringBeanAutowiringSupport
		implements Serializable 
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Autowired
	private ServiceFactory serviceFactory;

	public RecordingCourtOutcome_()
	{
		super();
		
	}
	
	public void createNewCourtAppearance(CourtAppearanceBO courtAppearance) throws ErrorSavingException, RequiredFieldMissingException
	{
		
		this.checkForRequiredFields(courtAppearance);
		
		RecordingCourtOutcomeService recordCourtAppearanceServ = this.serviceFactory.getRecordingCourtOutcomeService();
		
		try
		{
			recordCourtAppearanceServ.createNewCourtAppearance(courtAppearance);
		}
		catch(Exception exe)
		{
			exe.printStackTrace();
			if(exe.getClass() == ErrorSavingException.class || exe.getClass() == RequiredFieldMissingException.class)
				throw new ErrorSavingException(exe.getMessage());
			else
				throw new ErrorSavingException();
		}
	}

	public void updateCourtAppearance(CourtAppearanceBO currentCourtAppearance,CourtAppearanceBO newCourtAppearance) throws ErrorSavingException, RequiredFieldMissingException,InvalidFieldException 
	{
		
		
		//this.checkForRequiredFields(currentCourtAppearance);
		
		
		if(currentCourtAppearance.getCourtAppearanceId() == null || currentCourtAppearance.getCourtAppearanceId() <= 0)
			throw new RequiredFieldMissingException();
	/*	
		if(newCourtAppearance != null)
			this.checkForRequiredFields(newCourtAppearance);
	*/	
		
		
		RecordingCourtOutcomeService recordCourtAppearanceServ = this.serviceFactory.getRecordingCourtOutcomeService();
		
		try
		{
			recordCourtAppearanceServ.updateCourtAppearance(currentCourtAppearance, newCourtAppearance);
		}
		catch(Exception exe)
		{
			exe.printStackTrace();
			if(exe.getClass() == ErrorSavingException.class || exe.getClass() == RequiredFieldMissingException.class)
				throw new ErrorSavingException(exe.getMessage());
			
			if(exe.getClass() == InvalidFieldException.class)
				throw (InvalidFieldException)exe;
			
			else
				throw new ErrorSavingException();
		}
	}
	
	private void checkForRequiredFields(CourtAppearanceBO courtAppearance) throws RequiredFieldMissingException, ErrorSavingException 
	{
		if(courtAppearance == null)
			throw new RequiredFieldMissingException("Court Appearance is required.");
		
		
		if(courtAppearance.getCourtDate() == null)
			throw new RequiredFieldMissingException("Court Date is required.");
		
		if(! StringUtil.isSet(courtAppearance.getCourtTime()))
				throw new RequiredFieldMissingException("Court Time is required.");
		
		if(! StringUtil.isSet(courtAppearance.getCurrentUserName()))
			throw new RequiredFieldMissingException("Current User is required.");
		
		if(courtAppearance.getCourtCaseId() == null)
			throw new RequiredFieldMissingException("Court Case Id is required.");
		
		Calendar outputDate = Calendar.getInstance();
		
		outputDate.setTime(courtAppearance.getCourtDate());
		
		if(! DateUtils.isWeekDay(outputDate))
			throw new ErrorSavingException("Court date must be a week day.");
		
	}
	
	public List<CourtAppearanceBO> lookupCourtAppearance(CourtAppearanceCriteriaBO courtAppearanceCriteria) throws RequiredFieldMissingException, NoRecordFoundException, InvalidFieldException
	{
		
		if(courtAppearanceCriteria == null)
			throw new RequiredFieldMissingException();
		
		/*Check if a time is entered that then a courtAppearance date must also be set.*/
		if(StringUtil.isSet(courtAppearanceCriteria.getCourtTime()))
		{
			if(courtAppearanceCriteria.getCourtDate() == null)
				throw new RequiredFieldMissingException("If you are searching by time you must enter a date.");
		}
			
		RecordingCourtOutcomeService recordCourtAppearanceServ = this.serviceFactory.getRecordingCourtOutcomeService();
		
		try
		{
			List<CourtAppearanceBO> CourtAppearanceBOList = recordCourtAppearanceServ.lookUpCourtAppearance(courtAppearanceCriteria);
			
			if(CourtAppearanceBOList == null || CourtAppearanceBOList.size() < 1)
				throw new NoRecordFoundException();
			
			return CourtAppearanceBOList;
		}
		catch(Exception exe)
		{
			if(exe.getClass() == NoRecordFoundException.class)
				throw new NoRecordFoundException();
			
			if(exe.getClass() == InvalidFieldException.class)
				throw (InvalidFieldException)exe;
			
			exe.printStackTrace();
			
			return null;
		}
	}
	
	public void createCourtCase(CourtCaseBO courtCaseBO) throws ErrorSavingException, RequiredFieldMissingException
	{
		CourtCaseService courtCaseService = this.serviceFactory.getCourtCaseService();
		
		try
		{
			courtCaseService.createCourtCase(courtCaseBO);
		}
		catch(Exception exe)
		{
			exe.printStackTrace();
			if(exe.getClass() == ErrorSavingException.class)
				throw (ErrorSavingException)exe;
			else if(exe.getClass() == RequiredFieldMissingException.class)
				throw (RequiredFieldMissingException)exe;
			else
				throw new ErrorSavingException();
		}
	}
	
	public void updateCourtCase(CourtCaseBO courtCaseBO) throws ErrorSavingException, RequiredFieldMissingException
	{
		CourtCaseService courtCaseService = this.serviceFactory.getCourtCaseService();
		
		try
		{
			courtCaseService.updateCourtCase(courtCaseBO);
		}
		catch(Exception exe)
		{
			exe.printStackTrace();
			if(exe.getClass() == ErrorSavingException.class)
				throw (ErrorSavingException)exe;
			else if(exe.getClass() == RequiredFieldMissingException.class)
				throw (RequiredFieldMissingException)exe;
			else
				throw new ErrorSavingException();
			
			
		}
	}
	
	public List<CourtCaseBO> lookUpCourtCase(CourtCaseCriteriaBO criteria) throws NoRecordFoundException, RequiredFieldMissingException
	{
		CourtCaseService courtCaseService = this.serviceFactory.getCourtCaseService();
		
		return courtCaseService.lookUpCourtCase(criteria);
	}
}
