/**
 * Created By: oanguin
 * Date: Apr 19, 2013
 *
 */
package fsl.ta.toms.roms.dao;


import java.util.HashMap;
import java.util.List;

import fsl.dao.DAO;
import fsl.ta.toms.roms.bo.ArteryBO;
import fsl.ta.toms.roms.dataobjects.ArteryDO;
import fsl.ta.toms.roms.exception.InvalidFieldException;
import fsl.ta.toms.roms.search.criteria.impl.ArteryCriteriaBO;


/**
 * @author oanguin
 * Created Date: Apr 19, 2013
 */
public interface ArteryDAO extends DAO {

	public List<ArteryDO> getArteryReference(HashMap<String,String> filter,String status) throws InvalidFieldException;

	List<ArteryDO> lookupArteryDO(ArteryCriteriaBO arteryCriteriaBO);

	boolean arteryExists(Integer arteryId);

	boolean arteryShortDescriptionExists(ArteryBO artery);

	boolean arteryDescriptionExists(ArteryBO artery);
	
	String usernameToFullName(String username);
	
	boolean arteryLocationExists(ArteryBO artery);
}
