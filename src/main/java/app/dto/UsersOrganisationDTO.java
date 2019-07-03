package app.dto;

public class UsersOrganisationDTO {

	private String orgCode;
	
	private String[] userIds;

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
	
	public UsersOrganisationDTO() {
		// TODO Auto-generated constructor stub
	}
}
