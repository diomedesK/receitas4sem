package com.receitas.app.model;

import com.fasterxml.jackson.annotation.*;

public class IngredientModel {
  private String name;

  public IngredientModel(){
  }

  public IngredientModel( 
		  @JsonProperty("name")
		  String name ){
	  this.name = name;
  }

  public String getName(){
	return this.name;
  }

  public boolean setName( String name ){
	this.name = name;
	return true;
  }

}

