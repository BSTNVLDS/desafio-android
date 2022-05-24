package cl.accenture.githubjavapop.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import cl.accenture.githubjavapop.R
import cl.accenture.githubjavapop.databinding.ItemRepoBinding
import cl.accenture.githubjavapop.model.Repo
import com.squareup.picasso.Picasso

class RepoAdapter() :
    RecyclerView.Adapter<RepoAdapter.RepoHolder>(), View.OnClickListener {
    private var listener: View.OnClickListener? = null
    var lista :List<Repo> = emptyList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_repo,parent,false)
        view.setOnClickListener(this)
        return RepoHolder(view)
    }

    override fun onBindViewHolder(holder: RepoHolder, position: Int)
    = holder.bind(lista[position])

    fun setOnclickListener(listener: View.OnClickListener?) {
        this.listener = listener
    }

    override fun onClick(view: View) {
        if (listener != null) listener!!.onClick(view)
    }

    override fun getItemCount() = lista.size

    inner class RepoHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemRepoBinding.bind(itemView)
         fun bind(repo: Repo){
            binding.nombre.text = repo.name
            binding.descripcion.text= repo.description
            binding.estrellas.text= repo.watchers
            binding.forks.text= repo.forks
             binding.nombreUsuario.text = repo.owner!!.login.toString()
            binding.sobrenombreUsuario.text= repo.owner!!.login
            Picasso.get().load(repo.owner!!.avatar_url).into(binding.imageg)


        }
    }

    fun addList(nueva:List<Repo> ) {
        this.lista += nueva
        notifyDataSetChanged()
    }

    fun remplazar(pos: Int, nombre:String) {
      lista[pos].owner!!.login =nombre
   }
}