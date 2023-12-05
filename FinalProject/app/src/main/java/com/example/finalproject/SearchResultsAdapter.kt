package com.example.finalproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class SearchResultsAdapter(private var searchResults: List<WatchlistItem>? = null) :
    RecyclerView.Adapter<SearchResultsAdapter.SearchResultViewHolder>() {

    inner class SearchResultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val searchResultImageView: ImageView = itemView.findViewById(R.id.searchResultImageView)
        val searchResultNameTextView: TextView = itemView.findViewById(R.id.searchResultNameTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.search_result_item, parent, false)
        return SearchResultViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SearchResultViewHolder, position: Int) {
        val currentItem = searchResults?.get(position)

        // Bind data to views
        currentItem?.let {
            Glide.with(holder.itemView.context)
                .load(it.movieImage)
                .centerCrop()
                .placeholder(R.drawable.placeholder_image)
                .into(holder.searchResultImageView)

            holder.searchResultNameTextView.text = it.movieName
        }
    }

    override fun getItemCount(): Int {
        return searchResults?.size ?: 0
    }

    // Update searchResults when data changes
    fun setSearchResults(results: List<WatchlistItem>?) {
        searchResults = results
        notifyDataSetChanged()
    }

    // Search function
    fun search(query: String) {
        searchResults = if (query.isEmpty()) {
            // If the query is empty, show all items
            null
        } else {
            // Filter items based on the query (customize based on your requirements)
            searchResults?.filter { it.movieName.contains(query, true) }
        }

        notifyDataSetChanged()
    }
}

