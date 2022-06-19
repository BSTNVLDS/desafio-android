package cl.accenture.githubjavapop.util

import cl.accenture.githubjavapop.connection.GithubAPIService
import cl.accenture.githubjavapop.model.GitHubByPageError
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import retrofit2.HttpException
import retrofit2.Response


internal class MappersKtTest {

    private lateinit var genericError: Throwable
    private lateinit var exceptionU: HttpException
    private lateinit var exceptionT: HttpException

    private fun `set code to response error`(int: Int): Response<GithubAPIService> {
        val body = ResponseBody.create(
            MediaType.parse("application/json; charset=utf-8"), "test error"
        )
        return Response.error(int, body)
    }

    @Before
    fun setUp() {
        genericError = Throwable("other Error")
        exceptionU = HttpException(`set code to response error`(422))
        exceptionT = HttpException(`set code to response error`(403))
    }


    @Test
    fun `generic error returns unknown error`() =
        assertEquals(genericError.toGitHubByPageError(), GitHubByPageError.Unknown)


    @Test
    fun `http exception 422 returns unprocessed error`() =
        assertEquals(exceptionU.toGitHubByPageError(), GitHubByPageError.UnprocessableEntity)


    @Test
    fun `http 403 exception returns error from many requests`() =
        assertEquals(exceptionT.toGitHubByPageError(), GitHubByPageError.TooManyRequest)

}