package cl.accenture.githubjavapop.viewmodel


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

class RequestPullListViewModel(private val githubAPIService : GithubAPIService) : ViewModel() {
  private var state = true
    private var page =1
    val statePullList = MutableLiveData<ApiState<List<Pull>, GitHubByPageError>>()

    fun loadList(user: String,repo :String) {

        CoroutineScope(Dispatchers.IO).launch {
                statePullList.postValue(ApiState.Loading())
            while (state) {
                runCatching {
                    githubAPIService.getPullByRepo(user, repo, 100, "all", page)
                }.onSuccess { response ->
                    if (response.isSuccessful) {
                        val tempList = response.body() ?: emptyList()
                        val parseList = tempList.map { Pull(it.title, it.body, it.state, it.user) }
                        val definitiveList = loadNameByLogin(parseList)
                        if(definitiveList.isNotEmpty()){
                            statePullList.postValue(ApiState.Success(definitiveList))
                            page++
                        }else{
                            state=false
                        }

                    } else {
                        val error = HttpException(response).toGitHubByPageError()
                        statePullList.postValue(ApiState.Error(error))
                        state=false
                    }
                }.onFailure { throwable ->
                      val error = throwable.toGitHubByPageError()
                    statePullList.postValue(ApiState.Error(error))
                    state=false
                }

            }
        }

    }
    private fun loadNameByLogin(tempList: List<Pull>):List<Pull> {
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