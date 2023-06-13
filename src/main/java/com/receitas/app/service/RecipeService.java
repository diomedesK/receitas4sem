package com.receitas.app.service;

import com.receitas.app.dao.RecipeDAO;
import com.receitas.app.model.RecipeModel;

import com.receitas.app.service.ServiceAPIResponse;

import java.util.Optional;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.core.JsonProcessingException;

import com.receitas.app.utils.MyLogger;


public class RecipeService {
    private static RecipeDAO recipeDAO;
	private static RecipeService instance;

	private static ObjectMapper mapper = new ObjectMapper();
	private static ServiceAPIResponse invalidJSONResponse = new ServiceAPIResponse("Invalid JSON", 400);

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


	public List<RecipeModel> getManyRecipes( int count ){
		return recipeDAO.getManyRecipes(count);
	}

    public Optional<RecipeModel> getRecipeByID(String recipeID) {
        return recipeDAO.getRecipeByID(recipeID);
    }

    public List<RecipeModel> getRecipesByAuthorID(String authorID) {
        return recipeDAO.getRecipesByAuthorID(authorID);
    }

    public List<RecipeModel> getPopularRecipes() {
        return recipeDAO.getPopularRecipes(4);
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
		try {
			RecipeModel deserializedRecipe = mapper.readValue(recipeJSON, RecipeModel.class);
			return addRecipe( deserializedRecipe );

		} catch( UnrecognizedPropertyException e){
			return invalidJSONResponse;
		} catch ( JsonMappingException jme ){
			return invalidJSONResponse;
		} catch ( JsonProcessingException  jpe){
			return invalidJSONResponse;
		}  
	}

    public ServiceAPIResponse addRecipeFromJSON(String recipeJSON, String authorID) {
		try {
			RecipeModel deserializedRecipe = mapper.readValue(recipeJSON, RecipeModel.class);

			deserializedRecipe.setAuthorID(authorID);

			return addRecipe( deserializedRecipe );

		} catch( UnrecognizedPropertyException e){
			return invalidJSONResponse;
		} catch ( JsonMappingException jme ){
			return invalidJSONResponse;
		} catch ( JsonProcessingException  jpe){
			return invalidJSONResponse;
		}  
	}

	public ServiceAPIResponse addRecipe( RecipeModel recipe ){
		try {
			Optional<String> savedRecipeID = recipeDAO.saveRecipe( recipe );
			boolean wasAddedSuccesfuly = savedRecipeID.isPresent();

			if ( wasAddedSuccesfuly ){
				return new ServiceAPIResponse( savedRecipeID.get(), 201 );
			} else {
				return new ServiceAPIResponse("Invalid JSON", 400 );
			}

		} catch( RuntimeException e) {
			if ( e.getCause() instanceof java.sql.SQLIntegrityConstraintViolationException ){
				/* There should be handling for specific situations where
				 * the provided author is unxistent, for example  TODO*/
				return new ServiceAPIResponse(e.getCause().getMessage(), 400);

			} else {
				return new ServiceAPIResponse("Unexpected", 317);
			}
		}

	}

    public ServiceAPIResponse deleteRecipeByID(String recipeID) {

        boolean res = recipeDAO.deleteRecipeByID(recipeID);

		if ( res == true ){
			return new ServiceAPIResponse("Recipe deleted succesfuly", 200);
		} else {
			return new ServiceAPIResponse("Non-existing recipe", 404);
		}

    }

    public boolean addRating(String recipeID, String userID, int rating) {
		if ( rating >= 0 && rating <= 5 ){
			return recipeDAO.addRating(recipeID, userID, rating);
		} else {
			return false;
		}

    }

    public Optional<Integer> getRating(String recipeID, String userID) {
        return recipeDAO.getRating(recipeID, userID);
    }

    public boolean addAccess(String recipeID) {
        return recipeDAO.addAccess(recipeID);
    }

    public boolean clearAccessesOfRecipeFromDaysAgo(String recipeID, int days) {
        return recipeDAO.clearAccessesOfRecipeFromDaysAgo(recipeID, days);
    }

    public boolean clearAccessesOfAllRecipesFromDaysAgo(int days) {
        return recipeDAO.clearAccessesOfAllRecipesFromDaysAgo(days);
    }


}

