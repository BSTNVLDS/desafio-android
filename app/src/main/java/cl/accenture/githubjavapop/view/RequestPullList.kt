package cl.accenture.githubjavapop.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import cl.accenture.githubjavapop.adapter.PullAdapter
import cl.accenture.githubjavapop.conexion.APIService
import cl.accenture.githubjavapop.controller.Conexion
import cl.accenture.githubjavapop.databinding.ActivityDetalleRepositorioBinding
import cl.accenture.githubjavapop.model.Pull
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RequestPullList : AppCompatActivity() {
    private lateinit var binding :ActivityDetalleRepositorioBinding
    private var adapter = PullAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetalleRepositorioBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val extra =intent.extras!!
        val repo = extra.getString("repo")
        val usuario = extra.getString("usuario")
        title = repo
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        cargarLista("$usuario/$repo")
        initRecycleView()
    }

    private fun initRecycleView() {
        binding.rcr.layoutManager = LinearLayoutManager(this)
        binding.rcr.adapter = adapter

    }

    private fun cargarLista(fullname: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val call = Conexion.getRetrofit("https://api.github.com/repos/")
                .create(APIService::class.java).getPullByRepo("$fullname/pulls")
            runOnUiThread {
                if (call.isSuccessful) {
                    val pullreq = call.body() ?: emptyList()
                    adapter.addList(pullreq.map { Pull(it.title, it.body, it.user) })
                    adapter.notifyDataSetChanged()
                    val abiertos =adapter.lista.size
                    binding.abiertos.text= "$abiertos Abiertos"
                } else {
                    Toast.makeText(applicationContext,"sin conexion" , Toast.LENGTH_SHORT)
                        .show()
                }
            }

        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
       return when (item.itemId) {
            android.R.id.home ->{
                finish()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }

    }
}