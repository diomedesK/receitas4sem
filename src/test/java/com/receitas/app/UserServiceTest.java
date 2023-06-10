package com.receitas.app;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


import java.util.List;
import java.util.Optional;
import com.receitas.app.model.RecipeModel;
import com.receitas.app.model.UserModel;
import com.receitas.app.service.ServiceAPIResponse;
import com.receitas.app.service.UserService;

import com.receitas.app.dao.UserDAO;
import com.receitas.app.dao.RecipeDAO;
import com.receitas.app.utils.MyLogger;

public class UserServiceTest {

    private static UserService userService = UserService.getInstance();

	public static UserModel sampleUser;
	public static RecipeModel sampleRecipe;

    @BeforeAll
    public static void setUp() {
		sampleUser = UserDAO.getInstance().getRandomUserForTesting();
		sampleRecipe = RecipeDAO.getInstance().getRandomRecipeForTesting();
    }

    @Test
    public void testGetUserFavoriteRecipes() {
        String userID = sampleUser.getID();
        Optional<List<RecipeModel>> recipes = userService.getUserFavoriteRecipes(userID);
        Assertions.assertTrue(recipes.isPresent());
        // Perform assertions based on expected results
    }

    @Test
    public void testSaveRecipeAsFavorite() {
        String recipeID = sampleRecipe.getID();
        String userID = sampleUser.getID();
        ServiceAPIResponse response = userService.saveRecipeAsFavorite(recipeID, userID);

        Assertions.assertEquals(200, response.status);
    }

    @Test
    public void testRemoveRecipeFromFavorites() {
        String recipeID = sampleRecipe.getID();
        String userID = sampleUser.getID();
        ServiceAPIResponse response = userService.removeRecipeFromFavorites(recipeID, userID);

        Assertions.assertEquals(200, response.status);
    }

    @Test
    public void testRegisterNewUserFromJSON() {
        String userJSON = "{\"id\":\"99\", \"name\":\"John\",\"username\":\"jonjohny\",\"email\":\"john@mail.com\", \"password\":\"hashed123\", \"favoritedRecipes\":[]}";
        ServiceAPIResponse response = userService.registerNewUserFromJSON(userJSON);
        Assertions.assertEquals(201, response.status);
    }

    @Test
    public void deleteUserByID() {
        ServiceAPIResponse response = userService.deleteUserByID("99");
        Assertions.assertEquals(202, response.status);
    }

    @Test
    public void testAuthenticateUserByUsername() {
        Optional<UserModel> user = userService.authenticateUserByUsername(
				sampleUser.getUsername(),
				sampleUser.getPassword()
				);

        Assertions.assertTrue(user.isPresent());
		Assertions.assertFalse( userService.authenticateUserByUsername(
				sampleUser.getUsername(),
				"invalidPassword"
				).isPresent()
				);
    }

    @Test
    public void testAuthenticateUserByEmail() {
        Optional<UserModel> user = userService.authenticateUserByEmail(
				sampleUser.getEmail(),
				sampleUser.getPassword()
				);

        Assertions.assertTrue(user.isPresent());
		Assertions.assertFalse( userService.authenticateUserByEmail(
				sampleUser.getEmail(),
				"invalidPassword"
				).isPresent()
				);
    }


}
