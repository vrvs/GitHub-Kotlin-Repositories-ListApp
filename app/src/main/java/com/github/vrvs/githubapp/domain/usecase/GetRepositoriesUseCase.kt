package com.github.vrvs.githubapp.domain.usecase

import com.github.vrvs.githubapp.domain.entity.GitHubRepositoryEntity
import com.github.vrvs.githubapp.domain.entity.Result
import com.github.vrvs.githubapp.domain.repository.GitHubReposRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetRepositoriesUseCase: BaseUseCase<Long, List<GitHubRepositoryEntity>>(), KoinComponent {

    private val gitHubReposRepository: GitHubReposRepository by inject()

    override suspend fun execute(): Flow<Result<List<GitHubRepositoryEntity>>> = flow {
        super.parameter.let { page ->
            if (page == null) {
                emit(Result.Error(Exception(PARAMETER_NOT_RECEIVED_MESSAGE_EXCEPTION)))
            } else {
                val flow = gitHubReposRepository.getRepositories(page)
                flow.collect {
                    emit(it)
                }
            }
        }
    }

    companion object {
        const val PARAMETER_NOT_RECEIVED_MESSAGE_EXCEPTION = "Parameter Not Received"
    }
}