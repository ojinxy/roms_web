package org.fsl.roms.spring.security;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.fsl.application.ApplicationProperties;
import org.fsl.roms.security.ROMSUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

//import fsl.ta.toms.roms.romsuserdetails.ROMSUserDetailsPortProxy;

/**
 * @author jreid
 */
public class ROMSLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {

	Logger logger = Logger.getLogger(this.getClass().getName());
	
	@Override
	public void onLogoutSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {

		try {
			
			
			/*Context initialContext = new InitialContext();
			Context context = (Context) initialContext.lookup("java:comp/env");
			String spURL = (String) context.lookup("SSO_CONTEXT_PATH");*/
			
			
			/*String appName = (String)SSOUtil.getValueFor("APPLICATION_NAME");
			
			String spURL = (String)SSOUtil.getSSOSPPATH("SSO_CONTEXT_PATH",request);
			
			String url = spURL;*/
			
			HttpSession session = request.getSession();

			/*
			 RestTemplate restTemplate =  new RestTemplate();
			 
			 String token2 = (String)request.getSession().getAttribute("token");
			 
			 String param = "{\"appSession\":\""+appName+"\",\"token\":\""+token2+"\"}";
			 
			 System.err.println("Param: "+param);
			 String jsonStr = restTemplate.postForObject(url+"spring/sso/killSession",param,String.class);
			
			 
			 logger.info("Logging out: "+appName+"; with param: "+param);
			*/ 
			 
			 
			
			String sessionId = session.getId();

			/*ApplicationContext ctx = WebApplicationContextUtils
					.getWebApplicationContext(session.getServletContext());

			ROMSUserDetailsService romsUserDetailsService = (ROMSUserDetailsService) ctx
					.getBean("romsUserDetailsService");*/

			ROMSUserDetails loggedInUser = null;

			if (authentication != null) {
				loggedInUser = (ROMSUserDetails) authentication.getPrincipal();

				if (loggedInUser != null) {

				//	String status = (String) session.getAttribute(sessionId);

					// only if the person is still logged in should we log them
					// out
				//	if (status != null && status.equalsIgnoreCase("loggedIn")) {

						session.setAttribute(sessionId, "loggedOut");

						fsl.ta.toms.roms.webservices.ROMSUserDetails userdetailService = new fsl.ta.toms.roms.webservices.ROMSUserDetails();

						//userdetailService._getDescriptor().setEndpoint(ApplicationProperties.get("application.webservices.url").trim() + "ROMSUserDetailsService");

						//System.err.println(" user logged out : " + loggedInUser.getUsername());
						userdetailService.userLogOut(loggedInUser.getUsername());

					//}
				}
			}
			
			// url to go to on success
			/*setDefaultTargetUrl(spURL);*/
			setDefaultTargetUrl("/spring/login?logged_out=true");
			setAlwaysUseDefaultTargetUrl(true);

			super.onLogoutSuccess(request, response, authentication);
		} catch (ServletException e) {
			throw new ServletException(e);
		} catch (IOException e) {
			throw new IOException(e);
		} catch (Exception e) {
			throw new ServletException(e);
		}

	}

}
