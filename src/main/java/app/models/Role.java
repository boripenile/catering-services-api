package app.models;

import org.javalite.activejdbc.Model;

public class Role extends Model{

	static {
		validatePresenceOf("role_name").message("role.role_name.required");
		validatePresenceOf("description").message("role.description.required");
	}
}
