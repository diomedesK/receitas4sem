package com.receitas.app;

import java.util.Map;
import java.util.HashMap;
import java.util.Optional;

/*
 * "Javalin looks for template files in src/resources, and uses the correct rendering engine based on the extension of the provided template."
 * https://northcoder.com/post/thymeleaf-using-external-css-and-ja/
 * */

import io.javalin.Javalin;
import io.javalin.plugin.rendering.JavalinRenderer;
import io.javalin.plugin.rendering.template.JavalinThymeleaf;

import java.io.InputStream;
import io.javalin.http.Handler;

import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.TemplateEngine;

import org.thymeleaf.context.Context;
import org.thymeleaf.context.WebContext;


import com.receitas.app.controller.RecipeController;
import com.receitas.app.service.RecipeService;

import com.receitas.app.model.*;

import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.post;
import static io.javalin.apibuilder.ApiBuilder.delete;
import static io.javalin.apibuilder.ApiBuilder.put;


public class Server {

	static TemplateEngine HTMLTemplateEngine = ThymeleafConfig.templateEngine( 
			ThymeleafConfig.templateResolver( TemplateMode.HTML, "/thymeleaf-templates/", ".html")
			);

	public static void main(String[] args) {

		RecipeController recipeController = new RecipeController(RecipeService.getInstance());

		Javalin app = Javalin.create(config -> {
			// add static file folder
			config.addStaticFiles("/static");

			// Add live request logging
			config.requestLogger((ctx, ms) -> {
				System.out.println(ctx.method() + " " + ctx.fullUrl());
				System.out.println(ms + " ms");
			});

			// set the thymeleaf as the rendering engine 
			JavalinRenderer.register(JavalinThymeleaf.INSTANCE);
			JavalinThymeleaf.configure(
					// set the default template engine as HTML, using 'public/thymeleaf-templates/' as the source folder for .html files
					HTMLTemplateEngine
					);

		}).start( getPort() );

		app.routes(() -> {

			get("/", (ctx) -> ctx.redirect("/home"));
			get("/home", homePageHandler);

			get("/busca", homePageHandler);

			put("/recipes", recipeController::addRecipeJSON);
			get("/recipes/:id", recipePageHandler); // also handles to query params
			put("/recipes/:id/ratings", recipeController::addRecipeRating);
			delete("/recipes/:id", recipeController::deleteRecipe);

			get("/api/recipes", recipeController::getRecipes); // also handles to query params
			get("/api/recipes/popular", recipeController::getPopularRecipes); // also handles to query params

		});


		System.out.printf("The server is up on port %s \\o/\n", app.port());
	}


	private static final Handler homePageHandler = (ctx) -> {
		Map<String, Object> model = new HashMap<>();
		model.put("hello", "Hello, World.");
		ctx.render("index.html", model);
	};


	private static final Handler recipePageHandler = (ctx) -> {
		// Retrieve your data model, for example:
		RecipeModel recipe = RecipeService.getInstance().getRecipeByID(ctx.pathParam("id")).get();

		if (recipe != null) {
			RecipeService.getInstance().clearAccessesOfRecipeFromDaysAgo(ctx.pathParam("id"), 7);
			RecipeService.getInstance().addAccess(ctx.pathParam("id"));
			
			ctx.attribute("recipe", recipe);

			System.out.println("categoies " + recipe.getCategories());
			recipe.getCategories().forEach( (c) -> System.out.println(c) );

			ctx.render("recipe.html");

		} else {
			// Handle the case when the recipe is not found
			ctx.status(404);
		}
	};

	private static final Handler JS = (ctx) -> {
		String jsFileName = ctx.pathParam("jsFile");
		Context thymeleafCtx = new Context();
		thymeleafCtx.setVariable("jsTest", "This string is from a JS file");
		ResourceResponse resourceResponse = renderedFileAsStream(jsFileName, thymeleafCtx, TemplateMode.JAVASCRIPT);
		sendResult(resourceResponse, "application/javascript", ctx);
	};

	private static final Handler CSS = (ctx) -> {
		String cssFileName = ctx.pathParam("cssFile");
		Context thymeleafCtx = new Context();
		thymeleafCtx.setVariable("backgroundColor", "goldenrod");
		ResourceResponse resourceResponse = renderedFileAsStream(cssFileName, thymeleafCtx, TemplateMode.CSS);
		sendResult(resourceResponse, "text/css", ctx);
	};

	// read system variable PORT if defined otherwise defaults to 7777
	private static int getPort(){
		final String environmentDefinedPort = System.getenv("RECIPES_SERVER_PORT");
		return environmentDefinedPort != null ? Integer.parseInt(environmentDefinedPort) : 7777;
	}

	// Finalizes the rendering based on the processed template stream (wrapped inside resourceResponse)
	private static void sendResult(ResourceResponse resourceResponse, String contentType, io.javalin.http.Context ctx) {
		ctx.contentType(contentType);
		ctx.status(resourceResponse.getHttpStatus());
		ctx.result(resourceResponse.getResponse());
	}


	private static ResourceResponse renderedFileAsStream(String templateName, Context thymeleafCtx, TemplateMode templateMode) {
		return ThymeleafConfig.renderTemplate(templateName, thymeleafCtx, templateMode);
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
