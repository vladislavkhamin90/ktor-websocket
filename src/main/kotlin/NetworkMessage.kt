package com.example

import kotlinx.serialization.Serializable

@Serializable
data class NetworkMessage(
    val userId: String,
    val username: String,
    val text: String,
    val timestamp: Long
)
