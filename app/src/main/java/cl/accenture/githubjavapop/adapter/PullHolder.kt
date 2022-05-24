package cl.accenture.githubjavapop.adapter

import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import cl.accenture.githubjavapop.R
import cl.accenture.githubjavapop.databinding.ItemPullBinding
import cl.accenture.githubjavapop.model.Pull
import com.squareup.picasso.Picasso

class PullHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemPullBinding.bind(itemView)
        fun bind(pull: Pull){
            binding.namePull.text = pull.title
            binding.bodyPull.text= pull.body
            binding.namePull.text= pull.user!!.login
            binding.loginUserpull.text= pull.user!!.login
            binding.state.text = pull.state
            Picasso.get().load(pull.user!!.avatar_url).into(binding.imagePull)
        }
    }
