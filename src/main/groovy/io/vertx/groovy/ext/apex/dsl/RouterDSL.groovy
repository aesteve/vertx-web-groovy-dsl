package io.vertx.groovy.ext.apex.dsl

import groovy.transform.TypeChecked
import io.vertx.groovy.core.Vertx
import io.vertx.groovy.ext.apex.Router
import io.vertx.groovy.ext.apex.handler.StaticHandler
import io.vertx.groovy.ext.apex.handler.TemplateHandler
import io.vertx.groovy.ext.apex.templ.TemplateEngine

@TypeChecked
public class RouterDSL {
    Vertx vertx
    Router router
	StaticHandler staticHandler

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
			RouteDSL.make(router, path, closure)
		}
		router.route(path).handler(staticHandler)
	}
	
	def staticHandler(String path, String webroot, Closure closure = null) {
		if (closure) {
			RouteDSL.make(router, path, closure)
		}
		router.route(path).handler(StaticHandler.create(webroot))
	}
	
	def templateHandler(String path, TemplateEngine engine, Closure closure = null) {
		TemplateHandler tplHandler = TemplateHandler.create(engine)
		if (closure) {
			RouteDSL.make(router, path, closure)
		}
		router.route(path).handler(tplHandler)
	}
	
    def route(String path, Closure closure) {
        RouteDSL.make(router, path, closure)
    }

    def make(Closure closure) {
        router = Router.router(vertx)
        closure.delegate = this
        closure()
    }
}
