package cl.accenture.githubjavapop.util

import cl.accenture.githubjavapop.model.GitHubByPageError
import retrofit2.HttpException
import java.net.UnknownHostException

fun Throwable.toGitHubByPageError():GitHubByPageError{
    return when{
      this is HttpException && code() == 422 -> GitHubByPageError.UnprocessableEntity
        this is HttpException && code() == 403 ->GitHubByPageError.TooManyRequest
        this is UnknownHostException ->GitHubByPageError.NoConnection
       else -> GitHubByPageError.Unknown

    }

}