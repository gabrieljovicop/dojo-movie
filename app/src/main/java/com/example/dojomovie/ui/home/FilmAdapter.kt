package com.example.dojomovie.ui.home

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dojomovie.FilmDetail
import com.example.dojomovie.R
import com.example.dojomovie.Film
import com.squareup.picasso.Picasso

class FilmAdapter(private val context: Context, private val filmList: List<Film>) :
    RecyclerView.Adapter<FilmAdapter.FilmViewHolder>() {

    class FilmViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgFilm: ImageView = view.findViewById(R.id.imgFilm)
        val txtTitle: TextView = view.findViewById(R.id.txtFilmTitle)
        val txtPrice: TextView = view.findViewById(R.id.txtFilmPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_film, parent, false)
        return FilmViewHolder(view)
    }

    override fun onBindViewHolder(holder: FilmViewHolder, position: Int) {
        val film = filmList[position]
        holder.txtTitle.text = film.film_title
        holder.txtPrice.text = "Rp ${film.film_price}"
        Picasso.get().load(film.film_image).into(holder.imgFilm)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, FilmDetail::class.java)
            intent.putExtra("film_id", film.film_id)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = filmList.size
}