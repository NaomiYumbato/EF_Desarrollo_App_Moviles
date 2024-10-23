package com.example.ef_naomi_yumbato.model

data class Users(
    var id: Int,
    var email: String,
    var password: String,
    var name: String,
    var role: String,
    var avatar: String,
    var creationAt: String,
    var updatedAt: String
) {
    constructor() : this(
        id = 0,
        email = "",
        password = "",
        name = "",
        role = "",
        avatar = "",
        creationAt = "",
        updatedAt = ""
    )
}
