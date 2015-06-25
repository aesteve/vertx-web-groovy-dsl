package io.vertx.groovy.ext.web.dsl

import io.vertx.core.http.HttpMethod
import io.vertx.groovy.ext.web.Route
import io.vertx.groovy.ext.web.Router
import io.vertx.groovy.ext.web.handler.BodyHandler
import io.vertx.groovy.ext.web.handler.SessionHandler
import io.vertx.groovy.ext.web.sstore.ClusteredSessionStore
import io.vertx.groovy.ext.web.sstore.LocalSessionStore
import io.vertx.groovy.ext.web.sstore.SessionStore
import io.vertx.groovy.ext.web.handler.CorsHandler


import groovy.transform.TypeChecked

class RouteDSL {

	RouterDSL parent
	String path
	def bodyHandler
	boolean cookies
	def sessionStore
	private List<Route> routes = []
	private List<List<Object>> missingMethods = []

	def static make(RouterDSL parent, String path, Closure closure, boolean cookies) {
		RouteDSL routeDSL = new RouteDSL(path:path, parent:parent, cookies:cookies)
		closure.resolveStrategy = Closure.DELEGATE_FIRST
		closure.delegate = routeDSL
		closure.call()
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

	def cors(String origin) {
		parent.router.route(path).handler(CorsHandler.create(origin))
	}

	def session(Map options) {
		if (options.store) {
			sessionStore = options.store
		} else if (options.clustered) {
			sessionStore = LocalSessionStore.create(parent.vertx)
		} else {
			sessionStore = ClusteredSessionStore.create(parent.vertx)
		}
	}

	private void createRoute(HttpMethod method, Closure handler, boolean useBodyHandler = false) {
		if (useBodyHandler) {
			if (!bodyHandler) {
				bodyHandler = BodyHandler.create()
			}
			parent.router.route(method, path).handler(bodyHandler)
		}
		if (cookies || sessionStore) {
			parent.router.route(method, path).handler()
		}
		if (sessionStore) {
			parent.router.route(path).handler(SessionHandler.create(sessionStore))
		}
		Route route = parent.router.route(method, path)
		missingMethods.each { methodMissing ->
			callMethodOnRoute(route, methodMissing[0], methodMissing[1])
		}
		route.handler(handler)
		routes << route
	}

	def methodMissing(String name, args) {
		if (routes.empty) {
			// delay
			missingMethods << [name, args]
		}
		routes.each { Route route ->
			callMethodOnRoute(route, name, args)
		}
	}

	def callMethodOnRoute(route, name, args) {
		if (args && args.size() == 1) {
			route."$name"(args[0])
		} else {
			route."$name"(args)
		}
	}
}
