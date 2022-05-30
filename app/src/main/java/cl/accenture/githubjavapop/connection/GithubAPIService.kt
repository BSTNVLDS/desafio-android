package cl.accenture.githubjavapop.connection

import retrofit2.Response
import retrofit2.http.*

interface GithubAPIService {

    @GET("search/repositories")
    suspend fun getGithubByPage (
        @Query ("q") query:String,
    @Query ("sort") sort:String,
        @Query ("page") page:Int
    ) : Response<GithubResponse>

    @GET("repos/{user}/{repo}/pulls")
    suspend fun getPullByRepo(
        @Path ("user") user:String,
        @Path ("repo") repo:String,
        @Query ("per_page") perpage:Int,
        @Query ("state") state:String,
        @Query ("page") page:Int

    ): Response<List<PullResponse>>

    @GET("users/{user}")
    suspend fun getNameByUser(
        @Path ("user") user:String
    ): Response<UserResponse>


}