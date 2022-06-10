package cl.accenture.githubjavapop.model

sealed class GitHubByPageError {
    object TooManyRequest : GitHubByPageError()
    object UnprocessableEntity : GitHubByPageError()
    object Unknown : GitHubByPageError()
    object NoConnection : GitHubByPageError()
}