package cl.accenture.githubjavapop.conexion

import cl.accenture.githubjavapop.modelo.Owner
import cl.accenture.githubjavapop.modelo.Pull
import com.google.gson.annotations.SerializedName


data class PullResponse(
    @SerializedName("title") val title:String,
    @SerializedName("body") val body:String,
    @SerializedName("user") val user: Owner

)