package com.github.vrvs.githubapp.presentation.viewmodel.mapper

import com.github.vrvs.githubapp.domain.entity.GitHubRepositoryEntity
import com.github.vrvs.githubapp.uicomponent.repository.RepositoryComponent

object GitHubRepositoryMapper {
    fun toRepositoryComponentModel(entity: GitHubRepositoryEntity) = RepositoryComponent.ComponentModel(
        id = "${entity.authorName}/${entity.repoName}",
        repoName = entity.repoName,
        authorName = entity.authorName,
        starsNumber = entity.starsNumber,
        forksNumber = entity.forksNumber,
        imageUrl = entity.authorImageUrl,
    )
}