package cl.accenture.githubjavapop.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import cl.accenture.githubjavapop.R
import cl.accenture.githubjavapop.databinding.ItemPullBinding
import cl.accenture.githubjavapop.modelo.Pull
import com.squareup.picasso.Picasso

class PullAdapter() :RecyclerView.Adapter<PullAdapter.PullHolder>(){
    var lista :List<Pull> = emptyList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        PullHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pull,parent,false))

    override fun onBindViewHolder(holder: PullHolder, position: Int)
    = holder.bind(lista[position])

    override fun getItemCount() = lista.size

    inner class PullHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemPullBinding.bind(itemView)
        fun bind(pull: Pull){
            binding.nombrePull.text = pull.title
           binding.bodyPull.text= pull.body
            binding.nombreUsuariopull.text= pull.user!!.login
           binding.sobrenombreUsuariopull.text= pull.user!!.login
        Picasso.get().load(pull.user!!.avatar_url).into(binding.imagePull)
        }
    }
    fun addList(nueva:List<Pull> ) {
        this.lista += nueva
        notifyDataSetChanged()
    }
    }