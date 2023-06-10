package com.receitas.app.service;

import com.receitas.app.model.*;
import com.receitas.app.dao.UserDAO;

import java.util.Optional;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;


public class UserService{

	private static UserService instance;
	private static UserDAO userDAO = UserDAO.getInstance();

	private UserService(){
	}


	public static UserService getInstance(){
		if ( instance == null ){
			synchronized( UserService.class ){
				instance = new UserService();
			}
		}

		return instance;
	}

	public Optional<List<RecipeModel>> getUserFavoriteRecipes( String userID ){
		return userDAO.getUserFavoriteRecipes(userID);
	}

	public ServiceAPIResponse saveRecipeAsFavorite( String recipeID, String userID ){

        boolean res = userDAO.saveRecipeAsFavorite(recipeID, userID);
		if ( res == true ){
			return new ServiceAPIResponse("Recipe favorited succesfuly", 200);
		} else {
			return new ServiceAPIResponse("False", 404);
		}
	}

	public ServiceAPIResponse removeRecipeFromFavorites( String recipeID, String userID ){
        boolean res = userDAO.removeRecipeFromFavorites(recipeID, userID);

		if ( res == true ){
			return new ServiceAPIResponse("Recipe removed succesfuly", 200);
		} else {
			return new ServiceAPIResponse("False", 404);
		}

	}

	public ServiceAPIResponse registerNewUserFromJSON( String userJSON ){
		ObjectMapper mapper = new ObjectMapper();
		try {
			UserModel deserializedUser = mapper.readValue(userJSON, UserModel.class);
			userDAO.registerNewUser( deserializedUser );
			return new ServiceAPIResponse( "User registered succesfuly", 201 );

		} catch(UnrecognizedPropertyException e){
			return new ServiceAPIResponse("Invalid JSON", 400 );
		} catch( Exception e) {
			e.printStackTrace();
			return new ServiceAPIResponse("Internal error", 500);
		}
	}

	public ServiceAPIResponse deleteUserByID( String userID ){
        boolean res = userDAO.deleteUserByID(userID);

		if ( res == true ){
			return new ServiceAPIResponse("User removed succesfuly", 202);
		} else {
			return new ServiceAPIResponse("False", 404);
		}

	}

	public Optional<UserModel> authenticateUserByUsername( String username, String hashedPassword ){
		return userDAO.authenticateUserByUsername(username, hashedPassword);
	}

	public Optional<UserModel> authenticateUserByEmail ( String email, String hashedPassword ){
		return userDAO.authenticateUserByEmail(email, hashedPassword);
	}

}
