/**
 * Created By: oanguin
 * Date: May 3, 2013
 *
 */
package fsl.ta.toms.roms.dao.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;

import fsl.ta.toms.roms.bo.LMISApplicationBO;
import fsl.ta.toms.roms.bo.RoadLicenceBO;
import fsl.ta.toms.roms.bo.RoadLicenceDetailsBO;
import fsl.ta.toms.roms.bo.RoadLicenceDriverConductorBO;
import fsl.ta.toms.roms.bo.RoadLicenceOwnerBO;
import fsl.ta.toms.roms.bo.VehicleOwnerBO;
import fsl.ta.toms.roms.dao.RoadLicenceDAO;
import fsl.ta.toms.roms.dataobjects.LMIS_licenceOwnerView;
import fsl.ta.toms.roms.dataobjects.LMIS_licenceView;
import fsl.ta.toms.roms.dataobjects.VehicleOwnerDO;
import fsl.ta.toms.roms.util.StringUtil;

/**
 * @author oanguin
 * Created Date: May 3, 2013
 */
@Transactional
public class RoadLicenceDAOImpl extends ParentDAOImpl implements RoadLicenceDAO 
{

	@Override
	public final Serializable save(Object entity) throws DataAccessException {
		 throw new UnsupportedOperationException("not supported");
	}

	@Override
	public final  void update(Object entity) throws DataAccessException {
		 throw new UnsupportedOperationException("not supported");
	}

	@Override
	public final void delete(Object entity) throws DataAccessException {
		 throw new UnsupportedOperationException("not supported");
	}
	
	
	@Override
	public RoadLicenceBO getRoadLicenceByPlateNo(String licencePlate) 
	{
		/*List<LMIS_licenceView> licences = this.hibernateTemplate.find("select l from LMIS_licenceView l where l.licPlate = '" + licencePlate + "'" );
		
		if(licences == null || licences.isEmpty())
		{
			return null;
		}
		else
		{
			//System.err.println(" more than one returned ");
			LMIS_licenceView licenceDO = licences.get(0);
			
			List<RoadLicenceOwnerBO> roadLicenceOwnerBOList = this.createLicenceOwnerBO(licenceDO.getLicenceNo());
			
			RoadLicenceBO roadLicenceBO = new RoadLicenceBO(licenceDO.getLicenceNo(),licenceDO.getLicType(),
					licenceDO.getIssueDate(),licenceDO.getExpiryDate(),licenceDO.getLicPlate(),
					licenceDO.getStatusDesc(),licenceDO.getRouteDesc(),licenceDO.getVehMakeDesc(),licenceDO.getModelDesc(),
					licenceDO.getModelYear(),roadLicenceOwnerBOList, licenceDO.getRouteStart(), licenceDO.getRouteEnd(), licenceDO.getSeatCapacity());
			
			return roadLicenceBO;
		}*/
		/*The above function call was changed due to ensure that document manager and road check are using the same code to get the road licence number.
		 * Related to discrepancy 217
		 * OAnguin - 25 August 2015*/
		return getRoadLicence(licencePlate);
		
		
	}
	
	
	 
	@Override
	public RoadLicenceBO getRoadLicence(Integer roadLicenceNo) 
	{
		LMIS_licenceView licenceDO = this.find(LMIS_licenceView.class, roadLicenceNo);
		
		if(licenceDO == null)
		{
			return null;
		}
		else
		{
			List<RoadLicenceOwnerBO> roadLicenceOwnerBOList = this.createLicenceOwnerBO(licenceDO.getLicenceOwners());
			
			RoadLicenceBO roadLicenceBO = new RoadLicenceBO(licenceDO.getLicenceNo(),licenceDO.getLicType(),
					licenceDO.getIssueDate(),licenceDO.getExpiryDate(),licenceDO.getLicPlate(),
					licenceDO.getStatusDesc(),licenceDO.getRouteDesc(),licenceDO.getVehMakeDesc(),licenceDO.getModelDesc(),
					licenceDO.getModelYear(),roadLicenceOwnerBOList, licenceDO.getRouteStart(), licenceDO.getRouteEnd(), licenceDO.getSeatCapacity());
			
			return roadLicenceBO;
		}
		
		
	}
	
	
	
	@Override
	@SuppressWarnings("unchecked")
	public RoadLicenceBO getRoadLicence(String plateRegNo) 
	{
		
		System.err.println("USING SP");
		Query query = this.hibernateTemplate.getSessionFactory().getCurrentSession().createSQLQuery(
				"execute procedure roms_sp_getRoadLicenceDetails(:plateRegNo)")
				.setParameter("plateRegNo", plateRegNo)
				.setResultTransformer(Transformers.aliasToBean(RoadLicenceDetailsBO.class));
		
		List<RoadLicenceDetailsBO> resultSet = query.list();
		List<RoadLicenceOwnerBO> licenceOwners = new ArrayList<RoadLicenceOwnerBO>();
		List<RoadLicenceDriverConductorBO> assignedDriverConductor = new ArrayList<RoadLicenceDriverConductorBO>() ;
		
		RoadLicenceBO resultRoadLicenceBO = new RoadLicenceBO();
		String infoKey = null;
		
		if(resultSet != null )
		{
			if(resultSet.size() != 0)
			{
				RoadLicenceDetailsBO roadLicenceDetFirst =  resultSet.get(0);
				
			
				//add application information
				LMISApplicationBO lmisApplicationBO = new LMISApplicationBO();
				lmisApplicationBO.setApplDate(roadLicenceDetFirst.getAppl_date());
				lmisApplicationBO.setApplNo(roadLicenceDetFirst.getAppl_no());
				lmisApplicationBO.setLicDesc(roadLicenceDetFirst.getApp_lic_desc());
				lmisApplicationBO.setLicPlate(plateRegNo);
				lmisApplicationBO.setStatusDesc(roadLicenceDetFirst.getState_desc());
				
				
				
				for (RoadLicenceDetailsBO roadLicenceDetailsBO : resultSet) {
					
					infoKey = roadLicenceDetailsBO.getInfo_key();
					
					if(StringUtil.isSet(infoKey))
					{
						
						if(infoKey.equalsIgnoreCase("B"))//badge details
						{
							//add driver/conductor
							RoadLicenceDriverConductorBO drivCond = new RoadLicenceDriverConductorBO();
							drivCond.setBadgeNumber(roadLicenceDetailsBO.getBadge_trn() != null? roadLicenceDetailsBO.getBadge_trn().toString(): "");
							drivCond.setBadgeType(roadLicenceDetailsBO.getBadge_type());
							drivCond.setFirstName(roadLicenceDetailsBO.getBadge_first_name());
							drivCond.setLastName(roadLicenceDetailsBO.getBadge_last_name());
							
							assignedDriverConductor.add(drivCond);
						}
					
					
						if(infoKey.equalsIgnoreCase("O"))//owner details
						{
							//add owner
							RoadLicenceOwnerBO ownerBO = new RoadLicenceOwnerBO();
							ownerBO.setFirstName(roadLicenceDetailsBO.getFirst_name());
							ownerBO.setHomePhoneNo(roadLicenceDetailsBO.getHome_phone_no());
							ownerBO.setLastName(roadLicenceDetailsBO.getLast_name());
							ownerBO.setMark(roadLicenceDetailsBO.getMark());
							ownerBO.setMobilePhoneNo(roadLicenceDetailsBO.getMobile_phone_no());
							ownerBO.setOwnerType(roadLicenceDetailsBO.getOwner_type());
							ownerBO.setParishCode(roadLicenceDetailsBO.getParish_desc());
							ownerBO.setPoBox(roadLicenceDetailsBO.getPo_box());
							ownerBO.setPostOffice(roadLicenceDetailsBO.getPost_office());
							ownerBO.setStreetName(roadLicenceDetailsBO.getStreet_name());
							ownerBO.setStreetNo(roadLicenceDetailsBO.getStreet_no());
							ownerBO.setTrn(roadLicenceDetailsBO.getTrn() != null? roadLicenceDetailsBO.getTrn().toString() : "");
							ownerBO.setWorkPhoneNo(roadLicenceDetailsBO.getWork_phone_no());
					
							licenceOwners.add(ownerBO);
					
						}
					}
				}
				
				
				resultRoadLicenceBO = new RoadLicenceBO(roadLicenceDetFirst.getLicence_no(),roadLicenceDetFirst.getLic_desc(),
						roadLicenceDetFirst.getIssue_date(),roadLicenceDetFirst.getExpiry_date(),
						plateRegNo,roadLicenceDetFirst.getStatus_desc(),roadLicenceDetFirst.getStatus_code(),
						roadLicenceDetFirst.getRoute_desc(),roadLicenceDetFirst.getVeh_make_desc(),roadLicenceDetFirst.getModel_desc(),
						roadLicenceDetFirst.getModel_year(),licenceOwners,
						roadLicenceDetFirst.getRoute_start_txt(),roadLicenceDetFirst.getRoute_end_txt(),roadLicenceDetFirst.getPas_seating(),
						roadLicenceDetFirst.getControl_no(),assignedDriverConductor,roadLicenceDetFirst.getFitness_no(),roadLicenceDetFirst.getDepot_desc(),roadLicenceDetFirst.getFitness_iss_date(),
						roadLicenceDetFirst.getFitness_exp_date(),roadLicenceDetFirst.getIns_comp_name(),roadLicenceDetFirst.getIns_iss_date(),
						roadLicenceDetFirst.getIns_exp_date());
				
				
				
				
				resultRoadLicenceBO.setLmisApplicationBO(lmisApplicationBO);
				
			}
			
			
		}
		
		
		
		return resultRoadLicenceBO;
		
		
	}
	
	/**
	 * @author Oneal Anguin
	 * Date: 3 May 2013
	 * @param licenceOwnerDO
	 * @return List of RoadLicenceOwnerBO
	 */
	public List<RoadLicenceOwnerBO> createLicenceOwnerBO(List<LMIS_licenceOwnerView> licenceOwnerDO)
	{
		
		List <RoadLicenceOwnerBO> licenceOwnerBOList = new ArrayList<RoadLicenceOwnerBO>();
		
		for(LMIS_licenceOwnerView roadLicenceOwner: licenceOwnerDO)
		{
			
			licenceOwnerBOList.add(new RoadLicenceOwnerBO(roadLicenceOwner.getLicenceNo(),
					roadLicenceOwner.getFirstName(),roadLicenceOwner.getLastName(), roadLicenceOwner.getTrnNbr()));
		}
		
		return licenceOwnerBOList;
	}
	
	/**
	 * @author Oneal Anguin
	 * Date: 3 May 2013
	 * @param licenceOwnerDO
	 * @return List of RoadLicenceOwnerBO
	 */
	public List<RoadLicenceOwnerBO> createLicenceOwnerBO(Integer licenceNo)
	{
		
		Criteria criteria = this.hibernateTemplate.getSessionFactory().getCurrentSession().createCriteria(LMIS_licenceOwnerView.class);
		
		criteria.add(Restrictions.eq("licenceNo", licenceNo));
		criteria.addOrder(Order.asc("lastName"));
		criteria.addOrder(Order.asc("firstName"));
		
		List<LMIS_licenceOwnerView> listOfOwner = (List<LMIS_licenceOwnerView>)criteria.list();
		
		List<RoadLicenceOwnerBO> licenceOwnerBOList = new ArrayList<RoadLicenceOwnerBO>();
		
		for(LMIS_licenceOwnerView roadLicenceOwner: listOfOwner)
		{
			
			licenceOwnerBOList.add(new RoadLicenceOwnerBO(roadLicenceOwner.getLicenceNo(),
					roadLicenceOwner.getFirstName(),roadLicenceOwner.getLastName()));
		}
		
		return licenceOwnerBOList;
	}
	
	
	/**
	 * Needed for document view screens 
	 */
	@Override
	public List<VehicleOwnerBO> findVehicleOwnersByPlateForDocumentView(String plateNo) {
	
		List<VehicleOwnerDO> vehicleDOList = new ArrayList<VehicleOwnerDO>();
		List<VehicleOwnerBO> vehicleBOList = new ArrayList<VehicleOwnerBO>();
		
		String queryString = "select distinct o from VehicleOwnerDO o" +
				" where o.vehicle.plateRegNo = :plateRegNo";
		
		vehicleDOList = hibernateTemplate.findByNamedParam(queryString, "plateRegNo", plateNo);
			//System.err.println(" new vehicle changes ");
		for(VehicleOwnerDO vehicle: vehicleDOList)
		{
			
			vehicleBOList.add(new VehicleOwnerBO(vehicle));
		}
		
		return vehicleBOList;
	}
	
	/**
	 * Needed for document view screens 
	 */
	@Override
	public List<VehicleOwnerBO> findVehicleOwnersByVehicleIDForDocumentView(Integer vehId) {
	
		List<VehicleOwnerDO> vehicleDOList = new ArrayList<VehicleOwnerDO>();
		List<VehicleOwnerBO> vehicleBOList = new ArrayList<VehicleOwnerBO>();
		
		String queryString = "select distinct o from VehicleOwnerDO o" +
				" where o.vehicle.vehicleId = :vehicleId";
		
		vehicleDOList = hibernateTemplate.findByNamedParam(queryString, "vehicleId", vehId);
			//System.err.println(" new vehicle changes ");
		for(VehicleOwnerDO vehicle: vehicleDOList)
		{
			
			vehicleBOList.add(new VehicleOwnerBO(vehicle));
		}
		
		return vehicleBOList;
	}
	
	@Override
	public RoadLicenceBO getRoadLicenceByPlateNoForDocumentView(String licencePlate) 
	{
		/*List<LMIS_licenceView> licences = this.hibernateTemplate.find("select l from LMIS_licenceView l where l.licPlate = '" + licencePlate + "'" );
		
		if(licences == null || licences.isEmpty())
		{
			return null;
		}
		else
		{
			//System.err.println(" more than one returned ");
			LMIS_licenceView licenceDO = licences.get(0);			
			
			RoadLicenceBO roadLicenceBO = new RoadLicenceBO(licenceDO.getLicenceNo(),licenceDO.getLicType(),
					licenceDO.getIssueDate(),licenceDO.getExpiryDate(),licenceDO.getLicPlate(),
					licenceDO.getStatusDesc(),licenceDO.getRouteDesc(),licenceDO.getVehMakeDesc(),licenceDO.getModelDesc(),
					licenceDO.getModelYear(),null, licenceDO.getRouteStart(), licenceDO.getRouteEnd(), licenceDO.getSeatCapacity());
			
			return roadLicenceBO;
		}*/
		/*The above function call was changed due to ensure that document manager and road check are using the same code to get the road licence number.
		 * Related to discrepancy 217
		 * OAnguin - 25 August 2015*/
		return getRoadLicence(licencePlate);
		
		
	}
	

	
	
		
}
