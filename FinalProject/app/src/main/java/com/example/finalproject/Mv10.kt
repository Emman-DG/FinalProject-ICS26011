package com.example.finalproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import java.util.regex.Pattern

class Mv10 : AppCompatActivity() {
    private lateinit var database: FirebaseDatabase
    private lateinit var userMoviesReference: DatabaseReference
    private lateinit var movieName: TextView
    private lateinit var movieDesc: TextView
    private lateinit var addToWatchList: ImageView
    private lateinit var youTubePlayerView: YouTubePlayerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mv10)

        // Initialization
        database = FirebaseDatabase.getInstance()
        userMoviesReference = database.reference.child("user_movies")
        movieName = findViewById(R.id.MovieName10)
        movieDesc = findViewById(R.id.MovieDesc10)
        addToWatchList = findViewById(R.id.addButton10)
        youTubePlayerView = findViewById(R.id.youtubePlayerView)

        val movieId = "10" // ID of the second movie
        retrieveMovieDetails(movieId)

        //Trailer




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
                    loadYouTubeVideo(movie.trailer)
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
                        Toast.makeText(this@Mv10, "Movie already added to watchlist", Toast.LENGTH_SHORT).show()
                    } else {
                        // Movie is not in the watchlist, add it
                        userMovieRef.setValue(true)
                        Toast.makeText(this@Mv10, "Added to Watchlist", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                    Toast.makeText(this@Mv10, "Error checking watchlist", Toast.LENGTH_SHORT).show()
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
    private fun loadYouTubeVideo(videoUrl: String?) {
        // Extract video ID from the YouTube URL
        val videoId = extractYouTubeVideoId(videoUrl)

        // Initialize the YouTubePlayerView
        youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                // Check if videoId is not null before loading the video
                if (!videoId.isNullOrBlank()) {
                    // Load the video with autoplay set to true
                    youTubePlayer.loadVideo(videoId, 0f)
                } else {
                    // Handle the case when videoId is null or blank
                    // You can show an error message or take appropriate action
                }
            }
        })
    }

    private fun extractYouTubeVideoId(videoUrl: String?): String? {
        // Extract video ID from the YouTube URL
        val pattern = "(?<=youtu.be/|watch\\?v=|/videos/|embed\\/|youtu.be\\/|\\/v\\/|\\/e\\/|watch\\?v=|\\/videos\\/|embed\\/|youtu.be\\/|\\/v\\/|\\/e\\/|watch\\?v=|\\/videos\\/|embed\\/|youtu.be\\/|\\/v\\/|\\/e\\/)\\w+"
        val compiledPattern = Pattern.compile(pattern)
        val matcher = compiledPattern.matcher(videoUrl.orEmpty())

        return if (matcher.find()) {
            matcher.group()
        } else {
            null
        }
    }
}