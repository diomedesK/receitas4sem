package com.receitas.app;

import com.receitas.app.model.*;
import com.receitas.app.dao.*;

import com.receitas.app.utils.MyLogger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;

import com.receitas.app.utils.Hasher;

public class dbtest {
	public static void main(String[] args) {

        String password = "password123"; // Replace with the actual password
        
        // Hash the password
        String hashedPassword = Hasher.hashStringWithSHA256(password);
        System.out.println("Hashed Password: " + hashedPassword);

		MyLogger.info("Program ended");
		System.exit(0);

	}

}
