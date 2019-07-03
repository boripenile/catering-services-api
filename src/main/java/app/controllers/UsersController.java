package app.controllers;

import org.javalite.activejdbc.LazyList;
import org.javalite.activeweb.AppController;
import org.javalite.activeweb.annotations.OPTIONS;
import org.javalite.activeweb.annotations.RESTful;
import org.javalite.common.JsonHelper;

import com.google.gson.Gson;
import com.google.inject.Inject;

import app.dto.LoggedUserDTO;
import app.dto.RegistrationDTO;
import app.dto.UserDetailsDTO;
import app.dto.UserLogin;
import app.models.User;
import app.services.AuthService;
import app.services.UserService;

@RESTful
public class UsersController extends AppController {

	@Inject
	private UserService userService;
	
	@Inject
	private AuthService authService;
	
	public void index() {
		if (header("action") != null) {
			String actionName = header("action").toString();
			switch (actionName) {
			case "findUsersByOrganisation":
				findUsersByOrganisation();
				break;
			default:
				break;
			}
		} else {
			findAll();
		}
	}
	
	public void create() {
		if (header("action") != null) {
			String actionName = header("action").toString();
			switch (actionName) {
			case "registerUser":
				logInfo("I am calling function registerUser()");
				registerUser();
				break;
			case "verifyUserEmail":
				logInfo("I am calling function verifyUserEmail()");
				verifyUserEmail();
				break;
			case "validateReferralCode":
				logInfo("I am calling function validateReferralCode()");
				validateReferralCode();
				break;
			case "findUserByEmailOrUsername":
				logInfo("I am calling function findUserByEmailOrUsername()");
				findUserByEmailOrUsername();
				break;
			case "findUserDetails":
				logInfo("I am calling function findUserDetails()");
				findUserDetails();
				break;
			default:
				break;
			}
		}
	}
	
	public void findUserDetails() {
		if (header("org_code") != null) {
			try {
				String payload = getRequestString();
    			UserLogin userLogin = new Gson().fromJson(payload, UserLogin.class);
    				UserDetailsDTO userDetails = authService.getUserRoleAndPermission(userLogin.getUsername(), 
    						header("org_code"));
    				if (userDetails.getUser() != null) {
    					view("code", 200, "message", "Successful", "data", userDetails.getUser().toJson(true),
        						"roles", JsonHelper.toJsonString(userDetails.getRoles()),
        						"permissions", JsonHelper.toJsonString(userDetails.getPermissions()));
        				render("userdetails");
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
	
	public void registerUser() {
		try {
			String payload = getRequestString();
			RegistrationDTO registration = new Gson().fromJson(payload, RegistrationDTO.class);
			
			String result = userService.registerUser(registration);
			
			if (result != null) {
				view("code", 200, "message", result);
				render("error");
			} else {
				view("code", 400, "message", "Registration cannot be completed");
				render("error");
			}
		} catch (Exception e) {
			logError(e.toString(), e);
			view("code", 400, "message", e.getMessage() != null ? e.getMessage() : "Error occured");
			render("error");
		}
	}
	
	public void verifyUserEmail() {
		try {
			if (header("verify_code") != null && header("app_code") != null) {
				String verifyCode = header("verify_code").toString();
				String appCode = header("app_code").toString();
				LoggedUserDTO loggedUser = userService.verifyEmailAddress(verifyCode, appCode);
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
    				render("result");
				}
			} else {
				view("code", 400, "message", "verify_code and app_code are required as header parameters");
				render("error");
			}
		} catch (Exception e) {
			logError(e.toString(), e);
			view("code", 400, "message", e.getMessage() != null ? e.getMessage() : "Error occured");
			render("error");
		}
	}
	
	public void validateReferralCode() {
		try {
			if (header("referral_code") != null && header("app_code") != null) {
				String referralCode = header("referral_code").toString();
				boolean result = userService.validateReferralCode(referralCode);
				if (result) {
					view("code", 200, "message", "validated");
					render("error");
				}
			} else {
				view("code", 400, "message", "referral_code and app_code are required as header parameters");
				render("error");
			}
		} catch (Exception e) {
			logError(e.toString(), e);
			view("code", 400, "message", e.getMessage() != null ? e.getMessage() : "Error occured");
			render("error");
		}
	}
	
	public void findUserByEmailOrUsername() {
		try {
			if (header("search_parameter") != null) {
				String searchParam = header("search_parameter").toString();
				User result = userService.getUserByEmailOrUsername(searchParam);
				if (result != null) {
					view("code", 200, "total", 1, "data", result.toJson(true));
					render("message");
				}
			} else {
				view("code", 400, "message", "search_parameter must be user's email address or username");
				render("error");
			}
		} catch (Exception e) {
			logError(e.toString(), e);
			view("code", 400, "message", e.getMessage() != null ? e.getMessage() : "Error occured");
			render("error");
		}
	}
	
	public void findUsersByOrganisation() {
		try {
			if (header("org_code") != null) {
				LazyList<User> users = userService.getUsersByOrganisationCode(header("org_code"));
				if (users != null) {
					view("code", 200, "total", users.size(), "data", users.toJson(true));
					render("message");
				} else {
					view("code", 400, "message", "No users found");
					render("error");
				}
			}
		} catch (Exception e) {
			logError(e.toString(), e);
			view("code", 400, "message", e.getMessage() != null ? e.getMessage() : "Error occured");
			render("error");
		}
	}
	
	public void findAll() {
		try {
			
		} catch (Exception e) {
			logError(e.toString(), e);
			view("code", 400, "message", e.getMessage() != null ? e.getMessage() : "Error occured");
			render("error");
		}
	}
	
	@Override
	protected String getContentType() {
		return "application/json";
	}

	@Override
	protected String getLayout() {
		return null;
	}

	@OPTIONS
	public void options() {
		view("code", 200, "message", "Successful");
		render("error");
	}
}
