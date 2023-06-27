package com.receitas.app;


import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.receitas.app.model.RecipeModel;
import com.receitas.app.service.ServiceAPIResponse;

import com.receitas.app.service.UserService;
import com.receitas.app.service.RecipeService;

import com.receitas.app.service.SessionData;


public class UserServiceTest {

    private static UserService userService = UserService.getInstance();
    private static RecipeService recipeService = RecipeService.getInstance();

	private static String generatedRecipeID;
	private static String generatedUserID;

	@BeforeAll
	@Test
	public static void createSampleUserAndGetSampleRecipe(){

		Optional<String> userIDByEmail = userService.getUserIDByEmail("john@mail.com");

		if ( userIDByEmail.isPresent() ){
			generatedUserID = userIDByEmail.get();
		} else {
			ServiceAPIResponse addUserResponse = userService.registerNewUserFromJSON(TestDataSamples.userJSON);
			Assertions.assertEquals(201, addUserResponse.status);
			generatedUserID = addUserResponse.message;
		}

		ServiceAPIResponse addRecipeResponse = recipeService.addRecipeFromJSON( TestDataSamples.recipeJSON, generatedUserID);
		generatedRecipeID = addRecipeResponse.message;
	}

	@Test
    @AfterAll
    public static void destroy() {
        ServiceAPIResponse response = userService.deleteUserByID(generatedUserID);
        Assertions.assertEquals(202, response.status);

		recipeService.deleteRecipeByID(generatedRecipeID, generatedUserID);
    }

    @Test
    public void testRegisterDuplicatedUser() {
        ServiceAPIResponse response = userService.registerNewUserFromJSON( TestDataSamples.userJSON );
        Assertions.assertEquals(400, response.status);
    }

    @Test
    public void testAuthenticateUserCorrectPassword(){
        Optional<SessionData> userSession = userService.authenticateUserByUsername("jonjohny", "password123");
        Assertions.assertTrue( userSession.isPresent() );
    }

    @Test
    public void testAuthenticateUserIncorrectPassword(){
        Optional<SessionData> userSession = userService.authenticateUserByUsername("jonjohny", "incorrect");
        Assertions.assertTrue( userSession.isEmpty() );
    }

    @Test
    public void testAuthenticateUserByEmailCorrectPassword(){
        Optional<SessionData> userSession = userService.authenticateUserByEmail("john@mail.com", "password123");
        Assertions.assertTrue( userSession.isPresent() );
    }

    @Test
    public void testGetUserFavoriteRecipes() {
        Optional<List<RecipeModel>> recipes = userService.getUserFavoriteRecipes(generatedUserID);
        Assertions.assertTrue(recipes.isPresent());
    }

    @Test
    public void testSaveRecipeAsFavorite() {
        String recipeID = generatedRecipeID;
        ServiceAPIResponse response = userService.saveRecipeAsFavorite(recipeID, generatedUserID);

        Assertions.assertEquals(200, response.status);
    }

    @Test
    public void testRemoveRecipeFromFavorites() {
        String recipeID = generatedRecipeID;
        ServiceAPIResponse response = userService.removeRecipeFromFavorites(recipeID, generatedUserID);

        Assertions.assertEquals(200, response.status);
    }

    @Test
    public void testWasEmailRegisteredExpectTrue() {
        Optional<String> userID = userService.getUserIDByEmail("john@mail.com");
        Assertions.assertEquals(true, userID.isPresent());
    }

    @Test
    public void testWasEmailRegisteredExpectFalse() {
        Optional<String> userID = userService.getUserIDByEmail("notavalid@email.com");
        Assertions.assertEquals(false, userID.isPresent());
    }
	
    @Test
    public void testWasUsernameRegisteredExpectTrue() {
        Optional<String> userID = userService.getUserIDByUsername("jonjohny");
        Assertions.assertEquals(true, userID.isPresent());
    }

    @Test
    public void testWasUsernameRegisteredExpectFalse() {
        Optional<String> userID = userService.getUserIDByUsername("notavalid");
        Assertions.assertEquals(false, userID.isPresent());
    }




}
