package app.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.javalite.activejdbc.LazyList;
import org.javalite.common.JsonHelper;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.keys.HmacKey;
import org.jose4j.lang.JoseException;

import com.google.gson.Gson;

import app.dto.BasicPermissionDTO;
import app.dto.LoggedUserDTO;
import app.dto.PermissionsDTO;
import app.dto.RoleDTO;
import app.dto.SelfPermissionDTO;
import app.dto.Token;
import app.dto.UserDetailsDTO;
import app.exceptions.InvalidCredentialsException;
import app.exceptions.InvalidTokenException;
import app.models.Application;
import app.models.Organisation;
import app.models.Permission;
import app.models.Role;
import app.models.User;
import app.services.AuthService;
import app.utils.CommonUtil;
import app.utils.Constants;
import app.utils.IPasswords;
import app.utils.Passwords;
import app.utils.Utils;

public class AuthServiceImpl implements AuthService {

	private String appCode = null;
	
	@Override
	public LoggedUserDTO login(String username, String password, String appCode) throws InvalidCredentialsException, JoseException {
//		try {
			this.appCode = appCode;
			LazyList<User> user = null;
			if (password != null) {
				user = User.findBySQL("select users.* from users where username=? or email_address=?",
						username, username);
				if (user != null) {
					Map<String, byte[]> results = Utils.getPasswordAndSaltInBytes(user.get(0).getString("password"), 
							user.get(0).getString("salt"));
					IPasswords pass = new Passwords();
					if (!pass.isExpectedPassword(password, results.get("salt"), results.get("password"))) {
						throw new JoseException("Invalid username/password");
					}
				}
			} else {
				user = User.findBySQL("select users.* from users where username=? or email_address=?", 
						username, username);
			}
			
			if (user.size() > 0) {
				if (!user.get(0).getBoolean("active")) {
					throw new InvalidCredentialsException("Your account has been disabled. Please contact your administrator.");
				}
				LazyList<Application> app = null;
				LoggedUserDTO loggedUser = new LoggedUserDTO();
				
				app = Application.findBySQL("select applications.* from applications "
						+ "where applications.app_code=? and applications.active=?", 
						this.appCode, 1);
				
				int appLen = app.size();
				if (appLen > 0) {
					int length = 0; 
					LazyList<Organisation> organisations = null;
					try {
						organisations = Organisation.findBySQL("select organisations.* from organisations "
								+ "inner join applications on applications.id=organisations.application_id inner join users_organisations "
								+ "on users_organisations.application_id=applications.id inner join users on "
								+ "users.id=users_organisations.user_id "
								+ "where applications.app_code=? and applications.active=? "
								+ "and users_organisations.organisation_id=organisations.id and users.id=?", 
								this.appCode, 1, user.get(0).get("id"));
						length = organisations.size();
					} catch (Exception e) {
						System.out.println("Error occured: " + e.getMessage());
					}		
					if (length > 0) {
						if (length == 1) {
							loggedUser = addRolesAndPermissions(user.get(0), organisations.get(0).getString("code"));
							loggedUser.setApplication(app.get(0));
							loggedUser.setOrganisation(organisations);
						} else {
							loggedUser.setUser(user.get(0));
							loggedUser.setApplication(app.get(0));
							loggedUser.setOrganisation(organisations);
						}
					} else {
						loggedUser = addRolesAndPermissions(user.get(0), null);
						loggedUser.setOrganisation(null);
						loggedUser.setApplication(app.get(0));
					}
				} else {
					loggedUser.setApplication(null);
					loggedUser.setOrganisation(null);
				}
				return loggedUser;
			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//		}
		return null;
	}

	@Override
	public String getToken(String username, String orgCode) throws JoseException, InvalidCredentialsException {
		return generateToken(username, orgCode);
	}

	@Override
	public String getUsername(String token) throws JoseException, MalformedClaimException {
		if (token == null) {
			return null;
		}
		Properties tokenize = CommonUtil.loadPropertySettings("tokenize");
		JsonWebSignature receiverJws = new JsonWebSignature();
		receiverJws.setCompactSerialization(token);
		receiverJws.setKey(new HmacKey(tokenize.getProperty("token_secret").getBytes()));
		boolean signatureVerified = receiverJws.verifySignature();
		if (signatureVerified) {
			String plaintext = receiverJws.getPayload();
			Token login = new Gson().fromJson(plaintext, Token.class);
			if (System.currentTimeMillis() - login.getTimestamp() <= Constants.TOKEN_TTL_MS) {
				return login.getUsername();
			}
		}
		return null;
	}

	@Override
	public String generateToken(String username, String orgCode) throws JoseException, InvalidCredentialsException {
		try {
			User user = User.findFirst("username=? or email_address=? and active=?", username, username, 1);
			String token = addRolesAndPermissions(user, orgCode).getToken();
			return token;
		} finally {
			//Base.close();
		}
	}

	@Override
	public boolean isValid(String username, String password) {
		try {
			User user = User.findFirst("username=? or email_address=?", username, username);
			if (user != null) {
				Map<String, byte[]> results = Utils.getPasswordAndSaltInBytes(user.getString("password"), 
						user.getString("salt"));
				IPasswords pass = new Passwords();
				if (pass.isExpectedPassword(password, results.get("salt"), results.get("password"))) {
					return true;
				} else {
					return false;
				}
			}
		} catch (Exception e) {
		} finally {
		}
		return false;
	}

	@Override
	public String extendToken(String previousToken, String orgCode)
			throws JoseException, MalformedClaimException, InvalidCredentialsException, InvalidTokenException {
		String username = getUsername(previousToken);
		if (username == null) {
			throw new InvalidTokenException();
		}
		return generateToken(username, orgCode);
	}
	
	private LoggedUserDTO addRolesAndPermissions(User user, String orgCode) {
		LoggedUserDTO logged = new LoggedUserDTO();
		if (orgCode != null) {
			List<Role> roles = Role
					.findBySQL("select roles.* from roles inner join users_roles on users_roles.role_id=roles.id "
							+ "inner join users on users_roles.user_id=users.id inner join organisations "
							+ "on organisations.id=users_roles.organisation_id "
							+ "where users.id=? and organisations.code=? and users_roles.active=?", 
							user.getId(), orgCode, true);
			int length = roles.size();
			PermissionsDTO userPermissions = null;
			if (length > 0) {
				userPermissions = computeBasicPermissions(logged, roles, length, orgCode);
			}
			List<Permission> permissions = Permission
					.findBySQL(
							"select permissions.* from permissions inner join users_permissions "
									+ "on users_permissions.permission_id=permissions.id "
									+ "inner join users on users_permissions.user_id=users.id inner join organisations "
									+ "on organisations.id=users_permissions.organisation_id "
									+ "where users.id=? and organisations.code=? "
									+ "and users_permissions.active=?",
							user.getId(), orgCode, true);
			if (permissions != null) {
				int pemLength = permissions.size();
				if (pemLength > 0) {
					SelfPermissionDTO[] strPermissions = new SelfPermissionDTO[pemLength];
					for (int j = 0; j < pemLength; j++) {
						SelfPermissionDTO self = new SelfPermissionDTO();
						self.setName(permissions.get(j).getString("permission_name"));
						self.setDescription(permissions.get(j).getString("description"));
						strPermissions[j] = self;
					}
					userPermissions.setSelf(strPermissions);
				}
			}
			userPermissions = confirmPermissions(userPermissions);
			logged.setPermissions(userPermissions);
			logged.setPermissions(userPermissions);
		}
		try {
			String token = generateToken(user, logged);
			logged.setToken(token);
		} catch (JoseException e) {
			e.printStackTrace();
		}
		user.set("password", null);
		user.set("salt", null);
		logged.setUser(user);
		return logged;
	}
	
	private UserDetailsDTO addRolesAndPermissionsByUser(User user, String orgCode) {
		UserDetailsDTO logged = new UserDetailsDTO();
		List<Role> roles = Role
				.findBySQL("select roles.* from roles inner join users_roles on users_roles.role_id=roles.id "
						+ "inner join users on users_roles.user_id=users.id inner join organisations "
						+ "on organisations.id=users_roles.organisation_id "
						+ "where users.id=? and organisations.code=? and users_roles.active=?", 
						user.getId(), orgCode, true);
		int length = roles.size();
		PermissionsDTO userPermissions = null;
		if (length > 0) {
			userPermissions = computeBasicPermissions(logged, roles, length, orgCode);
		}
		List<Permission> permissions = Permission
				.findBySQL(
						"select permissions.* from permissions inner join users_permissions "
								+ "on users_permissions.permission_id=permissions.id "
								+ "inner join users on users_permissions.user_id=users.id inner join organisations "
								+ "on organisations.id=users_permissions.organisation_id "
								+ "where users.id=? and organisations.code=? "
								+ "and users_permissions.active=?",
						user.getId(), orgCode, true);
		if (permissions != null) {
			int pemLength = permissions.size();
			if (pemLength > 0) {
				SelfPermissionDTO[] strPermissions = new SelfPermissionDTO[pemLength];
				for (int j = 0; j < pemLength; j++) {
					SelfPermissionDTO self = new SelfPermissionDTO();
					self.setName(permissions.get(j).getString("permission_name"));
					self.setDescription(permissions.get(j).getString("description"));
					strPermissions[j] = self;
				}
				userPermissions.setSelf(strPermissions);
			}
		}
		userPermissions = confirmPermissions(userPermissions);
		logged.setPermissions(userPermissions);
		user.set("password", null);
		user.set("salt", null);
		logged.setPermissions(userPermissions);
		logged.setUser(user);
		return logged;
	}

	public RoleDTO[] getRoleDTO(String userId, String appCode) throws Exception {
		List<Role> roles = Role
				.findBySQL("select roles.* from roles inner join users_roles on users_roles.role_id=roles.id "
						+ "inner join users on users_roles.user_id=users.id inner join applications "
						+ "on applications.id=roles.application_id "
						+ "where users.id=? and applications.app_code=? "
						+ "and users_roles.active=?", 
						userId, appCode, true);
		int length = roles.size();
		RoleDTO[] strRoles = new RoleDTO[length];
		for (int i = 0; i < length; i++) {
			RoleDTO role = new RoleDTO();
			role.setName(roles.get(i).getString("role_name"));
			role.setDescription(roles.get(i).getString("description"));
			strRoles[i] = role;
		}
		return strRoles;
	}
	
	public PermissionsDTO getPermissionDTO(String userId, String appCode) throws Exception {
		List<Role> roles = Role
				.findBySQL("select roles.* from roles inner join users_roles on users_roles.role_id=roles.id "
						+ "inner join users on users_roles.user_id=users.id inner join applications "
						+ "on applications.id=roles.application_id "
						+ "where users.id=? and applications.app_code=? "
						+ "and users_roles.active=?", 
						userId, appCode, true);
		int length = roles.size();
		PermissionsDTO userPermissions = null;
		if (length > 0) {
			userPermissions = computeBasicPermissions(roles, length, appCode);
		}
		List<Permission> permissions = Permission
				.findBySQL(
						"select permissions.* from permissions inner join users_permissions "
								+ "on users_permissions.permission_id=permissions.id "
								+ "inner join users on users_permissions.user_id=users.id inner join applications "
								+ "on applications.id=permissions.application_id "
								+ "where users.id=? and applications.app_code=? "
								+ "and users_permissions.active=?",
						userId, appCode, true);
		if (permissions != null) {
			int pemLength = permissions.size();
			if (pemLength > 0) {
				SelfPermissionDTO[] strPermissions = new SelfPermissionDTO[pemLength];
				for (int j = 0; j < pemLength; j++) {
					SelfPermissionDTO self = new SelfPermissionDTO();
					self.setName(permissions.get(j).getString("permission_name"));
					self.setDescription(permissions.get(j).getString("description"));
					strPermissions[j] = self;
				}
				userPermissions.setSelf(strPermissions);
			}
		}
		userPermissions = confirmPermissions(userPermissions);
		return userPermissions;
	}
	
	private PermissionsDTO computeBasicPermissions(List<Role> roles, int length, String appCode) {
		PermissionsDTO userPermissions;
		userPermissions = new PermissionsDTO();
		RoleDTO[] strRoles = new RoleDTO[length];
		for (int i = 0; i < length; i++) {
			RoleDTO role = new RoleDTO();
			role.setName(roles.get(i).getString("role_name"));
			role.setDescription(roles.get(i).getString("description"));
			strRoles[i] = role;
		}
		List<BasicPermissionDTO> totalPermissions = new ArrayList<BasicPermissionDTO>();
		
		for (RoleDTO role : strRoles) {
			computeRolePermissions(totalPermissions, role, appCode);
		}
		if (totalPermissions.size() > 0) {
			BasicPermissionDTO[] perms = new BasicPermissionDTO[totalPermissions.size()];
			for (int k = 0; k < totalPermissions.size(); k++) {
				perms[k] = totalPermissions.get(k);
			}
			userPermissions.setBasic(perms);
		}
		return userPermissions;
	}

	private PermissionsDTO confirmPermissions(PermissionsDTO userPermissions) {
		if (userPermissions == null) {
			userPermissions = new PermissionsDTO();
			userPermissions.setBasic(new BasicPermissionDTO[] {});
			userPermissions.setSelf(new SelfPermissionDTO[] {});
		} else if (userPermissions.getBasic() != null && userPermissions.getSelf() == null) {
			userPermissions.setSelf(new SelfPermissionDTO[] {});
		} else if (userPermissions.getSelf() != null && userPermissions.getBasic() == null) {
			userPermissions.setBasic(new BasicPermissionDTO[] {});
		}
		return userPermissions;
	}

	private String generateToken(User user, LoggedUserDTO logged) throws JoseException {
		String username = user.getString("username");
		Properties tokenize = CommonUtil.loadPropertySettings("tokenize");
		JsonWebSignature jws = new JsonWebSignature();

		Token login = new Token(username, System.currentTimeMillis());
		if (logged.getRoles() != null && logged.getRoles().length > 0) {
			login.setRoles(logged.getRoles());
		}
		if (logged.getPermissions() != null) {
			if (logged.getPermissions().getBasic() != null) {
				login.setBasicPermissions(logged.getPermissions().getBasic());
			}
			if (logged.getPermissions().getSelf() != null) {
				login.setSelfPermissions(logged.getPermissions().getSelf());
			}

		}

		jws.setHeader("type", "JWT");
		jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.HMAC_SHA256);
		jws.setPayload(JsonHelper.toJsonString(login));
		jws.setKey(new HmacKey(tokenize.getProperty("token_secret").getBytes()));
		jws.setDoKeyValidation(false);
		String token = jws.getCompactSerialization();
		return token;
	}

	private PermissionsDTO computeBasicPermissions(LoggedUserDTO logged, List<Role> roles, int length, String orgCode) {
		PermissionsDTO userPermissions;
		userPermissions = new PermissionsDTO();
		RoleDTO[] strRoles = new RoleDTO[length];
		for (int i = 0; i < length; i++) {
			RoleDTO role = new RoleDTO();
			role.setName(roles.get(i).getString("role_name"));
			role.setDescription(roles.get(i).getString("description"));
			strRoles[i] = role;
		}
		logged.setRoles(strRoles);
		List<BasicPermissionDTO> totalPermissions = new ArrayList<BasicPermissionDTO>();
		
		for (RoleDTO role : strRoles) {
			computeRolePermissions(totalPermissions, role, orgCode);
		}
		if (totalPermissions.size() > 0) {
			BasicPermissionDTO[] perms = new BasicPermissionDTO[totalPermissions.size()];
			for (int k = 0; k < totalPermissions.size(); k++) {
				perms[k] = totalPermissions.get(k);
			}
			userPermissions.setBasic(perms);
		}
		return userPermissions;
	}
	
	private PermissionsDTO computeBasicPermissions(UserDetailsDTO logged, List<Role> roles, int length, String orgCode) {
		PermissionsDTO userPermissions;
		userPermissions = new PermissionsDTO();
		RoleDTO[] strRoles = new RoleDTO[length];
		for (int i = 0; i < length; i++) {
			RoleDTO role = new RoleDTO();
			role.setName(roles.get(i).getString("role_name"));
			role.setDescription(roles.get(i).getString("description"));
			strRoles[i] = role;
		}
		logged.setRoles(strRoles);
		List<BasicPermissionDTO> totalPermissions = new ArrayList<BasicPermissionDTO>();
		
		for (RoleDTO role : strRoles) {
			computeRolePermissions(totalPermissions, role, orgCode);
		}
		if (totalPermissions.size() > 0) {
			BasicPermissionDTO[] perms = new BasicPermissionDTO[totalPermissions.size()];
			for (int k = 0; k < totalPermissions.size(); k++) {
				perms[k] = totalPermissions.get(k);
			}
			userPermissions.setBasic(perms);
		}
		return userPermissions;
	}

	
	private void computeRolePermissions(List<BasicPermissionDTO> totalPermissions, RoleDTO role) {
		List<Permission> permissions = Permission.findBySQL(
				"select permissions.* from permissions inner join roles_permissions "
						+ "on roles_permissions.permission_id=permissions.id "
						+ "inner join roles on roles_permissions.role_id=roles.id inner join applications " 
						+ "on applications.id=permissions.application_id where roles.role_name=? "
						+ "and roles.active=? and applications.app_code=? and roles_permissions.active=?",
				role.getName(), 1, this.appCode, true);
		if (permissions != null) {
			int pemLength = permissions.size();
			if (pemLength > 0) {
				List<BasicPermissionDTO> strPermissions = new ArrayList<BasicPermissionDTO>();
				for (int j = 0; j < pemLength; j++) {
					BasicPermissionDTO perm = new BasicPermissionDTO();
					perm.setName(permissions.get(j).getString("permission_name"));
					perm.setDescription(permissions.get(j).getString("description"));
					strPermissions.add(perm);
				}
				totalPermissions.addAll(strPermissions);
			}
		}
	}
	
	private void computeRolePermissions(List<BasicPermissionDTO> totalPermissions, RoleDTO role, String orgCode) {
		List<Permission> permissions = Permission.findBySQL(
				"select permissions.* from permissions inner join roles_permissions "
						+ "on roles_permissions.permission_id=permissions.id "
						+ "inner join roles on roles_permissions.role_id=roles.id inner join organisations " 
						+ "on organisations.id=roles_permissions.organisation_id where roles.role_name=? "
						+ "and roles.active=? and organisations.code=? and roles_permissions.active=?",
				role.getName(), 1, orgCode, true);
		if (permissions != null) {
			int pemLength = permissions.size();
			if (pemLength > 0) {
				List<BasicPermissionDTO> strPermissions = new ArrayList<BasicPermissionDTO>();
				for (int j = 0; j < pemLength; j++) {
					BasicPermissionDTO perm = new BasicPermissionDTO();
					perm.setName(permissions.get(j).getString("permission_name"));
					perm.setDescription(permissions.get(j).getString("description"));
					strPermissions.add(perm);
				}
				totalPermissions.addAll(strPermissions);
			}
		}
	}
	
	@Override
	public Token getTokenObject(String token) throws JoseException, MalformedClaimException {
		if (token == null) {
			return null;
		}
		Properties tokenize = CommonUtil.loadPropertySettings("tokenize");
		JsonWebSignature receiverJws = new JsonWebSignature();
		receiverJws.setCompactSerialization(token);
		receiverJws.setKey(new HmacKey(tokenize.getProperty("token_secret").getBytes()));
		boolean signatureVerified = receiverJws.verifySignature();
		if (signatureVerified) {
			String plaintext = receiverJws.getPayload();
			Token login = new Gson().fromJson(plaintext, Token.class);
			if (System.currentTimeMillis() - login.getTimestamp() <= Constants.TOKEN_TTL_MS) {
				return login;
			}
		}
		throw new JoseException("Token has expired.");
	}

	@Override
	public LoggedUserDTO getRoleAndPermission(String username, String orgCode)
			throws InvalidCredentialsException, JoseException {
		try {
			User user = User.findFirst("username=? or email_address=?", username, username);
			if (user != null) {
				return addRolesAndPermissions(user, orgCode);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public LoggedUserDTO login(String username, String appCode) throws InvalidCredentialsException, JoseException {
		// TODO Auto-generated method stub
		return login(username, null, appCode);
	}

	@Override
	public UserDetailsDTO getUserRoleAndPermission(String username, String orgCode)
			throws InvalidCredentialsException, JoseException {
		try {
			User user = User.findFirst("username=? or email_address=?", username, username);
			if (user != null) {
				return addRolesAndPermissionsByUser(user, orgCode);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
