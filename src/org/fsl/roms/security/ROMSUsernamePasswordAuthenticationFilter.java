package org.fsl.roms.security;

import java.io.IOException;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.fsl.roms.spring.security.SSOWebHandler;
import org.fsl.roms.util.ContextUtil;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.sso.util.SSOUtil;

/**
 * @author jreid
 * @since This overrides the spring UsernamePasswordAuthenticationFilter and
 *        provides functionality to have a user manually terminate a current
 *        session by selecting a checkbox on the screen
 **/
public class ROMSUsernamePasswordAuthenticationFilter extends
		UsernamePasswordAuthenticationFilter {

	public static final String SPRING_SECURITY_FORM_OVERRIDE_SESSION_KEY = "j_override";

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request,
		HttpServletResponse response) throws AuthenticationException {

		//System.err.println("ROMSUsernamePasswordAuthenticationFilter ");
		
		String token = request.getParameter("token_auth");
	 	String usId = null;
	 	
	
	 	
	 	try{
		 	//Context initialContext = new InitialContext();
			//Context context = (Context)initialContext.lookup("java:comp/env");
			
	 		/*String spURL = (String)SSOUtil.getValueFor("REST_END_POINT");*///  (String)context.lookup("SSO_CONTEXT_PATH");
			
	 		String spURL =  (String)SSOUtil.getSSOSPPATH("SSO_CONTEXT_PATH",request);
	 		
	 		
	 		/*if(StringUtils.trimToNull(spURL)==null){
				spURL = (String)SSOUtil.getSSOSPPATH("SSO_CONTEXT_PATH",request);
			}*/
			
		 	System.err.println("Attempting to log user at: "+spURL);
		 	
		 	String param = "{\"token\":\""+token+"\"}";
		 	String userStr = SSOWebHandler.sendPost(spURL+"spring/sso/identity", param);
		 	
		 	
		 	//String userStr = getUserStr(spURL+"spring/sso/identity",token);
		 		 	
			JSONParser parser = new JSONParser();
			
			JSONObject jobj = (JSONObject) parser.parse(userStr);
			
			 usId = (String) jobj.get("user");
		 	
			//System.err.println("the usid recovered: "+usId);
			
			 Object loggedinUser = null;
			 
			 try{
				 loggedinUser = (ROMSUserDetails)SecurityContextHolder.getContext()
						.getAuthentication().getPrincipal(); 
			 }catch(Exception ex){
				 
				 System.out.println("No user logged in to this session...");
				 //ex.printStackTrace();
			 }
			 
			 if(loggedinUser != null){
				 SecurityContextHolder.getContext()
					.getAuthentication().setAuthenticated(false);
			 }
			 			 
			if(StringUtils.trimToNull(usId) == null){
				throw new AuthenticationServiceException("Authentication exception no user Id; User is not logged in.");
			}
			
			Logger.getLogger(this.getClass().getName()).info("Setting token in session: "+token);
			request.getSession().setAttribute("token",token);
			
	 	}catch(Exception ex){
	 		ex.printStackTrace();
	 		throw new AuthenticationServiceException("Authentication exception");
	 	}
	 	
	 	AuthenticationManager am = getAuthenticationManager();
					
		Authentication authreq = new UsernamePasswordAuthenticationToken(usId, "xxxxx");
		
		//Authentication result = am.authenticate(authreq);
	 
		return am.authenticate(authreq);
		//return super.attemptAuthentication(request, response);
	}
	
	
	@Override
	 protected void successfulAuthentication(HttpServletRequest request,
	         HttpServletResponse response,  FilterChain filterChain , Authentication authResult)
	         throws IOException, ServletException {
	     
		 	 
		//inject the station name of the logged in user here
		 ROMSUserDetails userDetails = (ROMSUserDetails) authResult.getPrincipal();
		 
		 //System.err.println(" Password filter User: "+userDetails.getUsername());
	     //set user in session 
	    request.getSession().setAttribute("loggedInUser",userDetails.getUsername());
	    
	    try{
		    //Context initialContext = new InitialContext();
			//Context context = (Context)initialContext.lookup("java:comp/env");
			String appName = (String)SSOUtil.getValueFor("APPLICATION_NAME");
			
			//String spURL = (String)SSOUtil.getValueFor("REST_END_POINT");
			
			String REST_END_POINT = (String)SSOUtil.getSSOSPPATH("SSO_CONTEXT_PATH",request);
			
			/*if(StringUtils.trimToNull(REST_END_POINT)==null){
				REST_END_POINT = (String)SSOUtil.getSSOSPPATH("SSO_CONTEXT_PATH",request);
			}*/
			
			String sessID = request.getSession(false).getId();
			
			request.getSession(false).setAttribute("SP_URL", REST_END_POINT);
			
			String param = "{\"appSession\":\""+appName+"\",\"token\":\""+
							request.getSession(false).getAttribute("token")+"\",\"sessionId\":\""+sessID+"\"}";
			SSOWebHandler.sendPost(REST_END_POINT+"spring/sso/addSession", param);
			
	    }catch(Exception ex){
	    	ex.printStackTrace();
	    	
	    }
	    
	    
		 super.successfulAuthentication(request, response, filterChain ,authResult);
	     
	     
	 }

	 @Override
	 protected void unsuccessfulAuthentication(HttpServletRequest request,
	         HttpServletResponse response, AuthenticationException failed)
	         throws IOException, ServletException {		
		
		 //put code for unsuccessful
		 //System.err.println("Authentication failed...redirecting");
		 try{
			 	//Context initialContext = new InitialContext();
				//Context context = (Context)initialContext.lookup("java:comp/env");
				//String spURL = (String)ContextUtil.getValueFor("SSO_CONTEXT_PATH");
			 					
				//response.sendRedirect(spURL);
				
				//Logger.getLogger(getFilterName()).info("Authentication failed...redirecting");
				
		 }catch(Exception ex){
			ex.printStackTrace();			 
		 }
		 
	     super.unsuccessfulAuthentication(request, response, failed);	    
	 }
	 
	
	 

}
