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
			getRecipeByID(
					context,
					context.queryParam("id")
					);

		} else if ( context.queryParamMap().containsKey("authorID") ){
			getRecipesByAuthorID(
					context,
					context.queryParam("authorID")
					);

		}  else if ( context.queryParamMap().containsKey("ingredient") ){
			getRecipesByIngredients(
					context,
					context.queryParam("ingredient").split(":")
					);

		} else if ( context.queryParamMap().containsKey("category")){
			getRecipesByCategories(
					context, 
					context.queryParam("category").split(":")
					);

		} else if ( context.queryParamMap().containsKey("name")){
			getRecipesByName(
					context,
					context.queryParam("name")
					);
			
		} else {
			getAllRecipes(context);
		}

	}

    public void getAllRecipes(Context context) {
        List<RecipeModel> recipes = recipeService.getAllRecipes();
        context.json(recipes);
    }

    public void getRecipeByID(Context context, String recipeID) {
		MyLogger.info("getByID");
        Optional<RecipeModel> recipe = recipeService.getRecipeByID(recipeID);

        context.json(recipe.orElseThrow(() -> new NotFoundResponse("Recipe not found")));
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
        List<RecipeModel> recipes = recipeService.getRecipesByName( context.queryParam("name") );
        context.json(recipes);
    }

    public void addRecipeJSON(Context context) {
        ServiceAPIResponse r = recipeService.addRecipeFromJSON( context.body() );

		context.status( r.status ).json(r.message);
    }

	public void addRecipeRating(Context context){
		context.status(404);
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
