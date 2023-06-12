package com.receitas.app.service;

import com.receitas.app.model.*;
import com.receitas.app.dao.UserDAO;

import java.util.Optional;
import java.util.List;
import java.util.UUID;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.receitas.app.utils.MyLogger;
import com.receitas.app.utils.Hasher;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.core.JsonProcessingException;

public class UserService{

	private static UserService instance;
	private static UserDAO userDAO = UserDAO.getInstance();
	private static ObjectMapper mapper = new ObjectMapper();

	private UserService(){
	}

	private static ServiceAPIResponse invalidJSONResponse = new ServiceAPIResponse("Invalid JSON", 400);

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

	public Optional<String> getUserIDBySessionToken( String token ){
		return userDAO.getUserIDBySessionToken(token);
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

		try {
			UserModel deserializedUser = mapper.readValue(userJSON, UserModel.class);
			System.out.println(mapper.writeValueAsString(deserializedUser));

			try{
				deserializedUser.setPassword( Hasher.hashStringWithSHA256( deserializedUser.getPassword() ) );
			} catch( NullPointerException npe ){
				return invalidJSONResponse;
			}
			Optional<String> generatedUserID = userDAO.registerNewUser( deserializedUser );

			if( generatedUserID.isPresent() ){
				MyLogger.info("Generated user ID: " +generatedUserID.get());
				return new ServiceAPIResponse( generatedUserID.get(), 201 );

			} else {
				return invalidJSONResponse;
			}

		} catch(UnrecognizedPropertyException e){
				return invalidJSONResponse;
		} catch ( JsonMappingException jme ){
				return invalidJSONResponse;
		} catch ( JsonProcessingException  jpe){
				return invalidJSONResponse;
		} catch( RuntimeException e ){
			if ( e.getCause() instanceof java.sql.SQLIntegrityConstraintViolationException ){
				return new ServiceAPIResponse( e.getCause().getMessage(), 400);

			} else {
				e.printStackTrace();
				return new ServiceAPIResponse("Unexpected", 317);
			}
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

	public Optional<SessionData> authenticateUserFromJSON( String userJSON ){
		try {
			UserModel deserializedUser = mapper.readValue( userJSON, UserModel.class);
			if( deserializedUser.getEmail() != null ){
				return authenticateUserByEmail( deserializedUser.getEmail(), deserializedUser.getPassword() );
			} else if ( deserializedUser.getUsername() != null ){
				return authenticateUserByUsername( deserializedUser.getUsername(), deserializedUser.getPassword() );
			} 
		} catch( Exception e ){
			e.printStackTrace();
		} 

		return Optional.empty();
	}

	public Optional<SessionData> authenticateUserByUsername( String username, String password ){
		String hashedPassword = Hasher.hashStringWithSHA256(password);

		Optional<UserModel> user = userDAO.authenticateUserByUsername(username, hashedPassword);
		if ( user.isPresent() ){
			return finalizeAuth(user.get());
		} else {
			return Optional.empty();
		}
	}

	public Optional<SessionData> authenticateUserByEmail ( String email, String password ){
		String hashedPassword = Hasher.hashStringWithSHA256(password);

		Optional<UserModel> user = userDAO.authenticateUserByEmail(email, hashedPassword);
		if ( user.isPresent() ){
			return finalizeAuth(user.get());
		} else {
			return Optional.empty();
		}

	}

	public Optional<SessionData> finalizeAuth( UserModel user  ){
		SessionData sd = new SessionData( user, UUID.randomUUID().toString() );
		LocalDateTime expiration = LocalDateTime.now().plus(1, ChronoUnit.HOURS); // expires in 1 hours

		boolean wasSessionSaved = userDAO.saveUserSession( user.getID(), sd.getSessionToken(), expiration );

		if( wasSessionSaved ){
			return Optional.of(sd);
		} else {
			return Optional.empty();
		}

	}
	
	public Optional<UserModel> getUserDataFromSessionToken( String sessionToken ){
		return userDAO.getUserDataFromSessionToken(sessionToken);
	}

	public boolean removeUserSession( String sessionToken ){
		return userDAO.removeUserSession(sessionToken);
	}

}
