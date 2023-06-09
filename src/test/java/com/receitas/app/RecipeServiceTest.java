package com.receitas.app;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import com.receitas.app.service.RecipeService;
import com.receitas.app.model.RecipeModel;

public class RecipeServiceTest {

    private RecipeService recipeService;

	private static String dummyAuthorID = "qAfxQr";
	private static String recipeJSON = "{ \"name\": \"Chocolate Cake\", \"authorID\": \"" + dummyAuthorID + "\", \"description\": \"Delicious chocolate cake recipe\", \"prepareInMinutes\": 60, \"cookingMethod\": \"Baking\", \"categories\": [ \"Desserts\", \"Cakes\" ], \"instructions\": { \"0\": \"Preheat the oven to 350°F\", \"1\": \"Mix the dry ingredients in a bowl\", \"2\": \"Combine the wet ingredients in another bowl\" }, \"accessesWithinLast7Days\": 0, \"ratings\": { \"user1\": 5, \"user2\": 4 }, \"ingredients\": [ { \"name\": \"Flour\" }, { \"name\": \"Sugar\" } ] }";

    @BeforeEach
	public void setUp() {
		recipeService = RecipeService.getInstance();
	}

    @Test
    public void testAddRecipeFromJSON() {
        boolean added = recipeService.addRecipeFromJSON(recipeJSON).status == 201;
        Assertions.assertTrue(added);

        // Perform assertions based on expected results
    }

    @Test
    public void testGetRecipeByID() {
		String recipeID = recipeService.getPopularRecipes().stream().findFirst().get().getID();

        Optional<RecipeModel> recipe = recipeService.getRecipeByID(recipeID);
        Assertions.assertTrue(recipe.isPresent());
        Assertions.assertEquals(recipeID, recipe.get().getID());
    }

    @Test
    public void testGetRecipesByAuthorID() {
		recipeService.addRecipeFromJSON( recipeJSON );
		recipeService.getRecipesByAuthorID( dummyAuthorID ).stream()
			.forEach( (recipe) -> Assertions.assertTrue( recipe.getAuthorID().equals(dummyAuthorID) ) );
    }

    @Test
    public void testGetPopularRecipes() {
        List<RecipeModel> popularRecipes = recipeService.getPopularRecipes();
        Assertions.assertNotNull(popularRecipes);
        // Perform assertions based on expected results
    }

    @Test
    public void testGetRecipesByName() {
        String recipeName = "cake";
        List<RecipeModel> recipes = recipeService.getRecipesByName(recipeName);

		recipes.forEach( (recipe) -> {
			Assertions.assertTrue( recipeName.toLowerCase().contains(recipeName.toLowerCase()) );
		});

        // Perform assertions based on expected results
    }

    @Test
    public void testGetRecipesByIngredients() {
        String[] ingredients = new String[]{"Flour", "Sugar" };
        List<RecipeModel> recipes = recipeService.getRecipesByIngredients( ingredients );

        Assertions.assertNotNull(recipes);
        // Perform assertions based on expected results
    }

    @Test
    public void testGetRecipesByCategories() {
        String category1 = "Desserts";
        String category2 = "Cakes";
        List<RecipeModel> recipes = recipeService.getRecipesByCategories(category1, category2);

        Assertions.assertNotNull(recipes);
        // Perform assertions based on expected results
    }


    @Test
    public void testAddRating() {
        String userID = "user1";
        int rating = 5;

        boolean added = recipeService.addRating(
				recipeService.getPopularRecipes().get(0).getID(),
				userID,
				rating
				);
        Assertions.assertTrue(added);
        // Perform assertions based on expected results
    }



}
