package app.services;

import org.javalite.activejdbc.LazyList;

import app.dto.PermissionsDTO;
import app.dto.RoleDTO;
import app.dto.RolesPermissionsDTO;
import app.dto.UsersPermissionsDTO;
import app.dto.UsersRolesDTO;
import app.models.Permission;
import app.models.Role;
import app.models.User;

public interface RoleService extends ModelService<Role> {
		
	public String[] addUsersToRoles(UsersRolesDTO usersRoles) throws Exception;
	
	public String[] removeUsersFromRoles(UsersRolesDTO usersRoles) throws Exception;
	
	public String[] removeRolesFromUsers(UsersRolesDTO usersRoles) throws Exception;
	
	public String[] addPermissionsToRoles(RolesPermissionsDTO rolesPerms) throws Exception;
	
	public String[] addPermissionsToUsers(UsersPermissionsDTO usersPerms) throws Exception;
	
	public String[] removePermissionsFromRoles(RolesPermissionsDTO rolesPerms) throws Exception;
	
	public String[] removePermissionsFromUsers(UsersPermissionsDTO usersPerms) throws Exception;
	
	public LazyList<User> findUsersByRoleName(String roleName, String orgCode) throws Exception;
	
	public LazyList<User> findUsersByRoleId(String roleId, String orgCode) throws Exception;
	
	public LazyList<Permission> findPermissionsByRoleId(String roleId) throws Exception;
	
	public String[] updateUsersRoles(UsersRolesDTO usersRoles, boolean active) throws Exception;
	
	public String[] updateUsersPermissions(UsersPermissionsDTO usersPermissions, boolean active) throws Exception;
	
	public String[] updateRolesPermissions(RolesPermissionsDTO rolesPerms, boolean active) throws Exception;
	
	public PermissionsDTO findUserPermissionsByUserId(String userId, String orgCode) throws Exception;
	
	public RoleDTO[] findUserRolesByUserId(String userId, String orgCode) throws Exception;
	
	public LazyList<Role> findRolesNotSuper() throws Exception;
	
	public void copyRolePermissions(String sourceRole, String dstinationOrganisationCode) throws Exception;
	
}
