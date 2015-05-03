package io.vertx.groovy.ext.apex.dsl

import io.vertx.core.http.HttpMethod
import io.vertx.groovy.ext.apex.Route
import io.vertx.groovy.ext.apex.Router
import io.vertx.groovy.ext.apex.handler.BodyHandler
import io.vertx.groovy.ext.apex.handler.SessionHandler
import io.vertx.groovy.ext.apex.sstore.ClusteredSessionStore
import io.vertx.groovy.ext.apex.sstore.LocalSessionStore
import io.vertx.groovy.ext.apex.sstore.SessionStore

import groovy.transform.TypeChecked

class RouteDSL {

	RouterDSL parent
    String path
	BodyHandler bodyHandler
	boolean cookies
	SessionStore sessionStore

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
			parent.router.route(method, path).handler(bodyHandler);
		}
		if (cookies || sessionStore) {
			parent.router.route(method, path).handler()
		}
		if (sessionStore) {
			parent.router.route(path).handler(SessionHandler.create(sessionStore))
		}
		parent.router.route(method, path).handler(handler)
	}
	
	def methodMissing(String name, args) {
		if (args && args.size() == 1) {
			parent.router.route(path)."$name"(args[0])
		} else {
			parent.router.route(path)."$name"(args)
		}
	}
}
