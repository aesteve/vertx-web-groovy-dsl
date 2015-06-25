package tests

import static org.junit.Assert.*
import groovy.transform.TypeChecked
import io.vertx.groovy.core.buffer.Buffer
import io.vertx.groovy.core.http.HttpClientResponse
import io.vertx.groovy.ext.unit.Async
import io.vertx.groovy.ext.unit.TestContext

import static io.vertx.core.http.HttpHeaders.*

import java.util.concurrent.CountDownLatch

import org.junit.Test

@TypeChecked
public class CORSTest extends TestBase {

	@Test
	public void testAllowOrigin(TestContext context) {
		Async async = context.async()
		client().get("/cors/test", { HttpClientResponse response ->
			assertEquals(200, response.statusCode())
			response.headers().each { println it }
			assertEquals("*", response.headers().get(ACCESS_CONTROL_ALLOW_ORIGIN.toString()))
			response.bodyHandler { Buffer buffer ->
				context.assertEquals(buffer.toString("UTF-8"), "CORS")
				async.complete()
			}
		})
		.putHeader("origin", "vertx.io")
		.end()
	}
}