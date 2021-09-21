package com.github.vrvs.githubapp

import com.github.vrvs.githubapp.data.datasource.remote.model.GitHubRepositoryListModel
import com.github.vrvs.githubapp.data.datasource.remote.model.GitHubRepositoryModel
import com.github.vrvs.githubapp.domain.entity.GitHubRepositoryEntity

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
            repoName = "okhttp",
            starsNumber = 40819,
            forksNumber = 8631,
            authorName = "square",
            authorImageUrl = "https://avatars.githubusercontent.com/u/82592?v=4",
        ),
        GitHubRepositoryEntity(
            repoName = "architecture-samples",
            starsNumber = 39467,
            forksNumber = 10827,
            authorName = "android",
            authorImageUrl = "https://avatars.githubusercontent.com/u/32689599?v=4",
        ),
    )
}