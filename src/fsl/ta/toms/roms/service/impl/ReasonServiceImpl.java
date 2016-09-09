package fsl.ta.toms.roms.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import fsl.ta.toms.roms.bo.ReasonBO;
import fsl.ta.toms.roms.constants.Constants;
import fsl.ta.toms.roms.dao.DAOFactory;
import fsl.ta.toms.roms.dataobjects.AuditEntry;
import fsl.ta.toms.roms.dataobjects.EventAuditDO;
import fsl.ta.toms.roms.dataobjects.ReasonDO;
import fsl.ta.toms.roms.dataobjects.ReasonTypeDO;
import fsl.ta.toms.roms.dataobjects.StatusDO;
import fsl.ta.toms.roms.search.criteria.impl.ReasonCriteriaBO;
import fsl.ta.toms.roms.service.ReasonService;
import fsl.ta.toms.roms.service.ServiceFactory;

@Transactional
public class ReasonServiceImpl implements ReasonService{

	private DAOFactory daoFactory;
	private ServiceFactory serviceFactory;
	
	public void setDaoFactory(DAOFactory daoFactory) {
		this.daoFactory = daoFactory;
	}
	
	public void setServiceFactory(ServiceFactory serviceFactory) {
		this.serviceFactory = serviceFactory;
	}

	@Override
	public List<ReasonBO> lookupReason(ReasonCriteriaBO reasonCriteriaBO) {
		List<ReasonDO> reasonDOs = new ArrayList<ReasonDO>();
		List<ReasonBO> reasonBOs = new ArrayList<ReasonBO>();
		
		reasonDOs= daoFactory.getReasonDAO().lookupReason(reasonCriteriaBO);
		if (reasonDOs == null || reasonDOs.isEmpty())
			return null;

		for (ReasonDO reasonDO : reasonDOs) {
			ReasonBO reasonBO = new ReasonBO(reasonDO);
			reasonBOs.add(reasonBO);
		}
		return reasonBOs;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW,rollbackFor={Exception.class,RuntimeException.class})
	public boolean saveReason(ReasonBO reasonBO) {
		ReasonDO reasonDO = new ReasonDO();
		AuditEntry auditEntry = new AuditEntry();
		Calendar calendar = Calendar.getInstance();
		Date currentDate = calendar.getTime();
		EventAuditDO eventAuditDO = new EventAuditDO();
		
		try{
			
			auditEntry.setCreateDTime(currentDate);
			auditEntry.setCreateUsername(reasonBO.getCurrentUsername());
			
			reasonDO.setDescription(reasonBO.getReasonDescription());
			//reasonDO.setType(reasonBO.getType());
			reasonDO.setReasonType(this.daoFactory.getReasonDAO().load(ReasonTypeDO.class,reasonBO.getType()));
			
			//Set default value for is_visible
			reasonDO.setIsVisible(reasonBO.getIsVisible() == null ? Constants.YesNo.YES_LABEL_STR : reasonBO.getIsVisible());
			
			StatusDO statusDO = new StatusDO();
			statusDO = daoFactory.getReasonDAO().find(StatusDO.class, Constants.Status.ACTIVE);
			reasonDO.setStatus(statusDO);

			reasonDO.setAuditEntry(auditEntry);
			
			Integer reasonId = daoFactory.getReasonDAO().saveReasonDO(reasonDO);
			//Set the reason id to be set on the Event Audit
			reasonBO.setReasonId(reasonId);
			
			
			
			eventAuditDO = createEventAuditRecord(reasonBO, Constants.EventCode.CREATE_REFERENCE_CODE);
			
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
	 * @param reasonBO
	 * @param eventCode
	 * @return
	 * @throws Exception
	 */
	private EventAuditDO createEventAuditRecord(ReasonBO reasonBO, Integer eventCode)throws Exception{
		try{
		EventAuditDO eventAuditBO = new EventAuditDO();
		AuditEntry auditEntry = new AuditEntry();   
		auditEntry.setCreateUsername(reasonBO.getCurrentUsername());
		
		StringBuffer comment = new StringBuffer();
		
		eventAuditBO.setEventCode(eventCode);	  
		
		
		
		eventAuditBO.setRefType1Code(Constants.EventRefTypeCode.REFERENCE_TABLE_NAME);
		eventAuditBO.setRefValue1(" roms_reason");
			
		comment.append("Reason Description: " + reasonBO.getReasonDescription());
	
		comment.append("; Reason Type: " + reasonBO.getTypeDescription());
	
		eventAuditBO.setComment(comment.toString());
		
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
	public boolean updateReason(ReasonBO reasonBO) {
		ReasonDO savedReasonDO= new ReasonDO();
		Calendar calendar = Calendar.getInstance();
		Date currentDate = calendar.getTime();
		EventAuditDO eventAuditDO = new EventAuditDO();
		try{
			
			savedReasonDO = daoFactory.getReasonDAO().find(ReasonDO.class, reasonBO.getReasonId());
			savedReasonDO.setDescription(reasonBO.getReasonDescription());
			//savedReasonDO.setType(reasonBO.getType());
			savedReasonDO.setReasonType(this.daoFactory.getReasonDAO().load(ReasonTypeDO.class,reasonBO.getType()));
			
			//Set default value for is_visible
			savedReasonDO.setIsVisible(reasonBO.getIsVisible() == null ? Constants.YesNo.YES_LABEL_STR : reasonBO.getIsVisible());
			
			StatusDO statusDO = new StatusDO();
			statusDO = daoFactory.getITAExaminerDAO().find(StatusDO.class, reasonBO.getStatusId());
			if(statusDO!=null){
				if(!statusDO.getType().equalsIgnoreCase(Constants.StatusType.MAINTENANCE)){
					throw new Exception();
				}
				savedReasonDO.setStatus(statusDO);
			}
			else{
				throw new Exception();
			}
			
		
			savedReasonDO.getAuditEntry().setUpdateDTime(currentDate);
			savedReasonDO.getAuditEntry().setUpdateUsername(reasonBO.getCurrentUsername());
			
			
			daoFactory.getReasonDAO().update(savedReasonDO);
			eventAuditDO = createEventAuditRecord(reasonBO, Constants.EventCode.UPDATE_REFERENCE_CODE);
			
			serviceFactory.getEventAuditService().saveEventAuditDO(eventAuditDO);
			
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public ReasonDO findReasonById(Integer reasonId) {
		ReasonDO reasonDO = new ReasonDO();

		reasonDO = daoFactory.getOffenceDAO().find(ReasonDO.class, reasonId);
		
		if(reasonDO!=null){
			return reasonDO;
		}
		
		return null;
	}

	@Override
	public boolean reasonDescExistForSelectedType(ReasonBO reasonBO) {
		return daoFactory.getReasonDAO().reasonDescExistForSelectedType(reasonBO);
	}
}
