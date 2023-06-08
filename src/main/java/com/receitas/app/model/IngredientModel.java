package com.receitas.app.model;

import com.fasterxml.jackson.annotation.*;

public class IngredientModel {
  private String id;
  private String name;

  public String getID(){
	return this.id;
  }

  public IngredientModel(){

  }

  public IngredientModel( 
		  @JsonProperty("name")
		  String name ){
	  this.name = name;
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

}


/*
@Entity
@Table( name = "Ingredient" )
public class IngredientModel {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @ElementCollection
  @MapKeyColumn(name = "language")
  @Column(name = "name")
  @CollectionTable(name = "ingredient_alias", joinColumns = @JoinColumn(name = "ingredient_id"))
  private Map<String, String> aliases;

  public IngredientModel() {
    this.aliases = new HashMap<>();
  }

  public IngredientModel(String defaultName) {
    this();
    this.aliases.put("default", defaultName);
  }

  public void addAlias(String language, String name) {
    this.aliases.put(language, name);
  }

  public String getName(String language) {
    return aliases.getOrDefault(language, aliases.get("default"));
  }

  // Getters and Setters
}
*/
