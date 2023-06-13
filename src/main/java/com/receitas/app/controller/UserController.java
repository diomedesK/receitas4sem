package com.receitas.app.controller;

import java.util.Optional;
import java.time.Duration;

import io.javalin.http.Context;

import com.receitas.app.service.UserService;
import com.receitas.app.service.SessionData;
import com.receitas.app.service.ServiceAPIResponse;

import com.receitas.app.model.UserModel;

public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;;
    }

	public void signUpUser( Context context ){
        ServiceAPIResponse r = userService.registerNewUserFromJSON( context.body()   );
		context.status( r.status ).json( r );
	}

	public void loginUser( Context context ){
		Optional<SessionData> sessionData = userService.authenticateUserFromJSON( context.body() );
		if (sessionData.isPresent()){
			// expires in 1 hour
			context.status(200).cookie("session-token", sessionData.get().getSessionToken(), (int) Duration.ofHours(1).toSeconds() ).json( sessionData.get().getUser() );
		} else {
			context.status(403);
		}
	}

	public void getUserDataFromSessionToken( Context context ){
		context.json( context.attributeMap().get("userData") );
	}

	public void accessPageHandler( Context context ){
		// logged users shouldnt access
		if (context.attribute("userData") == null){
			context.render("access.html");
		} else {
			context.redirect("/");
		}
	};

	public void signUpPageHandler(  Context context  ){
		if (context.attribute("userData") == null){
			context.render("signup.html");
		} else {
			context.redirect("/");
	};
	}

	public void logoutHandler( Context context ){
		userService.removeUserSession(context.cookie("session-token"));
		context.removeCookie("session-token");
		context.redirect("/");
	}


	public void getFavoritesPage( Context context ){
		context.render("user_favorites.html");
	}

	public void saveFavoriteRecipe( Context context ){
        String recipeID = context.pathParam("recipe_id");
		UserModel user = context.attribute("userData");
		if ( user != null ){
			ServiceAPIResponse res = userService.saveRecipeAsFavorite(recipeID, user.getID());
			context.status(res.status);
		} else {
			context.status(403);
		}

	}

	public void deleteFavoriteRecipe( Context context ){
        String recipeID = context.pathParam("recipe_id");
		UserModel user = context.attribute("userData");
		if ( user != null ){
			ServiceAPIResponse res = userService.removeRecipeFromFavorites(recipeID, user.getID());
			context.status(res.status);
		} else {
			context.status(403);
		}

	}

	
}
