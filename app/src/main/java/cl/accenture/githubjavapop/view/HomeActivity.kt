package cl.accenture.githubjavapop.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import kotlinx.coroutines.runBlocking

class HomeActivity : AppCompatActivity() {
    private lateinit var binding :ActivityHomeBinding
    private var adapter= RepoAdapter()
    private var page = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        pagination(page)
       load()

    }

    private fun load() {
        binding.rc.layoutManager = LinearLayoutManager(this)
        binding.rc.adapter = adapter
        adapter.setOnclickListener {
            val select = adapter.list[binding.rc.getChildAdapterPosition(it)]
            startActivity( Intent(this@HomeActivity, RequestPullList::class.java)
                .putExtra("repo",select.name).putExtra("user",select.owner!!.login))
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
                        .create(APIService::class.java).getGithubByPage("repositories?q=language:Java&sort=stars&per_page&page=$query")
                    runOnUiThread{
                        if(call.isSuccessful){
                            var tempList =call.body()?.repo ?: emptyList()
                            CoroutineScope(Dispatchers.IO).launch {
                                for (repo in tempList){
                                    val call2 = Conexion.getRetrofit("https://api.github.com/users/")
                                        .create(APIService::class.java).getNameByUser(repo.owner?.login.toString())
                                repo.owner?.name =call2.body()?.name
                                   runOnUiThread{
                                       adapter.addList(repo)
                                   }

                                }

                            }

                        }else{
                            val error_code = call.code()
                            Toast.makeText(applicationContext,"error code: $error_code", Toast.LENGTH_SHORT)
                                .show()                        }
                    }

                }
            }


        }

