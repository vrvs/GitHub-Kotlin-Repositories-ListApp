package com.github.vrvs.githubapp.data.repository

import com.github.vrvs.githubapp.data.datasource.GitHubReposDataSource
import com.github.vrvs.githubapp.data.datasource.remote.model.GitHubRepositoryModel.Companion.toGitHubRepository
import com.github.vrvs.githubapp.domain.entity.GitHubRepositoryEntity
import com.github.vrvs.githubapp.domain.entity.Result
import com.github.vrvs.githubapp.domain.repository.GitHubReposRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GitHubReposRepositoryImpl(
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
): GitHubReposRepository, KoinComponent {

    private val gitHubReposRemoteDataSource: GitHubReposDataSource by inject()

    override fun getRepositories(
        page: Long,
    ): Flow<Result<List<GitHubRepositoryEntity>>> = flow {
        emit(Result.Loading)
        try {
            val repositories = gitHubReposRemoteDataSource.getRepositories(page)
            repositories.map {
                GitHubRepositoryEntity.toGitHubRepository(it)
            }.also {
                emit(Result.Success(it))
            }
        } catch (ex: Throwable) {
            emit(Result.Error(ex))
        }
    }.flowOn(ioDispatcher)
}