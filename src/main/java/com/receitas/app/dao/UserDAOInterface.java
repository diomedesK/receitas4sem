package com.receitas.app.dao;
import com.receitas.app.model.UserModel;

public interface UserDAOInterface {

	public boolean getUserFavoriteRecipes( String userID );

	public boolean saveRecipeAsFavorite( String recipeID, String userID );
	public boolean removeRecipeFromFavorites( String recipeID, String userID );

	public boolean registerNewUser( UserModel user );

	public UserModel authenticateUserByUsername ( String username, String hashedPassword );
	public UserModel authenticateUserByEmail ( String email, String hashedPassword );

}
