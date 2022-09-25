package ru.netology.nmedia.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.repository.PostRepositoryInMemoryImpl

interface OnInteractionListener {
    fun onEdit(post: Post) {}
    fun onLike(post: Post) {}
    fun onRepost(post: Post) {}
    fun onRemove(post: Post) {}
}

class PostsAdapter(
    private val onInteractionListener: OnInteractionListener,
) : ListAdapter<Post, PostViewHolder>(PostDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding, onInteractionListener)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }

}

class PostViewHolder(
    private val binding: CardPostBinding,
    private val onInteractionListener: OnInteractionListener,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(post: Post) {
        binding.apply {
            author.text = post.author
            published.text = post.published
            content.text = post.content
            repost.text = printQuantity(post.repost)
            views.text = printQuantity(post.views)
            like.text = printQuantity(post.likes)
            favorite.setImageResource(
                if (post.likedByMe) R.drawable.ic_baseline_favorite_24 else R.drawable.ic_baseline_favorite_border_24
            )

            favorite.setOnClickListener {
                onInteractionListener.onLike(post)
            }

            share.setOnClickListener {
                onInteractionListener.onRepost(post)
            }

            menu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.options_post)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.remove -> {
                                onInteractionListener.onRemove(post)
                                true
                            }
                            R.id.edit -> {
                                onInteractionListener.onEdit(post)
                                true
                            }

                            else -> false
                        }
                    }
                }.show()
            }
        }
    }


    private fun printQuantity(quantity: Int): String {
        return when (quantity) {
            in 0..999 -> quantity.toString()
            in 1000..9999 -> quantity.toString()[0] + "," + quantity.toString()[1] + "K"
            in 10_000..99_999 -> quantity.toString()[0] + "" + quantity.toString()[1] + "," + quantity.toString()[2] + "K"
            in 100_000..999_999 -> quantity.toString()[0] + "" + quantity.toString()[1] + "" + quantity.toString()[2] + "," + quantity.toString()[3] + "K"
            in 1_000_000..9_999_999 -> quantity.toString()[0] + "," + quantity.toString()[1] + "M"
            in 10_000_000..99_999_999 -> quantity.toString()[0] + "" + quantity.toString()[1] + "," + quantity.toString()[2] + "M" // Не знаю как записать больше миллиона.
            else -> "Ага счас"
        }
    }

}

class PostDiffCallback : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem == newItem
    }
}
