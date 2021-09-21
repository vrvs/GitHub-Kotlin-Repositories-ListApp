package com.github.vrvs.githubapp.domain.entity

data class GitHubRepositoryEntity(
    val repoName: String,
    val starsNumber: Long,
    val forksNumber: Long,
    val authorName: String,
    val authorImageUrl: String?,
) {
    companion object
}
