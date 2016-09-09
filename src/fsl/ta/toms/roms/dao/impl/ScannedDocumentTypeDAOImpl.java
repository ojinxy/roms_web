/**
 * Created By: oanguin
 * Date: Apr 22, 2013
 *
 */
package fsl.ta.toms.roms.dao.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import fsl.ta.toms.roms.dao.ScannedDocumentTypeDAO;
import fsl.ta.toms.roms.dataobjects.ScannedDocumentTypeDO;
import fsl.ta.toms.roms.exception.InvalidFieldException;

/**
 * @author oanguin Created Date: Apr 22, 2013
 */
public class ScannedDocumentTypeDAOImpl extends ParentDAOImpl implements ScannedDocumentTypeDAO {

	
	@SuppressWarnings("unchecked")
	@Override
	public List<ScannedDocumentTypeDO> getScannedDocumentTypeReference(HashMap<String, String> filter,	String status) throws InvalidFieldException 
	{
		List<ScannedDocumentTypeDO> docTypes = (List<ScannedDocumentTypeDO>) super.getReference(ScannedDocumentTypeDO.class, null, filter);
				
		Collections.sort(docTypes, new Comparator<ScannedDocumentTypeDO>() {
		    public int compare(ScannedDocumentTypeDO result1, ScannedDocumentTypeDO result2) {
		        return result1.getDocumentTypeDescription().toLowerCase().compareTo(result2.getDocumentTypeDescription().toLowerCase());
		    }
		});
		
		return docTypes;
	}


}
