package app.config;

import org.javalite.activeweb.AbstractDBConfig;
import org.javalite.activeweb.AppContext;
import static org.javalite.app_config.AppConfig.p;

import org.javalite.activejdbc.Base;

public class DbConfig extends AbstractDBConfig {

    public void init(AppContext context) {
    	configFile(p("config_location") + "/" + "database.properties");
    	//environment("development");
    }
    
}
