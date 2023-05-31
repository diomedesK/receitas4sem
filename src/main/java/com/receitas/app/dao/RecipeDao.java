package com.receitas.app.dao;

import com.receitas.app.model.Recipe;
import java.util.List;
import java.util.Arrays;

// the dao uses a singleton pattern
public class RecipeDao {

  private List<Recipe> recipes = Arrays.asList(
	  new Recipe("abc", "Bolo de fubá", "delicioso acredite em mim"),
	  new Recipe("wyz", "Fubá de bolo", "mim em acredite delicioso")
	  );

  private static RecipeDao currentInstance = null;

  private RecipeDao(){

  }

  public static RecipeDao getInstance(){
	if( currentInstance == null ){
	  currentInstance = new RecipeDao();
	} 

	return currentInstance;

  }

}
