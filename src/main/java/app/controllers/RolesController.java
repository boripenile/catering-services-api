package app.controllers;

import org.javalite.activejdbc.LazyList;
import org.javalite.activeweb.AppController;
import org.javalite.activeweb.annotations.OPTIONS;
import org.javalite.activeweb.annotations.RESTful;
import org.javalite.common.Collections;
import org.javalite.common.JsonHelper;
import com.google.gson.Gson;
import com.google.inject.Inject;

import app.dto.LoggedUserDTO;
import app.dto.PermissionsDTO;
import app.dto.RoleDTO;
import app.dto.RolesPermissionsDTO;
import app.dto.UserLogin;
import app.dto.UsersPermissionsDTO;
import app.dto.UsersRolesDTO;
import app.models.Application;
import app.models.Permission;
import app.models.Role;
import app.models.User;
import app.services.AppService;
import app.services.RoleService;

@RESTful
public class RolesController extends AppController {

	@Inject
	private RoleService roleService;

	@Inject
	private AppService appService;

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
			case "updateRole":
				updateRole();
				break;
			case "createRole":
				createRole();
				break;
			case "deleteRole":
				deleteRole();
				break;
			case "addUsersToRoles":
				addUsersToRoles();
				break;
			case "addPermissionsToRoles":
				addPermissionsToRoles();
				break;
			case "addPermissionsToUsers":
				addPermissionsToUsers();
				break;
			case "removeUsersFromRoles":
				removeUsersFromRoles();
				break;
			case "removeRolesFromUsers":
				removeRolesFromUsers();
				break;
			case "removePermissionsFromUsers":
				removePermissionsFromUsers();
				break;
			case "removePermissionsFromRoles":
				removePermissionsFromRoles();
				break;
			case "updateUsersRoles":
				updateUsersRoles();
				break;
			case "updateUsersPermissions":
				updateUsersPermissions();
				break;
			case "updateRolesPermissions":
				updateRolesPermissions();
				break;
			case "findUserPermissionsByUserId":
				findUserPermissionsByUserId();
				break;
			case "findUserRolesByUserId":
				findUserRolesByUserId();
				break;
			case "findUsersByRoleName":
				findUsersByRoleName();
				break;
			case "findUsersByRoleId":
				findUsersByRoleId();
			case "findPermissionsByRoleId":
				findPermissionsByRoleId();
				break;
			case "findRoleNotSuper":
				findRoleNotSuper();
				break;
			default:
				break;
			}
		}
	}

	public void findAll() {
		try {
			LazyList<?> roles = roleService.findAll();
			if (roles != null) {
				view("code", 200, "data", roles.toJson(true));
				render("message");
			} else {
				view("code", 400, "message", "No roles");
				render("error");
			}
		} catch (Exception e) {
			logError(e.toString(), e);
			view("code", 400, "message", e.getMessage() != null ? e.getMessage() : "Error occured");
			render("error");
		}
	}

	public void findRoleNotSuper() {
		try {
			LazyList<?> roles = roleService.findRolesNotSuper();
			if (roles != null) {
				view("code", 200, "data", roles.toJson(true));
				render("message");
			} else {
				view("code", 400, "message", "No roles");
				render("error");
			}
		} catch (Exception e) {
			logError(e.toString(), e);
			view("code", 400, "message", e.getMessage() != null ? e.getMessage() : "Error occured");
			render("error");
		}
	}
	
	public void view() {
		try {
			if (header("id") != null) {
				String id = header("id");
				Role role = roleService.findById(id);
				if (role != null) {
					render("message", Collections.map("code", 200, "data", role.toJson(true)));
				} else {
					render("error", Collections.map("code", 400, "message", "No role found with id: " + id));
				}
			} else {
				render("error", Collections.map("code", 400, "message", "id is required as a header parameter"));
			}
		} catch (Exception e) {
			logError(e.toString(), e);
			render("error",
					Collections.map("code", 400, "message", e.getMessage() != null ? e.getMessage() : "Error occured"));
		}
	}

	public void updateRole() {
		try {
			if (header("id") != null) {
				String id = header("id");
				Role roleExisted = roleService.findById(id);
				if (roleExisted != null) {
					String payload = getRequestString();
					Role updatedRole = roleExisted.fromMap(JsonHelper.toMap(payload));
					updatedRole = roleService.update(updatedRole);
					render("message", Collections.map("code", 200, "data", updatedRole.toJson(true)));
					view("code", 200, "code", 200, "data", updatedRole.toJson(true));
					render("message");
				} else {
					view("code", 400, "message", "No role exist with id: " + id);
					render("error");
				}
			} else {
				view("code", 400, "message", "id is required as a header parameter");
				render("error");
			}

		} catch (Exception e) {
			logError(e.toString(), e);
			view("code", 400, "message", e.getMessage() != null ? e.getMessage() : "Error occured");
			render("error");
		}
	}

	public void createRole() {
		try {
			if (header("app_code") == null) {
				render("error", Collections.map("code", 400, "message", "app_code is required as a parameter"));
			} else {
				String appCode = header("app_code");
				Application app = appService.findByAppCode(appCode);
				String payload = getRequestString();
				Role role = new Role();
				Role newRole = role.fromMap(JsonHelper.toMap(payload));
				newRole.setParent(app);
				newRole = roleService.create(newRole);
				if (newRole.getId() != null) {
					view("code", 200, "data", newRole.toJson(true));
					render("message");
				} else {
					view("code", 400, "message", "Unable to create new role");
					render("error");
				}
			}
		} catch (Exception e) {
			logError(e.toString(), e);
			view("code", 400, "message", e.getMessage() != null ? e.getMessage() : "Error occured");
			render("error");
		}
	}

	public void deleteRole() {
		try {
			if (header("id") != null) {
				String id = header("id");
				boolean result = roleService.delete(id);
				if (result) {
					view("code", 200, "message", "Role with " + id + " deleted successfully.");
					render("error");
				} else {
					view("code", 400, "message", "No role found with id: " + id);
					render("error");
				}
			} else {
				view("code", 400, "message", "id is required as a header parameter");
				render("error");
			}
		} catch (Exception e) {
			logError(e.toString(), e);
			view("code", 400, "message", e.getMessage() != null ? e.getMessage() : "Error occured");
			render("error");
		}
	}

	public void addUsersToRoles() {
		try {
			String payload = getRequestString();
			UsersRolesDTO usersRoles = new Gson().fromJson(payload, UsersRolesDTO.class);
			String[] result = roleService.addUsersToRoles(usersRoles);
			if (result != null) {
				view("code", 200, "data", new Gson().toJson(result));
				render("message");
			}
		} catch (Exception e) {
			logError(e.toString(), e);
			view("code", 400, "message", e.getMessage() != null ? e.getMessage() : "Error occured");
			render("error");
		}
	}

	public void removeUsersFromRoles() {
		try {
			String payload = getRequestString();
			UsersRolesDTO usersRoles = new Gson().fromJson(payload, UsersRolesDTO.class);
			String[] result = roleService.removeUsersFromRoles(usersRoles);
			System.out.println("I am here...");
			if (result != null) {
				view("code", 200, "data", new Gson().toJson(result));
				render("message");
			}
		} catch (Exception e) {
			logError(e.toString(), e);
			view("code", 400, "message", e.getMessage() != null ? e.getMessage() : "Error occured");
			render("error");
		}
	}

	public void removeRolesFromUsers() {
		try {
			String payload = getRequestString();
			UsersRolesDTO usersRoles = new Gson().fromJson(payload, UsersRolesDTO.class);
			String[] result = roleService.removeRolesFromUsers(usersRoles);
			if (result != null) {
				view("code", 200, "data", new Gson().toJson(result));
				render("message");
			}
		} catch (Exception e) {
			logError(e.toString(), e);
			view("code", 400, "message", e.getMessage() != null ? e.getMessage() : "Error occured");
			render("error");
		}
	}
	
	public void removePermissionsFromUsers() {
		try {
			String payload = getRequestString();
			UsersPermissionsDTO usersPerms = new Gson().fromJson(payload, UsersPermissionsDTO.class);
			String[] result = roleService.removePermissionsFromUsers(usersPerms);
			if (result != null) {
				view("code", 200, "data", new Gson().toJson(result));
				render("message");
			}
		} catch (Exception e) {
			logError(e.toString(), e);
			view("code", 400, "message", e.getMessage() != null ? e.getMessage() : "Error occured");
			render("error");
		}
	}

	public void addPermissionsToUsers() {
		try {
			String payload = getRequestString();
			UsersPermissionsDTO usersPerms = new Gson().fromJson(payload, UsersPermissionsDTO.class);
			String[] result = roleService.addPermissionsToUsers(usersPerms);
			if (result != null) {
				view("code", 200, "data", new Gson().toJson(result));
				render("message");
			}
		} catch (Exception e) {
			logError(e.toString(), e);
			view("code", 400, "message", e.getMessage() != null ? e.getMessage() : "Error occured");
			render("error");
		}
	}
	
	public void addPermissionsToRoles() {
		try {
			String payload = getRequestString();
			RolesPermissionsDTO rolesPerms = new Gson().fromJson(payload, RolesPermissionsDTO.class);
			String[] result = roleService.addPermissionsToRoles(rolesPerms);
			if (result != null) {
				view("code", 200, "data", new Gson().toJson(result));
				render("message");
			}
		} catch (Exception e) {
			logError(e.toString(), e);
			view("code", 400, "message", e.getMessage() != null ? e.getMessage() : "Error occured");
			render("error");
		}
	}

	public void removePermissionsFromRoles() {
		try {
			String payload = getRequestString();
			RolesPermissionsDTO rolesPerms = new Gson().fromJson(payload, RolesPermissionsDTO.class);
			String[] result = roleService.removePermissionsFromRoles(rolesPerms);
			if (result != null) {
				view("code", 200, "data", new Gson().toJson(result));
				render("message");
			}
		} catch (Exception e) {
			logError(e.toString(), e);
			view("code", 400, "message", e.getMessage() != null ? e.getMessage() : "Error occured");
			render("error");
		}
	}

	public void findUsersByRoleName() {
		if (header("role_name") != null && header("org_code") != null) {
			try {
				String roleName = header("role_name");
				String orgCode = header("org_code");
				LazyList<User> result = roleService.findUsersByRoleName(roleName, orgCode);
				if (result != null) {
					view("code", 200, "data", result.toJson(true));
					render("message");
				} else {
					view("code", 400, "message", "No users found");
					render("error");
				}
			} catch (Exception e) {
				logError(e.toString(), e);
				view("code", 400, "message", e.getMessage() != null ? e.getMessage() : "Error occured");
				render("error");
			}
		} else {
			view("code", 400, "message", "role_name and org_code are required as header parameters");
			render("error");
		}
	}

	public void findUsersByRoleId() {
		if (header("role_id") != null && header("org_code") != null) {
			try {
				String roleId = header("role_id");
				String orgCode = header("org_code");
				LazyList<User> result = roleService.findUsersByRoleId(roleId, orgCode);
				if (result != null) {
					view("code", 200, "data", result.toJson(true));
					render("message");
				} else {
					view("code", 400, "message", "No users found");
					render("error");
				}
			} catch (Exception e) {
				logError(e.toString(), e);
				view("code", 400, "message", e.getMessage() != null ? e.getMessage() : "Error occured");
				render("error");
			}
		} else {
			view("code", 400, "message", "role_id and org_code are required as header parameters");
			render("error");
		}
	}

	public void findPermissionsByRoleId() {
		if (header("role_id") != null) {
			try {
				String roleId = header("role_id");
				LazyList<Permission> result = roleService.findPermissionsByRoleId(roleId);
				if (result != null) {
					view("code", 200, "data", result.toJson(true));
					render("message");
				} else {
					view("code", 400, "message", "No permissions found");
					render("error");
				}
			} catch (Exception e) {
				logError(e.toString(), e);
				view("code", 400, "message", e.getMessage() != null ? e.getMessage() : "Error occured");
				render("error");
			}
		} else {
			view("code", 400, "message", "role_id is required as a header parameter");
			render("error");
		}
	}

	public void updateUsersRoles() {
		if (header("active") != null && (header("active").equals("true") || header("active").equals("false"))) {
			try {
				String payload = getRequestString();
				UsersRolesDTO userRoles = new Gson().fromJson(payload, UsersRolesDTO.class);
				String[] result = roleService.updateUsersRoles(userRoles, Boolean.parseBoolean(header("active").toString()));
				if (result != null) {
					view("code", 200, "data", new Gson().toJson(result));
					render("message");
				}
			} catch (Exception e) {
				logError(e.toString(), e);
				view("code", 400, "message", e.getMessage() != null ? e.getMessage() : "Error occured");
				render("error");
			}
		} else {
			view("code", 400, "message", "active is required as a header parameter and set to true or false");
			render("error");
		}
	}

	public void updateUsersPermissions() {
		if (header("active") != null && (header("active").equals("true") || header("active").equals("false"))) {
			try {
				String payload = getRequestString();
				UsersPermissionsDTO userPerms = new Gson().fromJson(payload, UsersPermissionsDTO.class);
				String[] result = roleService.updateUsersPermissions(userPerms, Boolean.parseBoolean(header("active").toString()));
				if (result != null) {
					view("code", 200, "data", new Gson().toJson(result));
					render("message");
				}
			} catch (Exception e) {
				logError(e.toString(), e);
				view("code", 400, "message", e.getMessage() != null ? e.getMessage() : "Error occured");
				render("error");
			}
		} else {
			view("code", 400, "message", "active is required as a header parameter and set to true or false");
			render("error");
		}
	}

	public void updateRolesPermissions() {
		if (header("active") != null && (header("active").equals("true") || header("active").equals("false"))) {
			try {
				String payload = getRequestString();
				RolesPermissionsDTO rolePerms = new Gson().fromJson(payload, RolesPermissionsDTO.class);
				String[] result = roleService.updateRolesPermissions(rolePerms, Boolean.parseBoolean(header("active").toString()));
				if (result != null) {
					view("code", 200, "data", new Gson().toJson(result));
					render("message");
				}
			} catch (Exception e) {
				logError(e.toString(), e);
				view("code", 400, "message", e.getMessage() != null ? e.getMessage() : "Error occured");
				render("error");
			}
		} else {
			view("code", 400, "message", "active is required as a header parameter and set to true or false");
			render("error");
		}
	}

	public void findUserPermissionsByUserId() {
		if (header("user_id") != null && header("org_code") != null) {
			try {
				String userId = header("user_id");
				String orgCode = header("org_code");
				PermissionsDTO result = roleService.findUserPermissionsByUserId(userId, orgCode);
				if (result != null) {
					view("code", 200, "data", new Gson().toJson(result));
					render("message");
				} else {
					view("code", 400, "message", "No roles found");
					render("error");
				}
			} catch (Exception e) {
				logError(e.toString(), e);
				view("code", 400, "message", e.getMessage() != null ? e.getMessage() : "Error occured");
				render("error");
			}
		} else {
			view("code", 400, "message", "user_id and org_code are required as header parameters");
			render("error");
		}
	}

	public void findUserRolesByUserId() {
		if (header("user_id") != null && header("org_code") != null) {
			try {
				String userId = header("user_id");
				String orgCode = header("org_code");
				RoleDTO[] result = roleService.findUserRolesByUserId(userId, orgCode);
				if (result != null) {
					view("code", 200, "data", new Gson().toJson(result));
					render("message");
				} else {
					view("code", 400, "message", "No roles found");
					render("error");
				}
			} catch (Exception e) {
				logError(e.toString(), e);
				view("code", 400, "message", e.getMessage() != null ? e.getMessage() : "Error occured");
				render("error");
			}
		} else {
			view("code", 400, "message", "user_id and org_code are required as header parameters");
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
