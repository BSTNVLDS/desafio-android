


import cl.accenture.githubjavapop.connection.GithubAPIService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


fun getRetrofit():  GithubAPIService{
    return Retrofit.Builder().baseUrl("https://api.github.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build().create(GithubAPIService::class.java)
}


