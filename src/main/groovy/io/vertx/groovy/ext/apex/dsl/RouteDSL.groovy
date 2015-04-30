package io.vertx.groovy.ext.apex.dsl

import io.vertx.core.http.HttpMethod
import io.vertx.groovy.ext.apex.Route
import io.vertx.groovy.ext.apex.Router
import io.vertx.groovy.ext.apex.handler.BodyHandler

class RouteDSL {

    Router router
    String path
    String consumes
    String produces


    def GET(Closure handler) {
        router.route(HttpMethod.GET, path).handler(handler)
    }

    def POST(Closure handler) {
        router.route(HttpMethod.POST, path).handler(BodyHandler.create())
        router.route(HttpMethod.POST, path).handler(handler)
    }


    def static make(Router router, String path, Closure closure) {
        Route route = router.route(path)
        RouteDSL routeDSL = new RouteDSL(path:path, router:router)
        closure.delegate = routeDSL
        closure()
    }
}
