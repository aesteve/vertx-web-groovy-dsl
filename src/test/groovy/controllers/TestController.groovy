package controllers;

import io.vertx.groovy.ext.apex.RoutingContext;

public class TestController {
	
	def someMethod(RoutingContext context) {
		context.response().end("Test GET")
	}
	
}
