package org.fsl.roms.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.fsl.roms.constants.Constants;
import org.fsl.roms.service.action.BaseServiceAction;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import fsl.ta.toms.roms.bo.LMISUserViewBO;
import fsl.ta.toms.roms.exception.RequiredFieldMissingException;



public class ROMSUserDetailsService implements UserDetailsService {
	
	//@Autowired
	BaseServiceAction baseAction = new BaseServiceAction();


	@Override
	public UserDetails loadUserByUsername(String username){
		
		ROMSUserDetails userDetails = new ROMSUserDetails();
		fsl.ta.toms.roms.webservices.ROMSUserDetails userDetailsProxy = new fsl.ta.toms.roms.webservices.ROMSUserDetails();
		
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();
		

		try{
			//request.setAttribute("userName", username);
			HttpSession session = request.getSession();		
			
			if(session != null)
				session.setAttribute("SPRING_SECURITY_FORM_USERNAME", username);
			
			//web service fixed get user
			LMISUserViewBO userView = userDetailsProxy.findUser(username);
			
			//construct the user details 
			if(userView != null){				
				
				userDetails = new ROMSUserDetails(userView);				
				
				//set the defined roles for a user									
				userDetails.setAuthorities(convertResourcesToAuthorities(userView.getPermissions()));//getAuthoritiesForUser(userView.getUsername()));		
				//System.err.println(" userdetails name :" + userDetails.getUserFirstName());
			}else{
				
				throw new UsernameNotFoundException("User " + username +"  does not exist");
				
			}
			//ensure if there is a problem accessing database you catch it
		} catch (RequiredFieldMissingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new  UsernameNotFoundException("Database Error has occured. Please contact Administrator.");
		}catch(Exception ex){
			ex.printStackTrace();
			throw new UsernameNotFoundException("Error has occured. Please contact Administrator.");
		}
		
		return userDetails;
	
	}//public UserDetails loadUserByUsername(String username)
	
	
	
	/**
	 * Organise the resources as Granted Authority
	 * @param resources
	 * @return
	 */
	private Collection<GrantedAuthority> convertResourcesToAuthorities(List<String> resources ){
		
		Collection<GrantedAuthority> authList = new ArrayList<GrantedAuthority>();
			
		for(String thisResource:resources){			
			String roleName = Constants.SecurityRoles.ROLE_PREFIX + thisResource;
			//System.err.println(roleName);
			authList.add(new SimpleGrantedAuthority(roleName));			
		}		
		authList.add(new SimpleGrantedAuthority(Constants.SecurityRoles.GENERAL_USER_ROLE));
		
		return authList;
		
	}
	
	 private HashMap<String, String> removeDuplicateResources(){
		 HashMap<String, String> map = new HashMap<String, String>();
		 Set<String> keys = map.keySet(); // The set of keys in the map.

		 Iterator<String> keyIter = keys.iterator();

		 while (keyIter.hasNext()) {
		     String key = keyIter.next();
		     String value = map.get(key);
		     map.put(value, key);
		 }
		 
		 return map;
	 }
	
	
	

}
