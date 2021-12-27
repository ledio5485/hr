package io.led.hr.core

import io.led.hr.logger
import org.jgrapht.Graph
import org.jgrapht.GraphPath
import org.jgrapht.alg.shortestpath.DijkstraShortestPath
import org.jgrapht.graph.DefaultEdge
import org.jgrapht.graph.DirectedAcyclicGraph

fun DirectedAcyclicGraph<String, DefaultEdge>.addEdgesAndVertex(node: String, parent: Any) {
    logger.info { "adding $node to $parent" }
    addVertex(node)
    addVertex(parent as String)
    addEdge(parent, node)
}

fun Graph<String, DefaultEdge>.findShortestPathBetween(source: String, sink: String): GraphPath<String, DefaultEdge> =
    DijkstraShortestPath.findPathBetween(this, source, sink)

private fun Graph<String, DefaultEdge>.getRoots() = vertexSet().filter { key -> incomingEdgesOf(key).size == 0 }

fun Graph<String, DefaultEdge>.checkForMultipleRoots() = apply { check(getRoots().size == 1) { "Multiple roots are not allowed: ${getRoots()}." } }

fun Graph<String, DefaultEdge>.getRoot(): String = checkForMultipleRoots().getRoots().first()
