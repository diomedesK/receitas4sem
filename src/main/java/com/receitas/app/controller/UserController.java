package com.receitas.app.controller;

import java.util.Optional;
import java.time.Duration;

import io.javalin.http.Context;
import java.util.List;

import com.receitas.app.service.UserService;
import com.receitas.app.service.SessionData;
import com.receitas.app.service.ServiceAPIResponse;

import com.receitas.app.service.RecipeService;

import com.receitas.app.model.UserModel;
import com.receitas.app.model.RecipeModel;

public class UserController {

    private final UserService userService;
    private final RecipeService recipeService;

    public UserController(UserService userService, RecipeService recipeService) {
        this.userService = userService;
		this.recipeService = recipeService;
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
			context.render("accessPage.html");
		} else {
			context.redirect("/");
		}
	};

	public void signUpPageHandler(  Context context  ){
		if (context.attribute("userData") == null){
			context.render("signupPage.html");
		} else {
			context.redirect("/");
	};
	}

	public void logoutHandler( Context context ){
		userService.removeUserSession(context.cookie("session-token"));
		context.removeCookie("session-token");
		context.redirect("/");
	}

	public void getProfilePage( Context context ){
		if (context.attribute("userData") != null){
			context.render("userProfilePage.html");
		} else {
			context.redirect("/");
		}
	}

	public void getFavoritesPage( Context context ){
		if (context.attribute("userData") != null){
			context.render("userFavoritesPage.html");
		} else {
			context.redirect("/");
		}
	}

	public void getUserRecipesPage( Context context ){
		if (context.attribute("userData") != null){
			UserModel user = context.attribute("userData");
			List<RecipeModel> userRecipes = recipeService.getRecipesByAuthorID(user.getID());
			System.out.println(userRecipes);
			context.attribute("userRecipes", userRecipes );
			context.render("userRecipesPage.html");
		} else {
			context.redirect("/");
		}
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
