package io.led.hr.api

import io.led.hr.AbstractIntegrationTest
import io.led.hr.persistence.GraphRepository
import io.led.hr.security.TokenAuthConfig
import org.json.JSONObject
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders.ACCEPT
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody
import java.util.*

internal class GraphResourceIntegrationTest(
    @Autowired private val webTestClient: WebTestClient,
    @Autowired private val tokenAuthConfig: TokenAuthConfig,
    @Autowired private val graphRepository: GraphRepository
) : AbstractIntegrationTest() {
    private val json = """
            {
              "Pete": "Nick",
              "Barbara": "Nick",
              "Nick": "Sophie",
              "Sophie": "Jonas"
            }
        """.trimIndent()

    private val tree = """
            {
                "Jonas": {
                    "Sophie": {
                        "Nick": {
                            "Pete": {},
                            "Barbara": {}
                        }
                    }
                }
            }
        """.trimIndent()

    fun createTree() {
        webTestClient
            .post()
            .uri(GraphResource.GRAPHS_URI)
            .header(ACCEPT, APPLICATION_JSON_VALUE)
            .header(tokenAuthConfig.tokenHeaderName, tokenAuthConfig.token)
            .bodyValue(json)
            .exchange()
            .expectStatus()
            .isCreated
    }

    @Nested
    inner class CreateTree {
        @Test
        internal fun forbiddenTreeCreationWithNoApiKey() {
            webTestClient
                .post()
                .uri(GraphResource.GRAPHS_URI)
                .header(ACCEPT, APPLICATION_JSON_VALUE)
                .bodyValue(json)
                .exchange()
                .expectStatus()
                .isForbidden
        }

        @Test
        internal fun forbiddenTreeCreationWithWrongApiKey() {
            webTestClient
                .post()
                .uri(GraphResource.GRAPHS_URI)
                .header(ACCEPT, APPLICATION_JSON_VALUE)
                .header(tokenAuthConfig.tokenHeaderName, UUID.randomUUID().toString())
                .bodyValue(json)
                .exchange()
                .expectStatus()
                .isForbidden
        }

        @Test
        internal fun notAllowedKeyDuplication() {
            val jsonWithKeyDuplication = """
            {
              "Pete": "Nick",
              "Pete": "Nicky"
            }
        """.trimIndent()
            webTestClient
                .post()
                .uri(GraphResource.GRAPHS_URI)
                .header(ACCEPT, APPLICATION_JSON_VALUE)
                .header(tokenAuthConfig.tokenHeaderName, tokenAuthConfig.token)
                .bodyValue(jsonWithKeyDuplication)
                .exchange()
                .expectStatus()
                .isBadRequest
        }

        @Test
        internal fun notAllowedCyclicPath() {
            val jsonWithCyclicPath = """
            {
              "Pete": "Nick",
              "Nick": "Sophie",
              "Sophie": "Pete"
            }
        """.trimIndent()
            webTestClient
                .post()
                .uri(GraphResource.GRAPHS_URI)
                .header(ACCEPT, APPLICATION_JSON_VALUE)
                .header(tokenAuthConfig.tokenHeaderName, tokenAuthConfig.token)
                .bodyValue(jsonWithCyclicPath)
                .exchange()
                .expectStatus()
                .isBadRequest
        }

        @Test
        internal fun create_Tree() {
            createTree()

            val graphId = graphRepository.findAll().first().id
            webTestClient
                .get()
                .uri(GraphResource.GRAPHS_URI.plus(GraphResource.GRAPH_URI), graphId)
                .header(ACCEPT, APPLICATION_JSON_VALUE)
                .header(tokenAuthConfig.tokenHeaderName, tokenAuthConfig.token)
                .exchange()
                .expectStatus()
                .isOk
                .expectBody<String>()
                .isEqualTo(JSONObject(tree).toString())
        }
    }

    @Nested
    inner class GetTree {
        @Test
        internal fun forbiddenGetTreeWithWrongNoApiKey() {
            webTestClient
                .get()
                .uri(GraphResource.GRAPHS_URI.plus(GraphResource.GRAPH_URI), UUID.randomUUID())
                .header(ACCEPT, APPLICATION_JSON_VALUE)
                .header(tokenAuthConfig.tokenHeaderName, UUID.randomUUID().toString())
                .exchange()
                .expectStatus()
                .isForbidden
        }

        @Test
        internal fun forbiddenGetTreeWithNoApiKey() {
            webTestClient
                .get()
                .uri(GraphResource.GRAPHS_URI.plus(GraphResource.GRAPH_URI), UUID.randomUUID())
                .header(ACCEPT, APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus()
                .isForbidden
        }

        @Test
        internal fun getTree() {
            createTree()
            val graphId = graphRepository.findAll().first().id

            webTestClient
                .get()
                .uri(GraphResource.GRAPHS_URI.plus(GraphResource.GRAPH_URI), graphId)
                .header(ACCEPT, APPLICATION_JSON_VALUE)
                .header(tokenAuthConfig.tokenHeaderName, tokenAuthConfig.token)
                .exchange()
                .expectStatus()
                .isOk
                .expectBody<String>()
                .isEqualTo(JSONObject(tree).toString())
        }
    }

    @Nested
    inner class GetSupervisors {
        @Test
        internal fun forbiddenFilterWithNoApiKey() {
            webTestClient
                .get()
                .uri(
                    GraphResource.GRAPHS_URI.plus(GraphResource.FILTER_URI).plus("?name=Pete&level=3"),
                    UUID.randomUUID()
                )
                .header(ACCEPT, APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus()
                .isForbidden
        }

        @Test
        internal fun forbiddenFilterWithWrongNoApiKey() {
            webTestClient
                .get()
                .uri(
                    GraphResource.GRAPHS_URI.plus(GraphResource.FILTER_URI).plus("?name=Pete&level=3"),
                    UUID.randomUUID()
                )
                .header(ACCEPT, APPLICATION_JSON_VALUE)
                .header(tokenAuthConfig.tokenHeaderName, UUID.randomUUID().toString())
                .exchange()
                .expectStatus()
                .isForbidden
        }

        @Test
        internal fun getSupervisors() {
            createTree()
            val graphId = graphRepository.findAll().first().id
            val supervisorHierarchy = """
            {
                "Sophie": {
                    "Nick": {
                        "Pete": {}
                    }
                }
            }
        """.trimIndent()
            webTestClient
                .get()
                .uri(GraphResource.GRAPHS_URI.plus(GraphResource.FILTER_URI).plus("?name=Pete&level=3"), graphId)
                .header(ACCEPT, APPLICATION_JSON_VALUE)
                .header(tokenAuthConfig.tokenHeaderName, tokenAuthConfig.token)
                .exchange()
                .expectStatus()
                .isOk
                .expectBody<String>()
                .isEqualTo(JSONObject(supervisorHierarchy).toString())
        }
    }
}