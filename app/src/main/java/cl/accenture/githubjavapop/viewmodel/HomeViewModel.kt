package cl.accenture.githubjavapop.viewmodel

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cl.accenture.githubjavapop.adapter.RepoAdapter
import cl.accenture.githubjavapop.connection.GithubAPIService
import cl.accenture.githubjavapop.controller.Conexion
import cl.accenture.githubjavapop.view.RequestPullList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel:ViewModel() {
    private val adapter= RepoAdapter()
    private var _page =1
    private val page get() = _page
     fun load(rc: RecyclerView, context : Context) {

        rc.layoutManager = LinearLayoutManager(context)
        rc.adapter = adapter
         pagination(page,context)
        adapter.setOnclickListener {
            val positionTap = rc.getChildAdapterPosition(it)
            val selectTap = adapter.list[positionTap]
            val intentPRL = Intent(context, RequestPullList::class.java)
            intentPRL.putExtra("repo",selectTap.name)
            intentPRL.putExtra("user",selectTap.owner?.login)
            context.startActivity(intentPRL)

        }
        rc.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)){
                    _page++
                    pagination(page,context)
                }}

        })
    }

   private fun pagination(query:Int,context:Context){
        Log.e("","mesafeee")
        CoroutineScope(Dispatchers.IO).launch {
            val call = Conexion.getRetrofit("https://api.github.com/search/")
                .create(GithubAPIService::class.java).getGithubByPage("repositories?q=language:Java&sort=stars&page=$query")
            CoroutineScope(Dispatchers.Main).launch{
                if(call.isSuccessful){
                    val tempList =call.body()?.repo ?: emptyList()
                    for (repo in tempList){
                        CoroutineScope(Dispatchers.IO).launch {
                            val call2 = Conexion.getRetrofit("https://api.github.com/users/")
                                .create(GithubAPIService::class.java).getNameByUser(repo.owner?.login.toString())
                            CoroutineScope(Dispatchers.Main).launch{
                                val fullname = call2.body()?.name.toString()
                                repo.owner?.name = fullname
                                adapter.addList(repo)
                            }

                        }
                    }

                }else{
                    val ercode = call.code().toString()
                    Toast.makeText(context,"error code: $ercode", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }
}