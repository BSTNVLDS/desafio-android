package cl.accenture.githubjavapop.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cl.accenture.githubjavapop.R
import cl.accenture.githubjavapop.adapter.RepoAdapter
import cl.accenture.githubjavapop.conexion.APIService
import cl.accenture.githubjavapop.controller.Conexion
import cl.accenture.githubjavapop.databinding.ActivityHomeBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {
    private var _binding: ActivityHomeBinding? = null
    private val binding get() = _binding!!
    private val adapter= RepoAdapter()
    private var _page =1
    private val page get() = _page
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        pagination(page)
       load()
    }

    private fun load() {
        binding.rc.layoutManager = LinearLayoutManager(this)
        binding.rc.adapter = adapter
        adapter.setOnclickListener {
            val positionTap = binding.rc.getChildAdapterPosition(it)
            val selectTap = adapter.list[positionTap]
            val intentPRL = Intent(this@HomeActivity, RequestPullList::class.java)
           intentPRL.putExtra("repo",selectTap.name)
            intentPRL.putExtra("user",selectTap.owner!!.login)
            startActivity(intentPRL)

        }
        binding.rc.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)){
                    _page++
                    pagination(page)
            }}

        })
    }
            private fun pagination(query:Int){
                CoroutineScope(Dispatchers.IO).launch {
                    val call = Conexion.getRetrofit("https://api.github.com/search/")
                        .create(APIService::class.java).getGithubByPage("repositories?q=language:Java&sort=stars&page=$query")
                    CoroutineScope(Dispatchers.Main).launch{
                        if(call.isSuccessful){
                            val tempList =call.body()?.repo ?: emptyList()
                                for (repo in tempList){
                                    CoroutineScope(Dispatchers.IO).launch {
                                        val call2 = Conexion.getRetrofit("https://api.github.com/users/")
                                            .create(APIService::class.java).getNameByUser(repo.owner?.login.toString())
                                        CoroutineScope(Dispatchers.Main).launch{
                                            val fullname = call2.body()?.name.toString()
                                            repo.owner?.name = fullname
                                            adapter.addList(repo)
                                        }

                                    }
                            }

                        }else{
                            val ercode = call.code().toString()
                            Toast.makeText(applicationContext,"error code: $ercode", Toast.LENGTH_SHORT).show()
                        }
                    }

                }
            }


        }

