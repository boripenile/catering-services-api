package app.dto;

public class UsersPermissionsDTO {

	private String orgCode;
	
	private String[] userIds;
	
	private String[] permissionNames;

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String[] getUserIds() {
		return userIds;
	}

	public void setUserIds(String[] userIds) {
		this.userIds = userIds;
	}

	public String[] getPermissionNames() {
		return permissionNames;
	}

	public void setPermissionNames(String[] permissionNames) {
		this.permissionNames = permissionNames;
	}

	public UsersPermissionsDTO() {
		// TODO Auto-generated constructor stub
	}
}
