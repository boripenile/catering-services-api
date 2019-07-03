package app.sms;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.Properties;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import app.utils.CommonUtil;

public class SmsService {
	static Properties properties = null;

    static {
    	properties = CommonUtil.loadPropertySettings("sms");
	}
	public static void sendSMS(Sms simpleSms) {
		try {
			@SuppressWarnings("resource")
			DefaultHttpClient client = new DefaultHttpClient();
	        for (String number : simpleSms.getToNumbers()) {
	            String strUri = properties.get("url_sms_provider") + "?" + 
	            		properties.get("username_param_name") + "=" + encodeParam(properties.get("username").toString()) 
	                    + "&" + properties.get("password_param_name") + "=" + properties.get("password") + 
	                    "&" + properties.get("message_param_name") + "=" + encodeParam(simpleSms.getMessage()) + "&" + 
	                    properties.get("receipients_param_name") + "="
	                    + encodeParam(number) + "&" + properties.get("sender_param_name") + "=" + encodeParam(simpleSms.getSender()) + "&flash=1";
	            URI uri = new URI(strUri);
	            HttpGet get = new HttpGet(uri);

	            get.addHeader("accept", "text/html");

	            HttpResponse response = client.execute(get);
	            if (response.getStatusLine().getStatusCode() != 200) {
	                throw new RuntimeException("Operation failed: " + response.getStatusLine().getStatusCode());
	            }

	            System.out.println("Content-Type: " + response.getEntity().getContentType().getValue());

	            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

	            String line = reader.readLine();
	            while (line != null) {
	                System.out.println(line);
	                line = reader.readLine();
	            }
	        }
	        client.getConnectionManager().shutdown();
		} catch (Exception e) {
			// TODO: handle exception
		}  
    }
    
    private static String encodeParam(String paramValue) throws UnsupportedEncodingException{
        return URLEncoder.encode(paramValue, "UTF-8");
    }
    
    public static void main(String[] args) {
    	System.out.println(properties.get("username"));
    }
}
