package com.example.ef_naomi_yumbato.model

data class UserFirebase(
    var id: Int,
    var email: String,
    var name: String
) {
    constructor() : this(
        id = 0,
        email = "",
        name = ""
    )
}
