package io.vertx.groovy.ext.apex.dsl

import groovy.transform.TypeChecked
import io.vertx.core.http.HttpMethod
import io.vertx.groovy.core.Vertx
import io.vertx.groovy.ext.apex.Router
import io.vertx.groovy.ext.apex.handler.StaticHandler
import io.vertx.groovy.ext.apex.handler.TemplateHandler
import io.vertx.groovy.ext.apex.templ.TemplateEngine

public class RouterDSL {
    Vertx vertx
    Router router
	StaticHandler staticHandler
	boolean cookies 
	
	def make(Closure closure) {
		router = Router.router(vertx)
		closure.delegate = this
		closure()
	}

	def subRouter(String path, Closure closure) {
		RouterDSL dsl = new RouterDSL(vertx:vertx)
		dsl.make(closure)
		router.mountSubRouter(path, dsl.router)
	}
	
	def staticHandler(String path, Closure closure = null) {
		if (!staticHandler) {
			staticHandler = StaticHandler.create()
		}
		if (closure) {
			RouteDSL.make(router, path, closure, cookies)
		}
		router.route(path).handler(staticHandler)
	}
	
	def staticHandler(String path, String webroot, Closure closure = null) {
		if (closure) {
			RouteDSL.make(router, path, closure, cookies)
		}
		router.route(path).handler(StaticHandler.create(webroot))
	}
	
	def templateHandler(String path, TemplateEngine engine, Closure closure = null) {
		TemplateHandler tplHandler = TemplateHandler.create(engine)
		if (closure) {
			RouteDSL.make(router, path, closure, cookies)
		}
		router.route(path).handler(tplHandler)
	}
	
    def route(String path, Closure closure) {
        RouteDSL.make(router, path, closure, cookies)
    }
	
	def get(String path, Closure closure) {
		makeRoute(path, HttpMethod.GET, closure)
	}
	
	def post(String path, Closure closure) {
		makeRoute(path, HttpMethod.POST, closure)
	}

	def put(String path, Closure closure) {
		makeRoute(path, HttpMethod.PUT, closure)
	}
	
	def delete(String path, Closure closure) {
		makeRoute(path, HttpMethod.DELETE, closure)
	}
	
	def options(String path, Closure closure) {
		makeRoute(path, HttpMethod.OPTIONS, closure)
	}
	
	def head(String path, Closure closure) {
		makeRoute(path, HttpMethod.HEAD, closure)
	}
		
	def connect(String path, Closure closure) {
		makeRoute(path, HttpMethod.CONNECT, closure)
	}
	
	def patch(String path, Closure closure) {
		makeRoute(path, HttpMethod.PATCH, closure)
	}
	
	def trace(String path, Closure closure) {
		makeRoute(path, HttpMethod.TRACE, closure)
	}
	
	def makeRoute(String path, HttpMethod method, Closure closure) {
		RouteDSL.make(router,  path, method, closure, cookies)
	}
	
	def methodMissing(String name, args) {
		try {
			router."$name"(args)
		} catch (all) {
			throw new IllegalArgumentException("Method : $name doesn't exist on ${Router.class.toString()}")
		}
	}

}
