package com.example.uas

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.uas.database.Movie
import com.google.firebase.firestore.CollectionReference


class MovieAdapter(
    private val itemList: ArrayList<Movie>,
    private val movieadminCollectionRef: CollectionReference,
) : RecyclerView.Adapter<MovieAdapter.MyViewHolder>() {

    override fun getItemCount(): Int {
        return itemList.size
    }

    class MyViewHolder(itemList: View) : RecyclerView.ViewHolder(itemList) {
        val title: TextView = itemList.findViewById(R.id.text_judul_admin)
        val genre: TextView = itemList.findViewById(R.id.text_genre_admin)
        val desc: TextView = itemList.findViewById(R.id.text_desc_admin)
        val image: ImageView = itemList.findViewById(R.id.imageViewAdmin)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_movie, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = itemList[position]
        holder.title.text = currentItem.judul
        holder.genre.text = currentItem.genre
        holder.desc.text = currentItem.desc


        Glide.with(holder.itemView.context)
            .load(currentItem.url)
            .into(holder.image)

        holder.itemView.findViewById<ImageView>(R.id.icon_edit).setOnClickListener {
            val intent = Intent(holder.itemView.context, FormEditMovie::class.java)
            intent.putExtra("title", currentItem.judul)
            intent.putExtra("genre", currentItem.genre)
            intent.putExtra("description", currentItem.desc)
            intent.putExtra("imgId", currentItem.url)
            holder.itemView.context.startActivity(intent)
        }

        holder.itemView.findViewById<ImageView>(R.id.icon_delete).setOnClickListener {
            deleteItem(currentItem.documentId)
        }
    }

    private fun deleteItem(docId: String) {
        // Delete the document from Firestore
        movieadminCollectionRef.document(docId)
            .delete()
            .addOnSuccessListener {

                // After successful deletion, listen for updates
                movieadminCollectionRef.addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        Log.e("MovieAdapter", "Listen failed.", e)
                        return@addSnapshotListener
                    }

                    if (snapshot != null && !snapshot.isEmpty) {
                        itemList.clear()
                        for (document in snapshot.documents) {
                            val movie = document.toObject(Movie::class.java)
                            if (movie != null) {
                                itemList.add(movie)
                            }
                        }
                        notifyDataSetChanged() // Update the RecyclerView
                        Log.d("MovieAdapter", "Data retrieved successfully. Size: ${itemList.size}")
                    } else {
                        itemList.clear()
                        notifyDataSetChanged() // Update the RecyclerView for an empty list
                        Log.d("MovieAdapter", "No data found.")
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.e("MovieAdapter", "Error deleting document: $e")
            }
    }

}
