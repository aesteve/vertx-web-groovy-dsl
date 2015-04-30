package tests

import io.vertx.core.http.HttpClientOptions
import io.vertx.groovy.core.Vertx
import io.vertx.groovy.core.http.HttpClient
import org.junit.Before
import org.junit.After

class TestBase {
    final String HOST = "localhost"
    final Integer PORT = 9000


    protected Vertx vertx

    @Before
    public void before() {
        vertx = Vertx.vertx()
    }

    @After
    public void after() {
        if (vertx) {
            vertx.close()
        }
    }

    HttpClient client() {
        vertx.createHttpClient([defaultHost:HOST, defaultPort:PORT])
    }
}
