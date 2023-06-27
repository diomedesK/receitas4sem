package com.receitas.app.dao;

import com.receitas.app.model.*;

import java.util.List;
import java.util.Optional;

public interface UserDAOInterface {

	public Optional<List<RecipeModel>> getUserFavoriteRecipes( String userID );

	public boolean saveRecipeAsFavorite( String userID, String recipeID );
	public boolean removeRecipeFromFavorites( String recipeID, String userID );

	public Optional<String> registerNewUser( UserModel user );
	public boolean deleteUserByID( String userID );

	public Optional<UserModel> authenticateUserByUsername ( String username, String hashedPassword );
	public Optional<UserModel> authenticateUserByEmail ( String email, String hashedPassword );

	public Optional<String> getUserIDByEmail ( String email );
	public Optional<String> getUserIDByUsername ( String username );
}
