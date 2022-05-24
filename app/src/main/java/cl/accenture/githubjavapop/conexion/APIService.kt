package cl.accenture.githubjavapop.conexion

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface APIService {
    @GET
    suspend fun getGithubByPage(@Url url:String): Response<GithubResponse>
    @GET
    suspend fun getPullByRepo(@Url url:String): Response<List<PullResponse>>
    @GET
    suspend fun getNameByUser(@Url url:String): Response<UserResponse>


}