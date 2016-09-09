package fsl.ta.toms.roms.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import fsl.ta.toms.roms.bo.TAVehicleBO;
import fsl.ta.toms.roms.bo.VehicleBO;
import fsl.ta.toms.roms.constants.Constants;
import fsl.ta.toms.roms.dao.DAOFactory;
import fsl.ta.toms.roms.dataobjects.AuditEntry;
import fsl.ta.toms.roms.dataobjects.EventAuditDO;
import fsl.ta.toms.roms.dataobjects.StatusDO;
import fsl.ta.toms.roms.dataobjects.TAVehicleDO;
import fsl.ta.toms.roms.dataobjects.VehicleDO;
import fsl.ta.toms.roms.search.criteria.impl.TAVehicleCriteriaBO;
import fsl.ta.toms.roms.service.ServiceFactory;
import fsl.ta.toms.roms.service.TAVehicleService;

@Transactional
public class TAVehicleServiceImpl implements TAVehicleService{

	private DAOFactory daoFactory;
	private ServiceFactory serviceFactory;
	
	public void setDaoFactory(DAOFactory daoFactory) {
		this.daoFactory = daoFactory;
	}
	
	public void setServiceFactory(ServiceFactory serviceFactory) {
		this.serviceFactory = serviceFactory;
	}

	@Override
	public List<TAVehicleBO> lookupTAVehicle(
			TAVehicleCriteriaBO taVehicleCriteriaBO) {
		return daoFactory.getTAVehicleDAO().lookupTAVehicle(taVehicleCriteriaBO);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW,rollbackFor={Exception.class,RuntimeException.class})
	public boolean saveTAVehicle(TAVehicleBO taVehicleBO) {
		TAVehicleDO taVehicleDO = new TAVehicleDO();
		AuditEntry auditEntry = new AuditEntry();
		Calendar calendar = Calendar.getInstance();
		Date currentDate = calendar.getTime();
		EventAuditDO eventAuditDO = new EventAuditDO();
		
		try{
			
			auditEntry.setCreateDTime(currentDate);
			auditEntry.setCreateUsername(taVehicleBO.getCurrentUsername());
			
			taVehicleBO.getVehicle().trimVehicleDetails();
			taVehicleBO.getVehicle().setCurrentUserName(taVehicleBO.getCurrentUsername());
			
			VehicleBO vehicleBO = serviceFactory.getRoadCompliancyService().saveVehicle(taVehicleBO.getVehicle());

			VehicleDO savedVehicleDO = new VehicleDO();
			savedVehicleDO = daoFactory.getJPDAO().find(VehicleDO.class, vehicleBO.getVehicleId());
			
			taVehicleDO.setVehicle(savedVehicleDO);
			taVehicleDO.setOfficeLocationCode(taVehicleBO.getOfficeLocationCode());
			
			StatusDO statusDO = new StatusDO();
			statusDO = daoFactory.getITAExaminerDAO().find(StatusDO.class, Constants.Status.ACTIVE);
			taVehicleDO.setStatus(statusDO);
			taVehicleDO.setAuditEntry(auditEntry);
			
			
			Integer taVehicleId = daoFactory.getTAVehicleDAO().saveTAVehicleDO(taVehicleDO);
			//Set the ta vehicle id to be set on the Event Audit
			taVehicleBO.setTaVehicleId(taVehicleId);
			
			eventAuditDO = createEventAuditRecord(taVehicleBO, Constants.EventCode.CREATE_REFERENCE_CODE);
			
			serviceFactory.getEventAuditService().saveEventAuditDO(eventAuditDO);
			
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * This method populates the Event Audit record to be saved 
	 * @param taVehicleBO
	 * @param eventCode
	 * @return
	 * @throws Exception
	 */
	private EventAuditDO createEventAuditRecord(TAVehicleBO taVehicleBO, Integer eventCode)throws Exception{
		try{
		EventAuditDO eventAuditBO = new EventAuditDO();
		AuditEntry auditEntry = new AuditEntry();   
		auditEntry.setCreateUsername(taVehicleBO.getCurrentUsername());
		
		eventAuditBO.setEventCode(eventCode);	  
		
		
		
		eventAuditBO.setRefType1Code(Constants.EventRefTypeCode.REFERENCE_TABLE_NAME);
		eventAuditBO.setRefValue1(" roms_ta_vehicle");
			
		
		eventAuditBO.setComment("Plate Number: " + taVehicleBO.getVehicle().getPlateRegNo());
	
		
	
		eventAuditBO.setAuditEntry(auditEntry);
		
		return eventAuditBO;
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new Exception();
		}
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW,rollbackFor={Exception.class,RuntimeException.class})
	public boolean updateTAVehicle(TAVehicleBO taVehicleBO) {
		TAVehicleDO savedTAVehicleDO = new TAVehicleDO();
		Calendar calendar = Calendar.getInstance();
		Date currentDate = calendar.getTime();
		EventAuditDO eventAuditDO = new EventAuditDO();
		
		try{
		
			taVehicleBO.getVehicle().trimVehicleDetails();
			taVehicleBO.getVehicle().setCurrentUserName(taVehicleBO.getCurrentUsername());
			
			serviceFactory.getRoadCompliancyService().saveVehicle(taVehicleBO.getVehicle());
			
			savedTAVehicleDO = daoFactory.getTAVehicleDAO().find(TAVehicleDO.class, taVehicleBO.getTaVehicleId());
			
			savedTAVehicleDO.setOfficeLocationCode(taVehicleBO.getOfficeLocationCode());
			
			StatusDO statusDO = new StatusDO();
			statusDO = daoFactory.getITAExaminerDAO().find(StatusDO.class, taVehicleBO.getStatusId());
			if(statusDO!=null){
				if(!statusDO.getType().equalsIgnoreCase(Constants.StatusType.MAINTENANCE)){
					throw new Exception();
				}
				savedTAVehicleDO.setStatus(statusDO);
			}
			else{
				throw new Exception();
			}
			savedTAVehicleDO.getAuditEntry().setUpdateDTime(currentDate);
			savedTAVehicleDO.getAuditEntry().setUpdateUsername(taVehicleBO.getCurrentUsername());
			
			
			daoFactory.getTAVehicleDAO().update(savedTAVehicleDO);
			eventAuditDO = createEventAuditRecord(taVehicleBO, Constants.EventCode.UPDATE_REFERENCE_CODE);
			
			serviceFactory.getEventAuditService().saveEventAuditDO(eventAuditDO);
			
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean taVehicleExistWithVehicleId(VehicleBO vehicleBO) {

		VehicleDO vehicleDO = new VehicleDO();
		vehicleDO = serviceFactory.getRoadCompliancyService().vehicleExists(vehicleBO);
		if(vehicleDO!=null){
			TAVehicleDO taVehicleDO = new TAVehicleDO();
			taVehicleDO = daoFactory.getTAVehicleDAO().findTAVehicleByVehicleId(vehicleDO.getVehicleId());
			
			if(taVehicleDO!=null){
				return true;
			}
		}
		
		return false;

	}
	
	

	@Override
	public TAVehicleDO findTAVehicleById(Integer taVehicleId) {
		TAVehicleDO taVehicleDO = new TAVehicleDO();

		taVehicleDO = daoFactory.getITAExaminerDAO().find(TAVehicleDO.class, taVehicleId);
		
		if(taVehicleDO!=null){
			return taVehicleDO;
		}
		
		return null;
	}

	@Override
	public boolean activeTAVehicleWithPlateRegNoExists(String plateRegNo) {
		return daoFactory.getTAVehicleDAO().activeTAVehicleWithPlateRegNoExists(plateRegNo);
	}

	@Override
	public boolean taVehiclePlateRegNoExists(String plateRegNo) {
		return daoFactory.getTAVehicleDAO().taVehiclePlateRegNoExists(plateRegNo);
	}

}
