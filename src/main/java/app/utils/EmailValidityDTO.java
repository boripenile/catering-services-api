package app.utils;

public class EmailValidityDTO {

	private boolean isValid;
	public boolean isValid() {
		return isValid;
	}
	public void isValid(boolean valid) {
		this.isValid = valid;
	}
	@Override
	public String toString() {
		return "EmailValidityDTO [valid=" + isValid + "]";
	}
	
}
