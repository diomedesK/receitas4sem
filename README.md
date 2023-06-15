# RECEITAS 4 ADS

- Crie o banco de dados seguindo o modelo do arquivo <a href="./tables.sql">tables.sql</a>  

- Adicione suas credenciais ao arquivo .env

- Crie o banco de dados seguindo o modelo do arquivo <a href="./tables.sql">tables.sql</a>  

- Adicione suas credenciais ao arquivo .env, como em
``` shell
DB_URL=jdbc:mysql://localhost:3306/receitas4ADS
DB_USER=usuario
DB_PASSWORD=senha
```
- Construa o programa 
``` shell
$ mvn package
```

- Execute o programa 
``` shell
$ java -cp ./target/receitas-1.0-SNAPSHOT-jar-with-dependencies.jar com.receitas.app.Server
```
