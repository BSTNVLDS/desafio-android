package cl.accenture.githubjavapop

import cl.accenture.githubjavapop.viewmodel.HomeViewModel
import cl.accenture.githubjavapop.viewmodel.RequestPullListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        HomeViewModel()
    }
    viewModel {
        RequestPullListViewModel()
    }
}