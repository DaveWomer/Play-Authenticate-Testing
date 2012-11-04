import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import models.SecurityRole;
import models.User;

import com.feth.play.module.pa.PlayAuthenticate;
import com.feth.play.module.pa.PlayAuthenticate.Resolver;
import com.feth.play.module.pa.exceptions.AccessDeniedException;
import com.feth.play.module.pa.exceptions.AuthException;
import com.feth.play.module.pa.user.AuthUser;

import controllers.routes;

import play.Application;
import play.GlobalSettings;
import play.mvc.Call;
import providers.MyUsernamePasswordAuthProvider.MySignup;
import providers.MyUsernamePasswordAuthUser;

public class Global extends GlobalSettings {

	public void onStart(Application app) {
		PlayAuthenticate.setResolver(new Resolver() {

			@Override
			public Call login() {
				// Your login page
				return routes.Application.login();
			}

			@Override
			public Call afterAuth() {
				// The user will be redirected to this page after authentication
				// if no original URL was saved
				return routes.Application.index();
			}

			@Override
			public Call afterLogout() {
				return routes.Application.index();
			}

			@Override
			public Call auth(final String provider) {
				// You can provide your own authentication implementation,
				// however the default should be sufficient for most cases
				return com.feth.play.module.pa.controllers.routes.Authenticate
						.authenticate(provider);
			}

			@Override
			public Call askMerge() {
				return routes.Account.askMerge();
			}

			@Override
			public Call askLink() {
				return routes.Account.askLink();
			}

			@Override
			public Call onException(final AuthException e) {
				if (e instanceof AccessDeniedException) {
					return routes.Signup
							.oAuthDenied(((AccessDeniedException) e)
									.getProviderKey());
				}

				// more custom problem handling here...
				return super.onException(e);
			}
		});

		initialData();
	}

	private void initialData() {
		if (SecurityRole.find.findRowCount() == 0) {
/*			for (final String roleName : Arrays
					.asList(controllers.Application.USER_ROLE)) {
				final SecurityRole role = new SecurityRole();
				role.roleName = roleName;
				role.save();
			}*/
			
			SecurityRole role = new SecurityRole();
			role.roleName = controllers.Application.ADMIN_ROLE;
			role.save();
			
			SecurityRole role2 = new SecurityRole();
			role2.roleName = controllers.Application.USER_ROLE;
			role2.save();

		}
		
		//if(User.find.findRowCount() == 0){
			final MySignup signUp = new MySignup();
			signUp.email = "dave.womer@cdw.com";
			signUp.name = "Dave Womer";
			signUp.isAdmin = true;
			signUp.password = "12345";
			signUp.repeatPassword = "12345";
			
			AuthUser auser = new MyUsernamePasswordAuthUser(signUp);
			
			User.create(auser, true, false);
		//}
		
		
	}
}