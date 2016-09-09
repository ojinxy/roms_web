package org.fsl.roms.spring.security.sso;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.sso.util.SSOUtil;

/**
 * Servlet implementation class SSOLoginServlet
 */

@WebServlet(urlPatterns="/spring/login")
public class SSOLoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SSOLoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//System.err.println("Request URL: "+request.getRequestURL());
		
		
		doPost(request, response);
		
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//System.err.println("@@In Login servlet url "+request.getRequestURL());
		
		
		
		String usId = request.getParameter("usId");
		String logged_out = request.getParameter("logged_out");
		String token = request.getParameter("token_auth"); 
		String doLogin=request.getParameter("doLogin");
		
		/*
		String sessionEnd = null;
		try{
		sessionEnd = (String) request.getSession(false).getAttribute("sessionEnd");		
		
		//System.err.println("sessionEnd: "+sessionEnd);
		
		request.getSession().removeAttribute("timeOut");
		}catch(Exception ex){
			System.err.println("No Session created yet");
		}
		*/
		//System.err.println("doLogin: "+doLogin);
		//System.err.println("Server request: "+request.getRequestURI());
		//System.err.println("logout_out: "+logged_out);
		
		String url = "";
		
		try{
			//Context initialContext = new InitialContext();
			//Context context = (Context)initialContext.lookup("java:comp/env");
			//String spURL = (String)context.lookup("SSO_CONTEXT_PATH");
			String appName = (String)SSOUtil.getValueFor("APPLICATION_NAME");
			
			String spURL = (String)SSOUtil.getSSOSPPATH("SSO_CONTEXT_PATH",request);
			
			String restUrl = spURL;
			
			/*if(StringUtils.trimToNull(restUrl)==null){
				restUrl = spURL;
			}*/
			
			url = spURL;
			
			logged_out = StringUtils.trimToNull(logged_out);
			
			if(logged_out!=null&&"true".equalsIgnoreCase(logged_out)){
//				url = spURL;
				
				 RestTemplate restTemplate =  new RestTemplate();
				 
				 String token2 = (String)request.getSession(false).getAttribute("token");
				 
				 String param = "{\"appSession\":\""+appName+"\",\"token\":\""+token2+"\"}";
				 
				 String jsonStr = restTemplate.postForObject(restUrl+"spring/sso/killSession",param,String.class);
				 url = "../spring/ssologout?dologout=true";
				 response.sendRedirect(url);
			}else{	
				/*
				ROMSUserDetails loggedinUser = (ROMSUserDetails)SecurityContextHolder.getContext()
						.getAuthentication().getPrincipal();
				
				if(loggedinUser!=null){
					url="../spring/home";	*/				
				//}
				if(StringUtils.trimToNull(token)!=null){
					
					//System.err.println("token is not null redirectiing to auth with: "+token);
					//response.sendRedirect("../spring/loginProcess?usId="+token);
					url = "../spring/loginProcess?token_auth="+token;
					response.sendRedirect(url);
				}else{
					//if(StringUtils.trimToNull(timeOut)!=null){
					//	url = "../spring/timeOut";
					//}else{
					
						url = spURL;
						System.err.println("call Landing page");
					//}
					response.sendRedirect(url);	
				}
				
				
			}
		}catch(Exception e){
			e.printStackTrace();
			//System.err.println("Ple");
		}
		
		//response.sendRedirect(url);
	}

}
