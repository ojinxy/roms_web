package com.sso.client.filter;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.fsl.roms.security.ROMSUserDetails;
import org.fsl.roms.spring.security.SSOWebHandler;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.security.core.context.SecurityContextHolder;

import com.sso.client.user.SSOUserDetails;
import com.sso.util.SSOUtil;

/**
 * Servlet Filter implementation class SSOFilter
 */
public class SSOFilter implements Filter {

    /**
     * Default constructor. 
     */
	
	private String LOGIN_PATH;
	//private String SP_PATH;
	private String LOGOUT_PATH;
	private String USER_CLASS;
	private String APP_NAME;
	
	
	private static final String LOGOUT_ACTION = "/spring/logout";
	private static final String SSOTIMEOUT_ACTION = "/SSOTimeOut";
	private static final String LOGIN_ACTION = "/spring/login";
	
	
    public SSOFilter() {
        
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		
		
		//Logger logger = Logger.getLogger(this.getClass().getName());
		
		//logger.setUseParentHandlers(false);
		
		HttpServletRequest req = (HttpServletRequest)request;
		HttpServletResponse resp = (HttpServletResponse)response;
		
		boolean redirectOcuured = false;
		
		//System.err.println("SSO Login Filter called");
		
		
		/*String REST_END_POINT =  (String) SSOUtil.getValueFor("REST_END_POINT");*/
		
		String REST_END_POINT =  (String)SSOUtil.getSSOSPPATH("SSO_CONTEXT_PATH",req);
		
		/*if(StringUtils.trimToNull(REST_END_POINT)==null){
			REST_END_POINT = (String)SSOUtil.getSSOSPPATH("SSO_CONTEXT_PATH",req);
		}*/
		
				
		String token =  request.getParameter("token");
		String logout = request.getParameter("dologout");
		
		
		String doLogin=request.getParameter("doLogin");
		
		//String timeOut = null;
		//timeOut = (String) request.getSession(false).getAttribute("timeOut");		
		
		
		//System.err.println("doLogin: "+doLogin);
		
		
		
		
		//logger.info("token is: "+token);
		//logger.info("logout variable is: "+logout);
		String servletPath = req.getServletPath();
		
		//logger.info("Servlet path: "+servletPath);
		
		if (!servletPath.equals(SSOTIMEOUT_ACTION))			
		{	
			
			if(StringUtils.trimToNull(logout)!=null&&logout.equals("true")){
				redirectOcuured = true;
				
				req.getRequestDispatcher(LOGOUT_PATH).forward(request, response);				
			}else{
					if(StringUtils.trimToNull(token)!=null){
						redirectOcuured = true;
						
						request.getRequestDispatcher(LOGIN_PATH+"?token_auth="+token).forward(request, response);
									
					}else{
																		
						ROMSUserDetails loggedInUser =null;
													
						try{
							loggedInUser = (ROMSUserDetails) SecurityContextHolder
				    		.getContext().getAuthentication().getPrincipal();
						}catch(Exception e){
							//logger.warning("User details not set");
						}		
						
						if(loggedInUser!=null){
						
							JSONObject pobj = new JSONObject();
			
							pobj.put("appSession", APP_NAME);
							pobj.put("token", req.getSession(false).getAttribute("token"));
			
							//logger.info("JSON String: "+pobj.toJSONString());						
							String rsltStr = null;
							try {
								//logger.info("Rest called path: "+REST_END_POINT + "spring/sso/heart");
								rsltStr = SSOWebHandler.sendPost(REST_END_POINT + "spring/sso/heart", pobj.toJSONString());
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							
													
							JSONObject rslt = null;
							try {
								rslt = (JSONObject) new JSONParser().parse(rsltStr);
							} catch (ParseException e) {
								
								e.printStackTrace();
							}
							
							String pulse = rslt.get("pulse")!=null?rslt.get("pulse").toString():null;
							
							if(!"beep".equals(pulse)&&req.getSession(false).getAttribute("sessValid")!=null){
								
								req.getSession(false).removeAttribute("sessValid");
								
								redirectOcuured = true;
								
								req.getRequestDispatcher("/SSOTimeOut").forward(request, response);
								
							}else{
								if(req.getSession(false).getAttribute("sessValid")==null){					
									req.getSession(false).setAttribute("sessValid", "true");
								}
							}
						}
						
						if(!redirectOcuured){
							chain.doFilter(request, response);
						}
					}
			
			}
		}else{
			chain.doFilter(request, response);
		}
		
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		
		
		
		this.LOGIN_PATH = fConfig.getInitParameter("LOGIN_PATH");
		//
		this.LOGOUT_PATH = fConfig.getInitParameter("LOGOUT_PATH");
		this.USER_CLASS =fConfig.getInitParameter("USER_CLASS");
		this.APP_NAME = (String) ((boolean)(StringUtils.trimToNull(fConfig.getInitParameter("APPLICATION_NAME")) == null)?
							SSOUtil.getValueFor("APPLICATION_NAME"):
								fConfig.getInitParameter("APPLICATION_NAME"));
				
		
	}

}
