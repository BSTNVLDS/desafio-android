package cl.accenture.githubjavapop.model

interface PullRequestItem

class Pull(
    val title: String = "",
    val body: String = "",
    val state: String = "",
    val user: Owner = Owner()
):PullRequestItem

class Title(
    val title: String = "",
):PullRequestItem