/**
 * Created By: oanguin
 * Date: May 3, 2013
 *
 */
package fsl.ta.toms.roms.service.impl;

import fsl.ta.toms.roms.bo.LMISApplicationBO;
import fsl.ta.toms.roms.bo.RoadLicenceBO;
import fsl.ta.toms.roms.dao.DAOFactory;
import fsl.ta.toms.roms.dao.RoadLicenceDAO;
import fsl.ta.toms.roms.exception.NoRecordFoundException;
import fsl.ta.toms.roms.exception.RequiredFieldMissingException;
import fsl.ta.toms.roms.service.LMISService;

/**
 * @author oanguin
 * Created Date: May 3, 2013
 */
public class LMISServiceImpl implements LMISService {

	private DAOFactory daoFactory;
	
	public void setDaoFactory(DAOFactory daoFactory) {
		this.daoFactory = daoFactory;
	}
	/* (non-Javadoc)
	 * @see fsl.ta.toms.roms.service.LMISService#getRoadLicenceDetails(java.lang.Integer)
	 */
	@Override
	public RoadLicenceBO getRoadLicenceDetails(Integer roadLicenceNo)
			throws RequiredFieldMissingException, NoRecordFoundException 
	{
		RoadLicenceDAO roadLicenceDAO = daoFactory.getRoadLicenceDAO();
		
		if(roadLicenceNo == null)
			throw new RequiredFieldMissingException();
		
		RoadLicenceBO roadLicenceBO = roadLicenceDAO.getRoadLicence(roadLicenceNo);
		
		if(roadLicenceBO == null)
			throw new NoRecordFoundException();
		
		return roadLicenceBO;
	}
	

	/**
	 * @author Rackenee Brooks
	 * Date: 12 December 2013
	 * @param plateRegNo
	 * @return RoadLicenceBO
	 * Created to retrieve road license information by plate number as well as road licence application info
	 */
	@Override
	public RoadLicenceBO getRoadLicenceDetails(String plateRegNo)
			throws RequiredFieldMissingException, NoRecordFoundException 
	{
		RoadLicenceDAO roadLicenceDAO = daoFactory.getRoadLicenceDAO();
		
		if(plateRegNo == null)
			throw new RequiredFieldMissingException();
		
		RoadLicenceBO roadLicenceBO = roadLicenceDAO.getRoadLicence(plateRegNo);
		
		return roadLicenceBO;
	}
	
	
	
	}
