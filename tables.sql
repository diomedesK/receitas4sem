-- create database if not exists receitas4ADSTeste;

drop database if exists receitas4ADSTeste;
create database receitas4ADSTeste;

use receitas4ADSTeste;

CREATE TABLE IF NOT EXISTS users (
	id int NOT NULL auto_increment,

	name VARCHAR(100) NOT NULL,
	username VARCHAR(50) NOT NULL,
	email VARCHAR(100) NOT NULL,
	password VARCHAR(100) NOT NULL,

	PRIMARY KEY (id),
	UNIQUE KEY (username),
	UNIQUE KEY (email)
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
	FOREIGN KEY (author_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS recipe_accesses (
    id INT NOT NULL AUTO_INCREMENT,
    recipe_id INT NOT NULL,
    accessed_at DATETIME NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (recipe_id) REFERENCES recipes(id)
);

CREATE TABLE IF NOT EXISTS recipe_instructions (
	recipe_id INT NOT NULL,
	instruction_step INT NOT NULL,
	instruction_description VARCHAR(500) NOT NULL,

	PRIMARY KEY ( recipe_id, INSTRUCTION_STEP ),
	FOREIGN KEY ( recipe_id ) REFERENCES recipes(id)

);

CREATE TABLE IF NOT EXISTS favorite_user_recipes (
	user_id int NOT NULL,
	recipe_id INT NOT NULL NOT NULL,

	PRIMARY KEY (user_id, recipe_id),
	FOREIGN KEY (user_id) REFERENCES users(id),
	FOREIGN KEY (recipe_id) REFERENCES recipes(id)
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
	
	FOREIGN KEY ( recipe_id ) REFERENCES recipes(id),
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

	FOREIGN KEY ( recipe_id ) REFERENCES recipes(id),
	FOREIGN KEY ( ingredient_id ) REFERENCES ingredients(id)
);

CREATE TABLE IF NOT EXISTS recipe_rating (
	recipe_id int NOT NULL,
	user_id int NOT NULL,
	rating int NOT NULL,

	PRIMARY KEY ( recipe_id, user_id ),

	FOREIGN KEY ( recipe_id ) REFERENCES recipes(id),
	FOREIGN KEY ( user_id ) REFERENCES users(id)

);

-- Add some values

INSERT IGNORE INTO users (name, username, email, password)
VALUES
('John Doe', 'johndoe', 'johndoe@example.com', 'password1'),
('Jane Smith', 'janesmith', 'janesmith@example.com', 'password2'),
('Mike Johnson', 'mikejohnson', 'mikejohnson@example.com', 'password3');


INSERT IGNORE INTO recipes (name, author_id, description, prepare_in_minutes, cooking_method)
VALUES
('Chocolate Cake', '1', 'Delicious chocolate cake recipe', 60, 'Baking'),
('Chicken Stir-Fry', '2', 'Healthy chicken stir-fry recipe', 30, 'Stir-Frying'),
('Spaghetti Bolognese', '3', 'Classic spaghetti Bolognese recipe', 45, 'Boiling');

INSERT IGNORE INTO favorite_user_recipes (user_id, recipe_id)
VALUES
-- ('1', '1'),
('1', '2'),
('2', '1'),
('3', '2');

INSERT IGNORE INTO categories (name) VALUES
('Sweet'),
('Animal'),
('Family');

INSERT IGNORE INTO recipe_category (category_id, recipe_id) VALUES
('1', '1'),
('1', '2'),
('2', '2'),
('3', '3');

INSERT IGNORE INTO ingredients(name) VALUES 
('Flour'), ('Sugar'), ('Chicken'), ('Bell Pepers'), ('Pasta'), ('Pancetta');

INSERT IGNORE INTO recipe_ingredient (recipe_id, ingredient_id ) VALUES 
('1', '1'),
('1', '2'),
('2', '3'),
('2', '4'),
('3', '5'),
('3', '6');

INSERT IGNORE INTO recipe_rating ( recipe_id, user_id, rating ) VALUES
('1', '1', 10),
('1', '2', 9);

-- Search recipes by ingredient
-- SELECT r.* FROM ( ( recipes r JOIN recipe_ingredient ri ON r.id = ri.recipe_id ) JOIN ingredients i ON ri.ingredient_id = i.id ) WHERE i.name LIKE "%pasta%"

-- Search recipes by category
-- SELECT r.* FROM ( ( recipe_category rc join recipes r on rc.recipe_id = r.id) join categories c on rc.category_id = c.id ) where r.name LIKE "%cake%";
