package com.receitas.app.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;

import java.time.LocalDateTime;

import com.receitas.app.model.*;

public class UserDAO extends MySQLDAO implements UserDAOInterface  {
    private static UserDAO instance;

	Connection connection;
    private UserDAO() {
		try {
			connection = getConnection();
		} catch(Exception e){
			e.printStackTrace();
		}
    }

    public static UserDAO getInstance( ) {
        if (instance == null) {
            synchronized (UserDAO.class) {
                instance = new UserDAO();
            }
        }

        return instance;
    }

	@Override
    public Optional<List<RecipeModel>> getUserFavoriteRecipes(String userID) {
		try ( PreparedStatement statement = connection.prepareStatement("SELECT * FROM favorite_user_recipes WHERE user_id = ?"); ) {

			statement.setString(1, userID);
			ResultSet resultSet = statement.executeQuery();

			List<RecipeModel> recipes = new ArrayList<>();
			while (resultSet.next()) {
				String recipeID = resultSet.getString("recipe_id");
				Optional<RecipeModel> recipe = RecipeDAO.getInstance().getRecipeByID(recipeID);
				recipe.ifPresent(recipes::add);
			}
			return Optional.of(recipes);
		} catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

	@Override
    public boolean saveRecipeAsFavorite(String recipeID, String userID) {
        try ( PreparedStatement statement = connection.prepareStatement("INSERT IGNORE INTO favorite_user_recipes (user_id, recipe_id) VALUES (?, ?)");  ) {

            statement.setString(1, userID);
            statement.setString(2, recipeID);
            int rowsAffected = statement.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

	@Override
    public boolean removeRecipeFromFavorites(String recipeID, String userID) {
		try ( PreparedStatement statement = connection.prepareStatement("DELETE FROM favorite_user_recipes WHERE user_id = ? AND recipe_id = ?"); ) {

			
			statement.setString(1, userID);
			statement.setString(2, recipeID);
			int rowsAffected = statement.executeUpdate();
			System.out.println(rowsAffected);
			return rowsAffected > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
        }
    }

	@Override
    public Optional<String> registerNewUser(UserModel user){
        try ( PreparedStatement statement = connection.prepareStatement("INSERT INTO users (name, username, email, hashed_password) VALUES (?, ?, ?, ?)",
				Statement.RETURN_GENERATED_KEYS)) {

			statement.setString(1, user.getName());
			statement.setString(2, user.getUsername());
			statement.setString(3, user.getEmail());

			statement.setString(4, user.getPassword()); //hash it
			int rowsAffected = statement.executeUpdate();

			ResultSet generatedKeys = statement.getGeneratedKeys();
			generatedKeys.next();
			int generatedID = generatedKeys.getInt(1);
			generatedKeys.close();

			return Optional.of( "" + generatedID );

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public boolean deleteUserByID( String userID ){

		String[] neededStatements = {
			"DELETE FROM users WHERE id = ?;"
		};

		try{
			for( String s : neededStatements ){
				PreparedStatement statement = connection.prepareStatement(s);
				statement.setString(1, userID);

				int rowsAffected = statement.executeUpdate();
				statement.close();
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

		return true;

	}

	@Override
	public Optional<UserModel> authenticateUserByUsername(String username, String hashedPassword) {
		try ( PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE username = ? AND hashed_password = ?"); ) {

			statement.setString(1, username);
			statement.setString(2, hashedPassword);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				UserModel user = createUserFromResultSet(resultSet);
				return Optional.of(user);
			} else {
				return Optional.empty();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return Optional.empty();
		}
	}

	@Override
	public Optional<UserModel> authenticateUserByEmail(String email, String hashedPassword) {
		try( PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE email = ? AND hashed_password = ?"); ){
			statement.setString(1, email);
			statement.setString(2, hashedPassword);

			ResultSet resultSet = statement.executeQuery();

			if (resultSet.next()) {
				UserModel user = createUserFromResultSet(resultSet);
				return Optional.of(user);
			} else {
				return Optional.empty();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return Optional.empty();
		}
	}

	public boolean hasUserFavoritedRecipe( String userID, String recipeID ){
		try( PreparedStatement statement = connection.prepareStatement("SELECT COUNT(1) as c FROM favorite_user_recipes WHERE user_id = ? and recipe_id = ?;") ){
			statement.setString(1, userID);
			statement.setString(2, recipeID);
			
			ResultSet resultSet = statement.executeQuery();

			if (resultSet.next()){
				return resultSet.getInt("c") == 1;
			} 
		} catch ( SQLException e ){
			e.printStackTrace();
		}

		return false;
	}

	public boolean hasUserFavoritedRecipeFromSessionToken( String sessionToken, String recipeID ){
		try( PreparedStatement statement = connection.prepareStatement("SELECT COUNT(1) as c FROM  ( favorite_user_recipes f join sessions s on f.user_id = s.user_id ) where s.session_token = ?  and  f.recipe_id = ?") ){
			statement.setString(1, sessionToken);
			statement.setString(2, recipeID);
			
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()){
				return resultSet.getInt("c") == 1;
			} 

		} catch ( SQLException e ){
			e.printStackTrace();
		}

		return false;
	}


	public Optional<UserModel> getUserDataFromSessionToken( String token ){
		try( PreparedStatement statement = connection.prepareStatement("select u.* from ( sessions s join users u on s.user_id = u.id ) where session_token = ? and expiration > now()") ){
			statement.setString(1, token);
			
			ResultSet resultSet = statement.executeQuery();

			if (resultSet.next()){
				UserModel user = createUserFromResultSet(resultSet);
				return Optional.of(user);
			} else{
				return Optional.empty();
			}

		} catch ( SQLException e ){
			e.printStackTrace();
			return Optional.empty();
		}

	}

	public Optional<String> getUserIDBySessionToken( String token ){
		try( PreparedStatement statement = connection.prepareStatement("select user_id from sessions where session_token = ? and expiration > now()") ){
			statement.setString(1, token);
			
			ResultSet resultSet = statement.executeQuery();

			if (resultSet.next()){
				return Optional.of( resultSet.getString("user_id") );
			} else{
				return Optional.empty();
			}

		} catch ( SQLException e ){
			e.printStackTrace();
			return Optional.empty();
		}

	}

	public boolean saveUserSession( String userID, String sessionToken, LocalDateTime expirationTimestamp ){
		try (PreparedStatement statement = connection.prepareStatement(
					"INSERT INTO sessions (user_id, session_token, expiration) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE session_token = VALUES(`session_token`), expiration = VALUES(`expiration`)")) {
			statement.setString(1, userID);
			statement.setString(2, sessionToken);
			statement.setTimestamp(3, Timestamp.valueOf(expirationTimestamp));
			statement.executeUpdate();

			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	public boolean removeUserSession( String sessionToken ){
        try ( PreparedStatement statement = connection.prepareStatement("DELETE FROM sessions where session_token = ?");  ) {
            statement.setString(1, sessionToken);
            int rowsAffected = statement.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

	}

	private UserModel createUserFromResultSet(ResultSet resultSet) throws SQLException {
		UserModel user = new UserModel();
		user.setID(resultSet.getString("id"));
		user.setName(resultSet.getString("name"));
		user.setUsername(resultSet.getString("username"));
		user.setEmail(resultSet.getString("email"));
		user.setPassword(resultSet.getString("hashed_password"));

		Optional<List<RecipeModel>> favoritedRecipes = getUserFavoriteRecipes( user.getID() );

		if ( favoritedRecipes.isPresent() ){
			favoritedRecipes.get().forEach( recipe -> {
				user.addFavoriteRecipe( recipe );
			});
		}

		return user;
	}


}

