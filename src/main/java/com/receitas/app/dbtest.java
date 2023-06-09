package com.receitas.app;

import com.receitas.app.model.*;
import com.receitas.app.dao.*;
import com.receitas.app.dummyData.*;

import com.receitas.app.utils.MyLogger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class dbtest {


	public static void main(String[] args) {

		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.enable( SerializationFeature.INDENT_OUTPUT );

			UserModel u = new UserModel();
			u.setName("John");
			u.setEmail("john@mail.com");
			u.setUsername("jonjohny");
			u.setFavoritedRecipes( DummyRecipes.generateDummyRecipes().subList(0, 1) );
MyLogger.info("Created:");
			System.out.println(mapper.writeValueAsString(u));

			MyLogger.info("Deserialized:");
			UserModel u2 = mapper.readValue( mapper.writeValueAsString(u), UserModel.class );
			System.out.println(mapper.writeValueAsString(u2));
			

		} catch(Exception e){
			e.printStackTrace();
		}

		MyLogger.info("Program ended");

	}

}
