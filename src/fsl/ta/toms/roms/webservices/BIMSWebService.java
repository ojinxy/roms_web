/**
 * Created By: oanguin
 * Date: May 7, 2013
 *
 */
package fsl.ta.toms.roms.webservices;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import fsl.ta.toms.roms.bo.BadgeBO;
import fsl.ta.toms.roms.exception.NoRecordFoundException;
import fsl.ta.toms.roms.exception.RequiredFieldMissingException;
import fsl.ta.toms.roms.service.BIMSService;
import fsl.ta.toms.roms.service.ServiceFactory;

/**
 * @author oanguin
 * 
 * Created Date: May 7, 2013
 */
@Controller
@RequestMapping("/roms_rest/bims")
public class BIMSWebService extends SpringBeanAutowiringSupport implements
		Serializable 
{
	private static final long serialVersionUID = 1L;

	@Autowired
	private ServiceFactory serviceFactory;

	public BIMSWebService() 
	{
		super();
		// TODO Auto-generated constructor stub
	}
	
	@RequestMapping("/getBadgeDetails")
	public @ResponseBody BadgeBO getBadgeDetails(@RequestParam String badgeNo,@RequestParam String badgeType) throws RequiredFieldMissingException, NoRecordFoundException
	{
		BIMSService bimsService = this.serviceFactory.getBIMSService();
		
		return bimsService.getBadgeDetails(badgeNo, badgeType);
	}
	
	@RequestMapping("/getBadgeDetailsDriverConductor")
	public @ResponseBody List<BadgeBO> getBadgeDetailsDriverConductor(@RequestParam String badgeNo) throws RequiredFieldMissingException, NoRecordFoundException
	{
		
		List<BadgeBO> badges = new ArrayList<BadgeBO>();
		
		BadgeBO badgeConductor = null;
		
		BadgeBO badgeDriver = null;
		
		try{
			badgeConductor = this.getBadgeDetails(badgeNo, "c");
		}
		catch(Exception e){
			
		}
		
		try{
			badgeDriver = this.getBadgeDetails(badgeNo, "d");
		}catch(Exception e){
			
		}
		
		if(badgeDriver != null){
			badges.add(badgeDriver);
		}
		
		if(badgeConductor != null ){
			badges.add(badgeConductor);
		}
		
		if(badges.size() == 0){
			throw new NoRecordFoundException();
		}
		
		return badges;
		
		
	}
	
	@RequestMapping("/getBadgeByPersonDetails")
	public @ResponseBody List<BadgeBO> getBadgeByPersonDetails(@RequestParam String firstName,@RequestParam String midName,@RequestParam String lastName, @RequestParam String currentUsername) throws RequiredFieldMissingException, NoRecordFoundException
	{
		BIMSService bimsService = this.serviceFactory.getBIMSService();
		
		return bimsService.getBadgeByPersonDetails(firstName, midName, lastName, currentUsername);
		
	}
}
