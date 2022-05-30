package com.example.test_main_music_app.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.test_main_music_app.Model.AllCategory
import com.example.test_main_music_app.R

class MusicPlayerRecyrclerViewAdapter(
    private val fragment: Fragment,
    private val allCategory: ArrayList<AllCategory>,
    private val onItemClick: ((AllCategory) -> Unit)
) : RecyclerView.Adapter<MusicPlayerRecyrclerViewAdapter.MusicPlayerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int ): MusicPlayerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_music, parent, false)
        return MusicPlayerViewHolder(view)
    }

    override fun onBindViewHolder(holder: MusicPlayerViewHolder, position: Int) {
        val category = allCategory[position]
        holder.namemusic.text = category.namemusic
        holder.authormusic.text = category.author
        Glide.with(holder.itemView.context)
            .load(category.imageUrl)
            .into(holder.imageView)
        holder.itemView.setOnClickListener{
            onItemClick(allCategory[position])
        }
    }

    override fun getItemCount(): Int {
        return allCategory.size
    }
    class MusicPlayerViewHolder(itemView: View) :
            RecyclerView.ViewHolder(itemView){
        val imageView: ImageView = itemView.findViewById(R.id.image_music_row)
        val namemusic: TextView = itemView.findViewById(R.id.title_music_row)
        val authormusic: TextView = itemView.findViewById(R.id.author_music_row)
            }
}