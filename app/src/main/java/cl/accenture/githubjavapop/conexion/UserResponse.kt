package cl.accenture.githubjavapop.conexion

import com.google.gson.annotations.SerializedName


data class UserResponse(
    @SerializedName("name") val name:String

)