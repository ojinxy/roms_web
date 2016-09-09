package fsl.ta.toms.roms.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


public class RESTSpringAuthenticationFilter extends
		UsernamePasswordAuthenticationFilter {

	public static final String SPRING_SECURITY_FORM_OVERRIDE_SESSION_KEY = "j_override";

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request,
		HttpServletResponse response) throws AuthenticationException {

		System.err.println("In authentication man");
				 	
	 	AuthenticationManager am = getAuthenticationManager();
					
		Authentication authreq = new AnonymousAuthenticationToken("", null, null);
			 
		return am.authenticate(authreq);
		
	}
	
	
	@Override
	 protected void successfulAuthentication(HttpServletRequest request,
	         HttpServletResponse response,  FilterChain filterChain , Authentication authResult)
	         throws IOException, ServletException {
	    
		
		 super.successfulAuthentication(request, response, filterChain ,authResult);	     
	 }

	 @Override
	 protected void unsuccessfulAuthentication(HttpServletRequest request,
	         HttpServletResponse response, AuthenticationException failed)
	         throws IOException, ServletException {		
						 
	     super.unsuccessfulAuthentication(request, response, failed);	    
	 }
	 
	
	 

}
