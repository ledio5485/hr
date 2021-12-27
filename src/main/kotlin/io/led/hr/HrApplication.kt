package io.led.hr

import mu.KotlinLogging
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

val logger = KotlinLogging.logger {}

@SpringBootApplication
class HrApplication

fun main(args: Array<String>) {
    runApplication<HrApplication>(*args)
}
