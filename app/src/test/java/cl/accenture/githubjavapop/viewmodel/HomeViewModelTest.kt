package cl.accenture.githubjavapop.viewmodel

import android.net.ConnectivityManager
import androidx.lifecycle.MutableLiveData
import cl.accenture.githubjavapop.connection.GithubAPIService
import cl.accenture.githubjavapop.model.ApiState
import cl.accenture.githubjavapop.model.GitHubByPageError
import cl.accenture.githubjavapop.model.Repo
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)

class HomeViewModelTest {

    @RelaxedMockK
    private lateinit var viewModel: HomeViewModel
    @RelaxedMockK
    private lateinit var githubAPIService: GithubAPIService
    @RelaxedMockK
    private lateinit var connectivityManager: ConnectivityManager
    @RelaxedMockK
    private lateinit var stateRepoList :MutableLiveData<ApiState<List<Repo>, GitHubByPageError>>

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = HomeViewModel(githubAPIService, connectivityManager)
    }
    @Test
    fun demo() {
        assert(true)
    }

    @Test
    fun isCon() {
        //given
        every {   viewModel.loadListByPage(1)} returns
        //when
        viewModel.loadListByPage(1)
        //then
        verify(exactly = 1) { viewModel.loadListByPage(1) }


    }

}



