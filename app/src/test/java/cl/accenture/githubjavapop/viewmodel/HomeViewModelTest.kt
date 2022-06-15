package cl.accenture.githubjavapop.viewmodel

import android.content.Context
import android.net.ConnectivityManager
import cl.accenture.githubjavapop.connection.GithubAPIService
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class HomeViewModelTest{

    @RelaxedMockK
    private lateinit var viewModel:HomeViewModel
    private lateinit var githubAPIService: GithubAPIService
private lateinit var connectivityManager: ConnectivityManager
    private lateinit var context: Context

    @Before
    fun setUp(){
        MockKAnnotations.init(this)
        connectivityManager=   context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
         viewModel = HomeViewModel(githubAPIService, connectivityManager )

    }
    @Test
    fun isConnectionActive(){
        /*
        //given
        val bol = true
        every {   viewModel.stateRepoList} returns bol
        //when
        viewModel.stateRepoList
        //then
        verify(exactly = 1) { viewModel.stateRepoList }

         */
    }
}



