package cl.accenture.githubjavapop.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import cl.accenture.githubjavapop.R
import cl.accenture.githubjavapop.databinding.ItemPullBinding
import cl.accenture.githubjavapop.model.Pull
import com.squareup.picasso.Picasso

class PullAdapter() :RecyclerView.Adapter<PullAdapter.PullHolder>(){
    var list :List<Pull> = emptyList()
    var close :List<Pull> = emptyList()
    var open :List<Pull> = emptyList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        PullHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pull,parent,false))

    override fun onBindViewHolder(holder: PullHolder, position: Int)
    = holder.bind(list[position])

    override fun getItemCount() = list.size

    inner class PullHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemPullBinding.bind(itemView)
        fun bind(pull: Pull){
            binding.namePull.text = pull.title
           binding.bodyPull.text= pull.body
            binding.namePull.text= pull.user!!.login
           binding.loginUserpull.text= pull.user!!.login
        Picasso.get().load(pull.user!!.avatar_url).into(binding.imagePull)
        }
    }
    fun addList(nueva:List<Pull> ) {
        for(pull in nueva){
            if(pull.state == "open"){
                this.open += pull
            }else{
                this.close+=pull
            }
        }
        this.list += this.open
        this.list +=this.close

        notifyDataSetChanged()
    }
    }