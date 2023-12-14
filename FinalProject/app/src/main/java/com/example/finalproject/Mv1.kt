package com.example.finalproject

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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

class Mv1 : AppCompatActivity() {

    private lateinit var database: FirebaseDatabase
    private lateinit var userMoviesReference: DatabaseReference
    private lateinit var movieName: TextView
    private lateinit var movieDesc: TextView
    private lateinit var addToWatchList: ImageView
    private lateinit var youTubePlayerView: YouTubePlayerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mv1)

        database = FirebaseDatabase.getInstance()
        userMoviesReference = database.reference.child("user_movies")
        movieName = findViewById(R.id.Movie01)
        movieDesc = findViewById(R.id.MovieDesc1)
        addToWatchList = findViewById(R.id.addButton)
        youTubePlayerView = findViewById(R.id.youtubePlayerView1)

        // Retrieve movie details from Firebase
        val movieId = "1" // ID of the first movie
        retrieveMovieDetails(movieId)

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
                    movieName.text = movie.name
                    movieDesc.text = movie.description

                    loadYouTubeVideo(movie.trailer)
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun addMovieToWatchlist(movieId: String) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            val userId = currentUser.uid
            val userMovieRef = userMoviesReference.child(userId).child(movieId)

            userMovieRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        Toast.makeText(this@Mv1, "Movie already added to watchlist", Toast.LENGTH_SHORT).show()
                    } else {
                        userMovieRef.setValue(true)
                        Toast.makeText(this@Mv1, "Added to Watchlist", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@Mv1, "Error checking watchlist", Toast.LENGTH_SHORT).show()
                }
            })

        } else {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show()
        }
    }

    // Home
    fun navigateToHome(view: View) {
        val intent = Intent(this, Home::class.java)
        startActivity(intent)
    }

    // Search
    fun navigateToSearch(view: View) {
        val intent = Intent(this, Search::class.java)
        startActivity(intent)
    }

    // Profile
    fun navigateToProfile(view: View) {
        val intent = Intent(this, Profile::class.java)
        startActivity(intent)
    }

    private fun loadYouTubeVideo(videoUrl: String?) {
        val videoId = extractYouTubeVideoId(videoUrl)
        youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                if (!videoId.isNullOrBlank()) {
                    youTubePlayer.loadVideo(videoId, 0f)
                } else {
                }
            }
        })
    }

    private fun extractYouTubeVideoId(videoUrl: String?): String? {
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
