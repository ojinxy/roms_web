package org.fsl.roms.spring.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.fsl.application.ApplicationProperties;
import org.fsl.roms.security.ROMSUserDetails;
import org.fsl.roms.security.ROMSUserDetailsService;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.web.context.support.WebApplicationContextUtils;



/**
 * 
 * @author jreid
 * 
 */
public class ROMSSimpleUrlAuthenticationSuccessHandler extends
		SimpleUrlAuthenticationSuccessHandler {

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {

		HttpSession session = request.getSession();

		ApplicationContext ctx = WebApplicationContextUtils
				.getWebApplicationContext(session.getServletContext());

		ROMSUserDetails loggedInUser = (ROMSUserDetails) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal();

		String sessionId = session.getId();

		ROMSUserDetailsService romsUserDetailsService = (ROMSUserDetailsService) ctx
				.getBean("romsUserDetailsService");

		// save log in info to database audit
		//romsUserDetailsService.logIn(loggedInUser.getUsername());
		
		fsl.ta.toms.roms.webservices.ROMSUserDetails userdetailService = new fsl.ta.toms.roms.webservices.ROMSUserDetails();
		//ROMSUserDetailsPortProxy proxy = new ROMSUserDetailsPortProxy();

		//proxy._getDescriptor().setEndpoint(ApplicationProperties.get("application.webservices.url").trim() + "ROMSUserDetailsService");

		//System.err.println(" user logged in : " + loggedInUser.getUsername());
		try {
			userdetailService.userLogIn(loggedInUser.getUsername());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		session.setAttribute(sessionId, "loggedIn");

		super.onAuthenticationSuccess(request, response, authentication);

	}

}
