package com.receitas.app;


import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;

import com.receitas.app.model.RecipeModel;
import com.receitas.app.model.UserModel;
import com.receitas.app.service.ServiceAPIResponse;
import com.receitas.app.service.UserService;

import com.receitas.app.dao.UserDAO;
import com.receitas.app.dao.RecipeDAO;
import com.receitas.app.service.SessionData;

import com.receitas.app.utils.MyLogger;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserServiceTest {

    private static UserService userService = UserService.getInstance();
	private static String sampleRecipeID;

	private static String addedUserFromJSONID;
	private static String userJSON = "{\"name\":\"John\",\"username\":\"jonjohny\",\"email\":\"john@mail.com\", \"password\":\"password123\", \"favoritedRecipes\":[]}";

    public static String getAUserIDForTesting() {
        ServiceAPIResponse response = userService.registerNewUserFromJSON(userJSON);
		return response.message;
    }

    public static void deleteUserByIDForTesting( String id ) {
		userService.deleteUserByID(id);
    }

    @BeforeAll
    public static void setUp() {
    }

    @AfterAll
	@Test
    public static void deleteUserByID() {
        ServiceAPIResponse response = userService.deleteUserByID(addedUserFromJSONID);
        Assertions.assertEquals(202, response.status);

		RecipeServiceTest.deleteRecipeByIDForTesting(sampleRecipeID);
    }

    @Test
	@Order(1)
    public void testRegisterNewUserFromJSON() {
        ServiceAPIResponse response = userService.registerNewUserFromJSON(userJSON);
        Assertions.assertEquals(201, response.status);

		addedUserFromJSONID = response.message;
		sampleRecipeID = RecipeServiceTest.getARecipeIDForTesting( addedUserFromJSONID );
    }

    @Test
	@Order(2)
    public void testRegisterDuplicatedUser() {
        ServiceAPIResponse response = userService.registerNewUserFromJSON(userJSON);
        Assertions.assertEquals(400, response.status);
    }

    @Test
	@Order(3)
    public void testAuthenticateUserCorrectPassword(){
        Optional<SessionData> userSession = userService.authenticateUserByUsername("jonjohny", "password123");
        Assertions.assertTrue( userSession.isPresent() );
    }

    @Test
	@Order(4)
    public void testAuthenticateUserIncorrectPassword(){
        Optional<SessionData> userSession = userService.authenticateUserByUsername("jonjohny", "incorrect");
        Assertions.assertTrue( userSession.isEmpty() );
    }

    @Test
	@Order(5)
    public void testAuthenticateUserByEmailCorrectPassword(){
        Optional<SessionData> userSession = userService.authenticateUserByEmail("john@mail.com", "password123");
        Assertions.assertTrue( userSession.isPresent() );
    }

    @Test
	@Order(6)
    public void testGetUserFavoriteRecipes() {
        Optional<List<RecipeModel>> recipes = userService.getUserFavoriteRecipes(addedUserFromJSONID);
        Assertions.assertTrue(recipes.isPresent());
    }

    @Test
    public void testSaveRecipeAsFavorite() {
        String recipeID = sampleRecipeID;
        ServiceAPIResponse response = userService.saveRecipeAsFavorite(recipeID, addedUserFromJSONID);

        Assertions.assertEquals(200, response.status);
    }

    @Test
    public void testRemoveRecipeFromFavorites() {
        String recipeID = sampleRecipeID;
        ServiceAPIResponse response = userService.removeRecipeFromFavorites(recipeID, addedUserFromJSONID);

        Assertions.assertEquals(200, response.status);
    }



}
