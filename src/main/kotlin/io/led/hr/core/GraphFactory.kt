package io.led.hr.core

import org.jgrapht.Graph
import org.jgrapht.graph.DefaultEdge
import org.jgrapht.graph.DirectedAcyclicGraph
import org.json.JSONObject
import org.springframework.stereotype.Component

@Component
class GraphFactory {
    fun create(input: JSONObject): Graph<String, DefaultEdge> =
        with(DirectedAcyclicGraph<String, DefaultEdge>(DefaultEdge::class.java)) {
            apply {
                check(input.isEmpty.not()) { "Input should not be empty"}
                input.toMap().forEach { (employee: String, supervisor: Any) -> addEdgesAndVertex(employee, supervisor) }
                checkForMultipleRoots()
            }
        }
}
