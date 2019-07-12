package app.config;

import org.javalite.activeweb.AbstractControllerConfig;
import org.javalite.activeweb.AppContext;
import org.javalite.activeweb.controller_filters.DBConnectionFilter;
import org.javalite.activeweb.controller_filters.TimingFilter;

import app.controllers.LoginController;
import app.controllers.PermissionsController;
import app.controllers.RolesController;
import app.controllers.TokenController;
import app.controllers.UsersController;
import app.filters.AuthorizationFilter;
import app.filters.CatchFilter;
import app.filters.CorsFilter;

@SuppressWarnings("rawtypes")
public class AppControllerConfig extends AbstractControllerConfig {

    @SuppressWarnings("unchecked")
	public void init(AppContext context) {
    	add(new CorsFilter());
    	add(new CatchFilter());
    	add(new AuthorizationFilter()).exceptFor(LoginController.class);
    	add(new TimingFilter(), new DBConnectionFilter()).to(LoginController.class, 
    			RolesController.class, UsersController.class, PermissionsController.class);
    }
}
