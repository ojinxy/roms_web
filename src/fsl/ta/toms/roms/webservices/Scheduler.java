package fsl.ta.toms.roms.webservices;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import fsl.ta.toms.roms.bo.ProcessControlBO;
import fsl.ta.toms.roms.exception.ErrorSavingException;
import fsl.ta.toms.roms.exception.NoRecordFoundException;
import fsl.ta.toms.roms.exception.RequiredFieldMissingException;
import fsl.ta.toms.roms.service.SchedulerService;
import fsl.ta.toms.roms.service.ServiceFactory;
import fsl.ta.toms.roms.util.StringUtil;


public class Scheduler extends SpringBeanAutowiringSupport{

	
	
	@Autowired
	private ServiceFactory serviceFactory;
	
	/**
	 * 
	 * @param currentUser
	 * @return
	 * @throws ErrorSavingException
	 * @throws RequiredFieldMissingException 
	 */
	public void updatePoliceOfficer(String currentUser) throws ErrorSavingException,NoRecordFoundException, RequiredFieldMissingException
	{
		
		//current user is required
		if(!StringUtil.isSet(currentUser))
		{
			throw new RequiredFieldMissingException("Current User is Required");
		}
		
		SchedulerService service = serviceFactory.getSchedulerService();
		
		service.updatePoliceOfficer(currentUser);
			
			
	}

	
	/**
	 * 
	 * @param currentUser
	 * @return
	 * @throws ErrorSavingException
	 * @throws RequiredFieldMissingException 
	 */
	public void updateTAStaff(String currentUser) throws ErrorSavingException,NoRecordFoundException, RequiredFieldMissingException
	{
		//current user is required
		if(!StringUtil.isSet(currentUser))
		{
			throw new RequiredFieldMissingException("Current User is Required");
		}
				
		
		//if no run done previously, update all
		//if run previously, only update modified records since last run date time
		
		try{
			SchedulerService service = serviceFactory.getSchedulerService();
			
			service.updateTAStaff(currentUser);
			
		}catch (Exception e)
		{
			e.printStackTrace();
			throw new ErrorSavingException("Error Updating TA Staff");
			
		}
	}
	
	

	/**
	 * 
	 * @param currentUser
	 * @return
	 * @throws ErrorSavingException
	 * @throws RequiredFieldMissingException 
	 */
	public void updatePlea(String currentUser) throws ErrorSavingException,NoRecordFoundException, RequiredFieldMissingException
	{
			//current user is required
			if(!StringUtil.isSet(currentUser))
			{
				throw new RequiredFieldMissingException("Current User is Required");
			}
			
			SchedulerService service = serviceFactory.getSchedulerService();
			
			service.updatePlea(currentUser);
		
	}
	
	
	/**
	 * 
	 * @param currentUser
	 * @return
	 * @throws ErrorSavingException
	 * @throws RequiredFieldMissingException 
	 */
	public void updateVerdict(String currentUser) throws ErrorSavingException,NoRecordFoundException, RequiredFieldMissingException
	{
			
			//current user is required
			if(!StringUtil.isSet(currentUser))
			{
				throw new RequiredFieldMissingException("Current User is Required");
			}
				
		
			SchedulerService service = serviceFactory.getSchedulerService();
			
			service.updateVerdict(currentUser);
		
	}
	
	
	
	/**
	 * 
	 * @param currentUser
	 * @return
	 * @throws ErrorSavingException
	 * @throws RequiredFieldMissingException 
	 */
	public void updateCourtRuling(String currentUser) throws ErrorSavingException,NoRecordFoundException, RequiredFieldMissingException
	{
			//current user is required
			if(!StringUtil.isSet(currentUser))
			{
				throw new RequiredFieldMissingException("Current User is Required");
			}
				
		
			SchedulerService service = serviceFactory.getSchedulerService();
			
			service.updateCourtRuling(currentUser);
			
	}
	
	
	
	public List<ProcessControlBO> getLasRunProcesses() throws ErrorSavingException,NoRecordFoundException
	{		
			List<ProcessControlBO> result = new ArrayList<ProcessControlBO>();
			
			result  = serviceFactory.getSchedulerService().getLasRunProcesses();
			
			if(result == null || result.isEmpty())
			{
				throw new NoRecordFoundException("No Processes Found");
			}
			
			return result;
	}
	
	
	
	
}
