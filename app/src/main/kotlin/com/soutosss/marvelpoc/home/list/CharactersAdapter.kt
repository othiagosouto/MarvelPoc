package com.soutosss.marvelpoc.home.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.soutosss.marvelpoc.R
import com.soutosss.marvelpoc.data.model.view.CharacterHome
import com.soutosss.marvelpoc.home.list.CharactersAdapter.CharacterHomeViewHolder

class CharactersAdapter : ListAdapter<CharacterHome, CharacterHomeViewHolder>(CharacterHomeDiff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterHomeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.characters_item, parent, false)

        return CharacterHomeViewHolder(view)
    }

    override fun onBindViewHolder(holder: CharacterHomeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class CharacterHomeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val headerText: TextView = itemView.findViewById(R.id.text)
        private val imageView: ImageView = itemView.findViewById(R.id.image)

        fun bind(characterHome: CharacterHome) {
            headerText.text = characterHome.name

            Glide.with(imageView.context).load(characterHome.thumbnailUrl).error(R.drawable.ic_launcher_background).dontAnimate().into(imageView)
        }
    }

    class CharacterHomeDiff : DiffUtil.ItemCallback<CharacterHome>() {
        override fun areItemsTheSame(oldItem: CharacterHome, newItem: CharacterHome) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: CharacterHome, newItem: CharacterHome) =
            oldItem == newItem
    }

}