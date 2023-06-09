package com.receitas.app.dao;

import com.receitas.app.model.*;

import java.util.Optional;
import java.util.ArrayList;
import java.util.List;



public class UserDAO implements UserDAOInterface {

	private List<UserModel> dummyUsers;

	private static UserDAO instance;

	private UserDAO(){
		this.dummyUsers = new ArrayList<>();

		for (int i = 0; i < 8; i++) {
			UserModel user = new UserModel();
			user.setID(""+i);
			user.setName("User" + i);
			user.setUsername("user" + i);
			user.setEmail("user" + i + "@example.com");
			user.setPassword("password" + i);
			i++;
			dummyUsers.add(user);
		}

	}

	public static UserDAO getInstance(){
		if ( instance == null ){
			synchronized( UserDAO.class ){
				instance = new UserDAO();
			}
		}

		return instance;
	}

	public UserModel getRandomUserForTesting(){
		return this.dummyUsers.get(0);
	}

	private Optional<UserModel> getUserByID( String userID ){
		return this.dummyUsers.stream().filter( (user) -> {
			return user.getID().equals(userID) ;
		} ).findFirst();
	}

	private Optional<UserModel> getUserByUsername( String username ){
		return this.dummyUsers.stream().filter( (user) -> user.getUsername().equals(username) ).findFirst();
	}

	private Optional<UserModel> getUserByEmail( String email ){
		return this.dummyUsers.stream().filter( (user) -> user.getEmail().equals(email) ).findFirst();
	}

	public Optional<List<RecipeModel>> getUserFavoriteRecipes( String userID ){
		Optional<UserModel> tu = this.getUserByID( userID );
		
		if ( tu.isPresent() ){
			return Optional.of( tu.get().getFavoritedRecipes() );
		} else {
			return Optional.ofNullable(null);
		}
	}

	public boolean saveRecipeAsFavorite( String recipeID, String userID ){
		Optional<UserModel> tu = this.getUserByID( userID );
		Optional<RecipeModel> tr = RecipeDAO.getInstance().getRecipeByID(recipeID);

		if ( tu.isPresent() && tr.isPresent() ){
			tu.get().getFavoritedRecipes().add( tr.get() );
			return true;
		} else {
			return false;
		}

	}

	public boolean removeRecipeFromFavorites( String recipeID, String userID ){
		Optional<UserModel> tu = this.getUserByID( userID );
		Optional<RecipeModel> tr = RecipeDAO.getInstance().getRecipeByID(recipeID);

		if ( tu.isPresent() && tr.isPresent() && tu.get().getFavoritedRecipes().contains(tr.get()) ){
			tu.get().getFavoritedRecipes().remove( tr.get() );
			return true;
		} else {
			return false;
		}

	}

	public boolean registerNewUser( UserModel user ){
		this.dummyUsers.add(user);
		return true;
	}

	public Optional<UserModel> authenticateUserByUsername( String username, String hashedPassword ){
		Optional<UserModel> tu = this.getUserByUsername(username);

		if( tu.isPresent() && tu.get().getPassword().equals(hashedPassword) ){
			return tu;
		} else {
			return Optional.ofNullable(null);
		}

	}

	public Optional<UserModel> authenticateUserByEmail ( String email, String hashedPassword ){
		Optional<UserModel> tu = this.getUserByEmail(email);
		if( tu.isPresent() && tu.get().getPassword().equals(hashedPassword) ){
			return tu;
		} else {
			return Optional.ofNullable(null);
		}

	}

}
