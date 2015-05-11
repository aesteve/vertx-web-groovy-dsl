package io.vertx.groovy.ext.apex.dsl

import io.vertx.groovy.core.Vertx
import io.vertx.groovy.ext.apex.Router

import groovy.transform.TypeChecked
@TypeChecked
class RouterBuilder {

    def static Router buildRouter(Vertx vertx, File routingFile) {
        def binding = new Binding()
        def shell = new GroovyShell(binding)
        RouterDSL routerDSL = new RouterDSL(vertx:vertx)
        shell.setVariable("router", routerDSL.&make)
        shell.evaluate(routingFile)
        routerDSL.router
    }
	
	def static Router buildRouter(Vertx vertx, InputStream is) {
		if (!is) {
			throw new IllegalArgumentException("Routing file is null")
		}
		def binding = new Binding()
		def shell = new GroovyShell(binding)
		RouterDSL routerDSL = new RouterDSL(vertx:vertx)
		shell.setVariable("router", routerDSL.&make)
		is.withReader {
			shell.evaluate(it)
		}
		routerDSL.router
	}
	
	def static Router buildRouter(Vertx vertx, Closure closure)  {
		RouterDSL routerDSL = new RouterDSL(vertx:vertx)
		routerDSL.make(closure)
		routerDSL.router
	}
}