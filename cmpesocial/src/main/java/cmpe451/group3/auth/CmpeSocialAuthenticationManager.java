package cmpe451.group3.auth;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import cmpe451.group3.utils.SecurityUtils;

public class CmpeSocialAuthenticationManager implements AuthenticationManager {
	
	final List<GrantedAuthority> AUTHORITIES = new ArrayList<GrantedAuthority>();

	public CmpeSocialAuthenticationManager(){
		//it should read authorities from database
		AUTHORITIES.add(new SimpleGrantedAuthority("ROLE_USER"));
	}

	public Authentication authenticate(Authentication auth) throws AuthenticationException {
			//we should get Authorities(undergraduate, graduate, alumni, staff etc..) column from user table
			return new UsernamePasswordAuthenticationToken(auth.getName(),
					auth.getCredentials(), AUTHORITIES);
		//throw new BadCredentialsException("Bad Credentials");
	}
}