package app.mail;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.internet.MimeMessage;
//
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import app.utils.CommonUtil;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class EmailService {
	static Properties properties = null;

    static {
    	properties = CommonUtil.loadPropertySettings("mail");
	}
 
    public class SMTPAuthenticator extends javax.mail.Authenticator {
        public PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(properties.getProperty("sender_email_id"), 
            		properties.getProperty("sender_password"));
        }
    }
//    
    public static boolean sendSimpleMessage(Mail mail, String templateName) throws MessagingException, IOException, TemplateException {
    	try {
    		JavaMailSenderImpl emailSender = new JavaMailSenderImpl();
        	MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());

            emailSender.setHost(properties.getProperty("mail_host"));
            emailSender.setPort(Integer.parseInt(properties.getProperty("mail_host_port")));
             
            emailSender.setUsername(properties.getProperty("sender_email_id"));
            emailSender.setPassword(properties.getProperty("sender_password"));
            Properties props = emailSender.getJavaMailProperties();
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            FreemarkerConfig freemarker = new FreemarkerConfig();
            Template t = freemarker.getConfiguration().getTemplate(templateName);
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, mail.getModel());
            
            helper.setTo(mail.getTo());
            helper.setText(html, true);
            helper.setSubject(mail.getSubject());
            helper.setFrom(properties.getProperty("mail_from"), 
            		properties.getProperty("mail_personel"));

            emailSender.send(message);
            return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return false;
    }

}
