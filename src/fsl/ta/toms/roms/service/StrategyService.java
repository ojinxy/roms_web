package fsl.ta.toms.roms.service;

import java.util.List;

import fsl.ta.toms.roms.bo.StrategyBO;
import fsl.ta.toms.roms.bo.StrategyRuleBO;
import fsl.ta.toms.roms.dataobjects.StrategyDO;
import fsl.ta.toms.roms.search.criteria.impl.StrategyCriteriaBO;

public interface StrategyService {

	public List<StrategyBO> lookupStrategy(StrategyCriteriaBO strategyCriteriaBO);
	
	public boolean saveStrategy(StrategyBO strategyBO, List<StrategyRuleBO> strategyRuleList);
	
	public boolean updateStrategy(StrategyBO strategyBO, List<StrategyRuleBO> strategyRuleList);
	
	public StrategyDO findStrategyById(Integer strategyId);
	
	public List<StrategyRuleBO> getStrategyRulesForStrategy(Integer strategyId);
	
	public boolean strategyDescriptionExist(String description, Integer strategyId);
}
