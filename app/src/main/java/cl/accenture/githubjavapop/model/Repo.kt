package cl.accenture.githubjavapop.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Repo(
    val name: String = "",
    val description: String = "",
    val forks: String = "",
    val watchers: String = "",
    val owner: Owner = Owner()
):Parcelable


