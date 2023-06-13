package com.receitas.app.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import io.github.cdimascio.dotenv.Dotenv;

abstract public class MySQLDAO {

	protected Connection getConnection() throws SQLException {

        Dotenv dotenv = Dotenv.load();

        String DB_URL = dotenv.get("DB_URL");
        String DB_USER = dotenv.get("DB_USER");
        String DB_PASSWORD = dotenv.get("DB_PASSWORD");

		DriverManager.setLoginTimeout(10);
		return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
	}


}

