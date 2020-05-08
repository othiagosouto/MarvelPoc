package com.soutosss.marvelpoc

import android.widget.ImageView
import com.bumptech.glide.Glide

fun loadHomeImage(imageView: ImageView, url: String) {
    Glide.with(imageView.context).load(url)
        .error(R.drawable.ic_launcher_background).dontAnimate().into(imageView)
}