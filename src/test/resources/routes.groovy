import groovy.json.JsonBuilder
import controllers.TestController
import controllers.TestStaticController
import io.vertx.groovy.ext.apex.templ.HandlebarsTemplateEngine

TestController ctrlInstance = new TestController()

router {
	get "/simpleGet", { context ->
		context.response().end("Simple GET")
	}
    route "/handlers", {
        produces "application/json"
        consumes "application/json"
        get { context ->
            context.response().end(new JsonBuilder([result:"GET"]).toString())
        }
        post { context ->
            context.response().end(new JsonBuilder([result:"POST"]).toString())
        }
    }
    get "/staticClosure", TestStaticController.testClosure
	get "/controllerInstance", ctrlInstance.&someMethod
	staticHandler "/assets/*"
	staticHandler "/instrumented-assets/*", { 
		 get { context ->
			 context.request().headers().add("X-Custom-Header", "instrumented")
		 }
	}
	templateHandler "/dynamic/*", HandlebarsTemplateEngine.create()
	subRouter "/sub", {
		cookies : true
		staticHandler "/assets/*", "webroot/subDirectory"
		get "/firstSubRoute", { context ->
			context.response().end("firstSubRoute")
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