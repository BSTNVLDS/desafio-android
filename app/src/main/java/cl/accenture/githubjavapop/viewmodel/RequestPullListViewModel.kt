package cl.accenture.githubjavapop.viewmodel

import android.widget.ListAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cl.accenture.githubjavapop.connection.GithubAPIService
import cl.accenture.githubjavapop.model.*
import cl.accenture.githubjavapop.util.toGitHubByPageError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException

class RequestPullListViewModel(private val githubAPIService: GithubAPIService) : ViewModel() {

    val statePullList = MutableLiveData<ApiState<List<PullRequestItem>, GitHubByPageError>>()
    private var listPull= mutableListOf<Pull>()

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
                        val parseList = tempList.map {
                            Pull(
                                it.title,
                                it.body ?: "",
                                it.state,
                                it.user
                            )
                        }.toMutableList()
                       // val definitiveList = loadNameByLogin(parseList)

                        statePullList.postValue(ApiState.Success(pullToPullRequestItem(parseList)))
                    } else {
                        statePullList.postValue(ApiState.Success(emptyList()))
                    }

                } else {
                    val error = HttpException(response).toGitHubByPageError()
                    statePullList.postValue(ApiState.Error(error))
                }
            }.onFailure { throwable ->
                val error = throwable.toGitHubByPageError()
                statePullList.postValue(ApiState.Error(error))
            }
        }
    }
private  fun pullToPullRequestItem(list: List<Pull>):List<PullRequestItem>{

    //open = parseList.first.size
    //close = parseList.second.size
    listPull+=list
    val parseList = listPull.partition {it.state == "open"}
    val openList:MutableList<PullRequestItem> = parseList.first.toMutableList()
    val closeList :MutableList<PullRequestItem> = parseList.second.toMutableList()
    val pullOpens =Title("Open Requests")
    val pullClosed = Title("Close Requests")

    if (openList.isNotEmpty()){
        openList.add(0,pullOpens)
    }
    if (closeList.isNotEmpty()){
        closeList.add(0,pullClosed)
    }

    return openList+closeList

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