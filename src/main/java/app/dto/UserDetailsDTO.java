package app.dto;

import app.models.User;

public class UserDetailsDTO {

	private User user;
	
	private RoleDTO[] roles;
	
	private PermissionsDTO permissions;

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
	
}
