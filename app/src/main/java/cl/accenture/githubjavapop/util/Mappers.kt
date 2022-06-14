package cl.accenture.githubjavapop

import cl.accenture.githubjavapop.model.GitHubByPageError
import retrofit2.HttpException

fun Throwable.toGitHubByPageError():GitHubByPageError{
    return when{
      this is HttpException && code() == 422 -> GitHubByPageError.UnprocessableEntity
        this is HttpException && code() == 403 ->GitHubByPageError.TooManyRequest
       else -> GitHubByPageError.Unknown


    }

}