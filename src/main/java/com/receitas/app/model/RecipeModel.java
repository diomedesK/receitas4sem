package com.receitas.app.model;

import java.util.LinkedHashSet;
import java.util.HashMap;

import com.fasterxml.jackson.annotation.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

@JsonRootName("recipe")
@JsonPropertyOrder({ "id", "name", "authorID" })
public class RecipeModel{

	private String ID;

	private String name;
	private String description;
	private String authorID;
	private String cookingMethod;

	private String imagePath;

	private String additionalInfo;

	private int prepareInMinutes;

	private LinkedHashSet<String> categories;

	private LinkedHashSet<IngredientModel> ingredientList;

	private HashMap<Integer, String> instructions;
	private HashMap<String, Integer> userRatings;

	private int accessesWithinLast7Days;

	public RecipeModel(){
		this.categories = new LinkedHashSet<>();

		this.userRatings = new HashMap<String, Integer>();
		this.instructions = new HashMap<Integer, String>();
		this.ingredientList = new LinkedHashSet<>();
	}

	@JsonCreator
	public RecipeModel( 
			@JsonProperty("name") String name,
			@JsonProperty("description") String description,
			@JsonProperty("imagePath") String imagePath,
			@JsonProperty("prepareInMinutes") String prepareInMinutes,
			@JsonProperty("cookingMethod") String cookingMethod,
			@JsonProperty("categories") LinkedHashSet<String> categories,

			@JsonProperty("ingredientList") 
			@JsonDeserialize(using = IngredientListDeserializer.class)
		  	LinkedHashSet<IngredientModel> ingredientList,

			@JsonProperty("Instructions") HashMap<Integer, String> instructions,
			@JsonProperty("additionalInfo") String additionalInfo,
			@JsonProperty("accessesWithinLast7Days") int accessesWithinLast7Days
			)
	{
		this();
		try {
			this.name = name;

			this.setImagePath(imagePath);
			this.setDescription(description);
			this.prepareInMinutes = Integer.parseInt( prepareInMinutes );
			this.cookingMethod = cookingMethod;
			this.categories = categories;
			this.instructions = instructions;
			this.additionalInfo = additionalInfo;

			this.accessesWithinLast7Days = accessesWithinLast7Days;

		} catch(Exception e){
			e.printStackTrace();
		}

	}

	public boolean setID(String id){
		this.ID = id;
		return true;
	}

	public String getID(){
		return this.ID;
	}

	public boolean setImagePath(String imagePath){
		this.imagePath = imagePath;
		return true;
	}
	
	public String getImagePath(){
		return this.imagePath;
	}

	public boolean setAuthorID(String id){
		this.authorID = id;
		return true;
	}

	public String getAuthorID(){
		return this.authorID;
	}

	public boolean setName(String name){
		this.name = name;
		return true;
	}

	public String getName(){
		return this.name;
	}

	public boolean setDescription(String description){
		this.description = description;
		return true;
	}

	public String getDescription(){
		return this.description;
	}

	public boolean setAdditionalInfo(String additionalInfo){
		this.additionalInfo = additionalInfo;
		return true;
	}

	public String getAdditionalInfo(){
		return this.additionalInfo;
	}

	public boolean setPrepareInMinutes(int p){
		this.prepareInMinutes = p;
		return true;
	}

	public int getPrepareInMinutes(){
		return this.prepareInMinutes;
	}

	public boolean setCookingMethod(String c){
		this.cookingMethod = c;
		return true;
	}

	public String getCookingMethod(){
		return this.cookingMethod;
	}

	public boolean addCategory( String category ){
		this.categories.add(category);
		return true;
	}

	public LinkedHashSet<String > getCategories(){
		return this.categories;
	}

	public boolean setCategories( LinkedHashSet<String> categories ){
		this.categories = categories;
		return true;
	}

	public boolean addRating( String userID, int rating  ){
		this.userRatings.put( userID, rating );
		return true;
	}

	public boolean setRatings( HashMap<String, Integer> ratings ){
		this.userRatings = ratings;
		return true;
	}

	public HashMap<String, Integer> getRatings(){
		return this.userRatings;
	}

	public boolean setInstructions( HashMap<Integer, String> instructions ){
		this.instructions = instructions;
		return true;
	}

	public HashMap<Integer, String> getInstructions(){
		return this.instructions;
	}

	public boolean addInstruction( String instructionDescription ){
		this.instructions.put( this.instructions.size(), instructionDescription );
		return true;
	}

	public boolean addInstruction( int instructionStep, String instructionDescription ){
		this.instructions.put( instructionStep, instructionDescription );
		return true;
	}

	public LinkedHashSet<IngredientModel> getIngredients(){
		return this.ingredientList;
	}

	public boolean setIngredients(  LinkedHashSet<IngredientModel> ingredients ) {
		this.ingredientList = ingredients;
		return true;
	}

	public boolean addIngredient( IngredientModel i ){
		this.ingredientList.add(i);
		return true;
	}

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	public double getAverageRating(){
		if (userRatings.isEmpty()) {
			return 0.0; 
		}

		int sum = 0;
		for (int rating : userRatings.values()) {
			sum += rating;
		}

		return (double) sum / userRatings.size();
    }

	public boolean setAccessesWithinLast7Days( int accesses ) {
		this.accessesWithinLast7Days = accesses;
		return true;
	}
	
	public int getAccessesWithinLast7Days() {
		return this.accessesWithinLast7Days;
	}
	
	public String toString(){
		return String.format("%s(ID: '%s', name: '%s', [...])", this.getClass().getSimpleName(), this.ID, this.name );
	}

}


class IngredientListDeserializer extends JsonDeserializer<LinkedHashSet<IngredientModel>> {
    @Override
    public LinkedHashSet<IngredientModel> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {

        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        LinkedHashSet<IngredientModel> ingredientList = new LinkedHashSet<>();

        if (node.isArray()) {
            for (JsonNode ingredientNode : node) {
                if (ingredientNode.isTextual()) {
                    String ingredientName = ingredientNode.asText();
                    IngredientModel ingredientModel = new IngredientModel(ingredientName);
                    ingredientList.add(ingredientModel);
                }
            }
        }

        return ingredientList;
    }

}
