package tests

import static org.junit.Assert.*
import groovy.transform.TypeChecked
import io.vertx.groovy.core.Vertx
import io.vertx.groovy.core.buffer.Buffer
import io.vertx.groovy.core.http.HttpClientResponse
import io.vertx.groovy.core.http.HttpServer
import io.vertx.groovy.ext.unit.Async
import io.vertx.groovy.ext.unit.TestContext
import io.vertx.groovy.ext.web.Router
import io.vertx.groovy.ext.web.dsl.RouterBuilder

import org.junit.Before
import org.junit.Test

@TypeChecked
public class SubRouterTest extends TestBase {

	@Test
	public void testGetHandler(TestContext context) {
		Async async = context.async()
		client().getNow("/sub/firstSubRoute", { HttpClientResponse response ->
			assertEquals(200, response.statusCode())
			response.bodyHandler { Buffer buffer ->
				context.assertEquals(buffer.toString("UTF-8"), "firstSubRoute")
				async.complete()
			}
		})
	}
}