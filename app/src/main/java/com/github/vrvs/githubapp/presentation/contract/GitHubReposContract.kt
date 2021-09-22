package com.github.vrvs.githubapp.presentation.contract

import com.github.vrvs.githubapp.uicomponent.repository.RepositoryComponent

interface GitHubReposContract {
    sealed class State {
        data class UpdateList(
            val list: List<RepositoryComponent.ComponentModel>,
            val append: Boolean = true
        ): State()
        data class Error(
            val exception: Throwable
        ): State()
        object Loading: State()
    }

    sealed class Intent {
        object LoadMore: Intent()
        object Reload: Intent()
        data class ClickItem(
            val id: String
        ): Intent()
        data class SaveState(
            val list: List<RepositoryComponent.ComponentModel>,
            val force: Boolean = false
        ): Intent()
        object Create: Intent()
    }

    sealed class SideEffect {
        data class ShowToast(
            val message: String,
        ): SideEffect()
    }
}