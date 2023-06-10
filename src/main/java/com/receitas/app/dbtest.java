package com.receitas.app;

import com.receitas.app.model.*;
import com.receitas.app.dao.*;

import com.receitas.app.utils.MyLogger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;

public class dbtest {
	public static void main(String[] args) {
		try {

			RecipeModel r = RecipeDAO.getInstance().getRecipeByID("1").get();
			System.out.println(r);

			r.getIngredients().forEach( ingredient -> {
				System.out.println( "ingredient " + ingredient );
			});

			r.getCategories().forEach( category -> {
				System.out.println( "category " + category );
			});

			r.getRatings().forEach( (authorID, rating) -> {
				System.out.println( String.format("Rating: %s - %s", authorID, rating) );
			});

		} catch(Exception e){
			e.printStackTrace();
		}

		MyLogger.info("Program ended");
		System.exit(0);

	}

}
