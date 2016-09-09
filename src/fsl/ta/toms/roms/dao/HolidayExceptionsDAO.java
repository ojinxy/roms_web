package fsl.ta.toms.roms.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import fsl.dao.DAO;
import fsl.ta.toms.roms.bo.ConfigurationBO;
import fsl.ta.toms.roms.bo.HolidayExceptionBO;
import fsl.ta.toms.roms.dataobjects.ConfigurationDO;
import fsl.ta.toms.roms.dataobjects.HolidayExceptionsDO;
import fsl.ta.toms.roms.exception.InvalidFieldException;
import fsl.ta.toms.roms.search.criteria.impl.ConfigurationCriteriaBO;
import fsl.ta.toms.roms.search.criteria.impl.HolidayExceptionsCriteriaBO;

/**
 * 
 * @author rbrooks
 * Created Date: Jul 16, 2013
 */
public interface HolidayExceptionsDAO extends DAO {

	public List<HolidayExceptionsDO> getHolidayExceptionReference(HashMap<String,String> filter,String status) throws InvalidFieldException;
	
	
	List<HolidayExceptionsDO> lookupHolidayExceptions(
			HolidayExceptionsCriteriaBO holidayExceptionsCriteriaBO);

	boolean holidayExceptionExists(Date holidayDate);

	boolean holidayExcDescriptionExists(HolidayExceptionBO holidayException);
}












