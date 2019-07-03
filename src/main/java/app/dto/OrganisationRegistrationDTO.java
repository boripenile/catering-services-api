package app.dto;

public class OrganisationRegistrationDTO {
	
	private String referralCode;
	
	private String organisationName;
	
	private String workingDescription;
	
	private String motto;
	
	private ExtraData image;

	public String getReferralCode() {
		return referralCode;
	}

	public void setReferralCode(String referralCode) {
		this.referralCode = referralCode;
	}

	public String getOrganisationName() {
		return organisationName;
	}

	public void setOrganisationName(String organisationName) {
		this.organisationName = organisationName;
	}

	public String getWorkingDescription() {
		return workingDescription;
	}

	public void setWorkingDescription(String workingDescription) {
		this.workingDescription = workingDescription;
	}

	public String getMotto() {
		return motto;
	}

	public void setMotto(String motto) {
		this.motto = motto;
	}

	public ExtraData getImage() {
		return image;
	}

	public void setImage(ExtraData image) {
		this.image = image;
	}
	
	
}
