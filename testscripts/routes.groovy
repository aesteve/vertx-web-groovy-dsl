import groovy.json.JsonBuilder
import controllers.TestStaticController

router {
    route "/handlers", {
        produces : "application/json"
        consumes : "application/json"
        GET { context ->
            context.response().end(new JsonBuilder([result:"GET"]).toString())
        }
        POST { context ->
            context.response().end(new JsonBuilder([result:"POST"]).toString())
        }
    }
    route "/static", {
        GET TestStaticController.testClosure
    }
}

// vs
/*
 Router.router(vertx)
 router.route "/api/1/groovy"
 .consumes "application/json"
 .produces "application/json"
 .handler { context -> 
 context.response().end("Example")
 }
 */