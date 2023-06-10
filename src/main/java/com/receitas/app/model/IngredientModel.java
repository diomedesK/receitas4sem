package com.receitas.app.model;

import com.fasterxml.jackson.annotation.*;

public class IngredientModel {
  private String name;
  private String id;

  public IngredientModel(){

  }

  public IngredientModel( String name ){
	  this.name = name;
  }


  @JsonCreator
  public IngredientModel( 
		  @JsonProperty("id") String id,
		  @JsonProperty("name") String name )
		  {
	  this.name = name;
	  this.id = id;
  }

  public String getID(){
	return this.id;
  }

  public boolean setID( String id ){
	this.id = id;
	return true;
  }

  public String getName(){
	return this.name;
  }

  public boolean setName( String name ){
	this.name = name;
	return true;
  }

  public String toString(){
	  return String.format("%s( ID: %s, name: %s )", this.getClass().getSimpleName(), this.getID(), this.getName() );
  }

}

