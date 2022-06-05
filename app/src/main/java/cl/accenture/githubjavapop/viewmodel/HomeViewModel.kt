package cl.accenture.githubjavapop.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cl.accenture.githubjavapop.connection.GithubAPIService
import cl.accenture.githubjavapop.model.Repo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(api : GithubAPIService):ViewModel() {
     val repoList = MutableLiveData<List<Repo>>()
    val api = api
    fun loadListByPage(page:Int){
        CoroutineScope(Dispatchers.IO).launch {
            val call = api.getGithubByPage("language:Java","stars",page)
            CoroutineScope(Dispatchers.Main).launch{
                if(call.isSuccessful){
                    val tempList =call.body()?.repo ?: emptyList()
                    if(tempList.isNotEmpty()){
                        //quitado temporalmente
                        /* for (repo in tempList){
                             CoroutineScope(Dispatchers.IO).launch {
                                 val call2 = api.getNameByUser(repo.owner.login)
                                 if (call2.isSuccessful){
                                     CoroutineScope(Dispatchers.Main).launch{
                                         val username = call2.body()?.name.toString()
                                         repo.owner.name = username
                                     }
                                 }
                             }
                         }*/
                        //me agota la cantidad de solicitudes:(
                        repoList.postValue(tempList)
                    }
                }else{
                   repoList.postValue(null)
                }
            }

        }
    }
}