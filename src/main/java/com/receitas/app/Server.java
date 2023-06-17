package com.receitas.app;

import java.util.Optional;
import java.io.InputStream;

/*
 * "Javalin looks for template files in src/resources, and uses the correct rendering engine based on the extension of the provided template."
 * https://northcoder.com/post/thymeleaf-using-external-css-and-ja/
 * */

import io.javalin.Javalin;
import io.javalin.plugin.rendering.JavalinRenderer;
import io.javalin.plugin.rendering.template.JavalinThymeleaf;

import static io.javalin.apibuilder.ApiBuilder.put;
import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.post;
import static io.javalin.apibuilder.ApiBuilder.delete;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;

import com.receitas.app.service.UserService;
import com.receitas.app.service.RecipeService;
import com.receitas.app.controller.UserController;
import com.receitas.app.controller.RecipeController;

import com.receitas.app.model.UserModel;


public class Server {

	static TemplateEngine HTMLTemplateEngine = ThymeleafConfig.templateEngine( 
			ThymeleafConfig.templateResolver( TemplateMode.HTML, "/thymeleaf-templates/", ".html")
			);

	private static final UserService userService = UserService.getInstance();

	public static void main(String[] args) {

		UserController userController = new UserController(UserService.getInstance());
		RecipeController recipeController = new RecipeController(RecipeService.getInstance(), UserService.getInstance());

		Javalin app = Javalin.create(config -> {
			config.addStaticFiles("/static");

			config.requestLogger((ctx, ms) -> {
				System.out.println(ctx.method() + " " + ctx.fullUrl());
				System.out.println(ms + " ms");
			});

			JavalinRenderer.register(JavalinThymeleaf.INSTANCE);
			// set the default template engine as HTML, using 'public/thymeleaf-templates/' as the source folder for .html files
			JavalinThymeleaf.configure( HTMLTemplateEngine);

		}).start( getPort() );

		app.before(ctx -> {
			String sessionToken = ctx.cookie("session-token");
			if( sessionToken != null ){
				Optional<UserModel> userData = userService.getUserDataFromSessionToken(sessionToken);
				if( userData.isPresent() ){
					// the database gets queried all the time TODO
					ctx.attribute("userData", userData.get());
				}
			}

		});

		app.routes(() -> {
			get("/", (ctx) -> ctx.redirect("/home"));
			get("/home", recipeController::homePageHandler);
			get("/busca", recipeController::getSearchPage);

			get("/acesso", userController::accessPageHandler);
			get("/cadastro", userController::signUpPageHandler);

			get("/novareceita", recipeController::getNewRecipePage);

			put("/receitas", recipeController::addRecipeJSON);
			get("/receitas/:id", recipeController::getRecipePage); // also handles query params
			delete("/receitas/:id", recipeController::deleteRecipe);

			put("/receitas/:id/avaliacoes", recipeController::addRecipeRating);

			get("/api/receitas", recipeController::getRecipes); // also handles to query params
			get("/api/receitas/popular", recipeController::getPopularRecipes); // also handles to query params

			get("/perfil/:id", ( ctx ) -> ctx.redirect("/home"));  // not yet
			get("/perfil/:id/favoritos", userController::getFavoritesPage); 

			get("/api/perfil/", userController::getUserDataFromSessionToken); 

			put("/perfil/favoritos/:recipe_id", userController::saveFavoriteRecipe); 
			delete("/perfil/favoritos/:recipe_id", userController::deleteFavoriteRecipe); 


			put("/cadastro", userController::signUpUser);
			post("/acesso", userController::loginUser);
			get("/sair/", userController::logoutHandler); 

		});


		System.out.printf("The server is up on port %s \\o/\n", app.port());
	}


	private static int getPort(){
		final String environmentDefinedPort = System.getenv("RECIPES_SERVER_PORT");
		return environmentDefinedPort != null ? Integer.parseInt(environmentDefinedPort) : 7777;
	}

}


class ResourceResponse {
	private final int httpStatus;
	private final InputStream response;

	static final String FOUROHFOUR = "Not found.";
	static final String FIVEHUNDRED = "Internal server error.";

	ResourceResponse(int httpStatus, InputStream response) {
		this.httpStatus = httpStatus;
		this.response = response;
	}

	public int getHttpStatus() {
		return httpStatus;
	}

	public InputStream getResponse() {
		return response;
	}

}
