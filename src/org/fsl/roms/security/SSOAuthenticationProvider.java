package org.fsl.roms.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

public class SSOAuthenticationProvider implements AuthenticationProvider{

	private ROMSUserDetailsService userDetailsService;
	
	@Override
	public Authentication authenticate(Authentication auth)
			throws AuthenticationException {
		
		
		UserDetails userdetails = userDetailsService.loadUserByUsername(auth.getName());
		return new UsernamePasswordAuthenticationToken(userdetails, "", userdetails.getAuthorities());
	}

	@Override
	public boolean supports(Class<?> authentication) {
		
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
	
	public void setUserDetailsService(ROMSUserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

}
