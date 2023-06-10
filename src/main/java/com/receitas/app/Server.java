package com.receitas.app;

import io.javalin.Javalin;

/*
 * "Javalin looks for template files in src/resources, and uses the correct rendering engine based on the extension of the provided template."
 * https://northcoder.com/post/thymeleaf-using-external-css-and-ja/
 * */

import io.javalin.plugin.rendering.JavalinRenderer;
import io.javalin.plugin.rendering.template.JavalinThymeleaf;

import java.io.InputStream;
import io.javalin.http.Handler;
import java.util.HashMap;
import java.util.Map;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.context.Context;

import com.receitas.app.controller.RecipeController;
import com.receitas.app.service.RecipeService;

import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.post;
import static io.javalin.apibuilder.ApiBuilder.delete;
import static io.javalin.apibuilder.ApiBuilder.put;

public class Server {



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
		  ThymeleafConfig.templateEngine( ThymeleafConfig.templateResolver( TemplateMode.HTML, "/thymeleaf-templates/", ".html"))
		  );

	}).start( getPort() );

	app.routes(() -> {

		get("/", rootPageHandler); // should redirect to /home
		get("/recipes", recipeController::getRecipes); // also handles to query params
		get("/recipes/popular", recipeController::getPopularRecipes); // also handles to query params
		put("/recipes", recipeController::addRecipeJSON);
		put("/recipes/:id/ratings", recipeController::addRecipeRating);

        delete("/recipes/:id", recipeController::deleteRecipe);

	});


	System.out.printf("The server is up on port %s \\o/\n", app.port());
  }


  private static final Handler rootPageHandler = (ctx) -> {
	Map<String, Object> model = new HashMap<>();
	model.put("hello", "Hello, World.");
	ctx.render("index.html", model);
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
