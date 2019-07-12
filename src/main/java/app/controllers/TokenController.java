package app.controllers;

import org.javalite.activeweb.AppController;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.lang.JoseException;

import com.google.inject.Inject;

import app.dto.Token;
import app.services.AuthService;

public class TokenController extends AppController{

	@Inject
	private AuthService authService;
	
	public String getUsername() {
		String tokenStr = header("token").toString();
		try {
			Token token = authService.getTokenObject(tokenStr);
			if (token != null) {
				return token.getUsername();
			}
		} catch (MalformedClaimException | JoseException e) {
		}
		return null;
	}
}
