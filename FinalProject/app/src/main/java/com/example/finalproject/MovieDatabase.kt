package com.example.finalproject

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MovieDatabase {

    private val userId: String = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val moviesRef: DatabaseReference = database.reference.child("movies")

    // Retrieve a specific movie based on its ID
    fun getMovie(movieId: String, callback: (Movie?) -> Unit) {
        moviesRef.child(movieId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val movie = snapshot.getValue(Movie::class.java)
                callback(movie)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle the error
                callback(null)
            }
        })
    }
}
