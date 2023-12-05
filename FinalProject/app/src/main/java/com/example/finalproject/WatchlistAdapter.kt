package com.example.finalproject

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

// WatchlistAdapter.kt
class WatchlistAdapter(private val onRemoveClickListener: (WatchlistItem) -> Unit) :
    RecyclerView.Adapter<WatchlistAdapter.WatchlistViewHolder>() {

    private var watchlistItems: List<WatchlistItem> = emptyList()
    private var filteredItems: List<WatchlistItem> = emptyList()

    // ViewHolder
    inner class WatchlistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val movieNameTextView: TextView = itemView.findViewById(R.id.movieNameTextView)
        val movieImageView: ImageView = itemView.findViewById(R.id.movieImageView)
        val removeButton: Button = itemView.findViewById(R.id.removeButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WatchlistViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.watchlist_item, parent, false)
        return WatchlistViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: WatchlistViewHolder, position: Int) {
        val currentItem = filteredItems[position]

        // Bind data to views
        holder.movieNameTextView.text = currentItem.movieName
        // Load image using Glide or your preferred image-loading library
        Glide.with(holder.itemView.context)
            .load(currentItem.movieImage)
            .centerCrop()
            .placeholder(R.drawable.placeholder_image)
            .into(holder.movieImageView)

        // Implement remove button click listener
        holder.removeButton.setOnClickListener {
            // Handle remove button click
            // You can use currentItem.movieId to identify the movie to remove
            showConfirmationDialog(holder.itemView.context, currentItem)
        }
    }

    private fun showConfirmationDialog(context: Context, item: WatchlistItem) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Confirmation")
            .setMessage("Are you sure you want to remove this movie from your watchlist?")
            .setPositiveButton("Yes") { _, _ ->
                // User clicked Yes
                onRemoveClickListener.invoke(item)
                // Remove the item from the dataset
                watchlistItems = watchlistItems.filter { it != item }
                // Update the filtered items based on the latest watchlist
                filterItems()
                // Notify the adapter about the change
                notifyDataSetChanged()
            }
            .setNegativeButton("No") { _, _ ->
                // User clicked No, do nothing
            }
            .show()
    }

    override fun getItemCount(): Int {
        return filteredItems.size
    }

    // Update watchlistItems when data changes
    fun setWatchlistItems(items: List<WatchlistItem>) {
        watchlistItems = items
        filterItems()
        notifyDataSetChanged()
    }

    private fun filterItems() {
        filteredItems = watchlistItems
    }



}
