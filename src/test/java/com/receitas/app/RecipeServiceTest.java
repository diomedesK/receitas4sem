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

    private static RecipeService recipeService;

	public static RecipeModel sampleRecipe;
	public static UserModel sampleUser;

	private static String recipeJSON;
	private static String addedRecipeFromJSONID;


    @BeforeAll
	public static void setUp() {
		recipeService = RecipeService.getInstance();
		sampleRecipe = RecipeDAO.getInstance().getRandomRecipeForTesting();
		sampleUser = UserDAO.getInstance().getRandomUserForTesting();

		recipeJSON = "{ \"name\": \"Asuka Cake\", \"authorID\": \"" + sampleUser.getID() + "\", \"description\": \"Delicious chocolate cake recipe\", \"prepareInMinutes\": 60, \"cookingMethod\": \"Baking\", \"categories\": [ \"asukaNotThere\", \"asukaNotThere2\" ], \"instructions\": { \"0\": \"Preheat asuka at the oven to 350°F\", \"1\": \"Mix the dry asuka ingredients in a bowl\", \"2\": \"Combine the asuka wet ingredients in another bowl\" }, \"accessesWithinLast7Days\": 0, \"ratings\": { \"1\": 5, \"2\": 4 }, \"ingredients\": [ { \"name\": \"asuka\" }, { \"name\": \"ray\" } ] }";

	}

	@AfterAll
	public static void clearAddedJSONRecipe(){
		recipeService.deleteRecipeByID(addedRecipeFromJSONID);
	}

    @Test
    public void testAddRecipeFromJSON() {
		ServiceAPIResponse res = recipeService.addRecipeFromJSON(recipeJSON);
        boolean added = res.status == 201;
		Assertions.assertTrue(added);
		
		addedRecipeFromJSONID = res.message;
        // Perform assertions based on expected results
    }

    @Test
    public void testGetRecipeByID() {
		String recipeID = sampleRecipe.getID();

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
				sampleRecipe.getID(),
				sampleUser.getID(),
				rating
				);
        Assertions.assertTrue(added);

    }



}
