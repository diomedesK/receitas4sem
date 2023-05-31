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

import static io.javalin.apibuilder.ApiBuilder.get;

public class Server {

  public static void main(String[] args) {
        Javalin app = Javalin.create(config -> {
            config.addStaticFiles("/static");

			config.requestLogger((ctx, ms) -> {
			  System.out.println(ctx.method() + " " + ctx.fullUrl());
			  System.out.println(ms + " ms");
			});

            JavalinRenderer.register(JavalinThymeleaf.INSTANCE);

			// set the default template engine as HTML, using 'public/thymeleaf/' as the source folder for these files
            JavalinThymeleaf.configure(
				ThymeleafConfig.templateEngine( ThymeleafConfig.templateResolver( TemplateMode.HTML, "/thymeleaf/", ".html"))
            );
        }).start(7777);

        app.routes(() -> {
            get("/", TEST);
        });


		System.out.println("The server is up \\o/");
  }

  private static final Handler TEST = (ctx) -> {
	Map<String, Object> model = new HashMap<>();
	model.put("hello", "Hello, World.");
	ctx.render("test.html", model);
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
	final String environmentDefinedPort = System.getenv("PORT");
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
