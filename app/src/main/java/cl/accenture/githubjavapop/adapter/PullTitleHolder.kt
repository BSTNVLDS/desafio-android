package cl.accenture.githubjavapop.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import cl.accenture.githubjavapop.databinding.ItemPullTitleBinding

class PullTitleHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val binding = ItemPullTitleBinding.bind(itemView)

    fun bind(title: String) {
        binding.title.text = title
    }
}
