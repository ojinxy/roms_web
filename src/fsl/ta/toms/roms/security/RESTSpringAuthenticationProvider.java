package fsl.ta.toms.roms.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import fsl.ta.toms.roms.constants.Constants;

public class RESTSpringAuthenticationProvider implements AuthenticationProvider{

	//private ROMSUserDetailsService userDetailsService;
	
	@Override
	public Authentication authenticate(Authentication auth)
			throws AuthenticationException {
		
		
		//UserDetails userdetails = userDetailsService.loadUserByUsername(auth.getName());
		
		UserDetails userdetails = null;
		
		System.err.println("In  ROMS Authenticator");
		
		
		return new UsernamePasswordAuthenticationToken(userdetails, "", userdetails.getAuthorities());
	}

	@Override
	public boolean supports(Class<?> authentication) {
		
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
	
	/*public void setUserDetailsService(ROMSUserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}*/
	
	
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

}
