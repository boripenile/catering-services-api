package app.services;

import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.lang.JoseException;

import app.dto.LoggedUserDTO;
import app.dto.PermissionsDTO;
import app.dto.RoleDTO;
import app.dto.Token;
import app.dto.UserDetailsDTO;
import app.exceptions.InvalidCredentialsException;
import app.exceptions.InvalidTokenException;

public interface AuthService {

	 	public LoggedUserDTO login(String username, String password, String appCode) throws InvalidCredentialsException, JoseException;

	 	public LoggedUserDTO login(String username, String appCode) throws InvalidCredentialsException, JoseException;
	 	
	 	public LoggedUserDTO getRoleAndPermission(String username, String orgCode) throws InvalidCredentialsException, JoseException;

	 	public UserDetailsDTO getUserRoleAndPermission(String username, String orgCode) throws InvalidCredentialsException, JoseException;

	    public String getToken(String username, String orgCode) throws JoseException, InvalidCredentialsException;

	    public String extendToken(String previousToken, String orgCode) throws JoseException, MalformedClaimException, InvalidCredentialsException, InvalidTokenException;

	    public String getUsername(String token) throws JoseException, MalformedClaimException;

	    public String generateToken(String username, String orgCode) throws JoseException, InvalidCredentialsException;
	    
	    public boolean isValid(String username, String password);
	    
	    public Token getTokenObject(String token) throws JoseException, MalformedClaimException;
	    
	    public PermissionsDTO getPermissionDTO(String userId, String appCode) throws Exception;
	    
	    public RoleDTO[] getRoleDTO(String userId, String appCode) throws Exception;
	    
}
