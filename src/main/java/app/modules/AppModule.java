package app.modules;

import com.google.inject.AbstractModule;

import app.services.AppService;
import app.services.AuthService;
import app.services.PermissionService;
import app.services.RoleService;
import app.services.UserService;
import app.services.impl.AppServiceImpl;
import app.services.impl.AuthServiceImpl;
import app.services.impl.PermissionServiceImpl;
import app.services.impl.RoleServiceImpl;
import app.services.impl.UserServiceImpl;

public class AppModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(AuthService.class).to(AuthServiceImpl.class).asEagerSingleton();
		bind(RoleService.class).to(RoleServiceImpl.class).asEagerSingleton();
		bind(PermissionService.class).to(PermissionServiceImpl.class).asEagerSingleton();
		bind(AppService.class).to(AppServiceImpl.class).asEagerSingleton();
		bind(UserService.class).to(UserServiceImpl.class).asEagerSingleton();
	}

}
