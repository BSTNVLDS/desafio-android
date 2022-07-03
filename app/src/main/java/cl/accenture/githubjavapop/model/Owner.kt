package cl.accenture.githubjavapop.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Owner(
    val avatar_url: String = "",
    val login: String = "",
    var name: String = ""
):Parcelable