package com.example.finalproject

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import java.util.regex.Pattern

class MovieDetailsActivity : AppCompatActivity() {
    lateinit var BackBut: ImageView
    lateinit var AddButton: ImageView
    private lateinit var databaseReference: DatabaseReference
    private lateinit var userMoviesReference: DatabaseReference
    private lateinit var youTubePlayerView: YouTubePlayerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_details)

        BackBut = findViewById(R.id.back)
        AddButton = findViewById(R.id.addButton)
        youTubePlayerView = findViewById(R.id.youtubePlayerView)

        // Initialize Firebase references
        databaseReference = FirebaseDatabase.getInstance().getReference("movies")
        userMoviesReference = FirebaseDatabase.getInstance().getReference("user_movies")

        // Retrieve movie details from the Intent
        val movie = intent.getSerializableExtra("movie") as Movie?

        // Set up UI elements with movie details
        val movieName: TextView = findViewById(R.id.MovieName)
        val movieDescription: TextView = findViewById(R.id.MovieDescS)

        movieName.text = movie?.name.orEmpty()
        movieDescription.text = movie?.description.orEmpty()

        BackBut.setOnClickListener {
            finish()
        }

        AddButton.setOnClickListener {
            movie?.let {
                addMovieToWatchlist(it.getMovieId())
            }
        }

        // Load YouTube video
        loadYouTubeVideo(movie?.trailer)
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
                        Toast.makeText(
                            this@MovieDetailsActivity,
                            "Movie already added to watchlist",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        // Movie is not in the watchlist, add it
                        userMovieRef.setValue(true)
                        Toast.makeText(this@MovieDetailsActivity, "Added to Watchlist", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                    Toast.makeText(
                        this@MovieDetailsActivity,
                        "Error checking watchlist",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })

        } else {
            // Handle the case where the user is not authenticated
            Toast.makeText(this@MovieDetailsActivity, "User not authenticated", Toast.LENGTH_SHORT).show()
        }
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
