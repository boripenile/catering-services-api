package app.utils;

import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import javax.mail.MessagingException;

import org.apache.commons.codec.binary.Base64;
import org.javalite.http.Get;
import org.javalite.http.Http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

// import org.bouncycastle.util.encoders.Base64;

import app.dto.RegistrationDTO;
import app.mail.EmailService;
import app.mail.Mail;
import app.models.Application;
import app.sms.Sms;
import app.sms.SmsService;
import freemarker.template.TemplateException;

public class Utils {
	
	static Properties mailProps = null;
	static Properties emailValidatorProp = null;

	static {
		mailProps = CommonUtil.loadPropertySettings("mail");
		emailValidatorProp = CommonUtil.loadPropertySettings("emailvalidator");
	}
	
	public static String genWalletID () {
		Random random = new Random();
		String nextId = new BigInteger(50, random).toString(15);
		
		String extractDigits = "";
		
 		while (extractDigits.isEmpty() && nextId.length() >= 11) {
			extractDigits = nextId.substring(0, 10);
			
			if (extractDigits.length() == 10) break;
			
			nextId = new BigInteger(50, random).toString(15);
			extractDigits = "";
			
		}
		return extractDigits;
	}
	
	public static String genVerificationCode () {
		Random random = new Random();
		String nextId = new BigInteger(50, random).toString(10);
		
		String extractDigits = "";
		
 		while (extractDigits.isEmpty() && nextId.length() >= 7) {
			extractDigits = nextId.substring(0, 6);
			
			if (extractDigits.length() == 6) break;
			
			nextId = new BigInteger(50, random).toString(10);
			extractDigits = "";
			
		}
		return extractDigits;
	}
	
	public static String genOrganisationCode () {
		Random random = new Random();
		String nextId = new BigInteger(50, random).toString(10);
		
		String extractDigits = "";
		
 		while (extractDigits.isEmpty() && nextId.length() >= 7) {
			extractDigits = nextId.substring(0, 6);
			
			if (extractDigits.length() == 6) break;
			
			nextId = new BigInteger(50, random).toString(10);
			extractDigits = "";
			
		}
		return extractDigits;
	}
	
	public static String genReferralCode () {
		Random random = new Random();
		String nextId = new BigInteger(50, random).toString(15);
		
		String extractDigits = "";
		
 		while (extractDigits.isEmpty() && nextId.length() >= 11) {
			extractDigits = nextId.substring(0, 10);
			
			if (extractDigits.length() == 10) break;
			
			nextId = new BigInteger(50, random).toString(15);
			extractDigits = "";
		}
		return extractDigits.toUpperCase();
	}
	
	public static String genTaskReference () {
		Random random = new Random();
		String nextId = new BigInteger(50, random).toString(12);
		
		String extractDigits = "";
		
 		while (extractDigits.isEmpty() && nextId.length() >= 9) {
			extractDigits = nextId.substring(0, 8);
			
			if (extractDigits.length() == 8) break;
			
			nextId = new BigInteger(50, random).toString(12);
			extractDigits = "";
			
		}
 		StringBuilder taskRef = new StringBuilder();
 		taskRef.append("TSK-");
 		taskRef.append(extractDigits.toUpperCase());
		return taskRef.toString();
	}
	
	public static boolean sendEmail(String salutation, String organisationName, String recipient, String mailTitle,
			String body, String code, String actionUrl, String mailTemplate) throws MessagingException, IOException, TemplateException {
		Mail mail = new Mail();
		mail.setTo(recipient);
		mail.setSubject(mailTitle);
		Map<String, Object> model = new HashMap<>();
		if (salutation != null) {
			model.put("salutation", salutation);
		}
		model.put("organisation_name", organisationName);
		model.put("mail_body", body);
		if (actionUrl != null) {
			model.put("action_url", mailProps.getProperty(actionUrl) + code);
		}
		mail.setModel(model);
		return EmailService.sendSimpleMessage(mail, mailTemplate);
	}
	
	public static void sendVerificationSMS(String[] numbers, 
			String verificationCode, Boolean isResetPassword, Boolean requestPassword, String appShortName) throws Exception{
		StringBuilder appender = new StringBuilder();
		appender.append(appShortName);
		Sms sms = new Sms();
		sms.setToNumbers(numbers);
		if (verificationCode != null) {
			if (isResetPassword) {
				sms.setSender(appender.append("-RESET").toString());
				sms.setMessage("You have request for a password reset " + "on " + appShortName + ".\nUse this PIN "
						+ verificationCode + " to reset your password.");
			} else {
				if (requestPassword) {
					sms.setSender(appender.append("-TOKEN").toString());
					sms.setMessage("Use this PIN "
							+ verificationCode + " to proceed " + "with your registration.");
				} else {
					sms.setSender(appender.append("-TOKEN").toString());
					sms.setMessage("You have shown interest to become a service provider " + "on "+ appShortName +".\nUse this PIN "
							+ verificationCode + " to proceed " + "with your registration.");
				}
			}
			
		} else {
			sms.setSender(appender.append("-RESET").toString());
			sms.setMessage("Your have reset your password successfully "
					+ "on "+ appShortName +".\nWelcome on board!");
		}
		System.out.println(sms.toString());
		SmsService.sendSMS(sms);
	}
	
	public static void sendResetPasswordSMS(String[] numbers, String appShortName) throws Exception{
		StringBuilder appender = new StringBuilder();
		appender.append(appShortName);
		Sms sms = new Sms();
		sms.setToNumbers(numbers);
		sms.setSender(appender.append("-RESET").toString());
		sms.setMessage("Your password has been reset successfully.");
		SmsService.sendSMS(sms);
	}
	
	public static void sendRegistrationCompleteSMS(String[] numbers, String appShortName) throws Exception{
		StringBuilder appender = new StringBuilder();
		appender.append(appShortName);
		Sms sms = new Sms();
		sms.setToNumbers(numbers);
		sms.setSender(appender.append("-REG").toString());
		sms.setMessage("Your registration has been completed successfully " + "on " + appShortName + ".\nWelcome on board!");
		SmsService.sendSMS(sms);
	}
	
	public static String[] getHashedPasswordAndSalt(String password) {
		String[] results = new String[2];
		IPasswords pass = new Passwords();
		
		byte[] salt = pass.getSalt32();
		byte[] hashedPassword = pass.hash(password, salt);
		
		results[0] = new Base64().encodeToString(hashedPassword);
		results[1] = new Base64().encodeToString(salt);
		
		return results;
	}
	
	public static Map<String, byte[]> getPasswordAndSaltInBytes(String password, String salt) {
		Map<String, byte[]> results = new HashMap<String, byte[]>();
		
		results.put("password", new Base64().decode(password));
		results.put("salt", new Base64().decode(salt));
		
		return results;
	}
	
	public static boolean isEmailValid(String emailAddress) {
		try {
			if (emailAddress != null || emailAddress != "") {
				System.out.println("Validating email address: " + emailAddress);
				StringBuilder url = new StringBuilder();
				url.append(emailValidatorProp.getProperty("url")).append(emailAddress);
				Get getValidity = Http.get(url.toString(), 30000, 30000)
						.header(emailValidatorProp.getProperty("host-name"), emailValidatorProp.getProperty("host-value"))
						.header(emailValidatorProp.getProperty("key-name"), emailValidatorProp.getProperty("key-value"));
				if (getValidity.responseCode() == 200) {
					String response = getValidity.text("UTF-8");
					Gson gson = new Gson();
					EmailValidityDTO valid = gson.fromJson(response, EmailValidityDTO.class);
					return valid.isValid();
				} else {
					return false;
				} 
			}
		} catch (Exception e) {
		}
		return false;
	}
	public static void main(String[] args) {
		try {
			sendVerificationSMS(new String[] {"08069566914"}, "890987", false, false, "APP");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
