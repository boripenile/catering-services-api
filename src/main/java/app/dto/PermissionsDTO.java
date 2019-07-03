package app.dto;

public class PermissionsDTO {

	private BasicPermissionDTO[] basic;
	
	private SelfPermissionDTO[] self;

	public BasicPermissionDTO[] getBasic() {
		return basic;
	}

	public void setBasic(BasicPermissionDTO[] basic) {
		this.basic = basic;
	}

	public SelfPermissionDTO[] getSelf() {
		return self;
	}

	public void setSelf(SelfPermissionDTO[] self) {
		this.self = self;
	}
	
}
