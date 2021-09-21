package com.github.vrvs.githubapp.data.datasource.remote.model

import com.github.vrvs.githubapp.domain.entity.GitHubRepositoryEntity
import com.google.gson.annotations.SerializedName

data class GitHubRepositoryModel(
    @SerializedName("name")
    val name: String,
    @SerializedName("stargazers_count")
    val stargazersCount: Long,
    @SerializedName("forks_count")
    val forksCount: Long,
    @SerializedName("owner")
    val owner: Owner,
) {
    data class Owner(
        @SerializedName("login")
        val login: String,
        @SerializedName("avatar_url")
        val avatarUrl: String? = null,
    )

    companion object {
        fun GitHubRepositoryEntity.Companion.toGitHubRepository(model: GitHubRepositoryModel) = GitHubRepositoryEntity(
            repoName = model.name,
            starsNumber = model.stargazersCount,
            forksNumber = model.forksCount,
            authorName = model.owner.login,
            authorImageUrl = model.owner.avatarUrl,
        )
    }
}
