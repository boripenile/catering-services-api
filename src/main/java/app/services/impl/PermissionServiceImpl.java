package app.services.impl;

import org.javalite.activejdbc.LazyList;

import app.models.Permission;
import app.models.Role;
import app.services.PermissionService;

public class PermissionServiceImpl implements PermissionService {

	@Override
	public LazyList<?> findAll() throws Exception {
		try {
			LazyList<Permission> perms = Permission.findAll();
			int length = perms.size();
			if (length == 0) {
				throw new Exception("No permissions found");
			}
			return perms;
		} finally {
		}
	}

	@Override
	public Permission findById(String id) throws Exception {
		try {
			Permission perm = Permission.findById(id);
			if (perm != null) {
				return perm;
			}
			throw new Exception("No role found with id: " + id);
		} finally {
		}
	}

	@Override
	public Permission update(Permission model) throws Exception {
		try {
			if (!model.save()) {
				return model;
			}
			return model;
		} catch(Exception e) {
			throw new Exception(e);
		} finally { 
		}
	}

	@Override
	public Permission create(Permission model) throws Exception {
		try {
			if (!model.save()) {
				return model;
			}
			return model;
		} catch(Exception e) {
			throw new Exception(e);
		} finally { 
		}
	}

	@Override
	public boolean delete(String id) throws Exception {
		try {
			Permission perm = Permission.findById(id);
			return perm != null ? true : false;
		} finally {
		}
	}

	@Override
	public int count() throws Exception {
		try {
			int count = Permission.count().intValue();
			if (count != 0) {
				throw new Exception("No roles found to count");
			}
			return count;
		} finally {
		}
	}

	@Override
	public boolean exist(String id) throws Exception {
		try {
			Permission perm = Permission.findById(id);
			return perm != null ? true : false;
		} finally {
		}
	}
	
	@Override
	public LazyList<Permission> findPermissionsNotSuper() throws Exception {
		try {
			String permissionList = "roles:list,permission:list,roles:updateRole,roles:createRole,roles:deleteRole,"
					+ "permissions:updatePermission,permissions:createPermission,permissions:deleteRole,users:list";
			LazyList<Permission> permissions = Permission.findBySQL("select permissions.* FROM permissions "
					+ "WHERE permissions.permission_name NOT IN ('roles:list','permission:list','roles:updateRole','roles:createRole','roles:deleteRole'," 
					+ "'permissions:updatePermission','permissions:createPermission','permissions:deleteRole','users:list')");
			int length = permissions.size();
			if (length == 0) {
				throw new Exception("No roles found");
			}
			return permissions;
		} finally {
		}
	}

}
