package app.utils;

import static org.javalite.app_config.AppConfig.p;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;

public class FirebaseMessagingUtils {

	public static FirebaseApp getFirebaseApp() {
		String firebaseServiceAccountKeyPath = p("firebase_key_path");
		FileInputStream serviceAccount;
		try {
			serviceAccount = new FileInputStream(firebaseServiceAccountKeyPath);
			FirebaseOptions options;
			try {
				options = new FirebaseOptions.Builder()
				  .setCredentials(GoogleCredentials.fromStream(serviceAccount))
				  .setDatabaseUrl(p("firebase_account_url"))
				  .build();
				return FirebaseApp.initializeApp(options);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;		
	}
	
	public static ApiFuture<String> sendMessage(String receiverToken, 
			TopicEnum topic, String title, String body) {
		
		FirebaseMessaging firebaseMessage = 
				FirebaseMessaging.getInstance(getFirebaseApp());
		
		Notification notification = new Notification(title, body);
		Message message = Message.builder()
				.setNotification(notification)
				.setToken(receiverToken)
				.setTopic(topic.name())
				.build();
		
		return firebaseMessage.sendAsync(message);
	}
}
