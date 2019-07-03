package app.filters;

import org.javalite.activeweb.controller_filters.HttpSupportFilter;
import org.javalite.common.Collections;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.lang.JoseException;

import com.google.inject.Inject;

import app.dto.BasePermissionDTO;
import app.dto.RoleDTO;
import app.dto.Token;
import app.services.AuthService;
import app.utils.CommonUtil;

public class AuthorizationFilter extends HttpSupportFilter {

	@Inject
	private AuthService authService;
	
	@Override
	public void before() {
		if (getRoute().getMethod().toString().equalsIgnoreCase("OPTIONS")) {
			System.out.println("METHOD: " + getRoute().getMethod().toString());
			render("/layouts/error", Collections.map("code", 200, "message", "Successful"));
		}
		if (header("token") != null) {
			String tokenStr = header("token").toString(); 
			try {
				Token token = authService.getTokenObject(tokenStr);
				if (token.getRoles() != null) {
					RoleDTO[] roles = token.getRoles();
					for (int i = 0; i < token.getRoles().length; i++) {
						if (roles[i].getName().equalsIgnoreCase("superadmin")) {
							return;
						}
					}
					int selfLength = token.getSelfPermissions().length > 0 ? token.getSelfPermissions().length : 0;
					int basicLength = token.getBasicPermissions().length > 0 ? token.getBasicPermissions().length : 0;
					
					BasePermissionDTO[] permissions = new BasePermissionDTO [selfLength + basicLength];
					if (basicLength > 0) {
						for (int k = 0; k < basicLength; k++) {
							permissions[k] = token.getBasicPermissions()[k];
						}
					}
					if (selfLength > 0) {
						for (int l = 0; l < selfLength; l++) {
							permissions[l] = token.getSelfPermissions()[l];
						}
					}
					String controllerName = getRoute().getController().toString();
					String actionName = getRoute().getActionName();
					
					if (header("action") != null) {
						actionName = header("action").toString();
					}
					if (actionName.equals("index")) {
						actionName = "list";
					}
					if (actionName.equals("show")) {
						actionName = "view";
					}
					for (int p = 0; p < permissions.length; p++) {
						if (permissions[p].equals("*")) {
							return;
						}
						String[] splitPerms = permissions[p].getName().split(":");
						if (controllerName.toLowerCase().contains(splitPerms[0].toLowerCase()) && 
								splitPerms[1].toLowerCase().equalsIgnoreCase(actionName.toLowerCase())) {
							return;
						}
					}
				}
				render("/layouts/error", Collections.map("code", 401, "message", "You do not have the right permission to perform this operation")); 
			} catch (MalformedClaimException e) {
				logError(e.toString(), e);
				render("/layouts/error", Collections.map("code", 400, "message", e.getMessage() != null ? e.getMessage() : "Error occured"));
			} catch (JoseException e) {
				logError(e.toString(), e);
				render("/layouts/error", Collections.map("code", 400, "message", e.getMessage() != null ? e.getMessage() : "Error occured")); 
			} 
		} else {
			if (header("action") != null && header("action").toString().equalsIgnoreCase("registerUser")) {
				return;
			} else if (header("action") != null && header("action").toString().equalsIgnoreCase("verifyUserEmail")) {
				return;
			} else if (header("action") != null && header("action").toString().equalsIgnoreCase("validateReferralCode")) {
				return;
			} else {
				render("/layouts/error", Collections.map("code", 400, "message", "Token is requied in the header"));
			}
		}
	}
	
	
}
