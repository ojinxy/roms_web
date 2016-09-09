package org.fsl.roms.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.fsl.roms.service.action.RoadOperationsServiceAction;
import org.fsl.roms.test.beans.RoadOperationServiceActionTestBeans;
import org.fsl.roms.test.config.AppConfig;
import org.fsl.roms.view.RoadOperationView;
import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.primefaces.model.DualListModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.webflow.test.MockRequestContext;
import static org.mockito.Mockito.*;
import fsl.ta.toms.roms.bo.PersonBO;
import fsl.ta.toms.roms.bo.StrategyBO;
import fsl.ta.toms.roms.ui.ScheduleMinMaxUi;
import org.mockito.internal.matchers.Contains;
import fsl.ta.toms.roms.bo.StrategyRuleBO;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = AppConfig.class, loader = AnnotationConfigContextLoader.class)
public class RoadOperationServiceActionTest
{
	@Autowired
	PersonBO testPerson;
	
	@Resource
	List<StrategyRuleBO> testStrategyRuleBos;
	
	@Test
	public void testRun()
	{
		assertEquals(testPerson.getFirstName(), "Oneal");
	}
	
	@Test
	public void test_getMinMaxMessage_Function()
	{
		MockHttpServletRequest request = new MockHttpServletRequest();
		
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(
				request));
		
		MockRequestContext mrc = new MockRequestContext();
		
		RoadOperationView roadOpView = mock(RoadOperationView.class);
		when(roadOpView.getListOfSelectedStrategyID()).thenReturn("1,2");
		when(roadOpView.getListOfStrategies()).thenReturn(new DualListModel(new ArrayList<StrategyBO>(),new ArrayList<StrategyBO>()));
		
		mrc.getFlowScope().put("operation", roadOpView);
		org.springframework.webflow.execution.RequestContextHolder
				.setRequestContext(mrc);
		
		RoadOperationsServiceAction roadOpServiceAct = new RoadOperationsServiceAction();
		
		RoadOperationsServiceAction roadOpServiceActSpy = spy(roadOpServiceAct);
		doReturn(testStrategyRuleBos).when(roadOpServiceActSpy)
				.getStrategyRulesHelper(mrc);
		doReturn(true).when(roadOpServiceActSpy)
			.vehicleRequired((List<StrategyBO>) any());
		
		String[] staffTitles = { "TA", "JP", "IE", "PO", "VE" };
		
		for (String title : staffTitles)
		{
			ScheduleMinMaxUi resultScheduleMinMaxUi = roadOpServiceActSpy
					.getMinMaxMessage(title);
			
			String titleMsg = "";
			
			switch (title)
			{
			case "TA":
				titleMsg = "TA Staff";
				break;
			case "JP":
				titleMsg = "JPs";
				break;
			case "IE":
				titleMsg = "ITA Examiners";
				break;
			case "PO":
				titleMsg = "Police Officers";
				break;
			case "VE":
				titleMsg = "Vehicles";
				break;
			}
			
			if(!title.equalsIgnoreCase("VE")){
			assertEquals(resultScheduleMinMaxUi.getMessage(), "A minimum of "
					+ RoadOperationServiceActionTestBeans.MINSTAFF
					+ " and a maximum of "
					+ RoadOperationServiceActionTestBeans.MAXSTAFF
					+ " " + titleMsg + " is required");
			}else{
				assertEquals(resultScheduleMinMaxUi.getMessage(), "At least one vehicle is required");
			}
		}
		
	}
	
}
