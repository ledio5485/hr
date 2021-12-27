package io.led.hr.persistence

import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.Type
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "node")
class NodeEntity(

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false, columnDefinition = "BINARY(16)")
    val id: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "graph_id", nullable = false)
    val graph: GraphEntity,

    @Column(nullable = false)
    val name: String,

    @Column(nullable = false, columnDefinition = "ltree")
    @Type(type = "io.led.hr.persistence.LTreeType")
    val path: String,

    @Column
    val nLevel: Int? = null
)
