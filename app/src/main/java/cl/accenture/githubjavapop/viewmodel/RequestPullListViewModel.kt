package cl.accenture.githubjavapop.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import cl.accenture.githubjavapop.adapter.PullAdapter
import cl.accenture.githubjavapop.connection.GithubAPIService
import cl.accenture.githubjavapop.controller.Conexion
import cl.accenture.githubjavapop.databinding.ActivityRequestpulllistBinding
import cl.accenture.githubjavapop.model.Pull
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RequestPullListViewModel : ViewModel() {
    private val adapter = PullAdapter()
    private var _state = true
    private val state get() = _state!!


     fun initRecycleView(fullname: String,context: Context,binding:ActivityRequestpulllistBinding) {
        binding.rcr.layoutManager = LinearLayoutManager(context)
        binding.rcr.adapter = adapter
        loadLista(fullname,context,binding)
    }
   private fun loadLista(fullname: String,context: Context,binding:ActivityRequestpulllistBinding) {
        CoroutineScope(Dispatchers.IO).launch {
            var page =1
            while (state){
                val call = Conexion.getRetrofit("https://api.github.com/repos/")
                    .create(GithubAPIService::class.java).getPullByRepo("$fullname/pulls?per_page=100&state=all&page=$page")
                CoroutineScope(Dispatchers.Main).launch {
                    if (call.isSuccessful) {
                        val pullreq = call.body() ?: emptyList()
                        for (repo in pullreq){
                            CoroutineScope(Dispatchers.IO).launch {
                                val call2 = Conexion.getRetrofit("https://api.github.com/users/")
                                    .create(GithubAPIService::class.java).getNameByUser(repo.user?.login.toString())
                                CoroutineScope(Dispatchers.Main).launch{
                                    val fullname = call2.body()?.name.toString()
                                    repo.user?.name = fullname
                                }

                            }
                        }
                        adapter.addList(pullreq.map { Pull(it.title,it.body,it.state,it.user) })
                        val open_count =adapter.open.size
                        val close_count =adapter.close.size
                        binding.opens.text= "$open_count Opens"
                        binding.closed.text= "$close_count Closed"
                        page++
                    } else {
                        _state=false
                        val error_code = call.code()
                        Toast.makeText(context,"error code: $error_code", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

            }

        }
    }
}