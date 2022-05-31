package cl.accenture.githubjavapop.viewmodel

import Toastr
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cl.accenture.githubjavapop.model.Repo
import getRetrofit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pageget
import pageinc

class HomeViewModel:ViewModel() {


     val repoList = MutableLiveData<List<Repo>>()

    fun loadListByPage(page:Int){
        CoroutineScope(Dispatchers.IO).launch {
            val call = getRetrofit()
                .getGithubByPage("language:Java","stars",page)
            CoroutineScope(Dispatchers.Main).launch{
                if(call.isSuccessful){
                    val tempList =call.body()?.repo ?: emptyList()
                    if(tempList.isNotEmpty()){
                        for (repo in tempList){
                            CoroutineScope(Dispatchers.IO).launch {
                                val call2 = getRetrofit().getNameByUser(repo.owner.login)
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
                    //todo
                }
            }

        }
    }
}