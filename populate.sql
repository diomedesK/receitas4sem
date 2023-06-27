use receitas4ADS;

-- Insert users
INSERT INTO users (name, username, email, hashed_password)
VALUES ('John Doe', 'johndoe', 'johndoe@example.com', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f'),
       ('Jane Smith', 'janesmith', 'janesmith@example.com', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f');

-- Inserir receitas
INSERT INTO recipes (author_id, name, description, prepare_in_minutes, cooking_method, additional_info, image_path)
VALUES (1, 'Bolo de Chocolate', 'Deliciosa receita de bolo de chocolate', 60, 'Assar', 'Informações adicionais sobre a receita', '~/Pictures/028017771134155127.jpg'),
       (1, 'Macarrão Alfredo de Frango', 'Receita cremosa de macarrão Alfredo de frango', 30, 'Cozinhar', NULL, 'https://criticalhits.com.br/wp-content/uploads/2021/02/cosplay-asuka-brasileira.jpg'),
       (1, 'Salmão Grelhado', 'Receita saudável de salmão grelhado', 20, 'Grelhar', 'Informações adicionais sobre a receita', 'https://upload.wikimedia.org/wikipedia/pt/4/45/Asuka_Langley_Soryu.PNG'),
       (2, 'Legumes Salteados', 'Receita rápida e fácil de legumes salteados', 15, 'Saltear', NULL, 'https://i0.wp.com/evangelionbr.com/wp-content/uploads/2017/12/Asuka-Day.jpg?fit=758%2C360&ssl=1'),
       (2, 'Tacos de Carne', 'Clássica receita de tacos de carne', 45, 'Cozinhar', 'Informações adicionais sobre a receita', 'https://i.scdn.co/image/ab67616d0000b27326e7ec2275e8058ff2da38af'),
       (2, 'Risoto', 'Receita cremosa de risoto', 40, 'Cozinhar', NULL, 'https://images8.alphacoders.com/522/522662.jpg'),
       (1, 'Macarrão Pesto', 'Deliciosa receita de macarrão pesto', 25, 'Cozinhar', 'Informações adicionais sobre a receita', 'https://hippieartesanatos.fbitsstatic.net/img/p/329904/camiseta-nirvana-preta-120535/329904-1.jpg?w=740&h=1000'),
       (1, 'Costelas de Churrasco', 'Costelas de churrasco macias e saborosas', 180, 'Grelhar', NULL, NULL),
       (2, 'Salada Caprese', 'Salada Caprese fresca e vibrante', 10, 'Montagem', 'Informações adicionais sobre a receita', NULL),
       (2, 'Ensopado de Carne', 'Saboroso ensopado de carne', 120, 'Cozinhar', NULL, NULL),
       (1, 'Torta de Maçã', 'Receita caseira de torta de maçã', 75, 'Assar', 'Informações adicionais sobre a receita', NULL),
       (1, 'Salada Grega', 'Salada grega saudável e refrescante', 15, 'Montagem', NULL, NULL);

-- Inserir acessos às receitas
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

-- Inserir instruções de receitas
INSERT INTO recipe_instructions (recipe_id, step, description)
VALUES (1, 1, 'Passo 1: Pré-aqueça o forno a 350°F'),
       (1, 2, 'Passo 2: Misture os ingredientes secos'),
       (1, 3, 'Passo 3: Combine os ingredientes úmidos');
       -- Inserir instruções para outras receitas...

-- Inserir receitas favoritas dos usuários
INSERT INTO favorite_user_recipes (user_id, recipe_id)
VALUES (1, 3),
       (1, 7),
       (2, 2),
       (2, 8);

-- Inserir categorias
INSERT INTO categories (name)
VALUES ('Sobremesas'),
       ('Massas'),
       ('Frutos do Mar'),
       ('Saladas'),
       ('Carnes'),
       ('Vegetariano');

-- Inserir categorias das receitas
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

-- Inserir ingredientes
INSERT INTO ingredients (name)
VALUES ('Chocolate'),
       ('Frango'),
       ('Salmão'),
       ('Legumes'),
       ('Carne'),
       ('Cogumelos'),
       ('Massa'),
       ('Costelas'),
       ('Tomates'),
       ('Maçãs'),
       ('Alface'),
       ('Batatas');

-- Inserir ingredientes das receitas
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

-- Inserir avaliações das receitas
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
