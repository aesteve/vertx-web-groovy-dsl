package tests

import groovy.json.JsonBuilder
import io.vertx.groovy.core.Vertx
import io.vertx.groovy.core.buffer.Buffer
import io.vertx.groovy.core.http.HttpClientResponse
import io.vertx.groovy.core.http.HttpServer
import io.vertx.groovy.ext.web.Router
import io.vertx.groovy.ext.web.dsl.RouterBuilder
import io.vertx.groovy.ext.web.dsl.RouterDSL
import java.util.concurrent.CountDownLatch

import org.junit.Test
import org.junit.Before
import static org.junit.Assert.*

public class SubRouterTest extends TestBase {

    @Before
    public void createServer() {
        Router router = RouterBuilder.buildRouter(vertx, new File("src/test/resources/routes.groovy"))
        HttpServer server = vertx.createHttpServer()
        server.requestHandler(router.&accept)
        server.listen(PORT)
    }

    @Test
    public void testGetHandler(){
        CountDownLatch latch = new CountDownLatch(1)
        client().getNow("/sub/firstSubRoute", { HttpClientResponse response ->
            assertEquals(200, response.statusCode())
            response.bodyHandler { Buffer buffer ->
                assertEquals("firstSubRoute", buffer.toString("UTF-8"))
                latch.countDown()
            }
        })
        latch.await()
    }
}