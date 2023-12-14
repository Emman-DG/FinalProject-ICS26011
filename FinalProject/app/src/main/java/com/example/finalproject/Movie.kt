package com.example.finalproject

import java.io.Serializable

class Movie : Serializable {
    var description: String? = null
    var image: String? = null
    var name: String? = null
    val trailer: String? = null

    constructor() {

    }

    constructor(description: String?, image: String?, name: String?) {
        this.description = description
        this.image = image
        this.name = name
    }

    fun getMovieId(): String {
        return name.orEmpty()
    }
}