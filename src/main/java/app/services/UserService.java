package app.services;

import app.dto.RegistrationDTO;
import app.dto.UsersOrganisationDTO;

import org.javalite.activejdbc.LazyList;

import app.dto.LoggedUserDTO;
import app.dto.PasswordParams;
import app.models.User;

public interface UserService extends ModelService<User>{

	 public String registerUser(RegistrationDTO registration) throws Exception;
	 
	 public String verifyPhoneNumber(String verificationCode) throws Exception;
	 
	 public LoggedUserDTO verifyEmailAddress(String verificationCode, String appCode) throws Exception;
	 
	 public void requestPasswordChangeUsingPhoneNumber(String phoneNumber) throws Exception;
	 
	 public String requestPasswordChangeUsingEmailAddress(String emailAddress) throws Exception;
	 
	 public void resetPassword(PasswordParams passwordParams) throws Exception;
	 
	 public LazyList<User> getUsersByOrganisationCode(String organisationCode) throws Exception;
	 
	 public boolean validateReferralCode(String referralCode) throws Exception;
	 
	 public User getUserByEmailOrUsername(String uniqueParameter) throws Exception;
	 
	 public String[] addUsersToOrganisation(UsersOrganisationDTO usersOrganisation) throws Exception;
	 
	 public String[] removeUsersFromOrganisation(UsersOrganisationDTO usersOrganisation) throws Exception;
	 
}
