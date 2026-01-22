package com.example

import io.ktor.server.application.Application
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(
        Netty,
        environment = applicationEnvironment {}
    ).start(wait = true)
}


fun Application.module() {
    configureSockets()
    configureRouting()
}