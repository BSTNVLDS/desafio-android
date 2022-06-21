package cl.accenture.githubjavapop.connection

import com.google.gson.annotations.SerializedName


data class UserResponse(
    @SerializedName("name") val name:String
)