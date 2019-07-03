package app.dto;

import java.io.Serializable;

import org.javalite.activejdbc.LazyList;

import app.models.Application;
import app.models.Organisation;
import app.models.User;

public class LoggedUserDTO implements Serializable{

	private static final long serialVersionUID = 1L;

	private User user;

	private String token;
	
	private RoleDTO[] roles;
	
	private PermissionsDTO permissions;
	
	private Application application;
	
	private LazyList<Organisation> organisations;
	
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public RoleDTO[] getRoles() {
		return roles;
	}

	public void setRoles(RoleDTO[] roles) {
		this.roles = roles;
	}

	public PermissionsDTO getPermissions() {
		return permissions;
	}

	public void setPermissions(PermissionsDTO permissions) {
		this.permissions = permissions;
	}

	public LazyList<Organisation> getOrganisations() {
		return organisations;
	}

	public void setOrganisation(LazyList<Organisation> organisations) {
		this.organisations = organisations;
	}

	public Application getApplication() {
		return application;
	}

	public void setApplication(Application application) {
		this.application = application;
	}

}
