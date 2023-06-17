package com.receitas.app.controller;

import io.javalin.http.Context;

import java.util.List;
import java.util.Optional;
import java.util.Map;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.receitas.app.service.RecipeService;
import com.receitas.app.service.UserService;
import com.receitas.app.service.ServiceAPIResponse;

import com.receitas.app.model.RecipeModel;
import com.receitas.app.model.UserModel;

import com.receitas.app.utils.MyLogger;

public class RecipeController {

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

			context.render("recipePage.html");

		} else {
			context.status(404);
		}

	};

	public void getNewRecipePage( Context context ){
		if (context.attribute("userData") != null){
			context.render("addRecipePage.html");
		} else {
			context.redirect("/");
		}
	}

	public void homePageHandler ( Context context ){
		context.attribute("popularRecipes", recipeService.getPopularRecipes(8) );
		context.attribute("randomRecipes", recipeService.getRandomRecipes(4) );
		context.render("homePage.html");
	};

	public void getSearchPage( Context context ){

		context.attribute("results", getRecipesFromContext(context));
		context.attribute("query", context.queryParamMap());


		context.render("searchPage.html");
	}

	public void getRecipes( Context context ){
		LinkedHashSet<RecipeModel> r = getRecipesFromContext(context);
		context.json(r);
	}

	public LinkedHashSet<RecipeModel> getRecipesFromContext( Context context ){
		// TODO add specific queries in the database to handle all specific cases
		// Just for a while...

		LinkedHashSet<RecipeModel> recipes = new LinkedHashSet<>();

		if (context.queryParamMap().containsKey("id")) {
			Optional<RecipeModel> recipe = recipeService.getRecipeByID(context.queryParam("id"));
			if (recipe.isPresent()) {
				recipes.add(recipe.get());
			} 
			return recipes;
		}

		Set<String> commonRecipeIds = null;
		for (Map.Entry<String, List<String>> entry : context.queryParamMap().entrySet()) {
			String paramName = entry.getKey();
			List<String> paramValues = entry.getValue();
			List<RecipeModel> filteredRecipes;

			if (paramName.equals("autor")) {
				filteredRecipes = recipeService.getRecipesByAuthorID(paramValues.get(0));
			} else if (paramName.equals("ingrediente")) {
				filteredRecipes = recipeService.getRecipesByIngredients(paramValues.get(0).split(":"));
			} else if (paramName.equals("categoria")) {
				filteredRecipes = recipeService.getRecipesByCategories(paramValues.get(0).split(":"));
			} else if (paramName.equals("nome")) {
				filteredRecipes = recipeService.getRecipesByName(paramValues.get(0));
			} else {
				continue; 
			}

			Set<String> recipeIDs = filteredRecipes.stream()
				.map(RecipeModel::getID)
				.collect(Collectors.toSet());

			if (commonRecipeIds == null) {
				commonRecipeIds = recipeIDs;
			} else {
				commonRecipeIds.retainAll(recipeIDs);
			}
		}

		if (commonRecipeIds != null) {
			for (String recipeID : commonRecipeIds) {
				Optional<RecipeModel> recipe = recipeService.getRecipeByID(String.valueOf(recipeID));
				recipe.ifPresent(recipes::add);
			}
		}

		return recipes;
	}

    public void getPopularRecipes(Context context) {
		recipeService.clearAccessesOfAllRecipesFromDaysAgo(7);
        List<RecipeModel> recipes = recipeService.getPopularRecipes(8);
        context.json(recipes);
    }

    public void getRecipesByAuthorID(Context context, String authorID) {
		MyLogger.info("getByAuthorID");
        List<RecipeModel> recipes = recipeService.getRecipesByAuthorID( authorID );
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

    public void deleteRecipe(Context context) {
        String recipeID = context.pathParam("id");
		System.out.println(recipeID);
        ServiceAPIResponse res = recipeService.deleteRecipeByID(recipeID);

        context.status(res.status).json(res.message);
    }


}
