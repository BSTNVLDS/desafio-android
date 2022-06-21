package cl.accenture.githubjavapop.util

import cl.accenture.githubjavapop.connection.GithubAPIService
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
    private val exceptionU = HttpException(`set code to response error`(422))
    private val exceptionT = HttpException(`set code to response error`(403))
    private val exceptionN = UnknownHostException()

    @Test
    fun `generic error returns unknown error`() =
        Assert.assertEquals(genericError.toGitHubByPageError(), GitHubByPageError.Unknown)

    @Test
    fun `http exception 422 returns unprocessed error`() =
        Assert.assertEquals(exceptionU.toGitHubByPageError(), GitHubByPageError.UnprocessableEntity)

    @Test
    fun `http 403 exception returns error from many requests`() =
        Assert.assertEquals(exceptionT.toGitHubByPageError(), GitHubByPageError.TooManyRequest)

    @Test
    fun `no connection returns error`() =
        Assert.assertEquals(exceptionN.toGitHubByPageError(), GitHubByPageError.NoConnection)

    private fun `set code to response error`(int: Int): Response<GithubAPIService> {
        val body = ResponseBody.create(
            MediaType.parse("application/json; charset=utf-8"),
            "test error"
        )
        return Response.error(int, body)
    }
}