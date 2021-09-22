package com.github.vrvs.githubapp

import com.github.vrvs.githubapp.data.datasource.remote.model.GitHubRepositoryListModel
import com.github.vrvs.githubapp.data.datasource.remote.model.GitHubRepositoryModel
import com.github.vrvs.githubapp.domain.entity.GitHubRepositoryEntity
import com.github.vrvs.githubapp.uicomponent.repository.RepositoryComponent

object Fixture {
    val REPOSITORIES_RESPONSE = GitHubRepositoryListModel(
        items = listOf(
            GitHubRepositoryModel(
                name = "okhttp",
                stargazersCount = 40819,
                forksCount = 8631,
                owner = GitHubRepositoryModel.Owner(
                    login = "square",
                    avatarUrl = "https://avatars.githubusercontent.com/u/82592?v=4",
                )
            ),
            GitHubRepositoryModel(
                name = "architecture-samples",
                stargazersCount = 39467,
                forksCount = 10827,
                owner = GitHubRepositoryModel.Owner(
                    login = "android",
                    avatarUrl = "https://avatars.githubusercontent.com/u/32689599?v=4",
                )
            ),
        )
    )

    val REPOSITORIES_ENTITY_LIST = listOf(
        GitHubRepositoryEntity(
            repoName = REPOSITORIES_RESPONSE.items[0].name,
            starsNumber = REPOSITORIES_RESPONSE.items[0].stargazersCount,
            forksNumber = REPOSITORIES_RESPONSE.items[0].forksCount,
            authorName = REPOSITORIES_RESPONSE.items[0].owner.login,
            authorImageUrl = REPOSITORIES_RESPONSE.items[0].owner.avatarUrl,
        ),
        GitHubRepositoryEntity(
            repoName = REPOSITORIES_RESPONSE.items[1].name,
            starsNumber = REPOSITORIES_RESPONSE.items[1].stargazersCount,
            forksNumber = REPOSITORIES_RESPONSE.items[1].forksCount,
            authorName = REPOSITORIES_RESPONSE.items[1].owner.login,
            authorImageUrl = REPOSITORIES_RESPONSE.items[1].owner.avatarUrl,
        ),
    )

    val REPOSITORIES_COMPONENT_MODEL_LIST = listOf(
        RepositoryComponent.ComponentModel(
            repoName = REPOSITORIES_ENTITY_LIST[0].repoName,
            starsNumber = REPOSITORIES_ENTITY_LIST[0].starsNumber,
            forksNumber = REPOSITORIES_ENTITY_LIST[0].forksNumber,
            authorName = REPOSITORIES_ENTITY_LIST[0].authorName,
            imageUrl = REPOSITORIES_ENTITY_LIST[0].authorImageUrl,
            id = "${REPOSITORIES_ENTITY_LIST[0].authorName}/${REPOSITORIES_ENTITY_LIST[0].repoName}"
        ),
        RepositoryComponent.ComponentModel(
            repoName = REPOSITORIES_ENTITY_LIST[1].repoName,
            starsNumber = REPOSITORIES_ENTITY_LIST[1].starsNumber,
            forksNumber = REPOSITORIES_ENTITY_LIST[1].forksNumber,
            authorName = REPOSITORIES_ENTITY_LIST[1].authorName,
            imageUrl = REPOSITORIES_ENTITY_LIST[1].authorImageUrl,
            id = "${REPOSITORIES_ENTITY_LIST[1].authorName}/${REPOSITORIES_ENTITY_LIST[1].repoName}"
        ),
    )
}