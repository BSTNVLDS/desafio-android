package cl.accenture.githubjavapop.util

import cl.accenture.githubjavapop.connection.GithubAPIService
import cl.accenture.githubjavapop.connection.GithubResponse
import cl.accenture.githubjavapop.model.GitHubByPageError
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.Assert
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.net.UnknownHostException

internal class MappersKtTest {

    private val genericError = Throwable("other Error")
    private val exceptionU = `http exception whit code`(422)
    private val exceptionT = `http exception whit code`(403)
    private val exceptionN = UnknownHostException()

    @Test
    fun `generic error returns unknown error`() {
        val errorTest = genericError.toGitHubByPageError()
        Assert.assertTrue(errorTest is GitHubByPageError.Unknown)
        Assert.assertEquals(repoListErrorHandler(errorTest), GENERIC_ERROR)
    }

    @Test
    fun `http exception 422 returns unprocessed error`() {
        val errorTest = exceptionU.toGitHubByPageError()
        Assert.assertTrue(errorTest is GitHubByPageError.UnprocessableEntity)
        Assert.assertEquals(repoListErrorHandler(errorTest), PARAMETERS_NO_COMPLETED)
    }

    @Test
    fun `http 403 exception returns error from many requests`() {
        val errorTest = exceptionT.toGitHubByPageError()
        Assert.assertTrue(errorTest is GitHubByPageError.TooManyRequest)
        Assert.assertEquals(repoListErrorHandler(errorTest), EXCEEDED_LIMIT)
    }

    @Test
    fun `no connection returns error`() {
        val errorTest = exceptionN.toGitHubByPageError()
        Assert.assertTrue(errorTest is GitHubByPageError.NoConnection)
        Assert.assertEquals(repoListErrorHandler(errorTest), NO_INTERNET_MESSAGE)
    }

    private fun `http exception whit code`(int: Int): HttpException {
        val body = ResponseBody.create(
            MediaType.parse("application/json; charset=utf-8"),
            "test error"
        )
        val response: Response<GithubAPIService> = Response.error(int, body)
        return HttpException(response)
    }
}