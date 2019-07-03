package app.dto;

public class RolesPermissionsDTO {

	private String orgCode;
	
	private String[] roleNames;
	
	private String[] permissionNames;

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String[] getRoleNames() {
		return roleNames;
	}

	public void setRoleNames(String[] roleNames) {
		this.roleNames = roleNames;
	}

	public String[] getRolesNames() {
		return roleNames;
	}

	public void setRolesNames(String[] rolesNames) {
		this.roleNames = rolesNames;
	}

	public String[] getPermissionNames() {
		return permissionNames;
	}

	public void setPermissionNames(String[] permissionNames) {
		this.permissionNames = permissionNames;
	}

	public RolesPermissionsDTO() {
		// TODO Auto-generated constructor stub
	}
}
