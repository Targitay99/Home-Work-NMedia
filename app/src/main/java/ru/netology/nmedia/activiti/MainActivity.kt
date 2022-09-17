package ru.netology.nmedia.activiti

import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.databinding.ActivityMainBinding
import android.os.Bundle
import android.util.Log
import ru.netology.nmedia.R
import ru.netology.nmedia.viewmodel.PostViewModel
import androidx.activity.viewModels

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: PostViewModel by viewModels()
        viewModel.data.observe(this) { post ->
            with(binding) {
                author.text = post.author
                published.text = post.published
                content.text = post.content
                repost.text = printQuantity(post.repost)
                views.text = printQuantity(post.views)
                like.text = printQuantity(post.likes)
                favorite.setImageResource(
                if (post.likedByMe) R.drawable.ic_baseline_favorite_24 else R.drawable.ic_baseline_favorite_border_24
                )

                root.setOnClickListener {
                    Log.d("stuff", "stuff")
                }

                avatar.setOnClickListener {
                    Log.d("stuff", "avatar")
                }

                favorite.setOnClickListener {
                    Log.d("stuff", "like")
                    viewModel.like()
                    like.text = printQuantity(post.likes)
                }

                share.setOnClickListener {
                    Log.d("stuff", "share")
                    viewModel.repost()
                    repost.text = printQuantity(post.repost)
                }

            }
        }
    }

    //Пытался сделать через округление, но почему-то после операции Double.toString после запятой отображается только "0"
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