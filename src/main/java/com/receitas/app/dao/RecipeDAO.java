package com.receitas.app.dao;

import com.receitas.app.model.RecipeModel;
import com.receitas.app.dummyData.DummyRecipes;



import java.util.Optional;
import java.util.ArrayList;
import java.util.List;

import java.util.Collections;
import java.util.Comparator;
import java.util.NoSuchElementException;

// the dao uses a singleton pattern
public class RecipeDAO implements RecipeDAOInterface {

	private List<RecipeModel> dummyData;

	private static RecipeDAO instance;

	private RecipeDAO(){
		this.dummyData = DummyRecipes.generateDummyRecipes();
	}

	public static RecipeDAO getInstance(){
		if ( instance == null ){
			synchronized( RecipeDAO.class ){
				instance = new RecipeDAO();
			}
		}

		return instance;
	}


	public List<RecipeModel> getAllRecipes(){
		return this.dummyData;
	}

	@Override
	public Optional<RecipeModel> getRecipeByID( String recipeID ){
		return this.dummyData.stream().filter( (recipe) -> recipe.getID().equals(recipeID) ).findFirst();
	}

	@Override
	public List<RecipeModel> getRecipesByAuthorID( String authorID ){
		return this.dummyData.stream().filter( (recipe) -> recipe.getAuthorID().equals(authorID) ).toList();
	}


	@Override
	public List<RecipeModel> getPopularRecipes(){
		Collections.sort(this.dummyData, Comparator.comparing(RecipeModel::getAccessesWithinLast7Days));
		return this.dummyData.subList(0, Math.min(this.dummyData.size(), 4));
	}

	@Override
	public List<RecipeModel> getRecipesByName( String recipeName ){
		String rgx = ".*" + recipeName + ".*";
		return this.dummyData.stream().filter( (recipe) -> recipe.getName().matches(rgx) ).toList();
	}

	@Override
	public List<RecipeModel> getRecipesByIngredients( String... ingredients ){
		List<RecipeModel> matchingRecipes = new ArrayList<>();

		for (String targetIngredientName : ingredients) {
			this.dummyData.forEach(recipe -> {
				recipe.getIngredients().forEach(recipeIngredient -> {
					if (recipeIngredient.getName().equals(targetIngredientName)) {
						matchingRecipes.add(recipe);
						return; // Continue to the next iteration of the forEach loop
					}
				});
			});
		}

		return matchingRecipes;
	}

	@Override
	public List<RecipeModel> getRecipesByCategories( String... categories ){
		List<RecipeModel> matchingRecipes = new ArrayList<>();

		return matchingRecipes;
	}

	@Override
	public boolean deleteRecipeByID( String recipeID ){
		Optional<RecipeModel> tr = this.getRecipeByID(recipeID);

		if(tr.isPresent()){
			this.dummyData.remove( tr.get() );
			return true;
		} else {
			return false;
		}

	}

	@Override
	public boolean addRecipe( RecipeModel recipe ){
		this.dummyData.add( recipe );
		return true;
	}

	@Override
	public boolean addRating( String recipeID, String userID, int rating ){
		try {
			this.getRecipeByID(recipeID).get().addRating( userID, rating );
			return true;
		} catch( NoSuchElementException e){
			return false;
		}

	}


}
