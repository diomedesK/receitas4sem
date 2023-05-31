package com.receitas.app;

/*
 * https://northcoder.com/post/thymeleaf-using-external-css-and-ja/
 */

import java.io.ByteArrayInputStream;

import java.nio.charset.StandardCharsets;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

public class ThymeleafConfig {

  /*
   * The ClassLoaderTemplateResolver is responsible for resolving and loading templates from the classpath;
   * "A class loader in Java is a part of the Java Runtime Environment (JRE) responsible for dynamically loading Java classes into memory at runtime. It is an essential component of the Java Virtual Machine (JVM) that enables the execution of Java programs."
   *
   * It first gets the class loader from the current thread, generate a TemplateResolver from it and then apply the received parameters
   */
  /**
   * The function returns a ClassLoader based implementation of the ITemplateResolver interface; 
   * @param templateMODE a constant like 'TemplateMode.HTML'
   * @param prefix the folder prefix, like 'myfolder/'
   * @param suffix the file suffix, (like it's extension), such as '*.html'
   */
  public static ITemplateResolver templateResolver( TemplateMode templateMode, String prefix, String suffix) {
	ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver(Thread.currentThread().getContextClassLoader());
	templateResolver.setTemplateMode(templateMode);
	templateResolver.setPrefix(prefix);
	templateResolver.setSuffix(suffix);
	templateResolver.setCharacterEncoding("UTF-8");
	return templateResolver;
  }

  /**
   * This function creates a TemplateEngine based on the received implementation of the ITemplateResolver interface.
   */
  public static TemplateEngine templateEngine( ITemplateResolver templateResolver) {

	TemplateEngine templateEngine = new TemplateEngine();
	templateEngine.addDialect(new Java8TimeDialect());
	templateEngine.addTemplateResolver(templateResolver);

	return templateEngine;
  }


  /**
   * This function acts as a shortcut for generating and processing templates from defaults passed in the templateMode parameter,
   * such as "CSS" and "JAVASCRIPT"
   * @param templateName name of the file to be used as template
   * @param ctx	context to be passed for the template file, containing the target variables and its values
   * @param templateMode the type of the template ("CSS" or "JAVASCRIPT")
   */
  public static ResourceResponse renderTemplate(String templateName, Context ctx, TemplateMode templateMode) {
	String prefix;
	String suffix;
	switch (templateMode) {
	  case CSS -> {
		prefix = "/public/css/";
		suffix = ".css";
	  }
	  case JAVASCRIPT -> {
		prefix = "/public/js/";
		suffix = ".js";
	  }
	  default -> {
		prefix = "";
		suffix = "";
	  }
	}
	ITemplateResolver resolver = templateResolver(templateMode, prefix, suffix);
	TemplateEngine engine = templateEngine(resolver);
	String renderedTemplate = null;
	try {
	  renderedTemplate = engine.process(templateName, ctx);
	} catch (Exception ex) {
	  if (ex.getCause().getClass().equals(java.io.FileNotFoundException.class)) {
		return notFound();
	  } else {
		// exception already thrown
	  }
	}
	if (renderedTemplate != null) {
	  return ok(renderedTemplate);
	} else {
	  return serverError();
	}

  }

  private static ResourceResponse ok(String renderedTemplate) {
	return new ResourceResponse(200, new ByteArrayInputStream(
		  renderedTemplate.getBytes(StandardCharsets.UTF_8)));
  }

  private static ResourceResponse notFound() {
	return new ResourceResponse(404, new ByteArrayInputStream(
		  ResourceResponse.FOUROHFOUR.getBytes(StandardCharsets.UTF_8)));
  }

  private static ResourceResponse serverError() {
	return new ResourceResponse(500, new ByteArrayInputStream(
		  ResourceResponse.FIVEHUNDRED.getBytes(StandardCharsets.UTF_8)));
  }

}
