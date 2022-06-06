package cl.accenture.githubjavapop.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cl.accenture.githubjavapop.connection.GithubAPIService
import cl.accenture.githubjavapop.model.ApiState
import cl.accenture.githubjavapop.model.Repo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException

class HomeViewModel(private val githubAPIService: GithubAPIService):ViewModel() {
     val stateRepoList = MutableLiveData<ApiState<List<Repo>>>()

    fun loadListByPage(page:Int){
        CoroutineScope(Dispatchers.IO).launch {
            stateRepoList.postValue(ApiState.Loading())
            runCatching {
                githubAPIService.getGithubByPage("language:Java","stars",page)
            }.onSuccess {response->
                if(response.isSuccessful){
                    val tempList =response.body()?.repo ?: emptyList()
                    stateRepoList.postValue(ApiState.Success(tempList))
                }else{
                    stateRepoList.postValue(ApiState.Error(HttpException(response)))
                }
            }.onFailure { throwable->
                stateRepoList.postValue(ApiState.Error(throwable))
            }

        }
    }
}