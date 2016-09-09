/**
 * Created By: oanguin
 * Date: May 3, 2013
 *
 */
package fsl.ta.toms.roms.service;

import fsl.ta.toms.roms.bo.RoadLicenceBO;
import fsl.ta.toms.roms.exception.NoRecordFoundException;
import fsl.ta.toms.roms.exception.RequiredFieldMissingException;

/**
 * @author oanguin
 * Created Date: May 3, 2013
 */
public interface LMISService 
{
	public RoadLicenceBO getRoadLicenceDetails(Integer roadLicenceNo) throws RequiredFieldMissingException,NoRecordFoundException;

	RoadLicenceBO getRoadLicenceDetails(String plateRegNo)
			throws RequiredFieldMissingException, NoRecordFoundException;
}
