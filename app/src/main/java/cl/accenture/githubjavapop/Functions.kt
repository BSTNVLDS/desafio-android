

import android.content.Context
import android.widget.Toast
import cl.accenture.githubjavapop.connection.GithubAPIService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
private var _page =1
private val page get() = _page

fun getRetrofit():  GithubAPIService{
    return Retrofit.Builder().baseUrl("https://api.github.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build().create(GithubAPIService::class.java)
}

fun Toastr(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

    fun pageinc(){
        _page++
    }
    fun pagerest(){
        _page=1
    }

    fun pageget():Int = page


