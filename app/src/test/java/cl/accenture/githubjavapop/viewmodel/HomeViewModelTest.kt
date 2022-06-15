package cl.accenture.githubjavapop.viewmodel

import android.content.Context
import android.net.ConnectivityManager
import androidx.core.content.ContextCompat.getSystemService
import cl.accenture.githubjavapop.appModule
import cl.accenture.githubjavapop.connection.GithubAPIService
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.getScopeId
import org.koin.core.module.Module
import org.koin.dsl.module
import java.util.*
import kotlin.coroutines.coroutineContext

class HomeViewModelTest{

    @RelaxedMockK
    private lateinit var viewModel:HomeViewModel
    private lateinit var githubAPIService: GithubAPIService
private lateinit var connectivityManager: ConnectivityManager
    private lateinit var context: Context
    @Before
    fun onBefore(){
        MockKAnnotations.init(this)
        connectivityManager=   context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
         viewModel = HomeViewModel(githubAPIService, connectivityManager )

    }
    @Test
    fun isConnectionActive(){
        //given
        val bol = true
        every {   viewModel.stateRepoList} returns bol
        //when
        viewModel.stateRepoList
        //then
        verify(exactly = 1) { viewModel.stateRepoList }
    }
}



