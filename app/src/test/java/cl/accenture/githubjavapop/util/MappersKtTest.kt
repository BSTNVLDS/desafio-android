package cl.accenture.githubjavapop.util

import cl.accenture.githubjavapop.connection.GithubAPIService
import cl.accenture.githubjavapop.model.GitHubByPageError
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
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
    private lateinit var body: ResponseBody
    @RelaxedMockK
    private lateinit var mediaType: MediaType

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        genericError = Throwable("other Error")
        exceptionU = HttpException(`set Code to Response Error`(422))
        exceptionT = HttpException(`set Code to Response Error`(403))
    }


    @Test
    fun `error generico devuelve error desconcocido`() {
        assertEquals(genericError.toGitHubByPageError(), GitHubByPageError.Unknown)
    }

    @Test
    fun `excepecion http 422 devuelve error unprocesable`() {
        assertEquals(exceptionU.toGitHubByPageError(), GitHubByPageError.UnprocessableEntity)
    }

    @Test
    fun `excepecion http 403 devuelve error de muchas solicitudes XD`() {
        assertEquals(exceptionT.toGitHubByPageError(), GitHubByPageError.TooManyRequest)
    }

    private fun `set Code to Response Error`(int: Int): Response<GithubAPIService> {
        body = ResponseBody.create(mediaType, "test error")
        return Response.error(int, body)
    }
}