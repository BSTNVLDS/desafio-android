package cl.accenture.githubjavapop.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import cl.accenture.githubjavapop.connection.GithubAPIService
import cl.accenture.githubjavapop.model.ApiState
import cl.accenture.githubjavapop.model.GitHubByPageError
import cl.accenture.githubjavapop.model.Repo
import cl.accenture.githubjavapop.util.toGitHubByPageError
import cl.accenture.githubjavapop.view.fragment.HomeFragment
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.Assert.*
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class HomeViewModelTest {
    @get:Rule
    var rule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    private lateinit var githubAPIService: GithubAPIService
    private lateinit var viewModel: HomeViewModel
    private lateinit var mutablelist: MutableLiveData<ApiState<List<Repo>, GitHubByPageError>>
    private lateinit var apiStateLoading: ApiState<List<Repo>, GitHubByPageError>
    private lateinit var apiStateSuccessEmpty: ApiState<List<Repo>, GitHubByPageError>
    private lateinit var apiStateError: ApiState<List<Repo>, GitHubByPageError>
    @RelaxedMockK
    private lateinit var genericError: Throwable

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = HomeViewModel(githubAPIService)
        apiStateLoading = ApiState.Loading()
        apiStateError = ApiState.Error(genericError.toGitHubByPageError())
        apiStateSuccessEmpty= ApiState.Success(emptyList())
    }

    @Test
    fun `start in loading state`() = runBlocking {
        mutablelist.postValue(apiStateLoading)
        coEvery { viewModel.stateRepoList } returns mutablelist
        viewModel.loadListByPage(1)
        coVerify (exactly = 1){ viewModel.loadListByPage(1) }
    }

}


