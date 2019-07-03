package app.dto;

import java.util.Arrays;

public class UsersRolesDTO {

	private String orgCode;
	
	private String[] userIds;
	
	private String[] roleNames;

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

	public String[] getRoleNames() {
		return roleNames;
	}


	public void setRoleNames(String[] roleNames) {
		this.roleNames = roleNames;
	}


	public UsersRolesDTO() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		final int maxLen = 10;
		return "UsersRolesDTO [orgCode=" + orgCode + ", userIds="
				+ (userIds != null ? Arrays.asList(userIds).subList(0, Math.min(userIds.length, maxLen)) : null)
				+ ", roleNames="
				+ (roleNames != null ? Arrays.asList(roleNames).subList(0, Math.min(roleNames.length, maxLen)) : null)
				+ "]";
	}
	
	
}
