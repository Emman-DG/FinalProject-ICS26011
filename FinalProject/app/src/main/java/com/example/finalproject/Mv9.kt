package com.example.finalproject

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.MediaController
import android.widget.TextView
import android.widget.Toast
import android.widget.VideoView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Mv9 : AppCompatActivity() {
    private lateinit var database: FirebaseDatabase
    private lateinit var userMoviesReference: DatabaseReference
    private lateinit var movieName: TextView
    private lateinit var movieDesc: TextView
    private lateinit var addToWatchList: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mv9)

        database = FirebaseDatabase.getInstance()
        userMoviesReference = database.reference.child("user_movies")
        movieName = findViewById(R.id.MovieName09)
        movieDesc = findViewById(R.id.MovieDesc09)
        addToWatchList = findViewById(R.id.addButton9)

        val movieId = "9" // ID of the second movie
        retrieveMovieDetails(movieId)
        //Trailer
        val videoView = findViewById<VideoView>(R.id.Mv9)
        val packageName = "android.resource://" + getPackageName() + "/" + R.raw.theupside
        val uri = Uri.parse(packageName)
        videoView.setVideoURI(uri)

        val mediaController = MediaController(this)
        videoView.setMediaController(mediaController)

        addToWatchList.setOnClickListener {
            addMovieToWatchlist(movieId)
        }
    }

    private fun retrieveMovieDetails(movieId: String) {
        val movieReference = database.reference.child("movies").child(movieId)
        movieReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val movie = snapshot.getValue(Movie::class.java)
                if (movie != null) {
                    // Display movie details
                    movieName.text = movie.name
                    movieDesc.text = movie.description
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }
    private fun addMovieToWatchlist(movieId: String) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            val userId = currentUser.uid
            val userMovieRef = userMoviesReference.child(userId).child(movieId)

            // Check if the movie is already in the user's watchlist
            userMovieRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        // Movie is already in the watchlist
                        Toast.makeText(this@Mv9, "Movie already added to watchlist", Toast.LENGTH_SHORT).show()
                    } else {
                        // Movie is not in the watchlist, add it
                        userMovieRef.setValue(true)
                        Toast.makeText(this@Mv9, "Added to Watchlist", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                    Toast.makeText(this@Mv9, "Error checking watchlist", Toast.LENGTH_SHORT).show()
                }
            })

        } else {
            // Handle the case where the user is not authenticated
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show()
        }
    }
    //Home
    fun navigateToHome(view: View) {
        val intent = Intent(this, Home::class.java);
        startActivity(intent)
    }
    //Search
    fun navigateToSearch(view: View) {
        val intent = Intent(this, Search::class.java);
        startActivity(intent)
    }
    //Profile
    fun navigateToProfile(view: View) {
        val intent = Intent(this, Profile::class.java);
        startActivity(intent)
    }
}