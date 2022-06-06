package cl.accenture.githubjavapop.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cl.accenture.githubjavapop.connection.GithubAPIService
import cl.accenture.githubjavapop.model.ApiState
import cl.accenture.githubjavapop.model.Repo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException

class HomeViewModel(val githubAPIService: GithubAPIService):ViewModel() {
     val repoList = MutableLiveData<ApiState<List<Repo>>>()

    fun loadListByPage(page:Int){
        CoroutineScope(Dispatchers.IO).launch {
            repoList.postValue(ApiState.Loading())
            runCatching {
                githubAPIService.getGithubByPage("language:Java","stars",page)
            }.onSuccess {response->
                if(response.isSuccessful){
                    val tempList =response.body()?.repo ?: emptyList()
                    repoList.postValue(ApiState.Success(tempList))
                }else{
                    repoList.postValue(ApiState.Error(HttpException(response)))
                }
            }.onFailure { throwable->
                repoList.postValue(ApiState.Error(throwable))
            }

        }
    }
}