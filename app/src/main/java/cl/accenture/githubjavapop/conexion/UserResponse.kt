package cl.accenture.githubjavapop.conexion

import cl.accenture.githubjavapop.modelo.Owner
import cl.accenture.githubjavapop.modelo.Pull
import com.google.gson.annotations.SerializedName


data class UserResponse(
    @SerializedName("name") val name:String

)