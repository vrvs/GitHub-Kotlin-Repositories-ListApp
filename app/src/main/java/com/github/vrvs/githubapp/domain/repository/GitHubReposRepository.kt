package com.github.vrvs.githubapp.domain.repository

import com.github.vrvs.githubapp.domain.entity.GitHubRepositoryEntity
import com.github.vrvs.githubapp.domain.entity.Result
import kotlinx.coroutines.flow.Flow

interface GitHubReposRepository {
    fun getRepositories(page: Long): Flow<Result<List<GitHubRepositoryEntity>>>
}