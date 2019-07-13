package app.controllers;

import org.javalite.activeweb.AppController;
import org.javalite.activeweb.annotations.OPTIONS;
import org.javalite.activeweb.annotations.POST;
import org.javalite.activeweb.annotations.RESTful;
import org.javalite.common.JsonHelper;

import com.google.gson.Gson;
import com.google.inject.Inject;

import app.dto.LoggedUserDTO;
import app.dto.UserLogin;
import app.services.AuthService;

/**
 * @author Igor Polevoy on 9/29/14.
 */
@RESTful
public class LoginController extends AppController {

	@Inject
	private AuthService authService;
	
	@POST
    public void create(){
		if (header("action") != null && header("action").equalsIgnoreCase("getRolesAndPermissions")) {
			getRolesAndPermissions();
		} else {
			login();
		}
    	
    }
    
	public void login () {
		try {
    		if (header("app_code") != null) {
    			String payload = getRequestString();
    			UserLogin userLogin = new Gson().fromJson(payload, UserLogin.class);
    			if (authService.isValid(userLogin.getUsername(), userLogin.getPassword())) {
    				LoggedUserDTO loggedUser = authService.login(userLogin.getUsername(), userLogin.getPassword(), 
    						header("app_code"));
    				if (loggedUser.getApplication() != null) {
    					if (loggedUser.getOrganisations() != null) {
    						if (loggedUser.getOrganisations().size() > 1) {
        						view("code", 200, "message", "Successful", "data", loggedUser.getUser().toJson(true),
                						"application", loggedUser.getApplication() != null ? loggedUser.getApplication().toJson(true) : null,
                						"organisation", (loggedUser.getOrganisations() != null && loggedUser.getOrganisations().size() > 0) ? loggedUser.getOrganisations().toJson(true) : null);
                				render("userdata");
        					} else if (loggedUser.getOrganisations().size() == 1) {
        						view("code", 200, "message", "Successful", "data", loggedUser.getUser().toJson(true),
                						"token", loggedUser.getToken(), "roles", JsonHelper.toJsonString(loggedUser.getRoles()),
                						"permissions", JsonHelper.toJsonString(loggedUser.getPermissions()), 
                						"application", loggedUser.getApplication() != null ? loggedUser.getApplication().toJson(true) : null,
                						"organisation", (loggedUser.getOrganisations() != null && loggedUser.getOrganisations().size() > 0) ? loggedUser.getOrganisations().toJson(true) : null);
                				render("message");
        					}
    					} else {
    						view("code", 200, "message", "Successful", "data", loggedUser.getUser().toJson(true),
    								"token", loggedUser.getToken(),
            						"application", loggedUser.getApplication().toJson(true));
            				render("user");
    					}
    					
        				
    				} else {
        				view("code", 200, "message", "Successful", "data", loggedUser.getUser().toJson(true),
        						"token", loggedUser.getToken(), "roles", JsonHelper.toJsonString(loggedUser.getRoles()),
        						"permissions", JsonHelper.toJsonString(loggedUser.getPermissions()));
        				render("messages");
    				}
    				
        		} else {
        			view("code", 400, "message", "Invalid username or password");
        			render("error");
        		}
    		} else {
    			view("code", 400, "message", "app_code is required in the header.");
    			render("error");
    		}
		} catch (Exception e) {
			view("code", 400, "message", e.getMessage());
			render("error");
		}
	}
	public void getRolesAndPermissions () {
		if (header("org_code") != null) {
			try {
				String payload = getRequestString();
    			UserLogin userLogin = new Gson().fromJson(payload, UserLogin.class);
    				LoggedUserDTO loggedUser = authService.getRoleAndPermission(userLogin.getUsername(), 
    						header("org_code"));
    				if (loggedUser.getToken() != null) {
    					view("code", 200, "message", "Successful", "data", loggedUser.getUser().toJson(true),
        						"token", loggedUser.getToken(), "roles", JsonHelper.toJsonString(loggedUser.getRoles()),
        						"permissions", JsonHelper.toJsonString(loggedUser.getPermissions()));
        				render("rolespermissions");
    				} else {
        				view("code", 400, "message", "Invalid organisaion code was provided or username/password");
        				render("error");
    				}
			} catch (Exception e) {
				view("code", 400, "message", e.getMessage());
				render("error");
			}
		} else {
			view("code", 400, "message", "org_code is required as a header parameter.");
			render("error");
		}
	}
	
	@Override
	protected String getLayout() {
		return null;
	}

	@Override
	protected String getContentType() {
		return "application/json";
	}
    
	@OPTIONS
	public void options() {
		view("code", 200, "message", "Successful");
		render("error");
	}
}
