package cl.accenture.githubjavapop.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Owner(
    var avatar_url: String = "",
    var login: String = "",
    var name: String = ""
):Parcelable