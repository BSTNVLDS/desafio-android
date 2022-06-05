package cl.accenture.githubjavapop.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cl.accenture.githubjavapop.connection.GithubAPIService
import cl.accenture.githubjavapop.model.Pull
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RequestPullListViewModel(api : GithubAPIService) : ViewModel() {
    private var state = true
    private var page =1
    val pullList = MutableLiveData<List<Pull>>()
    val api = api

    fun loadList(user: String,repo :String) {
        CoroutineScope(Dispatchers.IO).launch {
            while (state){
                val call = api.getPullByRepo(user,repo,100,"all",page)
                CoroutineScope(Dispatchers.Main).launch {
                    if (call.isSuccessful) {
                        val pullreq = call.body() ?: emptyList()
                        if(pullreq.isNotEmpty()){
                            //quitado temporalmente
                            /* for (repo in tempList){
                                 CoroutineScope(Dispatchers.IO).launch {
                                     val call2 = api.getNameByUser(repo.user.login)
                                     if (call2.isSuccessful){
                                         CoroutineScope(Dispatchers.Main).launch{
                                             val username = call2.body()?.name.toString()
                                             repo.user.name = username
                                         }
                                     }
                                 }
                             }*/
                            //me agota la cantidad de solicitudes:(
                            pullList.postValue(pullreq.map { Pull(it.title,it.body,it.state,it.user) })
                            page++
                        }

                    } else {
                        state=false
                        pullList.postValue(null)
                    }
                }

            }

        }
    }
}