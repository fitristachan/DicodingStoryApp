package com.dicoding.dicodingstoryapp.stories.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.dicodingstoryapp.databinding.ItemStoriesBinding
import com.dicoding.dicodingstoryapp.retrofit.response.ListStoryItem
import com.dicoding.dicodingstoryapp.stories.activity.DetailStoryActivity
import com.dicoding.dicodingstoryapp.stories.activity.MainActivity.Companion.loadImage

class StoriesAdapter : ListAdapter<ListStoryItem, StoriesAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): MyViewHolder {
        val binding =
            ItemStoriesBinding.inflate(LayoutInflater.from((parent.context)), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val stories = getItem(position)
        holder.bind(stories)
        val imageUrl = stories.photoUrl
        val title = stories.name
        val desc = stories.description
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailStoryActivity::class.java)
            intent.putExtra("imageUrl", imageUrl)
            intent.putExtra("title", title)
            intent.putExtra("desc", desc)
            holder.itemView.context.startActivity(intent)
        }
    }

    class MyViewHolder(private val binding: ItemStoriesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(stories: ListStoryItem) {
            binding.ivItemPhoto.loadImage(stories.photoUrl)
            binding.tvItemName.text = stories.name.toString()
            binding.tvItemDesc.text = stories.description.toString()
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(
                oldItem: ListStoryItem, newItem: ListStoryItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ListStoryItem, newItem: ListStoryItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}