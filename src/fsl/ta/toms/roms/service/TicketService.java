package fsl.ta.toms.roms.service;


import java.util.List;

import fsl.ta.toms.roms.ticketwebservice.TrafficTicket;

/**
 * 
 * @author rbrooks
 * Created Date: May 16, 2013
 */
public interface TicketService 
{
	public List<TrafficTicket>  queryTrafficTicket(String dlNo, String fromDate);
	
	public List<TrafficTicket>  queryTrafficTicket(String dlNo);



	
}