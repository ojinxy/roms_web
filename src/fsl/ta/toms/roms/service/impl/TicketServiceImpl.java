package fsl.ta.toms.roms.service.impl;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import fsl.ta.toms.roms.constants.Constants.WebServiceConfig;
import fsl.ta.toms.roms.dao.DAOFactory;
import fsl.ta.toms.roms.dataobjects.ConfigurationDO;
import fsl.ta.toms.roms.service.TicketService;
import fsl.ta.toms.roms.ticketwebservice.FslTrafficTicket;
import fsl.ta.toms.roms.ticketwebservice.FslTrafficTicketProxy;
import fsl.ta.toms.roms.ticketwebservice.FslTrafficTicketProxy.Descriptor;
import fsl.ta.toms.roms.ticketwebservice.FslWebServiceException_Exception;
import fsl.ta.toms.roms.ticketwebservice.TrafficTicket;


@Transactional
public class TicketServiceImpl implements TicketService{
	
	private DAOFactory daoFactory;
	
	 
	public void setDaoFactory(DAOFactory daoFactory) {
		this.daoFactory = daoFactory;
	}
	
	
	
	public List<TrafficTicket>  queryTrafficTicket(String dlNo, String fromDate)
	{
		List<TrafficTicket> trafficTickets = null;
		
		try{
			FslTrafficTicketProxy ttProxy = new FslTrafficTicketProxy();
			
			//retrieve web service settings
			String endPoint = ((ConfigurationDO)daoFactory.getConfigurationDAO().find(ConfigurationDO.class, WebServiceConfig.TICKET_WS_URL)).getValue();
			//System.err.println("endPoint: " + endPoint);
			Descriptor desc = ttProxy._getDescriptor();
			desc.setEndpoint(endPoint);
			//System.err.println("desc endPoint: " + desc.getEndpoint());
			//System.err.println("dlno: " + dlNo);
			//System.err.println("fromDate " + fromDate);
			FslTrafficTicket fslTrafficTicket = desc.getProxy();
			
			//trafficTickets  = (List<TrafficTicket>) fslTrafficTicket.getTicketsForDriverI(dlNo, fromDate);
			trafficTickets = (fslTrafficTicket.getTicketsForDriverI(dlNo, fromDate)).getTrafficTicket();
			
			/*for (TrafficTicket trafficTicket : trafficTickets)
			{
				//System.err.println(trafficTicket.toString());
			}*/
			
			
			
		}
		catch(FslWebServiceException_Exception fsle)
		{
			fsle.printStackTrace();
		}
	
	
		return trafficTickets;
	}



	/**
	 * This function uses the limit from the configuration table for max amount of Traffic Ticket Results
	 */
	@Override
	public List<TrafficTicket> queryTrafficTicket(String dlNo)
	{
		List<TrafficTicket> trafficTickets = null;
		
		try{
			FslTrafficTicketProxy ttProxy = new FslTrafficTicketProxy();
			
			//retrieve web service settings
			String endPoint = ((ConfigurationDO)daoFactory.getConfigurationDAO().find(ConfigurationDO.class, WebServiceConfig.TICKET_WS_URL)).getValue();
			Descriptor desc = ttProxy._getDescriptor();
			desc.setEndpoint(endPoint);

			FslTrafficTicket fslTrafficTicket = desc.getProxy();
			
			Integer limit = 50;
			
			try
			{
				limit = Integer.parseInt(daoFactory.getConfigurationDAO().getConfigurationValue( fsl.ta.toms.roms.constants.Constants.Configuration.MAX_TTMS_OFFENCES));
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
			if(limit < 1 || limit > 200)
			{
				limit = 50;
			}
			
			trafficTickets = (fslTrafficTicket.getTicketsForDriverLimit(dlNo, limit)).getTrafficTicket();
			
			
			
		}
		catch(FslWebServiceException_Exception fsle)
		{
			fsle.printStackTrace();
		}
	
	
		return trafficTickets;
	}


}
