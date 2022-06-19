package cl.accenture.githubjavapop.module

import cl.accenture.githubjavapop.connection.GithubAPIService
import cl.accenture.githubjavapop.viewmodel.HomeViewModel
import cl.accenture.githubjavapop.viewmodel.RequestPullListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {
    single {
        Retrofit.Builder().baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(GithubAPIService::class.java)
    }
    viewModel {
        HomeViewModel(get())
    }
    viewModel {
        RequestPullListViewModel(get())
    }
}

