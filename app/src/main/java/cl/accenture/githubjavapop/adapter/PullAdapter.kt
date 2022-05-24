package cl.accenture.githubjavapop.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import cl.accenture.githubjavapop.R
import cl.accenture.githubjavapop.model.Pull

class PullAdapter() :RecyclerView.Adapter<PullHolder>(){
    var list :List<Pull> = emptyList()
    var close :List<Pull> = emptyList()
    var open :List<Pull> = emptyList()
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
        list += open
        list +=close

        notifyDataSetChanged()
    }
    }