package com.receitas.app;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.receitas.app.model.*;
import com.receitas.app.dao.*;

import com.receitas.app.utils.MyLogger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializationFeature;

public class dbtest {

	public static void main(String[] args) {
        String recipeJSON = "{\"id\":\"123\",\"name\":\"Chocolate Cake\",\"description\":\"Delicious chocolate cake\",\"author\":\"John Doe\"}";

		RecipeModel r1 = RecipeDAO.getInstance().getRecipeByID("1").get();

		try {
			ObjectMapper mapper = new ObjectMapper();

			// System.out.println( neoJsonNode );
			
			// mapper.enable( SerializationFeature.WRAP_ROOT_VALUE );
			mapper.enable( SerializationFeature.INDENT_OUTPUT );

			MyLogger.info("Original:");
			System.out.println( mapper.writeValueAsString(r1) );

			RecipeModel generatedRecipe = mapper.readValue( mapper.writeValueAsString(r1), RecipeModel.class  );

			MyLogger.info("Recreated:");
			System.out.println( mapper.writeValueAsString(generatedRecipe) );

			
		} catch(Exception e){
			e.printStackTrace();
		}

		MyLogger.info("Program ended");

	}

}
