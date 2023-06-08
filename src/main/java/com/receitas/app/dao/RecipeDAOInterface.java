package com.receitas.app.dao;
import com.receitas.app.model.RecipeModel;

import java.util.List;
import java.util.Optional;

public interface RecipeDAOInterface {

	public Optional<RecipeModel> getRecipeByID( String recipeID );


	public List<RecipeModel> getPopularRecipes();
	public List<RecipeModel> getRecipesByName( String recipeName );
	public List<RecipeModel> getRecipesByAuthorID( String authorID );
	public List<RecipeModel> getRecipesByIngredients( String... ingredients );
	public List<RecipeModel> getRecipesByCategories( String... categories );

	public boolean deleteRecipeByID( String recipeID );

	public boolean addRecipe( RecipeModel recipe );
	public boolean addRating( String recipeID, String userID, int rating );

}
