package com.receitas.app.controller;

import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;

import java.util.Map;
import java.util.List;
import java.util.Optional;

import com.receitas.app.service.RecipeService;
import com.receitas.app.service.ServiceAPIResponse;
import com.receitas.app.model.RecipeModel;

import com.receitas.app.utils.MyLogger;

public class RecipeController {

	/*
	 * Reading, updating and deleting operations are enveloped in the
	 * ServiceAPIResponse, meanwhile reading operations are handled
	 * by the Javalin API.
	 */

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

	public void getRecipes( Context context ){
		if( context.queryParamMap().containsKey("id") ){
			getRecipeByID(context);
		} else if ( context.queryParamMap().containsKey("authorID") ){
			getRecipesByAuthorID(context);
		}  else if ( context.queryParamMap().containsKey("ingredient") ){

		} else {
			getAllRecipes(context);
		}

	}

    public void getAllRecipes(Context context) {
        List<RecipeModel> recipes = recipeService.getAllRecipes();
        context.json(recipes);
    }

    public void getRecipeByID(Context context) {
		MyLogger.info("getByID");
        String recipeId = context.queryParam("id");
        Optional<RecipeModel> recipe = recipeService.getRecipeByID(recipeId);
        context.json(recipe.orElseThrow(() -> new NotFoundResponse("Recipe not found")));
    }

    public void getRecipesByAuthorID(Context context) {
		MyLogger.info("getByAuthorID");
        List<RecipeModel> recipes = recipeService.getRecipesByAuthorID( context.queryParam("authorID") );
        context.json(recipes);
    }

	/*
	 * after the user dao has been established, make a method for that
    public void getRecipesByAuthorUsername(Context context) {
        String recipeId = context.pathParam("username");
        List<RecipeModel> recipes = recipeService.getRecipesByAuthorID(recipeId);
        context.json(recipes);
    }
	*/

    public void addRecipeJSON(Context context) {
        ServiceAPIResponse r = recipeService.addRecipeFromJSON( context.body() );

		context.status( r.status ).json(r.message);
    }

    // public void updateRecipe(Context context) {
    //     String recipeId = context.pathParam("id");
    //     RecipeModel recipe = context.bodyAsClass(RecipeModel.class);
    //     recipeService.updateRecipe(recipeId, recipe);
    //     context.status(204);
    // }

    public void deleteRecipe(Context context) {
        String recipeId = context.pathParam("id");
        ServiceAPIResponse res = recipeService.deleteRecipeByID(recipeId);

        context.status(res.status).json(res.message);
    }

}
