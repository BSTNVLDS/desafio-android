package cl.accenture.githubjavapop.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cl.accenture.githubjavapop.connection.GithubAPIService
import cl.accenture.githubjavapop.controller.*
import cl.accenture.githubjavapop.model.Repo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel:ViewModel() {

     val repoList = MutableLiveData<List<Repo>>()

    fun pagination(context:Context){
        CoroutineScope(Dispatchers.IO).launch {
            val call = getRetrofit("https://api.github.com/search/")
                .create(GithubAPIService::class.java).getGithubByPage("repositories?q=language:Java&sort=stars&page=${pageget()}")
            CoroutineScope(Dispatchers.Main).launch{
                if(call.isSuccessful){
                    val tempList =call.body()?.repo ?: emptyList()
                    if(tempList.isNotEmpty()){
                        for (repo in tempList){
                            CoroutineScope(Dispatchers.IO).launch {
                                val call2 = getRetrofit("https://api.github.com/users/")
                                    .create(GithubAPIService::class.java).getNameByUser(repo.owner.login)
                                CoroutineScope(Dispatchers.Main).launch{
                                    val username = call2.body()?.name.toString()
                                    repo.owner.name = username
                                }
                            }
                        }
                        repoList.postValue(tempList)
                       pageinc()
                    }
                }else{
                    Toastr(context,"error code: "+call.code().toString())
                }
            }

        }
    }
}