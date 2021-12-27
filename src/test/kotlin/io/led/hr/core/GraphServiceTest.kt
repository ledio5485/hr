package io.led.hr.core

import io.led.hr.persistence.GraphEntity
import io.led.hr.persistence.GraphRepository
import io.led.hr.persistence.NodeEntity
import io.led.hr.persistence.NodeRepository
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.util.Lists
import org.json.JSONObject
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

@ExtendWith(MockitoExtension::class)
internal class GraphServiceTest(
    @Mock private val graphFactory: GraphFactory,
    @Mock private val graphRepository: GraphRepository,
    @Mock private val nodeRepository: NodeRepository
) {

    @InjectMocks
    private lateinit var sut: GraphService

    @Test
    internal fun getGraph() {
        val tree = """
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
        val expected = JSONObject(tree)
        val graph = GraphEntity(id = UUID.randomUUID(), "graph")
        val graphId = graph.id!!
        Mockito.`when`(nodeRepository.filterTree(graph.id!!))
            .thenReturn(
                Lists.list(
                    NodeEntity(UUID.fromString("fcf4df4b-e52c-453f-8b35-1f95a6a17422"), graph, "Jonas", "Jonas", 1),
                    NodeEntity(
                        UUID.fromString("b15586ae-e387-4b53-90a2-4ea90a32fd57"),
                        graph,
                        "Sophie",
                        "Jonas.Sophie",
                        2
                    ),
                    NodeEntity(
                        UUID.fromString("4c55960e-a60e-4a4a-b177-ca4dbd5d31bf"),
                        graph,
                        "Nick",
                        "Jonas.Sophie.Nick",
                        3
                    ),
                    NodeEntity(
                        UUID.fromString("da5f0c5c-1ae6-492e-81cd-d6724d587b0f"),
                        graph,
                        "Barbara",
                        "Jonas.Sophie.Nick.Barbara",
                        4
                    ),
                    NodeEntity(
                        UUID.fromString("c8ed2182-c74e-40c6-a30c-9054c54378cf"),
                        graph,
                        "Pete",
                        "Jonas.Sophie.Nick.Pete",
                        4
                    )

                )
            )

        val actual = sut.buildTree(graphId)

        assertThat(actual.toString()).isEqualTo(expected.toString())
    }

    @Test
    fun buildReversePath() {
        val tree = """
            {
                "Jonas": {
                    "Sophie": {
                        "Nick": {}
                    }
                }
            }
        """.trimIndent()
        val graph = GraphEntity(id = UUID.randomUUID(), "graph")
        val graphId = UUID.randomUUID()
        Mockito.`when`(nodeRepository.getUpperHierarchy(graphId, "Nick", 5))
            .thenReturn(
                Lists.list(
                    NodeEntity(UUID.fromString("fcf4df4b-e52c-453f-8b35-1f95a6a17422"), graph, "Jonas", "Jonas", 1),
                    NodeEntity(
                        UUID.fromString("b15586ae-e387-4b53-90a2-4ea90a32fd57"),
                        graph,
                        "Sophie",
                        "Jonas.Sophie",
                        2
                    ),
                    NodeEntity(
                        UUID.fromString("4c55960e-a60e-4a4a-b177-ca4dbd5d31bf"),
                        graph,
                        "Nick",
                        "Jonas.Sophie.Nick",
                        3
                    ),
                )
            )

        val actual = sut.getUpperHierarchy(graphId, "Nick", 5)

        assertThat(actual.toString()).isEqualTo(JSONObject(tree).toString())
    }
}