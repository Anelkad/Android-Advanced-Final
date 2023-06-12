package com.example.okhttp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.okhttp.R
import com.example.okhttp.databinding.AdItemBinding
import com.example.okhttp.models.Ad

class AdViewHolder(val adItemBinding: AdItemBinding):
    RecyclerView.ViewHolder(adItemBinding.root) {

    companion object {
        fun create (parent: ViewGroup): AdViewHolder{
            val binding = AdItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            return AdViewHolder(binding)
        }
    }

    fun bind(ad: Ad) {
        adItemBinding.title.text = ad.title
        adItemBinding.description.text = ad.description
            Glide
                .with(adItemBinding.imageView.context)
                .load(ad.image)
                .apply(RequestOptions.bitmapTransform(RoundedCorners(10)))
                .placeholder(R.drawable.progress_animation)
                .error(R.drawable.baseline_image_24)
                .into(adItemBinding.imageView)
    }
}