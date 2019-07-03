package app.filters;

import org.javalite.activeweb.controller_filters.HttpSupportFilter;
import org.javalite.common.Collections;

public class CorsFilter extends HttpSupportFilter {

	private static final String headerAllowHeaders = "Access-Control-Allow-Headers";
	private static final String headerAllowOrigin = "Access-Control-Allow-Origin";
	private static final String headerRequestMethod = "Access-Control-Request-Method";
	private static final String wildcard = "*";
	private static final String headers = "app_code,id,user_id,role_id,role_name,action,org_code,search_parameter,Accept,Content-Type";
	
	@Override
	public void before() {
		if (getRoute().getMethod().toString().equalsIgnoreCase("OPTIONS")) {
			System.out.println("I am here... METHOD: " + getRoute().getMethod().toString());
			render("/layouts/error", Collections.map("code", 200, "message", "Successful"));
		}
		header(headerAllowHeaders, headers);
		header(headerAllowOrigin, wildcard);
		header(headerRequestMethod, "OPTIONS, PATCH, POST, PUT, DELETE, GET");
		header(headerAllowHeaders, wildcard);
	}
}
