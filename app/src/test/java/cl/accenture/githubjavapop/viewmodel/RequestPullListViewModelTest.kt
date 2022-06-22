package cl.accenture.githubjavapop.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import cl.accenture.githubjavapop.connection.GithubAPIService
import cl.accenture.githubjavapop.connection.PullResponse
import cl.accenture.githubjavapop.model.ApiState
import cl.accenture.githubjavapop.model.Owner
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response

internal class RequestPullListViewModelTest {
    @get:Rule
    val rule: InstantTaskExecutorRule = InstantTaskExecutorRule()
    private val githubAPIService = mockk<GithubAPIService>()
    private val viewModel by lazy { RequestPullListViewModel(githubAPIService) }

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
    fun `when a correct response is received the status changes to success`() {   //use while
        every { `github by page method`() } returns `mock response`()             //and no mock state :(
        viewModel.loadList("user", "repo")
        verify(exactly = 1) { `github by page method`() }
        Assert.assertTrue(
            "mock response equals to success state",
            viewModel.statePullList.value is ApiState.Success
        )
    }

    @Test
    fun `when an exception is received it changes the state to error`() {
        every { `github by page method`() } returns `mock error response`()
        viewModel.loadList("user", "repo")
        verify(exactly = 1) { `github by page method`() }
        Assert.assertTrue(
            "mock error response equals to error state",
            viewModel.statePullList.value is ApiState.Error
        )
    }

    @Test
    fun `empty response returns success and empty list`() {
        every { `github by page method`() } returns `mock empty response`()
        viewModel.loadList("user", "repo")
        verify(exactly = 1) { `github by page method`() }
        val success = viewModel.statePullList.value as ApiState.Success
        val list = success.value
        Assert.assertTrue(
            "mock empty response equals to empty list",
            list.isEmpty()
        )
    }

    private fun `github by page method`() = runBlocking {
        githubAPIService.getPullByRepo(
            user = "user",
            repo = "repo",
            perPage = 100,
            state = "all",
            page = 1
        )
    }

    private fun `mock empty response`(): Response<List<PullResponse>> {
        return Response.success(null)
    }

    private fun `mock error response`(): Response<List<PullResponse>> {
        val body = ResponseBody.create(
            MediaType.parse("application/json; charset=utf-8"), "error github by page"
        )
        return Response.error(422, body)
    }

    private fun `mock response`(): Response<List<PullResponse>> {
        var listPullResponse = emptyList<PullResponse>()
        val pullResponse = PullResponse(
            title = "Fix 11111",
            body = "info info info",
            state = "closed",
            user = Owner(
                avatar_url = "http://www.google.com",
                login = "login",
                name = "name"
            )
        )
        listPullResponse+=pullResponse
        return Response.success(listPullResponse)
    }
}