package com.soutosss.marvelpoc.widget

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.core.widget.ContentLoadingProgressBar
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.soutosss.marvelpoc.R

fun loadHomeImage(imageView: ImageView, url: String, progress: ContentLoadingProgressBar) {
    progress.visibility = View.VISIBLE
    progress.show()
    imageView.visibility = View.INVISIBLE
    Glide.with(imageView.context).load(url).error(R.drawable.ic_launcher_background).dontAnimate()
        .listener(object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                progress.hide()
                imageView.visibility = View.VISIBLE
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                progress.hide()
                imageView.visibility = View.VISIBLE
                return false
            }
        }).into(imageView)
}