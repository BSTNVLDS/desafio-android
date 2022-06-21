package cl.accenture.githubjavapop.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cl.accenture.githubjavapop.connection.GithubAPIService
import cl.accenture.githubjavapop.model.ApiState
import cl.accenture.githubjavapop.model.GitHubByPageError
import cl.accenture.githubjavapop.model.Repo
import cl.accenture.githubjavapop.util.toGitHubByPageError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException

class HomeViewModel(private val githubAPIService: GithubAPIService) : ViewModel() {

    val stateRepoList = MutableLiveData<ApiState<List<Repo>, GitHubByPageError>>()

    fun loadListByPage(page: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            stateRepoList.postValue(ApiState.Loading())
            runCatching {
                githubAPIService.getGithubByPage(
                    query = "language:Java",
                    sort = "stars",
                    page = page
                )
            }.onSuccess { response ->
                if (response.isSuccessful) {
                    val tempList = response.body()?.repo ?: emptyList()
                    val definitiveList = loadNameByLogin(tempList)
                    stateRepoList.postValue(ApiState.Success(definitiveList))
                } else {
                    val error = HttpException(response).toGitHubByPageError()
                    stateRepoList.postValue(ApiState.Error(error))
                }
            }.onFailure { throwable ->
                val error = throwable.toGitHubByPageError()
                stateRepoList.postValue(ApiState.Error(error))
            }

        }

    }

    private fun loadNameByLogin(tempList: List<Repo>): List<Repo> {
        for (repo in tempList) {
            CoroutineScope(Dispatchers.IO).launch {
                val call = githubAPIService.getNameByUser(repo.owner.login)
                if (call.isSuccessful) {
                    val username = call.body()?.name.toString()
                    repo.owner.name = username
                }
            }
        }
        return tempList
    }

}