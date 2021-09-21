package com.github.vrvs.githubapp.app

import android.app.Application
import com.github.vrvs.githubapp.di.DependencyInjectionModule.DataModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class GitHubAppApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@GitHubAppApplication)
            modules(DataModule.modules)
        }
    }
}