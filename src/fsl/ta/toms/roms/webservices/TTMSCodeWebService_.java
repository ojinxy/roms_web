package fsl.ta.toms.roms.webservices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import fsl.ta.toms.roms.constants.Constants.EventCode;
import fsl.ta.toms.roms.constants.Constants.TTMSCodeTypes;
import fsl.ta.toms.roms.exception.NoRecordFoundException;
import fsl.ta.toms.roms.service.ServiceFactory;
import fsl.ta.toms.roms.service.TTMSCodeService;

public class TTMSCodeWebService_ extends SpringBeanAutowiringSupport{

	
	
	@Autowired
	private ServiceFactory serviceFactory;
	
	
	public String updateTTMSCode(String currentUser, String ttmsCodeType) throws NoRecordFoundException
	{
		TTMSCodeService service = serviceFactory.getTTMSCodeService();
		
		Integer event_code = null;
		
		if(ttmsCodeType.equals(TTMSCodeTypes.POLICE_OFFICER))
			event_code = EventCode.UPDATE_POLICE_OFFICER;
		
		if(ttmsCodeType.equals(TTMSCodeTypes.PLEA))
			event_code = EventCode.UPDATE_PLEA;
		
		if(ttmsCodeType.equals(TTMSCodeTypes.COURT_RULING))
			event_code = EventCode.UPDATE_COURT_RULING;
		
		if(ttmsCodeType.equals(TTMSCodeTypes.VERDICT))
			event_code = EventCode.UPDATE_VERDICT;
		
		String results = service.updateTTMSCode(currentUser, ttmsCodeType,event_code);
		return results;
	}
	
	
	
	public String getTTMSCodeDescription(String currentUser, String ttmsCodeType, String code) throws NoRecordFoundException
	{
		TTMSCodeService service = serviceFactory.getTTMSCodeService();
		
		String description = service.getTTMSCodeDescription(currentUser, ttmsCodeType,code);
		return description;
	}
	
}
