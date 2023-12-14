package com.example.finalproject

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class Profile : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var watchlistAdapter: WatchlistAdapter

    private lateinit var userId: String
    private lateinit var database: FirebaseDatabase
    private lateinit var userMoviesRef: DatabaseReference
    private lateinit var LogOut: Button
    private lateinit var usernameTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        database = FirebaseDatabase.getInstance()
        userMoviesRef = database.reference.child("user_movies").child(userId)

        LogOut = findViewById(R.id.button)
        LogOut.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            Toast.makeText(
                baseContext,
                "Logged Out",
                Toast.LENGTH_SHORT
            ).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Initialize usernameTextView
        usernameTextView = findViewById(R.id.usernameTextView)

        // Initialize RecyclerView and WatchlistAdapter
        recyclerView = findViewById(R.id.watchlistRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        watchlistAdapter = WatchlistAdapter { item -> removeMovieFromWatchlist(item) }
        recyclerView.adapter = watchlistAdapter

        // Retrieve and display the username and watchlist
        retrieveAndDisplayUsername()
        retrieveAndDisplayWatchlist()
    }

    // Main
    fun navigateToMain(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    // Home
    fun navigateToHome(view: View){
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
    }
    // Search
    fun navigateToSearch(view: View) {
        val intent = Intent(this, Search::class.java)
        startActivity(intent)
    }

    // Profile

    private fun retrieveAndDisplayUsername() {
        val userRef = database.reference.child("users").child(userId)
        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val username = snapshot.child("username").getValue(String::class.java)
                usernameTextView.text = "$username"
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle the error
                Toast.makeText(
                    baseContext,
                    "Failed to retrieve username",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun retrieveAndDisplayWatchlist() {
        val watchlistItems = mutableListOf<WatchlistItem>()
        userMoviesRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (movieSnapshot in snapshot.children) {
                    val movieId = movieSnapshot.key
                    if (movieId != null) {
                        // Get additional details from the "movies" node
                        val movieDetailsRef = database.reference.child("movies").child(movieId)
                        movieDetailsRef.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(movieDetailsSnapshot: DataSnapshot) {
                                val movieName = movieDetailsSnapshot.child("name").getValue(String::class.java)
                                val movieImage = movieDetailsSnapshot.child("image").getValue(String::class.java)
                                if (movieName != null && movieImage != null) {
                                    val watchlistItem = WatchlistItem(movieId, movieName, movieImage)
                                    watchlistItems.add(watchlistItem)

                                    // Update the WatchlistAdapter with the retrieved items
                                    watchlistAdapter.setWatchlistItems(watchlistItems)
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                // Handle the error
                                Log.e("ProfileActivity", "Error retrieving movie details: ${error.message}")
                            }
                        })
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle the error
                Log.e("ProfileActivity", "Error retrieving watchlist: ${error.message}")
                Toast.makeText(
                    baseContext,
                    "Failed to retrieve watchlist",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
    private fun removeMovieFromWatchlist(item: WatchlistItem) {
        val movieId = item.movieId

        // Get the current user ID
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        // Check if the user ID is not null
        if (userId != null) {
            // Reference to the user's movies node
            val userMoviesRef = database.reference.child("user_movies").child(userId)

            // Remove the movie with the specified ID from the watchlist
            userMoviesRef.child(movieId).removeValue()

            // Notify the adapter about the removal
            watchlistAdapter.notifyDataSetChanged()

            Toast.makeText(this, "Removed from Watchlist", Toast.LENGTH_SHORT).show()
        } else {
            // Handle the case where the user is not authenticated
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show()
        }
    }

}
