package org.fsl.roms.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.fsl.application.ApplicationProperties;



import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import fsl.ta.toms.roms.bo.BadgeBO;
import fsl.ta.toms.roms.dlwebservice.DriverLicenceDetails;
import fsl.ta.toms.roms.dlwebservice.FslWebServiceException;
import fsl.ta.toms.roms.dlwebservice.FslWebServiceException_Exception;
import fsl.ta.toms.roms.exception.NoRecordFoundException;
import fsl.ta.toms.roms.exception.RequiredFieldMissingException;
import fsl.ta.toms.roms.webservices.BIMSWebService;
import fsl.ta.toms.roms.webservices.DLWebService;

/**
 * Servlet implementation class PhotoServlet
 */
public class PhotoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PhotoServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		
		String badgeBadgeNo = request.getParameter("badgeNo");
		String badgeType = request.getParameter("badgeType");
		String serviceType = request.getParameter("serviceType");
		String dlNo = request.getParameter("dlNo");
		
		
		byte[] photoBytes = null;
		final HttpServletRequest req =
	        ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

		final HttpSession session = req.getSession();
		
		if(serviceType.equalsIgnoreCase("BIMS")){
			BIMSWebService bimsWebService = new BIMSWebService();
			
			//bimsWebService._getDescriptor().setEndpoint(ApplicationProperties.get("application.webservices.url").trim() + "BIMSWebService");
			
			BadgeBO badge = new BadgeBO();
			
			try {			
				badge = bimsWebService.getBadgeDetails(badgeBadgeNo, badgeType.charAt(0) + "");
				if(badge!=null){
					photoBytes=badge.getPhotoImg();
				}
			
			if(null!=photoBytes)
				{
				
					writeImage(photoBytes,request,response);//write repsonse
				}
			
			} catch (NoRecordFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (RequiredFieldMissingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
		if(serviceType.equalsIgnoreCase("DL")){
			DLWebService dlWebService = new DLWebService();
			
			//dlWebService._getDescriptor().setEndpoint(ApplicationProperties.get("application.webservices.url").trim() + "DLWebService");
			
			DriverLicenceDetails driverLicenceDetails = new DriverLicenceDetails();
			
						
				try {
					driverLicenceDetails = dlWebService.getDriversLicence(dlNo);
					
					if(driverLicenceDetails!=null){
						photoBytes=driverLicenceDetails.getPhotograph();
					}
				
				if(null!=photoBytes)
					{
					
						writeImage(photoBytes,request,response);//write repsonse
					}
				
				} catch ( FslWebServiceException_Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (RequiredFieldMissingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			
		}
	
	}


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}
	
	
	
	void writeImage(byte[] image,HttpServletRequest req, HttpServletResponse resp){									
		resp.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
		resp.setContentLength(image.length);
		resp.setContentType("image/jpeg");
		try {			
			resp.getOutputStream().write(image);
			//resp.getOutputStream().flush();	
			//resp.getOutputStream().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			try {
				resp.getOutputStream().flush();	
				resp.getOutputStream().close();
				
			} catch (IOException e) {
				
			}
		}
	}

}
