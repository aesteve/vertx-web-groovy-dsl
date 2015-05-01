import groovy.json.JsonBuilder
import controllers.TestStaticController
import io.vertx.groovy.ext.apex.templ.HandlebarsTemplateEngine

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
    route "/staticClosure", {
        GET TestStaticController.testClosure
    }
	staticHandler "/assets/*"
	staticHandler "/instrumented-assets/*", { 
		 GET { context ->
			 context.request().headers().add("X-Custom-Header", "instrumented")
		 }
	}
	templateHandler "/dynamic/*", HandlebarsTemplateEngine.create()
	subRouter "/sub", {
		staticHandler "/assets/*", "webroot/subDirectory"
		route "/firstSubRoute", {
			GET { context ->
				context.response().end("firstSubRoute")
			}
		}
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