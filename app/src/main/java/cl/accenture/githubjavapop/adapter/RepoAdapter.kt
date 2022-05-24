package cl.accenture.githubjavapop.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import cl.accenture.githubjavapop.R
import cl.accenture.githubjavapop.model.Repo

class RepoAdapter() :
    RecyclerView.Adapter<RepoHolder>(), View.OnClickListener {
    private var listener: View.OnClickListener? = null
    var list :List<Repo> = emptyList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_repo,parent,false)
        view.setOnClickListener(this)
        return RepoHolder(view)
    }

    override fun onBindViewHolder(holder: RepoHolder, position: Int)
    = holder.bind(list[position])

    fun setOnclickListener(listener: View.OnClickListener?) {
        this.listener = listener
    }

    override fun onClick(view: View) {
        if (listener != null) listener!!.onClick(view)
    }

    override fun getItemCount() = list.size

    fun addList(repo :Repo ) {
        this.list += repo
        notifyDataSetChanged()
    }

    fun remplazar(pos: Int, nombre:String) {
      list[pos].owner!!.login =nombre
   }
}