package io.vertx.groovy.ext.apex.dsl

import io.vertx.groovy.core.Vertx
import io.vertx.groovy.ext.apex.Router

class RouterBuilder {

    def static Router buildRouter(Vertx vertx, File routingFile) {
        def binding = new Binding()
        def shell = new GroovyShell(binding)
        RouterDSL routerDSL = new RouterDSL(vertx:Vertx.vertx())
        shell.setVariable("router", routerDSL.&make)
        def script = routingFile
        shell.evaluate(script)
        routerDSL.router
    }
}
