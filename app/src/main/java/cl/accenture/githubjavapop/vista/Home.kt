package cl.accenture.githubjavapop.vista

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cl.accenture.githubjavapop.adapter.RepoAdapter
import cl.accenture.githubjavapop.conexion.APIService
import cl.accenture.githubjavapop.controller.Conexion
import cl.accenture.githubjavapop.databinding.ActivityHomeBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Home : AppCompatActivity() {
    private lateinit var binding :ActivityHomeBinding
    private var adapter= RepoAdapter()
    private var page = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        pagination(page)
       cargar()

    }

    private fun cargar() {
        binding.rc.layoutManager = LinearLayoutManager(this)
        binding.rc.adapter = adapter
        adapter.setOnclickListener {
            val select = adapter.lista[binding.rc.getChildAdapterPosition(it)]
            startActivity( Intent(this@Home, DetalleRepositorio::class.java)
                .putExtra("repo",select.name).putExtra("usuario",select.owner!!.login))
        }
        binding.rc.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)){
                    page++
                    pagination(page)
            }}

        })
    }
            private fun pagination(query:Int){
                CoroutineScope(Dispatchers.IO).launch {
                    val call = Conexion.getRetrofit("https://api.github.com/search/")
                        .create(APIService::class.java).getGithubByPage("repositories?q=language:Java&sort=stars&page=$query")
                    runOnUiThread{
                        if(call.isSuccessful){
                            adapter.addList(call.body()?.repo ?: emptyList())
                        }else{
                            Toast.makeText(applicationContext,"falla de conexion",Toast.LENGTH_SHORT).show()
                        }
                    }

                }
            }


        }

