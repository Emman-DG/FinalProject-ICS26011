package com.example.finalproject
 class Movie {
    var description: String? = null
    var image: String? = null
    var name: String? = null

    constructor() {
        // Default constructor required for Firebase
    }

    constructor(description: String?, image: String?, name: String?) {
        this.description = description
        this.image = image
        this.name = name
    }
}

