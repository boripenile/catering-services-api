package app.services;

import org.javalite.activejdbc.LazyList;

import app.models.Permission;

public interface PermissionService extends ModelService<Permission> {

	public LazyList<Permission> findPermissionsNotSuper() throws Exception;
}
