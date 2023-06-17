use receitas4ADS;

-- Insert users
INSERT INTO users (name, username, email, hashed_password)
VALUES ('John Doe', 'johndoe', 'johndoe@example.com', '57db1253b68b6802b59a969f750fa32b60cb5cc8a3cb19b87dac28f541dc4e2a'),
       ('Jane Smith', 'janesmith', 'janesmith@example.com', '57db1253b68b6802b59a969f750fa32b60cb5cc8a3cb19b87dac28f541dc4e2a');

-- Insert recipes
INSERT INTO recipes (author_id, name, description, prepare_in_minutes, cooking_method, additional_info)
VALUES (1, 'Chocolate Cake', 'Delicious chocolate cake recipe', 60, 'Baking', 'Additional info about the recipe'),
       (1, 'Chicken Alfredo Pasta', 'Creamy chicken alfredo pasta recipe', 30, 'Cooking', NULL),
       (1, 'Grilled Salmon', 'Healthy grilled salmon recipe', 20, 'Grilling', 'Additional info about the recipe'),
       (2, 'Vegetable Stir-Fry', 'Quick and easy vegetable stir-fry recipe', 15, 'Stir-frying', NULL),
       (2, 'Beef Tacos', 'Classic beef tacos recipe', 45, 'Cooking', 'Additional info about the recipe'),
       (2, 'Mushroom Risotto', 'Creamy mushroom risotto recipe', 40, 'Cooking', NULL),
       (1, 'Pesto Pasta', 'Delicious pesto pasta recipe', 25, 'Cooking', 'Additional info about the recipe'),
       (1, 'BBQ Ribs', 'Tender and flavorful BBQ ribs recipe', 180, 'Grilling', NULL),
       (2, 'Caprese Salad', 'Fresh and vibrant caprese salad recipe', 10, 'Assembling', 'Additional info about the recipe'),
       (2, 'Beef Stew', 'Hearty beef stew recipe', 120, 'Cooking', NULL),
       (1, 'Apple Pie', 'Homemade apple pie recipe', 75, 'Baking', 'Additional info about the recipe'),
       (1, 'Greek Salad', 'Healthy and refreshing Greek salad recipe', 15, 'Assembling', NULL);

-- Insert recipe accesses
INSERT INTO recipe_accesses (recipe_id, accessed_at)
VALUES (1, NOW()),
       (2, NOW()),
       (3, NOW()),
       (4, NOW()),
       (5, NOW()),
       (6, NOW()),
       (7, NOW()),
       (8, NOW()),
       (9, NOW()),
       (10, NOW()),
       (11, NOW()),
       (12, NOW());

-- Insert recipe instructions
INSERT INTO recipe_instructions (recipe_id, instruction_step, instruction_description)
VALUES (1, 1, 'Step 1: Preheat the oven to 350°F'),
       (1, 2, 'Step 2: Mix the dry ingredients'),
       (1, 3, 'Step 3: Combine wet and dry ingredients');
       -- Insert instructions for other recipes...

-- Insert favorite user recipes
INSERT INTO favorite_user_recipes (user_id, recipe_id)
VALUES (1, 3),
       (1, 7),
       (2, 2),
       (2, 8);

-- Insert categories
INSERT INTO categories (name)
VALUES ('Desserts'),
       ('Pasta'),
       ('Seafood'),
       ('Salads'),
       ('Meat'),
       ('Vegetarian');

-- Insert recipe categories
INSERT INTO recipe_category (category_id, recipe_id)
VALUES (2, 1),
       (5, 2),
       (3, 3),
       (4, 4),
       (5, 5),
       (2, 6),
       (2, 7),
       (5, 8),
       (4, 9),
       (4, 10),
       (1, 11),
       (4, 12);

-- Insert ingredients
INSERT INTO ingredients (name)
VALUES ('Chocolate'),
       ('Chicken'),
       ('Salmon'),
       ('Vegetables'),
       ('Beef'),
       ('Mushrooms'),
       ('Pasta'),
       ('Ribs'),
       ('Tomatoes'),
       ('Apples'),
       ('Lettuce'),
       ('Potatoes');

-- Insert recipe ingredients
INSERT INTO recipe_ingredient (ingredient_id, recipe_id)
VALUES (1, 1),
       (2, 2),
       (3, 3),
       (4, 4),
       (5, 5),
       (6, 6),
       (7, 7),
       (8, 8),
       (9, 9),
       (10, 10),
       (11, 11),
       (12, 12);

-- Insert recipe ratings
INSERT INTO recipe_rating (recipe_id, user_id, rating)
VALUES (1, 1, 5),
       (2, 1, 4),
       (3, 2, 5),
       (4, 2, 3),
       (5, 1, 4),
       (6, 2, 5),
       (7, 1, 3),
       (8, 2, 4),
       (9, 1, 5),
       (10, 2, 4),
       (11, 1, 3),
       (12, 2, 5);

