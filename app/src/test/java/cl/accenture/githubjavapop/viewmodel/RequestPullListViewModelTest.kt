package cl.accenture.githubjavapop.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import cl.accenture.githubjavapop.connection.GithubAPIService
import cl.accenture.githubjavapop.connection.PullResponse
import cl.accenture.githubjavapop.model.Owner
import cl.accenture.githubjavapop.model.Pull
import io.mockk.InternalPlatformDsl.toArray
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
    fun `basic test mock response of github by page(1 execute)`() {
        val mockResponse = `mock response`()
        every { `github by page method`() } returns mockResponse
        `github by page method`()
        verify(exactly = 1) { `github by page method`() }
        Assert.assertEquals(`github by page method`().body(), mockResponse.body())
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

    private fun `mock empty response`(): Response<PullResponse> {
        return Response.success(null)
    }

    private fun `mock error response`(): Response<PullResponse> {
        val body = ResponseBody.create(
            MediaType.parse("application/json; charset=utf-8"), "error github by page"
        )
        return Response.error(422, body)
    }

    private fun `mock response`(): List<Response<PullResponse>> {
       val response= Response.success(
            PullResponse(
                title = "Fix 11111", body = "fix n1111", state = "cerrado",
                user = Owner(
                    avatar_url = "http://www.google.com", login = "yo", name = "bastian"
                )
            )
        )
        var list= emptyList<Response<PullResponse>>()
        list+=response
       return list
    }
}