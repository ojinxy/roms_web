/**
 * Created By: oanguin
 * Date: May 7, 2013
 *
 */
package fsl.ta.toms.roms.service;


import java.util.List;

import fsl.ta.toms.roms.bo.BadgeBO;
import fsl.ta.toms.roms.exception.NoRecordFoundException;
import fsl.ta.toms.roms.exception.RequiredFieldMissingException;

/**
 * @author oanguin
 * Created Date: May 7, 2013
 */
public interface BIMSService 
{
	/**
	 * @summary This method gets badge details based on the parameters.
	 * @param badgeNo
	 * @param badgeType
	 * @return <code>A badge business object based on the search criteria.</code>
	 * @throws RequiredFieldMissingException
	 * @throws NoRecordFoundException
	 */
	public BadgeBO getBadgeDetails(String badgeNo, String badgeType) throws RequiredFieldMissingException,NoRecordFoundException;
	
	/**
	 * @summary This method gets a list of badge details based on the parameters.
	 * @param firstName
	 * @param midName
	 * @param lastName
	 * @param userName - The person doing this type of search.
	 * @return
	 * @throws RequiredFieldMissingException - First and last name are mandatory fields. Middle name is optional.
	 * @throws NoRecordFoundException
	 */
	public List<BadgeBO> getBadgeByPersonDetails(String firstName,String midName,String lastName, String userName) throws RequiredFieldMissingException,NoRecordFoundException;
}
