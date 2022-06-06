package cl.accenture.githubjavapop.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cl.accenture.githubjavapop.connection.GithubAPIService
import cl.accenture.githubjavapop.model.ApiState
import cl.accenture.githubjavapop.model.Pull
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException

class RequestPullListViewModel(private val githubAPIService : GithubAPIService) : ViewModel() {
    private var state = true
    private var page =1
    val statePullList = MutableLiveData<ApiState<List<Pull>>>()

    fun loadList(user: String,repo :String) {
        CoroutineScope(Dispatchers.IO).launch {
            while (state){
                statePullList.postValue(ApiState.Loading())
                runCatching {
                    githubAPIService.getPullByRepo(user, repo, 100, "all", page)
                }.onSuccess { response->
                    if (response.isSuccessful){
                        val tempList =response.body() ?: emptyList()
                       val parseList = tempList.map { Pull(it.title,it.body,it.state,it.user) }
                        statePullList.postValue(ApiState.Success(parseList))
                        page++
                    }else{
                        statePullList.postValue(ApiState.Error(HttpException(response)))
                    }
                }.onFailure {  throwable->
                    statePullList.postValue(ApiState.Error(throwable))
                }
            }

        }
    }
    fun loadnamebylogin(tempList: List<Pull>){
         for (pull in tempList){
             CoroutineScope(Dispatchers.IO).launch {
                 val call2 = githubAPIService.getNameByUser(pull.user.login)
                 if (call2.isSuccessful){
                     CoroutineScope(Dispatchers.Main).launch{
                         val username = call2.body()?.name.toString()
                         pull.user.name = username
                     }
                 }
             }
         }
    }
}