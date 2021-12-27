package io.led.hr.core

import org.jgrapht.alg.cycle.CycleDetector
import org.json.JSONObject
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class GraphFactoryTest {
    private val graphFactory = GraphFactory()

    @Test
    internal fun `no cycles are allowed`() {
        val json = """
            {
                "Pete": "Nick",
                "Barbara": "Nick",
                "Nick": "Sophie",
                "Sophie": "Jonas"
            }
        """.trimIndent()

        val actual = graphFactory.create(JSONObject(json))

        assertTrue(CycleDetector(actual).detectCycles().not())

        actual.vertexSet()
            .filter { key: String? -> actual.incomingEdgesOf(key).size == 0 }
            .also { assertTrue(it.size == 1) { "Multiple top supervisors are not allowed" } }
    }
}