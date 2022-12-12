package ru.netology.nmedia.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.activity.NewPostFragment.Companion.textArg
import ru.netology.nmedia.adapter.OnInteractionListener
import ru.netology.nmedia.adapter.PostViewHolder
import ru.netology.nmedia.databinding.FragmentPostBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.viewmodel.PostViewModel

class PostFragment : Fragment() {

    private var _binding: FragmentPostBinding? = null
    private val mBinding get() = _binding!!
    val viewModel: PostViewModel by viewModels<PostViewModel>(ownerProducer = ::requireParentFragment)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPostBinding.inflate(layoutInflater, container, false)
        arguments?.textArg?.let {
            val id = it.toLong()
            val viewHolder = PostViewHolder(mBinding.post, object : OnInteractionListener {
                override fun onEdit(post: Post) {
                    val text:String = post.content
                    findNavController().navigate(R.id.action_postFragment_to_newPostFragment,
                        Bundle().apply
                        { textArg = text })
                    viewModel.edit(post)
                }

                override fun onLike(post: Post) {
                    viewModel.likeById(post.id)
                }

                override fun onPlay(post: Post) {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(post.video))
                    startActivity(intent)
                }

                override fun onRemove(post: Post) {
                    viewModel.removeById(post.id)
                }

                override fun onShare(post: Post) {
                    viewModel.shareById(post.id)
                    val intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, post.content)
                        type = "text/plain"
                    }
                    val shareIntent =
                        Intent.createChooser(intent, getString(R.string.chooser_share_post))
                    startActivity(shareIntent)
                }
            })

            viewModel.data.observe(viewLifecycleOwner) { posts ->
                val post = posts.find { it.id == id } ?: run {
                    findNavController().navigateUp()
                    return@observe
                }
                viewHolder.bind(post)
            }
        }
        return mBinding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}