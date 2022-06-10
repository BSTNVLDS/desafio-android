package cl.accenture.githubjavapop

import android.content.Context
import android.net.ConnectivityManager
import cl.accenture.githubjavapop.connection.GithubAPIService
import cl.accenture.githubjavapop.viewmodel.HomeViewModel
import cl.accenture.githubjavapop.viewmodel.RequestPullListViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.math.sin

val appModule = module {
    single {
        Retrofit.Builder().baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(GithubAPIService::class.java)
    }
    single {
        androidContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    }

    viewModel {
        HomeViewModel(get(), get())
    }
    viewModel {
        RequestPullListViewModel(get(), get())
    }
}

