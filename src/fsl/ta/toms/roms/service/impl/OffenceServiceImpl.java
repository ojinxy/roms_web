package fsl.ta.toms.roms.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import fsl.ta.toms.roms.bo.AssignedVehicleBO;
import fsl.ta.toms.roms.bo.OffenceBO;
import fsl.ta.toms.roms.bo.OffenceLawBO;
import fsl.ta.toms.roms.bo.OffenceParamBO;
import fsl.ta.toms.roms.constants.Constants;
import fsl.ta.toms.roms.dao.DAOFactory;
import fsl.ta.toms.roms.dao.OffenceDAO;
import fsl.ta.toms.roms.dao.RoadOperationDAO;
import fsl.ta.toms.roms.dataobjects.AssignedVehicleDO;
import fsl.ta.toms.roms.dataobjects.AssignedVehicleKey;
import fsl.ta.toms.roms.dataobjects.AuditEntry;
import fsl.ta.toms.roms.dataobjects.CDRoadCheckTypeDO;
import fsl.ta.toms.roms.dataobjects.EventAuditDO;
import fsl.ta.toms.roms.dataobjects.ExcerptParamMappingDO;
import fsl.ta.toms.roms.dataobjects.GoverningLawDO;
import fsl.ta.toms.roms.dataobjects.OffenceDO;
import fsl.ta.toms.roms.dataobjects.OffenceLawDO;
import fsl.ta.toms.roms.dataobjects.OffenceLawKey;
import fsl.ta.toms.roms.dataobjects.OffenceParamDO;
import fsl.ta.toms.roms.dataobjects.OffenceParamKey;
import fsl.ta.toms.roms.dataobjects.RoadOperationDO;
import fsl.ta.toms.roms.dataobjects.StatusDO;
import fsl.ta.toms.roms.dataobjects.TAVehicleDO;
import fsl.ta.toms.roms.dataobjects.TeamArteryDO;
import fsl.ta.toms.roms.dataobjects.TeamDO;
import fsl.ta.toms.roms.search.criteria.impl.OffenceCriteriaBO;
import fsl.ta.toms.roms.service.OffenceService;
import fsl.ta.toms.roms.service.ServiceFactory;

@Transactional
public class OffenceServiceImpl implements OffenceService{

	private DAOFactory daoFactory;
	private ServiceFactory serviceFactory;
	
	public void setDaoFactory(DAOFactory daoFactory) {
		this.daoFactory = daoFactory;
	}
	
	public void setServiceFactory(ServiceFactory serviceFactory) {
		this.serviceFactory = serviceFactory;
	}

	
	@Override
	public List<OffenceBO> lookupOffence(OffenceCriteriaBO offenceCriteriaBO) {
		List<OffenceDO> offenceDOs = new ArrayList<OffenceDO>();
		List<OffenceBO> offenceBOs = new ArrayList<OffenceBO>();
		
		offenceDOs= daoFactory.getOffenceDAO().lookupOffence(offenceCriteriaBO);
		if (offenceDOs == null || offenceDOs.isEmpty())
			return null;

		for (OffenceDO OffenceDO : offenceDOs) {
			OffenceBO offenceBO = new OffenceBO(OffenceDO);
			offenceBOs.add(offenceBO);
		}
		return offenceBOs;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW,rollbackFor={Exception.class,RuntimeException.class})
	public boolean saveOffence(OffenceBO offenceBO, List<OffenceParamBO> offenceParamList) {
		OffenceDO offenceDO = new OffenceDO();
		AuditEntry auditEntry = new AuditEntry();
		Calendar calendar = Calendar.getInstance();
		Date currentDate = calendar.getTime();
		EventAuditDO eventAuditDO = new EventAuditDO();
		
		try{
			
			auditEntry.setCreateDTime(currentDate);
			auditEntry.setCreateUsername(offenceBO.getCurrentUsername());
			
			offenceDO.setOffenceId(offenceBO.getOffenceId());
			offenceDO.setDescription(offenceBO.getOffenceDescription());
			offenceDO.setShortDescription(offenceBO.getShortDescription());
			offenceDO.setExcerpt(offenceBO.getExcerpt());
		
			
			
			StatusDO statusDO = new StatusDO();
			statusDO = daoFactory.getOffenceDAO().find(StatusDO.class, Constants.Status.ACTIVE);
			offenceDO.setStatus(statusDO);

			CDRoadCheckTypeDO cdRoadCheckTypeDO = new CDRoadCheckTypeDO();
			
			cdRoadCheckTypeDO = daoFactory.getOffenceDAO().find(CDRoadCheckTypeDO.class, offenceBO.getRoadCheckTypeId());
			if(cdRoadCheckTypeDO!=null){
				offenceDO.setRoadCheckType(cdRoadCheckTypeDO);
			}
			else{
				throw new Exception();
			}
			
			offenceDO.setAuditEntry(auditEntry);
			
			daoFactory.getOffenceDAO().save(offenceDO);
			
			OffenceDO savedOffenceDO = new OffenceDO();
			savedOffenceDO = daoFactory.getOffenceDAO().find(OffenceDO.class, offenceDO.getOffenceId());
			
			//Save Offence Law
			/*OffenceLawDO offenceLawDO;
			OffenceLawKey offenceLawKey;
			GoverningLawDO governingLawDO;
			for(OffenceLawBO offenceLaw : offenceLawList){
				offenceLawDO = new OffenceLawDO();
				offenceLawKey = new OffenceLawKey();
				governingLawDO = new GoverningLawDO();
				governingLawDO = daoFactory.getOffenceDAO().find(GoverningLawDO.class, offenceLaw.getLawId());
				
				if(governingLawDO==null){
					throw new Exception();
				}
				offenceLawKey.setOffence(savedOffenceDO);
				offenceLawKey.setGoverningLaw(governingLawDO);
				offenceLawDO.setOffenceLawKey(offenceLawKey);
				offenceLawDO.setAuditEntry(auditEntry);
				 
				daoFactory.getOffenceDAO().save(offenceLawDO);
			}
			*/
			//Save Offence Param
			OffenceParamDO offenceParamDO;
			OffenceParamKey offenceParamKey;
			ExcerptParamMappingDO excerptParamMappingDO;
			
			for(OffenceParamBO offenceParamBO : offenceParamList){
				offenceParamDO = new OffenceParamDO();
				offenceParamKey = new OffenceParamKey();
				excerptParamMappingDO = new ExcerptParamMappingDO();
				
				excerptParamMappingDO = daoFactory.getOffenceDAO().find(ExcerptParamMappingDO.class, offenceParamBO.getParamMapId());
				
				if(excerptParamMappingDO==null){
					throw new Exception();
				}
				
				offenceParamKey.setOffence(savedOffenceDO);
				offenceParamKey.setExcerptParamMapping(excerptParamMappingDO);
				offenceParamDO.setOffenceParamKey(offenceParamKey);
				offenceParamDO.setAuditEntry(auditEntry);
				
				daoFactory.getOffenceDAO().merge(offenceParamDO);
			}
			
			eventAuditDO = createEventAuditRecord(offenceBO, Constants.EventCode.CREATE_REFERENCE_CODE);
			
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
	 * @param offenceBO
	 * @param eventCode
	 * @return
	 * @throws Exception
	 */
	private EventAuditDO createEventAuditRecord(OffenceBO offenceBO, Integer eventCode)throws Exception{
		try{
		EventAuditDO eventAuditBO = new EventAuditDO();
		AuditEntry auditEntry = new AuditEntry();   
		auditEntry.setCreateUsername(offenceBO.getCurrentUsername());
		
		eventAuditBO.setEventCode(eventCode);	  
		
		
		
		eventAuditBO.setRefType1Code(Constants.EventRefTypeCode.REFERENCE_TABLE_NAME);
		eventAuditBO.setRefValue1(" roms_offence");
			
		eventAuditBO.setRefType2Code(Constants.EventRefTypeCode.REFERENCE_CODE);	
		//eventAuditBO.setRefType2Code(Constants.EventRefTypeCode.BADGE_TYPE);	
		eventAuditBO.setRefValue2(offenceBO.getOffenceId()+"");
	
		
		eventAuditBO.setComment("Offence Description " + offenceBO.getShortDescription() );
	
		eventAuditBO.setAuditEntry(auditEntry);
		
		return eventAuditBO;
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new Exception();
		}
	}

	/**
	 * This method return an Integer List of Saved Param Map Id for Offence
	 */
	private List<Integer> returnIntegerSavedOffenceParamList(List<OffenceParamDO> offenceParamList){
		List<Integer> integerList = new ArrayList<Integer>();
		
		for(OffenceParamDO offenceParam : offenceParamList){
			integerList.add(offenceParam.getOffenceParamKey().getExcerptParamMapping().getParamMapId());
		}
		
		return integerList;
	}
	
	/**
	 * This method return an Integer List of Selected Param Map Id for Offence
	 */
	private List<Integer> returnIntegerOffenceParamBOList(List<OffenceParamBO> offenceParamList){
		List<Integer> integerList = new ArrayList<Integer>();
		
		for(OffenceParamBO offenceParam : offenceParamList){
			integerList.add(offenceParam.getParamMapId());
		}
		
		return integerList;
	}
	
	/**
	 * This method return an Integer List of Saved Law Id for Offence
	 */
	private List<Integer> returnIntegerSavedOffenceLawList(List<OffenceLawDO> offenceLawList){
		List<Integer> integerList = new ArrayList<Integer>();
		
		for(OffenceLawDO offenceLaw : offenceLawList){
			integerList.add(offenceLaw.getOffenceLawKey().getGoverningLaw().getLawId());
		}
		
		return integerList;
	}
	
	/**
	 * This method return an Integer List of Selected Law Id for Offence
	 */
	private List<Integer> returnIntegerOffenceLawBOList(List<OffenceLawBO> offenceLawList){
		List<Integer> integerList = new ArrayList<Integer>();
		
		for(OffenceLawBO offenceLaw : offenceLawList){
			integerList.add(offenceLaw.getLawId());
		}
		
		return integerList;
	}

	
	/**
	 * This method returns am Integer List of Param Id to be Added to the roms_offence_param table
	 */
	private List<Integer> returnSelectedParamToAdd(List<Integer> selectedOffenceParamIntegerList, Integer offenceId){
		List<Integer> addList = null;
		List<Integer> savedOffenceParamIntegerList = null;
		List<OffenceParamBO> savedOffenceParamList = new ArrayList<OffenceParamBO>();
		
		
		
		savedOffenceParamList = daoFactory.getOffenceDAO().findOffenceParams(offenceId);
		if(savedOffenceParamList!=null){
			savedOffenceParamIntegerList = returnIntegerOffenceParamBOList(savedOffenceParamList);
			if(savedOffenceParamIntegerList!=null){
				addList = new ArrayList<Integer>();
				for(Integer paramId : selectedOffenceParamIntegerList){
					if(!savedOffenceParamIntegerList.contains(paramId)){
						addList.add(paramId);
					}
				}
			}
		}
		else{
			return selectedOffenceParamIntegerList;
		}
		return addList;
	}
	
	/**
	 * This method returns am Integer List of Param Id to be Deleted from the roms_offence_param table
	 */
	private List<Integer> returnSelectedOffenceParamToDelete(List<Integer> selectedOffenceParamIntegerList, Integer offenceId){
		List<Integer> deleteList = null;
		List<Integer> savedOffenceParamIntegerList = null;
		List<OffenceParamBO> savedOffenceParamList = new ArrayList<OffenceParamBO>();
		
		
		
		savedOffenceParamList = daoFactory.getOffenceDAO().findOffenceParams(offenceId);
		
		if(savedOffenceParamList!=null){
			savedOffenceParamIntegerList = returnIntegerOffenceParamBOList(savedOffenceParamList);
			
			if(selectedOffenceParamIntegerList==null){
				return savedOffenceParamIntegerList;
			}
			
			deleteList = new ArrayList<Integer>();
			for(Integer paramId : savedOffenceParamIntegerList){
				if(!selectedOffenceParamIntegerList.contains(paramId)){
					deleteList.add(paramId);
				}
			}
			
		}
		
		
		return deleteList;
	}
	
	
	/**
	 * This method returns am Integer List of Param Id to be Added to the roms_offence_param table
	 */
	private List<Integer> returnSelectedLawToAdd(List<Integer> selectedOffenceLawIntegerList, Integer offenceId){
		List<Integer> addList = null;
		List<Integer> savedOffenceLawIntegerList = null;
		List<OffenceLawBO> savedOffenceLawList = new ArrayList<OffenceLawBO>();
			
		
		savedOffenceLawList = daoFactory.getOffenceDAO().findOffenceLaws(offenceId);
		if(savedOffenceLawList!=null){
			savedOffenceLawIntegerList = returnIntegerOffenceLawBOList(savedOffenceLawList);
			if(savedOffenceLawIntegerList!=null){
				addList = new ArrayList<Integer>();
				for(Integer lawId : selectedOffenceLawIntegerList){
					if(!savedOffenceLawIntegerList.contains(lawId)){
						addList.add(lawId);
					}
				}
			}
		}
		else{
			return selectedOffenceLawIntegerList;
		}
		return addList;
	}
	
	/**
	 * This method returns am Integer List of Law Id to be Deleted from the roms_offence_law table
	 */
	private List<Integer> returnSelectedOffenceLawToDelete(List<Integer> selectedOffenceLawIntegerList, Integer offenceId){
		List<Integer> deleteList = null;
		List<Integer> savedOffenceLawIntegerList = null;
		List<OffenceLawBO> savedOffenceLawList = new ArrayList<OffenceLawBO>();
			
		
		savedOffenceLawList = daoFactory.getOffenceDAO().findOffenceLaws(offenceId);
		
		if(savedOffenceLawList!=null){
			savedOffenceLawIntegerList = returnIntegerOffenceLawBOList(savedOffenceLawList);
			
			if(selectedOffenceLawIntegerList==null){
				return savedOffenceLawIntegerList;
			}
			
			deleteList = new ArrayList<Integer>();
			for(Integer lawId : savedOffenceLawIntegerList){
				if(!selectedOffenceLawIntegerList.contains(lawId)){
					deleteList.add(lawId);
				}
			}
			
		}
		
		
		return deleteList;
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW,rollbackFor={Exception.class,RuntimeException.class})
	public boolean updateOffence(OffenceBO offenceBO, List<OffenceParamBO> offenceParamList) {
		OffenceDO savedOffenceDO= new OffenceDO();
		Calendar calendar = Calendar.getInstance();
		Date currentDate = calendar.getTime();
		EventAuditDO eventAuditDO = new EventAuditDO();
		
		AuditEntry auditEntry = new AuditEntry();
		
			
		auditEntry.setCreateDTime(currentDate);
		auditEntry.setCreateUsername(offenceBO.getCurrentUsername());
		try{	
			savedOffenceDO = daoFactory.getOffenceDAO().find(OffenceDO.class, offenceBO.getOffenceId());
			 
			savedOffenceDO.setDescription(offenceBO.getOffenceDescription());
			savedOffenceDO.setShortDescription(offenceBO.getShortDescription());
			savedOffenceDO.setExcerpt(offenceBO.getExcerpt());
				
			
			StatusDO statusDO = new StatusDO();
			statusDO = daoFactory.getITAExaminerDAO().find(StatusDO.class, offenceBO.getStatusId());
			if(statusDO!=null){
				if(!statusDO.getType().equalsIgnoreCase(Constants.StatusType.MAINTENANCE)){
					throw new Exception();
				}
				savedOffenceDO.setStatus(statusDO);
			}
			else{
				throw new Exception();
			}
			
			CDRoadCheckTypeDO cdRoadCheckTypeDO = new CDRoadCheckTypeDO();
			
			cdRoadCheckTypeDO = daoFactory.getOffenceDAO().find(CDRoadCheckTypeDO.class, offenceBO.getRoadCheckTypeId());
			if(cdRoadCheckTypeDO!=null){
				savedOffenceDO.setRoadCheckType(cdRoadCheckTypeDO);
			}
			else{
				throw new Exception();
			}
			
			
			savedOffenceDO.getAuditEntry().setUpdateDTime(currentDate);
			savedOffenceDO.getAuditEntry().setUpdateUsername(offenceBO.getCurrentUsername());
			
			
			daoFactory.getITAExaminerDAO().update(savedOffenceDO);
			
			
			
			//Add or Delete Offence Param
			List<Integer> selectedOffenceParamIntegerList = null;
			List<Integer> addOffenceParamIntegerList = null;
			List<Integer> deleteOffenceParamIntegerList;
			if(offenceParamList!=null){
				selectedOffenceParamIntegerList= returnIntegerOffenceParamBOList(offenceParamList);
				addOffenceParamIntegerList = returnSelectedParamToAdd(selectedOffenceParamIntegerList, savedOffenceDO.getOffenceId());
			}
			
			if(addOffenceParamIntegerList!=null && addOffenceParamIntegerList.size()>0){
				saveOffenceParam(addOffenceParamIntegerList, savedOffenceDO, auditEntry);
			}
			
			//Delete
			deleteOffenceParamIntegerList = returnSelectedOffenceParamToDelete(selectedOffenceParamIntegerList, savedOffenceDO.getOffenceId());
			if(deleteOffenceParamIntegerList!=null && deleteOffenceParamIntegerList.size()>0){
				deleteOffenceParam(deleteOffenceParamIntegerList, savedOffenceDO.getOffenceId());
			}
			
			//Add or Delete Offence Law
			/*List<Integer> selectedOffenceLawIntegerList = null;
			List<Integer> addOffenceLawIntegerList;
			List<Integer> deleteOffenceLawIntegerList;
			if(offenceLawList!=null){
				selectedOffenceLawIntegerList= returnIntegerOffenceLawBOList(offenceLawList);
			}
			addOffenceLawIntegerList = returnSelectedLawToAdd(selectedOffenceLawIntegerList, savedOffenceDO.getOffenceId());
			if(addOffenceLawIntegerList!=null && addOffenceLawIntegerList.size()>0){
				saveOffenceLaw(addOffenceLawIntegerList, savedOffenceDO, auditEntry);
			}
			
			//Delete
			deleteOffenceLawIntegerList = returnSelectedOffenceLawToDelete(selectedOffenceLawIntegerList, savedOffenceDO.getOffenceId());
			if(deleteOffenceLawIntegerList!=null && deleteOffenceLawIntegerList.size()>0){
				deleteOffenceLaw(deleteOffenceLawIntegerList, savedOffenceDO.getOffenceId());
			}*/
			
			
			
			eventAuditDO = createEventAuditRecord(offenceBO, Constants.EventCode.UPDATE_REFERENCE_CODE);
			
			serviceFactory.getEventAuditService().saveEventAuditDO(eventAuditDO);
			
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private void saveOffenceParam(List<Integer> addOffenceParamIntegerList, OffenceDO savedOffenceDO, AuditEntry auditEntry) throws Exception{
		OffenceDAO offenceDAO = daoFactory.getOffenceDAO(); 
	
		ExcerptParamMappingDO excerptParamMappingDO = new ExcerptParamMappingDO();
		OffenceParamDO offenceParamDO = new OffenceParamDO();
		OffenceParamKey offenceParamKey = new OffenceParamKey();
	
		for(Integer paramId : addOffenceParamIntegerList){
			excerptParamMappingDO = offenceDAO.find(ExcerptParamMappingDO.class, paramId);
			offenceParamKey = new OffenceParamKey();
			if(excerptParamMappingDO!=null){
				offenceParamKey.setExcerptParamMapping(excerptParamMappingDO);
			}
			else{
				throw new Exception();
			}
			offenceParamDO = new OffenceParamDO();
			
			offenceParamKey.setOffence(savedOffenceDO);
			offenceParamDO.setOffenceParamKey(offenceParamKey);
			offenceParamDO.setAuditEntry(auditEntry);
			
			offenceDAO.merge(offenceParamDO);
		}
	}
	
	private void deleteOffenceParam(List<Integer> deleteOffenceParamIntegerList, Integer offenceId) throws Exception{
		OffenceDAO offenceDAO = daoFactory.getOffenceDAO(); 
	
		OffenceParamDO offenceParamDO = new OffenceParamDO();
		
				
		
		for(Integer paramId : deleteOffenceParamIntegerList){
			offenceParamDO = offenceDAO.findOffenceParamsById(paramId, offenceId);
			if(offenceParamDO!=null){
				offenceDAO.delete(offenceParamDO);
			}
			else{
				throw new Exception();
			}
		}
	}
	

	private void saveOffenceLaw(List<Integer> addOffenceLawIntegerList, OffenceDO savedOffenceDO, AuditEntry auditEntry) throws Exception{
		OffenceDAO offenceDAO = daoFactory.getOffenceDAO(); 
	
		GoverningLawDO governingLawDO = new GoverningLawDO();
		OffenceLawDO offenceLawDO = new OffenceLawDO();
		OffenceLawKey offenceLawKey = new OffenceLawKey();
	
		for(Integer lawId : addOffenceLawIntegerList){
			governingLawDO = offenceDAO.find(GoverningLawDO.class, lawId);
			offenceLawKey = new OffenceLawKey();
			if(governingLawDO!=null){
				offenceLawKey.setGoverningLaw(governingLawDO);
			}
			else{
				throw new Exception();
			}
			offenceLawDO = new OffenceLawDO();
			
			offenceLawKey.setOffence(savedOffenceDO);
			offenceLawDO.setOffenceLawKey(offenceLawKey);
			offenceLawDO.setAuditEntry(auditEntry);
			
			offenceDAO.save(offenceLawDO);
		}
	}
	
	private void deleteOffenceLaw(List<Integer> deleteOffenceLawIntegerList, Integer offenceId) throws Exception{
		OffenceDAO offenceDAO = daoFactory.getOffenceDAO(); 
	
		OffenceLawDO offenceLawDO = new OffenceLawDO();
		
				
		
		for(Integer lawId : deleteOffenceLawIntegerList){
			offenceLawDO = offenceDAO.findOffenceLawsById(lawId, offenceId);
			if(offenceLawDO!=null){
				offenceDAO.delete(offenceLawDO);
			}
			else{
				throw new Exception();
			}
		}
	}
	
	@Override
	public OffenceDO findOffenceById(Integer offenceId) {
		OffenceDO offenceDO = new OffenceDO();

		offenceDO = daoFactory.getOffenceDAO().find(OffenceDO.class, offenceId);
		
		if(offenceDO!=null){
			return offenceDO;
		}
		
		return null;
	}

	@Override
	public boolean offenceDescriptionExist(String description, Integer offenceId) {
		return daoFactory.getOffenceDAO().offenceDescriptionExist(description, offenceId);
	}

	@Override
	public boolean offenceShortDescriptionExist(String shortDescription,
			Integer offenceId) {
		return daoFactory.getOffenceDAO().offenceShortDescriptionExist(shortDescription, offenceId);
	}

	@Override
	public List<OffenceParamBO> findOffenceParams(Integer offenceId) {
		List<OffenceParamBO> offenceParamBOList = new ArrayList<OffenceParamBO>();
		
		offenceParamBOList = daoFactory.getOffenceDAO().findOffenceParams(offenceId);
		
		if(offenceParamBOList==null || offenceParamBOList.size()<=0){
			return null;
		}
	
		
		return offenceParamBOList;
	}

	@Override
	public List<OffenceLawBO> findOffenceLaws(Integer offenceId) {
		List<OffenceLawBO> offenceLawBOList = new ArrayList<OffenceLawBO>();
		
		offenceLawBOList = daoFactory.getOffenceDAO().findOffenceLaws(offenceId);
		
		if(offenceLawBOList==null || offenceLawBOList.size()<=0){
			return null;
		}
	
		
		return offenceLawBOList;
	}

}
