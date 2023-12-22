package com.example.uas

import android.content.Intent
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

class UserAdapter (private val itemList : ArrayList<Movie>, private val movieadminCollectionRef: CollectionReference) : RecyclerView.Adapter<UserAdapter.MyViewHolder>() {

        class MyViewHolder(itemList : View) : RecyclerView.ViewHolder(itemList){
            val title : TextView = itemList.findViewById(R.id.judul1)
            val image : ImageView = itemList.findViewById(R.id.film1)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.list_movie_users,parent,false)
            return MyViewHolder(view)
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            val currentItem = itemList[position]
            holder.title.text = currentItem.judul

            // Load and display the image using Glide
            Glide.with(holder.itemView.context)
                .load(currentItem.url)
                .into(holder.image)

            holder.itemView.setOnClickListener{
                val intent = Intent(holder.itemView.context,DetailsMovie::class.java)
                intent.putExtra("title",currentItem.judul)
                intent.putExtra("genre",currentItem.genre)
                intent.putExtra("description",currentItem.desc)
                intent.putExtra("imgId", currentItem.url)
                holder.itemView.context.startActivity(intent)
            }
        }

        override fun getItemCount(): Int {
            return itemList.size
        }

}