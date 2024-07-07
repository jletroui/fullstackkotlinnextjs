package org.example.app.helpers

import io.vertx.core.Future
import io.vertx.core.Vertx
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.client.HttpResponse
import io.vertx.ext.web.client.WebClient
import io.vertx.ext.web.codec.BodyCodec

class TestClient {
    companion object {
        fun get(
            vertx: Vertx,
            path: String,
        ): Future<HttpResponse<JsonObject>> =
            WebClient
                .create(vertx)
                .get(8080, "localhost", path)
                .`as`(BodyCodec.jsonObject())
                .send()
    }
}
