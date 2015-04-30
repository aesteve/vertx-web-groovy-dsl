package io.vertx.groovy.ext.apex.dsl

import io.vertx.groovy.core.Vertx
import io.vertx.groovy.ext.apex.Router

public class RouterDSL {
    Vertx vertx
    Router router

    def route(String path, Closure closure) {
        RouteDSL.make(router, path, closure)
    }


    def make(Closure closure) {
        router = Router.router(vertx)
        closure.delegate = this
        closure()
    }
}
