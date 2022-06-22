package cl.accenture.githubjavapop.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cl.accenture.githubjavapop.connection.GithubAPIService
import cl.accenture.githubjavapop.model.ApiState
import cl.accenture.githubjavapop.model.GitHubByPageError
import cl.accenture.githubjavapop.model.Pull
import cl.accenture.githubjavapop.util.toGitHubByPageError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException

class RequestPullListViewModel(private val githubAPIService: GithubAPIService) : ViewModel() {

    val statePullList = MutableLiveData<ApiState<List<Pull>, GitHubByPageError>>()

    fun loadListPullByPage(user: String, repo: String, page: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            statePullList.postValue(ApiState.Loading())
            runCatching {
                githubAPIService.getPullByRepo(
                    user = user,
                    repo = repo,
                    state = "all",
                    page = page
                )
            }.onSuccess { response ->
                if (response.isSuccessful) {
                    val tempList = response.body() ?: emptyList()
                    if (tempList.isNotEmpty()) {
                        val parseList = tempList.map { Pull(it.title, it.body, it.state, it.user) }
                        val definitiveList = loadNameByLogin(parseList)
                        statePullList.postValue(ApiState.Success(definitiveList))
                    } else {
                        statePullList.postValue(ApiState.Success(emptyList()))
                    }

                } else {
                    val error = HttpException(response).toGitHubByPageError()
                    statePullList.postValue(ApiState.Error(error))
                }
            }.onFailure { throwable ->
                Log.e("error",throwable.message.toString())
                val error = throwable.toGitHubByPageError()
                statePullList.postValue(ApiState.Error(error))
            }
        }
    }


    private fun loadNameByLogin(tempList: List<Pull>): List<Pull> {
        for (pull in tempList) {
            CoroutineScope(Dispatchers.IO).launch {
                val call = githubAPIService.getNameByUser(pull.user.login)
                if (call.isSuccessful) {
                    val username = call.body()?.name.toString()
                    pull.user.name = username
                }
            }
        }
        return tempList
    }
}