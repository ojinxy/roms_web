package fsl.ta.toms.roms.dao;

import java.util.HashMap;
import java.util.List;

import fsl.dao.DAO;
import fsl.ta.toms.roms.bo.StrategyBO;
import fsl.ta.toms.roms.bo.StrategyRuleBO;
import fsl.ta.toms.roms.dataobjects.StrategyDO;
import fsl.ta.toms.roms.dataobjects.StrategyRuleDO;
import fsl.ta.toms.roms.exception.InvalidFieldException;
import fsl.ta.toms.roms.search.criteria.impl.StrategyCriteriaBO;

public interface StrategyDAO extends DAO
{
	public List<StrategyDO> getStrategyReference(HashMap<String,String> filter,String status) throws InvalidFieldException;
	
	public List<StrategyDO> lookupStrategy(StrategyCriteriaBO strategyCriteriaBO);
	
	public Integer saveStrategyDO(StrategyDO strategyDO);
	
	public List<StrategyRuleBO> getStrategyRulesForStrategy(Integer strategyId);
	
	public StrategyRuleDO findStrategyRuleById(Integer strategyId, String personTypeId);
	
	public boolean strategyDescriptionExist(String description, Integer strategyId);
}
