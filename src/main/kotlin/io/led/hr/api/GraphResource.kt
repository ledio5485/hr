package io.led.hr.api

import io.led.hr.api.GraphResource.Companion.GRAPHS_URI
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.math.BigInteger
import java.util.*
import javax.validation.Valid
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Positive

@RequestMapping(GRAPHS_URI)
@Validated
interface GraphResource {
    companion object {
        const val GRAPHS_URI = "/api/graphs"
        const val GRAPH_URI = "/{graphId}"
        const val FILTER_URI = GRAPH_URI.plus("/filter")
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    fun createGraph(@RequestBody @Valid @NotBlank(message = "request is mandatory") request: String?): ResponseEntity<String>

    @GetMapping(GRAPH_URI)
    @ResponseBody
    fun getGraph(@PathVariable("graphId") @Valid @NotNull(message = "graphId is mandatory") graphId: UUID?): ResponseEntity<String>

    @GetMapping(FILTER_URI)
    @ResponseBody
    fun getUpperHierarchy(
        @PathVariable("graphId") @Valid @NotNull(message = "graphId is mandatory") graphId: UUID?,
        @RequestParam("name") @Valid @NotBlank(message = "name is mandatory") node: String?,
        @RequestParam(name = "level", defaultValue = "3") @Valid @Positive(message = "level should be a positive number") level: BigInteger
    ): ResponseEntity<String>
}