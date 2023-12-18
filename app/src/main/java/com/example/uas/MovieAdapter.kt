package com.example.uas

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.uas.databinding.ListMovieBinding

class MovieAdapter(
    private var listMovie: List<Movie>,
    private val onEditClickListener: OnEditClickListener
) : RecyclerView.Adapter<MovieAdapter.ItemMovieViewHolder>() {

    interface OnEditClickListener {
        fun onEditClick(movie: Movie)
        fun onDeleteClick(movie: Movie)
    }

    inner class ItemMovieViewHolder(private val binding: ListMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onEditClickListener.onEditClick(listMovie[position])
                }
            }

            binding.iconEdit.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onEditClickListener.onEditClick(listMovie[position])
                }
            }

            binding.iconDelete.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onEditClickListener.onDeleteClick(listMovie[position])
                }
            }
        }

        fun bind(movie: Movie) {
            with(binding) {
                textJudul.text = movie.judul
                textGenre.text = movie.genre
                textDesc.text = movie.desc
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemMovieViewHolder {
        val binding = ListMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemMovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemMovieViewHolder, position: Int) {
        holder.bind(listMovie[position])
    }

    override fun getItemCount(): Int {
        return listMovie.size
    }

    fun updateData(newList: List<Movie>) {
        listMovie = newList
        notifyDataSetChanged()
    }
}
