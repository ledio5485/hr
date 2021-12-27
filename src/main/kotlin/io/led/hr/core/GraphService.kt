package io.led.hr.core

import io.led.hr.persistence.GraphEntity
import io.led.hr.persistence.GraphRepository
import io.led.hr.persistence.NodeEntity
import io.led.hr.persistence.NodeRepository
import org.apache.commons.lang3.RandomStringUtils
import org.jgrapht.Graph
import org.jgrapht.graph.DefaultEdge
import org.json.JSONObject
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class GraphService(
    private val graphBuilder: GraphFactory,
    private val graphRepository: GraphRepository,
    private val nodeRepository: NodeRepository
) {

    @Transactional
    fun createGraph(request: JSONObject): JSONObject = with(graphBuilder.create(request)) {
        val graphEntity = GraphEntity(name = RandomStringUtils.randomAlphabetic(10))
            .also { graphEntity ->
                graphEntity.nodes = buildNodes(this, graphEntity)
                graphRepository.save(graphEntity)
            }
        val savedGraph = graphRepository.save(graphEntity)
        //return buildTree(savedGraph.id!!)
        return JSONObject().put("id", savedGraph.id)
    }

    private fun buildNodes(graph: Graph<String, DefaultEdge>, graphEntity: GraphEntity): List<NodeEntity> =
        with(graph) {
            val root = getRoot()
            return vertexSet()
                .map { sink -> findShortestPathBetween(root, sink).vertexList.joinToString(separator = DOT) }
                .map { path ->
                    NodeEntity(
                        graph = graphEntity,
                        name = path.substring(path.lastIndexOf(DOT) + 1),
                        path = path
                    )
                }
        }

    fun buildTree(graphId: UUID): JSONObject = JSONObject().apply {
        nodeRepository.filterTree(graphId)
            .map { it.path }
            .sorted()
            .forEach { globalPath ->
                var localRoot = this
                globalPath.split(DOT)
                    .forEach { localPath -> localRoot = getNextLocalRoot(localRoot, localPath) }
            }
    }

    private fun getNextLocalRoot(localRoot: JSONObject, localPath: String): JSONObject =
        localRoot.optJSONObject(localPath) ?: localRoot.put(localPath, JSONObject())

    fun getUpperHierarchy(graphId: UUID, name: String, limit: Number): JSONObject =
        with(nodeRepository.getUpperHierarchy(graphId, name, limit)) {
            sortedByDescending { it.nLevel }
                .fold(JSONObject()) { path, treeEntry -> JSONObject().put(treeEntry.name, path) }
        }
}
