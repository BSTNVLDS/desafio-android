package cl.accenture.githubjavapop.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import cl.accenture.githubjavapop.R
import cl.accenture.githubjavapop.model.Pull
import cl.accenture.githubjavapop.model.Repo

class PullAdapter :RecyclerView.Adapter<PullHolder>(){
    var list  = mutableListOf<Pull>()
    var close = mutableListOf<Pull>()
    var open =  mutableListOf<Pull>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        PullHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pull,parent,false))

    override fun onBindViewHolder(holder: PullHolder, position: Int)
    = holder.bind(list[position])

    override fun getItemCount() = list.size

    fun addList(nueva:List<Pull> ) {
        for(pull in nueva){
            if(pull.state == "open") open += pull
            else close+=pull
        }
        list = (open+list).toMutableList() //???' ver que onda xd
        list +=close

        notifyDataSetChanged()
    }
    }