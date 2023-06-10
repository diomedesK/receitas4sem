package com.receitas.app.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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

	public UserModel getRandomUserForTesting(){
        try{
			PreparedStatement statement = connection.prepareStatement("SELECT * from users limit 1");
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                UserModel user = createUserFromResultSet(resultSet);
                return user;
            } 
        } catch (SQLException e) {
            e.printStackTrace();
        }

		return new UserModel();
	}

    public Optional<List<RecipeModel>> getUserFavoriteRecipes(String userID) {
		try {
			PreparedStatement statement = connection.prepareStatement("SELECT * FROM favorite_user_recipes WHERE user_id = ?");

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

    public boolean saveRecipeAsFavorite(String recipeID, String userID) {
        try {
			PreparedStatement statement = connection.prepareStatement("INSERT INTO favorite_user_recipes (user_id, recipe_id) VALUES (?, ?)");

            statement.setString(1, userID);
            statement.setString(2, recipeID);
            int rowsAffected = statement.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean removeRecipeFromFavorites(String recipeID, String userID) {
		try {
			PreparedStatement statement = connection.prepareStatement("DELETE FROM favorite_user_recipes WHERE user_id = ? AND recipe_id = ?");
			statement.setString(1, userID);
			statement.setString(2, recipeID);
			int rowsAffected = statement.executeUpdate();
			return rowsAffected > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
        }
    }

    public boolean registerNewUser(UserModel user) {
        try  {
			PreparedStatement statement = connection.prepareStatement("INSERT INTO users (id, name, username, email, password) VALUES (?, ?, ?, ?, ?)");

			statement.setString(1, user.getID());
			statement.setString(2, user.getName());
			statement.setString(3, user.getUsername());
			statement.setString(4, user.getEmail());
			statement.setString(5, user.getPassword());
			int rowsAffected = statement.executeUpdate();
			return rowsAffected > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean deleteUserByID( String userID ){
		try  {
			PreparedStatement statement = connection.prepareStatement("DELETE FROM users where id = ?");

			statement.setString(1, userID);
			int rowsAffected = statement.executeUpdate();
			return rowsAffected > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

	}

	public Optional<UserModel> authenticateUserByUsername(String username, String hashedPassword) {
		try  {
			PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?");

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

	public Optional<UserModel> authenticateUserByEmail(String email, String hashedPassword) {
		try{
			PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE email = ? AND password = ?");statement.setString(1, email);

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

	private UserModel createUserFromResultSet(ResultSet resultSet) throws SQLException {
		UserModel user = new UserModel();
		user.setID(resultSet.getString("id"));
		user.setName(resultSet.getString("name"));
		user.setUsername(resultSet.getString("username"));
		user.setEmail(resultSet.getString("email"));
		user.setPassword(resultSet.getString("password"));
		return user;
	}


}

