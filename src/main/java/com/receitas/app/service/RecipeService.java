package com.receitas.app.service;

import com.receitas.app.dao.RecipeDAO;
import com.receitas.app.model.RecipeModel;

import com.receitas.app.service.ServiceAPIResponse;

import java.util.Optional;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;

public class RecipeService {
    private static RecipeDAO recipeDAO;
	private static RecipeService instance;

    private RecipeService() {
        recipeDAO = RecipeDAO.getInstance();
    }

	public static RecipeService getInstance(){
		if ( instance == null ){
			synchronized (RecipeService.class) {
				if (instance == null) {
					instance = new RecipeService();
				}
			}
		}

		return instance;
	}

	public List<RecipeModel> getAllRecipes(){
		return recipeDAO.getAllRecipes();
	}

    public Optional<RecipeModel> getRecipeByID(String recipeID) {
        return recipeDAO.getRecipeByID(recipeID);
    }

    public List<RecipeModel> getRecipesByAuthorID(String authorID) {
        return recipeDAO.getRecipesByAuthorID(authorID);
    }

    public List<RecipeModel> getPopularRecipes() {
        return recipeDAO.getPopularRecipes();
    }

    public List<RecipeModel> getRecipesByName(String recipeName) {
        return recipeDAO.getRecipesByName(recipeName);
    }

    public List<RecipeModel> getRecipesByIngredients(String... ingredients) {
        return recipeDAO.getRecipesByIngredients(ingredients);
    }

    public List<RecipeModel> getRecipesByCategories(String... categories) {
        return recipeDAO.getRecipesByCategories(categories);
    }

    public ServiceAPIResponse addRecipeFromJSON(String recipeJSON) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			RecipeModel deserializedRecipe = mapper.readValue(recipeJSON, RecipeModel.class);

			recipeDAO.addRecipe( deserializedRecipe );
			return new ServiceAPIResponse( "Recipe added succesfuly", 201 );

		} catch(UnrecognizedPropertyException e){
			return new ServiceAPIResponse("Invalid JSON", 400 );
		} catch( Exception e) {
			e.printStackTrace();
			return new ServiceAPIResponse("Internal error", 500);
		}

    }

    public boolean addRating(String recipeID, String userID, int rating) {
        return recipeDAO.addRating(recipeID, userID, rating);
    }

    public ServiceAPIResponse deleteRecipeByID(String recipeID) {
        boolean res = recipeDAO.deleteRecipeByID(recipeID);

		if ( res == true ){
			return new ServiceAPIResponse("Recipe deleted succesfuly", 200);
		} else {
			return new ServiceAPIResponse("Non-existing recipe", 404);
		}
    }

}
