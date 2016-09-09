package org.fsl.roms.spring.security.sso;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.fsl.roms.security.ROMSUserDetails;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.Assert;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Servlet implementation class SSOTimeOutServlet
 */


//URL PATH /SSOTimeOut
public class SSOTimeOutServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
	
	private String LOGIN_PATH;
	
    public SSOTimeOutServlet() {
        super();
       
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
    	
    	this.LOGIN_PATH = config.getInitParameter("LOGIN_PATH");
    	
    	super.init(config);
    }
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Assert.notNull(request, "HttpServletRequest required");
		
		Logger logger = Logger.getLogger(this.getClass().getName());
		
		logger.info("doing a timeout");
		 HttpSession session = request.getSession(false);
         
         if (session != null) {
          	ApplicationContext ctx = 
                  WebApplicationContextUtils.
                        getWebApplicationContext(session.getServletContext());
      		
      		ROMSUserDetails loggedInUser = (ROMSUserDetails) SecurityContextHolder
      		.getContext().getAuthentication().getPrincipal();

      		 /* SecurityService securityService = (SecurityService) ctx.getBean("securityService");		  
      		  
      		  System.err.println(" I AM Timed out " + securityService);	
      		  
      		  securityService.logOut(loggedInUser.getUsername());*/
      		  
      		  String sessionId = session.getId();
      	      
      		  session.setAttribute(sessionId ,"loggedOut");
      		  
      		  //System.err.println("user status: " + session.getAttribute(sessionId));
      		 
      		  session.invalidate();
          }
         
         response.sendRedirect(request.getContextPath()+"/spring/timeOut");
        // request.getRequestDispatcher("");
         
	}

}
