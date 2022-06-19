package cl.accenture.githubjavapop

import android.app.Application
import cl.accenture.githubjavapop.module.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class GithubJavaPop : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@GithubJavaPop)
            modules(appModule)
        }
    }
}