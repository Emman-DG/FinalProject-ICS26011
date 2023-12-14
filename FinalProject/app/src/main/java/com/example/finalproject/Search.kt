package com.example.finalproject

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class Search : AppCompatActivity() {

    private lateinit var searchView: SearchView
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchAdapter: SearchAdapter

    private val movies: MutableList<Movie> = mutableListOf()

    private val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("movies")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        // Initialize UI components
        searchView = findViewById(R.id.searchView)
        recyclerView = findViewById(R.id.searchRecyclerView)

        // Initialize RecyclerView and adapter
        searchAdapter = SearchAdapter(this, movies) { selectedMovie ->
            // Handle item click, open MovieDetailsActivity, and pass the selected movie
            val intent = Intent(this, MovieDetailsActivity::class.java)
            intent.putExtra("movie", selectedMovie)
            startActivity(intent)
        }

        // Set up the RecyclerView with a GridLayoutManager (2 movies per row)
        val layoutManager = GridLayoutManager(this, 2, RecyclerView.VERTICAL, false)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = searchAdapter

        // Set up Firebase Database query to fetch the list of movies
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Convert the dataSnapshot to a list of Movie objects
                val moviesList = dataSnapshot.children.map { it.getValue(Movie::class.java)!! }

                // Update the RecyclerView adapter with the list of movies
                searchAdapter.updateMovies(moviesList)

                // Update the local movies list as well
                movies.clear()
                movies.addAll(moviesList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
            }
        })

        // Set up search functionality
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Handle search query submission if needed
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Log the search query
                Log.d("SearchActivity", "Search Query: $newText")

                // Log the list of movies
                Log.d("SearchActivity", "All Movies: $movies")

                // Filter the list of movies based on user input, using case-insensitive comparison
                val filteredMovies = movies.filter {
                    val movieName = it.name.orEmpty()
                    val isMatch = movieName.contains(newText.orEmpty(), ignoreCase = true)

                    // Log the movie name and whether it's a match
                    Log.d("SearchActivity", "Movie: $movieName, Is Match: $isMatch")

                    isMatch
                }

                // Log the filtered movies
                Log.d("SearchActivity", "Filtered Movies: $filteredMovies")

                // Update the RecyclerView adapter with the filtered list
                searchAdapter.updateMovies(filteredMovies)

                return true
            }

        })
    }
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
