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

    @RelaxedMockK
    private lateinit var githubAPIService: GithubAPIService

    @RelaxedMockK
    private lateinit var viewModel: HomeViewModel

    @get:Rule
    var rule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    // private lateinit var lista: MutableLiveData<ApiState<List<Repo>, GitHubByPageError>>
    //  private lateinit var view: HomeFragment

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = HomeViewModel(githubAPIService)
    }

    @Test
    fun `devuelve sin estado`() {
        assertNull(viewModel.stateRepoList.value)
    }

    @Test
    fun `probar que se observa el view model`() {
        //  view.onStart()
        //  assertTrue(viewModel.stateRepoList.hasObservers())
    }

    @Test
    fun `cuando se carga mande loadigin xd`() {
        //   lista.postValue(ApiState.Loading())
        //  val res = lista
        // coEvery { viewModel.stateRepoList} returns res
        viewModel.loadListByPage(1)
        verify { viewModel.loadListByPage(1) }
    }

}


