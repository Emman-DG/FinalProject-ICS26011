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


        databaseReference = FirebaseDatabase.getInstance().getReference("movies")
        userMoviesReference = FirebaseDatabase.getInstance().getReference("user_movies")


        val movie = intent.getSerializableExtra("movie") as Movie?


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


            userMovieRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {

                        Toast.makeText(
                            this@MovieDetailsActivity,
                            "Movie already added to watchlist",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {

                        userMovieRef.setValue(true)
                        Toast.makeText(this@MovieDetailsActivity, "Added to Watchlist", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        this@MovieDetailsActivity,
                        "Error checking watchlist",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })

        }
    }

    private fun loadYouTubeVideo(videoUrl: String?) {
        val videoId = extractYouTubeVideoId(videoUrl)

        youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                if (!videoId.isNullOrBlank()) {
                    youTubePlayer.loadVideo(videoId, 0f)
                } else {
                    Toast.makeText(this@MovieDetailsActivity, "Error: cannot play the video", Toast.LENGTH_SHORT).show()
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
