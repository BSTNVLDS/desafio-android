package cl.accenture.githubjavapop.util

import android.widget.ViewFlipper
import cl.accenture.githubjavapop.R
import cl.accenture.githubjavapop.model.GitHubByPageError
import retrofit2.HttpException
import java.net.UnknownHostException

fun Throwable.toGitHubByPageError(): GitHubByPageError {
    return when {
        this is HttpException && code() == 422 -> GitHubByPageError.UnprocessableEntity
        this is HttpException && code() == 403 -> GitHubByPageError.TooManyRequest
        this is UnknownHostException -> GitHubByPageError.NoConnection
        else -> GitHubByPageError.Unknown
    }
}

fun repoListErrorHandler(error: GitHubByPageError): String {
    return when (error) {
        is GitHubByPageError.UnprocessableEntity ->
            PARAMETERS_NO_COMPLETED
        is GitHubByPageError.TooManyRequest ->
            EXCEEDED_LIMIT
        is GitHubByPageError.Unknown ->
            GENERIC_ERROR
        is GitHubByPageError.NoConnection ->
            NO_INTERNET_MESSAGE
    }

}
