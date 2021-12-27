package io.led.hr.persistence

import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "graph")
class GraphEntity(

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false, columnDefinition = "BINARY(16)")
    val id: UUID? = null,

    @Column(nullable = false)
    val name: String,

    @OneToMany(fetch = FetchType.EAGER, cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "graph_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    var nodes: List<NodeEntity> = listOf()
)
