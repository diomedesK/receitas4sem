package com.receitas.app.dummyData;

import java.util.ArrayList;

import com.receitas.app.model.RecipeModel;
import com.receitas.app.model.IngredientModel;

public class DummyRecipes {
    public static ArrayList<RecipeModel> generateDummyRecipes() {
		ArrayList<RecipeModel> dummyRecipes = new ArrayList<>();

		// Recipe 1
		RecipeModel recipe1 = new RecipeModel();
		recipe1.setName("Chocolate Cake");
		recipe1.setDescription("Delicious chocolate cake recipe");
		recipe1.setAuthorID("author1");
		recipe1.setPrepareInMinutes(60);
		recipe1.setCookingMethod("Baking");
		recipe1.addCategory("Desserts");
		recipe1.addCategory("Cakes");
		recipe1.addRating("user1", 5);
		recipe1.addRating("user2", 4);
		recipe1.addInstruction("Preheat the oven to 350°F");
		recipe1.addInstruction("Mix the dry ingredients in a bowl");
		recipe1.addInstruction("Combine the wet ingredients in another bowl");
		recipe1.addIngredient(new IngredientModel("Flour"));
		recipe1.addIngredient(new IngredientModel("Sugar"));
		dummyRecipes.add(recipe1);

		// Recipe 2
		RecipeModel recipe2 = new RecipeModel();
		recipe2.setName("Pasta Carbonara");
		recipe2.setDescription("Classic pasta carbonara recipe");
		recipe2.setAuthorID("author2");
		recipe2.setPrepareInMinutes(30);
		recipe2.setCookingMethod("Boiling");
		recipe2.addCategory("Pasta");
		recipe2.addCategory("Italian");
		recipe2.addRating("user3", 4);
		recipe2.addRating("user4", 4);
		recipe2.addInstruction("Cook the pasta in salted boiling water");
		recipe2.addInstruction("In a pan, cook the pancetta until crispy");
		recipe2.addInstruction("Whisk the eggs, cheese, and black pepper together");
		recipe2.addIngredient(new IngredientModel("Pasta"));
		recipe2.addIngredient(new IngredientModel("Pancetta"));
		dummyRecipes.add(recipe2);

		// Recipe 3
		RecipeModel recipe3 = new RecipeModel();
		recipe3.setName("Chicken Stir-Fry");
		recipe3.setDescription("Quick and easy chicken stir-fry recipe");
		recipe3.setAuthorID("author3");
		recipe3.setPrepareInMinutes(25);
		recipe3.setCookingMethod("Stir-frying");
		recipe3.addCategory("Chicken");
		recipe3.addCategory("Asian");
		recipe3.addRating("user5", 4);
		recipe3.addRating("user6", 3);
		recipe3.addInstruction("Heat oil in a pan over high heat");
		recipe3.addInstruction("Add chicken and stir-fry until cooked");
		recipe3.addInstruction("Add vegetables and sauce, and cook for a few minutes");
		recipe3.addIngredient(new IngredientModel("Chicken"));
		recipe3.addIngredient(new IngredientModel("Bell Peppers"));
		dummyRecipes.add(recipe3);

		// Recipe 4
		RecipeModel recipe4 = new RecipeModel();
		recipe4.setName("Caprese Salad");
		recipe4.setDescription("Classic Caprese salad recipe");
		recipe4.setAuthorID("author4");
		recipe4.setPrepareInMinutes(10);
		recipe4.setCookingMethod("No-cook");
		recipe4.addCategory("Salads");
		recipe4.addCategory("Italian");
		recipe4.addRating("user7", 5);
		recipe4.addRating("user8", 4);
		recipe4.addInstruction("Slice tomatoes and mozzarella cheese");
		recipe4.addInstruction("Arrange tomato and mozzarella slices on a plate");
		recipe4.addInstruction("Drizzle with olive oil and balsamic glaze");
		recipe4.addIngredient(new IngredientModel("Tomatoes"));
		recipe4.addIngredient(new IngredientModel("Mozzarella Cheese"));
		dummyRecipes.add(recipe4);

		// Recipe 5
		RecipeModel recipe5 = new RecipeModel();
		recipe5.setName("Beef Tacos");
		recipe5.setDescription("Homemade beef tacos with salsa");
		recipe5.setAuthorID("author5");
		recipe5.setPrepareInMinutes(40);
		recipe5.setCookingMethod("Stovetop");
		recipe5.addCategory("Tacos");
		recipe5.addCategory("Mexican");
		recipe5.addRating("user9", 4);
		recipe5.addRating("user10", 5);
		recipe5.addInstruction("Cook ground beef in a skillet until browned");
		recipe5.addInstruction("Season with taco seasoning and simmer");
		recipe5.addInstruction("Warm up tortillas and assemble tacos");
		recipe5.addIngredient(new IngredientModel("Ground Beef"));
		recipe5.addIngredient(new IngredientModel("Tortillas"));
		dummyRecipes.add(recipe5);

		// Recipe 6
		RecipeModel recipe6 = new RecipeModel();
		recipe6.setName("Vegetable Curry");
		recipe6.setDescription("Flavorful vegetable curry recipe");
		recipe6.setAuthorID("author6");
		recipe6.setPrepareInMinutes(50);
		recipe6.setCookingMethod("Simmering");
		recipe6.addCategory("Curry");
		recipe6.addCategory("Vegetarian");
		recipe6.addRating("user11", 4);
		recipe6.addRating("user12", 4);
		recipe6.addInstruction("Sauté onions, garlic, and spices in a pot");
		recipe6.addInstruction("Add vegetables, coconut milk, and simmer until tender");
		recipe6.addInstruction("Serve with rice or naan bread");
		recipe6.addIngredient(new IngredientModel("Mixed Vegetables"));
		recipe6.addIngredient(new IngredientModel("Coconut Milk"));
		dummyRecipes.add(recipe6);

		// Recipe 7
		RecipeModel recipe7 = new RecipeModel();
		recipe7.setName("Mushroom Risotto");
		recipe7.setDescription("Creamy mushroom risotto recipe");
		recipe7.setAuthorID("author7");
		recipe7.setPrepareInMinutes(45);
		recipe7.setCookingMethod("Stovetop");
		recipe7.addCategory("Risotto");
		recipe7.addCategory("Italian");
		recipe7.addRating("user13", 5);
		recipe7.addRating("user14", 4);
		recipe7.addInstruction("Sauté mushrooms and onions in a pan");
		recipe7.addInstruction("Add Arborio rice and cook until translucent");
		recipe7.addInstruction("Gradually add vegetable broth and stir until absorbed");
		recipe7.addIngredient(new IngredientModel("Mushrooms"));
		recipe7.addIngredient(new IngredientModel("Arborio Rice"));
		dummyRecipes.add(recipe7);

		// Recipe 8
		RecipeModel recipe8 = new RecipeModel();
		recipe8.setName("Greek Salad");
		recipe8.setDescription("Refreshing Greek salad recipe");
		recipe8.setAuthorID("author8");
		recipe8.setPrepareInMinutes(15);
		recipe8.setCookingMethod("No-cook");
		recipe8.addCategory("Salads");
		recipe8.addCategory("Greek");
		recipe8.addRating("user15", 4);
		recipe8.addRating("user16", 3);
		recipe8.addInstruction("Chop cucumbers, tomatoes, and onions");
		recipe8.addInstruction("Combine with feta cheese and olives in a bowl");
		recipe8.addInstruction("Drizzle with olive oil and lemon juice");
		recipe8.addIngredient(new IngredientModel("Cucumbers"));
		recipe8.addIngredient(new IngredientModel("Feta Cheese"));
		dummyRecipes.add(recipe8);

		return dummyRecipes;
    }

}

