package com.example.marveldiscovery

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class MarvelAdapter (private val marvelNames: List<String>, private val marvelImages: List<String>, private val marvelInfo: List<String>) : RecyclerView.Adapter<MarvelAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val marvelImage: ImageView
        val marvelName: TextView
        val marvelInfo: TextView

        init {
            // Find our RecyclerView item's ImageView for future use
            marvelImage = view.findViewById(R.id.marvel_image)
            marvelName = view.findViewById(R.id.marvel_name)
            marvelInfo = view.findViewById(R.id.marvel_info)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.marvel_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(marvelImages[position])
            .centerCrop()
            .into(holder.marvelImage)

        holder.marvelName.text = marvelNames[position]
        if (marvelInfo[position] != "") {
            holder.marvelInfo.text = marvelInfo[position]
        } else {
            holder.marvelInfo.text = "Uh Oh! No description for this character :("
        }

        holder.marvelImage.setOnClickListener {
            Toast.makeText(holder.itemView.context, "Doggo at position $position clicked", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount() = marvelNames.size
}