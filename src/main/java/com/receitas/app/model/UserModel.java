package com.receitas.app.model;

import java.util.ArrayList;
import java.util.List;

/*
class Usuario:
  id: 6 chars
  nome: texto
  username: texto
  email: texto

  favoritos[<Receita>]

  adicionarFavorito( Receita r )
  removerFavorito( Receita r )
  obterFavoritos()
  */

public class UserModel  {

	private String name;
	private String id;
	private String username;
	private String email;

	private List<RecipeModel> favoriteRecipes;

	public UserModel(){
		this.favoriteRecipes = new ArrayList<>();
	}

	public boolean setID( String id ){
		this.id = id;
		return true;
	}

	public String getID(){
		return this.id;
	}

	public boolean setEmail( String email ){
		this.email = email;
		return true;
	}

	public String getEmail(){
		return this.email;
	}

	public String getUsername(){
		return this.username;
	}

	public boolean setUsername( String username ){
		this.username = username;
		return true;
	}

	public String getName(){
		return this.name;
	}

	public boolean setName( String name ){
		this.name = name;
		return true;
	}


	public boolean addFavoriteRecipe(  RecipeModel recipe ){
		this.favoriteRecipes.add(recipe);
		return true;
	}

	public boolean removeFavoritedRecipe(  RecipeModel targetRecipe ){
		if ( favoriteRecipes.indexOf( targetRecipe ) != -1 ){
			this.favoriteRecipes.remove( targetRecipe );
			return true;
		} else {
			return false;
		}
	}

	public List<RecipeModel> getFavoritedRecipes(){
		return this.favoriteRecipes;
	}

}
