package com.github.vrvs.githubapp.data.datasource

import com.github.vrvs.githubapp.data.datasource.remote.model.GitHubRepositoryModel
import kotlin.jvm.Throws

interface GitHubReposDataSource {

    @Throws(Throwable::class)
    suspend fun getRepositories(
        page: Long,
    ): List<GitHubRepositoryModel>
}