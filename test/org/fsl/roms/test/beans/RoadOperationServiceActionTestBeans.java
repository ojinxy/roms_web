package org.fsl.roms.test.beans;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import fsl.ta.toms.roms.bo.PersonBO;
import fsl.ta.toms.roms.bo.StrategyRuleBO;

@Service
public class RoadOperationServiceActionTestBeans
{
	
	public static int MINSTAFF = 2;
	
	public static int MAXSTAFF = 10; 
	
	@Bean
    public PersonBO testPerson() {
        PersonBO personBo = new PersonBO();
        personBo.setFirstName("Oneal");
        
        return personBo;
    }
    
    @Bean(name="testStrategyRuleBos")
    public List<StrategyRuleBO> testStrategyRuleBos() {
        List<StrategyRuleBO> stratRuleList = new ArrayList<StrategyRuleBO>();
        
        StrategyRuleBO stratRuleBOTA = new StrategyRuleBO("TA", MINSTAFF, MAXSTAFF);
        StrategyRuleBO stratRuleBOJP = new StrategyRuleBO("JP", MINSTAFF, MAXSTAFF);
        StrategyRuleBO stratRuleBOIT = new StrategyRuleBO("IE", MINSTAFF, MAXSTAFF);
        StrategyRuleBO stratRuleBOPO = new StrategyRuleBO("PO", MINSTAFF, MAXSTAFF);
        StrategyRuleBO stratRuleBOVE = new StrategyRuleBO("VE", MINSTAFF, MAXSTAFF);
        
        stratRuleList.add(stratRuleBOTA);
        stratRuleList.add(stratRuleBOJP);
        stratRuleList.add(stratRuleBOIT);
        stratRuleList.add(stratRuleBOPO);
        
        return stratRuleList;
    }
	
}
