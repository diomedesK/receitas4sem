package com.receitas.app.dao;

import com.receitas.app.model.RecipeModel;
import com.receitas.app.model.IngredientModel;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.receitas.app.utils.MyLogger;


public class RecipeDAO extends MySQLDAO implements RecipeDAOInterface {

    private static RecipeDAO instance;

	Connection connection;

    private RecipeDAO() {
		try {
			connection = getConnection();
		} catch(Exception e){
			e.printStackTrace();
		}
    }

    public static RecipeDAO getInstance() {
        if (instance == null) {
            synchronized (RecipeDAO.class) {
                instance = new RecipeDAO();
            }
        }

        return instance;
    }

	private RecipeModel createRecipeFromResultSet(ResultSet resultSet) throws SQLException {
		RecipeModel recipe = new RecipeModel();

		recipe.setID(resultSet.getString("id"));
		recipe.setName(resultSet.getString("name"));
		recipe.setAuthorID(resultSet.getString("author_id"));
		recipe.setDescription(resultSet.getString("description"));
		recipe.setPrepareInMinutes(resultSet.getInt("prepare_in_minutes"));
		recipe.setCookingMethod(resultSet.getString("cooking_method"));

		recipe.setAdditionalInfo( resultSet.getString("additional_info") );

		// Get categories
		PreparedStatement getRecipeCategories = connection.prepareStatement("select c.name from ( ( recipes r join recipe_category rc on r.id = rc.recipe_id  ) join categories c on rc.category_id = c.id) where r.id = ?");
		getRecipeCategories.setString(1, recipe.getID());
		ResultSet recipeCategories = getRecipeCategories.executeQuery();

		// Get ingredients
		PreparedStatement getRecipeIngredients = connection.prepareStatement("select i.* from ( ( recipes r join recipe_ingredient ri on ri.recipe_id = r.id ) join ingredients i on ri.ingredient_id = i.id ) where r.id = ?");

		getRecipeIngredients.setString(1, recipe.getID());
		ResultSet recipeIngredients = getRecipeIngredients.executeQuery();

		// Get ratings
		PreparedStatement getRecipeRatings = connection.prepareStatement("select * from recipe_rating rr where rr.recipe_id = ?");
		getRecipeRatings.setString(1, recipe.getID());
		ResultSet recipeRatings = getRecipeRatings.executeQuery();

		// Get instructions
		PreparedStatement getRecipeInstructions = connection.prepareStatement("select step, description from recipe_instructions where recipe_id = ?");
		getRecipeInstructions.setString(1, recipe.getID());
		ResultSet recipeInstructions = getRecipeInstructions.executeQuery();

		// Get accesses in the last 7 days
		PreparedStatement getRecipeAccesses = connection.prepareStatement("SELECT COUNT(*) as accesses FROM recipe_accesses WHERE recipe_id = ?");
		getRecipeAccesses.setString(1, recipe.getID());
		ResultSet recipeAccesses = getRecipeAccesses.executeQuery();

		recipeAccesses.next();
		recipe.setAccessesWithinLast7Days( recipeAccesses.getInt("accesses") );

		while( recipeInstructions.next() ){
			recipe.addInstruction( recipeInstructions.getInt("step"), recipeInstructions.getString("description") );
		}

		while( recipeIngredients.next() ){
			recipe.addIngredient( new IngredientModel( recipeIngredients.getString("id"), recipeIngredients.getString("name") ) );
		}

		while( recipeCategories.next() ){
			recipe.addCategory(  recipeCategories.getString("name")  );
		}

		while( recipeRatings.next() ){
			recipe.addRating( recipeRatings.getString("user_id"), recipeRatings.getInt("rating") );
		}
		
		return recipe;
	}

	public boolean clearAccessesOfRecipeFromDaysAgo( String recipeID, int olderThanDays){
        try ( 
				PreparedStatement clearAccessesStatement = connection.prepareStatement(
					"DELETE FROM recipe_accesses WHERE recipe_id = ? AND accessed_at < DATE_SUB(CURRENT_TIMESTAMP, INTERVAL ? DAY);");
				){

            clearAccessesStatement.setString(1, recipeID);
            clearAccessesStatement.setInt(2, olderThanDays);

            int rowsAffected = clearAccessesStatement.executeUpdate();
			return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

		return false;
	}

	public boolean clearAccessesOfAllRecipesFromDaysAgo( int olderThanDays){
        try ( 
				PreparedStatement clearAccessesStatement = connection.prepareStatement(
					"DELETE FROM recipe_accesses WHERE accessed_at < DATE_SUB(CURRENT_TIMESTAMP, INTERVAL ? DAY);");
				){

            clearAccessesStatement.setInt(1, olderThanDays);

            int rowsAffected = clearAccessesStatement.executeUpdate();
			return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

		return false;
	}

	public boolean addAccess( String recipeID ){
        try ( 
				PreparedStatement addAccessStatement = connection.prepareStatement(
					"INSERT INTO recipe_accesses (recipe_id, accessed_at) VALUES (?, CURRENT_TIMESTAMP)");
				){

            addAccessStatement.setString(1, recipeID);
            int rowsAffected = addAccessStatement.executeUpdate();

			return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

		return false;
	}

    public Optional<RecipeModel> getRecipeByID(String recipeID) {
        try ( PreparedStatement statement = connection.prepareStatement("SELECT * FROM recipes WHERE id = ?")) {

            statement.setString(1, recipeID);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return Optional.of( createRecipeFromResultSet(resultSet) );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }


    public List<RecipeModel> getRecipesByAuthorID(String authorID) {
        List<RecipeModel> recipes = new ArrayList<>();
        try (
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM recipes WHERE author_id = ?")) {

            statement.setString(1, authorID);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
				recipes.add( createRecipeFromResultSet( resultSet ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return recipes;
    }

	public List<RecipeModel> getRandomRecipes(int count){
		List<RecipeModel> recipes = new ArrayList<>();

		try (  PreparedStatement statement = connection.prepareStatement("SELECT * from recipes ORDER BY RAND() LIMIT ?") ) {
			statement.setInt(1, count);
			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {
				recipes.add( createRecipeFromResultSet( resultSet ));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return recipes;

	}

	public List<RecipeModel> getPopularRecipes(int count) {
		List<RecipeModel> recipes = new ArrayList<>();

		try (  PreparedStatement statement = connection.prepareStatement("SELECT * FROM ( ( SELECT recipe_id, COUNT(*) AS accesses FROM recipe_accesses GROUP BY recipe_id ORDER BY COUNT(*) DESC LIMIT ? ) ra JOIN recipes r ON ra.recipe_id = r.id ) ORDER BY accesses DESC") ) {
			statement.setInt(1, count);
			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {
				recipes.add( createRecipeFromResultSet( resultSet ));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return recipes;
	}

	public List<RecipeModel> getRecipesByName(String recipeName) {
		List<RecipeModel> recipes = new ArrayList<>();
		try ( PreparedStatement statement = connection.prepareStatement("SELECT * FROM recipes WHERE name LIKE ?")) {

			String likePattern = "%" + recipeName + "%";
			statement.setString(1, likePattern);
			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {
				recipes.add( createRecipeFromResultSet( resultSet ));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return recipes;
	}

	public List<RecipeModel> getRecipesByIngredients(String... ingredients) {
		List<RecipeModel> recipes = new ArrayList<>();
		try ( PreparedStatement statement = connection.prepareStatement("SELECT r.* FROM ( ( recipes r JOIN recipe_ingredient ri ON r.id = ri.recipe_id ) JOIN ingredients i ON ri.ingredient_id = i.id ) WHERE i.name LIKE ?")) {

			for (String ingredient : ingredients) {
				statement.setString(1, "%" + ingredient + "%");
				ResultSet resultSet = statement.executeQuery();

				while (resultSet.next()) {
					recipes.add( createRecipeFromResultSet( resultSet ));
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return recipes;
	}

	public List<RecipeModel> getRecipesByCategories(String... categories) {
		List<RecipeModel> recipes = new ArrayList<>();

		try ( PreparedStatement statement = connection.prepareStatement("SELECT r.* FROM ( ( recipe_category rc join recipes r on rc.recipe_id = r.id) join categories c on rc.category_id = c.id ) where c.name LIKE ?")) {

			for (String category : categories) {
				statement.setString(1, "%" + category + "%");
				ResultSet resultSet = statement.executeQuery();

				while (resultSet.next()) {
					recipes.add(  createRecipeFromResultSet( resultSet ) );
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return recipes;
	}

    public boolean deleteRecipeByID(String id) {
		MyLogger.info("Deleting recipe of ID " + id);

		String[] neededStatements = {
			"DELETE FROM recipes WHERE id = ?"
		};

        try{
			for ( String s : neededStatements ){
				PreparedStatement statement = connection.prepareStatement(s);
				statement.setString(1, id);
				statement.executeUpdate();
			}
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }


    public Optional<String> saveRecipe(RecipeModel recipe) {
        try (PreparedStatement insertRecipeStatement = connection.prepareStatement(
                "INSERT INTO recipes (name, author_id, description, prepare_in_minutes, cooking_method, additional_info) VALUES (?, ?, ?, ?, ?, ?)",
				Statement.RETURN_GENERATED_KEYS)) 
		{
            insertRecipeStatement.setString(1, recipe.getName());
            insertRecipeStatement.setString(2, recipe.getAuthorID());
            insertRecipeStatement.setString(3, recipe.getDescription());
            insertRecipeStatement.setInt(4, recipe.getPrepareInMinutes());
            insertRecipeStatement.setString(5, recipe.getCookingMethod());
            insertRecipeStatement.setString(6, recipe.getAdditionalInfo());
            insertRecipeStatement.executeUpdate();

			ResultSet generatedKeys = insertRecipeStatement.getGeneratedKeys();
			generatedKeys.next();
			int generatedID = generatedKeys.getInt(1);
			recipe.setID(""+ generatedID);
			MyLogger.info("Generated recipe ID: " + generatedID);
			generatedKeys.close();

        } catch (SQLException e) {
			MyLogger.error("insertRecipeStatement");
            e.printStackTrace();
            return Optional.empty();
        }

        // Save categories ( no creation of new categories )
        try (PreparedStatement insertCategoryStatement = connection.prepareStatement(
                "INSERT INTO recipe_category (recipe_id, category_id) VALUES (?, ?)")) {
            for (String category : recipe.getCategories()) {
                try (PreparedStatement getCategoryID = connection.prepareStatement("SELECT id FROM categories WHERE name = ?")) {
                    getCategoryID.setString(1, category);
                    try (ResultSet categoryIDResult = getCategoryID.executeQuery()) {
                        if (categoryIDResult.next()) {
                            String categoryID = categoryIDResult.getString("id");
                            insertCategoryStatement.setString(1, recipe.getID());
                            insertCategoryStatement.setString(2, categoryID);
                            insertCategoryStatement.addBatch();
                        }
                    }
                }
            }

            insertCategoryStatement.executeBatch();

        } catch (SQLException e) {
			MyLogger.error("insertCategoryStatement");
            e.printStackTrace();
            return Optional.empty();
        }


        // Save ingredients
        try (PreparedStatement insertRecipeIngredientStatement = connection.prepareStatement(
                "INSERT INTO recipe_ingredient (recipe_id, ingredient_id) VALUES (?, ?)");
			PreparedStatement insertIngredientStatement = connection.prepareStatement(
					"INSERT IGNORE INTO ingredients (name) VALUES (?)")
			) {
            for (IngredientModel ingredient : recipe.getIngredients()) {
				insertIngredientStatement.setString(1, ingredient.getName());
				insertIngredientStatement.executeUpdate();

                try (PreparedStatement getIngredientID = connection.prepareStatement("SELECT id FROM ingredients WHERE name = ?")) {
                    getIngredientID.setString(1, ingredient.getName());
                    try (ResultSet ingredientIDResult = getIngredientID.executeQuery()) {
                        if (ingredientIDResult.next()) {
                            String ingredientID = ingredientIDResult.getString("id");
                            insertRecipeIngredientStatement.setString(1, recipe.getID());
                            insertRecipeIngredientStatement.setString(2, ingredientID);
                            insertRecipeIngredientStatement.addBatch();
                        }
                    }
                }
            }

            insertRecipeIngredientStatement.executeBatch();
        } catch (SQLException e) {
			MyLogger.error("insertRecipeIngredientStatement");
            e.printStackTrace();
            return Optional.empty();
        }

        // Save instructions
        try (PreparedStatement insertInstructionStatement = connection.prepareStatement(
                "INSERT INTO recipe_instructions (recipe_id, step, description) VALUES (?, ?, ?)")) {
            for (int step : recipe.getInstructions().keySet()) {
                String description = recipe.getInstructions().get(step);
                insertInstructionStatement.setString(1, recipe.getID());
                insertInstructionStatement.setInt(2, step);
                insertInstructionStatement.setString(3, description);
                insertInstructionStatement.addBatch();
            }
            insertInstructionStatement.executeBatch();
        } catch (SQLException e) {
			MyLogger.error("insertInstructionStatement");
            e.printStackTrace();
            return Optional.empty();
        }

        return Optional.of( recipe.getID() );
    }

	public boolean addRating(String recipeID, String userID, int rating) {
		try ( PreparedStatement statement = connection.prepareStatement("INSERT INTO `recipe_rating`(`recipe_id`, `user_id`, `rating`) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE rating=VALUES(rating) ")){

			statement.setString(1, recipeID);
			statement.setString(2, userID);
			statement.setInt(3, rating);

			int rowsAffected = statement.executeUpdate();

			return rowsAffected > 0;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	public Optional<Integer> getRating(String recipeID, String userID) {
		try ( PreparedStatement statement = connection.prepareStatement("SELECT rating from recipe_rating where recipe_id = ? and user_id = ?")){

			statement.setString(1, recipeID);
			statement.setString(2, userID);

			ResultSet res = statement.executeQuery();

			if( res.next() ){
				int rating = res.getInt("rating");
				return Optional.of(rating);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return Optional.empty();
	}


}
