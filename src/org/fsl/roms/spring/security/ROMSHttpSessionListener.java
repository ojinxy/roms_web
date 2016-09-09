package org.fsl.roms.spring.security;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.commons.lang.StringUtils;
import org.fsl.roms.security.ROMSUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.session.HttpSessionCreatedEvent;
import org.springframework.security.web.session.HttpSessionDestroyedEvent;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.sso.util.SSOUtil;

/**
 * 
 * @author jreid
 * 
 */
public class ROMSHttpSessionListener implements HttpSessionListener {

	private static final String LOGGER_NAME = ROMSHttpSessionListener.class
			.getName();

	java.util.logging.Logger logger = java.util.logging.Logger.getLogger(this.getClass().getName());
	
	// ~ Methods
	// ========================================================================================================

	ApplicationContext getContext(ServletContext servletContext) {
		return WebApplicationContextUtils
				.getWebApplicationContext(servletContext);
	}

	/**
	 * Handles the HttpSessionEvent by publishing a
	 * {@link HttpSessionCreatedEvent} to the application appContext.
	 * 
	 * @param event
	 *            HttpSessionEvent passed in by the container
	 */
	@Override
	public void sessionCreated(HttpSessionEvent event) {
		HttpSessionCreatedEvent e = new HttpSessionCreatedEvent(
				event.getSession());
		final Logger log = LoggerFactory.getLogger(LOGGER_NAME);

		ApplicationContext context = getContext(event.getSession()
				.getServletContext());

		SessionRegistryImpl sessionRegistry = (SessionRegistryImpl) context
				.getBean("sessionRegistry");
		List<Object> authenticatedPrincipals = sessionRegistry
				.getAllPrincipals();
		
		if (log.isDebugEnabled()) {
			log.debug("Publishing event: " + e);
		}

		getContext(event.getSession().getServletContext()).publishEvent(e);
	}

	/**
	 * Handles the HttpSessionEvent by publishing a
	 * {@link HttpSessionDestroyedEvent} to the application appContext.
	 * 
	 * @param event
	 *            The HttpSessionEvent pass in by the container
	 */
	@Override
	public void sessionDestroyed(HttpSessionEvent event) {

		HttpSessionDestroyedEvent e = new HttpSessionDestroyedEvent(
				event.getSession());

		//System.err.println("Session ended");
				
		final Logger log = LoggerFactory.getLogger(LOGGER_NAME);

		if (log.isDebugEnabled()) {
			log.debug("Publishing event: " + e);
		}

		HttpSession session = event.getSession();
	
		
		/***Notify TAMS to Remove ROMS***/
		String appName = (String)SSOUtil.getValueFor("APPLICATION_NAME");
		
		String REST_END_POINT = (String) session.getAttribute("SP_URL");
		
		/*if(StringUtils.trimToNull(REST_END_POINT)==null){
			REST_END_POINT = (String) session.getAttribute("SP_URL");
		}*/
		
		
		String url = REST_END_POINT;
		
		session.removeAttribute("SP_URL");
		
		if(StringUtils.trimToNull(url)!=null){
			
		 RestTemplate restTemplate =  new RestTemplate();
		 
		 String token2 = (String)session.getAttribute("token");
		 
		 String param = "{\"appSession\":\""+appName+"\",\"token\":\""+token2+"\"}";
		 
		 //System.err.println("Param: "+param);
		 String jsonStr = restTemplate.postForObject(url+"spring/sso/killSession",param,String.class);
		
		
		 logger.info("Logging out: "+appName+"; with param: "+param);
		}
		 /*************************/
				
		
		String sessionId = session.getId();

		ApplicationContext ctx = getContext(event.getSession()
				.getServletContext());

		String status = (String) event.getSession().getAttribute(sessionId);
	
		if (status != null && status.equalsIgnoreCase("loggedIn")) {
		
			String userName = (String) event.getSession().getAttribute(
					"loggedInUser");
			
			logAction(event, userName);
			session.setAttribute(sessionId, "loggedOut");
			//session.setAttribute("sessionEnd", "sessionEnd");

		}
		/*** CLEAN UP ***/
		// if the status already loggedout remove value from session
		//if (status != null && status.equalsIgnoreCase("loggedOut")) {
		//session.removeAttribute(sessionId);
		session.removeAttribute("loggedInUser");
		//}

		ctx.publishEvent(e);
	}

	private void logAction(HttpSessionEvent sessionEvent, String userName) {

		HttpSession session = sessionEvent.getSession();

		ApplicationContext ctx = WebApplicationContextUtils
				.getWebApplicationContext(session.getServletContext());

		// finish
		ROMSUserDetailsService romsUserDetailsService = (ROMSUserDetailsService) ctx
				.getBean("romsUserDetailsService");

		//romsUserDetailsService.timeOut(userName);

	}

}
