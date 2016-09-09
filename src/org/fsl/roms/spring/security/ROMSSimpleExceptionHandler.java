package org.fsl.roms.spring.security;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.fsl.roms.security.ROMSUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

/**
 * @since
 * @author jreid This is a custom error handler class for BIMS This is
 *         configured in webmvc-config.xml This method overrides the spring
 *         class and provide customised logging of errors by the container
 * 
 */
public class ROMSSimpleExceptionHandler extends SimpleMappingExceptionResolver {

	/**
	 * Log the exception.
	 * 
	 * */
	protected Logger log = LogManager.getLogger(this.getClass().getName());
	
	@Override
	public ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception exception) {
		// instantiate logger
		

		String error = "";
		try{
			ROMSUserDetails loggedInUser = (ROMSUserDetails) SecurityContextHolder
					.getContext().getAuthentication().getPrincipal();
	
			
			// log the userName and time
			if (loggedInUser != null) {
			error = "USERNAME: " + loggedInUser.getUsername() + " TIME: "
				+ new Date() + " URL: " + request.getRequestURL();
		}
		}catch(Exception ex){
			log.error(error, exception);
			log.error("Error Handler", error);
			
			return super.resolveException(request, response, handler, exception);
		}
		
		// do actual log
		log.error(error, exception);
		//logException(exception, request);

		// call super class
		return super.resolveException(request, response, handler, exception);
	}

}