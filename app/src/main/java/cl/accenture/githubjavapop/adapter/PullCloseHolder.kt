package cl.accenture.githubjavapop.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import cl.accenture.githubjavapop.databinding.ItemPullCloseBinding
import cl.accenture.githubjavapop.model.Pull
import com.squareup.picasso.Picasso

class PullCloseHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val binding = ItemPullCloseBinding.bind(itemView)

    fun bind(pull: Pull) {
        binding.namePull.text = pull.title
        binding.bodyPull.text = pull.body
        binding.nameUserpull.text = pull.user.name
        binding.loginUserpull.text = pull.user.login
        binding.state.text = pull.state
        Picasso.get().load(pull.user.avatar_url).into(binding.imagePull)
    }
}
