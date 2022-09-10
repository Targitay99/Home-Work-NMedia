package ru.netology.nmedia

import android.icu.util.UniversalTimeScale.toBigDecimal
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.databinding.ActivityMainBinding
import android.os.Bundle
import android.util.Log
import ru.netology.nmedia.dto.Post
import java.math.RoundingMode

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val post = Post(
            id = 1,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
            published = "21 мая в 18:36",
            likedByMe = false
        )

        with(binding) {
            author.text = post.author
            published.text = post.published
            content.text = post.content
            repost.text = printQuantity(post.repost)
            views.text = printQuantity(post.views)

            if (post.likedByMe) {
                favorite.setImageResource(R.drawable.ic_baseline_favorite_24)
                post.likes++
                like.text = printQuantity(post.likes)
            } else {
                like.text = printQuantity(post.likes)
            }

            root.setOnClickListener {
                Log.d("stuff", "stuff")
            }

            avatar.setOnClickListener {
                Log.d("stuff", "avatar")
            }

            favorite.setOnClickListener {
                Log.d("stuff", "like")
                post.likedByMe = !post.likedByMe
                favorite.setImageResource(
                    if (post.likedByMe) R.drawable.ic_baseline_favorite_24 else R.drawable.ic_baseline_favorite_border_24
                )
                if (post.likedByMe) {
                    post.likes++
                    Log.d("stuff", "like +1")
                } else {
                    post.likes--
                    Log.d("stuff", "like -1")
                }
                like.text = printQuantity(post.likes)
            }

            share.setOnClickListener {
                Log.d("stuff", "share")
                post.repost = post.repost + 100
                repost.text = printQuantity(post.repost)
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