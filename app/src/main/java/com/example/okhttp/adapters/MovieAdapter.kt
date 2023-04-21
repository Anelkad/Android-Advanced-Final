package com.example.okhttp.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.okhttp.R
import com.example.okhttp.models.MovieItem

class MovieAdapter(
    private val context: Activity,
    private val arrayList: List<MovieItem>
    ):
    ArrayAdapter<MovieItem>(context, R.layout.list_item, arrayList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val imageUrl = "https://image.tmdb.org/t/p/w500"

        val inflater : LayoutInflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.list_item, null)

        val imageView = view.findViewById<ImageView>(R.id.imageView)
        val title = view.findViewById<TextView>(R.id.title)
        val description = view.findViewById<TextView>(R.id.description)

        title.text = arrayList[position].title
        description.text = "Рейтинг: ${arrayList[position].vote_average.toString()}\n" +
                "Премьера: ${arrayList[position].release_date}"
        val imagePath = arrayList[position].poster_path

        Glide
            .with(context)
            .load(imageUrl+imagePath)
            .placeholder(R.drawable.progress_animation)
            .error(R.drawable.baseline_image_24)
            .into(imageView);

        return view
    }
}