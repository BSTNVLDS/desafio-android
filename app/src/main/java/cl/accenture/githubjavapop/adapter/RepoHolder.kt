package cl.accenture.githubjavapop.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import cl.accenture.githubjavapop.databinding.ItemRepoBinding
import cl.accenture.githubjavapop.model.Repo
import com.squareup.picasso.Picasso

class RepoHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val binding = ItemRepoBinding.bind(itemView)
    fun bind(repo: Repo){
        binding.name.text = repo.name
        binding.descrip.text= repo.description
        binding.stars.text= repo.watchers
        binding.forks.text= repo.forks
        binding.nameUser.text = repo.owner!!.name
        binding.loginUser.text= repo.owner!!.login
        Picasso.get().load(repo.owner!!.avatar_url).into(binding.imageg)


    }
}