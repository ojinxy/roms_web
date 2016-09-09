/**
 * Created By: oanguin
 * Date: May 6, 2013
 *
 */
package fsl.ta.toms.roms.dao;

import java.sql.SQLException;
import java.util.List;

import fsl.dao.DAO;
import fsl.ta.toms.roms.bo.BadgeBO;

/**
 * @author oanguin
 * Created Date: May 6, 2013
 */
public interface BadgeDAO extends DAO 
{
	/**
	 * @summary Plese note that the two input fields are mandatory as only one badge will be returned.
	 * @param badgeNo This is the number of the badge that you are searching for.
	 * @param badgeType This is the type of badge you are searching for
	 * @return <code>BadgeBO</code> The badge object which was found based on input
	 * @throws SQLException
	 */
	public BadgeBO getBadgeDetails(String badgeNo, String badgeType) throws SQLException; 
		
	/**
	 * @summary This function return a list of Badge Business Objects based on the inputs. Please note that
	 * 	first name and last name are mandatory fields.
	 * @param firstName
	 * @param midName
	 * @param lastName
	 * @param userName - The name of the system user that is doing this type of search.
	 * @return <code>List of BadgeBO</code>
	 * @throws Exception 
	 */
	public List<BadgeBO> getBadgeByPersonDetails(String firstName,String midName,String lastName, String userName) throws Exception;
}
