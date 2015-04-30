package controllers

import groovy.json.JsonBuilder
import io.vertx.groovy.ext.apex.RoutingContext

class TestStaticController {
    static Closure testClosure = { RoutingContext context ->
        context.response().end(new JsonBuilder([result:"closure"]).toString())
    }
}
