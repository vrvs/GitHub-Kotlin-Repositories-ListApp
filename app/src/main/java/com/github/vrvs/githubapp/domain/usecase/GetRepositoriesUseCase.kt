package com.github.vrvs.githubapp.domain.usecase

import com.github.vrvs.githubapp.domain.entity.GitHubRepositoryEntity
import com.github.vrvs.githubapp.domain.entity.Result
import com.github.vrvs.githubapp.domain.repository.GitHubReposRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetRepositoriesUseCase:  KoinComponent {

    private val gitHubReposRepository: GitHubReposRepository by inject()

    fun execute(page: Long): Flow<Result<List<GitHubRepositoryEntity>>> =
        gitHubReposRepository.getRepositories(page)
}