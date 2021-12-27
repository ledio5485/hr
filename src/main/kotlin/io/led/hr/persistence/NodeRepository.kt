package io.led.hr.persistence

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface NodeRepository : JpaRepository<NodeEntity, UUID> {

    @Query(value = "SELECT id, graph_id, name, path, nlevel(path) AS n_level FROM node WHERE graph_id = CAST(:graphId AS UUID)", nativeQuery = true)
    fun filterTree(@Param("graphId") graphId: UUID): List<NodeEntity>

    @Query(value = "SELECT id, graph_id, name, path, nlevel(path) AS n_level FROM node WHERE graph_id = CAST(:graphId AS UUID) AND path @> (SELECT path FROM node WHERE graph_id = CAST(:graphId AS UUID) AND name = :name) ORDER BY n_level DESC limit :limit", nativeQuery = true)
    fun getUpperHierarchy(@Param("graphId") graphId: UUID, @Param("name") name: String, @Param("limit") limit: Number): List<NodeEntity>
}