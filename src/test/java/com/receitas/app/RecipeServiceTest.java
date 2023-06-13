package com.receitas.app;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import com.receitas.app.service.RecipeService;

import com.receitas.app.model.UserModel;
import com.receitas.app.model.RecipeModel;

import com.receitas.app.dao.RecipeDAO;
import com.receitas.app.dao.UserDAO;

import com.receitas.app.service.ServiceAPIResponse;

public class RecipeServiceTest {

    public static RecipeService recipeService = RecipeService.getInstance();

	public static String addedRecipeFromJSONID;
	public static String sampleUserID;

	public static String getRecipeJSON( String authorID ){
		return String.format( "{ \"name\": \"My Cake\", \"authorID\": \"%s\", \"description\": \"Delicious chocolate cake recipe\", \"prepareInMinutes\": 60, \"cookingMethod\": \"Baking\", \"categories\": [ \"Cakes\", \"Sweet\" ], \"instructions\": { \"0\": \"Prehea at the oven to 350°F\", \"1\": \"Mix the dry ingredients in a bowl\", \"2\": \"Combine the wet ingredients in another bowl\" }, \"accessesWithinLast7Days\": 0, \"ratings\": { \"1\": 5, \"2\": 4 }, \"ingredients\": [ { \"name\": \"wheat\" }, { \"name\": \"milk\" } ] }", authorID  );
	}

    public static String getARecipeIDForTesting( String authorID ) {
		String recipeID = recipeService.addRecipeFromJSON( getRecipeJSON(authorID) ).message;
		return recipeID;
    }

    public static void deleteRecipeByIDForTesting( String id ) {
		recipeService.deleteRecipeByID(id);
    }

    @BeforeAll
	public static void setUp() {
		sampleUserID = UserServiceTest.getAUserIDForTesting();
	}

	@AfterAll
	public static void clearAddedJSONRecipe(){
		recipeService.deleteRecipeByID(addedRecipeFromJSONID);
		UserServiceTest.deleteUserByIDForTesting(sampleUserID);
	}

    @Test
    public void testAddRecipeFromJSON() {
		ServiceAPIResponse res = recipeService.addRecipeFromJSON( getRecipeJSON(sampleUserID) );
        boolean added = res.status == 201;
		Assertions.assertTrue(added);
		
		addedRecipeFromJSONID = res.message;
    }

    @Test
    public void testGetRecipeByID() {
		String recipeID = addedRecipeFromJSONID;

        Optional<RecipeModel> recipe = recipeService.getRecipeByID(recipeID);
        Assertions.assertTrue(recipe.isPresent());

        Assertions.assertEquals(recipeID, recipe.get().getID());
    }

    @Test
    public void testGetPopularRecipes() {
        List<RecipeModel> popularRecipes = recipeService.getPopularRecipes();
        Assertions.assertNotNull(popularRecipes);
    }

    @Test
    public void testGetRecipesByName() {
        String recipeName = "cake";
        List<RecipeModel> recipes = recipeService.getRecipesByName(recipeName);

		recipes.forEach( (recipe) -> {
			Assertions.assertTrue( recipeName.toLowerCase().contains(recipeName.toLowerCase()) );
		});
    }

    @Test
    public void testGetRecipesByIngredients() {
        String[] ingredients = new String[]{"Flour", "Sugar" };
        List<RecipeModel> recipes = recipeService.getRecipesByIngredients( ingredients );

        Assertions.assertNotNull(recipes);
    }

    @Test
    public void testGetRecipesByCategories() {
        String category1 = "Desserts";
        String category2 = "Cakes";
        List<RecipeModel> recipes = recipeService.getRecipesByCategories(category1, category2);

        Assertions.assertNotNull(recipes);
    }


    @Test
    public void testAddRating() {
        int rating = 5;

        boolean added = recipeService.addRating(
				addedRecipeFromJSONID,
				sampleUserID,
				rating
				);
        Assertions.assertTrue(added);

    }



}
