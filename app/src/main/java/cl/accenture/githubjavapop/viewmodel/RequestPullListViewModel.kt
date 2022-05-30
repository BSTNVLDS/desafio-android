package cl.accenture.githubjavapop.viewmodel

import Toastr
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cl.accenture.githubjavapop.model.Pull
import getRetrofit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pageget
import pageinc

class RequestPullListViewModel : ViewModel() {
    private var _state = true
    private val state get() = this._state
    val pullList = MutableLiveData<List<Pull>>()

    fun loadList(user: String,repo :String,context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            while (state){
                val call = getRetrofit()
                    .getPullByRepo(user,repo,100,"all",pageget())
                CoroutineScope(Dispatchers.Main).launch {
                    if (call.isSuccessful) {
                        val pullreq = call.body() ?: emptyList()
                        if(pullreq.isNotEmpty()){
                        for (repo in pullreq){
                            CoroutineScope(Dispatchers.IO).launch {
                                val call2 = getRetrofit().getNameByUser(repo.user.login)
                                CoroutineScope(Dispatchers.Main).launch{
                                    val username = call2.body()?.name.toString()
                                    repo.user.name = username
                                }
                            }
                        }
                            pullList.postValue(pullreq.map { Pull(it.title,it.body,it.state,it.user) })
                            pageinc()

                        }

                    } else {
                        _state=false
                        Toastr(context,"error code:"+call.code().toString())
                    }
                }

            }

        }
    }
}