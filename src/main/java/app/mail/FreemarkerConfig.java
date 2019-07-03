package app.mail;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import app.utils.CommonUtil;
import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;

public class FreemarkerConfig {
	static Properties properties = null;

    static {
    	properties = CommonUtil.loadPropertySettings("mail");
	}
	private Configuration configuration;
	
	public FreemarkerConfig() {
		configuration = new Configuration(Configuration.VERSION_2_3_23);
        try {
			String templateLocation = properties.getProperty("template_location");
        	configuration.setDirectoryForTemplateLoading( new File(templateLocation));
		} catch (IOException e) {
			e.printStackTrace();
		}
        configuration.setDefaultEncoding("UTF-8");
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);       
	}
	
	public Configuration getConfiguration() {
		return this.configuration;
	}

}
