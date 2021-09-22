package com.github.vrvs.githubapp.di

import android.content.Context
import com.github.vrvs.githubapp.data.datasource.GitHubReposDataSource
import com.github.vrvs.githubapp.data.datasource.remote.GitHubApi
import com.github.vrvs.githubapp.data.datasource.remote.GitHubReposRemoteDataSource
import com.github.vrvs.githubapp.data.datasource.remote.interceptor.CacheInterceptor
import com.github.vrvs.githubapp.data.repository.GitHubReposRepositoryImpl
import com.github.vrvs.githubapp.domain.repository.GitHubReposRepository
import com.github.vrvs.githubapp.presentation.viewmodel.GitHubReposViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object DependencyInjectionModule {

    object DataModule {
        val modules = listOf(apiModule(), dataSourceModule(), repositoryModule())

        private fun apiModule() = module {
            single {
                provideHttpLoggingInterceptor()
            }
            single {
                provideCacheInterceptor(get())
            }
            single {
                provideHttpClient(get(), get())
            }
            single<Retrofit> {
                provideRetrofit(get())
            }
            single<GitHubApi> {
                provideGitHubApi(get())
            }
        }

        object PresentationModule {
            val modules = listOf(viewModel())

            private fun viewModel() = module {
                viewModel {
                    provideViewModel()
                }
            }
        }

        private fun provideViewModel() = GitHubReposViewModel()

        private fun dataSourceModule() = module {
            single<GitHubReposDataSource> {
                GitHubReposRemoteDataSource()
            }
        }

        private fun repositoryModule() = module {
            single<GitHubReposRepository> {
                GitHubReposRepositoryImpl()
            }
        }

        private fun provideHttpLoggingInterceptor() =
            HttpLoggingInterceptor().apply {
                this.level = HttpLoggingInterceptor.Level.BODY
            }

        private fun provideCacheInterceptor(context: Context) =
            CacheInterceptor(context)

        private fun provideHttpClient(
            loggingInterceptor: HttpLoggingInterceptor,
            cacheInterceptor: CacheInterceptor
        ) =
            OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(cacheInterceptor)
                .build()

        private fun provideRetrofit(okHttpClient: OkHttpClient) =
            Retrofit.Builder()
                .baseUrl(GitHubApi.URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        private fun provideGitHubApi(retrofit: Retrofit) =
            retrofit.create(GitHubApi::class.java)

    }
}