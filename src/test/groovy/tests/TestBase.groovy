package tests

import java.nio.channels.AsynchronousByteChannel

import groovy.transform.TypeChecked
import io.vertx.core.AsyncResult
import io.vertx.groovy.core.Vertx
import io.vertx.groovy.core.http.HttpClient
import io.vertx.groovy.core.http.HttpServer
import io.vertx.groovy.ext.unit.Async
import io.vertx.groovy.ext.unit.TestContext
import io.vertx.groovy.ext.unit.junit.VertxUnitRunner
import io.vertx.groovy.ext.web.Router
import io.vertx.groovy.ext.web.dsl.RouterBuilder

import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith

@RunWith(VertxUnitRunner.class)
abstract class TestBase {

	final String HOST = "localhost"
	final Integer PORT = 9000


	protected Vertx vertx

	@Before
	public void createServer(TestContext context) {
		Async async = context.async()
		vertx = Vertx.vertx()
		Router router = RouterBuilder.buildRouter(vertx, new File("src/test/resources/routes.groovy"))
		HttpServer server = vertx.createHttpServer()
		server.requestHandler(router.&accept)
		server.listen(PORT, { AsyncResult res ->
			if (res.failed()) {
				context.fail()
			} else {
				async.complete()
			}
		})
	}


	@After
	public void after(TestContext context) {
		Async async = context.async()
		if (vertx) {
			vertx.close({ AsyncResult res ->
				if (res.failed()) {
					context.fail()
				} else {
					async.complete()
				}
			})
		} else {
			async.complete()
		}
	}

	HttpClient client() {
		vertx.createHttpClient([defaultHost:HOST, defaultPort:PORT])
	}
}
