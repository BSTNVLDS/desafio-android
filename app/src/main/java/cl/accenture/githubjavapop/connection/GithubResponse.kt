package cl.accenture.githubjavapop.connection

import cl.accenture.githubjavapop.model.Repo
import com.google.gson.annotations.SerializedName

data class GithubResponse(
    @SerializedName("items") val repo: List<Repo>
)