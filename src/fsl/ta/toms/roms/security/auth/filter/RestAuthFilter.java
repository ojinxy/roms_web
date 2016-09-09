package fsl.ta.toms.roms.security.auth.filter;

import java.io.IOException;
import java.text.SimpleDateFormat;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.util.crypt.Obfuscator;
import com.util.crypt.WEncrypter;

/**
 * Servlet Filter implementation class RestAuthFilter
 */

public class RestAuthFilter implements Filter {

    /**
     * Default constructor. 
     */
    public RestAuthFilter() {
        // TODO Auto-generated constructor stub
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
		
		HttpServletRequest httpServletRequest = (HttpServletRequest)request;
		
		//System.err.println("filter called with: "+httpServletRequest.getRequestURL());
		
		//RestHttpServletRequest sr = new RestHttpServletRequest(httpServletRequest);
		
		//System.err.println(sr.getRequestURI());
		
		//System.err.println(sr.getRequestURL());
		
		//System.err.println("Auth token: "+httpServletRequest.getHeader("authToken"));
		
		String authToken = httpServletRequest.getHeader("authToken");
		
		boolean autherror = false;
		
		boolean restAuthEnable = (Boolean)getValueFor("REST_AUTH_ENABLE");
		
		if(restAuthEnable){
		
			if(StringUtils.trimToNull(authToken)==null){
				autherror = true;
				throw new IOException("Request not authorised: No Authentication token");
			}
			
			try{
				WEncrypter we = new WEncrypter();
							
				SimpleDateFormat df = new SimpleDateFormat("HHmmss");
				
				String systime = df.format(System.currentTimeMillis());
				
				int interval = (Integer)getValueFor("REST_AUTH_INTERVAL");
			
				
				//Obfuscator ob = new Obfuscator();
				
				//System.err.println("Sys Time: "+systime);
				//System.err.println("Hash time: "+ob.retriveTimeString(authToken));
				
				
				if(interval > 0){
					if(!we.isTokenValid(authToken, systime,interval)){
						autherror = true;
						throw new IOException("Request not authorised: Invalid token");
					}
				}else{
					if(!we.isTokenValid(authToken)){
						autherror = true;
						throw new IOException("Request not authorised: Invalid token");
					}
				}
				
				
			}catch(Exception ex){
				autherror = true;
				ex.printStackTrace();
			}
		}
		
		if(!autherror){
			chain.doFilter(httpServletRequest, response);
		}else{
			
			HttpServletResponse resp = (HttpServletResponse)response;
			
			resp.sendError(403);
			
			return;
		}
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

	
	private static Context getContext(){
		Context context = null;
		try{
			Context initialContext = new InitialContext();
			context = (Context)initialContext.lookup("java:comp/env");
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return context;
	}
		
	public static Object getValueFor(String varName){
		try {
			Object val = getContext().lookup(varName);
			return val;
		} catch (NamingException e) {
			System.out.println("Value for: "+varName+" not found");
			//e.printStackTrace();
		}
		return null;
	}

	
	
	
	
	
	private class RestHttpServletRequest extends HttpServletRequestWrapper{

		public RestHttpServletRequest(HttpServletRequest request) {
			super(request);
			
		}

		@Override
		public String getRealPath(String path) {
						
			return super.getRealPath(path);
		}
		
		
		@Override
		public StringBuffer getRequestURL() {
			
			StringBuffer url = super.getRequestURL();			
									
			return doRemoveRest(url);
		}
		
		@Override
		public String getRequestURI() {
			
			StringBuffer url = new StringBuffer(super.getRequestURI());			
						
			return doRemoveRest(url).toString();
		}
		
		
		public StringBuffer doRemoveRest(StringBuffer url){
			
			int inxst = url.indexOf("/rest/");
			int inxls = inxst + "/rest/".length();
			
			url.replace(inxst, inxls, "/");
						
			return url;
		}
		
	}
	
}
