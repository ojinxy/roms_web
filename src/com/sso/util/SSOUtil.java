package com.sso.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.fsl.roms.service.action.BaseServiceAction;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DeviceUtils;
import org.springframework.webflow.execution.RequestContext;
import org.springframework.webflow.execution.RequestContextHolder;
import fsl.ta.toms.roms.bo.RefCodeBO;


public class SSOUtil {
	
	// HTTP POST request
	public static String sendPost(String url, String postData) throws Exception {
		
		URL obj = new URL(url);
				
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
 
		//add reuqest header
		con.setDoOutput(true);
		con.setDoInput(true);
		con.setInstanceFollowRedirects(false);
		con.setRequestMethod("POST");
		con.setRequestProperty("Content-Type", "plain/text");
		con.setRequestProperty("charset", "utf-8");
		con.setRequestProperty("Content-Length", "" + Integer.toString(postData.getBytes().length));
		con.setUseCaches (false);
		
		// Send post request
		
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(postData);
		wr.flush();
		wr.close();
 
		int responseCode = con.getResponseCode();
		//System.err.println("\nSending 'POST' request to URL : " + url);
		//System.err.println("Post parameters : " + postData);
		//System.err.println("Response Code : " + responseCode);
 
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
 
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
 
		//print result
		//System.err.println(response.toString());
 
		return response.toString();
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
	
	/*
	public static String getSSOURL(){
		String spURL = (String)getContext().lookup("SSO_CONTEXT_PATH");
	}*/
	
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

	
	public static Object getSSOSPPATH(String varName, HttpServletRequest req){
		
		String SSO_PATH = "";
		
		//System.err.println("Request url: "+req.getRequestURL());
		
		
		try{
			boolean mobileDevice = isMobile(req);
		
			if(mobileDevice){
				SSO_PATH = (String)getValueFor(varName+"_MOB");				
			}else{
				SSO_PATH = (String)getValueFor(varName);
			}
		
		}catch(Exception ex){
			ex.printStackTrace();
			SSO_PATH = (String)getValueFor(varName);
		}
		
		//System.err.println("SSOPath: "+SSO_PATH);
		
		return SSO_PATH;
	}
		
	
	
	public static boolean isMobile(HttpServletRequest req){
		boolean isMobile = false;
		
		//RequestContext context = RequestContextHolder.getRequestContext();

		//HttpServletRequest req = (HttpServletRequest) context.getExternalContext().getNativeRequest();
		
		//Device currentDevice = DeviceUtils.getCurrentDevice(req);
		
		//if(currentDevice!=null){
		//	isMobile = currentDevice.isTablet() || currentDevice.isMobile();
			
		//	if(!isMobile){
				BaseServiceAction baseAction = new BaseServiceAction();
				
				HashMap<String,String> filter = new HashMap<String, String>();
				
				filter.put("cfg_code", "mobile_url");
				
				List<RefCodeBO> refInfos = baseAction.getRefInfo("roms_configuration",filter );
				
				if(refInfos.size() > 0)
				{
					String mobileUrl = refInfos.get(0).getAltId();
									 
					if(!StringUtils.isEmpty(mobileUrl) &&  req.getRequestURL().toString().toLowerCase().contains(mobileUrl.toLowerCase().trim()))
					{
						isMobile = true;
					}
					
				}
			//}
		//}		
		
		return isMobile;
	}
}
