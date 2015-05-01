package io.vertx.groovy.ext.apex.dsl

import io.vertx.core.http.HttpMethod
import io.vertx.groovy.ext.apex.Route
import io.vertx.groovy.ext.apex.Router
import io.vertx.groovy.ext.apex.handler.BodyHandler

import groovy.transform.TypeChecked

@TypeChecked
class RouteDSL {

    Router router
    String path
    String consumes
    String produces
	BodyHandler bodyHandler

    def GET(Closure handler) {
		createRoute(HttpMethod.GET, handler)
    }

    def POST(Closure handler) {
		createRoute(HttpMethod.POST, handler, true)
    }
	
	def PUT(Closure handler) {
		createRoute(HttpMethod.PUT, handler, true)
	}

	def DELETE(Closure handler) {
		createRoute(HttpMethod.DELETE, handler)
	}
	
	def OPTIONS(Closure handler) {
		createRoute(HttpMethod.OPTIONS, handler)
	}
	
    def static make(Router router, String path, Closure closure) {
        Route route = router.route(path)
        RouteDSL routeDSL = new RouteDSL(path:path, router:router)
        closure.delegate = routeDSL
        closure()
    }
	
	private void createRoute(HttpMethod method, Closure handler, boolean useBodyHandler = false) {
		if (useBodyHandler) {
			if (!bodyHandler) {
				bodyHandler = BodyHandler.create()
			}
			router.route(method, path).handler(bodyHandler);
		}
		if (produces) {
			router.route(method, path).produces(produces)
		}
		if (consumes) {
			router.route(method, path).consumes(consumes)
		}
		router.route(method, path).handler(handler)
	}
}
