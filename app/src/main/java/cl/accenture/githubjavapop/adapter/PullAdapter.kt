package cl.accenture.githubjavapop.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import cl.accenture.githubjavapop.R
import cl.accenture.githubjavapop.model.Pull

class PullAdapter : RecyclerView.Adapter<PullHolder>() {

    private var list = mutableListOf<Pull>()
    private var close = mutableListOf<Pull>()
    private var open = mutableListOf<Pull>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        PullHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_pull, parent, false)
        )

    override fun onBindViewHolder(holder: PullHolder, position: Int) = holder.bind(list[position])

    override fun getItemCount() = list.size

    fun addList(new: List<Pull>) {
        for (pull in new) {
            if (pull.state == "open") open += pull
            else close += pull
        }
        list = (open + list).toMutableList()
        list += close
        notifyDataSetChanged()
    }

    fun returnOpenPullCount(): String = "${open.size} Opens"

    fun returnClosePullCount(): String = "${close.size} Opens"

}