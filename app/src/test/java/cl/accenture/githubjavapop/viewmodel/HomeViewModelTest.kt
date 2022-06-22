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
    fun `receive a mock response from the server`() {
        val mockResponse = `mock response`()
        every { `github by page method`() } returns mockResponse
        `github by page method`()
        verify(exactly = 1) { `github by page method`() }
        Assert.assertEquals(`github by page method`().body(), mockResponse.body())
    }

    @Test
    fun `when a correct response is received the status changes to success`() {
        every { `github by page method`() } returns `mock response`()
        viewModel.loadListByPage(1)
        verify(exactly = 1) { `github by page method`() }
        Assert.assertTrue(
            "mock response equals to success state",
            viewModel.stateRepoList.value is ApiState.Success
        )
    }

    @Test
    fun `when an exception is received it changes the state to error`() {
        every { `github by page method`() } returns `mock error response`()
        viewModel.loadListByPage(1)
        verify(exactly = 1) { `github by page method`() }
        Assert.assertTrue(
            "mock error response equals to error state",
            viewModel.stateRepoList.value is ApiState.Error
        )
    }

    @Test
    fun `empty response returns success and empty list`() {
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
        githubAPIService.getGithubByPage(
            query = "language:Java",
            sort = "stars",
            page = 1
        )
    }

    private fun `mock empty response`(): Response<GithubResponse> {
        return Response.success(null)
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


