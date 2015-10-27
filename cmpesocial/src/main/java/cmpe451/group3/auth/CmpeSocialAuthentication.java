package cmpe451.group3.auth;

import org.springframework.security.authentication.*;
import org.springframework.security.core.*;
import org.springframework.security.core.context.SecurityContextHolder;

public class CmpeSocialAuthentication {
	private static AuthenticationManager am = new CmpeSocialAuthenticationManager();

	public static void getAuthentication (String email, String password) {

		try {
			Authentication request = new UsernamePasswordAuthenticationToken(email, password);
			Authentication result = am.authenticate(request);
			SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_GLOBAL);
			SecurityContextHolder.getContext().setAuthentication(result);
		} catch(AuthenticationException e) {
			System.out.println("Authentication failed: " + e.getMessage());
		}
		System.out.println("Successfully authenticated. Security context contains: " +
				SecurityContextHolder.getContext().getAuthentication());
	}
}