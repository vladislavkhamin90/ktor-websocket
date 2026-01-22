package com.example

import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.serialization.json.Json

private val json = Json {
    ignoreUnknownKeys = true
    encodeDefaults = true
}

fun Application.configureSockets() {

    install(WebSockets)

    routing {
        webSocket("/chat") {

            val userId = call.request.queryParameters["userId"]
                ?: return@webSocket close(
                    CloseReason(
                        CloseReason.Codes.CANNOT_ACCEPT,
                        "No userId"
                    )
                )

            val username = call.request.queryParameters["username"]
                ?: return@webSocket close(
                    CloseReason(
                        CloseReason.Codes.CANNOT_ACCEPT,
                        "No username"
                    )
                )

            val connection = ClientConnection(
                userId = userId,
                username = username,
                session = this
            )

            ChatRoom.join(connection)

            try {
                for (frame in incoming) {
                    if (frame is Frame.Text) {
                        val text = frame.readText()

                        val message =
                            json.decodeFromString<NetworkMessage>(text)

                        println("📩 ${message.username}: ${message.text}")

                        ChatRoom.broadcast(message)
                    }
                }
            } catch (e: Exception) {
                println("WebSocket error: ${e.message}")
            } finally {
                ChatRoom.leave(userId)
            }
        }
    }
}
