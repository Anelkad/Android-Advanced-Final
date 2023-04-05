package com.example.okhttp

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

class MovieAdapter(
    private val context: Activity,
    private val arrayList: List<Movie>):
    ArrayAdapter<Movie>(context,R.layout.list_item, arrayList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val imageUrl = "https://image.tmdb.org/t/p/w500/"

        val inflater : LayoutInflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.list_item, null)

        val imageView = view.findViewById<ImageView>(R.id.imageView)
        val title = view.findViewById<TextView>(R.id.title)
        val description = view.findViewById<TextView>(R.id.description)

        title.text = arrayList[position].original_title
        description.text = arrayList[position].overview
        val imagePath = arrayList[position].poster_path

        Glide.with(context).load(imageUrl+imagePath).into(imageView);

        return view
    }
}