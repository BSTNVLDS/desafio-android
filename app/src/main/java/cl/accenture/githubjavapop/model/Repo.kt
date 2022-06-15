package cl.accenture.githubjavapop.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Repo(
    var name: String = "",
    var description: String = "",
    var forks: String = "",
    var watchers: String = "",
    var owner: Owner = Owner()
):Parcelable


