package controllers;

import be.objectify.deadbolt.actions.Restrict;
import models.User;
import play.mvc.Controller;
import play.mvc.Result;
import be.objectify.deadbolt.actions.Restrict;

public class Users extends Controller{

	@Restrict(Application.ADMIN_ROLE)
	public static Result getAllUsers() {
		//com.feth.play.module.pa.controllers.Authenticate.noCache(response());
		final User user = Application.getLocalUser(session());
		return ok(views.html.user.render(User.all()));
	}

}
