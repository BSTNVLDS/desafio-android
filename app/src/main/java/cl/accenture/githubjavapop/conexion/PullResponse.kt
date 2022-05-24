package cl.accenture.githubjavapop.conexion

import cl.accenture.githubjavapop.model.Owner
import com.google.gson.annotations.SerializedName


data class PullResponse(
    @SerializedName("title") val title:String,
    @SerializedName("body") val body:String,
    @SerializedName("state") val state:String,
    @SerializedName("user") val user: Owner

)