package cl.accenture.githubjavapop.controller

import android.content.Context
import android.widget.Toast
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
private var _page =1
private val page get() = _page

fun getRetrofit(url: String): Retrofit {
    return Retrofit.Builder().baseUrl(url)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
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


