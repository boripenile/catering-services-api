package app.models;

import org.javalite.activejdbc.Model;

public class Permission extends Model{
	static {
		validatePresenceOf("permission_name").message("permission.permission_name.required");
		validatePresenceOf("description").message("permission.description.required");;
	}
}
