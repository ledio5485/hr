package io.led.hr.api

import io.led.hr.core.GraphService
import io.led.hr.logger
import org.json.JSONObject
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.math.BigInteger
import java.util.*

@RestController
class GraphRestController(private val graphService: GraphService) : GraphResource {

    override fun createGraph(request: String?): ResponseEntity<String> = ResponseEntity.status(HttpStatus.CREATED)
            .body(graphService.createGraph(JSONObject(request)).toString())
            .also { logger.info { "Created the tree $it" } }

    override fun getGraph(graphId: UUID?): ResponseEntity<String> =
        ResponseEntity.of(graphService.buildTree(graphId!!).toOptString())
            .also { logger.info { "Returned the tree $it" } }

    override fun getUpperHierarchy(graphId: UUID?, node: String?, level: BigInteger): ResponseEntity<String> =
        ResponseEntity.of(graphService.getUpperHierarchy(graphId!!, node!!, level).toOptString())
            .also { logger.info { "Filtered the tree $it" } }
}
