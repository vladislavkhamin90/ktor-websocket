package com.example

import io.ktor.server.websocket.DefaultWebSocketServerSession

data class ClientConnection(
    val userId: String,
    val username: String,
    val session: DefaultWebSocketServerSession
)