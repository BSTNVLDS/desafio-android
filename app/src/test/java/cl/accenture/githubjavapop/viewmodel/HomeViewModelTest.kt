package cl.accenture.githubjavapop.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import cl.accenture.githubjavapop.connection.GithubAPIService
import cl.accenture.githubjavapop.connection.GithubResponse
import cl.accenture.githubjavapop.model.ApiState
import cl.accenture.githubjavapop.model.Owner
import cl.accenture.githubjavapop.model.Repo
import io.mockk.*
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response

class HomeViewModelTest {
    @get:Rule
    val rule: InstantTaskExecutorRule = InstantTaskExecutorRule()
    private val githubAPIService = mockk<GithubAPIService>()
    private val viewModel by lazy { HomeViewModel(githubAPIService) }

    @Before
    fun setUp() = MockKAnnotations.init(this)

    @Test
    fun `basic test mock response of github by page(1 execute)`() {
        val mockResponse = `mock response`()
        every { `github by page method`() } returns mockResponse
        `github by page method`()
        verify(exactly = 1) { `github by page method`() }
        Assert.assertEquals(`github by page method`().body(), mockResponse.body())
    }

    @Test
    fun `basic test mock response of github by page(2 execute)`() {
        every { `github by page method`() } returns `mock response`()
        viewModel.loadListByPage(1)
        verify(exactly = 1) { `github by page method`() }
        Assert.assertTrue(
            "mock response equals to success state",
            viewModel.stateRepoList.value is ApiState.Success
        )
    }

    @Test
    fun `basic test mock response of github by page(3 execute)`() {
        every { `github by page method`() } returns `mock error response`()
        viewModel.loadListByPage(1)
        verify(exactly = 1) { `github by page method`() }
        Assert.assertTrue(
            "mock error response equals to error state",
            viewModel.stateRepoList.value is ApiState.Error
        )
    }

    @Test
    fun `basic test mock response of github by page(5 execute)`() {
        every { `github by page method`() } returns `mock empty response`()
        viewModel.loadListByPage(1)
        verify(exactly = 1) { `github by page method`() }
        val success = viewModel.stateRepoList.value as ApiState.Success
        val list = success.value
        Assert.assertTrue(
            "mock empty response equals to empty list",
            list.isEmpty()
        )
    }


    private fun `github by page method`() = runBlocking {
        githubAPIService.getGithubByPage("language:Java", "stars", 1)
    }

    private fun `mock empty response`(): Response<GithubResponse> {
        return Response.success(GithubResponse(listOf()))
    }

    private fun `mock error response`(): Response<GithubResponse> {
        val body = ResponseBody.create(
            MediaType.parse("application/json; charset=utf-8"), "error github by page"
        )
        return Response.error(422, body)
    }

    private fun `mock response`(): Response<GithubResponse> {
        return Response.success(
            GithubResponse(
                listOf(
                    Repo(
                        name = "repository1",
                        description = "desc info info",
                        forks = "1",
                        watchers = "1",
                        owner = Owner(
                            avatar_url = "http://www.google.com",
                            login = "yo",
                            name = "bastian"
                        )
                    )
                )
            )
        )
    }


}


