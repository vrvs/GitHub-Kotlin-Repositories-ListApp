package com.github.vrvs.githubapp.data.datasource.remote

import com.github.vrvs.githubapp.data.datasource.GitHubReposDataSource
import com.github.vrvs.githubapp.data.datasource.remote.model.GitHubRepositoryModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import retrofit2.await

class GitHubReposRemoteDataSource: GitHubReposDataSource, KoinComponent {

    private val gitHubApi: GitHubApi by inject()

    override suspend fun getRepositories(
        page: Long,
    ): List<GitHubRepositoryModel> {
        return gitHubApi.getRepositoriesAsync(
            query = QUERY,
            sort = STARS,
            perPage = PER_PAGE,
            page = page,
        ).await().items
    }

    companion object {
        private const val QUERY = "language:kotlin"
        private const val STARS = "stars"
        private const val PER_PAGE = 100L
    }
}