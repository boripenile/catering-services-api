package app.controllers;

import org.javalite.activeweb.controller_filters.HttpSupportFilter;

import app.controllers.authorization.Protected;

/**
 * @author Igor Polevoy on 9/29/14.
 */
public class AuthorizationFilter extends HttpSupportFilter {
    @Override
    public void before() {

        if(!controllerProtected()){
            return;// allow to fall to controller
        }

        if(!sessionHas("user") && controllerProtected()){
            redirect(LoginController.class);
        }
    }

    private boolean controllerProtected() {
        return getRoute().getController().getClass().getAnnotation(Protected.class) != null;
    }
}