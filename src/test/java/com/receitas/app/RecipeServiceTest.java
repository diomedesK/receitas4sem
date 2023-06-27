package com.receitas.app;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import com.receitas.app.model.RecipeModel;

import com.receitas.app.service.RecipeService;
import com.receitas.app.service.UserService;

import com.receitas.app.service.ServiceAPIResponse;

public class RecipeServiceTest {

    private static RecipeService recipeService = RecipeService.getInstance();
    private static UserService userService = UserService.getInstance();

	public static String generatedRecipeID;
	public static String generatedUserID;

	@BeforeAll
	@Test
	public static void createSampleRecipeAndGetSampleUser() {

		Optional<String> userIDByEmail = userService.getUserIDByEmail("john@mail.com");

		if ( userIDByEmail.isPresent() ){
			generatedUserID = userIDByEmail.get();
		} else {
			ServiceAPIResponse addUserResponse = userService.registerNewUserFromJSON(TestDataSamples.userJSON);
			Assertions.assertEquals(201, addUserResponse.status);
			generatedUserID = addUserResponse.message;
		}


		ServiceAPIResponse addRecipeResponse = recipeService.addRecipeFromJSON( TestDataSamples.recipeJSON, generatedUserID);
		Assertions.assertEquals(201, addRecipeResponse.status );
		generatedRecipeID = addRecipeResponse.message;
	}

	@AfterAll
	@Test
	public static void destroy(){
		ServiceAPIResponse deleteRecipeResponse = recipeService.deleteRecipeByID(generatedRecipeID, generatedUserID);

		System.out.println(deleteRecipeResponse.message);
        Assertions.assertEquals(202, deleteRecipeResponse.status);

        userService.deleteUserByID(generatedUserID);
	}

    @Test
    public void testGetRecipeByID() {
		String recipeID = generatedRecipeID;

        Optional<RecipeModel> recipe = recipeService.getRecipeByID(recipeID);
        Assertions.assertTrue(recipe.isPresent());

        Assertions.assertEquals(recipeID, recipe.get().getID());
    }

    @Test
    public void testGetPopularRecipes() {
        List<RecipeModel> popularRecipes = recipeService.getPopularRecipes(8);
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
				generatedRecipeID,
				generatedUserID,
				rating
				);
        Assertions.assertTrue(added);
    }

}
