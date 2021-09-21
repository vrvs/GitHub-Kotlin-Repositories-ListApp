package com.github.vrvs.githubapp.domain.usecase

import com.github.vrvs.githubapp.domain.entity.Result
import kotlinx.coroutines.flow.Flow

abstract class BaseUseCase<I, T> {

    internal var parameter: I? = null

    fun setParameter(parameter: I) {
        this.parameter = parameter
    }

    abstract suspend fun execute(): Flow<Result<T>>
}