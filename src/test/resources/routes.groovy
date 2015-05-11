import groovy.json.JsonBuilder
import controllers.TestController
import controllers.TestStaticController
import io.vertx.groovy.ext.apex.templ.HandlebarsTemplateEngine

TestController ctrlerInstance = new TestController()

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
	get "/controllerInstance", ctrlerInstance.&someMethod
	staticHandler "/assets/*"
	staticHandler "/instrumented-assets/*", { 
		 get { context ->
			 context.request().headers().add("X-Custom-Header", "instrumented")
		 }
	}
	templateHandler "/dynamic/*", HandlebarsTemplateEngine.create()
	subRouter "/sub", {
		cookies : true
		/*
		auth {
			basic : true
			provider :  
		}
		*/
		staticHandler "/assets/*", "webroot/subDirectory"
		get "/firstSubRoute", { context ->
			context.response().end("firstSubRoute")
		}
	}
	sockJS "/sockjs/*", { socket ->
		socket.handler(socket.&write)
	}
	//favicon "my_favicon.ico"
	route "/login", {
		session([clustered:true])
		get { context ->
			context.response().end(context.session().get("test"))
		}
	}
	route "/cors/test", {
		cors "*"
		get { context ->
			context.response().end("CORS")
		}
	}
}