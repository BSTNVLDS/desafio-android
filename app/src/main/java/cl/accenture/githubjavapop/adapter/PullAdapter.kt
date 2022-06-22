package cl.accenture.githubjavapop.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import cl.accenture.githubjavapop.R
import cl.accenture.githubjavapop.model.Pull

class PullAdapter : RecyclerView.Adapter<PullHolder>() {

    private var list = mutableListOf<Pull>()
    private var close = 0
    private var open = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        PullHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_pull, parent, false)
        )

    override fun onBindViewHolder(holder: PullHolder, position: Int) = holder.bind(list[position])

    override fun getItemCount() = list.size

    fun addList(new: List<Pull>) {
        list += new
        for (pull in new) {
            when (pull.state) {
                "open" -> open++
                else -> close++
            }
        }

        notifyDataSetChanged()
    }

    fun returnOpenPullCount(): String = "$open Opens"

    fun returnClosePullCount(): String = "$close Opens"

}