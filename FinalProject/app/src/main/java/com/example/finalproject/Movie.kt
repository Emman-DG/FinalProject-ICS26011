package com.example.finalproject

import java.io.Serializable

class Movie : Serializable {
    var description: String? = null
    var image: String? = null
    var name: String? = null
    val trailer: String? = null

    constructor() {
        // Default constructor required for Firebase
    }

    constructor(description: String?, image: String?, name: String?) {
        this.description = description
        this.image = image
        this.name = name
    }

    // Use the name as the unique identifier
    fun getMovieId(): String {
        return name.orEmpty()
    }
}