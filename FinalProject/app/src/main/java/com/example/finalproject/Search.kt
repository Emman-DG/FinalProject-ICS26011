package com.example.finalproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class Search : AppCompatActivity() {

    private lateinit var searchEditText: EditText
    private lateinit var searchResultsRecyclerView: RecyclerView
    private lateinit var searchResultsAdapter: SearchResultsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        // Initialize views
        searchEditText = findViewById(R.id.searchNameMovie)
        searchResultsRecyclerView = findViewById(R.id.searchRecyclerView)

        // Initialize RecyclerView and SearchResultsAdapter
        searchResultsAdapter = SearchResultsAdapter()
        searchResultsRecyclerView.layoutManager = LinearLayoutManager(this)
        searchResultsRecyclerView.adapter = searchResultsAdapter

        // Add a TextWatcher to the searchEditText to perform search on text changes
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not needed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Perform search when text changes
                searchResultsAdapter.search(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
                // Not needed
            }
        })
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
}
