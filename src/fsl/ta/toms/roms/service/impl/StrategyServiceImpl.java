package fsl.ta.toms.roms.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import fsl.ta.toms.roms.bo.StrategyBO;
import fsl.ta.toms.roms.bo.StrategyRuleBO;
import fsl.ta.toms.roms.constants.Constants;
import fsl.ta.toms.roms.dao.DAOFactory;
import fsl.ta.toms.roms.dataobjects.AuditEntry;
import fsl.ta.toms.roms.dataobjects.CDPersonTypeDO;
import fsl.ta.toms.roms.dataobjects.EventAuditDO;
import fsl.ta.toms.roms.dataobjects.StatusDO;
import fsl.ta.toms.roms.dataobjects.StrategyDO;
import fsl.ta.toms.roms.dataobjects.StrategyRuleDO;
import fsl.ta.toms.roms.dataobjects.StrategyRuleKey;
import fsl.ta.toms.roms.search.criteria.impl.StrategyCriteriaBO;
import fsl.ta.toms.roms.service.ServiceFactory;
import fsl.ta.toms.roms.service.StrategyService;

@Transactional
public class StrategyServiceImpl implements StrategyService{

	private DAOFactory daoFactory;
	private ServiceFactory serviceFactory;
	
	public void setDaoFactory(DAOFactory daoFactory) {
		this.daoFactory = daoFactory;
	}
	
	public void setServiceFactory(ServiceFactory serviceFactory) {
		this.serviceFactory = serviceFactory;
	}

	@Override
	public List<StrategyBO> lookupStrategy(StrategyCriteriaBO strategyCriteriaBO) {
		List<StrategyDO> strategyDOs = new ArrayList<StrategyDO>();
		List<StrategyBO> strategyBOs = new ArrayList<StrategyBO>();
		
		strategyDOs=  daoFactory.getStrategyDAO().lookupStrategy(strategyCriteriaBO);
		
		if (strategyDOs == null || strategyDOs.isEmpty())
			return null;

		for (StrategyDO strategyDO : strategyDOs) {
			StrategyBO strategyBO = new StrategyBO(strategyDO);
			strategyBOs.add(strategyBO);
		}
		return strategyBOs;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW,rollbackFor={Exception.class,RuntimeException.class})
	public boolean saveStrategy(StrategyBO strategyBO,
			List<StrategyRuleBO> strategyRuleList) {
		
		StrategyDO strategyDO = new StrategyDO();
		AuditEntry auditEntry = new AuditEntry();
		Calendar calendar = Calendar.getInstance();
		Date currentDate = calendar.getTime();
		EventAuditDO eventAuditDO = new EventAuditDO();
		
		try{
			
			auditEntry.setCreateDTime(currentDate);
			auditEntry.setCreateUsername(strategyBO.getCurrentUsername());
			
			strategyDO.setDescription(strategyBO.getStrategyDescription());

			if(strategyBO.isVehicleRequired()==false){
				strategyDO.setVehicleRequired("N");
			}
			else{
				strategyDO.setVehicleRequired("Y");
			}
			
			if(strategyBO.isArteryRequired()==false){
				strategyDO.setArteryRequired("N");
			}
			else{
				strategyDO.setArteryRequired("Y");
			}
			
			
			StatusDO statusDO = new StatusDO();
			statusDO = daoFactory.getReasonDAO().find(StatusDO.class, Constants.Status.ACTIVE);
			strategyDO.setStatus(statusDO);
			strategyDO.setAuditEntry(auditEntry);
			
			Integer strategyId = daoFactory.getStrategyDAO().saveStrategyDO(strategyDO);
			//Set the strategy id to be set on the Event Audit
			strategyBO.setStrategyId(strategyId);
			eventAuditDO = createEventAuditRecord(strategyBO, Constants.EventCode.CREATE_REFERENCE_CODE);
			
			StrategyDO savedStrategy = daoFactory.getStrategyDAO().find(StrategyDO.class, strategyId);
			
			if(savedStrategy==null){
				throw new Exception();
			}
			//Save Strategy Rules
			StrategyRuleDO strategyRuleDO = new StrategyRuleDO();
			StrategyRuleKey strategyRuleKey = new StrategyRuleKey();
			CDPersonTypeDO personTypeDO;
			
			for(StrategyRuleBO ruleBO : strategyRuleList){
				strategyRuleDO = new StrategyRuleDO();
				strategyRuleKey = new StrategyRuleKey();
				personTypeDO = new CDPersonTypeDO();
				
				strategyRuleKey.setStrategy(savedStrategy);
				personTypeDO = daoFactory.getStrategyDAO().find(CDPersonTypeDO.class, ruleBO.getPersonTypeId());
				
				if(personTypeDO==null){
					throw new Exception();
				}
				strategyRuleKey.setPersonType(personTypeDO);
				
				strategyRuleDO.setStrategyRuleKey(strategyRuleKey);
				
				if(ruleBO.getMaximum()==null){
					strategyRuleDO.setMaximum(Constants.maxStrategyRuleValue);
				}
				else{
					strategyRuleDO.setMaximum(ruleBO.getMaximum());
				}
				
				if(ruleBO.getMinimum()==null){
					strategyRuleDO.setMinimum(0);
				}
				else{
					strategyRuleDO.setMinimum(ruleBO.getMinimum());
				}
				strategyRuleDO.setAuditEntry(auditEntry);
				
				daoFactory.getStrategyDAO().save(strategyRuleDO);
			}
			
			
			serviceFactory.getEventAuditService().saveEventAuditDO(eventAuditDO);
			
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 *This method populates the Event Audit record to be saved 
	 * @param strategyBO
	 * @param eventCode
	 * @return
	 * @throws Exception
	 */
	private EventAuditDO createEventAuditRecord(StrategyBO strategyBO, Integer eventCode)throws Exception{
		try{
		EventAuditDO eventAuditBO = new EventAuditDO();
		AuditEntry auditEntry = new AuditEntry();   
		auditEntry.setCreateUsername(strategyBO.getCurrentUsername());
		
		eventAuditBO.setEventCode(eventCode);	  
		
		
		
		eventAuditBO.setRefType1Code(Constants.EventRefTypeCode.REFERENCE_TABLE_NAME);
		eventAuditBO.setRefValue1(" roms_strategy");
			
		
		eventAuditBO.setComment("Strategy Description: " + strategyBO.getStrategyDescription());
	
		
	
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
	public boolean updateStrategy(StrategyBO strategyBO,
			List<StrategyRuleBO> strategyRuleList) {

		StrategyDO savedStrategyDO = new StrategyDO();
		Calendar calendar = Calendar.getInstance();
		Date currentDate = calendar.getTime();
		EventAuditDO eventAuditDO = new EventAuditDO();
		try{
			
			savedStrategyDO = daoFactory.getStrategyDAO().find(StrategyDO.class, strategyBO.getStrategyId());
			
			savedStrategyDO.setDescription(strategyBO.getStrategyDescription());

			if(strategyBO.isVehicleRequired()==false){
				savedStrategyDO.setVehicleRequired("N");
			}
			else{
				savedStrategyDO.setVehicleRequired("Y");
			}
			
			if(strategyBO.isArteryRequired()==false){
				savedStrategyDO.setArteryRequired("N");
			}
			else{
				savedStrategyDO.setArteryRequired("Y");
			}
				
			StatusDO statusDO = new StatusDO();
			statusDO = daoFactory.getITAExaminerDAO().find(StatusDO.class, strategyBO.getStatusId());
			if(statusDO!=null){
				if(!statusDO.getType().equalsIgnoreCase(Constants.StatusType.MAINTENANCE)){
					throw new Exception();
				}
				savedStrategyDO.setStatus(statusDO);
			}
			else{
				throw new Exception();
			}
			
		
			savedStrategyDO.getAuditEntry().setUpdateDTime(currentDate);
			savedStrategyDO.getAuditEntry().setUpdateUsername(strategyBO.getCurrentUsername());
			
			
			daoFactory.getStrategyDAO().update(savedStrategyDO);
			eventAuditDO = createEventAuditRecord(strategyBO, Constants.EventCode.UPDATE_REFERENCE_CODE);
			
			//Updates Strategy Rules
			StrategyRuleDO savedStrategyRuleDO = new StrategyRuleDO();
			
			for(StrategyRuleBO ruleBO : strategyRuleList){
				savedStrategyRuleDO = new StrategyRuleDO();
				
				savedStrategyRuleDO = daoFactory.getStrategyDAO().findStrategyRuleById(strategyBO.getStrategyId(), ruleBO.getPersonTypeId());
					
				if(ruleBO.getMaximum()==null){
					savedStrategyRuleDO.setMaximum(Constants.maxStrategyRuleValue);
				}
				else{
					savedStrategyRuleDO.setMaximum(ruleBO.getMaximum());
				}
				
				if(ruleBO.getMinimum()==null){
					savedStrategyRuleDO.setMinimum(0);
				}
				else{
					savedStrategyRuleDO.setMinimum(ruleBO.getMinimum());
				}
				savedStrategyRuleDO.getAuditEntry().setUpdateDTime(currentDate);
				savedStrategyRuleDO.getAuditEntry().setUpdateUsername(strategyBO.getCurrentUsername());
				
				daoFactory.getStrategyDAO().update(savedStrategyRuleDO);
			}
			
			
			serviceFactory.getEventAuditService().saveEventAuditDO(eventAuditDO);
			
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public StrategyDO findStrategyById(Integer strategyId) {
		StrategyDO strategyDO = new StrategyDO();

		strategyDO = daoFactory.getOffenceDAO().find(StrategyDO.class, strategyId);
		
		if(strategyDO!=null){
			return strategyDO;
		}
		
		return null;
	}

	@Override
	public List<StrategyRuleBO> getStrategyRulesForStrategy(Integer strategyId) {
		return daoFactory.getStrategyDAO().getStrategyRulesForStrategy(strategyId);
	}

	@Override
	public boolean strategyDescriptionExist(String description,
			Integer strategyId) {
		return daoFactory.getStrategyDAO().strategyDescriptionExist(description, strategyId);
	}

}
