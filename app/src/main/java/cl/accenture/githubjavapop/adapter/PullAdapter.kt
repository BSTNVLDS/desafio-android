package cl.accenture.githubjavapop.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import cl.accenture.githubjavapop.R
import cl.accenture.githubjavapop.model.Pull
import cl.accenture.githubjavapop.model.PullRequestItem
import cl.accenture.githubjavapop.model.Title
import cl.accenture.githubjavapop.util.IS_CLOSE_PULL
import cl.accenture.githubjavapop.util.IS_OPEN_PULL
import cl.accenture.githubjavapop.util.IS_TITLE_PULL
import java.lang.RuntimeException

class PullAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var list = emptyList<PullRequestItem>()
    private var close = 0
    private var open = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            IS_OPEN_PULL -> PullHolder(viewInflater(parent, R.layout.item_pull))
            IS_CLOSE_PULL -> PullCloseHolder(viewInflater(parent, R.layout.item_pull_close))
            IS_TITLE_PULL -> PullTitleHolder(viewInflater(parent, R.layout.item_pull_title))
            else -> throw RuntimeException("no valid")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is PullHolder -> {
                val selected= list[position] as Pull
                holder.bind(selected )
            }
            is PullCloseHolder -> {
                val selected= list[position] as Pull
                holder.bind(selected)
            }
            is PullTitleHolder -> {
               val selected= list[position] as Title

                holder.bind(selected.title)
            }
        }
    }

    override fun getItemCount() = list.size

    override fun getItemViewType(position: Int): Int {
        val itemSelect = list[position]
        return when {
            itemSelect is Pull && itemSelect.state=="open"  -> IS_OPEN_PULL
            itemSelect is Pull &&  itemSelect.state == "closed" -> IS_CLOSE_PULL
            itemSelect is Title -> IS_TITLE_PULL
            else -> throw RuntimeException("error item view type")
        }

    }

    fun addList(newList: List<PullRequestItem>) {
       list=newList
        val parseListFirst = list.partition {it is Pull && it.state == "open"}
        open = parseListFirst.first.size
        val parseListSecond = parseListFirst.second.partition { it is Pull }
        close = parseListSecond.first.size
        notifyDataSetChanged()
    }

    fun returnOpenPullCount(): String = "$open Opens"

    fun returnClosePullCount(): String = "$close Closed"

    private fun viewInflater(parent: ViewGroup, resource: Int): View {
        return LayoutInflater.from(parent.context).inflate(
            resource,
            parent,
            false
        )

    }
}
