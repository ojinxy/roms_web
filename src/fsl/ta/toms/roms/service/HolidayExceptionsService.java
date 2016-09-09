package fsl.ta.toms.roms.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import fsl.ta.toms.roms.bo.ConfigurationBO;
import fsl.ta.toms.roms.bo.HolidayExceptionBO;
import fsl.ta.toms.roms.exception.ErrorSavingException;
import fsl.ta.toms.roms.exception.InvalidFieldException;
import fsl.ta.toms.roms.search.criteria.impl.ConfigurationCriteriaBO;
import fsl.ta.toms.roms.search.criteria.impl.HolidayExceptionsCriteriaBO;
/**
 * 
 * @author rbrooks
 * Created Date: Jul 16, 2013
 */
public interface HolidayExceptionsService {
	public List<HolidayExceptionBO> getHolidayExceptionsReference(HashMap<String,String> filter,String status) throws InvalidFieldException;

	public boolean holidayExceptionExists(Date holidayDate);


	List<HolidayExceptionBO> lookupHolidayExceptions(
			HolidayExceptionsCriteriaBO holidayExceptionsCriteriaBO);

	Date saveHolidayException(HolidayExceptionBO holidayExceptionsBO) throws ErrorSavingException;

	void updateHolidayExceptions(HolidayExceptionBO holidayExceptionsBO) throws ErrorSavingException;

	boolean descriptionExists(HolidayExceptionBO holidayExceptionsBO);

}
