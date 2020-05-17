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

fun loadHomeImage(
    imageView: ImageView,
    url: String,
    progress: ContentLoadingProgressBar,
    favorite: View,
    headerText: View,
    headerView: View
) {

    setLoading(true, imageView, progress, favorite, headerText, headerView)
    Glide.with(imageView.context).load(url).error(R.drawable.ic_launcher_background).dontAnimate()
        .listener(object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                setLoading(false, imageView, progress, favorite, headerText, headerView)
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                setLoading(false, imageView, progress, favorite, headerText, headerView)
                return false
            }
        }).into(imageView)
}

private fun setLoading(
    isLoading: Boolean,
    imageView: View,
    progress: ContentLoadingProgressBar,
    favorite: View,
    headerText: View,
    headerView: View
) {
    val visibility = if (isLoading) View.INVISIBLE else View.VISIBLE
    imageView.visibility = visibility
    favorite.visibility = visibility
    headerText.visibility = visibility
    headerView.visibility = visibility
    if (isLoading) {
        progress.visibility = View.VISIBLE
        progress.show()
    } else {
        progress.visibility = View.INVISIBLE
        progress.hide()
    }
}