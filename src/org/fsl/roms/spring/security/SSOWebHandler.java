package org.fsl.roms.spring.security;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HttpsURLConnection;

import org.springframework.web.client.RestTemplate;

public class SSOWebHandler {
	
 public static String sendPost(String url, String param){
		 
		 RestTemplate restTemplate =  new RestTemplate();		 
		
		 
		 String jsonStr = restTemplate.postForObject(url,param,String.class);
		 
		// System.err.println(" Register Json Str: "+jsonStr);
		 
		 return jsonStr;
	 }
 
 
 
 /*public static String sendPost(String url, String postData) throws Exception {
		
		URL obj = new URL(url);
		
		URLConnection con = null;
		
		
		try{			
			 con = (HttpsURLConnection) getHttpsConn(obj,Integer.toString(postData.getBytes().length));
		}catch (Exception e) {
			//System.err.println(e.getMessage());
			
			//System.err.println("Switching protocol");
			con = (HttpURLConnection) getHttpConn(obj,Integer.toString(postData.getBytes().length));
			
		}		
		
		
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		
		
		
		wr.writeBytes(postData);
		wr.flush();
		wr.close();

		
		

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		

		return response.toString();
	}

	private static HttpURLConnection getHttpConn(URL obj, String length) throws IOException {
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		 
		//add reuqest header
		con.setDoOutput(true);
		con.setDoInput(true);
		con.setInstanceFollowRedirects(false);
		con.setRequestMethod("POST");
		con.setRequestProperty("Content-Type", "plain/text");
		con.setRequestProperty("charset", "utf-8");
		con.setRequestProperty("Content-Length", "" + length);
		con.setUseCaches (false);
		
		
		return con;
	}

	private static URLConnection getHttpsConn(URL obj, String length ) throws IOException{
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
		 
		//add reuqest header
		con.setDoOutput(true);
		con.setDoInput(true);
		con.setInstanceFollowRedirects(false);
		con.setRequestMethod("POST");
		con.setRequestProperty("Content-Type", "plain/text");
		con.setRequestProperty("charset", "utf-8");
		con.setRequestProperty("Content-Length", "" + length);
		con.setUseCaches (false);
		
		
		return con;
	}
 */
 
 

}
