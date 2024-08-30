package com.minimalTest

import io.micronaut.core.io.buffer.ByteBuffer
import io.micronaut.http.HttpHeaders.USER_AGENT
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.MutableHttpResponse
import io.micronaut.http.annotation.*
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.ProxyHttpClient
import io.micronaut.http.client.StreamingHttpClient
import io.micronaut.http.uri.UriBuilder
import org.reactivestreams.Publisher
import java.net.URI

@Controller
class GithubController(
    private val proxyHttpClient: ProxyHttpClient,
    private val httpClient: HttpClient,
    private val streamingHttpClient: StreamingHttpClient
    ) {
    @Get("/proxy")
    fun proxiedClient(): Publisher<MutableHttpResponse<*>>? {
        val uri = UriBuilder.of(URI("https://docs.github.com/rest/overview/resources-in-the-rest-api#rate-limiting")).build()
        val req = HttpRequest.GET<Any>(uri)
            .header(USER_AGENT, "Micronaut HTTP Client")
        return proxyHttpClient.proxy(req)
    }

    @Get("/simple")
    fun nonProxyClient(): Publisher<HttpResponse<ByteBuffer<Any>>>? {
        val uri = UriBuilder.of(URI("https://docs.github.com/rest/overview/resources-in-the-rest-api#rate-limiting")).build()
        val req = HttpRequest.GET<Any>(uri)
            .header(USER_AGENT, "Micronaut HTTP Client")
        return httpClient.exchange(req)
    }

    @Get("/streaming")
    fun streamingClient(): Publisher<HttpResponse<ByteBuffer<Any>>>? {
        val uri = UriBuilder.of(URI("https://docs.github.com/rest/overview/resources-in-the-rest-api#rate-limiting")).build()
        val req = HttpRequest.GET<Any>(uri)
            .header(USER_AGENT, "Micronaut HTTP Client")
        return streamingHttpClient.exchange(req)
    }
}