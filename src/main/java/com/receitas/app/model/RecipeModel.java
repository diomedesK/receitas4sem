package com.receitas.app.model;

import java.util.LinkedHashSet;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Date;

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

	private static int nextIDForDummying = 0;

	private String ID;

	private String name;
	private String description;
	private String authorID;
	private String cookingMethod;

	private int prepareInMinutes;

	private LinkedHashSet<String> categories;

	private LinkedHashSet<IngredientModel> ingredientList;

	private HashMap<Integer, String> instructions;
	private HashMap<String, Integer> userRatings;

	class accessesWithinLast7DaysData{
		public int dayNumberSince1970;
		public int accesses;

		public accessesWithinLast7DaysData( int dayNumberSince1970 ){
			this.dayNumberSince1970 = dayNumberSince1970;
		}

	}

	private ArrayDeque<accessesWithinLast7DaysData> accessesWithinLast7Days;

	public RecipeModel(){
		this.ID = "" + nextIDForDummying++;
		this.categories = new LinkedHashSet<>();

		this.userRatings = new HashMap<String, Integer>();
		this.instructions = new HashMap<Integer, String>();
		this.ingredientList = new LinkedHashSet<>();
		this.accessesWithinLast7Days = new ArrayDeque<>();

	}

	@JsonCreator
	public RecipeModel( 
			@JsonProperty("authorID") String authorID,
			@JsonProperty("name") String name,
			@JsonProperty("description") String description,
			@JsonProperty("prepareInMinutes") String prepareInMinutes,
			@JsonProperty("cookingMethod") String cookingMethod,
			@JsonProperty("categories") LinkedHashSet<String> categories,

			@JsonProperty("ingredientList") 
			@JsonDeserialize(using = IngredientListDeserializer.class)
		  	LinkedHashSet<IngredientModel> ingredientList,

			@JsonProperty("Instructions") HashMap<Integer, String> instructions
			)
	{
		this();
		try {
			this.name = name;
			this.setAuthorID(authorID);

			this.setDescription(description);
			this.prepareInMinutes = Integer.parseInt( prepareInMinutes );
			this.cookingMethod = cookingMethod;
			this.categories = categories;
			this.instructions = instructions;
			this.accessesWithinLast7Days = new ArrayDeque<>();

		} catch(Exception e){
			e.printStackTrace();
		}

	}

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	public String getID(){
		return this.ID;
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
	
	public void addAccess() {
        int daysSince1970 = (int) new Date().getTime() / (24 * 60 * 60 * 1000);

		for ( accessesWithinLast7DaysData data : this.accessesWithinLast7Days ) {
			if ( data.dayNumberSince1970 == daysSince1970 ) {
				data.accesses++;
				return;
			}
		}

		accessesWithinLast7DaysData n = new accessesWithinLast7DaysData( daysSince1970 );
		n.accesses++;
		this.accessesWithinLast7Days.offer(n);

		if ( this.accessesWithinLast7Days.size() > 7) {
			this.accessesWithinLast7Days.poll();
		}

	}

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	public int getAccessesWithinLast7Days() {
		int totalAccesses = this.accessesWithinLast7Days.stream()
			.mapToInt(accessData -> accessData.accesses)
			.sum();
		return totalAccesses;
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
