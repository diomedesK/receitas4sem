package com.receitas.app.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.*;

@JsonRootName("user")
@JsonPropertyOrder({ "id", "name", "usernam", "email" })
public class UserModel  {

	private static int nextIDForDummying = 0;

	private String name;
	private String id;
	private String username;
	private String email;
	private String password;

	private List<RecipeModel> favoritedRecipes;

	public UserModel(){
		this.favoritedRecipes = new ArrayList<>();
		this.id = "" + nextIDForDummying++;
	}


	@JsonCreator
	public UserModel(
			@JsonProperty("id") String id,
			@JsonProperty("name") String name,
			@JsonProperty("username") String username,
			@JsonProperty("email") String email,
			@JsonProperty("password") String password,
			@JsonProperty("favoritedRecipes") ArrayList<RecipeModel> favoritedRecipes
			)
	{
		this.name = name;
		this.username = username;
		this.email = email;
		this.password = password;
		this.favoritedRecipes = favoritedRecipes;
		this.id = id;
	}

	public boolean setID(String id){
		this.id = id;
		return true;
	}

	public String getID(){
		return this.id;
	}

	public String getName(){
		return this.name;
	}

	public boolean setName( String name ){
		this.name = name;
		return true;
	}

	public boolean setEmail( String email ){
		this.email = email;
		return true;
	}

	public String getEmail(){
		return this.email;
	}

	public boolean setUsername( String username ){
		this.username = username;
		return true;
	}

	public String getUsername(){
		return this.username;
	}

	public boolean setPassword(String password){
		this.password = password;
		return true;
	}

	public String getPassword(){
		return this.password;
	}


	public boolean addFavoriteRecipe(  RecipeModel recipe ){
		this.favoritedRecipes.add(recipe);
		return true;
	}

	public boolean removeFavoritedRecipe(  RecipeModel targetRecipe ){
		if ( favoritedRecipes.indexOf( targetRecipe ) != -1 ){
			this.favoritedRecipes.remove( targetRecipe );
			return true;
		} else {
			return false;
		}
	}

	public boolean setFavoritedRecipes( List<RecipeModel> recipes ){
		this.favoritedRecipes = recipes;
		return true;
	}

	public List<RecipeModel> getFavoritedRecipes(){
		return this.favoritedRecipes;
	}


}
