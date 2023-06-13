-- drop database if exists receitas4ADSTeste;
create database if not exists receitas4ADSTeste;

use receitas4ADSTeste;

CREATE TABLE IF NOT EXISTS users (
	id int NOT NULL auto_increment,

	name VARCHAR(100) NOT NULL,
	username VARCHAR(50) NOT NULL,
	email VARCHAR(100) NOT NULL,
	hashed_password VARCHAR(100) NOT NULL,

	PRIMARY KEY (id),
	UNIQUE KEY (username),
	UNIQUE KEY (email)
);

CREATE TABLE IF NOT EXISTS sessions (
	id INT NOT NULL AUTO_INCREMENT,
	user_id INT NOT NULL,
	session_id VARCHAR(100) NOT NULL,
	expiration TIMESTAMP NOT NULL,
	PRIMARY KEY (id),

	FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,

	UNIQUE KEY (user_id)
);

CREATE TABLE IF NOT EXISTS recipes (
	id INT NOT NULL auto_increment,
	author_id int NOT NULL,

	name VARCHAR(100) NOT NULL,

	description VARCHAR(500) NOT NULL,
	prepare_in_minutes INT NOT NULL,
	cooking_method VARCHAR(50) NOT NULL,
	additional_info VARCHAR(1000),

	PRIMARY KEY (id),

	FOREIGN KEY (author_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS recipe_accesses (
    id INT NOT NULL AUTO_INCREMENT,
    recipe_id INT NOT NULL,
    accessed_at DATETIME NOT NULL,

    PRIMARY KEY (id),

    FOREIGN KEY (recipe_id) REFERENCES recipes(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS recipe_instructions (
	recipe_id INT NOT NULL,
	instruction_step INT NOT NULL,
	instruction_description VARCHAR(500) NOT NULL,

	PRIMARY KEY ( recipe_id, INSTRUCTION_STEP ),

	FOREIGN KEY ( recipe_id ) REFERENCES recipes(id) ON DELETE CASCADE

);

CREATE TABLE IF NOT EXISTS favorite_user_recipes (
	user_id int NOT NULL,
	recipe_id INT NOT NULL NOT NULL,

	PRIMARY KEY (user_id, recipe_id),

	FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,

	FOREIGN KEY (recipe_id) REFERENCES recipes(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS categories (
	id INT NOT NULL AUTO_INCREMENT,
	name varchar(50) not null,

	PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS recipe_category(
	category_id INT NOT NULL,
	recipe_id INT NOT NULL,

	PRIMARY KEY ( recipe_id, category_id ),
	
	FOREIGN KEY ( recipe_id ) REFERENCES recipes(id) ON DELETE CASCADE,
	FOREIGN KEY ( category_id ) REFERENCES categories(id)
);

CREATE TABLE IF NOT EXISTS ingredients (
	id INT NOT NULL AUTO_INCREMENT,
	name VARCHAR(50) NOT NULL,

	PRIMARY KEY (id),
	UNIQUE KEY (name)
);

CREATE TABLE IF NOT EXISTS recipe_ingredient (
	ingredient_id INT NOT NULL,
	recipe_id int NOT NULL,

	PRIMARY KEY ( recipe_id, ingredient_id ),

	FOREIGN KEY ( recipe_id ) REFERENCES recipes(id) ON DELETE CASCADE,
	FOREIGN KEY ( ingredient_id ) REFERENCES ingredients(id)
);

CREATE TABLE IF NOT EXISTS recipe_rating (
	recipe_id int NOT NULL,
	user_id int NOT NULL,
	rating int NOT NULL,

	PRIMARY KEY ( recipe_id, user_id ),

	FOREIGN KEY ( recipe_id ) REFERENCES recipes(id) ON DELETE CASCADE,
	FOREIGN KEY ( user_id ) REFERENCES users(id) ON DELETE CASCADE

);

