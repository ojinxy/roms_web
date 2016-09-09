package org.fsl.roms.spring.security;

import java.io.IOException;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;

/**
 * @author jreid
 */
public class ROMSSimpleUrlAuthenticationFailureHandler
		extends
		org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException exception)
			throws IOException, ServletException {

		/**
		 * If the user has two sessions then a SessionAuthenticationException
		 * will be thrown, check for this exception so you can redirect them to
		 * appropriate screen
		 **/
		try{
		//Context initialContext = new InitialContext();
		//Context context = (Context)initialContext.lookup("java:comp/env");
		//String spURL = (String)context.lookup("SSO_CONTEXT_PATH");
	 	
		//System.err.println("Authencation failed: redirecting");
		//response.sendRedirect(spURL);
		
		Logger.getLogger(this.getClass().getName()).info("Authentication failed...redirecting");
		
		//setDefaultFailureUrl(spURL);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		/*if (exception.getClass().equals(SessionAuthenticationException.class)) {
			// change the default failure URL if there are multiple sessions
			setDefaultFailureUrl("/spring/login?login_duplicate=true");

		}
		else if(exception.getClass().equals(BadCredentialsException.class)){
			setDefaultFailureUrl("/spring/login?login_error=1");
		}*/			
		
		super.onAuthenticationFailure(request, response, exception);
	}

}
