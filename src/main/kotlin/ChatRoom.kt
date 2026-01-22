package com.example

import io.ktor.websocket.*
import kotlinx.serialization.builtins.serializer
import java.util.concurrent.ConcurrentHashMap
import kotlinx.serialization.json.Json

private val json = Json {
    encodeDefaults = true
}

object ChatRoom {

    private val connections =
        ConcurrentHashMap<String, ClientConnection>() // userId -> connection

    fun join(connection: ClientConnection) {
        connections[connection.userId] = connection
        println("➕ ${connection.username} joined. Online: ${connections.size}")
    }

    fun leave(userId: String) {
        val removed = connections.remove(userId)
        if (removed != null) {
            println("➖ ${removed.username} left. Online: ${connections.size}")
        }
    }

    suspend fun broadcast(message: NetworkMessage) {
        val jsonText = json.encodeToString(message)

        connections.values.forEach { connection ->
            try {
                connection.session.send(Frame.Text(jsonText))
            } catch (e: Exception) {
                println("Failed to send to ${connection.username}")
            }
        }
    }
}
