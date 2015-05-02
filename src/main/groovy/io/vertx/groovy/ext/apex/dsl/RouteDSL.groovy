package io.vertx.groovy.ext.apex.dsl

import io.vertx.core.http.HttpMethod
import io.vertx.groovy.ext.apex.Route
import io.vertx.groovy.ext.apex.Router
import io.vertx.groovy.ext.apex.handler.BodyHandler

import groovy.transform.TypeChecked

class RouteDSL {

    Router router
    String path
	BodyHandler bodyHandler
	boolean cookies

	def static make(Router router, String path, Closure closure, boolean cookies) {
		RouteDSL routeDSL = new RouteDSL(path:path, router:router, cookies:cookies)
		closure.resolveStrategy = Closure.DELEGATE_FIRST
		closure.delegate = routeDSL
		closure.call()
	}
	
	def static make(Router router, String path, HttpMethod method, Closure closure, boolean cookies) {
		Route route = router.route(method, path)
		if (cookies) {
			route.handler(io.vertx.groovy.ext.apex.handler.CookieHandler.create())
		}
		route.handler(closure)
	}
	
    def get(Closure handler) {
		createRoute(HttpMethod.GET, handler)
    }

    def post(Closure handler) {
		createRoute(HttpMethod.POST, handler, true)
    }
	
	def put(Closure handler) {
		createRoute(HttpMethod.PUT, handler, true)
	}

	def delete(Closure handler) {
		createRoute(HttpMethod.DELETE, handler)
	}
	
	def options(Closure handler) {
		createRoute(HttpMethod.OPTIONS, handler)
	}
	
	def head(Closure handler) {
		createRoute(HttpMethod.HEAD, handler)
	}
	
	def connect(Closure handler) {
		createRoute(HttpMethod.CONNECT, handler)
	}
	
	def trace(Closure handler) {
		createRoute(HttpMethod.TRACE, handler)
	}

	def patch(Closure handler) {
		createRoute(HttpMethod.PATCH, handler)
	}

	private void createRoute(HttpMethod method, Closure handler, boolean useBodyHandler = false) {
		if (useBodyHandler) {
			if (!bodyHandler) {
				bodyHandler = BodyHandler.create()
			}
			router.route(method, path).handler(bodyHandler);
		}
		if (cookies) {
			router.route(method, path).handler(io.vertx.groovy.ext.apex.handler.CookieHandler.create())
		}
		router.route(method, path).handler(handler)
	}
	
	def methodMissing(String name, args) {
		try {
			if (args && args.size() == 1) {
				router.route(path)."$name"(args[0])
			} else {
				router.route(path)."$name"(args)
			}
		} catch (MissingMethodException mme) {
			throw new IllegalArgumentException("Method : $name doesn't exist on ${Route.class.toString()}")
		}  
	}
}
