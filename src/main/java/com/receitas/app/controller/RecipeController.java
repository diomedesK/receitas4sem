package com.receitas.app.controller;

import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;

import java.util.List;
import java.util.Optional;

import com.receitas.app.service.RecipeService;
import com.receitas.app.service.UserService;
import com.receitas.app.service.ServiceAPIResponse;

import com.receitas.app.model.RecipeModel;
import com.receitas.app.model.UserModel;

import com.receitas.app.utils.MyLogger;

public class RecipeController {

	/*
	 * Reading, updating and deleting operations are enveloped in the
	 * ServiceAPIResponse, meanwhile reading operations are handled
	 * by the Javalin API.
	 */

    private final RecipeService recipeService;
	private final UserService userService;

    public RecipeController(RecipeService recipeService, UserService userService) {
        this.recipeService = recipeService;
		this.userService = userService;
    }

	public void getRecipePage(Context context) {
		String recipeID = context.pathParam("id");
		RecipeModel recipe = recipeService.getRecipeByID( recipeID ).get();

		if (recipe != null) {
			recipeService.clearAccessesOfRecipeFromDaysAgo(recipeID, 7);
			recipeService.addAccess(context.pathParam("id"));
			
			context.attribute("recipe", recipe);
			if (context.attribute("userData") != null){
				UserModel user = context.attribute("userData");
				boolean wasRecipeFavorited = userService.hasUserFavoritedRecipeFromSessionToken( context.cookie("session-token"), recipeID );
				context.attribute("wasRecipeFavorited", wasRecipeFavorited);

				Optional<Integer> userRating = recipeService.getRating(recipeID, user.getID() );
				if(userRating.isPresent()){
					context.attribute("userRating", userRating.get());
				}

			}

			context.render("recipe.html");

		} else {
			context.status(404);
		}

	};

	public void getNewRecipePage( Context context ){
		if (context.attribute("userData") != null){
			context.render("new_recipe.html");
		} else {
			context.redirect("/");
		}

	}

	public void getRecipes( Context context ){

		if( context.queryParamMap().containsKey("id") ){
			getRecipeByID(
					context,
					context.queryParam("id")
					);

		} else if ( context.queryParamMap().containsKey("authorID") ){
			getRecipesByAuthorID(
					context,
					context.queryParam("authorID")
					);

		}  else if ( context.queryParamMap().containsKey("ingrediente") ){
			getRecipesByIngredients(
					context,
					context.queryParam("ingrediente").split(":")
					);

		} else if ( context.queryParamMap().containsKey("categoria")){
			getRecipesByCategories(
					context, 
					context.queryParam("categoria").split(":")
					);

		} else if ( context.queryParamMap().containsKey("nome")){
			getRecipesByName(
					context,
					context.queryParam("nome")
					);
			
		} else {
			getManyRecipes(context);
		}

	}

    public void getManyRecipes(Context context) {
		// limit by 8 should be dynamic
        List<RecipeModel> recipes = recipeService.getManyRecipes(8);
        context.json(recipes);
    }

    public void getPopularRecipes(Context context) {
		recipeService.clearAccessesOfAllRecipesFromDaysAgo(7);
        List<RecipeModel> recipes = recipeService.getPopularRecipes();
        context.json(recipes);
    }

    public void getRecipeByID(Context context, String recipeID) {
		MyLogger.info("getByID");
        Optional<RecipeModel> recipe = recipeService.getRecipeByID(recipeID);

		if ( recipe.isPresent() ){
			context.json(recipe.get());
		} else {
			context.status(404);
		}

    }

    public void getRecipesByAuthorID(Context context, String authorID) {
		MyLogger.info("getByAuthorID");
        List<RecipeModel> recipes = recipeService.getRecipesByAuthorID( authorID );
        context.json(recipes);
    }

    public void getRecipesByIngredients(Context context, String... ingredients ) {
		MyLogger.info("getByIngredients");
        List<RecipeModel> recipes = recipeService.getRecipesByIngredients( ingredients );
        context.json(recipes);
    }

    public void getRecipesByCategories(Context context, String... categories) {
        List<RecipeModel> recipes = recipeService.getRecipesByCategories( categories );
        context.json(recipes);
    }

    public void getRecipesByName(Context context, String name) {
		MyLogger.info("getByName");
        List<RecipeModel> recipes = recipeService.getRecipesByName(name);
        context.json(recipes);
    }

    public void addRecipeJSON(Context context) {
		Optional<String> userID = userService.getUserIDBySessionToken( context.cookie("session-token") );
		if ( userID.isPresent() ){
			ServiceAPIResponse r = recipeService.addRecipeFromJSON( context.body(), userID.get() );
			context.status( r.status ).json(r);
			return;
		} else {
			context.status(403);
			return;
		}
    }

	public void addRecipeRating(Context context){
		UserModel user = context.attribute("userData");
		String recipeID = context.pathParam("id");
		try {
			int rating = Integer.parseInt(context.queryParam("nota"));
			if( user != null  ){
				boolean success = recipeService.addRating(recipeID, user.getID(), rating);
				if(success == true){
					context.status(200);
					return;
				}
			}
		} catch(Exception e){
			e.printStackTrace();
		}

		context.status(403);
	}

	public void removeUserRating(Context context){
		Optional<String> userID = userService.getUserIDBySessionToken( context.cookie("session-token") );
		context.status(404);
	}

    public void deleteRecipe(Context context) {
        String recipeID = context.pathParam("id");
		System.out.println(recipeID);
        ServiceAPIResponse res = recipeService.deleteRecipeByID(recipeID);

        context.status(res.status).json(res.message);
    }


}
