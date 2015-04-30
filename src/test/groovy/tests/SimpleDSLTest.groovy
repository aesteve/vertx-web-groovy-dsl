package tests

import groovy.json.JsonBuilder
import io.vertx.groovy.core.Vertx
import io.vertx.groovy.core.buffer.Buffer
import io.vertx.groovy.core.http.HttpClientResponse
import io.vertx.groovy.core.http.HttpServer
import io.vertx.groovy.ext.apex.Router
import io.vertx.groovy.ext.apex.dsl.RouterBuilder
import io.vertx.groovy.ext.apex.dsl.RouterDSL
import java.util.concurrent.CountDownLatch

import org.junit.Test
import org.junit.Before
import static org.junit.Assert.*

public class SimpleDSLTest extends TestBase {

    @Before
    public void createServer() {
        Router router = RouterBuilder.buildRouter(vertx, new File("testscripts/routes.groovy"))
        HttpServer server = vertx.createHttpServer()
        server.requestHandler(router.&accept)
        server.listen(PORT)
    }

    @Test
    public void testGetHandler(){
        CountDownLatch latch = new CountDownLatch(1)
        client().get("/handlers", { HttpClientResponse response ->
            assertEquals(200, response.statusCode())
            response.bodyHandler { Buffer buffer ->
                assertEquals(new JsonBuilder([result:"GET"]).toString(), buffer.toString("UTF-8"))
                latch.countDown()
            }
        })
        .putHeader("Accept", "application/json")
        .putHeader("Content-Type", "application/json")
        .end()
        latch.await()
    }

    @Test
    public void testPostHandler(){
        CountDownLatch latch = new CountDownLatch(1)
        client().post("/handlers", { HttpClientResponse response ->
            assertEquals(200, response.statusCode())
            response.bodyHandler { Buffer buffer ->
                assertEquals(new JsonBuilder([result:"POST"]).toString(), buffer.toString("UTF-8"))
                latch.countDown()
            }
        })
        .putHeader("Accept", "application/json")
        .putHeader("Content-Type", "application/json")
        .end()
        latch.await()
    }

    @Test
    public void testGetStatic(){
        CountDownLatch latch = new CountDownLatch(1)
        client().get("/static", { HttpClientResponse response ->
            assertEquals(200, response.statusCode())
            response.bodyHandler { Buffer buffer ->
                assertEquals(new JsonBuilder([result:"closure"]).toString(), buffer.toString("UTF-8"))
                latch.countDown()
            }
        })
        .end()
        latch.await()
    }
}