/**
 * Created By: oanguin
 * Date: Apr 23, 2013
 *
 */
package fsl.ta.toms.roms.dao;

import org.hibernate.Session;

import fsl.dao.DAO;

/**
 * @author oanguin
 * Created Date: Apr 23, 2013
 */
public interface ParentDAO 
 extends DAO{
	public Session getSession();

	
}
