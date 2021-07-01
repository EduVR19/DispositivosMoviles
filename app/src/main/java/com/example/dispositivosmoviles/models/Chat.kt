package com.example.dispositivosmoviles.models

data class Chat(
    var id: String = "",
    var name: String = "",
    var Usuarios: List<String> = emptyList()
)