package com.receitas.app.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

abstract public class MySQLDAO {

	
	protected Connection getConnection() throws SQLException {
		// Replace the following code with your own connection logic

		String DB_URL = "jdbc:mysql://localhost:3306/receitas4ADSTeste";
		String DB_USER = "diomedes";
		String DB_PASSWORD = "1234";

		DriverManager.setLoginTimeout(10);
		return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
	}

}

