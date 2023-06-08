package com.receitas.app;

import com.receitas.app.model.RecipeModel;
import com.receitas.app.model.IngredientModel;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashSet;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.receitas.app.utils.MyLogger;


public class RecipeModelTest {

  private RecipeModel recipe;

  @BeforeEach
  public void setUp() {
	recipe = new RecipeModel();
  }

  @Test
  public void testAddCategory_ReturnsTrue() {
	assertTrue(recipe.addCategory("Breakfast"));
	assertEquals(1, recipe.getCategories().size());
	assertTrue(recipe.getCategories().contains("Breakfast"));
  }

  @Test
  public void testGetCategories() {
	recipe.addCategory("Breakfast");
	recipe.addCategory("Dinner");
	recipe.addCategory("Dinner");

	LinkedHashSet<String> categories = recipe.getCategories();

	assertEquals(2, categories.size());
	assertTrue(categories.contains("Breakfast"));
	assertTrue(categories.contains("Dinner"));
  }
  @Test

  public void testAddRating_ReturnsTrue() {
	assertTrue(recipe.addRating("user1", 4));
	assertEquals(1, recipe.getRatings().size());
	assertEquals(4, recipe.getRatings().get("user1"));
  }

  @Test
  public void testGetRatings() {
	recipe.addRating("user1", 4);
	recipe.addRating("user2", 5);
	HashMap<String, Integer> ratings = recipe.getRatings();
	assertEquals(2, ratings.size());
	assertEquals(4, ratings.get("user1"));
	assertEquals(5, ratings.get("user2"));
  }

  @Test
  public void testAddInstruction_ReturnsTrue() {
	assertTrue(recipe.addInstruction("Step 1: Preheat the oven"));
	assertEquals(1, recipe.getInstructions().size());
	assertEquals("Step 1: Preheat the oven", recipe.getInstructions().get(0));
  }

  @Test
  public void testAddIngredient() {
    IngredientModel ingredient1 = new IngredientModel("Flour");
    IngredientModel ingredient2 = new IngredientModel("Sugar");
    
    boolean result1 = recipe.addIngredient(ingredient1);
    boolean result2 = recipe.addIngredient(ingredient2);
    
    assertTrue(result1);
    assertTrue(result2);
    
    LinkedHashSet<IngredientModel> ingredients = recipe.getIngredients();
    
    assertTrue(ingredients.contains(ingredient1));
    assertTrue(ingredients.contains(ingredient2));
    assertEquals(2, ingredients.size());
  }
  
  @Test
  public void testGetIngredients() {
    IngredientModel ingredient1 = new IngredientModel("Salt");
    IngredientModel ingredient2 = new IngredientModel("Pepper");
    
    recipe.addIngredient(ingredient1);
    recipe.addIngredient(ingredient2);
    
    LinkedHashSet<IngredientModel> ingredients = recipe.getIngredients();
    
    assertTrue(ingredients.contains(ingredient1));
    assertTrue(ingredients.contains(ingredient2));
    assertEquals(2, ingredients.size());
  }

}
