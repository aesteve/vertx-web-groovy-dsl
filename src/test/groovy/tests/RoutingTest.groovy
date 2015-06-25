package tests

import static org.junit.Assert.*
import groovy.json.JsonBuilder
import groovy.transform.TypeChecked
import io.vertx.core.http.HttpHeaders
import io.vertx.groovy.core.Vertx
import io.vertx.groovy.core.buffer.Buffer
import io.vertx.groovy.core.http.HttpClientResponse
import io.vertx.groovy.core.http.HttpServer
import io.vertx.groovy.ext.unit.Async
import io.vertx.groovy.ext.unit.TestContext
import io.vertx.groovy.ext.web.Router
import io.vertx.groovy.ext.web.dsl.RouterBuilder
import static io.vertx.core.http.HttpHeaders.*
import java.util.concurrent.CountDownLatch

import org.junit.Before
import org.junit.Test

@TypeChecked
public class RoutingTest extends TestBase {

	@Test
	public void testGetHandler(TestContext context) {
		Async async = context.async()
		client().get("/handlers", { HttpClientResponse response ->
			assertEquals(200, response.statusCode())
			response.bodyHandler { Buffer buffer ->
				context.assertEquals(buffer.toString("UTF-8"), new JsonBuilder([result:"GET"]).toString())
				async.complete()
			}
		})
		.putHeader(ACCEPT.toString(), "application/json")
		.putHeader(CONTENT_TYPE.toString(), "application/json")
		.end()
	}

	@Test
	public void testWrongContentType(TestContext context) {
		Async async = context.async()
		client().get("/handlers", { HttpClientResponse response ->
			context.assertEquals(response.statusCode(), 404)
			async.complete()
		})
		.putHeader(ACCEPT.toString(), "application/xml")
		.putHeader(CONTENT_TYPE.toString(), "application/xml")
		.end()
	}

	@Test
	public void testPostHandler(TestContext context) {
		Async async = context.async()
		client().post("/handlers", { HttpClientResponse response ->
			context.assertEquals(response.statusCode(), 200)
			response.bodyHandler { Buffer buffer ->
				context.assertEquals(buffer.toString("UTF-8"), new JsonBuilder([result:"POST"]).toString())
				async.complete()
			}
		})
		.putHeader(ACCEPT.toString(), "application/json")
		.putHeader(CONTENT_TYPE.toString(), "application/json")
		.end()
	}

	@Test
	public void testGetStatic(TestContext context) {
		Async async = context.async()
		client().get("/staticClosure", { HttpClientResponse response ->
			assertEquals(200, response.statusCode())
			response.bodyHandler { Buffer buffer ->
				context.assertEquals(buffer.toString("UTF-8"), new JsonBuilder([result:"closure"]).toString())
				async.complete()
			}
		})
		.end()
	}
}