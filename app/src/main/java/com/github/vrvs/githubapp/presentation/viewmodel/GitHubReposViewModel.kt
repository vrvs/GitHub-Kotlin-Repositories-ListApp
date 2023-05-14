package com.github.vrvs.githubapp.presentation.viewmodel

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.viewModelScope
import com.github.vrvs.githubapp.domain.entity.GitHubRepositoryEntity
import com.github.vrvs.githubapp.domain.entity.Result
import com.github.vrvs.githubapp.domain.usecase.GetRepositoriesUseCase
import com.github.vrvs.githubapp.presentation.contract.GitHubReposContract.Intent
import com.github.vrvs.githubapp.presentation.contract.GitHubReposContract.SideEffect
import com.github.vrvs.githubapp.presentation.contract.GitHubReposContract.State
import com.github.vrvs.githubapp.presentation.viewmodel.mapper.GitHubRepositoryMapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicLong

class GitHubReposViewModel(
    dispatcher: CoroutineDispatcher = Dispatchers.IO
): BaseViewModel<State, Intent, SideEffect>(State.Loading) {

    @VisibleForTesting
    internal val page: AtomicLong = AtomicLong(1)

    private var privateCreated = false
    val created: Boolean
        get() = privateCreated

    init {
        viewModelScope.launch(dispatcher) {
            interpreter.collect { intent ->
                when (intent) {
                    is Intent.LoadMore,
                    is Intent.Reload -> {
                        interpretLoads(intent)
                    }
                    is Intent.ClickItem -> {
                        interpretSideEffect(intent)
                    }
                    is Intent.SaveState -> {
                        interpretSaveState(intent)
                    }
                    is Intent.Create -> {
                        privateCreated = true
                    }
                }
            }
        }
    }

    override fun onCleared() {
        privateCreated = false
        super.onCleared()
    }

    private fun interpretSaveState(intent: Intent.SaveState) {
        if (internalState.value is State.UpdateList || intent.force)
            internalState.value = State.UpdateList(intent.list, false)
    }

    private suspend fun interpretLoads(intent: Intent) {
        when (intent) {
            is Intent.LoadMore,
            is Intent.Reload -> {
                val useCase = GetRepositoriesUseCase()
                if (intent is Intent.Reload) {
                    page.set(1)
                }
                useCase.execute(page.get()).collect { result ->
                    reduce(result, intent)
                }
            }
            else -> {}
        }
    }

    private fun reduce(result: Result<List<GitHubRepositoryEntity>>, intent: Intent) {
        when (result) {
            is Result.Loading -> {
                internalState.value = State.Loading
            }
            is Result.Error -> {
                internalState.value = State.Error(result.error)
            }
            is Result.Success -> {
                page.incrementAndGet()
                internalState.value = State.UpdateList(
                    result.data.map {
                        GitHubRepositoryMapper.toRepositoryComponentModel(it)
                    },
                    append = intent is Intent.LoadMore
                )
            }
        }
    }

    private suspend fun interpretSideEffect(intent: Intent.ClickItem) {
        internalSideEffect.send(
            SideEffect.ShowToast(
                TOAST_TEXT + intent.id
            )
        )
    }

    companion object {
        const val TOAST_TEXT = "You clicked at repository "
    }
}