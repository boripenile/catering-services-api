package app.controllers;

import org.javalite.activejdbc.LazyList;
import org.javalite.activeweb.AppController;
import org.javalite.activeweb.annotations.OPTIONS;
import org.javalite.activeweb.annotations.RESTful;

import com.google.inject.Inject;

import app.models.Permission;
import app.services.PermissionService;

@RESTful
public class PermissionsController extends AppController{

	@Inject
	private PermissionService permissionService;
	
	public void index() {
		if (header("action") != null) {
			String actionName = header("action").toString();
			switch (actionName) {
			case "view":
				view();
				break;
			default:
				break;
			}
		} else {
			findAll();
		}
	}
	
	public void create() {
		if (header("action") != null) {
			String actionName = header("action").toString();
			switch (actionName) {
			case "findPermissionNotSuper":
				findPermissionNotSuper();
				break;
			default:
				break;
				
			}
		}
	}
	public void findAll() {
		try {
			LazyList<?> permissions = permissionService.findAll();
			if (permissions != null) {
				view("code", 200, "data", permissions.toJson(true));
				render("message");
			} else {
				view("code", 400, "message", "No permissions");
				render("error");
			}
		} catch (Exception e) {
			logError(e.toString(), e);
			view("code", 400, "message", e.getMessage() != null ? e.getMessage() : "Error occured");
			render("error");
		}
	}

	public void findPermissionNotSuper() {
		try {
			LazyList<Permission> permissions = permissionService.findPermissionsNotSuper();
			if (permissions != null) {
				view("code", 200, "data", permissions.toJson(true, "id", "permission_name", "description"));
				render("message");
			} else {
				view("code", 400, "message", "No permissions");
				render("error");
			}
		} catch (Exception e) {
			logError(e.toString(), e);
			view("code", 400, "message", e.getMessage() != null ? e.getMessage() : "Error occured");
			render("error");
		}
	}
	@Override
	protected String getContentType() {
		return "application/json";
	}

	@Override
	protected String getLayout() {
		return null;
	}

	@OPTIONS
	public void options() {
		view("code", 200, "message", "Successful");
		render("error");
	}
}
