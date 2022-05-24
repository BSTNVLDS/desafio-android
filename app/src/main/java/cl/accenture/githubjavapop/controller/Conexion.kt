package cl.accenture.githubjavapop.controller

import cl.accenture.githubjavapop.conexion.APIService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Conexion {
    companion object {

    fun getRetrofit(url :String): Retrofit {
        return Retrofit.Builder().baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    }
}