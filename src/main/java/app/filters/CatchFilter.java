package app.filters;

import org.javalite.activeweb.controller_filters.HttpSupportFilter;
import org.javalite.common.Collections;

public class CatchFilter extends HttpSupportFilter{

	@Override
	public void onException(Exception e) {
		System.out.println("Error has occured...");
		logError(e.toString(), e);
		if (e.getMessage() != null) {
			render("/system/error", Collections.map("code", 400, "message", e.getMessage()));
		} else {
			render("/system/error", Collections.map("code", 400, "message", "Something went wrong"));
		}
	}

}
