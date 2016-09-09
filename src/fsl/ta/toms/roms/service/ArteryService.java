package fsl.ta.toms.roms.service;

import java.util.HashMap;
import java.util.List;

import fsl.ta.toms.roms.bo.ArteryBO;
import fsl.ta.toms.roms.exception.ErrorSavingException;
import fsl.ta.toms.roms.exception.InvalidFieldException;
import fsl.ta.toms.roms.search.criteria.impl.ArteryCriteriaBO;

/**
 * 
 * @author jreid
 * Created Date: May 24, 2013
 */
public interface ArteryService {
	public List<ArteryBO> getArteryReference(HashMap<String,String> filter,String status) throws InvalidFieldException;


	List<ArteryBO> lookupArteries(ArteryCriteriaBO arteryCriteriaBO);

	boolean arteryExists(Integer arteryId);


	void updateArtery(ArteryBO arteryBO) throws ErrorSavingException, InvalidFieldException;


	Integer saveArtery(ArteryBO arteryBO) throws ErrorSavingException, InvalidFieldException;


	boolean descriptionExists(ArteryBO arteryBO);
	
	boolean locationExists(ArteryBO arteryBO);
	
	String usernameToFullName(String username);
	
	boolean shortDescriptionExists(ArteryBO arteryBO);

}
